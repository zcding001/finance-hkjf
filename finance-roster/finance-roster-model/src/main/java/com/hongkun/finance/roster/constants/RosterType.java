package com.hongkun.finance.roster.constants;

/**
 * @Description : 黑白名单类型
 * @Project : finance-roster-model
 * @Program Name : com.hongkun.finance.roster.constants.RosterType.java
 * @Author : zhichaoding@hongkun.com zc.ding
 */
public enum RosterType {

    DEFAULT(-999),
	/**
	 * 积分增值
	 */
	POINT_ADD(0),
	/**
	 * 积分转金额
	 */
	POINT_TO_MONEY(1),
	/**
	 * 金额转积分
	 */
	MONEY_TO_POINT(2),
	/**
	 * 积分投资
	 */
	POINT_TO_INVEST(3),
	/**
	 * 债权转让
	 */
	BOND_TRANSFER(4),
	/**
	 * 新手标投资
	 */
	FLEDGLING_BID(5),
	/**
	 * 钱袋子转入转出
	 */
	CURRENT_IN_OUT(6),
	/**
	 * 钱袋子推荐奖
	 */
	CURRENT_COMMENDED(7),
	/**
	 * 钱袋子债券通知
	 */
	QDZ_CREDITOR_NOTICE(8),
	
	/**
	 * 投资控制
	 */
	INVEST_CTL(9),
	/**
	 * 短信控制(所有短信)
	 */
	SMS_CTL(10),
	/**
	 * 还款短信通知
	 */
	SMS_REAPY_NOTICE(11),
    /**
     * 提现
     */
    WITHDRAW(12),
    /**
     * 注册
     */
    REGISTER(13),
	/**
	 * 平台对账发送邮件
	 */
    BAL_EMAIL(14),
	/**
	 * IOS版本查看
	 */
	IOS_VERSION_VIEW(15),
	/**
	* vip投资人
	*/
	VIP_INVESTOR(16),

    /**
    * 物业菜单
    */
	PROPERTY_OPERATE(17),
    /**
    * 充值
    */
    PROPERTY_PAY(18),
    /**
     * 美分期发送红包
     */
    MFQ_PACKAGE(20),

    /**
     * 物业现金缴费
     */
    property_LOAN(21),
    /**
     * 提现放款
     */
    WITHDRAW_LOAN(22),
	/**
	 * 海外投资人
	 */
	OVERSEA_INVESTOR(23),
	/*
	* 交易所匹配查询
	 */
	EXCHANGE_INVEST_SEACH(24);

	private int value;

	private RosterType(int value) {
		this.value = value;
	}

	public int getValue() {
		return this.value;
	}
	
	public static RosterType getRosterType(Integer val){
	    for(RosterType rt : RosterType.values()){
	        if(rt.getValue() == val){
	            return rt;
            }
        }
        return RosterType.DEFAULT;
    }
}
