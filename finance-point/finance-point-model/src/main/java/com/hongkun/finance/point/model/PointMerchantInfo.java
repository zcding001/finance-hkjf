package com.hongkun.finance.point.model;

import com.yirun.framework.core.annotation.Union;
import com.yirun.framework.core.model.BaseModel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.point.model.PointMerchantInfo.java
 * @Class Name    : PointMerchantInfo.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class PointMerchantInfo extends BaseModel {
	
	private static final long serialVersionUID = 1L;

	public static final PointMerchantInfo instance = new PointMerchantInfo();
 
	/**
	 * 描述: id
	 * 字段: id  INT(10)
	 */
	@NotNull(groups = {ValidateCheck.class,Delete.class},message = "请指定审核商户")
	private Integer id;
	
	/**
	 * 描述: 商户编号(由系统自动生成)
	 * 字段: merchant_code  VARCHAR(8)
	 */
	private String merchantCode;
	
	/**
	 * 描述: 商户名称
	 * 字段: merchant_name  VARCHAR(200)
	 */
	@NotNull(groups = {Save.class},message = "请输入商户名称")
	private String merchantName;
	
	/**
	 * 描述: 用户ID
	 * 字段: reg_user_id  INT(10)
	 */
	@NotNull(groups = {Save.class,PointMerchantCount.class},message = "请指定联系人")
	private Integer regUserId;
	
	/**
	 * 描述: 商户地址
	 * 字段: address  VARCHAR(50)
	 * 默认值: ''
	 */
	private String address;

	/**
	 * 描述: 营业执照(business license)
	 * 字段: business_license_url  VARCHAR(300)
	 */
	@NotNull(groups = {Save.class},message = "请指定营业执照")
	private String businessLicenseUrl;
	
	/**
	 * 描述: 开户许可证
	 * 字段: permit_url  VARCHAR(300)
	 */
	@NotNull(groups = {Save.class},message = "请指定开户许可证")
	private String permitUrl;
	
	/**
	 * 描述: 法人身份证正面
	 * 字段: idcard_front_url  VARCHAR(300)
	 */
	@NotNull(groups = {Save.class},message = "请指定法人身份证正面")
	private String idcardFrontUrl;
	
	/**
	 * 描述: 法人身份证反面
	 * 字段: idcard_back_url  VARCHAR(300)
	 */
	@NotNull(groups = {Save.class},message = "请指定法人身份证反面")
	private String idcardBackUrl;
	
	/**
	 * 描述: 是否是餐饮、食品、化妆品等行业
	 * 字段: business_type  TINYINT(3)
	 * 默认值: 0
	 */
	@NotNull(groups = {Save.class},message = "请指定行业")
	private Integer businessType;
	
	/**
	 * 描述: 卫生许可证(hygiene licenses)
	 * 字段: hygiene_license_url  VARCHAR(300)
	 */

	private String hygieneLicenseUrl;
	
	/**
	 * 描述: 收款码
	 * 字段: gathering_url  VARCHAR(300)
	 */
	private String gatheringUrl;
	
	/**
	 * 描述: 审核意见
	 * 字段: reason  VARCHAR(300)
	 */
	@NotNull(groups = ValidateCheck.class,message = "请指定审核意见")
	private String reason;
	
	/**
	 * 描述: 备注
	 * 字段: note  VARCHAR(100)
	 * 默认值: ''
	 */
	private String note;
	
	/**
	 * 描述: 状态：0未申请，1待审核，2审核失败，3审核成功
	 * 字段: state  TINYINT(3)
	 */
	@NotNull(groups = ValidateCheck.class,message = "请指定状态")
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
	 * 描述: 更新时间
	 * 字段: modify_time  DATETIME(19)
	 * 默认值: CURRENT_TIMESTAMP
	 */
	private java.util.Date modifyTime;
	
	//【非数据库字段，查询时使用】
	private java.util.Date modifyTimeBegin;
	
	//【非数据库字段，查询时使用】
	private java.util.Date modifyTimeEnd;

	/********************VO字段，因为较少，所以不单独建立VO************************/
	/**
	 * 用户姓名
	 */
	@Union(forLimitQuery = true)
	private String userName;
	/**
	 * 用户手机号
	 */
	@Union(forLimitQuery = true)
	private Long tel;

	/**
	 * 限制userIds
	 */
	private List<Integer> limitUserIds = new ArrayList<>();

	/********************验证组************************/
	public interface ValidateCheck{}
	public interface Delete{}
	public interface Save{}
	public interface PointMerchantCount{}

 
	public PointMerchantInfo(){
	}

	public PointMerchantInfo(Integer id){
		this.id = id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}
	
	public String getMerchantCode() {
		return this.merchantCode;
	}
	
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	
	public String getMerchantName() {
		return this.merchantName;
	}
	
	public void setRegUserId(Integer regUserId) {
		this.regUserId = regUserId;
	}
	
	public Integer getRegUserId() {
		return this.regUserId;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getAddress() {
		return this.address;
	}

	public void setBusinessLicenseUrl(String businessLicenseUrl) {
		this.businessLicenseUrl = businessLicenseUrl;
	}
	
	public String getBusinessLicenseUrl() {
		return this.businessLicenseUrl;
	}
	
	public void setPermitUrl(String permitUrl) {
		this.permitUrl = permitUrl;
	}
	
	public String getPermitUrl() {
		return this.permitUrl;
	}
	
	public void setIdcardFrontUrl(String idcardFrontUrl) {
		this.idcardFrontUrl = idcardFrontUrl;
	}
	
	public String getIdcardFrontUrl() {
		return this.idcardFrontUrl;
	}
	
	public void setIdcardBackUrl(String idcardBackUrl) {
		this.idcardBackUrl = idcardBackUrl;
	}
	
	public String getIdcardBackUrl() {
		return this.idcardBackUrl;
	}
	
	public void setBusinessType(Integer businessType) {
		this.businessType = businessType;
	}
	
	public Integer getBusinessType() {
		return this.businessType;
	}
	
	public void setHygieneLicenseUrl(String hygieneLicenseUrl) {
		this.hygieneLicenseUrl = hygieneLicenseUrl;
	}
	
	public String getHygieneLicenseUrl() {
		return this.hygieneLicenseUrl;
	}
	
	public void setGatheringUrl(String gatheringUrl) {
		this.gatheringUrl = gatheringUrl;
	}
	
	public String getGatheringUrl() {
		return this.gatheringUrl;
	}
	
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	public String getReason() {
		return this.reason;
	}
	
	public void setNote(String note) {
		this.note = note;
	}
	
	public String getNote() {
		return this.note;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getTel() {
		return tel;
	}

	public void setTel(Long tel) {
		this.tel = tel;
	}

	public List<Integer> getLimitUserIds() {
		return limitUserIds;
	}

	public void setLimitUserIds(List<Integer> limitUserIds) {
		this.limitUserIds = limitUserIds;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}

