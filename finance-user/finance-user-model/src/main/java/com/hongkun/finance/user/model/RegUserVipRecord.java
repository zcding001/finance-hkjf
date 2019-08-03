package com.hongkun.finance.user.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;

import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.user.model.RegUserVipRecord.java
 * @Class Name    : RegUserVipRecord.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class RegUserVipRecord extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: id
	 * 字段: id  INT(10)
	 */
	private Integer id;
	
	/**
	 * 描述: regUserId
	 * 字段: reg_user_id  INT(10)
	 */
	private Integer regUserId;
	
	/**
	 * 描述: 总资产
	 * 字段: now_money  DECIMAL(20)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal nowMoney;
	
	/**
	 * 描述: 不满足VIP标准的次数
	 * 字段: times  TINYINT(3)
	 * 默认值: 1
	 */
	private Integer times;
	
	/**
	 * 描述: createTime
	 * 字段: create_time  DATETIME(19)
	 * 默认值: CURRENT_TIMESTAMP
	 */
	private java.util.Date createTime;
	
	//【非数据库字段，查询时使用】
	private java.util.Date createTimeBegin;
	
	//【非数据库字段，查询时使用】
	private java.util.Date createTimeEnd;
	
	private List<Integer> regUserIds;
	private List<Integer> ids;
 
	public RegUserVipRecord(){
	}

	public RegUserVipRecord(Integer id){
		this.id = id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public void setRegUserId(Integer regUserId) {
		this.regUserId = regUserId;
	}
	
	public Integer getRegUserId() {
		return this.regUserId;
	}
	
	public void setNowMoney(java.math.BigDecimal nowMoney) {
		this.nowMoney = nowMoney;
	}
	
	public java.math.BigDecimal getNowMoney() {
		return this.nowMoney;
	}
		
	public void setTimes(Integer times) {
		this.times = times;
	}
	
	public Integer getTimes() {
		return this.times;
	}
	
	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}
	
	public java.util.Date getCreateTime() {
		return this.createTime;
	}
	
	public void setCreateTimeBegin(java.util.Date createTimeBegin) {
		this.createTimeBegin = createTimeBegin;
	}
	
	public java.util.Date getCreateTimeBegin() {
		return this.createTimeBegin;
	}
	
	public void setCreateTimeEnd(java.util.Date createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
	}
	
	public java.util.Date getCreateTimeEnd() {
		return this.createTimeEnd;
	}

    public List<Integer> getRegUserIds() {
        return regUserIds;
    }

    public void setRegUserIds(List<Integer> regUserIds) {
        this.regUserIds = regUserIds;
    }

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }

    @Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}

