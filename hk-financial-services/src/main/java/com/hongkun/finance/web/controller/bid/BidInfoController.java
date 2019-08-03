package com.hongkun.finance.web.controller.bid;


import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.info.service.InfoInformationNewsService;
import com.hongkun.finance.info.service.InfoQuestionnaireAnswerService;
import com.hongkun.finance.info.service.InformationNewsService;
import com.hongkun.finance.invest.constants.InvestConstants;
import com.hongkun.finance.invest.facade.BidInfoFacade;
import com.hongkun.finance.invest.facade.BidInvestExchangeFacade;
import com.hongkun.finance.invest.facade.BidInvestFacade;
import com.hongkun.finance.invest.facade.TransferManualFacade;
import com.hongkun.finance.invest.model.BidAutoScheme;
import com.hongkun.finance.invest.model.BidTransferManual;
import com.hongkun.finance.invest.model.vo.BidInfoSimpleVo;
import com.hongkun.finance.invest.model.vo.BidInfoVO;
import com.hongkun.finance.invest.model.vo.BidInvestDetailVO;
import com.hongkun.finance.invest.model.vo.IndexBidInfoVO;
import com.hongkun.finance.invest.service.*;
import com.hongkun.finance.loan.service.BidRepayPlanService;
import com.hongkun.finance.payment.constant.TradeTransferConstants;
import com.hongkun.finance.payment.model.FinAccount;
import com.hongkun.finance.payment.service.FinAccountService;
import com.hongkun.finance.roster.constants.RosterFlag;
import com.hongkun.finance.roster.constants.RosterType;
import com.hongkun.finance.roster.service.RosDepositInfoService;
import com.hongkun.finance.roster.service.RosInfoService;
import com.hongkun.finance.user.constants.UserConstants;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.model.RegUserDetail;
import com.hongkun.finance.user.service.RegUserDetailService;
import com.hongkun.finance.user.service.RegUserService;
import com.hongkun.finance.user.support.security.OperationTypeEnum;
import com.hongkun.finance.user.support.security.component.annotation.AskForPermission;
import com.hongkun.finance.user.utils.BaseUtil;
import com.hongkun.finance.vas.service.VasCouponDetailService;
import com.hongkun.finance.vas.service.VasSimRecordService;
import com.yirun.framework.core.annotation.Token;
import com.yirun.framework.core.annotation.Token.Ope;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.enums.PlatformSourceEnums;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.DateUtils;
import com.yirun.framework.core.utils.pager.Pager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

import static com.hongkun.finance.invest.constants.InvestConstants.*;
import static com.hongkun.finance.user.constants.UserConstants.USER_IDENTIFY_NO;

/**
 * @Description   : 标的信息
 * @Project       : hk-financial-services
 * @Program Name  : com.hongkun.finance.web.controller.bid.BidInfoController.java
 * @Author        : zhichaoding@hongkun.com zc.ding
 */

@Controller
@RequestMapping("/bidInfoController")
public class BidInfoController {
	
	private static final Logger logger = LoggerFactory.getLogger(BidInfoController.class);
			
	@Reference
	private BidInfoService bidInfoService;
	@Reference
	private BidInfoDetailService bidInfoDetailService;
	@Reference
	private BidInvestService bidInvestService;
	@Reference
	private BidTransferManualService bidTransferManualService;
	@Reference
	private BidInfoFacade bidInfoFacade;
	@Reference
	private FinAccountService finAccountService;
	@Reference
	private VasCouponDetailService vasCouponDetailService;
	@Reference
	private VasSimRecordService vasSimRecordService;
	@Reference
	private BidRepayPlanService bidRepayPlanService;
	@Reference
	private TransferManualFacade transferManualFacade;
	@Reference
    private BidInvestFacade bidInvestFacade;
	@Reference
	private RosDepositInfoService rosDepositInfoService;
	@Reference
	private RegUserService regUserService;
	@Reference
	private RegUserDetailService regUserDetailService;
	@Reference
	private BidAutoSchemeService bidAutoSchemeService;
	@Reference
	private InfoQuestionnaireAnswerService infoQuestionnaireAnswerService;
	@Reference
	private InfoInformationNewsService infoInformationNewsService;
	@Reference
	private InformationNewsService informationNewsService;
	@Reference
	private RosInfoService rosInfoService;
	@Reference
	private BidInvestExchangeFacade bidInvestExchangeFacade;

	/**
	 * 我要投资-项目列表查询
	 * @param bidInfoVO
	 * @param pager
	 * @return
	 */
	@RequestMapping("bidInfoList")
	@ResponseBody
	public ResponseEntity<?> bidInfoList(BidInfoVO bidInfoVO,Pager pager){
		RegUser loginUser = BaseUtil.getLoginUser();

		//判断是否是交易所匹配查询白名单
		boolean isExchange  =  this.rosInfoService.validateRoster(loginUser.getId(), RosterType.EXCHANGE_INVEST_SEACH, RosterFlag.WHITE);
		if(isExchange){
			//查询交易所匹配标的并返回
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("isExchangeBid",1);
			ResponseEntity<?> result =  bidInvestExchangeFacade.findExchangeBidList(null,pager);
			result.setParams(params);
			return result;
		}
		//查询投资中和已售罄的标的
		bidInfoVO.setStates(Arrays.asList(BID_STATE_WAIT_INVEST, BID_STATE_WAIT_AUDIT, BID_STATE_WAIT_LOAN));
		//标的类型:正常标,爆款标,推荐标
		bidInfoVO.setBidTypeLimitIds(Arrays.asList(BID_TYPE_COMMON,BID_TYPE_HOT,BID_TYPE_RECOMMEND));
		//还款方式:按月付息，到期还本 到期一次还本付息
		bidInfoVO.setRepayWayLimitIds(Arrays.asList(REPAYTYPE_INTEREST_MONTH_PRINCIPAL_END,REPAYTYPE_ONECE_REPAYMENT));
		//只查询上架中的产品类型
		bidInfoVO.setProductState(InvestConstants.PRODUCT_STATE_ON);
		//标的产品类型:新手标,月月赢,季季盈,年年盈
		bidInfoVO.setProductTypeLimitIds(Arrays.asList(BID_PRODUCT_PREFERRED,BID_PRODUCT_WINMONTH,BID_PRODUCT_WINSEASON,BID_PRODUCT_WINYEAR));
		// 海外产品列表标识
		if(bidInvestFacade.validOverseaInvestor(loginUser.getId()).validSuc()){
			bidInfoVO.setProductTypeLimitIds(Arrays.asList(BID_PRODUCT_OVERSEA));
		}
		ResponseEntity<?> result = this.bidInfoFacade.findBidVoListForInvest(pager, bidInfoVO, loginUser);
		// 债券转让标识
		BidTransferManual bidTransferManual = new BidTransferManual();
		bidTransferManual.setState(INVEST_TRANSFER_MANUAL_STATE_IN);
		Pager creditPage = new Pager();
		creditPage.setPageSize(Integer.MAX_VALUE);
		ResponseEntity<?> creditors = transferManualFacade.investListForCreditor(bidTransferManual, creditPage);
		result.getParams().putAll(creditors.getParams());
		return result;
	}

	/**
	 * 我要投资-体验金列表
	 * @param bidInfoVO
	 * @param pager
	 * @return
	 */
	@RequestMapping("experienceBidList")
	@ResponseBody
	public ResponseEntity<?> experienceBidList(BidInfoVO bidInfoVO,Pager pager){
		// 投资中
		bidInfoVO.setStates(Arrays.asList(BID_STATE_WAIT_INVEST));
		//限制查询
		bidInfoVO.setProductTypeLimitIds(Arrays.asList(BID_PRODUCT_EXPERIENCE));
		return this.bidInfoFacade.findBidVoListForInvest(pager, bidInfoVO, BaseUtil.getLoginUser());
	}

	/**
	 *  @Description    : 我要投资-债权转让
	 *  @Method_Name    : investTransferManualList
	 *  @param pager
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年12月26日 下午2:42:06 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	@RequestMapping("investTransferManualList")
	@ResponseBody
	public ResponseEntity<?> investTransferManualList(Pager pager){
		//查出所有的债权转让的标id
		BidTransferManual bidTransferManual = new BidTransferManual();
		bidTransferManual.setState(INVEST_TRANSFER_MANUAL_STATE_IN);
		return transferManualFacade.investListForCreditor(bidTransferManual, pager);
	}

	/**
	 * 我要投资-散标列表
	 * @param bidInfoVO
	 * @param pager
	 * @return
	 */
	@RequestMapping("commonBidList")
	@ResponseBody
	public ResponseEntity<?> commonBidList(BidInfoVO bidInfoVO,Pager pager){
		//只查询投标中的标的
		bidInfoVO.setProductType(BID_PRODUCT_COMMNE);
		// 投资中 满标待审核 待放款
		bidInfoVO.setStates(Arrays.asList(BID_STATE_WAIT_INVEST, BID_STATE_WAIT_AUDIT, BID_STATE_WAIT_LOAN));
		bidInfoVO.setMatchType(BID_MATCH_TYPE_NO);
		return this.bidInfoFacade.findBidVoListForInvest(pager, bidInfoVO, BaseUtil.getLoginUser());
	}
	
	
	/**
	 * 我要投资-钱坤宝列表
	 * @param bidInfoVO
	 * @param pager
	 * @return
	 */
	@RequestMapping("qkbBidList")
	@ResponseBody
	public ResponseEntity<?> qkbBidList(BidInfoVO bidInfoVO,Pager pager){
		// 投资中 满标待审核 待放款
		bidInfoVO.setStates(Arrays.asList(BID_STATE_WAIT_INVEST, BID_STATE_WAIT_AUDIT, BID_STATE_WAIT_LOAN));
		//限制查询
		bidInfoVO.setProductTypeLimitIds(Arrays.asList(BID_PRODUCT_BUYHOUSE, BID_PRODUCT_PROPERTY));
		return this.bidInfoFacade.findBidVoListForInvest(pager, bidInfoVO, BaseUtil.getLoginUser());
	}

	/**
	 *  @Description    : 进入投资详情页
	 *  @Method_Name    : toInvest
	 *  @param id		: 标的id
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年12月14日 下午5:04:35 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	@RequestMapping("toInvest")
	@ResponseBody
	public ResponseEntity<?> toInvest(@RequestParam("id") Integer id){
		return this.bidInfoFacade.findBidForInvest(BaseUtil.getLoginUser(), id);
	}
	
	/**
	 *  @Description    : 执行投资操作
	 *  @Method_Name    : invset
	 *  @param request
	 *  @param money	: 投资金额
	 *  @param bidId	: 标的id
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2018年1月4日 下午5:20:42 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	@RequestMapping("invest")
    @Token(operate = Ope.REFRESH)
    @AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_LOGIN)
    @ResponseBody
    public ResponseEntity<?> invest(HttpServletRequest request, @RequestParam("money")double money,  @RequestParam("bidId")int bidId, String investRedPacketId, String investRaiseInterestId) {
    	// 验证用户有效性
		RegUser regUser = BaseUtil.getRegUser(() -> this.regUserService.findRegUserById(BaseUtil.getLoginUser().getId()));
        if (regUser.getIdentify() == USER_IDENTIFY_NO) {
            return new ResponseEntity<>(UserConstants.NO_IDENTIFY, "为保障您的资金安全，请进行实名认证之后再投资");
        }

		return this.bidInvestFacade.invest(regUser,
				Integer.parseInt(StringUtils.isNotBlank(investRedPacketId) ? investRedPacketId : "0"),
				Integer.parseInt(StringUtils.isNotBlank(investRaiseInterestId) ? investRaiseInterestId : "0"),
				BigDecimal.valueOf(money), bidId, TradeTransferConstants.TRADE_TYPE_INVEST,
				InvestConstants.BID_INVEST_TYPE_MANUAL, PlatformSourceEnums.PC);
    }

	/**
	 *  @Description    : 条件检索投资记录
	 *  @Method_Name    : investList
	 *  @param pager
	 *  @param bidInvestDetailVO
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年12月26日 下午5:44:41 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("investList")
	@ResponseBody
	public ResponseEntity<?> investList(Pager pager, BidInvestDetailVO bidInvestDetailVO){
		bidInvestDetailVO.setRegUserId(BaseUtil.getLoginUser().getId());
		bidInvestDetailVO.setInvestChannel(InvestConstants.BID_INVEST_CHANNEL_IMMEDIATE);
		if (StringUtils.isNoneBlank(bidInvestDetailVO.getCreateTimeEnd())) {
			bidInvestDetailVO.setCreateTimeEnd(
					DateUtils.format(DateUtils.addDays(DateUtils.parse(bidInvestDetailVO.getCreateTimeEnd()), 1)));
		}
        bidInvestDetailVO.setSortColumns("b1.create_time DESC");
		Pager result = this.bidInvestService.findBidInvestDetailList(bidInvestDetailVO, pager);
		((List<BidInvestDetailVO>)result.getData()).forEach(invest -> invest.setInvestAmount(invest.getInvestAmount().subtract(invest.getTransAmount())));
		return new ResponseEntity(Constants.SUCCESS, result);
	}
	
	/**
	 * 
	 *  @Description    : 查询我的借款列表
	 *  @Method_Name    : borrowerRecordList
	 *  @param pager
	 *  @return
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2018年1月23日 下午2:25:57 
	 *  @Author         : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	@RequestMapping("borrowerRecordList")
	@ResponseBody
	public ResponseEntity<?> borrowerRecordList(Pager pager,BidInfoSimpleVo bidInfoSimpleVo){
		RegUser regUser = BaseUtil.getRegUser(() -> this.regUserService.findRegUserById(BaseUtil.getLoginUser().getId()));
		bidInfoSimpleVo.setBorrowerId(regUser.getId());
		return bidInfoFacade.findBidInfoByBorrowCode(bidInfoSimpleVo,pager);
	}
    
    /**
	 *  @Description    : 预更新自动投资配置
	 *  @Method_Name    : updateBidAutoScheme
	 *  @param id
	 *  @return         : BidAutoScheme
	 *  @Creation Date  : 2018年1月23日 下午4:13:38 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	@RequestMapping("preUpdateBidAutoScheme")
	@ResponseBody
	public BidAutoScheme updateBidAutoScheme(@RequestParam("id") Integer id){
		return this.bidAutoSchemeService.findBidAutoSchemeById(id);
	}
	
	/**
	 *  @Description    : 自动投资配置 增-改
	 *  @Method_Name    : bidAutoSchemeList
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2018年1月23日 下午1:45:46 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	@RequestMapping("updateBidAutoScheme")
	@ResponseBody
	public ResponseEntity<?> updateBidAutoScheme(@Validated BidAutoScheme bidAutoScheme){
		RegUser regUser = BaseUtil.getRegUser(() -> this.regUserService.findRegUserById(BaseUtil.getLoginUser().getId()));
		if(!regUser.hasIdentify()) {
			return new ResponseEntity<>(UserConstants.NO_IDENTIFY, "用户未实名");
		}
		if(bidAutoScheme.getId() != null && bidAutoScheme.getState() != null) {
			if(!Arrays.asList(0, 1).contains(bidAutoScheme.getState())){
	    		return ResponseEntity.error("无效的状态参数");
	    	}
		}
		bidAutoScheme.setRegUserId(regUser.getId());
		RegUserDetail regUserDetail = BaseUtil.getRegUserDetail(() -> this.regUserDetailService.findRegUserDetailByRegUserId(BaseUtil.getLoginUser().getId()));
		bidAutoScheme.setRealName(regUserDetail.getRealName());
		return this.bidAutoSchemeService.updateBidAutoScheme(bidAutoScheme);
	}
	
	/**
	 *  @Description    : 更新自动投资配置状态
	 *  @Method_Name    : updateBidAutoSchemeState
	 *  @param id
	 *  @param state
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2018年3月14日 下午5:25:33 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	@RequestMapping("updateBidAutoSchemeState")
	@ResponseBody
	public ResponseEntity<?> updateBidAutoSchemeState(@RequestParam("id") Integer id, @RequestParam("state") Integer state){
		if(!Arrays.asList(0, 1).contains(state)){
    		return ResponseEntity.error("无效的状态参数");
    	}
		BidAutoScheme bidAutoScheme = new BidAutoScheme();
		bidAutoScheme.setId(id);
		bidAutoScheme.setRegUserId(BaseUtil.getLoginUserId());
		bidAutoScheme.setState(state);
		return this.bidAutoSchemeService.updateBidAutoScheme(bidAutoScheme);
	}
	
	/**
	 *  @Description    : 删除自动投资配置
	 *  @Method_Name    : updateBidAutoSchemeState
	 *  @param id		: 自动投资配置id
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2018年3月14日 下午5:25:33 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	@RequestMapping("delBidAutoScheme")
	@ResponseBody
	public ResponseEntity<?> delBidAutoScheme(@RequestParam("id") Integer id){
		BidAutoScheme bidAutoScheme = new BidAutoScheme();
		bidAutoScheme.setId(id);
		bidAutoScheme.setRegUserId(BaseUtil.getLoginUserId());
		bidAutoScheme.setState(4);
		return this.bidAutoSchemeService.updateBidAutoScheme(bidAutoScheme);
	} 
	
	/**
	 *  @Description    : 查询自动投资配置
	 *  @Method_Name    : bidAutoSchemeList
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2018年1月23日 下午1:45:46 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	@RequestMapping("bidAutoSchemeList")
	@ResponseBody
	public ResponseEntity<?> bidAutoSchemeList(){
		Pager pager = new Pager();
		Optional.ofNullable(this.bidAutoSchemeService.findBidAutoSchemeByRegUserId(BaseUtil.getLoginUserId()))
				.ifPresent(o -> {
					pager.setData(Arrays.asList(o));
				});
		ResponseEntity<?> result = new ResponseEntity<>(Constants.SUCCESS, pager);
		FinAccount finAccount = this.finAccountService.findByRegUserId(BaseUtil.getLoginUser().getId());
		result.getParams().put("useableMoney", finAccount.getUseableMoney());
		result.getParams().put("xsbFlag", this.bidInvestService.findBidInvestCountForPrefered(BaseUtil.getLoginUser().getId()));
		return result;
	}


	/**
	 * 查询首页的标的信息
	 */
	@RequestMapping("/findIndexBidInfo")
	@ResponseBody
	@AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
	public ResponseEntity<?> findIndexBidInfo(){
		logger.info("首页：");
		ResponseEntity<?> indexBidInfo = bidInfoFacade.findIndexBidInfo(BaseUtil.getLoginUser());
		// 首页暂不展示到期一次还本付息的标的
		IndexBidInfoVO indexBidInfoVO = (IndexBidInfoVO) (indexBidInfo.getResMsg());
		indexBidInfoVO.setRepayOnceBid(null);
		return indexBidInfo;


	}
}
