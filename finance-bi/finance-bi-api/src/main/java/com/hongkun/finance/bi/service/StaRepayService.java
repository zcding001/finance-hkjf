package com.hongkun.finance.bi.service;

import java.util.List;

import com.yirun.framework.core.utils.pager.Pager;
import com.hongkun.finance.bi.model.StaRepay;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.bi.service.StaRepayService.java
 * @Class Name    : StaRepayService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface StaRepayService {
    
	/**
	 * @Described			: 条件检索数据
	 * @param staRepay 检索条件
	 * @return	List<StaRepay>
	 */
	List<StaRepay> findStaRepayList(StaRepay staRepay);
	
	/**
	 * @Described			: 条件检索数据
	 * @param staRepay 检索条件
	 * @param pager	分页数据
	 * @return	List<StaRepay>
	 */
	Pager findStaRepayList(StaRepay staRepay, Pager pager);

	/**
	*  还款累计查询
	*  @Method_Name             ：findStaRepayAddup
	*  @param staRepay
	*  @return com.hongkun.finance.bi.model.StaRepay
	*  @Creation Date           ：2018/9/20
	*  @Author                  ：zc.ding@foxmail.com
	*/
	StaRepay findStaRepayAddup(StaRepay staRepay);

	/**
	*  查询未来还款计划统计
	*  @Method_Name             ：findStaRepayFuture
	* 
	*  @return com.hongkun.finance.bi.model.StaRepay
	*  @Creation Date           ：2018/9/20
	*  @Author                  ：zc.ding@foxmail.com
	*/
    StaRepay findStaRepayFuture();
}
