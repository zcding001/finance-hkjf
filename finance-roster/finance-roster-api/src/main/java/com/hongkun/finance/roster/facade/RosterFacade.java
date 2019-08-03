package com.hongkun.finance.roster.facade;

import com.hongkun.finance.roster.constants.RosterFlag;
import com.hongkun.finance.roster.constants.RosterType;
import com.hongkun.finance.roster.model.RosDepositInfo;
import com.hongkun.finance.roster.model.RosInfo;
import com.hongkun.finance.roster.model.RosStaffInfo;
import com.hongkun.finance.roster.vo.RosStaffInfoVo;
import com.hongkun.finance.user.model.RegUser;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;

public interface RosterFacade {

	/**
	 *  @Description    : 通过用户手机号验证功能黑白名单
	 *  @Method_Name    : validateRoster
	 *  @param tel	用户手机号
	 *  @param rosterType	功能类型
	 *  @param rosterFlag 黑白名单标识 0：黑名单 1：白名单
	 *  @return         : boolean true表示存在查询结果，false表示查询结果为空
	 *  @Creation Date  : 2017年7月18日 上午9:30:13 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	boolean validateRoster(long tel, RosterType rosterType, RosterFlag rosterFlag);
	
	/**
	 *  @Description    : 条件检索用户功能黑白名单
	 *  @Method_Name    : findRosInfoList
	 *  @param pager
	 *  @param rosInfo
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年9月28日 上午11:15:43 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	ResponseEntity<?> findRosInfoList(Pager pager, RosInfo rosInfo);
	
	/**
	 *  @Description    : 添加用户功能功能权限黑白名单
	 *  @Method_Name    : addRosInfo
	 *  @param rosInfo
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年9月28日 下午2:28:02 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	ResponseEntity<?> addRosInfo(RosInfo rosInfo);
	
	/**
	 *  @Description    : 添加企业员工名单
	 *  @Method_Name    : addRosStaffInfo
	 *  @param rosStaffInfo
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年9月29日 下午4:05:00 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	ResponseEntity<?> addRosStaffInfo(RosStaffInfo rosStaffInfo);
	
	/**
	 *  @Description    : 条件检索购房宝&物业宝名单
	 *  @Method_Name    : findRosDepositInfoList
	 *  @param pager
	 *  @param rosDepositInfo
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年9月30日 下午1:56:54 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	ResponseEntity<?> findRosDepositInfoList(Pager pager, RosDepositInfo rosDepositInfo);
	
	/**
	 *  @Description    : 添加购房宝&物业宝名单
	 *  @Method_Name    : addRosDepositInfo
	 *  @param rosDepositInfo
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年9月30日 下午1:59:03 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	ResponseEntity<?> addRosDepositInfo(RosDepositInfo rosDepositInfo);
	/**
	 *  @Description    : 查询物业员工销售情况
	 *  @Method_Name    : findPropertySales
	 *  @param rosStaffInfoContidion
	 *  @param pager
	 *  @return
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2018年1月8日 上午10:46:22 
	 *  @Author         : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	ResponseEntity<?> findPropertySales(RosStaffInfoVo rosStaffInfoContidion ,Pager pager);

	/**
	*  @Description    ：通过id检索购房宝|物业宝配置
	*  @Method_Name    ：findDepositInfoById
	*  @param id
	*  @return com.hongkun.finance.roster.model.RosDepositInfo
	*  @Creation Date  ：2018/4/17
	*  @Author         ：zhichaoding@hongkun.com.cn
	*/
	RosDepositInfo findDepositInfoById(Integer id);
	/**
	*  @Description    ：加载前台我的账号菜单
	*  @Method_Name    ：loadMyAccountMenu
	*  @param regUser
	*  @return com.yirun.framework.core.model.ResponseEntity<?>
	*  @Creation Date  ：2018/8/10
	*  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
	*/
	ResponseEntity<?> loadMyAccountMenu(RegUser regUser);
}
