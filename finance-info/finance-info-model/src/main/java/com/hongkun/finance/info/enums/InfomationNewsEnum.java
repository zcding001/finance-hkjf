package com.hongkun.finance.info.enums;

/**
 * @Project : finance-info
 * @Program Name : com.hongkun.finance.info.enums.InfomationNewsEnum.java
 * @Class Name : InfomationNewsEnum.java
 * @Description : 资讯信息类型枚举类
 * @Author : yanbinghuang
 */
public enum InfomationNewsEnum {

	MEDIA_REPORT(1, "媒体报道"), COMPANY_NEWS(2, "平台资讯"), COMPANY_NOTICE(3, "公司公告"), FRIENDLY_URL(4, "友情链接"), ACTIVITY(5,
			"活动"), WELFARE(6, "福利"), RESEARCH_QUESTION(7, "调查问卷"), CAROUSEL_FIGURE(8,
					"轮播图"), ADVERTISEMENT(9, "广告"), FUNCTION_GUIDE(10, "功能指引"), AUDITING_INFO(11, "IOS资讯审核"),APP_MENU_BACKGROUND(18,"APP菜单背景图");

	private Integer infomationType;

	private String infomationsDes;

	private InfomationNewsEnum(Integer infomationType, String infomationsDes) {
		this.infomationsDes = infomationsDes;
		this.infomationType = infomationType;
	}

	public Integer getInfomationType() {
		return infomationType;
	}

	public void setInfomationType(Integer infomationType) {
		this.infomationType = infomationType;
	}

	public String getInfomationsDes() {
		return infomationsDes;
	}

	public void setInfomationsDes(String infomationsDes) {
		this.infomationsDes = infomationsDes;
	}
}
