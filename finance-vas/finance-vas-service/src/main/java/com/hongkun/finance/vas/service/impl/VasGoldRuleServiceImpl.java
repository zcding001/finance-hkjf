package com.hongkun.finance.vas.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.vas.constants.VasConstants;
import com.hongkun.finance.vas.dao.VasGoldRuleDao;
import com.hongkun.finance.vas.model.VasGoldRule;
import com.hongkun.finance.vas.service.VasGoldRuleService;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * @Project : finance
 * @Program Name :
 *          com.hongkun.finance.vas.service.impl.VasGoldRuleServiceImpl.java
 * @Class Name : VasGoldRuleServiceImpl.java
 * @Description : GENERATOR SERVICE实现类
 * @Author : generator
 */
@Service
public class VasGoldRuleServiceImpl implements VasGoldRuleService {

	private static final Logger logger = LoggerFactory.getLogger(VasGoldRuleServiceImpl.class);

	/**
	 * VasGoldRuleDAO
	 */
	@Autowired
	private VasGoldRuleDao vasGoldRuleDao;

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public int insertVasGoldRule(VasGoldRule vasGoldRule) {
		logger.info("方法: insertVasGoldRule, 保存体验金规则, 入参: vasGoldRule: {}", vasGoldRule.toString());
		try {
			return this.vasGoldRuleDao.save(vasGoldRule);
		} catch (Exception e) {
			logger.error("保存体验金规则, 保存失败: ", e);
			throw new GeneralException("保存体验金规则失败!");
		}
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertVasGoldRuleBatch(List<VasGoldRule> list) {
		this.vasGoldRuleDao.insertBatch(VasGoldRule.class, list);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertVasGoldRuleBatch(List<VasGoldRule> list, int count) {
		if (logger.isDebugEnabled()) {
			logger.debug("default batch insert size is " + count);
		}
		this.vasGoldRuleDao.insertBatch(VasGoldRule.class, list, count);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public int updateVasGoldRule(VasGoldRule vasGoldRule) {
		logger.info("方法: updateVasGoldRule, 修改体验金规则, 入参: vasGoldRule: {}", vasGoldRule.toString());
		try {
			return this.vasGoldRuleDao.update(vasGoldRule);
		} catch (Exception e) {
			logger.error("修改体验金规则, 修改失败: ", e);
			throw new GeneralException("修改体验金规则失败!");
		}
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateVasGoldRuleBatch(List<VasGoldRule> list, int count) {
		this.vasGoldRuleDao.updateBatch(VasGoldRule.class, list, count);
	}

	@Override
	public VasGoldRule findVasGoldRuleById(int id) {
		return this.vasGoldRuleDao.findByPK(Long.valueOf(id), VasGoldRule.class);
	}

	@Override
	public List<VasGoldRule> findVasGoldRuleList(VasGoldRule vasGoldRule) {
		return this.vasGoldRuleDao.findByCondition(vasGoldRule);
	}

	@Override
	public List<VasGoldRule> findVasGoldRuleList(VasGoldRule vasGoldRule, int start, int limit) {
		return this.vasGoldRuleDao.findByCondition(vasGoldRule, start, limit);
	}

	@Override
	public Pager findVasGoldRuleList(VasGoldRule vasGoldRule, Pager pager) {
		return this.vasGoldRuleDao.findByCondition(vasGoldRule, pager);
	}

	@Override
	public int findVasGoldRuleCount(VasGoldRule vasGoldRule) {
		return this.vasGoldRuleDao.getTotalCount(vasGoldRule);
	}

	@Override
	public VasGoldRule findVasGoldRuleByTypeAndState(int type, int state) {
		return this.vasGoldRuleDao.findVasGoldRuleByTypeAndState(type, state);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public ResponseEntity<?> updateVasGoldRuleById(VasGoldRule vasGoldRule) {
		logger.info("方法: updateVasGoldRuleById, 更新体验金规则, 入参: vasGoldRule: {}", vasGoldRule.toString());
		try {
			// 当设置为有效时，需要将其它有效的都设置为失效保证当前只有一个有效状态规则
			if (vasGoldRule.getState() == VasConstants.VAS_RULE_STATE_START) {
				// 根据类型和状态查询体验金规则列表
				VasGoldRule goldRule = new VasGoldRule();
				goldRule.setType(vasGoldRule.getType());
				goldRule.setState(VasConstants.VAS_RULE_STATE_START);
				List<VasGoldRule> goldRuleList = vasGoldRuleDao.findByCondition(goldRule);
				// 遍历循环将启用状态的规则，设置为失效状态
				if (goldRuleList != null && goldRuleList.size() > 0) {
					List<VasGoldRule> newGoldRuleList = new ArrayList<VasGoldRule>();
					for (VasGoldRule vasRule : goldRuleList) {
						VasGoldRule newGoldRule = new VasGoldRule();
						newGoldRule.setId(vasRule.getId());
						newGoldRule.setState(VasConstants.VAS_RULE_STATE_INVALID);
						newGoldRuleList.add(newGoldRule);
					}
					vasGoldRuleDao.updateBatch(VasGoldRule.class, newGoldRuleList, newGoldRuleList.size());
				}
			}
			// 更新体验金规则
			vasGoldRuleDao.update(vasGoldRule);
			return new ResponseEntity<>(SUCCESS, "更新成功！");
		} catch (Exception e) {
			logger.error("更新体验金规则, 体验金规则标识: {}, 更新体验金规则失败: ", vasGoldRule.getId(), e);
			throw new GeneralException("体验金规则更新失败！");
		}
	}

	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public ResponseEntity<?> deleteVasGoldRuleState(VasGoldRule vasGoldRule) {
		logger.info("方法: deleteVasGoldRuleState, 逻缉删除体验金规则, 入参: vasGoldRule: {}", vasGoldRule.toString());
		try {
			vasGoldRule.setState(VasConstants.VAS_RULE_STATE_INVALID);// 逻缉删除状态
			vasGoldRuleDao.update(vasGoldRule);
			return new ResponseEntity<>(SUCCESS, "删除成功！");
		} catch (Exception e) {
			logger.error("逻缉删除体验金规则, 体验金规则标识: {}, 删除失败: ", vasGoldRule.getId(), e);
			throw new GeneralException("删除失败！");
		}
	}
}
