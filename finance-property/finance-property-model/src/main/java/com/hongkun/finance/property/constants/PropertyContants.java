package com.hongkun.finance.property.constants;

public class PropertyContants {
	/**物业缴费记录状态：缴费中 */
	public static final int PROPERTY_RECORD_STATE_ING = 0 ;
	/**物业缴费记录状态：冻结 */
	public static final int PROPERTY_RECORD_STATE_FREE = 1 ;
	/**物业缴费记录状态：缴费失败 */
	public static final int PROPERTY_RECORD_STATE_FAIL = 2 ;
	/**物业缴费记录状态：缴费成功*/
	public static final int PROPERTY_RECORD_STATE_SUCCESS = 3 ;
	/**物业缴费记录状态：缴费审核拒绝 */
	public static final int PROPERTY_RECORD_STATE_REFUSE = 4 ;
	/**物业缴费审核：锁-key */
	public static final String PROPERTY_AUDIT_LOCK_KEY = "PROPERTY_AUDIT_LOCK_KEY" ;
	/**物业缴费-是否使用积分：使用 */
	public static final int PAY_PROPERTY_USEPOINTS_YES = 1 ;
	/**物业缴费-是否使用积分：不使用 */
	public static final int PAY_PROPERTY_USEPOINTS_NO = 0;
	/**物业缴费-锁 */
	public static final String PROPERTY_PAYMENT_LOCK_PREFIX = "property_payment_lock_prefix";
	/*****余额不足，给app提示********/
	public static final int PROPERTY_RECORD_STATE_NO_USEABLE = 5 ;
	/** 物业缴费paytype 物业费1*/
	public static final int PROPERTY_PAY_TYPE_PROPERTY = 1;
	/** 物业缴费paytype 车位费 2*/
	public static final int PROPERTY_PAY_TYPE_CAR = 2;
	/** 物业缴费描述  物业费*/
	public static final String PROPERTY_PAY_TYPE_PROPERTY_DESC = "物业费";
	/** 物业缴费描述 车位费 */
	public static final String PROPERTY_PAY_TYPE_CAR_DESC = "车位费";
	
	/** 缴费状态中文描述  缴费中**/
	public static final String PROPERTY_RECORD_STATE_PROCESSING_DESC = "缴费中";
	/** 缴费状态中文描述  待审核**/
	public static final String PROPERTY_RECORD_STATE_AUDITED_DESC = "待审核";
	/** 缴费状态中文描述 缴费成功**/
	public static final String PROPERTY_RECORD_STATE_SUCCESS_DESC = "缴费成功";
	/** 缴费状态中文描述  缴费失败**/
	public static final String PROPERTY_RECORD_STATE_FAIL_DESC = "缴费失败";
	/** 抵扣物业费 中文描述***/
	public static final String PROPERTY_RECORD_POINT_DEDUCTION = "抵扣物业费";
	/** 抵扣物业费退回 中文描述***/
	public static final String PROPERTY_RECORD_POINT_FALLBACK = "抵扣物业费退回";
}
