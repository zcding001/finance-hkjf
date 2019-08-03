package com.hongkun.finance.web.controller.bid;

import static com.yirun.framework.core.commons.Constants.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.loan.facade.LoanFacade;
import com.hongkun.finance.loan.model.vo.LoanVO;
import com.hongkun.finance.loan.service.BidReceiptPlanService;
import com.hongkun.finance.loan.service.BidRepayPlanService;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;

/**
 * @Description   : 还款计划、回款计划控制器
 * @Project       : management-financial-services
 * @Program Name  : com.hongkun.finance.web.controller.bid.BidPlanController.java
 * @Author        : zhichaoding@hongkun.com zc.ding
 */
@Controller
@RequestMapping("bidPlanController/")
public class BidPlanController {

	@Reference
	private LoanFacade loanFacade;
	@Reference
	private BidReceiptPlanService bidReceiptPlanService;
	@Reference
	private BidRepayPlanService bidRepayPlanService;
	
	/**
	 *  @Description    : 查询用户的还款计划统计、回款计划统计
	 *  @Method_Name    : planCountList
	 *  @param pager
	 *  @param loanVO
	 *  @return
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年9月20日 上午11:45:26 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	@RequestMapping("planCountList")
	@ResponseBody
	public ResponseEntity<?> planCountList(Pager pager, LoanVO loanVO){
		return new ResponseEntity<>(SUCCESS, this.loanFacade.findPlanCountList(pager, loanVO));
	}
	
	/**
	 *  @Description    : 检索用户还款计划、回款计划
	 *  @Method_Name    : planList
	 *  @param pager
	 *  @param loanVO
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年9月20日 上午11:50:10 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	@RequestMapping("planList")
	@ResponseBody
	public ResponseEntity<?> planList(Pager pager, LoanVO loanVO){
		return new ResponseEntity<>(SUCCESS, this.loanFacade.findPlanList(pager, loanVO));
	}
}
