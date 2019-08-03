package com.hongkun.finance.payment.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.payment.constant.TradeTransferConstants;
import com.hongkun.finance.payment.enums.FundtransferBigTypeStateEnum;
import com.hongkun.finance.payment.enums.FundtransferSmallTypeStateEnum;
import com.hongkun.finance.payment.enums.PayChannelEnum;
import com.hongkun.finance.payment.enums.PayStyleEnum;
import com.hongkun.finance.payment.facade.FinPaymentFacade;
import com.hongkun.finance.payment.llpayvo.BankCardSignInfo;
import com.hongkun.finance.payment.model.FinAccount;
import com.hongkun.finance.payment.model.FinBankCard;
import com.hongkun.finance.payment.model.FinFundtransfer;
import com.hongkun.finance.payment.model.FinTradeFlow;
import com.hongkun.finance.payment.model.vo.BankCardVo;
import com.hongkun.finance.payment.model.vo.RechargeCash;
import com.hongkun.finance.payment.model.vo.TransferVo;
import com.hongkun.finance.payment.model.vo.WithDrawCash;
import com.hongkun.finance.payment.util.FinTFUtil;
import com.yirun.framework.core.enums.PlatformSourceEnums;
import com.yirun.framework.core.enums.SystemTypeEnums;
import com.yirun.framework.core.model.ResponseEntity;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/applicationContext-payment.xml" })
public class TestPaymentInterface {
	@Reference
	private FinPlatformPaywayService finPlatformPaywayService;
	@Reference
	private FinBankReferService finBankReferService;
	@Reference(timeout = 1000)
	private FinConsumptionService finConsumptionService;
	@Reference(timeout = 50000)
	private FinTradeFlowService finTradeFlowService;
	@Reference(timeout = 50000)
	private FinFundtransferService finFundtransferService;
	@Reference(timeout = 50000)
	private FinAccountService finAccountService;
	@Reference
	private FinBankCardService finBankCardService;
	@Reference
	private FinPaymentFacade finPaymentFacade;

	/**
	 * @Description :查询支付方式信息
	 * @Method_Name : findPayInfoTest;
	 * @return : void;
	 * @Creation Date : 2017年6月14日 下午2:29:05;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@Test
	@Ignore
	public void findPayInfoTest() {
		Map<String, String> map = new HashMap<String, String>();
		// 系统下有哪几种支付渠道
		// map.put("sysNameCode", SystemTypeEnums.QKD.getType());
		// map.put("flag", "third_name_code");

		// 系统某个平台下有哪几种支付渠道
		// map.put("sysNameCode", SystemTypeEnums.QKD.getType());
		// map.put("platformName", PlatformSourceEnums.PC.getType());
		// map.put("flag", "third_name_code");

		// 系统下支持哪些平台
		// map.put("sysNameCode", SystemTypeEnums.QKD.getType());
		// map.put("flag", "platform_name");

		// 系统某个平台下有哪几种支付方式
		map.put("sysNameCode", SystemTypeEnums.QKD.getType());
		map.put("platformName", PlatformSourceEnums.PC.getType());
		map.put("flag", "payway_code");
		// List<FinPlatformPayway> payInfoList =
		// finPlatformPaywayService.findPayInfo(map);
		// for (FinPlatformPayway fin : payInfoList) {
		// System.out.println(fin.getSysNameCode() + "======" +
		// fin.getThirdNameCode() + "====" + fin.getPaywayName());
		// }
	}

	/**
	 * @Description :查询支付渠道之间公共的银行信息
	 * @Method_Name : findCommonBankInfo;
	 * @return : void;
	 * @Creation Date : 2017年6月14日 下午2:27:49;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@Test
	@Ignore
	public void findCommonBankInfo() {
		// List<FinBankRefer> list = finBankReferService.findCommonBankInfo();
		// for (FinBankRefer fin : list) {
		// // System.out.println(fin.getBankCode() + "============" +
		// // fin.getBankName());
		// }
		BankCardVo bankCardVo = new BankCardVo();
		bankCardVo.setPayChannel(1);
		bankCardVo.setRegUserId(32);
		bankCardVo.setFinBankCardId(3);
		// BankCardVo bvo = finBankCardService.findBankCardInfo(bankCardVo);
		// System.out.println(bvo.getBankThirdCode());
	}

	/*  *//**
			 * 待审核状态 0
			 */
	/*
	 * public static Integer PENDING_PAYMENT = 0;
	 *//**
		 * 已划转状态 1
		 */
	/*
	 * public static Integer ALREADY_PAYMENT = 1;
	 *//**
		 * 待放款 状态 2
		 */
	/*
	 * public static Integer WAIT_PAY_MONEY = 2;
	 *//***
		 * 运营审核拒绝 状态 3
		 */
	/*
	 * public static Integer OPERATION_AUDIT_REJECT = 3;
	 *//**
		 * 已冲正状态4
		 */
	/*
	 * public static Integer CORRECT_MONEY = 4;
	 *//***
		 * 财务审核拒绝 状态 7
		 */
	/*
	 * public static Integer FINANCE_AUDIT_REJECT = 7;
	 *//**
		 * 划转中状态 8
		 */
	/*
	 * public static Integer IN_TRANSIT = 8;
	 *//**
		 * 划转失败状态 9
		 *//*
		 * public static Integer TRANSIT_FAIL = 9;
		 */

	/***
	 * &#13;
	 * 
	 * @Description : 现金消费测试
	 * @Method_Name : cashPayTest&#13;
	 * @param response
	 * @param request
	 * @return : void&#13;
	 * @throws Exception
	 * @Creation Date : 2017年6月5日 下午5:27:26 &#13;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵&#13;
	 */
	@Test
	public void cashPayTest() throws Exception {
		FinTradeFlow finTradeFlow = FinTFUtil.initFinTradeFlow(6, 1, new BigDecimal(100),
				TradeTransferConstants.TRADE_TYPE_INVEST, PlatformSourceEnums.PC);
		// finTradeFlow.setPayChannel(PayChannelEnum.fromChannelName("lianlian"));
		// finTradeFlow.setInfo("提现");// 备注
		// finTradeFlow.setState(1);
		ResponseEntity<?> result = finConsumptionService.cashPay(finTradeFlow,
				TradeTransferConstants.TRANSFER_SUB_CODE_FREEZE);
		System.out.println("状态：" + result.getResStatus() + "信息：" + result.getResMsg());
	}

	/***
	 * &#13;
	 * 
	 * @Description : 转账消费测试方法
	 * @Method_Name : transferPayTest&#13;
	 * @param response
	 * @param request
	 * @return : void&#13;
	 * @Creation Date : 2017年6月5日 下午5:29:39 &#13;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵&#13;
	 */
	@Test
	@Ignore
	public void transferPayTest() {
		TransferVo transferInfo = new TransferVo();
		transferInfo.setFromUserId(1);
		transferInfo.setToUserId(3);
		transferInfo.setBusinessCode("pay1937343");
		transferInfo.setTradeType(9);
		transferInfo.setTransMoney(new BigDecimal(100));
		transferInfo.setTransferInSubCode(1016);
		transferInfo.setTransferOutSubCode(1017);
		// transferInfo.setInfo("转账");
		transferInfo.setPointChangeMoney(new BigDecimal(50));
		ResponseEntity<?> result = finConsumptionService.transferPay(transferInfo);
		System.out.println("状态：" + result.getResStatus() + "信息：" + result.getResMsg());
	}

	@Test
	@Ignore
	public void saveTransferTest() {
		// TransferVo transferInfo = new TransferVo();
		// transferInfo.setFromUserId(1);
		// transferInfo.setToUserId(3);
		// transferInfo.setPflowId("TF2017061909345937850879");
		// transferInfo.setTransMoney(new BigDecimal(100));
		// transferInfo.setInPutFlag(0);
		// transferInfo.setOutputFlag(0);
		// transferInfo.setState(9);
		// transferInfo.setTransferInSubCode(1016);
		// transferInfo.setTransferOutSubCode(1017);
		// transferInfo.setInfo("转账RRR");
		// transferInfo.setPointChangeMoney(new BigDecimal(50));
		// ResponseEntity<?> result =
		// finConsumptionService.saveTransfer(transferInfo);
		// System.out.println("状态：" + result.getResStatus() + "信息：" +
		// result.getResMsg());
	}

	/**
	 * 
	 * @Description : 查询卡BIN接口测试
	 * @Method_Name : findBankCard;
	 * @param response
	 * @param request
	 * @return : void;
	 * @Creation Date : 2017年6月8日 上午9:56:25;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	// @Test
	// @Ignore
	// public void findCardBin() {
	// ResponseEntity<?> result =
	// finThirdPaymentService.findCardBin("6212260200053518219",
	// SystemTypeEnums.QKD.getType(), PlatformSourceEnums.PC.getType(), "KBIN",
	// PayChannelEnum.LianLian.getChannelKey());
	// System.out.println("状态：" + result.getResStatus() + "信息：" +
	// result.getResMsg());
	// }

	/**
	 * @Description : 支付查证接口
	 * @Method_Name : findPayCheck;
	 * @return : void;
	 * @Creation Date : 2017年8月10日 下午3:42:39;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@Test
	// @Ignore
	public void findPayCheck() {
		// ResponseEntity<?> result =
		// finThirdPaymentService.findPayCheck("TF201706050225357554466214022373",
		// 3,
		// SystemTypeEnums.HKJF, PayStyleEnum.BKLB, PayChannelEnum.LianLian);
		// System.out.println("状态：" + result.getResStatus() + "信息：" +
		// result.getResMsg());
	}

	/**
	 * 
	 * @Description : 查询银行卡卡列表
	 * @Method_Name : findBankCard;
	 * @param response
	 * @param request
	 * @return : void;
	 * @Creation Date : 2017年6月8日 上午11:30:05;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	// @Test
	// @Ignore
	// public void findBankCard() {
	// ResponseEntity<?> result =
	// finThirdPaymentService.findBankCard("55490126-cc54-11e4-90b2-d89d67270c78",
	// SystemTypeEnums.QKD.getType(), PlatformSourceEnums.PC.getType(), "BKLB",
	// PayChannelEnum.LianLian.getChannelKey());
	// System.out.println("状态：" + result.getResStatus() + "信息：" +
	// result.getResMsg());
	// String str = (String) result.getParams().get("bankCard");
	// System.out.println(str);
	// }

	/**
	 * 
	 * @Description : 连连签约绑卡
	 * @Method_Name : bankCardSigningTest;
	 * @param response
	 * @param request
	 * @return : void;
	 * @Creation Date : 2017年6月8日 上午11:30:37;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@Test
	@Ignore
	public void bankCardSigningTest() {
		BankCardSignInfo bankCardSignInfo = new BankCardSignInfo();
		bankCardSignInfo.setCardNo("6212260200053518219");
		bankCardSignInfo.setEmail("");
		bankCardSignInfo.setIdCard("130722199210206012");
		bankCardSignInfo.setLoginName("18301306330");
		bankCardSignInfo.setTel("18301306330");
		bankCardSignInfo.setUserId("e04b200e-7338-11e6-aaf0-00163e0c10bf");
		bankCardSignInfo.setUserName("黄艳兵");
		bankCardSignInfo.setRegisterDate(new Date());
		// bankCardSignInfo.setPayChannel(PayChannelEnum.LianLian.getChannelKey());
		// bankCardSignInfo.setPlatformSourceName(PlatformSourceEnums.PC.getType());
		// bankCardSignInfo.setSysNameCode(SystemTypeEnums.QKD.getType());
		// bankCardSignInfo.setPayStyle("QY");
		// ResponseEntity<?> result =
		// finThirdPaymentService.bankCardSigning(bankCardSignInfo);
		// System.out.println("状态：" + result.getResStatus() + "信息：" +
		// result.getResMsg());
		// if (result.getResStatus() == 200) {
		// Map<String, Object> paymentMap = result.getParams();
		// for (Map.Entry<String, Object> entry : paymentMap.entrySet()) {
		// System.out.println(entry.getKey() + "==============" +
		// entry.getValue());
		// }
		// }
	}

	/**
	 * 
	 * @Description : 提现操作
	 * @Method_Name : withdrawCashTest;
	 * @param response
	 * @param request
	 * @return : void;
	 * @Creation Date : 2017年6月8日 下午6:22:34;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@Test
	@Ignore
	public void withdrawCashTest() {
		WithDrawCash withDrawCashInfo = new WithDrawCash();
		withDrawCashInfo.setFlowId("TF2017061517154181791228");
		withDrawCashInfo.setPayChannelEnum(PayChannelEnum.LianLian);
		// withDrawCashInfo.setNotifyUrl("http://www.baidu.com");
		withDrawCashInfo.setRegUserId(1234567890);
		withDrawCashInfo.setUserName("黄艳兵");
		withDrawCashInfo.setUserType(1);
		withDrawCashInfo.setPayStyleEnum(PayStyleEnum.DK);
		withDrawCashInfo.setPlatformSourceName(PlatformSourceEnums.PC.getType());
		withDrawCashInfo.setSysNameCode(SystemTypeEnums.QKD.getType());
		ResponseEntity<?> result = finConsumptionService.withdrawCash(withDrawCashInfo);
		System.out.println("状态：" + result.getResStatus() + "信息：" + result.getResMsg());
		String str = (String) result.getParams().get("flowId");
		System.out.println(str);
	}

	@Test
	@Ignore
	public void findCardlimit() {
		// ResponseEntity<?> result =
		// finThirdPaymentService.findCardlimit("01020000",
		// SystemTypeEnums.QKD.getType(),
		// PlatformSourceEnums.PC.getType(), "XE",
		// PayChannelEnum.LianLian.getChannelKey());
		// System.out.println("状态：" + result.getResStatus() + "信息：" +
		// result.getResMsg());
	}

	/**
	 * 
	 * @Description : caoxb 方法描述:充值测试
	 * @Method_Name : rechargeCashTest
	 * @return : void
	 * @Creation Date : 2017年6月15日 下午4:10:50
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	@Test
	@Ignore
	public void rechargeCashTest() {
		RechargeCash cash = new RechargeCash();
		// APP 连连测试充值
		cash.setSystemTypeName(SystemTypeEnums.QKD.getType());
		cash.setPlatformSourceName(PlatformSourceEnums.IOS.getType());
		cash.setBankCard("6226220124517466");
		cash.setThirdAccount("2016032223774682");
		cash.setPayStyle(PayStyleEnum.RZ.getType());
		cash.setPayChannel(PayChannelEnum.LianLian.getChannelKey());
		// cash.setFlowType(03);
		// cash.setSubCode(1001);
		cash.setNoAgree("2016032223774682");
		cash.setBankCode("03050000");
		// cash.setTransMoney(BigDecimal.valueOf(1));
		// cash.setInfo("连连APP认证充值");
		cash.setUserId(13);
		// cash.setPayCardStyle(LianLianPayCardStyleEnum.RZDC.getValue());
		// PC 连连测试充值
		cash.setPlatformSourceName(PlatformSourceEnums.PC.getType());
		// cash.setInfo("连连认证PC充值");
		// WAP 连连测试充值
		cash.setPlatformSourceName(PlatformSourceEnums.WAP.getType());
		// cash.setInfo("连连认证WAP充值");
		// PC连连网银充值
		// cash.setPlatformSourceName(PlatformSourceEnums.PC.getType());
		// cash.setPayStyle(PayStyleEnum.WY.getType());
		// cash.setPayCardStyle(LianLianPayCardStyleEnum.WYDC.getValue());
		// cash.setInfo("连连PC网银充值");
		// PC 联动测试充值
		// cash.setPayChannel(PayChannelEnum.LianDong.getChannelKey());
		// cash.setInfo("联动PC认证充值");
		// cash.setPayNoticeUrl("");
		// ResponseEntity<?> responseEntity =
		// finConsumptionService.rechargeCash(cash);
		// System.out.println("状态：" + responseEntity.getResStatus() + "信息：" +
		// responseEntity.getResMsg());

	}

	@Test
	@Ignore
	public void rechargeCashLianDongTest() {
		RechargeCash cash = new RechargeCash();
		// APP 连连测试充值
		cash.setSystemTypeName(SystemTypeEnums.QKD.getType());
		cash.setPlatformSourceName(PlatformSourceEnums.PC.getType());
		cash.setBankCard("6226220124517466");
		cash.setThirdAccount("2016032223774682");
		cash.setPayStyle(PayStyleEnum.RZ.getType());
		cash.setPayChannel(PayChannelEnum.LianDong.getChannelKey());
		// cash.setTransMoney(new BigDecimal(10));
		// cash.setFlowType(03);
		// cash.setSubCode(1001);
		cash.setNoAgree("2016032223774682");
		cash.setBankCode("03050000");
		// cash.setTransMoney(BigDecimal.valueOf(1));
		// cash.setInfo("APP充值");
		cash.setUserId(13);
		// cash.setPayTyle(LianLianPayCardStyleEnum.RZDC.getValue());
		// PC 连连测试充值
		cash.setPlatformSourceName(PlatformSourceEnums.PC.getType());
		// cash.setInfo("PC充值");
		// WAP 连连测试充值

		// PC 联动测试充值
		// ResponseEntity<?> responseEntity =
		// finConsumptionService.rechargeCash(cash);
		// System.out.println("状态：" + responseEntity.getResStatus() + "信息：" +
		// responseEntity.getResMsg());

	}

	@Test
	@Ignore
	public void findAccountInfo() {
		FinTradeFlow finTradeFlow = new FinTradeFlow();
		finTradeFlow.setPflowId("biddcode");
		// finTradeFlow.setState(1);
		List<FinTradeFlow> list = finTradeFlowService.findByCondition(finTradeFlow);
		System.out.println(list.size());
	}

	@Test
	@Ignore
	public void updateAccountByUserId() {
		List<FinAccount> list = new ArrayList<FinAccount>();
		FinAccount finAccount = new FinAccount();
		finAccount.setRegUserId(1);
		finAccount.setState(5);

		FinAccount finAccount2 = new FinAccount();
		finAccount2.setRegUserId(13);
		finAccount2.setState(9);
		list.add(finAccount);
		list.add(finAccount2);
		int kk = finAccountService.updateFinAccountBatchByUserId(list, 2);
		System.out.println(kk);
	}

	@Test
	public void batchInsertTradeAndTransfer() {
		FinTradeFlow finTradeFlow = FinTFUtil.initFinTradeFlow(29, "TF160112017113009395078968648",
				new BigDecimal(1000), TradeTransferConstants.TRADE_TYPE_REPAY, PlatformSourceEnums.PC);

		List<FinFundtransfer> transfers = new ArrayList<FinFundtransfer>();
		FinFundtransfer fin = FinTFUtil.initFinFundtransfer(finTradeFlow.getFlowId(), 29, 1, new BigDecimal(100),
				TradeTransferConstants.getFundTransferSubCodeByType(FundtransferBigTypeStateEnum.INCOME,
						FundtransferSmallTypeStateEnum.CAPITAL));
		FinFundtransfer fin2 = FinTFUtil.initFinFundtransfer(finTradeFlow.getFlowId(), 29, 1, new BigDecimal(100),
				TradeTransferConstants.getFundTransferSubCodeByType(FundtransferBigTypeStateEnum.INCOME,
						FundtransferSmallTypeStateEnum.INTEREST));

		// FinFundtransfer fin3 =
		// FinTFUtil.initFinFundtransfer(finTradeFlow.getFlowId(), 4, 5, new
		// BigDecimal(900),
		// TradeTransferConstants.getFundTransferSubCodeByType(FundtransferBigTypeStateEnum.INCOME,
		// FundtransferSmallTypeStateEnum.CAPITAL));
		//
		// FinFundtransfer fin4 =
		// FinTFUtil.initFinFundtransfer(finTradeFlow.getFlowId(), 4, 5, new
		// BigDecimal(100),
		// TradeTransferConstants.getFundTransferSubCodeByType(FundtransferBigTypeStateEnum.INCOME,
		// FundtransferSmallTypeStateEnum.INTEREST));
		transfers.add(fin);
		transfers.add(fin2);
		// transfers.add(fin3);
		// transfers.add(fin4);
		ResponseEntity<?> result = finConsumptionService.updateAccountInsertTradeAndTransfer(finTradeFlow, transfers);
		// ResponseEntity<?> result2 =
		// finConsumptionService.rollBackUpdateAccountInsertTradeAndTransfer(finTradeFlow,
		// transfers);

		System.out.println(result.getResStatus() + "=======" + result.getResMsg());

	}

	@Test
	@Ignore
	public void deleRollBackFinAccount() {
		FinAccount finAccount = new FinAccount();
		finAccount.setRegUserId(12);
		finAccount.setNowMoney(BigDecimal.valueOf(-500));
		finAccount.setUseableMoney(BigDecimal.valueOf(-500));

		try {
			// finConsumptionService.rollBackAccountAndDelFlows(finAccount,
			// "TF2017072611241566023582");
		} catch (Exception e) {

			// TODO Auto-generated catch block
			e.printStackTrace();

		}
	}

	@Test
	public void findFintransferSumMoney() {
		FinFundtransfer finFundtransfer = new FinFundtransfer();
		finFundtransfer.setRegUserId(5);
		List<Integer> subCodeList = new ArrayList<Integer>();
		subCodeList.add(1011);
		finFundtransfer.setSubCodeList(subCodeList);
		BigDecimal money = finFundtransferService.findFintransferSumMoney(finFundtransfer);
		System.out.println(money);
	}

	@Test
	public void findPageAndIncomeTotalMoney() {
		FinFundtransfer finFundtransfer = new FinFundtransfer();
		finFundtransfer.setRegUserId(5);
		List<Integer> subCodeList = new ArrayList<Integer>();
		subCodeList.add(1011);
		finFundtransfer.setSubCodeList(subCodeList);
		Map<String, Object> map = finFundtransferService.findPageAndIncomeTotalMoney(finFundtransfer, null, "111");
		System.out.println(map);
	}

	@Test
	public void testFindBankCardInfoListByUserIds() {
		Set<Integer> list = new HashSet<>();
		list.add(33);
		Set<Integer> list1 = new HashSet<>();
		list1.add(34);
		list1.add(35);
		list.addAll(list1);
		Map<Integer, FinBankCard> result = finBankCardService.findBankCardInfoListByUserIds(list);
		System.out.println(result);
	}

	@Test
	public void cashPayBatch() {
		List<FinTradeFlow> list = new ArrayList<FinTradeFlow>();

		FinTradeFlow finTradeFlow = FinTFUtil.initFinTradeFlow(33, "1", new BigDecimal(100),
				TradeTransferConstants.TRADE_TYPE_INVEST, PlatformSourceEnums.PC);
		finTradeFlow.setTransferSubCode(TradeTransferConstants.TRANSFER_SUB_CODE_FREEZE);
		list.add(finTradeFlow);
		FinTradeFlow finTradeFlow2 = FinTFUtil.initFinTradeFlow(34, "1", new BigDecimal(100),
				TradeTransferConstants.TRADE_TYPE_INVEST, PlatformSourceEnums.PC);
		finTradeFlow2.setTransferSubCode(TradeTransferConstants.TRANSFER_SUB_CODE_FREEZE);
		list.add(finTradeFlow2);
		FinTradeFlow finTradeFlow3 = FinTFUtil.initFinTradeFlow(35, "1", new BigDecimal(100),
				TradeTransferConstants.TRADE_TYPE_INVEST, PlatformSourceEnums.PC);
		finTradeFlow3.setTransferSubCode(TradeTransferConstants.TRANSFER_SUB_CODE_FREEZE);
		list.add(finTradeFlow3);
		FinTradeFlow finTradeFlow4 = FinTFUtil.initFinTradeFlow(36, "1", new BigDecimal(100),
				TradeTransferConstants.TRADE_TYPE_INVEST, PlatformSourceEnums.PC);
		finTradeFlow4.setTransferSubCode(TradeTransferConstants.TRANSFER_SUB_CODE_FREEZE);
		list.add(finTradeFlow4);

		ResponseEntity<?> result = finConsumptionService.cashPayBatch(list);
		System.out.println(result);
	}

	@Test
	public void withDrawTest() {
		ResponseEntity<?> res = finPaymentFacade.clientWithDrawFacade("100", 35, PlatformSourceEnums.IOS, null,
				SystemTypeEnums.HKJF);
		System.out.println(res.getResStatus() + "========" + res.getResMsg());
	}

}
