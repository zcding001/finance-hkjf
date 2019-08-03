package com.hongkun.finance.user.model;

import com.yirun.framework.core.model.BaseModel;
import com.yirun.framework.core.support.validate.UPDATE;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Project       : finance-hkjf
 * @Program Name  : com.hongkun.finance.user.model.AppMoreServe.java
 * @Class Name    : AppMoreServe.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class AppMoreServe extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: id
	 * 字段: id  INT(10)
	 */
	@NotNull(groups = UPDATE.class)
	private Integer id;

	/**
	 * 描述: 模块分类值
	 * 字段: type_value  INT(10)
	 */
	private Integer type;
	
	/**
	 * 描述: 服务名称
	 * 字段: service_name  VARCHAR(50)
	 */
	private String serviceName;

	/**
	 * 描述: 服务值
	 * 字段: service_value  INT(10)
	 */
	private Integer seq;
	
	/**
	 * 描述: 链接
	 * 字段: service_url  VARCHAR(100)
	 */
	private String serviceUrl;
	
	/**
	 * 描述: 图标
	 * 字段: ico_url  VARCHAR(200)
	 */
	private String icoUrl;
	
	/**
	 * 描述: 0展示，1隐藏
	 * 字段: is_show  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer isShow;

	private List<Integer> serveIds;

	public List<Integer> getServeIds() {
		return serveIds;
	}

	public void setServeIds(List<Integer> serveIds) {
		this.serveIds = serveIds;
	}

	public AppMoreServe(){
	}

	public AppMoreServe(Integer id){
		this.id = id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public void setType(Integer type) {
		this.type = type;
	}
	
	public Integer getType() {
		return this.type;
	}
	
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	
	public String getServiceName() {
		return this.serviceName;
	}
	
	public void setSeq(Integer seq) {
		this.seq = seq;
	}
	
	public Integer getSeq() {
		return this.seq;
	}
	
	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}
	
	public String getServiceUrl() {
		return this.serviceUrl;
	}
	
	public void setIcoUrl(String icoUrl) {
		this.icoUrl = icoUrl;
	}
	
	public String getIcoUrl() {
		return this.icoUrl;
	}
	
	public void setIsShow(Integer isShow) {
		this.isShow = isShow;
	}
	
	public Integer getIsShow() {
		return this.isShow;
	}
	
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}

