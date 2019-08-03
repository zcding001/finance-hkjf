package com.hongkun.finance.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.user.dao.OauthUserDao;
import com.hongkun.finance.user.model.OauthUser;
import com.hongkun.finance.user.service.OauthUserService;
import com.yirun.framework.core.utils.pager.Pager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.user.service.impl.OauthUserServiceImpl.java
 * @Class Name    : OauthUserServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class OauthUserServiceImpl implements OauthUserService {

	private static final Logger logger = LoggerFactory.getLogger(OauthUserServiceImpl.class);
	
	/**
	 * OauthUserDAO
	 */
	@Autowired
	private OauthUserDao oauthUserDao;
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertOauthUser(OauthUser oauthUser) {
		this.oauthUserDao.save(oauthUser);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertOauthUserBatch(List<OauthUser> list) {
		this.oauthUserDao.insertBatch(OauthUser.class, list);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertOauthUserBatch(List<OauthUser> list, int count) {
		if(logger.isDebugEnabled()){
			logger.debug("default batch insert size is " + count);
		}
		this.oauthUserDao.insertBatch(OauthUser.class, list, count);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateOauthUser(OauthUser oauthUser) {
		this.oauthUserDao.update(oauthUser);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateOauthUserBatch(List<OauthUser> list, int count) {
		this.oauthUserDao.updateBatch(OauthUser.class, list, count);
	}
	
	@Override
	public OauthUser findOauthUserById(int id) {
		return this.oauthUserDao.findByPK(Long.valueOf(id), OauthUser.class);
	}
	
	@Override
	public List<OauthUser> findOauthUserList(OauthUser oauthUser) {
		return this.oauthUserDao.findByCondition(oauthUser);
	}
	
	@Override
	public List<OauthUser> findOauthUserList(OauthUser oauthUser, int start, int limit) {
		return this.oauthUserDao.findByCondition(oauthUser, start, limit);
	}
	
	@Override
	public Pager findOauthUserList(OauthUser oauthUser, Pager pager) {
		return this.oauthUserDao.findByCondition(oauthUser, pager);
	}
	
	@Override
	public int findOauthUserCount(OauthUser oauthUser){
		return this.oauthUserDao.getTotalCount(oauthUser);
	}
	
	@Override
	public Pager findOauthUserList(OauthUser oauthUser, Pager pager, String sqlName){
		return this.oauthUserDao.findByCondition(oauthUser, pager, sqlName);
	}
	
	@Override
	public Integer findOauthUserCount(OauthUser oauthUser, String sqlName){
		return this.oauthUserDao.getTotalCount(oauthUser, sqlName);
	}

	@Override
	public OauthUser findOauthUserByUserName(String userName) {
		return this.oauthUserDao.findOauthUserByUserName(userName);
	}
}
