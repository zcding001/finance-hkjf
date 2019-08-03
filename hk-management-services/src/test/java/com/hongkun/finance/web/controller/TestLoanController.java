//package com.hongkun.finance.web.controller;
//
//import static java.lang.System.out;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.RequestBuilder;
//import org.springframework.test.web.servlet.ResultActions;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import com.alibaba.dubbo.config.annotation.Reference;
//import com.hongkun.finance.user.service.RegUserService;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {
//		"classpath:spring/applicationContext-dubbo.xml",
//		"classpath:springmvc-config.xml"
//		,"classpath:spring/applicationContext-jms.xml"
//		})
//public class TestLoanController {
//	private static final Logger logger = LoggerFactory.getLogger(TestLoanController.class);
//
//	private MockMvc mockMvc = null;
//	@Reference
//	private RegUserService regUserService;
//	@Autowired
//	LoanController loanController;
//
//
//	@Before
//	public void before(){
////		mockMvc = MockMvcBuilders.webAppContextSetup((WebApplicationContext)ApplicationContextUtils.getApplicationContext()).build();
//		mockMvc = MockMvcBuilders.standaloneSetup(loanController).build();
////		this.regUserService.findRegUserById(testUserId);
//	}
//
//	@Test
//	public void testInvest(){
//		out.println("################# start....makeLoans #################");
//		try {
//			RequestBuilder request = MockMvcRequestBuilders.post("/loanController/makeLoans")
//					.accept(MediaType.APPLICATION_JSON)
//					.param("bidInfoId", "4");//标的id
//			ResultActions resultActions = mockMvc.perform(request);
//			MvcResult result = resultActions.andReturn();
//			logger.info("########################   请求 status：{}", result.getResponse().getStatus());
//			logger.info("########################   响应结果：{}", result.getResponse().getContentAsString());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		out.println("################# end....makeLoans #################");
//	}
//}
