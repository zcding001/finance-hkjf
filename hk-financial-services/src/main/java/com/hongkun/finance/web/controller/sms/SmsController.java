package com.hongkun.finance.web.controller.sms;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.sms.model.SmsWebMsg;
import com.hongkun.finance.sms.service.SmsWebMsgDetailService;
import com.hongkun.finance.sms.service.SmsWebMsgService;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;

/**
 * @Description   : 短信、站内信控制层
 * @Project       : hk-financial-services
 * @Program Name  : com.hongkun.finance.web.controller.sms.SmsController.java
 * @Author        : zhichaoding@hongkun.com zc.ding
 */
@Controller
@RequestMapping("/smsController/")
public class SmsController {
	
	@Reference
	private SmsWebMsgService smsWebMsgService;
	@Reference
	private SmsWebMsgDetailService smsWebMsgDetailService;

	/**
	 *  @Description    : 加载所有未读的站内信
	 *  @Method_Name    : getUnReadWebMsg
	 *  @return
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年11月27日 下午5:07:55 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	@RequestMapping("getUnreadWebMsg")
	@ResponseBody
	public ResponseEntity<?> getUnreadWebMsg(HttpServletRequest request) {
		return ResponseEntity.SUCCESS.addParam("total",
				this.smsWebMsgService.findUnreadWebMsg(BaseUtil.getLoginUser().getId()));
	}
	
	/**
	 *  @Description    : 条件检索站内信信息
	 *  @Method_Name    : webMsgList
	 *  @param pager
	 *  @param smsWebMsg
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年11月28日 上午11:39:53 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	@RequestMapping("webMsgList")
	@ResponseBody
	public ResponseEntity<?> webMsgList(Pager pager, SmsWebMsg smsWebMsg) {
		smsWebMsg.setRegUserId(BaseUtil.getLoginUser().getId());
		return new ResponseEntity<>(Constants.SUCCESS, this.smsWebMsgService.findSmsWebMsgList(smsWebMsg, pager));
	}
	
	/**
	 * 
	 *  @Description    : 更新站内信状态
	 *  @Method_Name    : updateWebMsgState
	 *  @param ids
	 *  @param state
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年12月11日 下午3:37:05 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	@RequestMapping("updateWebMsgState")
	@ResponseBody
	public ResponseEntity<?> updateWebMsgState(String[] ids, Integer state) {
		if(ids == null || ids.length <= 0) {
			return new ResponseEntity<>(Constants.ERROR, "未找到需要更新的数据，请确认!");
		}
		if(Arrays.asList(ids).stream().filter(e -> !e.matches("[0-9]+")).findAny().isPresent()) {
			return  new ResponseEntity<>(Constants.ERROR, "含有非法数据!");
		}
		return this.smsWebMsgService.updateSmsWebMsg(ids, state, BaseUtil.getLoginUser().getId()) > 0
				? ResponseEntity.SUCCESS
				: ResponseEntity.ERROR;
	}
	
	/**
	 *  @Description    : 显示站内信详情
	 *  @Method_Name    : webMsgDetail
	 *  @param id
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年12月11日 下午4:00:48 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	@RequestMapping("webMsgDetail")
	@ResponseBody
	public ResponseEntity<?> webMsgDetail(@RequestParam("id") Integer id) {
		return new ResponseEntity<>(Constants.SUCCESS, this.smsWebMsgDetailService.findSmsWebMsgDetailById(id));
	}
}
