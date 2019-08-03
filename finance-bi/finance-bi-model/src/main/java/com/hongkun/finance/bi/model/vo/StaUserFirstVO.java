package com.hongkun.finance.bi.model.vo;

import com.yirun.framework.core.model.BaseModel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * 统计VO
 *
 * @author zc.ding
 * @create 2018/9/18
 */
public class StaUserFirstVO extends BaseModel {
    /**
    * 新用户数量
    */
    private Integer newUserCount;
    /**
    * 新用户投资金额
    */
    private BigDecimal newInvestAmountSum;
    /**
    * 老用户人数
    */
    private Integer oldUserCount;
    /**
    * 老用户投资总金额
    */
    private BigDecimal oldInvestAmountSum;

    public Integer getNewUserCount() {
        return newUserCount;
    }

    public void setNewUserCount(Integer newUserCount) {
        this.newUserCount = newUserCount;
    }

    public BigDecimal getNewInvestAmountSum() {
        return newInvestAmountSum;
    }

    public void setNewInvestAmountSum(BigDecimal newInvestAmountSum) {
        this.newInvestAmountSum = newInvestAmountSum;
    }

    public Integer getOldUserCount() {
        return oldUserCount;
    }

    public void setOldUserCount(Integer oldUserCount) {
        this.oldUserCount = oldUserCount;
    }

    public BigDecimal getOldInvestAmountSum() {
        return oldInvestAmountSum;
    }

    public void setOldInvestAmountSum(BigDecimal oldInvestAmountSum) {
        this.oldInvestAmountSum = oldInvestAmountSum;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
    }
}
