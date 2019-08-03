package com.hongkun.finance.activity.util;

import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.utils.NetUtils;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;

/**
 * @Description: 活动工具类
 * @Program: com.hongkun.finance.activity.util.ActivityUtils
 * @Author: yunlongliu@hongkun.com.cn
 * @Date: 2018-10-15 13:58
 **/
public class ActivityUtils {

    private static Logger logger = LoggerFactory.getLogger(ActivityUtils.class);

    /**
     *  @Description    ：随机生成N位0-9组合
     *  @Method_Name    ：creEightNo
     *  @param length
     *  @return java.lang.String
     *  @Creation Date  ：2018年10月15日 13:59
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    public static String creEightNo(int length) {
        String rad = "0123456789";
        StringBuffer result = new StringBuffer();
        Random rand = new Random();
        for (int i = 0; i < length; i++) {
            int randNum = rand.nextInt(10);
            result.append(rad.substring(randNum, randNum + 1));
        }
        return result.toString();
    }

    /**
     *  @Description    ： 获取京籍/非京籍
     *  @Method_Name    ：getUserLocationFlag
     *  @param request
     *  @return java.lang.Integer
     *  @Creation Date  ：2018年10月15日 15:38
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    public static Integer getUserLocationFlag(HttpServletRequest request) throws GeneralException {

        Integer locationFlag = 1;
        try {
            String strIP = NetUtils.getRequestIp(request);
            URL url = new URL("http://api.map.baidu.com/location/ip?ak=F454f8a5efe5e577997931cc01de3974&ip=" + strIP);
            URLConnection conn = url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String line = null;
            StringBuilder result = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            reader.close();
            JSONObject obj1 = JSONObject.parseObject(result.toString());
            if ("0".equals(obj1.get("status").toString())) {
                JSONObject obj2 = JSONObject.parseObject(obj1.get("content").toString());
                JSONObject obj3 = JSONObject.parseObject(obj2.get("address_detail").toString());
                String city = obj3.get("city").toString();
                logger.info("ip所在城市 -->" + city);
                locationFlag = "北京市".equals(city)? 1:2;
            } else {
                throw new GeneralException("获取用户IP失败 ");
            }
        } catch (Exception e) {
            logger.error("获取用户IP失败 ",e);
            throw new GeneralException("获取用户IP失败");
        }
        return locationFlag;
    }






}
