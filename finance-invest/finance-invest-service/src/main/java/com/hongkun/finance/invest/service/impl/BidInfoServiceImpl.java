package com.hongkun.finance.invest.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.invest.constants.InvestConstants;
import com.hongkun.finance.invest.dao.*;
import com.hongkun.finance.invest.model.*;
import com.hongkun.finance.invest.model.vo.*;
import com.hongkun.finance.invest.service.BidInfoService;
import com.hongkun.finance.invest.util.BidInfoUtil;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.exception.BusinessException;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.BeanPropertiesUtil;
import com.yirun.framework.core.utils.CommonUtils;
import com.yirun.framework.core.utils.DateUtils;
import com.yirun.framework.core.utils.PropertiesHolder;
import com.yirun.framework.core.utils.json.JsonUtils;
import com.yirun.framework.core.utils.pager.Pager;
import org.apache.commons.lang3.StringUtils;
import org.mengyun.tcctransaction.api.Compensable;
import org.mengyun.tcctransaction.dubbo.context.DubboTransactionContextEditor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.hongkun.finance.invest.constants.InvestConstants.*;
import static com.yirun.framework.core.commons.Constants.ERROR;
import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.invest.service.impl.
 * @Class Name : BidInfoServiceImpl.java
 * @Description : GENERATOR SERVICE实现类
 * @Author : generator
 */
@Service
public class BidInfoServiceImpl implements BidInfoService {

    private static final Logger logger = LoggerFactory.getLogger(BidInfoServiceImpl.class);

    /**
     * BidInfoDAO
     */
    @Autowired
    private BidInfoDao bidInfoDao;
    @Autowired
    private BidInfoDetailDao bidInfoDetailDao;
    @Autowired
    private BidProductDao bidProductDao;
    @Autowired
    private BidMatchDao bidMatchDao;
    @Autowired
    private BidInvestDao bidInvestDao;


    private static final String zeroTime = "0000-00-00";


    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public int insertBidInfo(BidInfo bidInfo) {
        return this.bidInfoDao.save(bidInfo);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public void insertBidInfoBatch(List<BidInfo> list) {
        this.bidInfoDao.insertBatch(BidInfo.class, list);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public void insertBidInfoBatch(List<BidInfo> list, int count) {
        if (logger.isDebugEnabled()) {
            logger.debug("default batch insert size is " + count);
        }
        this.bidInfoDao.insertBatch(BidInfo.class, list, count);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public int updateBidInfo(BidInfo bidInfo) {
        return this.bidInfoDao.update(bidInfo);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public void updateBidInfoBatch(List<BidInfo> list, int count) {
        this.bidInfoDao.updateBatch(BidInfo.class, list, count);
    }

    @Override
    public BidInfo findBidInfoById(int id) {
        return this.bidInfoDao.findByPK(Long.valueOf(id), BidInfo.class);
    }

    @Override
    public List<BidInfo> findBidInfoList(BidInfo bidInfo) {
        return this.bidInfoDao.findByCondition(bidInfo);
    }

    @Override
    public List<BidInfo> findBidInfoList(BidInfo bidInfo, int start, int limit) {
        return this.bidInfoDao.findByCondition(bidInfo, start, limit);
    }

    @Override
    public Pager findBidInfoList(BidInfo bidInfo, Pager pager) {
        return this.bidInfoDao.findByCondition(bidInfo, pager);
    }

    @Override
    public int findBidInfoCount(BidInfo bidInfo) {
        return this.bidInfoDao.getTotalCount(bidInfo);
    }

    @Override
    public Pager findConditionBidInfo(BidInfo bidInfo, Pager pager) {
        return this.bidInfoDao.findConditionBidInfo(bidInfo, pager);
    }

    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    @Override
    public boolean insertBidInfoWithBidDetail(BidInfo bidInfo, BidInfoDetail bidInfoDetail) {
        int updateBidInfoCount = insertBidInfo(bidInfo);
        // 设置关联id
        bidInfoDetail.setBiddInfoId(bidInfo.getId());
        // 插入bidinfodetailDao
        int updateBidDetailCount = bidInfoDetailDao.save(bidInfoDetail);
        return updateBidDetailCount > 0 && updateBidInfoCount > 0;
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public int updateState(int id, int state) {
        BidInfo bidInfo = new BidInfo(id);
        bidInfo.setState(state);
        //处理标的时间
        bidInfoTimeProcess(bidInfo);
        // 执行更新
        return updateBidInfo(bidInfo);
    }

    /**
    *  @Description    ：上下架的时间处理
    *  @Method_Name    ：bidInfoTimeProcess
    *  @param bidInfo
    *  @return void
    *  @Creation Date  ：2018/6/13
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    private void bidInfoTimeProcess(BidInfo bidInfo) {
        BidInfo orginBidInfo = bidInfoDao.findByPK(Long.valueOf(bidInfo.getId()), BidInfo.class);
        if (BaseUtil.equelsIntWraperPrimit(bidInfo.getState(), InvestConstants.BID_STATE_WAIT_INVEST)) {
            //上架操作,时间进行相应的处理
            boolean startTimeIsPresent  = !isDefalutTime(orginBidInfo.getStartTime());
            boolean endTimeIsPresent  = !isDefalutTime(orginBidInfo.getEndTime());
            Date currentDate = DateUtils.getCurrentDate();
            if (!startTimeIsPresent) {
                //没有填写开始时间,开始时间变为当前时间(上架时间)
                bidInfo.setStartTime(currentDate);
            }else{
                bidInfo.setStartTime(orginBidInfo.getStartTime());
            }

            if (!endTimeIsPresent) {
                //没有填写结束时间,结束时间变为加期限时间
                bidInfo.setEndTime(DateUtils.addDays(currentDate,bidProductDao.findByPK(Long.valueOf(orginBidInfo.getBidProductId()),BidProduct.class).getBidDeadline()));
            }else{
                bidInfo.setEndTime(orginBidInfo.getEndTime());
            }

        }

        if (BaseUtil.equelsIntWraperPrimit(bidInfo.getState(), InvestConstants.BID_STATE_WAIT_NEW)) {
            //下架操作,时间进行相应的处理，时间回置
            bidInfo.setStartTime(orginBidInfo.getStartTime());
            bidInfo.setEndTime(orginBidInfo.getEndTime());
        }
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public int updateStateAndLendingTime(int id, int state) {
        BidInfo bidInfo = new BidInfo(id);
        bidInfo.setState(state);
        bidInfo.setLendingTime(new Date());
        // 执行更新
        return updateBidInfo(bidInfo);
    }
    
    

    @Override
    public ResponseEntity<?> validateBidInfo(int biddId) {
        BidInfo bidInfo = bidInfoDao.findByPK(Long.valueOf(biddId), BidInfo.class);
        if (bidInfo == null) {
            return new ResponseEntity<>(ERROR, "标的不存在！");
        }
        BidInfoDetail infoDetail = new BidInfoDetail();
        infoDetail.setBiddInfoId(biddId);
        BidInfoDetail bidInfoDetail = bidInfoDetailDao.findByBiddInfoId(biddId);
        if (bidInfoDetail == null) {
            return new ResponseEntity<>(ERROR, "标的详情不存在！");
        }
        BidProduct bidProduct = bidProductDao.findByPK(Long.valueOf(bidInfo.getBidProductId()), BidProduct.class);
        if (bidProduct == null) {
            return new ResponseEntity<>(ERROR, "标的产品不存在！");
        }
        ResponseEntity<?> result = new ResponseEntity<>(SUCCESS);
        result.getParams().put("bidInfo", bidInfo);
        result.getParams().put("bidProduct", bidProduct);
        result.getParams().put("bidInfoDetail", bidInfoDetail);
        return result;
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public ResponseEntity<?> updateBidInfo(BidInfo bidInfo, BidInfoDetail bidInfoDetail) {
        bidInfoDao.update(bidInfo);
        bidInfoDetailDao.update(bidInfoDetail);
        return new ResponseEntity<>(SUCCESS);
    }

    @Override
    public List<BidInfo> findBidInfoList(Integer productType, Integer state) {
        return bidInfoDao.findBidInfoList(productType, state, null);
    }

    @Override
    public List<BidInfo> findBidInfoList(Integer productType, Integer state, String bidName) {
        return bidInfoDao.findBidInfoList(productType, state, bidName);
    }

    @Override
    public Pager findBidInfoList(Integer productType, List<Integer> state, String bidName, Pager pager) {
        return bidInfoDao.findBidInfoList(productType, state, bidName, pager);
    }

    @Override
    public Pager findMatchBidInfoList(BidInfoVO contidion, Pager pager) {
        return bidInfoDao.findMatchBidInfoList(contidion, pager);
    }

    @Override
    public List<BidInfoVO> findMatchBidInfoList(BidInfoVO contidion) {
        return bidInfoDao.findMatchBidInfoList(contidion);
    }

    @Override
    public Pager findBidInfoVOByCondition(BidInfoVO contidion, Pager pager) {
        return bidInfoDao.findBidInfoVOByCondition(contidion, pager);
    }

    @Override
    public List<BidInfo> findGoodList(int state) {
        List<Integer> bidStates = new ArrayList<Integer>();
        bidStates.add(state);
        return bidInfoDao.findGoodOrCommonList(bidStates, InvestConstants.BID_MATCH_TYPE_NO,
                BidInfoUtil.getProdutTypeList(InvestConstants.MATCH_BID_TYPE_GOOD));
    }

    @Override
    public List<BidInfo> findMatchCommonList(List<Integer> bidStates) {
        return bidInfoDao.findGoodOrCommonList(bidStates, InvestConstants.BID_MATCH_TYPE_YES,
                BidInfoUtil.getProdutTypeList(InvestConstants.MATCH_BID_TYPE_COMMON));
    }

    @Override
    public List<BidInfoVO> findBidInfoDetailsByEndDate(Date endDate) {
        return bidInfoDao.findBidInfoDetailsByEndDate(endDate);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public ResponseEntity<?> updateForRepay(BidInfo bidInfo, List<BidInfoDetail> updateBidInfoDetailList,
                                            List<BidMatch> insertBidMatchList, List<BidMatch> updateBidMatchList, List<BidInvest> insertBidInvestList,
                                            List<BidInvest> updateBidInvestList) {
        ResponseEntity<?> result = new ResponseEntity<>(SUCCESS);
        logger.info("updateForRepay, 更新标的信息. 标的标识: {}, 标的信息(detail): {}, 插入匹配记录: {}, 更新匹配记录: {}, 插入投资记录: {}, 更新投资记录: {}", bidInfo.toString(), JsonUtils.toJson(updateBidInfoDetailList),
                JsonUtils.toJson(insertBidMatchList), JsonUtils.toJson(updateBidMatchList), JsonUtils.toJson(insertBidInvestList), JsonUtils.toJson(updateBidInvestList));
        try{
            this.updateBidInfo(bidInfo);
            // 更新标的详情
            if (CommonUtils.isNotEmpty(updateBidInfoDetailList)) {
                this.bidInfoDetailDao.updateBatch(BidInfoDetail.class, updateBidInfoDetailList,
                        updateBidInfoDetailList.size());
                // updateBidInfoDetailList.forEach(this.bidInfoDetailDao::update);
            }
            // 插入新的匹配关系
            if (CommonUtils.isNotEmpty(insertBidMatchList)) {
                for (BidMatch bidMatch : insertBidMatchList) {
                    this.bidMatchDao.save(bidMatch);
                }
            }
            // 更新匹配关系
            if (CommonUtils.isNotEmpty(updateBidMatchList)) {
                this.bidMatchDao.updateBatch(BidMatch.class, updateBidMatchList, updateBidMatchList.size());
            }
            // 插入新的匹配投资记录
            if (CommonUtils.isNotEmpty(insertBidInvestList)) {
                for (BidInvest bidInvset : insertBidInvestList) {
                    this.bidInvestDao.save(bidInvset);
                }
            }
            // 更新匹配投资记录
            if (CommonUtils.isNotEmpty(updateBidInvestList)) {
                this.bidInvestDao.updateBatch(BidInvest.class, updateBidInvestList, updateBidInvestList.size());
            }
            result.getParams().put("insertBidInvestList", insertBidInvestList);
            return result;
        }catch(Exception e){
            logger.error("updateForRepay, 更新标的信息. 标的标识: {}, 标的信息(detail): {}, 插入匹配记录: {}, 更新匹配记录: {}, 插入投资记录: {}, 更新投资记录: {}", bidInfo.toString(), JsonUtils.toJson(updateBidInfoDetailList),
                    JsonUtils.toJson(insertBidMatchList), JsonUtils.toJson(updateBidMatchList), JsonUtils.toJson(insertBidInvestList), JsonUtils.toJson(updateBidInvestList), e);
            throw new GeneralException("更新标的失败");
        }
    }

    @Override
    @Compensable(cancelMethod = "updateBidInfoForMakeLoanCancel", transactionContextEditor = DubboTransactionContextEditor.class)
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public void updateBidInfoForMakeLoan(BidInfoDetail updateDetail) {
        try {
        	logger.info("{}, 放款更新标的信息, 标的详情：{}", BaseUtil.getTccTryLogPrefix(),updateDetail.toString());
			BidInfo bidInfo = new BidInfo(updateDetail.getBiddInfoId());
			bidInfo.setState(InvestConstants.BID_STATE_WAIT_REPAY);
			bidInfo.setLendingTime(new Date());
			Integer result = bidInfoDao.updateBidInfoForMakeLoan(bidInfo);
			if(result<=0){
				throw new BusinessException("放款操作正在处理，请耐心等待");
			}
			bidInfoDetailDao.update(updateDetail);
		} catch (Exception e) {
			logger.error("{}, 放款更新标的信息, 标的详情：{}, 异常信息: \n",BaseUtil.getTccTryLogPrefix(),updateDetail.toString(), e);
			throw new GeneralException("放款更新标的信息异常");
		}
    }

	/**
     * @param updateDetail
     * @return : void
     * @Description : TCC回滚方法 updateBidInfoForMakeLoan
     * @Method_Name : updateBidInfoForMakeLoanCancel
     * @Creation Date  : 2017年10月17日 上午11:45:01
     * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
     */
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public void updateBidInfoForMakeLoanCancel(BidInfoDetail updateDetail) {
    	logger.info("tcc cancel updateBidInfoForMakeLoan, 放款更新标的信息, 标的详情：{}", updateDetail.toString());
        try {
            BidInfo bidInfo = new BidInfo(updateDetail.getBiddInfoId());
            bidInfo.setState(InvestConstants.BID_STATE_WAIT_LOAN);
            bidInfo.setLendingTime(null);
            bidInfoDao.update(bidInfo);
            updateDetail.setNextMatchDate(new Date());
            bidInfoDetailDao.update(updateDetail);
        } catch (Exception e) {
            logger.info("tcc cancel updateBidInfoForMakeLoan, 放款更新标的信息, 标的详情：{}, 异常信息:\n", updateDetail.toString(),e);
            throw  new GeneralException("放款更新标的信息回滚异常");
        }
    }

    @Override
    public BidInfoVO findBidInfoDetailVo(Integer bidInfoId) {
        return bidInfoDao.findBidInfoDetailVoById(bidInfoId);
    }

    @Override
    public Pager findBidInfoDetailVoList(Pager pager, BidInfoVO vo) {
    	if(StringUtils.isBlank(vo.getSortColumns())){
    		vo.setSortColumns("id DESC");
    	}
        return this.bidInfoDao.findByCondition(vo, pager, BidInfo.class, ".findBidInfoDetailVoList");
    }

    @Override
    public List<BidInfoVO> findBidInfoVoList(BidInfoVO vo) {
        return this.bidInfoDao.findBidInfoVOList(vo);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public ResponseEntity saveInfoAndDetail(BidInfoVO bidInfoVO, Integer userId, Integer vasRebatesRuleId) {
       //step 0:校验选择的当前产品是否上架，如果是下架，不允许添加标的
        BidProduct bidProduct = bidProductDao.findByPK(Long.valueOf(bidInfoVO.getBidProductId()), BidProduct.class);
        if (bidProduct != null) {
            if (bidProduct.getState() != InvestConstants.PRODUCT_STATE_ON) {
                return ResponseEntity.error("所选择的借款产品处于未上架状态，不允许添加");
            }
        } else {
            return ResponseEntity.error("没有查询到当前标的产品");
        }

        //step 0.5:检查当前标的名称是否占用,如果占用,不允许添加

        BidInfo nameQueryInfo = new BidInfo();
        nameQueryInfo.setName(bidInfoVO.getTitle());
        Integer totalCount = bidInfoDao.getTotalCount(nameQueryInfo);
        if (totalCount>0) {
            return ResponseEntity.error("已经建立名称为:【"+bidInfoVO.getTitle()+"】标的,不允许重复添加");
        }

        //step 1:拆分BidInfo和BidDetail
        BidInfo bidInfo = new BidInfo();
        BidInfoDetail bidInfoDetail = new BidInfoDetail();
        bidInfoVO.setCreateUserId(userId);
        bidInfoVO.setModifyUserId(userId);

        // 执行拆分
        BeanPropertiesUtil.splitProperties(bidInfoVO, bidInfo, bidInfoDetail);

        //设置招标方案和招标方案
        bidInfo.setBidScheme(bidProduct.getBidScheme());
        bidInfo.setBidSchemeValue(bidProduct.getBidSchemeValue());



        // 设置初始状态是待上架
        bidInfo.setState(InvestConstants.BID_STATE_WAIT_NEW);
        bidInfo.setPrintImgurl(bidInfoVO.getPrintImgurl());
        bidInfo.setLabelText(bidInfoVO.getLabelText());
        bidInfo.setLabelUrl(bidInfoVO.getLabelUrl());
        bidInfo.setLendingTime(DateUtils.parse(bidInfoVO.getLendTime(), DateUtils.DATE_HH_MM_SS));
        //设置期限单位
        bidInfo.setTermUnit(bidProduct.getTermUnit());

        //step 2:设置标的剩余可投金额
        bidInfo.setResidueAmount(bidInfo.getTotalAmount());

        //step 3:执行插入操作
        int updateBidInfoCount = bidInfoDao.save(bidInfo);
        if (logger.isInfoEnabled()) {
            logger.info("新建标的步骤step1:  bidInfo：{}", bidInfo);
        }

        // 设置关联id
        bidInfoDetail.setBiddInfoId(bidInfo.getId());

        //设置当前的好友推荐规则
        bidInfoDetail.setRuleId(vasRebatesRuleId);

        // 插入bidinfodetailDao
        int updateBidDetailCount = bidInfoDetailDao.save(bidInfoDetail);

        if (logger.isInfoEnabled()) {
            logger.info("新建标的步骤step2:bidDetail：  {}", bidInfoDetail);
        }

        if (updateBidDetailCount > 0 && updateBidInfoCount > 0) {
            if (logger.isInfoEnabled()) {
                logger.info("新建标的成功,详细信息为：  bidInfo:{},bidDetail:{}", bidInfo, bidInfoDetail);
            }
            return new ResponseEntity(SUCCESS, "添加标的成功");

        }
        if (logger.isInfoEnabled()) {
            logger.info("新建标的失败,详细信息为：  bidInfo:{},bidDetail:{}", bidInfo, bidInfoDetail);
        }
        return new ResponseEntity(ERROR, "添加标的信息失败");
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public ResponseEntity updateBidInfoAndDetail(BidInfoVO bidInfoVO, Integer userId, Integer ruleId) {
        //判断标的是否可以更新,如果标的产品未上架，不允许修改
        BidProduct bidProduct = bidProductDao.findByPK(Long.valueOf(bidInfoVO.getBidProductId()), BidProduct.class);
        if (bidProduct != null) {
            if (bidProduct.getState() != InvestConstants.PRODUCT_STATE_ON) {
                return new ResponseEntity<>(ERROR, "所选择的借款产品处于未上架状态，不允许修改");
            }
        } else {
            return new ResponseEntity<>(ERROR, "没有查询到当前标的产品");
        }


        if (logger.isInfoEnabled()) {
            logger.info("更新标的信息开始：标的ID:{},标的详细信息：{}", bidInfoVO.getBidId(), bidInfoVO);
        }
        BidInfo bidInfo = new BidInfo();
        BidInfoDetail bidInfoDetail = new BidInfoDetail();
        bidInfoVO.setCreateUserId(userId);
        bidInfoVO.setModifyUserId(userId);

        // 执行拆分
        BeanPropertiesUtil.splitProperties(bidInfoVO, bidInfo, bidInfoDetail);
        bidInfo.setId(bidInfoVO.getBidId());

        bidInfo.setPrintImgurl(bidInfoVO.getPrintImgurl());
        bidInfo.setLendingTime(DateUtils.parse(bidInfoVO.getLendTime(), DateUtils.DATE_HH_MM_SS));
        // 设置关联id
        bidInfoDetail.setBiddInfoId(bidInfoVO.getBidId());

        //设置当前好友推荐规则
        bidInfoDetail.setRuleId(ruleId);

        BidInfo nowBidInfo = bidInfoDao.findByPK(Long.valueOf(bidInfo.getId()), BidInfo.class);
        BigDecimal totalMoney = bidInfoVO.getTotalAmount() != null ? bidInfoVO.getTotalAmount() : BigDecimal.ZERO;
        bidInfo.setTotalAmount(totalMoney.subtract(nowBidInfo.getTotalAmount()));

        if (bidInfoVO.getRaiseRate() != null) {
            bidInfo.setRaiseRate(bidInfoVO.getRaiseRate().subtract(nowBidInfo.getRaiseRate()));
        }
        if (bidInfoVO.getInterestRate() != null) {
            bidInfo.setInterestRate(bidInfoVO.getInterestRate().subtract(nowBidInfo.getInterestRate()));
        }
        if (bidInfoVO.getServiceRate() != null) {
            bidInfo.setServiceRate(bidInfoVO.getServiceRate().subtract(nowBidInfo.getServiceRate()));
        }
        if (bidInfoVO.getCommissionRate() != null) {
            bidInfo.setCommissionRate(bidInfoVO.getCommissionRate().subtract(nowBidInfo.getCommissionRate()));
        }
        if (bidInfoVO.getAdvanceServiceRate() != null) {
            bidInfo.setAdvanceServiceRate(bidInfoVO.getAdvanceServiceRate().subtract(nowBidInfo.getAdvanceServiceRate()));
        }
        if (bidInfo.getLiquidatedDamagesRate() != null) {
            bidInfo.setLiquidatedDamagesRate(bidInfo.getLiquidatedDamagesRate().subtract(nowBidInfo.getLiquidatedDamagesRate()));
        }

        //把标的的剩余可投金额跟标的总金额保持一致
        bidInfo.setResidueAmount(totalMoney.subtract(nowBidInfo.getTotalAmount()));
        ResponseEntity<?> updateResult = updateBidInfo(bidInfo, bidInfoDetail);
        if (SUCCESS == updateResult.getResStatus()) {
            if (logger.isInfoEnabled()) {
                logger.info("更新标的成功：标的ID:{},标的详细信息：{}", bidInfoVO.getBidId(), bidInfoVO);
            }
        } else {
            if (logger.isInfoEnabled()) {
                logger.info("更新标的失败：标的ID:{},错误原因:{}", bidInfoVO.getBidId(), updateResult);
            }
        }

        return updateResult;
    }


    @Override
    public List<BidInfoVO> findAutoInvestBidList(BidAutoScheme bidAutoScheme) {
        List<BidInfoVO> bidVoList = this.bidInfoDao.findAutoInvestBidList(bidAutoScheme);
        if (bidVoList != null && !bidVoList.isEmpty()) {
            bidVoList = bidVoList.stream().filter(o -> {    //过滤掉月份不满足的标的
                if (o.getTermUnit() == 1) {
                    return ((o.getTermValue() * 12) >= bidAutoScheme.getInvestTermMin() && o.getTermValue() * 12 <= bidAutoScheme.getInvestTermMax());
                } else if (o.getTermUnit() == 2) {
                    return ((o.getTermValue()) >= bidAutoScheme.getInvestTermMin() && o.getTermValue() <= bidAutoScheme.getInvestTermMax());
                } else {
                    return ((o.getTermValue() / 20) >= bidAutoScheme.getInvestTermMin() && o.getTermValue() / 30 <= bidAutoScheme.getInvestTermMax());
                }
            }).filter(o -> {    //过滤新手标
                if (String.valueOf(bidAutoScheme.getBidPriority()).endsWith("0") && o.getProductType() == BID_PRODUCT_PREFERRED) {
                    return false;
                }
                return true;
            }).collect(Collectors.toList());
            // 提取优先级
            int[] bidPriorityArray = new int[4];  // 高位 0,1,2,3 低位 14,13,12,11
            bidPriorityArray[0] = bidAutoScheme.getBidPriority() / 1000 == 9 ? 2 : (bidAutoScheme.getBidPriority() / 1000) + 10;
            bidPriorityArray[1] = bidAutoScheme.getBidPriority() / 100 % 10 == 9 ? 1 : (bidAutoScheme.getBidPriority() / 100 % 10) + 10;
            bidPriorityArray[2] = bidAutoScheme.getBidPriority() / 10 % 10 == 9 ? 0 : (bidAutoScheme.getBidPriority() / 10 % 10) + 10;
            bidPriorityArray[3] = bidAutoScheme.getBidPriority() % 10 == 9 ? 3 : (bidAutoScheme.getBidPriority() % 10) + 10;
            List<InvestPriorityVo> l = new ArrayList<>();
            l.add(new InvestPriorityVo(bidPriorityArray[0], bidVoList.stream().filter(o -> o.getProductType() == BID_PRODUCT_WINMONTH).collect(Collectors.toList())));
            l.add(new InvestPriorityVo(bidPriorityArray[1], bidVoList.stream().filter(o -> o.getProductType() == BID_PRODUCT_WINSEASON).collect(Collectors.toList())));
            l.add(new InvestPriorityVo(bidPriorityArray[2], bidVoList.stream().filter(o -> o.getProductType() == BID_PRODUCT_WINYEAR).collect(Collectors.toList())));
            l.add(new InvestPriorityVo(bidPriorityArray[3], bidVoList.stream().filter(o -> o.getProductType() == BID_PRODUCT_PREFERRED).collect(Collectors.toList())));
            Collections.sort(l,Comparator.comparing(InvestPriorityVo::getPriority).reversed());

            List<BidInfoVO> result = new ArrayList<>();
            l.forEach((e) -> {
                List<BidInfoVO> v = e.getList();
                if (v != null){
                    List<BidInfoVO> tempList;
                    // 投资期限优先 正序 创建时间正序
                    if(BaseUtil.equelsIntWraperPrimit(bidAutoScheme.getPriorityType(),2)){
                        tempList = v.stream().sorted(
                                Comparator.comparing(BidInfoVO::getTermValue).thenComparing((BidInfoVO b) -> b.getCreateTime().getTime())).collect(Collectors.toList());
                    // 年化利率优先 倒序  创建时间正序
                    }else {
                       tempList = v.stream().sorted(Comparator.comparing((BidInfoVO a) -> a.getInterestRate().doubleValue()).reversed()
                               .thenComparing((BidInfoVO b) -> b.getCreateTime().getTime())).collect(Collectors.toList());
                    }
                    result.addAll(tempList);
                }
            });
            bidVoList = result;
        }
        return bidVoList;
    }

    @Override
    public Pager findBidInfoSimpleVoList(Pager pager, BidInfoSimpleVo bidInfoSimpleVo) {
        return this.bidInfoDao.findBidInfoSimpleVoList(bidInfoSimpleVo, pager);
    }

    @Override
    public List<BidInfoVO> findBidInfoVOByCondition(BidInfoVO queryBidInfoVO) {
        return bidInfoDao.findBidInfoVOByCondition(queryBidInfoVO);
    }

    @Override
    public Map<Integer, BidInfoVO> findBidInfoDetailVoByIdList(Set<Integer> bidIdSet) {
        return this.bidInfoDao.findBidInfoDetailVoByIdList(bidIdSet);
    }

    @Override
    public String getViewOriginProjectSwitch(BidInfoVO bidInfoVO) {
        //查看源项目入口
        String viewOriginProjectSwitch = "0";
        //如果为优选标并且已放款才有查看源项目地址
        if (InvestConstants.BID_STATE_WAIT_REPAY == bidInfoVO.getState() && InvestConstants.MATCH_BID_TYPE_GOOD ==
                BidInfoUtil.matchBidTypeByProdutType(bidInfoVO.getProductType()) && StringUtils.isNotBlank
                (PropertiesHolder.getProperty("viewOriginProjectSwitch")) && "1".equals(PropertiesHolder.getProperty("viewOriginProjectSwitch"))){
            //判断优选标当前是否有匹配记录，有匹配记录展示入口，否则隐藏入口
            BidMatch condition = new BidMatch();
            condition.setGoodBidId(bidInfoVO.getId());
            condition.setState(MATCH_STATE_SUCCESS);
            List<BidMatch> list = this.bidMatchDao.findByCondition(condition);
            if (list.size() > 0){
                //优选开始期数判断当前时间是否有匹配记录
                int startTerm = DateUtils.getDaysBetween(bidInfoVO.getLendingTime(),new Date()) + 1;
                long count = list.stream().filter(bidMatch -> startTerm >= Integer.valueOf(bidMatch.getGoodTermValue()
                        .split(":")[0]) && startTerm <= Integer.valueOf(bidMatch.getGoodTermValue().split(":")[1])).count();
                if (count > 0){
                    viewOriginProjectSwitch = PropertiesHolder.getProperty("viewOriginProjectSwitch");
                }
            }
        }
        return viewOriginProjectSwitch;
    }


    /**
     *  @Description    ：判断是否是默认时间
     *  @Method_Name    ：isDefalutTime
     *  @param date
     *  @return boolean
     *  @Creation Date  ：2018/6/13
     *  @Author         ：zhongpingtang@hongkun.com.cn
     */
    private boolean isDefalutTime(Date date){
        return date.getTime()<0L;
    }

	@Override
	public List<BidInfo> findPurchaseBidInfoList(List<Integer> productTypeList, Integer state) {
		return this.bidInfoDao.findPurchaseBidInfoList(productTypeList, state);
	}

    @Override
    public Pager findStaFunBid(Pager pager, StaFunBidVO staFunBidVO) {
        return this.bidInfoDao.findStaFunBid(pager, staFunBidVO);
    }

    @Override
    public Map<String, Object> findStaBidUserAmountCount(StaFunBidVO staFunBidVO) {
        //查询借款人数&标的金额&标的数量
        Map<String, Object> result = this.bidInfoDao.findStaBidUserAmountCount(staFunBidVO);
        //查询投资人数量、投资笔数、投资金额
        StaFunInvestVO staFunInvestVO = new StaFunInvestVO();
        staFunInvestVO.setStartTime(staFunBidVO.getStartTime());
        staFunInvestVO.setEndTime(staFunBidVO.getEndTime());
        result.putAll(this.bidInvestDao.findStaFunInvestAddup(staFunInvestVO));
        return result;
    }

    @Override
    public List<BidInfoExchangeForAppVo> findBidInfoExchangeListForApp() {
        return this.bidInfoDao.findBidInfoExchangeListForApp();
    }

}
