package com.hongkun.finance.web.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.user.model.RegUserAddress;
import com.hongkun.finance.user.service.RegUserAddressService;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;

/**
 * @Description   : 用户地址信息管理
 * @Project       : management-financial-services
 * @Program Name  : com.hongkun.finance.web.controller.user.UserAddressController.java
 * @Author        : zhichaoding@hongkun.com zc.ding
 */
@Controller
@RequestMapping("userAddressController/")
public class UserAddressController {

	@Reference
	private RegUserAddressService regUserAddressService;
	
	/**
	 *  @Description    : 加载用户快递地址
	 *  @Method_Name    : addressList
	 *  @param pager
	 *  @param userId
	 *  @return
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年9月22日 上午10:55:06 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	@RequestMapping("addressList")
	@ResponseBody
	public ResponseEntity<?> addressList(Pager pager, RegUserAddress regUserAddress){
		return new ResponseEntity<>(Constants.SUCCESS, this.regUserAddressService.findRegUserAddressList(regUserAddress, pager));
	}
}
