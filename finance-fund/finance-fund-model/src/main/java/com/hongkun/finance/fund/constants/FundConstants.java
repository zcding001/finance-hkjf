package com.hongkun.finance.fund.constants;

public class FundConstants {

	/** 1-私募基金 */
	public static final int FUND_PROJECT_PARENT_TYPE_PRIVATE = 1;
	/** 2-海外基金 */
	public static final int FUND_PROJECT_PARENT_TYPE_ABROAD = 2;
	/** 3-信托产品 */
	public static final int FUND_PROJECT_PARENT_TYPE_TRUST = 3;
	/** 4-房产投资 */
	public static final int FUND_PROJECT_PARENT_TYPE_HOUSE = 4;
	/** 1-明星独角兽系列 */
	public static final int FUND_PROJECT_TYPE_STAR_UNICORN = 1;
	/** 2-PRE-IPO系列 */
	public static final int FUND_PROJECT_TYPE_PRE_IPO = 2;
	/** 3-产业协同系列 */
	public static final int FUND_PROJECT_TYPE_INDUSTRIAL_SYNERGY = 3;
	/** 4-地产基金 */
	public static final int FUND_PROJECT_TYPE_LAND = 4;
	/** 5-海外基金 */
	public static final int FUND_PROJECT_TYPE_ABROAD = 5;
	/** 6-信托产品 */
	public static final int FUND_PROJECT_TYPE_TRUST = 6;
	/** 股权项目信息删除 */
	public static final int FUND_INFO_STATE_DELETE = 0;
	/** 股权项目信息待上架 */
	public static final int FUND_INFO_STATE_WAIT_SHELF  = 1;
	/** 股权项目信息上架 */
	public static final int FUND_INFO_STATE_SHELF = 2;
	/** 股权项目信息下架 */
	public static final int FUND_INFO_STATE_OFF_SHELF = 3;

	/** 股权项目信息停约 */
	public static final int FUND_INFO_SUBSCRIBE_STATE_OFF = 0;
	/** 股权项目信息预约中 */
	public static final int FUND_INFO_SUBSCRIBE_STATE_ON = 1;
	/** 咨询信息客户来源 平台*/
	public static final int FUND_ADVISORY_SOURCE_PLATFORM  = 0;
	/** 咨询信息客户来源 后台录入*/
	public static final int FUND_ADVISORY_SOURCE_INPUT = 1;
	/** 项目存在*/
	public static final int FUND_INFO_EXIST_YES  = 1;
	/** 项目不存在*/
	public static final int FUND_INFO_EXIST_NO  = 0;
	/** 工作日*/
	public static final int FUND_INFO_OPENDAY_TYPE_WORKDAY  = 1;
	/** 自定义日*/
	public static final int FUND_INFO_OPENDAY_TYPE_CUSTOM  = 2;
	/** 日期范围*/
	public static final int FUND_INFO_OPENDAY_TYPE_RANGE  = 3;
	/** 自定义日:期限类型 周*/
	public static final int FUND_INFO_CUSTOMIZE_TYPE_WEEK  = 1;
	/** 自定义日:期限类型  月*/
	public static final int FUND_INFO_CUSTOMIZE_TYPE_MONTH  = 2;

	/** 测评 -股权测评*/
	public static final int EVALUTION_FUND  = 1;
	/** 测评 -投资测评*/
	public static final int EVALUTION_INVEST  = 2;

	/** api 返回码   -- 协议姓名输入有误*/
	public static final int ERROR_CODE_NAME = 2201;
	/** api 返回信息 -- 协议姓名输入有误*/
	public static final String ERROR_MSG_NAME = "请输入正确的姓名！";

	/** api 返回码   -- 预约信息不存在*/
	public static final int ERROR_CODE_NO_ADVISORY = 2202;
	/** api 返回信息 -- 预约信息不存在*/
	public static final String ERROR_MSG_NO_ADVISORY = "预约信息不存在！";

	/** api 返回码   -- 企业账号不能预约海外基金*/
	public static final int ERROR_CODE_FORBID_ENTERPRISE = 1006;
	/** api 返回信息 -- 企业账号不能预约海外基金*/
	public static final String ERROR_MSG_FORBID_ENTERPRISE = "企业账号暂不支持线上预约，如需要，请拨打客服电话：4009009630。";


	/**海外基金预约状态：初始化 */
	public static final int FUND_ADVISORY_STATE_INIT = 0;
	/**海外基金预约状态：审核中 */
	public static final int FUND_ADVISORY_STATE_UNDER_AUDIT = 1;
	/**海外基金预约状态：资质审核通过 */
	public static final int FUND_ADVISORY_STATE_QULICATION_PASS = 2;
	/**海外基金预约状态：资质审核拒绝 */
	public static final int FUND_ADVISORY_STATE_QULICATION_REJECT = 3;
	/**海外基金预约状态：打款成功 */
	public static final int FUND_ADVISORY_STATE_INMONEY_PASS = 4;
	/**海外基金预约状态：打款失败 */
	public static final int FUND_ADVISORY_STATE_INMONEY_ERROR  = 5;
	/**海外基金预约状态：历史版本 */
	public static final int FUND_ADVISORY_AUDIT_STATE_HISTORY = 9;

	/** 产品存续期限单位 ：年*/
	public static final int  FUND_INFO_TERM_UNIT_YEAR = 1;
	/** 产品存续期限单位 ：月*/
	public static final int  FUND_INFO_TERM_UNIT_MONTH = 2;
	/** 产品存续期限单位 ：日*/
	public static final int  FUND_INFO_TERM_UNIT_DAY = 3;

	/**海外基金协议选项：是 */
	public static final int FUND_AGREEMENT_YES  = 1;
	/**海外基金协议选项：否 */
	public static final int FUND_AGREEMENT_NO = 0;
}
