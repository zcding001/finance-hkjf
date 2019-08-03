package com.hongkun.finance.vas.constants;

import com.yirun.framework.core.utils.PropertiesHolder;

/**
 * @Description : app版本更新常量类
 * @Project : framework
 * @Program Name  : com.hongkun.finance.vas.constants.AppVersionConstants
 * @Author : pengwu@hongkun.com.cn 吴鹏
 */
public interface AppVersionConstants {

    /**
     * 下线状态
     */
    Integer APP_VERSION_STATE_DOWN = 0;
    /**
     * 上线状态
     */
    Integer APP_VERSION_STATE_UP = 1;
    /**
     * ios平台
     */
    String APP_VERSION_PLATFORM_IOS = "1";
    /**
     * android平台
     */
    String APP_VERSION_PLATFORM_ANDROID = "2";
}
