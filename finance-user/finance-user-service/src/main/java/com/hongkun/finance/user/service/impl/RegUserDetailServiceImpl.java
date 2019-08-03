package com.hongkun.finance.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.user.constants.UserConstants;
import com.hongkun.finance.user.dao.RegUserDao;
import com.hongkun.finance.user.dao.RegUserDetailDao;
import com.hongkun.finance.user.dao.RegUserInfoDao;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.model.RegUserDetail;
import com.hongkun.finance.user.model.RegUserInfo;
import com.hongkun.finance.user.model.vo.UserSimpleVo;
import com.hongkun.finance.user.model.vo.UserVO;
import com.hongkun.finance.user.service.RegUserDetailService;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.redis.JedisClusterUtils;
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
 * @Project : finance
 * @Program Name :
 *          com.hongkun.finance.user.service.impl.RegUserDetailServiceImpl.java
 * @Class Name : RegUserDetailServiceImpl.java
 * @Description : GENERATOR SERVICE实现类
 * @Author : generator
 */
@Service
public class RegUserDetailServiceImpl implements RegUserDetailService {

	private static final Logger logger = LoggerFactory.getLogger(RegUserDetailServiceImpl.class);

	/**
	 * RegUserDetailDAO
	 */
	@Autowired
	private RegUserDetailDao regUserDetailDao;
	@Autowired
	private RegUserDao regUserDao;
	@Autowired
	private RegUserInfoDao regUserInfoDao;

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public ResponseEntity<?> updateRegUserDetailForRealName(RegUserDetail regUserDetail, RegUserInfo regUserInfo,Integer state) {
		logger.info("updateRegUserDetailForRealName, 实名认证, 用户: {}, 用户信息(detail):  {}, 用户信息(info): {}, 实名状态: {}", regUserDetail.getRegUserId(), regUserDetail.toString(), regUserInfo.toString(), state);
		try {
			ResponseEntity<String> result = new ResponseEntity<>(ERROR);
			logger.info("第三方实名认证状态:{}", state);
			if (state == UserConstants.USER_REAL_NAME_YES) {
				this.regUserDetailDao.update(regUserDetail);
				RegUser regUser = new RegUser();
				regUser.setId(regUserDetail.getRegUserId());
				regUser.setIdentify(UserConstants.USER_IDENTIFY_YES);
				this.regUserDao.update(regUser);
				result.setResStatus(Constants.SUCCESS);
				result.setResMsg("认证成功");
				this.regUserInfoDao.update(regUserInfo);
				// 刷新缓存
				this.cacheUserData(regUserDetail);
			} else if (state == UserConstants.USER_REAL_NAME_NO) {
				result.setResMsg("姓名与身份证号码不一致，请核实后重新提交！");
			} else if (state == UserConstants.USER_REAL_NAME_NON) {
				result.setResMsg("无数据记录!");
			} else if (state == UserConstants.USER_REAL_NAME_ERROR) {
				result.setResMsg("网络异常，请您稍后重试！");
			}
			return result;
		} catch (Exception e) {
			logger.error("updateRegUserDetailForRealName, 实名认证, 用户: {}, 用户信息(detail): {}, 用户信息(info): {}\n", regUserDetail.getRegUserId(), regUserDetail.toString(), regUserInfo.toString(), e);
			throw new GeneralException("实名失败");
		}
	}

	@Override
	public RegUserDetail findRegUserDetailByRegUserId(int regUserId) {
		RegUserDetail regUserDetail = this.regUserDetailDao.findRegUserDetailByRegUserId(regUserId);
		if (regUserDetail!=null) {
			this.cacheUserData(regUserDetail);
		}
		return regUserDetail;
	}

	@Override
	public List<RegUserDetail> findRegUserDetailList(RegUserDetail regUserDetail) {
		return this.regUserDetailDao.findByCondition(regUserDetail);
	}

	@Override
	public String findRegUserDetailNameByRegUserId(int regUserId) {
		return this.regUserDetailDao.findRegUserDetailNameByRegUserId(regUserId);
	}

	@Override
	public Pager findRegUserDetailList(Pager pager, RegUserDetail regUserDetail) {
		return this.regUserDetailDao.findByCondition(regUserDetail, pager);
	}
	

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateRegUserDetailBatch(List<RegUserDetail> list, int count) {
		this.regUserDetailDao.updateBatch(RegUserDetail.class, list, count);
		list.forEach(e -> {
			JedisClusterUtils.delete(RegUser.class.getSimpleName() + e.getRegUserId());
			JedisClusterUtils.delete(RegUserDetail.class.getSimpleName() + e.getRegUserId());
			JedisClusterUtils.delete(RegUserInfo.class.getSimpleName() + e.getRegUserId());
		});
	}
	
	/**
	 *  @Description    : 缓存用户信息
	 *  @Method_Name    : cacheUserData
	 *  @param regUserDetail
	 *  @return         : void
	 *  @Creation Date  : 2017年10月16日 下午2:02:13 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	private void cacheUserData(RegUserDetail regUserDetail) {
		logger.info("为当前用户: {} 缓存用户信息",regUserDetail);
		Integer regUserId = regUserDetail.getRegUserId();
		// 缓存regUser
		RegUser regUser = this.regUserDao.findByPK(Long.valueOf(regUserId), RegUser.class);
		// 缓存regUserInfo
		RegUserInfo regUserInfo = this.regUserInfoDao.findRegUserInfoByRegUserId(regUserId);
		JedisClusterUtils.setAsJson(RegUser.class.getSimpleName() + regUserId, regUser);
		// 缓存regUserDetail
		JedisClusterUtils.setAsJson(RegUserDetail.class.getSimpleName() + regUserId, regUserDetail);
		// 缓存regUserInfo
		JedisClusterUtils.setAsJson(RegUserInfo.class.getSimpleName() + regUserId, regUserInfo);
	}

	@Override
	public RegUserDetail findRegUserDetailByGroupCode(String groupCode) {
		return this.regUserDetailDao.findRegUserDetailByGroupCode(groupCode);
	}
	
	@Override
	public RegUserDetail findRegUserDetailByInviteNo(String inviteNo) {
		return  this.regUserDetailDao.findRegUserDetailByInviteNo(inviteNo);
	}

	@Override
	public List<RegUser> findRegUserDetailListByBirthDay(Date currentDate) {
		return this.regUserDetailDao.findRegUserDetailListByBirthDay(currentDate);
	}
}
