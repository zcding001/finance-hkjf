package com.hongkun.finance.payment.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.hongkun.finance.payment.util.HttpClientMessageSender;

public class TestHttp {

    public static void main(String[] args) {
        // String reqStr =
        // "[{'oid_partner':'201408071000001543','card_no':'6212260200053518219','sign':'69a5258d7f161c40d42049fe9d38c731','sign_type':'MD5'}]";
        //
        // String reqStr2 =
        // "[{'oid_partner':'201408071000001543','offset':'0','user_id':'55490126-cc54-11e4-90b2-d89d67270c78','sign':'31d5a4334f04587607b31d2c58a68c4d','pay_type':'D','sign_type':'MD5','platform':'201408071000001543'}]";
        // String resJosn = HttpClientMessageSender
        // .sendPostJson("https://queryapi.lianlianpay.com/bankcardbin.htm", reqStr, null);
        // System.out.println(("银行卡卡bin信息查询响应报文[" + resJosn + "]"));
        //
        // JSONObject reqObj = new JSONObject();
        // reqObj.put("oid_partner", "201408071000001543");
        // reqObj.put("user_id", "55490126-cc54-11e4-90b2-d89d67270c78");
        // reqObj.put("platform", "201408071000001543");
        // reqObj.put("offset", "0");
        // reqObj.put("sign_type", "MD5");
        // reqObj.put("pay_type", "D");
        // String sign = LLPayMD5Util.addSign(reqObj,
        // "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOilN4tR7HpNYvSBra/DzebemoAiGtGeaxa+qebx/O2YAdUFPI+xTKTX2ETyqSzGfbxXpmSax7tXOdoa3uyaFnhKRGRvLdq1kTSTu7q5s6gTryxVH2m62Py8Pw0sKcuuV0CxtxkrxUzGQN+QSxf+TyNAv5rYi/ayvsDgWdB3cRqbAgMBAAECgYEAj02d/jqTcO6UQspSY484GLsL7luTq4Vqr5L4cyKiSvQ0RLQ6DsUG0g+Gz0muPb9ymf5fp17UIyjioN+ma5WquncHGm6ElIuRv2jYbGOnl9q2cMyNsAZCiSWfR++op+6UZbzpoNDiYzeKbNUz6L1fJjzCt52w/RbkDncJd2mVDRkCQQD/Uz3QnrWfCeWmBbsAZVoM57n01k7hyLWmDMYoKh8vnzKjrWScDkaQ6qGTbPVL3x0EBoxgb/smnT6/A5XyB9bvAkEA6UKhP1KLi/ImaLFUgLvEvmbUrpzY2I1+jgdsoj9Bm4a8K+KROsnNAIvRsKNgJPWd64uuQntUFPKkcyfBV1MXFQJBAJGs3Mf6xYVIEE75VgiTyx0x2VdoLvmDmqBzCVxBLCnvmuToOU8QlhJ4zFdhA1OWqOdzFQSw34rYjMRPN24wKuECQEqpYhVzpWkA9BxUjli6QUo0feT6HUqLV7O8WqBAIQ7X/IkLdzLa/vwqxM6GLLMHzylixz9OXGZsGAkn83GxDdUCQA9+pQOitY0WranUHeZFKWAHZszSjtbe6wDAdiKdXCfig0/rOdxAODCbQrQs7PYy1ed8DuVQlHPwRGtokVGHATU=",
        // "201408071000001543test_20140812");
        // reqObj.put("sign", sign);
        // String sts = reqObj.toString();
        //
        //
        // String resJosns = HttpClientMessageSender
        // .sendPostJson("https://queryapi.lianlianpay.com/bankcardbindlist.htm", sts, null);
        // System.out.println(("=========银行卡卡bin信息查询响应报文[" + resJosns + "]"));
       /* Map<String, Object> map = new HashMap<String, Object>();
        map.put("kk", "kkk222222222222222222222222222222222");
        String res = HttpClientMessageSender.doPostForm(
                "http://127.0.0.1:8080/yrtz/accPandectController.do?method=accPandectlist", map,
                null);
        System.out.println(res);*/
        
        List<String> list=new ArrayList<String>();
        for (int i = 0; i < 10; i++) {
            list.add("" + i);
        }
        
        System.out.println(list.contains("5"));
        
        Predicate<String> pred = (p) -> Integer.parseInt(p)>5;

        
        List<String> st=list.stream().filter(p-> {
            if(p.equals("5")){
                return false;
            }
            return true;
        }).collect(Collectors.toList());
        System.out.println(st.toString());
        list.stream().filter(pred).forEach(e->{
            System.out.println(e);
        });
        
        System.out.println(list.stream().count());
    }

}
