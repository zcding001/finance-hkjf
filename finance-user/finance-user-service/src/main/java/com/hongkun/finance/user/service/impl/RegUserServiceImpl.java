package com.hongkun.finance.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.user.constants.UserConstants;
import com.hongkun.finance.user.dao.*;
import com.hongkun.finance.user.dto.UserRoleDTO;
import com.hongkun.finance.user.model.*;
import com.hongkun.finance.user.model.vo.UserSimpleVo;
import com.hongkun.finance.user.model.vo.UserVO;
import com.hongkun.finance.user.service.RegUserService;
import com.hongkun.finance.user.support.AuthSecurityManager;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.CommonUtils;
import com.yirun.framework.core.utils.SpecialCodeGenerateUtils;
import com.yirun.framework.core.utils.json.JsonUtils;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.redis.JedisClusterUtils;
import com.yirun.framework.redis.RedisClusterPipeline;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.hongkun.finance.user.constants.UserConstants.*;
import static com.hongkun.finance.user.utils.BaseUtil.collectionIsEmpty;
import static com.hongkun.finance.user.utils.ValidateUtil.validatePasswd;
import static com.yirun.framework.core.commons.Constants.ERROR;
import static com.yirun.framework.core.commons.Constants.SUCCESS;
import static org.apache.commons.lang.math.NumberUtils.INTEGER_ZERO;
import static org.apache.commons.lang.math.NumberUtils.LONG_ZERO;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.user.service.impl.RegUserServiceImpl.java
 * @Class Name : RegUserServiceImpl.java
 * @Description : GENERATOR SERVICE实现类
 * @Author : generator
 */
@Service
public class RegUserServiceImpl implements RegUserService {

	private static final Logger logger = LoggerFactory.getLogger(RegUserServiceImpl.class);

	/**
	 * RegUserDAO
	 */
	@Autowired
	private RegUserDao regUserDao;
	@Autowired
	private RegUserInfoDao regUserInfoDao;
	@Autowired
	private RegUserDetailDao regUserDetailDao;
	@Autowired
	private RegUserFriendsDao regUserFriendsDao;
	@Autowired
	private RegUserFriendsGroupDao regUserFriendsGroupDao;
	@Autowired
	private RegCompanyInfoDao regCompanyInfoDao;
	@Autowired
	private SysUserRoleRelDao userRoleRelDao;
	@Autowired
	private SysRoleDao sysRoleDao;
	@Autowired
	private SysRoleMenuRelDao sysRoleMenuRelDao;

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public ResponseEntity<?> insertRegUserForCompany(RegUser regUser, RegUserDetail regUserDetail,
			RegCompanyInfo regCompanyInfo) {
		logger.info("{}", regUser);
		RegUser existUser = this.findRegUserByLogin(regUser.getLogin());
		if (existUser != null) {
			return new ResponseEntity<>(ERROR, "此手机号已注册");
		}
		// 验证身份证是否已经存在
		if (StringUtils.isNotBlank(regUserDetail.getIdCard())) {
			RegUserDetail cdt = new RegUserDetail();
			cdt.setIdCard(regUserDetail.getIdCard());
			if (Optional.ofNullable(this.regUserDetailDao.findByCondition(cdt)).orElse(new ArrayList<>()).size() > 0) {
				return ResponseEntity.error("此身份证已使用");
			}
		}
		if (regUser.getType() == UserConstants.USER_TYPE_TENEMENT) {
			String groupCode = regUserDetail.getGroupCode();
			if (StringUtils.isBlank(groupCode) || groupCode.length() != 4) {
				return new ResponseEntity<>(ERROR, "物业机构码不允许为空且为4位");
			}
			RegUserDetail exitRud = this.regUserDetailDao.findRegUserDetailByGroupCode(groupCode);
			if (exitRud != null) {
				return new ResponseEntity<>(ERROR, "当前物业推广码已经注册");
			}
		}
		this.parseInvestNo(regUser, regUserDetail);
		ResponseEntity<?> result = new ResponseEntity<>(Constants.SUCCESS);
		// 添加的企业账户时，信息中包含法人及身份证号、执照号，因此认为此用户已实名
		if (StringUtils.isNotBlank(regCompanyInfo.getLegalldNo())) {
			regUser.setIdentify(1);
		}
		this.regUserDao.save(regUser);
		RegUserInfo regUserInfo = new RegUserInfo();
		regUserInfo.setRegUserId(regUser.getId());
		this.regUserInfoDao.save(regUserInfo);
		regUserDetail.setRegUserId(regUser.getId());
		regUserDetail.setRealName(regCompanyInfo.getEnterpriseName());
		regUserDetail.setIdCard(regCompanyInfo.getLegalldNo());
		this.regUserDetailDao.save(regUserDetail);
		regCompanyInfo.setRegUserId(regUser.getId());
		this.regCompanyInfoDao.save(regCompanyInfo);
		result.getParams().put("regUser", regUser);
		return result;
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public ResponseEntity<?> updateRegUser(RegUser regUser) {
		logger.info("updateRegUser, 更新用户信息, 用户: {}, 用户信息: {}", regUser.getId(), regUser.toString());
		try {
			int count = this.regUserDao.update(regUser);
			this.clearCacheUserData(regUser.getId());
			return new ResponseEntity<>(count > 0 ? Constants.SUCCESS : Constants.ERROR, count > 0 ? regUser : "操作失败");
		} catch (Exception e) {
			logger.error("updateRegUser, 更新用户信息, 用户: {}, 用户信息: {}\n", regUser.getId(), regUser.toString(), e);
			throw new GeneralException("更新用户信息失败");
		}
	}

	@Override
	public RegUser findRegUserById(int id) {
		RegUser regUser = this.regUserDao.findByPK(Long.valueOf(id), RegUser.class);
		this.cacheUserData(regUser);
		return regUser;
	}

	@Override
	public RegUser findRegUserByLogin(Long login) {
		return this.regUserDao.findRegUserByLogin(login);
	}

	@Override
	public ResponseEntity<?> validateLoginAndPasswd(String login, String passwd) {
		ResponseEntity<?> result = new ResponseEntity<>(Constants.SUCCESS);
		if (!StringUtils.isNotBlank(login) || !StringUtils.isNotBlank(passwd)) {
			return new ResponseEntity<>(ERROR, "用户名或密码错误");
		}
		RegUser regUser = this.regUserDao.findUserWithPwdByLogin(Long.valueOf(login));
		if (regUser == null || !passwd.equalsIgnoreCase(regUser.getPasswd())) {
			return new ResponseEntity<>(ERROR, "用户名或密码错误");
		}
		if (regUser.getState() == UserConstants.USER_STATE_F) {
			return new ResponseEntity<>(ERROR, "用户已禁用");
		}
		// 刷新登录时间
		result.getParams().put("lastLoginTime", regUser.getLastLoginTime());
		regUser.setLastLoginTime(new Date());
		this.regUserDao.update(regUser);
		result.getParams().put("regUser", regUser);
		this.cacheUserData(regUser);
		return result;
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public ResponseEntity<?> insertRegUserForRegist(RegUser regUser, RegUserDetail regUserDetail,
			RegUserDetail commendRegUserDetial) {
		ResponseEntity<?> result = new ResponseEntity<>(SUCCESS);
		if (StringUtils.isBlank(regUser.getNickName())) {
			regUser.setNickName("鸿坤金服");
		}
		// 解析、设置邀请码
		this.parseInvestNo(regUser, regUserDetail);
		regUser.setType(USER_TYPE_GENERAL);
		regUser.setState(UserConstants.USER_STATE_Y);
		this.regUserDao.save(regUser);
		RegUserInfo regUserInfo = new RegUserInfo();
		regUserInfo.setRegUserId(regUser.getId());
		this.regUserInfoDao.save(regUserInfo);
		regUserDetail.setRegUserId(regUser.getId());
		this.regUserDetailDao.save(regUserDetail);
		// 维护好友推荐关系
//		if (commendRegUserDetial != null) {
//			List<RegUserFriends> regUserFriendsList = new ArrayList<>();
//			RegUserFriends regUserFriends = new RegUserFriends();
//			regUserFriends.setRegUserId(regUser.getId());
//			regUserFriends.setRealName(regUserDetail.getRealName());
//			regUserFriends.setTel(String.valueOf(regUser.getLogin()));
//			regUserFriends.setRecommendId(commendRegUserDetial.getId());
//			regUserFriends.setGrade(1);
//			if (StringUtils.isNotBlank(commendRegUserDetial.getCommendNo())) {
//				RegUserDetail twoLevelFriendRegUserDetail = this.regUserDetailDao
//						.findRegUserDetailByInviteNo(commendRegUserDetial.getCommendNo());
//				if (twoLevelFriendRegUserDetail != null) {
//					RegUserFriends twoRegUserFriends = new RegUserFriends();
//					BeanUtils.copyProperties(regUserFriends, twoRegUserFriends);
//					twoRegUserFriends.setRecommendId(twoLevelFriendRegUserDetail.getId());
//					twoRegUserFriends.setGrade(2);
//					regUserFriendsList.add(twoRegUserFriends);
//				}
//			}
//			regUserFriendsList.add(regUserFriends);
//			this.regUserFriendsDao.insertBatch(RegUserFriends.class, regUserFriendsList, regUserFriendsList.size());
//		}
		result.getParams().put("lastLoginTime", new Date());
		result.getParams().put("regUser", regUser);
		result.getParams().put("regUserDetail", regUserDetail);
		return result;
	}

	/**
	 * @param regUser
	 * @param regUserDetail
	 * @return : void
	 * @Description : 解析邀请码
	 * @Method_Name : parseInvestNo
	 * @Creation Date : 2017年6月2日 上午11:03:56
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	private void parseInvestNo(RegUser regUser, RegUserDetail regUserDetail) {
		// 设置推荐码
		String investNo = String.valueOf(regUser.getLogin()).substring(3);
		while (true) {
			RegUserDetail existRegUserDetial = this.regUserDetailDao.findRegUserDetailByInviteNo(investNo);
			if (existRegUserDetial == null) {
				regUserDetail.setInviteNo(investNo);
				break;
			}
			investNo = SpecialCodeGenerateUtils.getSpecialNumCode(8);
		}
	}

	@Override
	public List<Integer> findUserIdsByTel(Long userTel) {
		if (userTel == null) {
			return new ArrayList<>();
		}
		return this.regUserDao.findUserIdsByTel(userTel);
	}

	@Override
	public UserVO findRegUserTelAndRealNameById(Integer senderUserId) {
		if (senderUserId == null) {
			return null;
		}
		return this.regUserDao.findRegUserTelAndRealNameById(senderUserId);
	}

	@Override
	public Pager listConditionPage(UserVO userVO, Pager pager) {
		return this.regUserDao.listConditionPage(userVO, pager);
	}

	@Override
	public UserVO findUserWithDetailById(Integer userId) {
		return this.regUserDao.findUserWithDetailById(userId);
	}

	@Override
	public UserVO findUserWithDetailByLogin(Long login) {
		UserVO uservo = new UserVO();
		uservo.setLogin(login);
		List<UserVO> userVos = this.regUserDao.findUserWithDetailByInfo(uservo);
		return CommonUtils.isNotEmpty(userVos) ? userVos.get(0) : null;
	}

	@Override
	public List<UserVO> findUserWithDetailByInfo(UserVO userVO) {
		return this.regUserDao.findUserWithDetailByInfo(userVO);
	}

	@Override
	public List<UserVO> findUserWithDetailByInfo(List<Integer> regUserIds) {
		UserVO userVO = new UserVO();
		userVO.setUserIds(regUserIds);
		return this.regUserDao.findUserWithDetailByInfo(userVO);
	}

	@Override
	public List<Integer> findUserIdsByFuzzyName(String userName) {
		if (StringUtils.isEmpty(userName)) {
			return null;
		}
		return this.regUserDao.findUserIdsByFuzzyName(userName);
	}

	@Override
	public List<RegUser> findRegUserList(RegUser regUser) {
		return this.regUserDao.findByCondition(regUser);
	}

	@Override
	public List<Integer> findUserIdsByUserVO(UserVO userVO) {
		return this.regUserDao.findUserIdsByUserVO(userVO);
	}

	@Override
	public List<Map<String, Object>> findPropertyDicDataList() {
		return this.regUserDao.findPropertyDicDataList();
	}

	@Override
	public Pager findRegUserList(RegUser regUser, Pager pager) {
		Pager result = regUserDao.findByCondition(regUser, pager);
		return result;
	}

	/**
	 * @Description ：保存后者更新后台用户
	 * @Method_Name ：saveOrUpdateOperator
	 * @param unSavedOrUnUpdateUser
	 * @param email
	 * @return com.yirun.framework.core.model.ResponseEntity<?>
	 * @Creation Date ：2018/4/24
	 * @Author ：zhongpingtang@hongkun.com.cn
	 */
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public ResponseEntity<?> saveOrUpdateOperator(RegUser unSavedOrUnUpdateUser, String email) {
		if (logger.isInfoEnabled()) {
			logger.info("方法名: saveOrUpdateOperator, 保存后台用户, 后台用户信息: {}, email: {}", unSavedOrUnUpdateUser, email);
		}
		try {
			// 判断是否保存的状态
			boolean doSaveFlag = unSavedOrUnUpdateUser.getId() == null || unSavedOrUnUpdateUser.getId() <= 0;
			boolean noDeleteUser = false;
			// 添加
			if (doSaveFlag) {
				// 判断添加的管理员是否存在，并且正在使用
				if (operatorIsExit(unSavedOrUnUpdateUser)) {
					return new ResponseEntity<>(Constants.ERROR, "后台登录账号已存在");
				}
				// 再去判断是否有与此号码相同的并且已经删除的账号，如果有，这个账号的信息覆盖原来账号的信息
				RegUser validateHasDeleteUser = new RegUser();
				validateHasDeleteUser.setLogin(unSavedOrUnUpdateUser.getLogin());
				validateHasDeleteUser.setType(USER_TYPE_CONSOLE);
				List<RegUser> deletedUserList = regUserDao.findDeletedUser(validateHasDeleteUser);
				// 删除
				if (noDeleteUser = BaseUtil.collectionIsEmpty(deletedUserList)) {
					// 不存在此种账号，直接添加
					this.regUserDao.save(unSavedOrUnUpdateUser);
				} else {
					// 覆盖原来已经删除的账号
					RegUser deleteUser = deletedUserList.get(0);
					// 设置id，设置状态
					unSavedOrUnUpdateUser.setId(deleteUser.getId());
					unSavedOrUnUpdateUser.setState(USER_STATE_Y);
					Date currentDate = new Date();
					unSavedOrUnUpdateUser.setCreateTime(currentDate);
					unSavedOrUnUpdateUser.setModifyTime(currentDate);

					// 执行更新
					this.regUserDao.update(unSavedOrUnUpdateUser);
					this.clearCacheUserData(unSavedOrUnUpdateUser.getId());
				}

			} else {
				// 执行更新
				this.regUserDao.update(unSavedOrUnUpdateUser);
				this.clearCacheUserData(unSavedOrUnUpdateUser.getId());
			}

			// 更新RegUserInfo
			RegUserInfo regUserInfo = new RegUserInfo();
			regUserInfo.setRegUserId(unSavedOrUnUpdateUser.getId());
			regUserInfo.setEmail(email);

			if (doSaveFlag && noDeleteUser) {
				this.regUserInfoDao.save(regUserInfo);
			} else {
				if (!noDeleteUser) {
					// 更新为当前时间，就像新创建的一样
					regUserInfo.setCreateTime(new Date());
				}
				if (StringUtils.isNotBlank(email)) {
					regUserInfo.setState(USER_STATE_Y);
					this.regUserInfoDao.updateByRegUserId(regUserInfo);
				}
			}
			
			RegUserDetail regUserDetail = new RegUserDetail();
			regUserDetail.setRegUserId(unSavedOrUnUpdateUser.getId());

            if (doSaveFlag && noDeleteUser) {
                this.regUserDetailDao.save(regUserDetail);
            } else {
                if (StringUtils.isNotBlank(email)) {
                    this.regUserDetailDao.update(regUserDetail);
                }
            }
			return ResponseEntity.SUCCESS;
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("保存后台用户失败, 用户信息: {}, email: {}\n异常信息: ", unSavedOrUnUpdateUser, email, e);
			}
			throw new GeneralException("保存后台用户失败,请重试");
		}
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public ResponseEntity deleteOperator(RegUser regUser) {
		if (logger.isInfoEnabled()) {
			logger.info("方法名: deleteOperator, 删除后台用户, 删除用户: {}", regUser);
		}
		try {
			/**
			 * step 1:删除用户和角色的关联关系
			 */
			Integer userId = regUser.getId();
			userRoleRelDao.clearUserRoleRelByUserIdOrRoleId(new UserRoleDTO(Arrays.asList(userId), null));
			/**
			 * step 2:删除userIfo信息和 regUser信息
			 */
			regUser.setState(USER_STATE_N);
			regUserDao.update(regUser);
			regUserInfoDao.deleteUserInfoByUserId(userId);

			// 清除用户缓存
			this.clearCacheUserData(regUser.getId());
			return ResponseEntity.SUCCESS;
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("删除后台用户失败, 用户信息: {}\n异常信息: ", regUser, e);
			}
			throw new GeneralException("删除后台用户失败,请重试");
		}
	}

	@Override
	public ResponseEntity bindRolesToUser(Integer userId, Set<Integer> roleIds, Integer loginUserId) {
		if (logger.isInfoEnabled()) {
			logger.info("方法名: bindRolesToUser, 给后台用户分配角色, 目标用户ID: {}, 角色IDS: {}", userId, roleIds);
		}
		try {
			// 分配鉴权
			ResponseEntity validteResult = AuthSecurityManager.checkUserMenusWithOprateRoles(regUserDao,
					sysRoleMenuRelDao, loginUserId, roleIds);
			if (!validteResult.validSuc()) {
				return validteResult;
			}
			// 执行分配角色
			return ((collectionIsEmpty(roleIds)
					? userRoleRelDao.clearUserRoleRelByUserIdOrRoleId(new UserRoleDTO(Arrays.asList(userId), null))
					: dobindRoleToUser(userId, roleIds)) > INTEGER_ZERO) ? ResponseEntity.SUCCESS
							: ResponseEntity.ERROR;
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("给后台用户分配角色失败, 目标用户ID: {}, 角色IDS: {}\n异常信息: ", userId, roleIds, e);
			}
			throw new GeneralException("给后台用户分配角色失败,请重试");
		}
	}

	@Override
	public List<SysRole> findRoleBindToUser(Integer userId) {
		return userRoleRelDao.findByCondition(new SysUserRoleRel(userId, null)).stream().map(e -> e.getSysRoleId())
				.map(k -> sysRoleDao.findByPK(Long.valueOf(k), SysRole.class)).collect(Collectors.toList());
	}

	/**
	 * 执行吧roleIds绑定到user
	 *
	 * @param userId
	 * @param roleIds
	 * @return
	 */
	private Integer dobindRoleToUser(Integer userId, Set<Integer> roleIds) {
		SysUserRoleRel userRoleRel = new SysUserRoleRel();
		userRoleRel.setRegUserId(userId);
		return RelFunction.reBindRel(
				userRoleRelDao.findByCondition(userRoleRel).stream().map(e -> e.getSysRoleId())
						.collect(Collectors.toList()),
				roleIds,
				unDeleteIds -> userRoleRelDao.clearUserRoleRelByUserIdOrRoleId(new UserRoleDTO(null, unDeleteIds)),
				unSaveId -> userRoleRelDao.save(new SysUserRoleRel(userId, unSaveId)));
	}

	@Override
	public List<Integer> findUserAuthIdByMenus(List<Integer> userValidMenu) {
		return collectionIsEmpty(userValidMenu) ? Collections.emptyList()
				: regUserDao.findUserAuthIdByMenus(userValidMenu);
	}

	@Override
	public List<Integer> lookUpUserMenuIds(Long login, Integer type,Integer sysType) {
			return regUserDao.findUserMenuByLogin(String.valueOf(login), type,sysType);
	}

	@Override
		public List<String> findRolesBindMenuIds(Integer constantId) {
			return sysRoleMenuRelDao.findRolesBindMenuIds(constantId).stream().map(String::valueOf)
					.collect(Collectors.toList());
	}

	@Override
	public List<String> findUserMenuIdsWithTemplateBackUp(RegUser loginUser) {
		List<SysUserRoleRel> rolList = (List<SysUserRoleRel>) userRoleRelDao.findByCondition(new SysUserRoleRel(loginUser.getId(),null));
		List<Integer> roleIds = new ArrayList<Integer>();
		if (CommonUtils.isNotEmpty(rolList)){
			rolList.forEach(userRole->{
				roleIds.add(userRole.getSysRoleId());
			});
			if (loginUser.getType() == UserConstants.USER_TYPE_GENERAL){
				roleIds.add(UserConstants.USER_PRE_BASIC_ROLE_ID);
			}
			return sysRoleMenuRelDao.findMenuByRoleIds(roleIds).stream().map(String::valueOf).distinct()
					.collect(Collectors.toList());
		}
		return findRolesBindMenuIds(loginUser.getType());
//		return (userRoleRelDao.getTotalCount(new SysUserRoleRel(loginUser.getId(), null)) > INTEGER_ZERO)
//			? regUserDao.findUserMenuByLogin(String.valueOf(loginUser.getLogin()), loginUser.getType()).stream()
//				.map(String::valueOf).collect(Collectors.toList())
//			: findRolesBindMenuIds(loginUser.getType());
}

	/**
	 * 验证后台用户是否存在
	 *
	 * @param regUser
	 * @return
	 */
	private boolean operatorIsExit(RegUser regUser) {
		return regUserDao.findOperatorCount(regUser) > LONG_ZERO;
	}

	@Override
	public ResponseEntity findPointReceiveUserByTel(int userId, String tel) {
		Map<String, Object> result = new HashMap<>();
		RegUser receiveRegUser = this.regUserDao.findRegUserByLogin(Long.parseLong(tel));
		if (receiveRegUser == null) {
			return ResponseEntity.error("该用户未注册！");
		}
		if (receiveRegUser.getId() == userId) {
			return new ResponseEntity(ERROR, "不能添加自己为积分接收人！");
		}
		String name = regUserDetailDao.findRegUserDetailNameByRegUserId(receiveRegUser.getId());
		name = StringUtils.isBlank(name) ? "未实名" : "*" + name.substring(1);
		result.put("tel", tel);
		result.put("name", name);
		result.put("id", receiveRegUser.getId());
		return new ResponseEntity(SUCCESS, result);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public ResponseEntity<?> updateCommendNo(String ids, String commendNo) {
		// 验证推荐码是否有效
		RegUserDetail regUserDetail = this.regUserDetailDao.findRegUserDetailByInviteNo(commendNo);
		if (regUserDetail == null || regUserDetail.getId() < 0) {
			return new ResponseEntity<>(Constants.ERROR, "无效的邀请码");
		}
		RegUserDetail twoRegUserDetailTmp = null;
		if(StringUtils.isNotBlank(regUserDetail.getCommendNo())){
            twoRegUserDetailTmp = this.regUserDetailDao.findRegUserDetailByInviteNo(regUserDetail.getCommendNo());
        }
        final RegUserDetail twoRegUserDetail = twoRegUserDetailTmp;
		// 查询需要更新的用户
		UserVO userVO = new UserVO();
		userVO.setUserIds(Arrays.asList(ids.split(",")).stream().map(Integer::parseInt).collect(Collectors.toList()));
		List<UserVO> list = this.findUserWithDetailByInfo(userVO);
		// 要删除就的推荐关系
		List<RegUserFriends> regUserFriendsList = new ArrayList<>();
		list.forEach(vo -> {
			RegUserFriends regUserFriends = new RegUserFriends();
			regUserFriends.setRegUserId(vo.getUserId());
			regUserFriends.setState(1);
			List<RegUserFriends> delList = this.regUserFriendsDao.findByCondition(regUserFriends);
			delList.forEach(e -> e.setState(0));
			regUserFriendsList.addAll(delList);
		});
		this.regUserFriendsDao.updateBatch(RegUserFriends.class, regUserFriendsList, regUserFriendsList.size());
		// 要更新用户详情
		List<RegUserDetail> regUserDetailList = new ArrayList<>();
		list.forEach(vo -> {
			RegUserDetail detial = new RegUserDetail();
			detial.setRegUserId(vo.getUserId());
			detial.setCommendNo(commendNo);
			regUserDetailList.add(detial);
		});
		this.regUserDetailDao.updateBatch(RegUserDetail.class, regUserDetailList, regUserDetailList.size());
		// 维护信息推荐关系
		List<RegUserFriends> newRegUserFriendsList = new ArrayList<>();
		list.forEach(vo -> {
            //判断自己的投资状态
            RegUserFriends cdt = new RegUserFriends();
            cdt.setRegUserId(vo.getUserId());
            cdt.setState(UserConstants.USER_INVEST_STATE_YES);
            //如果含有已投资的好友关系，说明当前用户已经投资
            int investState = regUserFriendsDao.getTotalCount(cdt);
            //获取一级好友关系
			newRegUserFriendsList.add(this.getRegUserFriends(vo, regUserDetail, UserConstants.USER_FRIEND_GRADE_FIRST, investState));
			// 设置二级推荐人关系
			if (twoRegUserDetail != null && twoRegUserDetail.getId() > 0) {
				newRegUserFriendsList.add(this.getRegUserFriends(vo, twoRegUserDetail, UserConstants.USER_FRIEND_GRADE_SECOND, investState));
			}
		});
		this.regUserFriendsDao.insertBatch(RegUserFriends.class, newRegUserFriendsList, newRegUserFriendsList.size());
		return new ResponseEntity<>(Constants.SUCCESS);
	}
	
	/**
	*  初始化好友关系
	*  @Method_Name             ：getRegUserFriends
	*  @param vo
	*  @param regUserDetail
	*  @param grade
	*  @param investState
	*  @return com.hongkun.finance.user.model.RegUserFriends
	*  @Creation Date           ：2018/7/16
	*  @Author                  ：zc.ding@foxmail.com
	*/
	private RegUserFriends getRegUserFriends(UserVO vo, RegUserDetail regUserDetail, final Integer grade, final int investState){
        //查询一级好友的分组id
        List<RegUserFriendsGroup> groupList = this.regUserFriendsGroupDao.findGroupsAndNumByUserId(regUserDetail.getRegUserId());
        RegUserFriends regUserFriends = new RegUserFriends();
        regUserFriends.setRegUserId(vo.getUserId());
        regUserFriends.setRealName(vo.getRealName());
        regUserFriends.setTel(String.valueOf(vo.getLogin()));
        regUserFriends.setRecommendId(regUserDetail.getRegUserId());
        regUserFriends.setGrade(grade);
        groupList.stream().filter(o -> {
            //查找已投资的组id
            if(investState > 0){
                return UserConstants.USER_FRIENDS_GROUP_TYPE_INVEST == o.getType();
            }else{
                return UserConstants.USER_FRIENDS_GROUP_TYPE_NOT_INVEST == o.getType();
            }
        }).findAny().ifPresent(o -> regUserFriends.setGroupId(o.getId()));
        return regUserFriends;
    }

	/**
	 * @param regUser
	 * @return : void
	 * @Description : 缓存用户信息
	 * @Method_Name : cacheUserInfo
	 * @Creation Date : 2017年5月24日 下午2:17:41
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	private void cacheUserData(RegUser regUser) {
		// 缓存regUserDetail
		RegUserDetail regUserDetail = this.regUserDetailDao.findRegUserDetailByRegUserId(regUser.getId());
		// 缓存regUserInfo
		RegUserInfo regUserInfo = this.regUserInfoDao.findRegUserInfoByRegUserId(regUser.getId());
		this.cacheUserData(regUser, regUserDetail, regUserInfo);
	}

	/**
	 * @param regUser
	 * @param regUserDetail
	 * @param regUserInfo
	 * @return : void
	 * @Description : 缓存用户信息
	 * @Method_Name : cacheUserInfo
	 * @Creation Date : 2017年5月24日 下午2:20:43
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	private void cacheUserData(RegUser regUser, RegUserDetail regUserDetail, RegUserInfo regUserInfo) {
		JedisClusterUtils.setAsJson(RegUser.class.getSimpleName() + regUser.getId(), regUser);
		// 管理员无此信息
		if ((!regUser.getType().equals(USER_TYPE_CONSOLE)) && !regUser.getType().equals(UserConstants.USER_TYPE_ROOT)) {
			// 缓存regUserDetail
			JedisClusterUtils.setAsJson(RegUserDetail.class.getSimpleName() + regUserDetail.getRegUserId(),
					regUserDetail);
			// 缓存regUserInfo
			JedisClusterUtils.setAsJson(RegUserInfo.class.getSimpleName() + regUserInfo.getRegUserId(), regUserInfo);
		}

	}

	/**
	 * @param regUserId:
	 * @return : void
	 * @Description : 清空缓存
	 * @Method_Name : clearCacheUserData
	 * @Creation Date : 2017年12月29日 下午4:50:46
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	private void clearCacheUserData(Integer regUserId) {
		JedisClusterUtils.delete(RegUser.class.getSimpleName() + regUserId);
		JedisClusterUtils.delete(RegUserDetail.class.getSimpleName() + regUserId);
		JedisClusterUtils.delete(RegUserInfo.class.getSimpleName() + regUserId);
	}

	@Override
	public List<UserSimpleVo> findUserSimpleVoList(List<Integer> regUserIds) {
		return this.regUserDao.findUserSimpleVoList(regUserIds);
	}

	@Override
	public ResponseEntity changeOperatorPasswd(RegUser currentUser, String newPasswd) {
		ResponseEntity validateResult = validatePasswd(newPasswd);
		if (validateResult.getResStatus() == SUCCESS) {
			currentUser.setPasswd((String) validateResult.getResMsg());
			// 更新账户密码
			return saveOrUpdateOperator(currentUser, "");
		}
		// 密码验证不通过
		return validateResult;
	}

	@Override
	public RegUser findUserWithPwdByLogin(Long login) {
		return this.regUserDao.findUserWithPwdByLogin(login);
	}

	@Override
	public Map<Integer, UserSimpleVo> findUserSimpleVoByIdList(Set<Integer> userIdSet) {
		return this.regUserDao.findUserSimpleVoByIdList(userIdSet);
	}

	@Override
	public List<String> findEmailsByRegUserIds(Set<Integer> regUserIds) {
		return this.regUserDao.findEmailsByRegUserIds(regUserIds);
	}

	@Override
	public List<Integer> findCommonRecommendUser(List<Integer> otherRoleRegUserId) {
		return this.regUserDao.findCommonRecommendUser(otherRoleRegUserId);
	}

	@Override
	public Pager findRegUserVipRecordList(UserVO userVO, Pager pager) {
		return this.regUserDao.findRegUserVipRecordList(userVO, pager);
	}

    @Override
    public RegUser findCommendRegUserById(Integer regUserId) {
	    RegUserDetail regUserDetail = BaseUtil.getRegUserDetail(regUserId, () -> this.regUserDetailDao.findRegUserDetailByRegUserId(regUserId));
	    if(regUserDetail != null && StringUtils.isNotBlank(regUserDetail.getCommendNo())){
            RegUserDetail commendRegUserDetail = this.regUserDetailDao.findRegUserDetailByInviteNo(regUserDetail.getCommendNo());
            if(commendRegUserDetail != null){
                return BaseUtil.getRegUser(commendRegUserDetail.getRegUserId(), () -> this.regUserDao.findByPK(Long.valueOf(commendRegUserDetail.getRegUserId()), RegUser.class));
            }
        }
        return null;
    }

	@Override
	public List<String> findSecondMenuIdsByMenuName(String menuName) {
		return regUserDao.findAccountSecondMenuIdsByMenuName(menuName).stream().map(String::valueOf).collect(Collectors.toList());
	}

	@Override
	public ResponseEntity<?> getUserInfoByInviteNo(String inviteNo) {
		UserVO userVO = this.regUserDao.getUserInfoByInviteNo(inviteNo);
		if (userVO == null){
			return new ResponseEntity<>(ERROR);
		}
		return new ResponseEntity<>(SUCCESS,userVO.getLogin());
	}

    @Override
    public ResponseEntity<?> updateSpreadSource(Integer regUserId, String groupCode) {
        RegUserDetail regUserDetail=regUserDetailDao.findRegUserDetailByRegUserId(regUserId);
        if(regUserDetail == null){
            return new ResponseEntity<>(ERROR); 
        }
        RegUserDetail userDetail =new RegUserDetail();
        userDetail.setRegUserId(regUserId);
        userDetail.setExtenSource(groupCode);
        regUserDetailDao.update(userDetail);
        return new ResponseEntity<>(SUCCESS);
    }

    @Override
    public Map<Integer, RegUser> findRegUserByIds(List<Integer> ids) {
        RegUser regUser = new RegUser();
        regUser.setUserIds(ids);
        List<RegUser> list = this.regUserDao.findByCondition(regUser);
        this.cacheUserData(ids, list, null, null);
        return list.stream().collect(Collectors.toMap(RegUser::getId, value -> value, (k1, k2) -> k1));
    }

    @Override
    public Map<Integer, RegUserDetail> findRegUserDetailByIds(List<Integer> regUserIds) {
        RegUserDetail regUserDetail = new RegUserDetail();
        regUserDetail.setUserIds(regUserIds);
        List<RegUserDetail> list = this.regUserDetailDao.findByCondition(regUserDetail);
        this.cacheUserData(regUserIds, null, list, null);
        return list.stream().collect(Collectors.toMap(RegUserDetail::getRegUserId, value -> value, (k1, k2) -> k1));
    }

    @Override
    public Map<Integer, RegUserInfo> findRegUserInfoByIds(List<Integer> regUserIds) {
        RegUserInfo regUserInfo = new RegUserInfo();
        regUserInfo.setUserIds(regUserIds);
        List<RegUserInfo> list = this.regUserInfoDao.findByCondition(regUserInfo);
        this.cacheUserData(regUserIds, null, null, list);
        return list.stream().collect(Collectors.toMap(RegUserInfo::getRegUserId, value -> value, (k1, k2) -> k1));
    }
    
    /**
    *  批量缓存用户信息
    *  @param ids   用户ID集合
    *  @param regUsers  用户集合
    *  @param regUserDetails    用户详情
    *  @param regUserInfos  用户信息
    *  @date                    ：2019/1/28
    *  @author                  ：zc.ding@foxmail.com
    */
    private void cacheUserData(List<Integer> ids, List<RegUser> regUsers, List<RegUserDetail> regUserDetails, List<RegUserInfo> regUserInfos) {
	    if(CommonUtils.isNotEmpty(ids)){
	        if(CommonUtils.isEmpty(regUsers)){
                RegUser regUser = new RegUser();
                regUser.setUserIds(ids);
                regUsers = this.regUserDao.findByCondition(regUser);
            }
            if(CommonUtils.isEmpty(regUserDetails)){
                RegUserDetail regUserDetail = new RegUserDetail();
                regUserDetail.setUserIds(ids);
                regUserDetails = this.regUserDetailDao.findByCondition(regUserDetail);
            }
            if(CommonUtils.isEmpty(regUserInfos)){
                RegUserInfo regUserInfo = new RegUserInfo();
                regUserInfo.setUserIds(ids);
                regUserInfos = this.regUserInfoDao.findByCondition(regUserInfo);
            }
            try(RedisClusterPipeline pipeline = RedisClusterPipeline.pipeline(JedisClusterUtils.getJedisCluster())){
                regUsers.forEach(o -> pipeline.set(RegUser.class.getSimpleName() + o.getId(), JsonUtils.toJson(o)));
                regUserDetails.forEach(o -> pipeline.set(RegUserDetail.class.getSimpleName() + o.getId(), JsonUtils.toJson(o)));
                regUserInfos.forEach(o -> pipeline.set(RegUserInfo.class.getSimpleName() + o.getId(), JsonUtils.toJson(o)));
            }
        }
    }
}
