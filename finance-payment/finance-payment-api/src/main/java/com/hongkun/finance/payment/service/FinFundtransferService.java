package com.hongkun.finance.payment.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hongkun.finance.payment.model.FinFundtransfer;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.payment.service.FinFundtransferService.java
 * @Class Name : FinFundtransferService.java
 * @Description : GENERATOR SERVICE类
 * @Author : generator
 */
public interface FinFundtransferService {

	/**
	 * @Described : 单条插入
	 * @param finFundtransfer
	 *            持久化的数据对象
	 * @return : void
	 */
	int insert(FinFundtransfer finFundtransfer);

	/**
	 * @Described : 批量插入
	 * @param List<FinFundtransfer>
	 *            批量插入的数据
	 * @return : void
	 */
	void insertBatch(List<FinFundtransfer> list);

	/**
	 * @Described : 更新数据
	 * @param finFundtransfer
	 *            要更新的数据
	 * @return : void
	 */
	int update(FinFundtransfer finFundtransfer);

	/**
	 * @Described : 通过id查询数据
	 * @param id
	 *            id值
	 * @return FinFundtransfer
	 */
	FinFundtransfer findById(int id);

	/**
	 * @Described : 条件检索数据
	 * @param finFundtransfer
	 *            检索条件
	 * @return List<FinFundtransfer>
	 */
	List<FinFundtransfer> findByCondition(FinFundtransfer finFundtransfer);

	/**
	 * @Described : 条件检索数据
	 * @param finFundtransfer
	 *            检索条件
	 * @param start
	 *            起始页
	 * @param limit
	 *            检索条数
	 * @return List<FinFundtransfer>
	 */
	List<FinFundtransfer> findByCondition(FinFundtransfer finFundtransfer, int start, int limit);

	/**
	 * @Described : 条件检索数据
	 * @param finFundtransfer
	 *            检索条件
	 * @param pager
	 *            分页数据
	 * @return List<FinFundtransfer>
	 */
	Pager findByCondition(FinFundtransfer finFundtransfer, Pager pager);

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
	 * @param fundtransferList
	 * @return
	 * @return : int;
	 * @Creation Date : 2017年7月13日 上午9:21:40;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	int deleteByFlowIdBatch(List<FinFundtransfer> fundtransferList);

	/**
	 * @Description : 根据条件查询资金划转总金额
	 * @Method_Name : findFintransferSumMoney;
	 * @param finFundtransfer
	 * @return
	 * @return : BigDecimal;
	 * @Creation Date : 2017年7月18日 下午6:06:07;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	BigDecimal findFintransferSumMoney(FinFundtransfer finFundtransfer);

	/**
	 * @Description : 按条件查询资金划转的分页信息及收入总金额，总笔数
	 * @Method_Name : findPageAndIncomeTotalMoney;
	 * @param finFundtransfer
	 *            资金划转条件
	 * @param pager
	 * @param items
	 *            100：查询资金划转分页;110:查询资金划转分页，及资金划转总金额
	 *            ;111：查询资金划转分页，资金划转总金额，资金划转笔数; 010:查询资金划转总金额; 001：查询资金划转笔数;
	 * @return
	 * @return : Map<String,Object>;
	 * @Creation Date : 2018年3月23日 上午10:55:19;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	Map<String, Object> findPageAndIncomeTotalMoney(FinFundtransfer finFundtransfer, Pager pager, String items);
	/**
	*  @Description    ：查询收入统计需要的流水和资金划转
	*  @Method_Name    ：findParamsForStaIncome
	*  @param bidRepayIds  还款计划id集合
	*  @param beginMonth  当前月第一天
	*  @param endMonth    当前月最后一天
	*  @return java.util.Map<java.lang.String,java.lang.Object>
	*  @Creation Date  ：2018/4/28
	*  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
	*/
	Map<String,Object> findParamsForStaIncome(Set<Integer> bidRepayIds, Date beginMonth, Date endMonth);

	/**
	*  @Description    ：查询某用户出入账信息
	*  @Method_Name    ：findInAndOutMoneyByRegUserId
	*  @param regUserId
	*  @return com.yirun.framework.core.model.ResponseEntity<?>
	*  @Creation Date  ：2018/4/28
	*  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
	*/
    ResponseEntity<?> findInAndOutMoneyByRegUserId(Integer regUserId);
	/**
	*  @Description    ：查询昨天支出收入有变动的用户集合
	*  @Method_Name    ：findRegUserIdListYestoday
	*
	*  @return java.util.List<java.lang.Integer>
	*  @Creation Date  ：2018/5/2
	*  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
	*/
    List<Integer> findRegUserIdListYestoday();

}
