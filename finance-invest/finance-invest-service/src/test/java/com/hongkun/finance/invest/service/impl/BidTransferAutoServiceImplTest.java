package com.hongkun.finance.invest.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.invest.model.BidInfo;
import com.hongkun.finance.invest.model.BidInvest;
import com.hongkun.finance.invest.service.BidInfoService;
import com.hongkun.finance.invest.service.BidInvestService;
import com.hongkun.finance.invest.service.BidTransferAutoService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext-invest.xml",
        "classpath:spring/applicationContext-dubbo-provider.xml"})
public class BidTransferAutoServiceImplTest {

    @Reference
    private BidTransferAutoService bidTransferAutoService;
    @Reference
    private BidInfoService bidInfoService;
    @Reference
    private BidInvestService bidInvestService;
    @Test
    public void addTransferAuto() {
    	System.out.println("============addTransferAuto test begin============================");
    	BidInfo bidInfo = bidInfoService.findBidInfoById(4);
    	BidInvest oldBidInvest = bidInvestService.findBidInvestById(96);
    	BidInvest newBidInvest = bidInvestService.findBidInvestById(97);
    	List<BidInvest> oldList = new ArrayList<BidInvest>();
    	oldList.add(oldBidInvest);
    	List<BidInvest> newList = new ArrayList<BidInvest>();
    	newList.add(newBidInvest);
    	bidTransferAutoService.addTransferAuto(bidInfo, oldList, newList, new Date(), 1);
    	System.out.println("============addTransferAuto test end============================");
    }
    
    @Test
    public void addTransferRecord() {
    	System.out.println("============addTransferRecord test begin============================");
    	BidInfo old1 = bidInfoService.findBidInfoById(2);
    	BidInfo old2 = bidInfoService.findBidInfoById(7);
    	BidInfo new1 = bidInfoService.findBidInfoById(4);
    	BidInfo new2 = bidInfoService.findBidInfoById(8);
    	
    	BidInfo comnBidInfo = bidInfoService.findBidInfoById(5);
    	List<BidInfo> oldList = new ArrayList<BidInfo>();
    	oldList.add(old1);
    	oldList.add(old2);
    	List<BidInfo> newList = new ArrayList<BidInfo>();
    	newList.add(new1);
    	newList.add(new2);
    	bidTransferAutoService.addTransferRecord(oldList, newList, new Date(), comnBidInfo);
    	System.out.println("============addTransferRecord test end============================");
    }
}
