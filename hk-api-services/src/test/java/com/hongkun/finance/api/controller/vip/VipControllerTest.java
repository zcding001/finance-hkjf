package com.hongkun.finance.api.controller.vip;

import com.hongkun.finance.api.controller.BaseControllerTest;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description : 会员等级controller测试类
 * @Project : framework
 * @Program Name  : com.hongkun.finance.api.controller.vip.VipControllerTest
 * @Author : pengwu@hongkun.com.cn 吴鹏
 */
public class VipControllerTest extends BaseControllerTest{

    private static final String VIP_CONTROLLER = "/vipController/";

    @Test
    public void getVipGrowRecordList(){
        Map<String,String> param = new HashMap<>();
        param.put("currentPage","2");
        param.put("pageSize","20");
        doTest(VIP_CONTROLLER + "getVipGrowRecordList",param,32);
    }

    @Test
    public void getVipTreatMentListDescription(){
        doTest(VIP_CONTROLLER + "getVipTreatMentListDescription",32);
    }

    @Test
    public void getVipTreatMentListDescriptionByLevel(){
        Map<String,String> param = new HashMap<>();
        param.put("level","0");
        doTest(VIP_CONTROLLER + "getVipTreatMentListDescriptionByLevel",param,32);
    }

    @Test
    public void getVipGrowRuleListDescription(){
        doTest(VIP_CONTROLLER + "getVipGrowRuleListDescription",32);
    }

    @Test
    public void getVipIntroductionList(){
        doTest(VIP_CONTROLLER + "getVipIntroductionList",32);
    }

    @Test
    public void  getUserVipInfo(){
        doTest(VIP_CONTROLLER + "getUserVipInfo",1140);
    }
}
