package com.hongkun.finance.point.facade.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.point.constants.PointConstants;
import com.hongkun.finance.point.facade.PointRecordFacade;
import com.hongkun.finance.point.model.*;
import com.hongkun.finance.point.model.vo.PointVO;
import com.hongkun.finance.point.service.*;
import com.hongkun.finance.point.service.impl.PointRecordServiceImpl;
import com.hongkun.finance.point.utils.PointUtils;
import com.hongkun.finance.sms.constants.SmsConstants;
import com.hongkun.finance.sms.constants.SmsMsgTemplate;
import com.hongkun.finance.sms.model.SmsMsgInfo;
import com.hongkun.finance.sms.model.SmsWebMsg;
import com.hongkun.finance.sms.utils.SmsSendUtil;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.model.RegUserDetail;
import com.hongkun.finance.user.model.vo.UserVO;
import com.hongkun.finance.user.service.RegUserDetailService;
import com.hongkun.finance.user.service.RegUserService;
import com.hongkun.finance.user.utils.BaseUtil;
import com.hongkun.finance.vas.constants.VasVipConstants;
import com.hongkun.finance.vas.model.vo.VasVipGrowRecordMqVO;
import com.hongkun.finance.vas.utils.VipGrowRecordUtil;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.model.StateList;
import com.yirun.framework.core.utils.BeanPropertiesUtil;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.redis.JedisClusterLock;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.hongkun.finance.user.utils.BaseUtil.equelsIntWraperPrimit;
import static com.yirun.framework.core.commons.Constants.*;

/**
 * @Description :
 * @Project : finance
 * @Program Name  : com.hongkun.finance.point.facade.impl.PointRecordFacadeImpl
 * @Author : zhongpingtang@yiruntz.com 唐忠平
 */
@Service
public class PointRecordFacadeImpl implements PointRecordFacade {
    private static final Logger logger = LoggerFactory.getLogger(PointRecordFacadeImpl.class);

    @Reference
    private RegUserService userService;
    @Reference
    private RegUserDetailService regUserDetailService;

    @Reference
    private PointMerchantInfoService pointMerchantInfoService;

    @Autowired
    private PointRecordService recordService;
    @Autowired
    private PointProductOrderService pointProductOrderService;
    @Autowired
    private PointAccountService pointAccountService;
    @Autowired
    private PointRuleService pointRuleService;
    @Override
    public Pager listPointRecord(PointVO pointVO, Pager pager) {
        //查询限制id
        StateList stateList =BeanPropertiesUtil.getLimitConditions(pointVO, UserVO.class, userService::findUserIdsByUserVO);
        if (!stateList.isProceed()) {
            return pager;
        }
        pointVO.getLimitUserIds().addAll(stateList);
        pointVO.setSortColumns("modify_time desc");
        Pager result = recordService.listPointRecord(pointVO, pager);
        if (!BaseUtil.resultPageHasNoData(result)) {
            result.getData().stream().forEach((e) -> {
                PointVO record = (PointVO) e;
                //补全真实姓名和手机号
                UserVO user = userService.findRegUserTelAndRealNameById(record.getRegUserId());
                record.setRealName(user.getRealName());
                record.setLogin(user.getLogin());
                UserVO operator;
                //TODO:zhongping，2017/10/26 等待有其他类型的数据之后，补充积分来源
                if (equelsIntWraperPrimit(record.getType(),PointConstants.POINT_TYPE_DONATE)) {
                    //补全操作员姓名
                    RegUser queryUser = new RegUser();
                    queryUser.setId(record.getBusinessId());
                    queryUser.setQueryColumnId("idAndName");
                    List<RegUser> regUserList = userService.findRegUserList(queryUser);
                    if (!BaseUtil.collectionIsEmpty(regUserList)) {
                        record.setOpratorName(regUserList.get(0).getNickName());
                    }
                    //补全积分来源
                    record.setPointSource("平台赠送积分");
                }


            });

        }
        return result;
    }

    @Override
    public Pager listPointPayRecord(PointVO pointVO, Pager pager) {
        //根据商户名称、商户编号查询商户id集合
        StateList merchantIdList =BeanPropertiesUtil.getLimitConditions(pointVO, PointMerchantInfo.class, pointMerchantInfoService::findMerchantIdsByInfo);
        if (!merchantIdList.isProceed()) {
            return pager;
        }

        //根据买家用户手机、姓名查询用户id集合
        StateList userIdList =BeanPropertiesUtil.getLimitConditions(pointVO, UserVO.class, userService::findUserIdsByUserVO);
        if (!userIdList.isProceed()) {
            return pager;
        }
        pointVO.getLimitBusinessIds().addAll(merchantIdList);
        pointVO.getLimitUserIds().addAll(userIdList);
        //根有相关条件查询积分支付结果集
        pointVO.setType(PointConstants.POINT_TYPE_PAY);
        pointVO.setSortColumns("modify_time desc");
        Pager result = recordService.listPointRecord(pointVO, pager);
        if (!BaseUtil.resultPageHasNoData(result)) {
            result.getData().stream().forEach((e) -> {
                PointVO record = (PointVO) e;
                //补全真实姓名和手机号
                UserVO user = userService.findRegUserTelAndRealNameById(record.getRegUserId());
                record.setRealName(user.getRealName());
                record.setLogin(user.getLogin());
                //补全商户名称和手机号
                PointMerchantInfo merchantInfo = pointMerchantInfoService.findPointMerchantInfoById(record.getBusinessId());
                record.setMerchantCode(merchantInfo.getMerchantCode());
                record.setMerchantName(merchantInfo.getMerchantName());
            });

        }
        return result;
    }

    @Override
    public Pager findPointPayCountList(PointVO pointVO, Pager pager) {
      //查询限制id
        StateList stateList =BeanPropertiesUtil.getLimitConditions(pointVO, UserVO.class, userService::findUserIdsByUserVO);
        if (!stateList.isProceed()) {
            return pager;
        }
        pointVO.getLimitUserIds().addAll(stateList);
        pointVO.setSortColumns("modify_time desc");
        Pager result = recordService.listPointRecord(pointVO, pager);
        if (!BaseUtil.resultPageHasNoData(result)) {
            result.getData().stream().forEach((e) -> {
                PointVO record = (PointVO) e;
                //补全真实姓名和手机号
                final Integer regUserId = record.getRegUserId();
                record.setLogin(BaseUtil.getRegUser(regUserId, () -> this.userService.findRegUserById(regUserId)).getLogin());
                record.setRealName(BaseUtil.getRegUserDetail(regUserId, () -> this.regUserDetailService.findRegUserDetailByRegUserId(regUserId)).getRealName());
                if (equelsIntWraperPrimit(record.getType(),PointConstants.POINT_TYPE_CONVERT)) {                   
                    PointProductOrder pointProductOrder= pointProductOrderService.findPointProductOrderById(record.getBusinessId());
                    record.setAddress(pointProductOrder.getAddress());
                }
            });
        }
        return result;
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT,propagation = Propagation.REQUIRED,readOnly = false)
    public ResponseEntity userPointTransfer(int regUserId, int point, List<Integer> acceptUsers, String senderName) {
        logger.info("userPointTransfer, 积分转赠, 用户标识: {}, 用户名称: {}, 转赠积分: {}, 接收用户: {}",
                regUserId, senderName, point, acceptUsers);
        JedisClusterLock jedisLock = new JedisClusterLock();
        String lockKey = LOCK_PREFFIX + PointRecordServiceImpl.class.getSimpleName() + "_userPointTransfer_" + regUserId;
        try {
            //上redis锁防止并发
            if (!jedisLock.lock(lockKey, LOCK_EXPIRES, Constants.LOCK_WAITTIME)) {
                logger.error("userPointTransfer, 积分转赠获取锁超时, 用户标识: {}, 用户名称: {}, 转赠积分: {}, 接收用户: {}",
                        regUserId, senderName, point, acceptUsers);
                return new ResponseEntity(ERROR, "积分转赠超时，请重新操作！");
            }
            //获取当前用户的积分账户
            PointAccount pointAccount = pointAccountService.findPointAccountByRegUserId(regUserId);
            //1.先获取积分规则，获取积分转赠手续费率
            PointRule pointRule = pointRuleService.getCurrentOnUseRule();
            if (pointRule == null) {
                logger.error("userPointTransfer, 积分转赠无启用的积分规则, 用户标识: {}, 用户名称: {}, 转赠积分: {}, 接收用户: {}",
                        regUserId, senderName, point, acceptUsers);
                return new ResponseEntity(ERROR, "无启用的积分规则，请联系客服人员！");
            }
            //2.计算积分转赠需要的总积分数量:公式[实际扣除积分=（转赠积分 + 转赠手续费）*人数];转赠手续费=转赠积分*转赠费率(向上取整)
            if (point <= 0) {
                return new ResponseEntity(ERROR, "请输入正确的积分数量！");
            }
            if (acceptUsers.size() == 0){
                return new ResponseEntity(ERROR, "请选择要进行转赠的用户！");
            }
            //转赠手续费 = (转赠积分 * 费率 / 100) * 个数
            int sendFee = (int) Math.ceil((pointRule.getPointGivingRate().doubleValue())*Double.valueOf(point)*0.01d);
            //转赠用户实际消耗的总积分
            int actualPoint = (point + sendFee) * acceptUsers.size();
            //3.判断用户的积分数量是否足够使用
            if (actualPoint > pointAccount.getPoint()){
                return new ResponseEntity(ERROR, "积分数量不足！拥有积分：" + pointAccount.getPoint() + ";转赠积分：" + actualPoint);
            }
            //4.判断积分接收人用户有效性
            for (Integer userId:acceptUsers){
                if (userId == null || pointAccountService.findPointAccountByRegUserId(userId) == null){
                    return new ResponseEntity(ERROR,"积分接收人信息异常，请您重新选择！");
                }
                if (regUserId == userId){
                    return new ResponseEntity(ERROR,"积分接收人不能为自己！");
                }
            }

            //积分记录集合，发送积分减少和接收人积分增加
            List<PointRecord> recordList = new ArrayList<>();
            //积分账户集合，发送人积分账户 + 多个接收人积分账户
            List<PointAccount> accountList = new ArrayList<>();
            //5.赠送用户减少积分
            PointAccount senderAccount = new PointAccount();
            senderAccount.setRegUserId(regUserId);
            senderAccount.setPoint(actualPoint*-1);
            accountList.add(senderAccount);
            //组装积分记录和减少的积分值
            for (Integer userId:acceptUsers){
                String acceptUserInfo;
                //获取接收人信息
                RegUserDetail acceptUser = BaseUtil.getRegUserDetail(userId,() -> this.regUserDetailService.findRegUserDetailByRegUserId(userId));
                if (acceptUser != null && StringUtils.isNotBlank(acceptUser.getRealName())) {
                    acceptUserInfo = acceptUser.getRealName();
                }else {
                    acceptUserInfo = BaseUtil.getRegUser(userId,() -> this.userService.findRegUserById(userId)).getLogin().toString();
                }
                //添加赠送人积分划转记录
                PointRecord sendRecord = new PointRecord();
                sendRecord.setType(PointConstants.POINT_TYPE_PASS_OUT);
                sendRecord.setRegUserId(regUserId);
                //积分价值
                sendRecord.setWorth(PointUtils.pointToMoney(point,pointRule.getPerMoneyToPoint()));
                //积分价值
                sendRecord.setRealWorth(PointUtils.pointToMoney(point,pointRule.getPerMoneyToPoint()));
                sendRecord.setFee(sendFee*-1);
                //手续费价值
                sendRecord.setFeeWorth(PointUtils.pointToMoney(sendFee,pointRule.getPerMoneyToPoint()));
                sendRecord.setPoint((point + sendFee)*-1);
                sendRecord.setComments("转赠给" + acceptUserInfo + "的积分");
                //由于需要获取该记录的id先进行插入
                recordService.insertPointRecord(sendRecord);
                //添加接收人积分记录
                PointRecord acceptRecord = new PointRecord();
                acceptRecord.setRegUserId(userId);
                acceptRecord.setType(PointConstants.POINT_TYPE_PASS_IN);
                acceptRecord.setBusinessId(sendRecord.getId());
                acceptRecord.setWorth(PointUtils.pointToMoney(point,pointRule.getPerMoneyToPoint()));
                acceptRecord.setRealWorth(PointUtils.pointToMoney(point,pointRule.getPerMoneyToPoint()));
                acceptRecord.setPoint(point<0?point*-1:point);
                acceptRecord.setComments("收到" + senderName + "的积分");
                acceptRecord.setPlatform(1);
                recordList.add(acceptRecord);
                //添加接收人账户积分增加
                PointAccount acceptAccount = new PointAccount();
                acceptAccount.setRegUserId(userId);
                acceptAccount.setPoint(point);
                accountList.add(acceptAccount);
            }
            //批量添加积分记录集合,包括发送人积分减少记录和接收人积分增加记录
            recordService.insertPointRecordBatch(recordList);
            //批量处理发送积分用户和接收积分用户
            pointAccountService.updatePointAccountBatch(accountList,accountList.size());
        } catch (Exception e) {
            logger.error("userPointTransfer, 积分转赠异常, 用户标识: {}, 用户名称: {}, 转赠积分: {}, 接收用户: {}, 异常信息： ",
                    regUserId, senderName, point, acceptUsers,e);
            throw new GeneralException("积分转赠异常，请联系客服！");
        } finally {
            jedisLock.freeLock(lockKey);
        }

        //插入成长值记录
        try {
            VasVipGrowRecordMqVO record = new VasVipGrowRecordMqVO();
            record.setUserId(regUserId);
            record.setGrowType(VasVipConstants.VAS_VIP_GROW_TYPE_POINT_DONATION);
            VipGrowRecordUtil.sendVipGrowRecordToQueue(record);
        }catch (Exception e){
            logger.error("userPointTransfer, 积分转赠插入成长值异常, 用户标识: {}, 用户名称: {}, 转赠积分: {}, 接收用户: {}, 异常信息： ",
                    regUserId, senderName, point, acceptUsers, e);
        }
        //发送站内信
        try {
            List<SmsMsgInfo> list = new ArrayList<>();
            List<Object> args = new ArrayList<>();
            args.add(senderName);
            args.add(point);
            //组装积分接收人站内信集合
            for (Integer userId:acceptUsers){
                SmsMsgInfo info = new SmsWebMsg(userId, SmsMsgTemplate.MSG_USER_POINT_TRANSFER_SUCCESS.getTitle(),
                        SmsMsgTemplate.MSG_USER_POINT_TRANSFER_SUCCESS.getMsg(), SmsConstants.SMS_TYPE_NOTICE,args.toArray());
                list.add(info);
            }
            SmsSendUtil.sendWebMsgToQueue(list);
        }catch (Exception e){
            logger.error("userPointTransfer, 积分转赠发送消息异常, 用户标识: {}, 用户名称: {}, 转赠积分: {}, 接收用户: {}, 异常信息： ",
                    regUserId, senderName, point, acceptUsers, e);
        }

        return new ResponseEntity(SUCCESS, "转赠成功！");
    }
}
