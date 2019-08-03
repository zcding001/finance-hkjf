package com.hongkun.finance.vas.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.vas.model.VasVipGradeStandard.java
 * @Class Name    : VasVipGradeStandard.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class VasVipGradeStandard extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: id
	 * 字段: id  INT(10)
	 */
	private Integer id;
	
	/**
	 * 描述: 会员等级：0-LV0，1-LV1，2-LV2，3-LV3，4-LV4，5-LV5，6-LV6，7-LV7，8-LV8，9-LV9
	 * 字段: level  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer level;
	
	/**
	 * 描述: 该等级成长值下限
	 * 字段: growth_val_min  INT(10)
	 * 默认值: 0
	 */
	private Integer growthValMin;
	
	/**
	 * 描述: 该等级成长值上限
	 * 字段: growth_val_max  INT(10)
	 * 默认值: 0
	 */
	private Integer growthValMax;
	
	/**
	 * 描述: 启用状态：0-不启用，1-启用
	 * 字段: state  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer state;
	
	/**
	 * 描述: 创建时间
	 * 字段: create_time  DATETIME(19)
	 * 默认值: CURRENT_TIMESTAMP
	 */
	private java.util.Date createTime;
	
	//【非数据库字段，查询时使用】
	private java.util.Date createTimeBegin;
	
	//【非数据库字段，查询时使用】
	private java.util.Date createTimeEnd;
	/**
	 * 描述: 修改时间
	 * 字段: modify_time  DATETIME(19)
	 * 默认值: CURRENT_TIMESTAMP
	 */
	private java.util.Date modifyTime;
	
	//【非数据库字段，查询时使用】
	private java.util.Date modifyTimeBegin;
	
	//【非数据库字段，查询时使用】
	private java.util.Date modifyTimeEnd;
 
	public VasVipGradeStandard(){
	}

	public VasVipGradeStandard(Integer id){
		this.id = id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public void setLevel(Integer level) {
		this.level = level;
	}
	
	public Integer getLevel() {
		return this.level;
	}
	
	public void setGrowthValMin(Integer growthValMin) {
		this.growthValMin = growthValMin;
	}
	
	public Integer getGrowthValMin() {
		return this.growthValMin;
	}
	
	public void setGrowthValMax(Integer growthValMax) {
		this.growthValMax = growthValMax;
	}
	
	public Integer getGrowthValMax() {
		return this.growthValMax;
	}
	
	public void setState(Integer state) {
		this.state = state;
	}
	
	public Integer getState() {
		return this.state;
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
	public void setModifyTime(java.util.Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	
	public java.util.Date getModifyTime() {
		return this.modifyTime;
	}
	
	public void setModifyTimeBegin(java.util.Date modifyTimeBegin) {
		this.modifyTimeBegin = modifyTimeBegin;
	}
	
	public java.util.Date getModifyTimeBegin() {
		return this.modifyTimeBegin;
	}
	
	public void setModifyTimeEnd(java.util.Date modifyTimeEnd) {
		this.modifyTimeEnd = modifyTimeEnd;
	}
	
	public java.util.Date getModifyTimeEnd() {
		return this.modifyTimeEnd;
	}	
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}

