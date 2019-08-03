package com.hongkun.finance.api.controller.user;

import com.hongkun.finance.api.controller.BaseControllerTest;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class UserControllerTest extends BaseControllerTest {

	private final static String CONTROLLER = "userController/";

	@Test
	public void testLogin() {
		Map<String, String> params = new HashMap<>();
		params.put("login", "14510000110");
		params.put("passwd",
				"BPHVdiC3Qkwzv8JjKLqqxMpWRYud2OM/e1a2t6iip5vlwfDUc67z6kCZMHewyEJEH8SUKDlU/qUnPcXGXM89laylLg3V3B+yKzeBXIhbLvazSAX6pH601VVm+nAHXQeD1qIq11MKtvTdV+7E4HT4AHusDvyJgUPIFw9xeDizoMI=");
		params.put("pwdLength", "6");
		super.doTest(CONTROLLER + "login", params);
	}

	@Test
	public void testAccount() {
		super.doTest(CONTROLLER + "account", 5);
	}

	@Test
	public void testRealName() {
		Map<String, String> params = new HashMap<>();
		params.put("realName", "殷问枫");
		params.put("idCard", "150725197611278440");
		super.doTest(CONTROLLER + "realName", params, 1093);
	}

	@Test
	public void testGetPushData() {
		super.doTest(CONTROLLER + "getPushData", 5);
	}

	@Test
	public void testUpdateNickName() {
		Map<String, String> params = new HashMap<>();
		params.put("nickName", "junitNickName");
		super.doTest(CONTROLLER + "updateNickName", params, 5);
	}

	@Test
	public void testUpdatePassword() {
		Map<String, String> params = new HashMap<>();
		// a12345
		params.put("oldPasswd",
				"BPHVdiC3Qkwzv8JjKLqqxMpWRYud2OM/e1a2t6iip5vlwfDUc67z6kCZMHewyEJEH8SUKDlU/qUnPcXGXM89laylLg3V3B+yKzeBXIhbLvazSAX6pH601VVm+nAHXQeD1qIq11MKtvTdV+7E4HT4AHusDvyJgUPIFw9xeDizoMI=");
		params.put("oldPasswdLength", "6");
		// a123456
		params.put("newPasswd",
				"GPOohtUoW/CExnESQiWAOqNeviigApxIHETyGaDJfIuqj/3L38vECANudNYVUW181xp8auJqd75rJWeqaVl+oaZwj8xPidOl8bn1OOU0Qyd/002dG8BZwFWb35Qib7F/XrY7+XsL2NU4x74ekloZ3MWNr3H0VttwxupRaW4lotg=");
		params.put("newPasswdLength", "7");
		super.doTest(CONTROLLER + "updatePassword", params, 5);
	}
	
	@Test
	public void testReg() {
		Map<String, String> params = new HashMap<>();
		params.put("login", "14510000110");
		params.put("passwd", "toBLA75h93uavt%252BUzswmO8jDisXTbH/oH8dk5hxaFP%252BSWptLzGDvoshQEhdldZm%252BIZNI6dzRCoMU%0AjJxsvhiabPy4bQ0lAf9%252BAhtZ7O4f4xqJxLCWmVTCtgLh3RScZvZYrYiUD08ZtAsE6fek51Z%252BDgHT%0Al2pAbYN5qF7lKIAY7cA%3D%0A");
		params.put("pwdLength", "10");
		params.put("smsCode", "12");
		super.doTest(CONTROLLER + "reg", params);
	}

	@Test
	public void testGetServiceHotline() {
		super.doTest(CONTROLLER + "getServiceHotline");
	}
}
