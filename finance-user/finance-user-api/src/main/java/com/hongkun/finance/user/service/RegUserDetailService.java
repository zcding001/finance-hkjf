package com.hongkun.finance.user.service;

import java.util.Date;
import java.util.List;

import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.model.RegUserDetail;
import com.hongkun.finance.user.model.RegUserInfo;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.user.service.RegUserDetailService.java
 * @Class Name    : RegUserDetailService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface RegUserDetailService {
	
	/**
	 *  @Description    : 实名认证
	 *  @Method_Name    : updateRegUserDetailForRealName
	 *  @param regUserDetail
	 *  @param regUserInfo
	 *  @param state 0认证成功、1认证信息不一致、2认证信息不存在、9其他异常
	 *  
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年5月24日 上午10:27:55 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	public ResponseEntity<?> updateRegUserDetailForRealName(RegUserDetail regUserDetail, RegUserInfo regUserInfo, Integer state);
	
	/**
	 *  @Description    : 通过regUser的id查询用户详细信息
	 *  @Method_Name    : findRegUserDetailByRegUserId
	 *  @param regUserId
	 *  @return
	 *  @return         : RegUserDetail
	 *  @Creation Date  : 2017年5月25日 上午9:22:56 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	public RegUserDetail findRegUserDetailByRegUserId(int regUserId);
	
	/**
	 * @Described			: 条件检索数据
	 * @param regUserDetail 检索条件
	 * @return	List<RegUserDetail>
	 */
	List<RegUserDetail> findRegUserDetailList(RegUserDetail regUserDetail);

	/**
	 *  @Description    : 根据用户id获取用户姓名
	 *  @Method_Name    : findRegUserDetailNameByRegUserId
	 *  @param regUserId
	 *  @return
	 *  @return         : String
	 *  @Creation Date  : 2017年07月05日 上午午10:11:05
	 *  @Author         : pengwu@hongkun.com.cn
	 */
	String findRegUserDetailNameByRegUserId(int regUserId);
	
	/**
	 *  @Description    : 分页检索用户详细信息
	 *  @Method_Name    : findRegUserDetailList
	 *  @param pager
	 *  @param regUserDetail
	 *  @return
	 *  @return         : Pager
	 *  @Creation Date  : 2017年9月5日 下午6:58:25 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	Pager findRegUserDetailList(Pager pager, RegUserDetail regUserDetail);
	
	
	/**
	 * @Described : 批量更新数据
	 * @param list
	 *            要更新的数据
	 * @param count
	 *            多少条数提交一次
	 * @return : void
	 */
	void updateRegUserDetailBatch(List<RegUserDetail> list, int count);
	
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
	 *  @Description    ：根据当前时间获取当天生日的用户信息
	 *  @Method_Name    ：findRegUserDetailListByBirthDay
	 *  @param currentDate  当前时间
	 *  @return java.util.List<com.hongkun.finance.user.model.RegUser>
	 *  @Creation Date  ：2018/4/28
	 *  @Author         ：pengwu@hongkun.com.cn
	 */
	List<RegUser> findRegUserDetailListByBirthDay(Date currentDate);
}
