package com.hongkun.finance.user.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.CommonUtils;
import com.yirun.framework.core.utils.pager.Pager;
import com.hongkun.finance.user.model.RegUserFriends;
import com.hongkun.finance.user.model.RegUserFriendsGroup;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.user.constants.UserConstants;
import com.hongkun.finance.user.dao.RegUserFriendsDao;
import com.hongkun.finance.user.dao.RegUserFriendsGroupDao;
import com.hongkun.finance.user.service.RegUserFriendsGroupService;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.user.service.impl.RegUserFriendsGroupServiceImpl.java
 * @Class Name    : RegUserFriendsGroupServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class RegUserFriendsGroupServiceImpl implements RegUserFriendsGroupService {

	private static final Logger logger = LoggerFactory.getLogger(RegUserFriendsGroupServiceImpl.class);
	
	/**
	 * RegUserFriendsGroupDAO
	 */
	@Autowired
	private RegUserFriendsGroupDao regUserFriendsGroupDao;
	@Autowired
	private RegUserFriendsDao regUserFriendsDao;

	@Override
	public List<RegUserFriendsGroup> findGroupsByUserId(Integer regUserId) {
		RegUserFriendsGroup cdt = new RegUserFriendsGroup();
		cdt.setRegUserId(regUserId);
		return regUserFriendsGroupDao.findByCondition(cdt);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public ResponseEntity<String> editGroupName(String groupName, Integer groupId, Integer regUserId) {
		if(!existGroups(groupId,regUserId)){
			return new ResponseEntity<>(Constants.ERROR,"只能修改自定义分组");
		}
		RegUserFriendsGroup upGroup = new RegUserFriendsGroup();
		upGroup.setId(groupId);
		upGroup.setName(groupName);
		regUserFriendsGroupDao.update(upGroup);
		return new ResponseEntity<>(Constants.SUCCESS,"修改成功");
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public ResponseEntity<String> delGroupById(Integer groupId, Integer regUserId) {
		if(!existGroups(groupId,regUserId)){
			return new ResponseEntity<>(Constants.ERROR,"只能删除自定义分组");
		}
		regUserFriendsGroupDao.delete(Long.valueOf(groupId), RegUserFriendsGroup.class);
		return new ResponseEntity<>(Constants.SUCCESS,"删除成功");
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public ResponseEntity<String> addFriendsToGroup(Integer groupId, String idList, Integer regUserId) {
		if(StringUtils.isBlank(idList)){
			return new ResponseEntity<>(Constants.ERROR,"未选择好友");
		}
		if(!existGroups(groupId,regUserId)){
			return new ResponseEntity<>(Constants.ERROR,"操作的分组不存在");
		}
		String[] userIds = idList.split(",");
		List<Integer> regUserIdList = new ArrayList<Integer>();
		for(String s:userIds){
			regUserIdList.add(Integer.parseInt(s));
		}
		//删除原分组数据
		List<Integer> oldFriendsIds = new ArrayList<Integer>();
		//需要插入的新分组数据
		List<RegUserFriends> newFriendsList =  new ArrayList<RegUserFriends>();
		RegUserFriends regUserFriend = new RegUserFriends();
		regUserFriend.setRegUserIdList(regUserIdList);
		regUserFriend.setRecommendId(regUserId);
		List<RegUserFriends>  oldFriends = regUserFriendsDao.findRegUserFriendsAndGroup(regUserFriend);
		if(CommonUtils.isNotEmpty(oldFriends)){
			oldFriends.forEach(old->{
				//判断原分组是否是默认分组
				if(old.getGroupType()==UserConstants.USER_FRIENDS_GROUP_TYPE_COMMON){
					oldFriendsIds.add(old.getId());
					return;
				}
				//生成新的好友关系
				RegUserFriends newFriends  =  new RegUserFriends();
				newFriends.setRegUserId(old.getRegUserId());
				newFriends.setRecommendId(old.getRecommendId());
				newFriends.setRealName(old.getRealName());
				newFriends.setInvestState(old.getInvestState());
				newFriends.setMemoName(old.getMemoName());
				newFriends.setGrade(old.getGrade());
				newFriends.setRemarks(old.getRemarks());
				newFriends.setTel(old.getTel());
				newFriends.setGroupId(groupId);
				newFriendsList.add(newFriends);
			});
			if(CommonUtils.isNotEmpty(oldFriendsIds)){
				regUserFriendsDao.delBatch(oldFriendsIds);
			}
			regUserFriendsDao.insertBatch(RegUserFriends.class, newFriendsList);
		}
		return new ResponseEntity<>(Constants.SUCCESS,"移动好友成功");
	}
	/**
	 *  @Description    : 判断用户某个自定义分组是否存在
	 *  @Method_Name    : existGroups
	 *  @param groupId
	 *  @param regUserId
	 *  @return
	 *  @return         : boolean
	 *  @Creation Date  : 2018年3月13日 上午9:09:32 
	 *  @Author         : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	private boolean existGroups(Integer groupId,Integer regUserId){
		RegUserFriendsGroup cdt = new RegUserFriendsGroup();
		cdt.setRegUserId(regUserId);
		cdt.setId(groupId);
		cdt.setType(0);
		List<RegUserFriendsGroup>  groups = regUserFriendsGroupDao.findByCondition(cdt);
		return CommonUtils.isNotEmpty(groups);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public ResponseEntity<String> addFriendGroup(String name, Integer regUserId, Integer sort) {
		sort = sort == null?0:sort;
		RegUserFriendsGroup model = new RegUserFriendsGroup();
		model.setRegUserId(regUserId);
		model.setName(name);
		model.setSort(sort);
		model.setType(0);
		regUserFriendsGroupDao.save(model);
		return new ResponseEntity<>(Constants.SUCCESS,"创建分组成功");
	}

	@Override
	public List<RegUserFriendsGroup> findGroupsAndNumByUserId(Integer regUserId) {
		return regUserFriendsGroupDao.findGroupsAndNumByUserId(regUserId);
	}
	
}
