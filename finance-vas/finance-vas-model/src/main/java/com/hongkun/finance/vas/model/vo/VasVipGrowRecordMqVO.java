package com.hongkun.finance.vas.model.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * @Description : 会员等级成长值记录用于MQ传递的对象
 * @Project : framework
 * @Program Name  : com.hongkun.finance.vas.model.vo.VasVipGrowRecordMqVO
 * @Author : pengwu@hongkun.com.cn 吴鹏
 */
public class VasVipGrowRecordMqVO implements Serializable{

    
	private static final long serialVersionUID = 1L;

	/**用户id*/
    private Integer userId;

    /**成长值类型*/
    private Integer growType;

    /**投资天数*/
    private Integer investDay;

    /**投资金额*/
    private Double investMoney;

    /**成长值*/
    private Integer growValue;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getGrowType() {
        return growType;
    }

    public void setGrowType(Integer growType) {
        this.growType = growType;
    }

    public Integer getInvestDay() {
        return investDay;
    }

    public void setInvestDay(Integer investDay) {
        this.investDay = investDay;
    }

    public Double getInvestMoney() {
        return investMoney;
    }

    public void setInvestMoney(Double investMoney) {
        this.investMoney = investMoney;
    }

    public Integer getGrowValue() {
        return growValue;
    }

    public void setGrowValue(Integer growValue) {
        this.growValue = growValue;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
    }
}
