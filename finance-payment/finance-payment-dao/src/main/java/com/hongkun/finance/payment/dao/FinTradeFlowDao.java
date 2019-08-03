package com.hongkun.finance.payment.dao;

import java.util.List;
import java.util.Set;

import com.hongkun.finance.payment.model.FinTradeFlow;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.payment.dao.FinTradeFlowDao.java
 * @Class Name : FinTradeFlowDao.java
 * @Description : GENERATOR DAO类
 * @Author : generator
 */
public interface FinTradeFlowDao extends MyBatisBaseDao<FinTradeFlow, java.lang.Long> {
	/***
	 * @Description : 根据FLOWID查询流水对象信息
	 * @Method_Name : findByFlowId&#13;
	 * @param flowId
	 *            交易流水FLOWID
	 * @return FinTradeFlow
	 * @return : FinTradeFlow&#13;
	 * @Creation Date : 2017年5月31日 上午11:00:56 &#13;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵&#13;
	 */
	public FinTradeFlow findByFlowId(String flowId);

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
	 * @param flowIdList
	 * @return
	 * @return : int;
	 * @Creation Date : 2017年7月13日 上午9:04:11;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	int deleteByFlowIdBatch(List<String> flowIdList);
	/**
	 * @Description : 根据投资记录id查询用户投资冻结流水
	 * @Method_Name : findFreezeTradeFlow
	 * @param pFlowId  投资记录id
	 * @Creation Date : 2018年4月12日 上午10:37:33
	 * @Author : xuhui.liu@hongkun.com.cn 刘旭辉
	 */
	public FinTradeFlow findFreezeTradeFlow(String pFlowId);
	/**
	 * @Description : 根据投资记录id查询用户投资冻结流水
	 * @Method_Name : findFreezeTradeFlow
	 * @param pFlowId  投资记录id
	 * @Creation Date : 2018年4月12日 上午10:37:33
	 * @Author : xuhui.liu@hongkun.com.cn 刘旭辉
	 */
	public List<FinTradeFlow> findFreezeTradeFlowByPflowIds(List<Integer> bidInvestIds);
	/***
	 * 
	 *  @Description    : 根据pFlowId+tradeType返回唯一一条流水
	 *  @Method_Name    : findByFlowId;
	 *  @param pFlowId  : 业务Id
	 *  @param tradeType: 业务交易类型
	 *  @return
	 *  @return         : FinTradeFlow;
	 *  @Creation Date  : 2018年4月20日 下午3:28:23;
	 *  @Author         : xinbangcao@hongkun.com.cn 曹新帮
	 */
	public FinTradeFlow findByFlowId(String pFlowId,int tradeType);
	/**
	 * @Description : 根据pFlowId和tradeType查询唯一流水记录（两个参数必传）
	 * @Method_Name : findByPflowIdAndTradeType
	 * @param cdt
	 * @Creation Date : 2018年4月12日 上午10:37:33
	 * @Author : xuhui.liu@hongkun.com.cn 刘旭辉
	 */
    FinTradeFlow findByPflowIdAndTradeType(FinTradeFlow cdt);
	/**
	*  @Description    ：根据pflowId和tradeType 批量查询流水
	*  @Method_Name    ：findTradeFlowByPflowIds
	*  @param pflowIds
	*  @param tradeType
	*  @return java.util.List<com.hongkun.finance.payment.model.FinTradeFlow>
	*  @Creation Date  ：2018/4/27
	*  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
	*/
    List<FinTradeFlow> findTradeFlowByPflowIds(Set<Integer> pflowIds, Integer tradeType);
}
