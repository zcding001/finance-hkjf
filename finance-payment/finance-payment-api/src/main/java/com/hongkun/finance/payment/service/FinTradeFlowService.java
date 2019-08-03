package com.hongkun.finance.payment.service;

import java.util.List;
import java.util.Set;

import com.hongkun.finance.payment.model.FinTradeFlow;
import com.hongkun.finance.payment.model.vo.PaymentVO;
import com.yirun.framework.core.utils.pager.Pager;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.payment.service.FinTradeFlowService.java
 * @Class Name : FinTradeFlowService.java
 * @Description : GENERATOR SERVICE类
 * @Author : generator
 */
public interface FinTradeFlowService {

	/**
	 * @Described : 单条插入
	 * @param finTradeFlow
	 *            持久化的数据对象
	 * @return : void
	 */
	int insert(FinTradeFlow finTradeFlow);

	/**
	 * @Described : 批量插入
	 * @param List<FinTradeFlow>
	 *            批量插入的数据
	 * @return : void
	 */
	void insertBatch(List<FinTradeFlow> list);

	/**
	 * @Described : 更新数据
	 * @param finTradeFlow
	 *            要更新的数据
	 * @return : void
	 */
	int update(FinTradeFlow finTradeFlow);

	/**
	 * @Described : 通过id查询数据
	 * @param id
	 *            id值
	 * @return FinTradeFlow
	 */
	FinTradeFlow findById(int id);

	/**
	 * @Described : 条件检索数据
	 * @param finTradeFlow
	 *            检索条件
	 * @return List<FinTradeFlow>
	 */
	List<FinTradeFlow> findByCondition(FinTradeFlow finTradeFlow);

	/**
	 * @Described : 条件检索数据
	 * @param finTradeFlow
	 *            检索条件
	 * @param start
	 *            起始页
	 * @param limit
	 *            检索条数
	 * @return List<FinTradeFlow>
	 */
	List<FinTradeFlow> findByCondition(FinTradeFlow finTradeFlow, int start, int limit);

	/**
	 * @Described : 条件检索数据
	 * @param finTradeFlow
	 *            检索条件
	 * @param pager
	 *            分页数据
	 * @return List<FinTradeFlow>
	 */
	Pager findByCondition(FinTradeFlow finTradeFlow, Pager pager);

	/**
	 * @Described : 通过flowId查询数据
	 * @param flowId
	 *            flowId值
	 * @return FinTradeFlow
	 */
	FinTradeFlow findByFlowId(String flowId);

	/**
	 * @Description : 通过FLOW ID，更新流水信息
	 * @Method_Name : updateByFlowId;
	 * @param flowId
	 * @return
	 * @return : int;
	 * @Creation Date : 2017年7月12日 下午3:14:45;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	int updateByFlowId(FinTradeFlow finTradeFlow);

	/**
	 * @Description : 根据FLOWID，删除流水记录
	 * @Method_Name : deleteByFlowId;
	 * @param finTradeFlow
	 * @return
	 * @return : int;
	 * @Creation Date : 2017年7月13日 上午8:55:30;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	int deleteByFlowId(String finTradeFlowId);

	/**
	 * @Description : 根据FLOWID,批量删除流水信息
	 * @Method_Name : deleteBatchByFlowId;
	 * @param tradeFlowList
	 * @return
	 * @return : int;
	 * @Creation Date : 2017年7月13日 上午9:04:11;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	int deleteByFlowIdBatch(List<FinTradeFlow> tradeFlowList);

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
	 * @Description : 根据投资记录id查询用户投资冻结流水
	 * @Method_Name : findFreezeTradeFlow
	 * @param pFlowId  投资记录id
	 * @Creation Date : 2018年4月12日 上午10:37:33
	 * @Author : xuhui.liu@hongkun.com.cn 刘旭辉
	 */
	FinTradeFlow findFreezeTradeFlow(String pFlowId);
	/**
	 * @Description : 根据投资记录id集合查询用户投资冻结流水
	 * @Method_Name : findFreezeTradeFlow
	 * @param pFlowId  投资记录id
	 * @Creation Date : 2018年4月12日 上午10:37:33
	 * @Author : xuhui.liu@hongkun.com.cn 刘旭辉
	 */
	List<FinTradeFlow> findFreezeTradeFlowByPflowIds(List<Integer> bidInvestIds);

	/**
	 * @Description :根据交易类型和业务id查询流水
	 * @Method_Name : findByPflowIdAndTradeType
	 * @param pFlowId
	 * @param tradeType
	 * @Creation Date : 2018年4月12日 上午10:37:33
	 * @Author : xuhui.liu@hongkun.com.cn 刘旭辉
	 */
	FinTradeFlow findByPflowIdAndTradeType(String pFlowId, Integer tradeType);

	/**
	*  @Description    ：根据pflowid 和 tradeType 批量查询流水
	*  @Method_Name    ：findTradeFlowByPflowIds
	*  @param pflowIds
	*  @param tradeType
	*  @return java.util.List<com.hongkun.finance.payment.model.FinTradeFlow>
	*  @Creation Date  ：2018/4/27
	*  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
	*/
	List<FinTradeFlow> findTradeFlowByPflowIds(Set<Integer> pflowIds,Integer tradeType);
}
