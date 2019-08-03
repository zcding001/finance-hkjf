package com.hongkun.finance.vas.constants;

/**
 * @Description : 增值服务-卡券场内相关类
 * @Project : framework
 * @Program Name  : com.hongkun.finance.vas.constants.VasVipConstants
 * @Author : pengwu@hongkun.com.cn 忠平
 */
public class VasCouponConstants {

    //################################字典定义区#######################
    /**卡券使用场景*/
    public static final String COUPON_USE_SCENE = "coupon_use_scene";
    /**卡券产品类型*/
    public static final String COUPON_PRODUCT_TYPE = "coupon_product_type";
    /**卡券详情来源*/
    public static final String COUPON_DETAIL_SOURCE = "coupon_detail_source";
    /**卡券详情状态*/
    public static final String COUPON_DETAIL_STATE = "coupon_detail_state";



    //################################常量定义区#######################
    /**卡券使用场景-无*/
    public static final int COUPON_USE_SCENE_NONE=0;
    /**卡券使用场景-注册赠送加息券*/
    public static final int COUPON_USE_SCENE_GIVE_COUPON=1;
    /**卡券使用场景-会员待遇*/
    public static final int COUPON_USE_SCENE_MEMBER_TREAT=2;

    /**卡券产品类型-加息券*/
    public static final int COUPON_PRODUCT_TYPE_RATE_COUPON=0;
    /**卡券产品类型-投资红包*/
    public static final int COUPON_PRODUCT_TYPE_INVEST_REDPACKET=1;
    /**卡券产品类型-免费提现券*/
    public static final int COUPON_PRODUCT_TYPE_DEPOSIT=2;
    /**卡券产品类型-好友券*/
    public static final int COUPON_PRODUCT_TYPE_FRIENDS=3;


    /**卡券详情来源-线下生成*/
    public static final int COUPON_DETAIL_SOURCE_OFFLINE=1;
    /**卡券详情来源-手动派发*/
    public static final int COUPON_DETAIL_SOURCE_DISTRIBUTE=2;
    /**卡券详情来源-跑批生成*/
    public static final int COUPON_DETAIL_SOURCE_TIMED_TASK=3;
    /**卡券详情来源-系统补发*/
    public static final int COUPON_DETAIL_SOURCE_REISSUE=4;


    /**卡券详情状态-未领取*/
    public static final int COUPON_DETAIL_CREATE=0;
    /**卡券详情状态-已领取*/
    public static final int COUPON_DETAIL_SEND_ALREADY=1;
    /**卡券详情状态-已使用*/
    public static final int COUPON_DETAIL_USE_ALREADY=2;
    /**卡券详情状态-已过期*/
    public static final int COUPON_DETAIL_OUT_OF_DATE=3;
    /**卡券详情状态-可转赠*/
    public static final int COUPON_DETAIL_DONATION_ONLY=4;
    /**卡券详情状态-已失效*/
    public static final int COUPON_DETAIL_FAILURE=9;
    /**卡券详情截止日期类型-0为过期天数*/
    public static final int COUPON_PRODUCT_DEADLINE_TYPE_DAYS=0;

    /**卡券详情-生成卡券详情消息队列*/
    public static final String MQ_QUEUE_VAS_COUPON_DETAIL = "mq_queue_vas_coupon_detail";
}
