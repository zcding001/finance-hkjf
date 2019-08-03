package com.hongkun.finance.user.support.youdun;

/***
 * @Description 公共资源 - 有盾参数配置文件
 * @author xuchao
 * @version 0.1
 * 
 * */
public class YouDunConstant {
    
	//实名认证
	public static final String YOUDUN_VERSION = "youdun.version";//友盾版本号
	public static final String YOUDUN_MERCHANT = "youdun.merchant"; //商户号码
	public static final String YOUDUN_MD5_KEY = "youdun.md5.key";  //原始key值
	public static final String YOUDUN_REAL_NAME_AUTH_URL = "youdun.real.name.auth.url";//访问地址
	public static final String YOUDUN_BUS_TYPE = "youdun.bus_type"; //业务类型
	public static final String YOUDUN_BANKCARDBIN_QUERY = "youdun.bankcardbin.query";//友盾查询卡信息URL
	public static final String YOUDUN_BANKCARDBIN_QUERY_TYPE = "youdun.bankcardbin.query.type";//友盾查询卡bin业务类型
	public static final String YOUDUN_VERYFYBANKINFO_URL="youdun.veryfybankinfo.url"; //三要素验证地址
	public static final String YOUDUN_VERIFY_BANKINFO="youdun.verify.bankinfo";  //绑卡三要素认证业务类型

	public static String YOUDUN_VIRIFYID_URL="youdun.virieyid.url"; //两要素验证地址  add 2018-4-25 升级版
	public static String YOUDUN_SECURITY_KEY="youdun.security.key";//两要素验证密钥  add 2018-4-25 升级版
	public static String YOUDUN_PUB_KEY="youdun.pub.key";//两要素验证key  add 2018-4-25 升级版

}
