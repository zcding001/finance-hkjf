package com.hongkun.finance.sms.constants;

public class SmsConstants {
	
	/**
	 * 通知类型消息：短信、站内信、邮件
	 */
	public static final int SMS_TYPE_NOTICE = 1;
	/**
	 * 系统类型消息：推广类短信、系统通知类站内信、系统通知类邮件
	 */
	public static final int SMS_TYPE_SYS_NOTICE = 2;
	/**
	 * 验证码类型短信
	 */
	public static final int SMS_TYPE_VALIDATE_CODE = 3;
	/**
	 * 初始状态
	 */
	public static final int SMS_TEL_STATE_INIT = 0;
	/**
	 * 发送正常
	 */
	public static final int SMS_TEL_STATE_SUCCESS = 1;
	/**
	 * 验证码已使用
	 */
	public static final int SMS_TEL_STATE_USE = 2;
	/**
	 * 短信重发成功
	 */
	public static final int SMS_TEL_STATE_RETRY_SUCCESS = 3;
    /**
     * 登录短息验证码前缀
     */
    public static final String REDIS_SMS_LOGIN_PREFIX = "LOGIN_";
	/**
	 * 单用户redis中验证码短信的数量
	 */
	public static final String REDIS_SMS_COUNT = "SMS_COUNT_";
	/**
	 * redis短信验证码前缀
	 */
	public static final String REDIS_SMS_PRFFIX = "SMS_CODE_";
	/**
	 * 短信验证码在redis中的默认有效时间(60s)
	 */
	public static final int REDIS_SMS_EXPIRES_TIME = 60;
	
	//	消息模板前缀
	public static final String SMS_MSG_TEMPLATE_PREFIX = "sms.msg.template.prefix";
	
	//	短信消息队列
	public static final String MQ_QUEUE_MSG_TEL = "default_msg_tel";
	//	邮件消息队列
	public static final String MQ_QUEUE_MSG_EMAIL = "defalut_msg_email";
	//	站内信消息队列
	public static final String MQ_QUEUE_MSG_WEB = "default_msg_web";
	//	app消息推送队列
	public static final String MQ_QUEUE_MSG_PUSH = "default_msg_push";
	//	默认管理员CODE
	public static final String DEFAULT_CREATE_USER_CODE = "default.admin.user.code";
	//	默认管理员名称
	public static final String DEFAULT_CREATE_USER_NAME = "default.admin.user.name";
	//app消息推送待发送状态
	public static final int SMS_APP_MSG_STATE_SEND = 1;
	//app消息推送发送成功状态
	public static final int SMS_APP_MSG_STATE_SUCCESS = 2;
	//app消息推送发送失败状态
	public static final int SMS_APP_MSG_STATE_FAIL = 3;
	//app消息推送发送停止状态
	public static final int SMS_APP_MSG_STATE_STOP = 4;
	//app消息推送发送删除状态
	public static final int SMS_APP_MSG_STATE_DELETE = 5;
	//app消息推送类型-默认打开app
	public static final int SMS_APP_MSG_TYPE_DEFAULT = 0;
	//app消息推送类型-商户收款
	public static final int SMS_APP_MSG_TYPE_MERCHANT = 1;
	//app消息推送类型-卡券转增
	public static final int SMS_APP_MSG_TYPE_COUPON = 2;
	//app消息推送类型-跳转链接
	public static final int SMS_APP_MSG_TYPE_LINK = 3;
	//app消息推送类型-好友投资通知
	public static final int SMS_APP_MSG_TYPE_NOTICE = 4;
}
