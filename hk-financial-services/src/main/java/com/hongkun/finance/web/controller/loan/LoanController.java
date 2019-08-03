package com.hongkun.finance.web.controller.loan;

import static com.yirun.framework.core.commons.Constants.SUCCESS;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.loan.facade.LoanFacade;
import com.hongkun.finance.loan.facade.RepayFacade;
import com.hongkun.finance.loan.model.vo.LoanVO;
import com.hongkun.finance.loan.service.BidReceiptPlanService;
import com.hongkun.finance.loan.service.BidRepayPlanService;
import com.hongkun.finance.sms.model.SmsMsgInfo;
import com.hongkun.finance.sms.utils.SmsSendUtil;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.service.RegUserService;
import com.hongkun.finance.user.support.security.annotation.ActionLog;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.annotation.Token;
import com.yirun.framework.core.annotation.Token.Ope;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.enums.PlatformSourceEnums;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.CompareUtil;
import com.yirun.framework.core.utils.DateUtils;
import com.yirun.framework.core.utils.pager.Pager;

/**
 * @Description   : loan服务模块控制层
 * @Project       : hk-financial-services
 * @Program Name  : com.hongkun.finance.web.controller.loan.LoanController.java
 * @Author        : zhichaoding@hongkun.com zc.ding
 */
@Controller
@RequestMapping("loanController/")
public class LoanController {

	private final Logger logger = LoggerFactory.getLogger(LoanController.class);
	@Reference
	private BidReceiptPlanService bidReceiptPlanService;
	@Reference
	private BidRepayPlanService bidRepayPlanService;
	@Reference
	private LoanFacade loanFacade;
	@Reference
	private RepayFacade repayFacade;
	@Reference
	private RegUserService regUserService;
	
	
	/**
	 *  @Description    : 条件检索还款计划、回款计划
	 *  @Method_Name    : replanList
	 *  @param pager
	 *  @param loanVO
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年12月25日 下午2:34:36 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	@RequestMapping("replanList")
	@ResponseBody
	@ActionLog(msg = "查看{args[1].planType == 1 ? '还款计划' : '回款计划'}")
	public ResponseEntity<?> replanList(Pager pager, LoanVO loanVO){
		Date createTime = loanVO.getCreateTime();
		if(createTime != null) {
			loanVO.setCreateTime(DateUtils.addDays(createTime, 1));
		}
		loanVO.setUserId(BaseUtil.getLoginUser().getId());
		return this.loanFacade.findPlanListWithStatistics(pager, loanVO);
	}
	
	
	/**
	 * 
	 *  @Description    : 查询某个标的的还款计划
	 *  @Method_Name    : findReplanListByBidId
	 *  @param bidInfoId
	 *  @return
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年12月28日 下午1:56:47 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	@RequestMapping("findReplanListByBidId")
	@ResponseBody
	public ResponseEntity<?> findReplanListByBidId(Integer bidInfoId){
		return bidRepayPlanService.findReplanListByBidId(bidInfoId);
	}
	
	/**
	 * @Description 		: 还款操作
	 * @param repayId 		: 还款id
	 * @param capital 		: 还款本金
	 * @param withHoldflag 	: 代扣标识
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年6月22日 下午3:37:26
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	@SuppressWarnings({ "unchecked" })
	@RequestMapping("repay")
	@Token(operate = Ope.REFRESH)
	@ResponseBody
	public ResponseEntity<?> repay(@RequestParam("repayId") int repayId, int withHoldflag, BigDecimal capital) {
		ResponseEntity<?> result = null;
		try {
			RegUser regUser = BaseUtil.getRegUser(() -> this.regUserService.findRegUserById(BaseUtil.getLoginUser().getId()));
			result = doBaseValidate(regUser, repayId, capital);
			logger.info("LoanController#repay, 还款操作. 用户标识: {}, 还款标识: {}, 是否代扣: {}, 还本金额: {}", regUser.getId(), repayId, withHoldflag, capital);
			if(result.getResStatus() == Constants.SUCCESS) {
				// 执行还款操作
				result = this.repayFacade.repay(repayId, capital, withHoldflag, regUser, PlatformSourceEnums.PC);
				// 发送短信站内信
				if (result.getResStatus() == SUCCESS) {
					SmsSendUtil.sendSmsMsgToQueue((List<SmsMsgInfo>) result.getParams().get("webMsgList"),
							(List<SmsMsgInfo>) result.getParams().get("telMsgList"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 *  @Description    : 计算提前还本应该的本金+利息+服务费
	 *  @Method_Name    : calcAdvanceReapyAmount
	 *  @param repayId	: 还款计划id
	 *  @param capital  : 还款本金
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2018年1月3日 下午3:41:28 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	@RequestMapping("calcAdvanceReapyAmount")
	@Token(operate = Ope.REFRESH)
	@ResponseBody
	public ResponseEntity<?> calcAdvanceReapyAmount(@RequestParam("repayId") int repayId, @RequestParam("capital") BigDecimal capital){
		RegUser regUser = BaseUtil.getRegUser(() -> this.regUserService.findRegUserById(BaseUtil.getLoginUser().getId()));
		ResponseEntity<?> result = doBaseValidate(regUser, repayId, capital);
		if(result.getResStatus() == Constants.SUCCESS) {
			result = this.repayFacade.calcAdvanceReapyAmount(regUser.getId(), repayId, capital);
		}
		return result;
	}
		
	/**
	 * 
	 *  @Description    : 还款操作基础数据验证
	 *  @param regUser
	 *  @param repayId
	 *  @param capital
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2018年1月3日 下午3:15:00 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	private ResponseEntity<?> doBaseValidate(RegUser regUser, int repayId, BigDecimal capital){
		if (regUser == null) {
			return BaseUtil.error("用户未登录！");
		}
		if (repayId <= 0) {
			return BaseUtil.error("还款ID不存在！");
		}
		if (CompareUtil.ltZero(capital)) {
			return BaseUtil.error("提前还本金额不能小于0！");
		}
		return ResponseEntity.SUCCESS;
	}
}
