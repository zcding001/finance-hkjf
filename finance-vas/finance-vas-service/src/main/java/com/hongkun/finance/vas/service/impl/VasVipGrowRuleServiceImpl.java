package com.hongkun.finance.vas.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.vas.constants.VasConstants;
import com.hongkun.finance.vas.dao.VasVipGrowRuleDao;
import com.hongkun.finance.vas.model.VasVipGrowRule;
import com.hongkun.finance.vas.service.VasVipGrowRuleService;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static com.yirun.framework.core.commons.Constants.ERROR;
import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.vas.service.impl.VasVipGrowRuleServiceImpl.java
 * @Class Name    : VasVipGrowRuleServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class VasVipGrowRuleServiceImpl implements VasVipGrowRuleService {

	private static final Logger logger = LoggerFactory.getLogger(VasVipGrowRuleServiceImpl.class);
	
	/**
	 * VasVipGrowRuleDAO
	 */
	@Autowired
	private VasVipGrowRuleDao vasVipGrowRuleDao;
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public int insertVasVipGrowRule(VasVipGrowRule vasVipGrowRule) {
		try{
			return this.vasVipGrowRuleDao.save(vasVipGrowRule);
		}catch (Exception e){
			logger.error("insertVasVipGrowRule, 添加会员等级成长值规则异常, 成长值规则信息: {}, 异常信息: ",
					vasVipGrowRule.toString(), e);
			throw new GeneralException("添加会员等级成长值规则异常！");
		}
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public int updateVasVipGrowRule(VasVipGrowRule vasVipGrowRule) {
		try{
			return this.vasVipGrowRuleDao.update(vasVipGrowRule);
		}catch (Exception e){
			logger.error("updateVipGrowRule, 更新会员等级成长值规则异常, 成长值规则信息: {}, 异常信息: ",
					vasVipGrowRule.toString(), e);
			throw new GeneralException("更新会员等级成长值规则异常！");
		}
	}
	
	@Override
	public VasVipGrowRule findVasVipGrowRuleById(int id) {
		return this.vasVipGrowRuleDao.findByPK(Long.valueOf(id), VasVipGrowRule.class);
	}
	
	@Override
	public List<VasVipGrowRule> findVasVipGrowRuleList(VasVipGrowRule vasVipGrowRule) {
		return this.vasVipGrowRuleDao.findByCondition(vasVipGrowRule);
	}

	@Override
	public Pager findVasVipGrowRuleList(VasVipGrowRule vasVipGrowRule, Pager pager) {
		return this.vasVipGrowRuleDao.findByCondition(vasVipGrowRule, pager);
	}

	@Override
	public Integer findVasVipGrowRuleCount(VasVipGrowRule growRule) {
		return this.vasVipGrowRuleDao.getTotalCount(growRule);
	}

	@Override
	public ResponseEntity addVipGrowRule(VasVipGrowRule vasVipGrowRule) {
		try {
			//初始状态为不启用
			vasVipGrowRule.setState(VasConstants.VAS_STATE_N);
			//同一类型的成长值规则适用用户注册时间范围不能重复，对时间进行判断
			if (vasVipGrowRuleDao.findVasVipGrowRuleTimeCount(vasVipGrowRule) > 0){
				return new ResponseEntity(ERROR,"适用用户注册时间范围冲突！");
			}
			if (this.vasVipGrowRuleDao.save(vasVipGrowRule) > 0){
				return new ResponseEntity(SUCCESS,"添加会员等级成长值规则记录成功");
			}
		}catch (Exception e){
			logger.error("addVipGrowRule, 添加会员等级成长值规则异常, 成长值规则信息: {}, 异常信息: ",
					vasVipGrowRule.toString(), e);
			throw new GeneralException("添加会员等级成长值规则异常！");
		}
        return new ResponseEntity(ERROR,"添加会员等级成长值规则记录失败");
	}

	@Override
	public ResponseEntity updateVipGrowRule(VasVipGrowRule vasVipGrowRule) {
		try{
			VasVipGrowRule growRule = this.findVasVipGrowRuleById(vasVipGrowRule.getId());
			if (vasVipGrowRule == null || vasVipGrowRule.getId() == null ||
					growRule == null){
				return new ResponseEntity(ERROR,"请选择正确的记录进行修改");
			}
			//同一类型的成长值规则适用用户注册时间范围不能重复，对时间进行判断
			vasVipGrowRule.setType(growRule.getType());
			if (vasVipGrowRuleDao.findVasVipGrowRuleTimeCount(vasVipGrowRule) > 0){
				return new ResponseEntity(ERROR,"适用用户注册时间范围冲突！");
			}
			vasVipGrowRule.setModifyTime(new Date());
			if (this.vasVipGrowRuleDao.update(vasVipGrowRule) > 0){
				return new ResponseEntity(SUCCESS,"修改会员成长值规则记录成功");
			}
		}catch (Exception e){
			logger.error("updateVipGrowRule, 更新会员等级成长值规则异常, 成长值规则信息: {}, 异常信息: ",
					vasVipGrowRule.toString(), e);
			throw new GeneralException("更新会员等级成长值规则异常！");
		}
        return new ResponseEntity(ERROR,"修改会员成长值规则记录失败");
	}

	@Override
	public List<VasVipGrowRule> getVipGrowRuleByRegistTime(Date registTime) {
		VasVipGrowRule vipGrowRule = new VasVipGrowRule();
		vipGrowRule.setState(VasConstants.VAS_STATE_Y);
		vipGrowRule.setRegistBeginTimeEnd(registTime);
		vipGrowRule.setRegistEndTimeBegin(registTime);
		return this.findVasVipGrowRuleList(vipGrowRule);
	}

	@Override
	public VasVipGrowRule getVipGrowRuleByTypeAndRegistTime(int type, Date registTime) {
		return this.vasVipGrowRuleDao.getVipGrowRuleByTypeAndRegistTime(type, registTime);
	}

}
