package com.hongkun.finance.vas.model.dto;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * @Description : 赠送卡券给用户封装获取数据
 * @Project : framework
 * @Program Name  : com.hongkun.finance.vas.model.dto.DistributeCouponDTO
 * @Author : pengwu@hongkun.com.cn 吴鹏
 */
public class DistributeCouponDTO{
    /**
     * 目标用户id集合
     */
    @NotNull(message = "请至少指定一个用户")
    private List<Integer> userIds;

    /**
     * 目标卡券产品id集合
     */
    @NotNull(message = "请至少选择一个卡券产品")
    private List<Integer> couponIds;

    /**
     * 发送原因
     */
    @NotNull(message = "请输入发送原因")
    private String reason;

    public List<Integer> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Integer> userIds) {
        this.userIds = userIds;
    }

    public List<Integer> getCouponIds() {
        return couponIds;
    }

    public void setCouponIds(List<Integer> couponIds) {
        this.couponIds = couponIds;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "DistributeCouponDTO{" +
                "userIds=" + userIds +
                ", couponIds=" + couponIds +
                ", reason='" + reason + '\'' +
                '}';
    }
}
