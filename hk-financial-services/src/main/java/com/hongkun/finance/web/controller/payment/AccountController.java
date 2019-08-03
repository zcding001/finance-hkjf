package com.hongkun.finance.web.controller.payment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.payment.service.FinAccountService;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;
/**
 * @Description   : 账户管理
 * @Project       : hk-financial-services
 * @Program Name  : com.hongkun.finance.web.controller.payment.AccountController.java
 * @Author        : zhichaoding@hongkun.com zc.ding
 */
@Controller
@RequestMapping("accountController/")
public class AccountController {

	private final Logger logger = LoggerFactory.getLogger(AccountController.class);
	@Reference
	private FinAccountService finAccountService;
	/**
	 *  @Description    : 查询用户总资产
	 *  @Method_Name    : findAccountTotal
	 *  @param userId
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年9月18日 下午3:07:14 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	@RequestMapping("findAccountTotal")
	@ResponseBody
	public ResponseEntity<?> findAccountTotal(Integer regUserId){
		return new ResponseEntity<>(Constants.SUCCESS, this.finAccountService.findByRegUserId(regUserId));
	}
}
