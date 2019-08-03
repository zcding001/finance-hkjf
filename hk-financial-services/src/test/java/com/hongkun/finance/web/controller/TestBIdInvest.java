package com.hongkun.finance.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.invest.facade.BidInvestFacade;
import com.hongkun.finance.payment.constant.TradeTransferConstants;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.service.RegUserService;
//import com.hongkun.finance.web.controller.bid.BidInvestController;
import com.yirun.framework.core.enums.PlatformSourceEnums;
import com.yirun.framework.core.model.ResponseEntity;
import org.junit.Before;
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
public class TestBIdInvest {
	private static final Logger logger = LoggerFactory.getLogger(TestBIdInvest.class);

	private MockMvc mockMvc = null;
	@Reference
	private RegUserService regUserService;
	@Autowired
//	BidInvestController bidInvestController;
	@Reference
	private BidInvestFacade bidInvestFacade;

	private int testUserId = 12;
	private int bidId = 5;
	private int money = 10000;

	@Before
	public void before() {
		// mockMvc =
		// MockMvcBuilders.webAppContextSetup((WebApplicationContext)ApplicationContextUtils.getApplicationContext()).build();
//		mockMvc = MockMvcBuilders.standaloneSetup(bidInvestController)
//				.addMappedInterceptors(new String[] { "/bidInvestController/invest.json" }, new AuthorityIntercepter())
//				.build();
//		this.regUserService.findRegUserById(testUserId);
	}

	@Test
	// @Ignore
	public void testInvest() {
		out.println("################# start....invest #################");
		try {
			RequestBuilder request = MockMvcRequestBuilders.post("/bidInvestController/invest.json")
					.accept(MediaType.APPLICATION_JSON).param("bidId", String.valueOf(bidId))// 标的信息
					.param("money", String.valueOf(money))// 投资金额
					// 用户id
					.param("testUserId", String.valueOf(testUserId));
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
	// @Ignore
	public void testInvestFacade() {
		int bidId = 131;
		BigDecimal money = BigDecimal.valueOf(10000);
		int testUserId = 2;
		logger.info("================ invest start ============");
		RegUser regUser = this.regUserService.findRegUserById(testUserId);
		ResponseEntity<?> result = this.bidInvestFacade.invest(regUser, -1, -1, money, bidId, TradeTransferConstants.TRADE_TYPE_INVEST, 1,PlatformSourceEnums.PC);
		logger.info("\n响应结果\n:{}", result);
		logger.info("================ invest end ============");
	}
}
