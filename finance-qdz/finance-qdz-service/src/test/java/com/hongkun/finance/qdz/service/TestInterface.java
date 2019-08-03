package com.hongkun.finance.qdz.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.qdz.constant.QdzConstants;
import com.hongkun.finance.qdz.enums.TransTypeEnum;
import com.hongkun.finance.qdz.facade.QdzTaskJobFacade;
import com.hongkun.finance.qdz.model.QdzAccount;
import com.hongkun.finance.qdz.model.QdzInterestDayDetail;
import com.hongkun.finance.qdz.model.QdzTransRecord;
import com.hongkun.finance.qdz.utils.QdzDateUtils;
import com.hongkun.finance.qdz.vo.QdzInterestDetailInfoVo;
import com.hongkun.finance.qdz.vo.QdzTransferInfo;
import com.hongkun.finance.user.constants.UserConstants;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.DateUtils;
import com.yirun.framework.core.utils.PropertiesHolder;
import com.yirun.framework.core.utils.pager.Pager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext-qdz.xml"})
public class TestInterface {
	@Reference
	private QdzTransRecordService qdzTransRecordService;
	@Reference
	private QdzTransferService qdzTransferService;
	@Autowired
	private QdzAccountService qdzAccountService;
	@Reference
	private QdzInterestDayService qdzInterestDayService;
	@Reference
	private QdzInterestDayDetailService qdzInterestDayDetailService;
	@Autowired
	private QdzTaskJobFacade qdzTaskJobFacade;

	@Test
	public void findQdzAccountByRegUserId() {
		QdzAccount qdzAccount = qdzAccountService.findQdzAccountByRegUserId(12);
		System.out.println(
				qdzAccount.getRegUserId() + "===" + qdzAccount.getMoney() + "===" + qdzAccount.getYedInterest());
	}

	@Test

	public void findSumFailICreditorMoney() {
		QdzAccount qdzAccount = new QdzAccount();
		qdzAccount.setState(1);
		BigDecimal sumMoney = qdzAccountService.findSumFailICreditorMoney(qdzAccount);
		System.out.println(sumMoney);
	}

	@Test
	@Ignore
	public void findInvestSumMoney() {
		Date startTime = QdzDateUtils.format(new Date(), null, -1, PropertiesHolder.getProperty("qdz_income_time"));
		Date endTime = QdzDateUtils.format(new Date(), null, 0, PropertiesHolder.getProperty("qdz_income_time"));
		BigDecimal totalMoney = qdzTransRecordService.findInvestSumMoney(startTime, endTime, TransTypeEnum.PAYIN,
				QdzConstants.TRANS_RECORD_SUCCSS);
		System.out.println(totalMoney);
	}

	@Test
	@Ignore
	public void findSumTransMoneyOfDay() {
		BigDecimal totalMoney = qdzTransRecordService.findSumTransMoneyOfDay(12, 1);
		System.out.println(totalMoney);
	}

	@Test
	@Ignore
	public void findQdzTransRecordByRegUserId() {
		QdzTransRecord qdzTransRecord = qdzTransRecordService.findQdzTransRecordById(1);
		QdzTransRecord qdzTransRecord2 = qdzTransRecordService.findQdzTransRecordByRegUserId(12);
		System.out.println(qdzTransRecord);
		System.out.println(qdzTransRecord2);
	}

	@Test
	@Ignore
	public void findTransferInTimesOfMonth() {
		Integer num = qdzTransRecordService.findTransferInTimesOfMonth(12, TransTypeEnum.PAYIN.getValue());
		System.out.println(num);
	}

	@Test
	@Ignore
	public void findQdzInterestDayDetailInfo() {
		QdzInterestDetailInfoVo qdzInterestDetailInfo = new QdzInterestDetailInfoVo();
		qdzInterestDetailInfo.setDay(
				QdzDateUtils.format(new Date(), DateUtils.DATE, 0, PropertiesHolder.getProperty("qdz_income_time")));
		qdzInterestDetailInfo.setUserFlag(QdzConstants.PLAT_USER_FLAG);
		qdzInterestDetailInfo.setThirdRegUserId(UserConstants.PLATFORM_ACCOUNT_ID);
		/*QdzInterestDayDetail qdzInfo = qdzInterestDayDetailService.findQdzInterestDayDetailInfo(qdzInterestDetailInfo);
		System.out.println(qdzInfo);*/
	}

	@Test
	@Ignore
	public void findQdzTransferRecordList() {
		QdzTransferInfo qdzTransferInfo = new QdzTransferInfo();
		qdzTransferInfo.setType(1);
		qdzTransferInfo.setState(1);
		Pager qdzInfo = qdzTransRecordService.findQdzTransferRecordList(qdzTransferInfo, null);
		System.out.println(qdzInfo);
	}

	@Test
	@Ignore
	public void testUpdateCreditorMoney() {
		try {
			int r = qdzAccountService.updateCreditorMoney(13, BigDecimal.valueOf(100));
			System.out.println("更新结果=" + r);
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	@Test
	@Ignore
	public void testFindInterestDayDetails() {
		try {
			QdzInterestDayDetail dct = new QdzInterestDayDetail();
			dct.setDay(DateUtils.parse("2017-08-09 00:00:00", "yyyy-MM-dd HH:mm:ss"));
			List<QdzInterestDayDetail> s = qdzInterestDayDetailService.findSuccQdzInterestDayDetails(dct);
			for (QdzInterestDayDetail qdzInterestDayDetail : s) {
				System.out.println(qdzInterestDayDetail.getMoney());
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	@Test
	@Ignore
	public void testRepairCalcInterest() {
		try {
			String qdzInterestDayIds = "254,255";
			qdzTaskJobFacade.repairCalcInterest(qdzInterestDayIds);
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	@Test
	@Ignore
	public void testfindVasRebatesRuleByTypeAndState() {
		try {
			String qdzInterestDayIds = "254,255";
			qdzTaskJobFacade.repairCalcInterest(qdzInterestDayIds);
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	@Test
	public void testQdzRule() {
		qdzTaskJobFacade.qdzRateRecord();
	}

	@Test
	@Ignore
	public void testFindQdzAccountByShardingItem() {
		List<QdzAccount> accounts = qdzAccountService.findQdzAccountByShardingItem(1);

		System.out.println(accounts.isEmpty());
	}

	@Test
	@Ignore
	public void testFindQdzInterestDayDetailsByShardingItem() {
		List<QdzInterestDayDetail> dayDetails = qdzInterestDayDetailService.findQdzInterestDayDetailsByShardingItem(
				DateUtils.getFirstTimeOfDay(DateUtils.addDays(DateUtils.getCurrentDate(), -1)), 5);
		System.out.println(dayDetails.isEmpty());
	}

	@Test
	public void findQdzReceiptInfo() {
		ResponseEntity<?> result = (ResponseEntity<?>) qdzTransferService.findQdzReceiptInfo(32);
		Map<String, Object> map = result.getParams();
		System.out.println(map);
	}
	@Test
	public void testQdzMatch() {
		qdzTaskJobFacade.creditorMatch(new Date());
	}
}
