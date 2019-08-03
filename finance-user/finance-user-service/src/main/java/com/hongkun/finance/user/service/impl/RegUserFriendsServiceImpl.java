package com.hongkun.finance.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.hongkun.finance.user.constants.UserConstants;
import com.hongkun.finance.user.dao.RegUserDao;
import com.hongkun.finance.user.dao.RegUserDetailDao;
import com.hongkun.finance.user.dao.RegUserFriendsDao;
import com.hongkun.finance.user.dao.RegUserFriendsGroupDao;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.model.RegUserDetail;
import com.hongkun.finance.user.model.RegUserFriends;
import com.hongkun.finance.user.model.RegUserFriendsGroup;
import com.hongkun.finance.user.model.vo.UserVO;
import com.hongkun.finance.user.service.RegUserFriendsService;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.CommonUtils;
import com.yirun.framework.core.utils.pager.Pager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.hongkun.finance.user.constants.UserConstants.USER_FRIENDS_GROUP_TYPE_COMMON;
import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * @Project : finance
 * @Program Name :
 *          com.hongkun.finance.user.service.impl.RegUserFriendsServiceImpl.java
 * @Class Name : RegUserFriendsServiceImpl.java
 * @Description : GENERATOR SERVICE实现类
 * @Author : generator
 */
@Service
public class RegUserFriendsServiceImpl implements RegUserFriendsService {

	private static final Logger logger = LoggerFactory.getLogger(RegUserFriendsServiceImpl.class);

	/**
	 * RegUserFriendsDAO
	 */
	@Autowired
	private RegUserFriendsDao regUserFriendsDao;
	
	@Autowired
	private RegUserDao regUserDao;
	
	@Autowired
	private RegUserFriendsGroupDao regUserFriendsGroupDao;
	@Autowired
	private RegUserDetailDao regUserDetailDao;
	
	@Override
	public List<RegUserFriends> findFirstFriendsByRecommendIds(List<Integer> rosIds) {
		return regUserFriendsDao.findFirstFriendsByRecommendIds(rosIds);
	}

	@Override
	public List<RegUserFriends> findFirstFriendsByRecommendId(Integer regUserId,Integer grade) {
		RegUserFriends cdt = new RegUserFriends();
		cdt.setRecommendId(regUserId);
		cdt.setGrade(grade);
		return regUserFriendsDao.findByCondition(cdt);
	}
	
	@Override
	public ResponseEntity<?> findRegUserFriendsList(RegUserFriends regUserFriends, Pager pager) {
		List<Integer> regUserIdList = null;
		try {
			// 1、判断 如果被推荐人姓名为空，且好友级别也为空，则按照用户姓名进行模糊查询
			if (StringUtils.isNotBlank(regUserFriends.getRealName())
					&& (regUserFriends.getGrade() == null || regUserFriends.getGrade() == 0)) {
				UserVO userVO = new UserVO();
				userVO.setRealName(regUserFriends.getRealName());
				regUserIdList = regUserDao.findUserIdsByUserVO(userVO);
			}
			// 2、分页查询我的推荐好友信息
			regUserFriends.setRegUserIdList(regUserIdList);
			regUserFriends.setGrade((regUserFriends.getGrade() != null && regUserFriends.getGrade() == 0) ? null
					: regUserFriends.getGrade());
			regUserFriends.setSortColumns("create_time desc");
			regUserFriends.setState(1);
			Pager pagerInfo = regUserFriendsDao.findByCondition(regUserFriends, pager);
			List<RegUserFriends> regUserFriendsList = new ArrayList<RegUserFriends>();
			if (!BaseUtil.resultPageHasNoData(pagerInfo)) {
			    List<RegUserFriends> friendList = (List<RegUserFriends>) pagerInfo.getData();
			    friendList.forEach(regs->{
			        RegUser  userVoInfo = regUserDao.findByPK(Long.valueOf(regs.getRegUserId()),RegUser.class);
			        //如果未实名的显示尼称
			        if(userVoInfo.getIdentify() == 0){
			            regs.setRealName(userVoInfo.getNickName());
			        }else{
			            regs.setRealName("**" + regs.getRealName().substring(1));
			        }
			        regUserFriendsList.add(regs);
			    });
			    pagerInfo.setData(regUserFriendsList);
	        }
			return new ResponseEntity<>(SUCCESS, pagerInfo);
		} catch (Exception e) {
			logger.info("用户标识：{},前台查询我的推荐好友，异常：{}" + regUserFriends.getRecommendId(), e.getLocalizedMessage(),e.getLocalizedMessage());
			return new ResponseEntity<>(SUCCESS, null);
		}
	}

	@Override
	public List<RegUserFriends> findRecommendFriendsList(RegUserFriends regUserFriends) {
		return regUserFriendsDao.findByCondition(regUserFriends);
	}

	@Override
	public int updateRegUserFriends(RegUserFriends regUserFriends) {
		logger.info("updateRegUserFriends, 更新好友关系, 用户: {}, 好友关系: {}", regUserFriends.getRegUserId(), regUserFriends.toString());
		try {
			return this.regUserFriendsDao.update(regUserFriends);
		} catch (Exception e) {
			logger.error("updateRegUserFriends, 更新好友关系, 用户: {}, 好友关系: {}\n", regUserFriends.getRegUserId(), regUserFriends.toString(), e);
			throw new GeneralException("更新好友关系失败");
		}
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void dealRegUserFriendsForInvest(List<Integer> regUserIds) {
		logger.info("dealRegUserFriendsForInvest, 放款更新用户好友关系投资状态, regUserIds: {}",JSON.toJSON(regUserIds));
		try {
			List<Integer> ids = regUserFriendsDao.findRegUserFriendsNotInvestList(regUserIds);
			if(CommonUtils.isNotEmpty(ids)){
                regUserFriendsDao.updateStateAndGroupIdForInvest(ids);
            }
            //更新用户信息为已投资
			regUserDetailDao.updateInvestFlagByRegUserIds(regUserIds);
		} catch (Exception e) {
			logger.error("dealRegUserFriendsForInvest, 放款更新用户好友关系投资状态异常, regUserIds: {},异常信息: ",
					JSON.toJSON(regUserIds), e);
			throw new GeneralException("放款更新用户好友关系投资状态失败");
		}
	}

	@Override
	public ResponseEntity<String> setUpMyFriend(Integer friendUserId, String memoName, String remarks, Integer groupId,
			Integer regUserId) {
		//1、好友关系存在
		//2、分组存在
		//3、更新备注等信息
		RegUserFriends cdt = new RegUserFriends();
		cdt.setRegUserId(friendUserId);
		cdt.setRecommendId(regUserId);
		List<RegUserFriends> userFriends =  regUserFriendsDao.findRegUserFriendsAndGroup(cdt);
		if(CommonUtils.isEmpty(userFriends)){
			return new ResponseEntity<>(Constants.ERROR,"当前好友关系不存在");
		}
		RegUserFriends myselfFriend = null;
		for (RegUserFriends rf : userFriends){
			if (rf.getGroupType() == USER_FRIENDS_GROUP_TYPE_COMMON){//如果是自定义分组
				myselfFriend = rf;
			}
		}

		if (groupId != null && groupId != 0 ){
			RegUserFriendsGroup groupCdt = new RegUserFriendsGroup();
			groupCdt.setRegUserId(regUserId);
			groupCdt.setId(groupId);
			groupCdt.setType(USER_FRIENDS_GROUP_TYPE_COMMON);
			List<RegUserFriendsGroup> groupList = regUserFriendsGroupDao.findByCondition(groupCdt);
			if(CommonUtils.isEmpty(groupList)){
				return new ResponseEntity<>(Constants.ERROR,"分组不存在");
			}

			if (myselfFriend != null ){
				cdt.setId(myselfFriend.getId());
				cdt.setMemoName(memoName);
				cdt.setRemarks(remarks);
				cdt.setGroupId(groupId);
				regUserFriendsDao.update(cdt);
			}else{
				cdt.setMemoName(memoName);
				cdt.setRemarks(remarks);
				cdt.setGroupId(groupId);
				regUserFriendsDao.save(cdt);
			}
		}else{
			cdt.setMemoName(memoName);
			cdt.setRemarks(remarks);
			regUserFriendsDao.update(cdt);
		}
		return new ResponseEntity<>(Constants.SUCCESS,"设置好友信息成功");
	}

	@Override
	public ResponseEntity findFriendsByGroupId(Integer regUserId,Integer groupId) {
		if(CommonUtils.gtZero(groupId)){
			RegUserFriendsGroup cdt = new RegUserFriendsGroup();
			cdt.setRegUserId(regUserId);
			cdt.setId(groupId);
			List<RegUserFriendsGroup>  groups = regUserFriendsGroupDao.findByCondition(cdt);
			if(CommonUtils.isNotEmpty(groups)){
				String groupName = groups.get(0).getName();
				RegUserFriends friend = new RegUserFriends();
				friend.setGroupId(groupId);
				friend.setState(1);
				List<RegUserFriends> list = regUserFriendsDao.findByCondition(friend);
				if (CommonUtils.isNotEmpty(list)){
					for (RegUserFriends regUserFriends : list) {
						regUserFriends.setGroupName(groupName);
					}
				}
				return new ResponseEntity<>(Constants.SUCCESS,list);
			}
		}
		return new ResponseEntity<>(Constants.ERROR,"分组不存在");
	}

	@Override
	public List<RegUserFriends> findUserFriendsUnGrouped(Integer regUserId) {
		return	regUserFriendsDao.findUserFriendsUnGrouped(regUserId);
//		 regUserFriendsDao.findByCondition(cdt, pager.getCurrentPage(), pager.getPageSize(), null, ".findUserFriendsUnGrouped");
	}

	@Override
	public List<RegUserFriends> findUserFriendsByCondition(Integer regUserId,String param) {
		RegUserFriends cdt = new RegUserFriends();
		cdt.setRecommendId(regUserId);
		cdt.setRealName(param);
		cdt.setTel(param);
		cdt.setMemoName(param);
		return regUserFriendsDao.findUserFriendsByCondition(cdt);
	}

	
	
	@Override
	public void initUserFriends(Integer regUserId) {
		//1、查询好友
		UserVO userVo = regUserDao.findUserWithDetailById(regUserId);
		if(userVo!=null){
			//创建好友分组
			List<RegUserFriendsGroup> groups = new ArrayList<RegUserFriendsGroup>();
			List<RegUserFriends> friends = new ArrayList<RegUserFriends>();
			RegUserFriendsGroup groupCdt = new RegUserFriendsGroup();
			groupCdt.setRegUserId(userVo.getUserId());
			List<RegUserFriendsGroup> allGroups = regUserFriendsGroupDao.findByCondition(groupCdt);
			if(CommonUtils.isEmpty(allGroups)){
				RegUserFriendsGroup notInvestGroup = new RegUserFriendsGroup();
				notInvestGroup.setName(UserConstants.USER_FRIENDS_GROUP_INVEST_NOT);
				notInvestGroup.setRegUserId(regUserId);
				notInvestGroup.setType(UserConstants.USER_FRIENDS_GROUP_TYPE_NOT_INVEST);
				
				RegUserFriendsGroup investGroup = new RegUserFriendsGroup();
				investGroup.setName(UserConstants.USER_FRIENDS_GROUP_INVEST_YES);
				investGroup.setRegUserId(regUserId);
				investGroup.setType(UserConstants.USER_FRIENDS_GROUP_TYPE_INVEST);
				groups.add(notInvestGroup);
				groups.add(investGroup);
			}
			//创建好友关系
			if(StringUtils.isNotBlank(userVo.getCommendNo())){
				//通过邀请码查询好友
				RegUserDetail firstUser = regUserDetailDao.findRegUserDetailByCommendNo(userVo.getCommendNo());
				if(firstUser!=null){
					//查询好友分组
					RegUserFriendsGroup group = findGroupByUserIdAndType(firstUser.getRegUserId(),1);
					RegUserFriends userFriend = new RegUserFriends();
					userFriend.setGroupId(group.getId());
					userFriend.setRegUserId(userVo.getUserId());
					userFriend.setRecommendId(firstUser.getRegUserId());
					userFriend.setRealName(StringUtils.isBlank(userVo.getRealName())?userVo.getNickName():userVo.getRealName());
					userFriend.setTel(String.valueOf(userVo.getLogin()));
					userFriend.setGrade(UserConstants.USER_FRIEND_GRADE_FIRST);
					userFriend.setInvestState(UserConstants.USER_INVEST_STATE_NOT);
					userFriend.setState(UserConstants.USER_FRIENDS_STATE_VAL);
					friends.add(userFriend);
					if(StringUtils.isNotBlank(firstUser.getCommendNo())){
						//二级关系
						RegUserDetail secondUser = regUserDetailDao.findRegUserDetailByCommendNo(firstUser.getCommendNo());
						if(secondUser!=null){
							//查询好友分组
							RegUserFriendsGroup secondGroup = findGroupByUserIdAndType(secondUser.getRegUserId(),1);
							RegUserFriends secondUserFriend = new RegUserFriends();
							secondUserFriend.setGroupId(secondGroup.getId());
							secondUserFriend.setRegUserId(userVo.getUserId());
							secondUserFriend.setRecommendId(secondUser.getRegUserId());
							secondUserFriend.setRealName(StringUtils.isBlank(userVo.getRealName())?userVo.getNickName():userVo.getRealName());
							secondUserFriend.setTel(String.valueOf(userVo.getLogin()));
							secondUserFriend.setGrade(UserConstants.USER_FRIEND_GRADE_SECOND);
							secondUserFriend.setInvestState(UserConstants.USER_INVEST_STATE_NOT);
							secondUserFriend.setState(UserConstants.USER_FRIENDS_STATE_VAL);
							friends.add(secondUserFriend);
						}
					}
				}
			}
			if(CommonUtils.isNotEmpty(groups)){
				regUserFriendsGroupDao.insertBatch(RegUserFriendsGroup.class, groups);
			}
			if(CommonUtils.isNotEmpty(friends)){
				regUserFriendsDao.insertBatch(RegUserFriends.class, friends);
			}
		}
	}
	
	@Override
	public RegUserFriendsGroup findGroupByUserIdAndType(Integer regUserId, int type) {
		RegUserFriendsGroup cdt = new RegUserFriendsGroup();
		cdt.setRegUserId(regUserId);
		cdt.setType(type);
		List<RegUserFriendsGroup>  groups = regUserFriendsGroupDao.findByCondition(cdt);
		if(CommonUtils.isNotEmpty(groups)){
			return groups.get(0);
		}
		return null;
	}

	@Override
	public ResponseEntity<String> removeUserFriendByGroup(Integer regUserId, Integer userFriendId, Integer groupId) {
		//判断此好友关系是否正确
		RegUserFriends rcdt = new RegUserFriends();
		rcdt.setRecommendId(regUserId);
		rcdt.setRegUserId(userFriendId);
		rcdt.setGroupId(groupId);
		List<RegUserFriends> resultList = regUserFriendsDao.findByCondition(rcdt);
		if(CommonUtils.isEmpty(resultList)){
			return new ResponseEntity<>(Constants.ERROR,"没有查询到好友关系");
		}
		RegUserFriendsGroup group = regUserFriendsGroupDao.findByPK(Long.valueOf(groupId), RegUserFriendsGroup.class);
		if(group==null){
			return new ResponseEntity<>(Constants.ERROR,"没有查询到分组");
		}
		if(group.getType()!=0){
			return new ResponseEntity<>(Constants.ERROR,"只允许在自定义分组中移除好友");
		}
		regUserFriendsDao.delete(Long.valueOf(resultList.get(0).getId()), RegUserFriends.class);
		return new ResponseEntity<>(Constants.SUCCESS,"移除好友成功");
	}

    @Override
    public RegUserFriends findRegUserFriendsByRegUserId(Integer regUserId, Integer grade) {
        RegUserFriends rcdt = new RegUserFriends();
        rcdt.setRegUserId(regUserId);
        rcdt.setGrade(grade);
        List<RegUserFriends> resultList = regUserFriendsDao.findByCondition(rcdt);
        if(resultList != null && resultList.size() > 0){
            return resultList.get(0);
        }
        return null;
    }

	@Override
	public List<RegUserFriends> findRegUserFriendsAndGroup(RegUserFriends regUserFriend) {
		return regUserFriendsDao.findRegUserFriendsAndGroup(regUserFriend);
	}

	@Override
	public Map<Integer, RegUserFriends> findRecommendFriendsByRegUserIds(Set<Integer> regUserIds) {
		return regUserFriendsDao.findRecommendFriendsByRegUserIds(regUserIds);
	}
}
