package com.hongkun.finance.web.controller;

import javax.servlet.http.HttpServletRequest;

import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.vas.facade.VasRedpacketFacade;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.user.facade.UserFacade;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;

/**
 * @Description   : 业务数据导入功能模块
 * @Project       : management-financial-services
 * @Program Name  : com.hongkun.finance.web.controller.ImportController.java
 * @Author        : zhichaoding@hongkun.com zc.ding
 */

@Controller
@RequestMapping("importController/")
public class ImportController {
	@Reference
	private UserFacade userFacade;
	@Reference
	private VasRedpacketFacade vasRedpacketFacade;

	/**
	 *  @Description    : 导入用户信息
	 *  @Method_Name    : importUsers
	 *  @param request
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年10月17日 下午6:08:45 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	@RequestMapping("importUsers")
	@ResponseBody
	public ResponseEntity<?> importUsers(HttpServletRequest request, @RequestParam("uploadFile") MultipartFile multipartFile){
		ResponseEntity<?> result = BaseUtil.saveFile(request, multipartFile);
		if(result.getResStatus() == Constants.SUCCESS){
			return this.userFacade.importUsers((String)result.getResMsg());
		}
		return result;
	}

	/**
	 *  @Description    ：导入红包列表
	 *  @Method_Name    ：importPackets
	 *  @param request
	 *  @param multipartFile
	 *  @return com.yirun.framework.core.model.ResponseEntity<?>
	 *  @Creation Date  ：2018/9/13
	 *  @Author         ：pengwu@hongkun.com.cn
	 */
	@RequestMapping("importPackets")
	@ResponseBody
	public ResponseEntity<?> importPackets(HttpServletRequest request, @RequestParam("uploadFile") MultipartFile multipartFile){
		Integer currentUserId = BaseUtil.getLoginUser().getId();
		ResponseEntity<?> result = BaseUtil.saveFile(request, multipartFile);
		if (result.getResStatus() == Constants.SUCCESS){
			return this.vasRedpacketFacade.importRedPackets((String)result.getResMsg(),currentUserId);
		}
		return result;
	}
}
