package com.hongkun.finance.bi.model.vo;

import com.yirun.framework.core.model.BaseModel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

public class StaQdzInOutVo extends BaseModel {
    
 private static final long serialVersionUID = 1L;
    
    /**
    * 转入总金额
    */
    private BigDecimal inAmountSum;
    /**
     * 转出总金额
     */
    private BigDecimal outAmountSum;
    /**
    * 转入次数
    */
    private Integer inCount;
    /**
     * 转出次数
     */
    private Integer outCount;
   
   
    public BigDecimal getInAmountSum() {
        return inAmountSum;
    }


    public void setInAmountSum(BigDecimal inAmountSum) {
        this.inAmountSum = inAmountSum;
    }


    public BigDecimal getOutAmountSum() {
        return outAmountSum;
    }


    public void setOutAmountSum(BigDecimal outAmountSum) {
        this.outAmountSum = outAmountSum;
    }


    public Integer getInCount() {
        return inCount;
    }


    public void setInCount(Integer inCount) {
        this.inCount = inCount;
    }


    public Integer getOutCount() {
        return outCount;
    }


    public void setOutCount(Integer outCount) {
        this.outCount = outCount;
    }


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
    }
}
