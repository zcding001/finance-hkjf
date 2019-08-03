package com.hongkun.finance.api.controller;

import java.util.HashMap;
import java.util.Map;

/**
 * 使用缓存池管理的测试
 */
public abstract class ControllerImitatior extends BaseControllerTest {

    /**
     * 获取基本路径
     *
     * @return
     */
    public abstract String getBaseUrl();


    /**
     * 默认UserId
     */
    private static final int user_id = 32;
    

    /**
     * 提交测试的方法
     * @param args
     */
    protected void commitParams(Object ...args) {
        Map<String,String> paramsMap=new HashMap<>(32);
        // 获得当前方法名
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        //得到测试的url
        String testUrl = getBaseUrl() + methodName;
        //构造参数map
        //把测试参数加入参数map中
        if (args.length>0) {
            for (Object arg : args) {
                paramsMap.putAll(StringBeanMap.getBeanMap(arg));
            }
        }
        doTest(testUrl, paramsMap, user_id);
    }

    

}
