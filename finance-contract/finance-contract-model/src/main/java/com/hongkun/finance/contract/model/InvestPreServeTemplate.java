package  com.hongkun.finance.contract.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 投资交易过程数据保全模板
 * @author liangbin
 *
 */
public class InvestPreServeTemplate implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*************投资人信息********************/
	/**
	 * 保全号
	 * 当追加数据时使用
	 */
	private String recordNo;
	/**
	 * @desction  流程号(每一步操作的流程号，固定值)多流程保全需要传此号;
	 * @desction   值：X-0406001	投资交易过程信息数据保全(1);
	 * 				X-0406002	投资交易过程信息数据保全(2)附件追加; 
	 * 				X-0406003	投资交易过程信息数据保全(3)回款清单追加;
	 */
	private String flowNo;
	/**
	 * 投资记录code
	 * 唯一标识
	 */
	private String investCode;
	/**
	 * 姓名
	 */
	private String investUserName;
	/**
	 * 手机号
	 */
	private String investPhoneId;
	/**
	 * 身份证号
	 */
	private String investIdCard;
	/**
	 * 注册时间
	 */
	private String investRegTime;
	/**
	 * 认证成功时间
	 */
	private String investAuthTime;
	
	/*************项目信息********************/
	/**
	 * 项目编号
	 */
	private String loanNo;
	/**
	 * 项目title
	 */
	private String biddTitle;
	/**
	 * 年化收益率
	 */
	private String loanRate;
	/**
	 * 项目总额
	 */
	private BigDecimal loanMoney;
	/**
	 * 项目期限
	 * 字符（单位：天）
	 */
	private String loanDay;
	/**
	 * 还款方式
	 * 中文字符	例：到期还本付息
	 */
	private String loanRepayType;
	/**
	 * 发布时间
	 */
	private String releaseTime;
	/**
	 * 起息日期
	 */
	private String interestTime;
	/**
	 * 募集结束日期
	 */
	private String raiseEndTime;
	/**
	 * 起投金额
	 */
	private BigDecimal smallBeginMoney;
	/**
	 * 担保公司
	 */
	private String ensureCompany;
	
	/*************借款人信息********************/
	/**
	 * 借款人姓名
	 */
	private String loanUserName;
	/**
	 * 借款人身份证
	 */
	private String loanIdCard;
	/**
	 * 借款人联系电话
	 */
	private String loanPhoneId;
	/**
	 * 借款人联系地址
	 */
	private String loanUserAddress;
	/**
	 * 借款人抵押信息
	 */
	private String loanMortgage;
	
	/*************投资信息********************/
	/**
	 * 投资账户id
	 */
	private String investUserId;
	/**
	 * 投资金额
	 */
	private BigDecimal investMoeny;
	/**
	 * 红包抵扣金额
	 */
	private BigDecimal redDeduction;
	/**
	 * 购买时间
	 */
	private String investTime;
	/**
	 * 支付成功时间
	 */
	private String investPaymentTime;
	
	/*************实际回款清单（数据增加）*******************/
	/**
	 * 还款编号（数据增加）
	 */
	private String repayId;
	/**
	 * 实际回款日期（数据增加）
	 */
	private String repayTime;
	/**
	 * 实际回款金额（数据增加）
	 */
	private String repayMoney;
	/**
	 * 附件路径
	 */
	private String attachmentPath;
	/**
	 * 附件描述信息
	 */
	private String attachDesc;
	public String getRecordNo() {
		return recordNo;
	}
	public void setRecordNo(String recordNo) {
		this.recordNo = recordNo;
	}
	public String getFlowNo() {
		return flowNo;
	}
	public void setFlowNo(String flowNo) {
		this.flowNo = flowNo;
	}
	public String getInvestCode() {
		return investCode;
	}
	public void setInvestCode(String investCode) {
		this.investCode = investCode;
	}
	public String  getInvestUserName() {
		return investUserName;
	}
	public void setInvestUserName(String investUserName) {
		this.investUserName = investUserName;
	}
	public String getInvestPhoneId() {
		return investPhoneId;
	}
	public void setInvestPhoneId(String investPhoneId) {
		this.investPhoneId = investPhoneId;
	}
	public String getInvestIdCard() {
		return investIdCard;
	}
	public void setInvestIdCard(String investIdCard) {
		this.investIdCard = investIdCard;
	}
	public String getInvestRegTime() {
		return investRegTime;
	}
	public void setInvestRegTime(String investRegTime) {
		this.investRegTime = investRegTime;
	}
	public String getInvestAuthTime() {
		return investAuthTime;
	}
	public void setInvestAuthTime(String investAuthTime) {
		this.investAuthTime = investAuthTime;
	}
	public String getLoanNo() {
		return loanNo;
	}
	public void setLoanNo(String loanNo) {
		this.loanNo = loanNo;
	}
	public String getLoanRate() {
		return loanRate;
	}
	public void setLoanRate(String loanRate) {
		this.loanRate = loanRate;
	}
	public BigDecimal getLoanMoney() {
		return loanMoney;
	}
	public void setLoanMoney(BigDecimal loanMoney) {
		this.loanMoney = loanMoney;
	}
	public String getLoanDay() {
		return loanDay;
	}
	public void setLoanDay(String loanDay) {
		this.loanDay = loanDay;
	}
	public String getLoanRepayType() {
		return loanRepayType;
	}
	public void setLoanRepayType(String loanRepayType) {
		this.loanRepayType = loanRepayType;
	}
	public String getReleaseTime() {
		return releaseTime;
	}
	public void setReleaseTime(String releaseTime) {
		this.releaseTime = releaseTime;
	}
	public String getInterestTime() {
		return interestTime;
	}
	public void setInterestTime(String interestTime) {
		this.interestTime = interestTime;
	}
	public String getRaiseEndTime() {
		return raiseEndTime;
	}
	public void setRaiseEndTime(String raiseEndTime) {
		this.raiseEndTime = raiseEndTime;
	}
	public BigDecimal getSmallBeginMoney() {
		return smallBeginMoney;
	}
	public void setSmallBeginMoney(BigDecimal smallBeginMoney) {
		this.smallBeginMoney = smallBeginMoney;
	}
	public String getEnsureCompany() {
		return ensureCompany;
	}
	public void setEnsureCompany(String ensureCompany) {
		this.ensureCompany = ensureCompany;
	}
	public String getLoanUserName() {
		return loanUserName;
	}
	public void setLoanUserName(String loanUserName) {
		this.loanUserName = loanUserName;
	}
	public String getLoanIdCard() {
		return loanIdCard;
	}
	public void setLoanIdCard(String loanIdCard) {
		this.loanIdCard = loanIdCard;
	}
	public String getLoanPhoneId() {
		return loanPhoneId;
	}
	public void setLoanPhoneId(String loanPhoneId) {
		this.loanPhoneId = loanPhoneId;
	}
	public String getLoanUserAddress() {
		return loanUserAddress;
	}
	public void setLoanUserAddress(String loanUserAddress) {
		this.loanUserAddress = loanUserAddress;
	}
	public String getLoanMortgage() {
		return loanMortgage;
	}
	public void setLoanMortgage(String loanMortgage) {
		this.loanMortgage = loanMortgage;
	}
	public String getInvestUserId() {
		return investUserId;
	}
	public void setInvestUserId(String investUserId) {
		this.investUserId = investUserId;
	}
	public BigDecimal getInvestMoeny() {
		return investMoeny;
	}
	public void setInvestMoeny(BigDecimal investMoeny) {
		this.investMoeny = investMoeny;
	}
	public BigDecimal getRedDeduction() {
		return redDeduction;
	}
	public void setRedDeduction(BigDecimal redDeduction) {
		this.redDeduction = redDeduction;
	}
	public String getInvestTime() {
		return investTime;
	}
	public void setInvestTime(String investTime) {
		this.investTime = investTime;
	}
	public String getInvestPaymentTime() {
		return investPaymentTime;
	}
	public void setInvestPaymentTime(String investPaymentTime) {
		this.investPaymentTime = investPaymentTime;
	}
	public String getRepayId() {
		return repayId;
	}
	public void setRepayId(String repayId) {
		this.repayId = repayId;
	}
	public String getRepayTime() {
		return repayTime;
	}
	public void setRepayTime(String repayTime) {
		this.repayTime = repayTime;
	}
	public String getRepayMoney() {
		return repayMoney;
	}
	public void setRepayMoney(String repayMoney) {
		this.repayMoney = repayMoney;
	}
	public String getAttachmentPath() {
		return attachmentPath;
	}
	public void setAttachmentPath(String attachmentPath) {
		this.attachmentPath = attachmentPath;
	}
	public String getAttachDesc() {
		return attachDesc;
	}
	public void setAttachDesc(String attachDesc) {
		this.attachDesc = attachDesc;
	}
	public String getBiddTitle() {
		return biddTitle;
	}
	public void setBiddTitle(String biddTitle) {
		this.biddTitle = biddTitle;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}
