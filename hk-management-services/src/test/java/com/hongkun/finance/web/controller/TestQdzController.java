package com.hongkun.finance.web.controller;

import static java.lang.System.out;

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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.hongkun.finance.web.controller.qdz.QdzController;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/applicationContext-dubbo.xml", "classpath:springmvc-config.xml" })
public class TestQdzController {
	private static final Logger logger = LoggerFactory.getLogger(TestQdzController.class);

	private MockMvc mockMvc = null;
	@Autowired
	QdzController qdzConsoleController;

	@Before
	public void before() {
		mockMvc = MockMvcBuilders.standaloneSetup(qdzConsoleController).build();
	}

	@Test
	@Ignore
	public void testSearchByCondition() {
		out.println("################# start....QdzConsoleController #################");
		try {
			RequestBuilder request = MockMvcRequestBuilders.post("/qdzConsoleController/searchByCondition")
					.accept(MediaType.APPLICATION_JSON).param("startTime", "2017-07-25");

			ResultActions resultActions = mockMvc.perform(request);
			MvcResult result = resultActions.andReturn();
			logger.info("########################   请求 status：{}", result.getResponse().getStatus());
			logger.info("########################   响应结果：{}", result.getResponse().getContentAsString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		out.println("################# end....QdzConsoleController #################");
	}

	@Test
	@Ignore
	public void testSearchInterestInfo() {
		out.println("################# start....QdzConsoleController #################");
		try {
			RequestBuilder request = MockMvcRequestBuilders.post("/qdzConsoleController/searchInterestInfo")
					.accept(MediaType.APPLICATION_JSON).param("userFlag", "0").param("day", "2017-07-25");

			ResultActions resultActions = mockMvc.perform(request);
			MvcResult result = resultActions.andReturn();
			logger.info("########################   请求 status：{}", result.getResponse().getStatus());
			logger.info("########################   响应结果：{}", result.getResponse().getContentAsString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		out.println("################# end....QdzConsoleController #################");
	}

	@Test
	@Ignore
	public void testSearchCreditorInfo() {
		out.println("################# start....QdzConsoleController #################");
		try {
			RequestBuilder request = MockMvcRequestBuilders.post("/qdzConsoleController/searchCreditorInfo")
					.accept(MediaType.APPLICATION_JSON);

			ResultActions resultActions = mockMvc.perform(request);
			MvcResult result = resultActions.andReturn();
			logger.info("########################   请求 status：{}", result.getResponse().getStatus());
			logger.info("########################   响应结果：{}", result.getResponse().getContentAsString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		out.println("################# end....QdzConsoleController #################");
	}

	@Test
	@Ignore
	public void searchCreditorBalanceInfo() {
		out.println("################# start....QdzConsoleController #################");
		try {
			RequestBuilder request = MockMvcRequestBuilders.post("/qdzConsoleController/searchCreditorBalanceInfo")
					.accept(MediaType.APPLICATION_JSON);

			ResultActions resultActions = mockMvc.perform(request);
			MvcResult result = resultActions.andReturn();
			logger.info("########################   请求 status：{}", result.getResponse().getStatus());
			logger.info("########################   响应结果：{}", result.getResponse().getContentAsString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		out.println("################# end....QdzConsoleController #################");
	}

	@Test
	@Ignore
	public void searchQdzBillWater() {
		out.println("################# start....QdzConsoleController #################");
		try {
			RequestBuilder request = MockMvcRequestBuilders.post("/qdzConsoleController/searchQdzBillWater")
					.accept(MediaType.APPLICATION_JSON);

			ResultActions resultActions = mockMvc.perform(request);
			MvcResult result = resultActions.andReturn();
			logger.info("########################   请求 status：{}", result.getResponse().getStatus());
			logger.info("########################   响应结果：{}", result.getResponse().getContentAsString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		out.println("################# end....QdzConsoleController #################");
	}

	@Test
	@Ignore
	public void searchHqProductInfo() {
		out.println("################# start....QdzConsoleController #################");
		try {
			RequestBuilder request = MockMvcRequestBuilders.post("/qdzConsoleController/searchHqProductInfo")
					.accept(MediaType.APPLICATION_JSON);

			ResultActions resultActions = mockMvc.perform(request);
			MvcResult result = resultActions.andReturn();
			logger.info("########################   请求 status：{}", result.getResponse().getStatus());
			logger.info("########################   响应结果：{}", result.getResponse().getContentAsString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		out.println("################# end....QdzConsoleController #################");
	}

	@Test
	@Ignore
	public void insertQdzRule() {
		out.println("################# start....QdzConsoleController #################");
		try {
			RequestBuilder request = MockMvcRequestBuilders.post("/qdzConsoleController/insertQdzRule")
					.accept(MediaType.APPLICATION_JSON).param("inOPPPerMonth", "100").param("inMaxMoneyPPPD", "100000")
					.param("inPayRate", "6").param("outOPPPerMonth", "9000").param("outMaxMoneyPPPD", "10000")
					.param("outPayRate", "4").param("currInterestRate", "5").param("investLowest", "100")
					.param("holdInvestMax", "80000").param("noInOutStartTimes", "08:30:00")
					.param("noInOutEndTimes", "15:30:00");

			ResultActions resultActions = mockMvc.perform(request);
			MvcResult result = resultActions.andReturn();
			logger.info("########################   请求 status：{}", result.getResponse().getStatus());
			logger.info("########################   响应结果：{}", result.getResponse().getContentAsString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		out.println("################# end....QdzConsoleController #################");
	}

	@Test
	@Ignore
	public void updateQdzRule() {
		out.println("################# start....QdzConsoleController #################");
		try {
			RequestBuilder request = MockMvcRequestBuilders.post("/qdzConsoleController/updateQdzRule")
					.accept(MediaType.APPLICATION_JSON).param("qdzRuleId", "7").param("state", "1");

			ResultActions resultActions = mockMvc.perform(request);
			MvcResult result = resultActions.andReturn();
			logger.info("########################   请求 status：{}", result.getResponse().getStatus());
			logger.info("########################   响应结果：{}", result.getResponse().getContentAsString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		out.println("################# end....QdzConsoleController #################");
	}

	@Test
	@Ignore
	public void insertQdzRecommendEarnRule() {
		out.println("################# start....QdzConsoleController #################");
		try {
			RequestBuilder request = MockMvcRequestBuilders.post("/qdzConsoleController/insertQdzRecommendEarnRule")
					.accept(MediaType.APPLICATION_JSON).param("friendLevelOne", "1").param("invNumOne", "1000")
					.param("rebatesRateOne", "5");

			ResultActions resultActions = mockMvc.perform(request);
			MvcResult result = resultActions.andReturn();
			logger.info("########################   请求 status：{}", result.getResponse().getStatus());
			logger.info("########################   响应结果：{}", result.getResponse().getContentAsString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		out.println("################# end....QdzConsoleController #################");
	}

	@Test
	 @Ignore
	public void updateQdzRecommendEarnRule() {
		out.println("################# start....QdzConsoleController #################");
		try {
			RequestBuilder request = MockMvcRequestBuilders.post("/qdzConsoleController/updateQdzRecommendEarnRule")
					.accept(MediaType.APPLICATION_JSON).param("state", "5").param("vasRebatesRuleId", "9");

			ResultActions resultActions = mockMvc.perform(request);
			MvcResult result = resultActions.andReturn();
			logger.info("########################   请求 status：{}", result.getResponse().getStatus());
			logger.info("########################   响应结果：{}", result.getResponse().getContentAsString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		out.println("################# end....QdzConsoleController #################");
	}
}
