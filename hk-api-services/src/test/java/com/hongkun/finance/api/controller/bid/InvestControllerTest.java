package com.hongkun.finance.api.controller.bid;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.hongkun.finance.api.controller.BaseControllerTest;

public class InvestControllerTest extends BaseControllerTest{

	private final static String CONTROLLER = "/investController/";
	private final static int REG_USER_ID = 5;
	
	@Test
	public void testInvestList() {
		Map<String, String> params = super.getPagerParams();
		super.doTest(CONTROLLER + "investList", params, REG_USER_ID);
	}
	
	@Test
	public void testBidAutoSchemeList() {
		super.doTest(CONTROLLER + "bidAutoSchemeList", REG_USER_ID);
	}
	
	@Test
	public void testDelBas() {
		Map<String, String> params = new HashMap<>();
		params.put("basId", "183");
		super.doTest(CONTROLLER + "delBas", params, REG_USER_ID);
	}
	
	@Test
	public void testUpdateBasState() {
		Map<String, String> params = new HashMap<>();
		params.put("basId", "");
		params.put("state", "");
		super.doTest(CONTROLLER + "updateBasState", params, REG_USER_ID);
	}
	
	@Test
	public void testSaveOrUpdateBas() {
		Map<String, String> params = new HashMap<>();
		params.put("minRate", "5");
		params.put("reserveAmount", "0");
//		还款方式(可多选) 0 不限 2 按月付息，到期还本 3 到期还本付息'
		params.put("repayType", "0");
		params.put("investTermMin", "1");
		params.put("investTermMax", "3");
		params.put("useCouponFlag", "0");
		params.put("priorityType", "1");
		params.put("bidPriority", "1320");
		params.put("state", "0");
		params.put("effectiveType", "1");
		params.put("effectiveStartTime", "");
		params.put("effectiveEndTime", "");
		super.doTest(CONTROLLER + "saveOrUpdateBas", params, REG_USER_ID);
	}
	
	@Test
	public void testInvest() {
		Map<String, String> params = new HashMap<>();
		params.put("paymentFlowId", "");	//1104 银行卡投资时，非空
		params.put("bidId", "71");
		params.put("investRedPacketId", "");
		params.put("investRaiseInterestId", "");
		params.put("money", "100");
		params.put("signType", "");
		params.put("sign", "");
		params.put("investWay", "1101");	//1101正常投资 1103：钱袋子投资 1104 银行卡投资
		params.put("source", "12");
		super.doTest(CONTROLLER + "invest", params, REG_USER_ID);
	}
}
