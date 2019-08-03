package com.hongkun.finance.roster.facade.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.invest.model.BidInfo;
import com.hongkun.finance.invest.model.vo.BidInvestForRecommendVo;
import com.hongkun.finance.invest.service.BidInfoService;
import com.hongkun.finance.invest.service.BidInvestService;
import com.hongkun.finance.roster.constants.RosterConstants;
import com.hongkun.finance.roster.constants.RosterFlag;
import com.hongkun.finance.roster.constants.RosterType;
import com.hongkun.finance.roster.facade.RosterFacade;
import com.hongkun.finance.roster.model.RosDepositInfo;
import com.hongkun.finance.roster.model.RosInfo;
import com.hongkun.finance.roster.model.RosStaffInfo;
import com.hongkun.finance.roster.service.RosDepositInfoService;
import com.hongkun.finance.roster.service.RosInfoService;
import com.hongkun.finance.roster.service.RosStaffInfoService;
import com.hongkun.finance.roster.vo.RosStaffInfoVo;
import com.hongkun.finance.user.constants.UserConstants;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.model.RegUserFriends;
import com.hongkun.finance.user.model.SysUserRoleRel;
import com.hongkun.finance.user.model.vo.UserVO;
import com.hongkun.finance.user.service.RegUserFriendsService;
import com.hongkun.finance.user.service.RegUserService;
import com.hongkun.finance.user.support.security.AuthUtil;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.BeanPropertiesUtil;
import com.yirun.framework.core.utils.CommonUtils;
import com.yirun.framework.core.utils.CompareUtil;
import com.yirun.framework.core.utils.pager.PageHelper;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.redis.JedisClusterUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.hongkun.finance.user.constants.UserConstants.MENU_TYPE_MY_ACCOUNT;
import static com.hongkun.finance.user.support.security.AuthUtil.getMenuKey;
import static com.hongkun.finance.user.support.security.SecurityConstants.AUTH_MENU_EXPIRE_TIME;
import static com.hongkun.finance.user.support.security.SecurityConstants.USER_MENU_PREFIX;
import static com.hongkun.finance.user.utils.BaseUtil.tryLoadDataFromRedis;
import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.join;

@Service
public class RosterFacadeImpl implements RosterFacade{

	private static final Logger logger = LoggerFactory.getLogger(RosterFacadeImpl.class);
	
	@Reference
	private RegUserService regUserService;
	@Reference
	private RosInfoService rosInfoService;
	@Reference
	private RosStaffInfoService rosStaffInfoService;
	@Reference
	private RosDepositInfoService rosDepositInfoService;
	@Reference
	private BidInfoService bidInfoService;
	@Reference
	private RegUserFriendsService regUserFriendsService;
	@Reference
	private BidInvestService bidInvestService;

	@Override
	public boolean validateRoster(long tel, RosterType rosterType, RosterFlag rosterFlag) {
		RegUser regUser = this.regUserService.findRegUserByLogin(tel);
		if(regUser != null){
			return this.rosInfoService.validateRoster(regUser.getId(), rosterType, rosterFlag);
		}
		return false;
	}

	@Override
	public ResponseEntity<?> findRosInfoList(Pager pager, RosInfo rosInfo) {
		Pager result = this.rosInfoService.findRosInfoList(rosInfo, pager);
		if(result != null && CommonUtils.isNotEmpty(result.getData())){
			result.getData().forEach(e -> {
			    Integer regUserId = ((RosInfo)e).getRegUserId();
			    if(regUserId != null && regUserId > 0){
                    BeanPropertiesUtil.mergeProperties(e, this.regUserService.findUserWithDetailById(regUserId));
                }
            });
		}
		return new ResponseEntity<>(Constants.SUCCESS, result);
	}

	@Override
	public ResponseEntity<?> addRosInfo(RosInfo rosInfo) {
		logger.info("addRosInfo, 黑白名单, 手机号: {}, 类型: {}, 类别: {}",rosInfo.getLogin(), rosInfo.getType(), rosInfo.getFlag());
		try {
		    //注册的黑白名不需要验证用户是否存在
		    if(rosInfo.getType().equals(RosterType.REGISTER.getValue())){
                if(this.rosInfoService.validateRoster(rosInfo.getLogin(), RosterType.getRosterType(rosInfo.getType()), RosterFlag.getRosterFlag(rosInfo.getFlag()))){
                    return new ResponseEntity<>(Constants.ERROR, "用户此类别的黑白名单已存在");
                }
            }else{
                RegUser regUser = this.regUserService.findRegUserByLogin(rosInfo.getLogin());
                if(regUser == null){
                    return new ResponseEntity<>(Constants.ERROR, "未找到用户");
                }
                rosInfo.setRegUserId(regUser.getId());
                if(this.rosInfoService.validateRoster(rosInfo.getRegUserId(), RosterType.getRosterType(rosInfo.getType()), RosterFlag.getRosterFlag(rosInfo.getFlag()))){
                    return new ResponseEntity<>(Constants.ERROR, "用户此类别的黑白名单已存在");
                }
			    rosInfo.setLogin(regUser.getLogin());
            }
			this.rosInfoService.insertRosInfo(rosInfo);
			//删除对应类型的反名单
			RosInfo delInfo = new RosInfo();
			delInfo.setRegUserId(rosInfo.getRegUserId());
			delInfo.setLogin(rosInfo.getLogin());
			delInfo.setType(rosInfo.getType());
			if(rosInfo.getFlag() == RosterFlag.BLACK.getValue()){
				delInfo.setFlag(RosterFlag.WHITE.getValue());
			}else{
				delInfo.setFlag(RosterFlag.BLACK.getValue());
			}
			List<RosInfo> rosList = this.rosInfoService.findRosInfoList(delInfo);
			if(rosList != null && rosList.size() > 0){
				this.rosInfoService.delRosInfo(rosList.get(0).getId());
			}
			return new ResponseEntity<>(Constants.SUCCESS);
		} catch (Exception e) {
			logger.error("addRosInfo, 黑白名单, 手机号: {}, 类型: {}, 类别: {}\n",rosInfo.getLogin(), rosInfo.getType(), rosInfo.getFlag(), e);
			throw new GeneralException("添加黑白名单失败");
		}
	}

	@Override
	public ResponseEntity<?> addRosStaffInfo(RosStaffInfo rosStaffInfo) {
		UserVO userVO = new UserVO();
		userVO.setLogin(rosStaffInfo.getLogin());
		List<UserVO> list = this.regUserService.findUserWithDetailByInfo(userVO);
		if(CommonUtils.isEmpty(list)){
			return new ResponseEntity<>(Constants.ERROR, "员工不存在，请确认手机号!");
		}
		userVO = list.get(0);
		if(StringUtils.isBlank(userVO.getRealName())){
			return new ResponseEntity<>(Constants.ERROR, "员工未实名");
		}
        RosStaffInfo cdt = new RosStaffInfo();
		cdt.setRegUserId(userVO.getUserId());
		cdt.setState(RosterConstants.ROSTER_STAFF_STATE_VALID);
		List<RosStaffInfo> rosList = this.rosStaffInfoService.findRosStaffInfoList(cdt);
		if(CommonUtils.isNotEmpty(rosList)){
            return new ResponseEntity<>(Constants.ERROR, "此用户已是员工, 请确认!"); 
        }
		rosStaffInfo.setRealName(userVO.getRealName());
		rosStaffInfo.setRegUserId(userVO.getUserId());
		int count;
		if(CommonUtils.isEmpty(this.rosStaffInfoService.findRosStaffInfoList(rosStaffInfo))){
			count = this.rosStaffInfoService.insertRosStaffInfo(rosStaffInfo);
		}else{
			rosStaffInfo.setState(1);
			count = this.rosStaffInfoService.updateRosStaffInfo(rosStaffInfo);
		}
		if(count > 0){
			return new ResponseEntity<>(Constants.SUCCESS);
		}
		return new ResponseEntity<>(Constants.ERROR, "员工信息维护失败");
	}

	@Override
	public ResponseEntity<?> findRosDepositInfoList(Pager pager, RosDepositInfo rosDepositInfo) {
		// 封装姓名、手机号、身份证号检索条件
		if (StringUtils.isNotBlank(rosDepositInfo.getRealName())
				|| rosDepositInfo.getLogin() != null && rosDepositInfo.getLogin() > 0
				|| StringUtils.isNotBlank(rosDepositInfo.getIdCard())) {
			rosDepositInfo
					.setUserIds(
							this.regUserService
									.findUserWithDetailByInfo(
											BeanPropertiesUtil.mergeAndReturn(new UserVO(), rosDepositInfo))
									.stream().map(UserVO::getUserId).collect(Collectors.toList()));
		}
		// 封装标新信息检索条件
		if (StringUtils.isNotBlank(rosDepositInfo.getBidName())) {
			BidInfo bidInfo = new BidInfo();
			bidInfo.setName(rosDepositInfo.getBidName());
			rosDepositInfo.setBidIds(this.bidInfoService.findBidInfoList(bidInfo).stream().map(BidInfo::getId)
					.collect(Collectors.toList()));
		}
		Pager result = this.rosDepositInfoService.findRosDepositInfoList(rosDepositInfo, pager);
		if (result != null && CommonUtils.isNotEmpty(result.getData())) {
			//完成用户属性copy
			UserVO userVO = new UserVO();
			userVO.setUserIds(result.getData().stream().map(tmp -> ((RosDepositInfo) tmp).getRegUserId())
					.collect(Collectors.toList()));
			result.getData()
					.forEach(
							e -> BeanPropertiesUtil.mergeProperties(e,
									this.regUserService.findUserWithDetailByInfo(userVO).stream()
											.filter(u -> u.getUserId().equals(((RosDepositInfo) e).getRegUserId()))
											.findAny().get()));
			//完成标的信息的copy
			BidInfo bidInfo = new BidInfo();
			bidInfo.setIds(result.getData().stream().map(tmp -> ((RosDepositInfo) tmp).getBidId())
					.collect(Collectors.toList()));
			result.getData()
					.forEach(e -> BeanPropertiesUtil.mergeProperties(e, this.bidInfoService.findBidInfoList(bidInfo)
							.stream().filter(b -> b.getId().equals(((RosDepositInfo) e).getBidId())).findAny().get()));
		}
		return new ResponseEntity<>(Constants.SUCCESS, result);
	}

	@Override
	public ResponseEntity<?> addRosDepositInfo(RosDepositInfo rosDepositInfo) {
		RegUser regUser = this.regUserService.findRegUserByLogin(rosDepositInfo.getLogin());
		if(regUser == null){
			return new ResponseEntity<>(Constants.ERROR, "用户不存在，请确认手机号!");
		}
		if(regUser.getIdentify() == UserConstants.USER_IDENTIFY_NO){
			return new ResponseEntity<>(Constants.ERROR, "此用户未实名!");
		}
		rosDepositInfo.setRegUserId(regUser.getId());
		if(this.rosDepositInfoService.insertRosDepositInfo(rosDepositInfo) > 0){
			return new ResponseEntity<>(Constants.SUCCESS);
		}
		return new ResponseEntity<>(Constants.ERROR);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ResponseEntity<?> findPropertySales(RosStaffInfoVo rosStaffInfoContidion, Pager pager) {
		//1、员工列表
		List<RosStaffInfoVo> rosStaffInfos =  rosStaffInfoService.findRosStaffInfoList(rosStaffInfoContidion);
		List<Integer> rosIds = new ArrayList<Integer>();
		Map<Integer,BidInvestForRecommendVo> maps = null;
		Pager result = pager;
		BigDecimal investAmountSum = new BigDecimal(0);
		BigDecimal investBackStepMoneySum = new BigDecimal(0);
		if(CommonUtils.isNotEmpty(rosStaffInfos)){
			rosStaffInfos.forEach(vo->{
				rosIds.add(vo.getRegUserId());
			});
			//2、员工邀请人，投资金额（优选）
			List<RegUserFriends> regUserFriends = regUserFriendsService.findFirstFriendsByRecommendIds(rosIds);
			List<Integer> friendIds = new ArrayList<Integer>();
			if(CommonUtils.isNotEmpty(regUserFriends)){
				regUserFriends.forEach(userFriend->{
					friendIds.add(userFriend.getRegUserId());
				});
				//为员工分组，每个员工推荐了哪些好友
				Map<Integer,List<Integer>> rosMap = dealRegUserFriendsForRosStaffInfo(regUserFriends);
				//3、查询每个被推荐人投资金额
				if(CommonUtils.isNotEmpty(friendIds)){
					maps =  bidInvestService.findSumInvestAmount(friendIds);
					if(maps!=null){
						maps.forEach((key,value)->{
							rosStaffInfos.forEach(ros->{
								//key 是否属于当前员工的被推荐人
								List<Integer> recommends = rosMap.get(ros.getRegUserId());
								if(CommonUtils.isNotEmpty(recommends)&&recommends.contains(key)){
									BigDecimal investSum = ros.getInvestSumMoney()==null?new BigDecimal(0):ros.getInvestSumMoney();
									BigDecimal investBackStepSum = ros.getInvestBackStepMoney()==null?new BigDecimal(0):ros.getInvestBackStepMoney();
									ros.setInvestSumMoney(investSum.add(value.getInvestAmount()));
									ros.setInvestBackStepMoney(investBackStepSum.add(value.getInvestBackStepMoney()));
								}
							});
						});
					}
				}
			}
			//rosStaffInfos 按照投资累计金额 倒叙排序
			rosStaffInfos.sort((RosStaffInfoVo a, RosStaffInfoVo b) -> b.getInvestSumMoney().compareTo(a.getInvestSumMoney()));
			//取出分页条数
			result = PageHelper.getCurrentPager(rosStaffInfos, pager);
			List<RosStaffInfoVo> vos =  (List<RosStaffInfoVo>) result.getData();
			for(RosStaffInfoVo vo:vos){
				investAmountSum = investAmountSum.add(vo.getInvestSumMoney());
				investBackStepMoneySum = investBackStepMoneySum.add(vo.getInvestBackStepMoney());
			}
		}

		//物业公司本身投资金额
		List<Integer> propertyId = new ArrayList<Integer>();
		propertyId.add(rosStaffInfoContidion.getEnterpriseRegUserId());
		Map<Integer,BidInvestForRecommendVo> mapR =   bidInvestService.findSumInvestAmount(propertyId);
		BigDecimal enterpriseInvestSum = BigDecimal.ZERO;
		if (mapR!=null && mapR.size()>0){
			enterpriseInvestSum = mapR.get(rosStaffInfoContidion.getEnterpriseRegUserId()).getInvestAmount();
			investAmountSum = investAmountSum.add(enterpriseInvestSum);
			investBackStepMoneySum = investBackStepMoneySum
					.add(mapR.get(rosStaffInfoContidion.getEnterpriseRegUserId()).getInvestBackStepMoney());
		}
		Map<String,Object> params = new  HashMap<String,Object>();
		params.put("investAmountSum", investAmountSum);
		params.put("investBackStepMoneySum", investBackStepMoneySum);
		if (CompareUtil.gtZero(enterpriseInvestSum)){
			params.put("enterPriseInvestSum", enterpriseInvestSum);
		}
		return new ResponseEntity<>(Constants.SUCCESS,result,params);
	}
	/**
	 *  @Description    : 解析好友id，将好友根据员工进行分组
	 *  @Method_Name    : dealRegUserFriendsForRosStaffInfo
	 *  @param regUserFriends
	 *  @return
	 *  @return         : Map<Integer,List<Integer>>
	 *  @Creation Date  : 2018年1月8日 下午5:15:04 
	 *  @Author         : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	private Map<Integer, List<Integer>> dealRegUserFriendsForRosStaffInfo(List<RegUserFriends> regUserFriends) {
		Map<Integer, List<Integer>> params = new HashMap<Integer,List<Integer>>();
		regUserFriends.forEach(userFriend->{
			List<Integer> list = params.containsKey(userFriend.getRecommendId())?
					params.get(userFriend.getRecommendId()):new ArrayList<Integer>();
			list.add(userFriend.getRegUserId());
			params.put(userFriend.getRecommendId(), list);
		});
		return params;
	}

	@Override
	public RosDepositInfo findDepositInfoById(Integer id) {
		RosDepositInfo rosDepositInfo = this.rosDepositInfoService.findRosDepositInfoById(id);
		if(rosDepositInfo != null){
			BeanPropertiesUtil.mergeProperties(rosDepositInfo, this.bidInfoService.findBidInfoById(rosDepositInfo.getBidId()), this.regUserService.findRegUserById(rosDepositInfo.getRegUserId()));
		}
		return rosDepositInfo;
	}

	@Override
	public ResponseEntity<?> loadMyAccountMenu(RegUser regUser) {
		Integer regUserId = regUser.getId();
		//执行加载菜单
		List<String> menusStr = (List<String>) tryLoadDataFromRedis(
				join(asList(USER_MENU_PREFIX, regUser.getType(), regUser.getLogin()), "_"),
				true,
				() ->{
					List<String> userSecondMenuIds ;
					//1、特殊物业黑名单，只显示物业菜单
					if (rosInfoService.validateRoster(regUserId,RosterType.PROPERTY_OPERATE,RosterFlag.WHITE)){
						userSecondMenuIds = regUserService.findSecondMenuIdsByMenuName(UserConstants.ACCOUNT_MENU_NAME_PROPERTY);
					}else{
						//查询基础菜单--不包含物业菜单和债权转让菜单
						userSecondMenuIds =  regUserService.findRolesBindMenuIds(1);
						//2、物业账号
						if (regUser.getType() == UserConstants.USER_TYPE_TENEMENT){
							List<String> propertyMenus = regUserService.findSecondMenuIdsByMenuName(UserConstants.ACCOUNT_MENU_NAME_PROPERTY);
							userSecondMenuIds.addAll(propertyMenus);
						}
						//3、债权转让白名单
						if (rosInfoService.validateRoster(regUserId,RosterType.BOND_TRANSFER,RosterFlag.WHITE)){
							List<String> creditorMenus = regUserService.findSecondMenuIdsByMenuName(UserConstants.ACCOUNT_MENU_NAME_CREDITOR);
							userSecondMenuIds.addAll(creditorMenus);
						}
					}
					//构造用户菜单
					return AuthUtil.initJsonedMenuFromSecondMenuIds(userSecondMenuIds,MENU_TYPE_MY_ACCOUNT);
				},
				(jsonedMenus) -> cacheUserMenus(regUser, (List<String>) jsonedMenus)
		);
		return new ResponseEntity<>(Constants.SUCCESS,AuthUtil.JsonedMenuToMenuNode(menusStr));
	}

	/**
	 * 缓存用户的菜单
	 * @param currentLoginUser
	 * @param jesonedMenus
	 */
	private void cacheUserMenus(RegUser currentLoginUser, List<String> jesonedMenus) {
		//step 1:确定menuKey
		String menuKey = AuthUtil.getMenuKey(currentLoginUser);
		//step 2:存储相应的菜单数据
		try {
			JedisClusterUtils.delete(menuKey);
			JedisClusterUtils.setList(menuKey, jesonedMenus);
			JedisClusterUtils.setExpireTime(menuKey, AUTH_MENU_EXPIRE_TIME);
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("缓存菜单出错\n{}", CommonUtils.printStackTraceToString(e));
			}
		}
	}
}
