package com.hongkun.finance.contract.model.vo;

import com.yirun.framework.core.model.BaseModel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Project       : finance-hkjf
 * @Program Name  : com.hongkun.finance.contract.model.CarContract.java
 * @Class Name    : CarContract.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class CarContractVO extends BaseModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 描述: id主键值
	 * 字段: id  INT(10)
	 */
	private Integer id;

	/**
	 * 描述: 甲方用户id
	 * 字段: party_a_id  INT(10)
	 */
	private Integer partyAid;

	/**
	 * 描述: 乙方用户id
	 * 字段: party_b_id  INT(10)
	 */
	private Integer partyBid;

	/**
	 * 描述: 合同名称
	 * 字段: title  VARCHAR(35)
	 */
	private String title;

	/**
	 * 描述: 合同编号(自用）
	 * 字段: no  VARCHAR(32)
	 */
	private String no;

	/**
	 * 描述: 借款金额
	 * 字段: amount  DOUBLE(10)
	 * 默认值: 0.00
	 */
	private Double amount;

	/**
	 * 描述: 借款期限(月单位）
	 * 字段: duration  DOUBLE(11)
	 */
	private Double duration;

	/**
	 * 描述: 借款利率
	 * 字段: rate  DOUBLE(10)
	 */
	private Double rate;

	/**
	 * 描述: 借款开始时间
	 * 字段: loan_start_time  DATE(10)
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date loanStartTime;

	//【非数据库字段，查询时使用】
	private java.util.Date loanStartTimeBegin;

	//【非数据库字段，查询时使用】
	private java.util.Date loanStartTimeEnd;
	/**
	 * 描述: 借款结束时间
	 * 字段: loan_end_time  DATE(10)
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date loanEndTime;

	//【非数据库字段，查询时使用】
	private java.util.Date loanEndTimeBegin;

	//【非数据库字段，查询时使用】
	private java.util.Date loanEndTimeEnd;
	/**
	 * 描述: 委托截止日
	 * 字段: end_time  DATE(10)
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date endTime;

	//【非数据库字段，查询时使用】
	private java.util.Date endTimeBegin;

	//【非数据库字段，查询时使用】
	private java.util.Date endTimeEnd;
	/**
	 * 描述: 打印次数
	 * 字段: print_num  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer printNum;

	/**
	 * 描述: 下载次数
	 * 字段: download_num  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer downloadNum;

	/**
	 * 描述: 合同创建时间
	 * 字段: create_time  DATETIME(19)
	 */
	private java.util.Date createTime;

	//【非数据库字段，查询时使用】
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date createTimeBegin;

	//【非数据库字段，查询时使用】
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date createTimeEnd;
	/**
	 * 描述: 最后修改时间
	 * 字段: modify_time  DATETIME(19)
	 */
	private java.util.Date modifyTime;

	//【非数据库字段，查询时使用】
	private java.util.Date modifyTimeBegin;

	//【非数据库字段，查询时使用】
	private java.util.Date modifyTimeEnd;
	/**
	 * 描述: 甲方用户名字
	 * 字段: partyA_name  VARCHAR(30)
	 * 默认值: ''
	 */
	private String partyAname;

	/**
	 * 描述: 乙方用户名字
	 * 字段: partyB_name  VARCHAR(30)
	 * 默认值: ''
	 */
	private String partyBname;

	/**
	 * 描述: 甲方用户联系电话
	 * 字段: partyA_tel  VARCHAR(20)
	 * 默认值: ''
	 */
	private String partyAtel;

	/**
	 * 描述: 乙方用户名字
	 * 字段: partyB_tel  VARCHAR(20)
	 * 默认值: ''
	 */
	private String partyBtel;

	/**
	 * 描述：借款金额大写
	 */
	private String bigAmt;


	public CarContractVO(){
	}

	public CarContractVO(Integer id){
		this.id = id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public void setPartyAid(Integer partyAid) {
		this.partyAid = partyAid;
	}
	
	public Integer getPartyAid() {
		return this.partyAid;
	}
	
	public void setPartyBid(Integer partyBid) {
		this.partyBid = partyBid;
	}
	
	public Integer getPartyBid() {
		return this.partyBid;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public void setNo(String no) {
		this.no = no;
	}
	
	public String getNo() {
		return this.no;
	}
	
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	public Double getAmount() {
		return this.amount;
	}
	
	public void setDuration(Double duration) {
		this.duration = duration;
	}
	
	public Double getDuration() {
		return this.duration;
	}
	
	public void setRate(Double rate) {
		this.rate = rate;
	}
	
	public Double getRate() {
		return this.rate;
	}
	
	public void setLoanStartTime(java.util.Date loanStartTime) {
		this.loanStartTime = loanStartTime;
	}
	
	public java.util.Date getLoanStartTime() {
		return this.loanStartTime;
	}
	
	public void setLoanStartTimeBegin(java.util.Date loanStartTimeBegin) {
		this.loanStartTimeBegin = loanStartTimeBegin;
	}
	
	public java.util.Date getLoanStartTimeBegin() {
		return this.loanStartTimeBegin;
	}
	
	public void setLoanStartTimeEnd(java.util.Date loanStartTimeEnd) {
		this.loanStartTimeEnd = loanStartTimeEnd;
	}
	
	public java.util.Date getLoanStartTimeEnd() {
		return this.loanStartTimeEnd;
	}	
	public void setLoanEndTime(java.util.Date loanEndTime) {
		this.loanEndTime = loanEndTime;
	}
	
	public java.util.Date getLoanEndTime() {
		return this.loanEndTime;
	}
	
	public void setLoanEndTimeBegin(java.util.Date loanEndTimeBegin) {
		this.loanEndTimeBegin = loanEndTimeBegin;
	}
	
	public java.util.Date getLoanEndTimeBegin() {
		return this.loanEndTimeBegin;
	}
	
	public void setLoanEndTimeEnd(java.util.Date loanEndTimeEnd) {
		this.loanEndTimeEnd = loanEndTimeEnd;
	}
	
	public java.util.Date getLoanEndTimeEnd() {
		return this.loanEndTimeEnd;
	}	
	public void setEndTime(java.util.Date endTime) {
		this.endTime = endTime;
	}
	
	public java.util.Date getEndTime() {
		return this.endTime;
	}
	
	public void setEndTimeBegin(java.util.Date endTimeBegin) {
		this.endTimeBegin = endTimeBegin;
	}
	
	public java.util.Date getEndTimeBegin() {
		return this.endTimeBegin;
	}
	
	public void setEndTimeEnd(java.util.Date endTimeEnd) {
		this.endTimeEnd = endTimeEnd;
	}
	
	public java.util.Date getEndTimeEnd() {
		return this.endTimeEnd;
	}	
	public void setPrintNum(Integer printNum) {
		this.printNum = printNum;
	}
	
	public Integer getPrintNum() {
		return this.printNum;
	}
	
	public void setDownloadNum(Integer downloadNum) {
		this.downloadNum = downloadNum;
	}
	
	public Integer getDownloadNum() {
		return this.downloadNum;
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
	public void setPartyAname(String partyAname) {
		this.partyAname = partyAname;
	}
	
	public String getPartyAname() {
		return this.partyAname;
	}
	
	public void setPartyBname(String partyBname) {
		this.partyBname = partyBname;
	}
	
	public String getPartyBname() {
		return this.partyBname;
	}
	
	public void setPartyAtel(String partyAtel) {
		this.partyAtel = partyAtel;
	}
	
	public String getPartyAtel() {
		return this.partyAtel;
	}
	
	public void setPartyBtel(String partyBtel) {
		this.partyBtel = partyBtel;
	}
	
	public String getPartyBtel() {
		return this.partyBtel;
	}

	public String getBigAmt() {
		return bigAmt;
	}

	public void setBigAmt(String bigAmt) {
		this.bigAmt = bigAmt;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}

