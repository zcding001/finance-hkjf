package com.hongkun.finance.invest.model;

import com.yirun.framework.core.model.BaseModel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.invest.model.BidInfo.java
 * @Class Name    : BidInfo.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class BidInfo extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: id
	 * 字段: id  INT(10)
	 */
	private java.lang.Integer id;
	private List<Integer> ids = new ArrayList<>();
	
	/**
	 * 描述: 标的名称
	 * 字段: name  VARCHAR(50)
	 * 默认值: ''
	 */
	private java.lang.String name;
	
	/**
	 * 描述: 标的编码
	 * 字段: bid_code  VARCHAR(50)
	 * 默认值: ''
	 */
	private java.lang.String bidCode;
	
	/**
	 * 描述: 产品ID
	 * 字段: bid_product_id  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer bidProductId;
	
	/**
	 * 描述: 标的总金额
	 * 字段: total_amount  DECIMAL(20)
	 * 默认值: 0.00
	 */
	@NotNull(message = "标的总金额不能为空")
	private java.math.BigDecimal totalAmount;
	
	/**
	 * 描述: 标的剩余金额
	 * 字段: residue_amount  DECIMAL(20)
	 * 默认值: 0.00
	 */
	@NotNull(message = "标的剩余金额不能为空")
	private java.math.BigDecimal residueAmount;
	
	/**
	 * 上次更新时间，用于作为更新版本控制
	 */
	private java.math.BigDecimal preResidueAmount;	
	
	/**
	 * 描述: 年化率
	 * 字段: interest_rate  DECIMAL(4)
	 * 默认值: 0.00
	 */
	@NotNull(message = "年化率不能为空")
	private java.math.BigDecimal interestRate;


	/**
	 * 加息率
	 */
	private BigDecimal raiseRate;
	
	/**
	 * 描述: 期限值
	 * 字段: term_value  SMALLINT(5)
	 * 默认值: 0
	 */
	@NotNull(message = "期限值不能为空")
	private Integer termValue;
	
	/**
	 * 描述: 期限单位:1-年,2-月,3-天
	 * 字段: term_unit  TINYINT(3)
	 * 默认值: 2
	 */
	@NotNull(message = "期限单位不能为空")
	private Integer termUnit;
	
	/**
	 * 描述: 还款方式:1-等额本息,2-按月付息，到期还本,3-到期还本付息,4-到期付息，本金回收,5-每月付息，到期本金回收,6-按月付息，本金划归企业
	 * 字段: bidd_repayment_way  TINYINT(3)
	 * 默认值: 2
	 */
	@NotNull(message = "还款方式不能为空")
	private Integer biddRepaymentWay;
	
	/**
	 * 描述: 投标开始时间
	 * 字段: start_time  DATETIME(19)
	 * 默认值: 0000-00-00 00:00:00
	 */
	private java.util.Date startTime;
	
	//【非数据库字段，查询时使用】
	private java.util.Date startTimeBegin;
	
	//【非数据库字段，查询时使用】
	private java.util.Date startTimeEnd;
	/**
	 * 描述: 投标结束时间
	 * 字段: end_time  DATETIME(19)
	 * 默认值: 0000-00-00 00:00:00
	 */
	private java.util.Date endTime;
	
	//【非数据库字段，查询时使用】
	private java.util.Date endTimeBegin;
	
	//【非数据库字段，查询时使用】
	private java.util.Date endTimeEnd;
	/**
	 * 描述: 放款时间
	 * 字段: lending_time  DATETIME(19)
	 * 默认值: 0000-00-00 00:00:00
	 */
	private java.util.Date lendingTime;
	
	//【非数据库字段，查询时使用】
	private java.util.Date lendingTimeBegin;
	
	//【非数据库字段，查询时使用】
	private java.util.Date lendingTimeEnd;
	
	//【非数据库字段】
	private java.util.Date endDate;
	/**
	 * 描述: 标的属性:0-正常,1-爆款标,2-推荐标
	 * 字段: type  TINYINT(3)
	 * 默认值: 0
	 */
	@NotNull(message = "标的属性")
	private Integer type;
	
	/**
	 * 描述: 显示平台:1-PC,2-APP。
	 * 字段: show_position  VARCHAR(10)
	 * 默认值: '1,2'
	 */
	@NotNull(message = "显示平台不能为空")
	private java.lang.String showPosition;
	
	/**
	 * 描述: 投资平台:1-PC,2-APP
	 * 字段: invest_position  VARCHAR(10)
	 * 默认值: '1,2'
	 */
	@NotNull(message = "投资平台不能为空")
	private java.lang.String investPosition;
	
	/**
	 * 描述: 招标方案值
	 * 字段: bid_scheme_value  SMALLINT(5)
	 * 默认值: 0
	 */
	@NotNull(message = "招标方案值不能为空")
	private Integer bidSchemeValue;
	
	/**
	 * 描述: 招标方案:0-平均金额招标,1-最低金额招标
	 * 字段: bid_scheme  TINYINT(3)
	 * 默认值: 0
	 */
	@NotNull(message = "招标方案值不能为空")
	private Integer bidScheme;
	
	/**
	 * 描述: 排序:0-正常顺序,1-置顶
	 * 字段: sort  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer sort;
	
	/**
	 * 描述: 风控方式:1-质,2-保,3-实,4-抵
	 * 字段: assureType  VARCHAR(20)
	 * 默认值: ''
	 */
	@NotNull(message = "风控方式不能为空")
	private java.lang.String assureType;
	
	/**
	 * 描述: 信用等级
	 * 字段: credit_level  VARCHAR(20)
	 * 默认值: ''
	 */
	private java.lang.String creditLevel;
	
	/**
	 * 描述: 借款人ID
	 * 字段: borrower_id  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer borrowerId;
	
	/**
	 * 描述: 偿还本金人员ID
	 * 字段: repay_user_id  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer repayUserId;
	
	/**
	 * 描述: 本金接收人员ID
	 * 字段: receipt_user_id  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer receiptUserId;
	
	/**
	 * 描述: 企业账号ID
	 * 字段: company_id  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer companyId;
	
	/**
	 * 描述: 借款手续费率
	 * 字段: commission_rate  DECIMAL(2)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal commissionRate;
	
	/**
	 * 描述: 借款服务费率(备注：借款人每月应还给平台服务费=总借款额*借款服务费率/借款期限)
	 * 字段: service_rate  DECIMAL(2)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal serviceRate;
	
	/**
	 * 描述: 垫付服务费率
	 * 字段: advance_service_rate  DECIMAL(2)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal advanceServiceRate;
	
	/**
	 * 描述: 逾期违约金费率
	 * 字段: liquidated_damages_rate  DECIMAL(2)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal liquidatedDamagesRate;
	
	/**
	 * 描述: 项目图片
	 * 字段: img_url  VARCHAR(200)
	 * 默认值: ''
	 */
	private java.lang.String imgUrl;
	
	/**
	 * 描述: 印章图片
	 * 字段: print_imgurl  VARCHAR(200)
	 * 默认值: ''
	 */
	private java.lang.String printImgurl;

	/**
	 * 描述: 标地标签内容
	 * 字段: label_text  VARCHAR(50)
	 * 默认值: ''
	 */
	private java.lang.String labelText;

	/**
	 * 描述: 标签跳转链接
	 * 字段: label_url  VARCHAR(200)
	 * 默认值: ''
	 */
	private java.lang.String labelUrl;
	/**
	 * 描述: 借款用途:1-短期周转,2-项目贷款,3-临时倒短,4-扩大生产
	 * 字段: loan_use  TINYINT(3)
	 * 默认值: 1
	 */
	private Integer loanUse;
	
	/**
	 * 描述: 创建人员ID
	 * 字段: create_user_id  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer createUserId;
	
	/**
	 * 描述: 最后修改人员ID
	 * 字段: modify_user_id  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer modifyUserId;
	
	/**
	 * 描述: 状态:0-初始化,1-待上架,2-投标中,3-满标待审核,4-待放款,5-放款中,6-已完成,7-上架审核拒绝,8-满标审核拒绝,9-删除
	 * 字段: state  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer state;

    /**
     * 此标的是否允许使用卡券：0：不支持 1-不限制；2:仅加息券；3：仅红包
     */
    private Integer allowCoupon;

	/**
	 * 描述: 当招标方案选择为平均招标金额的时候的投资步长
	 * 字段: step_value  INT(11)
	 * 默认值: 0
	 */
	private Integer stepValue;

    /**
	 * 描述: 创建时间
	 * 字段: create_time  DATETIME(19)
	 * 默认值: CURRENT_TIMESTAMP
	 */
	private java.util.Date createTime;

	//作用域：1-hkjf, 2-cxj
	private Integer actionScope;

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
	//【非数据库字段，处理匹配转让时使用 临时存储转让标的的转让金额和接受标的的匹配金额】
	private java.math.BigDecimal creditorMoney;
	//标的状态集合
	private List<Integer> stateList;
	
	public BidInfo(){
	}



    public Integer getAllowCoupon() {
        return allowCoupon;
    }

    public void setAllowCoupon(Integer allowCoupon) {
        this.allowCoupon = allowCoupon;
    }

    public BidInfo(java.lang.Integer id){
		this.id = id;
	}

	public List<Integer> getStateList() {
        return stateList;
    }



    public void setStateList(List<Integer> stateList) {
        this.stateList = stateList;
    }



    public void setId(java.lang.Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public void setName(java.lang.String name) {
		this.name = name;
	}
	
	public java.lang.String getName() {
		return this.name;
	}
	
	public void setBidCode(java.lang.String bidCode) {
		this.bidCode = bidCode;
	}
	
	public java.lang.String getBidCode() {
		return this.bidCode;
	}
	
	public void setBidProductId(java.lang.Integer bidProductId) {
		this.bidProductId = bidProductId;
	}
	
	public java.lang.Integer getBidProductId() {
		return this.bidProductId;
	}
	
	public void setTotalAmount(java.math.BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	
	public java.math.BigDecimal getTotalAmount() {
		return this.totalAmount;
	}
	
	public void setResidueAmount(java.math.BigDecimal residueAmount) {
		this.residueAmount = residueAmount;
	}
	
	public java.math.BigDecimal getResidueAmount() {
		return this.residueAmount;
	}
	
	public void setInterestRate(java.math.BigDecimal interestRate) {
		this.interestRate = interestRate;
	}
	
	public java.math.BigDecimal getInterestRate() {
		return this.interestRate;
	}
	
	public void setTermValue(Integer termValue) {
		this.termValue = termValue;
	}
	
	public Integer getTermValue() {
		return this.termValue;
	}
	
	public void setTermUnit(Integer termUnit) {
		this.termUnit = termUnit;
	}
	
	public Integer getTermUnit() {
		return this.termUnit;
	}
	
	public void setBiddRepaymentWay(Integer biddRepaymentWay) {
		this.biddRepaymentWay = biddRepaymentWay;
	}
	
	public Integer getBiddRepaymentWay() {
		return this.biddRepaymentWay;
	}
	
	public void setStartTime(java.util.Date startTime) {
		this.startTime = startTime;
	}
	
	public java.util.Date getStartTime() {
		return this.startTime;
	}
	
	public void setStartTimeBegin(java.util.Date startTimeBegin) {
		this.startTimeBegin = startTimeBegin;
	}
	
	public java.util.Date getStartTimeBegin() {
		return this.startTimeBegin;
	}
	
	public void setStartTimeEnd(java.util.Date startTimeEnd) {
		this.startTimeEnd = startTimeEnd;
	}
	
	public java.util.Date getStartTimeEnd() {
		return this.startTimeEnd;
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
	public void setLendingTime(java.util.Date lendingTime) {
		this.lendingTime = lendingTime;
	}
	
	public java.util.Date getLendingTime() {
		return this.lendingTime;
	}
	
	public void setLendingTimeBegin(java.util.Date lendingTimeBegin) {
		this.lendingTimeBegin = lendingTimeBegin;
	}
	
	public java.util.Date getLendingTimeBegin() {
		return this.lendingTimeBegin;
	}
	
	public void setLendingTimeEnd(java.util.Date lendingTimeEnd) {
		this.lendingTimeEnd = lendingTimeEnd;
	}
	
	public java.util.Date getLendingTimeEnd() {
		return this.lendingTimeEnd;
	}	
	public void setType(Integer type) {
		this.type = type;
	}
	
	public Integer getType() {
		return this.type;
	}
	
	public void setShowPosition(java.lang.String showPosition) {
		this.showPosition = showPosition;
	}
	
	public java.lang.String getShowPosition() {
		return this.showPosition;
	}
	
	public void setInvestPosition(java.lang.String investPosition) {
		this.investPosition = investPosition;
	}
	
	public java.lang.String getInvestPosition() {
		return this.investPosition;
	}
	
	public void setBidSchemeValue(Integer bidSchemeValue) {
		this.bidSchemeValue = bidSchemeValue;
	}
	
	public Integer getBidSchemeValue() {
		return this.bidSchemeValue;
	}
	
	public void setBidScheme(Integer bidScheme) {
		this.bidScheme = bidScheme;
	}
	
	public Integer getBidScheme() {
		return this.bidScheme;
	}
	
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
	public Integer getSort() {
		return this.sort;
	}
	
	public void setAssureType(java.lang.String assureType) {
		this.assureType = assureType;
	}
	
	public java.lang.String getAssureType() {
		return this.assureType;
	}
	
	public void setCreditLevel(java.lang.String creditLevel) {
		this.creditLevel = creditLevel;
	}
	
	public java.lang.String getCreditLevel() {
		return this.creditLevel;
	}
	
	public void setBorrowerId(java.lang.Integer borrowerId) {
		this.borrowerId = borrowerId;
	}
	
	public java.lang.Integer getBorrowerId() {
		return this.borrowerId;
	}
	
	public void setRepayUserId(java.lang.Integer repayUserId) {
		this.repayUserId = repayUserId;
	}
	
	public java.lang.Integer getRepayUserId() {
		return this.repayUserId;
	}
	
	public void setReceiptUserId(java.lang.Integer receiptUserId) {
		this.receiptUserId = receiptUserId;
	}
	
	public java.lang.Integer getReceiptUserId() {
		return this.receiptUserId;
	}
	
	public void setCompanyId(java.lang.Integer companyId) {
		this.companyId = companyId;
	}
	
	public java.lang.Integer getCompanyId() {
		return this.companyId;
	}
	
	public void setCommissionRate(java.math.BigDecimal commissionRate) {
		this.commissionRate = commissionRate;
	}
	
	public java.math.BigDecimal getCommissionRate() {
		return this.commissionRate;
	}
	
	public void setServiceRate(java.math.BigDecimal serviceRate) {
		this.serviceRate = serviceRate;
	}
	
	public java.math.BigDecimal getServiceRate() {
		return this.serviceRate;
	}
	
	public void setAdvanceServiceRate(java.math.BigDecimal advanceServiceRate) {
		this.advanceServiceRate = advanceServiceRate;
	}
	
	public java.math.BigDecimal getAdvanceServiceRate() {
		return this.advanceServiceRate;
	}
	
	public void setLiquidatedDamagesRate(java.math.BigDecimal liquidatedDamagesRate) {
		this.liquidatedDamagesRate = liquidatedDamagesRate;
	}
	
	public java.math.BigDecimal getLiquidatedDamagesRate() {
		return this.liquidatedDamagesRate;
	}
	
	public void setImgUrl(java.lang.String imgUrl) {
		this.imgUrl = imgUrl;
	}
	
	public java.lang.String getImgUrl() {
		return this.imgUrl;
	}
	
	public void setPrintImgurl(java.lang.String printImgurl) {
		this.printImgurl = printImgurl;
	}
	
	public java.lang.String getPrintImgurl() {
		return this.printImgurl;
	}

	public String getLabelText() {
		return labelText;
	}

	public void setLabelText(String labelText) {
		this.labelText = labelText;
	}

	public String getLabelUrl() {
		return labelUrl;
	}

	public void setLabelUrl(String labelUrl) {
		this.labelUrl = labelUrl;
	}

	public void setLoanUse(Integer loanUse) {
		this.loanUse = loanUse;
	}
	
	public Integer getLoanUse() {
		return this.loanUse;
	}
	
	public void setCreateUserId(java.lang.Integer createUserId) {
		this.createUserId = createUserId;
	}
	
	public java.lang.Integer getCreateUserId() {
		return this.createUserId;
	}
	
	public void setModifyUserId(java.lang.Integer modifyUserId) {
		this.modifyUserId = modifyUserId;
	}
	
	public java.lang.Integer getModifyUserId() {
		return this.modifyUserId;
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
	public java.math.BigDecimal getCreditorMoney() {
		return creditorMoney;
	}
	public void setCreditorMoney(java.math.BigDecimal creditorMoney) {
		this.creditorMoney = creditorMoney;
	}

	public BigDecimal getRaiseRate() {
		return raiseRate;
	}

	public void setRaiseRate(BigDecimal raiseRate) {
		this.raiseRate = raiseRate;
	}

	public Integer getStepValue() {
		return stepValue;
	}

	public void setStepValue(Integer stepValue) {
		this.stepValue = stepValue;
	}

	public Integer getActionScope() {
		return actionScope;
	}

	public void setActionScope(Integer actionScope) {
		this.actionScope = actionScope;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
	public List<Integer> getIds() {
		return ids;
	}
	public void setIds(List<Integer> ids) {
		this.ids = ids;
	}
	public java.util.Date getEndDate() {
		return endDate;
	}
	public void setEndDate(java.util.Date endDate) {
		this.endDate = endDate;
	}
	public java.math.BigDecimal getPreResidueAmount() {
		return preResidueAmount;
	}
	public void setPreResidueAmount(java.math.BigDecimal preResidueAmount) {
		this.preResidueAmount = preResidueAmount;
	}
	
}

