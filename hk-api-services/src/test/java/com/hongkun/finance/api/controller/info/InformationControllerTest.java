package com.hongkun.finance.api.controller.info;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.hongkun.finance.api.controller.BaseControllerTest;
import com.hongkun.finance.info.model.InfoQuestionnaireAnswer;
import com.yirun.framework.core.utils.json.JsonUtils;

public class InformationControllerTest extends BaseControllerTest {
	/**
	 * @Description : 通过资讯类型，位置，交易渠道查询资讯相关信息
	 * @Method_Name : searchInformationByType;
	 * @throws Exception
	 * @return : void;
	 * @Creation Date : 2018年3月21日 上午9:18:10;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@Test
	public void searchInformationByType() throws Exception {
		Map<String, String> params = new HashMap<>();
		params.put("type", "8");// 资讯类型 类型 1-媒体报道 2-公司动态 3-公司公告 4-友情链接 5-活动
								// 6-(征文)福利 7-调查问卷 8-轮播图 9-广告10-功能指引
		params.put("position", "1");// 位置 位置 0-其它 1-首页 2-积分商城
		params.put("channel", "2");// 渠道 交易渠道1-PC 2-IOS 3-ANDRIOD 4-WAP
		super.doTest("informationController/searchInformationByType", params);
	}

	/**
	 * @Description : 根据资讯类型，位置，渠道，分页查询资讯相关信息
	 * @Method_Name : listInformationRecord;
	 * @throws Exception
	 * @return : void;
	 * @Creation Date : 2018年3月21日 上午9:19:40;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@Test
	public void listInformationRecord() throws Exception {
		Map<String, String> params = new HashMap<>();
		params.put("currentPage", "1");// 当前页
		params.put("pageSize", "5");// 每页显示多少条
		params.put("type", "3");// 资讯类型 类型 1-媒体报道 2-公司动态 3-公司公告 4-友情链接 5-活动
								// 6-(征文)福利 7-调查问卷 8-轮播图 9-广告10-功能指引
		params.put("position", "1");// 位置 位置 0-其它 1-首页 2-积分商城
		params.put("channel", "2");// 渠道 交易渠道1-PC 2-IOS 3-ANDRIOD 4-WAP
		super.doTest("informationController/listInformationRecord", params);
	}

	/**
	 * @Description : 通过资ID，查询资讯详情
	 * @Method_Name : searchInfomationsById;
	 * @throws Exception
	 * @return : void;
	 * @Creation Date : 2018年3月21日 上午9:22:28;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@Test
	public void searchInfomationsById() throws Exception {
		Map<String, String> params = new HashMap<>();
		params.put("id", "1");// 资讯ID
		super.doTest("informationController/searchInfomationsById", params);
	}

	/**
	 * @Description : 用户意见提交
	 * @Method_Name : saveInfoFeedback;
	 * @throws Exception
	 * @return : void;
	 * @Creation Date : 2018年3月21日 上午9:24:42;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@Test
	public void saveInfoFeedback() throws Exception {
		Map<String, String> params = new HashMap<>();
		params.put("content", "11111111111111111");// 意见内容
		params.put("source", "2");// 来源
		super.doTest("informationController/saveInfoFeedback", params);
	}

	/**
	 * @Description : 对某条资讯进行浏览，点赞
	 * @Method_Name : plusInfomationsBrowsing;
	 * @throws Exception
	 * @return : void;
	 * @Creation Date : 2018年3月21日 上午9:25:46;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@Test
	public void plusInfomationsBrowsing() throws Exception {
		Map<String, String> params = new HashMap<>();
		params.put("id", "48");// 资讯ID
		params.put("source", "2");// 来源 交易渠道1-PC 2-IOS 3-ANDRIOD 4-WAP
		params.put("type", "1");// 类型 1-浏览 2-点赞
		super.doTest("informationController/plusInfomationsBrowsing", params);
	}

	/**
	 * @Description : 调查问卷详情页
	 * @Method_Name : findInfoQuesetionById;
	 * @throws Exception
	 * @return : void;
	 * @Creation Date : 2018年3月21日 上午9:27:21;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@Test
	public void findInfoQuesetionById() throws Exception {
		Map<String, String> params = new HashMap<>();
		params.put("informationNewsId", "45");// 资讯ID
		super.doTest("informationController/findInfoQuesetionById", params);
	}

	@Test
	public void saveInfoQuestionnaireAnswer() throws Exception {
		Map<String, String> params = new HashMap<>();
		List<InfoQuestionnaireAnswer> list = new ArrayList<InfoQuestionnaireAnswer>();
		InfoQuestionnaireAnswer answer = new InfoQuestionnaireAnswer();
		answer.setInfoQuestionnaireId(2);
		answer.setAnswer("A");
		answer.setSource(2);
		InfoQuestionnaireAnswer answer1 = new InfoQuestionnaireAnswer();
		answer1.setInfoQuestionnaireId(4);
		answer1.setAnswer("A,B");
		answer1.setSource(2);
		InfoQuestionnaireAnswer answer2 = new InfoQuestionnaireAnswer();
		answer2.setInfoQuestionnaireId(9);
		answer2.setAnswer("金服项目");
		answer2.setSource(2);
		list.add(answer);
		list.add(answer1);
		list.add(answer2);

		params.put("questionnaireAnswer", JsonUtils.toJson(list));// 资讯ID
		super.doTest("informationController/saveInfoQuestionnaireAnswer", params, 78);
	}

}
