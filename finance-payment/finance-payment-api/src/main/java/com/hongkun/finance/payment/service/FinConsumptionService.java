package com.hongkun.finance.payment.service;

import com.alibaba.fastjson.JSONObject;
import com.hongkun.finance.payment.llpayvo.PayPlan;
import com.hongkun.finance.payment.model.FinAccount;
import com.hongkun.finance.payment.model.FinFundtransfer;
import com.hongkun.finance.payment.model.FinTradeFlow;
import com.hongkun.finance.payment.model.vo.RechargeCash;
import com.hongkun.finance.payment.model.vo.TransferVo;
import com.hongkun.finance.payment.model.vo.WithDrawCash;
import com.yirun.framework.core.enums.SystemTypeEnums;
import com.yirun.framework.core.model.ResponseEntity;
import org.mengyun.tcctransaction.api.Compensable;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.payment.service.FinConsumptionService.java
 * @Class Name : FinConsumptionService.java
 * @Description : 金融消费者类
 * @Author : yanbinghuang
 */
public interface FinConsumptionService {
	/**
	 * @Description : 现金消费接口:用于生成一条流水，一条资金划转，和根据transferSubCode更新账户金额
	 * @Method_Name : cashPay;
	 * @param finTradeFlow
	 *            交易流水对象
	 * @param transferSubCode
	 *            资金划转code TradeTransferConstants
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年4月17日 下午2:49:25;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@Compensable
	ResponseEntity<?> cashPay(FinTradeFlow finTradeFlow, Integer transferSubCode);

	/**
	 * @Description : 转账接口，用于两个账户之间转账操作，生成一条流水，两条资金划转，根据transferSubCode更新账户金额
	 * @Method_Name : transferPay;
	 * @param transferInfo
	 *            转账对象
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年4月17日 下午2:51:04;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@Compensable
	ResponseEntity<?> transferPay(TransferVo transferInfo);

	/***
	 * @Description : 用于提现放款操作
	 * @Method_Name : withdrawCash&#13;
	 * @param withDrawCashInfo
	 *            交易信息
	 * @return : ResponseEntity<?>&#13;
	 * @Creation Date : 2017年5月26日 上午9:13:33;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	ResponseEntity<?> withdrawCash(WithDrawCash withDrawCashInfo);

	/***
	 * @Description : 根据资金划转的subCode,更新用户账户信息，插入一条流水，多笔资金划转
	 * @Method_Name : updateAccountInsertTradeAndTransfer;
	 * @param tradeFlow
	 *            交易流水
	 * @param transfersList
	 *            资金划转List
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年12月13日 上午11:54:31;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@Compensable
	ResponseEntity<?> updateAccountInsertTradeAndTransfer(FinTradeFlow tradeFlow, List<FinFundtransfer> transfersList);

	/**
	 * @Description : 回滚 (根据资金划转的subCode,更新用户账户信息，插入一条流水，多笔资金划转的操作)
	 * @Method_Name : rollBackAccountAndTradeTransfer;
	 * @param tradeFlow
	 *            交易流水
	 * @param transfersList
	 *            资金划转List
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年12月13日 下午4:59:02;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	ResponseEntity<?> rollBackAccountAndTradeTransfer(FinTradeFlow tradeFlow, List<FinFundtransfer> transfersList);

	/**
	 * @Description : 生成一条流水多笔资金划转，不更新账户信息（目前只适用于体验金投资，放款）
	 * @Method_Name : batchInsertTradeAndTransfer;
	 * @param tradeFlow
	 *            交易流水
	 * @param transfersList
	 *            资金划转LIST
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年7月12日 下午2:36:00;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@Compensable
	ResponseEntity<?> batchInsertTradeAndTransfer(FinTradeFlow tradeFlow, List<FinFundtransfer> transfersList);

	/**
	 * 
	 * @Description : 连连签约接口
	 * @Method_Name : lianlianAgreeNoAuthApply
	 * @param repaymentNo
	 *            标的ID
	 * @param regUserId
	 *            用户ID
	 * @param noAgree
	 *            银行卡协议号
	 * @param repayInfo
	 *            还款信息
	 * @param systemType
	 *            系统类型
	 * @param platformSource
	 *            平台类型
	 * @return
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年6月28日 下午3:00:27
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	ResponseEntity<?> lianlianAgreeNoAuthApply(String repaymentNo, int regUserId, String noAgree, JSONObject repayInfo,
			String systemType, String platformSource) throws Exception;

	/**
	 * 
	 * @Description : 连连代扣借款
	 * @Method_Name : bankCardRepayment
	 * @param payPlan
	 * @return
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年6月28日 下午4:35:04
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	ResponseEntity<?> bankCardRepayment(PayPlan payPlan);

	/***
	 * 
	 * @Description : 回滚账户金额，删除流水资金划转
	 * @Method_Name : updateAccountAndDelFlows
	 * @return
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年7月28日 上午9:55:14
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	ResponseEntity<?> rollBackAccountAndDelFlows(FinAccount account, String flowId) throws Exception;

	/**
	 * 
	 * @Description : 充值接口
	 * @Method_Name : rechargeCash
	 * @param rechargeCash:充值对象
	 * @return
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年7月4日 下午5:42:06
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	ResponseEntity<?> rechargeCash(RechargeCash rechargeCash);

	/**
	 * 
	 * @Description : api方式确认充值接口(协议支付、易宝支付)
	 * @Method_Name : agreementPay
	 * @param rechargeCash:充值对象
	 * @return
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2018-05-12 16:55:27
	 * @Author : binliang@hongkun.com.cn 梁彬
	 */
	ResponseEntity<?> confirmPay(RechargeCash rechargeCash);

	/**
	 * 
	 * @Description : 支付发短验(预绑卡、预支付)
	 * @Method_Name : verificationCode
	 * @param rechargeCash:短信对象
	 * @return
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2018-05-14 16:55:27
	 * @Author : binliang@hongkun.com.cn 梁彬
	 */
	ResponseEntity<?> paymentVerificationCode(RechargeCash rechargeCash);
	
	/**
	 * 
	 * @Description : 易宝支付发短验(首次充值、重发短信)
	 * @Method_Name : yeepayVerificationCode
	 * @param rechargeCash:短信对象
	 * @return
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2018-09-26 15:54:27
	 * @Author : binliang@hongkun.com.cn 梁彬
	 */
	ResponseEntity<?> yeepayVerificationCode(RechargeCash rechargeCash);

	/**
	 * @Description : 提现审核
	 * @Method_Name : auditWithdrawals
	 * @Date : 2017/10/30 13:59
	 * @Author : ruilima@hongkun.com.cn 马瑞丽
	 * @param id
	 * @param systemTypeEnums
	 * @return
	 */
	ResponseEntity<?> auditWithdrawals(Integer id, SystemTypeEnums systemTypeEnums);

	/***
	 * 
	 * @Description : 钱袋子购买债转流水
	 * @Method_Name : dealCreditorMatchFlows;
	 * @param qdzRegUserId
	 * @param thirdRegUserId
	 * @param userTransMoney
	 * @param repairTime
	 * @param creditorFlag
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年4月20日 下午3:02:36;
	 * @Author : xinbangcao@hongkun.com.cn 曹新帮;
	 */
	@Compensable
	String dealCreditorMatchFlows(int qdzRegUserBiddInvestId, int qdzRegUserId, int thirdRegUserId,
			BigDecimal userTransMoney, Date repairTime, int creditorFlag);

	/**
	 * @Description : 根据资金划转SUBCODE，更新账户信息，插入资金划转对象
	 * @Method_Name : updateAccountInsertTransfer;
	 * @param tradeType
	 *            交易流水类型
	 * @param transfersList
	 *            资金划转LIST
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年4月19日 下午1:42:35;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	ResponseEntity<?> updateAccountInsertTransfer(Integer tradeType, List<FinFundtransfer> transfersList);

	/**
	 * @Description :现金消费，生成多笔流水，多笔资金划转，更新账户
	 * @Method_Name : cashPayBatch;
	 * @param finTradeFlowList
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年5月23日 下午6:50:12;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	ResponseEntity<?> cashPayBatch(List<FinTradeFlow> finTradeFlowList);

	/** 
	* @Description: 支付中间状态处理
	* @param flowId
	* @param payResult
	* @return: com.yirun.framework.core.model.ResponseEntity<?> 
	* @Author: hanghe@hongkunjinfu.com
	* @Date: 2018/12/10 19:41
	*/
	ResponseEntity<?> payWaiting(String flowId,Map<String, Object> payResult);
}
