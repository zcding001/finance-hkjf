package com.hongkun.finance.api.controller.payment;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.payment.facade.FinPaymentFacade;
import com.hongkun.finance.payment.factory.ValidateParamsFactory;
import com.hongkun.finance.payment.model.FinBankCard;
import com.hongkun.finance.sms.constants.SmsConstants;
import com.hongkun.finance.sms.constants.SmsMsgTemplate;
import com.hongkun.finance.sms.model.SmsWebMsg;
import com.hongkun.finance.sms.utils.SmsSendUtil;
import com.hongkun.finance.user.constants.UserConstants;
import com.hongkun.finance.user.model.RegCompanyInfo;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.model.RegUserDetail;
import com.hongkun.finance.user.service.RegCompanyInfoService;
import com.hongkun.finance.user.service.RegUserDetailService;
import com.hongkun.finance.user.service.RegUserService;
import com.hongkun.finance.user.utils.BaseUtil;
import com.hongkun.finance.vas.constants.VasCouponConstants;
import com.hongkun.finance.vas.model.vo.VasCouponDetailVO;
import com.hongkun.finance.vas.service.VasCouponDetailService;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.enums.PlatformSourceEnums;
import com.yirun.framework.core.enums.SystemTypeEnums;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.AppResultUtil;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * @Description : 提现业务相关接口
 * @Project : finance
 * @Program Name :
 *          com.hongkun.finance.api.controller.payment.WithdrawController.java
 * @Author : maruili on 2018/3/13 17:47
 */
@Controller
@RequestMapping("/withdrawController")
public class WithdrawController {
	private static final Logger logger = LoggerFactory.getLogger(WithdrawController.class);
	@Reference
	private FinPaymentFacade finPaymentFacade;
	@Reference
	private VasCouponDetailService vasCouponDetailService;
	@Reference
	private RegUserService regUserService;
	@Reference
	private RegUserDetailService regUserDetailService;
	@Reference
	private RegCompanyInfoService regCompanyInfoService;

	/**
	 * 跳转提现页面，获取页面所需数据
	 * 
	 * @return
	 */
	@RequestMapping("toWithdraw")
	@ResponseBody
	public Map<String, Object> toWithdraw(HttpServletRequest request) {
		RegUser regUser = BaseUtil.getRegUser(BaseUtil.getLoginUserId(),
				() -> this.regUserService.findRegUserById(BaseUtil.getLoginUserId()));
		RegUserDetail regUserDetail = regUserDetailService.findRegUserDetailByRegUserId(regUser.getId());
		try {
			String userName = regUserDetail.getRealName();
			if (regUser.getType() != UserConstants.USER_TYPE_GENERAL) {
				RegCompanyInfo regCompanyInfo = regCompanyInfoService.findRegCompanyInfoByRegUserId(regUser.getId());
				if (regCompanyInfo == null || StringUtils.isBlank(regCompanyInfo.getEnterpriseName())) {
					return AppResultUtil.errorOfMsg("请维护企业名称");
				} else {
					userName = regCompanyInfo.getEnterpriseName().length() <= 3 ? regCompanyInfo.getEnterpriseName()
							: ("***" + regCompanyInfo.getEnterpriseName().substring(3));
				}
			}
			ResponseEntity<?> responseEntity = finPaymentFacade.getDataWithDrawPageForApp(regUser.getId());
			FinBankCard bankCard = responseEntity.getParams().get("bankCard") != null
					? (FinBankCard) responseEntity.getParams().get("bankCard") : null;
			if (bankCard == null) {
				return AppResultUtil.errorOfMsg("没有查询到可提现的银行卡信息");
			}
			Integer couponDetailId = (Integer) responseEntity.getParams().get("couponDetailId");
			return AppResultUtil.successOfMsg("查询成功").addResParameter("id", bankCard.getId())
					.addResParameter("bankName", bankCard.getBankName())
					.addResParameter("useableMoney", responseEntity.getParams().get("useableMoney"))
					.addResParameter("userName", userName)
					.addResParameter("bankIconAddress",
							request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
									+ request.getContextPath() + "/" + "src/img/account/" + bankCard.getBankCode()
									+ ".png")
					.addResParameter("bankCard", bankCard.getBankCard())
					.addResParameter("couponDetailId", couponDetailId == null ? "" : String.valueOf(couponDetailId));
		} catch (Exception e) {
			logger.error("获取用户绑卡数据和提现券数据, 用户ID: {}, 失败: ", regUser.getId(), e);
			return AppResultUtil.errorOfMsg("系统繁忙请稍候再试");
		}
	}

	/**
	 * 提现申请
	 * 
	 * @param transMoney
	 * @return
	 */
	@RequestMapping("applyWithdraw")
	@ResponseBody
	public Map<String, Object> applyWithdraw(HttpServletRequest request,
			@RequestParam(value = "couponDetailId", required = false) Integer couponDetailId, String transMoney,
			Integer platformSource, String sign_type, String sign, Integer systemCode) {
		RegUser regUser = BaseUtil.getRegUser(BaseUtil.getLoginUserId(),
				() -> this.regUserService.findRegUserById(BaseUtil.getLoginUserId()));
		try {
			// 参数及权限验证
			if (regUser.getIdentify() == null || regUser.getIdentify() == UserConstants.USER_IDENTIFY_NO) {
				return AppResultUtil.errorOfMsg("请进行实名认证之后再提现!");
			}
			if (StringUtils.isBlank(transMoney) || new BigDecimal(transMoney).compareTo(BigDecimal.ZERO) <= 0) {
				return AppResultUtil.errorOfMsg("提现金额不能为空或小于等于零!");
			}
			// 提现申请验签
			boolean result = ValidateParamsFactory.checkWithDrawSign(new BigDecimal(transMoney), platformSource,
					sign_type, sign, request.getParameter(Constants.SESSION_ID_KEY), couponDetailId, systemCode);
			if (result == false) {
				logger.error("跳转到收银台页面, 签名验签失败！");
				return AppResultUtil.errorOfMsg("提现申请失败");
			}
			// 发起提现操作
			ResponseEntity<?> checkResult = finPaymentFacade.clientWithDrawFacade(transMoney, regUser.getId(),
					PlatformSourceEnums.typeByValue(platformSource), couponDetailId,
					SystemTypeEnums.getSystemTypeEnumsByValue(systemCode));
			if (checkResult.getResStatus() == SUCCESS) {
				// 发送站内信
				sendMsg(regUser.getId(), transMoney, SmsMsgTemplate.MSG_PAYMENT_TX_APPLY_SUCCESS.getTitle(),
						SmsMsgTemplate.MSG_PAYMENT_TX_APPLY_SUCCESS.getMsg());
				return AppResultUtil.successOfMsg("提现申请成功");
			}
			return AppResultUtil.errorOfMsg(checkResult.getResMsg());
		} catch (Exception e) {
			logger.error("提现申请失败, 用户Id: {}, 异常信息: ", regUser.getId(), e);
			return AppResultUtil.errorOfMsg("提现申请失败");
		}
	}

	/**
	 * @Description :提现申请发送站内信
	 * @Method_Name : sendMsg;
	 * @param regUserId
	 *            用户Id
	 * @param transMoney
	 *            交易金额
	 * @param title
	 *            标题
	 * @param content
	 *            标题内容
	 * @return : void;
	 * @Creation Date : 2018年5月29日 下午4:49:53;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	private void sendMsg(Integer regUserId, String transMoney, String title, String content) {
		logger.error("sendMsg, 提现申请操作发送站内信, 用户regUserId: {}, transMoney: {},", regUserId, transMoney);
		try {
			SmsSendUtil.sendWebMsgToQueue(new SmsWebMsg(regUserId, title, content, SmsConstants.SMS_TYPE_NOTICE,
					new String[] { String.valueOf(transMoney) }));
		} catch (Exception e) {
			logger.error("提现申请操作发送站内信, 用户id: {}, transMoney: {}, 失败:", regUserId, transMoney, e);
		}
	}

	/**
	 * @Description : 查询未使用的提现券
	 * @Method_Name : findUseableCouponDetail;
	 * @return
	 * @return : Map<String,Object>;
	 * @Creation Date : 2018年5月16日 下午5:22:43;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("findUseableCouponDetail")
	@ResponseBody
	public Map<String, Object> findUseableCouponDetail() {
		RegUser regUser = BaseUtil.getRegUser(BaseUtil.getLoginUserId(),
				() -> this.regUserService.findRegUserById(BaseUtil.getLoginUserId()));
		List<VasCouponDetailVO> couponDetailList = vasCouponDetailService.getUserWithdrawUsableCoupon(regUser.getId());
		return AppResultUtil.successOfListInProperties(couponDetailList, "查询成功", "id"/** 提现券ID **/
				, "endTime"/** 结束时间 **/
		);
	}
}
