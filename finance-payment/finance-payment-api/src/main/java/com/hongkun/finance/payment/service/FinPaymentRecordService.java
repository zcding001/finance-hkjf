package com.hongkun.finance.payment.service;

import java.util.List;

import com.yirun.framework.core.utils.pager.Pager;
import com.hongkun.finance.payment.model.FinPaymentRecord;
import com.hongkun.finance.payment.model.vo.PaymentVO;

/**
 * @Project : finance
 * @Program Name :
 *          com.hongkun.finance.payment.service.FinPaymentRecordService.java
 * @Class Name : FinPaymentRecordService.java
 * @Description : GENERATOR SERVICE类
 * @Author : generator
 */
public interface FinPaymentRecordService {

	/**
	 * @Described : 单条插入 返回主键ID
	 * @param finPaymentRecord
	 *            持久化的数据对象
	 * @return : void
	 */
	Integer insertFinPaymentRecord(FinPaymentRecord finPaymentRecord);

	/**
	 * @Described : 批量插入
	 * @param List<FinPaymentRecord>
	 *            批量插入的数据
	 * @return : void
	 */
	void insertFinPaymentRecordBatch(List<FinPaymentRecord> list);

	/**
	 * @Described : 批量插入
	 * @param List<FinPaymentRecord>
	 *            批量插入的数据
	 * @param count
	 *            多少条数提交一次
	 * @return : void
	 */
	void insertFinPaymentRecordBatch(List<FinPaymentRecord> list, int count);

	/**
	 * @Described : 更新数据
	 * @param finPaymentRecord
	 *            要更新的数据
	 * @return : void
	 */
	void updateFinPaymentRecord(FinPaymentRecord finPaymentRecord);

	/**
	 * @Described : 批量更新数据
	 * @param finPaymentRecord
	 *            要更新的数据
	 * @param count
	 *            多少条数提交一次
	 * @return : void
	 */
	void updateFinPaymentRecordBatch(List<FinPaymentRecord> list, int count);

	/**
	 * @Described : 通过id查询数据
	 * @param id
	 *            id值
	 * @return FinPaymentRecord
	 */
	FinPaymentRecord findFinPaymentRecordById(int id);

	/**
	 * @Description : 通过FLOWID，查询支付记录信息
	 * @Method_Name : findFinPaymentRecordByFlowId;
	 * @param flowId
	 * @return
	 * @return : FinPaymentRecord;
	 * @Creation Date : 2017年11月14日 上午11:37:45;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	FinPaymentRecord findFinPaymentRecordByFlowId(String flowId);

	/**
	 * @Described : 条件检索数据
	 * @param finPaymentRecord
	 *            检索条件
	 * @return List<FinPaymentRecord>
	 */
	List<FinPaymentRecord> findFinPaymentRecordList(FinPaymentRecord finPaymentRecord);

	/**
	 * @Described : 条件检索数据
	 * @param finPaymentRecord
	 *            检索条件
	 * @param start
	 *            起始页
	 * @param limit
	 *            检索条数
	 * @return List<FinPaymentRecord>
	 */
	List<FinPaymentRecord> findFinPaymentRecordList(FinPaymentRecord finPaymentRecord, int start, int limit);

	/**
	 * @Described : 条件检索数据
	 * @param finPaymentRecord
	 *            检索条件
	 * @param pager
	 *            分页数据
	 * @return pager
	 */
	Pager findFinPaymentRecordList(FinPaymentRecord finPaymentRecord, Pager pager);

	/**
	 * @Described : 统计条数
	 * @param finPaymentRecord
	 *            检索条件
	 * @param pager
	 *            分页数据
	 * @return int
	 */
	int findFinPaymentRecordCount(FinPaymentRecord finPaymentRecord);

	/**
	 * @Description :通过flowID更新支付记录表信息
	 * @Method_Name : updateByFlowId;
	 * @param finPaymentRecord
	 * @return
	 * @return : int;
	 * @Creation Date : 2017年11月14日 上午11:49:05;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	int updateByFlowId(FinPaymentRecord finPaymentRecord);

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
