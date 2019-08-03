package com.hongkun.finance.payment.model;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.payment.model.FinFundtransfer.java
 * @Class Name : FinFundtransfer.java
 * @Description : GENERATOR VO类
 * @Author : generator
 */
public class FinFundtransfer extends BaseModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 描述: id 字段: id INT UNSIGNED(10)
	 */
	private java.lang.Integer id;

	/**
	 * 描述: 交易流水:FT+YYYYMMDDH24mmssSSS+五位流水 字段: flow_id VARCHAR(40) 默认值: ''
	 */
	private java.lang.String flowId;

	/**
	 * 描述: 资金交易流水 字段: trade_flow_id VARCHAR(40) 默认值: ''
	 */
	private java.lang.String tradeFlowId;

	/**
	 * 描述: 用户id 字段: reg_user_id INT UNSIGNED(10) 默认值: 0
	 */
	private java.lang.Integer regUserId;

	/**
	 * 描述: 收款方发id 字段: rec_reg_user_id INT UNSIGNED(10) 默认值: 0
	 */
	private java.lang.Integer recRegUserId;

	/**
	 * 描述: 交易金额 字段: trans_money DECIMAL(20) 默认值: 0.00
	 */
	private java.math.BigDecimal transMoney;

	/**
	 * 描述: 资金科目
	 * 收入-1000,支出-2000,冻结-3000,解冻-4000,5000-转入,6000-转出,7000-债转收入,8000-债转支出 字段:
	 * sub_code SMALLINT(5) 默认值: 0
	 */
	private Integer subCode;

	/**
	 * 描述: 交易类型:(业务模块+业务操作)
	 * 充值-1001,投资-1101,放款-1201,还款-1301,提现-1401,红包-1501,推荐奖-1601,钱袋子转入-1701,钱袋子转出
	 * -1702,积分支付-1801,手动债权转让-1901,自动债权转让-1902,物业缴费-2001 字段: trade_type
	 * SMALLINT(5) 默认值: 0
	 */
	private Integer tradeType;

	/**
	 * 描述: 操作前余额 字段: pre_monry DECIMAL(20) 默认值: 0.00
	 */
	private java.math.BigDecimal preMoney;

	/**
	 * 描述: 可用余额操作后余额 字段: after_money DECIMAL(20) 默认值: 0.00
	 */
	private java.math.BigDecimal afterMoney;

	/**
	 * 描述: 操作后余额 字段: now_money DECIMAL(20) 默认值: 0.00
	 */
	private java.math.BigDecimal nowMoney;

	/**
	 * 描述: 创建时间 字段: create_time DATETIME(19) 默认值: CURRENT_TIMESTAMP
	 */
	private java.util.Date createTime;

	// 【非数据库字段，查询时使用】
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date createTimeBegin;

	// 【非数据库字段，查询时使用】
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date createTimeEnd;
	/**
	 * 描述: 修改时间 字段: modify_time DATETIME(19) 默认值: CURRENT_TIMESTAMP
	 */
	private java.util.Date modifyTime;

	// 【非数据库字段，查询时使用】
	private java.util.Date modifyTimeBegin;

	// 【非数据库字段，查询时使用】
	private java.util.Date modifyTimeEnd;
	// 【非数据库字段，查询时使用,交易最新金额】
	private java.math.BigDecimal minTransMoney;
	// 【非数据库字段，查询时使用，交易最大金额】
	private java.math.BigDecimal maxTransMoney;
	// 用户前端页面是否展示 0 展示 1 不展示
	private Integer showFlag;
	/**
	 * subCode集合
	 */
	private List<Integer> subCodeList;

	private List<String> tradeFlowIds;

	/**
	 * 传入交易类型标识 1:充值, 2:投资, 3:提现, 4:其他
	 */
	private Integer tradeTypeFlag;

	/**
	 * 加减号 0:－, 1:＋, 2:其他
	 */
	private Integer sumSubFlag;

	/**
	 * 是否过滤掉解冻交易记录 0:否, 1:是
	 */
	private Integer filterUnFreezeRecord;
	/**
	 * 交易类型集合
	 */
	private List<Integer> tradeTypeList;
	
	private String subCodeDesc;
	/**
	 * app端展示的金额涉及到正负号
	 */
	private String money;

	public FinFundtransfer() {
	}

	public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getSubCodeDesc() {
        return subCodeDesc;
    }

    public void setSubCodeDesc(String subCodeDesc) {
        this.subCodeDesc = subCodeDesc;
    }

    public List<Integer> getTradeTypeList() {
        return tradeTypeList;
    }

    public void setTradeTypeList(List<Integer> tradeTypeList) {
        this.tradeTypeList = tradeTypeList;
    }

    public FinFundtransfer(java.lang.Integer id) {
		this.id = id;
	}

	public void setId(java.lang.Integer id) {
		this.id = id;
	}

	public java.lang.Integer getId() {
		return this.id;
	}

	public void setFlowId(java.lang.String flowId) {
		this.flowId = flowId;
	}

	public java.lang.String getFlowId() {
		return this.flowId;
	}

	public void setTradeFlowId(java.lang.String tradeFlowId) {
		this.tradeFlowId = tradeFlowId;
	}

	public java.lang.String getTradeFlowId() {
		return this.tradeFlowId;
	}

	public void setRegUserId(java.lang.Integer regUserId) {
		this.regUserId = regUserId;
	}

	public java.lang.Integer getRegUserId() {
		return this.regUserId;
	}

	public void setRecRegUserId(java.lang.Integer recRegUserId) {
		this.recRegUserId = recRegUserId;
	}

	public java.lang.Integer getRecRegUserId() {
		return this.recRegUserId;
	}

	public void setTransMoney(java.math.BigDecimal transMoney) {
		this.transMoney = transMoney;
	}

	public java.math.BigDecimal getTransMoney() {
		return this.transMoney;
	}

	public void setSubCode(Integer subCode) {
		this.subCode = subCode;
	}

	public Integer getSubCode() {
		return this.subCode;
	}

	public void setTradeType(Integer tradeType) {
		this.tradeType = tradeType;
	}

	public Integer getTradeType() {
		return this.tradeType;
	}

	public java.math.BigDecimal getPreMoney() {
		return preMoney;
	}

	public void setPreMoney(java.math.BigDecimal preMoney) {
		this.preMoney = preMoney;
	}

	public void setNowMoney(java.math.BigDecimal nowMoney) {
		this.nowMoney = nowMoney;
	}

	public java.math.BigDecimal getNowMoney() {
		return this.nowMoney;
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

	public java.math.BigDecimal getMinTransMoney() {
		return minTransMoney;
	}

	public void setMinTransMoney(java.math.BigDecimal minTransMoney) {
		this.minTransMoney = minTransMoney;
	}

	public java.math.BigDecimal getMaxTransMoney() {
		return maxTransMoney;
	}

	public void setMaxTransMoney(java.math.BigDecimal maxTransMoney) {
		this.maxTransMoney = maxTransMoney;
	}



	public Integer getShowFlag() {
        return showFlag;
    }

    public void setShowFlag(Integer showFlag) {
        this.showFlag = showFlag;
    }

    public List<Integer> getSubCodeList() {
		return subCodeList;
	}

	public void setSubCodeList(List<Integer> subCodeList) {
		this.subCodeList = subCodeList;
	}

	public List<String> getTradeFlowIds() {
		return tradeFlowIds;
	}

	public void setTradeFlowIds(List<String> tradeFlowIds) {
		this.tradeFlowIds = tradeFlowIds;
	}

	public void setTradeTypeFlag(Integer tradeTypeFlag) {
		this.tradeTypeFlag = tradeTypeFlag;
	}

	public Integer getTradeTypeFlag() {
		return tradeTypeFlag;
	}

	public Integer getSumSubFlag() {
		return sumSubFlag;
	}

	public void setSumSubFlag(Integer sumSubFlag) {
		this.sumSubFlag = sumSubFlag;
	}

	public void setFilterUnFreezeRecord(Integer filterUnFreezeRecord) {
		this.filterUnFreezeRecord = filterUnFreezeRecord;
	}

	public Integer getFilterUnFreezeRecord() {
		return filterUnFreezeRecord;
	}

	public java.math.BigDecimal getAfterMoney() {
		return afterMoney;
	}

	public void setAfterMoney(java.math.BigDecimal afterMoney) {
		this.afterMoney = afterMoney;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}
