package com.hongkun.finance.web.controller.payment;

import static com.yirun.framework.core.commons.Constants.ERROR;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.esotericsoftware.minlog.Log;
import com.hongkun.finance.loan.facade.RepayFacade;
import com.hongkun.finance.loan.service.BidRepayPlanService;
import static com.hongkun.finance.payment.constant.PaymentConstants.*;
import com.hongkun.finance.payment.enums.LianLianPlatformPayStateEnum;
import com.hongkun.finance.payment.enums.PayChannelEnum;
import com.hongkun.finance.payment.facade.FinPayMentNoticeFacade;
import com.hongkun.finance.payment.llpayvo.RetBean;
import com.hongkun.finance.payment.service.FinAccountService;
import com.hongkun.finance.payment.service.FinBankCardService;
import com.hongkun.finance.payment.service.FinConsumptionService;
import com.hongkun.finance.payment.service.FinFundtransferService;
import com.hongkun.finance.payment.service.FinPayConfigService;
import com.hongkun.finance.payment.service.FinPaymentRecordService;
import com.hongkun.finance.payment.service.FinTradeFlowService;
import com.hongkun.finance.payment.util.PaymentUtil;
import com.hongkun.finance.point.service.PointCommonService;
import com.hongkun.finance.point.service.PointRecordService;
import com.hongkun.finance.property.facade.PropertyPaymentFacade;
import com.hongkun.finance.property.service.ProPayRecordService;
import com.hongkun.finance.user.service.RegUserDetailService;
import com.hongkun.finance.user.service.RegUserService;
import com.hongkun.finance.user.support.security.OperationTypeEnum;
import com.hongkun.finance.user.support.security.component.annotation.AskForPermission;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.enums.SystemTypeEnums;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.PropertiesHolder;
import com.yirun.framework.core.utils.response.ResponseUtils;

/**
 * @Description : 金融支付回调接口
 * @Project : hk-financial-services
 * @Program Name : com.hongkun.finance.web.controller.FinPaymentController.java
 * @Author : yanbinghuang@hongkun.com
 */
@Controller
@RequestMapping("finPaymenCallBackController")
public class FinPaymentCallBackController {
	private static final Logger logger = LoggerFactory.getLogger(FinPaymentCallBackController.class);
	public static String ENCONDING_UTF = "UTF-8";
	@Reference(timeout = 5000)
	private FinConsumptionService finConsumptionService;
	@Reference(timeout = 5000)
	private FinPaymentRecordService finPaymentRecordService;
	@Reference(timeout = 5000)
	private FinTradeFlowService finTradeFlowService;
	@Reference(timeout = 5000)
	private FinFundtransferService finFundtransferService;
	@Reference(timeout = 5000)
	private FinBankCardService finBankCardService;
	@Reference(timeout = 5000)
	private RegUserService regUserService;
	@Reference(timeout = 5000)
	private FinPayConfigService finPayConfigService;
	@Reference
	private BidRepayPlanService bidRepayPlanService;
	@Reference(timeout = 5000)
	private FinAccountService finAccountService;
	@Reference(timeout = 5000)
	private FinPayMentNoticeFacade finPayMentNoticeFacade;
	@Reference(timeout = 5000)
	private RegUserDetailService regUserDetailService;
	@Reference(timeout = 5000)
	private RepayFacade reapyFacade;
	@Reference(timeout = 5000)
	private ProPayRecordService proPayRecordService;
	@Reference(timeout = 5000)
	private PointCommonService pointCommonService;
	@Reference(timeout = 5000)
	private PointRecordService pointRecordService;
	@Reference(timeout = 5000)
	private PropertyPaymentFacade propertyPaymentFacade;

	/***
	 * @Description : 连连提现成功后，异步通知
	 * @Method_Name : lianlianAgentNotifyUrl&#13;
	 * @param response
	 * @param request
	 * @return : void&#13;
	 * @Creation Date : 2017年6月2日 下午3:14:20 &#13;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵&#13;
	 */
	@RequestMapping("lianlianAsyncNotifyUrl")
	@AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
	public void lianlianAsyncNotifyUrl(HttpServletResponse response, HttpServletRequest request) {
		try {
			String reqStr = "";
			// 测试环境测试用
			if ("false".equals(PropertiesHolder.getProperty("isonline"))) {
				reqStr = request.getParameter("reqStr");
			} else {
				// 通过request请求获取返回参数
				reqStr = PaymentUtil.readReqStr(request);
				logger.info("连连提现异步通知, 返回的参数data_content: {}", reqStr);
				// 校验对象数据是否为空
				if (PaymentUtil.isNull(reqStr) || reqStr.length() == 0) {
					logger.error("连连提现异步通知, 返回的参数data_content为空!");
				}
			}
			// 连连提现异步通知业务处理
			ResponseEntity<?> resResult = finPayMentNoticeFacade.lianlianAsyncNotice(reqStr);
			if (resResult.getResStatus() == Constants.SUCCESS) {
				ResponseUtils.responseJson(response, JSON.toJSONString((RetBean) resResult.getParams().get("retBean")));
			} else {
				RetBean retBean = new RetBean();
				retBean.setRet_code(LianLianPlatformPayStateEnum.PLATFAIL.getThirdPaymentState());
				retBean.setRet_msg((String) resResult.getResMsg());
				ResponseUtils.responseJson(response, JSON.toJSONString(retBean));
				logger.info("连连提现异步通知,  提现异步通知处理状态: {}, 异步通知处理结果描述: {}", resResult.getResStatus(),
						resResult.getResMsg().toString());
			}
		} catch (Exception e) {
			logger.error("连连提现异步通知, 提现异步通知处理失败:", e);
			RetBean retBean = new RetBean();
			retBean.setRet_code(LianLianPlatformPayStateEnum.PLATFAIL.getThirdPaymentState());
			retBean.setRet_msg("交易失败!");
			ResponseUtils.responseJson(response, JSON.toJSONString(retBean));
		}
		return;
	}
	
	/**
	 * @Description :宝付提现异步通知
	 * @Method_Name : baofuAsyncNotifyUrl;
	 * @param response
	 * @param request
	 * @return : void;
	 * @Creation Date : 2018年5月23日 上午9:42:03;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("baofuAsyncNotifyUrl")
	@AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
	public void baofuAsyncNotifyUrl(HttpServletResponse response, HttpServletRequest request) {
		try {
			// 获取回调参数
			String data_content = request.getParameter("data_content");
			logger.info("宝付提现异步通知, 返回的参数data_content: {}", data_content);
			// 校验对象数据是否为空
			if (PaymentUtil.isNull(data_content) || data_content.length() == 0) {
				logger.error("宝付提现异步通知, 返回的参数data_content为空!");
				ResponseUtils.responseJson(response, BAOFU_FAILURE);
			}
			// 宝付提现异步通知业务处理
			ResponseEntity<?> resResult = finPayMentNoticeFacade.baofuAsyncNotice(data_content, SystemTypeEnums.HKJF,
					PayChannelEnum.BaoFu);
			// 响应给商户处理结果
			ResponseUtils.responseJson(response, resResult.getParams().get("ret_msg").toString());
		} catch (Exception e) {
			logger.error("宝付提现异步通知,  提现异步通知失败: ", e);
			ResponseUtils.responseJson(response, BAOFU_FAILURE);
		}
		return;
	}

	/***
	 * 
	 * @Description : 连连签约同步返回方法
	 * @Method_Name : lianlianSignReturnUrl;
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 * @return : ModelAndView;
	 * @Creation Date : 2017年6月7日 下午1:57:04;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public ModelAndView lianlianSignReturnUrl(HttpServletResponse response, HttpServletRequest request)
			throws Exception {
		ModelAndView modelAndView = new ModelAndView("yrtz/tool/returnPageQy");
		JSONObject lianItemObj = new JSONObject();
		lianItemObj.put("oid_partner", request.getParameter("oid_partner")); // 商户编号
		lianItemObj.put("sign_type", request.getParameter("sign_type"));// 签名方式
		lianItemObj.put("sign", request.getParameter("sign"));// 签名
		lianItemObj.put("result_sign", request.getParameter("result_sign"));// 签约结果
		lianItemObj.put("no_agree", request.getParameter("no_agree"));// 签约协议号
		lianItemObj.put("user_id", request.getParameter("user_id"));// 商户用户唯一编号
		lianItemObj.put("ret_code", request.getParameter("ret_code"));// 交易结果代码
		lianItemObj.put("ret_msg", request.getParameter("ret_msg"));// 交易结果描述
		String bankCardId = request.getParameter("bankCardId");
		ResponseEntity<?> result = finPayMentNoticeFacade.lianlianSignNotice(lianItemObj.toString(), bankCardId);
		modelAndView.addObject("state", "success");
		if (result.getResStatus() == Constants.ERROR) {
			modelAndView.addObject("state", "error");
		}
		modelAndView.addObject("description", "连连银行卡签约同步回调通知返回页面");
		modelAndView.addObject("message", result.getResMsg());
		modelAndView.addObject("returnUrl", "accPandectController.do?method=accPandectlist&topage=toBankCard()");
		return modelAndView;
	}

	/**
	 * 
	 * @Description : 还款代扣异步通知处理
	 * @Method_Name : lianlianRepayNotice
	 * @param response
	 * @param request
	 * @return : void
	 * @Creation Date : 2017年6月29日 上午11:03:47
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	public void lianlianRepayNotice(HttpServletResponse response, HttpServletRequest request) {

		// 还款代扣异步通知逻辑处理
		response.setCharacterEncoding(ENCONDING_UTF);
		String reqStr = "";
		try {
			reqStr = PaymentUtil.readReqStr(request);
			logger.info("连连还款代扣异步数据：{" + reqStr + "}");
			if (StringUtils.isBlank(reqStr)) {
				return;
			}
			// 代扣
			ResponseEntity<?> entity = reapyFacade.lianlianRepayNotice(reqStr);
			if (BaseUtil.error(entity)) {
				logger.info("回调参数{" + reqStr + "},连连还款代扣异步:{" + BaseUtil.errorMsg(entity) + "}");
			}
		} catch (Exception e) {
			logger.error("支付异步通知失败！{" + reqStr + "}");
		}
	}

	/**
	 * @Description : 宝付认证充值同步回调函数
	 * @Method_Name : baofuRechargePageNotice;
	 * @param response
	 * @param request
	 * @return
	 * @return : ModelAndView;
	 * @Creation Date : 2018年1月24日 上午11:17:57;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/baofuRechargePageNotice")
	@AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
	@ResponseBody
	public ModelAndView baofuRechargePageNotice(HttpServletResponse response, HttpServletRequest request) {
		logger.info("方法: baofuRechargePageNotice, 宝付认证充值同步回调操作, 入参: data_content: {}",
				request.getParameter("data_content"));
		ModelAndView modelAndView = new ModelAndView("success");
		try {
			// 获取回调参数
			String data_content = request.getParameter("data_content");
			logger.info("宝付认证充值同步回调操作, 同步通知内容data_content: {}", data_content);
			// 校验对象数据是否为空
			if (PaymentUtil.isNull(data_content) || data_content.length() == 0) {
				logger.error("宝付认证充值同步回调操作, 交易失败, 同步通知数据为空！");
				modelAndView.addObject("message", "支付失败！");
				modelAndView.addObject("state", Constants.ERROR);
				return modelAndView;
			}
			// 处理宝付认证充值,同步通知的操作
			ResponseEntity<?> responseEntity = finPayMentNoticeFacade.baofuRechargeSyncNotifyUrl(data_content);
			modelAndView.addObject("message", responseEntity.getResMsg().toString());
			modelAndView.addObject("state", responseEntity.getResStatus());
		} catch (Exception e) {
			logger.error("宝付认证充值同步回调操作, 认证充值同步回调处理失败: ", e);
			modelAndView.addObject("message", "支付失败！");
			modelAndView.addObject("state", Constants.ERROR);
		}
		return modelAndView;
	}

	/**
	 * @Description : 连连充值同步回调函数
	 * @Method_Name : lianlianRechargeSynchronousNotifyUrl
	 * @param response
	 * @param request
	 * @return : void
	 * @Creation Date : 2017年7月5日 下午4:25:47
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	@RequestMapping("/lianlianRechargePageNotice")
	@AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
	@ResponseBody
	public ModelAndView lianlianRechargePageNotice(HttpServletResponse response, HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView("success");
		try {
			JSONObject lianItemObj = new JSONObject();
			lianItemObj.put("oid_partner", request.getParameter("oid_partner"));// 商户编号
			lianItemObj.put("sign_type", request.getParameter("sign_type")); // 签名方式
			lianItemObj.put("sign", request.getParameter("sign"));// 签名
			lianItemObj.put("dt_order", request.getParameter("dt_order"));// 商户订单时间
			lianItemObj.put("no_order", request.getParameter("no_order"));// 商户唯一订单号
			lianItemObj.put("oid_paybill", request.getParameter("oid_paybill"));// 联联支付单号
			lianItemObj.put("money_order", request.getParameter("money_order"));// 订单金额
			lianItemObj.put("result_pay", request.getParameter("result_pay"));// 支付结果
			lianItemObj.put("settle_date", request.getParameter("settle_date"));// 清算日期
			lianItemObj.put("info_order", request.getParameter("info_order"));// 订单描述
			lianItemObj.put("pay_type", request.getParameter("pay_type"));// 支付方式
			lianItemObj.put("bank_code", request.getParameter("bank_code"));// 银行编号
			lianItemObj.put("no_agree", request.getParameter("no_agree"));// 签约协议号
			String reqStr = lianItemObj.toString();
			logger.info("连连充值同步回调操作, 同步通知内容: {}", reqStr);
			if (StringUtils.isBlank(reqStr)) {
				modelAndView.addObject("message", "交易失败，同步通知数据为空！");
				modelAndView.addObject("state", Constants.ERROR);
				return modelAndView;
			}
			ResponseEntity<?> responseEntity = finPayMentNoticeFacade.lianlianRechargeSyncNotifyUrl(reqStr);
			modelAndView.addObject("message", responseEntity.getResMsg().toString());
			modelAndView.addObject("state", responseEntity.getResStatus());
		} catch (Exception e) {
			logger.error("连连充值同步回调操作, 处理同步回调失败: ", e);
			modelAndView.addObject("message", "支付失败！");
			modelAndView.addObject("state", Constants.ERROR);
		}
		return modelAndView;
	}

	/***
	 * @Description : 宝付认证充值异步通知
	 * @Method_Name : baofuRechargeAsyncNotifyUrl;
	 * @param response
	 * @param request
	 * @return : void;
	 * @Creation Date : 2018年1月24日 下午3:07:58;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("baofuRechargeAsyncNotifyUrl")
	@AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
	public void baofuRechargeAsyncNotifyUrl(HttpServletResponse response, HttpServletRequest request) {
		logger.info("方法: baofuRechargeAsyncNotifyUrl, 宝付认证充值异步回调操作, 入参: data_content: {}",
				request.getParameter("data_content"));
		try {
			// 获取回调参数
			String data_content = request.getParameter("data_content");
			logger.info("宝付认证充值异步回调操作, 异步通知内容: {}", data_content);
			// 校验对象数据是否为空
			if (PaymentUtil.isNull(data_content) || data_content.length() == 0) {
				logger.error("宝付认证充值异步回调操作, 异步通知内容为空!");
				ResponseUtils.responseJson(response, BAOFU_FAILURE);
			}
			// 宝付认证充值异步回调通知处理
			ResponseEntity<?> responseEntity = finPayMentNoticeFacade.baofuRechargeAsyncNotifyUrl(data_content,
					INCOME_TYPE_COMMON);
			if (responseEntity.getResStatus() == ERROR) {
				logger.error("宝付认证充值异步回调操作, 异步回调处理失败: {}", responseEntity.getResMsg().toString());
				ResponseUtils.responseJson(response, BAOFU_FAILURE);
			} else {
				ResponseUtils.responseJson(response, BAOFU_SUCCESS);
			}
		} catch (Exception e) {
			logger.error("宝付认证充值异步回调操作, 异步通知处理失败: ", e);
			ResponseUtils.responseJson(response, BAOFU_FAILURE);
		}
	}
	
	/**
	 * 
	 * @Description : 连连充值异步回调函数
	 * @Method_Name : lianlianRechargeAsyncNotifyUrl
	 * @param response
	 * @param request
	 * @return : void
	 * @Creation Date : 2017年7月5日 下午4:25:47
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	@RequestMapping("lianlianRechargeAsyncNotifyUrl")
	@AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
	public void lianlianRechargeAsyncNotifyUrl(HttpServletResponse response, HttpServletRequest request) {
		RetBean retBean = new RetBean();// 通知响应对象
		try {
			response.setCharacterEncoding(ENCONDING_UTF);
			String reqStr = "";
			// 测试环境测试用
			if ("false".equals(PropertiesHolder.getProperty("isonline"))) {
				reqStr = request.getParameter("reqStr");
			} else {
				reqStr = PaymentUtil.readReqStr(request);
				logger.error("连连充值异步回调操作, 异步数据: reqStr: {}", reqStr);
				if (StringUtils.isBlank(reqStr)) {
					logger.error("连连充值异步回调操作, 异步回调充值处理失败: {}", "充值异步数据返回为空 !");
					retBean.setRet_code(LianLianPlatformPayStateEnum.PLATFAIL.getThirdPaymentState());
					retBean.setRet_msg("异步充值返回数据为空!");
					ResponseUtils.responseJson(response, JSON.toJSONString(retBean));
				}
			}
			// 连连充值异步通知处理
			ResponseEntity<?> resNotice = finPayMentNoticeFacade.lianlianRechargeAsyncNotifyUrl(reqStr,
					INCOME_TYPE_COMMON);
			if (resNotice.getResStatus() == Constants.SUCCESS) {
				retBean.setRet_code(LianLianPlatformPayStateEnum.PLATSUCCESS.getThirdPaymentState());
				retBean.setRet_msg(resNotice.getResMsg().toString());
				ResponseUtils.responseJson(response, JSON.toJSONString(retBean));
			} else {
				retBean.setRet_code(LianLianPlatformPayStateEnum.PLATFAIL.getThirdPaymentState());
				retBean.setRet_msg(resNotice.getResMsg().toString());
				ResponseUtils.responseJson(response, JSON.toJSONString(retBean));
			}
		} catch (Exception e) {
			logger.error("连连充值异步回调操作, 异步回调充值处理失败: ", e);
			retBean.setRet_code(LianLianPlatformPayStateEnum.PLATFAIL.getThirdPaymentState());
			retBean.setRet_msg(e.getMessage());
			ResponseUtils.responseJson(response, JSON.toJSONString(retBean));
		}
	}

	/***
	 * @Description :宝付网银充值，同步通知
	 * @Method_Name : baofuWyRechargePageNotice;
	 * @param response
	 * @param request
	 * @return
	 * @return : ModelAndView;
	 * @Creation Date : 2018年1月24日 下午5:16:47;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("baofuWyRechargePageNotice")
	@AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
	@ResponseBody
	public ModelAndView baofuWyRechargePageNotice(HttpServletResponse response, HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView("success");
		try {
			Map<String, String> resultData = new LinkedHashMap<String, String>();
			resultData.put("TransID=", request.getParameter("TransID"));// 商户订单号
			resultData.put("Result=", request.getParameter("Result"));// 支付结果
			resultData.put("ResultDesc=", request.getParameter("ResultDesc"));// 支付结果描述
			// 实际成功金额 返回单位（分）
			resultData.put("FactMoney=", request.getParameter("FactMoney"));
			resultData.put("AdditionalInfo=", request.getParameter("AdditionalInfo"));// 订单附加消息
			resultData.put("SuccTime=", request.getParameter("SuccTime"));// 支付完成时间
			ResponseEntity<?> noticeResult = finPayMentNoticeFacade.baofuWyRechargePageNotice(resultData,
					request.getParameter("Md5Sign"));
			modelAndView.addObject("state", noticeResult.getResStatus());
			modelAndView.addObject("message", noticeResult.getResMsg().toString());
		} catch (Exception e) {
			logger.error("宝付网银充值同步通知操作, 网银充值同步通知处理失败: ", e);
			modelAndView.addObject("state", Constants.ERROR);
			modelAndView.addObject("message", "支付失败！");
		}
		return modelAndView;
	}

	/***
	 * @Description : 宝付网银充值，异步通知
	 * @Method_Name : baofuWyRechargeAsyncNotifyUrl;
	 * @param response
	 * @param request
	 * @return : void;
	 * @Creation Date : 2018年1月24日 下午6:17:09;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("baofuWyRechargeAsyncNotifyUrl")
	@AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
	public void baofuWyRechargeAsyncNotifyUrl(HttpServletResponse response, HttpServletRequest request) {
		try {
			Map<String, String> resultData = new LinkedHashMap<String, String>();
			resultData.put("TransID=", request.getParameter("TransID"));// 商户订单号
			resultData.put("Result=", request.getParameter("Result"));// 支付结果
			resultData.put("ResultDesc=", request.getParameter("ResultDesc"));// 支付结果描述
			// 实际成功金额返回单位（分）
			resultData.put("FactMoney=", request.getParameter("FactMoney"));
			resultData.put("AdditionalInfo=", request.getParameter("AdditionalInfo"));// 订单附加消息
			resultData.put("SuccTime=", request.getParameter("SuccTime"));// 支付完成时间
			ResponseEntity<?> noticeResult = finPayMentNoticeFacade.baofuWyRechargeAsyncNotifyUrl(resultData,
					request.getParameter("Md5Sign"), INCOME_TYPE_COMMON);
			if (noticeResult.getResStatus() == ERROR) {
				ResponseUtils.responseJson(response, BAOFU_FAILURE);
			} else {
				ResponseUtils.responseJson(response, BAOFU_SUCCESS);
			}
		} catch (Exception e) {
			logger.error("宝付网银充值异步通知, 异步通知处理失败: ", e);
			ResponseUtils.responseJson(response, BAOFU_FAILURE);
		}
		return;
	}
}
