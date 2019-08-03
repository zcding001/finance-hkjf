package com.hongkun.finance.api.controller.qdz;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.hongkun.finance.api.controller.BaseControllerTest;

public class QdzControllerTest extends BaseControllerTest {

	private static String QDZ_TEST_PATH = "qdzController/";

	/**
	 * @Description :钱袋子转入
	 * @Method_Name : searchBankCardBin;
	 * @throws Exception
	 * @return : void;
	 * @Creation Date : 2018年3月21日 上午9:13:40;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@Test
	public void testQdzTransferIn() throws Exception {
		Map<String, String> params = new HashMap<>();
		params.put("money", "1000");// 金额
		params.put("source", "11");// 平台来源
		super.doTest(QDZ_TEST_PATH + "qdzTransferIn", params);
	}

	/**
	 * @Description : 钱袋子转出
	 * @Method_Name : qdzTransferOut;
	 * @throws Exception
	 * @return : void;
	 * @Creation Date : 2018年3月21日 上午9:13:02;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@Test
	public void testQdzTransferOut() throws Exception {
		Map<String, String> params = new HashMap<>();
		params.put("money", "100");// 金额
		params.put("source", "11");// 平台来源
		super.doTest(QDZ_TEST_PATH + "qdzTransferOut", params);
	}

	/**
	 * 
	 * @Description : 获取首页和产品页钱袋子信息
	 * @Method_Name : testQdzfindQdzInfo
	 * @throws Exception
	 * @return : void
	 * @Creation Date : 2018年3月26日 下午1:51:30
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	@Test
	public void testQdzfindQdzInfo() throws Exception {
		super.doTest(QDZ_TEST_PATH + "findQdzInfo");
	}

	/**
	 * 
	 * @Description : 获取我的钱袋子信息
	 * @Method_Name : testFindMyQdzInfo
	 * @throws Exception
	 * @return : void
	 * @Creation Date : 2018年3月26日 下午1:51:30
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	@Test
	public void testFindMyQdzInfo() throws Exception {
		super.doTest(QDZ_TEST_PATH + "findMyQdzInfo");
	}

	/**
	 * 
	 * @Description : 获取我的钱袋子交易记录
	 * @Method_Name : testGetQdzTradeRecord
	 * @throws Exception
	 * @return : void
	 * @Creation Date : 2018年3月26日 下午1:51:30
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	@Test
	public void testGetQdzTradeRecord() throws Exception {
		Map<String, String> params = new HashMap<>();
		params.put("type", "0");// 全部查询
		params.put("startTime", "");
		params.put("endTime", "");
		params.put("maxMoney", "1000");
		params.put("minMoney", "100");
		params.put("currentPage", "1");
		params.put("pageSize", "20");
		super.doTest(QDZ_TEST_PATH + "getQdzTradeRecord", params);
	}

	/**
	 * 
	 * @Description : 计算转出手续费
	 * @Method_Name : testCalculateTransferOutFee
	 * @throws Exception
	 * @return : void
	 * @Creation Date : 2018年3月26日 下午1:51:30
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	@Test
	public void testCalculateTransferOutFee() throws Exception {
		Map<String, String> params = new HashMap<>();
		params.put("money", "100");// 金额
		params.put("type", "0");// 0 转出到可用余额 1转出到银行卡
		super.doTest(QDZ_TEST_PATH + "calculateTransferOutFee", params);
	}

	/**
	 * 
	 * @Description : 转出到银行卡
	 * @Method_Name : transferOutToBank
	 * @throws Exception
	 * @return : void
	 * @Creation Date : 2018年3月26日 下午1:51:30
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	@Test
	public void testTransferOutToBank() throws Exception {
		Map<String, String> params = new HashMap<>();
		params.put("money", "1000");// 金额
		params.put("source", "11");// IOS("IOS", 11), ANDROID("ANDROID", 12)
		super.doTest(QDZ_TEST_PATH + "transferOutToBank", params);
	}
}
