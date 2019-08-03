package com.hongkun.finance.vas.constants;

import java.math.BigDecimal;

/**
 * @Description : 增值服务常量类
 * @Project : finance
 * @Program Name : com.hongkun.finance.vas.VasConstants
 * @Author : zhongpingtang@yiruntz.com 唐忠平
 */
public class VasConstants {

	/***
	 * 生成线下红包的最大的单个金额
	 */
	public static final BigDecimal MAX_OFFLINE_REDPACKET_VALUE = BigDecimal.valueOf(50);

	/**
	 * 增值服务的bussiness_name
	 */
	public static final String VAS = "vas";

	/**
	 * 红包来源
	 */
	public static final String REDPACKET_SOURCE = "redpacket_source";

	/**
	 * 红包状态
	 */
	public static final String REDPACKET_STATE = "redpacket_state";

	/**
	 * 产生和派发红包的锁（主要是因为生成红包key和插入到数据库为两步，所以必须保证为同一流程）
	 */
	public static final String DISTRIBUTEREDPACKET_LOCK = "DISTRIBUTEREDPACKET_LOCK";

	/** 会员等级-成长值类型 */
	public static final String VIP_GROW_TYPE = "vip_grow_type";

	/** 会员等级-会员待遇类型 */
	public static final String VIP_TREATMENT_TYPE = "vip_treatment_type";

	// ######################常量定义区域###########################################
	/**
	 * 红包未领取状态
	 */
	public static final Integer REDPACKET_STATE_UNRECEIVED = 0;
	/**
	 * 红包已经领取状态
	 */
	public static final Integer REDPACKET_STATE_HASRECEIVED = 1;
	/**
	 * 红包已过期
	 */
	public static final Integer REDPACKET_STATE_OUT_OF_DATE = 2;
	/**
	 * 红包待审核
	 */
	public static final Integer REDPACKET_STATE_ON_CHECK = 4;
	/**
	 * 红包财务拒绝
	 */
	public static final Integer REDPACKET_STATE_REJECT = 5;
	/**
	 * 红包失效
	 */
	public static final Integer REDPACKET_STATE_FAILED = 9;

	/**
	 * 红包已经删除(后台不显示)
	 */
	public static final Integer REDPACKET_STATE_DELETED = 10;

	// ###########################红包来源################
	/**
	 * 红包线下生成
	 */
	public static final Integer REDPACKET_SOURCE_OFFLINE = 1;

	/**
	 * 红包运营派发
	 */
	public static final Integer REDPACKET_SOURCE_DISTRIBUTE = 2;

	/**
	 * 红包跑批生成
	 */
	public static final Integer REDPACKET_SOURCE_TIMED_TASK = 3;

	/**
	 * 个人派发
	 */
	public static final Integer REDPACKET_SOURCE_PERSONAL = 4;

	/**
	 * 红包运营导入派发
	 */
	public static final Integer REDPACKET_SOURCE_IMPORT = 5;

	/** 增值服务模块：禁用状态 */
	public static final int VAS_STATE_N = 0;

	/** 增值服务模块：启用状态 */
	public static final int VAS_STATE_Y = 1;
	/****************** 体验金表状态 **********************************/
	/** 初始化 **/
	public static final int VAS_SIM_STATE_INIT = 0;
	/** 已使用 **/
	public static final int VAS_SIM_STATE_USES = 1;
	/** 过期 **/
	public static final int VAS_SIM_STATE_EXPIRE = 2;
	/** 无效 **/
	public static final int VAS_SIM_STATE_INVALID = 3;

	/****************** 规则表状态 **********************************/
	/** 初始化 **/
	public static final int VAS_RULE_STATE_INIT = 0;
	/** 启用 **/
	public static final int VAS_RULE_STATE_START = 1;
	/** 失效 **/
	public static final int VAS_RULE_STATE_INVALID = 2;

	/*********************** 推荐奖记录表状态 **************************/
	/**
	 * 0-初始化
	 */
	public static final int RECOMMEND_EARN_STATE_INIT = 0;
	/**
	 * 1-无需审核已发放
	 */
	public static final int RECOMMEND_EARN_STATE_NOT_REVIEW_TRANT = 1;
	/**
	 * 2-无需审核待发放(线下处理)
	 */
	public static final int RECOMMEND_EARN_STATE_NOT_REVIEW_WAIT_TRANT = 2;
	/**
	 * 3-审核成功
	 */
	public static final int RECOMMEND_EARN_STATE_REVIEW_SUCCESS = 3;
	/**
	 * 4-审核失败
	 */
	public static final int RECOMMEND_EARN_STATE_REVIEW_FAIL = 4;
	/**
	 * 5-已发放
	 */
	public static final int RECOMMEND_EARN_STATE_GRANT_SUCCESS = 5;
	/**
	 * 6-发放失败
	 */
	public static final int RECOMMEND_EARN_STATE_GRANT_FAIL = 6;
	/**
	 * 7-失效
	 */
	public static final int RECOMMEND_EARN_STATE_INVALID = 7;
	/**
	 * 一级好友
	 */
	public static final int RECOMMEND_FRIEND_LEVEL_ONE = 1;
	/**
	 * 二级好友
	 */
	public static final int RECOMMEND_FRIEND_LEVEL_TWO = 2;

	/**
	 * 普通红包
	 */
	public static final int RED_PACKET_TYPE_DEFAULT=0;
	/**
	 * 鸿坤金服推荐红包
	 */
	public static final int RED_PACKET_TYPE_PLUS=1;

	/**
	 * 派发红包的模板消息
	 */
	public static String RED_PACKAGE_MSG =
			"系统已向您的账号发送一个面值为%s元的红包，兑换编码为%s ，过期时间为%s，请及时在【我的账户->红包管理->红包兑换】功能处兑换红包。（移动端请直接兑换）";
	public static String RED_PACKAGE_MSG_PLUS =
			"您收到鸿坤金服发来%s元推荐奖励红包，兑换编码为%s，有效期至%s ，请及时在鸿坤金服APP【我的->红包】直接领取或者在【我的->红包->红包兑换】功能处兑换红包。";

	/** 推荐奖MQ队列名称 **/
	public static final String MQ_QUEUE_RECOMMEND_EARN_RECORD = "mq_queue_recommend_earn_record";

	public static final String RECOMMEND_CODE_FILE_PATH = "vasrecommend/qrCode";
	/*** 规则 key前缀 **/
	public static final String VAR_RULE_KEY = "findVasRebatesRule_VAS_";
	/*** 红包平台派发前端展示名称 **/
	public static final String SEND_BY_PLATFORM = "平台派发";
	/****************** 推荐奖用户角色 ***********************************************/
	/** 0-普通用户 **/
	public static final int RECOMMEND_USER_TYPE_COMMON = 0;
	/** 1-物业员工 **/
	public static final int RECOMMEND_USER_TYPE_BUSINESS = 1;
	/** 2-销售员工 **/
	public static final int RECOMMEND_USER_TYPE_PROPERTY = 2;
	/** 3-内部员工 **/
	public static final int RECOMMEND_USER_TYPE_INNER = 3;
	/** 4-理财家 **/
	public static final int RECOMMEND_USER_TYPE_MANAGER = 4;


    /** 钱袋子规则状态 抢购中 **/
    public static final int QDZ_RULR_STATE_BUYING = 0;
    /** 钱袋子规则状态 倒计息 **/
    public static final int QDZ_RULR_STATE_COUNTDOWN = 1;
    /** 钱袋子规则状态已售罄 **/
    public static final int QDZ_RULR_STATE_SOLDOUT = 2;
    /** 钱袋子规则状态 下架 **/
    public static final int QDZ_RULR_STATE_OFFSHELF = 3;

    /** 钱袋子总开关 1-允许转入转出 **/
    public static final String QDZ_INOUT_ON = "1";
    /** 钱袋子总开关 0-不允许转入转出 **/
    public static final String QDZ_INOUT_OFF = "0";
    /**定期投资奖励类型**/
    public static final int INVEST_EARN = 0;
    /**钱袋子投资奖励类型**/
    public static final int QDZ_INVEST_EARN = 1;

}
