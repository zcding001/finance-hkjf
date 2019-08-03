package com.hongkun.finance.api.controller.redpacket;

import com.hongkun.finance.api.controller.BaseControllerTest;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description : 红包controller测试类
 * @Project : framework
 * @Program Name  : com.hongkun.finance.api.controller.redpacket.RedPacketControllerTest
 * @Author : pengwu@hongkun.com.cn 吴鹏
 */
public class RedPacketControllerTest extends BaseControllerTest{

    private static final String RED_PACKET_CONTROLLER = "/redPacketController/";

    @Test
    public void getRedPacketInfoList(){
        Map<String,String> param = new HashMap<>();
        param.put("state","2");
        param.put("currentPage","1");
        param.put("pageSize", "20");
        doTest(RED_PACKET_CONTROLLER + "getRedPacketInfoList",param,32);
    }

    @Test
    public void exchangeRedPacketInfo(){
        Map<String,String> param = new HashMap<>();
        param.put("key","PXBW4TWEAW");
        param.put("typeValue","11");
        doTest(RED_PACKET_CONTROLLER + "exchangeRedPacketInfo",param,32);
    }
}
