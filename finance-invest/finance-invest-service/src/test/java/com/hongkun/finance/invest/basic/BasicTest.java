package com.hongkun.finance.invest.basic;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.hongkun.finance.invest.model.BidInvest;
import com.hongkun.finance.invest.service.BidInvestService;

@ContextConfiguration(locations = { "classpath:spring/applicationContext-invest.xml",
		"classpath:spring/applicationContext-dubbo-provider.xml" })
public class BasicTest extends AbstractTransactionalJUnit4SpringContextTests {
	@Autowired
	private BidInvestService bidInvestService;

	@Test
	@Ignore
	public void testInvests() {
		List<BidInvest> s = bidInvestService.findInvests(0, 6, null);
		for (BidInvest bidInvest : s) {
			logger.info("结果：" + bidInvest.getBidInfoId());
		}

		BigDecimal sumA = bidInvestService.findInvestSumAmount(0, 6, 12);
		logger.info("结果：" + sumA);
	}
}
