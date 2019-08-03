package com.hongkun.finance.fund.model.vo;


import java.math.BigDecimal;

public class FundInfoVo implements java.io.Serializable{
		
		private static final long serialVersionUID = 1L;
	 
		/**
		 * 描述: id
		 * 字段: id  INT(10)
		 */
		private java.lang.Integer id;
		
		/**
		 * 描述: 产品类型
		 * 字段: project_id  INT(10)
		 * 默认值: 0
		 */
		private java.lang.Integer projectId;
		/**
		 * 描述: 产品名称
		 * 字段: project_id  INT(10)
		 * 默认值: 0
		 */
		private java.lang.String projectName;
		/**
		 * 产品父类型
		 */
		private java.lang.Integer parentType;
		/**
		 * 描述: 是否有项目信息: 0-否,1-是
		 * 字段: info_exist  TINYINT(3)
		 * 默认值: 1
		 */
		private Integer infoExist;
		
		/**
		 * 描述: 无项目时的项目描述
		 * 字段: introduction  TEXT(65535)
		 */
		private java.lang.String introduction;
		
		/**
		 * 描述: 项目信息名称
		 * 字段: name  VARCHAR(100)
		 * 默认值: ''
		 */
		private java.lang.String name;
		
		/**
		 * 描述: 存息期限单位:1-年,2-月,3-日
		 * 字段: term_unit  TINYINT(3)
		 * 默认值: 2
		 */
		private Integer termUnit;
		
		/**
		 * 描述: 存息期限值
		 * 字段: term_value  TINYINT(3)
		 * 默认值: 0
		 */
		private Integer termValue;
		
		/**
		 * 描述: 起投金额
		 * 字段: lowest_amount  DECIMAL(8)
		 * 默认值: 100.0000
		 */
		private java.math.BigDecimal lowestAmount;
		
		/**
		 * 描述: 起投金额单位：1-元,2-美元
		 * 字段: lowest_amount_unit  TINYINT(3)
		 * 默认值: 1
		 */
		private Integer lowestAmountUnit;
		
		/**
		 * 描述: 产品亮点
		 * 字段: highlights  VARCHAR(255)
		 * 默认值: '严格风控，加速退出，起投门槛低'
		 */
		private java.lang.String highlights;
		
		/**
		 * 描述: 开放日类型：1-工作日,2-自定义日,3-日期范围
		 * 字段: openday_type  TINYINT(3)
		 * 默认值: 1
		 */
		private Integer opendayType;
		
		/**
		 * 描述: 自定义日:期限类型：1-每月,2-每周
		 * 字段: customize_type  TINYINT(3)
		 * 默认值: 0
		 */
		private Integer customizeType;
		
		/**
		 * 描述: 自定义日：期限值
		 * 字段: customize_value  TINYINT(3)
		 * 默认值: 0
		 */
		private Integer customizeValue;
		
		/**
		 * 描述: 日期范围:开始时间
		 * 字段: start_time  TIMESTAMP(19)
		 * 默认值: CURRENT_TIMESTAMP
		 */
		private java.util.Date startTime;
		
		//【非数据库字段，查询时使用】
		private java.util.Date startTimeBegin;
		
		//【非数据库字段，查询时使用】
		private java.util.Date startTimeEnd;
		/**
		 * 描述: 日期范围:结束时间
		 * 字段: end_time  TIMESTAMP(19)
		 * 默认值: CURRENT_TIMESTAMP
		 */
		private java.util.Date endTime;
		
		//【非数据库字段，查询时使用】
		private java.util.Date endTimeBegin;
		
		//【非数据库字段，查询时使用】
		private java.util.Date endTimeEnd;
		/**
		 * 描述: 投资范围
		 * 字段: invest_range  TEXT(65535)
		 */
		private java.lang.String investRange;
		
		/**
		 * 描述: 运作方式
		 * 字段: operation_style  VARCHAR(255)
		 * 默认值: '契约封闭式'
		 */
		private java.lang.String operationStyle;
		
		/**
		 * 描述: 备注
		 * 字段: remark  TEXT(65535)
		 */
		private java.lang.String remark;
		
		/**
		 * 描述: 创建时间
		 * 字段: create_time  TIMESTAMP(19)
		 * 默认值: CURRENT_TIMESTAMP
		 */
		private java.util.Date createTime;
		
		//【非数据库字段，查询时使用】
		private java.util.Date createTimeBegin;
		
		//【非数据库字段，查询时使用】
		private java.util.Date createTimeEnd;
		/**
		 * 描述: 创建人员ID
		 * 字段: create_user_id  INT(10)
		 * 默认值: 0
		 */
		private java.lang.Integer createUserId;
		/***
		 * 描述: 创建人员
		 */
		private java.lang.String createUserName;
		
		/**
		 * 描述: 修改时间
		 * 字段: modify_time  TIMESTAMP(19)
		 * 默认值: CURRENT_TIMESTAMP
		 */
		private java.util.Date modifyTime;
		
		//【非数据库字段，查询时使用】
		private java.util.Date modifyTimeBegin;
		
		//【非数据库字段，查询时使用】
		private java.util.Date modifyTimeEnd;
		/**
		 * 描述: 最后修改人员ID
		 * 字段: modify_user_id  INT(10)
		 * 默认值: 0
		 */
		private java.lang.Integer modifyUserId;
		/**
		 * 描述: 最后修改人员
		 * 字段: modify_user_id  INT(10)
		 * 默认值: 0
		 */
		private java.lang.String modifyUserName;
		/**
		 * 描述: 管理机构
		 * 字段: management  VARCHAR(100)
		 * 默认值: ''
		 */
		private java.lang.String management;
		
		/**
		 * 描述: 状态:0-删除,1-待上架,2-上架,3-下架
		 * 字段: state  TINYINT(3)
		 * 默认值: 1
		 */
		private Integer state;
		
		/**
		 * 描述: 预约状态:0-停约,1-预约中
		 * 字段: subscribe_state  TINYINT(3)
		 * 默认值: 0
		 */
		private Integer subscribeState;
		
		/**
		 * 描述: 收益类型
		 * 字段: revenue_type  VARCHAR(255)
		 * 默认值: '固定收益类'
		 */
		private java.lang.String revenueType;
		
		/**
		 * 描述: 付息方式: 1-按月付息,2-按季付息,3-半年付息,4-按年付息,5-到期付息
		 * 字段: pay_way  TINYINT(3)
		 * 默认值: 1
		 */
		private Integer payWay;
		
		/**
		 * 描述: 成立日期
		 * 字段: established_time  TIMESTAMP(19)
		 * 默认值: CURRENT_TIMESTAMP
		 */
		private java.util.Date establishedTime;
		
		//【非数据库字段，查询时使用】
		private java.util.Date establishedTimeBegin;
		
		//【非数据库字段，查询时使用】
		private java.util.Date establishedTimeEnd;
		/**
		 * 描述: 年利率范围起始(%)
		 * 字段: min_rate  DOUBLE(10)
		 * 默认值: 0.00
		 */
		private java.lang.Double minRate;
		
		/**
		 * 描述: 年利率范围结束(%)
		 * 字段: max_rate  DOUBLE(10)
		 * 默认值: 0.00
		 */
		private java.lang.Double maxRate;
		/**
		 * 股权产品项目类型介绍
		 */
		private java.lang.String projectIntroduction;
		  /**
		   * 排序字段
		   */
		private String sortColumns;
		/**
		 * 描述: 1-明星独角兽系列,2- PRE-IPO系列,3-产业协同系列,4-地产基金,5-海外基金 6-信托产品
		 * 字段: type  TINYINT(3)
		 * 默认值: 1
		 */
		private Integer type;
		/**
		 * 是否开放日
		 */
		private Integer openDateFlag;
		/**
		 * 开发日描述
		 */
		private String openDateDescribe;


		/**
		 * 描述: 认购协议类型：1-Class A Subsciption； 2-Class B Subsciption
		 * 字段: contract_type  TINYINT(3)
		 * 默认值: 1
		 */
		private Integer contractType;

		/**
		 * 描述: 海外基金投资步长
		 * 字段: stepValue  DECIMAL(20)
		 * 默认值: 0.00
		 */
		private java.math.BigDecimal stepValue;


		/**
		 *  ##### 添加字段  审核状态（用于前台控制）：0-立即预约，1-预约中
		 */
		private Integer auditState;

		/**
		 * 存续期限描述
		 */
		private String termDescribe;

		public FundInfoVo(){
		}

		public String getTermDescribe() {
			return termDescribe;
		}

		public void setTermDescribe(String termDescribe) {
			this.termDescribe = termDescribe;
		}

		public void setAuditState(Integer auditState) {
				this.auditState = auditState;
			}

		public Integer getAuditState() {
			return auditState;
		}

		public Integer getContractType() {
					return contractType;
				}

		public void setContractType(Integer contractType) {
			this.contractType = contractType;
		}

		public BigDecimal getStepValue() {
			return stepValue;
		}

		public void setStepValue(BigDecimal stepValue) {
			this.stepValue = stepValue;
		}

		public Integer getOpenDateFlag() {
			return openDateFlag;
		}

		public void setOpenDateFlag(Integer openDateFlag) {
			this.openDateFlag = openDateFlag;
		}

		public String getOpenDateDescribe() {
			return openDateDescribe;
		}

		public void setOpenDateDescribe(String openDateDescribe) {
			this.openDateDescribe = openDateDescribe;
		}

		public FundInfoVo(java.lang.Integer id){
			this.id = id;
		}

		public void setId(java.lang.Integer id) {
			this.id = id;
		}
		
		public java.lang.Integer getId() {
			return this.id;
		}
		
		public void setProjectId(java.lang.Integer projectId) {
			this.projectId = projectId;
		}
		
		public java.lang.Integer getProjectId() {
			return this.projectId;
		}
		
		public void setInfoExist(Integer infoExist) {
			this.infoExist = infoExist;
		}
		
		public Integer getInfoExist() {
			return this.infoExist;
		}
		
		public void setIntroduction(java.lang.String introduction) {
			this.introduction = introduction;
		}
		
		public java.lang.String getIntroduction() {
			return this.introduction;
		}
		
		public void setName(java.lang.String name) {
			this.name = name;
		}
		
		public java.lang.String getName() {
			return this.name;
		}
		
		public void setTermUnit(Integer termUnit) {
			this.termUnit = termUnit;
		}
		
		public Integer getTermUnit() {
			return this.termUnit;
		}
		
		public void setTermValue(Integer termValue) {
			this.termValue = termValue;
		}
		
		public Integer getTermValue() {
			return this.termValue;
		}
		
		public void setLowestAmount(java.math.BigDecimal lowestAmount) {
			this.lowestAmount = lowestAmount;
		}
		
		public java.math.BigDecimal getLowestAmount() {
			return this.lowestAmount;
		}
		
		public void setLowestAmountUnit(Integer lowestAmountUnit) {
			this.lowestAmountUnit = lowestAmountUnit;
		}
		
		public Integer getLowestAmountUnit() {
			return this.lowestAmountUnit;
		}
		
		public void setHighlights(java.lang.String highlights) {
			this.highlights = highlights;
		}
		
		public java.lang.String getHighlights() {
			return this.highlights;
		}
		
		public void setOpendayType(Integer opendayType) {
			this.opendayType = opendayType;
		}
		
		public Integer getOpendayType() {
			return this.opendayType;
		}
		
		public void setCustomizeType(Integer customizeType) {
			this.customizeType = customizeType;
		}
		
		public Integer getCustomizeType() {
			return this.customizeType;
		}
		
		public void setCustomizeValue(Integer customizeValue) {
			this.customizeValue = customizeValue;
		}
		
		public Integer getCustomizeValue() {
			return this.customizeValue;
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
		public void setInvestRange(java.lang.String investRange) {
			this.investRange = investRange;
		}
		
		public java.lang.String getInvestRange() {
			return this.investRange;
		}
		
		public void setOperationStyle(java.lang.String operationStyle) {
			this.operationStyle = operationStyle;
		}
		
		public java.lang.String getOperationStyle() {
			return this.operationStyle;
		}
		
		public void setRemark(java.lang.String remark) {
			this.remark = remark;
		}
		
		public java.lang.String getRemark() {
			return this.remark;
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
		public void setCreateUserId(java.lang.Integer createUserId) {
			this.createUserId = createUserId;
		}
		
		public java.lang.Integer getCreateUserId() {
			return this.createUserId;
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
		public void setModifyUserId(java.lang.Integer modifyUserId) {
			this.modifyUserId = modifyUserId;
		}
		
		public java.lang.Integer getModifyUserId() {
			return this.modifyUserId;
		}
		
		public void setManagement(java.lang.String management) {
			this.management = management;
		}
		
		public java.lang.String getManagement() {
			return this.management;
		}
		
		public void setState(Integer state) {
			this.state = state;
		}
		
		public Integer getState() {
			return this.state;
		}
		
		public void setSubscribeState(Integer subscribeState) {
			this.subscribeState = subscribeState;
		}
		
		public Integer getSubscribeState() {
			return this.subscribeState;
		}
		
		public void setRevenueType(java.lang.String revenueType) {
			this.revenueType = revenueType;
		}
		
		public java.lang.String getRevenueType() {
			return this.revenueType;
		}
		
		public void setPayWay(Integer payWay) {
			this.payWay = payWay;
		}
		
		public Integer getPayWay() {
			return this.payWay;
		}
		
		public void setEstablishedTime(java.util.Date establishedTime) {
			this.establishedTime = establishedTime;
		}
		
		public java.util.Date getEstablishedTime() {
			return this.establishedTime;
		}
		
		public void setEstablishedTimeBegin(java.util.Date establishedTimeBegin) {
			this.establishedTimeBegin = establishedTimeBegin;
		}
		
		public java.util.Date getEstablishedTimeBegin() {
			return this.establishedTimeBegin;
		}
		
		public void setEstablishedTimeEnd(java.util.Date establishedTimeEnd) {
			this.establishedTimeEnd = establishedTimeEnd;
		}
		
		public java.util.Date getEstablishedTimeEnd() {
			return this.establishedTimeEnd;
		}	
		public void setMinRate(java.lang.Double minRate) {
			this.minRate = minRate;
		}
		
		public java.lang.Double getMinRate() {
			return this.minRate;
		}
		
		public void setMaxRate(java.lang.Double maxRate) {
			this.maxRate = maxRate;
		}
		
		public java.lang.Double getMaxRate() {
			return this.maxRate;
		}

		public java.lang.String getProjectName() {
			return projectName;
		}

		public void setProjectName(java.lang.String projectName) {
			this.projectName = projectName;
		}

		public java.lang.Integer getParentType() {
			return parentType;
		}

		public void setParentType(java.lang.Integer parentType) {
			this.parentType = parentType;
		}

		public java.lang.String getProjectIntroduction() {
			return projectIntroduction;
		}

		public void setProjectIntroduction(java.lang.String projectIntroduction) {
			this.projectIntroduction = projectIntroduction;
		}

		public java.lang.String getCreateUserName() {
			return createUserName;
		}

		public void setCreateUserName(java.lang.String createUserName) {
			this.createUserName = createUserName;
		}

		public java.lang.String getModifyUserName() {
			return modifyUserName;
		}

		public void setModifyUserName(java.lang.String modifyUserName) {
			this.modifyUserName = modifyUserName;
		}

		public String getSortColumns() {
			return sortColumns;
		}

		public void setSortColumns(String sortColumns) {
			this.sortColumns = sortColumns;
		}

		public Integer getType() {
			return type;
		}

		public void setType(Integer type) {
			this.type = type;
		}
		
	}

