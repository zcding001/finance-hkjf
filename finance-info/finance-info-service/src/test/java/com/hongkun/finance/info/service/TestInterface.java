package com.hongkun.finance.info.service;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.info.constant.InfomationConstants;
import com.hongkun.finance.info.enums.InfomationNewsEnum;
import com.hongkun.finance.info.model.InfoInformationNews;
import com.hongkun.finance.info.vo.InformationVo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/applicationContext-info.xml" })
public class TestInterface {
	@Autowired
	private InfoInformationNewsService infoInformationNewsService;
	@Reference
	private InfoBrowsingRecordService infoBrowsingRecordService;

	@Test
	@Ignore
	public void insertInfomations() {
		InformationVo informationCondition = new InformationVo();
		informationCondition.setKeyword("P2P,2553,28,中国,涉及");
		informationCondition.setChannel(InfomationConstants.CHANNEL_PC);
		informationCondition.setPosition(InfomationConstants.INFO_POSITION_FIRST_PAGE);
		informationCondition.setResideSelect(InfomationConstants.RESIDE_SELECT_YIRUN);
		informationCondition.setSort(1);
		informationCondition.setTitle("互联网金融风云再起");
		informationCondition.setType(InfomationNewsEnum.MEDIA_REPORT.getInfomationType());
		informationCondition.setContent(
				"截止2015年6月底，中国P2P网贷指数在选择的样本2553家P2P网贷平台中，涉及到28个省、市，平台数量前三名是广东省（507家）、山东省（383家）、北京市（287家），这三个省的1177家P2P网贷平台，超过了全国总数的46%。");
		informationCondition.setSource("新华社日报");
		int result = infoInformationNewsService.insertInfomationsNews(informationCondition);
		System.out.println(result);
	}

	@Test
	@Ignore
	public void updateInfomationsByID() {
		int result = infoInformationNewsService.updateInfomationStateById(1, InfomationConstants.INFO_VALID);
		System.out.println(result);
	}

	@Test
	@Ignore
	public void findInformationNewsByTypeAndState() {
		List<InfoInformationNews> result = infoInformationNewsService
				.findInformationNewsByTypeAndState(InfomationNewsEnum.MEDIA_REPORT, InfomationConstants.INFO_VALID);
		System.out.println(result.size());
	}

	@Test
	@Ignore
	public void updateInfomationsNews() {
		InformationVo informationCondition = new InformationVo();
		informationCondition.setInfomationId(1);
		informationCondition.setResideSelect(InfomationConstants.RESIDE_SELECT_HONGKUN);
		int result = infoInformationNewsService.updateInfomationsNews(informationCondition);
		System.out.println(result);
	}

	@Test
	@Ignore
	public void findInfoBrowsingRecordCount() {
		int result = infoBrowsingRecordService.findInfoBrowsingRecordCount(InfomationConstants.EULOGIZE_NUM, 1);
		int result2 = infoBrowsingRecordService.findInfoBrowsingRecordCount(InfomationConstants.EULOGIZE_NUM, 1, 12);
		System.out.println(result);
		System.out.println(result2);
	}

	@Test
	@Ignore
	public void findInformationByType() {
		// ResponseEntity<?> responseEntity = informationNewsFacade
		// .findInformationByType(InfomationNewsEnum.MEDIA_REPORT.getInfomationType(),
		// null);
		// System.out.println(responseEntity.getResStatus());
		// Pager pager = (Pager) responseEntity.getParams().get("pager");
		// System.out.println(pager.getData());
	}

	@Test
	// @Ignore
	public void eulogizeInformations() {
		// ResponseEntity<?> responseEntity =
		// informationNewsFacade.eulogizeInformations(1,
		// InfomationConstants.EULOGIZE_NUM, 3, 17);
		// System.out.println(responseEntity.getResStatus());
	}
}
