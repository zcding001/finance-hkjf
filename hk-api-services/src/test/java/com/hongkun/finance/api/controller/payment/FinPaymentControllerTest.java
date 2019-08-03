package com.hongkun.finance.api.controller.payment;

import com.hongkun.finance.api.controller.BaseControllerTest;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description : 交易记录及充值提现记录测试类
 * @Project : finance
 * @Program Name  : com.hongkun.finance.api.controller.payment.FinPaymentControllerTest.java
 * @Author : maruili on  2018/3/21 10:59
 */
public class FinPaymentControllerTest extends BaseControllerTest {
    private final static String CONTROLLER = "finPaymentController/";
    @Test
    public void listPaymentRecordTest() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("currentPage", "1");
        params.put("pageSize", "10");
        params.put("tradeType", "10");//10. 充值 14 提现
//        params.put("state", "21");//1.划转中 2 成功 3 失败
//        params.put("createTimeBegin", "2017-01-01");
//        params.put("createTimeEnd", "2019-01-01");
//        params.put("maxTransMoney", "21");
//        params.put("minTransMoney", "21");
        super.doTest(CONTROLLER + "listPaymentRecord", params,78);
    }
    @Test
    public void listTransactionRecordTest() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("currentPage", "1");
        params.put("pageSize", "10");
        //params.put("tradeType", "10");//1.充值, 2.提现，3.投资,4.钱袋子,5.其它
        params.put("createTimeBegin", "2017-01-01");
        params.put("createTimeEnd", "2019-01-01");
        super.doTest(CONTROLLER + "listTransactionRecord", params,78);
    }
}
