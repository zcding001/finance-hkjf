package com.hongkun.finance.activity.model.vo;

import com.hongkun.finance.activity.model.LotteryItem;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: 活动奖项vo
 * @Program: com.hongkun.finance.activity.model.vo.LotteryActivityItemsVo
 * @Author: yunlongliu@hongkun.com.cn
 * @Date: 2018-10-08 09:54
 **/
public class LotteryActivityItemsVo implements Serializable {

    private static final long serialVersionUID = 1L;

    // 奖品分组
    private Integer itemGroup;
    // 是否为京籍
    private Integer locationFlag;
    private List<LotteryItem> lotteryItems;

    public Integer getLocationFlag() {
        return locationFlag;
    }

    public void setLocationFlag(Integer locationFlag) {
        this.locationFlag = locationFlag;
    }

    public Integer getItemGroup() {
        return itemGroup;
    }

    public void setItemGroup(Integer itemGroup) {
        this.itemGroup = itemGroup;
    }

    public LotteryActivityItemsVo(Integer itemGroup, Integer locationFlag,List<LotteryItem> lotteryItems) {
        this.itemGroup = itemGroup;
        this.locationFlag = locationFlag;
        this.lotteryItems = lotteryItems;
    }

    public List<LotteryItem> getLotteryItems() {
        return lotteryItems;
    }

    public void setLotteryItems(List<LotteryItem> lotteryItems) {
        this.lotteryItems = lotteryItems;
    }


}
