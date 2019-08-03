package com.hongkun.finance.user.constants;

import com.yirun.framework.core.utils.PropertiesHolder;

import java.math.BigDecimal;

/**
 * @author zhongping
 * @date 2017/5/26
 */
public interface UserConstants {

	/**
	 * 平台账户id
	 */
	final int PLATFORM_ACCOUNT_ID = 1;
	/**
	 * 风险储备金账户ID
	 */
	final int PLATFORM_RISK_RESERVE_ID = 2;

	/** 验证实名认证开关*/
	final String OPEN_VALIDATE_REAL_NAME = "open_validate_real_name";
	/** 宝付实名是否开启- 对应 sys_function_cfg 开关码表 func_code 字段值 **/
	final String OPEN_BAOFU_REALNAME = "BAOFU_REALNAME";
	/** 宝付实名开启状态 1 开启- 对应 sys_function_cfg 开关码表 ENABLE 字段值 **/
	final int IS_ENABLE = 1;
	
	/** 未实名用户标识 **/
	final int USER_IDENTIFY_NO = 0;
	int USER_IDENTIFY_YES = 1;

	/** 用户类型 **/
	/** 一般用户 */
	int USER_TYPE_GENERAL = 1;
	/** 企业 */
	int USER_TYPE_ENTERPRISE =2;
	/** 物业 */
	int USER_TYPE_TENEMENT = 3;
	/** 第三方账户 */
	int USER_TYPE_ESCROW_ACCOUNT = 4;
	/** 后台用户类型 */
	int USER_TYPE_CONSOLE = 5;
	/** 后台超级管理员 */
	int USER_TYPE_ROOT = 6;

	/**菜单类型*/
	Integer MENU_TYPE_HKJF_CONSOLE = Integer.valueOf(1);/* 鸿坤金服后台 */
	Integer MENU_TYPE_MY_ACCOUNT = Integer.valueOf(2);/* 我的账户 */

	/** 用户状态 **/
	int USER_STATE_N = 0;/* 删除 */
	int USER_STATE_Y = 1;/* 正常 */
	int USER_STATE_F = 2;/* 禁用 */

	/** 用户状态 **/
	int APP_MENU_STATE_DELETE = 0;/* 删除 */
	int APP_MENU_STATE_ENABLE = 1;/* 正常 */
	int APP_MENU_STATE_DISABLE = 2;/* 禁用 */

	/** app菜单状态 **/
	int APP_SERVE_STATE_SHOW = 0;	/* 展示 */
	int APP_SERVE_STATE_HIDE = 1;	/* 隐藏 */

	/**
	 * 小区地址类型
	 */
	int COMMUNITY_TYPE_NOT_SELF_PICK_UP = 0;/*非自提点*/
	int COMMUNITY_TYPE_SELF_PICK_UP = 1;/*自提点*/

	/* 用户平台区分 */
	String USER_PLATFORM_CONSOLE_QSH = "CONSOLE_QSH_";/* 前生活后台用户 */
	String USER_PLATFORM_CONSOLE_HKJF = "CONSOLE_HKJF_";/* 鸿坤金服后台用户 */
	String USER_PLATFORM_CONSOLE_QKD = "CONSOLE_QKD_";/* 乾坤袋后台用户 */
	/** 一级好友 */
	final int FRIEND_LEVEL_ONE = 1;
	/** 二级好友 */
	final int FRIEND_LEVEL_TWO = 2;
	/** 好友投资状态*/
	final int FRIEND_INVEST_STATE = 1;
	
	/**默认密码:a111111*/
	final String USER_DEFAULT_PASSWD = "6846860684f05029abccc09a53cd66f1";
	
	/**管理员操作日志的mq队列*/
	final String MQ_QUEUE_ACTION_LOG = "defalut_action_log";

	/**通知拦截器对用户授权*/
	String AUTHORITYINTERCEPTER_AUTHORIZE_FLAG = "authorityintercepter_authorize_flag";

	/** 好友投资状态消息队列  */
	String MQ_QUEUE_USER_FRIEND_INVEST = "reg_user_friend_invest";
	String MQ_QUEUE_USER_FRIEND_REGIST = "reg_user_friend_regist";
    /**
     * 推荐人在鸿坤金服的用户信息维护
     */
	String MQ_QUEUE_USER_RECOMMEND_REGIST = "reg_user_recommend_regist";
	//	App端登录记录的队列
	 String MQ_QUEUE_APP_LOGIN_LOG = "mq_queue_app_login_log";
	 /** 用户升级为VIP消息 **/
	 String MQ_QUEUE_USER_VIP = "mq_queue_user_vip";
	
	/**
	 * 未实名
	 */
	int NO_IDENTIFY = 2001;
	/**
	 * 未绑定银行卡
	 */
	int NO_BAND_CARD = 2002;
	/**
	 * 未登录默认值
	 */
	int NO_LOGIN = 2003;
	/**
	 * 未设置支付密码
	 */
	int NO_PAY_PWD = 2004;
	/**
	 * 没有权限默认值
	 */
	int REQUEST_FORBID = 2005;
	/**
	 * 不是VIP客户
	 */
	int NOT_VIP = 2006;

	/**
	 * 错误的支付密码
	 */
	int ERROR_PAY_PWD = 2008;

	public static final String USER_FRIENDS_GROUP_INVEST_YES = "已投资分组";
	
	public static final String USER_FRIENDS_GROUP_INVEST_NOT = "未投资分组";
	
	/**好友投资状态：已投资 */
	public static final int USER_INVEST_STATE_YES = 1;
	/**好友投资状态：未投资 */
	public static final int USER_INVEST_STATE_NOT =0;
	/**好友等级：一级 */
	public static final int USER_FRIEND_GRADE_FIRST = 1;
	/**好友等级：二级 */
	public static final int USER_FRIEND_GRADE_SECOND = 2;
	/**好友关系状态：删除 */
	public static final int USER_FRIENDS_STATE_DEL = 0;
	/**好友关系状态：正常 */
	public static final int USER_FRIENDS_STATE_VAL = 1;
	
	public static final int USER_FRIENDS_GROUP_TYPE_NOT_INVEST = 1;
	public static final int USER_FRIENDS_GROUP_TYPE_INVEST = 2;
	public static final int USER_FRIENDS_GROUP_TYPE_COMMON = 0;

    /**
     *  身份证
     */
    static String REG_AUDIT_PREFIX = "user_aduit/";
	/**
	*  身份证
	*/
	static int REG_AUDIT_TYPE_IDENTIFY = 1;
	
	/**
	*  服务热线
    */
	String SERVICE_HOTLINE = PropertiesHolder.getProperty("service.hotline");
    /**
     *  飞VIP用户提示信息
     */
	String NOT_VIP_MSG = "您还不是我们的特定服务用户，请拨打客服电话: " + SERVICE_HOTLINE;
	/**
	 * 实名成功
	 */
	public static final int USER_REAL_NAME_YES = 0;
	/**
	 * 实名信息不一致
	 */
	public static final int USER_REAL_NAME_NO = 1;
	/**
	 * 实名不存在
	 */
	public static final int USER_REAL_NAME_NON = 2;
	/**
	 * 实名系统异常
	 */
	public static final int USER_REAL_NAME_ERROR = 9;
	/**
	 * 前台用户默认角色id
	 */
	public static final Integer USER_PRE_BASIC_ROLE_ID = 1;
	/**
	 * 我的账户一级菜单名称：物业管理
	 */
	public static final  String ACCOUNT_MENU_NAME_PROPERTY = "物业管理";
	/**
	 * 我的账户一级菜单名称：债权转让
	 */
	public static final  String ACCOUNT_MENU_NAME_CREDITOR = "债权转让";
	
	/** vip用户标识 **/
	int USER_IS_VIP = 1;
	int USER_IS_NOT_VIP = 0;
	/** vip会员最低标准 100万**/
	BigDecimal USER_VIP_LOW_STANDARD = BigDecimal.valueOf(1000000);
	/** 降级底线 **/
	int USER_VIP_LOW_TIMES = 4;

    /**
     * 用户鸿坤金服注册状态，在redis中前缀标识
     */
	String KEY_PREFIX_HKJF_USER_STATE = "HKJK_USER_STATE_";

	/**
	 * 我的账号自动投资菜单名称
	 */
	String MENU_AUTO_INVEST = "自动投资";


	/** 用户是否可以投资定期 -- 是**/
	int USER_CAN_INVEST_YES = 1;
	/** 用户是否可以投资定期 -- 否**/
	int USER_CAN_INVEST_NO = 0;
	/** 菜单类型--运营后台*/
	String MENU_TYPE_SYS_MANAGEMET = "1";
	/** 菜单类型--用户前台*/
	String MENU_TYPE_USER_INDEX = "2";
	/** 菜单类型--BI系统*/
	String MENU_TYPE_SYS_BI = "3";

	/** 用户是否做过调查问卷 -- 是**/
	int USER_QUESTIONNAIRE_YES = 1;
	/** 用户是否做过调查问卷 -- 否**/
	int USER_QUESTIONNAIRE_NO = 0;

	/** APP菜单类型 全部功能 **/
	int APP_MENU_TYPE_ALL = 0;
	/** APP菜单类型 用户自定义菜单 **/
	int APP_MENU_TYPE_USER = 1;
	/** APP菜单类型 总菜单 **/
	int APP_MENU_TYPE_TOTAL = 2;
	/** APP菜单类型 活动菜单 **/
	int APP_MENU_TYPE_ACTIVITY = 3;

	/** APP菜单是否显示 显示 **/
	int APP_MENU_SHOW = 0;
	/** APP菜单是否显示 隐藏 **/
	int APP_MENU_HIDE = 1;

	/** APP自定义菜单位置 首页 **/
	int APP_MENU_POSITION_INDEX = 0;
}
