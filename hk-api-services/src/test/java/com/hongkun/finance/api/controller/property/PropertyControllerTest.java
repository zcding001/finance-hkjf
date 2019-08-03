package com.hongkun.finance.api.controller.property;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.gexin.fastjson.JSON;
import com.hongkun.finance.api.controller.BaseControllerTest;

public class PropertyControllerTest extends BaseControllerTest {
	
	   private final static String CONTROLLER = "propertyController/";
	   
	   @Test
	   public void getPointMoney() throws Exception 
	   {
		    Map<String, String> params = new HashMap<>();
			params.put("money", "100");
			super.doTest(CONTROLLER + "getPointMoney", params,33);
	   }
	   
	   @Test
	   public void proPayDetail() throws Exception 
	   {
		   Map<String, String> params = new HashMap<>();
			params.put("payRecordId", "154");
			super.doTest(CONTROLLER + "proPayDetail",params, 33);
	   }
	   @Test
	   public void getProAddressList() throws Exception 
	   {
			super.doTest(CONTROLLER + "getProAddressList");
	   }
	   @Test
	   public void getPropertypayRecordList() throws Exception 
	   {
		    Map<String, String> params = new HashMap<>();
			params.put("pageSize", "20");
			params.put("currentPage", "1");
			//params.put("regUserId", "1133");
			super.doTest(CONTROLLER + "getPropertypayRecordList",params);
			
	   }
	   @Test
	   public void delProAddress() throws Exception 
	   {
		    Map<String, String> params = new HashMap<>();
			params.put("addressId", "177");
			super.doTest(CONTROLLER + "delProAddress",params, 33);
	   }
	   
	   @Test
	   public void addProAddress() throws Exception 
	   {
		   Map<String, String> params = new HashMap<>();
			params.put("communityId", "11");
			params.put("floor", "13#");
			params.put("unit", "2");
			params.put("door", "901");
			params.put("note", "小区");
			super.doTest(CONTROLLER + "addProAddress",params, 33);
	   }
	   
	   
	   @Test
	   public void payProperty() throws Exception 
	   {
		   Map<String, String> params = new HashMap<>();
			params.put("usePoints", "10");
			params.put("proAddressId", "229");
			params.put("money", "100");
			params.put("startTime", "2018-12-11");
			params.put("endTime", "2019-12-11");
			params.put("payType", "1");
			params.put("note", "测试");
			super.doTest(CONTROLLER + "payProperty",params, 2);
	   }
	   @Test
	   public void  payPropertyCallBack() throws Exception{
		   Map<String, String> params = new HashMap<>();
		   params.put("proPayRecordId", "20");
		   params.put("paymentFlowId", "PY1010002018040310581111801378");
		   super.doTest(CONTROLLER + "payPropertyCallBack",params, 2);
		}
	   
	   @Test
	   public void findCommunityList(){
		   super.doTest(CONTROLLER + "findCommunityList");
	   }
	   
	   @Test
	   public void toPropertyInfo(){
		   Map<String, String> params = new HashMap<>();
			params.put("platformSource", "11");
		   super.doTest(CONTROLLER + "toPropertyInfo",params,33);
	   }
}
