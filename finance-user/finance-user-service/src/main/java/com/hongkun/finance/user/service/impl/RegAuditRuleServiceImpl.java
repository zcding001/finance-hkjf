package com.hongkun.finance.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.user.dao.RegAuditRuleDao;
import com.hongkun.finance.user.model.RegAuditRule;
import com.hongkun.finance.user.service.RegAuditRuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.user.service.impl.RegAuditRuleServiceImpl.java
 * @Class Name    : RegAuditRuleServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class RegAuditRuleServiceImpl implements RegAuditRuleService {

	private static final Logger logger = LoggerFactory.getLogger(RegAuditRuleServiceImpl.class);
	
	/**
	 * RegAuditRuleDAO
	 */
	@Autowired
	private RegAuditRuleDao regAuditRuleDao;
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertRegAuditRule(RegAuditRule regAuditRule) {
		this.regAuditRuleDao.save(regAuditRule);
	}	
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateRegAuditRule(RegAuditRule regAuditRule) {
		this.regAuditRuleDao.update(regAuditRule);
	}
	
	@Override
	public RegAuditRule findRegAuditRuleById(int id) {
		return this.regAuditRuleDao.findByPK(Long.valueOf(id), RegAuditRule.class);
	}
	
	@Override
	public List<RegAuditRule> findRegAuditRuleList(RegAuditRule regAuditRule) {
		return this.regAuditRuleDao.findByCondition(regAuditRule);
	}
}
