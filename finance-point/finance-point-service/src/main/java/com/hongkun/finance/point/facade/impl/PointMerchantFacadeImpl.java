package com.hongkun.finance.point.facade.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.payment.constant.TradeTransferConstants;
import com.hongkun.finance.payment.enums.FundtransferBigTypeStateEnum;
import com.hongkun.finance.payment.enums.FundtransferSmallTypeStateEnum;
import com.hongkun.finance.payment.model.FinFundtransfer;
import com.hongkun.finance.payment.model.FinTradeFlow;
import com.hongkun.finance.payment.service.FinAccountService;
import com.hongkun.finance.payment.service.FinConsumptionService;
import com.hongkun.finance.payment.util.FinTFUtil;
import com.hongkun.finance.point.constants.PointConstants;
import com.hongkun.finance.point.facade.PointMerchantFacade;
import com.hongkun.finance.point.model.PointAccount;
import com.hongkun.finance.point.model.PointMerchantInfo;
import com.hongkun.finance.point.model.PointRecord;
import com.hongkun.finance.point.model.PointRule;
import com.hongkun.finance.point.service.PointAccountService;
import com.hongkun.finance.point.service.PointMerchantInfoService;
import com.hongkun.finance.point.service.PointRecordService;
import com.hongkun.finance.point.service.PointRuleService;
import com.hongkun.finance.point.utils.PointUtils;
import com.hongkun.finance.sms.constants.SmsConstants;
import com.hongkun.finance.sms.constants.SmsMsgTemplate;
import com.hongkun.finance.sms.model.SmsAppMsgPush;
import com.hongkun.finance.sms.model.SmsWebMsg;
import com.hongkun.finance.sms.utils.SmsSendUtil;
import com.hongkun.finance.user.constants.UserConstants;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.model.vo.UserVO;
import com.hongkun.finance.user.service.RegUserDetailService;
import com.hongkun.finance.user.service.RegUserService;
import com.hongkun.finance.user.utils.BaseUtil;
import com.hongkun.finance.vas.constants.VasVipConstants;
import com.hongkun.finance.vas.model.vo.VasVipGrowRecordMqVO;
import com.hongkun.finance.vas.utils.VipGrowRecordUtil;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.enums.PlatformSourceEnums;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.model.StateList;
import com.yirun.framework.core.utils.BeanPropertiesUtil;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.jms.JmsService;
import com.yirun.framework.redis.JedisClusterLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

import static com.hongkun.finance.payment.constant.TradeTransferConstants.TRANSFER_SUB_CODE_PAY;
import static com.hongkun.finance.sms.constants.SmsConstants.SMS_APP_MSG_TYPE_MERCHANT;
import static com.yirun.framework.core.commons.Constants.*;

/**
 * @Description : 积分商户facade实现类
 * @Project : finance
 * @Program Name  : com.hongkun.finance.point.facade.impl.PointMerchantFacadeImpl
 * @Author : zhongpingtang@yiruntz.com 唐忠平
 */
@Service
public class PointMerchantFacadeImpl implements PointMerchantFacade {
    private static final Logger logger = LoggerFactory.getLogger(PointMerchantFacadeImpl.class);

    @Autowired
    private PointMerchantInfoService pointMerchantInfoService;
    @Autowired
    private PointRuleService pointRuleService;
    @Autowired
    private PointAccountService pointAccountService;
    @Autowired
    private PointRecordService pointRecordService;
    @Autowired
    private JmsService jmsService;
    @Reference
    private RegUserService regUserService;
    @Reference
    private RegUserDetailService regUserDetailService;
    @Reference
    private FinConsumptionService finConsumptionService;
    @Reference
    private FinAccountService finAccountService;

    @Override
    public ResponseEntity pointMerchantList(PointMerchantInfo pointMerchantInfo, Pager pager) {
       //step 1:查出所有的limitIds
        StateList limitConditions =
                BeanPropertiesUtil.getLimitConditions(pointMerchantInfo, UserVO.class, regUserService::findUserIdsByUserVO);
        if (!limitConditions.isProceed()) {
            return new ResponseEntity(SUCCESS, pager);
        }

        pointMerchantInfo.getLimitUserIds().addAll(limitConditions);

        //step 2:查出所有符合情况的记录，补全数据
        Pager result = pointMerchantInfoService.findPointMerchantInfoList(pointMerchantInfo, pager);
        if (!BaseUtil.resultPageHasNoData(result)) {
            result.getData().stream().forEach(e -> {
                PointMerchantInfo pointMerchant = (PointMerchantInfo) e;
                UserVO user = regUserService.findRegUserTelAndRealNameById(pointMerchant.getRegUserId());
                pointMerchant.setUserName(user.getRealName());
                pointMerchant.setTel(user.getLogin());
            });
        }

        return new ResponseEntity(SUCCESS, result);
    }



    @Override
    public ResponseEntity selectPointMerchantInfoDetail(Integer merchantId) {
        PointMerchantInfo merchantInfo = pointMerchantInfoService.findPointMerchantInfoById(merchantId);
        if (merchantInfo != null) {
            UserVO user = regUserService.findRegUserTelAndRealNameById(merchantInfo.getRegUserId());
            merchantInfo.setUserName(user.getRealName());
            merchantInfo.setTel(user.getLogin());
        }
        return new ResponseEntity(SUCCESS, merchantInfo);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT,propagation = Propagation.REQUIRED,readOnly = false)
    public ResponseEntity pointPayment(int regUserId, String payPass, String merchantCode, BigDecimal money,PlatformSourceEnums platformSourceEnums) {
        logger.info("pointPayment, 积分支付, 用户标识: {}, 支付金额: {}, 商户号: {}, 支付来源: {}", regUserId, money, merchantCode,
                platformSourceEnums);
        Map<String,Object> param = new HashMap<>();
        PointMerchantInfo merchantInfo = null;
        RegUser regUser = null;
        JedisClusterLock jedisLock = new JedisClusterLock();
        String lockKey = LOCK_PREFFIX + PointMerchantFacadeImpl.class.getSimpleName() + "_pointPayment_" + regUserId;
        try {
            //上redis锁防止并发
            if (!jedisLock.lock(lockKey, LOCK_EXPIRES, Constants.LOCK_WAITTIME)) {
                logger.error("pointPayment, 积分支付获取锁超时, 用户标识: {}, 支付金额: {}, 商户号: {}, 支付来源: {}", regUserId,
                        money, merchantCode, platformSourceEnums);
                return new ResponseEntity(ERROR, "积分支付超时，请重新操作！");
            }
            //获取积分账户
            PointAccount pointAccount = pointAccountService.findPointAccountByRegUserId(regUserId);
            if (pointAccount == null){
                return new ResponseEntity(ERROR,"用户积分账户异常！");
            }
            //1.判断用户支付密码是否正确
            ResponseEntity judgeResult = finAccountService.judgePayPassword(regUserId,payPass);
            if (judgeResult.getResStatus() != SUCCESS){
                return judgeResult;
            }
            //2.判断商户信息是否正确
            merchantInfo = pointMerchantInfoService.getMerchantInfoByCode(merchantCode);
            if (merchantInfo == null){
                return new ResponseEntity(ERROR,"商户信息不存在！");
            }
            //3.判断积分规则是否启用，换算的积分值是否大于0
            PointRule pointRule = pointRuleService.getCurrentOnUseRule();
            if (pointRule == null) {
                logger.error("pointPayment, 积分支付无启用的积分规则, 用户标识: {}, 支付金额: {}, 商户号: {}, 支付来源: {}", regUserId,
                        money, merchantCode, platformSourceEnums);
                return new ResponseEntity(ERROR, "无启用的积分规则，请联系客服人员！");
            }
            int point = PointUtils.moneyToPoint(money,pointRule.getPerMoneyToPoint());
            if (point < 1){
                return new ResponseEntity(ERROR,"换算的积分值应大于0！");
            }
            //4.判断用户积分是否足够
            if (pointAccount.getPoint() < point){
                return new ResponseEntity(ERROR,"用户积分不足！");
            }
            //5.积分支付用户减少积分
            PointAccount account = new PointAccount();
            account.setRegUserId(regUserId);
            account.setPoint(point*-1);
            pointAccountService.updateByRegUserId(account);
            //6.添加积分支付划转记录
            PointRecord pointRecord = new PointRecord();
            pointRecord.setType(PointConstants.POINT_TYPE_PAY);
            pointRecord.setRegUserId(regUserId);
            pointRecord.setWorth(PointUtils.pointToMoney(point,pointRule.getPerMoneyToPoint()));
            pointRecord.setRealWorth(money);
            pointRecord.setPoint(point*-1);
            pointRecord.setBusinessId(merchantInfo.getId());
            pointRecord.setComments("积分支付给" + merchantInfo.getMerchantName());
            pointRecord.setCreateTime(new Date());
            pointRecord.setModifyTime(new Date());

            pointRecordService.insertPointRecord(pointRecord);
            //7.商户增加账户金额
            int transferUserId = UserConstants.PLATFORM_ACCOUNT_ID;
            FinTradeFlow tradeFlow = FinTFUtil.initFinTradeFlow(merchantInfo.getRegUserId(),pointRecord.getId(),money,
                    TradeTransferConstants.TRADE_TYPE_POINT_PAY,platformSourceEnums);
            List<FinFundtransfer> transfersList = new ArrayList<>();
            //当前用户收入红包资金划转流水
            FinFundtransfer userFundtransfer = FinTFUtil.initFinFundtransfer(tradeFlow.getFlowId(),merchantInfo.getRegUserId(), transferUserId,
                    money,TradeTransferConstants.getFundTransferSubCodeByType
                            (FundtransferBigTypeStateEnum.INCOME, FundtransferSmallTypeStateEnum.POINT_MONEY));

            //平台或个人支出冻结资金划转
            FinFundtransfer transferUserFundtransfer = FinTFUtil.initFinFundtransfer(tradeFlow.getFlowId(),
                    transferUserId,merchantInfo.getRegUserId(),money,TRANSFER_SUB_CODE_PAY);
            transfersList.add(userFundtransfer);
            transfersList.add(transferUserFundtransfer);
            finConsumptionService.updateAccountInsertTradeAndTransfer(tradeFlow,transfersList);
            //组装返回的数据
            param.put("point",point);
            param.put("merchantName",merchantInfo.getMerchantName());
            param.put("createTime",pointRecord.getCreateTime());
        } catch (Exception e){
            logger.error("pointPayment, 积分支付异常, 用户标识: {}, 支付金额: {}, 商户号: {}, 支付来源: {}, 异常信息: ",
                    regUserId, money, merchantCode, platformSourceEnums, e);
            throw new GeneralException("积分支付异常，请联系客服人员！");
        } finally {
            jedisLock.freeLock(lockKey);
        }

        try{
            //插入成长值记录
            VasVipGrowRecordMqVO record = new VasVipGrowRecordMqVO();
            record.setUserId(regUserId);
            record.setGrowType(VasVipConstants.VAS_VIP_GROW_TYPE_POINT_PAYMENT);
            VipGrowRecordUtil.sendVipGrowRecordToQueue(record);
        }catch (Exception e){
            logger.error("pointPayment, 积分支付插入成长值异常, 用户标识: {}, 支付金额: {}, 商户号: {}, 支付来源: {}, 异常信息: ",
                    regUserId, money, merchantCode, platformSourceEnums, e);
        }

        try{
            //发送站内信息和app推送消息
            String tel = regUser.getLogin().toString().substring(0, 3) + "****" + regUser.getLogin().toString().substring(7);
            String userName = regUserDetailService.findRegUserDetailNameByRegUserId(regUserId);
            userName = "*" + userName.substring(1);
            List<Object> list = new ArrayList<>();
            list.add(tel);
            list.add(userName);
            list.add(money);
            //商户积分收款站内信
            SmsWebMsg smsWebMsg = new SmsWebMsg(merchantInfo.getRegUserId(), SmsMsgTemplate
                    .MSG_MERCHANT_POINT_PAYMENT_SUCCESS.getTitle(),SmsMsgTemplate.MSG_MERCHANT_POINT_PAYMENT_SUCCESS
                    .getMsg(), SmsConstants.SMS_TYPE_NOTICE,list.toArray());
            //商户积分收款消息推送
            SmsAppMsgPush smsAppMsgPush = new SmsAppMsgPush(merchantInfo.getRegUserId(),SmsMsgTemplate
                    .MSG_MERCHANT_POINT_PAYMENT_SUCCESS.getTitle(),SmsMsgTemplate.MSG_MERCHANT_POINT_PAYMENT_SUCCESS
                    .getMsg(),list.toArray());
            smsAppMsgPush.setType(SMS_APP_MSG_TYPE_MERCHANT);
            SmsSendUtil.sendSmsMsgToQueue(smsWebMsg,smsAppMsgPush);
        }catch (Exception e){
            logger.error("pointPayment, 积分支付发送消息异常, 用户标识: {}, 支付金额: {}, 商户号: {}, 支付来源: {}, 异常信息: ",
                    regUserId, money, merchantCode, platformSourceEnums, e);
        }
        return new ResponseEntity(SUCCESS, "积分支付成功！",param);
    }
}
