package com.hongkun.finance.roster.model;

import com.yirun.framework.core.annotation.Union;
import com.yirun.framework.core.model.BaseModel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.roster.model.RosInfo.java
 * @Class Name    : RosInfo.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class RosInfo extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: id
	 * 字段: id  INT(10)
	 */
	private java.lang.Integer id;
	
	/**
	 * 描述: 用户id
	 * 字段: reg_user_id  INT(10)
	 */
	private java.lang.Integer regUserId;

    /**
     * 描述: 用户手机号
     * 字段: login  BIGINT(10)
     */
    private java.lang.Long login;
	
	/**
	 * 描述: 0：积分增值，1：积分转金额，2：金额转积分，3：积分投资，4：债权转让，5：新手标投资，6：钱袋子转入转出，7：钱袋子推荐奖
	 * 字段: type  TINYINT(3)
	 * 默认值: 0
	 */
	@Union(changeAbleIfHasValue = false)
	private Integer type;
	
	/**
	 * 描述: 0：黑名单，1：白名单
	 * 字段: flag  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer flag;
	
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
 
	public RosInfo(){
	}

	public RosInfo(java.lang.Integer id){
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
	
	public void setType(Integer type) {
		this.type = type;
	}
	
	public Integer getType() {
		return this.type;
	}
	
	public void setFlag(Integer flag) {
		this.flag = flag;
	}
	
	public Integer getFlag() {
		return this.flag;
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
	
	
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
	
	/**用户id集合*/
	private List<Integer> userIds = new ArrayList<>();
	/**用户真实姓名*/
	private String realName;

	public List<Integer> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<Integer> userIds) {
		this.userIds = userIds;
	}

	public Long getLogin() {
		return login;
	}

	public void setLogin(Long login) {
		this.login = login;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}
}

