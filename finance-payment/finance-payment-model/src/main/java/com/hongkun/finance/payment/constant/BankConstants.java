package com.hongkun.finance.payment.constant;
/**
 * @Description : 与第三方支付相关或支付服务自己定义的支付常量类
 * @Project : finance-payment-model
 * @Program Name : com.hongkun.finance.payment.constant.BankConstants.java
 * @Author : yunlongliu@hongkun.com.cn
 */
public class BankConstants {


    /**解绑银行卡- 待审核**/
    public static Integer BANK_UPDATE_WAIT_AUDIT = 0;
    /**解绑银行卡- 审核通过**/
    public static Integer BANK_UPDATE_AUDIT_PASS = 1;
    /**解绑银行卡- 审核拒绝**/
    public static Integer BANK_UPDATE_AUDIT_REJECT = 2;
    /**绑卡状态-未认证**/
    public static Integer BANK_CARD_NO_RZ = 1;
    /**绑卡状态-已认证**/
    public static Integer BANK_CARD_YES_RZ = 2;

}
