package com.hongkun.finance.payment.dao;

import java.math.BigDecimal;
import java.util.List;

import com.hongkun.finance.payment.model.FinFundtransfer;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.payment.dao.FinFundtransferDao.java
 * @Class Name : FinFundtransferDao.java
 * @Description : GENERATOR DAO类
 * @Author : generator
 */
public interface FinFundtransferDao extends MyBatisBaseDao<FinFundtransfer, java.lang.Long> {

	/**
	 * @Description : 通过flowID,更新资金划转信息
	 * @Method_Name : updateByFlowId;
	 * @param finFundtransferList
	 * @param count
	 * @return
	 * @return : int;
	 * @Creation Date : 2017年7月12日 下午3:25:56;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	int updateByFlowId(List<FinFundtransfer> finFundtransferList, Integer count);

	/**
	 * @Description : 通过FLOWID，删除资金划转
	 * @Method_Name : delteByFlowId;
	 * @param finFundtransfer
	 * @return
	 * @return : int;
	 * @Creation Date : 2017年7月13日 上午9:20:36;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	int deleteByFlowId(FinFundtransfer finFundtransfer);

	/**
	 * @Description : 根据FLOWID，批量删除资金划转
	 * @Method_Name : deleteByFlowIdBatch;
	 * @param flowIdList
	 * @return
	 * @return : int;
	 * @Creation Date : 2017年7月13日 上午9:21:40;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	int deleteByFlowIdBatch(List<String> flowIdList);

	/**
	 * @Description : 根据条件查询资金划转总金额
	 * @Method_Name : findFintransferSumMoney;
	 * @param finFundtransfe
	 * @return
	 * @return : BigDecimal;
	 * @Creation Date : 2017年7月18日 下午6:06:07;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	BigDecimal findFintransferSumMoney(FinFundtransfer finFundtransfe);

	/**
	 * 
	 * @Description : 通过PflowId 删除资金划转
	 * @Method_Name : deleteByPflowId
	 * @param finFundtransfer
	 * @return : void
	 * @Creation Date : 2017年7月28日 上午10:19:54
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	void deleteByPflowId(String PflowId);

	/**
	 * @Description : 查询资金划转信息，通过flowId
	 * @Method_Name : findTransferByFlowId;
	 * @param flowId
	 *            资金划转流水ID
	 * @return
	 * @return : FinFundtransfer;
	 * @Creation Date : 2018年4月25日 上午10:54:46;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	FinFundtransfer findTransferByFlowId(String flowId);
	/**
	*  @Description    ：查询昨日账户有变动的用户集合
	*  @Method_Name    ：findRegUserIdListYestoday
	*
	*  @return java.util.List<java.lang.Integer>
	*  @Creation Date  ：2018/5/2
	*  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
	*/
    List<Integer> findRegUserIdListYestoday();
}
