package com.hongkun.finance.payment.dao;

import java.util.List;

import com.hongkun.finance.payment.model.FinPaymentRecord;
import com.hongkun.finance.payment.model.vo.PaymentVO;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.payment.dao.FinPaymentRecordDao.java
 * @Class Name : FinPaymentRecordDao.java
 * @Description : GENERATOR DAO类
 * @Author : generator
 */
public interface FinPaymentRecordDao extends MyBatisBaseDao<FinPaymentRecord, java.lang.Long> {
	/**
	 * @Description : 通过flowId查询支付表记录信息
	 * @Method_Name : findFinPaymentRecordByFlowId;
	 * @param flowId
	 *            支付记录表业务ID
	 * @return
	 * @return : FinPaymentRecord;
	 * @Creation Date : 2017年11月14日 下午3:51:07;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public FinPaymentRecord findFinPaymentRecordByFlowId(String flowId);

	/**
	 * @Description : 通过flowId,更新支付记录表信息
	 * @Method_Name : updateByFlowId;
	 * @param finPaymentRecord
	 *            支付记录表对象
	 * @return
	 * @return : int;
	 * @Creation Date : 2017年11月14日 下午3:51:53;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public int updateByFlowId(FinPaymentRecord finPaymentRecord);

	/**
	 * @Description : 后台功能与第三方对账结果查询
	 * @Method_Name : findPayCheckByCondition;
	 * @param regUserId
	 * @param finPaymentRecord
	 * @param pager
	 * @return
	 * @return : Pager;
	 * @Creation Date : 2017年11月22日 下午2:17:24;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public Pager findPayCheckByCondition(List<Integer> regUserId, FinPaymentRecord finPaymentRecord, Pager pager);
	/**
	 *  @Description    : 统计充值，提现的总人数，总次数，总金额
	 *  @Method_Name    : findPaymentSum;
	 *  @param finPaymentRecord
	 *  @return
	 *  @return         : PaymentVO;
	 *  @Creation Date  : 2018年9月21日 上午11:40:57;
	 *  @Author         : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	PaymentVO findPaymentSum(FinPaymentRecord finPaymentRecord);
}

