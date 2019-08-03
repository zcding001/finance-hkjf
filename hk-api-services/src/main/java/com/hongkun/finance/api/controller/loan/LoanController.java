package com.hongkun.finance.api.controller.loan;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.invest.constants.InvestConstants;
import com.hongkun.finance.invest.facade.BidInvestFacade;
import com.hongkun.finance.invest.model.vo.BidInvestDetailVO;
import com.hongkun.finance.invest.service.BidInfoService;
import com.hongkun.finance.loan.constants.RepayConstants;
import com.hongkun.finance.loan.facade.LoanFacade;
import com.hongkun.finance.loan.facade.RepayFacade;
import com.hongkun.finance.loan.model.BidReceiptPlan;
import com.hongkun.finance.loan.service.BidReceiptPlanService;
import com.hongkun.finance.payment.model.FinAccount;
import com.hongkun.finance.payment.service.FinAccountService;
import com.hongkun.finance.sms.model.SmsMsgInfo;
import com.hongkun.finance.sms.utils.SmsSendUtil;
import com.hongkun.finance.user.constants.UserConstants;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.service.RegUserService;
import com.hongkun.finance.user.support.security.annotation.ActionLog;
import com.hongkun.finance.user.utils.BaseUtil;
import com.hongkun.finance.user.utils.ValidateUtil;
import com.yirun.framework.core.annotation.Token;
import com.yirun.framework.core.annotation.Token.Ope;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.enums.PlatformSourceEnums;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.AppResultUtil;
import com.yirun.framework.core.utils.CommonUtils;
import com.yirun.framework.core.utils.CompareUtil;
import com.yirun.framework.core.utils.pager.Pager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * @Description   : 还款&回款服务模块
 * @Project       : hk-api-services
 * @Program Name  : com.hongkun.finance.api.controller.loan.LoanController.java
 * @Author        : zhichaoding@hongkun.com zc.ding
 */

@Controller
@RequestMapping("/loanController/")
public class LoanController {
	
	private final Logger logger = LoggerFactory.getLogger(LoanController.class);
	
	@Reference
	private LoanFacade loanFacade;
	@Reference
	private BidInfoService bidInfoService;
	@Reference
	private BidReceiptPlanService bidReceiptPlanService;
	@Reference
	private RegUserService regUserService;
	@Reference
	private RepayFacade repayFacade;
	@Reference
	private FinAccountService finAccountService;
	@Reference
	private BidInvestFacade bidInvestFacade;

	/**
	*  回款统计数据
	*  @Method_Name             ：receiptPlanGather
	*  @param pager
	*  @return java.util.Map<java.lang.String,java.lang.Object>
	*  @Creation Date           ：2018/5/8
	*  @Author                  ：zhichaoding@hongkun.com.cn
	*/
    @RequestMapping("receiptPlanGather")
    @ResponseBody
    public Map<String, Object> receiptPlanGather(Pager pager){
        Map<String, Object> map = this.loanFacade.findReceiptPlanList(BaseUtil.getLoginUserId());
        //设置钱袋子标识
        map.put("qdzEnable",
                (this.bidInvestFacade.validQdzEnable(BaseUtil.getLoginUserId()) > 0 && CompareUtil.gtZero((BigDecimal) ((Map<String, Object>) map.get("qdz")).get("waitAmountSum")) ? 1 : 0));
        return AppResultUtil.mapOfInProperties(map, Constants.SUCCESS, "", "amountSum", "capitalSum", "interestSum", 
                "increaseSum");
    }
    
	/**
	 *  @Description    : 查询回款详情
	 *  @Method_Name    : receiptPlanList
	 *  @param bidId
	 *  @return         : Map<String,Object>
	 *  @Creation Date  : 2018年3月11日 下午12:26:21 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	@RequestMapping("receiptPlanList")
	@ResponseBody
	public Map<String, Object> receiptPlanList(@RequestParam("bidId") Integer bidId, Integer investId){
		return AppResultUtil.mapOfResponseEntity(this.loanFacade.findReceiptPlanDetailList(BaseUtil.getLoginUserId(), bidId, investId));
	}

	/**
	*  查询回款日历
	*  @Method_Name             ：receiptPlanCalendar
	*  @param month             ：回款月份 eg 2018-01-01
	*  @return java.util.Map<java.lang.String,java.lang.Object>
	*  @Creation Date           ：2018/4/28
	*  @Author                  ：zhichaoding@hongkun.com.cn
	*/
    @RequestMapping("receiptPlanCalendar")
    @ResponseBody
	public Map<String, Object> receiptPlanCalendar(@RequestParam("month")String month){
        if(month.matches("[0-9]{4}[-][0-9]{2}")){
            month += "-01";
        }
        Map<String, Object> result = this.loanFacade.findReceiptPlanCalendar(BaseUtil.getLoginUserId(), month);
        Map<String, Object> map = AppResultUtil.successOfListInProperties((List<?>) result.get("list"), 
                "amount", "investId", "bidName", "capitalAmount", "increaseAmount", "interestAmount", "periods",
                "planTime", "state", "bidId");
        result.put("list", map.get("dataList"));
        return AppResultUtil.successOf(result);
    }
	
	/**
	 *  @Description    : 查询我的借款列表（鸿坤金服）
	 *  @Method_Name    : myLoanList
	 *  @return         : Map<String,Object>
	 *  @Creation Date  : 2018年3月11日 上午11:21:33 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	@RequestMapping("myLoanList")
	@ResponseBody
    @Token(operate = Ope.ADD)
	public Map<String, Object> myLoanList(Pager pager){
        //app端没有代扣，去掉这个字段属性值"withholdState"
        pager.setPageSize(Integer.MAX_VALUE);
        ResponseEntity<?> result = this.loanFacade.findRepayPlanListCount(BaseUtil.getLoginUserId(), pager);
        result.getParams().put("waitRepayMoney", result.getParams().get("repayState1"));
        return AppResultUtil.mapOfResponseEntity(result, "capitalSum", "serviceSum", "interestSum", "repayState2", 
                "repaySum", "punishSum", "repayState3", "repayState1", "state");
	}
	
	/**
	 *  @Description    : 查看还款计划详情
	 *  @Method_Name    : repayPlanList
	 *  @param bidId
	 *  @return         : Map<String,Object>
	 *  @Creation Date  : 2018年3月11日 下午12:33:45 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	@SuppressWarnings({"rawtypes" })
	@RequestMapping("repayPlanList")
    @Token(operate = Ope.ADD)
	@ResponseBody
	public Map<String, Object> repayPlanList(Integer bidId){
		ResponseEntity<?> result = this.loanFacade.findRepayPlanDetailList(BaseUtil.getLoginUserId(), bidId);
		List<?> list = ((List) result.getParams().remove("dataList"));
		if(CommonUtils.isEmpty(list)) {
			return AppResultUtil.errorOfMsg("未找到还款计划,请联系管理员");
		}
		Map<String, Object> res = AppResultUtil
				.successOfList(list, "bidIds", "bidId", "createTime", "createTimeEnd", "createTimeBegin", "modifyTime", "riskaccountTime",
						"regUserId", "userIds", "planType", "userId", "bidName", "productType", "allowWithholdFlag", "states")
				.reNameParameterInList("id", "planId");
		res.putAll(result.getParams());
		return res;
	}
	
	/**
	 *  @Description    : 计算提前还本应该的本金+利息+服务费
	 *  @Method_Name    : calcAdvanceReapyAmount
	 *  @param repayId	: 还款计划id
	 *  @param capital  : 还款本金
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2018年3月15日 上午11:15:13 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	@RequestMapping("calcAdvanceReapyAmount")
	@Token(operate = Ope.REFRESH)
	@ResponseBody
	public Map<String, Object> calcAdvanceReapyAmount(@RequestParam("repayId") int repayId, @RequestParam("capital") BigDecimal capital){
        Map<String, Object> res;
	    RegUser regUser = BaseUtil.getRegUser(() -> this.regUserService.findRegUserById(BaseUtil.getLoginUserId()));
		ResponseEntity<?> result = doBaseValidate(repayId, capital);
		if(result.validSuc()) {
            res = AppResultUtil.mapOfResponseEntity(this.repayFacade.calcAdvanceReapyAmount(regUser.getId(), repayId, capital));
		}else{
            res = AppResultUtil.mapOfResponseEntity(result);
        }
		return res;
	}
	
	/**
	 * @Description 		: 还款操作，无支付密码
	 * @param repayId 		: 还款id
	 * @param capital 		: 还款本金
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2018年3月15日 上午11:15:13 
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	@SuppressWarnings({ "unchecked" })
	@RequestMapping("repaySample")
	@Token(operate = Ope.REFRESH)
	@ResponseBody
    @ActionLog(msg = "还款, 还款计划标识: {args[0]}, 本金: {args[1]}")
	public Map<String, Object> repaySample(@RequestParam("repayId") int repayId, BigDecimal capital, Integer source) {
	    RegUser regUser = BaseUtil.getRegUser(() -> this.regUserService.findRegUserById(BaseUtil.getLoginUserId()));
        logger.info("还款操作：用户标识：{}, 还款标识：{} ,还本金额：{}", regUser.getId(), repayId, capital);
        ResponseEntity<?> result = doBaseValidate(repayId, capital);
        if(result.validSuc()) {
            // 执行还款操作
            PlatformSourceEnums platformSourceEnums = source == null ? PlatformSourceEnums.PC : PlatformSourceEnums.typeByValue(source);
            result = this.repayFacade.repay(repayId, capital == null ? BigDecimal.ZERO : capital, 0, regUser, platformSourceEnums);
            // 发送短信站内信
            if (result.getResStatus() == SUCCESS) {
                try{
                    SmsSendUtil.sendSmsMsgToQueue((List<SmsMsgInfo>) result.getParams().get("webMsgList"),
                            (List<SmsMsgInfo>) result.getParams().get("telMsgList"));
                }catch(Exception e){
                    logger.error("还款操作：用户标识：{}, 还款标识：{}, 还本金额：{}, 短信消息失败!", regUser.getId(), repayId, capital, e);
                }
            }
        }
		return AppResultUtil.mapOfResponseEntity(result, "telMsgList", "webMsgList");
	}

    /**
     * @Description 		: 还款操作
     * @param repayId 		: 还款id
     * @param capital 		: 还款本金
     * @param payPasswd 	: 支付密码
     * @return : ResponseEntity<?>
     * @Creation Date : 2018年3月15日 上午11:15:13 
     * @Author : zhichaoding@hongkun.com zc.ding
     */
    @SuppressWarnings({ "unchecked" })
    @RequestMapping("repay")
    @Token(operate = Ope.REFRESH)
    @ResponseBody
    @ActionLog(msg = "还款, 还款计划标识: {args[0]}, 本金: {args[2]}, 支付密码: ******")
    public Map<String, Object> repay(@RequestParam("repayId") int repayId, @RequestParam("payPasswd") String payPasswd, BigDecimal capital, Integer source) {
        RegUser regUser = BaseUtil.getRegUser(() -> this.regUserService.findRegUserById(BaseUtil.getLoginUserId()));
        logger.info("还款操作：用户标识：{}, 还款标识：{} ,还本金额：{}", regUser.getId(), repayId, capital);
		//1.判断用户支付密码是否正确
		ResponseEntity judgeResult = finAccountService.judgePayPassword(regUser.getId(),payPasswd);
		if (judgeResult.getResStatus() != SUCCESS){
			return AppResultUtil.mapOfResponseEntity(judgeResult);
		}
		ResponseEntity result = doBaseValidate(repayId, capital);
		if(result.validSuc()) {
			// 执行还款操作
			PlatformSourceEnums platformSourceEnums = source == null ? PlatformSourceEnums.PC : PlatformSourceEnums.typeByValue(source);
			result = this.repayFacade.repay(repayId, capital == null ? BigDecimal.ZERO : capital, 0, regUser, platformSourceEnums);
			// 发送短信站内信
			if (result.getResStatus() == SUCCESS) {
				if (result.getResStatus() == SUCCESS) {
					try{
						SmsSendUtil.sendSmsMsgToQueue((List<SmsMsgInfo>) result.getParams().get("webMsgList"),
								(List<SmsMsgInfo>) result.getParams().get("telMsgList"));
					}catch(Exception e){
						logger.error("还款操作：用户标识：{}, 还款标识：{}, 还本金额：{}, 短信消息失败!", regUser.getId(), repayId, capital, e);
					}
				}
			}
		}
        return AppResultUtil.mapOfResponseEntity(result, "telMsgList", "webMsgList");
    }
	
	
	/**
	 * 
	 *  @Description    : 还款操作基础数据验证
	 *  @param repayId
	 *  @param capital
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2018年1月3日 下午3:15:00 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	private ResponseEntity<?> doBaseValidate(int repayId, BigDecimal capital){
		if (repayId <= 0) {
			return BaseUtil.error("还款ID不存在！");
		}
		if (CompareUtil.ltZero(capital)) {
			return BaseUtil.error("提前还本金额不能小于0！");
		}
		return ResponseEntity.SUCCESS;
	}
}
