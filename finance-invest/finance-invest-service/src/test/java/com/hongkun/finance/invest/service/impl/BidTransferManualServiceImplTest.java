package com.hongkun.finance.invest.service.impl;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.invest.model.BidTransferManual;
import com.hongkun.finance.invest.service.BidTransferManualService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext-invest.xml",
        "classpath:spring/applicationContext-dubbo-provider.xml"})
public class BidTransferManualServiceImplTest {

    @Reference
    private BidTransferManualService bidTransferManualService;

    @Test
    public void query() {
        BidTransferManual bidT = bidTransferManualService.findBidTransferManualById(6);
        bidT.getId();
        Assert.assertTrue(6 == bidT.getId());

    }

    @Test
    public void insertTest() {
        BidTransferManual bidTransferManual = new BidTransferManual();
        bidTransferManual.setBuyUserId(1);
        bidTransferManual.setInvestUserId(2);
        bidTransferManual.setBidInfoId(1);
        bidTransferManualService.insertBidTransferManual(bidTransferManual);
        // bidTransferManualService.buyCreditor(1, 1, "");
    }
}
