package com.hongkun.finance.vas.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.sms.model.SmsWebMsg;
import com.hongkun.finance.sms.utils.SmsSendUtil;
import com.hongkun.finance.user.utils.BaseUtil;
import com.hongkun.finance.vas.constants.VasConstants;
import com.hongkun.finance.vas.dao.VasRedpacketInfoDao;
import com.hongkun.finance.vas.model.VasRedpacketInfo;
import com.hongkun.finance.vas.model.dto.RedpacketDTO;
import com.hongkun.finance.vas.model.vo.RedPacketVO;
import com.hongkun.finance.vas.model.vo.VasRedpacketVO;
import com.hongkun.finance.vas.service.VasRedpacketInfoService;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.DateUtils;
import com.yirun.framework.core.utils.pager.Pager;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.hongkun.finance.user.constants.UserConstants.PLATFORM_ACCOUNT_ID;
import static com.hongkun.finance.vas.constants.VasConstants.*;
import static com.yirun.framework.core.commons.Constants.ERROR;
import static com.yirun.framework.core.commons.Constants.SUCCESS;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ZERO;


/**
 * @Project : finance
 * @Program Name  : com.hongkun.finance.loan.service.impl.VasRedpacketInfoServiceImpl.java
 * @Class Name    : VasRedpacketInfoServiceImpl.java
 * @Description : GENERATOR SERVICE实现类
 * @Author : generator
 */
@Service
public class VasRedpacketInfoServiceImpl implements VasRedpacketInfoService {

    private static final Logger logger = LoggerFactory.getLogger(VasRedpacketInfoServiceImpl.class);

    /**
     * VasRedpacketInfoDAO
     */
    @Autowired
    private VasRedpacketInfoDao vasRedpacketInfoDao;

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public int updateVasRedpacketInfo(VasRedpacketInfo vasRedpacketInfo) {
        if (logger.isInfoEnabled()) {
            logger.info("方法名: updateVasRedpacketInfo, 执行更新红包信息操作, 入参: 待更新的红包信息: {}", vasRedpacketInfo);
        }
        try {
            return this.vasRedpacketInfoDao.update(vasRedpacketInfo);
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("更新红包信息异常, 红包信息: {}\n异常信息: ", vasRedpacketInfo, e);
            }
            throw new GeneralException("更新红包信息异常,请重试!");
        }
    }

    @Override
    public VasRedpacketInfo findVasRedpacketInfoById(int id) {
        return this.vasRedpacketInfoDao.findByPK(Long.valueOf(id), VasRedpacketInfo.class);
    }

    @Override
    public List<VasRedpacketInfo> findVasRedpacketInfoList(VasRedpacketInfo vasRedpacketInfo) {
        return this.vasRedpacketInfoDao.findByCondition(vasRedpacketInfo);
    }


    @Override
    public int findVasRedpacketInfoCount(VasRedpacketInfo vasRedpacketInfo) {
        return this.vasRedpacketInfoDao.getTotalCount(vasRedpacketInfo);
    }

    @Override
    public Pager findVasRedpacketInfoList(List<Integer> sendUserIds, List<Integer> acceptorUserIds, VasRedpacketVO vasRedpacketVO, Pager pager) {
        return this.vasRedpacketInfoDao.findByCondition(constructMap(sendUserIds, acceptorUserIds, vasRedpacketVO), pager, VasRedpacketInfo.class, ".searchRedpacketByCondition");
    }

    @Override
    public BigDecimal findVasRedpacketTotalNum(List<Integer> sendUserIds, List<Integer> acceptorUserIds, VasRedpacketVO vasRedpacketVO, Pager pager) {
        return this.vasRedpacketInfoDao.findVasRedpacketTotalNum(constructMap(sendUserIds, acceptorUserIds, vasRedpacketVO));
    }


    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public List<VasRedpacketInfo> produceRedpacket(RedpacketDTO redpacketDTO) {
        if (logger.isInfoEnabled()) {
            logger.info("方法名: produceRedpacket, 执行生成线下红包操作, 入参: 待生成的红包信息: {}", redpacketDTO);
        }
        try {
            List<VasRedpacketInfo> redpacketList = Stream.generate(() -> {
                String redpageKey = generateRedPacketKey();
                VasRedpacketInfo unSaveRedPacketInfo = new VasRedpacketInfo();
                unSaveRedPacketInfo.setKey(redpageKey);
                //设置来源为线下生成
                unSaveRedPacketInfo.setRedpacketSource(REDPACKET_SOURCE_OFFLINE);
                //设置状态为未领取
                unSaveRedPacketInfo.setState(REDPACKET_STATE_UNRECEIVED);
                //设置基本属性
                initPacketInfoBasic(redpacketDTO, unSaveRedPacketInfo);
                //线下生成为平台派发，放在设置基本属性之后，防止属性值被覆盖
                unSaveRedPacketInfo.setSenderUserId(PLATFORM_ACCOUNT_ID);
                unSaveRedPacketInfo.setSenderUserName(SEND_BY_PLATFORM);
                return unSaveRedPacketInfo;

            }).limit(redpacketDTO.getNum()).collect(Collectors.toList());


            //执行插入操作
            this.vasRedpacketInfoDao.insertBatch(VasRedpacketInfo.class, redpacketList);

            if (logger.isInfoEnabled()) {
                logger.info("线下生成红包成功, 红包列表: {}", redpacketList);
            }
//            return redpacketList.stream().map(e -> e.getId()).collect(Collectors.toList());
            return redpacketList;
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("线下生成红包息异常, 红包信息: {}\n异常信息: ", redpacketDTO, e);
            }
            throw new GeneralException("线下生成红包息异常,请重试!");
        }
    }


    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public List<VasRedpacketInfo> distributeRedPacketToUser(RedpacketDTO redpacketDTO, Integer source) {
        boolean isOperator = !REDPACKET_SOURCE_PERSONAL.equals(source);
        if (logger.isInfoEnabled()) {
            logger.info("方法名: distributeRedPacketToUser, 执行派发红包给用户操作, 入参: 待派发的红包信息: {}, 是否是运营人员派发: {}", redpacketDTO, isOperator);
        }
        try {
            List<VasRedpacketInfo> vasRedpacketInfos = redpacketDTO.getUserIds().stream().map((userId) -> {
                VasRedpacketInfo vasRedpacketInfo = new VasRedpacketInfo();
                vasRedpacketInfo.setKey(generateRedPacketKey());
                //设置基本属性
                initPacketInfoBasic(redpacketDTO, vasRedpacketInfo);
                if (isOperator) {
                    //设置来源为运营派发
                    vasRedpacketInfo.setRedpacketSource(source);
                    //设置状态为待审核
                    vasRedpacketInfo.setState(REDPACKET_STATE_ON_CHECK);
                    //设置为平台派发
                    vasRedpacketInfo.setSenderUserId(PLATFORM_ACCOUNT_ID);
                    vasRedpacketInfo.setSenderUserName(SEND_BY_PLATFORM);
                    //设置红包类型
                    vasRedpacketInfo.setType(redpacketDTO.getRedPacketType());
                    //设置派发原因
                    vasRedpacketInfo.setSendReason(redpacketDTO.getSendReason());
                } else {
                    vasRedpacketInfo.setRedpacketSource(REDPACKET_SOURCE_PERSONAL);
                    //设置为未领取，注(发送给平台的红包直接设置为已领取)
                    vasRedpacketInfo.setState(userId == PLATFORM_ACCOUNT_ID ? REDPACKET_STATE_HASRECEIVED:REDPACKET_STATE_UNRECEIVED);
                }
                vasRedpacketInfo.setAcceptorUserId(userId);

                return vasRedpacketInfo;
            }).collect(Collectors.toList());

            /**
             * step 3:保存红包信息
             */
            vasRedpacketInfoDao.insertBatch(VasRedpacketInfo.class, vasRedpacketInfos);
            return vasRedpacketInfos;
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("执行派发红包给用户操作异常, 红包信息: {}, 是否运营人员操作: {}\n异常信息: ", redpacketDTO, isOperator, e);
            }
            throw new GeneralException("执行派发红包给用户操作异常,请重试!");
        }
    }


    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public List<VasRedpacketInfo> changeRedPacketStateForUnCheckPacket(RedpacketDTO redpacketDTO) {
        if (logger.isInfoEnabled()) {
            logger.info("方法名: changeRedPacketStateForUnCheckPacket, 更改红包的状态操作, 入参: 待更改的红包信息: {}", redpacketDTO);
        }
        Integer changeState = redpacketDTO.getState();
        /**
         * step 1:解析要处理的id
         */
        List<Integer> unChangedPacketIds = findChangeRedPacketsIds(redpacketDTO, changeState);

        /**
         * step 2:进行红包状态处理
         */
        try {
            return Optional.ofNullable(unChangedPacketIds)
                           .map(ids -> {
                               /**
                                * step 2.2:执行更新状态(sql描述为只更新待审核的红包, 如果红包不是待审核, 不执行更新)
                                */
                               List<VasRedpacketInfo> actuallyUnCheckRedpacksByIds=vasRedpacketInfoDao.findUnCheckRedpacksByIds(ids);
                               if (BaseUtil.collectionIsEmpty(actuallyUnCheckRedpacksByIds)) {
                                   //没有实际处理的红包信息
                                   return new ArrayList<VasRedpacketInfo>();
                               }

                               Integer updateCount = vasRedpacketInfoDao.udpateRedPacketStateByIds(changeState, redpacketDTO.getReason(), unChangedPacketIds, redpacketDTO.getId()/*实际为userId*/);
                               if (logger.isInfoEnabled()) {
                                   logger.info("改变红包状态, 红包ids: {}, 改变为状态: {}", ids, changeState);
                                   logger.info("改变红包状态统计, 预计个数: {}, 改变个数: {}", ids.size(), updateCount);
                               }
                               /**
                                * step 2.3:如果是审核通过, 那么发送站内信通知用户领红包
                                */
                               ids = actuallyUnCheckRedpacksByIds.stream().map(VasRedpacketInfo::getId).collect(Collectors.toList());
                               if (REDPACKET_STATE_UNRECEIVED.equals(changeState)) {
                                   notifyUserGetPackets(ids,redpacketDTO.getPacketInfo());
                               }
                               //返回所有操作的红包
                               return ids.stream().map(packetId -> vasRedpacketInfoDao.findByPK(Long.valueOf(packetId), VasRedpacketInfo.class)).collect(Collectors.toList());

                           }).orElse(new ArrayList<>());
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("更改红包的状态操作异常, 红包信息: {}\n异常信息: ", redpacketDTO, e);
            }
            throw new GeneralException("更改红包的状态操作异常,请重试!");
        }

    }

    @Override
    public Pager findVasRedpacketInfoList(VasRedpacketInfo vasRedpacketInfo, Pager pager) {
        return this.vasRedpacketInfoDao.findByCondition(vasRedpacketInfo, pager);
    }

    @Override
    public ResponseEntity getUserRedPacketInfo(Integer userId, Pager pager) {
        VasRedpacketInfo vasRedpacketInfo = new VasRedpacketInfo();
        vasRedpacketInfo.setAcceptorUserId(userId);
        vasRedpacketInfo.setState(VasConstants.REDPACKET_STATE_HASRECEIVED);
        vasRedpacketInfo.setSortColumns("modify_time desc");
        return new ResponseEntity(SUCCESS, this.findVasRedpacketInfoList(vasRedpacketInfo, pager));
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public ResponseEntity deleteRedPacket(RedpacketDTO redpacketDTO) {
        if (logger.isInfoEnabled()) {
            logger.info("方法名: deleteRedPacket, 删除红包操作, 入参: 待删除的红包信息: {}", redpacketDTO);
        }
        try {
            //解析要处理的id
            List<Integer> unChangedPacketIds = findChangeRedPacketsIds(redpacketDTO, redpacketDTO.getState());
            //执行更新(sql描述为只已经领取的红包才允许删除)
            return Optional.ofNullable(unChangedPacketIds)
                           .map(ids -> {
                               Integer updateCount = vasRedpacketInfoDao.deleteRedPacket(unChangedPacketIds, redpacketDTO.getReason(), redpacketDTO.getId()/*实际为操作员id*/);
                               if (logger.isInfoEnabled()) {
                                   logger.info("删除红包, 红包ids: {}", ids);
                                   logger.info("删除红包红包状态统计, 预计个数: {}, 改变个数: {}", ids.size(), updateCount);
                               }
                               if (ids.size() != updateCount) {
                                   return new ResponseEntity(ERROR,
                                           String.format("删除红包状态统计, 预计个数: %s,删除个数: %s,红包中存在状态不能被删除的红包", ids.size(), updateCount));
                               }
                               return ResponseEntity.SUCCESS;
                           }).orElse(new ResponseEntity(ERROR, "没有要删除的红包"));
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("删除红包操作异常, 红包信息: {}\n异常信息: ", redpacketDTO, e);
            }
            throw new GeneralException("删除红包操作异常,请重试!");
        }
    }

    @Override
    public ResponseEntity getRedPacketInfoList(Integer regUserId, int state, Pager pager) {
        VasRedpacketInfo condition = new VasRedpacketInfo();
        condition.setAcceptorUserId(regUserId);
        condition.setState(state);
        condition.setSortColumns("modify_time desc");
        return new ResponseEntity(SUCCESS, vasRedpacketInfoDao.getRedPacketInfoList(condition, pager));
    }

    @Override
    public int getUnReceiveRedPacketCount(Integer regUserId) {
        VasRedpacketInfo condition = new VasRedpacketInfo();
        condition.setAcceptorUserId(regUserId);
        condition.setState(VasConstants.REDPACKET_STATE_UNRECEIVED);
        return vasRedpacketInfoDao.getRedPacketInfoListCount(condition);
    }


    /***
     * 检查红包编码是否已经重复
     * @param redpageKey
     * @return
     */
    public Integer checkRedpageKey(String redpageKey) {
        VasRedpacketInfo vasRedpacketInfo = new VasRedpacketInfo();
        vasRedpacketInfo.setKey(redpageKey);
        List<VasRedpacketInfo> byCondition = this.vasRedpacketInfoDao.findByCondition(vasRedpacketInfo);
        if (byCondition != null) {
            return byCondition.size();
        }
        return INTEGER_ZERO;
    }

    @Override
    public VasRedpacketInfo findVasRedpacketInfoByKey(String key) {
        return this.vasRedpacketInfoDao.findVasRedpacketInfoByKey(key);
    }

    @Override
    public List<VasRedpacketInfo> findInvalidRedPackets(Date currentDate) {
        return this.vasRedpacketInfoDao.findInvalidRedPackets(currentDate);
    }

    @Override
    public void updateVasRedpacketInfoBetch(List<VasRedpacketInfo> invalidRedPackets) {
        this.vasRedpacketInfoDao.updateBatch(VasRedpacketInfo.class,invalidRedPackets,invalidRedPackets.size());
    }

    @Override
    public int exchangeRedPacketInfo(VasRedpacketInfo updateRedPacket) {
        return this.vasRedpacketInfoDao.exchangeRedPacketInfo(updateRedPacket);
    }

    /**
     *  @Description    ：找到要处理的红包id
     *  @Method_Name    ：findChangeRedPacketsIds
     *  @param redpacketDTO
     *  @param changeState
     *  @return java.util.List<java.lang.Integer>
     *  @Creation Date  ：2018/4/24
     *  @Author         ：zhongpingtang@hongkun.com.cn
     */
    private List<Integer> findChangeRedPacketsIds(RedpacketDTO redpacketDTO, Integer changeState) {
        if (REDPACKET_STATE_UNRECEIVED.equals(changeState) || REDPACKET_STATE_REJECT.equals(changeState)) {
            //如果是审核操作
            return Arrays.stream(StringUtils.split(redpacketDTO.getCheckId(), ",")).map(Integer::valueOf).collect(Collectors.toList());
        } else if (REDPACKET_STATE_FAILED.equals(changeState)) {
            redpacketDTO.setReason("红包撤销");
            //失效红包操作
            return new ArrayList(redpacketDTO.getRedPacketIds());
        } else if (REDPACKET_STATE_DELETED.equals(changeState)) {
            redpacketDTO.setReason("红包记录删除");
            //删除红包操作
            return new ArrayList(redpacketDTO.getRedPacketIds());
        }
        return null;
    }

    /**
     * 通知用户领取红包
     *  @param ids
     * @param packetInfo
     */
    private void notifyUserGetPackets(List<Integer> ids, String packetInfo) {
        ids.forEach((packetId) -> {
            VasRedpacketInfo packet = this.vasRedpacketInfoDao.findByPK(Long.valueOf(packetId), VasRedpacketInfo.class);
            String msgTemple=packetInfo;
            //如果是鸿坤金服的类型，更换信息模板
            if(BaseUtil.equelsIntWraperPrimit(packet.getType(),RED_PACKET_TYPE_PLUS)){
                msgTemple = RED_PACKAGE_MSG_PLUS;
            }
            String message = String.format(StringUtils.defaultIfBlank(msgTemple,RED_PACKAGE_MSG) , packet.getValue(), packet.getKey(), DateUtils.format(packet.getEndTime()));
            //发送站内信
            SmsSendUtil.sendSmsMsgToQueue(new SmsWebMsg(packet.getAcceptorUserId(), "收到红包通知", message, 1));
        });
    }

    /**
     * 设置基本的红包信息
     *
     * @param redpacketDTO
     * @param unSaveRedPacketInfo
     */
    private void initPacketInfoBasic(RedpacketDTO redpacketDTO, VasRedpacketInfo unSaveRedPacketInfo) {
        Integer currentUserId = redpacketDTO.getId();
        unSaveRedPacketInfo.setValue(redpacketDTO.getValue());
        unSaveRedPacketInfo.setEndTime(redpacketDTO.getEndTime());
        unSaveRedPacketInfo.setCreateUserId(currentUserId);
        unSaveRedPacketInfo.setModifiedUserId(currentUserId);
        unSaveRedPacketInfo.setSenderUserId(currentUserId);
        unSaveRedPacketInfo.setName(redpacketDTO.getName());
        if (StringUtils.isEmpty(unSaveRedPacketInfo.getSenderUserName())) {
            unSaveRedPacketInfo.setSenderUserName(redpacketDTO.getNickName());
        }
    }

    /**
     * 构造map
     *
     * @param sendUserIds
     * @param acceptorUserIds
     * @param vasRedpacketVO
     * @return
     */
    private Map constructMap(List<Integer> sendUserIds,
                             List<Integer> acceptorUserIds,
                             VasRedpacketVO vasRedpacketVO) {
        return new HashMap(3) {
            {
                put("sendUserIds", sendUserIds);
                put("acceptorUserIds", acceptorUserIds);
                put("vasRedpacketVO", vasRedpacketVO);
            }
        };

    }

    private String generateRedPacketKey() {
        String redpageKey;
        while (true) {
            // 随机生成红包编码, 规则：10位A-Z和0-9随机组合
            redpageKey = creRedpageRands(10);
            Integer countTmp = checkRedpageKey(redpageKey);
            if (countTmp == 0) {
                break;
            }
        }
        return redpageKey;
    }

    /**
     * @param length
     * @return
     * @Description 生成红包随机码
     */
    private String creRedpageRands(int length) {
        String rad = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuffer result = new StringBuffer();
        Random rand = new Random();
        for (int i = 0; i < length; i++) {
            int randNum = rand.nextInt(36);
            result.append(rad.substring(randNum, randNum + 1));
        }
        return result.toString();
    }

    @Override
    public RedPacketVO findRedPacketUserSum(VasRedpacketInfo updateRedPacket) {
        return this.vasRedpacketInfoDao.findRedPacketUserSum(updateRedPacket);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public VasRedpacketInfo insert(VasRedpacketInfo vasRedpacketInfo) {
        String key = generateRedPacketKey();
        vasRedpacketInfo.setKey(key);
        try{
            this.vasRedpacketInfoDao.save(vasRedpacketInfo);
        }catch (Exception e){
            if (logger.isErrorEnabled()) {
                logger.error("插入红包失败,红包信息{}\n异常信息:",vasRedpacketInfo, e.getMessage());
            }
            throw new GeneralException("插入红包失败");
        }
        return vasRedpacketInfo;
    }


}
