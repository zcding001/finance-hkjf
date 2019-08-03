package com.hongkun.finance.bi.constants;

/**
 * @Description : 收入统计
 * @Project : finance
 * @Program Name  : com.hongkun.finance.bi.constants.StaIncomeConstants.java
 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
 */
public class BiConstants {
    /**收入统计-状态： 成功 */
    public static final int STA_INCOME_STATE_SUCCESS = 1;
    /**收入统计-交易类型： 手续费 */
    public static final int STA_INCOME_TRADE_TYPE_POUNDAGE = 1;
    /**收入统计-交易类型： 服务费 */
    public static final int STA_INCOME_TRADE_TYPE_SERVICE_CHARGE = 2;
    /**收入统计-交易类型： 罚息 */
    public static final int STA_INCOME_TRADE_TYPE_LATE_CHARGE = 3;

    /**平台对账：对账方式-实时对账 */
    public static  final int BALANCE_TYPE_ACTUALTIME = 1;
    /**平台对账：对账方式-历史对账 */
    public static  final int BALANCE_TYPE_RECORD = 2;

    /**平台对账：对账结果-一致 */
    public static final int BALANCE_STATE_IS_EQUAL = 1;
    /**平台对账：对账结果-不一致 */
    public static final int BALANCE_STATE_IS_NOT_EQUAL = 0;
    /**平台对账：对账结果-全部 */
    public static final int BALANCE_STATE_ALL = 2;
}
