package com.hongkun.finance.user.dao.impl;

import com.hongkun.finance.user.dao.RegUserCommunityDao;
import com.hongkun.finance.user.model.RegUserCommunity;
import com.hongkun.finance.user.model.vo.RegUserCommunityVO;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;
import com.yirun.framework.redis.annotation.Cache;
import com.yirun.framework.redis.constants.CacheOperType;

import java.util.List;
import java.util.Map;

import static com.hongkun.finance.user.constants.UserConstants.COMMUNITY_TYPE_NOT_SELF_PICK_UP;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.user.dao.impl.RegUserCommunityDaoImpl.java
 * @Class Name    : RegUserCommunityDaoImpl.java
 * @Description   : GENERATOR DAO实现类
 * @Author        : generator
 */
@Dao
public class RegUserCommunityDaoImpl extends MyBatisBaseDaoImpl<RegUserCommunity, java.lang.Long> implements RegUserCommunityDao {

	private final String FIND_COMMUNITY_DICDATALIST = ".findCommunityDicDataList";
	private final String LOAD_OFFLINE_STORE_ADDRESS = ".loadOfflineStoreAddress";
	private final String FIND_COMMUNITY_LIST = ".findCommunityList";
	private final String DELECT_COMMUNITY_ONCASCADE = ".delectCommunityOnCascade";
	private final String FIND_COMMUNITY_AVAILABLE = ".findCommunityAvailable";
	private final String COMMUNITY_RELSHOULD_DELETE = ".communityRelShouldDelete";
	private final String BIND_COMMUNITY_TO_TENUMENT = ".bindCommunityToTenument";

	@Override
	public List<Map<String, Object>> findCommunityDicDataList(Integer regUserId) {
		return getCurSqlSessionTemplate().selectList(RegUserCommunity.class.getName()+ FIND_COMMUNITY_DICDATALIST,regUserId);
	}

	@Override
	public List<RegUserCommunityVO> loadOfflineStoreAddress() {
		return getCurSqlSessionTemplate().selectList(RegUserCommunity.class.getName()+ LOAD_OFFLINE_STORE_ADDRESS);
	}

	@Override
	public Pager findCommunityList(RegUserCommunityVO communityVO, Pager pager) {
		return findByCondition(communityVO, pager, RegUserCommunity.class, FIND_COMMUNITY_LIST);
	}

	@Override
	public boolean delectCommunityOnCascade(Integer id) {
		return getCurSqlSessionTemplate().delete(RegUserCommunity.class.getName()+DELECT_COMMUNITY_ONCASCADE, id)>0;
	}

	@Override
	public List<RegUserCommunity> findCommunityAvailable() {
		RegUserCommunity query = new RegUserCommunity();
		query.setCommunityType(COMMUNITY_TYPE_NOT_SELF_PICK_UP);/*非自提点*/
		query.setQueryColumnId("idAndName");
		return getCurSqlSessionTemplate().selectList(RegUserCommunity.class.getName() + FIND_COMMUNITY_AVAILABLE,query);
	}

	@Override
	@Cache(key="REGUSER_COMMUNITY_ALL", operType = CacheOperType.DELETE)
	public void unBindCommunity(List<Integer> communityRelShouldDelete) {
		getCurSqlSessionTemplate().update(RegUserCommunity.class.getName() + COMMUNITY_RELSHOULD_DELETE,communityRelShouldDelete);
		return ;
	}

	@Override
	@Cache(key="REGUSER_COMMUNITY_ALL", operType = CacheOperType.DELETE)
	public void bindCommunityToTenument(RegUserCommunityVO communityVO) {
		getCurSqlSessionTemplate().update(RegUserCommunity.class.getName() + BIND_COMMUNITY_TO_TENUMENT,communityVO);
		return ;
	}

	@Override
	@Cache(key="REGUSER_COMMUNITY_ALL", operType = CacheOperType.READ_WRITE)
	public List<RegUserCommunity> findAllCommunityList() {
		return getCurSqlSessionTemplate().selectList(RegUserCommunity.class.getName() + ".findAllCommunityList");
	}
}
