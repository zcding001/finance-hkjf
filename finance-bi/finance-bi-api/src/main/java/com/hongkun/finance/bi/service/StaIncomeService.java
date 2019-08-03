package com.hongkun.finance.bi.service;

import java.util.List;
import java.util.Map;

import com.yirun.framework.core.utils.pager.Pager;
import com.hongkun.finance.bi.model.StaIncome;

import javax.servlet.http.HttpServletResponse;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.bi.service.StaIncomeService.java
 * @Class Name    : StaIncomeService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface StaIncomeService {
	
	/**
	 * @Described			: 单条插入
	 * @param staIncome 持久化的数据对象
	 * @return				: void
	 */
	void insertStaIncome(StaIncome staIncome);
	
	/**
	 * @Described			: 批量插入
	 * @param List<StaIncome> 批量插入的数据
	 * @return				: void
	 */
	void insertStaIncomeBatch(List<StaIncome> list);
	
	/**
	 * @Described			: 批量插入
	 * @param List<StaIncome> 批量插入的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void insertStaIncomeBatch(List<StaIncome> list, int count);
	
	/**
	 * @Described			: 更新数据
	 * @param staIncome 要更新的数据
	 * @return				: void
	 */
	void updateStaIncome(StaIncome staIncome);
	
	/**
	 * @Described			: 批量更新数据
	 * @param staIncome 要更新的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void updateStaIncomeBatch(List<StaIncome> list, int count);
	
	/**
	 * @Described			: 通过id查询数据
	 * @param id id值
	 * @return	StaIncome
	 */
	StaIncome findStaIncomeById(int id);
	
	/**
	 * @Described			: 条件检索数据
	 * @param staIncome 检索条件
	 * @return	List<StaIncome>
	 */
	List<StaIncome> findStaIncomeList(StaIncome staIncome);
	
	/**
	 * @Described			: 条件检索数据
	 * @param staIncome 检索条件
	 * @param start	起始页
	 * @param limit 检索条数
	 * @return	List<StaIncome>
	 */
	List<StaIncome> findStaIncomeList(StaIncome staIncome, int start, int limit);
	
	/**
	 * @Described			: 条件检索数据
	 * @param staIncome 检索条件
	 * @param pager	分页数据
	 * @return	List<StaIncome>
	 */
	Pager findStaIncomeList(StaIncome staIncome, Pager pager);

	/**
	 * @Described			: 统计条数
	 * @param staIncome 检索条件
	 * @param pager	分页数据
	 * @return	int
	 */
	int findStaIncomeCount(StaIncome staIncome);
	
	/**
	 * 自定义sql查询count
	 * @param obj
	 * @param pager
	 * @param sqlName
	 * @return
	 */
	Pager findStaIncomeList(StaIncome staIncome, Pager pager, String sqlName/*添加一个自定义的sql的名字*/);
	
	/**
	 * 
	 *  @Description    : 
	 *  @Method_Name    : getTotalCount
	 *  @param object
	 *  @param sqlName
	 *  @return
	 *  @return         : Integer
	 *  @Creation Date  : 2017年6月15日 上午10:14:01 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	Integer findStaIncomeCount(StaIncome staIncome, String sqlName);

	/**
	*  @Description    ：查询总收入
	*  @Method_Name    ：findSumStaCharges
	*  @param staIncome
	*  @return java.util.Map<java.lang.String,java.lang.Object>
	*  @Creation Date  ：2018/6/6
	*  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
	*/
    Map<String,Object> findSumStaCharges(StaIncome staIncome);

    /**
    *  @Description    ：收入统计导出
    *  @Method_Name    ：exportExcelForStaIncomeList
    *  @param staIncome
    *  @param response
    *  @param exportMonth
    *  @return void
    *  @Creation Date  ：2018/6/7
    *  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
    */
    void exportExcelForStaIncomeList(StaIncome staIncome, HttpServletResponse response, String exportMonth);
}
