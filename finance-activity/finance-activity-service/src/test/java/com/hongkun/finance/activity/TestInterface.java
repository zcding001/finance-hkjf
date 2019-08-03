package com.hongkun.finance.activity;

import com.hongkun.finance.activity.model.LotteryActivity;
import com.hongkun.finance.activity.service.LotteryActivityService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration(locations = { "classpath*:spring/applicationContext-activity.xml" })
public class TestInterface {

    @Autowired
    private LotteryActivityService lotteryActivityService;

    @Test
    public void findLotteryActivityById() {
        LotteryActivity result = lotteryActivityService.findLotteryActivityById(11);
        System.out.println("test");
    }
}