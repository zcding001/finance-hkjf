package com.hongkun.finance.roster.model;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.roster.model.RosStaffInfo.java
 * @Class Name : RosStaffInfo.java
 * @Description : GENERATOR VO类
 * @Author : generator
 */
public class RosStaffInfo extends BaseModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 描述: id 字段: id INT(10)
	 */
	private java.lang.Integer id;

	/**
	 * 描述: 用户Id 字段: reg_user_id INT(10)
	 */
	private java.lang.Integer regUserId;

	/**
	 * 描述: 手机号 字段: login BIGINT(19)
	 */
	private java.lang.Long login;

	/**
	 * 描述: 真实姓名 字段: real_name VARCHAR(20) 默认值: ''
	 */
	private java.lang.String realName;

	/**
	 * 描述: 所属企业id 字段: enterprise_reg_user_id INT(10)
	 */
	private java.lang.Integer enterpriseRegUserId;

	/**
	 * 描述: 企业名称 字段: enterprise_real_name VARCHAR(30) 默认值: ''
	 */
	private java.lang.String enterpriseRealName;

	/**
	 * 描述: 类型1：物业员工，2：销售员工，3：内部员工' 字段: type TINYINT(3) 默认值: 0
	 */
	private Integer type;

	/**
	 * 描述: 0:删除，1：正常 字段: state TINYINT(3) 默认值: 1
	 */
	private Integer state;

	/**
	 * 描述: 创建时间 字段: create_time DATETIME(19) 默认值: CURRENT_TIMESTAMP
	 */
	private java.util.Date createTime;

	// 【非数据库字段，查询时使用】
	private java.util.Date createTimeBegin;

	// 【非数据库字段，查询时使用】
	private java.util.Date createTimeEnd;
	/**
	 * 描述: 更新时间 字段: modify_time TIMESTAMP(19) 默认值: CURRENT_TIMESTAMP
	 */
	private java.util.Date modifyTime;

	// 【非数据库字段，查询时使用】
	private java.util.Date modifyTimeBegin;

	// 【非数据库字段，查询时使用】
	private java.util.Date modifyTimeEnd;
	/**
	 * 用户集合
	 */
	private List<Integer> regUserIdList;
	/**
	 * 类型集合
	 */
	private List<Integer> types;
	/**
	 * 0:不发放推荐奖，1：发放推荐奖
	 */
	private Integer recommendState;
	

	public RosStaffInfo() {
	}

	public RosStaffInfo(java.lang.Integer id) {
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

	public void setLogin(java.lang.Long login) {
		this.login = login;
	}

	public java.lang.Long getLogin() {
		return this.login;
	}

	public void setRealName(java.lang.String realName) {
		this.realName = realName;
	}

	public java.lang.String getRealName() {
		return this.realName;
	}

	public void setEnterpriseRegUserId(java.lang.Integer enterpriseRegUserId) {
		this.enterpriseRegUserId = enterpriseRegUserId;
	}

	public java.lang.Integer getEnterpriseRegUserId() {
		return this.enterpriseRegUserId;
	}

	public void setEnterpriseRealName(java.lang.String enterpriseRealName) {
		this.enterpriseRealName = enterpriseRealName;
	}

	public java.lang.String getEnterpriseRealName() {
		return this.enterpriseRealName;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getType() {
		return this.type;
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

	public List<Integer> getRegUserIdList() {
		return regUserIdList;
	}

	public void setRegUserIdList(List<Integer> regUserIdList) {
		this.regUserIdList = regUserIdList;
	}

	public List<Integer> getTypes() {
		return types;
	}

	public void setTypes(List<Integer> types) {
		this.types = types;
	}

    public Integer getRecommendState() {
        return recommendState;
    }

    public void setRecommendState(Integer recommendState) {
        this.recommendState = recommendState;
    }

    @Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}
