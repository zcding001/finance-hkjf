package com.hongkun.finance.fund.model;

import com.yirun.framework.core.model.BaseModel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.fund.model.FundAgreement.java
 * @Class Name    : FundAgreement.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class FundAgreement extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: id
	 * 字段: id  INT(10)
	 */
	private Integer id;
	
	/**
	 * 描述: 预约记录ID
	 * 字段: fund_advisory_id  INT(10)
	 * 默认值: 0
	 */
	private Integer fundAdvisoryId;
	
	/**
	 * 描述: 股权信息ID
	 * 字段: fund_info_id  INT(10)
	 * 默认值: 0
	 */
	private Integer fundInfoId;
	
	/**
	 * 描述: 状态0-初始化,1-资质审核中,2-资质审核通过,3-资质审核拒绝,4-打款成功,5-打款失败
	 * 字段: state  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer state;
	
	/**
	 * 描述: 审核拒绝原因
	 * 字段: reason  VARCHAR(50)
	 * 默认值: ''
	 */
	private String reason;
	
	/**
	 * 描述: 投资金额
	 * 字段: invest_amount  DECIMAL(20)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal investAmount;
	
	/**
	 * 描述: 用户ID
	 * 字段: reg_user_id  INT(10)
	 * 默认值: 0
	 */
	private Integer regUserId;
	
	/**
	 * 描述: 投资人姓
	 * 字段: user_surname  VARCHAR(36)
	 * 默认值: ''
	 */
	private String userSurname;
	
	/**
	 * 描述: 投资人名
	 * 字段: user_name  VARCHAR(36)
	 * 默认值: ''
	 */
	private String userName;
	
	/**
	 * 描述: 出生日期
	 * 字段: birthday  VARCHAR(36)
	 * 默认值: ''
	 */
	private String birthday;
	
	/**
	 * 描述: 国籍
	 * 字段: nationality  VARCHAR(36)
	 * 默认值: ''
	 */
	private String nationality;
	
	/**
	 * 描述: 出生地
	 * 字段: birth_place  VARCHAR(36)
	 * 默认值: ''
	 */
	private String birthPlace;
	
	/**
	 * 描述: 街道门牌号
	 * 字段: street_number  VARCHAR(50)
	 * 默认值: ''
	 */
	private String streetNumber;
	
	/**
	 * 描述: 行政区
	 * 字段: region  VARCHAR(50)
	 * 默认值: ''
	 */
	private String region;
	
	/**
	 * 描述: 省份/州
	 * 字段: province  VARCHAR(36)
	 * 默认值: ''
	 */
	private String province;
	
	/**
	 * 描述: 国家
	 * 字段: country  VARCHAR(36)
	 * 默认值: ''
	 */
	private String country;
	
	/**
	 * 描述: 邮编
	 * 字段: post_code  VARCHAR(36)
	 * 默认值: ''
	 */
	private String postCode;
	
	/**
	 * 描述: 联系电话
	 * 字段: tel  VARCHAR(36)
	 * 默认值: ''
	 */
	private String tel;
	
	/**
	 * 描述: 联系邮箱
	 * 字段: email  VARCHAR(36)
	 * 默认值: ''
	 */
	private String email;
	
	/**
	 * 描述: 永久地址街道门牌号
	 * 字段: permanent_street_number  VARCHAR(50)
	 * 默认值: ''
	 */
	private String permanentStreetNumber;
	
	/**
	 * 描述: 永久地址行政区
	 * 字段: permanent_region  VARCHAR(50)
	 * 默认值: ''
	 */
	private String permanentRegion;
	
	/**
	 * 描述: 永久地址省份/州
	 * 字段: permanent_province  VARCHAR(36)
	 * 默认值: ''
	 */
	private String permanentProvince;
	
	/**
	 * 描述: 永久住址国家
	 * 字段: permanent_country  VARCHAR(36)
	 * 默认值: ''
	 */
	private String permanentCountry;
	
	/**
	 * 描述: 永久地址邮编
	 * 字段: permanent_post_code  VARCHAR(36)
	 * 默认值: ''
	 */
	private String permanentPostCode;
	
	/**
	 * 描述: 职业
	 * 字段: profession  VARCHAR(36)
	 * 默认值: ''
	 */
	private String profession;
	
	/**
	 * 描述: 护照号
	 * 字段: passport_no  VARCHAR(36)
	 * 默认值: ''
	 */
	private String passportNo;
	
	/**
	 * 描述: 收款银行名称
	 * 字段: bank_name  VARCHAR(50)
	 * 默认值: ''
	 */
	private String bankName;
	
	/**
	 * 描述: 收款银行地址门牌号
	 * 字段: bank_street_number  VARCHAR(50)
	 * 默认值: ''
	 */
	private String bankStreetNumber;
	
	/**
	 * 描述: 地区
	 * 字段: bank_region  VARCHAR(60)
	 * 默认值: ''
	 */
	private String bankRegion;

	/**
	 * 描述: 银行所在国家
	 * 字段: bank_country  VARCHAR(60)
	 * 默认值: ''
	 */
	private java.lang.String bankCountry;

	/**
	 * 描述: 银行所在省
	 * 字段: bank_province  VARCHAR(60)
	 * 默认值: ''
	 */
	private java.lang.String bankProvince;
	
	/**
	 * 描述: 银行邮编
	 * 字段: bank_post  VARCHAR(50)
	 * 默认值: ''
	 */
	private String bankPost;
	
	/**
	 * 描述: 收款银行SEIFT编码
	 * 字段: seift_code  VARCHAR(50)
	 * 默认值: ''
	 */
	private String seiftCode;
	
	/**
	 * 描述: 收款银行ABA编码
	 * 字段: aba_code  VARCHAR(50)
	 * 默认值: ''
	 */
	private String abaCode;
	
	/**
	 * 描述: 收款账号号码
	 * 字段: recipient_account_code  VARCHAR(50)
	 * 默认值: ''
	 */
	private String recipientAccountCode;
	
	/**
	 * 描述: 是否来自投资者本人账户0-否,1-是
	 * 字段: pay_flag  TINYINT(3)
	 * 默认值: 1
	 */
	private Integer payFlag;
	
	/**
	 * 描述: 签名
	 * 字段: signature  VARCHAR(50)
	 * 默认值: ''
	 */
	private String signature;
	
	/**
	 * 描述: 投资者声01 0-否,1-是
	 * 字段: invest_statement_flag  VARCHAR(50)
	 * 默认值: ''
	 */
	private String investStatementFlag;
	
	/**
	 * 描述: 投资声明02 0-否,1-是
	 * 字段: control_statement_flag  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer controlStatementFlag;
	
	/**
	 * 描述: 投资者知悉情况 0-否,1-是
	 * 字段: invest_aware_flag  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer investAwareFlag;
	
	/**
	 * 描述: 签订交换协议情况 0-否,1-是
	 * 字段: signed_swap_flag  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer signedSwapFlag;
	
	/**
	 * 描述: 代理提名人情况 0-否,1-是
	 * 字段: proxy_flag  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer proxyFlag;
	
	/**
	 * 描述: 是否是美国税民 0-否,1-是
	 * 字段: american_tax_flag  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer americanTaxFlag;
	
	/**
	 * 描述: 用户海外基金协议oss地址
	 * 字段: contract_url  VARCHAR(50)
	 * 默认值: ''
	 */
	private String contractUrl;
	
	/**
	 * 描述: 创建时间
	 * 字段: create_time  DATETIME(19)
	 * 默认值: CURRENT_TIMESTAMP(3)
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
	//【非数据库字段】海外及基金预约记录使用的协议类型
	private Integer fundContractType;
	//【非数据库字段】用户身份证号
	private String idCard;
	//【非数据库字段，查询时使用】
	private java.util.Date modifyTimeBegin;
	
	//【非数据库字段，查询时使用】
	private java.util.Date modifyTimeEnd;

	//【非数据库字段，查询时使用】产品起投金额
	private BigDecimal lowestAmount;

	//【非数据库字段，查询时使用】产品投资步长
	private BigDecimal stepValue;

	//【非数据库字段，查询时使用】实名姓名
	private String realName;

	//【非数据库字段，查询时使用】产品起投金额单位
	private Integer lowestAmountUnit;

	/**
	 * 描述: 中间行名称
	 * 字段: mid_bank_name  VARCHAR(60)
	 * 默认值: ''
	 */
	private java.lang.String midBankName;

	/**
	 * 描述: 中间行街道门牌号
	 * 字段: mid_bank_street_number  VARCHAR(60)
	 * 默认值: ''
	 */
	private java.lang.String midBankStreetNumber;

	/**
	 * 描述: 中间行所在地区
	 * 字段: mid_bank_region  VARCHAR(60)
	 * 默认值: ''
	 */
	private java.lang.String midBankRegion;

	/**
	 * 描述: 中间行所在省
	 * 字段: mid_bank_province  VARCHAR(60)
	 * 默认值: ''
	 */
	private java.lang.String midBankProvince;

	/**
	 * 描述: 中间行所在国家
	 * 字段: mid_bank_country  VARCHAR(60)
	 * 默认值: ''
	 */
	private java.lang.String midBankCountry;

	/**
	 * 描述: 中间行所在地邮编
	 * 字段: mid_bank_post_code  VARCHAR(60)
	 * 默认值: ''
	 */
	private java.lang.String midBankPostCode;

	/**
	 * 描述: 中间行SWIFT编码
	 * 字段: mid_bank_swift_code  VARCHAR(60)
	 * 默认值: ''
	 */
	private java.lang.String midBankSwiftCode;

	/**
	 * 描述: 中间行ABA编码
	 * 字段: mid_bank_aba_code  VARCHAR(60)
	 * 默认值: ''
	 */
	private java.lang.String midBankAbaCode;

	public FundAgreement(){
	}

	public FundAgreement(Integer id){
		this.id = id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public void setFundAdvisoryId(Integer fundAdvisoryId) {
		this.fundAdvisoryId = fundAdvisoryId;
	}
	
	public Integer getFundAdvisoryId() {
		return this.fundAdvisoryId;
	}
	
	public void setFundInfoId(Integer fundInfoId) {
		this.fundInfoId = fundInfoId;
	}
	
	public Integer getFundInfoId() {
		return this.fundInfoId;
	}
	
	public void setState(Integer state) {
		this.state = state;
	}
	
	public Integer getState() {
		return this.state;
	}
	
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	public String getReason() {
		return this.reason;
	}
	
	public void setInvestAmount(java.math.BigDecimal investAmount) {
		this.investAmount = investAmount;
	}
	
	public java.math.BigDecimal getInvestAmount() {
		return this.investAmount;
	}
	
	public void setRegUserId(Integer regUserId) {
		this.regUserId = regUserId;
	}
	
	public Integer getRegUserId() {
		return this.regUserId;
	}
	
	public void setUserSurname(String userSurname) {
		this.userSurname = userSurname;
	}
	
	public String getUserSurname() {
		return this.userSurname;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getUserName() {
		return this.userName;
	}
	
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	
	public String getBirthday() {
		return this.birthday;
	}
	
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	
	public String getNationality() {
		return this.nationality;
	}
	
	public void setBirthPlace(String birthPlace) {
		this.birthPlace = birthPlace;
	}
	
	public String getBirthPlace() {
		return this.birthPlace;
	}
	
	public void setStreetNumber(String streetNumber) {
		this.streetNumber = streetNumber;
	}
	
	public String getStreetNumber() {
		return this.streetNumber;
	}
	
	public void setRegion(String region) {
		this.region = region;
	}
	
	public String getRegion() {
		return this.region;
	}
	
	public void setProvince(String province) {
		this.province = province;
	}
	
	public String getProvince() {
		return this.province;
	}
	
	public void setCountry(String country) {
		this.country = country;
	}
	
	public String getCountry() {
		return this.country;
	}
	
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	
	public String getPostCode() {
		return this.postCode;
	}
	
	public void setTel(String tel) {
		this.tel = tel;
	}
	
	public String getTel() {
		return this.tel;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getEmail() {
		return this.email;
	}
	
	public void setPermanentStreetNumber(String permanentStreetNumber) {
		this.permanentStreetNumber = permanentStreetNumber;
	}
	
	public String getPermanentStreetNumber() {
		return this.permanentStreetNumber;
	}
	
	public void setPermanentRegion(String permanentRegion) {
		this.permanentRegion = permanentRegion;
	}
	
	public String getPermanentRegion() {
		return this.permanentRegion;
	}
	
	public void setPermanentProvince(String permanentProvince) {
		this.permanentProvince = permanentProvince;
	}
	
	public String getPermanentProvince() {
		return this.permanentProvince;
	}
	
	public void setPermanentCountry(String permanentCountry) {
		this.permanentCountry = permanentCountry;
	}
	
	public String getPermanentCountry() {
		return this.permanentCountry;
	}
	
	public void setPermanentPostCode(String permanentPostCode) {
		this.permanentPostCode = permanentPostCode;
	}
	
	public String getPermanentPostCode() {
		return this.permanentPostCode;
	}
	
	public void setProfession(String profession) {
		this.profession = profession;
	}
	
	public String getProfession() {
		return this.profession;
	}
	
	public void setPassportNo(String passportNo) {
		this.passportNo = passportNo;
	}
	
	public String getPassportNo() {
		return this.passportNo;
	}
	
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	
	public String getBankName() {
		return this.bankName;
	}
	
	public void setBankStreetNumber(String bankStreetNumber) {
		this.bankStreetNumber = bankStreetNumber;
	}
	
	public String getBankStreetNumber() {
		return this.bankStreetNumber;
	}
	
	public void setBankRegion(String bankRegion) {
		this.bankRegion = bankRegion;
	}
	
	public String getBankRegion() {
		return this.bankRegion;
	}
	
	public void setBankPost(String bankPost) {
		this.bankPost = bankPost;
	}
	
	public String getBankPost() {
		return this.bankPost;
	}
	
	public void setSeiftCode(String seiftCode) {
		this.seiftCode = seiftCode;
	}
	
	public String getSeiftCode() {
		return this.seiftCode;
	}
	
	public void setAbaCode(String abaCode) {
		this.abaCode = abaCode;
	}
	
	public String getAbaCode() {
		return this.abaCode;
	}
	
	public void setRecipientAccountCode(String recipientAccountCode) {
		this.recipientAccountCode = recipientAccountCode;
	}
	
	public String getRecipientAccountCode() {
		return this.recipientAccountCode;
	}
	
	public void setPayFlag(Integer payFlag) {
		this.payFlag = payFlag;
	}
	
	public Integer getPayFlag() {
		return this.payFlag;
	}
	
	public void setSignature(String signature) {
		this.signature = signature;
	}
	
	public String getSignature() {
		return this.signature;
	}
	
	public void setInvestStatementFlag(String investStatementFlag) {
		this.investStatementFlag = investStatementFlag;
	}
	
	public String getInvestStatementFlag() {
		return this.investStatementFlag;
	}
	
	public void setControlStatementFlag(Integer controlStatementFlag) {
		this.controlStatementFlag = controlStatementFlag;
	}
	
	public Integer getControlStatementFlag() {
		return this.controlStatementFlag;
	}
	
	public void setInvestAwareFlag(Integer investAwareFlag) {
		this.investAwareFlag = investAwareFlag;
	}
	
	public Integer getInvestAwareFlag() {
		return this.investAwareFlag;
	}
	
	public void setSignedSwapFlag(Integer signedSwapFlag) {
		this.signedSwapFlag = signedSwapFlag;
	}
	
	public Integer getSignedSwapFlag() {
		return this.signedSwapFlag;
	}
	
	public void setProxyFlag(Integer proxyFlag) {
		this.proxyFlag = proxyFlag;
	}
	
	public Integer getProxyFlag() {
		return this.proxyFlag;
	}
	
	public void setAmericanTaxFlag(Integer americanTaxFlag) {
		this.americanTaxFlag = americanTaxFlag;
	}
	
	public Integer getAmericanTaxFlag() {
		return this.americanTaxFlag;
	}
	
	public void setContractUrl(String contractUrl) {
		this.contractUrl = contractUrl;
	}
	
	public String getContractUrl() {
		return this.contractUrl;
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

	public Integer getFundContractType() {
		return fundContractType;
	}

	public void setFundContractType(Integer fundContractType) {
		this.fundContractType = fundContractType;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getBankCountry() {
		return bankCountry;
	}

	public void setBankCountry(String bankCountry) {
		this.bankCountry = bankCountry;
	}

	public String getBankProvince() {
		return bankProvince;
	}

	public void setBankProvince(String bankProvince) {
		this.bankProvince = bankProvince;
	}

	public String getMidBankName() {
		return midBankName;
	}

	public void setMidBankName(String midBankName) {
		this.midBankName = midBankName;
	}

	public String getMidBankStreetNumber() {
		return midBankStreetNumber;
	}

	public void setMidBankStreetNumber(String midBankStreetNumber) {
		this.midBankStreetNumber = midBankStreetNumber;
	}

	public String getMidBankRegion() {
		return midBankRegion;
	}

	public void setMidBankRegion(String midBankRegion) {
		this.midBankRegion = midBankRegion;
	}

	public String getMidBankProvince() {
		return midBankProvince;
	}

	public void setMidBankProvince(String midBankProvince) {
		this.midBankProvince = midBankProvince;
	}

	public String getMidBankCountry() {
		return midBankCountry;
	}

	public void setMidBankCountry(String midBankCountry) {
		this.midBankCountry = midBankCountry;
	}

	public String getMidBankPostCode() {
		return midBankPostCode;
	}

	public void setMidBankPostCode(String midBankPostCode) {
		this.midBankPostCode = midBankPostCode;
	}

	public String getMidBankSwiftCode() {
		return midBankSwiftCode;
	}

	public void setMidBankSwiftCode(String midBankSwiftCode) {
		this.midBankSwiftCode = midBankSwiftCode;
	}

	public String getMidBankAbaCode() {
		return midBankAbaCode;
	}

	public void setMidBankAbaCode(String midBankAbaCode) {
		this.midBankAbaCode = midBankAbaCode;
	}

	public BigDecimal getStepValue() {
		return stepValue;
	}

	public void setStepValue(BigDecimal stepValue) {
		this.stepValue = stepValue;
	}

	public BigDecimal getLowestAmount() {
		return lowestAmount;
	}

	public void setLowestAmount(BigDecimal lowestAmount) {
		this.lowestAmount = lowestAmount;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public Integer getLowestAmountUnit() {
		return lowestAmountUnit;
	}

	public void setLowestAmountUnit(Integer lowestAmountUnit) {
		this.lowestAmountUnit = lowestAmountUnit;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}

