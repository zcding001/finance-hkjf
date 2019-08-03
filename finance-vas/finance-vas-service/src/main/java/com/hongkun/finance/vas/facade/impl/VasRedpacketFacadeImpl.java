package com.hongkun.finance.vas.facade.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.hongkun.finance.payment.constant.TradeTransferConstants;
import com.hongkun.finance.payment.enums.FundtransferBigTypeStateEnum;
import com.hongkun.finance.payment.enums.FundtransferSmallTypeStateEnum;
import com.hongkun.finance.payment.model.FinFundtransfer;
import com.hongkun.finance.payment.model.FinTradeFlow;
import com.hongkun.finance.payment.service.FinAccountService;
import com.hongkun.finance.payment.service.FinConsumptionService;
import com.hongkun.finance.payment.service.FinTradeFlowService;
import com.hongkun.finance.payment.util.FinTFUtil;
import com.hongkun.finance.sms.constants.SmsConstants;
import com.hongkun.finance.sms.constants.SmsMsgTemplate;
import com.hongkun.finance.sms.model.SmsMsgInfo;
import com.hongkun.finance.sms.model.SmsWebMsg;
import com.hongkun.finance.sms.utils.SmsSendUtil;
import com.hongkun.finance.user.constants.UserConstants;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.model.vo.UserVO;
import com.hongkun.finance.user.service.RegUserDetailService;
import com.hongkun.finance.user.service.RegUserService;
import com.hongkun.finance.user.utils.BaseUtil;
import com.hongkun.finance.user.utils.ValidateUtil;
import com.hongkun.finance.vas.constants.VasConstants;
import com.hongkun.finance.vas.facade.VasRedpacketFacade;
import com.hongkun.finance.vas.model.VasRedpacketInfo;
import com.hongkun.finance.vas.model.dto.RedpacketDTO;
import com.hongkun.finance.vas.model.vo.VasRedpacketVO;
import com.hongkun.finance.vas.service.VasRedpacketInfoService;
import com.hongkun.finance.vas.service.impl.VasRedpacketInfoServiceImpl;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.enums.PlatformSourceEnums;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.CommonUtils;
import com.yirun.framework.core.utils.DateUtils;
import com.yirun.framework.core.utils.ExcelUtil;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.redis.JedisClusterLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

import static com.hongkun.finance.payment.constant.TradeTransferConstants.*;
import static com.hongkun.finance.user.constants.UserConstants.PLATFORM_ACCOUNT_ID;
import static com.hongkun.finance.user.utils.BaseUtil.redisLock;
import static com.hongkun.finance.vas.constants.VasConstants.*;
import static com.yirun.framework.core.commons.Constants.*;

/**
 * @Description :
 * @Project : finance
 * @Program Name : com.hongkun.finance.vas.facade.impl.VasRedpacketFacadeImpl
 * @Author : zhongpingtang@yiruntz.com 唐忠平
 */
@Service
public class VasRedpacketFacadeImpl implements VasRedpacketFacade {

    private static final Logger logger = LoggerFactory.getLogger(VasRedpacketFacadeImpl.class);

    @Autowired
    private VasRedpacketInfoService vasRedpacketInfoService;

    @Reference
    private FinConsumptionService finConsumptionService;

    @Reference
    private RegUserService userService;

    @Reference
    private FinAccountService finAccountService;

    @Reference
    private RegUserDetailService regUserDetailService;

    @Reference
    private FinTradeFlowService finTradeFlowService;
    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public ResponseEntity distributeRedpacket(RedpacketDTO redpacketDTO, Integer source,
                                              PlatformSourceEnums platformSourceEnums) {
        if (logger.isInfoEnabled()) {
            logger.info("方法名: distributeRedpacket, 执行派发红包操作, 入参: 待派发的红包信息: {}, 派发来源: {}, 平台信息: {}", redpacketDTO,
                    source, platformSourceEnums);
        }
        try {
            /**
             * step 1：生成或者 派发红包（包括平台或者个人）
             */
            boolean operateDistribute = !REDPACKET_SOURCE_PERSONAL.equals(source);
            boolean sourceOffline = REDPACKET_SOURCE_OFFLINE.equals(source);
            //锁住：因为生成红包key在内存，无法保证多节点操作
            List<VasRedpacketInfo> redPacketList = (List<VasRedpacketInfo>) redisLock(DISTRIBUTEREDPACKET_LOCK, null, "派发红包申请锁失败",
                    (unUsedParam) -> sourceOffline ? vasRedpacketInfoService.produceRedpacket(redpacketDTO)
                            : vasRedpacketInfoService.distributeRedPacketToUser(redpacketDTO, source));

            /**
             * step 2:冻结出钱账户的资金 如果是运营派发或者生成则设为平台账户
             */
            if (operateDistribute || sourceOffline) {
                // 平台账户
                redpacketDTO.setId(UserConstants.PLATFORM_ACCOUNT_ID);
            }
//            List<Integer> redPacketKeys = redPacketList.stream().map(e -> e.getId()).collect(Collectors.toList());
            /**
             * step 3:记录操作流水
             */
            ResponseEntity result = logCashPay(redpacketDTO, redPacketList, sourceOffline ?
                            TRADE_TYPE_RED_PACKET_GENERATE
                            : (operateDistribute ? TRADE_TYPE_RED_PACKET_DISTRIBUTE : TRADE_TYPE_RED_PACKET_DISTRIBUTE_PERSONAL),
                    platformSourceEnums);
            /**
             * step 4:个人发送红包发送站内信
             */
            if (result.getResStatus() == SUCCESS && !operateDistribute){
                //组装站内信
                List<SmsMsgInfo> webMsgList = new ArrayList<>();
                try {
                    //模糊的手机号
                    String tel = redpacketDTO.getLogin().toString().substring(0,3) + "****" + redpacketDTO.getLogin().toString()
                            .substring(7);
                    redPacketList.forEach((redPacket) -> {
                        //替换消息模板的参数
                        List<String> args = new ArrayList<>();
                        args.add(tel);
                        args.add(redPacket.getValue().toString());
                        args.add(redPacket.getKey());
                        args.add(DateUtils.format(redPacket.getEndTime(),DateUtils.DATE_HH_MM_SS));

                        //红包接收人站内信
                        SmsMsgInfo smsWebMsg = new SmsWebMsg(redPacket.getAcceptorUserId(),
                                SmsMsgTemplate.MSG_RED_PACKET_PERSON.getTitle(),
                                SmsMsgTemplate.MSG_RED_PACKET_PERSON.getMsg(), SmsConstants.SMS_TYPE_NOTICE, args.toArray());
                        webMsgList.add(smsWebMsg);
                    });
                    //执行发送站内信逻辑
                    SmsSendUtil.sendWebMsgToQueue(webMsgList);
                }catch (Exception e){
                    logger.error("distributeRedpacket, 个人发送红包发送消息失败, 用户标识: {}, 接收用户id: {}, 发送红包金额: {}, 异常信息: {}",
                            redpacketDTO.getId(), redpacketDTO.getUserIds(), redpacketDTO.getValue(), e);
                }
            }
            return result;
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("执行派发红包操作异常, 红包信息: {}, 派发来源: {}, 平台信息: {}\n异常信息: ", redpacketDTO, source,
                        platformSourceEnums, e);
            }
            throw new GeneralException("执行派发红包操作异常,请重试!");
        }

    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public ResponseEntity changeRedPacketState(RedpacketDTO redpacketDTO, PlatformSourceEnums platformSourceEnums) {
        if (logger.isInfoEnabled()) {
            logger.info("方法名: changeRedPacketState, 改变红包操作, 入参: 待改变的红包信息: {}, 平台信息: {}", redpacketDTO, platformSourceEnums);
        }
        try {
            Integer changeState = redpacketDTO.getState();
            List<VasRedpacketInfo> packets = vasRedpacketInfoService.changeRedPacketStateForUnCheckPacket(redpacketDTO);
            if (logger.isInfoEnabled()) {
                logger.info("方法名: changeRedPacketState: 待处理的红包信息: packets: {}", packets);
            }
            if (!BaseUtil.collectionIsEmpty(packets)) {
                /**
                 * step 2.4:失效红包或者审核拒绝，解冻平台账户资金
                 */
                if (REDPACKET_STATE_REJECT.equals(changeState) || REDPACKET_STATE_FAILED.equals(changeState)) {

                    /**
                     * step 2.4.1:执行解冻
                     */
                    unfreezeAccountMoney(packets, REDPACKET_STATE_REJECT.equals(changeState)
                            ? TRADE_TYPE_RED_PACKET_CHECK_REJECT : TRADE_TYPE_RED_PACKET_CHECK_INVALID, platformSourceEnums);
                }
            }
            return ResponseEntity.SUCCESS;
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("改变红包操作异常, 红包信息: {}, 平台信息: {}\n异常信息: ", redpacketDTO, platformSourceEnums, e);
            }
            throw new GeneralException("改变红包操作异常,请重试!");
        }


    }

    @Override
    public ResponseEntity listPage(VasRedpacketVO vasRedpacketVO, Pager pager) {
        Long senderTel = vasRedpacketVO.getSenderTel();
        List<Integer> sendUserIds = new ArrayList<>();
        List<Integer> acceptorUserIds = new ArrayList<>();

        // 找到限制分页ids
        querySendAndAcceptUserIds(vasRedpacketVO, senderTel, sendUserIds, acceptorUserIds);

        vasRedpacketVO.setSortColumns("create_time desc");
        // 查出红包表中的分页
        Pager resultPage = vasRedpacketInfoService.findVasRedpacketInfoList(sendUserIds, acceptorUserIds,
                vasRedpacketVO, pager);

        ResponseEntity result = new ResponseEntity(SUCCESS);
        // 统计查询条件下红包个数
        result.getParams().put("totalCount", resultPage.getTotalRows());
        // 统计查询条件下红包总金额
        result.getParams().put("totalNum",
                vasRedpacketInfoService.findVasRedpacketTotalNum(sendUserIds, acceptorUserIds, vasRedpacketVO, pager));

        if (!BaseUtil.resultPageHasNoData(resultPage)) {
            // 组装数据
            resultPage.getData().stream().forEach((redpacket) -> {
                VasRedpacketVO redpacketVO = (VasRedpacketVO) redpacket;
                UserVO acceptor = userService.findRegUserTelAndRealNameById(redpacketVO.getAcceptorUserId());
                RegUser sender = BaseUtil.getRegUser(redpacketVO.getSenderUserId(), () -> userService.findRegUserById(redpacketVO.getSenderUserId()));
                // 设置发送人手机号
                redpacketVO.setSenderTel(sender.getLogin());
                if (acceptor != null) {
                    // 设置兑换用户手机号
                    redpacketVO.setAcceptorTel(acceptor.getLogin());
                    // 设置兑换用户姓名
                    redpacketVO.setAcceptorName(acceptor.getRealName());
                }

            });
        }

        result.setResMsg(resultPage);
        // 返回数据
        return result;
    }

    @Override
    public ResponseEntity getUserSendOutRedPacketInfo(Integer userId, Pager pager) {
        VasRedpacketInfo vasRedpacketInfo = new VasRedpacketInfo();
        vasRedpacketInfo.setSenderUserId(userId);
        vasRedpacketInfo.setSortColumns("modify_time desc");
        Pager result = vasRedpacketInfoService.findVasRedpacketInfoList(vasRedpacketInfo, pager);
        List<VasRedpacketInfo> list = (List<VasRedpacketInfo>) result.getData();
        List<VasRedpacketVO> dataList = new ArrayList<>();
        list.forEach((redPacket) -> {
            VasRedpacketVO vasRedpacketVO = new VasRedpacketVO();
            String name = regUserDetailService.findRegUserDetailNameByRegUserId(redPacket.getAcceptorUserId());
            vasRedpacketVO.setName(redPacket.getName());//红包名称
            vasRedpacketVO.setValue(redPacket.getValue());//红包金额
            vasRedpacketVO.setCreateTime(redPacket.getCreateTime());//发送时间
            vasRedpacketVO.setAcceptorName(name);//领取对象
            vasRedpacketVO.setState(redPacket.getState());//状态
            vasRedpacketVO.setRedpacketSource(redPacket.getRedpacketSource());//红包类型
            dataList.add(vasRedpacketVO);
        });
        result.setData(dataList);
        return new ResponseEntity(SUCCESS, result);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public ResponseEntity exchangeRedPacketInfo(Integer userId, String key, PlatformSourceEnums platformSourceEnums) {
        logger.info("exchangeRedPacketInfo, 兑换红包, 用户标识: {}, 兑换码: {}, 兑换来源: {}", userId, key, platformSourceEnums);
        JedisClusterLock jedisLock = new JedisClusterLock();
        String lockKey = LOCK_PREFFIX + VasRedpacketInfoServiceImpl.class.getSimpleName() + "_exchangeRedPacketInfo_" + key;
        try {
            //添加redis锁,如果获取锁超时则返回提示信息
            if (!jedisLock.lock(lockKey, LOCK_EXPIRES, Constants.LOCK_WAITTIME)) {
                logger.error("exchangeRedPacketInfo, 兑换红包超时, 用户标识: {}, 兑换码: {}, 兑换来源: {}",
                        userId, key, platformSourceEnums);
                return new ResponseEntity(ERROR, "兑换红包超时，请重新操作！");
            }
            //根据key查询红包记录
            VasRedpacketInfo redpacketInfo = vasRedpacketInfoService.findVasRedpacketInfoByKey(key);
            //1:判断该key是否能查到有效的红包
            if (redpacketInfo == null) {
                return new ResponseEntity(ERROR, "无效的红包码，请您检查红包码是否正确！");
            }
            //2:判断红包是否已经被兑换
            if (Objects.equals(redpacketInfo.getState(), VasConstants.REDPACKET_STATE_HASRECEIVED)) {
                return new ResponseEntity(ERROR, "该红包兑换码已被使用！");
            }
            //3:判断该红包是否为未领取状态
            if (!Objects.equals(redpacketInfo.getState(), VasConstants.REDPACKET_STATE_UNRECEIVED)) {
                return new ResponseEntity(ERROR, "该红包无法兑换，请您联系客服！");
            }
            //4:判断该红包是否在截止时间前
            if (DateUtils.getCurrentDate().after(redpacketInfo.getEndTime())) {
                return new ResponseEntity(ERROR, "该红包已过兑换期限！");
            }
            //5:判断该红包是否为该用户所属、或红包为线下红包
            if (redpacketInfo.getAcceptorUserId() != 0 && !(redpacketInfo.getAcceptorUserId().equals(userId))){
                return new ResponseEntity(ERROR, "红包兑换码只限本人使用！");
            }
            //6:若该红包已生成相关流水，说明已被领取
            FinTradeFlow finTradeFlow = finTradeFlowService.findByPflowIdAndTradeType(redpacketInfo.getId().toString(),
                    TradeTransferConstants.TRADE_TYPE_RED_PACKET_EXCHANGE);
            if (finTradeFlow != null){
                return new ResponseEntity(ERROR,"该红包已被领取，请勿重复领取！");
            }
            //7:开始兑换红包逻辑
            VasRedpacketInfo updateRedPacket = new VasRedpacketInfo();
            updateRedPacket.setId(redpacketInfo.getId());
            updateRedPacket.setState(VasConstants.REDPACKET_STATE_HASRECEIVED);
            updateRedPacket.setAcceptorUserId(userId);
            int num = vasRedpacketInfoService.exchangeRedPacketInfo(updateRedPacket);
            if (num <= 0) {
                logger.error("exchangeRedPacketInfo, 兑换红包更新红包信息失败, 用户标识: {}, 兑换红包信息: {}, 兑换来源: {}",
                        userId, updateRedPacket.toString(), platformSourceEnums);
                return new ResponseEntity(ERROR, "兑换红包失败，请联系客服人员！");
            }
            //8:生成用户红包兑换流水信息
            FinTradeFlow tradeFlow = FinTFUtil.initFinTradeFlow(userId, redpacketInfo.getId(), redpacketInfo.getValue(),
                    TradeTransferConstants.TRADE_TYPE_RED_PACKET_EXCHANGE, platformSourceEnums);
            List<FinFundtransfer> transfersList = new ArrayList<>();
            //9:生成用户获取红包金额资金划转
            //判断资金划转为个人用户还是平台账户,红包来源为，4：个人派发时为个人用户，其他为平台账户派发
            int transferUserId = UserConstants.PLATFORM_ACCOUNT_ID;
            if (VasConstants.REDPACKET_SOURCE_PERSONAL.equals(redpacketInfo.getRedpacketSource())) {
                transferUserId = redpacketInfo.getSenderUserId();
            }
            //当前用户收入红包资金划转流水
            FinFundtransfer userFundtransfer = FinTFUtil.initFinFundtransfer(tradeFlow.getFlowId(), userId, transferUserId,
                    redpacketInfo.getValue(), TRANSFER_SUB_CODE_INCOME);
            //平台或个人支出冻结资金划转
            FinFundtransfer transferUserFundtransfer = FinTFUtil.initFinFundtransfer(tradeFlow.getFlowId(),
                    transferUserId, userId, redpacketInfo.getValue(), TradeTransferConstants.getFundTransferSubCodeByType
                            (FundtransferBigTypeStateEnum.PAY, FundtransferSmallTypeStateEnum.FROZEN));
            transfersList.add(userFundtransfer);
            transfersList.add(transferUserFundtransfer);
            finConsumptionService.updateAccountInsertTradeAndTransfer(tradeFlow, transfersList);
        } catch (Exception e) {
            logger.error("exchangeRedPacketInfo, 兑换红包时异常, 用户标识: {}, 兑换码: {}, 兑换来源: {}, 异常信息: ",
                    userId, key, platformSourceEnums, e);
            throw new GeneralException("兑换红包异常，请联系客服人员！");
        } finally {
            jedisLock.freeLock(lockKey);
        }
        return new ResponseEntity(SUCCESS, "兑换成功！");
    }

   /**
   *  @Description    ：根据前台搜索条件，找到限制分页ids
   *  @Method_Name    ：querySendAndAcceptUserIds
   *  @param vasRedpacketVO
   *  @param senderTel
   *  @param sendUserIds
   *  @param acceptorUserIds
   *  @return void
   *  @Creation Date  ：2018/4/24
   *  @Author         ：zhongpingtang@hongkun.com.cn
   */
    private void querySendAndAcceptUserIds(VasRedpacketVO vasRedpacketVO, Long senderTel, List<Integer> sendUserIds,
                                           List<Integer> acceptorUserIds) {
        if (senderTel != null) {
            sendUserIds.add(-1);/* 添加未找到的状态位 */
            sendUserIds.addAll(userService.findUserIdsByTel(senderTel));
        }
        UserVO selectVO = new UserVO();
        // 兑换用户手机号
        Long acceptorTel = vasRedpacketVO.getAcceptorTel();
        selectVO.setLogin(acceptorTel);
        // 姓名
        String acceptorName = vasRedpacketVO.getAcceptorName();
        if (StringUtils.isNotEmpty(acceptorName)) {
            selectVO.setRealName(acceptorName);
        }

        if (StringUtils.isNotEmpty(acceptorName) || acceptorTel != null) {
            acceptorUserIds.add(-1);// 添加未找到的状态位置
            acceptorUserIds.addAll(userService.findUserIdsByUserVO(selectVO));
        }

    }

   /**
   *  @Description    ：审核拒绝或者红包失效解冻账户资金
   *  @Method_Name    ：unfreezeAccountMoney
   *  @param unRollbackPacket
   *  @param type
   *  @param platformSourceEnums
   *  @return void
   *  @Creation Date  ：2018/4/24
   *  @Author         ：zhongpingtang@hongkun.com.cn
   */
    private void unfreezeAccountMoney(List<VasRedpacketInfo> unRollbackPacket, Integer type,
                                      PlatformSourceEnums platformSourceEnums) {
        unRollbackPacket
                .forEach(packet -> finConsumptionService.cashPay(FinTFUtil.initFinTradeFlow(UserConstants.PLATFORM_ACCOUNT_ID,
                        packet.getId(), packet.getValue(), type, platformSourceEnums), TRANSFER_SUB_CODE_THAW));

    }

    /**
    *  @Description    ：记录红包流水
    *  @Method_Name    ：logCashPay
    *  @param redpacketDTO
    *  @param redPacketList
    *  @param type
    *  @param platformSourceEnums
    *  @return com.yirun.framework.core.model.ResponseEntity
    *  @Creation Date  ：2018/4/24
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    private ResponseEntity logCashPay(RedpacketDTO redpacketDTO, List<VasRedpacketInfo> redPacketList, Integer type,
                                      PlatformSourceEnums platformSourceEnums) {
        //生成(个人发送给个人红包)批量流水列表
        List<FinTradeFlow> finTradeFlows = new ArrayList<>();
        //个人发送给平台红包的流水，最多只会有一条
        FinTradeFlow platformFinTradeFlow = null;
        //个人发送给平台红包流水对应的划转记录，个人支出和平台收入
        List<FinFundtransfer> fundtransferList = null;
        for (VasRedpacketInfo redpacketInfo:redPacketList){
            //个人发送红包给个人时，生成流水和划转，（由于生成线下红包时，存在没有acceptorUserId情况，因此使用Objects.equals方法）
            if (!Objects.equals(redpacketInfo.getAcceptorUserId(),PLATFORM_ACCOUNT_ID)){
                FinTradeFlow finTradeFlow = FinTFUtil.initFinTradeFlow(redpacketDTO.getId(), redpacketInfo.getId(),
                        redpacketDTO.getValue(), type, platformSourceEnums);
                finTradeFlow.setTransferSubCode(TRANSFER_SUB_CODE_FREEZE);
                finTradeFlows.add(finTradeFlow);
            }else {
                fundtransferList = new ArrayList<>();
                platformFinTradeFlow = FinTFUtil.initFinTradeFlow(redpacketDTO.getId(), redpacketInfo.getId(),
                        redpacketDTO.getValue(), TRADE_TYPE_RED_PACKET_DISTRIBUTE_PLATFORM, platformSourceEnums);
                //个人支出
                FinFundtransfer personFund = FinTFUtil.initFinFundtransfer(platformFinTradeFlow.getFlowId(),
                        redpacketDTO.getId(),PLATFORM_ACCOUNT_ID,redpacketDTO.getValue(),TRANSFER_SUB_CODE_PAY);
                fundtransferList.add(personFund);
                //平台收入
                FinFundtransfer platFund = FinTFUtil.initFinFundtransfer(platformFinTradeFlow.getFlowId(),
                        PLATFORM_ACCOUNT_ID,redpacketDTO.getId(),redpacketDTO.getValue(),TRANSFER_SUB_CODE_INCOME);
                fundtransferList.add(platFund);
            }
        }
        //批量流水记录
        finConsumptionService.cashPayBatch(finTradeFlows);
        //特殊处理个人发送红包给平台
        if (platformFinTradeFlow != null){
            finConsumptionService.updateAccountInsertTradeAndTransfer(platformFinTradeFlow,fundtransferList);
        }
        return ResponseEntity.SUCCESS;
    }


    @Override
    public void invalidateRedPacketOverTime(Date currentDate, int shardingItem) {
        String currentJobDate = DateUtils.format(currentDate, DateUtils.DATE_HH_MM_SS);
        try{
            //1.获取已过期红包集合；当前时间超过使用截止时间且状态不为已过期状态
            List<VasRedpacketInfo> invalidRedPackets = this.vasRedpacketInfoService.findInvalidRedPackets(currentDate);
            if (!BaseUtil.collectionIsEmpty(invalidRedPackets)) {
                //设置红包状态为失效
                invalidRedPackets.forEach(rp->rp.setState(VasConstants.REDPACKET_STATE_OUT_OF_DATE));
                //批量失效红包
                this.vasRedpacketInfoService.updateVasRedpacketInfoBetch(invalidRedPackets);
                //解冻用户冻结资金
                unfreezeAccountMoney(invalidRedPackets,TradeTransferConstants.TRADE_TYPE_RED_PACKET_CHECK_INVALID, PlatformSourceEnums.PC);
            }

            logger.info("invalidateRedPacketOverTime, 失效过期红包, 处理时间: {}, 处理分片项: {}, " +
                    "要处理的红包集合: {}", currentJobDate, shardingItem, JSON.toJSON(invalidRedPackets));
        }catch (Exception e){
            logger.error("invalidateRedPacketOverTime, 失效过期红包异常, 处理时间: {}, 处理分片项: {}, 异常信息: ",
                    currentJobDate, shardingItem, e);
        }
    }

    @Override
    public ResponseEntity<?> importRedPackets(String filePath,Integer currentUserId) {
        List<List<String>> dataList = ExcelUtil.getDataList(filePath);
        if (CommonUtils.isEmpty(dataList)) {
            return new ResponseEntity<>(Constants.ERROR, "未找到有效的数据");
        }
        //初始化红包发送失败索引记录
        List<Integer> failIndexList = new ArrayList<>();
        //截止时间为当天发送后一个月内
        Date endDate = DateUtils.addMonth(new Date(),1);
        //遍历所有数据
        for (int i = 0;i < dataList.size();i++){
            RedpacketDTO redpacketDTO = new RedpacketDTO();
            List<String> l = dataList.get(i);
            //手机号
            String tel = l.get(0);
            //红包金额
            String money = l.get(1);
            //发送原因
            String reason = l.get(2);
            //红包类型
            String packetType = l.get(3);
            //验证手机号格式
            ResponseEntity<?> result = ValidateUtil.validateLogin(tel);
            if (result.getResStatus() == Constants.ERROR){
                return new ResponseEntity<>(Constants.ERROR,"非法手机号[" + tel + "],出现在第[" + (i + 1) + "]行");
            }
            RegUser regUser = this.userService.findRegUserByLogin(Long.parseLong(tel));
            if (regUser == null){
                return new ResponseEntity<>(Constants.ERROR,"手机号[" + tel + "]账户不存,出现在第[" + (i + 1) + "]行");
            }

            BigDecimal moneyDeci;
            //验证红包金额
            if (StringUtils.isNotEmpty(money)){
                try {
                    moneyDeci = BigDecimal.valueOf(Double.valueOf(money));
                } catch (NumberFormatException e) {
                    return new ResponseEntity<>(Constants.ERROR,"红包金额格式不正确,出现在第[" + (i + 1) +"]行");
                }
            }else {
                return new ResponseEntity<>(Constants.ERROR,"红包金额不能为空,出现在第[" + (i + 1) +"]行");
            }
            redpacketDTO.setValue(moneyDeci);
            //发送理由
            if (StringUtils.isEmpty(reason)){
                return new ResponseEntity<>(Constants.ERROR,"发送红包理由不能为空,出现在第[" + (i + 1) +"]行");
            }
            //红包类型
            Integer packetTypeInt = null;
            if (StringUtils.isNotEmpty(packetType)){
                try {
                    packetTypeInt = Integer.valueOf(packetType);
                } catch (NumberFormatException e) {
                    return new ResponseEntity<>(Constants.ERROR,"发送红包类型格式不正确,出现在第[" + (i + 1) +"]行");
                }
            }else {
                return new ResponseEntity<>(Constants.ERROR,"发送红包类型不能为空,出现在第[" + (i + 1) +"]行");
            }
            //组装数据
            HashSet<Integer> userIds = new HashSet<>();
            userIds.add(regUser.getId());
            redpacketDTO.setUserIds(userIds);
            redpacketDTO.setValue(moneyDeci);
            redpacketDTO.setSendReason(reason);
            redpacketDTO.setNum(1);
            redpacketDTO.setRedPacketType(packetTypeInt);
            redpacketDTO.setId(currentUserId);
            redpacketDTO.setEndTime(endDate);
            //发送红包
            ResponseEntity response = distributeRedpacket(redpacketDTO,REDPACKET_SOURCE_IMPORT,PlatformSourceEnums.PC);
            //记录发送失败的行
            if (response.getResStatus() == ERROR){
                failIndexList.add(i + 1);
            }
        }
        if (failIndexList.size() > 0){
            String msg = "以下行数" + failIndexList.toString() + "导入失败，请核实！";
            return new ResponseEntity<>(Constants.ERROR,msg);
        }
        return new ResponseEntity<>(Constants.SUCCESS,"所有红包记录导入成功");
    }

}
