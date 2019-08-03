package com.hongkun.finance.vas.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.vas.constants.VasConstants;
import com.hongkun.finance.vas.dao.VasVipGradeStandardDao;
import com.hongkun.finance.vas.model.VasVipGradeStandard;
import com.hongkun.finance.vas.service.VasVipGradeStandardService;
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
 * @Program Name  : com.hongkun.finance.vas.service.impl.VasVipGradeStandardServiceImpl.java
 * @Class Name    : VasVipGradeStandardServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class VasVipGradeStandardServiceImpl implements VasVipGradeStandardService {

	private static final Logger logger = LoggerFactory.getLogger(VasVipGradeStandardServiceImpl.class);
	
	/**
	 * VasVipGradeStandardDAO
	 */
	@Autowired
	private VasVipGradeStandardDao vasVipGradeStandardDao;
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public int insertVasVipGradeStandard(VasVipGradeStandard vasVipGradeStandard) {
		try{
			return this.vasVipGradeStandardDao.save(vasVipGradeStandard);
		}catch (Exception e){
			logger.error("insertVasVipGradeStandard, 插入会员等级标准异常, 会员等级标准信息: {}, 异常信息: ",
					vasVipGradeStandard.toString(), e);
			throw new GeneralException("插入会员等级标准异常！");
		}
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public int updateVasVipGradeStandard(VasVipGradeStandard vasVipGradeStandard) {
		try{
			return this.vasVipGradeStandardDao.update(vasVipGradeStandard);
		}catch (Exception e){
			logger.error("insertVasVipGradeStandard, 更新会员等级标准异常, 会员等级标准信息: {}, 异常信息: ",
					vasVipGradeStandard.toString(), e);
			throw new GeneralException("更新会员等级标准异常！");
		}
	}

	@Override
	public VasVipGradeStandard findVasVipGradeStandardById(int id) {
		return this.vasVipGradeStandardDao.findByPK(Long.valueOf(id), VasVipGradeStandard.class);
	}
	
	@Override
	public List<VasVipGradeStandard> findVasVipGradeStandardList(VasVipGradeStandard vasVipGradeStandard) {
		return this.vasVipGradeStandardDao.findByCondition(vasVipGradeStandard);
	}

	@Override
	public Pager findVasVipGradeStandardList(VasVipGradeStandard vasVipGradeStandard, Pager pager) {
		return this.vasVipGradeStandardDao.findByCondition(vasVipGradeStandard, pager);
	}

	@Override
	public VasVipGradeStandard findVasVipGradeStandardByLevel(int level) {
		return this.vasVipGradeStandardDao.findVasVipGradeStandardByLevel(level);
	}

	@Override
	public Integer findLevelByGrowValue(Integer growValue) {
		return this.vasVipGradeStandardDao.findLevelByGrowValue(growValue);
	}

	@Override
	public ResponseEntity addVipGradeStandard(VasVipGradeStandard vasVipGradeStandard) {
		try{
			//初始状态为启用
			vasVipGradeStandard.setState(VasConstants.VAS_STATE_Y);
			if (this.vasVipGradeStandardDao.save(vasVipGradeStandard) > 0){
				return new ResponseEntity(SUCCESS,"添加会员等级标准记录成功");
			}
		}catch (Exception e){
			logger.error("addVipGradeStandard, 添加会员等级标准异常, 会员等级标准信息: {}, 异常信息: ",
					vasVipGradeStandard.toString(), e);
			throw new GeneralException("添加会员等级标准异常！");
		}
        return new ResponseEntity(ERROR,"添加会员等级标准记录失败");
	}

	@Override
	public ResponseEntity updateVipGradeStandard(VasVipGradeStandard vasVipGradeStandard) {
		try{
			if (vasVipGradeStandard == null || vasVipGradeStandard.getId() == null ||
					this.findVasVipGradeStandardById(vasVipGradeStandard.getId()) == null){
				return new ResponseEntity(ERROR,"请选择正确的记录进行修改");
			}
			vasVipGradeStandard.setModifyTime(new Date());
			if(this.vasVipGradeStandardDao.update(vasVipGradeStandard) > 0){
				return new ResponseEntity(SUCCESS,"修改会员等级标准记录成功");
			}
		}catch (Exception e){
			logger.error("updateVipGradeStandard, 更新会员等级标准异常, 会员等级标准信息: {}, 异常信息: ",
					vasVipGradeStandard.toString(), e);
			throw new GeneralException("更新会员等级标准异常！");
		}
        return new ResponseEntity(ERROR,"修改会员等级标准记录失败");
	}

	@Override
	public List<VasVipGradeStandard> getAllGradeList() {
		VasVipGradeStandard condition = new VasVipGradeStandard();
		condition.setState(VasConstants.VAS_STATE_Y);
		condition.setSortColumns("level");
		return this.findVasVipGradeStandardList(condition);
	}
}
