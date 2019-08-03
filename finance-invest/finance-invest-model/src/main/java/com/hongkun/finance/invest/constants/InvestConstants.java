package com.hongkun.finance.invest.constants;

import java.util.Arrays;
import java.util.List;

/**
 * @Description : 投资服务常量管理
 * @Project : finance-invest-model
 * @Program Name : com.hongkun.finance.invest.constants.InvestContants.java
 * @Author : xuhuiliu@hongkun.com.cn 刘旭辉
 */
public class InvestConstants {

	// ###########################################################################################
	// ###数据字典
	// ###########################################################################################

	/* 投资模块bussiness_name */
	public static final String INVEST = "invest";
	/* 还款方式字典key */
	public static final String REPAYMENT_MODE = "bid_repayment";
	/* 招标方案 */
	public static final String LOAN_SCHEME = "loan_scheme";
	/* 合同类型 */
	public static final String CONTRACT_TYPE = "contract_type";
	/* 产品类型 */
	public static final String BID_PRODUCT = "bid_product";
	/* 产品状态 */
	public static final String PRODUCT_STATE = "product_state";
	/* 风控方式 */
	public static final String ASSURE_TYPE = "assure_type";
	/* 借款用途 */
	public static final String LOAN_USE = "bid_loan_use";
	/* 借款周期 */
	public static final String TERM_UNIT = "bid_term_unit";

	// ###########################################################################################
	// ###投资 & 债权转让
	// ###########################################################################################

	/** 新手标最大投资金额 默认值：56800 */
	public static final int INVEST_FLEDGLING_INVEST_MAX_ATM = 56800;
	/** 新手标建议起投金额 默认值：5000 */
	public static final int INVEST_PREFERRED_RECOMMEND_ATM = 5000;

	/** 投资状态：投资删除 */
	public static final int INVEST_STATE_DEL = 9;
	/** 投资状态：投资成功 */
	public static final int INVEST_STATE_SUCCESS = 1;
	/** 投资状态：已转让（手动） */
	public static final int INVEST_STATE_MANUAL = 2;
	/** 投资状态：已转让（自动） */
	public static final int INVEST_STATE_AUTO = 3;
	/** 投资状态：转让中 */
	public static final int INVEST_STATE_TRANSFER = 4;
	/** 投资状态：购买成功（手动转让）*/
	public static final int INVEST_STATE_SUCCESS_BUYER = 5;

	/** 投资方式 ： 手动 */
	public static final int BID_INVEST_TYPE_MANUAL = 1;
	/** 投资方式 ： 自动 */
	public static final int BID_INVEST_TYPE_AUTO = 2;
	/** 投资方式 ： 钱袋子 */
	public static final int BID_INVEST_TYPE_QDZ = 3;

	/** 投资记录转让状态：不允许转让 */
	public static final int INVEST_TRANSSTATE_ALLOW = 1;
	/** 投资记录转让状态：允许转让 */
	public static final int INVEST_TRANSSTATE_NOTALLOW = 0;

	/** 债权转让 用户身份：转让人 */
	public static final int INVEST_USER_FLAG_TRANSFER = 1;
	/** 债权转让 用户身份：购买人 */
	public static final int INVEST_USER_FLAG_BUYER = 2;

	/** 手动债权转让状态：待转让【页面查询状态，非数据库状态】 */
	public static final int INVEST_TRANSFER_MANUAL_STATE_INIT = 0;
	/** 手动债权转让状态：转让中 */
	public static final int INVEST_TRANSFER_MANUAL_STATE_IN = 1;
	/** 手动债权转让状态：已转让 */
	public static final int INVEST_TRANSFER_MANUAL_STATE_OVER = 2;
	/** 手动债权转让状态：已失效 */
	public static final int INVEST_TRANSFER_MANUAL_STATE_LOSE = 3;

	/** 自动债权转让状态：已转让（通过手动转让产生） */
	public static final int INVEST_TRANSFER_AUTO_STATE_MANUAL = 1;
	/** 自动债权转让状态：已转让（通过匹配产生） */
	public static final int INVEST_TRANSFER_AUTO_STATE_MATCH = 2;

	/** 手动债权转让查询条件：新债权人投资记录id */
	public static final String INVEST_MANUAL_FIELD_NEWINVESTID = "new_invest_id";
	/** 手动债权转让查询条件：原债权人投资记录id */
	public static final String INVEST_MANUAL_FIELD_OLDINVESTID = "old_invest_id";

	// ###########################################################################################
	// ###标的 & 产品
	// ###########################################################################################

	/** 标的图片阿里云路径 */
	public static final String BIDINFO_FILE_PATH = "invest/bidInfo";

	/** 标的状态：待上架 */
	public static final int BID_STATE_WAIT_NEW = 1;
	/** 标的状态：投资中 */
	public static final int BID_STATE_WAIT_INVEST = 2;
	/** 标的状态：满标待审核 */
	public static final int BID_STATE_WAIT_AUDIT = 3;
	/** 标的状态：待放款 */
	public static final int BID_STATE_WAIT_LOAN = 4;
	/** 标的状态： 放款中 */
	public static final Integer BID_STATE_WAIT_REPAY = 5;
	/** 标的状态：已完成 */
	public static final int BID_STATE_WAIT_FINISH = 6;
	/** 标的状态： 满标审核拒绝 */
	public static final int BID_STATE_CHECK_REJECT = 8;
	/** 标的状态：标的已删除 */
	public static final int BID_STATE_DELETE = 9;

	/** 标的类型：正常标 **/
	public static final int BID_TYPE_COMMON = 0;
	/** 标的类型：爆款标 **/
	public static final int BID_TYPE_HOT = 1;
	/** 标的类型：推荐标 **/
	public static final int BID_TYPE_RECOMMEND = 2;

	/** 标的期限单位 ：天 */
	public static final int BID_TERM_UNIT_DAY = 3;
	/** 标的期限单位：月 */
	public static final int BID_TERM_UNIT_MONTH = 2;
	/** 标的期限单位：年 */
	public static final int BID_TERM_UNIT_YEAR = 1;

	/** 标的详情： 是否参与好友推荐--是 */
	public static final int BID_DETAIL_RECOMMEND_YES = 1;
	/** 标的详情： 是否参与好友推荐--否 */
	public static final int BID_DETAIL_RECOMMEND_NO = 0;
	/** 标的详情： 是否赠送积分--是 */
	public static final int BID_DETAIL_GIVE_POINT_YES = 1;
	/** 标的详情： 是否赠送积分--否 */
	public static final int BID_DETAIL_GIVE_POINT_NO = 0;
	/** 标的详情： 是否预留借款人利息--是 */
	public static final int BID_DETAIL_RESERVE_INTEREST_YES = 1;
	/** 标的详情： 是否预留借款人利息--否 */
	public static final int BID_DETAIL_RESERVE_INTEREST_NO = 0;
	/** 标的是否支持代扣：支持 **/
	public static final int IS_WITHHOLD_STATE = 1;
	/** 标的是否支持罚息：支持 **/
	public static int IS_PUNISH_STATE = 1;

	/** 标的是否是匹配标：直投 **/
	public static final int BID_INVEST_CHANNEL_IMMEDIATE = 1;
	/** 标的是否是匹配标：匹配 **/
	public static final int BID_INVEST_CHANNEL_MATCH = 2;
	/** 标的是否是匹配标：匹配 **/
	public static final int BID_INVEST_CHANNEL_QDZ = 3;

	/** 标的卡券支持 不支持 **/
	public static final int BID_ALLOW_COUPON_NO = 0;
	/** 标的卡券支持 不限制 **/
	public static final int BID_ALLOW_COUPON_YES = 1;
	/** 标的卡券支持 只支持加息券 **/
	public static final int BID_ALLOW_COUPON_RAISE_INTEREST = 2;
	/** 标的卡券支持 只支持投资红包 **/
	public static final int BID_ALLOW_COUPON_RED_PACKET = 3;

	/** 产品类型：普通产品 */
	public static final int BID_PRODUCT_COMMNE = 0;
	/** 产品类型： 新手标 */
	public static final int BID_PRODUCT_PREFERRED = 1;
	/** 产品类型： 月月赢 */
	public static final int BID_PRODUCT_WINMONTH = 2;
	/** 产品类型： 季季赢 */
	public static final int BID_PRODUCT_WINSEASON = 3;
	/** 产品类型： 年年赢 */
	public static final int BID_PRODUCT_WINYEAR = 4;
	/** 产品类型：体验金 */
	public static final int BID_PRODUCT_EXPERIENCE = 5;
	/** 产品类型：购房宝 */
	public static final int BID_PRODUCT_BUYHOUSE = 6;
	/** 产品类型：活期产品 */
	public static final int BID_PRODUCT_CURRENT = 7;
	/** 产品类型：活动产品 */
	public static final int BID_PRODUCT_ACTIVITY = 8;
	/** 产品类型：物业宝 */
	public static final int BID_PRODUCT_PROPERTY = 9;
	/** 产品类型：海外产品 */
	public static final int BID_PRODUCT_OVERSEA = 10;


	/** 标的类型：直投 */
	public static final int BID_MATCH_TYPE_NO = 0;
	/** 标的类型：匹配 */
	public static final int BID_MATCH_TYPE_YES = 1;

	/** 标的显示位置：PC */
	public static final int BID_SHOW_POSITION_PC = 1;
	/** 标的显示位置：APP */
	public static final int BID_SHOW_POSITION_APP = 2;

	/** 产品状态：删除 */
	public static final int PRODUCT_STATE_DELTE = 0;
	/** 产品状态：上架 */
	public static final int PRODUCT_STATE_ON = 1;
	/** 产品状态：下架 */
	public static final int PRODUCT_STATE_OFF = 2;

	/**
	 * 产品描述常量,含乾坤袋
	 */
	public static final String BID_PRODUCT_TYPE_DESC_QKD = "本项目为个人借款，借款人向乾坤袋或乾坤袋核准的合作方提交贷款申请，乾坤袋按照风控标准严格进行贷前调查及审批，并进行还款管理。\r";
	/**
	 * 产品描述常量,含优选
	 */
	public static final String BID_PRODUCT_TYPE_DESC_YX = "优选计划是乾坤袋推出的便捷高效的投标工具。该计划在用户认可的标的范围内，对符合要求的标的进行投标，目前该产品对接的项目主要为供应链金融以及有优质资产作为抵押、质押的融资标的。每月返息，到期还本付息，100元起投。\r";

	// ###########################################################################################
	// ###还款、回款
	// ###########################################################################################

	/** 还款方式：等额本息 */
	public static final int REPAYTYPE_PRINCIPAL_INTEREST_EQ = 1;
	/** 还款方式：按月付息，到期还本 */
	public static final int REPAYTYPE_INTEREST_MONTH_PRINCIPAL_END = 2;
	/** 还款方式：到期一次还本付息 */
	public static final int REPAYTYPE_ONECE_REPAYMENT = 3;
	/** 还款方式：到期付息，本金回收 */
	public static final int REPAYTYPE_INTEREST_END_PRINCIPAL_RECOVERY = 4;
	/** 还款方式：每月付息，到期本金回收 */
	public static final int REPAYTYPE_INTEREST_MONTH_PRINCIPAL_RECOVERY = 5;
	/** 还款方式：按月付息，本金划归企业 */
	public static final int REPAYTYPE_INTEREST_MONTH_PRINCIPAL_ENTERPRISE = 6;
    /** 还款方式：按月付息，按日计息 */
    public static final int REPAYTYPE_INTEREST_CAL_DAY_REPAY_MONTH = 7 ;
    
	/** 还本方式：按日计息 **/
	public static final int REPAYTYPE_CAT_WAY_DAY = 1;
	/** 还本方式：按月计息 **/
	public static final int REPAYTYPE_CAT_WAY_MONTH = 2;
	// ###########################################################################################
	// 匹配
	// ###########################################################################################
	/** 匹配-标的类型：优选 */
	public static final int MATCH_BID_TYPE_GOOD = 1;
	/** 匹配-标的类型：散标 */
	public static final Integer MATCH_BID_TYPE_COMMON = 2;
	/** 匹配：获取匹配开始天数 */
	public static final int MATCH_TERM_START = 1;
	/** 匹配：获取匹配结束天数 */
	public static final int MATCH_TERM_END = 2;
	/** 匹配：待匹配的金额 */
	public static final String MATCH_WILL_MONEY = "money";
	/** 匹配：待匹配的开始天数 */
	public static final String MATCH_WILL_TERM_START = "startTerm";
	/** 匹配：待匹配的结束天数 */
	public static final String MATCH_WILL_TERM_END = "endTerm";
	/** 匹配状态：成功 */
	public static final int MATCH_STATE_SUCCESS = 1;
	/** 匹配：未匹配 **/
	public static final int MATCH_STATE_NONE = 0;

	/** 匹配：完全匹配，用于标地和投资记录 **/
	public static final int MATCH_STATE_FINISH = 1;
	/** 匹配：部分匹配，用于标地和投资记录 **/
	public static final int MATCH_STATE_PART = 2;
	/** 匹配：标的是否存在匹配记录 是 */
	public static final int MATCH_STATUS_MATCHLIST_YES = 1;
	/** 匹配：标的是否存在匹配记录 否 */
	public static final int MATCH_STATUS_MATCHLIST_NO = 0;
	/**匹配查询页面 */
	public static final Integer MATCH_SHOW_TYPE_BASIC_PAGE = 1;
	/**匹配页面 */
	public static final Integer MATCH_SHOW_TYPE_MINI_PAGE = 2;
	/** 是否允许债权转让： 是*/
	public static final Integer INVEST_CREDITOR_STATE_YES =1;
	/** 是否允许债权转让： 否*/
	public static final Integer INVEST_CREDITOR_STATE_NO =0;

	// ####放款查询常量
	// 放款常量--产品
	public static final String MAKELOAN_BIDPRODUCT = "makeloan_bidproduct";
	// 放款常量--投资记录
	public static final String MAKELOAN_BIDINVESTS = "makeloan_bidInvests";
	// 放款常量--标的详情
	public static final String MAKELOAN_BIDDETAIL = "makeloan_bidDetail";
	/**匹配时标的有效的还款方式：2-按月付息，3-到期还本付息,6-按月付息，本金划归企业,7-按月付息，按日计息 */
	public static final List<Integer> MATCH_VALID_REPAYMENT_WAY = Arrays.asList(2,3,6,7);
	/**购房宝用户信息是否展示  1：不展示**/
	public static final Integer PURCHASE_CUSTOMER_STATE_NOT_SHOW =1;
	/**购房宝用户信息是否展示  0：展示**/
	public static final Integer PURCHASE_CUSTOMER_STATE_SHOW =0;
	/** 首页搜索定期理财产品 **/
	public static final Integer FIX_PRODUCT = 10;

    /**自动投资消息队列*/
    public static final String MQ_QUEUE_AUTO_INVEST = "auto_invest";
	/**自动投资设置状态  0：禁用**/
	public static final Integer AUTO_SCHEME_STATE_FORBIDDEN = 0;
	/**自动投资设置状态  1：启用**/
	public static final Integer AUTO_SCHEME_STATE_OPEN = 1;
	/**自动投资设置状态  4：删除*/
	public static final Integer AUTO_SCHEME_STATE_DEL = 1;
    /**自动投资设置时间类型  2：自定义 */
	public static final Integer AUTO_SCHEME_EFFECTIVE_TYPE_CUST = 2;


	/** APP展示产品类型： 新手标 */
	public static final int APP_SHOW_TYPE_PREFERRED = 10;
	/** APP展示产品类型： 爆款标 */
	public static final int APP_SHOW_TYPE_HOT = 11;
	/** APP展示产品类型： 推荐标 */
	public static final int APP_SHOW_TYPE_RECOMMEND = 12;
	/** APP展示产品类型： 月月赢 */
	public static final int APP_SHOW_TYPE_WINMONTH = 13;
	/** APP展示产品类型： 季季赢 */
	public static final int APP_SHOW_TYPE_WINSEASON = 14;
	/** APP展示产品类型： 年年赢 */
	public static final int APP_SHOW_TYPE_WINYEAR = 15;
	/** APP展示产品类型：体验金 */
	public static final int APP_SHOW_TYPE_EXPERIENCE = 16;
	/** APP展示产品类型：普通散标 */
	public static final int APP_SHOW_TYPE_COMMNE = 17;

    /*
    * 最低招标方案
    */
	public static final int BID_SCHEME_MIN = 1;
}
