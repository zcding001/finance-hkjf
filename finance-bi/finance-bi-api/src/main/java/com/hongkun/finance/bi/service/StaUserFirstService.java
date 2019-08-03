package com.hongkun.finance.bi.service;

import java.util.List;

import com.hongkun.finance.bi.model.StaUserFirst;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;

/**
 * @Project       : finance
 * @Program Name  : com.yirun.finance.bi.service.StaUserFirstService.java
 * @Class Name    : StaUserFirstService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface StaUserFirstService {
    
	/**
	 * @Described			: 条件检索数据
	 * @param staUserFirst 检索条件
	 * @return	List<StaUserFirst>
	 */
	List<StaUserFirst> findStaUserFirstList(StaUserFirst staUserFirst);
	
	/**
	 * @Described			: 条件检索数据
	 * @param staUserFirst 检索条件
	 * @param start	起始页
	 * @param limit 检索条数
	 * @return	List<StaUserFirst>
	 */
	List<StaUserFirst> findStaUserFirstList(StaUserFirst staUserFirst, int start, int limit);
	
	/**
	 * @Described			: 条件检索数据
	 * @param staUserFirst 检索条件
	 * @param pager	分页数据
	 * @return	List<StaUserFirst>
	 */
	Pager findStaUserFirstList(StaUserFirst staUserFirst, Pager pager);
	
	/**
	 * 自定义sql查询count
	 * @param staUserFirst
	 * @param pager
	 * @param sqlName
	 * @return
	 */
	Pager findStaUserFirstList(StaUserFirst staUserFirst, Pager pager, String sqlName/*添加一个自定义的sql的名字*/);
	
	/**
	 * 
	 *  @Description    : 
	 *  @Method_Name    : getTotalCount
	 *  @param sqlName
	 *  @return
	 *  @return         : Integer
	 *  @Creation Date  : 2017年6月15日 上午10:14:01 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	Integer findStaUserFirstCount(StaUserFirst staUserFirst, String sqlName);
	
	/**
	*  查询用户转化率
	*  @Method_Name             ：findUserCvr
	*  @param period            ：时间周期 link PeriodEnum.java
	*  @param startTime
	*  @param endTime
	*  @return com.yirun.framework.core.model.ResponseEntity<?>
	*  @Creation Date           ：2018/9/17
	*  @Author                  ：zc.ding@foxmail.com
	*/
	ResponseEntity<?> findUserCvr(Integer period, String startTime, String endTime);
	
	/**
	*  查询指定时间周期内的用户统计
	*  @Method_Name             ：findStaUserFirstCount
	*  @param staUserFirst
	*  @return com.hongkun.finance.bi.model.StaUserFirst
	*  @Creation Date           ：2018/9/18
	*  @Author                  ：zc.ding@foxmail.com
	*/
	StaUserFirst findStaUserFirstCount(StaUserFirst staUserFirst);

}
