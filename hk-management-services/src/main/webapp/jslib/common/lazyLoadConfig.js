/**
 * 各个功能页面需要动态加载的js文件
 * create by xuhui.liu 
 */
var LazyLoadConfig={};
// 版本号管理
LazyLoadConfig.VERSION=20180525

LazyLoadConfig.CONSTANTS={
	LOAD_JS_OPERATEBIDINFO              		:["pages/controller/bidInfo/operateBidInfoController.js?_v="+LazyLoadConfig.VERSION],//标的新增、修改
	INFO_JS_FRIENDURL                   		:["pages/controller/info/friendlyurlController.js?_v="+LazyLoadConfig.VERSION],//资讯友情链接
	INFO_JS_FRIENDURLDETAIL                     :["pages/controller/info/friendlyDetailController.js?_v="+LazyLoadConfig.VERSION],//资讯友情链接查看
	INFO_JS_CAROUSELFIGURE                      :["pages/controller/info/carouselFigureController.js?_v="+LazyLoadConfig.VERSION],//资讯广告轮播新增、修改
	INFO_JS_CAROUSELFIGUREDETAIL                :["pages/controller/info/carouselFigureDetailController.js?_v="+LazyLoadConfig.VERSION],//资讯广告轮播查看
	INFO_JS_NEWS                        		:["pages/controller/info/newsController.js?_v="+LazyLoadConfig.VERSION],//资讯新闻管理
	INFO_JS_QUESTIONNAIRE                       :["pages/controller/info/questionnaireController.js?_v="+LazyLoadConfig.VERSION],//资讯新闻管理-新增调查问卷
	INFO_JS_UPDATE_QUESTIONNAIRE                       :["pages/controller/info/updateQuestionnaireController.js?_v="+LazyLoadConfig.VERSION],//资讯新闻管理-调查问卷修改
	INFO_JS_NOTICE                      		:["pages/controller/info/noticeController.js?_v="+LazyLoadConfig.VERSION],//资讯公告管理
	LOAD_JS_BIDINFODETAIL              			:["pages/controller/bidInfo/bidInfoDetailController.js?_v="+LazyLoadConfig.VERSION],//标的详情查看
    INFO_JS_NOTICEDETAIL                		:["pages/controller/info/noticeDetailController.js?_v="+LazyLoadConfig.VERSION],//资讯公告详情查看
    INFO_JS_NEWSDETAIL                  		:["pages/controller/info/newsDetailController.js?_v="+LazyLoadConfig.VERSION],//资讯新闻详情查看
	//LOAD_JS_BIDINFODETAIL              			:["pages/controller/bidInfo/bidInfoDetailController.js?_v="+LazyLoadConfig.VERSION], //标的详情查看
	LOAD_JS_BIDPRODUCT              			:["pages/controller/bidInfo/bidProductController.js?_v="+LazyLoadConfig.VERSION], //标的产品
	LOAD_JS_POINTRULE              	    		:["pages/controller/pointRule/pointRuleController.js?_v="+LazyLoadConfig.VERSION], //积分规则的新增、修改
	LOAD_JS_CREDITORLIST                        :["pages/controller/bidInfo/creditorListController.js?_v="+LazyLoadConfig.VERSION], //标的设置债权转让
	LOAD_JS_MATCHBIDDETAIL                      :["pages/controller/bidInfo/matchBidDetailController.js?_v="+LazyLoadConfig.VERSION], //匹配标的详情
	LOAD_JS_MATCHEDDETAILS						:["pages/controller/bidInfo/matchedDetailsController.js?_v="+LazyLoadConfig.VERSION], //匹配标的详情
    LOAD_JS_INVEST_EXCHANGE						:["pages/controller/bidInfo/investExchangeController.js?_v="+LazyLoadConfig.VERSION], //交易所标的匹配
	LOAD_JS_SYS_MENU     						:["pages/controller/menuAndAuth/sysMenus.js?_v="+LazyLoadConfig.VERSION], //菜单管理
	LOAD_JS_APP_SERVES     						:["pages/controller/menuAndAuth/appServes.js?_v="+LazyLoadConfig.VERSION], //菜单管理
	LOAD_JS_SYS_MENU_BIND_PRI     				:["pages/controller/menuAndAuth/menuBindPri.js?_v="+LazyLoadConfig.VERSION], //菜单管理
	LOAD_JS_SYS_ROLES     						:["pages/controller/menuAndAuth/roles.js?_v="+LazyLoadConfig.VERSION], //角色管理
	LOAD_JS_OPERATOR_MANAGE   					:["pages/controller/menuAndAuth/operatorManage.js?_v="+LazyLoadConfig.VERSION], //后台用户管理
	LOAD_JS_SYS_PRIVILEGE     					:["pages/controller/menuAndAuth/privilege.js?_v="+LazyLoadConfig.VERSION], //权限管理
	LOAD_JS_BID_PRODUCT							:["pages/controller/bidInfo/bidProductController.js?_v="+LazyLoadConfig.VERSION] ,//标的产品
	LOAD_JS_CREDITOR_PORPERTY              		:["pages/controller/bidInfo/creditorPorpertyController.js?_v="+LazyLoadConfig.VERSION],//标的新增、修改
    LOAD_JS_USER_ROLES		              		:["pages/controller/menuAndAuth/userRolesController.js?_v="+LazyLoadConfig.VERSION]//标的新增、修改


};

/**用户详情**/
LazyLoadConfig.CONSTANTS = jQuery.extend({
	//用户详情
	LOAD_JS_USER_DETAIL	              	:["pages/controller/user/userDetailController.js?_v="+LazyLoadConfig.VERSION],
	LOAD_JS_USER_ACCOUNT_MONEY	              	:["pages/controller/user/userAccountMoneyController.js?_v="+LazyLoadConfig.VERSION],
	//小区维护
	LOAD_JS_COMMUNITY_CONTROLLER	              	:["pages/controller/user/communityController.js?_v="+LazyLoadConfig.VERSION],
    //绑定小区到物业账户
    LOAD_JS_TENEMENT_BIND_COMMUNITIES	              	:["pages/controller/user/tenementBindCommunities.js?_v="+LazyLoadConfig.VERSION],
	//企业账户
	LOAD_JS_COMPANY_USER	              	:["pages/controller/user/companyUserController.js?_v="+LazyLoadConfig.VERSION],
    //用户资料审核
    LOAD_JS_COMPANY_USER_AUDIT	              	:["pages/controller/user/userAuditController.js?_v="+LazyLoadConfig.VERSION]
}, LazyLoadConfig.CONSTANTS || {});

/**钱袋子管理**/
LazyLoadConfig.CONSTANTS = jQuery.extend({
	//当前债权
	LOAD_JS_QDZ_CREDITORINFO	              	:["pages/controller/qdz/creditorInfoController.js?_v="+LazyLoadConfig.VERSION],
	//钱袋子规则增加修改
	LOAD_JS_QDZ_RULE                :["pages/controller/qdz/operateQdzRuleController.js?_v="+LazyLoadConfig.VERSION],
	//钱袋子推荐奖规则增加修改
	LOAD_JS_QDZ_RULE_RECOMMEND_EARN    :["pages/controller/qdz/operateQdzRecommendEarnRuleController.js?_v="+LazyLoadConfig.VERSION],
	//钱袋子债权池设置规则
    LOAD_JS_QDZ_RULE_CREDITOR        :["pages/controller/qdz/operateQdzCreditorRuleController.js?_v="+LazyLoadConfig.VERSION]
}, LazyLoadConfig.CONSTANTS || {});

/**支付管理**/
LazyLoadConfig.CONSTANTS = jQuery.extend({
	//支付模式详情
	LOAD_JS_PAYMENT_PAYCHANNELDETAIL	              	:["pages/controller/payment/paymentchannelController.js?_v="+LazyLoadConfig.VERSION],
	//提现放款
	LOAD_JS_PAYMENT_WITHDRAWALSLOAN	              	:["pages/controller/payment/withdrawalsLoanController.js?_v="+LazyLoadConfig.VERSION],
	//第三方银行维护
	LOAD_JS_PAYMENT_BANKREFER	              	:["pages/controller/payment/payBankController.js?_v="+LazyLoadConfig.VERSION],
	//提现审核
	LOAD_JS_PAYMENT_WITHDRAWALSAUDIT	              	:["pages/controller/payment/withdrawalsAuditController.js?_v="+LazyLoadConfig.VERSION],
    //解绑银行卡审核
    LOAD_JS_BANK_CARD_UPDATE_AUDIT	              	:["pages/controller/payment/bankCardUpdateAuditController.js?_v="+LazyLoadConfig.VERSION]
}, LazyLoadConfig.CONSTANTS || {});

/**合同管理**/
LazyLoadConfig.CONSTANTS = jQuery.extend({
	//添加或修改合同类型
	LOAD_JS_CONTRACTTYPE					:["pages/controller/contract/contractTypeController.js?_v="+LazyLoadConfig.VERSION], //合同类型的新增、修改
	LOAD_JS_CONTRACTTEMPLATE				:["pages/controller/contract/contractTemplateController.js?_v="+LazyLoadConfig.VERSION],//合同模板内容的新增、修改
	LOAD_JS_CONTRACTTEMPLATEDETAIL		:["pages/controller/contract/contractTemplateDetailController.js?_v="+LazyLoadConfig.VERSION],	//合同模板内容的查看
    CAR_INFO                 			:["pages/controller/contract/carInfoController.js?_v="+LazyLoadConfig.VERSION],//车辆信息新增，修改
    CAR_DETAIL                 			:["pages/controller/contract/carDetail.js?_v="+LazyLoadConfig.VERSION],//车辆信息查看
    CAR_CONTRACT                 		:["pages/controller/contract/carContractController.js?_v="+LazyLoadConfig.VERSION],//汽车合同添加
    CAR_CONTRACT_DETAIL					:["pages/controller/contract/carContractDetail.js?_v="+LazyLoadConfig.VERSION],//汽车合同查看
    CAR_CONTRACT_PRINT					:["pages/controller/contract/carContractPrintCtrl.js?_v="+LazyLoadConfig.VERSION],//汽车合同查看
    CAR_INFO_IMPORT						:["pages/controller/contract/carInfoImport.js?_v="+LazyLoadConfig.VERSION]//汽车信息导入
},LazyLoadConfig.CONSTANTS || {});

/**体验金规则**/
LazyLoadConfig.CONSTANTS = jQuery.extend({
	VAS_JS_SIMGOLD                  :["pages/controller/sim/simGoldController.js?_v="+LazyLoadConfig.VERSION],
	VAS_JS_GOLDRULE					:["pages/controller/sim/simGoldRuleController.js?_v="+LazyLoadConfig.VERSION],
	VAS_JS_GOLDIMPORT					:["pages/controller/sim/simGoldImportController.js?_v="+LazyLoadConfig.VERSION],
	VAS_JS_GOLDRULEDETAIL					:["pages/controller/sim/simGoldDetailController.js?_v="+LazyLoadConfig.VERSION]
},LazyLoadConfig.CONSTANTS || {});

/**app端消息推送**/
LazyLoadConfig.CONSTANTS = jQuery.extend({
	SMS_JS_APP_MSG_PUSH				:["pages/controller/sms/appSmsMsgPushController.js?_v="+LazyLoadConfig.VERSION]
},LazyLoadConfig.CONSTANTS || {});
/**黑白名单管理**/
LazyLoadConfig.CONSTANTS = jQuery.extend({
	//功能邮件or短信通知管理
    LOAD_JS_ROSNOTICE	              	:["pages/controller/ros/rosNoticeController.js?_v="+LazyLoadConfig.VERSION],
	//用户功能黑白名单
	LOAD_JS_ROSINFO	              	:["pages/controller/ros/rosInfoController.js?_v="+LazyLoadConfig.VERSION],
	//企业员工关系名单
	LOAD_JS_ROS_STAFF_INFO	              	:["pages/controller/ros/rosStaffInfoController.js?_v="+LazyLoadConfig.VERSION],
	//添加物业宝&购房宝名单
	LOAD_JS_ROS_DEPOSIT_INFO	              	:["pages/controller/ros/rosDepositInfoController.js?_v="+LazyLoadConfig.VERSION]
}, LazyLoadConfig.CONSTANTS || {});

/**会员管理**/
LazyLoadConfig.CONSTANTS = jQuery.extend({
	LOAD_JS_VIP_GRADE_STANDARD				:["pages/controller/vas/vipGradeStandardController.js?_v="+LazyLoadConfig.VERSION],//会员等级标准管理
	LOAD_JS_VIP_GROW_RULE						:["pages/controller/vas/vipGrowRuleController.js?_v="+LazyLoadConfig.VERSION],//会员成长值规则管理
	LOAD_JS_VIP_TREATMENT						:["pages/controller/vas/vipTreatmentController.js?_v="+LazyLoadConfig.VERSION]//会员待遇管理
},LazyLoadConfig.CONSTANTS || {});

/**好友推荐管理**/
LazyLoadConfig.CONSTANTS = jQuery.extend({
	//用户功能黑白名单
	 RECOMMEND_JS_RULE	              	:["pages/controller/recommend/addRecommendRuleController.js?_v="+LazyLoadConfig.VERSION],
     SPREAD_JS_RULE                    :["pages/controller/recommend/updateSpreadSourceController.js?_v="+LazyLoadConfig.VERSION]
}, LazyLoadConfig.CONSTANTS || {});

/**增值服务管理**/
LazyLoadConfig.CONSTANTS = jQuery.extend({
    //红包线下推广
    REDPACKET_OFFLINE	              	:["pages/controller/vas/redPacketOffLine.js?_v="+LazyLoadConfig.VERSION]
}, LazyLoadConfig.CONSTANTS || {});

/**积分模块管理*/
LazyLoadConfig.CONSTANTS = jQuery.extend({
    //红包线下推广
    POINT_ACCOUNT	              	:["pages/controller/point/pointController.js?_v="+LazyLoadConfig.VERSION],
    POINT_MERCHANT	              	:["pages/controller/point/pointMerchantController.js?_v="+LazyLoadConfig.VERSION]
}, LazyLoadConfig.CONSTANTS || {});

/**积分商城管理*/
LazyLoadConfig.CONSTANTS = jQuery.extend({
    POINT_PRODUCT_CATEGORY	              	:["pages/controller/pointmall/pointProductCategory.js?_v="+LazyLoadConfig.VERSION],
    POINT_PRODUCT_PART	              	:["pages/controller/pointmall/pointProdutPart.js?_v="+LazyLoadConfig.VERSION],
    PRODUCT_TO_PART	              	:["pages/controller/pointmall/productToPart.js?_v="+LazyLoadConfig.VERSION],
    POINT_PRODUCT	              	:["pages/controller/pointmall/pointProduct.js?_v="+LazyLoadConfig.VERSION],
    POINT_PRODUCT_ORDER	              	:["pages/controller/pointmall/pointProductOrder.js?_v="+LazyLoadConfig.VERSION],
    POINT_PRODUCT_RECORD	              	:["pages/controller/pointmall/pointProductDetailController.js?_v="+LazyLoadConfig.VERSION]

}, LazyLoadConfig.CONSTANTS || {});


/**导入文件公共js**/
LazyLoadConfig.CONSTANTS = jQuery.extend({
	LOAD_JS_IMPORT_FILE	              	:["pages/controller/importController.js?_v="+LazyLoadConfig.VERSION]
}, LazyLoadConfig.CONSTANTS || {});

/**卡券模块管理**/
LazyLoadConfig.CONSTANTS = jQuery.extend({
	COUPON_PRODUCT					:["pages/controller/vas/coupon/couponProductController.js?_v="+LazyLoadConfig.VERSION],
	COUPON_DETAIL					:["pages/controller/vas/coupon/couponDetailController.js?_v="+LazyLoadConfig.VERSION]
}, LazyLoadConfig.CONSTANTS || {});

/**app版本更新规则管理**/
LazyLoadConfig.CONSTANTS = jQuery.extend({
	APP_VERSION_RULE                 :["pages/controller/vas/appVersionRuleController.js?_v="+LazyLoadConfig.VERSION]
}, LazyLoadConfig.CONSTANTS || {});


/**股权咨询信息维护**/
LazyLoadConfig.CONSTANTS = jQuery.extend({
    FUND_ADVISORY                 :["pages/controller/fund/fundAdvisoryController.js?_v="+LazyLoadConfig.VERSION],
	FUND_PROJECT                  :["pages/controller/fund/fundProjectController.js?_v="+LazyLoadConfig.VERSION],
	FUND_ADVISORY_DETAIL           :["pages/controller/fund/fundAdvisoryDetail.js?_v="+LazyLoadConfig.VERSION]
}, LazyLoadConfig.CONSTANTS || {});

/**股权项目管理**/
LazyLoadConfig.CONSTANTS = jQuery.extend({
	FUND_INFO                 :["pages/controller/fund/fundInfoController.js?_v="+LazyLoadConfig.VERSION],
	FUND_DETAIL                 :["pages/controller/fund/fundDetail.js?_v="+LazyLoadConfig.VERSION],
	FUND_EVALUATION_DETAIL                 :["pages/controller/fund/fundEvaluationDetail.js?_v="+LazyLoadConfig.VERSION]
}, LazyLoadConfig.CONSTANTS || {});

/**活动管理**/
LazyLoadConfig.CONSTANTS = jQuery.extend({
    LOTTERY_ACTIVITY                 :["pages/controller/activity/lotteryActivityController.js?_v="+LazyLoadConfig.VERSION],
    LOTTERY_ITEMS                    :["pages/controller/activity/lotteryItemsController.js?_v="+LazyLoadConfig.VERSION]
}, LazyLoadConfig.CONSTANTS || {});

/**房产信息管理**/
LazyLoadConfig.CONSTANTS = jQuery.extend({
	HOUSE                         :["pages/controller/house/houseController.js?_v=" + LazyLoadConfig.VERSION]
}, LazyLoadConfig.CONSTANTS || {});