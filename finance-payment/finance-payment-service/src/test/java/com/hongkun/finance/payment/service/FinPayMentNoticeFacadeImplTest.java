package com.hongkun.finance.payment.service;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.payment.facade.FinPayMentNoticeFacade;

/**
 * @Description : 协议支付通知测试类
 * @Project : finance
 * @Program Name  : com.hongkun.finance.payment.service.impl
 * @Author : binliang@hongkun.com.cn 梁彬
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext-payment.xml"})
public class FinPayMentNoticeFacadeImplTest {
	
    @Reference
    private FinPayMentNoticeFacade finPayMentNoticeFacade;
    
    @Test
    public void noticeAgreementPay(){  //认证异步Facade
//    	baofuProtocolRechargeAsyncNotifyUrl 宝付协议异步通知参数：resp_code=S&terminal_id=40268&member_id=1216626&biz_resp_code=0000&biz_resp_msg=交易成功&order_id=1951139114&trans_id=PY1011002018052214440903870194&succ_amt=1&succ_time=2018-05-22 14:44:09&signature=0a06212937acae59e74a477ae22b99543a2426dc173bc0b7e8458b57f6e98a98aa444f39130cc9e74a8c90321562789cd630a7ada430d90f76df9994aa50e7959e6139a784f8a0b61865005e7b0be02e891a325082cefb3c6ffc2393303b3ca59408f39ff82857fe140430601e2e44ce738f86159459d19f5f43ebafeb01b7bc
    	Map<String, String> map = new TreeMap<String, String>();
    	map.put("resp_code", "S");
		map.put("terminal_id", "40268");
		map.put("member_id", "1216626");
		map.put("biz_resp_code", "0000");
		map.put("biz_resp_msg", "交易成功");
		map.put("order_id", "1951139114");
		map.put("trans_id", "PY1011002018052214440903870194");
		map.put("succ_amt", "1");
		map.put("succ_time", "2018-05-22 14:44:09");
		map.put("signature", "0a06212937acae59e74a477ae22b99543a2426dc173bc0b7e8458b57f6e98a98aa444f39130cc9e74a8c90321562789cd630a7ada430d90f76df9994aa50e7959e6139a784f8a0b61865005e7b0be02e891a325082cefb3c6ffc2393303b3ca59408f39ff82857fe140430601e2e44ce738f86159459d19f5f43ebafeb01b7bc");
		 
		this.finPayMentNoticeFacade.baofuAgreePayAsyncNotifyUrl(map, 0);
    }
    
	  
    
}
