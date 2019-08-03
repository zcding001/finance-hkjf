package com.hongkun.finance.web.controller.monitor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.monitor.model.MonitorActionLog;
import com.hongkun.finance.monitor.service.MonitorActionLogService;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;

/**
 * @Description   : 管理员操作日志监控
 * @Project       : hk-management-services
 * @Program Name  : com.hongkun.finance.web.controller.monitor.ActionLogController.java
 * @Author        : zhichaoding@hongkun.com zc.ding
 */
@Controller
@RequestMapping("/actionLogController/")
public class ActionLogController {

	@Reference
	private MonitorActionLogService monitorActionLogService;
	
	/**
	 *  @Description    : 查询管理员操作日志
	 *  @Method_Name    : actionLogList
	 *  @param pager
	 *  @param monitorActionLog
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年12月6日 上午10:43:42 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	@RequestMapping("actionLogList")
	@ResponseBody
	public ResponseEntity<?> actionLogList(Pager pager, MonitorActionLog monitorActionLog){
		return new ResponseEntity<>(Constants.SUCCESS, this.monitorActionLogService.findMonitorActionLogList(monitorActionLog, pager));
	}
}
