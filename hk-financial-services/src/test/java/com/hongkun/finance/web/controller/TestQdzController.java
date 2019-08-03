package com.hongkun.finance.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.user.service.RegUserService;
import com.hongkun.finance.web.controller.qdz.QdzController;
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

import static java.lang.System.out;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/applicationContext-dubbo.xml", "classpath:springmvc-config.xml",
		"classpath:spring/applicationContext-jms.xml" })
public class TestQdzController {
	private static final Logger logger = LoggerFactory.getLogger(TestQdzController.class);

	private MockMvc mockMvc = null;
	@Autowired
	private QdzController qdzController;
	@Reference
	private RegUserService regUserService;

	@Before
	public void before() {
//		mockMvc = MockMvcBuilders.standaloneSetup(qdzController)
//				.addMappedInterceptors(new String[] { "/qdzController/match" }, new AuthorityIntercepter()).build();

	}

	@Test
	public void testQdzController() {
		out.println("################# start....qdzController #################");
		try {
			RequestBuilder request = MockMvcRequestBuilders.post("/qdzController/qdzTransferIn")
					.accept(MediaType.APPLICATION_JSON).param("transMoney", "10000");
			ResultActions resultActions = mockMvc.perform(request);
			MvcResult result = resultActions.andReturn();
			logger.info("########################   请求 status：{}", result.getResponse().getStatus());
			logger.info("########################   响应结果：{}", result.getResponse().getContentAsString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		out.println("################# end....qdzController #################");
	}

	@Test

	public void testMatch() {
		out.println("################# start....qdzController #################");
		try {
			RequestBuilder request = MockMvcRequestBuilders.post("/qdzController/match")
					.accept(MediaType.APPLICATION_JSON);
			ResultActions resultActions = mockMvc.perform(request);
			MvcResult result = resultActions.andReturn();
			logger.info("########################   请求 status：{}", result.getResponse().getStatus());
			logger.info("########################   响应结果：{}", result.getResponse().getContentAsString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		out.println("################# end....qdzController #################");
	}

	@Test
	// @Ignore
	public void testqdzRateRecord() {
		out.println("################# start....qdzController #################");
		try {
			RequestBuilder request = MockMvcRequestBuilders.post("/qdzController/qdzRateRecord")
					.accept(MediaType.APPLICATION_JSON);
			ResultActions resultActions = mockMvc.perform(request);
			MvcResult result = resultActions.andReturn();
			logger.info("########################   请求 status：{}", result.getResponse().getStatus());
			logger.info("########################   响应结果：{}", result.getResponse().getContentAsString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		out.println("################# end....qdzController #################");
	}

	@Test
	public void testqdzTransferOut() {
		out.println("################# start....qdzController #################");
		try {
			RequestBuilder request = MockMvcRequestBuilders.post("/qdzController/qdzTransferOut")
					.accept(MediaType.APPLICATION_JSON).param("transMoney", "800");
			ResultActions resultActions = mockMvc.perform(request);
			MvcResult result = resultActions.andReturn();
			logger.info("########################   请求 status：{}", result.getResponse().getStatus());
			logger.info("########################   响应结果：{}", result.getResponse().getContentAsString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		out.println("################# end....qdzController #################");
	}
}
