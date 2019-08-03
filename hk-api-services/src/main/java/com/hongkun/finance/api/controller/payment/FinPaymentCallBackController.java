package com.hongkun.finance.api.controller.payment;

import static com.yirun.framework.core.commons.Constants.ERROR;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.dubbo.config.annotation.Reference;
import static com.hongkun.finance.payment.constant.PaymentConstants.*;
import com.hongkun.finance.payment.enums.PayChannelEnum;
import com.hongkun.finance.payment.facade.FinPayMentNoticeFacade;
import com.hongkun.finance.payment.util.PaymentUtil;
import com.hongkun.finance.user.support.security.OperationTypeEnum;
import com.hongkun.finance.user.support.security.component.annotation.AskForPermission;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.enums.SystemTypeEnums;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.response.ResponseUtils;

/**
 * @Description : 金融支付回调接口
 * @Project : hk-financial-services
 * @Program Name : com.hongkun.finance.web.controller.FinPaymentController.java
 * @Author : yanbinghuang@hongkun.com
 */
@Controller
@RequestMapping("/finPaymenCallBackController")
public class FinPaymentCallBackController {
	private static final Logger logger = LoggerFactory.getLogger(FinPaymentCallBackController.class);
	public static String ENCONDING_UTF = "UTF-8";
	@Reference
	private FinPayMentNoticeFacade finPayMentNoticeFacade;

	/**
	 * @Description : 宝付协议支付提现异步通知处理
	 * @Method_Name : baofuProtocolAsyncNotifyUrl;
	 * @param response
	 * @param request
	 * @return : void;
	 * @Creation Date : 2018年5月23日 上午9:40:24;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("baofuRushAsyncNotifyUrl")
	@AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
	public void baofuRushAsyncNotifyUrl(HttpServletResponse response, HttpServletRequest request) {
		try {
			// 获取回调参数
			String data_content = request.getParameter("data_content");
			logger.info("方法，baofuRushAsyncNotifyUrl controller 层，宝付协议支付提现异步通知, 返回的参数data_content: {}", data_content);
			// 校验对象数据是否为空
			if (PaymentUtil.isNull(data_content) || data_content.length() == 0) {
				logger.error("方法，baofuRushAsyncNotifyUrl controller层,宝付协议支付提现异步通知, 返回的参数data_content为空!");
				ResponseUtils.responseJson(response, BAOFU_FAILURE);
			}
			// 宝付提现异步通知业务处理
			ResponseEntity<?> resResult = finPayMentNoticeFacade.baofuAsyncNotice(data_content, SystemTypeEnums.HKJF,
					PayChannelEnum.BaoFu);
			// 响应给商户处理结果
			ResponseUtils.responseJson(response, resResult.getParams().get("ret_msg").toString());
		} catch (Exception e) {
			logger.error("方法，baofuRushAsyncNotifyUrl controller层,宝付协议支付提现异步通知处理异常:{} ", e);
			ResponseUtils.responseJson(response, BAOFU_FAILURE);
		}
		return;
	}


	/***
	 * @Description : 宝付协议支付-异步通知
	 * @Method_Name : baofuProtocolRechargeAsyncNotifyUrl;
	 * @param response
	 * @param request
	 * @return : void;
	 * @Creation Date : 2018-05-17 16:12:56;
	 * @Author : binliang@hongkun.com.cn 梁彬;
	 */
	@RequestMapping("baofuProtocolRechargeAsyncNotifyUrl")
	@AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
	public void baofuProtocolRechargeAsyncNotifyUrl(HttpServletResponse response, HttpServletRequest request) {
		logger.info("方法: baofuProtocolRechargeAsyncNotifyUrl, 宝付协议支付异步回调controller层操作, 入参: trans_id: {}",
				request.getParameter("trans_id"));
		try {
			String resp_code = request.getParameter("resp_code");// 应答码
			String terminal_id = request.getParameter("terminal_id");
			String member_id = request.getParameter("member_id");
			String biz_resp_code = request.getParameter("biz_resp_code");// 业务应答码
			String biz_resp_msg = request.getParameter("biz_resp_msg");// 业务返回说明
			String order_id = request.getParameter("order_id");
			String trans_id = request.getParameter("trans_id");// 商户原始订单号
			String succ_amt = request.getParameter("succ_amt");// 充值金额，异步分为单位
			String succ_time = request.getParameter("succ_time");
			String signature = request.getParameter("signature");// 签名域
			logger.info(
					"baofuProtocolRechargeAsyncNotifyUrl 宝付协议异步通知参数：resp_code: {},terminal_id: {},member_id: {}, biz_resp_code: {}"
							+ ", biz_resp_msg: {}, order_id: {}, trans_id: {}, succ_amt: {}, succ_time: {}, signature: {}",
					resp_code, terminal_id, member_id, biz_resp_code, biz_resp_msg, order_id, trans_id, succ_amt,
					succ_time, signature);
			
			// 校验对象数据是否为空
			if (PaymentUtil.isNull(resp_code) || PaymentUtil.isNull(biz_resp_code) || PaymentUtil.isNull(trans_id)) {
				logger.error("宝付协议支付回调操作, 异步通知内容为空!");
				ResponseUtils.responseJson(response, BAOFU_FAILURE);
			}
			Map<String, String> dateArry = new TreeMap<String, String>();
			dateArry.put("resp_code", resp_code);
			dateArry.put("terminal_id", terminal_id);
			dateArry.put("member_id", member_id);
			dateArry.put("biz_resp_code", biz_resp_code);
			dateArry.put("biz_resp_msg", biz_resp_msg);
			dateArry.put("order_id", order_id);
			dateArry.put("trans_id", trans_id);
			dateArry.put("succ_amt", succ_amt);
			dateArry.put("succ_time", succ_time);
			dateArry.put("signature", signature);
			// 宝付认证充值异步回调通知处理
			ResponseEntity<?> responseEntity = finPayMentNoticeFacade.baofuAgreePayAsyncNotifyUrl(dateArry,
					INCOME_TYPE_COMMON);
			if (responseEntity.getResStatus() == ERROR) {
				logger.error("宝付协议支付异步回调操作, 异步回调处理失败: {}", responseEntity.getResMsg().toString());
				ResponseUtils.responseJson(response, BAOFU_FAILURE);
			} else {
				ResponseUtils.responseJson(response, BAOFU_SUCCESS);
			}
		} catch (Exception e) {
			logger.error("方法: baofuProtocolRechargeAsyncNotifyUrl,宝付协议支付异步回调操作异常信息:{} ", e);
			ResponseUtils.responseJson(response, BAOFU_FAILURE);
		}
	}
	
	/***
	 * @Description : 易宝认证充值-异步通知
	 * @Method_Name : yeepayRechargeRzAsyncNotifyUrl;
	 * @param response
	 * @param request
	 * @return : void;
	 * @Creation Date : 2018-10-17 09:11:31;
	 * @Author : binliang@hongkun.com.cn 梁彬;
	 */
	@RequestMapping("yeepayRechargeRzAsyncNotifyUrl")
	@AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
	public void yeepayRechargeRzAsyncNotifyUrl(HttpServletResponse response, HttpServletRequest request) {
		logger.info("方法: yeepayRechargeRzAsyncNotifyUrl, 易宝认证支付异步回调controller层操作, 入参: data: {} , encryptkey: {}",
				request.getParameter("data"),request.getParameter("encryptkey"));
		try {
			String data = request.getParameter("data");
	        String encryptkey = request.getParameter("encryptkey");
			if(StringUtils.isBlank(data) || StringUtils.isBlank(encryptkey)){
				logger.error("易宝认证支付回调操作, 异步通知内容为空!");
				ResponseUtils.responseJson(response, YEEPAY_NOTICE_FAILURE);
			}
	        Map<String,String> paramsMap = new HashMap<>();
	        paramsMap.put("data", data);
	        paramsMap.put("encryptkey", encryptkey);
			ResponseEntity<?> responseEntity = this.finPayMentNoticeFacade.yeepayRzAsyncNotifyUrl(paramsMap, INCOME_TYPE_COMMON);
			if (responseEntity.getResStatus() == ERROR) {
				logger.error("易宝认证支付异步回调操作, 异步回调处理失败: {}", responseEntity.getResMsg().toString());
				ResponseUtils.responseJson(response, YEEPAY_NOTICE_FAILURE);
			} else {
				ResponseUtils.responseJson(response, YEEPAY_NOTICE_SUCCESS);
			}
		} catch (Exception e) {
			logger.error("方法: yeepayRechargeRzAsyncNotifyUrl,易宝认证支付异步回调操作异常信息:{} ", e);
			ResponseUtils.responseJson(response, YEEPAY_NOTICE_FAILURE);
		}
	}
	
	/***
	 * @Description : 易宝网银充值-异步通知
	 * @Method_Name : yeepayRechargeWyAsyncNotifyUrl;
	 * @param response
	 * @param request
	 * @return : void;
	 * @Creation Date : 2018-10-17 09:11:31;
	 * @Author : binliang@hongkun.com.cn 梁彬;
	 */
	@RequestMapping("yeepayWyRechargeAsyncNotifyUrl")
	@AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
	public void yeepayWyRechargeAsyncNotifyUrl(HttpServletResponse response, HttpServletRequest request) {
		logger.info("方法: yeepayWyRechargeAsyncNotifyUrl, 易宝网银支付异步回调controller层操作, 入参: data: {} , encryptkey: {}",
				request.getParameter("data"),request.getParameter("encryptkey"));
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("p1_MerId", request.getParameter("p1_MerId"));
		paramsMap.put("r0_Cmd", request.getParameter("r0_Cmd"));
		paramsMap.put("r1_Code", request.getParameter("r1_Code"));
		paramsMap.put("r2_TrxId", request.getParameter("r2_TrxId"));
		paramsMap.put("r3_Amt", request.getParameter("r3_Amt"));
		paramsMap.put("r4_Cur", request.getParameter("r4_Cur"));
		paramsMap.put("r5_Pid", request.getParameter("r5_Pid"));
		paramsMap.put("r6_Order", request.getParameter("r6_Order"));
		paramsMap.put("r7_Uid", request.getParameter("r7_Uid"));
		paramsMap.put("r8_MP", request.getParameter("r8_MP"));
		paramsMap.put("r9_BType", request.getParameter("r9_BType"));
		paramsMap.put("hmac_safe", request.getParameter("hmac_safe"));
		paramsMap.put("hmac", request.getParameter("hmac"));
		logger.info("接收到的参数 paramsMap: {}", paramsMap);
		try {
			ResponseEntity<?> result = this.finPayMentNoticeFacade.yeepayWyAsyncNotifyUrl(paramsMap);
			if (result.getResStatus() == ERROR) {
				logger.error("易宝网银支付异步回调操作, 异步回调处理失败: {}", result.getResMsg().toString());
				ResponseUtils.responseJson(response, YEEPAY_NOTICE_FAILURE);
			} else {
				ResponseUtils.responseJson(response, YEEPAY_NOTICE_SUCCESS);
			}
		} catch (Exception e) {
			logger.error("方法: yeepayRechargeWyAsyncNotifyUrl,易宝网银支付异步回调操作异常信息:{} ", e);
			ResponseUtils.responseJson(response, YEEPAY_NOTICE_FAILURE);
		}
	}
	
	/**
	 * @Description : 易宝提现异步通知处理
	 * @Method_Name : yeepayRushAsyncNotifyUrl;
	 * @param response
	 * @param request
	 * @return : void;
	 * @Creation Date : 2018-10-17 16:19:37;
	 * @Author : binliang@hongkun.com.cn 梁彬;
	 */
	@RequestMapping("yeepayRushAsyncNotifyUrl")
	@AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
	public void yeepayRushAsyncNotifyUrl(HttpServletResponse response, HttpServletRequest request) {
		try {
			// 获取回调参数
			StringBuffer sb = new StringBuffer(2000);
	    	InputStream is = request.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
			//读取HTTP请求内容
	        String buffer = null;
	        while ((buffer = br.readLine()) != null) {
	       		sb.append(buffer);
	        }
	    	String responseMsg = sb.toString();
			logger.info("方法，yeepayRushAsyncNotifyUrl controller 层，易宝提现异步通知, 接收参数: {}", responseMsg);
			// 校验对象数据是否为空
			if (PaymentUtil.isNull(responseMsg) || responseMsg.length() == 0) {
				logger.error("方法，yeepayRushAsyncNotifyUrl controller层,易宝提现异步通知参数为空!");
				ResponseUtils.responseJson(response, BAOFU_FAILURE);
			}
			ResponseEntity<?> result = this.finPayMentNoticeFacade.yeepayRushAsyncNotifyUrl(responseMsg);
			
			if(result.getResStatus() == Constants.SUCCESS){
				String resultStr = result.getParams().get("returnStr").toString();
				logger.info("回写易宝数据，resultStr: {}", resultStr);
				response.getOutputStream().write(resultStr.getBytes());
			}
			
		} catch (Exception e) {
			logger.error("方法，baofuRushAsyncNotifyUrl controller层,宝付协议支付提现异步通知处理异常:{} ", e);
			ResponseUtils.responseJson(response, BAOFU_FAILURE);
		}
		return;
	}
	
	
}
