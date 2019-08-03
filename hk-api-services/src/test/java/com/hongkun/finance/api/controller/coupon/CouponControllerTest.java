package com.hongkun.finance.api.controller.coupon;

import com.hongkun.finance.api.controller.BaseControllerTest;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description : 卡券controller测试类
 * @Project : framework
 * @Program Name  : com.hongkun.finance.api.controller.coupon.CouponControllerTest
 * @Author : pengwu@hongkun.com.cn 吴鹏
 */
public class CouponControllerTest extends BaseControllerTest{

    private static final String COUPON_CONTROLLER = "/couponController/";

    @Test
    public void getCouponDetailList(){
        Map<String,String> param = new HashMap<>();
        param.put("state","2");
        param.put("currentPage","1");
        param.put("pageSize","30");
        doTest(COUPON_CONTROLLER + "getCouponDetailList",param,32);
    }

    @Test
    public void activeCouponDetail(){
        Map<String,String> param = new HashMap<>();
        param.put("key","K-U85PF96HRFW4");
        doTest(COUPON_CONTROLLER + "activeCouponDetail",param,32);
    }

    @Test
    public void donationCouponDetail(){
        Map<String,String> param = new HashMap<>();
        param.put("couponDetailId","4");
        param.put("recipientId","1");
        doTest(COUPON_CONTROLLER + "donationCouponDetail",param,32);
    }

    @Test
    public void getCouponWithdrawCount(){
        doTest(COUPON_CONTROLLER + "getCouponWithdrawCount",32);
    }

    @Test
    public void getBidCouponDetailList(){
        Map<String,String> param = new HashMap<>();
        param.put("bidId","6");
        doTest(COUPON_CONTROLLER + "getBidCouponDetailList",param,32);
    }

}
