package com.hongkun.finance.info.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.format.annotation.DateTimeFormat;

import com.yirun.framework.core.model.BaseModel;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.info.model.InfoInformationNews.java
 * @Class Name : InfoInformationNews.java
 * @Description : GENERATOR VO类
 * @Author : generator
 */
public class InfoInformationNews extends BaseModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 描述: 主键ID 字段: id INT(10)
	 */
	private java.lang.Integer id;

	/**
	 * 描述: 标题 字段: title VARCHAR(300) 默认值: ''
	 */
	private java.lang.String title;

	/**
	 * 描述: 类型 1-媒体报道 2-公司动态 3-公司公告 4-友情链接 5-活动 6-(征文)福利 7-调查问卷 字段: type
	 * TINYINT(3) 默认值: 1
	 */
	private Integer type;

	/**
	 * 描述: 来源 字段: source VARCHAR(50) 默认值: ''
	 */
	private java.lang.String source;

	/**
	 * 描述: 渠道 1-PC 2-IOS 3-ANDRIOD 4-WAP 字段: channel VARCHAR(20) 默认值: 1
	 */
	private String channel;

	/**
	 * 描述: 位置 0-其它 1-首页 2-积分商城 3-投资 字段: position TINYINT(3) 默认值: 0
	 */
	private Integer position;

	/**
	 * 描述: 内容 字段: content TEXT(65535)
	 */
	private java.lang.String content;

	/**
	 * 描述: 图片地址链接（友情链接url） 字段: url VARCHAR(200) 默认值: ''
	 */
	private java.lang.String url;

	/**
	 * 描述: 关键字 字段: keyword VARCHAR(500) 默认值: ''
	 */
	private java.lang.String keyword;

	/**
	 * 描述: 所属版块 1-亿润投资集团 2-鸿坤集团 字段: reside_select TINYINT(3) 默认值: 1
	 */
	private Integer resideSelect;

	/**
	 * 描述: 排序号 字段: sort INT(10) 默认值: 0
	 */
	private java.lang.Integer sort;

	/**
	 * 描述: 点赞数 字段: eulogize_num INT(10) 默认值: 0
	 */
	private java.lang.Integer eulogizeNum;

	/**
	 * 描述: 浏览数 字段: browse_num INT(10) 默认值: 0
	 */
	private java.lang.Integer browseNum;

	/**
	 * 描述: 状态 0-删除 1-正常 2-上架 3-下架 字段: state TINYINT(3) 默认值: 1
	 */
	private Integer state;

	/**
	 * 描述: 发布时间 字段: create_time DATETIME(19) 默认值: CURRENT_TIMESTAMP
	 */
	private java.util.Date createTime;

	// 【非数据库字段，查询时使用】
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date createTimeBegin;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	// 【非数据库字段，查询时使用】
	private java.util.Date createTimeEnd;
	/**
	 * 描述: 修改时间 字段: modify_time DATETIME(19) 默认值: CURRENT_TIMESTAMP
	 */
	private java.util.Date modifyTime;

	// 【非数据库字段，查询时使用】
	private java.util.Date modifyTimeBegin;

	// 【非数据库字段，查询时使用】
	private java.util.Date modifyTimeEnd;
	/**
	 * 资讯图标
	 */
	private String icon;
	 /**
     * 描述: 标题图片 字段: img_url VARCHAR(200) 默认值: ''
     */
    private java.lang.String imgUrl;
    
    /**
     * 描述: 状态 0-对所有用户可见 1-对有权限看定期的用户可见 默认值: 0
     */
    private Integer showFlag;

	
    public Integer getShowFlag() {
        return showFlag;
    }

    public void setShowFlag(Integer showFlag) {
        this.showFlag = showFlag;
    }

    public InfoInformationNews() {
	}

	public InfoInformationNews(java.lang.Integer id) {
		this.id = id;
	}

	public void setId(java.lang.Integer id) {
		this.id = id;
	}

	public java.lang.Integer getId() {
		return this.id;
	}

	public void setTitle(java.lang.String title) {
		this.title = title;
	}

	public java.lang.String getTitle() {
		return this.title;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getType() {
		return this.type;
	}

	public void setSource(java.lang.String source) {
		this.source = source;
	}

	public java.lang.String getSource() {
		return this.source;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public Integer getPosition() {
		return this.position;
	}

	public void setContent(java.lang.String content) {
		this.content = content;
	}

	public java.lang.String getContent() {
		return this.content;
	}

	public void setUrl(java.lang.String url) {
		this.url = url;
	}

	public java.lang.String getUrl() {
		return this.url;
	}

	public void setKeyword(java.lang.String keyword) {
		this.keyword = keyword;
	}

	public java.lang.String getKeyword() {
		return this.keyword;
	}

	public void setResideSelect(Integer resideSelect) {
		this.resideSelect = resideSelect;
	}

	public Integer getResideSelect() {
		return this.resideSelect;
	}

	public void setSort(java.lang.Integer sort) {
		this.sort = sort;
	}

	public java.lang.Integer getSort() {
		return this.sort;
	}

	public void setEulogizeNum(java.lang.Integer eulogizeNum) {
		this.eulogizeNum = eulogizeNum;
	}

	public java.lang.Integer getEulogizeNum() {
		return this.eulogizeNum;
	}

	public void setBrowseNum(java.lang.Integer browseNum) {
		this.browseNum = browseNum;
	}

	public java.lang.Integer getBrowseNum() {
		return this.browseNum;
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

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public java.lang.String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(java.lang.String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}
