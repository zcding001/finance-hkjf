package com.hongkun.finance.user.dao;

import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.model.RegUserDetail;
import com.hongkun.finance.user.model.vo.UserVO;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

import java.util.Date;
import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.user.dao.RegUserDetailDao.java
 * @Class Name    : RegUserDetailDao.java
 * @Description   : GENERATOR DAO类
 * @Author        : generator
 */
public interface RegUserDetailDao extends MyBatisBaseDao<RegUserDetail, java.lang.Long> {
	
	/**
	 *  @Description    : 通过邀请码查询用户详情
	 *  @Method_Name    : findRegUserDetailByInviteNo
	 *  @param inviteNo
	 *  @return
	 *  @return         : RegUserDetail
	 *  @Creation Date  : 2017年5月22日 下午9:40:32 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	RegUserDetail findRegUserDetailByInviteNo(String inviteNo);

	/**
	 *  @Description    : 通过推荐码查询用户详情
	 *  @Method_Name    : findRegUserDetailByCommendNo
	 *  @param commendNo
	 *  @return
	 *  @return         : RegUserDetail
	 *  @Creation Date  : 2017年5月22日 下午9:42:12 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	RegUserDetail findRegUserDetailByCommendNo(String commendNo);
	
	/**
	 *  @Description    : 通过regUser的id查询用户详细信息
	 *  @Method_Name    : findRegUSerDetailByRegUserId
	 *  @param regUserId
	 *  @return
	 *  @return         : RegUserDetail
	 *  @Creation Date  : 2017年5月24日 上午11:40:32 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	RegUserDetail findRegUserDetailByRegUserId(int regUserId);
	
	/**
	 *  @Description    : 通过机构码查询用户信息
	 *  @Method_Name    : findRegUserDetailByRegGroupCode
	 *  @param groupCode
	 *  @return
	 *  @return         : RegUserDetail
	 *  @Creation Date  : 2017年5月27日 上午10:30:04 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	RegUserDetail findRegUserDetailByGroupCode(String groupCode);
	
	/**
	 *  @Description    : 通过扩展来源查询用户信息
	 *  @Method_Name    : findRegUserDetailByExtenSource
	 *  @param extenSource
	 *  @return
	 *  @return         : RegUserDetail
	 *  @Creation Date  : 2017年5月27日 上午10:31:20 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	RegUserDetail findRegUserDetailByExtenSource(String extenSource);

	/**
	 *  @Description    : 根据用户id获取用户姓名
	 *  @Method_Name    : findRegUserDetailNameByRegUserId
	 *  @param regUserId
	 *  @return
	 *  @return         : String
	 *  @Creation Date  : 2017年07月05日 上午午10:17:05
	 *  @Author         : pengwu@hongkun.com.cn
	 */
	String findRegUserDetailNameByRegUserId(int regUserId);

	/**
	 *  @Description    ：根据当前时间获取当天生日的用户信息
	 *  @Method_Name    ：findRegUserDetailListByBirthDay
	 *  @param currentDate  当前时间
	 *  @return java.util.List<com.hongkun.finance.user.model.RegUser>
	 *  @Creation Date  ：2018/4/28
	 *  @Author         ：pengwu@hongkun.com.cn
	 */
    List<RegUser> findRegUserDetailListByBirthDay(Date currentDate);
	/**
	*  @Description    ：修改用户投资状态为已投资
	*  @Method_Name    ：updateInvestFlagByRegUserIds
	*  @param regUserIds
	*  @return void
	*  @Creation Date  ：2018/11/26
	*  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
	*/
    void updateInvestFlagByRegUserIds(List<Integer> regUserIds);
}