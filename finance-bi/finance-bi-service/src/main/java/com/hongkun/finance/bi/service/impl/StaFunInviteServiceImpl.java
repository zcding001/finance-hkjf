package com.hongkun.finance.bi.service.impl;

import java.util.List;

import com.hongkun.finance.bi.service.StaFunInviteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yirun.framework.core.utils.pager.Pager;

import com.hongkun.finance.bi.model.StaFunInvite;

import com.alibaba.dubbo.config.annotation.Service;

import com.hongkun.finance.bi.dao.StaFunInviteDao;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.bi.service.impl.StaFunInviteServiceImpl.java
 * @Class Name    : StaFunInviteServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class StaFunInviteServiceImpl implements StaFunInviteService {

	private static final Logger logger = LoggerFactory.getLogger(StaFunInviteServiceImpl.class);
	
	/**
	 * StaFunInviteDAO
	 */
	@Autowired
	private StaFunInviteDao staFunInviteDao;
	
	@Override
	public List<StaFunInvite> findStaFunInviteList(StaFunInvite staFunInvite) {
		return this.staFunInviteDao.findByCondition(staFunInvite);
	}
	
	@Override
	public Pager findStaFunInviteList(StaFunInvite staFunInvite, Pager pager) {
		return this.staFunInviteDao.findByCondition(staFunInvite, pager);
	}
	
	@Override
	public Pager findStaFunInviteList(StaFunInvite staFunInvite, Pager pager, String sqlName){
		return this.staFunInviteDao.findByCondition(staFunInvite, pager, sqlName);
	}
}
