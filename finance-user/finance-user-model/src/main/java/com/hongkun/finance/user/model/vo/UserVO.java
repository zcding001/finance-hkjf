package com.hongkun.finance.user.model.vo;

import com.yirun.framework.core.annotation.Union;
import com.yirun.framework.core.model.BaseModel;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description : 用户表和用户详情表的部分字段的VO
 * @Project : finance
 * @Program Name : com.hongkun.finance.user.model.vo.UserVO
 * @Author : zhongpingtang@yiruntz.com 唐忠平
 */
public class UserVO extends BaseModel {

	private static final long serialVersionUID = 1L;
	@Union(bind = { "RegUser" }, reNameTo = "id")
	private Integer userId;
	private List<Integer> userIds = new ArrayList<>();
	private java.lang.String nickName;
	@Union(changeAble = false)
	private java.lang.String realName;
	private java.lang.String idCard;
	private String enterpriseName;
	private String headPortrait;
	private String email;
	
    //企业id
    private java.lang.Integer enterpriseRegUserId;
	
	/**
	 * 描述: 实名状态:0-未实名,1-已实名
	 * 字段: identify  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer identify;
	/** 手机号 */
	private java.lang.Long login;
	private java.lang.String inviteNo;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date createTimeStart;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date createTimeEnd;
	// 联系地址
	private String contactAddress;
	private String bankName;
	private String bankCard;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Union(changeAbleIfHasValue = false)
	private Date createTime;
	/** 邀请码 **/
	private String commendNo;
	/** 用户类型 **/
	private Integer type;
	/**用户类型集合*/
    private List<Integer> types = new ArrayList<>();
	/** 推广来源 **/
	private String extenSource;
	/** 机构码 **/
	private String groupCode;
	/** 注册来源 **/
	private Integer registSource;
	/** 状态 **/
	private Integer state;
	/**是否已投资**/
	private Integer investFlag;

	/**
	 * 用户身份证图片集合
	 */
	private List<String> identifyPics;

	/*********** 其他字段 *******************/

	/************ 用于账户资金管理页面start ****************/
	// from LoadVO
	/** 待收本金->总本金 **/
	@Union(mergeKey = "LoanVO")
	private BigDecimal capitalAmount;
	/** 代收利息->总利息 **/
	@Union(mergeKey = "LoanVO")
	private BigDecimal interestAmount;
	/** 待加息收益->总加息收益 **/
	@Union(mergeKey = "LoanVO")
	private BigDecimal increaseAmount;

	// from FinAccount
	/** 描述: 冻结金额(元) 字段: freeze_money DECIMAL(20) 默认值: 0.00 */
	@Union(bind = { "FinAccount" })
	private BigDecimal freezeMoney;

	/*** 描述: 可用余额(元) 字段: useable_money DECIMAL(20) 默认值: 0.00 */
	@Union(bind = { "FinAccount" })
	private BigDecimal useableMoney;

	// 计算得出字段：总资产
	@Union(bind = { "FinAccount" }, reNameTo = "nowMoney")
	private BigDecimal userTotalMoeny;

	/************ 用于账户资金管理页面end *************************/

	/************** 用于查询字段 **************************/
	/** 可用余额最低 */
	private BigDecimal useableMoneyStart;

	/** 可用余额最高 */
	private BigDecimal useableMoneyEnd;

	/**
	 * 不包含的用户id
	 */
	private List<Integer> notIncludeIds;
	
	/**客户群 1：投资用户，2：实名未投资，3：注册未实名，4：男性用户，5：女性用户，6：不活跃用户，7：导入型用户**/
	private Integer userGroup;
	
	/** 账户资产*/
	private BigDecimal nowMoney;
	/** VIP降级次数**/
	private Integer times;	


	public String getHeadPortrait() {
		return headPortrait;
	}

	public void setHeadPortrait(String headPortrait) {
		this.headPortrait = headPortrait;
	}

	public List<String> getIdentifyPics() {
		return identifyPics;
	}

	public void setIdentifyPics(List<String> identifyPics) {
		this.identifyPics = identifyPics;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public Long getLogin() {
		return login;
	}

	public void setLogin(Long login) {
		this.login = login;
	}

	public java.lang.String getInviteNo() {
		return inviteNo;
	}

	public void setInviteNo(String inviteNo) {
		this.inviteNo = inviteNo;
	}

	public Date getCreateTimeStart() {
		return createTimeStart;
	}

	public void setCreateTimeStart(Date createTimeStart) {
		this.createTimeStart = createTimeStart;
	}

	public Date getCreateTimeEnd() {
		return createTimeEnd;
	}

	public void setCreateTimeEnd(Date createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
	}

	public String getContactAddress() {
		return contactAddress;
	}

	public void setContactAddress(String contactAddress) {
		this.contactAddress = contactAddress;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankCard() {
		return bankCard;
	}

	public void setBankCard(String bankCard) {
		this.bankCard = bankCard;
	}

	public String getCommendNo() {
		return commendNo;
	}

	public void setCommendNo(String commendNo) {
		this.commendNo = commendNo;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getExtenSource() {
		return extenSource;
	}

	public void setExtenSource(String extenSource) {
		this.extenSource = extenSource;
	}

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public Integer getRegistSource() {
		return registSource;
	}

	public void setRegistSource(Integer registSource) {
		this.registSource = registSource;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public BigDecimal getCapitalAmount() {
		return capitalAmount;
	}

	public void setCapitalAmount(BigDecimal capitalAmount) {
		this.capitalAmount = capitalAmount;
	}

	public BigDecimal getInterestAmount() {
		return interestAmount;
	}

	public void setInterestAmount(BigDecimal interestAmount) {
		this.interestAmount = interestAmount;
	}

	public BigDecimal getIncreaseAmount() {
		return increaseAmount;
	}

	public void setIncreaseAmount(BigDecimal increaseAmount) {
		this.increaseAmount = increaseAmount;
	}

	public BigDecimal getFreezeMoney() {
		return freezeMoney;
	}

	public void setFreezeMoney(BigDecimal freezeMoney) {
		this.freezeMoney = freezeMoney;
	}

	public BigDecimal getUseableMoney() {
		return useableMoney;
	}

	public void setUseableMoney(BigDecimal useableMoney) {
		this.useableMoney = useableMoney;
	}

	public BigDecimal getUserTotalMoeny() {
		return userTotalMoeny;
	}

	public void setUserTotalMoeny(BigDecimal userTotalMoeny) {
		this.userTotalMoeny = userTotalMoeny;
	}

	public BigDecimal getUseableMoneyStart() {
		return useableMoneyStart;
	}

	public void setUseableMoneyStart(BigDecimal useableMoneyStart) {
		this.useableMoneyStart = useableMoneyStart;
	}

	public BigDecimal getUseableMoneyEnd() {
		return useableMoneyEnd;
	}

	public void setUseableMoneyEnd(BigDecimal useableMoneyEnd) {
		this.useableMoneyEnd = useableMoneyEnd;
	}

	public List<Integer> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<Integer> userIds) {
		this.userIds = userIds;
	}

	public List<Integer> getTypes() {
		return types;
	}

	public void setTypes(List<Integer> types) {
		this.types = types;
	}

	public Integer getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(Integer userGroup) {
		this.userGroup = userGroup;
	}

	public Integer getInvestFlag() {
		return investFlag;
	}
	
	public void setInvestFlag(Integer investFlag) {
		this.investFlag = investFlag;
	}

	public String getEnterpriseName() {
		return enterpriseName;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}

	public Integer getIdentify() {
		return identify;
	}

	public void setIdentify(Integer identify) {
		this.identify = identify;
	}

	public List<Integer> getNotIncludeIds() {
		return notIncludeIds;
	}

	public void setNotIncludeIds(List<Integer> notIncludeIds) {
		this.notIncludeIds = notIncludeIds;
	}
    public BigDecimal getNowMoney() {
        return nowMoney;
    }

    public void setNowMoney(BigDecimal nowMoney) {
        this.nowMoney = nowMoney;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public java.lang.Integer getEnterpriseRegUserId() {
        return enterpriseRegUserId;
    }

    public void setEnterpriseRegUserId(java.lang.Integer enterpriseRegUserId) {
        this.enterpriseRegUserId = enterpriseRegUserId;
    }

    @Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}

}
