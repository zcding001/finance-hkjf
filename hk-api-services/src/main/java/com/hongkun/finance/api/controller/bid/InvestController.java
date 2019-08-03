package com.hongkun.finance.api.controller.bid;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.hongkun.finance.invest.constants.InvestConstants;
import com.hongkun.finance.invest.facade.BidInvestFacade;
import com.hongkun.finance.invest.model.BidAutoScheme;
import com.hongkun.finance.invest.model.vo.BidInvestDetailVO;
import com.hongkun.finance.invest.service.BidAutoSchemeService;
import com.hongkun.finance.invest.service.BidInvestService;
import com.hongkun.finance.payment.constant.TradeStateConstants;
import com.hongkun.finance.payment.constant.TradeTransferConstants;
import com.hongkun.finance.payment.model.FinAccount;
import com.hongkun.finance.payment.model.FinPaymentRecord;
import com.hongkun.finance.payment.service.FinAccountService;
import com.hongkun.finance.payment.service.FinPaymentRecordService;
import com.hongkun.finance.payment.util.PaymentUtil;
import com.hongkun.finance.qdz.constant.QdzConstants;
import com.hongkun.finance.qdz.facade.QdzTransferFacade;
import com.hongkun.finance.user.constants.UserConstants;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.service.RegUserDetailService;
import com.hongkun.finance.user.service.RegUserService;
import com.hongkun.finance.user.support.security.annotation.ActionLog;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.enums.PlatformSourceEnums;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.AppResultUtil;
import com.yirun.framework.core.utils.DateUtils;
import com.yirun.framework.core.utils.HttpSessionUtil;
import com.yirun.framework.core.utils.PropertiesHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @Description : 投资、自动投资充值层
 * @Project : hk-api-services
 * @Program Name : com.hongkun.finance.api.controller.bid.InvestController.java
 * @Author : zhichaoding@hongkun.com zc.ding
 */
@Controller
@RequestMapping("/investController/")
public class InvestController {
	private static final Logger logger = LoggerFactory.getLogger(InvestController.class);
	@Reference
	private BidInvestFacade bidInvestFacade;
	@Reference
	private RegUserService regUserService;
	@Reference
	private RegUserDetailService regUserDetailService;
	@Reference
	private BidAutoSchemeService bidAutoSchemeService;
	@Reference
	private BidInvestService bidInvestService;
	@Reference
	private FinAccountService finAccountService;
	@Reference
	private QdzTransferFacade qdzTransferFacade;
	@Reference
	private FinPaymentRecordService finPaymentRecordService;

	/**
	 * @Description : 我的账户-投资记录
	 * @Method_Name : investList
	 * @return : Map<String,Object>
	 * @Creation Date : 2018年3月12日 下午6:11:41
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	@RequestMapping("/investList")
	@ResponseBody
	public Map<String, Object> investList(BidInvestDetailVO bidInvestDetailVO) {
		bidInvestDetailVO.setRegUserId(BaseUtil.getLoginUserId());
		bidInvestDetailVO.setBidInvestStates(Arrays.asList(InvestConstants.INVEST_STATE_SUCCESS, InvestConstants.INVEST_STATE_DEL,InvestConstants.INVEST_STATE_TRANSFER,InvestConstants.INVEST_STATE_SUCCESS_BUYER));
		//查询投资中、待审核、待放款、还款中
        bidInvestDetailVO.setBidStates(Arrays.asList(InvestConstants.BID_STATE_WAIT_REPAY, InvestConstants.BID_STATE_WAIT_INVEST, InvestConstants.BID_STATE_WAIT_AUDIT, InvestConstants.BID_STATE_WAIT_LOAN));
        if(StringUtils.isNotBlank(bidInvestDetailVO.getCreateTimeEnd())){
            bidInvestDetailVO.setCreateTimeEnd(bidInvestDetailVO.getCreateTimeEnd() + " 23:59:59");
        }
        //产品类型:0-其他，2-月月盈,3-季季盈,4-年年盈
        if(Objects.equals(bidInvestDetailVO.getBidProductType(), 0)){
            bidInvestDetailVO.setBidProductTypes(Arrays.asList(InvestConstants.BID_PRODUCT_COMMNE,
                    InvestConstants.BID_PRODUCT_PREFERRED, InvestConstants.BID_PRODUCT_EXPERIENCE,
                    InvestConstants.BID_PRODUCT_BUYHOUSE, InvestConstants.BID_PRODUCT_PROPERTY, InvestConstants.BID_PRODUCT_CURRENT,
                    InvestConstants.BID_PRODUCT_PROPERTY, InvestConstants.BID_PRODUCT_ACTIVITY));
            bidInvestDetailVO.setBidProductType(null);
        }
        bidInvestDetailVO.setSortColumns("b1.create_time DESC");
		return AppResultUtil.successOfListInProperties(
				this.bidInvestFacade.findBidInvestListForApp(bidInvestDetailVO), "", "bidInvestId", "bidInfoId", "bidName",
				"investAmount", "createTime", "finishTime", "lendingTime", "bidInvestState");
	}

	/**
	 * @Description : 自动投资列表
	 * @Method_Name : bidAutoSchemeList
	 * @return : Map<String,Object>
	 * @Creation Date : 2018年3月13日 上午10:23:36
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	@RequestMapping("/bidAutoSchemeList")
	@ResponseBody
	public Map<String, Object> bidAutoSchemeList() {
		final Integer regUserId = BaseUtil.getLoginUserId();
		RegUser regUser = BaseUtil.getRegUser(() -> this.regUserService.findRegUserById(regUserId));
		if (regUser.getIdentify() == UserConstants.USER_IDENTIFY_NO) {
			return AppResultUtil.errorOfMsg("为保障您的资金安全，请进行实名认证！");
		}
		Integer xsbFlag = this.bidInvestService.findBidInvestCountForPrefered(regUserId);
		BidAutoScheme bidAutoScheme = this.bidAutoSchemeService.findBidAutoSchemeByRegUserId(BaseUtil.getLoginUserId());
		FinAccount finAccount = this.finAccountService.findByRegUserId(regUserId);
		Map<String, Object> result;
		int exist = 0;
		if (bidAutoScheme == null) {
			result = AppResultUtil.successOfMsg("");
		} else {
			exist = 1;
			result = AppResultUtil.successOf(bidAutoScheme, "createTime", "modifyTime", "realName")
					.reNameParameter("id", "basId");
		}
		result.put("xsbFlag", Objects.equals(xsbFlag, 0) ? 1 : 0);
		result.put("exist", exist);
		result.put("useableMoney", finAccount.getUseableMoney());
		return result;
	}

	/**
	 * @Description : 删除自动投资配置
	 * @Method_Name : delBas
	 * @param basId
	 *            : 配置id
	 * @return : Map<String,Object>
	 * @Creation Date : 2018年3月14日 下午5:19:45
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	@RequestMapping("/delBas")
	@ResponseBody
	public Map<String, Object> delBas(@RequestParam("basId") Integer basId) {
		BidAutoScheme bidAutoScheme = new BidAutoScheme();
		bidAutoScheme.setId(basId);
		bidAutoScheme.setRegUserId(BaseUtil.getLoginUserId());
		bidAutoScheme.setState(4);
		return AppResultUtil.mapOfResponseEntity(this.bidAutoSchemeService.updateBidAutoScheme(bidAutoScheme));
	}

	/**
	 * @Description : 删除&更新自动投资配置
	 * @Method_Name : updateBasState
	 * @param basId
	 *            : 自动投资配置id
	 * @param state
	 *            : 状态
	 * @return : Map<String,Object>
	 * @Creation Date : 2018年3月13日 下午2:00:28
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	@RequestMapping("/updateBasState")
	@ResponseBody
	public Map<String, Object> updateBasState(@RequestParam("basId") Integer basId,
			@RequestParam("state") Integer state) {
		if (!Arrays.asList(0, 1).contains(state)) {
			return AppResultUtil.errorOfMsg("无效的状态参数");
		}
        BidAutoScheme bas = bidAutoSchemeService.findBidAutoSchemeById(basId);
        if(state == 1 && Objects.equals(bas.getEffectiveType(), InvestConstants.AUTO_SCHEME_EFFECTIVE_TYPE_CUST)){
            Date date = new Date();
            if(date.after(bas.getEffectiveEndTime())){
                return AppResultUtil.errorOfMsg("请设置有效的投资有效期");
            }
        }
		BidAutoScheme bidAutoScheme = new BidAutoScheme();
		bidAutoScheme.setId(basId);
		bidAutoScheme.setRegUserId(BaseUtil.getLoginUserId());
		bidAutoScheme.setState(state);
		return AppResultUtil.mapOfResponseEntity(this.bidAutoSchemeService.updateBidAutoScheme(bidAutoScheme));
	}

	/**
	 * @Description : 添加|更新自动投资配置
	 * @Method_Name : saveOrUpdateBas
	 * @param basId : 配置id
	 * @param minRate : 最小利率
	 * @param reserveAmount : 账户保留金额
	 * @param repayType : 还款方式 还款方式(可多选) 0 不限 2 按月付息，到期还本 3 到期还本付息'
	 * @param investTermMin : 最新投资期限
	 * @param investTermMax : 最大投资期限
	 * @param effectiveType : 有效期
	 * @param effectiveStartTime : 有效开始时间
	 * @param effectiveEndTime : 有效结束时间
	 * @param useCouponFlag : 使用卡券标识
	 * @param priorityType : 预期年化收益率和投资期限优先级
	 * @param bidPriority : 标的优先级
	 * @return : Map<String,Object>
	 * @Creation Date : 2018年3月13日 下午2:27:41
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	@RequestMapping("/saveOrUpdateBas")
	@ResponseBody
	public Map<String, Object> saveOrUpdateBas(Integer basId, @RequestParam("minRate") BigDecimal minRate,
			@RequestParam("reserveAmount") BigDecimal reserveAmount, @RequestParam("repayType") int repayType,
			@RequestParam("investTermMin") int investTermMin, @RequestParam("investTermMax") int investTermMax,
			@RequestParam("effectiveType") int effectiveType, String effectiveStartTime, String effectiveEndTime,
			@RequestParam("useCouponFlag") int useCouponFlag, @RequestParam("priorityType") int priorityType,
			@RequestParam("bidPriority") int bidPriority, Integer state) {
		// 验证数据准确性
		if (!Pattern.matches("^[0-9]+([.]{1}[0-9]{1,2})?$", String.valueOf(minRate)) || minRate.doubleValue() > 100
				|| minRate.doubleValue() < 5) {
			return AppResultUtil.errorOfMsg("最低年化收益率：最大值100%，最小不能低于5%，最多保留两位小数");
		}
		String regex = "(^[1-9]$)|(^[1-2][0-9]$)|(^[3][0-6]$)";
		if (!Pattern.matches(regex, String.valueOf(investTermMin))
				|| !Pattern.matches(regex, String.valueOf(investTermMax)) || investTermMax < investTermMin) {
			return AppResultUtil.errorOfMsg("投资期限最小1个月,投资期限最大36个月,最小投资期限不得超过最大投资期限");
		}
		// 自定义有效期时、验证投资有效时间
        if (effectiveType == 2) {
            if (StringUtils.isBlank(effectiveStartTime) || StringUtils.isBlank(effectiveEndTime)
                    || DateUtils.parse(effectiveStartTime + " 00:00:00", DateUtils.DATE_HH_MM_SS).after(DateUtils.parse(effectiveEndTime + " 23:59:59", DateUtils.DATE_HH_MM_SS))) {
                return AppResultUtil.errorOfMsg("开始结束时间不能为空,开始时间不能大于等于结束时间");
            }
            if(state == 1){
                Date date = new Date();
                if(date.after(DateUtils.parse(effectiveEndTime + " 23:59:59", DateUtils.DATE_HH_MM_SS))){
                    return AppResultUtil.errorOfMsg("请设置有效的投资有效期");
                }
            }
        }
		if (basId != null && state != null) {
			if (!Arrays.asList(0, 1).contains(state)) {
				return AppResultUtil.errorOfMsg("非法的状态参数");
			}
		}
		BidAutoScheme bidAutoScheme = new BidAutoScheme();
		bidAutoScheme.setId((basId == null || basId == 0) ? null : basId);
		bidAutoScheme.setRegUserId(BaseUtil.getLoginUserId());
		bidAutoScheme.setRealName(BaseUtil
				.getRegUserDetail(
						() -> this.regUserDetailService.findRegUserDetailByRegUserId(BaseUtil.getLoginUserId()))
				.getRealName());
		bidAutoScheme.setUseCouponFlag(useCouponFlag);
		bidAutoScheme.setMinRate(minRate);
		bidAutoScheme.setInvestTermMax(investTermMax);
		bidAutoScheme.setInvestTermMin(investTermMin);
		bidAutoScheme.setRepayType(repayType);
		bidAutoScheme.setBidPriority(bidPriority);
		bidAutoScheme.setPriorityType(priorityType);
		bidAutoScheme.setEffectiveType(effectiveType);
		bidAutoScheme.setReserveAmount(reserveAmount);
		bidAutoScheme.setState(state);
		if (effectiveType == 2) {
			bidAutoScheme.setEffectiveStartTime(DateUtils.parse(effectiveStartTime + " 00:00:00", DateUtils.DATE_HH_MM_SS));
			bidAutoScheme.setEffectiveEndTime(DateUtils.parse(effectiveEndTime + " 23:59:59", DateUtils.DATE_HH_MM_SS));
		}
		return AppResultUtil.mapOfResponseEntity(this.bidAutoSchemeService.updateBidAutoScheme(bidAutoScheme));
	}

	/**
	*  预投资，投资确认页
	*  @param bidId 标的ID
	*  @return java.util.Map<java.lang.String,java.lang.Object>
	*  @date                    ：2018/11/12
	*  @author                  ：zc.ding@foxmail.com
	*/
    @RequestMapping("preInvest")
    @ResponseBody
    public Map<String, Object> preInvest(@RequestParam Integer bidId){
        final Integer regUserId = BaseUtil.getLoginUserId();
        Map<String, Object> result = this.bidInvestFacade.preInvest(regUserId, bidId);
        //设置钱袋子标识
        result.put("qdzEnable", this.bidInvestFacade.validQdzEnable(regUserId));
	    return result;
    }

	/**
	 * @Description : 投资操作
	 * @Method_Name : invest
	 * @param paymentFlowId         : 支付记录ID流水 标的ID
	 * @param bidId                 : 标的ID
	 * @param investRedPacketId     : 红包id
	 * @param investRaiseInterestId ：加息券id
	 * @param money                 ：投资金额
	 * @param sign_type              ：签名方式
	 * @param sign                  ：签名数据
	 * @param investWay             ：投资方式 1101正常投资 1102: 体验金投资 1103：钱袋子投资 1104 银行卡投资
	 * @param source                ：投资来源 11-IOS 12-android
	 * @return : Map<String,Object>
	 * @Creation Date : 2018年3月14日 下午3:50:06
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	@RequestMapping("invest")
	@ResponseBody
    @ActionLog(msg = "投资操作, 标的: {args[1]}, 红包: {args[2]}, 加息卷: {args[3]}, 投资金额: {args[4]}, 投资方式: {args[7]}, 投资来源: {args[8]}, 投资流水: {args[0]}")
	public Map<String, Object> invest(@RequestParam(required = false) String paymentFlowId, @RequestParam Integer bidId,
			@RequestParam(required = false) String investRedPacketId,
			@RequestParam(required = false) String investRaiseInterestId, @RequestParam BigDecimal money,
			@RequestParam String sign_type, @RequestParam String sign, @RequestParam Integer investWay,
			@RequestParam Integer source) {
		ResponseEntity<?> responseEntity = null;// 钱袋子投资返回的responseEntity
		ResponseEntity<?> result = null;// 投资返回的ResponseEntity
		RegUser regUser = BaseUtil.getRegUser(() -> this.regUserService.findRegUserById(BaseUtil.getLoginUserId()));
		try {
			// 验证签名
			JSONObject reqObj = new JSONObject();
			reqObj.put("sessionId", HttpSessionUtil.getRequest().getParameter(Constants.SESSION_ID_KEY));
			reqObj.put("bidId", bidId);
			reqObj.put("money", money);
			reqObj.put("investWay", investWay);
			reqObj.put("paymentFlowId", paymentFlowId);
			reqObj.put("sign_type", sign_type);
			reqObj.put("sign", sign);
            reqObj.put("source", source);
            reqObj.put("investRedPacketId", investRedPacketId);
            reqObj.put("investRaiseInterestId", investRaiseInterestId);
			if (StringUtils.isNotBlank(investRaiseInterestId)) {
				reqObj.put("investRaiseInterestId", investRaiseInterestId);
			}
			if (StringUtils.isNotBlank(investRedPacketId)) {
				reqObj.put("investRedPacketId", investRedPacketId);
			}
			// 验证签名
			if ("1".equals(PropertiesHolder.getProperty("valid_sign")) && !PaymentUtil.checkSignByMd5(reqObj.toString())) {
				return AppResultUtil.errorOfMsg("签名错误");
			}
			// 判断实名
			if (!regUser.hasIdentify()) {
			    if(regUser.getType() == UserConstants.USER_TYPE_ENTERPRISE || regUser.getType() == UserConstants.USER_TYPE_TENEMENT){
                    Map<String, Object> res = AppResultUtil.mapOfResponseEntity(new ResponseEntity<>(UserConstants.NO_IDENTIFY, "您还未进行实名认证或绑定银行卡，可以拨打客服电话：" + UserConstants.SERVICE_HOTLINE + "，联系客服人员"));
			        res.put("serviceHotline", UserConstants.SERVICE_HOTLINE);
                    return res;
                }
			    return AppResultUtil.mapOfResponseEntity(new ResponseEntity<>(UserConstants.NO_IDENTIFY, "为保障您的资金安全，请进行实名认证之后再投资"));
			}
			// 第三方账户不能使用钱袋子投资
			if (investWay.equals(TradeTransferConstants.TRADE_TYPE_INVEST_QDZ)
					&& regUser.getType() == UserConstants.USER_TYPE_ESCROW_ACCOUNT) {
				return AppResultUtil.errorOfMsg("您没有权限进行钱袋子操作");
			}
			// 钱袋子投资处理逻缉，进行转出到余额再进行投资
			if (investWay.equals(TradeTransferConstants.TRADE_TYPE_INVEST_QDZ)) {
				responseEntity = qdzTransferFacade.transferOut(regUser, money, source.toString(),
						QdzConstants.QDZ_TURNINOUT_TYPE_INVEST);
				if (responseEntity.getResStatus() == Constants.ERROR) {
					logger.info("用户标识：{},钱袋子投资转出失败：{}", regUser.getId(), responseEntity.getResMsg());
					return AppResultUtil.errorOfMsg("钱袋子投资失败");
				}
			}
			// 如果是银行卡投资，则轮循查询充值接口,充值成功后，再进行投资
			boolean payResult = false;
			if (investWay.equals(TradeTransferConstants.TRADE_TYPE_INVEST_RECHARGE)) {
				for (int i = 1; i <= 10; i++) {
					FinPaymentRecord finPaymentRecord = finPaymentRecordService
							.findFinPaymentRecordByFlowId(paymentFlowId);
					if (finPaymentRecord != null
							&& finPaymentRecord.getState() == TradeStateConstants.ALREADY_PAYMENT) {
						payResult = true;
						break;
					}
					Thread.sleep(1000);
				}
				if (!payResult) {
					return AppResultUtil.errorOfMsg("投资充值失败，请稍候查询充值记录结果！");
				}
			}
			// 执行投资操作
			result = this.bidInvestFacade.invest(regUser,
					Integer.parseInt(StringUtils.isNotBlank(investRedPacketId) ? investRedPacketId : "0"),
					Integer.parseInt(StringUtils.isNotBlank(investRaiseInterestId) ? investRaiseInterestId : "0"),
					money, bidId, investWay, InvestConstants.BID_INVEST_TYPE_MANUAL,
					PlatformSourceEnums.typeByValue(source));
			// 投资失败
			if (!result.validSuc()) {
			    Map<String, Object> map;
				// 钱袋子投资处理返回信息
				if (investWay.equals(TradeTransferConstants.TRADE_TYPE_INVEST_QDZ)) {
                    map = AppResultUtil.errorOfMsg(result.getResStatus(), "您使用钱袋子投资申请失败，资金已回退到您鸿坤金服主账户的可用余额中，请到【我的账号】中进行查询！");
				} else if (investWay.equals(TradeTransferConstants.TRADE_TYPE_INVEST_RECHARGE)) {
                    map =  AppResultUtil.errorOfMsg(result.getResStatus(), "您使用银行卡进行投资申请失败，资金已回退到您鸿坤金服主账户的可用余额中，请到【我的账号】中进行查询！v");
				} else {
                    map =  AppResultUtil.errorOfMsg(result.getResStatus(), result.getResMsg());
				}
				if(result.getResStatus() == UserConstants.NOT_VIP){
				    map.put("serviceHotline", PropertiesHolder.getProperty("service.hotline"));
                }
				return map;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return AppResultUtil.errorOfMsg(result.getResStatus(), "投资失败！");
		}
		return AppResultUtil.successOfMsg("投资成功！");
	}
}
