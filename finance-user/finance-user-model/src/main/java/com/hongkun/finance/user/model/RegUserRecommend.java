package com.hongkun.finance.user.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.user.model.RegUserRecommend.java
 * @Class Name    : RegUserRecommend.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class RegUserRecommend extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: 主键id
	 * 字段: id  INT UNSIGNED(10)
	 */
	private java.lang.Integer id;
	
	/**
	 * 描述: 用户id
	 * 字段: reg_user_id  INT UNSIGNED(10)
	 * 默认值: 0
	 */
	private java.lang.Integer regUserId;
	
	/**
	 * 描述: 推荐人id
	 * 字段: recommend_userid  INT UNSIGNED(10)
	 * 默认值: 0
	 */
	private java.lang.Integer recommendUserid;
	
	/**
	 * 描述: 二级推荐人id
	 * 字段: two_recommend_userid  INT UNSIGNED(10)
	 * 默认值: 0
	 */
	private java.lang.Integer twoRecommendUserid;
	
	/**
	 * 描述: 投资时间
	 * 字段: invest_time  DATETIME(19)
	 * 默认值: CURRENT_TIMESTAMP
	 */
	private java.util.Date investTime;
	
	//【非数据库字段，查询时使用】
	private java.util.Date investTimeBegin;
	
	//【非数据库字段，查询时使用】
	private java.util.Date investTimeEnd;
	/**
	 * 描述: 状态:0-删除,1-正常
	 * 字段: state  TINYINT(3)
	 * 默认值: 1
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
 
	public RegUserRecommend(){
	}

	public RegUserRecommend(java.lang.Integer id){
		this.id = id;
	}

	public void setId(java.lang.Integer id) {
		this.id = id;
	}
	
	public java.lang.Integer getId() {
		return this.id;
	}
	
	public void setRegUserId(java.lang.Integer regUserId) {
		this.regUserId = regUserId;
	}
	
	public java.lang.Integer getRegUserId() {
		return this.regUserId;
	}
	
	public void setRecommendUserid(java.lang.Integer recommendUserid) {
		this.recommendUserid = recommendUserid;
	}
	
	public java.lang.Integer getRecommendUserid() {
		return this.recommendUserid;
	}
	
	public void setTwoRecommendUserid(java.lang.Integer twoRecommendUserid) {
		this.twoRecommendUserid = twoRecommendUserid;
	}
	
	public java.lang.Integer getTwoRecommendUserid() {
		return this.twoRecommendUserid;
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

