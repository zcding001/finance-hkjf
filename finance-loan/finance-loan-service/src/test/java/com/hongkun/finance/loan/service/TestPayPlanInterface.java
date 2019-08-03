
/**
 * 
 */
package com.hongkun.finance.loan.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.invest.model.BidInfoDetail;
import com.hongkun.finance.loan.facade.LoanFacade;
import com.hongkun.finance.loan.facade.RepayFacade;
import com.hongkun.finance.loan.model.BidRepayPlan;
import com.hongkun.finance.payment.model.FinAccount;
import com.hongkun.finance.user.model.RegUser;
import com.yirun.framework.core.model.ResponseEntity;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Description : TODO caoxb 类描述
 * @Project : finance-loan-service
 * @Program Name : com.hongkun.finance.loan.service.TestPayPlanInterface.java
 * @Author : caoxinbang@hongkun.com.cn 曹新帮
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext-loan.xml" })
public class TestPayPlanInterface {

	private static final Logger logger = LoggerFactory.getLogger(TestPayPlanInterface.class);
	@Reference
	private BidRepayPlanService bidRepayPlanService;
	@Reference
	private RepayFacade repayFacade;
	@Reference
	private LoanFacade loanFacade;

	@Test

	public void countRepayPlanAmount() {
		BidRepayPlan bidRepayPlan = new BidRepayPlan();
		bidRepayPlan.setBidId(4);
		bidRepayPlanService.countRepayPlanAmount(bidRepayPlan);
	}

	@Test
	@Ignore
	public void testValidateRepayPlan() {
		ResponseEntity<?> result = bidRepayPlanService.validateRepayPlan(29, 3);
		logger.info("status : {}", result.getResStatus());
		logger.info("param  :{}", result.getResMsg());
	}

	@Test
	@Ignore
	public void testWithHoldRepayMoney() {
		FinAccount account = new FinAccount();
		account.setRegUserId(15);
		RegUser regUser = new RegUser();
		regUser.setId(15);
		regUser.setIdentify(1);
		BidRepayPlan repayPlan = bidRepayPlanService.findBidRepayPlanById(20);
		// BidInfoDetail infoDetail =
		// bidInfoDetailService.findBidInfoDetailByBidId(4);
		BidInfoDetail infoDetail = new BidInfoDetail();
		infoDetail.setBiddInfoId(4);
		infoDetail.setWithholdState(1);
		try {
			// ResponseEntity<?> entity =
			// repayFacade.withHoldRepayMoney(infoDetail, repayPlan, regUser,
			// account, null);
			// logger.info("entity : {}", entity.getResStatus());
		} catch (Exception e) {
			e.printStackTrace();

		}
	}
	
	@Test
	@Ignore
	public void testFindReceiptPlan() {
//		Pager pager = loanFacade.findReceiptPlanList(new Pager(), 5);
//		logger.info("{}", pager);
	}
	
	@Test
	@Ignore
	public void testFindCurrRepayPlanIdByBidIds() {
		List<Integer> bidIds = Arrays.asList(39);
		List<Map<String, Object>> list = this.bidRepayPlanService.findCurrRepayPlanIdByBidIds(bidIds, 3);
		System.out.println(list.size());
	}
	
	@Test
	public void testFindRepayPlanAndNotice(){
	    this.loanFacade.findRepayPlanAndNotice();
    }

	@Test
    public void findSumRepayAtmByLoanCode(){
		BigDecimal result = bidRepayPlanService.findSumRepayAtmByBidId(140);
		System.out.print(result);
	}
}
