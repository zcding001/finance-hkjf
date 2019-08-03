package com.hongkun.finance.api.controller;

import com.hongkun.finance.user.model.RegUser;
import com.yirun.framework.core.commons.Constants;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/applicationContext-db.xml",
		"classpath:spring/applicationContext-jms.xml", "classpath:spring/applicationContext-dubbo.xml",
		"classpath:springmvc-config.xml" })
@WebAppConfiguration
public class BaseControllerTest {

	@Autowired
	protected WebApplicationContext wac;

	@Before() // 这个方法在每个方法执行之前都会执行一遍
	public void setup() {
	}

	/**
	 * @Description : 执行测试操作
	 * @Method_Name : doTest
	 * @param uri
	 * @param params
	 * @throws Exception
	 * @return : void
	 * @Creation Date : 2018年3月20日 下午6:14:08
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	private String execTest(String uri, MultiValueMap<String, String> params, Integer regUserId) {
		String resMsg = "";
		try {
			if (!uri.startsWith("/")) {
				uri = "/" + uri;
			}

			// 初始化请求
			MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.get(uri)
					.contentType(MediaType.APPLICATION_FORM_URLENCODED);// 数据的格式

			// 设置请求参数
			if (params != null && params.size() > 0) {
				mockHttpServletRequestBuilder.params(params);
			}
			// 设置登录用户
			if (regUserId != null && regUserId > 0) {
				RegUser user = new RegUser();
				user.setId(regUserId);
				user.setType(1);
				user.setIdentify(1);
				user.setLogin(Long.parseLong("18301306330"));
				mockHttpServletRequestBuilder.sessionAttr(Constants.CURRENT_USER, user);
			}
			resMsg = MockMvcBuilders.webAppContextSetup(wac).build().perform(mockHttpServletRequestBuilder)// 封装请求数据
					.andDo(MockMvcResultHandlers.print()) // 打印出请求和相应的内容
					.andReturn().getResponse().getContentAsString(); // 将相应的数据转换为字符串
			System.out.println("========================== 测试结果   =============================");
			System.out.println("=");
			System.out.println(resMsg);
			System.out.println("=");
			System.out.println("========================== 测试结果   =============================");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("========================== 测试结束   =============================");
		return resMsg;
	}

	/**
	 * @Description : 执行测试操作
	 * @Method_Name : doTest
	 * @param uri
	 * @param params
	 * @throws Exception
	 * @return : void
	 * @Creation Date : 2018年3月20日 下午6:29:31
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	protected String doTest(String uri, Map<String, String> params) {
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		params.forEach((k, v) -> map.add(k, v));
		return this.execTest(uri, map, null);
	}

	/**
	 * @Description : 执行测试操作
	 * @Method_Name : doTest
	 * @param uri
	 * @param params
	 * @param regUserId:
	 *            存储于session中的用户id
	 * @return : void
	 * @Creation Date : 2018年3月21日 上午9:28:28
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	protected String doTest(String uri, Map<String, String> params, Integer regUserId) {
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		params.forEach((k, v) -> map.add(k, v));
		return this.execTest(uri, map, regUserId);
	}

	/**
	 * @Description : 执行测试操作
	 * @Method_Name : doTest
	 * @param uri
	 * @return : void
	 * @Creation Date : 2018年3月20日 下午6:55:38
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	protected String doTest(String uri) {
		return this.execTest(uri, new LinkedMultiValueMap<>(), null);
	}

	/**
	 * @Description : 执行测试操作
	 * @Method_Name : doTest
	 * @param uri
	 * @param regUserId:
	 *            存储于session中的用户id
	 * @return : void
	 * @Creation Date : 2018年3月21日 上午9:28:53
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	protected String doTest(String uri, Integer regUserId) {
		return this.execTest(uri, new LinkedMultiValueMap<>(), regUserId);
	}

	/**
	 * @Description : 获得默认的分页参数
	 * @Method_Name : getPagerParams
	 * @return
	 * @return : Map<String,String>
	 * @Creation Date : 2018年3月21日 下午2:12:20
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	protected Map<String, String> getPagerParams() {
		Map<String, String> params = new HashMap<>();
		params.put("currentPage", "1");
		params.put("pageSize", "20");
		return params;
	}
}
