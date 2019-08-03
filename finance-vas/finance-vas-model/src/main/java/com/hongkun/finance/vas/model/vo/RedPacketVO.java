package com.hongkun.finance.vas.model.vo;


import java.io.Serializable;
import java.math.BigDecimal;


public class RedPacketVO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer redPacketUserNum;
	private Integer  redPacketTimes;
    private BigDecimal redPacketMoneySum;
    public Integer getRedPacketUserNum() {
        return redPacketUserNum;
    }
    public void setRedPacketUserNum(Integer redPacketUserNum) {
        this.redPacketUserNum = redPacketUserNum;
    }
    
    public Integer getRedPacketTimes() {
        return redPacketTimes;
    }
    public void setRedPacketTimes(Integer redPacketTimes) {
        this.redPacketTimes = redPacketTimes;
    }
    public BigDecimal getRedPacketMoneySum() {
        return redPacketMoneySum;
    }
    public void setRedPacketMoneySum(BigDecimal redPacketMoneySum) {
        this.redPacketMoneySum = redPacketMoneySum;
    }
    
}
