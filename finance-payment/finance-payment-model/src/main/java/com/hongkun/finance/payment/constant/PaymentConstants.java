
/**
 * 
 */
package com.hongkun.finance.payment.constant;

/**
 * @Description : 与第三方支付相关或支付服务自己定义的支付常量类
 * @Project : finance-payment-model
 * @Program Name : com.hongkun.finance.payment.constant.PaymentConstants.java
 * @Author : caoxinbang@hongkun.com.cn 曹新帮
 */

public class PaymentConstants {
	/**
	 * 支付服务端成功标识 PY200
	 */
	public static String SERVER_SUCCESS_FLAG = "PY200";
	/**
	 * 支付服务端失败标识 PY201
	 */
	public static String SERVER_FAIL_FLAG = "PY201";
	/**
	 * 支付成功标识信息 success
	 */
	public static String SUCCESS_FLAG_NAME = "success";
	/**
	 * 支付失败标识信息 error
	 */
	public static String ERROR_FLAG_NAME = "error";
	/**
	 * 第三方返回成功标识 0000
	 */
	public static String THIRD_SUCCESS_FLAG = "0000";
	/** 数据字典支付模块 **/
	public static String PAYMENT_DIC_BUSINESS = "payment";
	/** 数据字典支付渠道子模块 **/
	public static String PAYMENT_DIC_BUSINESS_SUBJECT_CHANNEL = "channel";
	/** 支付,系统CODE **/
	public static String PAYMENT_DIC_BUSINESS_SUBJECT_SYSCODE = "sys_code";
	public static String PAYMENT_DIC_BUSINESS_SUBJECT_TRANSFER = "fin_transfer";
	public static String PAYMENT_DIC_BUSINESS_SUBJECT_TRANSRECORD = "trans_record";

	public static String PAYMENT_DIC_BUSINESS_SUBJECT_TRADE = "fin_trade";
	/***************************************** 第三方支付系统提现异步通知状态 ******************************/
	/** 订单划转中 **/
	public static int PAYMENT_THIRD_BANK_SYSTEM_STATE_TRANSFER = 0;
	/** 订单交易成功 **/
	public static int PAYMENT_THIRD_BANK_SYSTEM_STATE_SUCCESS = 1;
	/** 订单交易失败 **/
	public static int PAYMENT_THIRD_BANK_SYSTEM_STATE_FAIL = -1;
	/** 订单已退款 **/
	public static int PAYMENT_THIRD_BANK_SYSTEM_STATE_REFUND = 2;
	/***************************************** 平台处理提现异步通知响应状态 ******************************/
	/** 订单处理成功 **/
	public static int PAYMENT_PLATFORM_SYSTEM_WITHDRAW_STATE_SUCESS = 100;
	/** 订单处理失败 **/
	public static int PAYMENT_PLATFORM_SYSTEM_WITHDRAW_STATE_FAIL = -666;
	/** 订单不存在 **/
	public static int PAYMENT_PLATFORM_SYSTEM_WITHDRAW_STATE_NON_EXISTENT = 777;
	/** 订单重复请求 **/
	public static int PAYMENT_PLATFORM_SYSTEM_WITHDRAW_STATE_REPEAT = 888;

	/********************* 用户类型 *************************************************************************/
	/** 个人用户 **/
	public static int PERSONAL_USER = 1;
	/** 企业用户 **/
	public static int BUSINESS_USER = 2;
	/********************* 提现账户类型 *************************************************************************/
	/** 对私 **/
	public static int TRADE_TYPE_USER_PERSONAL = 1;
	/** 对公 **/
	public static int TRADE_TYPE_USER_BUSINESS = 2;
	
	/************************* 是否有积分消费状态 ************************************/
	/**
	 * 1-有积分消费
	 */
	public static Integer EXIST_POINT_PAY = 1;
	/**
	 * 0:没有积分消费
	 */
	public static Integer NO_EXIST_POINT_PAY = 0;

	/************************* 宝付对账文件类型 ************************************/
	/** 收款 fi **/
	public static String BAOFU_FILE_TYPE_INCOME = "fi";
	/** 出款 fo **/
	public static String BAOFU_FILE_TYPE_PAY = "fo";
	/************************* 平台系统对账文件类型 ************************************/
	/** 收款 0 **/
	public static int PAYMENT_INCOME = 0;
	/** 出款 1 **/
	public static int PAYMENT_OUT = 1;

	/********************* 平台和第三方支付系统对账公用的状态 *********************************************/
	/** 支付成功状态 0 **/
	public static int PAY_STATE_SUCCESS = 0;
	/** 支付失败状态 5 **/
	public static int PAY_STATE_FAIL = 5;
	/** 银行支付处理中状态 2 **/
	public static int PAY_STATE_PROCESSING = 2;
	/** 等待支付状态 3 **/
	public static int PAY_STATE_WAIT = 3;
	/** 订单不存在4 **/
	public static int PAY_STATE_NO_ORDER = 4;
	/********************* 连连支付查证结果状态 ******************************************************/
	/** 支付查证结果成功 SUCCESS **/
	public static String LIANLIAN_PAY_CHECK_STATE_SUCCESS = "SUCCESS";
	/** 支付查证结果 银行代付处理中 PROCESSING **/
	public static String LIANLIAN_PAY_CHECK_STATE_PROCESSING = "PROCESSING";
	/** 支付查证结果 等待支付 AITING **/
	public static String LIANLIAN_PAY_CHECK_STATE_WAITING = "WAITING";
	/** 支付查证结果 代付失败 FAILURE **/
	public static String LIANLIAN_PAY_CHECK_STATE_FAILURE = "FAILURE";
	/** 支付查证结果 代付退款 CANCEL **/
	public static String LIANLIAN_PAY_CHECK_STATE_CANCEL = "CANCEL";
	/** 支付查证结果 连连查询无此订单状态 8901 **/
	public static String LIANLIAN_PAY_CHECK_STATE_NO_DATA = "8901";

	/********************* 宝付支付查证结果状态 ******************************************************/

	/********************* 宝付认证支付查证状态******************start ********/
	/** 支付查证结果 成功 0000 **/
	public static String BAOFU_RZ_PAY_CHECK_STATE_SUCCESS = "0000";
	/** 支付查证结果 交易结果未知，请稍后查询 FI00002 **/
	public static String BAOFU_RZ_PAY_CHECK_STATE_PROCESSING = "FI00002";
	/** 支付查证结果 订单未支付 FI00054 **/
	public static String BAOFU_RZ_PAY_CHECK_STATE_AITING = "FI00054";
	/** 支付查证结果 交易失败 FI00007 **/
	public static String BAOFU_RZ_PAY_CHECK_STATE_FAILURE = "FI00007";
	/** 支付查证结果 订单不存在 FI00014 **/
	public static String BAOFU_RZ_PAY_CHECK_STATE_NO_DATA = "FI00014";
	/*********** 宝付认证支付查证状态 *************end ******************/

	/*********** 宝付网银支付查证状态***********start ******************/
	/** 支付查证结果 成功 Y **/
	public static String BAOFU_WY_PAY_CHECK_STATE_SUCCESS = "Y";
	/** 支付查证结果 处理中 P **/
	public static String BAOFU_WY_PAY_CHECK_STATE_PROCESSING = "P";
	/** 支付查证结果 交易失败 N **/
	public static String BAOFU_WY_PAY_CHECK_STATE_FAILURE = "N";
	/** 支付查证结果 订单不存在 F **/
	public static String BAOFU_WY_PAY_CHECK_STATE_NO_DATA = "F";
	/*********** 宝付网银支付查证状态 **********end *******************/

	/*********** 宝付提现查证状态***********start ******************/
	/** 支付查证结果 成功 1 **/
	public static String BAOFU_WITHDRAW_CHECK_STATE_SUCCESS = "1";
	/** 支付查证结果 处理中 0 **/
	public static String BAOFU_WITHDRAW_CHECK_STATE_PROCESSING = "0";
	/** 支付查证结果 交易失败 -1 **/
	public static String BAOFU_WITHDRAW_CHECK_STATE_FAILURE = "-1";
	/** 支付查证结果 退款2 **/
	public static String BAOFU_WITHDRAW_CHECK_STATE_CANCEL = "2";
	/*********** 宝付提现查证状态 **********end *******************/

	/*********** 宝付提现同步响应状态***********start ******************/
	/** 代付请求交易成功（交易已受理） 0000 **/
	public static String BAOFU_WITHDRAW_STATE_REQ_SUCCESS = "0000";
	/** 代付交易成功 200 ***/
	public static String BAOFU_WITHDRAW_STATE_PAY_SUCCESS = "200";
	/** 代付交易未明，请发起该笔订单查询 0300 **/
	public static String BAOFU_WITHDRAW_STATE_PAY_PROCESSING = "0300";
	/** 代付交易查证信息不存在 0401 **/
	public static String BAOFU_WITHDRAW_STATE_NO_DATA = "0401";
	/** 代付主机系统繁忙 0999 **/
	public static String BAOFU_WITHDRAW_STATE_BUSY = "0999";
	/*********** 宝付提现同步响应状态***********end ******************/

	/*********** 宝付认证充值同步通知响应状态***********start ******************/
	/** 系统未开放或暂时关闭，请稍后再试 ***/
	public static String BAOFU_RZ_NOTICE_STATE_NO_OPEN = "0002";
	/** 交易通讯超时，请发起查询交易 **/
	public static String BAOFU_RZ_NOTICE_STATE_OVERTIME = "0003";
	/** 交易已受理，请稍后查询交易结果 **/
	public static String BAOFU_RZ_NOTICE_STATE_PROCESSING = "0004";
	/** 交易状态未明，请查询对账结果 **/
	public static String BAOFU_RZ_NOTICE_STATE_UNKNOWN = "0005";
	/** 交易结果未知，请稍后查询 **/
	public static String BAOFU_RZ_NOTICE_STATE_RESULT_UNKNOWN = "0303";
	/** 交易正在处理，请勿重复支付 **/
	public static String BAOFU_RZ_NOTICE_STATE_PAYING = "0304";
	/*********** 宝付认证充值同步通知响应状态***********END ******************/

	/** 宝付返回成功code **/
	public static String BAOFU_SUCCESS_CODE = "0000";
	/** 连连返回成功code **/
	public static String LIAN_SUCCESS_CODE = "0000";
	/** 连连返回成功标识 **/
	public static String LIAN_SUCESS_SIGN = "SUCCESS";

	/** 宝付认证充值异步通知后，平台同步响应状态 处理成功 **/
	public static String BAOFU_SUCCESS = "OK";
	/** 宝付认证充值异步通知后，平台同步响应状态 处理失败 **/
	public static String BAOFU_FAILURE = "FAILURE";

	/** 宝付网银充值同步通知响应状态 处理成功 **/
	public static String BAOFU_WY_SUCCESS = "1";
	/** 宝付网银充值同步通知响应状态 处理失败 **/
	public static String BAOFU_WY_FAILURE = "0";
	
	/** 宝付对账文件只返回成功的交易状态 **/
	public static int BAOFU_PAY_STATE_SUCCESS = 1;

	/** 充值类型：普通充值 */
	public static int INCOME_TYPE_COMMON = 0;
	/** 充值类型：物业缴费充值 */
	public static int INCOME_TYPE_PROPERTY = 1;
	
	// ################放款常量
	/** 放款常量-借款人账户 */
	public static String MAKELOAN_BORRORWERACCOUNT = "makeloan_borrorweraccount";
	/** 放款常量-本金接收人账户 */
	public static String MAKELOAN_RECEIPTACCOUNT = "makeloan_receiptAccount";

	public static int SHOW_FRONT_YES = 0;

	public static int SHOW_FRONT_NO = 1;
	
	/***********宝付协议支付 start*************/
	/**宝付协议支付报文应答码-成功**/
	public static String BAOFU_PROTOCAL_SUCCESS = "S";
	/**宝付协议支付报文应答码-失败**/
	public static String BAOFU_PROTOCAL_FAILURE = "F";
	/**宝付协议支付报文应答码-处理中**/
	public static String BAOFU_PROTOCAL_PROCESSING = "I";
	/**宝付协议支付报文应答码-支付结果查询类交易才会返回，表示订单查询参数错误或其他原因导致的订单查询失败，而非订单交易失败**/
	public static String BAOFU_PROTOCAL_PAY_FAILURE = "FF";
	
	/**宝付协议支付业务应答码-成功**/
	public static String BAOFU_PROTOCAL_CODE_SUCCESS = "0000";
	/**预绑卡**/
	public static Integer PRE_TIED_CARD = 1;
	/**确认绑卡**/
	public static Integer CONFIG_TIED_CARD = 2;
	/**直接支付**/
	public static Integer DIRECT_PAYMENT = 3;
	/**预支付**/
	public static Integer PRE_PAYMENT = 4;
	/**确认支付**/
	public static Integer CONFIRM_PAYMENT = 5;
	
	/** 协议支付提现订单不存在0401 **/
	public static String BAOFUP_ROTOCAL_TX_CHECK_STATE_NO_DATA = "0401";
	
	/***********宝付协议支付 end*************/
	
	/**********易宝支付 操作状态* start************/
	/**易宝预支付，即首次点发送验证码**/
	public static Integer YEEPAY_FIRST_RECHARGE = 4;
	/**易宝确认支付**/
	public static Integer YEEPAY_CONFIRM_PAYMENT = 5;
	/**易宝 重发短信**/
	public static Integer YEEPAY_SENDSMS_AGAIN = 6;
	/***易宝提现同步成功编码**/
	public static String YEEPAY_WITHDRAW_CODE_SUCCESS = "1";
	/** 易宝认证充值异步通知后，平台同步响应状态 处理成功 **/
	public static String YEEPAY_NOTICE_SUCCESS = "SUCCESS";
	/** 易宝认证充值异步通知后，平台同步响应状态 处理失败 **/
	public static String YEEPAY_NOTICE_FAILURE = "FAILURE";
	/**易宝认证充值 异步通知 状态码  success***/
	public static String YEEPAY_RZ_SUCCESS_CODE = "PAY_SUCCESS";
	/**易宝认证充值 异步通知 状态码  fail***/
	public static String YEEPAY_RZ_FAIL_CODE = "PAY_FAIL";
	/**易宝认证充值 异步通知 状态码  time out***/
	public static String YEEPAY_RZ_TIME_OUT_CODE = "TIME_OUT";
	/***易宝提现 通知支付状态 以及回写 的状态码 ，提现成功、回调处理成功****/
	public static String YEEPAY_RUSH_SUCCESS = "S";
	/***易宝提现 通知支付状态 以及回写 的状态码 ，提现失败、回调处理失败****/
	public static String YEEPAY_RUSH_FAIL = "F";
	/**********易宝支付 操作状态* end************/

	/**********易宝充值返回中间状态码****************/
	public static int PAY_STATE_WAITING = 1001;
	
}
