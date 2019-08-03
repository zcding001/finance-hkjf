package com.hongkun.finance.vas.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.hongkun.finance.invest.model.BidInvest;
import com.hongkun.finance.invest.service.BidInvestService;
import com.hongkun.finance.vas.constants.VasConstants;
import com.hongkun.finance.vas.enums.VasRuleTypeEnum;
import com.hongkun.finance.vas.facade.RecommendEarnFacade;
import com.hongkun.finance.vas.model.RcommendEarnInfo;
import com.hongkun.finance.vas.model.RecommendEarnBuild;
import com.hongkun.finance.vas.model.VasBidRecommendEarn;
import com.hongkun.finance.vas.model.VasRebatesRule;
import com.hongkun.finance.vas.model.VasSimRecord;
import com.hongkun.finance.vas.model.vo.VasVipGrowRecordMqVO;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.jms.JmsService;
import com.yirun.framework.jms.enums.DestinationType;
import com.yirun.framework.jms.enums.JmsMessageType;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.jms.JMSException;
import java.math.BigDecimal;
import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/applicationContext-vas.xml" })
public class TestInterface extends Thread {
	@Reference
	private VasBidRecommendEarnService vasBiddRecommendEarnService;
	@Reference
	private VasSimRecordService vasSimRecordService;
	@Reference(timeout = 5000)
	private VasRebatesRuleService vasRebatesRuleService;
	@Reference
	private VasVipGrowRecordService vasVipGrowRecordService;
	@Reference
	private BidInvestService bidInvestService;
	@Reference
	private RecommendEarnFacade recommendEarnFacade;

	@Test
	@Ignore
	public void findVasBiddRecommendEarnList() {

		// regUserId,stateList, recommendRegUserId, biddIdList,state,
		// createTimeBegin,createTimeEnd
		// biddIdList
		List<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(222222);
		list.add(333333);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("recommendRegUserId", 1);
		map.put("state", 0);
		map.put("biddIdList", list);
		Pager pager = new Pager();
		Pager pagerInfo = vasBiddRecommendEarnService.findVasBidRecommendEarnListByInfo(map, pager);
		System.out.println(pagerInfo.getData().size());
	}

	@Test
	@Ignore
	public void saveRecommendRecordInfo() {
		RcommendEarnInfo bidRcommendEarnInfo = new RcommendEarnInfo();
		bidRcommendEarnInfo.setFriendLevel(1);
		bidRcommendEarnInfo.setInvestAmount(new BigDecimal(10000));
		bidRcommendEarnInfo.setRegUserId(1234567890);
		bidRcommendEarnInfo.setRecommendRegUserId(543210);
		bidRcommendEarnInfo.setResourceId(123);
		bidRcommendEarnInfo.setState(1);
		bidRcommendEarnInfo.setType(0);
		bidRcommendEarnInfo.setBiddId(1256);
		bidRcommendEarnInfo.setInvestNum(100);
		ResponseEntity<?> responseEntity = vasBiddRecommendEarnService.insertVasBidRecommendEarn(bidRcommendEarnInfo);
		System.out.println(responseEntity.getResStatus());
	}

	@Test
	@Ignore
	public void findVasSimRecord() {
		ResponseEntity<?> responseEntity = vasSimRecordService.findVasSimRecordByRegUserId(13);
		System.out.println(responseEntity.getResStatus());
		Map<String, Object> map = responseEntity.getParams();

		System.out.println(map.get("vasSimRecordList"));
		System.out.println(map.get("totalMoney"));

		vasSimRecordService.updateBatchVasSimRecordById(3, (List<VasSimRecord>) map.get("vasSimRecordList"));
	}

	@Test
	@Ignore
	public void findVasRebatesRuleByTypeAndState() {
		VasRebatesRule vasRebatesRule = vasRebatesRuleService.findVasRebatesRuleByTypeAndState(5, 1);
		System.out.println(vasRebatesRule.getContent());
	}

	@Test
	@Ignore
	public void insertVasRebatesRulee() {
		String content = "{\"inOPPPerMonth\":100,\"inMaxMoneyPPPD\":100000,\"inPayRate\":6.0,\"outOPPPerMonth\":9000,\"outMaxMoneyPPPD\":10000,\"outPayRate\":4.0,\"currInterestRate\":5.0,\"investLowest\":100,\"holdInvestMax\":80000,\"noInOutStartTimes\":\"08:30\",\"noInOutEndTimes\":\"15:30\"}";
		// String content = "80000";
		int kk = vasRebatesRuleService.insertVasRebatesRule(content, VasRuleTypeEnum.QDZRECOMMONEY, null, null);
		System.out.println(kk);
	}

	@Test
	// @Ignore
	public void testUpdateCreditorMoney() {
		ResponseEntity<?> result = vasRebatesRuleService.updateCreditorMoney(BigDecimal.valueOf(1000));
		System.out.println(result.getResStatus());
	}

	@Test
	@Ignore
	public void findSimGoldCountInfo() {
		ResponseEntity<?> result = vasSimRecordService.findSimGoldCountInfo();
		System.out.println(result.getResStatus());
	}

	@Test
	@Ignore
	public void findBidRecomCountInfo() {
		VasBidRecommendEarn vasBiddRecommendEarn = new VasBidRecommendEarn();
		vasBiddRecommendEarn.setFriendLevel(1);
		vasBiddRecommendEarn.setRecommendRegUserId(12);
		ResponseEntity<?> result = vasBiddRecommendEarnService.bidRecommendEarnInfoCount(vasBiddRecommendEarn);
		Map<String, Object> map = (Map<String, Object>) result.getResMsg();

		Integer mm = Integer.parseInt(map.get("sumFriendLevel").toString());
		System.out.println(mm);
		// System.out.println((Integer) map.get("sumFriendLevel"));
		// System.out.println((BigDecimal) map.get("sumEarnAmount"));
		System.out.println(result.getResStatus());
	}

	@Test
	@Ignore
	public void insertVasVipGrowRecord() {
		// 起多线程进行并发测试
		for (int i = 0; i < 10; i++) {
			new Thread() {
				@Override
				public void run() {
					VasVipGrowRecordMqVO vasVipGrowRecordMqVO = new VasVipGrowRecordMqVO();
					vasVipGrowRecordMqVO.setGrowType(3);
					vasVipGrowRecordMqVO.setUserId(32);
					vasVipGrowRecordMqVO.setInvestMoney(5000d);
					vasVipGrowRecordMqVO.setInvestDay(30);
					try {
						vasVipGrowRecordService.insertVasVipGrowRecord(new Date(), vasVipGrowRecordMqVO);
					} catch (JMSException e) {
						e.printStackTrace();
					}
				}
			}.start();
		}
		try {
			TestInterface.sleep(600000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// VasVipGrowRecordMqVO vasVipGrowRecordMqVO = new
		// VasVipGrowRecordMqVO();
		// vasVipGrowRecordMqVO.setGrowType(3);
		// vasVipGrowRecordMqVO.setUserId(32);
		// vasVipGrowRecordMqVO.setInvestMoney(5000d);
		// vasVipGrowRecordMqVO.setInvestDay(30);
		// vasVipGrowRecordService.insertVasVipGrowRecord(new
		// Date(),vasVipGrowRecordMqVO);
	}

	@Test
	@Ignore
	public void findSimSumMoney() {
		VasSimRecord vasSimRecord = new VasSimRecord();
		vasSimRecord.setRegUserId(1);
		BigDecimal money = vasSimRecordService.findSimSumMoney(vasSimRecord);
		System.out.println(money);
	}

	public static void main(String[] args) {
		System.out.println(VasRuleTypeEnum.QDZ.getValue());
	}

	@Test
	public void sendMqRecommend() {
		List<BidInvest> bidInvestList = bidInvestService.findBidInvestListByBidId(110);
		RecommendEarnBuild recommendEarnBuild = new RecommendEarnBuild();
		recommendEarnBuild.setVasRuleTypeEnum(VasRuleTypeEnum.RECOMMEND);
		recommendEarnBuild.setRecommendRecordList(bidInvestList);
		recommendEarnFacade.createRecommendRecordInfo(recommendEarnBuild);
	}

}
