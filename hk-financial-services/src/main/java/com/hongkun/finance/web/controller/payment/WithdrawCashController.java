package com.hongkun.finance.web.controller.payment;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.payment.facade.FinPaymentFacade;
import com.hongkun.finance.payment.service.FinConsumptionService;
import com.hongkun.finance.sms.constants.SmsConstants;
import com.hongkun.finance.sms.constants.SmsMsgTemplate;
import com.hongkun.finance.sms.model.SmsWebMsg;
import com.hongkun.finance.sms.utils.SmsSendUtil;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.service.RegUserDetailService;
import com.hongkun.finance.user.service.RegUserService;
import com.hongkun.finance.user.support.security.OperationTypeEnum;
import com.hongkun.finance.user.support.security.component.annotation.AskForPermission;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.annotation.Token;
import com.yirun.framework.core.annotation.Token.Ope;
import com.yirun.framework.core.enums.PlatformSourceEnums;
import com.yirun.framework.core.enums.SystemTypeEnums;
import com.yirun.framework.core.model.ResponseEntity;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.yirun.framework.core.commons.Constants.ERROR;
import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * @Description : 提现业务接口
 * @Project : hk-financial-services
 * @Program Name : com.hongkun.finance.web.controller.WithdrawCashController.java
 * @Author : yanbinghuang
 */
@Controller
@RequestMapping("/withdrawCashController/")
public class WithdrawCashController {
	private static final Logger logger = LoggerFactory.getLogger(WithdrawCashController.class);
	@Reference
	private FinPaymentFacade finPaymentFacade;
	@Reference
	private FinConsumptionService finConsumptionService;
	@Reference
	private RegUserDetailService regUserDetailService;
	@Reference
	private RegUserService regUserService;

	/**
	 * @Description :前台用户发起提现操作
	 * @Method_Name : clientWithDraw;
	 * @param request
	 * @param response
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年6月20日 下午4:01:48;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("clientWithDraw")
	@Token(operate = Ope.REFRESH)
	@AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_LOGIN)
	@ResponseBody
	public ResponseEntity<?> clientWithDraw(HttpServletRequest request, HttpServletResponse response) {
		RegUser regUser = BaseUtil
				.getRegUser(() -> this.regUserService.findRegUserById(BaseUtil.getLoginUser().getId()));
		String couponDetailId = request.getParameter("couponDetailId");//提现券Id
		// 校验参数有效性，及调用支付服务冻结金额，生成流水和资金划转
		ResponseEntity<?> checkResult = finPaymentFacade.clientWithDrawFacade(request.getParameter("transMoney"),
				regUser.getId(), PlatformSourceEnums.PC, StringUtils.isBlank(couponDetailId) ? null : Integer.parseInt(couponDetailId), SystemTypeEnums.HKJF);
		if (checkResult.getResStatus() == SUCCESS) {
			// 发送站内信
			SmsSendUtil.sendWebMsgToQueue(new SmsWebMsg((Integer) checkResult.getResMsg(),
					SmsMsgTemplate.MSG_PAYMENT_TX_APPLY_SUCCESS.getTitle(),
					StringUtils.isBlank(couponDetailId) ? SmsMsgTemplate.MSG_PAYMENT_TX_APPLY__NOTICKER_SUCCESS.getMsg(): SmsMsgTemplate.MSG_PAYMENT_TX_APPLY_SUCCESS.getMsg(), 
					SmsConstants.SMS_TYPE_NOTICE));
			return new ResponseEntity<>(SUCCESS, "提现申请成功");
		}
		return checkResult;
	}

	@RequestMapping("toWithDrawPage")
	@Token(operate = Ope.REFRESH)
	@AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_LOGIN)
	@ResponseBody
	public ResponseEntity<?> toWithDrawPage(HttpServletRequest request, HttpServletResponse response) {
		RegUser regUser = BaseUtil
				.getRegUser(() -> this.regUserService.findRegUserById(BaseUtil.getLoginUser().getId()));
		ResponseEntity<?> responseEntity = null;
		try {
			responseEntity = finPaymentFacade.getDataWithDrawPageForWeb(regUser.getId());
		} catch (Exception e) {
			logger.error("toWithDrawPage, 跳转到提现页面, userId: {}", regUser.getId(), e);
			responseEntity = new ResponseEntity<>(ERROR, "获取提现数据失败，请联系管理员");
		}
		return responseEntity;
	}
}
