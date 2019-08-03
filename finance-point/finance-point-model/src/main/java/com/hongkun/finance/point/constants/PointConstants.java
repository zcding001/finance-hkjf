package com.hongkun.finance.point.constants;

/**
 * @Description : 积分模块常量类
 * @Project : finance
 * @Program Name  : com.hongkun.finance.point.model.PointConstants
 * @Author : zhongpingtang@yiruntz.com 唐忠平
 */
public class PointConstants {

    /**
     * 修改规则锁的key
     */
    public static final String POINT_RULE_CHANGE_LOCK_KEY = "point_rule_change";

    /**
     * 下订单锁的key
     */
    public static final String POINT_CREATE_ORDER_LOCK_KEY = "point_create_order_lock_key_";
    /**
     * 审核订单锁的key
     */
    public static final String POINT_CHECK_ORDER_LOCK_KEY = "point_check_order_lock_key_";
    /**
     * 积分商品库存锁的key
     */
    public static final String POINT_PRODUCT_LOCK_KEY = "point_product_lock_key_";
    /**
     * 取消订单的锁的key
     */
    public static final String POINT_ORDER_CANCEL_LOCK_KEY = "point_order_cancel_lock_";

    /**
     * 用户积分账户锁
     */
    public static final String POINT_ACCOUNT_LOCK_KEY = "point_account_lock_key_";
    /**
     * 修改规则锁的超时时间 ,单位：秒
     */
    public static final int EXPIRE_TIME = 5;

    /**
     * 商品模块文件夹名称（积分模块如还有常量，均以point文件夹为根目录）
     */
    public static String POINT_FOLDER_PRODUCT = "point/product";
    /**
     * 字典常量
     */
    public static String POINT = "point";

    /**
     * 积分类型
     */
    public static String POINT_TYPE = "type";


    /**
     * 积分状态
     */
    public static String POINT_STATE = "state";


    /**
     * 所有的类目
     */
    public static final String CATES = "ALL_CATES";


    /*****************商品状态************************/
    /**
     * 待审核
     */
    public static int UN_CHECK = 0;

    /**
     * 审核通过
     */
    public static int CHECK_PASS = 1;

    /**
     * 已上架
     */
    public static int ON_SALE = 2;

    /**
     * 审核拒绝
     */
    public static int CHECK_REJECT = 3;
    /**
     * 逻缉删除
     */
    public static int LOGICAL_DEL = 4;

    /*****************商品其他************************/
    /**
     * 商品首图
     */
    public static final int FIRST_IMG=1;



    /************************积分来源类型***************************/

    /**
     * 投资
     */
    public static int POINT_TYPE_INVEST = 1;
    /**
     * 平台赠送
     */
    public static int POINT_TYPE_DONATE = 2;
    /**
     * 积分兑换
     */
    public static int POINT_TYPE_CONVERT = 3;
    /**
     * 转赠收入
     */
    public static int POINT_TYPE_PASS_IN = 4;
    /**
     * 转赠支出
     */
    public static int POINT_TYPE_PASS_OUT = 5;
    /**
     * 积分支付
     */
    public static int POINT_TYPE_PAY = 6;
    /**
     * 签到
     */
    public static int POINT_TYPE_SIGN_IN = 7;
    /**
     * 活动赠送
     */
    public static int POINT_TYPE_ACTIVITY = 8;
    /**
     * 物业抵扣
     */
    public static int POINT_TYPE_TENEMENT = 9;
    /**
     * 物业抵扣退还积分
     */
    public static int POINT_TYPE_TENEMENT_BACK = 10;

    /**
     * 实名认证赠送积分
     */
    public static int POINT_TYPE_REAL_NAME_AUTH = 11;

    /***********************积分状态*************************************/
    /**
     * 已经确认
     */
    public static int POINT_STATE_CONFIRMED = 0;
    /**
     * 待审核
     */
    public static int POINT_STATE_UNCHECK = 1;
    /**
     * 审核失败
     */
    public static int POINT_STATE_CHECK_FAIL = 2;

    /*******************规则状态*************************/
    /**
     * 未启用
     */
    public static int POINT_RULE_STATE_UN_USE = 0;
    /**
     * 启用
     */
    public static int POINT_RULE_STATE_ON_USE = 1;
    /**
     * 失效
     */
    public static int POINT_RULE_STATE_INVALID = 2;


    /***************订单配送方式：0-邮寄或自提，1-自提，2-邮寄，3-兑换码',*************************/
    /**
     * 邮寄或者自提
     */
    public static int POINT_ORDER_SENDWAY_POST_OR_PICK_SELF= 0;
    /**
     * 自提
     */
    public static int POINT_ORDER_SENDWAY_PICK_SELF= 1;
    /**
     * 邮寄
     */
    public static int POINT_ORDER_SENDWAY_POST= 2;
    /**
     * 无需配送
     */
    public static int POINT_ORDER_SENDWAY_NO_NEED_SEND= 3;



    /***************订单状态：0-待审核，1-商家处理中，2-商家处理完成，3-订单已取消，4-审核拒绝'*************************/

    /**
     * 待审核
     */
    public static int POINT_ORDER_STATE_UN_CHECK = 0;
    /**
     * 商家处理中
     */
    public static int POINT_ORDER_STATE_MERCHANT_HANDING = 1;

    /**
     * 商家处理完成
     */
    public static int POINT_ORDER_STATE_MERCHANT_HANDLE_COMPLETE = 2;
    /**
     * 取消订单
     */
    public static int POINT_ORDER_STATE_CANCEL = 3;
    /**
     * 审核拒绝订单
     */
    public static int POINT_ORDER_STATE_CHECK_REJECT = 4;

    /***********************积分商户相关************************************/
    /**
     * 积分商户待审核
     */
    public static int POINT_MERCHANT_STATE_WAIT_DELETE = 0;
    /**
     * 积分商户待审核
     */
    public static int POINT_MERCHANT_STATE_WAIT_CHECK = 1;
    /**
     * 积分商户待审核失败
     */
    public static int POINT_MERCHANT_STATE_CHECK_FAIL = 2;
    /**
     * 积分商户审核成功
     */
    public static int POINT_MERCHANT_STATE_CHECK_SUCCESS = 3;
    /**
     * 积分商户未申请
     */
    public static int POINT_MERCHANT_STATE_CHECK_NOT_APPLY = 4;

    /*****************消息队列常量相关***********************************/
    public static String PROCESSKEY = "key";
    /** 积分消息队列*/
    public static String MQ_QUEUE_POINT_RECORD = "mq_queue_point_record";
    /**
     * 创建积分账户
     */
    public static String MQ_QUEUE_INIT_POINT_ACCOUNT = "mq_queue_init_point_account";


    public static final String QR_CORD_FILE_PATH = "pointMerchant/qrCode";
    /****非限时抢购******/
    public static final int POINT_FLASH_SALE_ZERO = 0;
    /*****限时抢购商品*****/
    public static final int POINT_FLASH_SALE_ONE = 1;
}
