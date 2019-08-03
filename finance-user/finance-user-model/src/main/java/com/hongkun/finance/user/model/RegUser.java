package com.hongkun.finance.user.model;

import com.hongkun.finance.user.constants.UserConstants;
import com.yirun.framework.core.model.BaseModel;
import com.yirun.framework.core.support.validate.DELETE;
import com.yirun.framework.core.support.validate.SAVE;
import com.yirun.framework.core.support.validate.UPDATE;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.user.model.RegUser.java
 * @Class Name    : RegUser.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class RegUser extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: 用户id
	 * 字段: id  INT UNSIGNED(10)
	 */
	@NotNull(message = "请指用户",groups = {UPDATE.class, DELETE.class})
	private java.lang.Integer id;
	
	/**
	 * 描述: 用户编号
	 * 字段: code  VARCHAR(36)
	 * 默认值: ''
	 */
	private java.lang.String code;
	
	/**
	 * 描述: 登录手机号
	 * 字段: login  BIGINT UNSIGNED(20)
	 * 默认值: 0
	 */
	@NotNull(message = "请指定登录用户",groups = {SAVE.class,UPDATE.class})
	private java.lang.Long login;
	
	/**
	 * 描述: 用户昵称
	 * 字段: nick_name  VARCHAR(30)
	 * 默认值: ''
	 */
	@NotNull(message = "请指定用户名称",groups = {SAVE.class,UPDATE.class})
	private java.lang.String nickName;
	
	/**
	 * 描述: 登录密码
	 * 字段: passwd  VARCHAR(32)
	 * 默认值: ''
	 */
	@NotNull(message = "请指定登录密码",groups = {SAVE.class,UPDATE.class})
	private java.lang.String passwd;
	
	/**
	 * 描述: 实名状态:0-未实名,1-已实名
	 * 字段: identify  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer identify;
	
	/**
	 * 描述: 注册类型:1-一般用户,2-企业,3-物业,4-第三方账户,5-后台账户
	 * 字段: type  TINYINT UNSIGNED(3)
	 * 默认值: 1
	 */
	private Integer type;

	/**
	 * 描述: 用户头像
	 * 字段: type  TINYINT UNSIGNED(3)
	 * 默认值: 1
	 */
	private String headPortrait;
	
	/**
	 * 描述: 上次登录时间
	 * 字段: last_login_time  DATETIME(19)
	 * 默认值: 0000-00-00 00:00:00
	 */
	private java.util.Date lastLoginTime;
	
	//【非数据库字段，查询时使用】
	private java.util.Date lastLoginTimeBegin;
	
	//【非数据库字段，查询时使用】
	private java.util.Date lastLoginTimeEnd;
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
     * 描述: 状态:0-删除,1-正常
     * 字段: state  TINYINT(2)
     * 默认值: 0
     */
    private Integer vipFlag;
    
	public RegUser(){
	}

	public RegUser(java.lang.Integer id){
		this.id = id;
	}

	public String getHeadPortrait() {
		return headPortrait;
	}

	public void setHeadPortrait(String headPortrait) {
		this.headPortrait = headPortrait;
	}

	public void setId(java.lang.Integer id) {
		this.id = id;
	}
	
	public java.lang.Integer getId() {
		return this.id;
	}
	
	public void setCode(java.lang.String code) {
		this.code = code;
	}
	
	public java.lang.String getCode() {
		return this.code;
	}
	
	public void setLogin(java.lang.Long login) {
		this.login = login;
	}
	
	public java.lang.Long getLogin() {
		return this.login;
	}
	
	public void setNickName(java.lang.String nickName) {
		this.nickName = nickName;
	}
	
	public java.lang.String getNickName() {
		return this.nickName;
	}
	
	public void setPasswd(java.lang.String passwd) {
		this.passwd = passwd;
	}
	
	public java.lang.String getPasswd() {
		return this.passwd;
	}
	
	public void setIdentify(Integer identify) {
		this.identify = identify;
	}
	
	public Integer getIdentify() {
		return this.identify;
	}
	
	public void setType(Integer type) {
		this.type = type;
	}
	
	public Integer getType() {
		return this.type;
	}
	
	public void setLastLoginTime(java.util.Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	
	public java.util.Date getLastLoginTime() {
		return this.lastLoginTime;
	}
	
	public void setLastLoginTimeBegin(java.util.Date lastLoginTimeBegin) {
		this.lastLoginTimeBegin = lastLoginTimeBegin;
	}
	
	public java.util.Date getLastLoginTimeBegin() {
		return this.lastLoginTimeBegin;
	}
	
	public void setLastLoginTimeEnd(java.util.Date lastLoginTimeEnd) {
		this.lastLoginTimeEnd = lastLoginTimeEnd;
	}
	
	public java.util.Date getLastLoginTimeEnd() {
		return this.lastLoginTimeEnd;
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
	
	/**
	 *  @Description    : 判断当前用户是否实名
	 *  @Method_Name    : hasIdentify
	 *  @return         : boolean
	 *  @Creation Date  : 2018年3月14日 下午3:38:23 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	public boolean hasIdentify(){
		return this.identify != null && this.identify != UserConstants.USER_IDENTIFY_NO;
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

    public Integer getVipFlag() {
        return vipFlag;
    }

    public void setVipFlag(Integer vipFlag) {
        this.vipFlag = vipFlag;
    }
}

