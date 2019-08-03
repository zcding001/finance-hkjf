package com.hongkun.finance.user.service;

import com.hongkun.finance.user.model.RegUserFriends;
import com.hongkun.finance.user.model.RegUserFriendsGroup;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.user.service.RegUserFriendsService.java
 * @Class Name : RegUserFriendsService.java
 * @Description : GENERATOR SERVICE类
 * @Author : generator
 */
public interface RegUserFriendsService {
	/**
	 *  @Description    : 根据推荐人查询被推荐人集合
	 *  @Method_Name    : findFirstFriendsByRecommendIds
	 *  @param rosIds
	 *  @return
	 *  @return         : List<RegUserFriends>
	 *  @Creation Date  : 2018年1月8日 下午5:01:13 
	 *  @Author         : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	List<RegUserFriends> findFirstFriendsByRecommendIds(List<Integer> rosIds);
	
	/**
	 * @Description : 前台条件检索数据,分页查询我的推荐好友
	 * @Method_Name : findRegUserFriendsList;
	 * @param regUserFriends
	 * @param pager
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年1月10日 上午9:37:46;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	ResponseEntity<?> findRegUserFriendsList(RegUserFriends regUserFriends, Pager pager);

	/**
	 * @Description : 按条件查询推荐好友信息
	 * @Method_Name : findRecommendFriendsList;
	 * @param regUserFriends
	 * @return
	 * @return : List<RegUserFriends>;
	 * @Creation Date : 2018年1月10日 上午9:38:19;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	List<RegUserFriends> findRecommendFriendsList(RegUserFriends regUserFriends);
	/**
	 * 
	 *  @Description    : 根据推荐人查询所有一级好友
	 *  @Method_Name    : findFirstFriendsByRecommendId
	 *  @param regUserId
	 *  @return
	 *  @return         : List<RegUserFriends>
	 *  @Creation Date  : 2018年1月11日 下午6:37:33 
	 *  @Author         : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	List<RegUserFriends> findFirstFriendsByRecommendId(Integer regUserId,Integer grade);
	
	/**
	 *  @Description    : 更新投资好友关系
	 *  @Method_Name    : updateRegUserFriends
	 *  @param regUserFriends
	 *  @return         : int
	 *  @Creation Date  : 2018年1月30日 下午4:51:41 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	int updateRegUserFriends(RegUserFriends regUserFriends);
	/**
	 * 
	 *  @Description    : 放款之后处理用户好友关系
	 *  @Method_Name    : dealRegUserFriendsForInvest
	 *  @param regUserIds
	 *  @return         : void
	 *  @Creation Date  : 2018年1月31日 下午4:01:55 
	 *  @Author         : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	void dealRegUserFriendsForInvest(List<Integer> regUserIds);
	/**
	 *  @Description    : 设置好友备注&分组
	 *  @Method_Name    : setUpMyFriend
	 *  @param friendUserId
	 *  @param memoName
	 *  @param remarks
	 *  @param groupId
	 *  @param regUserId
	 *  @return
	 *  @return         : ResponseEntity<String>
	 *  @Creation Date  : 2018年3月12日 下午3:19:00 
	 *  @Author         : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	ResponseEntity<String> setUpMyFriend(Integer friendUserId, String memoName, String remarks, Integer groupId,
			Integer regUserId);
	/**
	 * 
	 *  @Description    : 查询某分组下的好友列表
	 *  @Method_Name    : findFriendsByGroupId
	 *  @param groupId
	 *  @param regUserId
	 *  @return
	 *  @return         : ResponseEntity<String>
	 *  @Creation Date  : 2018年3月15日 下午3:20:13 
	 *  @Author         : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	ResponseEntity findFriendsByGroupId(Integer regUserId,Integer groupId);
	/**
	 * 
	 *  @Description    : 查询未分组到自定义分组中的好友列表
	 *  @Method_Name    : findUserFriendsUnGrouped
	 *  @return
	 *  @return         : List<RegUserFriends>
	 *  @Creation Date  : 2018年3月15日 下午5:28:14 
	 *  @Author         : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	List<RegUserFriends> findUserFriendsUnGrouped(Integer regUserId);
	/**
	 * 
	 *  @Description    : 條件查詢好友信息（通过好友手机号、姓名、备注姓名模糊查询）
	 *  @Method_Name    : findUserFriendsByCondition
	 *  @param param
	 *  @return
	 *  @return         : List<RegUserFriends>
	 *  @Creation Date  : 2018年3月16日 上午10:02:38 
	 *  @Author         : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	List<RegUserFriends> findUserFriendsByCondition(Integer regUserId,String param);
	/**
	 * 
	 *  @Description    : 初始化好友关系和分组
	 *  @Method_Name    : initUserFriends
	 *  @param regUserId
	 *  @return         : void
	 *  @Creation Date  : 2018年3月20日 上午9:56:46 
	 *  @Author         : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	void initUserFriends(Integer regUserId);

	RegUserFriendsGroup findGroupByUserIdAndType(Integer regUserId, int type);
	/**
	 * 
	 *  @Description    : 把某个好友从自定义分组中删除
	 *  @Method_Name    : removeUserFriendByGroup
	 *  @param regUserId
	 *  @param userFriendId
	 *  @param groupId
	 *  @return
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2018年3月20日 下午6:00:59 
	 *  @Author         : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	ResponseEntity<String> removeUserFriendByGroup(Integer regUserId, Integer userFriendId, Integer groupId);
    /**
     *  @Description    : 根据投资人ID,及等级查询推荐人信息
     *  @Method_Name    : findRegUserFriendsByRegUserId;
     *  @param regUserId
     *  @param grade
     *  @return
     *  @return         : RegUserFriends;
     *  @Creation Date  : 2018年8月21日 下午4:35:02;
     *  @Author         : yanbinghuang@hongkun.com.cn 黄艳兵;
     */
	RegUserFriends findRegUserFriendsByRegUserId(Integer regUserId,Integer grade);

	List<RegUserFriends>  findRegUserFriendsAndGroup( RegUserFriends regUserFriend);

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
