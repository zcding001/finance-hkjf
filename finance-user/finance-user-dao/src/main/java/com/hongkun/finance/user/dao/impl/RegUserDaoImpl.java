package com.hongkun.finance.user.dao.impl;

import com.hongkun.finance.user.dao.RegUserDao;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.model.RegUserDetail;
import com.hongkun.finance.user.model.vo.UserSimpleVo;
import com.hongkun.finance.user.model.vo.UserVO;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

import java.util.*;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.user.dao.impl.RegUserDaoImpl.java
 * @Class Name : RegUserDaoImpl.java
 * @Description : GENERATOR DAO实现类
 * @Author : generator
 */
@Dao
public class RegUserDaoImpl extends MyBatisBaseDaoImpl<RegUser, java.lang.Long> implements RegUserDao {

	/**
	 * 搜寻菜单的分页数据
	 */
	public static final String FIND_USERID_BY_TEL = ".findUserIdsByTel";

	public static final String FIND_REG_USER_TEL_BY_ID = ".findRegUserTelAndRealNameById";

	public static final String LIST_CONDITION_PAGE = ".listConditionPage";

	public static final String FIND_USERWITHDETAIL_BY_ID = ".findUserWithDetailById";

	public static final String FIND_USERIDS_BY_FUZZYNAME = ".findUserIdsByFuzzyName";

	public static final String FIND_USERIDS_BY_USERVO = ".findUserIdsByUserVO";
	/**
	 * 通过条件查询用户信息
	 */
	public static final String FIND_USERWITHDETAIL_BY_INFO = ".findUserWithDetailByInfo";

	public static final String FIND_PROPERTY_DICDATA_LIST = ".findPropertyDicDataList";

	public static final String FIND_OPERATOR_COUNT = ".findOperatorCount";

	public static final String FIND_USER_MENU_BY_LOGIN = ".findUserMenuByLogin";

	public static final String FIND_USER_AUTH_ID_BY_MENUS = ".findUserAuthIdByMenus";

	@Override
	public List<Integer> findUserIdsByTel(Long userTel) {
		return getCurSqlSessionTemplate().selectList(RegUser.class.getName() + FIND_USERID_BY_TEL, userTel);
	}

	@Override
	public UserVO findRegUserTelAndRealNameById(Integer userId) {
		return getCurSqlSessionTemplate().selectOne(RegUser.class.getName() + FIND_REG_USER_TEL_BY_ID, userId);
	}

	@Override
	public Pager listConditionPage(UserVO userVO, Pager pager) {
		return this.findByCondition(userVO, pager, RegUser.class, LIST_CONDITION_PAGE);
	}

	@Override
	public UserVO findUserWithDetailById(Integer userId) {
		return getCurSqlSessionTemplate().selectOne(RegUser.class.getName() + FIND_USERWITHDETAIL_BY_ID, userId);
	}

	@Override
	public List<UserVO> findUserWithDetailByInfo(UserVO userVO) {
		return getCurSqlSessionTemplate().selectList(RegUser.class.getName() + FIND_USERWITHDETAIL_BY_INFO, userVO);
	}

	@Override
	public List<Integer> findUserIdsByFuzzyName(String userName) {
		List<Integer> userids = getCurSqlSessionTemplate()
				.selectList(RegUser.class.getName() + FIND_USERIDS_BY_FUZZYNAME, userName);
		return userids == null || userids.isEmpty() ? Arrays.asList(-1/*-1不可能是id*/) : userids;
	}

	@Override
	public List<Integer> findUserIdsByUserVO(UserVO userVO) {
		return getCurSqlSessionTemplate().selectList(RegUser.class.getName() + FIND_USERIDS_BY_USERVO, userVO);
	}

	@Override
	public List<Map<String, Object>> findPropertyDicDataList() {
		return getCurSqlSessionTemplate().selectList(RegUser.class.getName() + FIND_PROPERTY_DICDATA_LIST);

	}

	@Override
	public Long findOperatorCount(RegUser regUser) {
		return getCurSqlSessionTemplate().selectOne(RegUser.class.getName() + FIND_OPERATOR_COUNT, regUser);
	}

	@Override
	public List<Integer> findUserMenuByLogin(String login, Integer userType,Integer sysType) {
		return getCurSqlSessionTemplate().selectList(RegUser.class.getName() + FIND_USER_MENU_BY_LOGIN, new HashMap() {
			{
				put("login", login);
				put("userType", userType);
				put("sysType", sysType);
			}
		});
	}

	@Override
	public List<Integer> findUserAuthIdByMenus(List<Integer> list) {
		return getCurSqlSessionTemplate().selectList(RegUser.class.getName() + FIND_USER_AUTH_ID_BY_MENUS, list);
	}

	@Override
	public List<UserSimpleVo> findUserSimpleVoList(List<Integer> regUserIds) {
		return getCurSqlSessionTemplate().selectList(RegUser.class.getName() + ".findUserSimpleVoList", regUserIds);
	}

	@Override
	public RegUser findUserWithPwdByLogin(Long login) {
		return getCurSqlSessionTemplate().selectOne(RegUser.class.getName() + ".findUserWithPwdByLogin", login);
	}

	@Override
	public List<RegUser> findDeletedUser(RegUser validateHasDeleteUser) {
		return getCurSqlSessionTemplate().selectList(RegUser.class.getName() + ".findDeletedUser",
				validateHasDeleteUser);
	}

	@Override
	public RegUser findRegUserByLogin(Long login) {
		return getCurSqlSessionTemplate().selectOne(RegUser.class.getName() + ".findRegUserByLogin", login);
	}

	@Override
	public Map<Integer, UserSimpleVo> findUserSimpleVoByIdList(Set<Integer> userIdSet) {
		return this.getCurSqlSessionTemplate().selectMap(RegUser.class.getName() + ".findUserSimpleVoByIdList",
				userIdSet, "id");
	}

	@Override
	public List<String> findEmailsByRegUserIds(Set<Integer> regUserIds) {
		return getCurSqlSessionTemplate().selectList(RegUser.class.getName() + ".findEmailsByRegUserIds", regUserIds);
	}

	@Override
	public List<Integer> findCommonRecommendUser(List<Integer> otherRoleRegUserId) {
		RegUser regUser = new RegUser();
		regUser.setUserIds(otherRoleRegUserId);
		return getCurSqlSessionTemplate().selectList(RegUser.class.getName() + ".findCommonRecommendUser", regUser);
	}

    @Override
    public Pager findRegUserVipRecordList(UserVO userVO, Pager pager) {
        return this.findByCondition(userVO, pager, RegUser.class, ".findRegUserVipRecordList");
    }

	@Override
	public List<Integer> findAccountSecondMenuIdsByMenuName(String menuName) {
		return getCurSqlSessionTemplate().selectList(RegUser.class.getName() + ".findAccountSecondMenuIdsByMenuName", menuName);
	}
	public UserVO getUserInfoByInviteNo(String inviteNo) {
		return getCurSqlSessionTemplate().selectOne(RegUser.class.getName() + ".getUserInfoByInviteNo",inviteNo);
	}
}
