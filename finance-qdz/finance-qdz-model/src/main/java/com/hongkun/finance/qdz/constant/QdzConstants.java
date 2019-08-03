package com.hongkun.finance.qdz.constant;

import java.util.HashMap;
import java.util.Map;

public class QdzConstants {
	/**
	 * 钱袋子转入转出开关
	 */
	public static final String QDZ_TRANSFER_IN_OUT_OPEN = "qdz_transfer_open";
	/**
	 * 钱袋子转入转出开关状态 1-启用 允许转入转出
	 */
	public static final int QDZ_TRANSFER_INOUT_START = 1;
	/**
	 * 钱袋子转入转出开关状态 0-禁止 允许转入转出
	 */
	public static final int QDZ_TRANSFER_INOUT_FORBID = 1;
	/** 转入转出成功标识 **/
	public static final int TRANS_RECORD_SUCCSS = 1;
	/** 转入转出失败标识 **/
	public static final int TRANS_RECORD_FAIL = 2;
	/** 转出跑批债券异常 **/
	public static final int TRANS_RECORD_EXCEPT = 3;
	/** 购买债券标识 **/
	public static final int CREDITOR_FLAG_BUY = 1;
	/** 释放债券标识 **/
	public static final int CREDITOR_FLAG_SELL = 2;
	/** 处理债券购买异常消息 **/
	public static final String REDIS_QDZ_BUY_CREDITOR_EXCEPT = "qdz_buy_creditor_except";
	/** 处理债券释放异常消息 **/
	public static final String REDIS_QDZ_SELL_CREDITOR_EXCEPT = "qdz_sell_creditor_except";
	/** 计算钱袋子利息异常消息 **/
	public static final String REDIS_QDZ_CALCULATE_INTEREST_EXCEPT = "redis_qdz_calculate_interest_except";
	/** 钱袋子推荐奖励 **/
	public static final String REDIS_QDZ_RECOMMEND_AWARD_EXCEPT = "redis_qdz_recommend_award_except";

	/** 计算钱袋子每天利息成功标识 **/
	public static final int QDZ_INTEREST_DAY_SUCCSS = 1;
	/** 计算钱袋子每天利息失败标识 **/
	public static final int QDZ_INTEREST_DAY_FAIL = 2;
	/** 平台用户标识 **/
	public static final int PLAT_USER_FLAG = 0;
	/** 第三方用户标识 **/
	public static final int THIRD_USER_FLAG = 1;
	/** 钱袋子总开关 1-允许转入转出 **/
	public static final String QDZ_INOUT_ON = "1";
	/** 钱袋子总开关 0-不允许转入转出 **/
	public static final String QDZ_INOUT_OFF = "0";
	/** 钱袋子处理异常数据ID **/
	public static final int QDZ_EXCEPTION_ID = -9999;
	/** 钱袋子新用户标识 **/
	public static final int QDZ_INSERT_FALG = 0;
	/** 钱袋子老用户标识 **/
	public static final int QDZ_UPDATE_FALG = 1;

	/** 钱袋子账户逻缉删除 ***/
	public static final int QDZ_ACCOUNT_STATE_DEL = 0;
	/** 钱袋子账户正常 ***/
	public static final int QDZ_ACCOUNT_STATE_COM = 1;
	/** 钱袋子记录状态成功 ***/
	public static final int QDZ_RECORD_STATE_SUCCESS = 1;

	/*** 钱袋子正常转入转出 **/
	public static final int QDZ_TURNINOUT_TYPE_COMMON = 1;
	/*** 钱袋子投资转入转出 **/
	public static final int QDZ_TURNINOUT_TYPE_INVEST = 2;
	/*** 钱袋子提现转出 **/
	public static final int QDZ_TURNINOUT_TYPE_WITHDRAW = 3;
    /** 钱袋子每日计息成功状态**/
	public static final int QDZ_INTEREST_DAY_STATE_SUCCESS = 1;
	/** 钱袋子每日计息失败状态**/
	public static final int QDZ_INTEREST_DAY_STATE_FAIL = 2;

	/** 钱袋子转出自动转让债权MQ队列名称 **/
	public static final String MQ_QUEUE_AUTO_CREDITOR_TRANSFER = "mq_queue_auto_creditor_transfer";
	/** 钱袋子规则状态 抢购中 **/
	public static final int QDZ_RULR_STATE_BUYING = 0;
	/** 钱袋子规则状态 倒计息 **/
	public static final int QDZ_RULR_STATE_COUNTDOWN = 1;
	/** 钱袋子规则状态已售罄 **/
	public static final int QDZ_RULR_STATE_SOLDOUT = 2;
	/** 钱袋子规则状态 下架 **/
	public static final int QDZ_RULR_STATE_OFFSHELF = 3;

	/** 前台钱袋子是否展示 是 **/
	public static final int QDZ_SHOW_FLAG_TRUE = 1;
	/** 前台钱袋子是否展示 否 **/
	public static final int QDZ_SHOW_FLAG_FLASE = 0;
    /***钱袋子转入***/
    public static final int QDZ_TURN_IN = 1;
    /***钱袋子转出***/
    public static final int QDZ_TURN_OUT = 2;

	public static final Map<Integer, String> QDZ_RULR_STATE = new HashMap<Integer, String>();
	static {
		QDZ_RULR_STATE.put(QDZ_RULR_STATE_BUYING, "抢购");
		QDZ_RULR_STATE.put(QDZ_RULR_STATE_COUNTDOWN, "倒计时");
		QDZ_RULR_STATE.put(QDZ_RULR_STATE_SOLDOUT, "已售罄");
		QDZ_RULR_STATE.put(QDZ_RULR_STATE_OFFSHELF, "下架");
	}
}
