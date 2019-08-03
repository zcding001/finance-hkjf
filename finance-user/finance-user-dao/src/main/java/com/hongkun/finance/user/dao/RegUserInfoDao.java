package com.hongkun.finance.user.dao;

import com.hongkun.finance.user.model.RegUserInfo;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.user.dao.RegUserInfoDao.java
 * @Class Name    : RegUserInfoDao.java
 * @Description   : GENERATOR DAO类
 * @Author        : generator
 */
public interface RegUserInfoDao extends MyBatisBaseDao<RegUserInfo, java.lang.Long> {
	
	/**
	 *  @Description    : 通过regUser的Id检索regUserInfo
	 *  @Method_Name    : findRegUserInfoByRegUserId
	 *  @param regUserId
	 *  @return
	 *  @return         : RegUserInfo
	 *  @Creation Date  : 2017年5月24日 上午11:48:36 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	RegUserInfo findRegUserInfoByRegUserId(int regUserId);

	/**
	 * 根据userid来删除userInfo
	 * @param userId
	 */
    void deleteUserInfoByUserId(Integer userId);

	/**
	 * 根据userId更新Userinfo
	 * @param regUserInfo
	 */
	void updateByRegUserId(RegUserInfo regUserInfo);
}
