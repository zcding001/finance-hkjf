package com.hongkun.finance.api.controller.loan;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.hongkun.finance.api.controller.BaseControllerTest;

public class LoanControllerTest extends BaseControllerTest{

	private final static String CONTROLLER = "/loanController/";
	private final static Integer REG_USER_ID = 5;
	
	@Test
	public void testReceiptPlanCount() {
		super.doTest(CONTROLLER + "receiptPlanCount", super.getPagerParams(), REG_USER_ID);
	}
	
	@Test
	public void testReceiptPlanList() {
		Map<String, String> params = super.getPagerParams();
		params.put("bidId", "71");
		super.doTest(CONTROLLER + "receiptPlanList", params, REG_USER_ID);
	}
	
	@Test
	public void testMyLoanList() {
		super.doTest(CONTROLLER + "myLoanList", super.getPagerParams(), REG_USER_ID);
	}
	
	@Test
	public void testRepayPlanList() {
		Map<String, String> params = new HashMap<>();
		params.put("bidId", "71");
		super.doTest(CONTROLLER + "repayPlanList", params, 3);
	}
	
	@Test
	public void testCalcAdvanceReapyAmount() {
		Map<String, String> params = new HashMap<>();
		params.put("reapyId", "122");
		params.put("capital", "5000");
		super.doTest(CONTROLLER + "calcAdvanceReapyAmount", params, 3);
	}
	
	@Test
	public void testRepay() {
		Map<String, String> params = new HashMap<>();
		params.put("repayId", "122");
		params.put("payPasswd", "BPHVdiC3Qkwzv8JjKLqqxMpWRYud2OM/e1a2t6iip5vlwfDUc67z6kCZMHewyEJEH8SUKDlU/qUnPcXGXM89laylLg3V3B+yKzeBXIhbLvazSAX6pH601VVm+nAHXQeD1qIq11MKtvTdV+7E4HT4AHusDvyJgUPIFw9xeDizoMI=");
		params.put("pwdLength", "6");
		params.put("capital", "5000");
		super.doTest(CONTROLLER + "repay", params, 3);
	}
	
}
