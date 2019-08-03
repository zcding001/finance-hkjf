package com.hongkun.finance.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.model.RegUserCommunity;
import com.hongkun.finance.user.model.vo.RegUserCommunityVO;
import com.hongkun.finance.user.service.RegUserCommunityService;
import com.hongkun.finance.user.service.RegUserService;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

import static com.hongkun.finance.user.constants.UserConstants.USER_TYPE_TENEMENT;
import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * @Description   : 小区地址Controller
 * @Project       : hk-management-services
 * @Program Name  : com.hongkun.finance.web.controller.CommunityController.java
 * @Author        : xuhuiliu@hongkun.com.cn 劉旭輝
 */
@Controller
@RequestMapping("/communityController")
public class CommunityController {
	@Reference
	private RegUserCommunityService regUserCommunityService;

	@Reference
	private RegUserService regUserService;


	/**
	 * 查询物业账户对应的所有的小区
	 * @param regUserId
	 * @return
	 */
	@RequestMapping("/findCommunityDicDataList")
	@ResponseBody
	public List<Map<String,Object>> findCommunityDicDataList(Integer regUserId){
		return regUserCommunityService.findCommunityDicDataList(regUserId);
	}

	/**
	 * 查询物业账户的信息-带分页
	 * @return
	 */
	@RequestMapping("/communityList")
	@ResponseBody
	public ResponseEntity findUserTypeTenement(RegUserCommunityVO communityVO, Pager pager){
		return new ResponseEntity(SUCCESS,regUserCommunityService.findCommunityList(communityVO, pager));
	}

	/**
	 * 查询物业账户的信息-不带分页
	 * @return
	 */
	@RequestMapping("/findUserTypeTenementNoPage")
	@ResponseBody
	public ResponseEntity findUserTypeTenementNoPage(){
		return new ResponseEntity(SUCCESS,regUserCommunityService.findUserTypeTenementNoPage());
	}

	/**
	 * 查询所有未被物业绑定的小区
	 * @return
	 */
	@RequestMapping("/findCommunityAvailable")
	@ResponseBody
	public ResponseEntity findCommunityAvailable(){
		return new ResponseEntity(SUCCESS,regUserCommunityService.findCommunityAvailable());
	}

	/**
	 * 查询物业下的小区
	 * @return
	 */
	@RequestMapping("/findTenementsCommunity")
	@ResponseBody
	public ResponseEntity findTenementsCommunity(@RequestParam("id")Integer id){
		return new ResponseEntity(SUCCESS,regUserCommunityService.findTenementsCommunity(id));
	}



	/**
	 * 查询所有的小区名称
	 * @return
	 */
	@RequestMapping("/findUserTypeTenement")
	@ResponseBody
	public ResponseEntity findUserTypeTenement(RegUser regUser, Pager pager){
		//只查物业账户
		regUser.setType(USER_TYPE_TENEMENT);
		//指定查询列
		regUser.setQueryColumnId("nameTelType");
		return new ResponseEntity(SUCCESS,regUserService.findRegUserList(regUser, pager));
	}

    /**
     * 添加小区或者门店
     * @return
     */
    @RequestMapping("/saveCommunity")
    @ResponseBody
    public ResponseEntity deleteCommunity(RegUserCommunity unSaveCommunity){
        if (regUserCommunityService.insertRegUserCommunity(unSaveCommunity)>0) {
            return ResponseEntity.SUCCESS;
        }
        return ResponseEntity.ERROR;
    }

	/**
	 * 删除小区及下面的子小区或者门店
	 * @return
	 */
	@RequestMapping("/delectCommunityOnCascade")
	@ResponseBody
	public ResponseEntity deleteCommunity(@RequestParam("id")Integer id){
		return new ResponseEntity(SUCCESS,regUserCommunityService.deleteCommunityOnCascade(id));
	}

	/**
	 * 给指定的物业绑定小区
	 * @return
	 */
	@RequestMapping("/bindCommunityToTenement")
	@ResponseBody
	public ResponseEntity bindCommunityToTenement(RegUserCommunityVO communityVO){
		return regUserCommunityService.bindCommunityToTenement(communityVO);
	}


}
