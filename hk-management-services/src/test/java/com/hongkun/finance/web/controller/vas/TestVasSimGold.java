//package com.hongkun.finance.web.controller.vas;
//
//import static java.lang.System.out;
//
//import org.junit.Before;
//import org.junit.Ignore;
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
//import com.hongkun.finance.user.support.security.AuthorityIntercepter;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"classpath:spring/applicationContext-dubbo.xml",
//        "classpath:springmvc-config.xml"})
//public class TestVasSimGold {
//    private static final Logger logger = LoggerFactory.getLogger(TestVasSimGold.class);
//
//    private MockMvc mockMvc = null;
//    @Autowired
//    RecommendEarnController recommendEarnController;
//    @Autowired
//    SimGoldController simGoldController;
//
//
//    // private int testUserId = 12;
//
//    @Before
//    public void before() {
//        // mockMvc =
//        // MockMvcBuilders.webAppContextSetup((WebApplicationContext)ApplicationContextUtils.getApplicationContext()).build();
//        // mockMvc = MockMvcBuilders.standaloneSetup(recommendEarnController)
//        mockMvc = MockMvcBuilders.standaloneSetup(simGoldController)
//                .addMappedInterceptors(
//                        // new String[] {"/recommendEarnController/search.json"},
//                        new String[] {"/simGoldController/search.json"}, new AuthorityIntercepter())
//                .build();
//        // this.regUserService.findRegUserById(testUserId);
//    }
//
//    @Test
//    @Ignore
//    public void testRecommendEarn() {
//        out.println("################# start....RecommendEarn #################");
//        try {
//            RequestBuilder request =
//                    MockMvcRequestBuilders.post("/recommendEarnController/search.json")
//                            .accept(MediaType.APPLICATION_JSON);
//
//            ResultActions resultActions = mockMvc.perform(request);
//            MvcResult result = resultActions.andReturn();
//            logger.info("########################   请求 status：{}",
//                    result.getResponse().getStatus());
//            logger.info("########################   响应结果：{}",
//                    result.getResponse().getContentAsString());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        out.println("################# end....RecommendEarnt #################");
//    }
//
//    @Test
//    // @Ignore
//    public void testSimGold() {
//        out.println("################# start....testSimGold #################");
//        try {
//            RequestBuilder request = MockMvcRequestBuilders.post("/simGoldController/search.json")
//                    .accept(MediaType.APPLICATION_JSON);
//
//            ResultActions resultActions = mockMvc.perform(request);
//            MvcResult result = resultActions.andReturn();
//            logger.info("########################   请求 status：{}",
//                    result.getResponse().getStatus());
//            logger.info("########################   响应结果：{}",
//                    result.getResponse().getContentAsString());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//
//        out.println("################# end....testSimGold #################");
//    }
//}
