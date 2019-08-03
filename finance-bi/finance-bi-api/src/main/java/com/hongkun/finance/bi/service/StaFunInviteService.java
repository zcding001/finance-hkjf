package com.hongkun.finance.bi.service;

import java.util.List;

import com.hongkun.finance.bi.model.StaFunInvite;
import com.yirun.framework.core.utils.pager.Pager;

/**
 * @Project       : finance
 * @Program Name  : com.yirun.finance.bi.service.StaFunInviteService.java
 * @Class Name    : StaFunInviteService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface StaFunInviteService {
	
	/**
	 * @Described			: 条件检索数据
	 * @param staFunInvite 检索条件
	 * @return	List<StaFunInvite>
	 */
	List<StaFunInvite> findStaFunInviteList(StaFunInvite staFunInvite);
	
	/**
	 * @Described			: 条件检索数据
	 * @param staFunInvite 检索条件
	 * @param pager	分页数据
	 * @return	List<StaFunInvite>
	 */
	Pager findStaFunInviteList(StaFunInvite staFunInvite, Pager pager);
	
	/**
	 * 自定义sql查询count
	 * @param staFunInvite
	 * @param pager
	 * @param sqlName
	 * @return
	 */
	Pager findStaFunInviteList(StaFunInvite staFunInvite, Pager pager, String sqlName/*添加一个自定义的sql的名字*/);
	
}
