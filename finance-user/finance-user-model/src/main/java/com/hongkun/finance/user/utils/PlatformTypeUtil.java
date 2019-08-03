package com.hongkun.finance.user.utils;

import static com.hongkun.finance.user.constants.UserConstants.USER_PLATFORM_CONSOLE_HKJF;
import static com.hongkun.finance.user.constants.UserConstants.USER_PLATFORM_CONSOLE_QKD;
import static com.hongkun.finance.user.constants.UserConstants.USER_PLATFORM_CONSOLE_QSH;

/**
 * @Description : 平台类型工具类
 * 0-乾坤袋,1-前生活,2-我的账户 3-鸿坤金服
 * @Project : finance
 * @Program Name  : com.hongkun.finance.user.utils.PlatformTypeUtil
 * @Author : zhongpingtang@yiruntz.com 唐忠平
 */
public class PlatformTypeUtil {
    /**
     * 获取当前平台前缀
     *
     * @param type
     * @return
     */
    public static String getPlatformPrefix(Integer type) {
        //TODO:zhongping 2017/5/27 扩充平台类型
        switch (type) {
            case 0:
                return USER_PLATFORM_CONSOLE_QKD;
            case 1:
                return USER_PLATFORM_CONSOLE_QSH;

            case 3:
                return USER_PLATFORM_CONSOLE_HKJF;
        }

        return null;
    }


    /**
     * 获取平台名称
     *
     * @return
     */
    public static String getPlatformName(Integer type) {
        //TODO:zhongping 2017/5/27 扩充平台类型
        switch (type) {
            case 0:
                return "乾坤袋";
            case 1:
                return "前生活";

            case 3:
                return "鸿坤金服";
        }

        return null;
    }


}
