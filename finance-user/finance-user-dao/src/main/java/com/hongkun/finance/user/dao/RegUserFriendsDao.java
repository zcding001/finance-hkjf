package com.hongkun.finance.user.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hongkun.finance.user.model.RegUserFriends;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.user.dao.RegUserFriendsDao.java
 * @Class Name    : RegUserFriendsDao.java
 * @Description   : GENERATOR DAO类
 * @Author        : generator
 */
public interface RegUserFriendsDao extends MyBatisBaseDao<RegUserFriends, java.lang.Long> {
	/**
	 *  @Description    : 通过推荐人id查询被推荐人集合
	 *  @Method_Name    : findFirstFriendsByRecommendIds
	 *  @param rosIds
	 *  @return
	 *  @return         : List<RegUserFriends>
	 *  @Creation Date  : 2018年1月8日 下午5:02:26 
	 *  @Author         : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	List<RegUserFriends> findFirstFriendsByRecommendIds(List<Integer> rosIds);
	/**
	 * 
	 *  @Description    : 查询用户中是未投资，并且好友分组是默认分组的好友关系id集合
	 *  @Method_Name    : findRegUserFriendsNotInvestList
	 *  @param regUserIds
	 *  @return
	 *  @return         : List<Integer>
	 *  @Creation Date  : 2018年1月31日 下午4:05:29 
	 *  @Author         : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	List<Integer> findRegUserFriendsNotInvestList(List<Integer> regUserIds);
	/**
	 * 
	 *  @Description    : 更新好有关系为已投资，并且更新分组id为已投资分组id
	 *  @Method_Name    : updateStateAndGroupIdForInvest
	 *  @param ids
	 *  @return         : void
	 *  @Creation Date  : 2018年1月31日 下午4:11:59 
	 *  @Author         : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	void updateStateAndGroupIdForInvest(List<Integer> ids);
	/**
	 * 
	 *  @Description    : 通过id批量删除好友关系
	 *  @Method_Name    : delBatch
	 *  @param oldFriendsIds
	 *  @return         : void
	 *  @Creation Date  : 2018年3月15日 上午9:22:48 
	 *  @Author         : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	void delBatch(List<Integer> oldFriendsIds);
	/**
	 * 
	 *  @Description    : 查询未分组好友
	 *  @Method_Name    : findUserFriendsUnGrouped
	 *  @param recommendId
	 *  @return
	 *  @return         : List<RegUserFriends>
	 *  @Creation Date  : 2018年3月21日 上午9:25:47 
	 *  @Author         : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	List<RegUserFriends> findUserFriendsUnGrouped(Integer recommendId);
	/**
	 * 
	 *  @Description    : 条件查询好友列表
	 *  @Method_Name    : findUserFriendsByCondition
	 *  @param regUserFriends
	 *  @return
	 *  @return         : List<RegUserFriends>
	 *  @Creation Date  : 2018年3月21日 上午10:07:10 
	 *  @Author         : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	List<RegUserFriends> findUserFriendsByCondition(RegUserFriends regUserFriends);
	/**
	 * 
	 *  @Description    : 条件查询好友关系（和分组表关联查询）
	 *  @Method_Name    : RegUserFriendsAndGroup
	 *  @param regUserFriend
	 *  @return
	 *  @return         : List<RegUserFriends>
	 *  @Creation Date  : 2018年3月21日 上午10:56:02 
	 *  @Author         : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	List<RegUserFriends> findRegUserFriendsAndGroup(RegUserFriends regUserFriend);

	/**
	 *  @Description    ：获取投资人id与推荐人id集合
	 *  @Method_Name    ：findRecommendFriendsByRegUserIds
	 *  @param regUserIds 投资人id集合
	 *  @return java.util.Map<java.lang.Integer,java.lang.Integer>
	 *  @Creation Date  ：2018/10/19
	 *  @Author         ：pengwu@hongkun.com.cn
	 */
    Map<Integer, RegUserFriends> findRecommendFriendsByRegUserIds(Set<Integer> regUserIds);
}
