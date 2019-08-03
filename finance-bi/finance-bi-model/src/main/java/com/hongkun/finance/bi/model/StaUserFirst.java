package com.hongkun.finance.bi.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;

import java.math.BigDecimal;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.bi.model.StaUserFirst.java
 * @Class Name    : StaUserFirst.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class StaUserFirst extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: 自增ID
	 * 字段: id  INT UNSIGNED(10)
	 */
	private Integer id;
	
	/**
	 * 描述: 用户注册的年月日
	 * 字段: da  DATETIME(19)
	 * 默认值: 0000-00-00 00:00:00.000000
	 */
	private java.util.Date da;
	
	//【非数据库字段，查询时使用】
	private java.util.Date daBegin;
	
	//【非数据库字段，查询时使用】
	private java.util.Date daEnd;
	/**
	 * 描述: regUserId
	 * 字段: reg_user_id  INT(10)
	 */
	private Integer regUserId;
	
	/**
	 * 描述: 注册来源:1-qkd(pc),2-qkd(wap),3-qkd(ios),4-qkd(andriod),11-qsh(pc),12-qsh(wap),13-qsh(ios),14-qsh(andriod),21-hkjf(pc), 22-hkjf(wap), 23-hkjf(ios),24-hkjf(andriod), 31-cxj(pc), 32-cxj(wap), 33-cxj(ios),34-cxj(andriod), 99(导入)'
	 * 字段: reg_source  TINYINT(3)
	 * 默认值: 22
	 */
	private Integer regSource;
	
	/**
	 * 描述: 推广来源
	 * 字段: exten_source  VARCHAR(10)
	 * 默认值: ''
	 */
	private String extenSource;
	
	/**
	 * 描述: 用户手机号
	 * 字段: login  BIGINT(19)
	 * 默认值: 0
	 */
	private Long login;
	
	/**
	 * 描述: 首次投资时间
	 * 字段: invest_time  DATETIME(19)
	 * 默认值: 0000-00-00 00:00:00
	 */
	private java.util.Date investTime;
	
	//【非数据库字段，查询时使用】
	private java.util.Date investTimeBegin;
	
	//【非数据库字段，查询时使用】
	private java.util.Date investTimeEnd;
	/**
	 * 描述: 首次投资金额
	 * 字段: invest_money  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal investMoney;
	
	/**
	 * 描述: 累计投资次数
	 * 字段: invest_times  INT(10)
	 * 默认值: 0
	 */
	private Integer investTimes;
	
	/**
	 * 描述: 累计投资金额
	 * 字段: invest_amount  DECIMAL(20)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal investAmount;
	
	/**
	 * 描述: 首次充值时间
	 * 字段: rechange_time  DATETIME(19)
	 * 默认值: 0000-00-00 00:00:00
	 */
	private java.util.Date rechangeTime;
	
	//【非数据库字段，查询时使用】
	private java.util.Date rechangeTimeBegin;
	
	//【非数据库字段，查询时使用】
	private java.util.Date rechangeTimeEnd;
	/**
	 * 描述: 首次充值金额
	 * 字段: rechange_money  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal rechangeMoney;
	
	/**
	 * 描述: 实名时间
	 * 字段: real_name_time  DATETIME(19)
	 * 默认值: 0000-00-00 00:00:00
	 */
	private java.util.Date realNameTime;
	
	//【非数据库字段，查询时使用】
	private java.util.Date realNameTimeBegin;
	
	//【非数据库字段，查询时使用】
	private java.util.Date realNameTimeEnd;
	/**
	 * 描述: 身份证号
	 * 字段: id_card  VARCHAR(20)
	 * 默认值: ''
	 */
	private String idCard;
	
	/**
	* 注册人数
	*/
	private Integer userCount;
	/**
	* 实名人数
	*/
	private Integer realNameCount;
	/**
	* 投资人数
	*/
	private Integer investCount;
	/**
	* 充值人数
	*/
	private Integer rechangeCount;
	/**
	* 投资总金额
	*/
	private BigDecimal investAmountSum; 
	
 
	public StaUserFirst(){
	}

	public StaUserFirst(Integer id){
		this.id = id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public void setDa(java.util.Date da) {
		this.da = da;
	}
	
	public java.util.Date getDa() {
		return this.da;
	}
	
	public void setDaBegin(java.util.Date daBegin) {
		this.daBegin = daBegin;
	}
	
	public java.util.Date getDaBegin() {
		return this.daBegin;
	}
	
	public void setDaEnd(java.util.Date daEnd) {
		this.daEnd = daEnd;
	}
	
	public java.util.Date getDaEnd() {
		return this.daEnd;
	}	
	public void setRegUserId(Integer regUserId) {
		this.regUserId = regUserId;
	}
	
	public Integer getRegUserId() {
		return this.regUserId;
	}
	
	public void setRegSource(Integer regSource) {
		this.regSource = regSource;
	}
	
	public Integer getRegSource() {
		return this.regSource;
	}
	
	public void setExtenSource(String extenSource) {
		this.extenSource = extenSource;
	}
	
	public String getExtenSource() {
		return this.extenSource;
	}
	
	public void setLogin(Long login) {
		this.login = login;
	}
	
	public Long getLogin() {
		return this.login;
	}
	
	public void setInvestTime(java.util.Date investTime) {
		this.investTime = investTime;
	}
	
	public java.util.Date getInvestTime() {
		return this.investTime;
	}
	
	public void setInvestTimeBegin(java.util.Date investTimeBegin) {
		this.investTimeBegin = investTimeBegin;
	}
	
	public java.util.Date getInvestTimeBegin() {
		return this.investTimeBegin;
	}
	
	public void setInvestTimeEnd(java.util.Date investTimeEnd) {
		this.investTimeEnd = investTimeEnd;
	}
	
	public java.util.Date getInvestTimeEnd() {
		return this.investTimeEnd;
	}	
	public void setInvestMoney(java.math.BigDecimal investMoney) {
		this.investMoney = investMoney;
	}
	
	public java.math.BigDecimal getInvestMoney() {
		return this.investMoney;
	}
	
	public void setInvestTimes(Integer investTimes) {
		this.investTimes = investTimes;
	}
	
	public Integer getInvestTimes() {
		return this.investTimes;
	}
	
	public void setInvestAmount(java.math.BigDecimal investAmount) {
		this.investAmount = investAmount;
	}
	
	public java.math.BigDecimal getInvestAmount() {
		return this.investAmount;
	}
	
	public void setRechangeTime(java.util.Date rechangeTime) {
		this.rechangeTime = rechangeTime;
	}
	
	public java.util.Date getRechangeTime() {
		return this.rechangeTime;
	}
	
	public void setRechangeTimeBegin(java.util.Date rechangeTimeBegin) {
		this.rechangeTimeBegin = rechangeTimeBegin;
	}
	
	public java.util.Date getRechangeTimeBegin() {
		return this.rechangeTimeBegin;
	}
	
	public void setRechangeTimeEnd(java.util.Date rechangeTimeEnd) {
		this.rechangeTimeEnd = rechangeTimeEnd;
	}
	
	public java.util.Date getRechangeTimeEnd() {
		return this.rechangeTimeEnd;
	}	
	public void setRechangeMoney(java.math.BigDecimal rechangeMoney) {
		this.rechangeMoney = rechangeMoney;
	}
	
	public java.math.BigDecimal getRechangeMoney() {
		return this.rechangeMoney;
	}
	
	public void setRealNameTime(java.util.Date realNameTime) {
		this.realNameTime = realNameTime;
	}
	
	public java.util.Date getRealNameTime() {
		return this.realNameTime;
	}
	
	public void setRealNameTimeBegin(java.util.Date realNameTimeBegin) {
		this.realNameTimeBegin = realNameTimeBegin;
	}
	
	public java.util.Date getRealNameTimeBegin() {
		return this.realNameTimeBegin;
	}
	
	public void setRealNameTimeEnd(java.util.Date realNameTimeEnd) {
		this.realNameTimeEnd = realNameTimeEnd;
	}
	
	public java.util.Date getRealNameTimeEnd() {
		return this.realNameTimeEnd;
	}	
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	
	public String getIdCard() {
		return this.idCard;
	}

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getUserCount() {
        return userCount;
    }

    public void setUserCount(Integer userCount) {
        this.userCount = userCount;
    }

    public Integer getRealNameCount() {
        return realNameCount;
    }

    public void setRealNameCount(Integer realNameCount) {
        this.realNameCount = realNameCount;
    }

    public Integer getInvestCount() {
        return investCount;
    }

    public void setInvestCount(Integer investCount) {
        this.investCount = investCount;
    }

    public Integer getRechangeCount() {
        return rechangeCount;
    }

    public void setRechangeCount(Integer rechangeCount) {
        this.rechangeCount = rechangeCount;
    }

    public BigDecimal getInvestAmountSum() {
        return investAmountSum;
    }

    public void setInvestAmountSum(BigDecimal investAmountSum) {
        this.investAmountSum = investAmountSum;
    }

    @Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}

