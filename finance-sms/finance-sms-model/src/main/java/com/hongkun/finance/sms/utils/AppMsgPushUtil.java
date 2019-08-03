package com.hongkun.finance.sms.utils;

import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.AppMessage;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.exceptions.RequestException;
import com.gexin.rp.sdk.http.IGtPush;
import com.hongkun.finance.sms.constants.AppMsgPushConfig;
import com.yirun.framework.core.utils.PropertiesHolder;

/**
 * @Description : app消息推送工具类
 * @Project : framework
 * @Program Name  : com.hongkun.finance.sms.utils.AppMsgPushUtil
 * @Author : pengwu@hongkun.com.cn 吴鹏
 */
public class AppMsgPushUtil {

    /**主内存中单例的个推实体*/
    private volatile static IGtPush push;
    /**同步锁*/
    private static final Object LOCK = new Object();

    private static IGtPush getIGtPush(){
        if (push == null){
            synchronized (LOCK){
                if (push == null){
                    try{
                        push = new IGtPush(AppMsgPushConfig.getPushHost(),AppMsgPushConfig.getPushAppkey(),
                                AppMsgPushConfig.getPushMastersecret());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
        return push;
    }

    public static IPushResult pushMessageToSingle(SingleMessage singleMessage,Target target)throws RequestException {
        return getIGtPush().pushMessageToSingle(singleMessage,target);
    }

    public static IPushResult pushMessageToSingle(SingleMessage singleMessage,Target target,String requestId){
        return getIGtPush().pushMessageToSingle(singleMessage, target, requestId);
    }

    public static IPushResult pushMessageToApp(AppMessage message,String taskGroupName){
        return getIGtPush().pushMessageToApp(message, taskGroupName);
    }
}
