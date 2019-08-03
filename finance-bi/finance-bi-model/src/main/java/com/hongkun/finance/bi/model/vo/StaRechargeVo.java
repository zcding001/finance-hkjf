package com.hongkun.finance.bi.model.vo;

import com.yirun.framework.core.model.BaseModel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

public class StaRechargeVo extends BaseModel {
 private static final long serialVersionUID = 1L;
    
    /**
    * 充值总金额
    */
    private BigDecimal rechargeAmountSum;
    /**
    * 充值次数
    */
    private Integer rechargeCount;
    

    public BigDecimal getRechargeAmountSum() {
        return rechargeAmountSum;
    }

    public void setRechargeAmountSum(BigDecimal rechargeAmountSum) {
        this.rechargeAmountSum = rechargeAmountSum;
    }

    public Integer getRechargeCount() {
        return rechargeCount;
    }

    public void setRechargeCount(Integer rechargeCount) {
        this.rechargeCount = rechargeCount;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
    }
}
