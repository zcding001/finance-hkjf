package com.hongkun.finance.user.dao.impl;

import com.hongkun.finance.user.dao.RegUserFriendsDao;
import com.hongkun.finance.user.model.RegUserFriends;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.user.dao.impl.RegUserFriendsDaoImpl.java
 * @Class Name    : RegUserFriendsDaoImpl.java
 * @Description   : GENERATOR DAO实现类
 * @Author        : generator
 */
@Dao
public class RegUserFriendsDaoImpl extends MyBatisBaseDaoImpl<RegUserFriends, java.lang.Long> implements RegUserFriendsDao {

	@Override
	public List<RegUserFriends> findFirstFriendsByRecommendIds(List<Integer> rosIds) {
		return super.getCurSqlSessionTemplate().selectList(RegUserFriends.class.getName() + ".findFirstFriendsByRecommendIds", rosIds);
	}

	@Override
	public List<Integer> findRegUserFriendsNotInvestList(List<Integer> regUserIds) {
		return super.getCurSqlSessionTemplate().selectList(RegUserFriends.class.getName() + ".findRegUserFriendsNotInvestList", regUserIds);
	}

	@Override
	public void updateStateAndGroupIdForInvest(List<Integer> ids) {
		super.getCurSqlSessionTemplate().update(RegUserFriends.class.getName() + ".updateStateAndGroupIdForInvest", ids);
	}

	@Override
	public void delBatch(List<Integer> oldFriendsIds) {
		super.getCurSqlSessionTemplate().delete(RegUserFriends.class.getName() + ".delBatch", oldFriendsIds);
	}

	@Override
	public List<RegUserFriends> findUserFriendsUnGrouped(Integer recommendId) {
		return super.getCurSqlSessionTemplate().selectList(RegUserFriends.class.getName() + ".findUserFriendsUnGrouped", recommendId);
	}

	@Override
	public List<RegUserFriends> findUserFriendsByCondition(RegUserFriends regUserFriends) {
		return super.getCurSqlSessionTemplate().selectList(RegUserFriends.class.getName() + ".findUserFriendsByCondition", regUserFriends);
	}

	@Override
	public List<RegUserFriends> findRegUserFriendsAndGroup(RegUserFriends regUserFriend) {
		return super.getCurSqlSessionTemplate().selectList(RegUserFriends.class.getName() + ".findRegUserFriendsAndGroup", regUserFriend);
	}

	@Override
	public Map<Integer, RegUserFriends> findRecommendFriendsByRegUserIds(Set<Integer> regUserIds) {
		return super.getCurSqlSessionTemplate().selectMap(RegUserFriends.class.getName() +
				".findRecommendFriendsByRegUserIds",regUserIds,"regUserId");
	}
}
