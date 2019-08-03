package com.hongkun.finance.invest.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.hongkun.finance.invest.model.BidInfo;
import com.hongkun.finance.invest.model.BidInfoDetail;
import com.hongkun.finance.invest.model.BidProduct;
import com.hongkun.finance.invest.model.vo.BidInfoVO;
import com.hongkun.finance.invest.service.BidInfoService;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.yirun.framework.core.commons.Constants.ERROR;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/applicationContext-invest.xml" })
public class TestBidInfoService {
	private static final Logger logger = LoggerFactory.getLogger(TestBidInfoService.class);
	@Reference
	private BidInfoService bidInfoService;

	/**
	 * 
	 * @Description : 校验标的信息
	 * @Method_Name : testValideInfo
	 * @return : void
	 * @Creation Date : 2017年7月6日 下午2:22:05
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	@Test
	@Ignore
	public void testValideInfo() {
		ResponseEntity<?> result = bidInfoService.validateBidInfo(5);
		if (result.getResStatus() == ERROR) {
			logger.error("ERROR");
		}
		BidInfo bidInfo = (BidInfo) result.getParams().get("bidInfo");
		logger.info("bidInfo:{}", bidInfo);
		BidInfoDetail bidInfoDetail = (BidInfoDetail) result.getParams().get("bidInfoDetail");
		logger.info("bidInfoDetail:{}", bidInfoDetail);
		BidProduct bidProduct = (BidProduct) result.getParams().get("bidProduct");
		logger.info("bidProduct:{}", bidProduct);
	}

	@Test
	@Ignore
	public void testbidProduct() {
		List<BidInfo> infos = bidInfoService.findBidInfoList(0, 6);
		for (BidInfo bidInfo : infos) {
			logger.info("bidInfo:{}", bidInfo);
		}
	}
	
	/**
	 *  @Description    : 用于修复使用多个foreach中只能使用$不能使用#的bug
	 *  @Method_Name    : testForeach
	 *  @return         : void
	 *  @Creation Date  : 2018年1月16日 下午2:50:55 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	@Test
	public void testForeach() {
		Pager pager = new Pager();
		BidInfoVO bidInfoVO = new BidInfoVO();
		bidInfoVO.setBidTypeLimitIds(Arrays.asList(0, 1, 2));
		bidInfoVO.setProductTypeLimitIds(Stream.iterate(1, i -> i + 1).limit(9).collect(Collectors.toList()));
		Pager result = this.bidInfoService.findBidInfoVOByCondition(bidInfoVO, pager);
		logger.info("{}", result);
	}

	@Test
	public void testFindBidInvestListByIdList() {
		Set<Integer> bidIdSet = new HashSet<>();
		bidIdSet.add(92);
		bidIdSet.add(94);
		Map<Integer, BidInfoVO> result = this.bidInfoService.findBidInfoDetailVoByIdList(bidIdSet);
		result.values().forEach((v) -> {
			logger.info(JSON.toJSONString(v));
		});
	}
}
