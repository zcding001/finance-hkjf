package com.hongkun.finance.test.sms;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.sms.model.SmsEmailMsg;
import com.hongkun.finance.sms.model.SmsWebMsg;
import com.hongkun.finance.sms.service.SmsEmailMsgService;
import com.hongkun.finance.sms.service.SmsWebMsgService;
import com.hongkun.finance.test.BaseTest;
import org.junit.Test;

import java.util.List;

/**
 * TODO
 *
 * @author zc.ding
 * @create 2018/4/26
 */
public class TestSmsService extends BaseTest {
    
    @Reference
    private SmsWebMsgService smsWebMsgService;
    @Reference
    private SmsEmailMsgService smsEmailMsgService;
    
    @Test
    public void testFindSmsWebMsgList(){
        List<SmsWebMsg> list  = this.smsWebMsgService.findSmsWebMsgList(new SmsWebMsg());
        System.out.println(list.size());
    }
    
    @Test
    public void test(){
        SmsEmailMsg smsEmailMsg = new SmsEmailMsg();
        String email = "zhichaoding@hongkun.com.cn, xuhuiliu@hongkun.com.cn, xiaolingli@hongkun.com.cn";
        smsEmailMsg.setEmail(email);
        smsEmailMsg.setTitle("测试");
        smsEmailMsg.setMsg("刘旭辉帅小伙！");
        this.smsEmailMsgService.insertSmsEmailMsg(smsEmailMsg);
    }
    
}
