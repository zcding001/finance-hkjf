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

import com.hongkun.finance.web.controller.payment.PaymentReconciliationController;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/applicationContext-dubbo.xml", "classpath:springmvc-config.xml" })
public class TestPaymentController {
	private static final Logger logger = LoggerFactory.getLogger(TestQdzController.class);

	private MockMvc mockMvc = null;
	@Autowired
	PaymentReconciliationController paymentReconciliationController;

	@Before
	public void before() {
		mockMvc = MockMvcBuilders.standaloneSetup(paymentReconciliationController).build();
	}

	@Test
	 @Ignore
	public void searchByCondition() {
		out.println("################# start....paymentReconciliationController #################");
		try {
			RequestBuilder request = MockMvcRequestBuilders.post("/paymentReconciliationController/searchByCondition")
					.accept(MediaType.APPLICATION_JSON);

			ResultActions resultActions = mockMvc.perform(request);
			MvcResult result = resultActions.andReturn();
			logger.info("########################   请求 status：{}", result.getResponse().getStatus());
			logger.info("########################   响应结果：{}", result.getResponse().getContentAsString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		out.println("################# end....paymentReconciliationController #################");
	}

	@Test
	@Ignore
	public void testPayCheck() {
		out.println("################# start....paymentReconciliationController #################");
		try {
			RequestBuilder request = MockMvcRequestBuilders.post("/paymentReconciliationController/testPayCheck")
					.accept(MediaType.APPLICATION_JSON);

			ResultActions resultActions = mockMvc.perform(request);
			MvcResult result = resultActions.andReturn();
			logger.info("########################   请求 status：{}", result.getResponse().getStatus());
			logger.info("########################   响应结果：{}", result.getResponse().getContentAsString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		out.println("################# end....paymentReconciliationController #################");
	}
}
