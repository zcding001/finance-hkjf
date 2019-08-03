package com.hongkun.finance.fund.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yirun.framework.core.utils.HttpClientUtils;
import com.yirun.framework.core.utils.crypto.MD5;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description : 调用百度api进行翻译的工具类
 * @Project : framework
 * @Program Name  : com.hongkun.finance.web.controller.fund.TranslateUtil
 * @Author : pengwu@hongkun.com.cn 吴鹏
 */
public class TranslateUtil {
    private final String TRANS_API_HOST = "http://api.fanyi.baidu.com/api/trans/vip/translate";

    private final String appid = "20180625000179821";
    private final String securityKey = "omkx1s9pAR_dd_qlsQKR";
    private static volatile TranslateUtil instance;
    private static final Object lock = new Object();
    private TranslateUtil(){

    }

    public static TranslateUtil getInstance(){
        if (instance == null){
            synchronized (lock){
                if (instance == null){
                    instance = new TranslateUtil();
                }
            }
        }
        return instance;
    }

    public static void main(String[] args) {
        TranslateUtil.getInstance().cnToEn("鸿坤广场写字楼");
    }

    public String cnToEn(String cn){
        //如果翻译的内容为null或是""，则返回""
        if (StringUtils.isBlank(cn)){
            return "";
        }
        Map<String,String> params = buildParams(cn,"auto","en");

        String data = HttpClientUtils.httpPost(TRANS_API_HOST,params);
        //如果翻译出问题，返回异常信息
        JSONObject dataObject = JSON.parseObject(data);
        if (StringUtils.isNotBlank(dataObject.getString("error_code"))){
            //翻译异常返回提示信息
            throw new TranslateException(dataObject.getString("error_code") + "|" + dataObject.getString("error_msg"));
        }else {
            return dataObject.getJSONArray("trans_result").getJSONObject(0).getString("dst");
        }
    }

    private Map<String,String> buildParams(String query,String from,String to){
        Map<String, String> params = new HashMap<>();
        params.put("q", query);
        params.put("from", from);
        params.put("to", to);

        params.put("appid", appid);
        // 随机数
        String salt = String.valueOf(System.currentTimeMillis());
        params.put("salt", salt);
        // 加密前的原文
        String src = appid + query + salt + securityKey;
        params.put("sign", MD5.encrypt(src));

        return params;
    }
}
