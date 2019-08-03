package com.hongkun.finance.invest.model.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 用于前台首页信息展示的vo
 * @author penn.z.tang
 */
public class IndexBidInfoVO implements Serializable {

    /**
     * 新手专享标的
     */
    private BaseBidInfoVO greenhandBid;
    /**
     * 爆款标的
     */
    private BaseBidInfoVO hotBid;
    /**
     * 推荐标的
     */
    private BaseBidInfoVO recommendBid;

    /**
     * 体验金理财
     */
    private BaseBidInfoVO experienceBid;


    /**
     * 按月付息-顺序月月盈 ，季季盈 ，年年盈
     */
    private List<BaseBidInfoVO> repayByMonthBid;

    /**
     * 一次本息-月月盈 ，季季盈 ，年年盈
     */
    private List<BaseBidInfoVO> repayOnceBid;


    public BaseBidInfoVO getGreenhandBid() {
        return greenhandBid;
    }

    public void setGreenhandBid(BaseBidInfoVO greenhandBid) {
        this.greenhandBid = greenhandBid;
    }

    public BaseBidInfoVO getHotBid() {
        return hotBid;
    }

    public void setHotBid(BaseBidInfoVO hotBid) {
        this.hotBid = hotBid;
    }

    public BaseBidInfoVO getRecommendBid() {
        return recommendBid;
    }

    public void setRecommendBid(BaseBidInfoVO recommendBid) {
        this.recommendBid = recommendBid;
    }

    public BaseBidInfoVO getExperienceBid() {
        return experienceBid;
    }

    public void setExperienceBid(BaseBidInfoVO experienceBid) {
        this.experienceBid = experienceBid;
    }

    public List<BaseBidInfoVO> getRepayByMonthBid() {
        return repayByMonthBid;
    }

    public void setRepayByMonthBid(List<BaseBidInfoVO> repayByMonthBid) {
        this.repayByMonthBid = repayByMonthBid;
    }

    public List<BaseBidInfoVO> getRepayOnceBid() {
        return repayOnceBid;
    }

    public void setRepayOnceBid(List<BaseBidInfoVO> repayOnceBid) {
        this.repayOnceBid = repayOnceBid;
    }
}


