package com.hongkun.finance.web.controller.ros;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.roster.facade.RosterFacade;
import com.hongkun.finance.roster.model.RosDepositInfo;
import com.hongkun.finance.roster.model.RosInfo;
import com.hongkun.finance.roster.model.RosStaffInfo;
import com.hongkun.finance.roster.service.RosDepositInfoService;
import com.hongkun.finance.roster.service.RosInfoService;
import com.hongkun.finance.roster.service.RosStaffInfoService;
import com.hongkun.finance.user.support.security.annotation.ActionLog;
import com.yirun.framework.core.annotation.Token;
import com.yirun.framework.core.annotation.Token.Ope;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description   : 黑白名单(花名册)管理
 * @Project       : management-financial-services
 * @Program Name  : com.hongkun.finance.web.controller.ros.RosController.java
 * @Author        : zhichaoding@hongkun.com zc.ding
 */
@Controller
@RequestMapping("rosInfoController/")
public class RosInfoController {

	@Reference
	private RosterFacade rosterFacade;
	@Reference
	private RosInfoService rosInfoService;
	@Reference
	private RosStaffInfoService rosStaffInfoService;
	@Reference
	private RosDepositInfoService rosDepositInfoService;
	
	/**
	 *  @Description    : 用户功能开关
	 *  @Method_Name    : rosList
	 *  @param pager
	 *  @param rosInfo
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年9月28日 上午11:52:01 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	@RequestMapping("rosInfoList")
	@ResponseBody
	public ResponseEntity<?> rosInfoList(Pager pager, RosInfo rosInfo){
		return this.rosterFacade.findRosInfoList(pager, rosInfo);
	}
	
	/**
	 *  @Description    : 添加用户权限黑白名单
	 *  @Method_Name    : addRosInfo
	 *  @param rosInfo
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年9月28日 下午2:29:08 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	@RequestMapping("addRosInfo")
	@ResponseBody
	@Token(operate = Ope.REFRESH)
    @ActionLog(msg = "添加黑白名单, 手机号: {args[0].login}, 类型: {args[0].type}, 标识: {args[0].flag}")
	public ResponseEntity<?> addRosInfo(RosInfo rosInfo){
		return this.rosterFacade.addRosInfo(rosInfo);
	}
	
	/**
	 *  @Description    : 删除用户功能黑白名单
	 *  @Method_Name    : delRosInfo
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年9月28日 下午2:36:12 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	@RequestMapping("delRosInfo")
	@ResponseBody
    @ActionLog(msg = "删除黑白名单, 标识: {args[0]}")
	public ResponseEntity<?> delRosInfo(Integer id){
		if(this.rosInfoService.delRosInfo(id) > 0){
			return new ResponseEntity<>(Constants.SUCCESS);
		}
		return new ResponseEntity<>(Constants.ERROR, "名单删除失败");
	}
	
	/**
	 *  @Description    : 企业员工关系表
	 *  @Method_Name    : rosStaffInfoList
	 *  @param pager
	 *  @param rosStaffInfo
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年9月29日 下午1:53:48 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	@RequestMapping("rosStaffInfoList")
	@ResponseBody
	public ResponseEntity<?> rosStaffInfoList(Pager pager, RosStaffInfo rosStaffInfo){
		return new ResponseEntity<>(Constants.SUCCESS, this.rosStaffInfoService.findRosStaffInfoList(rosStaffInfo, pager));
	}
	
	/**
	 *  @Description    : 添加企业员工关系
	 *  @Method_Name    : addRosStaffInfo
	 *  @param rosStaffInfo
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年9月29日 下午1:55:14 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	@RequestMapping("addRosStaffInfo")
	@ResponseBody
    @ActionLog(msg = "添加企业员工, 手机号: {args[0].login}, 企业标识: {args[0].enterpriseRegUserId}, 企业名称: {args[0].enterpriseRealName}, 员工类型: {args[0].type}")
	public ResponseEntity<?> addRosStaffInfo(RosStaffInfo rosStaffInfo){
		return this.rosterFacade.addRosStaffInfo(rosStaffInfo);
	}
	
	/**
	 *  @Description    : 删除物业员工关系
	 *  @Method_Name    : delRosStaffInfo
	 *  @param id
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年9月29日 下午2:24:35 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	@RequestMapping("delRosStaffInfo")
	@ResponseBody
    @ActionLog(msg = "删除企业员工, 标识: {args[0]}")
	public ResponseEntity<?> delRosStaffInfo(Integer id){
		RosStaffInfo rosStaffInfo = new RosStaffInfo();
		rosStaffInfo.setId(id);
		rosStaffInfo.setState(0);
		if(this.rosStaffInfoService.updateRosStaffInfo(rosStaffInfo) > 0){
			return new ResponseEntity<>(Constants.SUCCESS);
		}
		return new ResponseEntity<>(Constants.ERROR);
	}
	
	/**
	 *  @Description    : 条件检索物业宝&购房宝意向金名单
	 *  @Method_Name    : rosDepositInfoList
	 *  @param pager
	 *  @param rosDepositInfo
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年9月30日 上午11:41:48 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	@RequestMapping("rosDepositInfoList")
	@ResponseBody
	public ResponseEntity<?> rosDepositInfoList(Pager pager, RosDepositInfo rosDepositInfo){
		return this.rosterFacade.findRosDepositInfoList(pager, rosDepositInfo);
	}
	
	/**
	 *  @Description    : 添加物业宝&购房宝名单
	 *  @Method_Name    : addRosDepositInfo
	 *  @param rosDepositInfo
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年9月30日 上午11:42:18 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	@RequestMapping("addRosDepositInfo")
	@ResponseBody
    @ActionLog(msg = "添加意向金, 用户标识: {args[0].regUserId}, 标的标识: {args[0].bidId}, 金额: {args[0].money}, 类型: {args[0].type}")
	public ResponseEntity<?> addRosDepositInfo(RosDepositInfo rosDepositInfo){
		if(rosDepositInfo.getId() != null && rosDepositInfo.getId() > 0){
			if(this.rosDepositInfoService.updateRosDepositInfo(rosDepositInfo) > 0){
				return new ResponseEntity<>(Constants.SUCCESS);
			}else{
				return new ResponseEntity<>(Constants.ERROR, "更新失败");
			}
		}
		return this.rosterFacade.addRosDepositInfo(rosDepositInfo);
	}
	
	/**
	 *  @Description    : 删除物业宝&购房宝意向金名单
	 *  @Method_Name    : delRosDepositInfo
	 *  @param id
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年9月30日 上午11:48:55 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	@RequestMapping("delRosDepositInfo")
	@ResponseBody
    @ActionLog(msg = "删除意向金, 标识: {args[0]}")
	public ResponseEntity<?> delRosDepositInfo(Integer id){
		RosDepositInfo rosDepositInfo = new RosDepositInfo();
		rosDepositInfo.setId(id);
		rosDepositInfo.setState(0);
		if(this.rosDepositInfoService.updateRosDepositInfo(rosDepositInfo) > 0){
			return new ResponseEntity<>(Constants.SUCCESS);
		}
		return new ResponseEntity<>(Constants.ERROR);
	}
	
	/**
	 *  @Description    : 预更新意向金名单
	 *  @Method_Name    : preUpdateRosDepositInfo
	 *  @param id
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年10月9日 上午10:09:42 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	@RequestMapping("preUpdateRosDepositInfo")
	@ResponseBody
	public ResponseEntity<?> preUpdateRosDepositInfo(Integer id){
		return new ResponseEntity<>(Constants.SUCCESS, this.rosterFacade.findDepositInfoById(id));
	}
}
