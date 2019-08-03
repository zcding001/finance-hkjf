package com.hongkun.finance.payment.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.payment.model.FinBankRefer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @Description : TODO
 * @Project : finance
 * @Program Name  : com.hongkun.finance.payment.service
 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext-payment.xml"})
public class FinBankReferServiceImplTest {

    @Reference
    private FinBankReferService finBankReferService;

    @Test
    public void findBankInfo(){
        List<FinBankRefer> list =  finBankReferService.findBankInfo("baofuProtocol","RZ","1");
        for (FinBankRefer fb:list){
            System.out.print("========================="+fb.getBankThirdCode());
        }
        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
