package com.hongkun.finance.api.controller.payment;

import com.hongkun.finance.api.controller.BaseControllerTest;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
public class PaymentControllerTest extends BaseControllerTest {

	/**
	 * @Description : 根据支付渠道获取支付渠道下的银行列表
	 * @Method_Name : searchBankCardList;
	 * @throws Exception
	 * @return : void;
	 * @Creation Date : 2018年3月21日 上午9:30:09;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@Test
	public void searchBankCardList() throws Exception {
		Map<String, String> params = new HashMap<>();
		params.put("payChannel", "1");// 支付渠道 1- lianlian ；2- liandong ；3- baofu
		doTest("paymentRechargeController/searchBankCardList", params, 3);
	}

	/**
	 * @Description : 查询当前系统启用的支付渠道列表，以及当前绑定的银行卡列表
	 * @Method_Name : searchPayChannelList;
	 * @throws Exception
	 * @return : void;
	 * @Creation Date : 2018年3月21日 上午9:30:30;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@Test
	public void searchPayChannelList() throws Exception {
		Map<String, String> params = new HashMap<>();
		params.put("payStyle", "10");// 支付方式 10 充值 14提现
		params.put("platformSource", "11");// 交易来源 10-PC 11-IOS 12-ANDRIOD
											// 13-WAP
		doTest("paymentRechargeController/searchPayChannelList", params, 3);
	}

	/**
	 * @Description : 充值
	 * @Method_Name : toRecharge;
	 * @throws Exception
	 * @return : void;
	 * @Creation Date : 2018年3月21日 上午9:31:48;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@Test
	public void toRecharge() throws Exception {
		Map<String, String> params = new HashMap<>();
		params.put("bankCard", "6226220114154312");// 充值的银行卡号
		params.put("platformSourceName", "11");// 交易来源 10-PC 11-IOS 12-ANDRIOD
												// 13-WAP
		params.put("rechargeFlag", "0");// 0:正常充值 1：投资充值
		params.put("transMoney", "1");// 充值金额(元)
		params.put("payChannel", "3");// 支付渠道 1-连连,2-联动,3-宝付
		params.put("sign_type", "MD5");// RAS /MD5（默认MD5）
		params.put("sessionId", "123");
		params.put("sign", "d9adc5f1812388e974a4f9b4fa95657e");// 加密签名
		doTest("paymentRechargeController/toRecharge", params, 78);
	}

	/**
	 * @Description : 当客户端调用SDK失败，则将订单状态置为失败
	 * @Method_Name : updateOrderState;
	 * @throws Exception
	 * @return : void;
	 * @Creation Date : 2018年3月21日 上午9:37:04;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@Test
	public void updateOrderState() throws Exception {
		Map<String, String> params = new HashMap<>();
		params.put("orderId", "48");// 订单号
		doTest("paymentRechargeController/updateOrderState", params, 78);
	}

	/**
	 * @Description : 投资选择付款方式
	 * @Method_Name : toChooseInvestAccount;
	 * @throws Exception
	 * @return : void;
	 * @Creation Date : 2018年3月21日 上午9:37:54;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@Test
	public void toChooseInvestAccount() throws Exception {
		Map<String, String> params = new HashMap<>();
		params.put("platformSource", "11");// 平台来源 10-PC 11-IOS 12-ANDRIOD
											// 13-WAP
		super.doTest("paymentRechargeController/toChooseInvestAccount", params, 78);
	}

	/**
	 * @Description : 根据卡号和支付渠道查询银行卡BIN
	 * @Method_Name : searchBankCardBin;
	 * @throws Exception
	 * @return : void;
	 * @Creation Date : 2018年3月21日 上午9:29:16;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@Test
	public void searchBankCardBin() throws Exception {
		Map<String, String> params = new HashMap<>();
		params.put("cardNo", "6226220114154312");// 银行卡号
		params.put("payChannel", "1");// 支付渠道 1- lianlian ；2- liandong ；3- baofu
		doTest("paymentRechargeController/searchBankCardBin", params, 78);
	}
	/**
	 * @Description : 短验
	 * @Method_Name : paymentVerificationCode;
	 * @throws Exception
	 * @return : void;
	 * @Creation Date : 2018-05-17 14:13:33;
	 * @Author : binliang@hongkun.com.cn 梁彬;
	 */
	@Test
	public void paymentVerificationCode() throws Exception {
		Map<String, String> params = new HashMap<>();
		params.put("bankCardId", "23");
		params.put("userId", "78");
		params.put("rechargeFlag", "0");
		params.put("transMoney", "1");
		params.put("payChannel", "4");
		params.put("platformSourceName", "11");
		params.put("systemTypeName", "4");
		params.put("bankCard", "6222020200106704618");
		params.put("tel", "15011101965");
		params.put("userName", "梁彬");
		params.put("idCard", "370921198804075739");
		params.put("uType", "1");
		params.put("bankCode", "ICBC");
		
		doTest("paymentRechargeController/paymentVerificationCode", params, 78);
	}
}
