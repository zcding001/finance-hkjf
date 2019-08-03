package com.hongkun.finance.payment.facade;

import java.util.List;

import com.hongkun.finance.payment.model.FinChannelGrant;
import com.hongkun.finance.payment.model.vo.PayCheckVo;
import com.hongkun.finance.payment.model.vo.PaymentRecordVo;
import com.hongkun.finance.payment.model.vo.PaymentVO;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;

/**
 * @Description : 支付后台功能 facade
 * @Project : finance-payment-api
 * @Program Name : com.hongkun.finance.payment.facade.FinPaymentConsoleFacade.java
 * @Author : yanbinghuang
 */
public interface FinPaymentConsoleFacade {
	/**
	 * @Description :后台对账查询
	 * @Method_Name : findPayReconliciation;
	 * @param payCheckCondition
	 *            查询条件VO
	 * @param pager
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年8月15日 下午2:59:18;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	ResponseEntity<?> findPayReconliciation(PayCheckVo payCheckCondition, Pager pager);

	/**
	 * @Description : 检索交易记录
	 * @Method_Name : findPaymentVoList
	 * @param paymentVO
	 * @param pager
	 * @return : Pager
	 * @Creation Date : 2017年9月19日 上午10:37:33
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	Pager findPaymentVoList(Pager pager, PaymentVO paymentVO);

	/**
	 * @Description : 查询交易记录详细信息
	 * @Method_Name : getPaymentVoById
	 * @Date : 2017/10/27 11:42
	 * @Author : ruilima@hongkun.com.cn 马瑞丽
	 * @param id
	 * @return
	 */
	PayCheckVo getPayRecord(Integer id);

	/**
	 * @Description : 充值提现交易记录查询
	 * @Method_Name : findTransRecords
	 * @Date : 2017/11/24 11:06
	 * @Author : ruilima@hongkun.com.cn 马瑞丽
	 * @param payCheckCondition
	 * @param pager
	 * @return
	 */
	ResponseEntity<?> findPayRecord(PayCheckVo payCheckCondition, Pager pager);

	/**
	 * @Description : 提现放款审核
	 * @Method_Name : loanWithdrawals
	 * @Date : 2017/10/30 13:59
	 * @Author : ruilima@hongkun.com.cn 马瑞丽
	 * @param id
	 * @return
	 */
	ResponseEntity<?> loanWithdrawals(List<Integer> ids);

	/**
	 * @Description : 添加支付渠道下的支付模式
	 * @Method_Name : insertPaymentChannel;
	 * @param finChannelGrant
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年11月2日 上午11:31:06;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	ResponseEntity<?> insertPaymentChannel(FinChannelGrant finChannelGrant);

	/**
	 * @Description : 更新支付渠道下的支付模式
	 * @Method_Name : updatePaymentChannel;
	 * @param finChannelGrant
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年11月2日 上午11:48:04;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	ResponseEntity<?> updatePaymentChannel(FinChannelGrant finChannelGrant);

	/**
	 * @Description : 定时提现
	 * @Method_Name : executeWithDrawFacade;
	 * @param startTime
	 *            提现记录条件查询开始时间 yyyy-MM-dd
	 * @param endTime
	 *            提现记录条件查询结束时间 yyyy-MM-dd hh:00:00 (必须控制到小时范围，否则达不到延时处理效果)
	 * @return
	 * @Creation Date : 2018年5月2日 13:56:31;
	 * @Author : binliang@hongkun.com.cn 梁彬;
	 */
	void executeWithDrawFacade(String startTime, String endTime);

	/**
	 * @Description : 提现放款审核拒绝
	 * @Method_Name : loanRejectWithdrawals
	 * @Date : 2017/10/31 10:37
	 * @Author : ruilima@hongkun.com.cn 马瑞丽
	 * @param ids
	 * @param rejectInfo
	 * @return
	 */
	ResponseEntity<?> loanRejectWithdrawals(List<Integer> ids, String rejectInfo);

	/**
	 * * @Description : 提现审核拒绝
	 *
	 * @Method_Name : auditRejectWithdrawals
	 * @Date : 2017/10/31 10:37
	 * @Author : ruilima@hongkun.com.cn 马瑞丽
	 * @param id
	 * @param rejectInfo
	 * @return
	 */
	ResponseEntity<?> auditRejectWithdrawals(Integer id, String rejectInfo);

	/**
	 * @Description : 查询提现充值统计信息
	 * @Method_Name : findPaymentRecordCountList;
	 * @param pager
	 * @param payCheckVo
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年6月13日 下午6:00:06;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	ResponseEntity<?> findPaymentRecordCountList(Pager pager, PayCheckVo payCheckVo);
	
	/**
	 *  @Description    : 查询支付充值提现统计信息
	 *  @Method_Name    : findPaymentCountList;
	 *  @param pager
	 *  @param paymentRecordVo
	 *  @return
	 *  @return         : ResponseEntity<?>;
	 *  @Creation Date  : 2018年9月19日 下午4:01:04;
	 *  @Author         : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	ResponseEntity<?> findPaymentCountList(Pager pager,PaymentRecordVo paymentRecordVo);
}
