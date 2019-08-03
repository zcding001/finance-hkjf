package com.hongkun.finance.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.user.dao.RegUserDao;
import com.hongkun.finance.user.dao.RegUserDetailDao;
import com.hongkun.finance.user.dao.RegUserInfoDao;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.model.RegUserDetail;
import com.hongkun.finance.user.model.RegUserInfo;
import com.hongkun.finance.user.service.RegUserInfoService;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.redis.JedisClusterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.user.service.impl.RegUserInfoServiceImpl.java
 * @Class Name    : RegUserInfoServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class RegUserInfoServiceImpl implements RegUserInfoService {

	private static final Logger logger = LoggerFactory.getLogger(RegUserInfoServiceImpl.class);

	/**
	 * RegUserInfoDAO
	 */
	@Autowired
	private RegUserInfoDao regUserInfoDao;
	@Autowired
	private RegUserDao regUserDao;
	@Autowired
	private RegUserDetailDao regUserDetailDao;
	
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateRegUserInfo(RegUserInfo regUserInfo) {
		logger.info("updateRegUserInfo, 更新用户信息, 用户: {}, 用户信息: {}", regUserInfo.getRegUserId(), regUserInfo);
		try {
			this.regUserInfoDao.update(regUserInfo);
			this.clearCacheUserData(regUserInfo.getRegUserId());
		} catch (Exception e) {
			logger.error("updateRegUserInfo, 更新用户信息, 用户: {}, 用户信息: {}\n", regUserInfo.getRegUserId(), regUserInfo, e);
			throw new GeneralException("用户信息更新失败");
		}
	}
	
	@Override
	public RegUserInfo findRegUserInfoByRegUserId(int regUserId) {
		RegUserInfo regUserInfo = this.regUserInfoDao.findRegUserInfoByRegUserId(regUserId);
		this.cacheUserData(regUserInfo);
		return regUserInfo;
	}
	
	/**
	 *  @Description    : 缓存用户信息
	 *  @Method_Name    : cacheUserData
	 *  @param regUserInfo
	 *  @return         : void
	 *  @Creation Date  : 2017年10月16日 下午5:57:55 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	private void cacheUserData(RegUserInfo regUserInfo) {
		Integer regUserId = regUserInfo.getRegUserId();
		// 缓存regUser
		RegUser regUser = this.regUserDao.findByPK(Long.valueOf(regUserId), RegUser.class);
		// 缓存regUserInfo
		RegUserDetail regUserDetail = this.regUserDetailDao.findRegUserDetailByRegUserId(regUserId);
		JedisClusterUtils.setAsJson(RegUser.class.getSimpleName() + regUserId, regUser);
		// 缓存regUserDetail
		JedisClusterUtils.setAsJson(RegUserDetail.class.getSimpleName() + regUserId, regUserDetail);
		// 缓存regUserInfo
		JedisClusterUtils.setAsJson(RegUserInfo.class.getSimpleName() + regUserId, regUserInfo);
	}
	
	/**
	 *  @Description    : 清空缓存
	 *  @Method_Name    : clearCacheUserData
	 *  @param regUserId:
	 *  @return         : void
	 *  @Creation Date  : 2017年12月29日 下午4:50:46 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	private void clearCacheUserData(Integer regUserId) {
		JedisClusterUtils.delete(RegUser.class.getSimpleName() + regUserId);
		JedisClusterUtils.delete(RegUserDetail.class.getSimpleName() + regUserId);
		JedisClusterUtils.delete(RegUserInfo.class.getSimpleName() + regUserId);
	}
}
