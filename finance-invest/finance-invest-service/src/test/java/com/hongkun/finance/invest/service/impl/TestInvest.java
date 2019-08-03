package com.hongkun.finance.invest.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.alibaba.fastjson.JSON;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.invest.constants.InvestConstants;
import com.hongkun.finance.invest.facade.BidInvestFacade;
import com.hongkun.finance.invest.model.BidInvest;
import com.hongkun.finance.invest.service.BidInvestService;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.model.RegUserDetail;
import com.hongkun.finance.user.service.RegUserDetailService;
import com.hongkun.finance.user.service.RegUserService;
import com.yirun.framework.core.enums.PlatformSourceEnums;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.redis.JedisClusterUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/applicationContext-invest.xml" })
public class TestInvest {

	private static final Logger logger = LoggerFactory.getLogger(TestInvest.class);

	@Reference
	private BidInvestFacade bidInvestFacade;
	@Reference
	private RegUserService regUserService;
	@Reference
	private RegUserDetailService regUserDetailService;
	@Reference
	private BidInvestService bidInvestService;

	private int bidId = 31;
	private BigDecimal money = BigDecimal.valueOf(5000);

	@Test
	// @Ignore
	public void testInvest() {
//		Object obj = ApplicationContextUtils.getBean(VasCouponDetailService.class);
//		Class<?>[] arr = obj.getClass().getInterfaces();
		logger.info("================ invest start ============");
		RegUser regUser = this.regUserService.findRegUserById(33);
		RegUserDetail regUserDetail = this.regUserDetailService.findRegUserDetailByRegUserId(33);
		JedisClusterUtils.setAsJson(RegUserDetail.class.getSimpleName() + 33, regUserDetail);
		ResponseEntity<?> result = this.bidInvestFacade.invest(regUser, -1, -1, BigDecimal.valueOf(10000), 115, 1101, 1,PlatformSourceEnums.PC);
		logger.info("\n响应结果\n:{}", result);
		logger.info("================ invest end ============");
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
//	@Ignore
	public void testSaveInvest() {
		BidInvest bidInvest = new BidInvest();
		bidInvest.setInvestAmount(BigDecimal.valueOf(100));

		bidInvestService.insertBidInvest(bidInvest);

		logger.info("================ invest end ============");
		logger.info("================ invest end ============");
	}


	@Test
	@Ignore
	public void testInvest1() {
		List<Integer> investState = new ArrayList<Integer>();
		investState.add(InvestConstants.INVEST_STATE_SUCCESS);// 投资成功
		investState.add(InvestConstants.INVEST_STATE_AUTO);// 自动转让
		// 查询对应活期标的投资记录， 获取第三方投资记录
		BidInvest thirdBiddInvest = bidInvestService.findInvestRecord(9, investState);
		System.out.println(thirdBiddInvest);
	}
	
	
	@Test
//	@Ignore
	public void testAuotInvest() {
		this.bidInvestFacade.autoInvest();
		try {
			Thread.sleep(1000000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		List<String> list = new ArrayList<>(Arrays.asList(null, null, null, null));
		list.add(3, "a");
		System.out.println(list);
	}

	@Test
	public void testFindBidInvestListByIdList(){
		List<Integer> param = new ArrayList<>();
		param.add(116);
		param.add(117);
		List<BidInvest> list = bidInvestService.findBidInvestListByIdList(param);
		logger.info(JSON.toJSONString(list));
	}
}
