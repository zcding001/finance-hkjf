package com.hongkun.finance.web.controller.property;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.invest.facade.BidInvestFacade;
import com.hongkun.finance.property.facade.PropertyPaymentFacade;
import com.hongkun.finance.property.model.ProPayRecord;
import com.hongkun.finance.property.service.ProPayRecordService;
import com.hongkun.finance.roster.constants.RosterConstants;
import com.hongkun.finance.roster.facade.RosterFacade;
import com.hongkun.finance.roster.vo.RosStaffInfoVo;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.service.RegUserService;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.exception.ExcelException;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.ExcelUtil;
import com.yirun.framework.core.utils.pager.Pager;

import static com.hongkun.finance.user.constants.UserConstants.USER_TYPE_ENTERPRISE;
import static com.hongkun.finance.user.constants.UserConstants.USER_TYPE_TENEMENT;

import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/propertyManagementController")
public class PropertyManagementController {
	@Reference
	private  RosterFacade rosterFacade;
	@Reference
	private RegUserService regUserService;
	@Reference
	private BidInvestFacade bidInvestFacade;
	@Reference
	private ProPayRecordService proPayRecordService;
	@Reference
	private PropertyPaymentFacade propertyPaymentFacade;
	/**
	 * 
	 *  @Description    : 物业管理--查询销售推荐
	 *  @Method_Name    : findPropertySales
	 *  @param RosStaffInfoContidion
	 *  @param pager
	 *  @return
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2018年1月11日 上午10:36:35 
	 *  @Author         : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	@RequestMapping("/findPropertySales")
    @ResponseBody
    public ResponseEntity<?>  findPropertySales(RosStaffInfoVo rosStaffInfoContidion ,Pager pager){
		RegUser regUser = BaseUtil.getRegUser(() -> this.regUserService.findRegUserById(BaseUtil.getLoginUser().getId()));
		rosStaffInfoContidion.setEnterpriseRegUserId(regUser.getId());
		rosStaffInfoContidion.setState(RosterConstants.ROSTER_STAFF_STATE_VALID);
		return rosterFacade.findPropertySales(rosStaffInfoContidion, pager);
    }
	/**
	 * 
	 *  @Description    : 物业管理--查询销售推荐详情
	 *  @Method_Name    : findRecommendFriends
	 *  @param regUserId
	 *  @param pager
	 *  @return
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2018年1月12日 上午11:16:34 
	 *  @Author         : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	@RequestMapping("/findRecommendFriends")
    @ResponseBody
    public ResponseEntity<?>  findRecommendFriends(Integer regUserId ,Pager pager){
		Pager result = bidInvestFacade.findRecommendListForInvest(pager, regUserId);
		return new ResponseEntity<>(Constants.SUCCESS,result);
    }
	/**
	 * 
	 *  @Description    : 物业管理--缴费记录
	 *  @Method_Name    : findPropertyFeeRecord
	 *  @param proPayRecordVo
	 *  @param pager
	 *  @return
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2018年1月12日 下午3:09:04 
	 *  @Author         : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	@RequestMapping("/findPropertyFeeRecord")
    @ResponseBody
    public ResponseEntity<?>  findPropertyFeeRecord(ProPayRecord proPayRecord,Pager pager){
		RegUser regUser = BaseUtil.getRegUser(() -> this.regUserService.findRegUserById(BaseUtil.getLoginUser().getId()));
		proPayRecord.setId(regUser.getId());
		return propertyPaymentFacade.findPropertyRecordListByPropertyId(proPayRecord, pager);
    }
	
	/**
	 * 
	 *  @Description    : 物业缴费记录导出
	 *  @Method_Name    : exportPropertyFeeRecord
	 *  @param proPayRecordVo
	 *  @param response
	 *  @param start
	 *  @param pageSize
	 *  @return
	 *  @return         : ResponseEntity<?>
	 * @throws UnsupportedEncodingException 
	 * @throws ExcelException 
	 *  @Creation Date  : 2018年1月15日 下午1:39:12 
	 *  @Author         : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	@RequestMapping("/exportPropertyFeeRecord")
    @ResponseBody
    public void exportPropertyFeeRecord(ProPayRecord proPayRecordVo,HttpServletResponse response,int start,int pageSize) throws ExcelException, UnsupportedEncodingException{
		RegUser regUser = BaseUtil.getRegUser(() -> this.regUserService.findRegUserById(BaseUtil.getLoginUser().getId()));
		proPayRecordVo.setId(regUser.getId());
		List<ProPayRecord> result =  propertyPaymentFacade.findPropertyRecordForExport(proPayRecordVo,0,1);
		LinkedHashMap<String,String> fieldMap = new LinkedHashMap<String,String>();
		fieldMap.put("payTypeStr", "项目名称");
		fieldMap.put("tel", "手机号");
		fieldMap.put("userName", "用户名");
		fieldMap.put("money", "缴纳总金额");
		fieldMap.put("pointMoney", "积分抵扣金额");
		fieldMap.put("point", "抵扣所用积分");
		fieldMap.put("createdTimeStr", "交易时间");
		fieldMap.put("communityName", "居住小区");
		fieldMap.put("stateStr", "状态");
		ExcelUtil.exportExcel(null, null, result, fieldMap, 100, response);
    }
}
