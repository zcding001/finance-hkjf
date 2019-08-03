package com.hongkun.finance.roster.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.annotation.Union;
import com.yirun.framework.core.model.BaseModel;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.roster.model.RosDepositInfo.java
 * @Class Name    : RosDepositInfo.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class RosDepositInfo extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: id
	 * 字段: id  INT(10)
	 */
	@Union(changeAbleIfHasValue = false)
	private java.lang.Integer id;
	
	/**
	 * 描述: 用户id
	 * 字段: reg_user_id  INT(10)
	 */
	private java.lang.Integer regUserId;
	
	/**
	 * 描述: 标的id
	 * 字段: bid_id  INT(10)
	 */
	private java.lang.Integer bidId;
	
	/**
	 * 描述: 意向金
	 * 字段: money  DECIMAL(20)
	 */
	@Union(changeAbleIfHasValue = false)
	private java.math.BigDecimal money;
	
	/**
	 * 描述: 状态  0：删除 1：正常
	 * 字段: state  TINYINT(3)
	 * 默认值: 1
	 */
	@Union(changeAbleIfHasValue = false)
	private Integer state;
	
	/**
	 * 描述: 1:购房宝，2：物业宝
	 * 字段: type  TINYINT(3)
	 * 默认值: 1
	 */
	@Union(changeAble = false, changeAbleIfHasValue = false)
	private Integer type;
	
	/**
	 * 描述: 创建时间
	 * 字段: create_time  TIMESTAMP(19)
	 * 默认值: CURRENT_TIMESTAMP
	 */
	private java.util.Date createTime;
	
	//【非数据库字段，查询时使用】
	private java.util.Date createTimeBegin;
	
	//【非数据库字段，查询时使用】
	private java.util.Date createTimeEnd;
	/**
	 * 描述: 更新时间
	 * 字段: modify_time  TIMESTAMP(19)
	 * 默认值: CURRENT_TIMESTAMP
	 */
	private java.util.Date modifyTime;
	
	//【非数据库字段，查询时使用】
	private java.util.Date modifyTimeBegin;
	
	//【非数据库字段，查询时使用】
	private java.util.Date modifyTimeEnd;
	
	/**手机号*/
	private Long login;
	/**真实姓名*/
	private String realName;
	/**身份证号**/
	private String idCard;
	/**标的名称**/
	@Union(reNameTo = "name")
	private String bidName;
	private List<Integer> userIds = new ArrayList<>();
	private List<Integer> bidIds = new ArrayList<>();
	
	public RosDepositInfo(){
	}

	public RosDepositInfo(java.lang.Integer id){
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
	
	public void setBidId(java.lang.Integer bidId) {
		this.bidId = bidId;
	}
	
	public java.lang.Integer getBidId() {
		return this.bidId;
	}
	
	public void setMoney(java.math.BigDecimal money) {
		this.money = money;
	}
	
	public java.math.BigDecimal getMoney() {
		return this.money;
	}
	
	public void setState(Integer state) {
		this.state = state;
	}
	
	public Integer getState() {
		return this.state;
	}
	
	public void setType(Integer type) {
		this.type = type;
	}
	
	public Integer getType() {
		return this.type;
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

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getBidName() {
		return bidName;
	}

	public void setBidName(String bidName) {
		this.bidName = bidName;
	}

	public Long getLogin() {
		return login;
	}

	public void setLogin(Long login) {
		this.login = login;
	}

	public List<Integer> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<Integer> userIds) {
		this.userIds = userIds;
	}

	public List<Integer> getBidIds() {
		return bidIds;
	}

	public void setBidIds(List<Integer> bidIds) {
		this.bidIds = bidIds;
	}
}

