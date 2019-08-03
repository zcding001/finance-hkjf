package com.hongkun.finance.user.dao;

import java.util.List;

import com.hongkun.finance.user.model.RegUserFriendsGroup;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.user.dao.RegUserFriendsGroupDao.java
 * @Class Name    : RegUserFriendsGroupDao.java
 * @Description   : GENERATOR DAO类
 * @Author        : generator
 */
public interface RegUserFriendsGroupDao extends MyBatisBaseDao<RegUserFriendsGroup, java.lang.Long> {

	List<RegUserFriendsGroup> findGroupsAndNumByUserId(Integer regUserId);
	
}
