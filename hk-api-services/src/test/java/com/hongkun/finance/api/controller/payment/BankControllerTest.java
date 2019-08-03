package com.hongkun.finance.api.controller.payment;

import com.hongkun.finance.api.controller.BaseControllerTest;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description : 银行卡维护测试类
 * @Project : finance
 * @Program Name  : com.hongkun.finance.api.controller.payment.BankControllerTest.java
 * @Author : maruili on  2018/3/20 14:02
 */
public class BankControllerTest extends BaseControllerTest {
    private final static String CONTROLLER = "bankController/";
    @Test
    public void getBankInfoTest() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("id", "21");
        super.doTest(CONTROLLER + "getBankInfo", params,78);
    }
    @Test
    public  void isBindBankTest() throws Exception{
        Map<String, String> params = new HashMap<>();
        super.doTest(CONTROLLER + "isBindBank", params,78);
    }
    @Test
    public  void getBankCardListTest() throws Exception{
        Map<String, String> params = new HashMap<>();
        params.put("state", "1");
        super.doTest(CONTROLLER + "getBankCardList", params,78);
        params = new HashMap<>();
        params.put("state", "2");
        super.doTest(CONTROLLER + "getBankCardList", params,78);
        params = new HashMap<>();
        super.doTest(CONTROLLER + "getBankCardList", params,78);
    }
    @Test
    public  void bindingCardTest() throws Exception{
        Map<String, String> params = new HashMap<>();
        params.put("bankCard", "6222020200106704621");
        params.put("bankCode", "ICBC");
        params.put("bankName", "中国工商银行");
        params.put("proviceCode", "110000");
        params.put("cityCode", "110105");
        params.put("branchName", "中国工商银行分行名称");
        params.put("platformSource", "12");
        super.doTest(CONTROLLER + "bindingCard", params,78);
    }
    @Test
    public  void updateBankCardTest() throws Exception{
        Map<String, String> params = new HashMap<>();
        params.put("id", "21");
        params.put("proviceCode", "110000");
        params.put("cityCode", "110107");
        params.put("branchName", "中国工商银行大兴支行");
        super.doTest(CONTROLLER + "updateBankCard", params,78);
        params = new HashMap<>();

        params.put("id", "18");
        params.put("bankCard", "6222020200106704622");
        params.put("bankCode", "ICBC");
        params.put("bankName", "中国民生银行");
        params.put("proviceCode", "110000");
        params.put("cityCode", "110107");
        params.put("branchName", "中国工商银行丰台支行");
        super.doTest(CONTROLLER + "updateBankCard", params,78);
    }

}
