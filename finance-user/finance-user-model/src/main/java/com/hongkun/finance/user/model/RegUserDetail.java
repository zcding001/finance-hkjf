package com.hongkun.finance.user.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.invest.model.RegUserDetail.java
 * @Class Name    : RegUserDetail.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class RegUserDetail extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: 主键id
	 * 字段: id  INT UNSIGNED(10)
	 */
	private java.lang.Integer id;
	
	/**
	 * 描述: 用户id
	 * 字段: reg_user_id  INT UNSIGNED(10)
	 */
	private java.lang.Integer regUserId;
	
	/**
	 * 描述: 用户编号
	 * 字段: code  VARCHAR(36)
	 * 默认值: ''
	 */
	private java.lang.String code;
	
	/**
	 * 描述: 用户姓名
	 * 字段: real_name  VARCHAR(20)
	 * 默认值: ''
	 */
	private java.lang.String realName;
	
	/**
	 * 描述: 用户身份证号
	 * 字段: id_card  VARCHAR(20)
	 * 默认值: ''
	 */
	private java.lang.String idCard;
	
	/**
	 * 描述: 用户邀请码
	 * 字段: invite_no  VARCHAR(8)
	 * 默认值: ''
	 */
	private java.lang.String inviteNo;
	
	/**
	 * 描述: 推荐人邀请码
	 * 字段: commend_no  VARCHAR(8)
	 * 默认值: ''
	 */
	private java.lang.String commendNo;
	
	/**
	 * 描述: 推广来源
	 * 字段: exten_source  VARCHAR(10)
	 * 默认值: 0
	 */
	private String extenSource;
	
	/**
	 * 描述: 注册来源:1-qkd(pc),2-qkd(wap),3-qkd(app),11-qsh(pc),12-qsh(wap),13-qsh(app)
	 * 字段: regist_source  TINYINT UNSIGNED(3)
	 * 默认值: 0
	 */
	private Integer registSource;
	
	/**
	 * 描述: 机构编码
	 * 字段: group_code  VARCHAR(10)
	 * 默认值: ''
	 */
	private java.lang.String groupCode;
	
	/**
	 * 描述: 信用等级
	 * 字段: credit_level  VARCHAR(20)
	 * 默认值: ''
	 */
	private java.lang.String creditLevel;

    /**
     * 描述: 状态:0-删除,1-正常
     * 字段: investFlag  TINYINT(4)
     * 默认值: 1
     */
    private Integer investFlag;
	
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
	
	/**
	 * 描述: 实名时间
	 * 字段: realNameTime  DATETIME(19)
	 * 默认值: CURRENT_TIMESTAMP
	 */
	private java.util.Date realNameTime;
	
	//【非数据库字段，查询时使用】
	private java.util.Date realNameTimeBegin;
	
	//【非数据库字段，查询时使用】
	private java.util.Date realNameTimeEnd;
 
	public RegUserDetail(){
	}

	public RegUserDetail(java.lang.Integer id){
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
	
	public void setCode(java.lang.String code) {
		this.code = code;
	}
	
	public java.lang.String getCode() {
		return this.code;
	}
	
	public void setRealName(java.lang.String realName) {
		this.realName = realName;
	}
	
	public java.lang.String getRealName() {
		return this.realName;
	}
	
	public void setIdCard(java.lang.String idCard) {
		this.idCard = idCard;
	}
	
	public java.lang.String getIdCard() {
		return this.idCard;
	}
	
	public void setInviteNo(java.lang.String inviteNo) {
		this.inviteNo = inviteNo;
	}
	
	public java.lang.String getInviteNo() {
		return this.inviteNo;
	}
	
	public void setCommendNo(java.lang.String commendNo) {
		this.commendNo = commendNo;
	}
	
	public java.lang.String getCommendNo() {
		return this.commendNo;
	}
	
	public void setExtenSource(String extenSource) {
		this.extenSource = extenSource;
	}
	
	public String getExtenSource() {
		return this.extenSource;
	}
	
	public void setRegistSource(Integer registSource) {
		this.registSource = registSource;
	}
	
	public Integer getRegistSource() {
		return this.registSource;
	}
	
	public void setGroupCode(java.lang.String groupCode) {
		this.groupCode = groupCode;
	}
	
	public java.lang.String getGroupCode() {
		return this.groupCode;
	}
	
	public void setCreditLevel(java.lang.String creditLevel) {
		this.creditLevel = creditLevel;
	}
	
	public java.lang.String getCreditLevel() {
		return this.creditLevel;
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
	public java.util.Date getRealNameTime() {
		return realNameTime;
	}
	public void setRealNameTime(java.util.Date realNameTime) {
		this.realNameTime = realNameTime;
	}
	public java.util.Date getRealNameTimeBegin() {
		return realNameTimeBegin;
	}
	public void setRealNameTimeBegin(java.util.Date realNameTimeBegin) {
		this.realNameTimeBegin = realNameTimeBegin;
	}
	public java.util.Date getRealNameTimeEnd() {
		return realNameTimeEnd;
	}
	public void setRealNameTimeEnd(java.util.Date realNameTimeEnd) {
		this.realNameTimeEnd = realNameTimeEnd;
	}

    public Integer getInvestFlag() {
        return investFlag;
    }

    public void setInvestFlag(Integer investFlag) {
        this.investFlag = investFlag;
    }

    @Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}

    private List<Integer> userIds = new ArrayList<>();

    public List<Integer> getUserIds() {
        return userIds;
    }
    public void setUserIds(List<Integer> userIds) {
        this.userIds = userIds;
    }
}

