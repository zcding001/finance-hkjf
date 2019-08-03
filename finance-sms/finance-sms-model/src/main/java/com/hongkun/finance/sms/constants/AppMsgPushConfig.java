package com.hongkun.finance.sms.constants;

import com.yirun.framework.core.utils.PropertiesHolder;

/**
 * @Description : app消息推送配置信息
 * @Project : framework
 * @Program Name  : com.hongkun.finance.sms.constants.AppMsgPushConfig
 * @Author : pengwu@hongkun.com.cn 吴鹏
 */
public class AppMsgPushConfig {

    private static String push_appid;

    private static String push_appkey;

    private static String push_mastersecret;

    private static String push_host;

    static {
        push_appid = PropertiesHolder.getProperty("push.appid");
        push_appkey = PropertiesHolder.getProperty("push.appkey");
        push_mastersecret = PropertiesHolder.getProperty("push.mastersecret");
        push_host = PropertiesHolder.getProperty("push.host");
    }

    public static String getPushAppid() {
        return push_appid;
    }

    public static String getPushAppkey() {
        return push_appkey;
    }

    public static String getPushMastersecret() {
        return push_mastersecret;
    }

    public static String getPushHost() {
        return push_host;
    }

}
