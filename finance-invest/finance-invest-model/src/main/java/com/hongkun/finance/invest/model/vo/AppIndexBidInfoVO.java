package com.hongkun.finance.invest.model.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * app端首页标的信息的BidInfo
 */
public class AppIndexBidInfoVO extends IndexBidInfoVO{

      /**依次为月月盈，季季盈，年年盈*/
     List<BaseBidInfoVO> appLinedBidsInfo=new ArrayList<>();

    /**
     * 新手专享标的列表
     */
    private List<BaseBidInfoVO> greenhandBidInfo = new ArrayList<>();

    /**
     * 直投散标列表
     */
    private List<BaseBidInfoVO> commonBidInfo = new ArrayList<>();

    /**
     * 体验金列表
     */
    private List<BaseBidInfoVO> experienceBidInfo = new ArrayList<>();

    public List<BaseBidInfoVO> getGreenhandBidInfo() {
        return greenhandBidInfo;
    }

    public void setGreenhandBidInfo(List<BaseBidInfoVO> greenhandBidInfo) {
        this.greenhandBidInfo = greenhandBidInfo;
    }

    public List<BaseBidInfoVO> getExperienceBidInfo() {
        return experienceBidInfo;
    }

    public void setExperienceBidInfo(List<BaseBidInfoVO> experienceBidInfo) {
        this.experienceBidInfo = experienceBidInfo;
    }

    public List<BaseBidInfoVO> getAppLinedBidsInfo() {
        return appLinedBidsInfo;
    }

    public List<BaseBidInfoVO> getCommonBidInfo() {
        return commonBidInfo;
    }

    public void setCommonBidInfo(List<BaseBidInfoVO> commonBidInfo) {
        this.commonBidInfo = commonBidInfo;
    }

    public void setAppLinedBidsInfo(List<BaseBidInfoVO> appLinedBidsInfo) {
        this.appLinedBidsInfo = appLinedBidsInfo;
    }
}
