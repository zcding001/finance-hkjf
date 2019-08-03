package com.hongkun.finance.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.invest.constants.InvestConstants;
import com.hongkun.finance.invest.facade.BidInvestFacade;
import com.hongkun.finance.invest.model.BidInfo;
import com.hongkun.finance.invest.model.BidInfoDetail;
import com.hongkun.finance.invest.service.BidInfoService;
import com.hongkun.finance.loan.facade.MakeLoanFacade;
import com.hongkun.finance.loan.facade.RepayFacade;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.service.RegUserService;
import com.hongkun.finance.web.controller.loan.LoanController;
import com.yirun.framework.core.enums.PlatformSourceEnums;
import com.yirun.framework.core.model.ResponseEntity;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;

import static java.lang.System.out;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/applicationContext-dubbo.xml", "classpath:springmvc-config.xml",
		"classpath:spring/applicationContext-jms.xml" })
public class TestRepay {
	private static final Logger logger = LoggerFactory.getLogger(TestRepay.class);

	private MockMvc mockMvc = null;
	@Reference
	private RegUserService regUserService;
	@Reference
	private BidInfoService bidInfoService;
	@Reference
	private MakeLoanFacade makeLoanFacade;
	@Autowired
	LoanController loanRepayController;
	@Reference
	private BidInvestFacade bidInvestFacade;
	@Reference
	private RepayFacade repayFacade;

	private int userId = 3;

	@Before
	public void before() {
		// mockMvc =
		// MockMvcBuilders.webAppContextSetup((WebApplicationContext)ApplicationContextUtils.getApplicationContext()).build();
//		mockMvc = MockMvcBuilders.standaloneSetup(loanRepayController)
//				.addMappedInterceptors(new String[] { "/loanRepayController/repay.json" }, new AuthorityIntercepter())
//				.build();
		// this.regUserService.findRegUserById(userId);
	}

	@Test
	@Ignore
	public void testRepay() {
		out.println("################# start....invest #################");
		String repayId = "77";
		try {
			RequestBuilder request = MockMvcRequestBuilders.post("/loanRepayController/repay.json")
					.accept(MediaType.APPLICATION_JSON).param("repayId", repayId)// 标的信息
					.param("capital", "0")// 正常还款
					// .param("capital", "10000")//提前还款（结清）
					// .param("capital", "5000")//提前还款
					.param("withHoldflag", "0")// 代扣
					// .param("withHoldflag", "999")//用于测试风险储备金还款
					.param("testUserId", String.valueOf(userId));// 用户id
			ResultActions resultActions = mockMvc.perform(request);
			MvcResult result = resultActions.andReturn();
			logger.info("########################   请求 status：{}", result.getResponse().getStatus());
			logger.info("########################   响应结果：{}", result.getResponse().getContentAsString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		out.println("################# end....invest #################");
	}

	@Test
	@Ignore
	public void testAddBid() {
		BidInfo bidInfo = new BidInfo();
		bidInfo.setName("cxb-testQdzRepay");
		bidInfo.setBidProductId(11);
		bidInfo.setTotalAmount(BigDecimal.valueOf(10000));
		bidInfo.setResidueAmount(BigDecimal.valueOf(10000));
		bidInfo.setInterestRate(BigDecimal.valueOf(10));
		bidInfo.setTermValue(3);
		bidInfo.setBiddRepaymentWay(2);// 按月付息到期还本
		bidInfo.setServiceRate(BigDecimal.valueOf(1));
		// bidInfo.setState(InvestContants.BID_STATE_WAIT_LOAN);
		bidInfo.setState(InvestConstants.BID_STATE_WAIT_INVEST);
		bidInfo.setBorrowerId(3);
		BidInfoDetail detail = new BidInfoDetail();
		detail.setAdvanceRepayState(1);
		detail.setReserveInterest(0);// 预留利息
		detail.setWithholdState(0);// 同意代扣
		this.bidInfoService.insertBidInfoWithBidDetail(bidInfo, detail);
		System.out.println("okok...");
	}

	@Test
	// @Ignore
	public void testLending() {
		out.println("################# lending....start #################");
		for (int i = 26; i < 27; i++) {
//			ResponseEntity<?> result = makeLoanFacade.makeLoans(i);
//			logger.info("\n响应结果\n:{}", result);
		}
		out.println("################# lending....end #################");
	}

	@Test
	// @Ignore
	public void testDoRepay() {
		int repayId = 372;
		BigDecimal capital = BigDecimal.valueOf(10000);
		int withHoldflag = 0;
		RegUser regUser = this.regUserService.findRegUserById(3);
		ResponseEntity<?> result = repayFacade.repay(repayId, capital, withHoldflag, regUser, PlatformSourceEnums.PC);
		logger.info("\n还款结果:\n{}", result);
		logger.info("======================================");
	}
}
