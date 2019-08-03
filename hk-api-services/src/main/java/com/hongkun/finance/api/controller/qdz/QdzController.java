package com.hongkun.finance.api.controller.qdz;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.hongkun.finance.contract.constants.ContractConstants;
import com.hongkun.finance.invest.facade.BidInvestFacade;
import com.hongkun.finance.payment.constant.PaymentConstants;
import com.hongkun.finance.payment.constant.TradeTransferConstants;
import com.hongkun.finance.payment.facade.FinPaymentFacade;
import com.hongkun.finance.payment.model.FinAccount;
import com.hongkun.finance.payment.model.FinFundtransfer;
import com.hongkun.finance.payment.service.FinAccountService;
import com.hongkun.finance.payment.service.FinFundtransferService;
import com.hongkun.finance.payment.util.PaymentUtil;
import com.hongkun.finance.qdz.constant.QdzConstants;
import com.hongkun.finance.qdz.facade.QdzTransferFacade;
import com.hongkun.finance.qdz.model.QdzTransRecord;
import com.hongkun.finance.qdz.service.QdzTransRecordService;
import com.hongkun.finance.sms.constants.SmsConstants;
import com.hongkun.finance.sms.constants.SmsMsgTemplate;
import com.hongkun.finance.sms.model.SmsWebMsg;
import com.hongkun.finance.sms.utils.SmsSendUtil;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.model.RegUserDetail;
import com.hongkun.finance.user.service.DicDataService;
import com.hongkun.finance.user.service.RegUserDetailService;
import com.hongkun.finance.user.service.RegUserService;
import com.hongkun.finance.user.support.security.OperationTypeEnum;
import com.hongkun.finance.user.support.security.component.annotation.AskForPermission;
import com.hongkun.finance.user.utils.BaseUtil;
import com.hongkun.finance.vas.enums.VasRuleTypeEnum;
import com.hongkun.finance.vas.model.VasRebatesRule;
import com.hongkun.finance.vas.model.vo.VasCouponDetailVO;
import com.hongkun.finance.vas.service.VasCouponDetailService;
import com.hongkun.finance.vas.service.VasRebatesRuleService;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.enums.PlatformSourceEnums;
import com.yirun.framework.core.enums.SystemTypeEnums;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.AppResultUtil;
import com.yirun.framework.core.utils.CompareUtil;
import com.yirun.framework.core.utils.DateUtils;
import com.yirun.framework.core.utils.StringUtilsExtend;
import com.yirun.framework.core.utils.pager.Pager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hongkun.finance.payment.constant.PaymentConstants.PAYMENT_DIC_BUSINESS;
import static com.hongkun.finance.payment.enums.FundtransferBigTypeStateEnum.*;

/**
 * @Description : 钱袋子
 * @Project : hk-api-services
 * @Program Name : com.hongkun.finance.api.qdz.QdzController.java
 * @Author : caoxinbang@hongkun.com.cn 曹新帮
 */
@Controller
@RequestMapping("/qdzController")
public class QdzController {

	private static final Logger logger = LoggerFactory.getLogger(QdzController.class);

	@Reference
	private QdzTransferFacade qdzTransferFacade;
	@Reference
	private RegUserService regUserService;
	@Reference
	private FinFundtransferService finFundtransferService;
	@Reference
	private FinPaymentFacade finPaymentFacade;
	@Reference
	private DicDataService dicDataService;
	@Reference
	private BidInvestFacade bidInvestFacade;
	@Reference
	private VasRebatesRuleService vasRebatesRuleService;
	@Reference
	private QdzTransRecordService qdzTransRecordService;
	@Reference
	private VasCouponDetailService vasCouponDetailService;
	@Reference
	private FinAccountService finAccountService;
	@Reference
	private RegUserDetailService regUserDetailService;

	
	/**
	 * 
	 * @Description : 获取首页和产品页钱袋子信息
	 * @Method_Name : findQdzInfo
	 * @return
	 * @return : Map<String,Object>
	 * @Creation Date : 2018年3月8日 下午2:20:05
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	@RequestMapping("/findQdzInfo")
	@ResponseBody
	@AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
	public Map<String, Object> findQdzInfo() {
		return AppResultUtil.mapOfResponseEntity(qdzTransferFacade.findQdzInfo(BaseUtil.getLoginUserId()));
	}

	/**
	 * 
	 * @Description : 获取我的钱袋子信息
	 * @Method_Name : findMyQdzInfo
	 * @return
	 * @return : Map<String,Object>
	 * @Creation Date : 2018年3月8日 下午2:21:59
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	@RequestMapping("/findMyQdzInfo")
	@ResponseBody
	public Map<String,Object> findMyQdzInfo() {
	    RegUser regUser = BaseUtil.getRegUser(BaseUtil.getLoginUserId(),
                () -> this.regUserService.findRegUserById(BaseUtil.getLoginUserId()));
		Map<String,Object> qdzInfo = new HashMap<>();
		ResponseEntity<?> qdzEntity = qdzTransferFacade.findMyQdzInfo(regUser);
		if(null == regUser || !qdzEntity.validSuc()
				|| BaseUtil.equelsIntWraperPrimit((Integer)qdzEntity.getParams().get("qdzFlag"),QdzConstants.QDZ_SHOW_FLAG_FLASE)
				|| (!bidInvestFacade.validInvestQualification(regUser.getId()).validSuc())
				|| (bidInvestFacade.validOverseaInvestor(regUser.getId()).validSuc())){
			return AppResultUtil.successOfMsg("成功!");
		}
		qdzEntity.getParams().remove("qdzFlag");
		qdzInfo.put("qdzInfo",qdzEntity.getParams());
		return AppResultUtil.mapOf(qdzInfo,Constants.SUCCESS,"成功!");
	}

	/**
	 * 
	 * @Description : 钱袋子转入
	 * @Method_Name : qdzTransferIn
	 * @return
	 * @return : Map<String,Object>
	 * @throws Exception
	 * @Creation Date : 2018年3月8日 下午2:21:59
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	@RequestMapping("/qdzTransferIn")
	@ResponseBody
	public Map<String, Object> qdzTransferIn(HttpServletRequest request,@RequestParam(value = "money", required = false) BigDecimal money,
			@RequestParam(value = "source", required = false) String source,@RequestParam String sign_type, @RequestParam String sign) throws Exception {
	    RegUser regUser = BaseUtil.getRegUser(BaseUtil.getLoginUserId(),
                () -> this.regUserService.findRegUserById(BaseUtil.getLoginUserId()));
	    //进行签名验证
	    JSONObject reqObj = new JSONObject();
        reqObj.put("money", money);
        reqObj.put("source",source);
        reqObj.put("sign_type", sign_type);
        reqObj.put("sessionId", request.getParameter(Constants.SESSION_ID_KEY));
        reqObj.put("sign", sign);
        if(!PaymentUtil.checkSignByMd5(reqObj.toString())){
            return  AppResultUtil.errorOfMsg(Constants.ERROR, "签名错误");
        }
		return AppResultUtil.mapOfResponseEntity(
				qdzTransferFacade.transferIn(regUser, money, source, QdzConstants.QDZ_TURNINOUT_TYPE_COMMON));
	}

	/**
	 * 
	 * @Description : 钱袋子转出
	 * @Method_Name : qdzTransferOut
	 * @return
	 * @return : Map<String,Object>
	 * @throws Exception
	 * @Creation Date : 2018年3月8日 下午2:21:59
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	@RequestMapping("/qdzTransferOut")
	@ResponseBody
	public Map<String, Object> qdzTransferOut(HttpServletRequest request,@RequestParam(value = "money", required = false) BigDecimal money,
			@RequestParam(value = "source", required = false) String source,@RequestParam String sign_type, @RequestParam String sign) throws Exception {
	    RegUser regUser = BaseUtil.getRegUser(BaseUtil.getLoginUserId(),
                () -> this.regUserService.findRegUserById(BaseUtil.getLoginUserId()));
	    //进行签名验证
        JSONObject reqObj = new JSONObject();
        reqObj.put("money", money);
        reqObj.put("source",source);
        reqObj.put("sign_type", sign_type);
        reqObj.put("sessionId", request.getParameter(Constants.SESSION_ID_KEY));
        reqObj.put("sign", sign);
        if(!PaymentUtil.checkSignByMd5(reqObj.toString())){
            return  AppResultUtil.errorOfMsg(Constants.ERROR, "签名错误");
        }
		return AppResultUtil.mapOfResponseEntity(
				qdzTransferFacade.transferOut(regUser, money, source, QdzConstants.QDZ_TURNINOUT_TYPE_COMMON),
				"qdzTransRecordId", "transActualMoney", "transMoney", "waitMatchMoney");
	}

	/**
	 * 
	 * @Description : 获取我的钱袋子交易记录
	 * @Method_Name : getQdzTradeRecord
	 * @return
	 * @return : Map<String,Object>
	 * @Creation Date : 2018年3月8日 下午2:21:59
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	@RequestMapping("/getQdzTradeRecord")
	@ResponseBody
	public Map<String, Object> getQdzTradeRecord(@RequestParam(value = "type", required = false) Integer type,
			@RequestParam(value = "startTime", required = false) String startTime,
			@RequestParam(value = "endTime", required = false) String endTime,
			@RequestParam(value = "minMoney", required = false) BigDecimal minMoney,
			@RequestParam(value = "maxMoney", required = false) BigDecimal maxMoney, Pager pager) {
	    RegUser regUser = BaseUtil.getRegUser(BaseUtil.getLoginUserId(),
                () -> this.regUserService.findRegUserById(BaseUtil.getLoginUserId()));
		FinFundtransfer fundtransferDtc = new FinFundtransfer();
		if (!StringUtilsExtend.isEmpty(startTime)) {
			fundtransferDtc.setCreateTimeBegin(DateUtils.parse(startTime));
		}
		if (!StringUtilsExtend.isEmpty(endTime)) {
			fundtransferDtc.setCreateTimeEnd(DateUtils.addDays(DateUtils.parse(endTime), 1));
		}
		fundtransferDtc.setRegUserId(regUser.getId());
		fundtransferDtc.setMinTransMoney(minMoney);
		fundtransferDtc.setMaxTransMoney(maxMoney);
		fundtransferDtc.setSortColumns("create_time desc");
		if (type == 17) {
			fundtransferDtc.setTradeTypeList(Arrays.asList(TradeTransferConstants.TRADE_TYPE_QDZ_TURNS_IN,TradeTransferConstants.TRADE_TYPE_QDZ_TURNS_OUT,TradeTransferConstants.TRADE_TYPE_QDZ_DAY_INTEREST,TradeTransferConstants.TRADE_TYPE_QDZ_TURNS_OUT_INVEST));
		} else {
			fundtransferDtc.setTradeType(type);
		}
		pager = finFundtransferService.findByCondition(fundtransferDtc, pager);
		if(pager != null && pager.getData().size()>0){
    		pager.getData().forEach(e->{
    		    FinFundtransfer ft = (FinFundtransfer)e;
    		    ft.setSubCodeDesc(dicDataService.findNameByValue(PAYMENT_DIC_BUSINESS,
                        PaymentConstants.PAYMENT_DIC_BUSINESS_SUBJECT_TRANSFER, ft.getSubCode()));
    		    Integer subInt = Integer.valueOf(String.valueOf(ft.getSubCode()).substring(0, 2));
                // 收入 解冻
                if(INCOME.getBigTransferType() == subInt || THAW.getBigTransferType() == subInt ||  TURNS_IN.getBigTransferType() ==subInt){
                    ft.setMoney("+" + ft.getTransMoney());;//加号
                }else{
                    ft.setMoney("-"+ ft.getTransMoney());//减号
                }
    		   
    		});
		}
		return AppResultUtil
				.successOfListInProperties(pager.getData(), "查询成功！", "money", "tradeType", "createTime", "subCode","subCodeDesc");
	}

	/**
	 * 
	 * @Description : 计算转出手续费
	 * @Method_Name : calculateTransferOutFee
	 * @param type
	 * @param money
	 * @return
	 * @return : Map<String,Object>
	 * @Creation Date : 2018年3月15日 下午3:19:19
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	@RequestMapping("/calculateTransferOutFee")
	@ResponseBody
	public Map<String, Object> calculateTransferOutFee(@RequestParam(value = "type", required = false) int type,
			@RequestParam(value = "money", required = false) BigDecimal money) {
	    RegUser regUser = BaseUtil.getRegUser(BaseUtil.getLoginUserId(),
                () -> this.regUserService.findRegUserById(BaseUtil.getLoginUserId()));
		return AppResultUtil.mapOfResponseEntity(qdzTransferFacade.calculateTransferOutFee(regUser, money, type));
	}

	/**
	 * 
	 * @Description : 转出到银行卡
	 * @Method_Name : calculateTransferOutFee
	 * @param type
	 * @param money
	 * @return
	 * @return : Map<String,Object>
	 * @Creation Date : 2018年3月15日 下午3:19:19
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	@RequestMapping("/transferOutToBank")
	@ResponseBody
	public Map<String, Object> transferOutToBank(HttpServletRequest request,@RequestParam(value = "source", required = false) Integer source,
			@RequestParam(value = "money", required = false) BigDecimal money,@RequestParam String sign_type, @RequestParam String sign) {
	    RegUser regUser = BaseUtil.getLoginUser();
		try {
		    //进行签名验证
	        JSONObject reqObj = new JSONObject();
	        reqObj.put("money", money);
	        reqObj.put("source",source);
	        reqObj.put("sign_type", sign_type);
	        reqObj.put("sessionId", request.getParameter(Constants.SESSION_ID_KEY));
	        reqObj.put("sign", sign);
	        if(!PaymentUtil.checkSignByMd5(reqObj.toString())){
	            return  AppResultUtil.errorOfMsg(Constants.ERROR, "签名错误");
	        }
			if (regUser == null) {
				return AppResultUtil.errorOfMsg("用户未登录！");
			}
			if (CompareUtil.lteZero(money)) {
				return AppResultUtil.errorOfMsg("转出金额不能小于零！");
			}
			if (money.divideAndRemainder(new BigDecimal(100))[1].compareTo(BigDecimal.ZERO) != 0) {
				return AppResultUtil.errorOfMsg("转出金额必须是100的整数倍！");
			}
			// 调用转出逻辑
			ResponseEntity<?> qdzOutResult = qdzTransferFacade.transferOut(regUser, money, String.valueOf(source),
					QdzConstants.QDZ_TURNINOUT_TYPE_WITHDRAW);
			if (qdzOutResult.getResStatus() == Constants.ERROR) {
				return AppResultUtil.errorOfMsg(qdzOutResult.getResMsg());
			}
			Map<String, Object> qdzMap = qdzOutResult.getParams();
			//查询用户是否有可使用的优惠券
	        List<VasCouponDetailVO> couponDetailList = vasCouponDetailService.getUserWithdrawUsableCoupon(regUser.getId());
			Integer couponDetailId =null;
		    BigDecimal actualMoney = new BigDecimal(String.valueOf(qdzMap.get("transActualMoney")));
	        if(couponDetailList!=null && couponDetailList.size()>0){
	            couponDetailId = couponDetailList.get(0).getId();
			}else{
			   FinAccount account = finAccountService.findByRegUserId(regUser.getId());
			   if(account!=null){
			       if(CompareUtil.gt(new BigDecimal(1),account.getUseableMoney().subtract(actualMoney))){
			           actualMoney = actualMoney.subtract(new BigDecimal(1));
			       }
			   }
			}
	        //调用提现接口
			ResponseEntity<?> withDrawResult = finPaymentFacade.clientWithDrawFacade(
			        actualMoney.toString(), regUser.getId(),
					PlatformSourceEnums.typeByValue(source), couponDetailId, SystemTypeEnums.HKJF);
			if (withDrawResult.getResStatus() == Constants.ERROR) {
				return AppResultUtil.errorOfMsg("转出银行卡失败，您的钱已到账户可用余额，请查看我的账户,失败原因：" + withDrawResult.getResMsg());
			}
			if (withDrawResult.getResStatus() == Constants.SUCCESS) {
				// 如果投资成功，则自动释放债权
			//	qdzTransferFacade.autoCreditorMQ(qdzOutResult.getParams());
				// 发送站内信
				SmsSendUtil.sendWebMsgToQueue(new SmsWebMsg((Integer) withDrawResult.getResMsg(),
						SmsMsgTemplate.MSG_PAYMENT_TX_APPLY_SUCCESS.getTitle(),
						SmsMsgTemplate.MSG_PAYMENT_TX_APPLY_SUCCESS.getMsg(), SmsConstants.SMS_TYPE_NOTICE));
				return AppResultUtil.successOfMsg("转出银行卡申请成功！");
			}
		} catch (Exception e) {
		    logger.error("钱袋子转出到银行卡失败, 用户标识: {}, 转出金额: {}, 来源: {}",regUser.getId(),money,source);
		}
		return AppResultUtil.errorOfMsg("转出银行卡失败，您的钱已到账户可用余额，请查看我的账户!");
	}
	/**
	 *  @Description    : 根据类型获取APP钱袋子规则介绍信息(1-转入 2-转出)
	 *  @Method_Name    : getQdzRule;
	 *  @return
	 *  @return         : Map<String,Object>;
	 *  @Creation Date  : 2018年10月22日 下午3:53:38;
	 *  @Author         : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
    @RequestMapping("/getQdzRule")
    @ResponseBody
    public Map<String, Object> getQdzRule(Integer type) {
        return AppResultUtil.mapOfResponseEntity(vasRebatesRuleService.getQdzRule(type));
                
   }
    /**
     *  @Description    : 查询用户是否购买过钱袋子
     *  @Method_Name    : buyQdzInfo;
     *  @return
     *  @return         : Map<String,Object>;
     *  @Creation Date  : 2018年10月22日 下午4:04:28;
     *  @Author         : yanbinghuang@hongkun.com.cn 黄艳兵;
     */
    @RequestMapping("/buyQdzInfo")
    @ResponseBody
    public Map<String, Object> bugQdzInfo() {
        RegUser regUser = BaseUtil.getRegUser(BaseUtil.getLoginUserId(),
                () -> this.regUserService.findRegUserById(BaseUtil.getLoginUserId()));
        QdzTransRecord qdzTransRecord = new QdzTransRecord();
        qdzTransRecord.setRegUserId(regUser.getId());
        int buyQdzCount = qdzTransRecordService.findQdzTransRecordCount(qdzTransRecord);
        //是否购买过钱袋子标只
        boolean isBuyQdzInvest = false;
        if (buyQdzCount > 0) {
            isBuyQdzInvest = true;
        }
        return AppResultUtil.successOfMsg("查询成功").addResParameter("isBuyQdzInvest", isBuyQdzInvest);
   }
    /**
     *  @Description    : 获取用户可用余额，及钱袋子可转入金额
     *  @Method_Name    : getQdzUseableAmount;
     *  @return
     *  @return         : Map<String,Object>;
     *  @Creation Date  : 2018年10月24日 上午9:35:54;
     *  @Author         : yanbinghuang@hongkun.com.cn 黄艳兵;
     */
    @RequestMapping("/getQdzUseableAmount")
    @ResponseBody
    public Map<String, Object> getQdzUseableAmount() {
        RegUser regUser = BaseUtil.getRegUser(BaseUtil.getLoginUserId(),
                () -> this.regUserService.findRegUserById(BaseUtil.getLoginUserId()));
        return AppResultUtil.mapOfResponseEntity(qdzTransferFacade.getQdzUseableAmount(regUser));
    }
    /**
     *  @Description    : app展示钱袋子协议
     *  @Method_Name    : viewQdzAgreement;
     *  @param 			：type - 0没买过， 1 买过 展示协议合同用户信息
     *  @param 			：sessionId
     *  @return
     *  @return         : ModelAndView;
     *  @Creation Date  : 2019-01-25 15:02:31;
     *  @Author         : binliang@hongkun.com.cn 梁彬;
     */
    @RequestMapping("/viewQdzAgreement")
    @ResponseBody
    public ModelAndView viewQdzAgreement(Integer type,String sessionId ,String access_token) {
    	ModelAndView result = new ModelAndView("qdz/qdzAgreement");
        result.addObject("companyName", ContractConstants.COMPANY_NAME);
        result.addObject("companyTel", ContractConstants.COMPANY_TEL);
        if(type == 0 ){
        	return result;
        }
        RegUser regUser = BaseUtil.getLoginUser();
        if(regUser == null){
        	return result;
        }
        RegUserDetail regUserDetail = regUserDetailService.findRegUserDetailByRegUserId(regUser.getId());
        result.addObject("qdzAccountNo", "qdz_"+regUser.getLogin());
        result.addObject("userName", regUserDetail.getRealName());
        result.addObject("idCard", regUserDetail.getIdCard());
        return result;
    }

    /** 
    * @Description: 钱袋子介绍
	* @param
    * @return: org.springframework.web.servlet.ModelAndView 
    * @Author: hanghe@hongkunjinfu.com
    * @Date: 2019/1/28 10:14
    */
	@RequestMapping("/viewQdzIntroduction")
	@ResponseBody
	@AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
	public ModelAndView viewQdzIntroduction() {
		ModelAndView result = new ModelAndView("qdz/qdzdetail");
		VasRebatesRule vasRebatesRule = vasRebatesRuleService.findVasRebatesRuleByTypeAndState(VasRuleTypeEnum.QDZ.getValue(),1);
		Map<String,Object> vasDetail = (Map)JSONObject.parseObject(vasRebatesRule.getContent());
		result.addObject("investLowest", vasDetail.get("investLowest"));			//最低转入转出金额
		result.addObject("inMaxMoneyPPPD", vasDetail.get("inMaxMoneyPPPD"));		//每人每天最大转入金额
		result.addObject("outMaxMoneyPPPD", vasDetail.get("outMaxMoneyPPPD"));		//每人每天最大转出金额
		result.addObject("noInOutStartTimes", vasDetail.get("noInOutStartTimes"));	//不可转入开始时间
		result.addObject("noInOutEndTimes", vasDetail.get("noInOutEndTimes"));		//不可转入结束时间
		result.addObject("outOPPPerMonth", vasDetail.get("outOPPPerMonth"));		//每个月转出免费次数
		result.addObject("outPayRate", vasDetail.get("outPayRate"));				//免费之后手续费率
		return result;
	}
}
