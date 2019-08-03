package com.hongkun.finance.contract.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;

import java.math.BigDecimal;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.contract.model.ConInfo.java
 * @Class Name    : ConInfo.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class ConInfo extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: id
	 * 字段: id  INT(10)
	 */
	private Integer id;
	
	/**
	 * 描述: 合同编号/协议编号
	 * 字段: contract_code  VARCHAR(36)
	 * 默认值: ''
	 */
	private String contractCode;
	
	/**
	 * 描述: 标地id
	 * 字段: bid_id  INT(10)
	 * 默认值: 0
	 */
	private Integer bidId;
	
	/**
	 * 描述: 投资记录id
	 * 字段: bid_invest_id  INT(10)
	 * 默认值: 0
	 */
	private Integer bidInvestId;
	
	/**
	 * 描述: 合同类型
	 * 字段: contract_type  INT(10)
	 * 默认值: 0
	 */
	private Integer contractType;
	
	/**
	 * 描述: 合同模板id
	 * 字段: contract_template_id  INT(10)
	 * 默认值: 0
	 */
	private Integer contractTemplateId;
	
	/**
	 * 描述: 合同生效日期
	 * 字段: effective_date  DATETIME(19)
	 * 默认值: 0000-00-00 00:00:00
	 */
	private java.util.Date effectiveDate;
	
	//【非数据库字段，查询时使用】
	private java.util.Date effectiveDateBegin;
	
	//【非数据库字段，查询时使用】
	private java.util.Date effectiveDateEnd;
	/**
	 * 描述: 合同截止日期
	 * 字段: expire_date  DATETIME(19)
	 * 默认值: 0000-00-00 00:00:00
	 */
	private java.util.Date expireDate;
	
	//【非数据库字段，查询时使用】
	private java.util.Date expireDateBegin;
	
	//【非数据库字段，查询时使用】
	private java.util.Date expireDateEnd;
	/**
	 * 描述: 投资用户ID
	 * 字段: invest_user_id  INT(10)
	 * 默认值: 0
	 */
	private Integer investUserId;
	
	/**
	 * 描述: 投资人真实姓名
	 * 字段: invest_name  VARCHAR(20)
	 * 默认值: ''
	 */
	private String investName;
	
	/**
	 * 描述: 投资人身份证号
	 * 字段: invest_card_id  VARCHAR(20)
	 * 默认值: ''
	 */
	private String investCardId;
	
	/**
	 * 描述: 投资金额
	 * 字段: invest_amount  DECIMAL(20)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal investAmount;

	/**
	 * 描述: 投资使用投资红包金额
	 * 字段: invest_coupon_k_value  DECIMAL(12)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal investCouponKValue;

	/**
	 * 描述: 投资使用加息券的值
	 * 字段: invest_coupon_j_value  DECIMAL(12)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal investCouponJValue;
	/**
	 * 描述: 标地名称
	 * 字段: bid_name  VARCHAR(50)
	 * 默认值: ''
	 */
	private String bidName;
	
	/**
	 * 描述: 标地利率
	 * 字段: bid_rate  DECIMAL(4)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal bidRate;
	
	/**
	 * 描述: 标的总金额
	 * 字段: bid_amount  DECIMAL(20)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal bidAmount;
	
	/**
	 * 描述: 标地期限值
	 * 字段: bid_term_value  SMALLINT(5)
	 * 默认值: 0
	 */
	private Integer bidTermValue;
	
	/**
	 * 描述: 期限单位:1-年,2-月,3-天
	 * 字段: bid_term  TINYINT(3)
	 * 默认值: 2
	 */
	private Integer bidTerm;
	
	/**
	 * 描述: 借款人真实姓名
	 * 字段: borrow_name  VARCHAR(20)
	 * 默认值: ''
	 */
	private String borrowName;
	
	/**
	 * 描述: 借款人身份证号
	 * 字段: borrow_card_id  VARCHAR(20)
	 * 默认值: ''
	 */
	private String borrowCardId;
	
	/**
	 * 描述: 借款用途:1-短期周转,2-项目贷款,3-临时倒短,4-扩大生产
	 * 字段: bid_loan_use  TINYINT(3)
	 * 默认值: 1
	 */
	private Integer bidLoanUse;
	
	/**
	 * 描述: 还款方式:1-等额本息,2-按月付息，到期还本,3-到期还本付息,4-到期付息，本金回收,5-每月付息，到期本金回收,6-按月付息，本金划归企业
	 * 字段: bidd_repayment_way  TINYINT(3)
	 * 默认值: 2
	 */
	private Integer biddRepaymentWay;
	
	/**
	 * 描述: 收款人名称
	 * 字段: payee_name  VARCHAR(20)
	 * 默认值: ''
	 */
	private String payeeName;
	
	/**
	 * 描述: 收款人银行卡号
	 * 字段: payee_bank_card  VARCHAR(30)
	 * 默认值: ''
	 */
	private String payeeBankCard;
	
	/**
	 * 描述: 收款人银行
	 * 字段: payee_bank_name  VARCHAR(30)
	 * 默认值: ''
	 */
	private String payeeBankName;
	
	/**
	 * 描述: 标地项目编号
	 * 字段: bid_project_no  VARCHAR(50)
	 * 默认值: ''
	 */
	private String bidProjectNo;
	
	/**
	 * 描述: 投资人电话
	 * 字段: invest_tel  BIGINT(19)
	 * 默认值: 0
	 */
	private Long investTel;
	
	/**
	 * 描述: 投资人邮箱
	 * 字段: invest_email  VARCHAR(50)
	 * 默认值: ''
	 */
	private String investEmail;
	
	/**
	 * 描述: 借款人住所
	 * 字段: borrow_address  VARCHAR(100)
	 * 默认值: ''
	 */
	private String borrowAddress;
	
	/**
	 * 描述: 标地票据号码
	 * 字段: bid_bill_no  VARCHAR(36)
	 * 默认值: ''
	 */
	private String bidBillNo;
	
	/**
	 * 描述: 状态：0-禁用，1-启用
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
 
	public ConInfo(){
	}

	public ConInfo(Integer id){
		this.id = id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}
	
	public String getContractCode() {
		return this.contractCode;
	}
	
	public void setBidId(Integer bidId) {
		this.bidId = bidId;
	}
	
	public Integer getBidId() {
		return this.bidId;
	}
	
	public void setBidInvestId(Integer bidInvestId) {
		this.bidInvestId = bidInvestId;
	}
	
	public Integer getBidInvestId() {
		return this.bidInvestId;
	}
	
	public void setContractType(Integer contractType) {
		this.contractType = contractType;
	}
	
	public Integer getContractType() {
		return this.contractType;
	}
	
	public void setContractTemplateId(Integer contractTemplateId) {
		this.contractTemplateId = contractTemplateId;
	}
	
	public Integer getContractTemplateId() {
		return this.contractTemplateId;
	}
	
	public void setEffectiveDate(java.util.Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	
	public java.util.Date getEffectiveDate() {
		return this.effectiveDate;
	}
	
	public void setEffectiveDateBegin(java.util.Date effectiveDateBegin) {
		this.effectiveDateBegin = effectiveDateBegin;
	}
	
	public java.util.Date getEffectiveDateBegin() {
		return this.effectiveDateBegin;
	}
	
	public void setEffectiveDateEnd(java.util.Date effectiveDateEnd) {
		this.effectiveDateEnd = effectiveDateEnd;
	}
	
	public java.util.Date getEffectiveDateEnd() {
		return this.effectiveDateEnd;
	}	
	public void setExpireDate(java.util.Date expireDate) {
		this.expireDate = expireDate;
	}
	
	public java.util.Date getExpireDate() {
		return this.expireDate;
	}
	
	public void setExpireDateBegin(java.util.Date expireDateBegin) {
		this.expireDateBegin = expireDateBegin;
	}
	
	public java.util.Date getExpireDateBegin() {
		return this.expireDateBegin;
	}
	
	public void setExpireDateEnd(java.util.Date expireDateEnd) {
		this.expireDateEnd = expireDateEnd;
	}
	
	public java.util.Date getExpireDateEnd() {
		return this.expireDateEnd;
	}	
	public void setInvestUserId(Integer investUserId) {
		this.investUserId = investUserId;
	}
	
	public Integer getInvestUserId() {
		return this.investUserId;
	}
	
	public void setInvestName(String investName) {
		this.investName = investName;
	}
	
	public String getInvestName() {
		return this.investName;
	}
	
	public void setInvestCardId(String investCardId) {
		this.investCardId = investCardId;
	}
	
	public String getInvestCardId() {
		return this.investCardId;
	}
	
	public void setInvestAmount(java.math.BigDecimal investAmount) {
		this.investAmount = investAmount;
	}
	
	public java.math.BigDecimal getInvestAmount() {
		return this.investAmount;
	}

	public BigDecimal getInvestCouponKValue() {
		return investCouponKValue;
	}

	public void setInvestCouponKValue(BigDecimal investCouponKValue) {
		this.investCouponKValue = investCouponKValue;
	}

	public BigDecimal getInvestCouponJValue() {
		return investCouponJValue;
	}

	public void setInvestCouponJValue(BigDecimal investCouponJValue) {
		this.investCouponJValue = investCouponJValue;
	}

	public void setBidName(String bidName) {
		this.bidName = bidName;
	}
	
	public String getBidName() {
		return this.bidName;
	}
	
	public void setBidRate(java.math.BigDecimal bidRate) {
		this.bidRate = bidRate;
	}
	
	public java.math.BigDecimal getBidRate() {
		return this.bidRate;
	}
	
	public void setBidAmount(java.math.BigDecimal bidAmount) {
		this.bidAmount = bidAmount;
	}
	
	public java.math.BigDecimal getBidAmount() {
		return this.bidAmount;
	}
	
	public void setBidTermValue(Integer bidTermValue) {
		this.bidTermValue = bidTermValue;
	}
	
	public Integer getBidTermValue() {
		return this.bidTermValue;
	}
	
	public void setBidTerm(Integer bidTerm) {
		this.bidTerm = bidTerm;
	}
	
	public Integer getBidTerm() {
		return this.bidTerm;
	}
	
	public void setBorrowName(String borrowName) {
		this.borrowName = borrowName;
	}
	
	public String getBorrowName() {
		return this.borrowName;
	}
	
	public void setBorrowCardId(String borrowCardId) {
		this.borrowCardId = borrowCardId;
	}
	
	public String getBorrowCardId() {
		return this.borrowCardId;
	}
	
	public void setBidLoanUse(Integer bidLoanUse) {
		this.bidLoanUse = bidLoanUse;
	}
	
	public Integer getBidLoanUse() {
		return this.bidLoanUse;
	}
	
	public void setBiddRepaymentWay(Integer biddRepaymentWay) {
		this.biddRepaymentWay = biddRepaymentWay;
	}
	
	public Integer getBiddRepaymentWay() {
		return this.biddRepaymentWay;
	}
	
	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}
	
	public String getPayeeName() {
		return this.payeeName;
	}
	
	public void setPayeeBankCard(String payeeBankCard) {
		this.payeeBankCard = payeeBankCard;
	}
	
	public String getPayeeBankCard() {
		return this.payeeBankCard;
	}
	
	public void setPayeeBankName(String payeeBankName) {
		this.payeeBankName = payeeBankName;
	}
	
	public String getPayeeBankName() {
		return this.payeeBankName;
	}
	
	public void setBidProjectNo(String bidProjectNo) {
		this.bidProjectNo = bidProjectNo;
	}
	
	public String getBidProjectNo() {
		return this.bidProjectNo;
	}
	
	public void setInvestTel(Long investTel) {
		this.investTel = investTel;
	}
	
	public Long getInvestTel() {
		return this.investTel;
	}
	
	public void setInvestEmail(String investEmail) {
		this.investEmail = investEmail;
	}
	
	public String getInvestEmail() {
		return this.investEmail;
	}
	
	public void setBorrowAddress(String borrowAddress) {
		this.borrowAddress = borrowAddress;
	}
	
	public String getBorrowAddress() {
		return this.borrowAddress;
	}
	
	public void setBidBillNo(String bidBillNo) {
		this.bidBillNo = bidBillNo;
	}
	
	public String getBidBillNo() {
		return this.bidBillNo;
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
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}

