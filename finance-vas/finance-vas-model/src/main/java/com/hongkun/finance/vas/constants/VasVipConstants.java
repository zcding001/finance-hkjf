package com.hongkun.finance.vas.constants;

/**
 * @Description : 增值服务-会员等级常量类
 * @Project : framework
 * @Program Name  : com.hongkun.finance.vas.constants.VasVipConstants
 * @Author : pengwu@hongkun.com.cn 吴鹏
 */
public class VasVipConstants {

    /**会员等级-成长值类型-投资*/
    public static final int VAS_VIP_GROW_TYPE_INVEST = 1;

    /**会员等级-成长值类型-邀请好友注册*/
    public static final int VAS_VIP_GROW_TYPE_INVITE_USER_REGIST = 2;

    /**会员等级-成长值类型-签到*/
    public static final int VAS_VIP_GROW_TYPE_SIGN = 3;

    /**会员等级-成长值类型-首次充值*/
    public static final int VAS_VIP_GROW_TYPE_FIRST_RECHARGE = 4;

    /**会员等级-成长值类型-积分兑换*/
    public static final int VAS_VIP_GROW_TYPE_EXCHANGE = 5;

    /**会员等级-成长值类型-邀请用户投资*/
    public static final int VAS_VIP_GROW_TYPE_INVITE_USER_INVEST = 6;

    /**会员等级-成长值类型-积分支付*/
    public static final int VAS_VIP_GROW_TYPE_POINT_PAYMENT = 7;

    /**会员等级-成长值类型-积分转赠*/
    public static final int VAS_VIP_GROW_TYPE_POINT_DONATION = 8;

    /**会员等级-成长值类型-平台赠送*/
    public static final int VAS_VIP_GROW_TYPE_POINT_GIVE = 9;

    /**会员等级-成长值类型-会员降级*/
    public static final int VAS_VIP_GROW_TYPE_POINT_DEGRADE = 10;

    /**会员待遇-类型-投资加息*/
    public static final int VAS_VIP_TREATMENT_TYPE_INVEST_INCREASE = 1;

    /**会员待遇-类型-提现优惠*/
    public static final int VAS_VIP_TREATMENT_TYPE_CASH_COUPON = 4;

    /**会员待遇-类型-投资红包*/
    public static final int VAS_VIP_TREATMENT_TYPE_INVEST_RED_PACKET = 7;

    /**会员待遇-类型-好友券*/
    public static final int VAS_VIP_TREATMENT_TYPE_FRIEND_COUPON = 8;

    /**会员待遇-类型-生日礼券*/
    public static final int VAS_VIP_TREATMENT_TYPE_BIRTH = 3;

    /**会员等级-成长值记录消息队列*/
    public static final String MQ_QUEUE_VAS_VIP_GROW_RECORD = "mq_queue_vas_vip_grow_record";

    /**会员等级-会员等级中文描述**/
    public static final String VAS_VIP_GRADE_NAME = "vip_grade_name";
}
