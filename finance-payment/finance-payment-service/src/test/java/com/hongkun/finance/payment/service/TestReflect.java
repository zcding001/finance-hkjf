package com.hongkun.finance.payment.service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class TestReflect {

    public static void main(String[] args) throws Exception {
        // PaymentInfo a = new PaymentInfo();
        // a.setAcct_name("kkkkkkkkkkk");
        // a.setBack_url("http://www.baidu.com");
        // Map<String, Object> mp = objToMap(a);
        // for (Map.Entry<String, Object> entry : mp.entrySet()) {
        // System.out.println(entry.getKey());
        // System.out.println(entry.getValue());
        // }
        // String str = objToStr(a, "&");
        // System.out.println(str);
        // 里面写自己的类名及路径
        Class<?> c = Class.forName("TestReflect");
        Object obj = c.newInstance();
        // 第一个参数写的是方法名,第二个\第三个\...写的是方法参数列表中参数的类型
        String ce = "cesh" + "i2";
        Method method = c.getMethod(ce, String.class, int.class);
        // invoke是执行该方法,并携带参数值
        String str2 = (String) method.invoke(obj, new Object[] {"myname", 4});
        System.out.println(str2);
    }

    public static Map<String, Object> objToMap(Object obj) {
        Map<String, Object> map = new HashMap<String, Object>();
        Field fields[] = obj.getClass().getDeclaredFields();
        String[] name = new String[fields.length];
        Object[] value = new Object[fields.length];
        try {
            Field.setAccessible(fields, true);
            for (int i = 0; i < name.length; i++) {
                name[i] = fields[i].getName();
                value[i] = fields[i].get(obj);
                if (!"serialVersionUID".equals(name[i]) && value[i] != null) {
                    map.put(name[i], value[i]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public static String objToStr(Object obj, String signFlag) {
        Map<String, Object> map = new HashMap<String, Object>();
        Field fields[] = obj.getClass().getDeclaredFields();
        String[] name = new String[fields.length];
        Object[] value = new Object[fields.length];
        String res = "";
        try {
            Field.setAccessible(fields, true);
            for (int i = 0; i < name.length; i++) {
                name[i] = fields[i].getName();
                value[i] = fields[i].get(obj);
                if (!"serialVersionUID".equals(name[i]) && value[i] != null) {
                    map.put(name[i], value[i]);
                    res += name[i] + "=" + URLEncoder.encode((String) value[i], "UTF-8") + signFlag;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "".equals(res) ? "" : StringUtils.chop(res);
    }

    public String ceshi2(String str, int i) {
        for (int j = 0; j < i; j++) {
            System.out.println(str + "22222");
        }

        return str;
    }
}
