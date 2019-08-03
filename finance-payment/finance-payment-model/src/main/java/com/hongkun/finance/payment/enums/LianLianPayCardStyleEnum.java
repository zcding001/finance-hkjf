

/**
 * 
 */
package com.hongkun.finance.payment.enums;

/**
 * @Description : caoxb 类描述 连连支付卡类型枚举
 * @Project : finance-payment-model
 * @Program Name : com.hongkun.finance.payment.enums.LianLianPayStyleEnum.java
 * @Author : caoxinbang@hongkun.com.cn 曹新帮
 */

public enum LianLianPayCardStyleEnum {
    /**
     * 1：网银支付（借记卡） 2：快捷支付（借记卡） 3：快捷支付（信用卡） 8：网银支付（信用卡） 9：B2B 企业网银支付 D 认证支付 注：连连 网银支付方式和认证支付方式 必填字段
     * 
     */
    WYDC("WYDC", "1"), KJDC("KJDC", "2"), KJCC("KJCC", "3"), WYCC("WYCC", "8"), QYWY("QYWY", "9"), RZDC("RZDC", "D");

    /**
     * 标识
     */
    private String type;
    /**
     * 值
     */
    private String value;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    /**
     * LianLianPayStyleEnum.
     */

    private LianLianPayCardStyleEnum(String type, String value) {

        this.type = type;

        this.value = value;
    }

    /**
     * 
     * @Description : caoxb 方法描述:获取连连支付方式
     * @Method_Name : valueByType
     * @param type
     * @return
     * @return : String
     * @Creation Date : 2017年6月13日 上午10:31:38
     * @Author : caoxinbang@hongkun.com.cn 曹新帮
     */
    public static String valueByType(String type) {
        for (LianLianPayCardStyleEnum s : LianLianPayCardStyleEnum.values()) {
            if (s.getType().equals(type)) {
                return s.getValue();
            }
        }
        return "";
    }
}

