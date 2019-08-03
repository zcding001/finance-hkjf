package com.hongkun.finance.invest.model.dto;

import com.hongkun.finance.invest.model.BidInfo;
import com.hongkun.finance.invest.model.BidInvest;
import com.hongkun.finance.point.model.PointProcessorKeys;
import com.yirun.framework.jms.KeyAssignedAware;

import java.io.Serializable;
import java.util.List;

/**
 * @Description :
 * @Project : finance
 * @Program Name  : com.hongkun.finance.point.consumer.PointKeys
 * @Author : zhongpingtang@yiruntz.com 唐忠平
 */
public class InvestPointDTO implements KeyAssignedAware,Serializable {

    private static final long serialVersionUID = 7350537051632310117L;

    private BidInfo bidInfo;

    private List<BidInvest> bidInvests;

    public InvestPointDTO(BidInfo bidInfo, List<BidInvest> bidInvests) {
        this.bidInfo = bidInfo;
        this.bidInvests = bidInvests;
    }

    public InvestPointDTO() {
    }

    public BidInfo getBidInfo() {
        return bidInfo;
    }

    public void setBidInfo(BidInfo bidInfo) {
        this.bidInfo = bidInfo;
    }

    public List<BidInvest> getBidInvests() {
        return bidInvests;
    }

    public void setBidInvests(List<BidInvest> bidInvests) {
        this.bidInvests = bidInvests;
    }

    @Override
    public String getAssignedKey() {
        return PointProcessorKeys.INVEST_POINT_KEY;
    }
}
