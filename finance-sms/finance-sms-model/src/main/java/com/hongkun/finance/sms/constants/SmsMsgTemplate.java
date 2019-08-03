package com.hongkun.finance.sms.constants;

import com.yirun.framework.core.utils.PropertiesHolder;

public enum SmsMsgTemplate {
	/**
	 * 注册通知
	 */
	MSG_REGIST("注册成功提醒", "恭喜您已成功注册" + PropertiesHolder.getProperty("service.name") + "，如有问题请致电： " + PropertiesHolder.getProperty("service.hotline") + "，感谢您的关注与支持！"),
	/**
	 * 注册给推荐人发送站内信
	 */
	MSG_RECOMMEND("好友推荐", "您推荐的好友%s已经成功注册为鸿坤金服用户 ，您可登录鸿坤金服“我的账号-我的推荐-推荐好友-已推荐用户”进行查看。"),

	/**
	 * 注册通知
	 */
	MSG_SMS_CODE("验证码", "尊敬的用户您好！ 您的验证码是： %s，请您及时使用，请勿向任何人泄露。"),
	/**
	 * 邀请注册
	 */
	MSG_INVITE("邀请提醒", "您收到鸿坤金服好友%s发送的邀请，请登录www.hongkunjinfu.com/indexController/regist.do?inviteNo=%s&from=singlemessage&isappinstalled=1 进行注册，加入鸿坤金服与友乾人一起赚钱。"),
	/**
	 * 实名通知
	 */
	MSG_REALNAME("实名通知", "恭喜您完成实名注册，鸿坤金服赠送了您%s个积分，请注意查收！"),
	/**
	 * 投资-无卡券
	 */
	MSG_INVEST("投资成功提醒", "尊敬的用户：您已成功投资《%s》，投资期限：%s%s，预期年化收益率：%s%%，投资金额：%s元，到期收益总额：%s元！"),
	/**
	 * 投资-使用加息券
	 */
	// MSG_INVEST_K("投资成功提醒", "尊敬的用户：您已成功投资《%s》，投资期限：%s%s，预期年化收益率：%s%%,
	// 投资金额：%s元, 到期收益总额：%s元，另外您使用了加息券,加息收益：%s元！"),
	MSG_INVEST_K("投资成功提醒", "尊敬的用户：您已成功投资《%s》，投资期限：%s%s，预期年化收益率：%s%%，投资金额：%s元，到期收益总额：%s元，另外您使用了投资红包%s元！"),
	/**
	 * 投资-使用投资红包
	 */
	MSG_INVEST_J("投资成功提醒", "尊敬的用户：您已成功投资《%s》，投资期限：%s%s，预期年化收益率：%s%%，投资金额：%s元，到期收益总额：%s元，其中您使用了加息券%s%%，加息收益：%s元！"),
	/**
	 * 投资-使用加息券&投资红包
	 */
	MSG_INVEST_K_J("投资成功提醒", "尊敬的用户：您已成功投资《%s》，投资期限：%s%s，预期年化收益率：%s%%，投资金额：%s元，到期收益总额：%s元，其中您使用了加息券%s%%，加息收益：%s元，另外您使用了投资红包%s元！"),
	/**
	 * 购房宝|物业宝还本
	 */
	MSG_REPAY_ENTERPRISE_CAP("收益通知", "尊敬的用户：您的%s意向金%s元，已划归您的账户，请您登录查实！"),
	/**
	 * 购房宝|物业宝收益
	 */
	MSG_REPAY_ENTERPRISE("收益通知", "尊敬的用户：您的%s第%s期的投资收益%s元已发放，请您注意查收！"),
	/**
	 * 回款收益（一条回款计划）
	 */
	MSG_REPAY_INCOME_ONE("收到本息提醒", "尊敬的用户：您投资的《%s》，还款已经完成。 本期应得总金额：%s元，其中本金部分为：%s元，利息部分：%s元，加息收益：%s元"),
	/**
	 * 回款收益（多条回款计划）
	 */
	MSG_REPAY_INCOME("收到本息提醒", "尊敬的用户：您投资的《%s》，第%s期还款已经完成。 本期应得总额：%s元，其中本金部分为：%s元，利息部分：%s元，加息收益：%s元"),
	/**
	 * 钱袋子转入通知
	 */
	MSG_QDZ_TRANSFER_IN_SUCCESS("钱袋子转入通知", "您成功从鸿坤金服主账户转入%s元至钱袋子，查询详情请登录鸿坤金服到我的账户中查询！"),
	/**
	 * 钱袋子转出通知
	 */
	MSG_QDZ_TRANSFER_OUT_SUCCESS("钱袋子转出通知", "您成功从钱袋子账户转出%s元至鸿坤金服账户，查询详情请登录鸿坤金服到我的账户中查询。"),
	/**
	 * 钱袋子转出通知
	 */
	MSG_QDZ_CREDITOR_NOTICE("钱袋子债权通知", "今日活期可购债权不够，请购买债权！"),
	/**
	 * 好友推荐奖金发放短信通知
	 */
	MSG_RECOMMEND_EARN_GRANT("佣金发放通知", "恭喜您获取好友推荐奖金 %s元，奖金已打入您的账户，请继续推荐新好友投资获取更多奖金！"),
	/**
	 * 好友推荐奖金发放站内信通知
	 */

	MSG_RECOMMEND_EARN_GRANT_SUCCESS("佣金发放通知", "鸿坤金服向您发来%s元推荐奖励红包，请及时在【我的账户->红包管理->红包兑换】功能处兑换红包。移动端客户在 “我的>红包”中直接领取"),
	/**
	 * 提现申请成功通知
	 */
	MSG_PAYMENT_TX_APPLY_SUCCESS("提现申请成功通知", "您已申请提现%s元！"),
	/**
	 * 提现申请成功通知（未使用优惠券）
	 */
	MSG_PAYMENT_TX_APPLY__NOTICKER_SUCCESS("提现申请成功通知", "您已申请提现%s元！（其中包含提现手续费1.00元）"),
	/**
	 * 提现申请成功通知（使用优惠券）
	 */
	MSG_PAYMENT_TX_APPLY__TICKER_SUCCESS("提现申请成功通知", "您已申请提现%s元！"),
	/**
	 * 提现申请失败通知
	 */
	MSG_PAYMENT_TX_APPLY_FAIL("提现申请失败通知", "您的%s元的提现申请失败，如有疑问请致电：" + PropertiesHolder.getProperty("service.hotline") + "。"),
	/**
	 * 提现成功短信通知(使用提现券)
	 */
	MSG_PAYMENT_TX_TICKET_SUCCESS("提现通知", "尊敬的用户：您于%s申请%s元提现成功，（提现手续费 1.00 元，提现券已抵扣） 请核实资金到账，本短信不作为银行入账凭证，具体到账时间以银行确认为准。若有疑问，请致电" + PropertiesHolder.getProperty("service.hotline") + "。"),
	/**
	 * 提现成功短信通知(没有提现券)
	 */
	MSG_PAYMENT_TX_SUCCESS("提现通知", "尊敬的用户：您于%s申请%s元提现成功，（提现手续费 1.00 元） 请核实资金到账，本短信不作为银行入账凭证，具体到账时间以银行确认为准。若有疑问，请致电" + PropertiesHolder.getProperty("service.hotline") + "。"),
	/**
	 * 提现成功站内信成功通知
	 */
	MSG_PAYMENT_TX_WEB_SUCCESS("提现通知", "尊敬的用户：恭喜您成功提现%s元！（其中包含提现手续费1.00元）"),
	/**
	 * 提现失败站内信失败通知
	 */
	MSG_PAYMENT_TX_WEB_FAIL("提现通知", "尊敬的用户：您提现%s元失败！（其中包含提现手续费1.00元）"),
	/**
	 * 提现失败站内信冲正通知
	 */
	MSG_PAYMENT_TX_WEB_CORRECT("提现通知", "尊敬的用户：您提现%s元已冲正！（其中包含提现手续费1.00元）"),
	/**
	 * 充值成功站内信成功通知
	 */
	MSG_PAYMENT_RECHARGE_SUCCESS("充值通知", "您已成功充值%s元，可登录账户查看余额。如有问题请致电：" + PropertiesHolder.getProperty("service.hotline") + "，感谢您的关注与支持！"),
	/**
	 * 商户收款站内信成功通知
	 */
	MSG_MERCHANT_POINT_PAYMENT_SUCCESS("商户收款", "收到%s（%s）的付款%s元，已划转至您的账户，请注意账户余额变化！"),

	/**
	 * 卡券转赠站内信和app推送信息成功通知
	 */
	MSG_DO_COUPON_DONATION_SUCCESS("好友赠送卡券", "您收到好友%s（%s）转赠给您的%s，请在PC端“我的账户>增值服务>券卡查询”中进行查询; 移动端（APP）用户到“我的福利”中查询。"),

	/**
	 * 积分转赠站内信成功通知
	 */
	MSG_USER_POINT_TRANSFER_SUCCESS("积分转赠", "您收到鸿坤金服好友%s赠送的%s积分，请查收！"),
	/**
	 * 平台赠送积分
	 */
	MSG_USER_POINT_GIVE_SUCCESS("积分赠送", "系统已向您的账号赠送%s积分，请在PC端“我的账户>积分管理>积分记录”功能处进行查看！"),
	/**
	 * 积分兑换,审核不通过
	 */
	MSG_USER_POINT_CHANGE_SUCCESS("积分兑换,审核不通过", "您兑换的%s，审核未通过，兑换花费的%s积分已退还至您的账户，请及时查看！"),

	
	/**
	 * 还款提醒
	 */
	// MSG_REPAY_NOTICE("还款提醒", "尊敬的%s用户，您在" +
	// PropertiesHolder.getProperty("service.name") + "有%s笔借款要还，应在%s,
	// 截至目前您的鸿坤金服账户余额为: %s元，特此提醒！ 可登录鸿坤金服到“我的借款>还款计划”中查看详情并进行还款！"),
	MSG_REPAY_NOTICE("还款提醒", "尊敬的用户，您在" + PropertiesHolder.getProperty("service.name") + "有%s笔借款要还，应在%s，特此提醒！"),
	/**
	 * 还款数量提醒管理员
	 */
	MSG_REPAY_NOTICE_ADMIN("还款提醒", "有%s个借款人在%s日需要还款，请登录" + PropertiesHolder.getProperty("service.name") + "进行查询，特此提醒！"),
	/**
	 * 生日发送卡券
	 */
	MSG_TREATMENT_BIRTH("生日卡券", "今天是您的生日，鸿坤金服祝您生日快乐！ %s张%s%s加息券已发送至您的账户中，请登录鸿坤金服进行查看."),
	/**
	 * 用户会员降级
	 */
	MSG_TREATMENT_DOWN_GRADE("会员降级", "尊敬的用户，由于您最近三个月内没有过投资且账户没有待收收益，会员等级下降至V%s."),
	/**
	 * 每月1号发送卡券
	 */
	MSG_TREATMENT_PER_MONTH("每月卡券", "恭喜您，%s张%s已发送至您的账户中，PC端“我的账户>增值服务>券卡查询”中进行查询; 移动端（APP）用户到“我的福利”中查询."),
	/**
	 * 后台赠送用户卡券
	 */
	// MSG_DISTRIBUTE_COUPON("恭喜您,一张%s已经发送到您的账户中,PC端“我的账户>增值服务>券卡查询”中进行查询;移动端(APP)用户到“我的福利”中查询。")
	MSG_DISTRIBUTE_COUPON("收到卡券通知", "您收到鸿坤金服发送的%s，请注意查收，您可以到“福利卡券”中查询。"),
	/**
	 * 后台赠送用户卡券通知
	 */
	MSG_DISTRIBUTE_COUPON_NOTICE("赠送卡券通知","接收人手机号：%s，接收人姓名：%s，%s"),
	/**
	 * 后台补发卡券失败通知
	 */
	MSG_REISSUE_COUPON_FAIL_NOTICE("补发卡券失败通知","接收人手机号：%s，卡券产品名称：%s，补发卡券失败请及时处理。"),

	MSG_BID_TRANSFER_SUCCESS("债权转让成功通知","尊敬的用户：您的%s借款项目，原债权人已将其债权转让给了新债权人，您需对新的债权人承担归还剩余本金及利息的义务，特此通知，谢谢！"),
	/**
	 * 个人发送红包
	 */
	MSG_RED_PACKET_PERSON("个人发送红包","您收到鸿坤金服好友%s发来的%s元的红包，兑换编码为%s，过期时间为%s，请及时在【我的账户->红包管理->红包兑换】功能处兑换红包。（移动端请直接兑换）"),
    /**
     * 钱袋子转入
     */
    MSG_QDZ_TRUN_IN("钱袋子转入通知"," 您已成功转入%s元到钱袋子！请到您的账户核对金额."),
    /**
     * 钱袋子转出
     */
    MSG_QDZ_TRUN_OUT("钱袋子转出通知"," 您已成功转出%s元到鸿坤金服！请到您的账户核对金额.");

	/**
	 * 标题
	 */
	private String title;
	/**
	 * 消息模板
	 */
	private String msg;

	private SmsMsgTemplate(String title, String msg) {
		this.title = title;
		this.msg = msg;
	}

	public String getTitle() {
		return title;
	}

	public String getMsg() {
		return msg;
	}
}
