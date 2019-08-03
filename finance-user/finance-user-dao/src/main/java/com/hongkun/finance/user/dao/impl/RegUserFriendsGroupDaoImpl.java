package com.hongkun.finance.user.dao.impl;

import com.hongkun.finance.user.model.RegUserFriendsGroup;

import java.util.List;

import com.hongkun.finance.user.dao.RegUserFriendsGroupDao;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.user.dao.impl.RegUserFriendsGroupDaoImpl.java
 * @Class Name    : RegUserFriendsGroupDaoImpl.java
 * @Description   : GENERATOR DAO实现类
 * @Author        : generator
 */
@Dao
public class RegUserFriendsGroupDaoImpl extends MyBatisBaseDaoImpl<RegUserFriendsGroup, java.lang.Long> implements RegUserFriendsGroupDao {

	@Override
	public List<RegUserFriendsGroup> findGroupsAndNumByUserId(Integer regUserId) {
		return super.getCurSqlSessionTemplate().selectList(RegUserFriendsGroup.class.getName()+".findGroupsAndNumByUserId", regUserId); 
	}

}
