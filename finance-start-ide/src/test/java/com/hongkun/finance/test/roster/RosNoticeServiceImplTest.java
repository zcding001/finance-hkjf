package com.hongkun.finance.test.roster;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.roster.service.RosNoticeService;
import com.hongkun.finance.test.BaseTest;
import org.junit.Test;

/**
 * @Description : TODO
 * @Project : finance
 * @Program Name  : com.hongkun.finance.test.roster
 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
 */
public class RosNoticeServiceImplTest extends BaseTest {
    @Reference
    private RosNoticeService rosNoticeService;
    @Test
    public void getEmails(){
        String emails = rosNoticeService.getEmailsByType(1);
        System.out.print("====================="+emails+"==================");
    }

}
