package com.hongkun.finance.monitor.constants;

/**
 * 监控常量
 *
 * @author zc.ding
 * @create 2018/5/31
 */
public interface MonitorConstants {

    /** 用户第三方操作记录的mq队列 */
    final String MQ_QUEUE_MONITOR_OPERATE_RECORD = "defalut_monitor_operate_record";
    /**有盾实名操作**/
    public static final int USER_REAL_NAME_YOUDUN = 1;
    /**宝付实名操作**/
	public static final int USER_REAL_NAME_BAOFU = 2;
	/**充值异步操作**/
	public static final int USER_RECHARGE_OPERATE = 3;
}
