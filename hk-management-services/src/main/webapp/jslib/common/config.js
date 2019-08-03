/**
 * 接口定义 xuhui.liu
 */
'use strict';
var CONFIG={};
var HOST = commonUtil.getUrl();

/**消息框提示类型**/
var ALERT_SUC  = "SUCCESS" ;
var ALERT_ERR  = "ERROR" ;
var ALERT_WARN  = "WARN" ;

CONFIG.CONSTANTS={
		IMG_URL_ROOT  : 'http://test-yr-platform-hkjf.oss-cn-beijing.aliyuncs.com/',
		IMG_URL_HKJF_ROOT  : 'http://test-yr-platform-hkjf.oss-cn-beijing.aliyuncs.com/',
        IMG_URL_UNIVERSAL    :       '${oss.url.universal}',
		IMG_URL       :  HOST+'upload',
		SUCCESS_STATE : 1000, //后台回调成功状态码,
    	CONTAINER_FLAG	:'container_flag',
        /**登录票据*/
        SSO_TICKET : "ticket_admin",
        /**SSO登录状态-失败同步次数*/
        SSO_STATE_RETRY_TIMES : 2,
        /**SSO登录状态-在线*/
        SSO_STATE_ONLINE : "online",
        /**SSO登录状态-离线*/
        SSO_STATE_OFFLINE : "offline",
        /**登录用户*/
        LOGIN_REG_USER : "login_reg_user",
        /**前台项目根路径*/
        FINANCE_BASE_PATH:"${finance_project_base_path}"
};
/**平台类型**/
CONFIG.PLATFORMS={
		QSH : 'qsh',
		HKJF : 'hkjf'
};
/**图片在阿里云的存储路径**/
CONFIG.FILEPATHS={
		INFO_FILEPATH : '/information/info',
		POINT_PRODUCT_FILEPATH : '/pointmall/pointProduct'
};

/**公共接口路由*/
CONFIG.interface={
	dicList              			:HOST+'dicController/dicList',/*加载字典表*/
	dicAreaList              		:HOST+'dicController/dicAreaList',/*加载区域字典表*/
	uploadFile             			:HOST+'commonsController/uploadFile'/*文件上传*/
};

/**标的路由（invest&loan服务模块）*/
CONFIG.interface = jQuery.extend({
	loanBidInfoList      			:HOST+'loanController/loanBidInfoList',/**标的列表查询（待放款）*/
	auditBidInfoList                :HOST+'bidInfoController/auditBidInfoList',/**标的列表查询（待审核）*/
    auditBid                :HOST+'bidInfoController/auditBid',/**标的列表查询（待审核）*/
	bidInfoAfterLoanList            :HOST+'bidInfoController/bidInfoAfterLoanList',/**标的列表查询（还款中&已完成）*/
	findBidInfoDetails				:HOST+'bidInfoController/findBidInfoDetails',/**标的详情查询*/
	bidInfoDetailList      			:HOST+'bidInfoController/bidInfoDetailList',/**标的列表查询（筹款中）*/
	allBidInfoList					:HOST+'bidInfoController/allBidInfoList',/**标的列表查询(所有标，用于匹配管理中标的查询)*/
	makeLoans			 			:HOST+'loanController/makeLoans',/** 放款 */
	findBidInfos	        		:HOST+'bidInfoController/findBidInfo',
    hasRecommendEarnEnable	        :HOST+'bidInfoController/hasRecommendEarnEnable',/**判断当前是否有启用的规则*/
	investList						:HOST+'bidInvestController/investList',
	saveBidInfos	        		:HOST+'bidInfoController/save',
	updateBidInfos	        		:HOST+'bidInfoController/updateBidInfo',
	updateCreditorProperty			:HOST+'bidInfoController/updateCreditorProperty',
	planCountList					:HOST + "bidPlanController/planCountList",
	planList						:HOST + "bidPlanController/planList",
	bidInvestListForCreditor        :HOST + "bidInvestController/listAllBidInvest", /**根据标的id获取投资记录 */
	depositBidList        			:HOST + "bidInfoController/depositBidList", /**加载购房宝&物业宝标的信息 */
	bidProductList					:HOST + "bidProductController/bidProductList", /**检索标的产品信息 */
	addBidProduct					:HOST + "bidProductController/save",	/**添加标的产品信息*/
	updateBidProduct				:HOST + "bidProductController/update",	/**添加标的产品信息*/
	preUpdateBidProduct				:HOST + "bidProductController/select",	/**检索标的产品信息*/
	bidAutoSchemeList				:HOST + "bidInvestController/bidAutoSchemeList"	/**检索用户设置的自动投资设置*/
}, CONFIG.interface || {});

/**匹配管理路由*/
CONFIG.interface = jQuery.extend({
	matchBidInfoList          			:HOST+'bidMatchController/matchBidInfoList',
	matchBidDetail						:HOST+'bidMatchController/matchBidDetail',
	doMatch								:HOST+'bidMatchController/doMatch',
	bidMatchRecord						:HOST+'bidMatchController/matchListByBid',
	bidMatchList						:HOST+'bidMatchController/bidMatchList',
	matchedDetails						:HOST+'bidMatchController/matchedDetails',
	bidMatchAmountStatistics			:HOST+'bidMatchController/bidMatchAmountStatistics',
    miniMatchBidInfoList                :HOST+'bidMatchController/miniMatchBidInfoList',
    exportExcelMatchBidList             :HOST+'bidMatchController/exportExcelMatchBidList',
    bidExchangeList                     :HOST+'bidInvestExchangeController/bidExchangeList',
    investExchangeList                  :HOST+'bidInvestExchangeController/investExchangeList',
    matchInvestExchange                 :HOST+'bidInvestExchangeController/matchInvestExchange',
    investExchangeListForPager          :HOST+'bidInvestExchangeController/investExchangeListForPager'
}, CONFIG.interface || {});


/**积分规则路由*/
CONFIG.interface = jQuery.extend({
	pointRuleList          			:HOST+'pointRuleController/pointRuleList',
    pointAccountList          			:HOST+'pointController/pointAccountList',
    pointRecordList          			:HOST+'pointController/pointRecordList',
    pointPayRecordList          			:HOST+'pointController/pointPayRecordList',
    givePointToUser          			:HOST+'pointController/givePointToUser',
    checkPoint          			:HOST+'pointController/checkPoint',
	addPointRule          				:HOST+'pointRuleController/addPointRule',
	updatePointRule        			:HOST+'pointRuleController/updatePointRule',
    savePointMerchant        			:HOST+'pointMerchantController/save',
    pointMerchantCount        			:HOST+'pointMerchantController/pointMerchantCount',
    selectPointMerchantInfoDetail        			:HOST+'pointMerchantController/selectPointMerchantInfoDetail',
    updatePointMerchant        			:HOST+'pointMerchantController/update',
    pointMerchantList        			:HOST+'pointMerchantController/pointMerchantList',
    checkPointMerchant        			:HOST+'pointMerchantController/checkPointMerchant'
}, CONFIG.interface || {});

/**积分商城规则路由*/
CONFIG.interface = jQuery.extend({
    listCategories          			:HOST+'pointProductCategoryController/listCategories',
    listProducts         			:HOST+'pointProductController/pointProductList',
    listOrder         			:HOST+'pointOrderController/listPointOrder',
    partProductList         			:HOST+'pointProductPartController/partProductList',
    filterProducts         			:HOST+'pointProductPartController/filterProducts',
    deleteCategory          			:HOST+'pointProductCategoryController/delete',
    updateCategory          			:HOST+'pointProductCategoryController/update',
    importProductToPart          			:HOST+'pointProductPartController/importProductToPart',
    savePointProduct          			:HOST+'pointProductController/save',
    checkPointProduct          			:HOST+'pointProductController/check',
    checkOrder          			:HOST+'pointOrderController/checkOrder',
    updateCourierNo          			:HOST+'pointOrderController/updateCourierNo',
    updatePointProduct          			:HOST+'pointProductController/update',
    pointProductInfo          			:HOST+'pointProductController/pointProductInfo',
    saveCategory          			:HOST+'pointProductCategoryController/save',
    refreshCategories          			:HOST+'pointProductCategoryController/refreshCategories',
    findSellingRecord          			:HOST+'pointProductController/findSellingRecord'
}, CONFIG.interface || {});



/**资讯路由*/
CONFIG.interface = jQuery.extend({
	//资讯调查问卷添加
	insertInfoQuestionnaire	 		:HOST+'informationNewsController/insertInfoQuestionnaire',
	//资讯调查问卷更新
	updateInfoQuestionnaire	 		:HOST+'informationNewsController/updateInfoQuestionnaire',
	//查询调查问卷及选项
	findQuestionAndItem	 		:HOST+'informationNewsController/findQuestionAndItem',
	//资讯信息公告类插入
	insertInfomations	 			:HOST+'informationNewsController/insertInformationNews',
	//资讯广告轮播类信息插入
	insertCarouselFigure	 			:HOST+'informationNewsController/insertCarouselFigure',
	//资讯新闻信息插入
	insertNews	 			:HOST+'informationNewsController/insertNews',
	//查询资讯信息
	searchInfomations 	   			:HOST+'informationNewsController/searchInfomationByCondition',
	//查询新闻信息
	searchInfoNewsByCondition 	    :HOST+'informationNewsController/searchInfoNewsByCondition',
	//查询广告轮播信息
	searchCarouselFigureByCondition :HOST+'informationNewsController/searchCarouselFigureByCondition',
	//通过ID，查询资讯信息
	findInfomationById 	    		:HOST+'informationNewsController/searchInformationNewsById',
	//查询资讯的调查问卷
	findInfoQuesetionById 	    		:HOST+'informationNewsController/findInfoQuesetionById',
	//更新公告类资讯信息
	updateInformationNews  	 		:HOST+'informationNewsController/updateInformationNews',
	//更新广告轮播类资讯信息
	updateCarouselFigure  	 		:HOST+'informationNewsController/updateCarouselFigure',
	//更新新闻资讯信息
	updateNews  	 		:HOST+'informationNewsController/updateNews'
}, CONFIG.interface || {});

/**用户路由*/
CONFIG.interface = jQuery.extend({
	//用户列表
	userList            			:HOST + 'userController/userList',
    //物业列表
	tenement            			:HOST + 'communityController/findUserTypeTenement',
    //小区列表
    community            			:HOST + 'communityController/communityList',
    //找到可以用来绑定的小区列表
    findCommunityAvailable            			:HOST + 'communityController/findCommunityAvailable',
    //查询物业已经绑定的小区列表
    findTenementsCommunity            			:HOST + 'communityController/findTenementsCommunity',
	//加载企业用户
	enterpriseUserList            	:HOST + 'userController/enterpriseUserList',
	//账户资金详情
	userAccountMoney    			:HOST + 'userController/userAccountMoney',
	//用户详情
	userDetial          			:HOST + 'userController/userDetial',
	//添加企业用户
	addCompanyUser          		:HOST + 'userController/addCompanyUser',
	//地址列表
	addressList          :HOST + 'userAddressController/addressList',
	//物业公司列表
	findPropertyDicDataList : HOST + 'userController/findPropertyDicDataList',
	//更新用户邀请码
	updateCommendNo          		:HOST + 'userController/updateCommendNo',
	//app用户登录信息列表
	appLoginLogList          		:HOST + 'userController/appLoginLogList',
    //更新用户状态
    chgUserState                    :HOST + 'userController/chgRegUserState',
    //用户资料维护
    userAudit                    :HOST + 'userController/userAudit',
    //用户资料上传
    userAuditUpload                    :HOST + 'userController/userAuditUpload',
    //删除用户资料
    delUserAudit                    :HOST + 'userController/delUserAudit',
    //VIP记录列表
    vipRecordsList            			:HOST + 'userController/vipRecordsList',
    //VIP记录详情列表
    vipRecordsDetailList            			:HOST + 'userController/vipRecordsDetailList'

}, CONFIG.interface || {});

/**支付路由*/
CONFIG.interface = jQuery.extend({
	//交易记录
	tradeRecordList            	: HOST + 'paymentController/tradeRecordList',
	//充值提现统计
	findPaymentRecordCountList            	: HOST + 'paymentController/findPaymentRecordCountList',
	//查询用户的银行卡绑定信息
	bankCardList				: HOST + 'paymentController/bankCardList',
	//支付模式查询
	paymentChannelList			: HOST + 'paymentController/paymentChannelList',
	//支付模式设置
	insertPaymentChannel			: HOST + 'paymentController/insertPaymentChannel',
	//修改支付模式
	updatePaymentChannel			: HOST + 'paymentController/updatePaymentChannel',
	//启用禁用支付模式
	paymentChannelState			: HOST + 'paymentController/paymentChannelState',
	//账户资金划转查询
	paymentFundtransferList            	: HOST + 'paymentController/tradeRecordList',
	//通过ID查询支付模式
	searchPaymentChannelById            : HOST + 'paymentController/searchPaymentChannelById',
	//查询第三方银行信息
	findBankList                  : HOST + 'paymentController/findBankList',
	//查询第三方银行搜索条件信息
	findBankCondition                  : HOST + 'paymentController/findBankCondition',
	//通过ID，查询银行信息
	findBankReferById                  : HOST + 'paymentController/findBankReferById',
	//通过ID，更新银行限额
	updateBankReferById                  : HOST + 'paymentController/updateBankReferById',
	//用户与第三方对账
	findUserPayCheck  : HOST + 'paymentReconciliationController/findUserPayCheck',
	//平台与第三方支付对账
	findPayCheckReconliciation  : HOST + 'paymentReconciliationController/findPayCheckReconliciation',
	//解绑银行卡查询
	findBankCardUpdateList      : HOST + 'bankCardController/findBankCardUpdateList',
	//解绑银行卡审核
	auditBankCardUpdate         : HOST + 'bankCardController/auditBankCardUpdate',
	//解绑银行卡详情
	findBankCardUpdateInfo      : HOST + 'bankCardController/findBankCardUpdateInfo'
}, CONFIG.interface || {});

/**钱袋子路由*/
CONFIG.interface = jQuery.extend({
	//每日利息错误记录
	searchInterestDayFailList     :HOST+'qdzController/searchInterestDayFailList',
	//活期产品查询
	qdzHqProductInfoList    :HOST+'qdzController/searchHqProductInfo',
	//账单流水查询
	searchQdzBillWater    :HOST+'qdzController/searchQdzBillWater',
	//交易失败记录查询
	searchTransRecordFailList    :HOST+"qdzController/searchTransRecordFailList",
	//利息对账查询
	searchInterestBalance    :HOST+'qdzController/searchInterestBalance',
	//当前债权查询
	searchCreditorInfo    :HOST+'qdzController/searchCreditorInfo',
	//债权对账查询
	searchCreditorBalanceInfo    :HOST+'qdzController/searchCreditorBalanceInfo',
	//钱袋子规则管理
	searchQdzRule    :HOST+'qdzController/searchQdzRule',
	//保存钱袋子规则
	saveQdzRule	        		:HOST+'qdzController/insertQdzRule',
	//根据ID查询钱袋子规则
	getQdzRule	        		:HOST+'qdzController/getQdzRule',
	//修改钱袋子规则
	updateQdzRule             :HOST+'qdzController/updateQdzRule',
	//查询钱袋子推荐奖规则
    searchQdzRecommendEarnRule    :HOST+'qdzController/searchQdzRecommendEarnRule',
	//保存钱袋子推荐奖规则
	saveQdzRecommendEarnRule	        		:HOST+'qdzController/insertQdzRecommendEarnRule',
	//修改钱袋子推荐奖规则
	updateQdzRecommendEarnRule             :HOST+'qdzController/updateQdzRecommendEarnRule',
	//查询债券池规则设置
	searchQdzCreditorRule             :HOST+'qdzController/searchQdzCreditorRule',
	//修改债券池规则
	updateQdzCreditorRule       :HOST+'qdzController/updateQdzCreditorRule',
	//查询钱袋子利息明细
	searchInterestInfo       :HOST+'qdzController/searchInterestInfo'

}, CONFIG.interface || {});


/**增值服务路由*/
CONFIG.interface = jQuery.extend({
	//推荐奖
	recommendEarnList     : HOST + 'recommendEarnController/recommendEarnList',
	//红包线下推广
	redPacketOffLine     : HOST + 'redpacketController/list'
}, CONFIG.interface || {});

/**合同类型维护路由**/
CONFIG.interface = jQuery.extend({
    contractTypeList       :HOST+'contractTypeController/contractTypeList',
    addContractType		 :HOST+'contractTypeController/addContractType',
	updateContractType	 :HOST+'contractTypeController/updateContractType',
	getContractTypeAndName:HOST+'contractTypeController/getContractTypeAndName',
	contractTemplateList	 :HOST+'contractTemplateController/contractTemplateList',
	addContractTemplate	 :HOST+'contractTemplateController/addContractTemplate',
	updateContractTemplate :HOST+'contractTemplateController/updateContractTemplate',
    findContractTemplateDetail:HOST+'contractTemplateController/findContractTemplateDetail',
    //车辆信息列表
    searchCarInfoList            :HOST + 'carInfoController/searchCarInfoList',
    //车辆信息添加
    addCarInfo            		  : HOST + 'carInfoController/addCarInfo',
    //车辆信息更新
    updateCarInfo            	  : HOST + 'carInfoController/updateCarInfo',
    //车辆信息查询
    findCarInfoById              : HOST + 'carInfoController/findCarInfoById',
    //车辆信息删除
    deleteCarInfoById            : HOST + 'carInfoController/deleteCarInfoById',
    //汽车合同列表
    searchCarContractList       : HOST + 'carContractController/searchCarContractList',
    //汽车合同添加
    addCarContract              : HOST + 'carContractController/addCarContract',
    //查询合同用户信息
    selectUserInfoByTel         : HOST + 'carContractController/selectUserInfoByTel',
    //汽车合同查询
    findCarContractById         : HOST + 'carContractController/findCarContractById',
    //汽车合同更新
    updateCarContract           : HOST + 'carContractController/updateCarContract',
    //汽车合同下载
    downloadCarContract         : HOST + 'carContractController/downloadCarContract',
    //汽车信息导入
    carInfoImport               : HOST + 'carInfoController/carInfoImport'
},CONFIG.interface || {});

/**体验金管理维护路由**/
CONFIG.interface = jQuery.extend({
	//体验金发放
	saveSimGrant       :HOST+'simGoldController/saveSimGrant',
	//体验金规则查询
	searchGoldRule     :HOST+'simGoldRuleController/searchGoldRule',
	//生成体验金规则
	saveVasGoldRule     :HOST+'simGoldRuleController/saveVasGoldRule',
	//用户体验金查询
	searchSimGold       :HOST+'simGoldController/searchSimGold',
	//更新体验金状态
	updateSimGold       :HOST+'simGoldController/updateSimGold',
	//统计体验金信息
	findSimGoldCountInfo       :HOST+'simGoldController/findSimGoldCountInfo',
	//查询体验金规则状态
	findSimGoldRuleState      :HOST+'simGoldController/findSimGoldRuleState',
    //体验金批量发放
    simGoldImport      :HOST+'simGoldController/simGoldImport'
},CONFIG.interface || {});

/**黑白名单管理*/
CONFIG.interface = jQuery.extend({
	//用户权限功能列表
	rosInfoList     : HOST + 'rosInfoController/rosInfoList',
    //删除黑白名单
    delRosInfo     : HOST + 'rosInfoController/delRosInfo',
	//添加用户权限功能名单
	addRosInfo          :HOST + 'rosInfoController/addRosInfo',
    //企业员工关系表
    rosStaffInfoList     : HOST + 'rosInfoController/rosStaffInfoList',
    //添加企业员工关系名单
    addRosStaffInfo          :HOST + 'rosInfoController/addRosStaffInfo',
    //删除企业员工名单
    delRosStaffInfo          :HOST + 'rosInfoController/delRosStaffInfo',
	//购房宝&物业宝意向金名单列表
	rosDepositInfoList     : HOST + 'rosInfoController/rosDepositInfoList',
	//添加购房宝&物业宝意向金名单
	addRosDepositInfo          :HOST + 'rosInfoController/addRosDepositInfo',
	//预更新购房宝&物业宝意向金名单
	preUpdateRosDepositInfo          :HOST + 'rosInfoController/preUpdateRosDepositInfo'
}, CONFIG.interface || {});

/**黑白名单管理*/
CONFIG.interface = jQuery.extend({
    //用户权限功能列表
    rosNoticeList     : HOST + 'rosNoticeController/rosNoticeList',
    //删除黑白名单
    addRosNotice     : HOST + 'rosNoticeController/addRosNotice',
    //添加用户权限功能名单
    delRosNotice          :HOST + 'rosNoticeController/delRosNotice'
}, CONFIG.interface || {});

/**系统开关管理*/
CONFIG.interface = jQuery.extend({
    //用户权限功能列表
    sysFunctionCfgList     : HOST + 'sysFunctionCfgController/sysFunctionCfgList',
    updateFunctionCfgState     : HOST + 'sysFunctionCfgController/updateFunctionCfgState'
}, CONFIG.interface || {});




/**好友推荐管理*/
CONFIG.interface = jQuery.extend({
	//查询推荐规则
	findRecommendRule          :HOST + 'recommendRuleController/search',
	//添加推荐规则
	addRecommendRule           :HOST + 'recommendRuleController/save',
	//更新推荐规则
	updateRecommendRule           :HOST + 'recommendRuleController/update',
	//佣金发放、审核列表查询
	findRecommendEarnByCondition :HOST + 'recommendEarnController/searchRecommendEarnByCondition',
	//更新佣金审核状态
	updateRecommendEarnByIds :HOST + 'recommendEarnController/updateRecommendEarnByIds',
	//好友推荐奖励发放
	sendReconmmendEarn     :HOST + 'recommendEarnController/sendReconmmendEarn',
	//佣金查询
	findRecommendEarnCountInfo     :HOST + 'recommendEarnController/findRecommendEarnCountInfo',
	//查询规则
	searchVasRuleById     :HOST + 'recommendRuleController/searchVasRuleById',
	//更新规则
	updateVasRuleById     :HOST + 'recommendRuleController/updateVasRuleById',
	//物业业绩查询
	findPropertyAchievement     :HOST + 'recommendEarnController/findPropertyAchievement',
	//查询物业公司机构编码
	findGroupCodeInfo     :HOST + 'recommendEarnController/findGroupCodeInfo',
	//更新推广来源
	updateSpreadSource     :HOST + 'recommendEarnController/updateSpreadSource',
	//查询推广来源信息
	findSpreadSourceInfo     :HOST + 'recommendEarnController/findSpreadSourceInfo',
	//佣金发放、审核列表查询
	findRecommendEarnInfo    :HOST + 'recommendEarnController/findRecommendEarnInfo',
	//查询推荐人推荐奖励明细信息
	searchRecommendEarnByUserId :HOST + 'recommendEarnController/searchRecommendEarnByUserId',
}, CONFIG.interface || {});


/**消息管理*/
CONFIG.interface = jQuery.extend({
	//检索需要发送短信的用户
	sendTelMsgList     		 : HOST + 'smsController/sendTelMsgList',
	//短信发送操作
	sendTelMsg     			 : HOST + 'smsController/sendTelMsg',
    //一键发送短信
    sendTelMsgBatch     	 : HOST + 'smsController/sendTelMsgBatch',
	//检索用户验证码笑消息
	authCodeTelMsgList     	 : HOST + 'smsController/authCodeTelMsgList',
	//检索站内信消息
	webMsgList     			 : HOST + 'smsController/webMsgList',
	//查看站内信详情
	webMsgDetail     		 : HOST + 'smsController/webMsgDetail',
    //查看app推送消息
    appMsgPushList           : HOST + 'smsController/appMsgPushList',
    //添加app消息推送
    addAppMsgPush            : HOST + 'smsController/addAppMsgPush',
    //删除app消息推送
    deleteAppMsgPush         :HOST + 'smsController/deleteAppMsgPush',
    //停止app消息推送
    stopAppMsgPush           :HOST + 'smsController/stopAppMsgPush',
    //获取app消息推送详情
    appMsgPushDetail         :HOST + 'smsController/appMsgPushDetail',
    //短信消息管理
    telMsgManageList         :HOST + 'smsController/telMsgManageList'
}, CONFIG.interface || {});

/**物业缴费管理*/
CONFIG.interface = jQuery.extend({
	//物业缴费审核列表
	auditProPayRecordList     : HOST + 'propertyPaymentController/auditProPayRecordList',
	auditProPayment			  : HOST + 'propertyPaymentController/auditProPayment'
}, CONFIG.interface || {});

/** 小区地址管理*/
CONFIG.interface = jQuery.extend({
	//物业缴费审核列表
	findCommunityDicDataList     : HOST + 'communityController/findCommunityDicDataList' ,
    bindCommunityToTenement      : HOST + 'communityController/bindCommunityToTenement' ,
    //找到不带分页的物业列表
    findUserTypeTenementNoPage   : HOST + 'communityController/findUserTypeTenementNoPage',
    //保存小区
    saveCommunity                : HOST + 'communityController/saveCommunity',
    //找到物业对应的小区
    findTenementsCommunity       : HOST + 'communityController/findTenementsCommunity'
}, CONFIG.interface || {});


/**增值服务-会员等级**/
CONFIG.interface = jQuery.extend({
	//会员等级标准列表
	vipGradeStandardList		:HOST + 'vipGradeStandardController/vipGradeStandardList',
	//添加会员等级标准
	addVipGradeStandard		:HOST + 'vipGradeStandardController/addVipGradeStandard',
	//更新会员等级标准
    updateVipGradeStandard	:HOST + 'vipGradeStandardController/updateVipGradeStandard',
	//会员成长值规则列表
    vipGrowRuleList			:HOST + 'vipGrowRuleController/vipGrowRuleList',
	//添加会员成长值规则
	addVipGrowRule				:HOST + 'vipGrowRuleController/addVipGrowRule',
	//更新会员成长值规则
	updateVipGrowRule			:HOST + 'vipGrowRuleController/updateVipGrowRule',
	//生成线下红包
    produceRedpacket			:HOST + 'redpacketController/produceRedpacket',
	//审核派发红包
    checkRedpacket			:HOST + 'redpacketController/checkRedPacket',
	//批量失效红包
    invalidRedPacket			:HOST + 'redpacketController/invalidRedPacket',
    //批量删除红包
    deleteRedPacket			:HOST + 'redpacketController/deleteRedPacket',
	//会员待遇列表
	vipTreatmentList			:HOST + 'vipTreatmentController/vipTreatmentList',
	//添加会员待遇
    addVipTreatment			:HOST + 'vipTreatmentController/addVipTreatment',
	//更新会员待遇
	updateVipTreatment		:HOST + 'vipTreatmentController/updateVipTreatment',
	//获取会员成长值记录
    vipGrowRecordList			:HOST + 'vipGrowRecordController/vipGrowRecordList',
	//获取会员成长值详情
    userGrowRecordDetail		:HOST + 'vipGrowRecordController/userGrowRecordDetail',
	//获取会员待遇的卡券产品列表
    getCouponProduct			:HOST + 'vipTreatmentController/getCouponProduct'

},CONFIG.interface || {});

/**财务管理路由*/
CONFIG.interface = jQuery.extend({
	//提现放款审核列表
	searchWithdrawalsLoan            	: HOST + 'withdrawalsController/searchWithdrawalsLoan',
	//提现审核列表
	searchWithdrawalsAudit				: HOST + 'withdrawalsController/searchWithdrawalsAudit',
	//获取交易信息
	getPayRecord				: HOST + 'withdrawalsController/getPayRecord',
	//提现放款
	loanWithdrawals				: HOST + 'withdrawalsController/loanWithdrawals',
	//提现审核
	auditWithdrawals				: HOST + 'withdrawalsController/auditWithdrawals',
	//提现放款拒绝
	loanRejectWithdrawals				: HOST + 'withdrawalsController/loanRejectWithdrawals',
	//提现拒绝
	auditRejectWithdrawals				: HOST + 'withdrawalsController/auditRejectWithdrawals'

}, CONFIG.interface || {});

/**卡券管理路由**/
CONFIG.interface = jQuery.extend({
    //获取卡券产品列表
    couponProductList				:HOST + 'couponProductController/couponProductList',
	//添加卡券产品
	addCouponProduct				:HOST + 'couponProductController/addCouponProduct',
	//修改卡券产品
	updateCouponProduct			    :HOST + 'couponProductController/updateCouponProduct',
	//禁用卡券产品
	disableCouponProduct			:HOST + 'couponProductController/disableCouponProduct',
	//获取可赠送卡券产品列表
	giveCouponProductList			:HOST + 'couponProductController/giveCouponProductList',
	//赠送用户户卡券
    distributeCouponToUser		     :HOST + 'couponDetailController/distributeCouponToUser',
	//查询卡券详情列表
	couponDetailList				:HOST + 'couponDetailController/couponDetailList',
	//查看卡券转赠记录列表
	couponDonationRecordList		:HOST + 'couponDetailController/couponDonationRecordList',
	//生成卡券详情列表
    generateCouponDetailList		:HOST + 'couponDetailController/generateCouponDetailList'
}, CONFIG.interface || {});

/**系统管理路由**/
CONFIG.interface = jQuery.extend({
    refreshAuthAndMenu		:HOST + 'authController/refreshAuthAndMenu',
    userIsLogin				:HOST + 'commonsController/userIsLogin',
    listAllMenus			:HOST + 'menuController/listAllMenus',
    listAppMenus			:HOST + 'appMenuController/listAppMenus',
    listAppServes			:HOST + 'appMenuController/listAppServes',
    listChildMenus			:HOST + 'menuController/listChildMenus',
    addMenu				    :HOST + 'menuController/addMenu',
    addAppMenu				:HOST + 'appMenuController/addAppMenu',
    addAppServe				:HOST + 'appMenuController/addAppServe',
    updateMenu				:HOST + 'menuController/updateMenu',
    updateAppMenu			:HOST + 'appMenuController/updateAppMenu',
    updateAppServe			:HOST + 'appMenuController/updateAppServe',
    bindPrisToMenu			:HOST + 'menuController/bindPrisToMenu',
    listPrivileges			:HOST + 'privilegeController/listPrivileges',
    savePrivilege			:HOST + 'privilegeController/savePrivilege',
    scannedPrivileges		:HOST + 'privilegeController/scannedPrivileges',
    listPrivilegesNoPager	:HOST + 'privilegeController/listPrivilegesNoPager',
    listAllMenusNoPager		:HOST + 'menuController/listAllMenusNoPager',
    findAllRolesNoPager		:HOST + 'roleController/findAllRolesNoPager',
    findPrivigeIdBindToMenu	:HOST + 'menuController/findPrivigeIdBindToMenu',
    findMenuBindToRole		:HOST + 'roleController/findMenuBindToRole',
    findAllRoles			:HOST + 'roleController/findAllRoles',
    listAllOperators		:HOST + 'operatorManageController/listAllOperators',
    addRole				    :HOST + 'roleController/saveRole',
    bindMenuToRole			:HOST + 'roleController/bindMenuToRole',
    updateRole				:HOST + 'roleController/updateRole',
    findRoleBindToUser		:HOST + 'operatorManageController/findRoleBindToUser',
    bindRolesToUser			:HOST + 'operatorManageController/bindRolesToUser',
    addOperator				:HOST + 'operatorManageController/addOperator',
    changePasswd			:HOST + 'operatorManageController/changePasswd',
    updateOperator			:HOST + 'operatorManageController/updateOperator',
    userRolesList           :HOST + 'roleController/userRolesList',
    delUserRoles            :HOST +  'roleController/delUserRoles',
    addUserRoles            :HOST +  'roleController/addUserRoles',
    myAccountRoles          :HOST + 'roleController/myAccountRoles'
}, CONFIG.interface || {});

/**监控管理*/
CONFIG.interface = jQuery.extend({
	//jms异常消息监控列表
	jmsFailMsgList     				: HOST + 'jmsMonitorController/jmsFailMsgList',
	// jms异常消息恢复
	recover			  				: HOST + 'jmsMonitorController/recover',
    // jms删除异常消息
    del			  				    : HOST + 'jmsMonitorController/del',
	//管理员操作日志列表
	actionLogList					: HOST + 'actionLogController/actionLogList'
}, CONFIG.interface || {});

/**app版本更新规则管理**/
CONFIG.interface = jQuery.extend({
    //app版本更新规则列表
    appVersionRuleList            : HOST + 'appVersionController/appVersionRuleList',
    //添加app版本更新规则
    addAppVersionRule             : HOST + 'appVersionController/addAppVersionRule',
    //app版本规则上线
    upAppVersionRule              : HOST + 'appVersionController/upAppVersionRule',
    //app版本规则下线
    downAppVersionRule            : HOST + 'appVersionController/downAppVersionRule'
}, CONFIG.interface || {});

/** 股权中心**/
CONFIG.interface = jQuery.extend({
    //股权类型列表
    fundTypeList                  : HOST + 'fundAdvisoryController/fundTypeList',
    //股权类型详情
    fundProjectInfo               : HOST + 'fundAdvisoryController/findFundProjectInfo',
    //股权咨询列表
    fundAdvisoryList              : HOST + 'fundAdvisoryController/fundAdvisoryList',
    //股权咨询详情
    findFundAdvisoryInfo          : HOST + 'fundAdvisoryController/findFundAdvisoryInfo',
    //保存咨询信息
    saveFundAdvisory        	  : HOST + 'fundAdvisoryController/save',
    //更新咨询信息
    updateFundAdvisory        	  : HOST + 'fundAdvisoryController/update',
    //股权项目列表
    searchFundInfoList            : HOST + 'fundInfoController/searchFundInfoList',
    //股权项目添加
    addFundInfo            		  : HOST + 'fundInfoController/addFundInfo',
    //股权项目更新
    updateFundInfo            	  : HOST + 'fundInfoController/updateFundInfo',
    //股权项目改变状态
    updateFundInfoState           : HOST + 'fundInfoController/updateFundInfoState',
    //股权项目预约
    updateFundInfoSubscribeState  : HOST + 'fundInfoController/updateFundInfoSubscribeState',
    //股权项目查询
    findFundInfoById              : HOST + 'fundInfoController/findFundInfoById',
    //风险测评答案查询
    fundEvaluationList            : HOST + 'fundEvaluationController/fundEvaluationList',
    //风险测评答案详情
    fundEvaluationDetail          : HOST + 'fundEvaluationController/fundEvaluationDetail',
    //海外基金审核
    fundAgreementAudit            : HOST + 'fundAdvisoryController/fundAgreementAudit',
    //海外基金下载
    fundAgreementDownLoad         : HOST + 'fundAdvisoryController/fundAgreementDownLoad'
}, CONFIG.interface || {});

/**统计管理路由*/
CONFIG.interface = jQuery.extend({
    //收入统计查询
    staIncomeList				: HOST + 'staIncomeController/staIncomeList',
    balanceList                 : HOST + 'balAccountController/balanceList',
    exportExcelForStaIncomeList : HOST + 'staIncomeController/exportExcelForStaIncomeList',
    //还款计划
    staRepayPlanList			: HOST + 'staRepayReceiptController/repayPlanList',
    //回款计划
    staReceiptPlanList			: HOST + 'staRepayReceiptController/receiptPlanList',
    //回款计划详情列表
    staReceiptPlanListInfos		: HOST + 'staRepayReceiptController/staReceiptPlanListInfos'

}, CONFIG.interface || {});


/** 活动服务**/
CONFIG.interface = jQuery.extend({
    //抽奖活动管理
    lotteryActivityList                  : HOST + 'lotteryActivityController/findLotteryActivityList',
    //抽奖活动详情
    lotteryActivityDetail                : HOST + 'lotteryActivityController/getLotteryActivityDetail',
    //更新抽奖活动
    updateLotteryActivity                : HOST + 'lotteryActivityController/updateLotteryActivity',
    //保存抽奖活动
    saveLotteryActivity                  : HOST + 'lotteryActivityController/saveLotteryActivity',
    //保存抽奖活动奖项
    saveLotteryActivityItems             : HOST + 'lotteryActivityController/saveLotteryActivityItems',
    //获取抽奖活动奖项
    getLotteryActivityItems             : HOST + 'lotteryActivityController/getLotteryActivityItems',
    //抽奖记录列表
    lotteryRecordList                    : HOST + 'lotteryActivityController/lotteryRecordList'

}, CONFIG.interface || {});

/** 房产投资服务**/
CONFIG.interface = jQuery.extend({
    //房产信息管理
    houseList                            : HOST + 'houseController/houseList',
    //添加房产信息
    addHouse                             : HOST + 'houseController/addHouse',
    //修改房产信息
    updateHouse                          : HOST + 'houseController/updateHouse',
    //获取房产信息
    findHouse                            : HOST + 'houseController/findHouse',
    //删除房产预售证信息
    deleteHousePermit                    : HOST + 'houseController/deleteHousePermit',
    //保存房产预售证信息
    saveHousePermit                      : HOST + 'houseController/saveHousePermit',
    //删除房产图片记录及oss端图片
    deleteHousePic                       : HOST + 'houseController/deleteHousePic',
    //保存房产图片记录
    saveHousePic                         : HOST + 'houseController/saveHousePic'

}, CONFIG.interface || {});





/**字典常量**/
var DIC_CONSTANT = {"data":{}};
(function () {
    /**初始化数据字典**/
    DIC_CONSTANT.init = function () {
        if (!containerUtil.get("DIC_CONSTANT")){
            $.ajax({
                url : CONFIG.interface.dicList,
                type : 'POST',
                dataType : 'JSON',
                async : true,
                success : function(data) {
                    DIC_CONSTANT.data = data;
                    containerUtil.set("DIC_CONSTANT",data);
                }
            });
        }else {
            DIC_CONSTANT.data = containerUtil.get("DIC_CONSTANT");
        }
    };
    DIC_CONSTANT.getName = function(businessName, subjectName, value){
        var arr = DIC_CONSTANT.getValueAndName(businessName, subjectName);
        var name = value + "(未定义)";
        for(var i in arr){
            var o = arr[i];
            if(o.value == value){
                name = o.name;
                break;
            }
        }
        return name;
    };
    DIC_CONSTANT.getValue = function (businessName, subjectName, name) {
        var arr = DIC_CONSTANT.getValueAndName(businessName, subjectName);
        var value = "";
        for (var i in arr){
            var o = arr[i];
            if (o.name == name){
                value = o.value;
                break;
            }
        }
        return value;
    }
    DIC_CONSTANT.getNames = function(businessName, subjectName, values,splitSign){
        if(commonUtil.isEmpty(splitSign)){
            splitSign = ","
        }
        var showValue =  values.split(splitSign);
        var name = '' ;
        for(var i in DIC_CONSTANT.data){
            var o = DIC_CONSTANT.data[i];
            if(o.businessName == businessName && o.subjectName == subjectName)
            {
                for(var j in showValue){
                    var v = showValue[j];
                    if(o.value==v){
                        if(name==''){
                            name = o.name;
                        }else{
                            name = name +splitSign+ o.name;
                        }
                    }
                }
            }
        }
        return name;
    };
    /**
     * 根据limitValues来获取字典中的值
     * @param businessName
     * @param subjectName
     * @param limitValues
     * @returns {Array}
     */
    DIC_CONSTANT.getDicObjInLimitValues = function(businessName, subjectName, limitValues){
        var showValue =  limitValues.split(",");
        var objs=[];
        for(var i in DIC_CONSTANT.data){
            var o = DIC_CONSTANT.data[i];
            if(o.businessName == businessName && o.subjectName == subjectName)
            {
                for(var j in showValue){
                    var v = showValue[j];
                    if(o.value==v){
                        objs.push(o);
                    }
                }
            }
        }
        return objs;

    }
    //通过字典表初始化option
    DIC_CONSTANT.getOption = function(businessName, subjectName,valueName,limitValue,limitFalg/*正向判断或者反向判断，默认正向*/,needAllOption/*是否需要【全部】选项*/){
        if(limitFalg==null){
            limitFalg=true;
        }
        var option ="";
        var showValue=null;
        if(limitValue!=null){
            limitValue += '';
            showValue = limitValue.split(",");
        }
        if(needAllOption==null){
            needAllOption=true;
        }
        if(needAllOption){
            if(commonUtil.isEmpty(valueName)){
                option ="<option value='-999'>全部</option>";
            }else if(valueName=="empty"){
                option ="<option value=''  >全部</option>";
            }else{
                option="<option value='"+valueName+"' >全部</option>";
            }
        }

        for(var i in DIC_CONSTANT.data){
            var o = DIC_CONSTANT.data[i];
            if(o.businessName == businessName && o.subjectName == subjectName){
                if(limitValue==null){
                    option += "<option value='" + o.value + "'>" + o.name + "</option>";
                }else{
                    if(showValue!=null&&showValue.length>0){
                        var showAdd=false;
                        var findFlag=$.inArray(o.value+'', showValue)!=-1;
                        if((limitFalg&&findFlag)||(!limitFalg&&!findFlag)){
                            showAdd=true;
                        }
                        if(showAdd) {
                            option += "<option value='" + o.value + "'>" + o.name + "</option>";
                        }
                    }
                }

            }
        }
        return option;
    };
    /**
     * 通过字典表初始化下拉菜单
     * @param name select元素的name值
     * @param businessName 服务标识
     * @param subjectName 业务标识
     */
    DIC_CONSTANT.initSelect = function(name, businessName, subjectName){
        $("select[name='" + name + "']").append(DIC_CONSTANT.getOption(businessName, subjectName));
    };

    DIC_CONSTANT.getOptionByUrl = function(url){
        var option ="<option  value=\"\">=请选择=</option>";
        $.ajax({
            url : url,
            type : 'POST',
            dataType : 'JSON',
            async : false,
            success : function(data) {
                var ll = data.resMsg;
                for(var i in ll){
                    var o = ll[i];
                    option += "<option value='" + o.value + "'>" + o.name + "</option>";
                }
            }
        });
        return option;
    };

    DIC_CONSTANT.initSelectByUrl = function(name,url){
        $("select[name='" + name + "']").append(DIC_CONSTANT.getOptionByUrl(url));
    };

    DIC_CONSTANT.getValueAndName = function(businessName, subjectName){
        var valueNameArray=[];
        for(var i in DIC_CONSTANT.data){
            var o = DIC_CONSTANT.data[i];
            var valueNameObj={};
            if(o.businessName == businessName && o.subjectName == subjectName){
                valueNameObj.value=o.value;
                valueNameObj.name=o.name;
                valueNameArray.push(valueNameObj);
            }
        }
        return valueNameArray;
    };
}());

/**
 * 区域字典表
 * @type {{data: {}}}
 */
var AREA_CONSTANT = {"data":{}};
(function () {
    /**初始化区域字典数据**/
    AREA_CONSTANT.init = function () {
        if (!containerUtil.get("AREA_CONSTANT")){
            $.ajax({
                url : CONFIG.interface.dicAreaList,
                type : 'POST',
                dataType : 'JSON',
                async : true,
                success : function(data) {
                    AREA_CONSTANT.data = data;
                    containerUtil.set("AREA_CONSTANT", data);
                }
            });
        }else{
            AREA_CONSTANT.data = containerUtil.get("AREA_CONSTANT")
        }
    };
    /**
     * 获得省、市、县
     * @param parentCode
     * @param type 1：省，2：市，3：县，默认 省
     * @returns {Array}
     */
     AREA_CONSTANT.getData = function(parentCode){
        if(parentCode == -1){
            return [];
        }
        var arr = [];
        var areaCode = -1;
        for(var i in AREA_CONSTANT.data){
            var o = AREA_CONSTANT.data[i];
            if(o.areaName == parentCode){
                areaCode = o.areaCode;
                break;
            }
        }
        for(var i in AREA_CONSTANT.data){
            var o = AREA_CONSTANT.data[i];
            if(o.parentCode == parentCode || o.parentCode == areaCode){
                if(commonUtil.isNotEmpty(o.areaName)){
                    arr.push(o);
                }
            }
        }
        return arr;
    };
    /**
     * 根据市名称获取区/县的列表记录
     * @param cityName
     * @returns {Array}
     */
    AREA_CONSTANT.getCountysByCityName = function(cityName){
        if (commonUtil.isEmpty(cityName)){
            return [];
        }
        var arr = [];
        var areaCode = -1;
        for(var i in AREA_CONSTANT.data){
            var o = AREA_CONSTANT.data[i];
            if(o.areaName == cityName && o.grade == 2){
                areaCode = o.areaCode;
                break;
            }
        }
        for(var i in AREA_CONSTANT.data){
            var o = AREA_CONSTANT.data[i];
            if(o.parentCode == areaCode && o.grade == 3){
                if(commonUtil.isNotEmpty(o.areaName)){
                    arr.push(o);
                }
            }
        }
        return arr;
    }

	AREA_CONSTANT.getAreaName = function(areaCode){
		var arr = AREA_CONSTANT.data;
		for(var i in arr){
			var o = arr[i];
			if(o.areaCode == areaCode){
				return o.areaName;
			}
		}
		return "";
	};
}());

//合同类型常量类
var CONTRACTTYPE_CONSTANT = {};
(function () {
    CONTRACTTYPE_CONSTANT.init = function () {
        //containerUtil已保存合同类型则从容器中获取，否则请求后台获取
        if (!containerUtil.get("contractTypeAndName")){
            $.ajax({
                url : CONFIG.interface.getContractTypeAndName,
                type : 'POST',
                dataType : 'JSON',
                async : true,
                success : function(data) {
                    CONTRACTTYPE_CONSTANT.data = data;
                    //将合同类型后台显示名称数据集合添加到containerUtil中，以减少请求后台次数
                    containerUtil.set("contractTypeAndName",data);
                }
            })
        }else {
            CONTRACTTYPE_CONSTANT.data = containerUtil.get("contractTypeAndName");
        }
    };
    CONTRACTTYPE_CONSTANT.getName = function (type) {
        var name = type + "(未定义)";
        for(var i in CONTRACTTYPE_CONSTANT.data){
            var o = CONTRACTTYPE_CONSTANT.data[i];
            if(o.type == type){
                name = o.name;
                return name;
            }
        }
        return name;
    };
    CONTRACTTYPE_CONSTANT.getShowName = function (type) {
        var name = type + "(未定义)";
        for(var i in CONTRACTTYPE_CONSTANT.data){
            var o = CONTRACTTYPE_CONSTANT.data[i];
            if(o.type == type){
                name = o.showName;
                return name;
            }
        }
        return name;
    };
    //根据状态获取所需要的数据，0-禁用状态，1-启用状态，
    CONTRACTTYPE_CONSTANT.getData = function (state) {
        var data = new Array();
        for (var i in CONTRACTTYPE_CONSTANT.data){
            var o = CONTRACTTYPE_CONSTANT.data[i];
            if (o.state == state){
                data.push(CONTRACTTYPE_CONSTANT.data[i]);
            }
        }
        return data;
    };
}());

var BANK_NAMES = [
	{name : "中国工商银行", value :"ICBC"},
	{name : "中国农业银行", value :"ABC"},
	{name : "中国建设银行", value :"CCB"},
	{name : "招商银行", value :"CMB"},
	{name : "浦发银行", value :"SPDB"},
	{name : "中国银行", value :"BOC"},
	{name : "交通银行", value :"BCOM"},
	{name : "中信银行", value :"CITIC"},
	{name : "光大银行", value :"CEB"},
	{name : "广发银行", value :"GDB"},
	{name : "平安银行", value :"PAB"},
	{name : "华夏银行", value :"HXB"},
	{name : "民生银行", value :"CMBC"},
	{name : "邮储银行", value :"PSBC"},
	{name : "北京银行", value :"BJB"},
	{name : "兴业银行", value :"CIB"},
	{name : "上海银行", value :"SHB"},
	{name : "杭州银行", value :"HZB"},
	{name : "浙商银行", value :"ZSB"},
	{name : "宁波银行", value :"NBCB"},
	{name : "东亚银行", value :"BEA"}
];


