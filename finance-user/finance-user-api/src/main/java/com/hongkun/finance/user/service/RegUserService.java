package com.hongkun.finance.user.service;

import com.hongkun.finance.user.model.*;
import com.hongkun.finance.user.model.vo.UserSimpleVo;
import com.hongkun.finance.user.model.vo.UserVO;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.user.service.RegUserService.java
 * @Class Name : RegUserService.java
 * @Description : GENERATOR SERVICE类
 * @Author : generator
 */
public interface RegUserService {

	/**
	 * @Description : 添加企业账户
	 * @Method_Name : insertRegUserForCompany
	 * @param regUser
	 * @param regUserDetail
	 * @return
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年6月2日 上午10:25:56
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	ResponseEntity<?> insertRegUserForCompany(RegUser regUser, RegUserDetail regUserDetail,
			RegCompanyInfo regCompanyInfo);

	/**
	 * @Described : 更新数据
	 * @param regUser
	 *            要更新的数据
	 * @return : void
	 */
	ResponseEntity<?> updateRegUser(RegUser regUser);

	/**
	 * @Described : 通过id查询数据
	 * @param id
	 *            id值
	 * @return RegUser
	 */
	RegUser findRegUserById(int id);

	/**
	 * 通过登录凭证（手机号）查询用户信息
	 * 
	 * @author zc.ding
	 * @since 2017年5月19日
	 * @param login
	 *            手机号
	 * @return
	 */
	RegUser findRegUserByLogin(Long login);

	/**
	 * 验证用户名密码的正确性
	 * 
	 * @author zc.ding
	 * @since 2017年5月18日
	 * @param login
	 * @param passwd
	 * @return
	 */
	ResponseEntity<?> validateLoginAndPasswd(String login, String passwd);

	/**
	 * @Description : 注册用户信息
	 * @Method_Name : registUser
	 * @param regUser
	 * @param regUserDetail
	 *            : 注册用户详情
	 * @param commendRegUserDetial
	 *            :推荐用户详情
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年5月22日 下午6:49:52
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	ResponseEntity<?> insertRegUserForRegist(RegUser regUser, RegUserDetail regUserDetail,
			RegUserDetail commendRegUserDetial);

	/**
	 * 根据手机号查id
	 * 
	 * @param senderTel
	 * @return
	 */
	List<Integer> findUserIdsByTel(Long senderTel);

	/**
	 * 根据用户id查询用户的手机号
	 * 
	 * @param senderUserId
	 * @return
	 */
	UserVO findRegUserTelAndRealNameById(Integer senderUserId);

	/**
	 * 根据reguser和reguserDetail的信息来查询相关的分页数据
	 * 
	 * @param userVO
	 * @param pager
	 * @return
	 */
	Pager listConditionPage(UserVO userVO, Pager pager);

	/**
	 * @Description : 通过id查询用户信息（联表）
	 * @Method_Name : findUserWithDetailById
	 * @param userId
	 * @return
	 * @return : UserVO
	 * @Creation Date : 2017年7月4日 下午2:29:40
	 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	UserVO findUserWithDetailById(Integer userId);

	UserVO findUserWithDetailByLogin(Long login);

	/**
	 * @Description : 通过条件查询用户信息
	 * @Method_Name : findUserWithDetailByInfo;
	 * @param userVO
	 * @return
	 * @return : List<UserVO>;
	 * @Creation Date : 2017年7月20日 上午9:48:06;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	List<UserVO> findUserWithDetailByInfo(UserVO userVO);

	/**
	 * @Description : 通过用户id查询用户信息
	 * @Method_Name : findUserWithDetailByInfo;
	 * @param regUserIds
	 *            用户id集合
	 * @return
	 * @return : List<UserVO>;
	 * @Creation Date : 2018年4月17日 下午17:00:06;
	 * @Author : xuhui.liu@hongkun.com.cn 刘旭辉
	 */
	List<UserVO> findUserWithDetailByInfo(List<Integer> regUserIds);

	/**
	 * 根据用户姓名模糊查询用户名id
	 * 
	 * @param userName
	 * @return
	 */
	List<Integer> findUserIdsByFuzzyName(String userName);

	/**
	 * 
	 * @Description : 根据条件查询用户id
	 * @Method_Name : findUserIdsByUserVO
	 * @param userVO
	 * @return
	 * @return : List<Integer>
	 * @Creation Date : 2017年10月10日 上午10:20:35
	 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	List<Integer> findUserIdsByUserVO(UserVO userVO);

	/**
	 * @Description : 获取物业公司列表（用于数据字典）
	 * @Method_Name : findPropertyDicDataList
	 * @return
	 * @return : List<CommonDicData>
	 * @Creation Date : 2017年10月11日 上午11:34:41
	 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	List<Map<String, Object>> findPropertyDicDataList();

	List<RegUser> findRegUserList(RegUser regUser);

	/**
	 * 查询用户的分页数据
	 * 
	 * @param regUser
	 * @param pager
	 * @return
	 */
	Pager findRegUserList(RegUser regUser, Pager pager);

	/**
	 * 保存后台用户
	 * 
	 * @param regUser
	 * @param email
	 * @return
	 */
	ResponseEntity<?> saveOrUpdateOperator(RegUser regUser, String email);

	/**
	 * 删除后台用户
	 * 
	 * @param id
	 * @return
	 */
	ResponseEntity deleteOperator(RegUser id);

	/**
	 * 给后台用户分配角色
	 * 
	 * @param userId
	 * @param roleIds
	 * @param loginUserId
	 * @return
	 */
	ResponseEntity bindRolesToUser(Integer userId, Set<Integer> roleIds, Integer loginUserId);

	/**
	 * 查询已经绑定到用户的角色
	 * 
	 * @param userId
	 * @return
	 */
	List<SysRole> findRoleBindToUser(Integer userId);

	/**
	 * 查询用户权限
	 * 
	 * @param userValidMenu
	 * @return
	 */
	List<Integer> findUserAuthIdByMenus(List<Integer> userValidMenu);

	List<Integer> lookUpUserMenuIds(Long login, Integer type,Integer sysType);

	List<String> findRolesBindMenuIds(Integer userTypeGeneral);

	List<String> findUserMenuIdsWithTemplateBackUp(RegUser loginUser);

	/**
	 * @Description : 根据手机号积分转赠用户信息，(模糊的姓名，手机号，id)例如：*鹏，17001279697,66
	 * @Method_Name : findPointReceiveUserByTel
	 * @param userId
	 *            当前登录人id
	 * @param tel
	 *            积分接收人手机号
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年12月25日 下午15:52:27
	 * @Author : pengwu@hongkun.com.cn
	 */
	ResponseEntity findPointReceiveUserByTel(int userId, String tel);

	/**
	 * @Description : 更新用户的推荐人
	 * @Method_Name : updateCommendNo
	 * @param ids
	 *            用户id集合
	 * @param commendNo
	 *            新的推荐人信息
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年10月10日 下午5:30:56
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	ResponseEntity<?> updateCommendNo(String ids, String commendNo);

	/**
	 * @Description : 查询用户基础信息
	 * @Method_Name : findUserSimpleVoList
	 * @param regUserIds
	 * @return
	 * @return : List<UserSimpleVo>
	 * @Creation Date : 2018年1月12日 下午2:47:49
	 * @Author : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	List<UserSimpleVo> findUserSimpleVoList(List<Integer> regUserIds);

	/**
	 * 后台用户修改密码
	 * 
	 * @param currentUser
	 * @param newPasswd
	 * @return
	 */
	ResponseEntity changeOperatorPasswd(RegUser currentUser, String newPasswd);

	/**
	 * @Description : 查询含有密码的用户信息
	 * @Method_Name : findUserWithPwdByLogin
	 * @param login
	 * @return : RegUser
	 * @Creation Date : 2018年3月6日 下午4:01:05
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	RegUser findUserWithPwdByLogin(Long login);

	/**
	 * @Description ：根据用户id集合获取用户信息
	 * @Method_Name ：findUserSimpleVoByIdList
	 * @param userIdSet
	 *            用户id集合
	 * @return java.util.Map
	 *         <java.lang.Integer,com.hongkun.finance.user.model.vo.UserSimpleVo>
	 * @Creation Date ：2018/4/18
	 * @Author ：pengwu@hongkun.com.cn
	 */
	Map<Integer, UserSimpleVo> findUserSimpleVoByIdList(Set<Integer> userIdSet);

	/**
	 * @Description ：根据userid获取用户邮箱
	 * @Method_Name ：findEmailsByRegUserIds
	 * @param regUserIds
	 * @return java.util.List<java.lang.String>
	 * @Creation Date ：2018/5/3
	 * @Author ：xuhui.liu@hongkun.com.cn 刘旭辉
	 */
	List<String> findEmailsByRegUserIds(Set<Integer> regUserIds);

	/**
	 * @Description : 查询普通推荐用户集合
	 * @Method_Name : findCommonRecommendUser;
	 * @param otherRoleRegUserId
	 *            其它用户角色集合
	 * @return
	 * @return : List<Integer>;
	 * @Creation Date : 2018年5月9日 下午5:32:09;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	List<Integer> findCommonRecommendUser(List<Integer> otherRoleRegUserId);
	
	/**
	*  查询VIP用户降级记录
	*  @Method_Name             ：findRegUserVipRecordList
	*  @param userVO
	*  @param pager
	*  @return com.yirun.framework.core.utils.pager.Pager
	*  @Creation Date           ：2018/6/7
	*  @Author                  ：zhichaoding@hongkun.com.cn
	*/
    Pager findRegUserVipRecordList(UserVO userVO, Pager pager);
    
    /**
    *  查询用户推荐人信息
    *  @Method_Name             ：findCommendRegUserById
    *  @param regUserId
    *  @return com.hongkun.finance.user.model.RegUser
    *  @Creation Date           ：2018/8/1
    *  @Author                  ：zc.ding@foxmail.com
    */
    RegUser findCommendRegUserById(Integer regUserId);

	/**
	 *  @Description    ：通过一级菜单名称查询二级菜单id
	 *  @Method_Name    ：findSecondMenuIdsByMenuName
	 *  @param menuName
	 *  @return java.util.List<java.lang.String>
	 *  @Creation Date  ：2018/8/10
	 *  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
	 */
	List<String> findSecondMenuIdsByMenuName(String menuName);
	/*
	 *  @Description    ：用户邀请注册时根据邀请码获取用户信息
	 *  @Method_Name    ：getUserInfoByInviteNo
	 *  @param inviteNo  用户邀请码
	 *  @return com.yirun.framework.core.model.ResponseEntity<?>
	 *  @Creation Date  ：2018/8/14
	 *  @Author         ：pengwu@hongkun.com.cn
	 */
	ResponseEntity<?> getUserInfoByInviteNo(String inviteNo);
	/***
	 *  @Description    : 更新用户的推广来源
	 *  @Method_Name    : updateSpreadSource;
	 *  @param regUserId 用户ID
	 *  @param groupCode 机构编码
	 *  @return
	 *  @return         : ResponseEntity<?>;
	 *  @Creation Date  : 2018年8月20日 下午5:28:04;
	 *  @Author         : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	ResponseEntity<?> updateSpreadSource(Integer regUserId,String groupCode);
	
	/**
	*  查询用户信息
	*  @param ids 用户id集合
	*  @return java.util.Map<java.lang.Integer,com.hongkun.finance.user.model.RegUser>
	*  @date                    ：2019/1/28
	*  @author                  ：zc.ding@foxmail.com
	*/
	Map<Integer, RegUser> findRegUserByIds(List<Integer> ids);
	
	/**
	*  查询用户详情
	*  @param regUserIds    用户id集合
	*  @return java.util.Map<java.lang.Integer,com.hongkun.finance.user.model.RegUserDetail>
	*  @date                    ：2019/1/28
	*  @author                  ：zc.ding@foxmail.com
	*/
    Map<Integer, RegUserDetail> findRegUserDetailByIds(List<Integer> regUserIds);
    
    /**
    *  查询用户信息并缓存
    *  @param regUserIds    用户集合
    *  @return java.util.Map<java.lang.Integer,com.hongkun.finance.user.model.RegUserInfo>
    *  @date                    ：2019/1/28
    *  @author                  ：zc.ding@foxmail.com
    */
    Map<Integer, RegUserInfo> findRegUserInfoByIds(List<Integer> regUserIds);
}
