package com.hongkun.finance.invest.model;

import com.yirun.framework.core.model.BaseModel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.loan.model.BidInfoDetail.java
 * @Class Name    : BidInfoDetail.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class BidInfoDetail extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: id
	 * 字段: id  INT(10)
	 */
	private java.lang.Integer id;
	
	/**
	 * 描述: 标的表ID
	 * 字段: bidd_info_id  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer biddInfoId;
	
	/**
	 * 描述: 审核意见
	 * 字段: autid_note  VARCHAR(300)
	 * 默认值: ''
	 */
	private java.lang.String autidNote;
	
	/**
	 * 描述: 借款描述
	 * 字段: description  VARCHAR(300)
	 * 默认值: ''
	 */
	private java.lang.String description;
	
	/**
	 * 描述: 项目信息
	 * 字段: project_description  VARCHAR(100)
	 * 默认值: ''
	 */
	private java.lang.String projectDescription;
	
	/**
	 * 描述: 风险控制措施
	 * 字段: assure_description  VARCHAR(1000)
	 * 默认值: ''
	 */
	private java.lang.String assureDescription;
	
	/**
	 * 描述: 还款来源
	 * 字段: repayment_source  VARCHAR(200)
	 * 默认值: ''
	 */
	private java.lang.String repaymentSource;
	
	/**
	 * 描述: 相关法律文件
	 * 字段: legal_files  VARCHAR(2000)
	 * 默认值: ''
	 */
	private java.lang.String legalFiles;
	
	/**
	 * 描述: 资金用途描述
	 * 字段: fund_use  VARCHAR(500)
	 * 默认值: ''
	 */
	private java.lang.String fundUse;
	
	/**
	 * 描述: 票据号
	 * 字段: bill_no  VARCHAR(36)
	 * 默认值: ''
	 */
	private java.lang.String billNo;
	
	/**
	 * 描述: 好友推荐规则ID
	 * 字段: rule_id  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer ruleId;
	
	/**
	 * 描述: 好友推荐状态:0-不参与,1-参与
	 * 字段: recommend_state  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer recommendState;
	
	/**
	 * 描述: 赠送积分状态:0-不赠送,1-赠送
	 * 字段: giving_point_state  TINYINT(3)
	 * 默认值: 1
	 */
	private Integer givingPointState;
	
	/**
	 * 描述: 提前归还本金状态:0-不允许,1-允许
	 * 字段: advance_repay_state  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer advanceRepayState;
	
	/**
	 * 描述: 逾期罚息状态:0-否,1-是
	 * 字段: punish_state  TINYINT(3)
	 * 默认值: 1
	 */
	private Integer punishState;
	
	/**
	 * 描述: 预留借款人利息状态:0-不预留,1-预留
	 * 字段: reserve_interest  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer reserveInterest;
	
	/**
	 * 描述: 代扣状态:0-默认,1-同意(代扣),2-拒绝(原始还款模式)
	 * 字段: withhold_state  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer withholdState;
	
	/**
	 * 描述: 匹配状态:0-未匹配,1-完全匹配,2-部分匹配,3-匹配完毕。
	 * 字段: match_state  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer matchState;
	
	/**
	 * 描述: 下次匹配时间
	 * 字段: next_match_date  DATETIME(19)
	 * 默认值: 0000-00-00 00:00:00
	 */
	private java.util.Date nextMatchDate;
	
	//【非数据库字段，查询时使用】
	private java.util.Date nextMatchDateBegin;
	
	//【非数据库字段，查询时使用】
	private java.util.Date nextMatchDateEnd;
	/**
	 * 描述: 0-默认直接投资,1-代表后台匹配
	 * 字段: match_type  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer matchType;
	
	/**
	 * 描述: 是否为交易所产品:0-不是,1-是
	 * 字段: bourse_flg  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer bourseFlg;
	
	/**
	 * 描述: 归还本金计息方式:1-按日计息,2-按月计息
	 * 字段: return_cap_way  TINYINT(3)
	 * 默认值: 1
	 */
	private Integer returnCapWay;
	
	/**
	 * 描述: 借款满n天后才允许提前还本金
	 * 字段: return_cap_days  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer returnCapDays;
	
	/**
	 * 描述: 已还本金
	 * 字段: return_cap_amount  DECIMAL(20)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal returnCapAmount;
	
	/**
	 * 描述: 转让前持有债权天数
	 * 字段: creditor_days  SMALLINT(5)
	 * 默认值: 0
	 */
	private Integer creditorDays;
	
	/**
	 * 描述: 转让交易日期距最后一次还款日期天数
	 * 字段: deal_repay_days  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer dealRepayDays;
	
	/**
	 * 描述: 折价率范围起始（%）
	 * 字段: convert_rate_start  TINYINT(3)
	 * 默认值: 100
	 */
	private BigDecimal convertRateStart;

	/**
	 * 描述: 折价率范围结束（%）
	 * 字段: convert_rate_start  TINYINT(3)
	 * 默认值: 100
	 */
	private BigDecimal convertRateEnd;

	/**
	 * 描述: 溢价率范围起始（%）
	 * 字段: overflow_rate_end  TINYINT(3)
	 * 默认值: 100
	 */
	private BigDecimal overflowRateStart;

	/**
	 * 描述: 溢价率范围结束（%）
	 * 字段: overflow_rate_end  TINYINT(3)
	 * 默认值: 100
	 */
	private BigDecimal overflowRateEnd;
	
	/**
	 * 描述: 到期是否提醒0-不提醒,1-提醒
	 * 字段: maturity_remind  TINYINT(3)
	 * 默认值: 1
	 */
	private Integer maturityRemind;
	
	/**
	 * 描述: 债权最多转让次数
	 * 字段: most_transfer_count  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer mostTransferCount;
	
	/**
	 * 描述: 债券转让手续费率
	 * 字段: transfer_rate  DECIMAL(4)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal transferRate;
	
	/**
	 * 描述: 创建人员ID
	 * 字段: created_user_id  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer createdUserId;
	
	/**
	 * 描述: 最后修改人员ID
	 * 字段: modified_user_id  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer modifiedUserId;
	
	/**
	 * 描述: 创建时间
	 * 字段: create_time  DATETIME(19)
	 * 默认值: CURRENT_TIMESTAMP
	 */
	private java.util.Date createTime;
	
	private Integer creditorState;
	
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
	
	private Integer transferDays;
 
	public BidInfoDetail(){
	}

	public BidInfoDetail(java.lang.Integer id){
		this.id = id;
	}

	public void setId(java.lang.Integer id) {
		this.id = id;
	}
	
	public java.lang.Integer getId() {
		return this.id;
	}
	
	public void setBiddInfoId(java.lang.Integer biddInfoId) {
		this.biddInfoId = biddInfoId;
	}
	
	public java.lang.Integer getBiddInfoId() {
		return this.biddInfoId;
	}
	
	public void setAutidNote(java.lang.String autidNote) {
		this.autidNote = autidNote;
	}
	
	public java.lang.String getAutidNote() {
		return this.autidNote;
	}
	
	public void setDescription(java.lang.String description) {
		this.description = description;
	}
	
	public java.lang.String getDescription() {
		return this.description;
	}
	
	public void setProjectDescription(java.lang.String projectDescription) {
		this.projectDescription = projectDescription;
	}
	
	public java.lang.String getProjectDescription() {
		return this.projectDescription;
	}
	
	public void setAssureDescription(java.lang.String assureDescription) {
		this.assureDescription = assureDescription;
	}
	
	public java.lang.String getAssureDescription() {
		return this.assureDescription;
	}
	
	public void setRepaymentSource(java.lang.String repaymentSource) {
		this.repaymentSource = repaymentSource;
	}
	
	public java.lang.String getRepaymentSource() {
		return this.repaymentSource;
	}
	
	public void setLegalFiles(java.lang.String legalFiles) {
		this.legalFiles = legalFiles;
	}
	
	public java.lang.String getLegalFiles() {
		return this.legalFiles;
	}
	
	public void setFundUse(java.lang.String fundUse) {
		this.fundUse = fundUse;
	}
	
	public java.lang.String getFundUse() {
		return this.fundUse;
	}
	
	public void setBillNo(java.lang.String billNo) {
		this.billNo = billNo;
	}
	
	public java.lang.String getBillNo() {
		return this.billNo;
	}
	
	public void setRuleId(java.lang.Integer ruleId) {
		this.ruleId = ruleId;
	}
	
	public java.lang.Integer getRuleId() {
		return this.ruleId;
	}
	
	public void setRecommendState(Integer recommendState) {
		this.recommendState = recommendState;
	}
	
	public Integer getRecommendState() {
		return this.recommendState;
	}
	
	public void setGivingPointState(Integer givingPointState) {
		this.givingPointState = givingPointState;
	}
	
	public Integer getGivingPointState() {
		return this.givingPointState;
	}
	
	public void setAdvanceRepayState(Integer advanceRepayState) {
		this.advanceRepayState = advanceRepayState;
	}
	
	public Integer getAdvanceRepayState() {
		return this.advanceRepayState;
	}
	
	public void setPunishState(Integer punishState) {
		this.punishState = punishState;
	}
	
	public Integer getPunishState() {
		return this.punishState;
	}
	
	public void setReserveInterest(Integer reserveInterest) {
		this.reserveInterest = reserveInterest;
	}
	
	public Integer getReserveInterest() {
		return this.reserveInterest;
	}
	
	public void setWithholdState(Integer withholdState) {
		this.withholdState = withholdState;
	}
	
	public Integer getWithholdState() {
		return this.withholdState;
	}
	
	public void setMatchState(Integer matchState) {
		this.matchState = matchState;
	}
	
	public Integer getMatchState() {
		return this.matchState;
	}
	
	public void setNextMatchDate(java.util.Date nextMatchDate) {
		this.nextMatchDate = nextMatchDate;
	}
	
	public java.util.Date getNextMatchDate() {
		return this.nextMatchDate;
	}
	
	public void setNextMatchDateBegin(java.util.Date nextMatchDateBegin) {
		this.nextMatchDateBegin = nextMatchDateBegin;
	}
	
	public java.util.Date getNextMatchDateBegin() {
		return this.nextMatchDateBegin;
	}
	
	public void setNextMatchDateEnd(java.util.Date nextMatchDateEnd) {
		this.nextMatchDateEnd = nextMatchDateEnd;
	}
	
	public java.util.Date getNextMatchDateEnd() {
		return this.nextMatchDateEnd;
	}	
	public void setMatchType(Integer matchType) {
		this.matchType = matchType;
	}
	
	public Integer getMatchType() {
		return this.matchType;
	}
	
	public void setBourseFlg(Integer bourseFlg) {
		this.bourseFlg = bourseFlg;
	}
	
	public Integer getBourseFlg() {
		return this.bourseFlg;
	}
	
	public void setReturnCapWay(Integer returnCapWay) {
		this.returnCapWay = returnCapWay;
	}
	
	public Integer getReturnCapWay() {
		return this.returnCapWay;
	}
	
	public void setReturnCapDays(Integer returnCapDays) {
		this.returnCapDays = returnCapDays;
	}
	
	public Integer getReturnCapDays() {
		return this.returnCapDays;
	}
	
	public void setReturnCapAmount(java.math.BigDecimal returnCapAmount) {
		this.returnCapAmount = returnCapAmount;
	}
	
	public java.math.BigDecimal getReturnCapAmount() {
		return this.returnCapAmount;
	}
	
	public void setCreditorDays(Integer creditorDays) {
		this.creditorDays = creditorDays;
	}
	
	public Integer getCreditorDays() {
		return this.creditorDays;
	}
	
	public void setDealRepayDays(Integer dealRepayDays) {
		this.dealRepayDays = dealRepayDays;
	}
	
	public Integer getDealRepayDays() {
		return this.dealRepayDays;
	}
	
	public void setConvertRateStart(BigDecimal convertRateStart) {
		this.convertRateStart = convertRateStart;
	}
	
	public BigDecimal getConvertRateStart() {
		return this.convertRateStart;
	}
	
	public void setOverflowRateEnd(BigDecimal overflowRateEnd) {
		this.overflowRateEnd = overflowRateEnd;
	}
	
	public BigDecimal getOverflowRateEnd() {
		return this.overflowRateEnd;
	}
	
	public void setMaturityRemind(Integer maturityRemind) {
		this.maturityRemind = maturityRemind;
	}
	
	public Integer getMaturityRemind() {
		return this.maturityRemind;
	}
	
	public void setMostTransferCount(Integer mostTransferCount) {
		this.mostTransferCount = mostTransferCount;
	}
	
	public Integer getMostTransferCount() {
		return this.mostTransferCount;
	}
	
	public void setTransferRate(java.math.BigDecimal transferRate) {
		this.transferRate = transferRate;
	}
	
	public java.math.BigDecimal getTransferRate() {
		return this.transferRate;
	}
	
	public void setCreatedUserId(java.lang.Integer createdUserId) {
		this.createdUserId = createdUserId;
	}
	
	public java.lang.Integer getCreatedUserId() {
		return this.createdUserId;
	}
	
	public void setModifiedUserId(java.lang.Integer modifiedUserId) {
		this.modifiedUserId = modifiedUserId;
	}
	
	public java.lang.Integer getModifiedUserId() {
		return this.modifiedUserId;
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

	public BigDecimal getConvertRateEnd() {
		return convertRateEnd;
	}

	public void setConvertRateEnd(BigDecimal convertRateEnd) {
		this.convertRateEnd = convertRateEnd;
	}

	public BigDecimal getOverflowRateStart() {
		return overflowRateStart;
	}

	public void setOverflowRateStart(BigDecimal overflowRateStart) {
		this.overflowRateStart = overflowRateStart;
	}
	public Integer getCreditorState() {
		return creditorState;
	}

	public void setCreditorState(Integer creditorState) {
		this.creditorState = creditorState;
	}
	
	public Integer getTransferDays() {
		return transferDays;
	}
	public void setTransferDays(Integer transferDays) {
		this.transferDays = transferDays;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}

