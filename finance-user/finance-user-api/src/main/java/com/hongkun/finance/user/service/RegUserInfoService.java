package com.hongkun.finance.user.service;

import com.hongkun.finance.user.model.RegUserInfo;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.user.service.RegUserInfoService.java
 * @Class Name    : RegUserInfoService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface RegUserInfoService {
	
	/**
	 * @Described			: 更新数据
	 * @param regUserInfo 要更新的数据
	 * @return				: void
	 */
	void updateRegUserInfo(RegUserInfo regUserInfo);
	
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
}
