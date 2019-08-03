package com.hongkun.finance.user.service;

import com.hongkun.finance.user.model.RegAuditRule;

import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.user.service.RegAuditRuleService.java
 * @Class Name    : RegAuditRuleService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface RegAuditRuleService {
	
	/**
	 * @Described			: 单条插入
	 * @param regAuditRule 持久化的数据对象
	 * @return				: void
	 */
	void insertRegAuditRule(RegAuditRule regAuditRule);
	
	/**
	 * @Described			: 更新数据
	 * @param regAuditRule 要更新的数据
	 * @return				: void
	 */
	void updateRegAuditRule(RegAuditRule regAuditRule);
	
	/**
	 * @Described			: 通过id查询数据
	 * @param id id值
	 * @return	RegAuditRule
	 */
	RegAuditRule findRegAuditRuleById(int id);
	
	/**
	 * @Described			: 条件检索数据
	 * @param regAuditRule 检索条件
	 * @return	List<RegAuditRule>
	 */
	List<RegAuditRule> findRegAuditRuleList(RegAuditRule regAuditRule);
	
}
