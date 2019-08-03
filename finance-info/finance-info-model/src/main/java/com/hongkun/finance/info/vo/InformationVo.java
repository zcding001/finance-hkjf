package com.hongkun.finance.info.vo;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class InformationVo implements Serializable {

	private static final long serialVersionUID = 1L;
	// 资讯类型
	private Integer type = 0;
	// 所属版块
	private Integer resideSelect;
	// 标题
	private String title;
	// 关键字
	private String keyword;
	// 正文内容
	private String content;
	// 渠道
	private String channel = "1";
	// 位置
	private Integer position = 0;
	// 排序号
	private Integer sort = 1;
	// 来源
	private String source;
	// 图片地址链接（友情链接url）
	private String url;
	// 资讯ID
	private int infomationId;
	//标题图标
	private String imgUrl;
	//是否展示
	private Integer showFlag;

	public Integer getShowFlag() {
        return showFlag;
    }

    public void setShowFlag(Integer showFlag) {
        this.showFlag = showFlag;
    }

    public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getResideSelect() {
		return resideSelect;
	}

	public void setResideSelect(Integer resideSelect) {
		this.resideSelect = resideSelect;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getInfomationId() {
		return infomationId;
	}

	public void setInfomationId(int infomationId) {
		this.infomationId = infomationId;
	}

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
    }
}
