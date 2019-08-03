package com.hongkun.finance.fund.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;

import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.fund.model.FundEvaluation.java
 * @Class Name    : FundEvaluation.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class FundEvaluation extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: 主键ID
	 * 字段: id  INT(10)
	 */
	private Integer id;
	
	/**
	 * 描述: 类型：1-股权测评 2-投资测评
	 * 字段: type  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer type;
	
	/**
	 * 描述: 用户id
	 * 字段: reg_user_id  INT(10)
	 * 默认值: 0
	 */
	private Integer regUserId;
	
	/**
	 * 描述: 所选选项
	 * 字段: options  VARCHAR(30)
	 * 默认值: ''
	 */
	private String options;
	
	/**
	 * 描述: 得分
	 * 字段: score  INT(10)
	 * 默认值: 0
	 */
	private Integer score;
	
	/**
	 * 描述: 测评结果
	 * 字段: result_type  VARCHAR(30)
	 * 默认值: ''
	 */
	private String resultType;
	
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
	 * 联系电话
	 */
	private Long tel;

	/**
	 * 客户姓名
	 */
	private String name;

	private List<Integer> regUserIdList;
 
	public FundEvaluation(){
	}

	public List<Integer> getRegUserIdList() {
		return regUserIdList;
	}

	public void setRegUserIdList(List<Integer> regUserIdList) {
		this.regUserIdList = regUserIdList;
	}

	public Long getTel() {
		return tel;
	}

	public void setTel(Long tel) {
		this.tel = tel;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public FundEvaluation(Integer id){
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
	
	public void setRegUserId(Integer regUserId) {
		this.regUserId = regUserId;
	}
	
	public Integer getRegUserId() {
		return this.regUserId;
	}
	
	public void setOptions(String options) {
		this.options = options;
	}
	
	public String getOptions() {
		return this.options;
	}
	
	public void setScore(Integer score) {
		this.score = score;
	}
	
	public Integer getScore() {
		return this.score;
	}
	
	public void setResultType(String resultType) {
		this.resultType = resultType;
	}
	
	public String getResultType() {
		return this.resultType;
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
}

