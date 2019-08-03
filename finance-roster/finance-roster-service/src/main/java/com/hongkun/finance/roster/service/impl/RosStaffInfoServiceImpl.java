package com.hongkun.finance.roster.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.roster.dao.RosStaffInfoDao;
import com.hongkun.finance.roster.model.RosStaffInfo;
import com.hongkun.finance.roster.service.RosStaffInfoService;
import com.hongkun.finance.roster.vo.RosStaffInfoVo;
import com.yirun.framework.core.utils.pager.Pager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Project : finance
 * @Program Name :
 *          com.hongkun.finance.roster.service.impl.RosStaffInfoServiceImpl.java
 * @Class Name : RosStaffInfoServiceImpl.java
 * @Description : GENERATOR SERVICE实现类
 * @Author : generator
 */
@Service
public class RosStaffInfoServiceImpl implements RosStaffInfoService {

	private static final Logger logger = LoggerFactory.getLogger(RosStaffInfoServiceImpl.class);

	/**
	 * RosStaffInfoDAO
	 */
	@Autowired
	private RosStaffInfoDao rosStaffInfoDao;

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public int insertRosStaffInfo(RosStaffInfo rosStaffInfo) {
		return this.rosStaffInfoDao.save(rosStaffInfo);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertRosStaffInfoBatch(List<RosStaffInfo> list) {
		this.rosStaffInfoDao.insertBatch(RosStaffInfo.class, list);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertRosStaffInfoBatch(List<RosStaffInfo> list, int count) {
		if (logger.isDebugEnabled()) {
			logger.debug("default batch insert size is " + count);
		}
		this.rosStaffInfoDao.insertBatch(RosStaffInfo.class, list, count);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public int updateRosStaffInfo(RosStaffInfo rosStaffInfo) {
		return this.rosStaffInfoDao.update(rosStaffInfo);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateRosStaffInfoBatch(List<RosStaffInfo> list, int count) {
		this.rosStaffInfoDao.updateBatch(RosStaffInfo.class, list, count);
	}

	@Override
	public RosStaffInfo findRosStaffInfoById(int id) {
		return this.rosStaffInfoDao.findByPK(Long.valueOf(id), RosStaffInfo.class);
	}

	@Override
	public List<RosStaffInfo> findRosStaffInfoList(RosStaffInfo rosStaffInfo) {
		return this.rosStaffInfoDao.findByCondition(rosStaffInfo);
	}

	@Override
	public List<RosStaffInfo> findRosStaffInfoList(RosStaffInfo rosStaffInfo, int start, int limit) {
		return this.rosStaffInfoDao.findByCondition(rosStaffInfo, start, limit);
	}

	@Override
	public Pager findRosStaffInfoList(RosStaffInfo rosStaffInfo, Pager pager) {
		return this.rosStaffInfoDao.findByCondition(rosStaffInfo, pager);
	}

	@Override
	public int findRosStaffInfoCount(RosStaffInfo rosStaffInfo) {
		return this.rosStaffInfoDao.getTotalCount(rosStaffInfo);
	}

	@Override
	public Pager findRosStaffInfoList(RosStaffInfo rosStaffInfo, Pager pager, String sqlName) {
		return this.rosStaffInfoDao.findByCondition(rosStaffInfo, pager, sqlName);
	}

	@Override
	public Integer findRosStaffInfoCount(RosStaffInfo rosStaffInfo, String sqlName) {
		return this.rosStaffInfoDao.getTotalCount(rosStaffInfo, sqlName);
	}

	@Override
	public RosStaffInfo findRosStaffInfo(Integer regUserId, Integer type, Integer state,Integer recommendState) {
	    logger.info("黑白名单查询, 用户标识: {}, 类型: {}, 状态: {}", regUserId, type, state);
		return this.rosStaffInfoDao.findRosStaffInfo(regUserId, type, state,recommendState);
	}

	@Override
	public List<RosStaffInfoVo> findRosStaffInfoList(RosStaffInfoVo rosStaffInfoContidion) {
		return this.rosStaffInfoDao.findRosStaffInfoList(rosStaffInfoContidion);
	}

	@Override
	public List<Integer> findRosStaffInfoLists(List<Integer> regUserIdList, Integer type, Integer state) {
		return this.rosStaffInfoDao.findRosStaffInfoList(regUserIdList, type, state);
	}

	@Override
	public List<Integer> findRosStaffInfoByTypes(List<Integer> types) {
		return this.rosStaffInfoDao.findRosStaffInfoByTypes(types);
	}

}
