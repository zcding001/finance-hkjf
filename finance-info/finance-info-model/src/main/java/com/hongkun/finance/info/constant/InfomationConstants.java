package com.hongkun.finance.info.constant;

/**
 * @Project : finance-info
 * @Program Name : com.hongkun.finance.info.constant.InfomationConstants.java
 * @Class Name : InfomationConstants.java
 * @Description : 资讯信息常量类
 * @Author : yanbinghuang
 */
public class InfomationConstants {
	/******************* 资讯信息表状态 *************************/
	/** 资讯信息表状态 删除 0 **/
	public static int INFO_DEL = 0;
	/** 资讯信息表状态 正常 1 **/
	public static int INFO_NORMAL = 1;
	/** 资讯信息表状态 有效 2 **/
	public static int INFO_VALID = 2;
	/** 资讯信息表状态 无效 3 **/
	public static int INFO_INVALID = 3;

	/********************* 资讯记录类型 *************************************/
	/** 资讯记录 类型 1 浏览 **/
	public static int BROWSE_NUM = 1;
	/** 资讯记录 类型 2 点赞 **/
	public static int EULOGIZE_NUM = 2;
	/** PC渠道 **/
	public static String CHANNEL_PC = "1";
	/** 手机渠道 ios **/
	public static String CHANNEL_IOS = "2";
	/** 手机渠道 ANDRIOD **/
	public static String CHANNEL_ANDRIOD = "3";
	/** 手机渠道 WAP **/
	public static String CHANNEL_WAP = "4";
	/** 资讯位置 0-其它 **/
	public static int INFO_POSTION_OTHER = 0;
	/** 资讯位置 1-首页 **/
	public static int INFO_POSITION_FIRST_PAGE = 1;
	/** 资讯位置 2-积分商城 **/
	public static int INFO_POSITION_POINT = 2;
	/** 资讯位置 3-私募基金 **/
	public static int INFO_POSITION_PE = 3;
	/** 资讯位置 4-海外基金 **/
	public static int INFO_POSITION_OVERSEA_FUND = 4;
	/** 资讯位置 5-信托产品 **/
	public static int INFO_POSITION_FOT = 5;
	/** 资讯位置 6-投资 **/
	public static int INFO_POSITION_INVEST = 6;
	/** 所属版块 1-亿润投资集团 **/
	public static int RESIDE_SELECT_YIRUN = 1;
	/** 所属版块 2-鸿坤集团 **/
	public static int RESIDE_SELECT_HONGKUN = 2;
	/** 调查问卷类型 单选 **/
	public static int INFO_QUESTIONNAIRE_TYPE_RADIO = 1;
	/** 调查问卷类型 复选 **/
	public static int INFO_QUESTIONNAIRE_TYPE_CHECKBOX = 2;
	/** 调查问卷类型 文本框 **/
	public static int INFO_QUESTIONNAIRE_TYPE_TEXT = 3;
	/** 调查问卷状态 显示 **/
	public static int INFO_QUESTIONNAIRE_STATE_SHOW = 1;
	/** 调查问卷状态 隐藏 **/
	public static int INFO_QUESTTONNAIRE_STATE_HIDE = 2;
	/** 调查问卷状态 删除 **/
	public static int INFO_QUESTIONNAIRE_STATE_DEL = 3;
	/** banner图是否可见 0:所有用户可见 1:可以看见定期投资的用户可见 **/
	public static int INFO_SHOW_ZERO = 0;
	public static int INFO_SHOW_ONE = 1;
	/** 资讯类型 14-私募基金 **/
	public static int INFO_TYPE_PE = 14;
	/** 资讯类型 15-中国海外基金 **/
	public static int INFO_TYPE_OVERSEA_FUND = 15;
	/** 资讯类型 16-认购信托产品 **/
	public static int INFO_TYPE_FOT = 16;
	/** 资讯类型 17-房产投资 **/
	public static int INFO_TYPE_HOUSE = 17;
}
