package com.hongkun.finance.user.dao;

import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.model.vo.UserSimpleVo;
import com.hongkun.finance.user.model.vo.UserVO;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.user.dao.RegUserDao.java
 * @Class Name : RegUserDao.java
 * @Description : GENERATOR DAO类
 * @Author : generator
 */
public interface RegUserDao extends MyBatisBaseDao<RegUser, java.lang.Long> {

	/**
	 * 根据用户的tel查询id，是模糊查询
	 * 
	 * @param userTel
	 * @return
	 */
	List<Integer> findUserIdsByTel(Long userTel);

	/**
	 * 根据用户的id查询用户的tel
	 * 
	 * @param userId
	 * @return
	 */
	UserVO findRegUserTelAndRealNameById(Integer userId);

	/**
	 * 根据user和detail的联合信息来查询记录
	 * 
	 * @param userVO
	 * @param pager
	 * @return
	 */
	Pager listConditionPage(UserVO userVO, Pager pager);

	UserVO findUserWithDetailById(Integer userId);

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
	 * 根据userName模糊查询userId
	 * 
	 * @param userName
	 * @return
	 */
	List<Integer> findUserIdsByFuzzyName(String userName);

	/**
	 * 
	 * @Description : 根据条件模糊查询userId
	 * @Method_Name : findUserIdsByUserVO
	 * @param userVO
	 * @return
	 * @return : List<Integer>
	 * @Creation Date : 2017年10月10日 上午10:24:14
	 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	List<Integer> findUserIdsByUserVO(UserVO userVO);

	/**
	 * 
	 * @Description : 查询物业公司列表（用于数据字典）
	 * @Method_Name : findPropertyDicDataList
	 * @return
	 * @return : List<CommonDicData>
	 * @Creation Date : 2017年10月11日 上午11:36:20
	 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	List<Map<String, Object>> findPropertyDicDataList();

	/**
	 * 查询用户数量
	 * 
	 * @param regUser
	 * @return
	 */
	Long findOperatorCount(RegUser regUser);

	/**
	 * 根据用户的登录号码查询用户的菜单id
	 * 
	 * @param login
	 * @return
	 */
	List<Integer> findUserMenuByLogin(String login, Integer userType,Integer sysType);

	/**
	 * 找到用户对应的权限
	 * 
	 * @param userValidMenu
	 * @return
	 */
	List<Integer> findUserAuthIdByMenus(List<Integer> userValidMenu);

	List<UserSimpleVo> findUserSimpleVoList(List<Integer> regUserIds);

	/**
	 * @Description : 通过手机号查询用户信息，用户信息含有密码信息
	 * @Method_Name : findUserWithPwdByLogin
	 * @param login
	 *            : 手机号
	 * @return : RegUser
	 * @Creation Date : 2018年2月1日 下午2:04:07
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	RegUser findUserWithPwdByLogin(Long login);

	/**
	 * 查找已经删除的用户
	 * 
	 * @param validateHasDeleteUser
	 * @return
	 */
	List<RegUser> findDeletedUser(RegUser validateHasDeleteUser);

	/**
	 * @Description ：通过手机号查询用户信息
	 * @Method_Name ：findRegUserByLogin
	 * @param login
	 * @return com.hongkun.finance.user.model.RegUser
	 * @Creation Date ：2018/4/17
	 * @Author ：zhichaoding@hongkun.com.cn
	 */
	RegUser findRegUserByLogin(Long login);

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
	*  查询用户降级记录
	*  @Method_Name             ：findRegUserVipRecordList
	*  @param userVO
	*  @param pager
	*  @return com.yirun.framework.core.utils.pager.Pager
	*  @Creation Date           ：2018/6/7
	*  @Author                  ：zhichaoding@hongkun.com.cn
	*/
    Pager findRegUserVipRecordList(UserVO userVO, Pager pager);
	/**
	*  @Description    ：根据菜单名称：查询我的账户二级菜单
	*  @Method_Name    ：findAccountSecondMenuIdsByMenuName
	*  @param menuName
	*  @return java.util.List<java.lang.Integer>
	*  @Creation Date  ：2018/8/10
	*  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
	*/
    List<Integer> findAccountSecondMenuIdsByMenuName(String menuName);


	/**
	 *  @Description    ：根据用户邀请码获取用户信息
	 *  @Method_Name    ：getUserInfoByInviteNo
	 *  @param inviteNo  用户邀请码
	 *  @return com.hongkun.finance.user.model.vo.UserVO
	 *  @Creation Date  ：2018/8/15
	 *  @Author         ：pengwu@hongkun.com.cn
	 */
	UserVO getUserInfoByInviteNo(String inviteNo);
}
