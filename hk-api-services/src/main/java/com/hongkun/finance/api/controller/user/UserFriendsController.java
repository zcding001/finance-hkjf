package com.hongkun.finance.api.controller.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hongkun.finance.user.facade.RegUserFriendsFacade;
import com.hongkun.finance.user.model.RegUserDetail;
import com.hongkun.finance.user.service.RegUserDetailService;
import com.yirun.framework.core.utils.PropertiesHolder;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.invest.facade.BidInvestFacade;
import com.hongkun.finance.invest.model.vo.BidInvestVoForApp;
import com.hongkun.finance.invest.model.vo.BidInvestVoForSales;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.model.RegUserFriends;
import com.hongkun.finance.user.model.RegUserFriendsGroup;
import com.hongkun.finance.user.service.RegUserFriendsGroupService;
import com.hongkun.finance.user.service.RegUserFriendsService;
import com.hongkun.finance.user.support.security.OperationTypeEnum;
import com.hongkun.finance.user.support.security.component.annotation.AskForPermission;
import com.hongkun.finance.user.utils.BaseUtil;
import com.hongkun.finance.vas.facade.RecommendEarnFacade;
import com.hongkun.finance.vas.model.vo.RecommendEarnForAppVo;
import com.hongkun.finance.vas.service.VasBidRecommendEarnService;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.AppResultUtil;
import com.yirun.framework.core.utils.pager.Pager;
/**
 * 
 * @Description   : 邀请好友接口
 * @Project       : hk-api-services
 * @Program Name  : com.hongkun.finance.api.controller.user.UserFriendsController.java
 * @Author        : xuhuiliu@honghun.com.cn 刘旭辉
 */
@Controller
@RequestMapping("/userFriends/")
@AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
public class UserFriendsController {
	
	@Reference(timeout=5000)
	private RegUserFriendsService regUserFriendsService;
	@Reference(timeout=5000)
	private RegUserFriendsGroupService regUserFriendsGroupService;
	@Reference
	private BidInvestFacade bidInvestFacade;
	@Reference
	private VasBidRecommendEarnService vasBidRecommendEarnService;
	@Reference
	private RecommendEarnFacade recommendEarnFacade;
	@Reference
	private RegUserDetailService regUserDetailService;
	@Reference
	private RegUserFriendsFacade regUserFriendsFacade;
	
	/**
	 * 
	 *  @Description    : 我的好友-好友分组列表
	 *  @Method_Name    : myFriendGroupList
	 *  @return
	 *  @return         : Map<String,Object>
	 *  @Creation Date  : 2018年3月9日 下午2:30:53 
	 *  @Author         : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	@RequestMapping("/myFriendGroupList")
    @ResponseBody
	public Map<String,Object> myFriendGroupList(){
		RegUser regUser = BaseUtil.getLoginUser();
		List<RegUserFriendsGroup> groups = regUserFriendsGroupService.findGroupsAndNumByUserId(regUser.getId());
		return AppResultUtil.successOfListInProperties(groups, "查询成功", "id","name","type","userNum");
	}
	
	/**
	 * 
	 *  @Description    : 更改分组名称
	 *  @Method_Name    : editGroupName
	 *  @param groupId
	 *  @param groupName
	 *  @return
	 *  @return         : Map<String,Object>
	 *  @Creation Date  : 2018年3月12日 上午9:43:36 
	 *  @Author         : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	@RequestMapping("/editGroupName")
    @ResponseBody
	public Map<String,Object> editGroupName(Integer groupId,String groupName){
		//校验修改的分组是当前登录用户
		RegUser regUser = BaseUtil.getLoginUser();
		ResponseEntity<String> response = regUserFriendsGroupService.editGroupName(groupName,groupId,regUser.getId());
		if(BaseUtil.error(response)){
			return AppResultUtil.errorOfMsg(response.getResMsg());
		}
		return AppResultUtil.successOfMsg(response.getResMsg());
	}
	
	/**
	 * 
	 *  @Description    : 删除自定义分组
	 *  @Method_Name    : delGroupById
	 *  @param groupId
	 *  @return
	 *  @return         : Map<String,Object>
	 *  @Creation Date  : 2018年3月12日 下午3:08:27 
	 *  @Author         : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	@RequestMapping("/delGroupById")
    @ResponseBody
	public Map<String,Object> delGroupById(Integer groupId){
		//校验修改的分组是当前登录用户
		RegUser regUser = BaseUtil.getLoginUser();
		ResponseEntity<String> response = regUserFriendsGroupService.delGroupById(groupId,regUser.getId());
		if(BaseUtil.error(response)){
			return AppResultUtil.errorOfMsg(response.getResMsg());
		}
		return AppResultUtil.successOfMsg(response.getResMsg());
	}
	/**
	 *  @Description    : 设置好友信息
	 *  @Method_Name    : setUpMyFriend
	 *  @param friendUserId
	 *  @param memoName
	 *  @param remarks
	 *  @param groupId
	 *  @return
	 *  @return         : Map<String,Object>
	 *  @Creation Date  : 2018年3月12日 下午3:12:46 
	 *  @Author         : xuhuiliu@honghun.com.cn 刘旭辉findFriendsByGroupId
	 */
	@RequestMapping("/setUpMyFriend")
    @ResponseBody
	public Map<String,Object> setUpMyFriend(Integer friendUserId,String memoName,String remarks,Integer groupId){
		RegUser regUser = BaseUtil.getLoginUser();
		ResponseEntity<String> response = regUserFriendsService.setUpMyFriend(friendUserId,memoName,remarks,groupId,regUser.getId());
		if(BaseUtil.error(response)){
			return AppResultUtil.errorOfMsg(response.getResMsg());
		}
		return AppResultUtil.successOfMsg(response.getResMsg());
	}
	/**
	 * 
	 *  @Description    : 批量添加好友到指定分组
	 *  @Method_Name    : addFriendsToGroup
	 *  @param groupId
	 *  @param idList
	 *  @return
	 *  @return         : Map<String,Object>
	 *  @Creation Date  : 2018年3月12日 下午6:09:32 
	 *  @Author         : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	@RequestMapping("/addFriendsToGroup")
    @ResponseBody
	public Map<String,Object> addFriendsToGroup(Integer groupId,String idList){
		RegUser regUser = BaseUtil.getLoginUser();
		//解析好友id
		ResponseEntity<String> response = regUserFriendsGroupService.addFriendsToGroup(groupId,idList,regUser.getId());
		if(BaseUtil.error(response)){
			return AppResultUtil.errorOfMsg(response.getResMsg());
		}
		return AppResultUtil.successOfMsg(response.getResMsg());
	}
	/**
	 * 
	 *  @Description    : 创建自定义分组
	 *  @Method_Name    : addFriendGroup
	 *  @param name
	 *  @param sort
	 *  @return
	 *  @return         : Map<String,Object>
	 *  @Creation Date  : 2018年3月15日 上午9:42:25 
	 *  @Author         : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	@RequestMapping("/addFriendGroup")
    @ResponseBody
	public Map<String,Object> addFriendGroup(String name,@RequestParam(value = "sort",required = false) Integer sort){
		RegUser regUser = BaseUtil.getLoginUser();
		//解析好友id
		ResponseEntity<String> response = regUserFriendsGroupService.addFriendGroup(name,regUser.getId(),sort);
		if(BaseUtil.error(response)){
			return AppResultUtil.errorOfMsg(response.getResMsg());
		}
		return AppResultUtil.successOfMsg(response.getResMsg());
	}
	/**
	 * 
	 *  @Description    : 查询某分组下好友列表
	 *  @Method_Name    : findFriendsByGroupId
	 *  @param groupId
	 *  @return
	 *  @return         : Map<String,Object>
	 *  @Creation Date  : 2018年3月15日 上午9:47:47 
	 *  @Author         : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/findFriendsByGroupId")
    @ResponseBody
	public Map<String,Object> findFriendsByGroupId(Integer groupId){
		RegUser regUser = BaseUtil.getLoginUser();
		ResponseEntity response = regUserFriendsService.findFriendsByGroupId(regUser.getId(),groupId);
		if(BaseUtil.error(response)){
			return AppResultUtil.errorOfMsg(response.getResMsg());
		}
		return returnFriendsList((List<RegUserFriends>)response.getResMsg());
	}
	/**
	 * 
	 *  @Description    : 查询未分组好友列表
	 *  @Method_Name    : findUserFriendsUnGrouped
	 *  @return
	 *  @return         : Map<String,Object>
	 *  @Creation Date  : 2018年3月16日 上午9:31:28 
	 *  @Author         : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	@RequestMapping("/findUserFriendsUnGrouped")
    @ResponseBody
	public Map<String,Object> findUserFriendsUnGrouped(){
		RegUser regUser = BaseUtil.getLoginUser();
		List<RegUserFriends> resultList = regUserFriendsService.findUserFriendsUnGrouped(regUser.getId());
		return returnFriendsList(resultList);
	}
	
	/**
	 *  @Description    : 返回json好友列表
	 *  @Method_Name    : returnFriendsList
	 *  @param resultList
	 *  @return
	 *  @return         : Map<String,Object>
	 *  @Creation Date  : 2018年3月21日 上午9:56:37 
	 *  @Author         : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	private Map<String,Object> returnFriendsList(List<RegUserFriends> resultList){
		return AppResultUtil.successOfListInProperties(resultList, "查询成功", "id","groupId","groupName",
				"regUserId","memoName","realName","tel","investState","grade","remarks");
	}
	/**
	 *  @Description    : 通过手机号或姓名模糊查询好友信息
	 *  @Method_Name    : findUserFriendsByCondition
	 *  @return
	 *  @return         : Map<String,Object>
	 *  @Creation Date  : 2018年3月16日 上午9:55:03 
	 *  @Author         : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	@RequestMapping("/findUserFriendsByCondition")
    @ResponseBody
	public Map<String,Object> findUserFriendsByCondition(String param){
		RegUser regUser = BaseUtil.getLoginUser();
		if(StringUtils.isBlank(param)){
			return AppResultUtil.errorOfMsg("请输入需要查询的手机号或姓名");
		}
		List<RegUserFriends> resultList = regUserFriendsService.findUserFriendsByCondition(regUser.getId(),param);
		return returnFriendsList(resultList); 
	}
	
	/**
	 * 
	 *  @Description    : 查询投资记录列表
	 *  @Method_Name    : friendInvestList
	 *  @param pager
	 *  @param friendUserId
	 *  @return
	 *  @return         : Map<String,Object>
	 *  @Creation Date  : 2018年3月16日 下午6:50:09 
	 *  @Author         : xuhuiliu@honghun.com.cn 刘旭辉
	 */
  	@RequestMapping("/friendInvestList")
    @ResponseBody
	public Map<String, Object> friendInvestList(Pager pager,Integer friendUserId) {
  		RegUser regUser = BaseUtil.getLoginUser();
    	ResponseEntity<?> result = bidInvestFacade.findBidInvestListForApp(regUser.getId(),friendUserId,pager);
    	if(BaseUtil.error(result)){
    		return AppResultUtil.errorOf(result.getResMsg());
    	}
    	return AppResultUtil.successOfList((List<BidInvestVoForApp>)result.getResMsg());
	}
  	
  	/**
  	 *  @Description    : 查询好友投资信息（只针对销售）
  	 *  @Method_Name    : friendInvestListForSales
  	 *  @param pager 
  	 *  @param friendUserId 好友id
  	 *  @param state 标的状态 
  	 *  @return
  	 *  @return         : Map<String,Object>
  	 *  @Creation Date  : 2018年3月21日 下午4:02:28 
  	 *  @Author         : xuhuiliu@honghun.com.cn 刘旭辉
  	 */
  	@RequestMapping("/friendInvestListForSales")
    @ResponseBody
	public Map<String, Object> friendInvestListForSales(Pager pager,Integer friendUserId,Integer state) {
  		RegUser regUser = BaseUtil.getLoginUser();
    	ResponseEntity<?> result = bidInvestFacade.friendInvestListForSales(regUser.getId(),state,friendUserId,pager);
    	if(BaseUtil.error(result)){
    		return AppResultUtil.errorOf(result.getResMsg());
    	}
    	return AppResultUtil.successOf(result.getResMsg());
	}

	/**
	*  @Description    ：查看好友可用余额
	*  @Method_Name    ：findFriendsUseAbleMoney
	*  @param friendUserId
	*  @return java.util.Map<java.lang.String,java.lang.Object>
	*  @Creation Date  ：2018/10/16
	*  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
	*/
	@RequestMapping("/findFriendsUseAbleMoney")
	@ResponseBody
	public Map<String, Object> findFriendsUseAbleMoney(Integer friendUserId) {
		RegUser regUser = BaseUtil.getLoginUser();
		ResponseEntity<?> result = bidInvestFacade.findFriendsUseAbleMoney(regUser.getId(),friendUserId);
		if(BaseUtil.error(result)){
			return AppResultUtil.errorOf(result.getResMsg());
		}
		return AppResultUtil.successOf(result.getResMsg());
	}

	/**
	 * 
	 *  @Description    : 查询我的佣金
	 *  @Method_Name    : friendInviteCommision
	 *  @return
	 *  @return         : Map<String,Object>
	 *  @Creation Date  : 2018年3月16日 下午6:51:19 
	 *  @Author         : xuhuiliu@honghun.com.cn 刘旭辉
	 */
  	@RequestMapping("/friendInviteCommision")
    @ResponseBody
  	public Map<String,Object> friendInviteCommision(){
  		RegUser regUser = BaseUtil.getLoginUser();
  		ResponseEntity<?> result =  vasBidRecommendEarnService.friendInviteCommision(regUser.getId());
  		if(BaseUtil.error(result)){
    		return AppResultUtil.errorOf(result.getResMsg());
    	}
    	return AppResultUtil.successOf(result.getResMsg());
  	}
  	
  	/**
  	 * 
  	 *  @Description    : 查看佣金明细
  	 *  @Method_Name    : commisionDetails
  	 *  @param pager
  	 *  @return
  	 *  @return         : Map<String,Object>
  	 *  @Creation Date  : 2018年3月17日 下午5:18:22 
  	 *  @Author         : xuhuiliu@honghun.com.cn 刘旭辉
  	 */
	@RequestMapping("/commisionDetails")
    @ResponseBody
  	public Map<String,Object> commisionDetails(Pager pager){
  		RegUser regUser = BaseUtil.getLoginUser();
  		ResponseEntity<?> result = recommendEarnFacade.recommendEarnListForApp(regUser.getId(), pager);
  		if(BaseUtil.error(result)){
    		return AppResultUtil.errorOf(result.getResMsg());
    	}
    	return AppResultUtil.successOfList((List<RecommendEarnForAppVo>)result.getResMsg(), "regUserId","recommendRegUserId","sortColumns","queryColumnId","regUserTel","type");
  	}
	
	/**
	 *  @Description    : 从自定义分组中移除好友
	 *  @Method_Name    : removeUserFriendByGroup
	 *  @param groupId
	 *  @param userFriendId
	 *  @return
	 *  @return         : Map<String,Object>
	 *  @Creation Date  : 2018年3月20日 下午6:11:26 
	 *  @Author         : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	@RequestMapping("/removeUserFriendByGroup")
    @ResponseBody
  	public Map<String,Object> removeUserFriendByGroup(Integer groupId,Integer userFriendId){
  		RegUser regUser = BaseUtil.getLoginUser();
  		ResponseEntity<String> result = regUserFriendsService.removeUserFriendByGroup(regUser.getId(),userFriendId,groupId);
  		if(BaseUtil.error(result)){
    		return AppResultUtil.errorOfMsg(result.getResMsg());
    	}
    	return AppResultUtil.successOfMsg(result.getResMsg());
  	}

  	/**
  	*  @Description    ：好友邀请页面
  	*  @Method_Name    ：friendsIndex
  	*  @return java.util.Map<java.lang.String,java.lang.Object>
  	*  @Creation Date  ：2018/10/15
  	*  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
  	*/
	@RequestMapping("/friendsIndex")
	@ResponseBody
	public Map<String,Object> friendsIndex(){
		RegUserDetail regUser = BaseUtil.getRegUserDetail(() -> this.regUserDetailService.findRegUserDetailByRegUserId(BaseUtil.getLoginUserId()));
		//邀请码]
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("inviteNo",regUser.getInviteNo());
		params.put("title","快来，鸿坤金服送钱啦~");
		params.put("content","快来！我正在使用鸿坤金服理财，现在注册即送加券息或投资红包，点击链接完成注册。鸿坤金服，我推荐!");
		params.put("url", PropertiesHolder.getProperty("hkjf_invite_regist_url") + "?inviteNo=" +regUser.getInviteNo());
		return AppResultUtil.successOf(params);
	}
	/**
	*  @Description    ：方法描述信息
	*  @Method_Name    ：findUserFriendsForInvestDue
	*
	*  @return java.util.Map<java.lang.String,java.lang.Object>
	*  @Creation Date  ：2018/10/17
	*  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
	*/
	@RequestMapping("/findUserFriendsForInvestDue")
	@ResponseBody
	public Map<String,Object> findUserFriendsForInvestDue(){
		RegUser regUser = BaseUtil.getLoginUser();
		ResponseEntity result = regUserFriendsFacade.findUserFriendsForInvestDue(regUser.getId());
		return returnFriendsList((List<RegUserFriends>) result.getResMsg());
	}
}
