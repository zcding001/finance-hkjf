package com.hongkun.finance.roster.vo;

import java.math.BigDecimal;
import java.util.List;

import com.yirun.framework.core.model.BaseModel;

public class RosStaffInfoVo extends BaseModel {
	private static final long serialVersionUID = 1L;
	
	private java.lang.Integer id;
	//员工id
	private java.lang.Integer regUserId;
	//员工手机号
	private java.lang.Long login;
	//员工姓名
	private java.lang.String realName;
	//企业id
	private java.lang.Integer enterpriseRegUserId;
	//企业名称
	private java.lang.String enterpriseRealName;
	//员工类型
	private Integer type;
    //状态：0-删除 1-正常
	private Integer state;
	//推荐累计投金额
	private BigDecimal investSumMoney;
	//推荐累计投金额(折标后)
	private BigDecimal investBackStepMoney;
	//开始时间
	private java.util.Date startTime;
	//结束时间
	private java.util.Date endTime;
	//发放推荐奖状态 0-不发放，1-发放
	private Integer recommendState;
	
	public Integer getRecommendState() {
        return recommendState;
    }
    public void setRecommendState(Integer recommendState) {
        this.recommendState = recommendState;
    }
    /**
     * 企业ID集合
     */
    private List<Integer> enterpriseRegUserIdList;
    
    
	public List<Integer> getEnterpriseRegUserIdList() {
        return enterpriseRegUserIdList;
    }
    public void setEnterpriseRegUserIdList(List<Integer> enterpriseRegUserIdList) {
        this.enterpriseRegUserIdList = enterpriseRegUserIdList;
    }
    public java.lang.Integer getId() {
		return id;
	}
	public void setId(java.lang.Integer id) {
		this.id = id;
	}
	public java.lang.Integer getRegUserId() {
		return regUserId;
	}
	public void setRegUserId(java.lang.Integer regUserId) {
		this.regUserId = regUserId;
	}
	public java.lang.Long getLogin() {
		return login;
	}
	public void setLogin(java.lang.Long login) {
		this.login = login;
	}
	public java.lang.String getRealName() {
		return realName;
	}
	public void setRealName(java.lang.String realName) {
		this.realName = realName;
	}
	public java.lang.Integer getEnterpriseRegUserId() {
		return enterpriseRegUserId;
	}
	public void setEnterpriseRegUserId(java.lang.Integer enterpriseRegUserId) {
		this.enterpriseRegUserId = enterpriseRegUserId;
	}
	public java.lang.String getEnterpriseRealName() {
		return enterpriseRealName;
	}
	public void setEnterpriseRealName(java.lang.String enterpriseRealName) {
		this.enterpriseRealName = enterpriseRealName;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public BigDecimal getInvestSumMoney() {
		return investSumMoney;
	}
	public void setInvestSumMoney(BigDecimal investSumMoney) {
		this.investSumMoney = investSumMoney;
	}
	public BigDecimal getInvestBackStepMoney() {
		return investBackStepMoney;
	}
	public void setInvestBackStepMoney(BigDecimal investBackStepMoney) {
		this.investBackStepMoney = investBackStepMoney;
	}
	public java.util.Date getStartTime() {
		return startTime;
	}
	public void setStartTime(java.util.Date startTime) {
		this.startTime = startTime;
	}
	public java.util.Date getEndTime() {
		return endTime;
	}
	public void setEndTime(java.util.Date endTime) {
		this.endTime = endTime;
	}
		
	
}
