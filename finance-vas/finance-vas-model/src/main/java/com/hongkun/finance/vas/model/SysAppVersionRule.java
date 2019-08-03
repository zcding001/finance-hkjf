package com.hongkun.finance.vas.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.vas.model.SysAppVersionRule.java
 * @Class Name    : SysAppVersionRule.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class SysAppVersionRule extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: id
	 * 字段: id  INT(10)
	 */
	private Integer id;
	
	/**
	 * 描述: 平台：1-ios，2-android
	 * 字段: platform  VARCHAR(10)
	 * 默认值: '1,2'
	 */
	private String platform;
	
	/**
	 * 描述: app最低版本，例如：2.0.0
	 * 字段: min_version  VARCHAR(11)
	 * 默认值: ''
	 */
	private String minVersion;
	
	/**
	 * 描述: app最高版本，例如：2.1.1
	 * 字段: max_version  VARCHAR(11)
	 * 默认值: ''
	 */
	private String maxVersion;
	
	/**
	 * 描述: app版本更新内容
	 * 字段: content  TINYTEXT(255)
	 */
	private String content;
	
	/**
	 * 描述: 状态：0-下线，1-上线
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
	 * 描述: 上线时间
	 * 字段: uptime  DATETIME(19)
	 * 默认值: 0000-00-00 00:00:00
	 */
	private java.util.Date uptime;
	
	//【非数据库字段，查询时使用】
	private java.util.Date uptimeBegin;
	
	//【非数据库字段，查询时使用】
	private java.util.Date uptimeEnd;
	/**
	 * 描述: 下线时间
	 * 字段: downtime  DATETIME(19)
	 * 默认值: 0000-00-00 00:00:00
	 */
	private java.util.Date downtime;
	
	//【非数据库字段，查询时使用】
	private java.util.Date downtimeBegin;
	
	//【非数据库字段，查询时使用】
	private java.util.Date downtimeEnd;
 
	//【非数据库字段，查询时使用】
	private String version;

	public SysAppVersionRule(){
	}

	public SysAppVersionRule(Integer id){
		this.id = id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	
	public String getPlatform() {
		return this.platform;
	}
	
	public void setMinVersion(String minVersion) {
		this.minVersion = minVersion;
	}
	
	public String getMinVersion() {
		return this.minVersion;
	}
	
	public void setMaxVersion(String maxVersion) {
		this.maxVersion = maxVersion;
	}
	
	public String getMaxVersion() {
		return this.maxVersion;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public String getContent() {
		return this.content;
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
	public void setUptime(java.util.Date uptime) {
		this.uptime = uptime;
	}
	
	public java.util.Date getUptime() {
		return this.uptime;
	}
	
	public void setUptimeBegin(java.util.Date uptimeBegin) {
		this.uptimeBegin = uptimeBegin;
	}
	
	public java.util.Date getUptimeBegin() {
		return this.uptimeBegin;
	}
	
	public void setUptimeEnd(java.util.Date uptimeEnd) {
		this.uptimeEnd = uptimeEnd;
	}
	
	public java.util.Date getUptimeEnd() {
		return this.uptimeEnd;
	}	
	public void setDowntime(java.util.Date downtime) {
		this.downtime = downtime;
	}
	
	public java.util.Date getDowntime() {
		return this.downtime;
	}
	
	public void setDowntimeBegin(java.util.Date downtimeBegin) {
		this.downtimeBegin = downtimeBegin;
	}
	
	public java.util.Date getDowntimeBegin() {
		return this.downtimeBegin;
	}
	
	public void setDowntimeEnd(java.util.Date downtimeEnd) {
		this.downtimeEnd = downtimeEnd;
	}
	
	public java.util.Date getDowntimeEnd() {
		return this.downtimeEnd;
	}	
	
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}

