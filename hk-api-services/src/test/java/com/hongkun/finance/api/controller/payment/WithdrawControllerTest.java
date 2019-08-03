package com.hongkun.finance.api.controller.payment;

import com.hongkun.finance.api.controller.BaseControllerTest;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description : 提现测试类
 * @Project : finance
 * @Program Name  : com.hongkun.finance.api.controller.payment.WithdrawControllerTest.java
 * @Author : maruili on  2018/3/21 11:18
 */
public class WithdrawControllerTest extends BaseControllerTest {
    private final static String CONTROLLER = "withdrawController/";
    @Test
    public void toWithdrawTest() throws Exception {
        Map<String, String> params = new HashMap<>();
        super.doTest(CONTROLLER + "toWithdraw", params,78);
    }
    @Test
    public void applyWithdrawTest() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("transMoney", "100");
        params.put("platformSource", "12");
        super.doTest(CONTROLLER + "applyWithdraw", params,78);
    }
}
