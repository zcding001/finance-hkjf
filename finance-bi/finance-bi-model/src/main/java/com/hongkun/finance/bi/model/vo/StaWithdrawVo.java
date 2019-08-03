package com.hongkun.finance.bi.model.vo;

import com.yirun.framework.core.model.BaseModel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

public class StaWithdrawVo extends BaseModel {
    
 private static final long serialVersionUID = 1L;
    
    /**
    * 提现总金额
    */
    private BigDecimal withdrawAmountSum;
    /**
    * 提现次数
    */
    private Integer withdrawCount;
    

   
    public BigDecimal getWithdrawAmountSum() {
        return withdrawAmountSum;
    }



    public void setWithdrawAmountSum(BigDecimal withdrawAmountSum) {
        this.withdrawAmountSum = withdrawAmountSum;
    }



    public Integer getWithdrawCount() {
        return withdrawCount;
    }



    public void setWithdrawCount(Integer withdrawCount) {
        this.withdrawCount = withdrawCount;
    }



    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
    }
}
