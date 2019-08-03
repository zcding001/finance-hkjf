package com.hongkun.finance.bi.service;

import java.util.List;
import java.util.Map;

import com.hongkun.finance.bi.model.StaFunProDis;
import com.yirun.framework.core.utils.pager.Pager;

/**
 * @Project       : finance
 * @Program Name  : com.yirun.finance.bi.service.StaFunProDisService.java
 * @Class Name    : StaFunProDisService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface StaFunProDisService {
	
	/**
	 * @Described			: 条件检索数据
	 * @param staFunProDis 检索条件
	 * @return	List<StaFunProDis>
	 */
	List<StaFunProDis> findStaFunProDisList(StaFunProDis staFunProDis);
	
	/**
	 * @Described			: 条件检索数据
	 * @param staFunProDis 检索条件
	 * @param pager	分页数据
	 * @return	List<StaFunProDis>
	 */
	Pager findStaFunProDisList(StaFunProDis staFunProDis, Pager pager);
	
	/**
	*  通过时间周期查询产品投资&还款信息
	*  @Method_Name             ：findStaFunProDisListByBidProType
	*  @param period            ：时间周期
	*  @param startTime         : da开始时间
	*  @param endTime           : da截止时间
	*  @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
	*  @Creation Date           ：2018/9/19
	*  @Author                  ：zc.ding@foxmail.com
	*/
	List<Map<String, Object>> findStaFunProDisListByBidProType(Integer period, String startTime, String endTime);
}
