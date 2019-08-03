package com.hongkun.finance.user.service;

import java.util.List;

import com.hongkun.finance.user.model.RegUserFriendsGroup;
import com.yirun.framework.core.model.ResponseEntity;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.user.service.RegUserFriendsGroupService.java
 * @Class Name    : RegUserFriendsGroupService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface RegUserFriendsGroupService {
	
	List<RegUserFriendsGroup> findGroupsByUserId(Integer regUserId);
	
	List<RegUserFriendsGroup> findGroupsAndNumByUserId(Integer regUserId);
	/**
	 *  @Description    : 修改用戶分組名稱
	 *  @Method_Name    : editGroupName
	 *  @param groupName  新名稱
	 *  @param groupId 分組id
	 *  @param regUserId 當前用戶id
	 *  @return
	 *  @return         : ResponseEntity<String>
	 *  @Creation Date  : 2018年3月12日 上午10:44:23 
	 *  @Author         : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	ResponseEntity<String> editGroupName(String groupName, Integer groupId, Integer regUserId);
	/**
	 * 
	 *  @Description    : 删除自定义分组
	 *  @Method_Name    : delGroupById
	 *  @param groupId
	 *  @param regUserId
	 *  @return
	 *  @return         : ResponseEntity<String>
	 *  @Creation Date  : 2018年3月12日 下午3:06:11 
	 *  @Author         : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	ResponseEntity<String> delGroupById(Integer groupId, Integer regUserId);
	/**
	 *  @Description    : 批量添加好友到指定分组
	 *  @Method_Name    : addFriendsToGroup
	 *  @param groupId
	 *  @param idList
	 *  @return
	 *  @return         : ResponseEntity<String>
	 *  @Creation Date  : 2018年3月13日 上午9:03:47 
	 *  @Author         : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	ResponseEntity<String> addFriendsToGroup(Integer groupId, String idList,Integer regUserId);
	/**
	 * 
	 *  @Description    : 新建自定义分组
	 *  @Method_Name    : addFriendGroup
	 *  @param name
	 *  @param regUserId
	 *  @param sort
	 *  @return
	 *  @return         : ResponseEntity<String>
	 *  @Creation Date  : 2018年3月15日 上午9:39:13 
	 *  @Author         : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	ResponseEntity<String> addFriendGroup(String name, Integer regUserId, Integer sort);
	
}
