package com.hongkun.finance.roster.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.roster.constants.RosterFlag;
import com.hongkun.finance.roster.constants.RosterType;
import com.hongkun.finance.roster.dao.RosInfoDao;
import com.hongkun.finance.roster.model.RosInfo;
import com.hongkun.finance.roster.service.RosInfoService;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.utils.CommonUtils;
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
 * @Program Name  : com.hongkun.finance.roster.service.impl.RosInfoServiceImpl.java
 * @Class Name    : RosInfoServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class RosInfoServiceImpl implements RosInfoService {

	private static final Logger logger = LoggerFactory.getLogger(RosInfoServiceImpl.class);
	
	/**
	 * RosInfoDAO
	 */
	@Autowired
	private RosInfoDao rosInfoDao;
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertRosInfo(RosInfo rosInfo) {
		logger.info("insertRosInfo, 添加黑白名单. 名单信息: {}", rosInfo.toString());
		try {
			this.rosInfoDao.save(rosInfo);
		} catch (Exception e) {
			logger.error("insertRosInfo, 添加黑白名单. 名单信息: {}\n", rosInfo.toString(), e);
			throw new GeneralException("");
		}
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertRosInfoBatch(List<RosInfo> list) {
		this.rosInfoDao.insertBatch(RosInfo.class, list);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertRosInfoBatch(List<RosInfo> list, int count) {
		if(logger.isDebugEnabled()){
			logger.debug("default batch insert size is " + count);
		}
		this.rosInfoDao.insertBatch(RosInfo.class, list, count);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateRosInfo(RosInfo rosInfo) {
		this.rosInfoDao.update(rosInfo);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateRosInfoBatch(List<RosInfo> list, int count) {
		this.rosInfoDao.updateBatch(RosInfo.class, list, count);
	}
	
	@Override
	public RosInfo findRosInfoById(int id) {
		return this.rosInfoDao.findByPK(Long.valueOf(id), RosInfo.class);
	}
	
	@Override
	public List<RosInfo> findRosInfoList(RosInfo rosInfo) {
		return this.rosInfoDao.findByCondition(rosInfo);
	}
	
	@Override
	public List<RosInfo> findRosInfoList(RosInfo rosInfo, int start, int limit) {
		return this.rosInfoDao.findByCondition(rosInfo, start, limit);
	}
	
	@Override
	public Pager findRosInfoList(RosInfo rosInfo, Pager pager) {
		return this.rosInfoDao.findByCondition(rosInfo, pager);
	}
	
	@Override
	public int findRosInfoCount(RosInfo rosInfo){
		return this.rosInfoDao.getTotalCount(rosInfo);
	}
	
	@Override
	public Pager findRosInfoList(RosInfo rosInfo, Pager pager, String sqlName){
		return this.rosInfoDao.findByCondition(rosInfo, pager, sqlName);
	}
	
	@Override
	public Integer findRosInfoCount(RosInfo rosInfo, String sqlName){
		return this.rosInfoDao.getTotalCount(rosInfo, sqlName);
	}

	@Override
	public Integer delRosInfo(Integer id) {
		return this.rosInfoDao.delete(Long.valueOf(id), RosInfo.class);
	}
	
	@Override
	public boolean validateRoster(Integer regUserId, RosterType rosterType, RosterFlag rosterFlag) {
        return this.validDateRoster(regUserId, null, rosterType, rosterFlag);
	}

    @Override
    public boolean validateRoster(Long login, RosterType rosterType, RosterFlag rosterFlag) {
        return this.validDateRoster(null, login, rosterType, rosterFlag);
    }

    @Override
	public boolean validateSmsRoster(Integer regUserId, RosterType rosterType, RosterFlag rosterFlag) {
		RosInfo rosInfo = new RosInfo();
		rosInfo.setRegUserId(regUserId);
		rosInfo.setType(rosterType.getValue());
		rosInfo.setFlag(rosterFlag.getValue());
		return this.rosInfoDao.findRosInfoForSmsTel(rosInfo) > 0;
	}
	
	/**
	*  验证要添加的信息是否在黑白名单中存在
	*  @Method_Name             ：validDateRoster
	*  @param regUserId
	*  @param login
	*  @param rosterType
	*  @param rosterFlag
	*  @return boolean
	*  @Creation Date           ：2018/5/2
	*  @Author                  ：zhichaoding@hongkun.com.cn
	*/
	private boolean validDateRoster(Integer regUserId, Long login, RosterType rosterType, RosterFlag rosterFlag) {
        RosInfo rosInfo = new RosInfo();
        rosInfo.setRegUserId(regUserId);
        rosInfo.setLogin(login);
        rosInfo.setType(rosterType.getValue());
        rosInfo.setFlag(rosterFlag.getValue());
        return CommonUtils.isNotEmpty(this.rosInfoDao.findByCondition(rosInfo));
    }
}
