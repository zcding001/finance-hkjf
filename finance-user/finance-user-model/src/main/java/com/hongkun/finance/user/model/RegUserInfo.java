package com.hongkun.finance.user.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.format.annotation.DateTimeFormat;

import com.yirun.framework.core.model.BaseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.user.model.RegUserInfo.java
 * @Class Name    : RegUserInfo.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class RegUserInfo extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: 主键id
	 * 字段: id  INT UNSIGNED(10)
	 */
	private java.lang.Integer id;
	
	/**
	 * 描述: 用户id
	 * 字段: reg_user_id  INT UNSIGNED(10)
	 */
	private java.lang.Integer regUserId;
	
	/**
	 * 描述: 用户编号
	 * 字段: code  VARCHAR(36)
	 * 默认值: ''
	 */
	private java.lang.String code;
	
	/**
	 * 描述: 性别:1-男,2-女
	 * 字段: sex  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer sex;
	
	/**
	 * 描述: 年龄
	 * 字段: age  TINYINT UNSIGNED(3)
	 * 默认值: 0
	 */
	private Integer age;
	
	/**
	 * 描述: 出生日期
	 * 字段: birthday  DATE(10)
	 * 默认值: 0000-00-00
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date birthday;
	
	//【非数据库字段，查询时使用】
	private java.util.Date birthdayBegin;
	
	//【非数据库字段，查询时使用】
	private java.util.Date birthdayEnd;
	/**
	 * 描述: 婚姻状况:0-未婚,1-已婚
	 * 字段: marry_state  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer marryState;
	
	/**
	 * 描述: 学历:0-小学,1-初中,2-高中,3-中专,4-大专,5-本科,7-硕士,8-博士,9-博士后
	 * 字段: degree  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer degree;
	
	/**
	 * 描述: 工作所在省
	 * 字段: work_province  VARCHAR(10)
	 * 默认值: ''
	 */
	private java.lang.String workProvince;
	
	/**
	 * 描述: 工作所在市
	 * 字段: work_city  VARCHAR(20)
	 * 默认值: ''
	 */
	private java.lang.String workCity;
	
	/**
	 * 描述: 工作所在县
	 * 字段: work_country  VARCHAR(20)
	 * 默认值: ''
	 */
	private java.lang.String workCountry;
	
	/**
	 * 描述: 户籍所在省
	 * 字段: birth_province  VARCHAR(10)
	 * 默认值: ''
	 */
	private java.lang.String birthProvince;
	
	/**
	 * 描述: 户籍所在市
	 * 字段: birth_city  VARCHAR(20)
	 * 默认值: ''
	 */
	private java.lang.String birthCity;
	
	/**
	 * 描述: 户籍所在县
	 * 字段: birth_country  VARCHAR(20)
	 * 默认值: ''
	 */
	private java.lang.String birthCountry;
	
	/**
	 * 描述: 联系地址
	 * 字段: contact_address  VARCHAR(100)
	 * 默认值: ''
	 */
	private java.lang.String contactAddress;
	
	/**
	 * 描述: 邮编
	 * 字段: post_code  VARCHAR(10)
	 * 默认值: ''
	 */
	private java.lang.String postCode;
	
	/**
	 * 描述: 工作职位
	 * 字段: job  VARCHAR(20)
	 * 默认值: ''
	 */
	private java.lang.String job;
	
	/**
	 * 描述: 工作年收入(万)
	 * 字段: work_income  SMALLINT(5)
	 * 默认值: 0
	 */
	private Integer workIncome;
	
	/**
	 * 描述: 所属行业
	 * 字段: work_industry  VARCHAR(20)
	 * 默认值: ''
	 */
	private java.lang.String workIndustry;
	
	/**
	 * 描述: 资产状况(万)
	 * 字段: assetcondition  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer assetcondition;
	
	/**
	 * 描述: 邮箱
	 * 字段: email  VARCHAR(50)
	 * 默认值: ''
	 */
	private java.lang.String email;
	
	/**
	 * 描述: 邮箱状态:0-未绑定,1-已绑定
	 * 字段: email_state  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer emailState;
	
	/**
	 * 描述: 公司名称
	 * 字段: company  VARCHAR(50)
	 * 默认值: ''
	 */
	private java.lang.String company;
	
	/**
	 * 描述: 公司电话
	 * 字段: company_tel  VARCHAR(20)
	 * 默认值: ''
	 */
	private java.lang.String companyTel;
	
	/**
	 * 描述: 公司地址
	 * 字段: company_address  VARCHAR(100)
	 * 默认值: ''
	 */
	private java.lang.String companyAddress;
	
	/**
	 * 描述: 图片地址
	 * 字段: photo_url  VARCHAR(100)
	 * 默认值: ''
	 */
	private java.lang.String photoUrl;
	
	/**
	 * 描述: 紧急联系人姓名
	 * 字段: emergency_contact  VARCHAR(20)
	 * 默认值: ''
	 */
	private java.lang.String emergencyContact;
	
	/**
	 * 描述: 紧急联系人电话
	 * 字段: emergency_tel  VARCHAR(20)
	 * 默认值: ''
	 */
	private java.lang.String emergencyTel;
	
	/**
	 * 描述: 状态:0-删除,1-正常
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
 
	public RegUserInfo(){
	}

	public RegUserInfo(java.lang.Integer id){
		this.id = id;
	}

	public void setId(java.lang.Integer id) {
		this.id = id;
	}
	
	public java.lang.Integer getId() {
		return this.id;
	}
	
	public void setRegUserId(java.lang.Integer regUserId) {
		this.regUserId = regUserId;
	}
	
	public java.lang.Integer getRegUserId() {
		return this.regUserId;
	}
	
	public void setCode(java.lang.String code) {
		this.code = code;
	}
	
	public java.lang.String getCode() {
		return this.code;
	}
	
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	
	public Integer getSex() {
		return this.sex;
	}
	
	public void setAge(Integer age) {
		this.age = age;
	}
	
	public Integer getAge() {
		return this.age;
	}
	
	public void setBirthday(java.util.Date birthday) {
		this.birthday = birthday;
	}
	
	public java.util.Date getBirthday() {
		return this.birthday;
	}
	
	public void setBirthdayBegin(java.util.Date birthdayBegin) {
		this.birthdayBegin = birthdayBegin;
	}
	
	public java.util.Date getBirthdayBegin() {
		return this.birthdayBegin;
	}
	
	public void setBirthdayEnd(java.util.Date birthdayEnd) {
		this.birthdayEnd = birthdayEnd;
	}
	
	public java.util.Date getBirthdayEnd() {
		return this.birthdayEnd;
	}	
	public void setMarryState(Integer marryState) {
		this.marryState = marryState;
	}
	
	public Integer getMarryState() {
		return this.marryState;
	}
	
	public void setDegree(Integer degree) {
		this.degree = degree;
	}
	
	public Integer getDegree() {
		return this.degree;
	}
	
	public void setWorkProvince(java.lang.String workProvince) {
		this.workProvince = workProvince;
	}
	
	public java.lang.String getWorkProvince() {
		return this.workProvince;
	}
	
	public void setWorkCity(java.lang.String workCity) {
		this.workCity = workCity;
	}
	
	public java.lang.String getWorkCity() {
		return this.workCity;
	}
	
	public void setWorkCountry(java.lang.String workCountry) {
		this.workCountry = workCountry;
	}
	
	public java.lang.String getWorkCountry() {
		return this.workCountry;
	}
	
	public void setBirthProvince(java.lang.String birthProvince) {
		this.birthProvince = birthProvince;
	}
	
	public java.lang.String getBirthProvince() {
		return this.birthProvince;
	}
	
	public void setBirthCity(java.lang.String birthCity) {
		this.birthCity = birthCity;
	}
	
	public java.lang.String getBirthCity() {
		return this.birthCity;
	}
	
	public void setBirthCountry(java.lang.String birthCountry) {
		this.birthCountry = birthCountry;
	}
	
	public java.lang.String getBirthCountry() {
		return this.birthCountry;
	}
	
	public void setContactAddress(java.lang.String contactAddress) {
		this.contactAddress = contactAddress;
	}
	
	public java.lang.String getContactAddress() {
		return this.contactAddress;
	}
	
	public void setPostCode(java.lang.String postCode) {
		this.postCode = postCode;
	}
	
	public java.lang.String getPostCode() {
		return this.postCode;
	}
	
	public void setJob(java.lang.String job) {
		this.job = job;
	}
	
	public java.lang.String getJob() {
		return this.job;
	}
	
	public void setWorkIncome(Integer workIncome) {
		this.workIncome = workIncome;
	}
	
	public Integer getWorkIncome() {
		return this.workIncome;
	}
	
	public void setWorkIndustry(java.lang.String workIndustry) {
		this.workIndustry = workIndustry;
	}
	
	public java.lang.String getWorkIndustry() {
		return this.workIndustry;
	}
	
	public void setAssetcondition(java.lang.Integer assetcondition) {
		this.assetcondition = assetcondition;
	}
	
	public java.lang.Integer getAssetcondition() {
		return this.assetcondition;
	}
	
	public void setEmail(java.lang.String email) {
		this.email = email;
	}
	
	public java.lang.String getEmail() {
		return this.email;
	}
	
	public void setEmailState(Integer emailState) {
		this.emailState = emailState;
	}
	
	public Integer getEmailState() {
		return this.emailState;
	}
	
	public void setCompany(java.lang.String company) {
		this.company = company;
	}
	
	public java.lang.String getCompany() {
		return this.company;
	}
	
	public void setCompanyTel(java.lang.String companyTel) {
		this.companyTel = companyTel;
	}
	
	public java.lang.String getCompanyTel() {
		return this.companyTel;
	}
	
	public void setCompanyAddress(java.lang.String companyAddress) {
		this.companyAddress = companyAddress;
	}
	
	public java.lang.String getCompanyAddress() {
		return this.companyAddress;
	}
	
	public void setPhotoUrl(java.lang.String photoUrl) {
		this.photoUrl = photoUrl;
	}
	
	public java.lang.String getPhotoUrl() {
		return this.photoUrl;
	}
	
	public void setEmergencyContact(java.lang.String emergencyContact) {
		this.emergencyContact = emergencyContact;
	}
	
	public java.lang.String getEmergencyContact() {
		return this.emergencyContact;
	}
	
	public void setEmergencyTel(java.lang.String emergencyTel) {
		this.emergencyTel = emergencyTel;
	}
	
	public java.lang.String getEmergencyTel() {
		return this.emergencyTel;
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

    private List<Integer> userIds = new ArrayList<>();

    public List<Integer> getUserIds() {
        return userIds;
    }
    public void setUserIds(List<Integer> userIds) {
        this.userIds = userIds;
    }
}

