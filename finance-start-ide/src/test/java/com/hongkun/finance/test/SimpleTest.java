package com.hongkun.finance.test;

import com.hongkun.finance.payment.util.PaymentUtil;
import com.yirun.framework.core.exception.BaseException;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.utils.HttpClientUtils;
import net.sf.json.JSONObject;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO
 *
 * @author zc.ding
 * @create 2018/5/29
 */
public class SimpleTest {
    
    @Test
    public void test1(){
        System.out.println(GeneralException.class.isAssignableFrom(RuntimeException.class));
        System.out.println(RuntimeException.class.isAssignableFrom(GeneralException.class));
        System.out.println(GeneralException.class.isAssignableFrom(BaseException.class));
        System.out.println(BaseException.class.isAssignableFrom(GeneralException.class));
    }
    
    @Test
    public void test2(){
        Map<String, Object> data = new HashMap<>();
        data.put("login", "13894849844");
        String msg = HttpClientUtils.httpPost("http://localhost:8081/hkjf/index.do?method=getRecommendInfo", data);
        System.out.println(msg);
        JSONObject json = JSONObject.fromObject(msg);
        System.out.println(json.get("recommendFlag"));
        System.out.println(json.get("login"));
        System.out.println(json.get("secret"));
        System.out.println(json.get("type"));
    }
    
    @Test
    public void test3(){
//        String msg = {"login":"18910684114","pwd":"AF8F9DFFA5D420FBC249141645B962EE","recommendFlag":1,"sign":"8dd9fd16192e304b59abc207528b0638","sign_type":"MD5","type":1}
        com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject();
        json.put("login", "18910684114");
        json.put("pwd", "AF8F9DFFA5D420FBC249141645B962EE");
        json.put("recommendFlag", 1);
        json.put("sign", "4aa6024accba9585cd1b8e0a2f93792a");
        json.put("sign_type", "MD5");
        json.put("type", 1);
        System.out.println(PaymentUtil.checkSignByMd5(json.toJSONString()));
    }
}
