package com.hongkun.finance.api.controller.sms;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.hongkun.finance.api.controller.BaseControllerTest;

public class SmsControllerTest extends BaseControllerTest{

	private final static String CONTROLLER = "/smsController/";
	private final static Integer REG_USER_ID = 5;
	
	@Test
	public void testWebMsgList() {
		super.doTest(CONTROLLER, super.getPagerParams(), REG_USER_ID);
	}
	
	@Test
	public void testUpdateWebMsgState() {
		Map<String, String> params = new HashMap<>();
		params.put("ids", "185,186");
		params.put("state", "1");
		super.doTest(CONTROLLER + "updateWebMsgState", params, REG_USER_ID);
	}
	
	/**
	 *  @Description    : 需要禁用掉validCode的验证
	 *  @Method_Name    : testSendSmsCode
	 *  @return         : void
	 *  @Creation Date  : 2018年3月21日 下午1:39:52 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	@Test
	public void testSendSmsCode() {
		Map<String, String> params = new HashMap<>();
		params.put("login", "1355224081");
		params.put("validCode", "123");
		params.put("type", "1");
		super.doTest(CONTROLLER + "sendSmsCode", params);
	}
	
	@Test
	public void testSendSmsTelMsg() {
		Map<String, String> params = new HashMap<>();
		params.put("login", "14510000898");
		params.put("type", "1");
		super.doTest(CONTROLLER + "sendSmsTelMsg", params, REG_USER_ID);
	}
}
