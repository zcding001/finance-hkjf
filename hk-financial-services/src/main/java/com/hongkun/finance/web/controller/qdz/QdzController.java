package com.hongkun.finance.web.controller.qdz;

import static com.hongkun.finance.user.constants.UserConstants.USER_IDENTIFY_NO;
import static com.yirun.framework.core.commons.Constants.ERROR;

import java.math.BigDecimal;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.qdz.constant.QdzConstants;
import com.hongkun.finance.qdz.facade.QdzTaskJobFacade;
import com.hongkun.finance.qdz.facade.QdzTransferFacade;
import com.hongkun.finance.sms.constants.SmsConstants;
import com.hongkun.finance.sms.constants.SmsMsgTemplate;
import com.hongkun.finance.sms.model.SmsTelMsg;
import com.hongkun.finance.sms.model.SmsWebMsg;
import com.hongkun.finance.sms.utils.SmsSendUtil;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.service.RegUserService;
import com.hongkun.finance.user.support.security.OperationTypeEnum;
import com.hongkun.finance.user.support.security.component.annotation.AskForPermission;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.annotation.Token;
import com.yirun.framework.core.annotation.Token.Ope;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.CommonUtils;
import com.yirun.framework.core.utils.CompareUtil;

/**
 * @Description :钱袋子业务相关
 * @Project : hk-financial-services
 * @Program Name : com.hongkun.finance.web.controller.qdz.QdzController.java
 * @Author : yanbinghuang@hongkun.com
 */
@Controller
@RequestMapping("qdzController")
public class QdzController {
	private static final Logger logger = LoggerFactory.getLogger(QdzController.class);

	@Reference
	private QdzTransferFacade qdzTransferFacade;
	@Reference
	private QdzTaskJobFacade qdzTaskJobFacade;
	@Reference
	private RegUserService regUserService;

	/**
	 * @Description : 钱袋子转入
	 * @Method_Name : qdzTransferIn;
	 * @param request
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年7月14日 下午4:24:26;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("qdzTransferIn")
	@Token(operate = Ope.REFRESH)
	@AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
	@ResponseBody
	public ResponseEntity<?> qdzTransferIn(HttpServletRequest request) {
		// 验证用户有效性
		RegUser regUser = BaseUtil
				.getRegUser(() -> this.regUserService.findRegUserById(BaseUtil.getLoginUser().getId()));
		try {
			if (regUser == null) {
				logger.error("钱袋子转入, 转入失败: {}", "请您登录后再转入!");
				return new ResponseEntity<String>(Constants.ERROR, "请您登录后再转入!");
			}
			// 判断用户是否实名认证
			if (regUser.getIdentify() == USER_IDENTIFY_NO) {
				logger.error("钱袋子转入, 用户标识: {}, 转入失败: {}", regUser.getId(), "为保障您的资金安全,请进行实名认证之后再转入!");
				return new ResponseEntity<>(ERROR, "为保障您的资金安全,请进行实名认证之后再转入!");
			}
			// 验证转入金额的正确性
			String transMoney = request.getParameter("transMoney");// 转入金额
			String transferSource = request.getParameter("transferSource");// 转入来源
			if (StringUtils.isBlank(transMoney) || CompareUtil
					.lteZero(StringUtils.isBlank(transMoney) ? BigDecimal.ZERO : new BigDecimal(transMoney))) {
				logger.error("钱袋子转入, 用户标识: {}, 转入失败: {}", regUser.getId(), "转入金额不能为空！");
				return new ResponseEntity<>(ERROR, "转入金额不能为空！");
			}
			// 进行转入逻缉处理
			ResponseEntity<?> result = qdzTransferFacade.transferIn(regUser, new BigDecimal(transMoney), transferSource,
					2);
			if (result.getResStatus() == Constants.ERROR) {
				logger.error("钱袋子转入, 用户标识: {}, 转入失败: {}", regUser.getId(), result.getResMsg().toString());
				return result;
			}
			// 发送短信和站内信
			String msg = SmsMsgTemplate.MSG_QDZ_TRANSFER_IN_SUCCESS.getMsg();
			SmsSendUtil.sendSmsMsgToQueue(
					new SmsWebMsg(regUser.getId(), SmsMsgTemplate.MSG_QDZ_TRANSFER_IN_SUCCESS.getTitle(), msg,
							SmsConstants.SMS_TYPE_NOTICE, transMoney.split(",")),
					new SmsTelMsg(regUser.getId(), regUser.getLogin(), msg, SmsConstants.SMS_TYPE_NOTICE,
							transMoney.split(",")));
			return result;
		} catch (Exception e) {
			logger.error("钱袋子转入, 用户标识: {}, 转入失败: ,", regUser.getId(), e);
			return new ResponseEntity<>(ERROR, "钱袋子转入失败!");
		}

	}

	/**
	 * @Description :钱袋子转出
	 * @Method_Name : qdzTransferOut;
	 * @param request
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年7月17日 下午5:35:08;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("qdzTransferOut")
	@Token(operate = Ope.REFRESH)
	@AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
	@ResponseBody
	public ResponseEntity<?> qdzTransferOut(HttpServletRequest request) {
		// 校验用户是否登录
		RegUser regUser = BaseUtil
				.getRegUser(() -> this.regUserService.findRegUserById(BaseUtil.getLoginUser().getId()));
		if (regUser == null) {
			logger.error("钱袋子转出, 转出失败: {}", "请您登录后再转出!");
			return new ResponseEntity<>(ERROR, "请您登录后再转出!");
		}
		// 验证转出金额的正确性
		String transMoney = request.getParameter("transMoney");// 转出金额
		String transferSource = request.getParameter("transferSource");// 转出来源
		if (StringUtils.isBlank(transMoney) || CompareUtil
				.lteZero(StringUtils.isBlank(transMoney) ? BigDecimal.ZERO : new BigDecimal(transMoney))) {
			return new ResponseEntity<>(ERROR, "转出金额不能为空！");
		}
		// 进行转出逻缉处理
		ResponseEntity<?> result = qdzTransferFacade.transferOut(regUser, new BigDecimal(transMoney), transferSource,
				QdzConstants.QDZ_TURNINOUT_TYPE_COMMON);
		if (result.getResStatus() == Constants.ERROR) {
			return result;
		}
		// 发送短信和站内信
		// String msg = SmsMsgTemplate.MSG_QDZ_TRANSFER_OUT_SUCCESS.getMsg();
		// SmsSendUtil.sendSmsMsgToQueue(
		// new SmsWebMsg(regUser.getId(),
		// SmsMsgTemplate.MSG_QDZ_TRANSFER_OUT_SUCCESS.getTitle(), msg,
		// SmsConstants.SMS_TYPE_NOTICE, transMoney.split(",")),
		// new SmsTelMsg(regUser.getId(), regUser.getLogin(), msg,
		// SmsConstants.SMS_TYPE_NOTICE,
		// transMoney.split(",")));
		return result;
	}

	@RequestMapping("match")
	@AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
	@ResponseBody
	public ResponseEntity<?> match(HttpServletRequest request) {
		return qdzTaskJobFacade.creditorMatch(new Date());
	}

	@RequestMapping("qdzRateRecord")
	@AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
	@ResponseBody
	public ResponseEntity<?> qdzRateRecord(HttpServletRequest request) {
		return qdzTaskJobFacade.qdzRateRecord();
	}
}
