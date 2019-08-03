package com.hongkun.finance.api.controller.user;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.hongkun.finance.api.controller.BaseControllerTest;

public class UserFriendsControllerTest extends BaseControllerTest {
	   
	   private final static String CONTROLLER = "userFriends/";
	   
	   @Test
	   public void addFriendGroup() throws Exception{
		   Map<String, String> params = new HashMap<>();
		   params.put("name", "1111");
		   super.doTest(CONTROLLER + "addFriendGroup", params,33);
	   }
	   
	   @Test
	   public void myFriendGroupList() throws Exception 
	   {
		   super.doTest(CONTROLLER + "myFriendGroupList", 33);
	   }
	   
	   @Test
	   public void editGroupName() throws Exception 
	   {
		   Map<String, String> params = new HashMap<>();
		   params.put("groupId", "42808");
		   params.put("groupName", "222");
		   super.doTest(CONTROLLER + "editGroupName", 33);
	   }
	   
	   @Test
	   public void delGroupById() throws Exception 
	   {
		   Map<String, String> params = new HashMap<>();
		   params.put("groupId", "42808");
		   super.doTest(CONTROLLER + "delGroupById", 33);
	   }
	   
	   @Test
	   public void setUpMyFriend() throws Exception 
	   {
		   
		   Map<String, String> params = new HashMap<>();
		   params.put("groupId", "42803");
		   params.put("friendUserId", "37");
		   params.put("memoName", "测试一");
		   params.put("remarks", "测试");
		   super.doTest(CONTROLLER + "setUpMyFriend", 33);
	   }
	   
	   
	   @Test
	   public void addFriendsToGroup() throws Exception 
	   {
		   
		   Map<String, String> params = new HashMap<>();
		   params.put("groupId", "42809");
		   params.put("idList", "37");
		   super.doTest(CONTROLLER + "addFriendsToGroup", 33);
	   }
	   
	   @Test
	   public void findFriendsByGroupId() throws Exception 
	   {
		   Map<String, String> params = new HashMap<>();
		   params.put("groupId", "42803");
		   super.doTest(CONTROLLER + "findFriendsByGroupId", 33);
	   }
	   
	   @Test
	   public void findUserFriendsUnGrouped() throws Exception 
	   {
		   super.doTest(CONTROLLER + "findUserFriendsUnGrouped", 33);
	   }
	   
	   @Test
	   public void findUserFriendsByCondition() throws Exception 
	   {
		   Map<String, String> params = new HashMap<>();
		   params.put("param", "温振国");
		   super.doTest(CONTROLLER + "findUserFriendsByCondition", 33);
	   }
	   
	   @Test
	   public void friendInvestList() throws Exception 
	   {
		   Map<String, String> params = new HashMap<>();
		   params.put("friendUserId", "37");
		   params.put("pageSize", "10");
		   params.put("currentPage", "1");
		   super.doTest(CONTROLLER + "friendInvestList", 33);
		   
	   }
	   
	   @Test
	   public void friendInviteCommision() throws Exception 
	   {
		   super.doTest(CONTROLLER + "friendInviteCommision", 33);
	   }
	   
	   
	   @Test
	   public void commisionDetails() throws Exception 
	   {
		   super.doTest(CONTROLLER + "commisionDetails", 33);
	   }
	   
	   
	   @Test
	   public void removeUserFriendByGroup() throws Exception 
	   {
		   Map<String, String> params = new HashMap<>();
		   params.put("groupId", "42809");
		   params.put("userFriendId", "22409");
		   super.doTest(CONTROLLER + "removeUserFriendByGroup", 33);
	   }
	   
	   @Test
	   public void friendInvestListForSales() throws Exception 
	   {
		   Map<String, String> params = new HashMap<>();
		   params.put("friendUserId", "1005");
		   params.put("state", "5");
		   super.doTest(CONTROLLER + "friendInvestListForSales",params, 33);
	   }
}
