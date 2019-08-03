package com.hongkun.finance.api.controller.user;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/applicationContext-db.xml",
		"classpath:spring/applicationContext-jms.xml", "classpath:spring/applicationContext-dubbo.xml",
		"classpath:springmvc-config.xml" })
@WebAppConfiguration
public class QdzControllerTest {
	protected MockMvc mockMvc;

	@Autowired
	protected WebApplicationContext wac;

	@Before() // 这个方法在每个方法执行之前都会执行一遍
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build(); // 初始化MockMvc对象
	}

	@Test
	public void qdzTransferIn() throws Exception {
		String responseString = mockMvc.perform(MockMvcRequestBuilders.get("/qdzController/qdzTransferIn") // 请求的url,请求的方法是get
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)// 数据的格式
				.param("money", "200") // 添加参数(可以添加多个)
				.param("source", "11")// 添加参数(可以添加多个)
		).andDo(MockMvcResultHandlers.print()) // 打印出请求和相应的内容
				.andReturn().getResponse().getContentAsString(); // 将相应的数据转换为字符串
		System.out.println("-----返回的json = " + responseString);
	}

	// @Test
	public void searchBankCardList() throws Exception {
		String responseString = mockMvc
				.perform(MockMvcRequestBuilders.get("/qdzController/qdzTransferOut") // 请求的url,请求的方法是get
						.contentType(MediaType.APPLICATION_FORM_URLENCODED)// 数据的格式
						.param("money", "100").param("source", "11"))
				.andDo(MockMvcResultHandlers.print()) // 打印出请求和相应的内容
				.andReturn().getResponse().getContentAsString(); // 将相应的数据转换为字符串
		System.out.println("-----返回的json = " + responseString);
	}

}
