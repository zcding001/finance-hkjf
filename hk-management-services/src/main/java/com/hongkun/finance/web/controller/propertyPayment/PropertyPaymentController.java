package com.hongkun.finance.web.controller.propertyPayment;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.property.facade.PropertyPaymentFacade;
import com.hongkun.finance.property.model.ProPayRecord;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.service.RegUserService;
import com.hongkun.finance.user.support.security.OperationTypeEnum;
import com.hongkun.finance.user.support.security.annotation.ActionLog;
import com.hongkun.finance.user.support.security.component.annotation.AskForPermission;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.yirun.framework.core.commons.Constants.ERROR;

/**
 * 
 * @Description   : 物业缴费controller
 * @Project       : hk-management-services
 * @Program Name  : com.hongkun.finance.web.controller.propertyPayment.PropertyPaymentController.java
 * @Author        : xuhuiliu@hongkun.com.cn 劉旭輝
 */
@Controller
@RequestMapping("/propertyPaymentController")
@AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
public class PropertyPaymentController {
	
	@Reference
	private PropertyPaymentFacade propertyPaymentFacade;
	
	@Reference
	private RegUserService regUserService;
	
	/**
	 *  @Description    : 物业缴费审核列表
	 *  @Method_Name    : bidInfoDetailList
	 *  @param proPayRecordVo
	 *  @param pager
	 *  @return
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年10月9日 下午4:43:32 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	@RequestMapping("/auditProPayRecordList")
    @ResponseBody
    public ResponseEntity<?> auditProPayRecordList(ProPayRecord proPayRecordVo, Pager pager){
    	return propertyPaymentFacade.findPropertyRecordVoList(proPayRecordVo, pager);
    }
	/**
	 * 
	 *  @Description    : 物业缴费审核
	 *  @Method_Name    : auditProPayment
	 *  @param proPayRecordId
	 *  @param opinion
	 *  @param state
	 *  @return
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年10月12日 下午3:39:18 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	@RequestMapping("/auditProPayment")
    @ResponseBody
	@ActionLog(msg="物业缴费审核,审核id: {args[0]},审核描述: {args[1]}, 审核状态: {args[2]}")
    public ResponseEntity<?> auditProPayment(Integer proPayRecordId, String opinion, Integer state){
		try {
			RegUser regUser = BaseUtil.getRegUser(() -> this.regUserService.findRegUserById(BaseUtil.getLoginUser().getId()));
	    	return propertyPaymentFacade.auditProPayment(proPayRecordId, opinion, state, regUser);
		} catch (Exception e) {
			return new ResponseEntity<>(ERROR, "审核失败，请检查数据后重新审核");
		}
    }
}
