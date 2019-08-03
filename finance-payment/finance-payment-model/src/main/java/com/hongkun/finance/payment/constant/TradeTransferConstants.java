package com.hongkun.finance.payment.constant;

import java.util.HashMap;
import java.util.Map;

import com.hongkun.finance.payment.enums.FundtransferBigTypeStateEnum;
import com.hongkun.finance.payment.enums.FundtransferSmallTypeStateEnum;

/**
 * @Project : finance
 * @Program Name :
 *          com.hongkun.finance.payment.constant.TradeTransferConstants.java
 * @Class Name : TradeTransferConstants.java
 * @Description : GENERATOR VO类 交易资金划转常量类
 * @Author : 曹新帮
 */
public class TradeTransferConstants {

	/**
	 * 开户
	 */
	public static final int TRADE_TYPE_OPEN_ACCOUNT = 0;
	/**
	 * 绑卡
	 */
	public static final int TRADE_TYPE_TIED_CARD = 1;
	/**
	 * 支付密码修改
	 */
	public static final int TRADE_TYPE_PAY_PASSWORD_UPDATE = 2;
	/** 提现手续费 **/
	public static final int TRADE_WITHDRAWALS_COMMISSION = 1;
	
    
	/** 交易流水：充值 */
	public static final int TRADE_TYPE_RECHARGE = 1001;
	/** 交易流水：物业充值 */
	public static final int TRADE_TYPE_RECHARGE_PROPERTY = 1002;
	/** 交易流水：银行卡充值投资 **/
	public static final int TRADE_TYPE_RECHARGE_INVEST = 1003;
	
	/************************ 投资相关交易流水 ***********************************/
	/** 交易流水：投资 */
	public static final int TRADE_TYPE_INVEST = 1101;
	/** 交易流水：体验金投资 */
	public static final int TRADE_TYPE_INVEST_SIM = 1102;
	/** 交易流水：钱袋子投资 */
	public static final int TRADE_TYPE_INVEST_QDZ = 1103;
	/** 交易流水：充值投资 */
	public static final int TRADE_TYPE_INVEST_RECHARGE = 1104;
	/** 交易流水：匹配投资 */
	public static final int TRADE_TYPE_INVEST_MATCH = 1105;

	/************************ 放款相关交易流水 ***********************************/
	/** 交易流水：放款 */
	public static final int TRADE_TYPE_MAKELOAN = 1201;
	/** 交易流水：体验金放款 */
	public static final int TRADE_TYPE_MAKELOAN_SIM = 1202;

	/************************ 还款相关交易流水 ***********************************/
	/** 交易流水：还款 */
	public static final int TRADE_TYPE_REPAY = 1301;
	/** 交易流水：提前还本 */
	public static final int TRADE_TYPE_REPAY_ADVANCE = 1302;
	/** 交易流水：风险储备金还款 */
	public static final int TRADE_TYPE_REPAY_RISK_RESERVE = 1303;
	/** 交易流水：还风险储备金 */
	public static final int TRADE_TYPE_REPAY_RETURN_RISK_RESERVE = 1304;

	/************************ 提现相关交易流水 ***********************************/
	/** 交易流水：提现申请 */
	public static final int TRADE_TYPE_WITHDRAW_APPLY = 1401;
	/** 交易流水：提现审核拒绝 */
	public static final int TRADE_TYPE_WITHDRAW_AUDIT_REJECT = 1402;
	/** 交易流水：提现放款 */
	public static final int TRADE_TYPE_WITHDRAW_LOAN = 1403;
	/** 交易流水：提现冲正 */
	public static final int TRADE_TYPE_WITHDRAW_CORRECT = 1404;

	/************************ 红包相关交易流水 ***********************************/
	/** 交易流水：线下生成红包 */
	public static final int TRADE_TYPE_RED_PACKET_GENERATE = 1501;
	/** 交易流水：运营派发红包 */
	public static final int TRADE_TYPE_RED_PACKET_DISTRIBUTE = 1502;
	/** 交易流水：个人对个人派发红包 */
	public static final int TRADE_TYPE_RED_PACKET_DISTRIBUTE_PERSONAL = 1503;
	/** 交易流水：审核红包拒绝 */
	public static final int TRADE_TYPE_RED_PACKET_CHECK_REJECT = 1504;
	/** 交易流水：红包失效 */
	public static final int TRADE_TYPE_RED_PACKET_CHECK_INVALID = 1505;
	/** 交易流水：红包兑换 */
	public static final int TRADE_TYPE_RED_PACKET_EXCHANGE = 1506;
	/** 交易流水：个人对平台派发红包 */
	public static final int TRADE_TYPE_RED_PACKET_DISTRIBUTE_PLATFORM = 1507;

	/************************ 推荐奖相关交易流水 ***********************************/
	/** 交易流水：推荐奖 */
	public static final int TRADE_TYPE_RECOMMEND = 1601;

	/************************ 钱袋子相关交易流水 ***********************************/
	/** 交易流水：钱袋子 */
	public static final int TRADE_TYPE_QDZ = 17;
	/** 交易流水：钱袋子转入 */
	public static final int TRADE_TYPE_QDZ_TURNS_IN = 1701;
	/** 交易流水：钱袋子转出 */
	public static final int TRADE_TYPE_QDZ_TURNS_OUT = 1702;
	/** 交易流水：钱袋子佣金 **/
	public static final int TRADE_TYPE_QDZ_COMMISSION = 1703;
	/** 交易流水：钱袋子每日利息 **/
	public static final int TRADE_TYPE_QDZ_DAY_INTEREST = 1704;
	/** 交易流水：钱袋子投资失败转入 */
	public static final int TRADE_TYPE_QDZ_TURNS_IN_INVEST = 1705;
	/** 交易流水：钱袋子投资转出 */
	public static final int TRADE_TYPE_QDZ_TURNS_OUT_INVEST = 1706;

	/************************ 积分相关交易流水 ***********************************/
	/** 交易流水：积分支付 */
	public static final int TRADE_TYPE_POINT_PAY = 1801;

	/************************ 债权相关交易流水 ***********************************/
	/** 交易流水：手动债权转让 */
	public static final int TRADE_TYPE_CREDITOR_TRANSFER_MANUAL = 1901;
	/** 交易流水：自动债权转让 */
	public static final int TRADE_TYPE_CREDITOR_TRANSFER_AUTO = 1902;
	/** 交易流水：钱袋子自动债权转让 */
	public static final int TRADE_TYPE_CREDITOR_TRANSFER_QDZ = 1903;
	/************************ 物业缴相关交易流水 ***********************************/
	/** 交易流水：物业缴费 */
	public static final int TRADE_TYPE_PROPERTY_FEE = 2001;
	/** 交易流水：物业缴费审核通过 */
	public static final int TRADE_TYPE_PROPERTY_FEE_AUDIT_SUCCESS = 2002;
	/** 交易流水：物业缴费审核拒绝 */
	public static final int TRADE_TYPE_PROPERTY_FEE_AUDIT_REFUSE = 2003;

	/************************ 标的相关交易流水 ***********************************/
	/** 交易流水：标的审核拒绝 */
	public static final int TRADE_TYPE_BID_CHECK_REJECT = 2101;
	/**平台对账***/
	public static final int TRADE_TYPE_PLATFROM_CHECK = 2201;

	/************************ 交易流水业务组 ***********************************/
	/** 普通充值标识 ***/
	public static final int RECHARGE_FLAG_COMMON = 0;
	/** 银行卡充值标识 ***/
	public static final int RECHARGE_FLAG_BANK = 1;
	/**
	 * 普通业务（默认）
	 */
	public static final String COMMON_BUSINESS = "00";
	/**
	 * 钱袋子投资业务
	 */
	public static final String QDZ_INVEST_BUSINESS = "01";
	/**
	 * 银行卡充值投资业务
	 */
	public static final String BANK_INVEST_BUSINESS = "02";
	/************************ 交易流水业务组 ***********************************/

	/************************ 资金划转subcode默认的几种类型 ***********************************/
	// sub_code 现在定义六种类型分别代表：
	/** 1、收入--可用余额增加，冻结金额不变
	           收入物业费 (1025)比较特殊，是针对物业账户来说的，冻结金额增加，可用余额不变，nowMoney增加**/
	// 2、支出--可用余额减少，冻结金额不变
	// 3、冻结--可用余额减少，冻结金额增加（只涉及自身账户）(不对账)
	// 4、解冻--可用余额增加，冻结金额减少（只涉及自身账户）(不对账)
	// 5、转入--从用户主账户转入到钱袋子账户(不对账)
	// 6、转出--从用户钱袋子账户转出到用户主账户(不对账)
	// 7、债转收入--钱袋子晚上跑批进行债权转让,当转出时相当于回收债权,只针对于个人用户类型(不对账)
	// 8、债转支出--钱袋子晚上跑批进行债权转让,当转入时相当于购买债权,只针对于个人用户类型(不对账)

	/************************ 账户的变动会依据资金划转subcode进行变动 ***********************************/
	/**
	 * 1、资金划转大类型为：PAY(20, "支出"), TURNS_IN(50,"转入") 或 特殊的资金划转subcode为：6001、6002时
	 */
	// 账户金额减少，即useableMoney和nowMoney减少。
	/**
	 * 2、资金划转大类型为: INCOME(10, "收入"),TURNS_OUT(60, "转出")或
	 * 特殊的资金划转subcode为：5001、5002时
	 */
	// 账户金额增加,即useableMoney和nowMoney增加。
	/**
	 * 3、资金划转大类型为：FREEZE(30, "冻结")
	 */
	// 即userableMoney减少，freezeMoney增加
	/**
	 * 4、资金划转大类型为: THAW(40, "解冻")
	 */
	// 即userableMoney增加，freezeMoney减少
	/**
	 * 5、有一种特殊的:资金划转为支出冻结：2020时
	 */
	// 账户金额减少，即freezeMoney减少，nowMoney减少。
	/**
	 * 6、资金划转subcode为:5001(转入钱袋子还款本金) 、 5002(转入钱袋子还款利息
	 * )、6001(转出钱袋子还款本金)、6002(转出钱袋子还款利息)、6003(转出钱袋子购买债权)、 7000(
	 * 钱袋子债转收入)、8000(钱袋子债转支出) 不做前台展示
	 */

	/** 资金划转： 收入 */
	public static final int TRANSFER_SUB_CODE_INCOME = 1000;
	/** 资金划转： 支出 */
	public static final int TRANSFER_SUB_CODE_PAY = 2000;
	/** 资金划转： 冻结 */
	public static final int TRANSFER_SUB_CODE_FREEZE = 3000;
	/** 资金划转： 解冻 */
	public static final int TRANSFER_SUB_CODE_THAW = 4000;
	/** 资金划转： 钱袋子转入 */
	public static final int TRANSFER_SUB_CODE_TURNS_IN = 5000;
	/** 资金划转：转入钱袋子还款本金 */
	public static final int TRANSFER_SUB_CODE_TURNS_IN_QDZ_REPAY_CAPITAL = 5001;
	/** 资金划转：转入钱袋子还款利息 */
	public static final int TRANSFER_SUB_CODE_TURNS_IN_QDZ_REPAY_INTEREST = 5002;
	/** 资金划转：钱袋子债权自动转入 */
	public static final int TRANSFER_SUB_CODE_TURNS_IN_QDZ_CREDITOR = 5003;

	/** 资金划转： 钱袋子转出 */
	public static final int TRANSFER_SUB_CODE_TURNS_OUT = 6000;
	/** 资金划转：转出钱袋子还款本金 */
	public static final int TRANSFER_SUB_CODE_TURNS_OUT_QDZ_REPAY_CAPITAL = 6001;
	/** 资金划转：转出钱袋子还款利息 */
	public static final int TRANSFER_SUB_CODE_TURNS_OUT_QDZ_REPAY_INTEREST = 6002;
	/** 资金划转：钱袋子债权自动转出 */
	public static final int TRANSFER_SUB_CODE_TURNS_OUT_QDZ_CREDITOR = 6003;
	/** 资金划转： 钱袋子债转收入(可用余额减少) */
	public static final int TRANSFER_SUB_CODE_CREDITOR_TRANSFER_INCOME = 7000;
	/** 资金划转： 钱袋子债转支出(可用余额增加) */
	public static final int TRANSFER_SUB_CODE_CREDITOR_TRANSFER_PAY = 8000;

	/***********************************************************************/

	/**
	 * @Description : 通过资金划转的大类型和小类型组合成资金划转的subCode
	 * @Method_Name : getFundTransferSubCodeByType;
	 * @param fundtransferBigTypeStateEnum
	 *            资金划转的大类型枚举类型
	 * @param fundtransferSmallTypeStateEnum
	 *            资金划转的小类型枚举类型
	 * @return
	 * @return : Integer;
	 * @Creation Date : 2017年11月14日 下午3:37:58;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static Integer getFundTransferSubCodeByType(FundtransferBigTypeStateEnum fundtransferBigTypeStateEnum,
			FundtransferSmallTypeStateEnum fundtransferSmallTypeStateEnum) {
		StringBuilder subCode = new StringBuilder();
		subCode.append(fundtransferBigTypeStateEnum.getBigTransferType());
		subCode.append(fundtransferSmallTypeStateEnum.getSmallTransferType());
		return Integer.parseInt(subCode.toString());
	}

	/***
	 * @Description : 根据资金划转的subcode，获取资金划转的大类型资金划转状态
	 * @Method_Name : getBigTypeSubCodeByTransferSubCode;
	 * @param transferSubCode
	 *            资金划转subCode
	 * @return
	 * @return : int;
	 * @Creation Date : 2017年12月12日 下午2:21:32;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static int getBigTypeSubCodeByTransferSubCode(int transferSubCode) {
		return Integer.parseInt(String.valueOf(transferSubCode).substring(0, 2));
	}

	/**
	 * @Description : 根据资金划转subCode,判断资金是否属于支出:
	 *              目前资金划转大类型为支出、钱袋子转入或者是特殊SUBCODE为:转出钱袋子还款本金、 转出钱袋子还款利息
	 *              、钱袋子债权自动转入、 钱袋子债转收入(可用余额减少) 、则属于资金支出
	 * @Method_Name : transferPayOut;
	 * @param transferSubCode
	 *            资金划转subcode
	 * @return
	 * @return : boolean;
	 * @Creation Date : 2017年12月12日 下午2:42:09;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static boolean transferPayOut(int transferSubCode) {
		if (getBigTypeSubCodeByTransferSubCode(transferSubCode) == FundtransferBigTypeStateEnum.PAY.getBigTransferType()
				|| transferSubCode == TRANSFER_SUB_CODE_TURNS_OUT_QDZ_REPAY_CAPITAL
				|| transferSubCode == TRANSFER_SUB_CODE_TURNS_OUT_QDZ_REPAY_INTEREST
				|| transferSubCode == TRANSFER_SUB_CODE_TURNS_IN
				|| transferSubCode == TRANSFER_SUB_CODE_TURNS_IN_QDZ_CREDITOR
				|| transferSubCode == TRANSFER_SUB_CODE_CREDITOR_TRANSFER_INCOME) {
			return true;
		}
		return false;
	}

	/**
	 * @Description : 根据资金划转SubCode,判断资金是否属于收入：目前资金划转大类型为收入、
	 *              钱袋子转出或者是特殊SUBCODE为转入钱袋子还款本金 、转入钱袋子还款利息 、钱袋子债权自动转出
	 *              、钱袋子债转支出(可用余额增加) 则认为是资金收入
	 * @Method_Name : transferIncome;
	 * @param transferSubCode
	 * @return
	 * @return : boolean;
	 * @Creation Date : 2017年12月12日 下午2:45:44;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static boolean transferIncome(int transferSubCode) {
		if (getBigTypeSubCodeByTransferSubCode(transferSubCode) == FundtransferBigTypeStateEnum.INCOME
				.getBigTransferType() 
				|| transferSubCode == TRANSFER_SUB_CODE_TURNS_IN_QDZ_REPAY_CAPITAL
				|| transferSubCode == TRANSFER_SUB_CODE_TURNS_IN_QDZ_REPAY_INTEREST
				|| transferSubCode == TRANSFER_SUB_CODE_TURNS_OUT
				|| transferSubCode == TRANSFER_SUB_CODE_TURNS_OUT_QDZ_CREDITOR
				|| transferSubCode == TRANSFER_SUB_CODE_CREDITOR_TRANSFER_PAY) {
			return true;
		}
		return false;
	}
}
