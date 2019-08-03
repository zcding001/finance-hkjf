package com.hongkun.finance.invest.facade.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.contract.constants.ContractConstants;
import com.hongkun.finance.contract.model.ConType;
import com.hongkun.finance.contract.service.ConInfoService;
import com.hongkun.finance.contract.service.ConTypeService;
import com.hongkun.finance.fund.model.vo.FundInfoVo;
import com.hongkun.finance.fund.service.FundInfoService;
import com.hongkun.finance.fund.util.FundUtils;
import com.hongkun.finance.invest.constants.InvestConstants;
import com.hongkun.finance.invest.facade.BidInfoFacade;
import com.hongkun.finance.invest.facade.BidInvestFacade;
import com.hongkun.finance.invest.model.BidInfo;
import com.hongkun.finance.invest.model.BidInfoDetail;
import com.hongkun.finance.invest.model.BidInvest;
import com.hongkun.finance.invest.model.BidInvestExchange;
import com.hongkun.finance.invest.model.vo.*;
import com.hongkun.finance.invest.service.*;
import com.hongkun.finance.invest.util.BidInfoUtil;
import com.hongkun.finance.invest.util.CalcInterestUtil;
import com.hongkun.finance.loan.model.BidRepayPlan;
import com.hongkun.finance.loan.service.BidRepayPlanService;
import com.hongkun.finance.payment.model.FinAccount;
import com.hongkun.finance.payment.model.FinFundtransfer;
import com.hongkun.finance.payment.model.FinTradeFlow;
import com.hongkun.finance.payment.service.FinAccountService;
import com.hongkun.finance.payment.service.FinConsumptionService;
import com.hongkun.finance.payment.util.FinTFUtil;
import com.hongkun.finance.qdz.service.QdzAccountService;
import com.hongkun.finance.qdz.service.QdzTransRecordService;
import com.hongkun.finance.roster.constants.RosterFlag;
import com.hongkun.finance.roster.constants.RosterType;
import com.hongkun.finance.roster.model.RosDepositInfo;
import com.hongkun.finance.roster.service.RosDepositInfoService;
import com.hongkun.finance.roster.service.RosInfoService;
import com.hongkun.finance.user.constants.UserConstants;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.model.RegUserDetail;
import com.hongkun.finance.user.service.DicDataService;
import com.hongkun.finance.user.service.RegUserDetailService;
import com.hongkun.finance.user.utils.BaseUtil;
import com.hongkun.finance.vas.constants.VasConstants;
import com.hongkun.finance.vas.constants.VasCouponConstants;
import com.hongkun.finance.vas.enums.VasRuleTypeEnum;
import com.hongkun.finance.vas.model.VasCouponDetail;
import com.hongkun.finance.vas.model.VasRebatesRule;
import com.hongkun.finance.vas.model.VasSimRecord;
import com.hongkun.finance.vas.service.VasCouponDetailService;
import com.hongkun.finance.vas.service.VasRebatesRuleService;
import com.hongkun.finance.vas.service.VasSimRecordService;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.enums.PlatformSourceEnums;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.singleton.SingletonThreadPool;
import com.yirun.framework.core.utils.*;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.jms.JmsService;
import com.yirun.framework.jms.enums.DestinationType;
import com.yirun.framework.jms.enums.JmsMessageType;
import javafx.util.Pair;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.mengyun.tcctransaction.api.Compensable;
import org.mengyun.tcctransaction.dubbo.context.DubboTransactionContextEditor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NamedThreadLocal;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.hongkun.finance.fund.constants.FundConstants.FUND_INFO_EXIST_YES;
import static com.hongkun.finance.fund.constants.FundConstants.FUND_PROJECT_PARENT_TYPE_PRIVATE;
import static com.hongkun.finance.invest.constants.InvestConstants.*;
import static com.hongkun.finance.payment.constant.TradeTransferConstants.TRADE_TYPE_BID_CHECK_REJECT;
import static com.hongkun.finance.payment.constant.TradeTransferConstants.TRANSFER_SUB_CODE_THAW;
import static com.hongkun.finance.user.utils.BaseUtil.equelsIntWraperPrimit;
import static com.hongkun.finance.user.utils.BaseUtil.isResponseSuccess;
import static com.hongkun.finance.vas.constants.VasConstants.VAS_RULE_STATE_START;
import static com.yirun.framework.core.commons.Constants.ERROR;
import static com.yirun.framework.core.commons.Constants.SUCCESS;
import static com.yirun.framework.core.utils.AppResultUtil.DATA_LIST;
import static java.lang.Boolean.TRUE;

@Service
public class BidInfoFacadeImpl implements BidInfoFacade {

    private static final Logger logger = LoggerFactory.getLogger(BidInfoFacadeImpl.class);
    
    @Reference
    private BidInfoService bidInfoService;
    @Reference
    private BidProductService bidProductService;
    @Reference
    private RegUserDetailService regUserDetailService;
    @Autowired
    private BidInfoDetailService bidInfoDetailService;
    @Autowired
    private BidMatchService bidMatchService;
    @Autowired
    private BidInvestService bidInvestService;
    @Reference
    private FinAccountService finAccountService;
    @Reference
    private FinConsumptionService finConsumptionService;
    @Reference
    private VasSimRecordService vasSimRecordService;
    @Reference
    private VasCouponDetailService vasCouponDetailService;
    @Reference
    private BidRepayPlanService bidRepayPlanService;
    @Reference
    private RosInfoService rosInfoService;
    @Reference
    private RosDepositInfoService rosDepositInfoService;
    @Reference
    private ConTypeService conTypeService;
    @Reference
    private VasRebatesRuleService vasRebatesRuleService;

    @Reference
    private DicDataService dicDataService;
    @Reference
    private ConInfoService conInfoService;
    @Reference
    private FundInfoService fundInfoService;
    @Autowired
    private JmsService jmsService;
    @Reference
    private QdzAccountService qdzAccountService;
    @Reference
    private QdzTransRecordService qdzTransRecordService;
    @Reference
    private BidInvestFacade bidInvestFacade;
    @Reference
    private BidInfoFacade bidInfoFacade;
    @Reference
    private BidInvestExchangeService bidInvestExchangeService;

    private final ThreadLocal<Boolean> neddRollBack = new NamedThreadLocal("neddRollBack");


    @Override
    public ResponseEntity<?> findBidInfoDetailVo(int bidInfoId) {
        BidInfoVO vo = bidInfoService.findBidInfoDetailVo(bidInfoId);
        if (vo == null) {
            return new ResponseEntity<>(ERROR, "标的详情不存在");
        }
        // 查询借款人ID用户信息
        if (vo.getBorrowerId() != null && vo.getBorrowerId() != 0) {
            RegUserDetail regUserDetailBorrower = regUserDetailService.findRegUserDetailByRegUserId(vo.getBorrowerId());
            vo.setBorrowerName(regUserDetailBorrower.getRealName());
        }
        // 查询本金接收人员信息
        if (vo.getReceiptUserId() != null && vo.getReceiptUserId() != 0) {
            RegUserDetail regUserDetailgetReceiptUser = regUserDetailService
                    .findRegUserDetailByRegUserId(vo.getReceiptUserId());
            vo.setReceiptUserName(regUserDetailgetReceiptUser.getRealName());
        }
        //查询企业账户信息
        if(vo.getCompanyId() != null){
            RegUserDetail userDetail = regUserDetailService
            .findRegUserDetailByRegUserId(vo.getCompanyId());
            vo.setCompanyName(userDetail !=null ? userDetail.getRealName() : "");
        }

        //设置总付利息
        BigDecimal interestAmount=BigDecimal.ZERO;
        if (BaseUtil.equelsIntWraperPrimit(vo.getBiddRepaymentWay(), REPAYTYPE_INTEREST_CAL_DAY_REPAY_MONTH)) {
            //按月付息，按日计息的标的总付利息计算方式
            Date currentDate = new Date();
            int calDay = 0 ;
            for (int i = 1; i <= vo.getTermValue(); i++) {
                BigDecimal totalAmount = vo.getTotalAmount();
                //本月天数 = 本期结束日期 - 放款日期 - 已处理过的天数
                Date endDate = DateUtils.addMonth(currentDate,i);
                int thisMonthDay = DateUtils.getDaysBetween(currentDate,endDate) - calDay;
                interestAmount=interestAmount.add(totalAmount.multiply(vo.getInterestRate()).multiply(new BigDecimal(thisMonthDay)).divide(new BigDecimal(36000), 2,BigDecimal.ROUND_HALF_UP));
                calDay = calDay + thisMonthDay;
            }
        }else{
            interestAmount = CalcInterestUtil.calcInterest(vo.getTermUnit(), vo.getTermValue(),
                    vo.getTotalAmount(), vo.getInterestRate());
        }
        vo.setInterestAmount(interestAmount);
        return new ResponseEntity<>(SUCCESS, vo);
    }

    @Override
    public ResponseEntity<?> findBidInfoDetailVoList(Pager pager, BidInfoVO vo) {
        if (StringUtils.isNotBlank(vo.getBorrowerName())) {
            List<Integer> ids = new ArrayList<>();
            RegUserDetail regUserDetail = new RegUserDetail();
            regUserDetail.setRealName(vo.getBorrowerName());
            List<RegUserDetail> list = this.regUserDetailService.findRegUserDetailList(regUserDetail);
            if (list.isEmpty()) {
                return BaseUtil.emptyResult();
            }
            list.forEach(tmp -> ids.add(tmp.getRegUserId()));
            vo.setBorrowerIds(ids);
        }
        Pager result = this.bidInfoService.findBidInfoDetailVoList(pager, vo);

        if (!BaseUtil.resultPageHasNoData(result)) {
            result.getData().forEach(tmp -> {
                BidInfoVO bidVOTemp = (BidInfoVO) tmp;
                RegUserDetail regUserDetail = this.regUserDetailService
                        .findRegUserDetailByRegUserId(((BidInfoVO) tmp).getBorrowerId());
                if (regUserDetail != null) {
                    bidVOTemp.setBorrowerName(regUserDetail.getRealName());
                }

                //设置本息
                if (Arrays.asList(BID_STATE_WAIT_REPAY,BID_STATE_WAIT_FINISH).contains(bidVOTemp.getState())) {
                    //如果是已经放款，计算本息
                    bidVOTemp.setSumRepay(bidRepayPlanService.findSumRepayAtmByBidId(bidVOTemp.getId()));
                }

                //row.state==0 || row.state==1 && (row.totalAmount == row.residueAmount)
                boolean stateCondition = (equelsIntWraperPrimit(bidVOTemp.getState(),0) || equelsIntWraperPrimit(bidVOTemp.getState() , InvestConstants.BID_STATE_WAIT_NEW));
                boolean moneyConition = bidVOTemp.getTotalAmount().equals(bidVOTemp.getResidueAmount());

                if (bidVOTemp.getModifyAble()) {
                    if (!(stateCondition && moneyConition)) {
                        bidVOTemp.setModifyAble(false);
                    }
                }

            });
        }
        return new ResponseEntity<>(SUCCESS, result);
    }


    @Override
    public Pager findBidVoListForMatch(Pager pager, BidInfoVO vo) {
        if (StringUtils.isNotBlank(vo.getBorrowerName())) {
            List<Integer> ids = new ArrayList<>();
            RegUserDetail regUserDetail = new RegUserDetail();
            regUserDetail.setRealName(vo.getBorrowerName());
            List<RegUserDetail> list = this.regUserDetailService.findRegUserDetailList(regUserDetail);
            if (!list.isEmpty()) {
                list.forEach(tmp -> ids.add(tmp.getRegUserId()));
            }
            vo.setBorrowerIds(ids);
        }
        Pager result = this.bidInfoService.findBidInfoDetailVoList(pager, vo);
        if (CommonUtils.isNotEmpty(result.getData())) {
            result.getData().forEach(tmp -> {
                RegUserDetail regUserDetail = this.regUserDetailService
                        .findRegUserDetailByRegUserId(((BidInfoVO) tmp).getBorrowerId());
                if (regUserDetail != null) {
                    ((BidInfoVO) tmp).setBorrowerName(regUserDetail.getRealName());
                }
                BigDecimal interestAmount = CalcInterestUtil.calcInterest(((BidInfoVO) tmp).getTermUnit(),
                        ((BidInfoVO) tmp).getTermValue(), ((BidInfoVO) tmp).getTotalAmount(),
                        ((BidInfoVO) tmp).getInterestRate());
                ((BidInfoVO) tmp).setInterestAmount(interestAmount);
                // 查询此标的是否存在匹配
                List<BidMatchVo> resultList;
                BidMatchVo contidion = new BidMatchVo();
                if (BidInfoUtil.isCommonBid(((BidInfoVO) tmp).getProductType())) {
                    contidion.setCommonBidId(((BidInfoVO) tmp).getId());
                } else {
                    contidion.setGoodBidId(((BidInfoVO) tmp).getId());
                }
                resultList = bidMatchService.findMatchListByContidion(contidion);
                if (CommonUtils.isNotEmpty(resultList)) {
                    ((BidInfoVO) tmp).setHasMatch(MATCH_STATUS_MATCHLIST_YES);
                } else {
                    ((BidInfoVO) tmp).setHasMatch(MATCH_STATUS_MATCHLIST_NO);
                }

            });
        }
        return result;
    }

    @Override
    @Compensable(cancelMethod = "updateBidStateForCancel", transactionContextEditor = DubboTransactionContextEditor.class)
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public ResponseEntity<?> updateBidState(Integer bidId, int chanState, String reason, Integer regUserId) {
        if (logger.isInfoEnabled()) {
            logger.info("updateBidState, 改变标的状态, 标的id: {}, 改变为状态: {}, 改变原因: {}, 操作用户ID: {}", bidId, chanState, reason, regUserId);
        }
        //检查标的原来的状态
        BidInfoVO orginBid = bidInfoService.findBidInfoDetailVo(bidId);

        ResponseEntity result;
        try {
            result = isResponseSuccess(result = updateBidInfoPreliminaryChecks(orginBid, chanState)/* 标的信息前置检查 */)
                    ? doUpdateBidState(orginBid, chanState, reason) : result;
            if (result.validSuc() && InvestConstants.BID_STATE_WAIT_INVEST == chanState) {
                try {
                    jmsService.sendMsg(InvestConstants.MQ_QUEUE_AUTO_INVEST, DestinationType.QUEUE, InvestConstants
                            .MQ_QUEUE_AUTO_INVEST, JmsMessageType.TEXT);
                } catch (Exception e) {
                    logger.info("updateBidState, 标的上架, 发送自动投资消息失败, 改变标的状态, 标的id: {}, 改变为状态: {}, 改变原因: {}, 操作用户ID: " +
                            "{}", bidId, chanState, reason, regUserId, e);
                }
            }
            return result;
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("updateBidState, 改变标的状态失败, 异常信息: {}", e);
            }
            throw new GeneralException("改变标的状态失败");
        }
    }


    /**
    *  @Description    ：tcc回滚方法
    *  @Method_Name    ：updateBidStateForCancel
    *  @param bidId
    *  @param chanState
    *  @param reason
    *  @param regUserId
    *  @return com.yirun.framework.core.model.ResponseEntity<?>
    *  @Creation Date  ：2018/5/31
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    @Compensable(cancelMethod = "updateBidStateForCancel", transactionContextEditor = DubboTransactionContextEditor.class)
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public ResponseEntity<?> updateBidStateForCancel(Integer bidId, int chanState, String reason, Integer regUserId) {
        try {
            // 3.满标待审核(审核成功或者拒绝)
            if ((BaseUtil.equelsIntWraperPrimit(chanState , BID_STATE_WAIT_LOAN )|| BaseUtil.equelsIntWraperPrimit(chanState ,BID_STATE_CHECK_REJECT))) {
                BidInfo orginBidInfo = bidInfoService.findBidInfoById(bidId);
                //设置标的的状态为满标待审核
                orginBidInfo.setState(BID_STATE_WAIT_AUDIT);
                bidInfoService.updateBidInfo(orginBidInfo);
                //标的详情可以不更新
            }
            return ResponseEntity.SUCCESS;
        }catch (Exception e){
            logger.error("tcc cancel updateBidStateForCancel error. 更新标的ID: {}, 需要改变的状态: {}\n 异常为: {}", bidId,chanState, e);
            throw new GeneralException("卡券还原失败");
        }
    }


    /**
    *  @Description    ：执行标的更新
    *  @Method_Name    ：doUpdateBidState
    *  @param orginBidInfoVo
    *  @param chanState
    *  @param reason
    *  @return com.yirun.framework.core.model.ResponseEntity
    *  @Creation Date  ：2018/4/24
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    private ResponseEntity doUpdateBidState(BidInfoVO orginBidInfoVo, int chanState, String reason) {
        BidInfo orginBidInfo = new BidInfo();
        BeanPropertiesUtil.splitProperties(orginBidInfoVo, orginBidInfo);
        Integer bidInfoId = orginBidInfo.getId();
       //step 1:更新标的信息
        this.bidInfoService.updateState(bidInfoId, chanState);
        //step 2:更新标的Detail信息
        if (StringUtils.isNotEmpty(reason)) {
            BidInfoDetail unUpdateDetail = new BidInfoDetail();
            unUpdateDetail.setBiddInfoId(bidInfoId);
            unUpdateDetail.setAutidNote(reason);
            this.bidInfoDetailService.updateBidInfoDetail(unUpdateDetail);

        }
        // 返回处理结果
        if (logger.isInfoEnabled()) {
            logger.info("更新标的状态成功,标的ID：{},更新后状态：{}", bidInfoId, chanState);
        }
        return ResponseEntity.SUCCESS;
    }

    /**
    *  @Description    ：满标审核拒绝回滚操作
    *  @Method_Name    ：doRollBackInvests
    *  @param operatorUserId
    *  @return boolean
    *  @Creation Date  ：2018/4/24
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    private boolean doRollBackInvests(Integer bidId, Integer operatorUserId) {
        BidInfo orginBidInfo = bidInfoService.findBidInfoById(bidId);
        if (logger.isInfoEnabled()) {
            logger.info("doRollBackInvests, 满标审核拒绝回滚操作, 标的信息: {}, 操作用户ID: {}", orginBidInfo,operatorUserId);
        }
        //审核流水
        FinTradeFlow unSavedFinTradeFlow;
        //资金划转记录
        List<FinFundtransfer> unSavedFundtransfers = new ArrayList<>();
        try {
            /**
             * step 1:逻辑删除本个标的所有的投资记录
             */
            BidInvest bidInvest = new BidInvest();
            Integer bindInfoId = orginBidInfo.getId();
            bidInvest.setBidInfoId(bindInfoId);

            List<BidInvest> bidInvestList = bidInvestService.findBidInvestList(bidInvest);
            List<Integer> investIds = new ArrayList<Integer>();
            /**
             * step 2:审核拒绝流水（只记录一条审核拒绝操作）->解冻用户资金->生成资金划转记录
             */
             unSavedFinTradeFlow = FinTFUtil.initFinTradeFlow(operatorUserId, bindInfoId,
                    orginBidInfo.getTotalAmount(), TRADE_TYPE_BID_CHECK_REJECT, PlatformSourceEnums.PC);
            //需要回滚得加息券id
            List<Integer> couponIdJs = new ArrayList<Integer>();
            bidInvestList.stream().forEach((invest) -> {
                investIds.add(invest.getId());
                // 生成资金划转
                unSavedFundtransfers.add(FinTFUtil.initFinFundtransfer(unSavedFinTradeFlow.getFlowId(),
                        invest.getRegUserId(), null,  invest.getInvestAmount(), TRANSFER_SUB_CODE_THAW));
                //如果存在加息，把加息券状态改为可用
                if(invest.getCouponIdJ()!=null && invest.getCouponIdJ().intValue() > 0){
                    couponIdJs.add(invest.getCouponIdJ());
                }
            });
            //回滚所有加息券状态为已领取
            if(CommonUtils.isNotEmpty(couponIdJs)){
                VasCouponDetail vDetail = new VasCouponDetail();
                vDetail.setIds(couponIdJs);
                vDetail.setState(VasCouponConstants.COUPON_DETAIL_SEND_ALREADY);
                //更新卡券
                BaseUtil.getTccProxyBean(VasCouponDetailService.class, getClass(),"auditBid").updateVasCouponDetail(vDetail);
            }
            // 执行更新
            BaseUtil.getTccProxyBean(FinConsumptionService.class, getClass(),"auditBid").updateAccountInsertTradeAndTransfer(unSavedFinTradeFlow, unSavedFundtransfers);
            BidInvest investCdt = new BidInvest();
            investCdt.setBidInfoId(bindInfoId);
            investCdt.setIds(investIds);
            investCdt.setState(INVEST_STATE_DEL);
            //回滚投资记录
            BaseUtil.getTccProxyBean(BidInvestService.class, getClass(),"auditBid").updateBidInvest(investCdt);
        } catch (Exception exception) {
            if (logger.isErrorEnabled()) {
                logger.error("doRollBackInvests, 审核拒绝标的失败, 异常信息: {}",exception);
            }
            throw new GeneralException("审核拒绝标的失败");
        }

        if (logger.isInfoEnabled()) {
            logger.info("doRollBackInvests, 审核拒绝标的成功, 标的信息: {}, 审核拒绝流水信息: {}, 用户资金划转信息: {}",orginBidInfo, unSavedFinTradeFlow,unSavedFundtransfers);
        }
        return true;
    }

    /**
    *  @Description    ：更改标的信息的前置检查
    *  @Method_Name    ：updateBidInfoPreliminaryChecks
    *  @param orginBid
    *  @param chanState
    *  @return com.yirun.framework.core.model.ResponseEntity
    *  @Creation Date  ：2018/4/24
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    private ResponseEntity updateBidInfoPreliminaryChecks(BidInfoVO orginBid, int chanState) {

        if (orginBid == null) {
            return new ResponseEntity<>(ERROR, "审核标的不存在");
        }

        int orginState = orginBid.getState();

        //step 2:判断要修改的状态
        //上架一个匹配标的
        //0.判断是否是匹配标的,如果是匹配标的,不允许上架
        boolean isMatchType=(BaseUtil.equelsIntWraperPrimit(chanState , InvestConstants.BID_STATE_WAIT_INVEST))
                && BaseUtil.equelsIntWraperPrimit(orginBid.getMatchType(), InvestConstants.BID_MATCH_TYPE_YES)
                && BaseUtil.equelsIntWraperPrimit(orginBid.getType(), InvestConstants.BID_TYPE_COMMON);
        if (isMatchType) {
            return new ResponseEntity<>(ERROR, "匹配标的不允许上架");
        }
        // 1.上架一个标的或者删除一个标的
        boolean putOnOrDeleteFlag = (BaseUtil.equelsIntWraperPrimit(chanState,InvestConstants.BID_STATE_WAIT_INVEST )||
                BaseUtil.equelsIntWraperPrimit(chanState ,InvestConstants.BID_STATE_DELETE))
                && !BaseUtil.equelsIntWraperPrimit(orginState , InvestConstants.BID_STATE_WAIT_NEW);
        if (putOnOrDeleteFlag) {
            return new ResponseEntity<>(ERROR, "当前标的不是待上架状态不允许上架");
        }

        // 2.下架一个标的
        if (BaseUtil.equelsIntWraperPrimit(chanState,InvestConstants.BID_STATE_WAIT_NEW)) {
            BidInvest bidInvest = new BidInvest();
            bidInvest.setBidInfoId(orginBid.getId());
            // 判断标的是否有人投资
            if (bidInvestService.findBidInvestCount(bidInvest) > 0) {
                return new ResponseEntity<>(ERROR, "当前标的已经有人投资，不允许下架");
            }
        }

        // 3.满标待审核(审核成功或者拒绝)
        if ((BaseUtil.equelsIntWraperPrimit(chanState , BID_STATE_WAIT_LOAN )|| BaseUtil.equelsIntWraperPrimit(chanState ,BID_STATE_CHECK_REJECT))) {
            if (!BaseUtil.equelsIntWraperPrimit(orginState , InvestConstants.BID_STATE_WAIT_AUDIT)) {
                return new ResponseEntity<>(ERROR, "当前标的未满标，不允许审核");
            }
            // 设置是否需要回滚的状态为
            if (BaseUtil.equelsIntWraperPrimit(chanState ,BID_STATE_CHECK_REJECT)) {
                if (orginBid.getMatchType()==BID_MATCH_TYPE_YES){
                    return new ResponseEntity<>(ERROR,"此标的不允许拒绝");
                }
                neddRollBack.set(TRUE);
            }

        }
        return ResponseEntity.SUCCESS;

    }

    @Override
    public ResponseEntity<?> updateCreditorProperty(BidInfoDetail bidInfoDetail, RegUser regUser) {
        if (bidInfoDetail != null && bidInfoDetail.getBiddInfoId()!=null && bidInfoDetail.getBiddInfoId()>0) {
            if (bidInfoDetail.getTransferDays()<=0){
                return new ResponseEntity<>(Constants.ERROR, "筹集天数至少为1天");
            }
            BidInvest cdt = new BidInvest();
            cdt.setBidInfoId(bidInfoDetail.getBiddInfoId());
            cdt.setTransState(INVEST_CREDITOR_STATE_YES);
            List<BidInvest> list =  bidInvestService.findBidInvestList(cdt);
            if (bidInfoDetail.getCreditorState() == INVEST_CREDITOR_STATE_NO &&  CommonUtils.isNotEmpty(list)){
                return new ResponseEntity<>(Constants.ERROR, "此标的不能修改为不允许转让状态");
            }
            bidInfoDetail.setModifyTime(new Date());
            bidInfoDetail.setModifiedUserId(regUser.getId());
            bidInfoDetailService.updateBidInfoDetail(bidInfoDetail);
            return new ResponseEntity<>(Constants.SUCCESS, "更新债权转让信息成功");
        }
        return new ResponseEntity<>(Constants.ERROR, "传递参数有误");
    }

    @Override
    public ResponseEntity<?> findBidForInvest(RegUser regUser, Integer id) {
        ResponseEntity<?> result = new ResponseEntity<>(Constants.SUCCESS);
        //检索标的和详情信息
        BidInfoVO bidInfo = this.bidInfoService.findBidInfoDetailVo(id);
        if(null != bidInfo && bidInfo.getRaiseRate() != null && CompareUtil.gtZero(bidInfo.getRaiseRate())){
            bidInfo.setBaseRate(bidInfo.getInterestRate().subtract(bidInfo.getRaiseRate()));
        }
        if (this.rosInfoService.validateRoster(regUser.getId(), RosterType.INVEST_CTL, RosterFlag.BLACK)) {
            bidInfo.setState(InvestConstants.BID_STATE_WAIT_AUDIT);
        }
        //产品描述，如果项目信息为null，取产品描述作为项目信息
        bidInfo.setProjectDescription(initSynopsis(bidInfo.getProjectDescription(), bidInfo.getDescription(), bidInfo.getProductType()));
        result.getParams().put("bidInfo", bidInfo);
        //体验金->查询总得体验金金额
        if (bidInfo.getProductType() == InvestConstants.BID_PRODUCT_EXPERIENCE) {
            VasSimRecord vasSimRecord = new VasSimRecord();
            vasSimRecord.setRegUserId(regUser.getId());
            vasSimRecord.setState(VasConstants.VAS_SIM_STATE_INIT);
            vasSimRecord.setExpireTimeBegin(new Date());
            result.getParams().put("simAmount", this.vasSimRecordService.findSimSumMoney(vasSimRecord));
        }
        //查询账户信息
        FinAccount finAccount = this.finAccountService.findByRegUserId(regUser.getId());
        result.getParams().put("useableMoney", finAccount.getUseableMoney());
        //查询卡券信息
        if (bidInfo.getProductType() != InvestConstants.BID_PRODUCT_EXPERIENCE
                && bidInfo.getProductType() != InvestConstants.BID_PRODUCT_BUYHOUSE
                && bidInfo.getProductType() != InvestConstants.BID_PRODUCT_PROPERTY) {
            //加载加息券、投资红包
            Map<String, Object> map = this.vasCouponDetailService.getUserInvestUsableCoupon(bidInfo.getProductType(), regUser.getId());
            result.getParams().put("kList", map.get("redPacketsCouponList"));
            result.getParams().put("jList", map.get("interestCouponList"));
        }
        //散标查询还款计划
        if (bidInfo.getProductType() == InvestConstants.BID_PRODUCT_COMMNE) {
            BidRepayPlan bidRepayPlan = new BidRepayPlan();
            bidRepayPlan.setBidId(id);
            result.getParams().put("bidRepayPlanList", this.bidRepayPlanService.findBidRepayPlanList(bidRepayPlan));
        }
        boolean isExchange  =  this.rosInfoService.validateRoster(regUser.getId(), RosterType.EXCHANGE_INVEST_SEACH, RosterFlag.WHITE);
        if (isExchange){
            List<BidInvestExchange>  exchangeInvestList = bidInvestExchangeService.findExchangeInvestListByBidId(id);
            result.getParams().put("investPersonSum", CommonUtils.isNotEmpty(exchangeInvestList)?exchangeInvestList.size():0);
            result.getParams().put("bidInvestList", exchangeInvestList);
        }else{
            //加载投资记录
            BidInvest condition = new BidInvest();
            condition.setBidInfoId(id);
            condition.setInvestChannel(InvestConstants.BID_INVEST_CHANNEL_IMMEDIATE);
            condition.setStateList(Arrays.asList(INVEST_STATE_SUCCESS,INVEST_STATE_MANUAL,INVEST_STATE_TRANSFER,INVEST_STATE_SUCCESS_BUYER));
            List<BidInvest>  bidInvestList = this.bidInvestService.findBidInvestListByCondition(condition);
            //投资金额 = 投资金额 - 已转出金额
            bidInvestList.forEach(invest -> invest.setInvestAmount(invest.getInvestAmount().subtract(invest.getTransAmount())));
            bidInvestList.sort(Comparator.comparing(BidInvest::getCreateTime).reversed());
            result.getParams().put("bidInvestList", bidInvestList);
            result.getParams().put("investPersonSum", bidInvestList.size());
        }
        //设置万元收益
        result.getParams().put("millionIncome", CalcInterestUtil.calcInterest(bidInfo.getTermUnit(), bidInfo.getTermValue(), BigDecimal.valueOf(10000D), bidInfo.getInterestRate()));
        return result;
    }

    @Override
    public ResponseEntity<?> findBidInfoByBorrowCode(BidInfoSimpleVo bidInfoSimpleVo, Pager pager) {
        //查询标的列表
        Pager resultPager = bidInfoService.findBidInfoSimpleVoList(pager, bidInfoSimpleVo);
        if (!BaseUtil.resultPageHasNoData(resultPager)) {
            List<BidInfoSimpleVo> bidList = (List<BidInfoSimpleVo>) resultPager.getData();
            List<Integer> bidIds = new ArrayList<Integer>();
            bidList.forEach(bid -> {
                if (bid.getState() == BID_STATE_WAIT_REPAY || bid.getState() == BID_STATE_WAIT_FINISH) {
                    bidIds.add(bid.getId());
                }
            });
            //查询标的最后一次还款日
            if (CommonUtils.isNotEmpty(bidIds)) {
                List<BidRepayPlan> planList = bidRepayPlanService.findLastRepayPlanTimeByIds(bidIds);
                if (CommonUtils.isNotEmpty(planList)) {
                    bidList.forEach(bid -> {
                        planList.forEach(plan -> {
                            if (bid.getId().equals(plan.getBidId())) {
                                bid.setEndDate(plan.getPlanTime());
                            }
                        });
                    });
                }
                resultPager.setData(bidList);
            }
        }
        //查询截至日期
        return new ResponseEntity<>(SUCCESS, resultPager);
    }


    @Override
    public ResponseEntity<?> findBidVoListForInvest(Pager pager, BidInfoVO vo, RegUser regUser) {
        //包含pc端展示的标的
        vo.setShowPosition(String.valueOf(BID_SHOW_POSITION_PC));
        //执行查询
        pager.setPageSize(Integer.MAX_VALUE);
        Pager result = this.bidInfoService.findBidInfoVOByCondition(vo, pager);

        List<BidInfoVO> allBidInfo = (List<BidInfoVO>)result.getData();
        result.setData(allBidInfo.stream().filter(e -> CompareUtil.gt(e.getTotalAmount(),new BigDecimal(2000))).collect(Collectors.toList()));
        allBidInfo = (List<BidInfoVO>)result.getData();

        List<BaseBidInfoVO> showResult = new ArrayList<>();
        //按照标的类型分组
        Map<Integer, List<BidInfoVO>> typedGroupCommBidInfo = allBidInfo.stream().collect(Collectors.groupingBy(BidInfoVO::getType));
        // 新手标
        bidInfoSelectMaker(showResult, allBidInfo, null, Arrays.asList(BID_PRODUCT_PREFERRED),3, false, BidInfoVO::getProductType);
        //爆款标的
        bidInfoSelectMaker(showResult, null, typedGroupCommBidInfo, Arrays.asList(BID_TYPE_HOT), 3, true, null);
        //推荐标的
        bidInfoSelectMaker(showResult, null, typedGroupCommBidInfo, Arrays.asList(BID_TYPE_RECOMMEND), 3, true, null);
        //移除爆款
        typedGroupCommBidInfo.remove(BID_TYPE_HOT);
        //移除推荐
        typedGroupCommBidInfo.remove(BID_TYPE_RECOMMEND);
        //剔除推荐和爆款之后再次分组-剩下的全部是正常标
        List<BidInfoVO> afterHotAndRecommendRemoved = typedGroupCommBidInfo.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
        //处理月月盈,季季盈,年年盈,散标
        bidInfoSelectMaker(showResult, afterHotAndRecommendRemoved, null, Arrays.asList(BID_PRODUCT_WINMONTH/*月月盈*/, BID_PRODUCT_WINSEASON/*季季盈*/,
                BID_PRODUCT_WINYEAR/*年年盈*/),3, false, BidInfoVO::getProductType);
        // 处理散标
        bidInfoSelectMaker(showResult, afterHotAndRecommendRemoved, null, Arrays.asList(BID_PRODUCT_COMMNE /*散标*/),3, false, BidInfoVO::getProductType);
        // 体验金  数量不受限制
        bidInfoSelectMaker(showResult, afterHotAndRecommendRemoved, null, Arrays.asList(BID_PRODUCT_EXPERIENCE),Integer.MAX_VALUE, true, BidInfoVO::getProductType);
        // 购房宝、物业宝
        bidInfoSelectMaker(showResult, afterHotAndRecommendRemoved, null, Arrays.asList(BID_PRODUCT_BUYHOUSE,BID_PRODUCT_PROPERTY), 3, false, BidInfoVO::getProductType);
        // 海外产品
        bidInfoSelectMaker(showResult, afterHotAndRecommendRemoved, null, Arrays.asList(BID_PRODUCT_OVERSEA), 3, false, BidInfoVO::getProductType);

        // 过滤新手标、月月盈、年年盈与爆款标、推荐标的互斥关系
        List<BaseBidInfoVO> baseBidInfoVOList = showResult.stream().filter((e) -> {
            if (vo.getType() != null && !BaseUtil.equelsIntWraperPrimit(vo.getType(), BID_TYPE_COMMON)) {
                return BaseUtil.equelsIntWraperPrimit(e.getType(), vo.getType());
            } else if (vo.getProductType() != null) {
                return BaseUtil.equelsIntWraperPrimit(e.getProductType(), vo.getProductType())
                        && BaseUtil.equelsIntWraperPrimit(e.getType(), BID_TYPE_COMMON);
            } else {
                return true;
            }
        }).collect(Collectors.toList());

        result.setData(baseBidInfoVOList);
        ResponseEntity<?> res = new ResponseEntity<>(Constants.SUCCESS, result);
        //购房宝标的显示逻辑
        if (regUser != null && vo.getProductTypeLimitIds() != null && vo.getProductTypeLimitIds().size() >= 1 ||
                regUser != null && vo.getProductType() != null) {
            //查询此用户是否有资格查看购房宝标的信息
            RosDepositInfo rosDepositInfo = new RosDepositInfo();
            rosDepositInfo.setRegUserId(regUser.getId());
            rosDepositInfo.setState(1);
            //查询用户可以看的购房宝的标的
            List<RosDepositInfo> rosList = this.rosDepositInfoService.findRosDepositInfoList(rosDepositInfo);
            List<Integer> bidIdList = new ArrayList<Integer>();
            rosList.stream().forEach(ros-> bidIdList.add(ros.getBidId()));
            //过滤用户可以看到的购房宝标的
            List<BaseBidInfoVO> fromBaseBidInfoVoList = (List<BaseBidInfoVO>)result.getData();
            List<BaseBidInfoVO> backBaseBidInfoVoList = fromBaseBidInfoVoList.stream().filter(bidInfo->{
                if(BaseUtil.equelsIntWraperPrimit(BID_PRODUCT_BUYHOUSE, bidInfo.getProductType()) ||  BaseUtil.equelsIntWraperPrimit(BID_PRODUCT_PROPERTY, bidInfo.getProductType())){
                    if(!bidIdList.contains(bidInfo.getId())){
                        return false;
                    }
                }
                return true;
            }).collect(Collectors.toList());
            result.setData(backBaseBidInfoVoList);
            res.getParams().put("gfbFlag", this.rosDepositInfoService.findRosDepositInfoCount(rosDepositInfo));
        }

        // 散标显示逻辑  -- 单独查询
        BidInfoVO infoVO = new BidInfoVO();
        infoVO.setProductType(BID_PRODUCT_COMMNE);
        infoVO.setShowPosition(String.valueOf(BID_SHOW_POSITION_PC));
        infoVO.setStates(Arrays.asList(BID_STATE_WAIT_INVEST, BID_STATE_WAIT_AUDIT, BID_STATE_WAIT_LOAN));
        infoVO.setMatchType(BID_MATCH_TYPE_NO);
        Pager pg = new Pager();
        pg.setPageSize(Integer.MAX_VALUE);
        List<BidInfoVO> commonBidInfos = (List<BidInfoVO>)this.bidInfoService.findBidInfoVOByCondition(infoVO, pg).getData();
        res.getParams().put("commonFlag", CollectionUtils.isEmpty(commonBidInfos) ? 0 : 1);
        return res;
    }


    @Override
    public ResponseEntity<?> findIndexBidInfo(RegUser currentUser) {
        //找到合适的标的信息
        List<BidInfoVO> allBidInfo = findAvailableBids(String.valueOf(InvestConstants.BID_SHOW_POSITION_PC));
        IndexBidInfoVO indexBidInfoVO = new IndexBidInfoVO();
        boolean isExchange  =  this.rosInfoService.validateRoster(currentUser.getId(), RosterType.EXCHANGE_INVEST_SEACH, RosterFlag.WHITE);
        //如果是交易所匹配白名单，不查询标的
        if (!BaseUtil.collectionIsEmpty(allBidInfo) && !isExchange) {
            //按照产品类型分组
            Map<Integer, List<BidInfoVO>> productTypedBidInfoVO = allBidInfo.stream().collect(Collectors.groupingBy(BidInfoVO::getProductType));
            //处理新手标的
            indexBidInfoVO.setGreenhandBid(initIndexBidInfoFromBidVOs(productTypedBidInfoVO.get(BID_PRODUCT_PREFERRED)));
            productTypedBidInfoVO.remove(BID_PRODUCT_PREFERRED);
            //处理体验金标的
            indexBidInfoVO.setExperienceBid(initIndexBidInfoFromBidVOs(productTypedBidInfoVO.get(BID_PRODUCT_EXPERIENCE)));
            productTypedBidInfoVO.remove(BID_PRODUCT_EXPERIENCE);

            Collection<List<BidInfoVO>> restBids = productTypedBidInfoVO.values();
            if (BaseUtil.collectionIsEmpty(restBids)) {
                return new ResponseEntity(SUCCESS, indexBidInfoVO);
            }

            //合并剩下的标的信息
            List<BidInfoVO> cleanUnUsedBidinfos = restBids.stream().flatMap((e) -> e.stream()).collect(Collectors.toList());
            Map<Integer, List<BidInfoVO>> typedGroupCommBidInfo = cleanUnUsedBidinfos.stream().collect(Collectors.groupingBy(BidInfoVO::getType));


            //找到爆款标的
            List<BidInfoVO> hotBids = typedGroupCommBidInfo.get(BID_TYPE_HOT);
            BaseBidInfoVO hotBid = initIndexBidInfoFromBidVOs(hotBids);
            if (hotBid!=null&&hotBid.getState().equals(InvestConstants.BID_STATE_WAIT_INVEST)) {
                indexBidInfoVO.setHotBid(hotBid);
            }
            //移除爆款
            typedGroupCommBidInfo.remove(BID_TYPE_HOT);

            //找到推荐标的
            List<BidInfoVO> recommendBids = typedGroupCommBidInfo.get(BID_TYPE_RECOMMEND);
            BaseBidInfoVO recommendBid = initIndexBidInfoFromBidVOs(recommendBids);
            if (recommendBid!=null&&recommendBid.getState().equals(InvestConstants.BID_STATE_WAIT_INVEST)) {
                indexBidInfoVO.setRecommendBid(recommendBid);
            }
            //移除推荐
            typedGroupCommBidInfo.remove(BID_TYPE_RECOMMEND);
            //剔除推荐和爆款之后再次分组-剩下的全部是正常标
            productTypedBidInfoVO = typedGroupCommBidInfo
                    .values().stream()
                    .flatMap(Collection::stream)
                    .collect(Collectors.groupingBy(BidInfoVO::getProductType));

            //处理还款方式栏目
            List<BaseBidInfoVO> cachedInfosByMonth = new ArrayList<>(10);
            List<BaseBidInfoVO> cachedInfosByOnce = new ArrayList<>(10);

            Arrays.asList(productTypedBidInfoVO.get(BID_PRODUCT_WINMONTH)/*月月盈*/,
                    productTypedBidInfoVO.get(BID_PRODUCT_WINSEASON)/*季季盈*/,
                    productTypedBidInfoVO.get(BID_PRODUCT_WINYEAR)/*年年盈*/)
                  .stream()
                  .forEach(temp -> {
                      if (!BaseUtil.collectionIsEmpty(temp)) {
                          Map<Integer, List<BidInfoVO>> repayedWayGroupBidInfo = temp.stream().collect(Collectors.groupingBy(BidInfoVO::getBiddRepaymentWay));
                          //按月付息
                          groupFromProductTypedBidInfo(repayedWayGroupBidInfo.get(REPAYTYPE_INTEREST_MONTH_PRINCIPAL_END), cachedInfosByMonth);
                          //一次本息
                          groupFromProductTypedBidInfo(repayedWayGroupBidInfo.get(REPAYTYPE_ONECE_REPAYMENT), cachedInfosByOnce);
                      }
                  });

            //设置按月付息
            indexBidInfoVO.setRepayByMonthBid(cachedInfosByMonth);
            //设置一次本息
            indexBidInfoVO.setRepayOnceBid(cachedInfosByOnce);

        }
        return new ResponseEntity(SUCCESS, indexBidInfoVO);
    }


    @Override
    public  Map<String,Object> findAppIndexBidInfo(RegUser loginUser) {
        Map<String,Object> bidsMap = new HashMap<>();
        //找到合适的标的信息
        List<BidInfoVO> allBidInfo = findAvailableBids(String.valueOf(InvestConstants.BID_SHOW_POSITION_APP));
        AppIndexBidInfoVO appIndexBidInfoVO = new AppIndexBidInfoVO();
        //按照标的类型分组
        Map<Integer, List<BidInfoVO>> typedGroupCommBidInfo = allBidInfo.stream().collect(Collectors.groupingBy(BidInfoVO::getType));
        //新手标
        bidInfoSelectMaker(appIndexBidInfoVO.getGreenhandBidInfo(), allBidInfo, null, Arrays.asList(BID_PRODUCT_PREFERRED),1, true, BidInfoVO::getProductType);
        //找到爆款标的
        bidInfoSelectMaker(appIndexBidInfoVO.getAppLinedBidsInfo(), null, typedGroupCommBidInfo, Arrays.asList(BID_TYPE_HOT), 1, true, BidInfoVO::getProductType);
        //找到推荐标的
        bidInfoSelectMaker(appIndexBidInfoVO.getAppLinedBidsInfo(), null, typedGroupCommBidInfo, Arrays.asList(BID_TYPE_RECOMMEND), 1, true, BidInfoVO::getProductType);
        //移除爆款
        typedGroupCommBidInfo.remove(BID_TYPE_HOT);
        //移除推荐
        typedGroupCommBidInfo.remove(BID_TYPE_RECOMMEND);
        //剔除推荐和爆款之后再次分组-剩下的全部是正常标
        List<BidInfoVO> afterHotAndRecommendRemoved = typedGroupCommBidInfo.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
        //处理月月盈,季季盈,年年盈标的
        bidInfoSelectMaker(appIndexBidInfoVO.getAppLinedBidsInfo(), afterHotAndRecommendRemoved, null, Arrays.asList(BID_PRODUCT_WINMONTH/*月月盈*/, BID_PRODUCT_WINSEASON/*季季盈*/, BID_PRODUCT_WINYEAR/*年年盈*/),
                1, false, BidInfoVO::getProductType);
        // 体验金
        bidInfoSelectMaker(appIndexBidInfoVO.getExperienceBidInfo(), afterHotAndRecommendRemoved, null, Arrays.asList(BID_PRODUCT_EXPERIENCE),1, true, BidInfoVO::getProductType);

        if(null != loginUser){
           showOrHideBidInfo(loginUser, appIndexBidInfoVO,bidsMap,1);
        }
        return bidsMap;
    }


    @Override
    public Map<String,Object> findBidInfoList(RegUser loginUser,int overseaFlag) {
        Map<String,Object> bidsMap = new HashMap<>();
        //如果是交易所标的白名单
        boolean isExchange  =  this.rosInfoService.validateRoster(loginUser.getId(), RosterType.EXCHANGE_INVEST_SEACH, RosterFlag.WHITE);
        if (isExchange){
            //查询交易所标的
            return findExchangeBidList();
        }

        // 海外投资
        if(overseaFlag == 1){
            return this.getOverseaBids(bidsMap);
        }
        //找到合适的标的信息
        List<BidInfoVO> allBidInfo = findAvailableBids(String.valueOf(InvestConstants.BID_SHOW_POSITION_APP));
        AppIndexBidInfoVO appIndexBidInfoVO = new AppIndexBidInfoVO();
        //按照标的类型分组
        Map<Integer, List<BidInfoVO>> typedGroupCommBidInfo = allBidInfo.stream().collect(Collectors.groupingBy(BidInfoVO::getType));
        //新手标
        bidInfoSelectMaker(appIndexBidInfoVO.getGreenhandBidInfo(), allBidInfo, null, Arrays.asList(BID_PRODUCT_PREFERRED),3, false, BidInfoVO::getProductType);
        //找到爆款标的
        bidInfoSelectMaker(appIndexBidInfoVO.getAppLinedBidsInfo(), null, typedGroupCommBidInfo, Arrays.asList(BID_TYPE_HOT), 3, true, BidInfoVO::getProductType);
        //找到推荐标的
        bidInfoSelectMaker(appIndexBidInfoVO.getAppLinedBidsInfo(), null, typedGroupCommBidInfo, Arrays.asList(BID_TYPE_RECOMMEND), 3, true, BidInfoVO::getProductType);
        //移除爆款
        typedGroupCommBidInfo.remove(BID_TYPE_HOT);
        //移除推荐
        typedGroupCommBidInfo.remove(BID_TYPE_RECOMMEND);
        //剔除推荐和爆款之后再次分组-剩下的全部是正常标
        List<BidInfoVO> afterHotAndRecommendRemoved = typedGroupCommBidInfo.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
        //处理月月盈,季季盈,年年盈标的
        bidInfoSelectMaker(appIndexBidInfoVO.getAppLinedBidsInfo(), afterHotAndRecommendRemoved, null, Arrays.asList(BID_PRODUCT_WINMONTH/*月月盈*/, BID_PRODUCT_WINSEASON/*季季盈*/, BID_PRODUCT_WINYEAR/*年年盈*/),
                3, false, BidInfoVO::getProductType);
        //体验金
        bidInfoSelectMaker(appIndexBidInfoVO.getExperienceBidInfo(), afterHotAndRecommendRemoved, null, Arrays.asList(BID_PRODUCT_EXPERIENCE),1, true, BidInfoVO::getProductType);
        
        //直投散标
        BidInfoVO infoVO = new BidInfoVO();
        infoVO.setProductType(BID_PRODUCT_COMMNE);
        infoVO.setShowPosition(String.valueOf(BID_SHOW_POSITION_APP));
        infoVO.setStates(Arrays.asList(BID_STATE_WAIT_INVEST, BID_STATE_WAIT_AUDIT, BID_STATE_WAIT_LOAN));
        infoVO.setMatchType(BID_MATCH_TYPE_NO);
        Pager pg = new Pager();
        pg.setPageSize(Integer.MAX_VALUE);
        List<BidInfoVO> commonBidInfos = (List<BidInfoVO>)this.bidInfoService.findBidInfoVOByCondition(infoVO, pg).getData();
        bidInfoSelectMaker(appIndexBidInfoVO.getCommonBidInfo(), commonBidInfos, null, Arrays.asList(BID_PRODUCT_COMMNE), 3, false, BidInfoVO::getProductType);
        if(null != loginUser){
            // showPosition : 1-首页，2-产品列表
            showOrHideBidInfo(loginUser, appIndexBidInfoVO,bidsMap,2);
        }
        return bidsMap;
    }

    /**
    *  @Description    ：查询交易所匹配标的
    *  @Method_Name    ：findExchangeBidList
    *
    *  @return  Map<String,Object>
    *  @Creation Date  ：2019/1/22
    *  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
    */
    private Map<String,Object> findExchangeBidList(){
        Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("resStatus",SUCCESS);
        resultMap.put("resMsg","查询成功");
        List<BidInfoExchangeForAppVo> bidList = bidInfoService.findBidInfoExchangeListForApp();
        if(CommonUtils.isNotEmpty(bidList)){
            bidList.forEach(appVo->{
                //biddRepaymentWayDesc  还款方式描述
                appVo.setBiddRepaymentWayDesc(dicDataService.findNameByValue(INVEST, REPAYMENT_MODE, appVo.getBiddRepaymentWay()));
                appVo.setHasStarted(true);
                appVo.setProgress(100);
                // 万元收益
                BigDecimal tenThousandIncome = CalcInterestUtil.calcInterest(appVo.getTermUnit(),appVo.getTermValue(),
                        BigDecimal.valueOf(10000),appVo.getInterestRate());
                appVo.setTenThousandIncome(tenThousandIncome);
            });
            resultMap.put("commonBidInfo",bidList);
        }else{
            resultMap.put("commonBidInfo",new ArrayList<BidInfoExchangeForAppVo>());
        }
        return resultMap;
    }
    /**
     *  @Description    ：获取海外投资标的信息
     *  @Method_Name    ：getOverseaBids
     *  @param bidsMap
     *  @return java.util.Map<java.lang.String,java.lang.Object>
     *  @Creation Date  ：2018年10月10日 11:22
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    private Map<String, Object> getOverseaBids(Map<String, Object> bidsMap) {
        BidInfoVO queryBidInfoVO = new BidInfoVO();
        //设置标的显示位置
        queryBidInfoVO.setShowPosition(String.valueOf(InvestConstants.BID_SHOW_POSITION_APP));
        //只查询投标中,已售罄之间状态的标的
        queryBidInfoVO.setStates(Arrays.asList(BID_STATE_WAIT_INVEST, BID_STATE_WAIT_AUDIT, BID_STATE_WAIT_LOAN, BID_STATE_WAIT_REPAY, BID_STATE_WAIT_FINISH));
        //只查询上架中的产品类型
        queryBidInfoVO.setProductState(InvestConstants.PRODUCT_STATE_ON);
        //标的产品类型:海外产品
        queryBidInfoVO.setProductTypeLimitIds(Arrays.asList(BID_PRODUCT_OVERSEA));
        //标的类型:正常标
        queryBidInfoVO.setBidTypeLimitIds(Arrays.asList(BID_TYPE_COMMON));
        List<BidInfoVO> bidInfoVOS = bidInfoService.findBidInfoVOByCondition(queryBidInfoVO);
        AppIndexBidInfoVO appIndexBidInfoVO = new AppIndexBidInfoVO();
        //处理海外产品
        bidInfoSelectMaker(appIndexBidInfoVO.getAppLinedBidsInfo(), bidInfoVOS, null, Arrays.asList(BID_PRODUCT_OVERSEA), 3, false, BidInfoVO::getProductType);
        if(!CollectionUtils.isEmpty(appIndexBidInfoVO.getAppLinedBidsInfo())){
            bidsMap.put("appLinedBidsInfo",appIndexBidInfoVO.getAppLinedBidsInfo());
        }
        return bidsMap;
    }

    /**
     *  @Description    ：首页标的展示限制
     *  @Method_Name    ：showOrHideBidInfo
     *  @param currentUser
     *  @param appIndexBidInfoVO
     *  @return void
     *  @Creation Date  ：2018年09月18日 13:46
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    private void showOrHideBidInfo(RegUser currentUser, AppIndexBidInfoVO appIndexBidInfoVO,Map<String,Object> bidsMap,Integer showPosition) {
/*        //首页每类标展示一个
        if(Integer.valueOf("1").equals(showPosition)){
            appIndexBidInfoVO.setAppLinedBidsInfo(filterAgain(appIndexBidInfoVO.getAppLinedBidsInfo()));
        }*/
        // 判断是否有相应标的，如果没有则不显示相应字段
        // 合并列表
        if(BaseUtil.equelsIntWraperPrimit(showPosition,1)){
            // 体验标
            //体验金
            if(appIndexBidInfoVO.getExperienceBidInfo().size() > 0) {
                appIndexBidInfoVO.getAppLinedBidsInfo().add(appIndexBidInfoVO.getExperienceBidInfo().get(0));
            }
        }else{
            // 散标
            appIndexBidInfoVO.getAppLinedBidsInfo().addAll(appIndexBidInfoVO.getCommonBidInfo());
            //体验金
            if(appIndexBidInfoVO.getExperienceBidInfo().size() > 0){
                appIndexBidInfoVO.getAppLinedBidsInfo().add(appIndexBidInfoVO.getExperienceBidInfo().get(0));
            }
        }
        if(!CollectionUtils.isEmpty(appIndexBidInfoVO.getAppLinedBidsInfo())){
            bidsMap.put("appLinedBidsInfo",appIndexBidInfoVO.getAppLinedBidsInfo());
        }
        // 新手标
        Integer count = this.bidInvestService.findBidInvestCountForPrefered(currentUser.getId());
        if((count <= 0 || this.rosInfoService.validateRoster(currentUser.getId(), RosterType.FLEDGLING_BID, RosterFlag.WHITE)) && !CollectionUtils.isEmpty(appIndexBidInfoVO.getGreenhandBidInfo())){
            bidsMap.put("greenhandBids",appIndexBidInfoVO.getGreenhandBidInfo());
        }
    }

    @Override
    public AppIndexBidInfoVO findPlusIndexBidsInfo(RegUser currentUser) {
        //找到合适的标的信息
        List<BidInfoVO> allBidInfo = findAvailableBids(String.valueOf(InvestConstants.BID_SHOW_POSITION_APP));
        AppIndexBidInfoVO appIndexBidInfoVO = new AppIndexBidInfoVO();
        //按照标的类型分组
        Map<Integer, List<BidInfoVO>> typedGroupCommBidInfo = allBidInfo.stream().collect(Collectors.groupingBy(BidInfoVO::getType));
        //找到爆款标的
        bidInfoSelectMaker(appIndexBidInfoVO.getAppLinedBidsInfo(), null, typedGroupCommBidInfo, Arrays.asList(BID_TYPE_HOT), 1, true, null);
        //找到推荐标的
        bidInfoSelectMaker(appIndexBidInfoVO.getAppLinedBidsInfo(), null, typedGroupCommBidInfo, Arrays.asList(BID_TYPE_RECOMMEND), 1, true, null);
        //移除爆款
        typedGroupCommBidInfo.remove(BID_TYPE_HOT);
        //移除推荐
        typedGroupCommBidInfo.remove(BID_TYPE_RECOMMEND);
        //剔除推荐和爆款之后再次分组-剩下的全部是正常标
        List<BidInfoVO> afterHotAndRecommendRemoved = typedGroupCommBidInfo.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
        //处理月月盈,季季盈标的
        bidInfoSelectMaker(appIndexBidInfoVO.getAppLinedBidsInfo(), afterHotAndRecommendRemoved, null, Arrays.asList(BID_PRODUCT_WINMONTH/*月月盈*/, BID_PRODUCT_WINSEASON/*季季盈*/, BID_PRODUCT_WINYEAR/*年年盈*/),
                1, false, BidInfoVO::getProductType);
        //APP隐藏标的信息
        if (currentUser != null) {
            hideBidInfoIfNeeded(currentUser, appIndexBidInfoVO);
        }
        return appIndexBidInfoVO;
    }


    @Override
    public Map<String, Object> findAppBidDetail(Integer bidId, final Integer regUserId, Integer fromPlace) {
        //查询标的信息
        BidInfoVO bidInfoVO = bidInfoService.findBidInfoDetailVo(bidId);
        //查询体验金
        Future<BigDecimal> simAmountFuture = null;
        if(bidInfoVO.getProductType().equals(InvestConstants.BID_PRODUCT_EXPERIENCE)){
            VasSimRecord vasSimRecord = new VasSimRecord();
            vasSimRecord.setRegUserId(regUserId);
            vasSimRecord.setState(VasConstants.VAS_SIM_STATE_INIT);
            vasSimRecord.setExpireTimeBegin(new Date());
            simAmountFuture = SingletonThreadPool.callCachedThreadPool(() -> this.vasSimRecordService.findSimSumMoney(vasSimRecord));
        }
        
        //查询购买记录
        BidInvest queryBidInvest = new BidInvest();
        queryBidInvest.setBidInfoId(bidId);
        queryBidInvest.setInvestChannel(InvestConstants.BID_INVEST_CHANNEL_IMMEDIATE);
        List<BidInvest> resultBidInvestList = bidInvestService.findBidInvestList(queryBidInvest);
        List<BidInvest> bidInvestLists = new ArrayList<BidInvest>();
        if (CommonUtils.isNotEmpty(resultBidInvestList)){
            for (BidInvest invest:resultBidInvestList){
                if (invest.getState()==InvestConstants.INVEST_STATE_SUCCESS ||
                        invest.getState()==InvestConstants.INVEST_STATE_SUCCESS_BUYER  ||
                             invest.getState()==InvestConstants.INVEST_STATE_TRANSFER){
                    invest.setInvestAmount(invest.getInvestAmount().subtract(invest.getTransAmount()));
                    bidInvestLists.add(invest);
                }
            }
        }
        //对投资记录进行排序
        bidInvestLists.sort(Comparator.comparing(BidInvest::getCreateTime).reversed());
        AppResultUtil.ExtendMap extendMap = AppResultUtil.mapOfListInProperties(bidInvestLists,
                0, null, "realName", "createTime", "investAmount")
                                                         .processObjInList(thisMap ->
                                                         {
                                                             String realName = (String) thisMap.get("realName");
                                                             if (StringUtils.isNotEmpty(realName)) {
                                                                 thisMap.put("realName", realName.substring(0, 1).concat("**"));
                                                             }
                                                         });
        //标的信息中需要保留的字段
        String[] includeProperties = {"interestRate"/*年化率*/, "raiseRate"/*已加息利率*/, "givingPointState"/*赠送积分*/, 
                "recommendState"/*好友推荐*/, "allowCoupon"/*支持卡券*/, "stepValue"/*投资步长*/, "termValue"/*投资期限值*/,
                "termUnit"/*投资期限单位*/, "residueAmount"/*剩余可投*/,
                "title"/*项目名称*/, "totalAmount"/*标的总金额*/, "bidScheme"/*招标方案*/, "bidSchemeValue"/*招标方案值*/,
                "biddRepaymentWay"/*还款方式*/, "productType", "startTime"
        };

        //返回响应的信息
        Date currentDate = new Date();
        //预计开始时间
        Date expectedstartDate = DateUtils.addDays(currentDate, 1);
        //期满结束时间
        Date expectedEndDate = BidInfoUtil.getFinishTime(expectedstartDate, bidInfoVO.getTermValue(), bidInfoVO.getTermUnit());
        //获取描述
        Function<Object,Object> getKeyDesc = (key) -> dicDataService.findNameByValue(INVEST, REPAYMENT_MODE, (Integer) key);

        //处理投资步长,如果是是平均招标方案，那么投资步长即为招标方案值
        if (BaseUtil.equelsIntWraperPrimit(bidInfoVO.getBidScheme(),0)) {
            bidInfoVO.setStepValue(bidInfoVO.getBidSchemeValue());
        }

        String viewOriginProjectSwitch =  bidInfoService.getViewOriginProjectSwitch(bidInfoVO);
        AppResultUtil.ExtendMap resultMap = AppResultUtil.successOfInProperties(bidInfoVO, null, includeProperties)
                                                          .addResParameter("bidId", bidInfoVO.getId())
                                                          .addResParameter("systemTime", currentDate)
                                                          .addResParameter("expectedStartDate", expectedstartDate)
                                                          .addParameterDesc("biddRepaymentWay", getKeyDesc)
                                                          .addResParameter("expectedEndDate", expectedEndDate)
                                                          .addResParameter("viewOriginProjectSwitch", viewOriginProjectSwitch)
                                                          .addResParameter("investList", extendMap.get(DATA_LIST))//投资记录
                                                          .addResParameter("synopsis", initSynopsis(bidInfoVO.getProjectDescription(), bidInfoVO.getDescription(), bidInfoVO.getProductType()))//项目描述
                                                          .addResParameter("safeMessage", initSafeMessage())//获取保障计划
                                                          .addResParameter("contracts", conInfoService.initContracts(bidInfoVO.getContract(), fromPlace));//初始化合同类型
        //到期还本付息,增加提示信息
        String alertStr = BaseUtil.equelsIntWraperPrimit(bidInfoVO.getBiddRepaymentWay(), InvestConstants.REPAYTYPE_ONECE_REPAYMENT) ?
                "此标的还款方式为：到期一次还本付息（即到期后一次性返还本金和收益），您是否要继续投资？" : "";
        resultMap.addResParameter("alertMsg", alertStr);
        resultMap.addResParameter("baseRate", bidInfoVO.getInterestRate().subtract(bidInfoVO.getRaiseRate()));

        String investRequire = "平均金额招标(建议5000.00元起投)";
        if(bidInfoVO.getBidScheme() == InvestConstants.BID_SCHEME_MIN){
            investRequire = "最低金额招标(建议";
            investRequire = investRequire + (bidInfoVO.getBidSchemeValue() > 5000 ? bidInfoVO.getBidSchemeValue() : "5000.00") + "元起投";
            investRequire = investRequire + (bidInfoVO.getStepValue() > 0 ? ", 且以" + bidInfoVO.getStepValue() + "元倍数递增)" : ")");
        }
        resultMap.addResParameter("investRequire", investRequire.toString());
        //是否允许在app端投资
        resultMap.addResParameter("allowInvest", bidInfoVO.getInvestPosition().contains("2") ? 1 : 0);
        //查询体验金金额
        if(bidInfoVO.getProductType().equals(InvestConstants.BID_PRODUCT_EXPERIENCE)){
            VasSimRecord vasSimRecord = new VasSimRecord();
            vasSimRecord.setRegUserId(regUserId);
            vasSimRecord.setState(VasConstants.VAS_SIM_STATE_INIT);
            vasSimRecord.setExpireTimeBegin(new Date());
            resultMap.put("simAmount", this.vasSimRecordService.findSimSumMoney(vasSimRecord));
        }
        try {
            //查询用户体验金
            resultMap.put("simAmount", 0);
            if(bidInfoVO.getProductType().equals(InvestConstants.BID_PRODUCT_EXPERIENCE) && simAmountFuture != null){
                resultMap.put("simAmount", simAmountFuture.get());
            }
        } catch (Exception e) {
            throw new GeneralException("数据加载异常");
        } 
        return resultMap;
    }

    @Override
    public Map<String, Object> filterAppProductBidInfo(Integer termValue, Integer bidType) {
        return initModelProductBidInfo(termValue, bidType,false);

    }


    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public ResponseEntity saveBid(BidInfoVO bidInfoVO, Integer currentUserId) {
        VasRebatesRule vasRebatesRule = vasRebatesRuleService.findVasRebatesRuleByTypeAndState(
                VasRuleTypeEnum.RECOMMEND.getValue(), VAS_RULE_STATE_START);
        return bidInfoService.saveInfoAndDetail(bidInfoVO, currentUserId, vasRebatesRule == null ? null : vasRebatesRule.getId());
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public ResponseEntity updateBidInfoAndDetail(BidInfoVO bidInfoVO, Integer currentUserId) {
        VasRebatesRule vasRebatesRule = vasRebatesRuleService.findVasRebatesRuleByTypeAndState(
                VasRuleTypeEnum.RECOMMEND.getValue(), VAS_RULE_STATE_START);
        return  bidInfoService.updateBidInfoAndDetail(bidInfoVO,currentUserId, vasRebatesRule == null ? 0 : vasRebatesRule.getId());
    }

    @Override
    public Map<String, Object> filterProductBidInfoForPlus(Integer termValue, Integer bidType) {
        return initModelProductBidInfo(termValue, bidType,true);
    }

    @Override
    @Compensable
    public ResponseEntity auditBid(Integer bidId, int chanState, String reason, Integer userId) {
        logger.info("auditBid, 审核标的, 标的ID: {}, 审核状态为: {}, 原因: {}, 操作用户ID: {}", bidId, chanState, reason, userId);
        try {
            //更新标的信息
            ResponseEntity<?> responseEntity =
                    BaseUtil.getTccProxyBean(BidInfoFacade.class, getClass(), "auditBid").updateBidState(bidId, chanState, reason, userId);
            if (!responseEntity.validSuc()) {
                return responseEntity;
            }
            //判断是否需要回滚
            Boolean neddRollBackFlag = neddRollBack.get();
            if (neddRollBackFlag!=null&&neddRollBackFlag) {
                //判断是否执行回滚逻辑
                doRollBackInvests(bidId, userId);
            }
            return new ResponseEntity(SUCCESS, "审核标的成功");
        }catch (Exception e){
            if (logger.isErrorEnabled()) {
                logger.error("auditBid, 审核标的, 标的ID: {}, 审核状态为: {}, 原因: {}, 操作用户ID: {}, 异常信息: {}", bidId, chanState, reason, userId,e);
            }
            throw new GeneralException("审核标的失败");
        }

    }

    /**
   *  @Description    ：如果有number个选取number个标的信息，否则全部选取
   *  @Method_Name    ：selectThreeOfThen
   *  @param bidInfoVOS
   *  @return java.util.List<com.hongkun.finance.invest.model.vo.BaseBidInfoVO>
   *  @Creation Date  ：2018/4/24
   *  @Author         ：zhongpingtang@hongkun.com.cn
   */
    private List<BaseBidInfoVO> selectNumberOfThen(List<BidInfoVO> bidInfoVOS,int number) {
        if (!BaseUtil.collectionIsEmpty(bidInfoVOS)) {
            //排序
            bidListSortRule(bidInfoVOS);
            //选取前三个
            List<BidInfoVO> selectedBids = bidInfoVOS.size() > number ? bidInfoVOS.subList(0, number) : bidInfoVOS;
            return initIndexBidInfoListFromBidVOs(selectedBids);
        }
        return Collections.EMPTY_LIST;
    }




   /**
   *  @Description    ：初始化合同
   *  @Method_Name    ：initContracts
   *  @param contractStr
   *  @param fromPlace  从什么地方查询标的详情， 1-首页 2-投资记录
   *                    如果是从首页不显示保障合同，从投资记录正常显示
   *                   默认为从投资记录，不隐藏
   * @return java.util.List
   *  @Creation Date  ：2018/4/24
   *  @Author         ：zhongpingtang@hongkun.com.cn
   */
    private List<?> initContracts(String contractStr, Integer fromPlace) {
        //获取
        String[] contracts = StringUtils.split(contractStr, ",");
        //是否要执行隐藏策略
        boolean needHide=BaseUtil.equelsIntWraperPrimit(fromPlace, ContractConstants.CONTRACT_LOCATION_BIDD);
        if (ArrayUtils.isNotEmpty(contracts)) {
            return Arrays.stream(contracts).map(type -> {
                Integer typeInt = Integer.valueOf(type);
                if (needHide && BaseUtil.equelsIntWraperPrimit(typeInt, ContractConstants.CONTRACT_TYPE_SECURITY_PLAN)) {
                    //本条合同不需要展示
                    return null;
                }
                ConType queryConType = new ConType();
                queryConType.setType(Integer.valueOf(type));
                queryConType.setState(ContractConstants.CONTRACT_STATE_Y);
                //查询具体合同
                List<ConType> conTypes = conTypeService.findConTypeList(queryConType);
                if (!BaseUtil.collectionIsEmpty(conTypes)) {
                    return conTypes.stream().findFirst()
                            .map(first -> new HashMap<String, Object>() {
                                {
                                    put("type", first.getType());
                                    put("text", first.getShowName());
                                }
                            }).orElse(null);

                }
                return null;
            }).filter(map->map!=null).collect(Collectors.toList());



        }
        return Collections.EMPTY_LIST;
    }

   /**
   *  @Description    ：初始化保障信息
   *  @Method_Name    ：initSafeMessage
   *
   *  @return java.util.List
   *  @Creation Date  ：2018/4/24
   *  @Author         ：zhongpingtang@hongkun.com.cn
   */
    private List initSafeMessage() {
        return Arrays.asList(new Pair("资金安全保障", "由央行监管的、专业的第三方支付机构为您的交易资金划转提供相应的服务，实现资金流转清晰、合法、可查，增加交易透明度及安全性。"),
                new Pair("电子合同保障", "鸿坤金服促成的交易均会在线上生成相应的电子合同及协议，明确投资人与融资人的债权债务关系。电子合同均由鸿坤金服及合作的律师团队制定。平台的数据及合同将通过与第三方数据保全机构合作的方式实现实时、完整性的第三方数据备份及保管，并可将所有数据通过公证机关进行全流程数据公证和加印国家授时中心统一时间，实现电子证据的固化和保全。"),
                new Pair("技术安全保障", "鸿坤金服有完善的系统安全内部控制制度，严格管理用户数据，从流程上实现用户隐私不被泄露，更不会将用户信息泄露给第三方机构，实现用户隐私不受侵犯。采用数字签名技术、安全套接协议，对用户与平台的网络访问进行加密，实现通信私密不被窃听，通过三层防火墙对各系统进行隔离，最大程度实现用户数据安全， 严格执行备份容灾计划，保证数据和系统在极端情况下不受影响。")
        );
    }

     /**
     *  @Description    ：初始化产品描述
     *  @Method_Name    ：initSynopsis
     *  @param projectDescription
     *  @param description
     *  @param productType
     *  @return java.lang.Object
     *  @Creation Date  ：2018/4/24
     *  @Author         ：zhongpingtang@hongkun.com.cn
     */
    private String initSynopsis(String projectDescription, String description, Integer productType) {
        //先取标地项目信息
        if (StringUtils.isNotBlank(projectDescription)) {
            return projectDescription.replaceAll("<br>", "");   //项目描述
            //再取标地产品项目信息
        } else if (StringUtils.isNotBlank(description)) {
            return description.replaceAll("<br>", "");  //项目描述
        } else {
            if (productType == 0) {
                return InvestConstants.BID_PRODUCT_TYPE_DESC_QKD;//项目描述
            } else {
                return InvestConstants.BID_PRODUCT_TYPE_DESC_YX;//项目描述
            }
        }
    }

    /**
    *  @Description    ：如果有必要需要隐藏标的信息
    *  @Method_Name    ：hideBidInfoIfNeeded
    *  @param currentUser
    *  @param appIndexBidInfoVO
    *  @return void
    *  @Creation Date  ：2018/5/2
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    private void hideBidInfoIfNeeded(RegUser currentUser, AppIndexBidInfoVO appIndexBidInfoVO) {
        if (this.rosInfoService.validateRoster(currentUser.getId(), RosterType.INVEST_CTL, RosterFlag.BLACK)) {
            List<BaseBidInfoVO> unHidedBidInfo = new ArrayList<>(16);

            unHidedBidInfo.add(appIndexBidInfoVO.getGreenhandBid());

            //隐藏推荐和爆款标
            appIndexBidInfoVO.setHotBid(null);
            appIndexBidInfoVO.setRecommendBid(null);
            unHidedBidInfo.addAll(appIndexBidInfoVO.getAppLinedBidsInfo());
            //此用户禁止投资，屏蔽标的信息
            hideBidInfoFromRoster(unHidedBidInfo);
        }
    }


    /**
    *  @Description    ：标的过滤器,根据特定规则，选择标的
    *  @Method_Name    ：bidInfoSelectMaker
    *  @param destList 目标List
    *  @param originsMaterial 原始材料
    *  @param selectProductType 选择产品类型
    *  @param selectNumber 选择数量
    *  @param bidStateMustOn 是否要求标的正在投标中的
    *  @param groupKey
     * @return java.util.List<com.hongkun.finance.invest.model.vo.BaseBidInfoVO>
    *  @Creation Date  ：2018/7/4
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    private List<BaseBidInfoVO> bidInfoSelectMaker(List<BaseBidInfoVO> destList,
                                                   List<BidInfoVO> originsMaterial,
                                                   Map<Integer, List<BidInfoVO>> finalGroupedSource,
                                                   List<Integer> selectProductType,
                                                   int selectNumber,
                                                   boolean bidStateMustOn,
                                                   Function<BidInfoVO, Integer> groupKey){
        //step1 对标的进行分组 ,如果有必要的话
        if (CollectionUtils.isEmpty(finalGroupedSource)&&groupKey!=null) {
            finalGroupedSource = originsMaterial.stream().collect(Collectors.groupingBy(groupKey));
        }

        //step2 获取必要的类型
        Map<Integer, List<BidInfoVO>> finalGroupedSource1 = finalGroupedSource;
        selectProductType.stream().forEach(bidType -> {
            //取出当前类型的标的集合
            List<BidInfoVO> currentTypeBids = finalGroupedSource1.get(bidType);
            if (!BaseUtil.collectionIsEmpty(currentTypeBids)) {
                if (bidStateMustOn) {
                    //过滤不是投资中的标的
                    currentTypeBids= currentTypeBids.stream().filter(e -> BaseUtil.equelsIntWraperPrimit(e.getState(), InvestConstants.BID_STATE_WAIT_INVEST)).collect(Collectors.toList());
                }
                //按照产品id(产品类型)去分组涉及到的产品 一次本息&创建时间排序
                Map<Integer, List<BidInfoVO>> innerProductTypedBidInfo=currentTypeBids.stream().collect(Collectors.groupingBy(BidInfoVO::getBidProductId));
                List<Integer> orderedProductId = new ArrayList(innerProductTypedBidInfo.keySet());
                if (!BaseUtil.collectionIsEmpty(orderedProductId)) {
                    orderedProductId= bidProductService.orderedByPayemntAndCreateTime(orderedProductId);
                }
                //按照指定的产品顺序,添加选定类型的标的
                orderedProductId.forEach((productId) -> destList.addAll(selectNumberOfThen(innerProductTypedBidInfo.get(productId), selectNumber)));
            }

        });
        return destList;
    }

  /**
  *  @Description    ：查询标的信息
  *  @Method_Name    ：findAvailableBids
  *  @param showPosition
  *  @return java.util.List<com.hongkun.finance.invest.model.vo.BidInfoVO>
  *  @Creation Date  ：2018/4/24
  *  @Author         ：zhongpingtang@hongkun.com.cn
  */
    private List<BidInfoVO> findAvailableBids(String showPosition) {
        BidInfoVO queryBidInfoVO = new BidInfoVO();
        //设置标的显示位置
        queryBidInfoVO.setShowPosition(showPosition);
        //只查询投标中,已售罄之间状态的标的
        //queryBidInfoVO.setStates(Arrays.asList(BID_STATE_WAIT_INVEST, BID_STATE_WAIT_AUDIT, BID_STATE_WAIT_LOAN, BID_STATE_WAIT_REPAY, BID_STATE_WAIT_FINISH));
        queryBidInfoVO.setStates(Arrays.asList(BID_STATE_WAIT_INVEST, BID_STATE_WAIT_AUDIT, BID_STATE_WAIT_LOAN));
        //限制查询
        //只查询上架中的产品类型
        queryBidInfoVO.setProductState(InvestConstants.PRODUCT_STATE_ON);
        //标的产品类型:新手标,月月赢,季季盈,年年盈,体验金
        queryBidInfoVO.setProductTypeLimitIds(Arrays.asList(
                BID_PRODUCT_PREFERRED/*新手标*/,
                BID_PRODUCT_WINMONTH/*月月赢*/,
                BID_PRODUCT_WINSEASON/*季季盈*/,
                BID_PRODUCT_WINYEAR/*年年盈*/,
                BID_PRODUCT_EXPERIENCE/*体验金*/));
        //标的类型
        queryBidInfoVO.setBidTypeLimitIds(Arrays.asList(BID_TYPE_COMMON, BID_TYPE_HOT, BID_TYPE_RECOMMEND));
        //还款方式 按月付息-到期还本，到期还本付息
        //queryBidInfoVO.setRepayWayLimitIds(Arrays.asList(REPAYTYPE_INTEREST_MONTH_PRINCIPAL_END, REPAYTYPE_ONECE_REPAYMENT));
        //去数据库中查询
        List<BidInfoVO> result = bidInfoService.findBidInfoVOByCondition(queryBidInfoVO);
        return  result.stream().filter(e -> CompareUtil.gt(e.getTotalAmount(),new BigDecimal(2000))).collect(Collectors.toList());
    }

    /**
    *  @Description    ：屏蔽标的信息
    *  @Method_Name    ：hideBidInfoFromRoster
    *  @param unHidedBidInfo
    *  @return void
    *  @Creation Date  ：2018/4/24
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    private void hideBidInfoFromRoster(List<? extends BaseBidInfoVO> unHidedBidInfo) {
        unHidedBidInfo.stream().forEach(e -> {
            //设置剩余金额为0
            e.setResidueAmount(BigDecimal.ZERO);
            //设置进度为100
            e.setProgress(BigDecimal.valueOf(100));
            //设置状态
            e.setState(InvestConstants.BID_STATE_WAIT_AUDIT);
            //设置已经开始
            e.setHasStarted(true);
        });

    }


   /**
   *  @Description    ：从指定分组中找到满足条件的标的信息，并且存入list缓存中
   *  @Method_Name    ：groupFromProductTypedBidInfo
   *  @param tempBidInfoVOs
   *  @param cachedInfos
   *  @return void
   *  @Creation Date  ：2018/4/24
   *  @Author         ：zhongpingtang@hongkun.com.cn
   */
    private void groupFromProductTypedBidInfo(List<BidInfoVO> tempBidInfoVOs, List<BaseBidInfoVO> cachedInfos) {
        if (!BaseUtil.collectionIsEmpty(tempBidInfoVOs)) {
            BaseBidInfoVO impe = initIndexBidInfoFromBidVOs(tempBidInfoVOs);
            if (impe != null) {
                cachedInfos.add(impe);
            }
        }
    }


    /**
    *  @Description    ：从查询的标的信息转换为首页需要的信息
    *  @Method_Name    ：initIndexBidInfoFromBidVOs
    *  @param bidInfoVOS
    *  @return com.hongkun.finance.invest.model.vo.BaseBidInfoVO
    *  @Creation Date  ：2018/4/24
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    private BaseBidInfoVO initIndexBidInfoFromBidVOs(List<BidInfoVO> bidInfoVOS) {
        return initBaseBidInfoFromBidInfoVO(findMathIndexBidInfo(bidInfoVOS));
    }

  /**
  *  @Description    ：从查询的标的信息转换为需要的信息-List
  *  @Method_Name    ：initIndexBidInfoListFromBidVOs
  *  @param bidInfoVOS
  *  @return java.util.List<com.hongkun.finance.invest.model.vo.BaseBidInfoVO>
  *  @Creation Date  ：2018/4/24
  *  @Author         ：zhongpingtang@hongkun.com.cn
  */
    private List<BaseBidInfoVO> initIndexBidInfoListFromBidVOs(List<BidInfoVO> bidInfoVOS) {
        if (!BaseUtil.collectionIsEmpty(bidInfoVOS)) {
            return bidInfoVOS.stream().map(e -> initBaseBidInfoFromBidInfoVO(e))
                             .collect(Collectors.toList());
        }
        return Collections.EMPTY_LIST;
    }

    /**
    *  @Description    ：从标的vo中构造baseBidinfo
    *  @Method_Name    ：initBaseBidInfoFromBidInfoVO
    *  @param orginVO
    *  @return com.hongkun.finance.invest.model.vo.BaseBidInfoVO
    *  @Creation Date  ：2018/4/24
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    private BaseBidInfoVO initBaseBidInfoFromBidInfoVO(BidInfoVO orginVO) {
        if (orginVO == null) {
            return null;
        }
        BaseBidInfoVO baseBidInfoVO = new BaseBidInfoVO();
        BeanPropertiesUtil.splitProperties(orginVO, baseBidInfoVO);
        baseBidInfoVO.setImgUrl(orginVO.getImgUrl());
        baseBidInfoVO.setPrintImgurl(orginVO.getPrintImgurl());
        if (!BigDecimal.ZERO.equals(orginVO.getRaiseRate())) {
            //处理年化率和加息率
            baseBidInfoVO.setBaseRate(orginVO.getInterestRate().subtract(orginVO.getRaiseRate()));
        }
        //计算进度
        baseBidInfoVO.setProgress((baseBidInfoVO.getTotalAmount().subtract(baseBidInfoVO.getResidueAmount())).divide(baseBidInfoVO.getTotalAmount(),4,BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100)));
        //计算万元收益
        baseBidInfoVO.setTenThousandIncome(CalcInterestUtil.calcInterest(
                orginVO.getTermUnit(),
                orginVO.getTermValue(),
                BigDecimal.valueOf(10000),
                orginVO.getInterestRate(),
                BigDecimal.ZERO));

        //判断是否是新手标，如果是新手标，那么设置最大投资金额 maxInvestMoney 建议投资金额 recommendAmount
        if(BaseUtil.equelsIntWraperPrimit(orginVO.getProductType(),BID_PRODUCT_PREFERRED)){
            baseBidInfoVO.setMaxInvestMoney(BigDecimal.valueOf(INVEST_FLEDGLING_INVEST_MAX_ATM));
            baseBidInfoVO.setMaxInvestMoneyStr("限投"+BigDecimal.valueOf(INVEST_FLEDGLING_INVEST_MAX_ATM)+"元");
            baseBidInfoVO.setRecommendAmount(BigDecimal.valueOf(INVEST_PREFERRED_RECOMMEND_ATM));
        }

        //计算标的是否开始
        Date startTime = baseBidInfoVO.getStartTime();
        if (startTime != null) {
            baseBidInfoVO.setHasStarted(new Date().after(startTime));
        }

        //为前端屏蔽状态,折合成只有三种状态:0-未开始,2-投标中,3-已售罄
        if (!BaseUtil.equelsIntWraperPrimit(baseBidInfoVO.getState(), InvestConstants.BID_STATE_WAIT_INVEST)) {
            if (baseBidInfoVO.getHasStarted()) {
                //原状态不是投标中,但是已经开始 = 已售罄
                baseBidInfoVO.setState(3);
            }else{
                //原状态不是投标中,但是未开始 = 未开始
                baseBidInfoVO.setState(0);
            }
        }
        // 还款方式描述
        baseBidInfoVO.setBiddRepaymentWayDesc(dicDataService.findNameByValue(INVEST, REPAYMENT_MODE, orginVO.getBiddRepaymentWay()));

        // APP展示标的类型
        baseBidInfoVO.setAppShowType(BidInfoUtil.getAppShowType(orginVO.getType(),orginVO.getProductType()));

        return baseBidInfoVO;
    }


   /**
   *  @Description    ：选取标的信息的规则
   *  @Method_Name    ：findMathIndexBidInfo
   *  @param bidInfoVOS
   *  @return com.hongkun.finance.invest.model.vo.BidInfoVO
   *  @Creation Date  ：2018/4/24
   *  @Author         ：zhongpingtang@hongkun.com.cn
   */
    private BidInfoVO findMathIndexBidInfo(List<BidInfoVO> bidInfoVOS) {
        if (!BaseUtil.collectionIsEmpty(bidInfoVOS)) {
            bidListSortRule(bidInfoVOS);
            return bidInfoVOS.stream().findFirst().orElse(null);
        }
        return null;
    }



  /**
  *  @Description    ：对选择的标的进行排序 1.按照创建时间排序，2.按照标的状态排序
  *  @Method_Name    ：bidListSortRule
  *  @param bidInfoVOS
  *  @return void
  *  @Creation Date  ：2018/4/24
  *  @Author         ：zhongpingtang@hongkun.com.cn
  */
    private void bidListSortRule(List<BidInfoVO> bidInfoVOS) {
        //多条件排序
        bidInfoVOS.sort((lbid, rbid) -> {
            if (lbid.getState().equals(rbid.getState())) {
                //再按照创建时间排序,优先显示最早的标的
                return lbid.getCreateTime().compareTo(rbid.getCreateTime());
            } else {
                //首先按照标的状态排序
                return lbid.getState().compareTo(rbid.getState());
            }
        });
    }

   /**
   *  @Description    ：初始化两种模式的产品页标的列表 1.带新手标和体验标 2. 不带新手标和体验标
   *  @Method_Name    ：initModelProductBidInfo
   *  @param termValue
   *  @param bidType
   *  @param plusMode   是否是鸿坤金服 模式
   *  @return java.util.Map<java.lang.String,java.lang.Object>
   *  @Creation Date  ：2018/5/7
   *  @Author         ：zhongpingtang@hongkun.com.cn
   */
    private Map<String, Object> initModelProductBidInfo(Integer termValue, Integer bidType,boolean plusMode) {
        //找到合适的标的信息
        List<BidInfoVO> allBidInfo = findAvailableBids(String.valueOf(InvestConstants.BID_SHOW_POSITION_APP));

        if (BaseUtil.collectionIsEmpty(allBidInfo)) {
            return AppResultUtil.successOfMsg("没有找到合适的标的产品");
        }
        //过滤时间和标的类型
        if (termValue!=null) {
            allBidInfo=allBidInfo.stream().filter(e -> (termValue.equals(e.getTermValue()))
                    &&(CalcInterestUtil.calInvestDays(e.getTermUnit(),e.getTermValue())==(CalcInterestUtil.calInvestDays(BID_TERM_UNIT_MONTH,termValue)))).collect(Collectors.toList());
        }
        if (bidType!=null) {
            allBidInfo=allBidInfo.stream().filter(e -> {
                if (bidType==0) {
                    //散标
                    return e.getType() == BID_PRODUCT_COMMNE;
                }
                if (bidType==1) {
                    //优选
                    return Arrays.asList(BID_PRODUCT_WINMONTH, BID_PRODUCT_WINSEASON, BID_PRODUCT_WINYEAR).contains(e.getType());
                }
                return false;
            }).collect(Collectors.toList());
        }

        //最后结果返回的标的信息
        LinkedList<BaseBidInfoVO> bids = new LinkedList();
        //按照产品类型分组
        Map<Integer, List<BidInfoVO>> productTypedBidInfoVO = allBidInfo.stream()
                                                                        //找出能够显示在App端的标的
                                                                        .filter(e -> (e.getShowPosition() != null && e.getShowPosition().contains(String.valueOf(BID_SHOW_POSITION_APP))))
                                                                        .collect(Collectors.groupingBy(BidInfoVO::getProductType));
        List<BaseBidInfoVO> experienceBids=null;
        if (!plusMode) {
            //处理新手标的
            bids.addAll(initIndexBidInfoListFromBidVOs(productTypedBidInfoVO.get(BID_PRODUCT_PREFERRED)));
            //处理体验金标的
            experienceBids = initIndexBidInfoListFromBidVOs(productTypedBidInfoVO.get(BID_PRODUCT_EXPERIENCE));

        }
        //删除新手标 体验金标
        productTypedBidInfoVO.remove(BID_PRODUCT_PREFERRED);
        productTypedBidInfoVO.remove(BID_PRODUCT_EXPERIENCE);

        //合流,处理爆款标,推荐标的
        Map<Integer, List<BidInfoVO>> typeGroupedBids = productTypedBidInfoVO.values()
                                                                     .stream().flatMap((list) -> list.stream())
                                                                     .collect(Collectors.groupingBy(BidInfoVO::getType));
        //添加爆款标
        bidInfoSelectMaker(bids,null,typeGroupedBids,Arrays.asList(InvestConstants.BID_TYPE_HOT),3,true,null);
        //添加推荐标
        bidInfoSelectMaker(bids,null,typeGroupedBids,Arrays.asList(InvestConstants.BID_TYPE_RECOMMEND),3,true,null);
        //处理其他非爆款标
        bidInfoSelectMaker(bids, typeGroupedBids.get(InvestConstants.BID_TYPE_COMMON), null,
                Arrays.asList(BID_PRODUCT_WINMONTH/*月月盈*/, BID_PRODUCT_WINSEASON/*季季盈*/, BID_PRODUCT_WINYEAR/*年年盈*/),
                3, false, BidInfoVO::getProductType);
        if (!plusMode) {
            //添加体验标
            bids.addAll(experienceBids);
        }
        return AppResultUtil.mapOfListNullable(bids, SUCCESS, null, false/*指定不允许为null*/)
                            //给还款类型添加描述
                            .addParameterDescInList("biddRepaymentWay", (key) -> dicDataService.findNameByValue(INVEST, REPAYMENT_MODE, (Integer) key))
                            .addResParameter("systemTime", new Date());
    }


    /**
     *  @Description    ：标的过滤器,根据特定规则，选择标的
     *  @Method_Name    ：bidInfoSelectMaker
     *  @param destList 目标List
     *  @param originsMaterial 原始材料
     *  @param selectProductType 选择产品类型
     *  @param selectNumber 选择数量
     *  @param bidStateMustOn 是否要求标的正在投标中的
     *  @param groupKey
     * @return java.util.List<com.hongkun.finance.invest.model.vo.BaseBidInfoVO>
     *  @Creation Date  ：2018/7/4
     *  @Author         ：zhongpingtang@hongkun.com.cn
     */
    private List<BaseBidInfoVO> bidInfoSelects(List<BaseBidInfoVO> destList,
                                                   List<BidInfoVO> originsMaterial,
                                                   Map<Integer, List<BidInfoVO>> finalGroupedSource,
                                                   List<Integer> selectProductType,
                                                   int selectNumber,
                                                   boolean bidStateMustOn,
                                                   Function<BidInfoVO, Integer> groupKey){
        //step1 对标的进行分组 ,如果有必要的话
        if (CollectionUtils.isEmpty(finalGroupedSource)&&groupKey!=null) {
            finalGroupedSource = originsMaterial.stream().collect(Collectors.groupingBy(groupKey));
        }
        //step2 获取必要的类型
        Map<Integer, List<BidInfoVO>> finalGroupedSource1 = finalGroupedSource;
        selectProductType.stream().forEach(bidType -> {
            //取出当前类型的标的集合
            List<BidInfoVO> currentTypeBids = finalGroupedSource1.get(bidType);
            if (!BaseUtil.collectionIsEmpty(currentTypeBids)) {
                if (bidStateMustOn) {
                    //过滤不是投资中的标的
                    currentTypeBids= currentTypeBids.stream().filter(e -> BaseUtil.equelsIntWraperPrimit(e.getState(), InvestConstants.BID_STATE_WAIT_INVEST)).collect(Collectors.toList());
                }
                //按照产品id(产品类型)去分组涉及到的产品   排序字段：按月计息，到期还本；创建时间
                Map<Integer, List<BidInfoVO>> innerProductTypedBidInfo=currentTypeBids.stream().collect(Collectors.groupingBy(BidInfoVO::getBidProductId));
                List<Integer> orderedProductId = new ArrayList(innerProductTypedBidInfo.keySet());
                if (!BaseUtil.collectionIsEmpty(orderedProductId)) {
                    orderedProductId= bidProductService.orderedByPayemntAndCreateTime(orderedProductId);
                }
                //按照指定的产品顺序,添加选定类型的标的
                orderedProductId.forEach((productId) -> destList.addAll(selectNumberOfThen(innerProductTypedBidInfo.get(productId), selectNumber)));
            }

        });
        return destList;
    }


    @Override
    public ResponseEntity<?> productSearch(RegUser regUser, Integer type, String keyWord,Integer fixFlag) {
        // 默认搜索产品类型
        if(BaseUtil.equelsIntWraperPrimit(fixFlag, UserConstants.USER_CAN_INVEST_NO) && type == null){
            return new ResponseEntity<>(SUCCESS,  getFundProduct(FUND_PROJECT_PARENT_TYPE_PRIVATE, keyWord));
        }
        if(null != type && !FIX_PRODUCT.equals(type)){
            return new ResponseEntity<>(SUCCESS,getFundProduct(type, keyWord));
        }else {
            BidInfoVO bidInfoVO = new BidInfoVO();
            Pager pager = new Pager();
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
            if(bidInvestFacade.validOverseaInvestor(regUser.getId()).validSuc()){
                bidInfoVO.setProductTypeLimitIds(Arrays.asList(BID_PRODUCT_OVERSEA));
            }
            ResponseEntity<?> result = this.bidInfoFacade.findBidVoListForInvest(pager, bidInfoVO, regUser);
            List<BaseBidInfoVO> resultList =  (List<BaseBidInfoVO>)((Pager)result.getResMsg()).getData();
            resultList = resultList.stream().filter(bid -> bid
                    .getName().contains(keyWord))
                    .collect(Collectors.toList());
            return new ResponseEntity<>(SUCCESS,resultList);
        }

    }

    /**
     * 股权产品
     * @param type
     * @param keyWord
     * @return
     */
    private List<FundInfoVo> getFundProduct(Integer type, String keyWord) {
        FundInfoVo vo = new FundInfoVo();
        vo.setName(keyWord);
        vo.setParentType(type);
        vo.setState(2); // 0-删除,1-待上架,2-上架,3-下架
        vo.setSortColumns("info.create_time DESC");
        Pager pager = new Pager();
        pager.setPageSize(Integer.MAX_VALUE);
        Pager resultPager = fundInfoService.findFundInfoVoByCondition(vo, pager);
        List<FundInfoVo> resultList = new ArrayList<>();
        resultPager.getData().forEach(fundInfoVo -> {
            FundInfoVo infoVo = (FundInfoVo) fundInfoVo;
            if(infoVo.getInfoExist() == FUND_INFO_EXIST_YES){
                //开放日描述
                infoVo.setOpenDateDescribe(FundUtils.getFundOpenTime(infoVo));
                //是否在开放日 注：不在开放日设置成停约
                if(!FundUtils.checkOpenDate(infoVo)){
                    infoVo.setSubscribeState(0);
                }
                //起投金额
                if(CompareUtil.gtZero(infoVo.getLowestAmount())) {
                    infoVo.setLowestAmount(infoVo.getLowestAmount().divide(BigDecimal.valueOf(10000),2, RoundingMode.HALF_UP));
                }
                //存续期限
                infoVo.setTermDescribe(FundUtils.getTermDes(infoVo));
            }else{
                infoVo.setName(dicDataService.findNameByValue("fund" ,"project_type", infoVo.getProjectId()));
            }
            resultList.add(infoVo);
        });
        return resultList;

    }

    /**
     * 定期产品
     * @param keyWord
     * @return
     */
    private List<BaseBidInfoVO> getFixProduct(String keyWord) {
        BidInfoVO vo = new BidInfoVO();
        Pager pager = new Pager();
        //包含pc端展示的标的
        vo.setShowPosition(String.valueOf(BID_SHOW_POSITION_PC));
        //查询投资中和已售罄的标的
        vo.setStates(Arrays.asList(BID_STATE_WAIT_INVEST, BID_STATE_WAIT_AUDIT, BID_STATE_WAIT_LOAN));
        //标的类型:正常标,爆款标,推荐标
        vo.setBidTypeLimitIds(Arrays.asList(BID_TYPE_COMMON,BID_TYPE_HOT,BID_TYPE_RECOMMEND));
        //还款方式:按月付息，到期还本 到期一次还本付息
        vo.setRepayWayLimitIds(Arrays.asList(REPAYTYPE_INTEREST_MONTH_PRINCIPAL_END,REPAYTYPE_ONECE_REPAYMENT));
        //只查询上架中的产品类型
        vo.setProductState(InvestConstants.PRODUCT_STATE_ON);
        //标的产品类型:新手标,月月赢,季季盈,年年盈
        vo.setProductTypeLimitIds(Arrays.asList(BID_PRODUCT_PREFERRED,BID_PRODUCT_WINMONTH,BID_PRODUCT_WINSEASON,BID_PRODUCT_WINYEAR));
        vo.setTitle(keyWord);
        //执行查询
        pager.setPageSize(Integer.MAX_VALUE);
        Pager result = this.bidInfoService.findBidInfoVOByCondition(vo, pager);

        List<BidInfoVO> allBidInfo = (List<BidInfoVO>)result.getData();
        List<BaseBidInfoVO> showResult = new ArrayList<>();
        //按照标的类型分组
        Map<Integer, List<BidInfoVO>> typedGroupCommBidInfo = allBidInfo.stream().collect(Collectors.groupingBy(BidInfoVO::getType));
        // 新手标
        bidInfoSelectMaker(showResult, allBidInfo, null, Arrays.asList(BID_PRODUCT_PREFERRED),Integer.MAX_VALUE, false, BidInfoVO::getProductType);
        //爆款标的
        bidInfoSelectMaker(showResult, null, typedGroupCommBidInfo, Arrays.asList(BID_TYPE_HOT), Integer.MAX_VALUE, false, null);
        //推荐标的
        bidInfoSelectMaker(showResult, null, typedGroupCommBidInfo, Arrays.asList(BID_TYPE_RECOMMEND), Integer.MAX_VALUE, false, null);
        //移除爆款
        typedGroupCommBidInfo.remove(BID_TYPE_HOT);
        //移除推荐
        typedGroupCommBidInfo.remove(BID_TYPE_RECOMMEND);
        //剔除推荐和爆款之后再次分组-剩下的全部是正常标
        List<BidInfoVO> afterHotAndRecommendRemoved = typedGroupCommBidInfo.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
        //处理月月盈,季季盈,年年盈,散标
        bidInfoSelectMaker(showResult, afterHotAndRecommendRemoved, null, Arrays.asList(BID_PRODUCT_WINMONTH/*月月盈*/, BID_PRODUCT_WINSEASON/*季季盈*/,
                BID_PRODUCT_WINYEAR/*年年盈*/,BID_PRODUCT_COMMNE),Integer.MAX_VALUE, false, BidInfoVO::getProductType);
        // 体验金
        bidInfoSelectMaker(showResult, afterHotAndRecommendRemoved, null, Arrays.asList(BID_PRODUCT_EXPERIENCE),Integer.MAX_VALUE, false, BidInfoVO::getProductType);
        showResult.stream().forEach(e ->
                e.setResidueAmount(e.getResidueAmount().divide(BigDecimal.valueOf(10000),2, RoundingMode.HALF_UP))
        );
        return showResult;
    }
    
   /**
    *  @Description    : 查询月月赢,季季盈,年年盈标的信息
    *  @Method_Name    : findAvailableBid;
    *  @param showPosition
    *  @return
    *  @return         : List<BidInfoVO>;
    *  @Creation Date  : 2018年10月23日 上午10:10:07;
    *  @Author         : yanbinghuang@hongkun.com.cn 黄艳兵;
    */
   private List<BidInfoVO> findAvailableBid(String showPosition) {
       BidInfoVO queryBidInfoVO = new BidInfoVO();
       //设置标的显示位置
       queryBidInfoVO.setShowPosition(showPosition);
       //只查询投标中,已售罄之间状态的标的
       queryBidInfoVO.setStates(Arrays.asList(BID_STATE_WAIT_INVEST, BID_STATE_WAIT_AUDIT, BID_STATE_WAIT_LOAN, BID_STATE_WAIT_REPAY, BID_STATE_WAIT_FINISH));
       //限制查询
       //只查询上架中的产品类型
       queryBidInfoVO.setProductState(InvestConstants.PRODUCT_STATE_ON);
       //标的产品类型:月月赢,季季盈,年年盈
       queryBidInfoVO.setProductTypeLimitIds(Arrays.asList(
               BID_PRODUCT_WINMONTH/*月月赢*/,
               BID_PRODUCT_WINSEASON/*季季盈*/,
               BID_PRODUCT_WINYEAR/*年年盈*/));
       //标的类型
       queryBidInfoVO.setBidTypeLimitIds(Arrays.asList(BID_TYPE_COMMON));
       //去数据库中查询
       List<BidInfoVO> result = bidInfoService.findBidInfoVOByCondition(queryBidInfoVO);
       return  result.stream().filter(e -> CompareUtil.gt(e.getTotalAmount(),new BigDecimal(2000))).collect(Collectors.toList());
   }

    @Override
    public Map<String, Object> findQdzPageBidInfo(RegUser loginUser) {
        Map<String,Object> bidsMap = new HashMap<>();
        //找到合适的标的信息
        List<BidInfoVO> allBidInfo = findAvailableBid(String.valueOf(InvestConstants.BID_SHOW_POSITION_APP));
        AppIndexBidInfoVO appIndexBidInfoVO = new AppIndexBidInfoVO();
        //处理月月盈,季季盈,年年盈标的
        bidInfoSelectMaker(appIndexBidInfoVO.getAppLinedBidsInfo(), allBidInfo, null, Arrays.asList(BID_PRODUCT_WINMONTH/*月月盈*/, BID_PRODUCT_WINSEASON/*季季盈*/, BID_PRODUCT_WINYEAR/*年年盈*/),
                1, false, BidInfoVO::getProductType);
        //二次处理，每类标的只保留一个
        List<BaseBidInfoVO> resultBidInfo = filterAgain(appIndexBidInfoVO.getAppLinedBidsInfo());
        bidsMap.put("qdzPageBids", resultBidInfo);
        return bidsMap;
    }

    /**
     * @Description: 首页和钱袋子标的二次筛选，每类标只展示一个
     * @param baseBidList
     * @return: java.util.List<com.hongkun.finance.invest.model.vo.BaseBidInfoVO>
     * @Author: hanghe@hongkunjinfu.com
     * @Date: 2018/12/11 17:55
     */
    public List<BaseBidInfoVO> filterAgain(List<BaseBidInfoVO> baseBidList){
        //按照产品类型分组
        Map<Integer, List<BaseBidInfoVO>> finalGroupedSource=new HashMap<Integer,List<BaseBidInfoVO>>();
        if (!CollectionUtils.isEmpty(baseBidList)) {
            finalGroupedSource = baseBidList.stream().collect(Collectors.groupingBy(BaseBidInfoVO::getProductType));
        }
        List<BaseBidInfoVO> resultBidInfo = new ArrayList<BaseBidInfoVO>();
        //step2 获取必要的类型
        Map<Integer, List<BaseBidInfoVO>> finalGroupedSource1 =finalGroupedSource;
        Arrays.asList(
                BID_PRODUCT_WINMONTH/*月月赢*/,
                BID_PRODUCT_WINSEASON/*季季盈*/,
                BID_PRODUCT_WINYEAR/*年年盈*/).stream().forEach(productType->{
            //取出当前类型的标的集合
            List<BaseBidInfoVO> currentTypeBids = finalGroupedSource1.get(productType);
            if (!BaseUtil.collectionIsEmpty(currentTypeBids)) {

                bidListSortRuleAgain(currentTypeBids);
                resultBidInfo.addAll(selectNumberOfThenAgain(currentTypeBids, 1));
            }
        });
        return resultBidInfo;
    }

    /**
     * @Description: 对选择的标的进行再次排序 1.按照标的状态，2.按照标的还款方式排序 3.按照创建时间排序
     * @param bidInfoVOS
     * @return: void
     * @Author: hanghe@hongkunjinfu.com
     * @Date: 2018/12/11 17:05
     */
    private void bidListSortRuleAgain(List<BaseBidInfoVO> bidInfoVOS) {
        //多条件排序
        bidInfoVOS.sort((lbid, rbid) -> {
            if (lbid.getState().equals(rbid.getState())) {
                if(lbid.getBiddRepaymentWay().equals(rbid.getBiddRepaymentWay())){
                    //标的状态和还款方式都相同，按照标的创建时间排序
                    return bidInfoService.findBidInfoById(rbid.getId()).getCreateTime().compareTo(bidInfoService.findBidInfoById(lbid.getId()).getCreateTime());
                }else{
                    //再按照还款方式排序,优先显示按月付息
                    return lbid.getBiddRepaymentWay().compareTo(rbid.getBiddRepaymentWay());
                }
            } else {
                //首先按照标的状态排序
                return lbid.getState().compareTo(rbid.getState());
            }
        });
    }

    /** 
    * @Description: 再次排序筛选
    * @param bidInfoVOS
    * @param number
    * @return: java.util.List<com.hongkun.finance.invest.model.vo.BaseBidInfoVO> 
    * @Author: hanghe@hongkunjinfu.com
    * @Date: 2018/12/12 10:18
    */
    private List<BaseBidInfoVO> selectNumberOfThenAgain(List<BaseBidInfoVO> bidInfoVOS,int number) {
        if (!BaseUtil.collectionIsEmpty(bidInfoVOS)) {
            //排序
            bidListSortRuleAgain(bidInfoVOS);
            //选取前n个
            List<BaseBidInfoVO> selectedBids = bidInfoVOS.size() > number ? bidInfoVOS.subList(0, number) : bidInfoVOS;
            return selectedBids;
        }
        return Collections.EMPTY_LIST;
    }
}
