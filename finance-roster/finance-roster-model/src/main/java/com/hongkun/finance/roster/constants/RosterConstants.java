package com.hongkun.finance.roster.constants;

public class RosterConstants {

	/** 物业员工 **/
	public static final int ROSTER_STAFF_TYPE_PROPERTY = 1;
	/** 销售员工 **/
	public static final int ROSTER_STAFF_TYPE_SALES = 2;
	/** 内部员工 **/
	public static final int ROSTER_STAFF_TYPE_INNER_STAFF = 3;
	/** 理财家 **/
	public static final int ROSTER_STAFF_TYPE_INNER_MANAGER = 4;

	/** 购房宝 **/
	public static final int ROSTER_DEPOSIT_TYPE_BUYHOURSE = 1;
	/** 物业宝 **/
	public static final int ROSTER_DEPOSIT_TYPE_PROPERTY = 2;
    /**正常**/
	public static final int ROSTER_STAFF_STATE_VALID = 1;
	/**删除**/
	public static final int ROSTER_STAFF_STATE_DEL = 0;

	/** 提醒方式：邮件*/
	public static final int ROS_NOTICE_WAY_EMAIL = 1;
	/** 提醒方式：短信*/
	public static final int ROS_NOTICE_WAY_TEL = 2;
	/** 提醒方式：站内信*/
	public static final int ROS_NOTICE_WAY_SMS = 3;

	/**提醒模块功能：平台对账 */
	public static final int ROS_NOTICE_TYPE_BAL = 1;
	/**提醒模块功能：发送现金红包 */
	public static final int ROS_NOTICE_TYPE_REDPACK= 2;
	/**提醒模块功能：发送加息券 */
	public static final int ROS_NOTICE_TYPE_RATE_COUPON = 3;
	/**提醒模块功能：发送投资红包 */
	public static final int ROS_NOTICE_INVEST_REDPCAK = 4;
	/**提醒模块功能：补发卡券提醒 */
	public static final int ROS_NOTICE_REISSUE_COUPON = 5;
	/**系统开关：启用 */
	public static final int SYS_FUNCTION_CFG_INABLE = 1;
	/**系统开关：禁用 */
	public static final int SYS_FUNCTION_CFG_DISABLE = 0;
	/** 系统开关：房产投资*/
	public static final String SYS_FUNCTION_CFG_HOUSE_INVEST = "HOUSE_INVEST";
	
	 /**0:不发放推荐奖**/
    public static final int ROSTER_STAFF_RECOM_STATE_NOSEND = 0;
    /**1：发放推荐奖**/
    public static final int ROSTER_STAFF_RECOM_STATE_SEND = 1;
}
