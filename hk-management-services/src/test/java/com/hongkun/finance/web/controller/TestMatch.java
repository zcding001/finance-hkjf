package com.hongkun.finance.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.invest.constants.InvestConstants;
import com.hongkun.finance.invest.facade.BidInvestFacade;
import com.hongkun.finance.invest.model.BidInfo;
import com.hongkun.finance.invest.model.BidInfoDetail;
import com.hongkun.finance.invest.model.BidInvest;
import com.hongkun.finance.invest.service.BidInfoService;
import com.hongkun.finance.invest.service.BidInvestService;
import com.hongkun.finance.loan.constants.RepayConstants;
import com.hongkun.finance.loan.facade.MakeLoanFacade;
import com.hongkun.finance.loan.facade.RepayFacade;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.service.RegUserService;
import com.yirun.framework.core.enums.PlatformSourceEnums;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.DateUtils;
import com.yirun.framework.jms.JmsService;
import com.yirun.framework.jms.enums.DestinationType;
import com.yirun.framework.jms.enums.JmsMessageType;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.lang.System.out;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:springmvc-config.xml" })
public class TestMatch {

	private static final Logger logger = LoggerFactory.getLogger(TestMatch.class);

	@Reference
	private BidInvestFacade bidInvestFacade;
	@Reference
	private RegUserService regUserService;
	@Reference
	private MakeLoanFacade makeLoanFacade;
	@Reference
	private BidInvestService bidInvestService;
	@Reference
	private BidInfoService bidInfoService;
	@Reference
	private RepayFacade repayFacade;
	@Autowired
	private JmsService jmsService;

	@Test
	 @Ignore
	public void testInvest() {
		logger.info("================ invest start ============");
		RegUser regUser = this.regUserService.findRegUserById(5);
		//ResponseEntity<?> result = this.bidInvestFacade.invest(regUser, -1, -1, BigDecimal.valueOf(10000), 3, TradeTransferConstants.TRADE_TYPE_INVEST, PlatformSourceEnums.PC);
		//logger.info("\n响应结果\n:{}", result);
		logger.info("================ invest end ============");
	}

	@Test
	@Ignore
	public void testLending() {
		out.println("################# lending....start #################");
		// for (int i = 20; i <= 21; i++) {
//		ResponseEntity<?> result = makeLoanFacade.makeLoans(2);
//		logger.info("\n响应结果\n:{}", result);
		// }
		out.println("################# lending....end #################");
	}

	@Test
	@Ignore
	public void testMatch() {
		out.println("################# lending....start #################");
		List<Integer> list = new ArrayList<>();
		list.add(30);
		ResponseEntity<?> result = this.bidInvestFacade.match(26, list, 2, 2);
		logger.info("\n响应结果\n:{}", result);
		out.println("################# lending....end #################");
	}

	@Test
	@Ignore
	public void testBatchInvest() {
		List<BidInvest> list = new ArrayList<>();
		BidInvest bidInvest = new BidInvest();
		bidInvest.setInvestAmount(BigDecimal.valueOf(100));
		BidInvest bidInvest1 = new BidInvest();
		bidInvest1.setInvestAmount(BigDecimal.valueOf(100));
		list.add(bidInvest);
		list.add(bidInvest1);
		bidInvestService.insertBidInvestBatch(list, list.size());
		logger.info("================ invest end ============");
		for (BidInvest tmp : list) {
			logger.info("\n插入后的投资记录：\n{}", tmp);
		}
		logger.info("================ invest end ============");
	}

	@Test
	@Ignore
	public void testDoRepay() {
		int repayId = 10;
		// BigDecimal capital = BigDecimal.valueOf(6000);
		BigDecimal capital = BigDecimal.ZERO;
		int withHoldflag = 0;
		RegUser regUser = this.regUserService.findRegUserById(3);
		ResponseEntity<?> result = repayFacade.repay(repayId, capital, withHoldflag, regUser, PlatformSourceEnums.PC);
		logger.info("\n还款结果:\n{}", result);
		logger.info("======================================");
	}

	@Test
	 @Ignore
	public void testAddBid() {
		BidInfo bidInfo = new BidInfo();
		bidInfo.setName("testTcc001");
		bidInfo.setBidProductId(1);
		bidInfo.setTotalAmount(BigDecimal.valueOf(10000));
		bidInfo.setResidueAmount(BigDecimal.valueOf(10000));
		bidInfo.setInterestRate(BigDecimal.valueOf(10));
		bidInfo.setTermValue(3);
		bidInfo.setBiddRepaymentWay(2);// 按月付息到期还本
		bidInfo.setServiceRate(BigDecimal.valueOf(1));
		// bidInfo.setState(InvestContants.BID_STATE_WAIT_LOAN);
		bidInfo.setState(InvestConstants.BID_STATE_WAIT_INVEST);
		bidInfo.setBorrowerId(3);
		bidInfo.setEndTime(DateUtils.addMonth(new Date(), 1));
		BidInfoDetail detail = new BidInfoDetail();
		detail.setAdvanceRepayState(0);
		detail.setReserveInterest(0);// 预留利息
		detail.setWithholdState(0);// 同意代扣
		this.bidInfoService.insertBidInfoWithBidDetail(bidInfo, detail);
		System.out.println("okok...");
	}
	
	@Test
	public void testMqConsumerInit() {
		jmsService.sendMsg(RepayConstants.MQ_QUEUE_REPAYANDRECEIPTPLAN + "zcding", DestinationType.QUEUE, 
				2,JmsMessageType.OBJECT);
	}
}
