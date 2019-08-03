package com.hongkun.finance.invest.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.invest.constants.InvestConstants;
import com.hongkun.finance.invest.dao.*;
import com.hongkun.finance.invest.model.*;
import com.hongkun.finance.invest.model.vo.TransferAutoVo;
import com.hongkun.finance.invest.service.BidTransferAutoService;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.CommonUtils;
import com.yirun.framework.core.utils.CompareUtil;
import com.yirun.framework.core.utils.pager.Pager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.invest.service.impl.BidTransferAutoServiceImpl.java
 * @Class Name    : BidTransferAutoServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class BidTransferAutoServiceImpl implements BidTransferAutoService {

	private static final Logger logger = LoggerFactory.getLogger(BidTransferAutoServiceImpl.class);
	
	/**
	 * BidTransferAutoDAO
	 */
	@Autowired
	private BidTransferAutoDao bidTransferAutoDao;
	@Autowired
	private BidInvestDao bidInvestDao;
	@Autowired
	private BidTransferRecordDao bidTransferRecordDao;
	@Autowired
	private BidMatchDao bidMatchDao;
	@Autowired
	private BidInfoDetailDao bidInfoDetailDao;
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertBidTransferAuto(BidTransferAuto bidTransferAuto) {
		this.bidTransferAutoDao.save(bidTransferAuto);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertBidTransferAutoBatch(List<BidTransferAuto> list) {
		this.bidTransferAutoDao.insertBatch(BidTransferAuto.class, list);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertBidTransferAutoBatch(List<BidTransferAuto> list, int count) {
		if(logger.isDebugEnabled()){
			logger.debug("default batch insert size is " + count);
		}
		this.bidTransferAutoDao.insertBatch(BidTransferAuto.class, list, count);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateBidTransferAuto(BidTransferAuto bidTransferAuto) {
		this.bidTransferAutoDao.update(bidTransferAuto);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateBidTransferAutoBatch(List<BidTransferAuto> list, int count) {
		this.bidTransferAutoDao.updateBatch(BidTransferAuto.class, list, count);
	}
	
	@Override
	public BidTransferAuto findBidTransferAutoById(int id) {
		return this.bidTransferAutoDao.findByPK(Long.valueOf(id), BidTransferAuto.class);
	}
	
	@Override
	public List<BidTransferAuto> findBidTransferAutoList(BidTransferAuto bidTransferAuto) {
		return this.bidTransferAutoDao.findByCondition(bidTransferAuto);
	}
	
	@Override
	public List<BidTransferAuto> findBidTransferAutoList(BidTransferAuto bidTransferAuto, int start, int limit) {
		return this.bidTransferAutoDao.findByCondition(bidTransferAuto, start, limit);
	}
	
	@Override
	public Pager findBidTransferAutoList(BidTransferAuto bidTransferAuto, Pager pager) {
		return this.bidTransferAutoDao.findByCondition(bidTransferAuto, pager);
	}
	
	@Override
	public int findBidTransferAutoCount(BidTransferAuto bidTransferAuto){
		return this.bidTransferAutoDao.getTotalCount(bidTransferAuto);
	}
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public ResponseEntity<?> addTransferRecord(List<BidInfo> transferBids,List<BidInfo> newGoodBids,Date transferDate
			,BidInfo comnBidInfo){
		//对标的记录按照id排序
		transferBids.sort((BidInfo b1,BidInfo b2)->b1.getId().compareTo(b2.getId()));
		newGoodBids.sort((BidInfo b1,BidInfo b2)->b1.getId().compareTo(b2.getId()));
		List<BidTransferRecord> resultList = new ArrayList<BidTransferRecord>();
		BigDecimal transferMoney = new BigDecimal(0);
		BigDecimal newGoodMoney = new BigDecimal(0);
		int n = 0;
		for(BidInfo bidInfo:transferBids){
			for(;n<newGoodBids.size();){
				if(CompareUtil.eZero(transferMoney)){
					transferMoney = bidInfo.getTotalAmount();
				}
				if(CompareUtil.eZero(newGoodMoney)){
					newGoodMoney = newGoodBids.get(n).getTotalAmount();
				}
				BidTransferRecord transferRecord = new BidTransferRecord();
				transferRecord.setTransferbidid(bidInfo.getId());
				transferRecord.setReceivebidid(newGoodBids.get(n).getId());
				transferRecord.setCommonbidid(comnBidInfo.getId());
				transferRecord.setTransferTime(transferDate);
				if(CompareUtil.gt(transferMoney, newGoodMoney)){
					//转让金额 newGoodMoney
					transferRecord.setTransferAmount(newGoodMoney);
					resultList.add(transferRecord);
					transferMoney = transferMoney.subtract(newGoodMoney);
					n++;
					continue;
				}
				//转让金额 transferMoney
				transferRecord.setTransferAmount(transferMoney);
				resultList.add(transferRecord);
				newGoodMoney = newGoodMoney.subtract(transferMoney);
				if(CompareUtil.eZero(newGoodMoney)){
					n++;
				}
				break;
			}
		}
		bidTransferRecordDao.insertBatch(BidTransferRecord.class, resultList);
		return new ResponseEntity<String>(Constants.SUCCESS,"转让成功");
	}
	
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public  ResponseEntity<?> match(List<BidInvest> bidInvestList, List<TransferAutoVo> transferAutoVos,
			List<BidMatch> matchList, List<BidInvest> updateGoodInvestList, List<BidInfoDetail> bidInfoDetailList,
									int adminUserId){
		//生成债权转让协议的投资记录id集合
		List<Integer> investListForTransferContract = new ArrayList<>();
		//生成散标产品对应合同的投资记录id集合
		List<Integer> investListForCommonContract = new ArrayList<>();

		if (CommonUtils.isNotEmpty(bidInvestList)){
			bidInvestDao.insertBatch(BidInvest.class, bidInvestList);
			bidInvestList.forEach(invest -> investListForCommonContract.add(invest.getId()));
		}
		if(CommonUtils.isNotEmpty(transferAutoVos)){
			for(TransferAutoVo vo:transferAutoVos){
				List<BidInvest> newInvestList = vo.getReceiveInvestList();
				bidInvestDao.insertBatch(BidInvest.class, newInvestList,newInvestList.size());
				newInvestList.forEach(invest -> investListForTransferContract.add(invest.getId()));
				addTransferAuto(vo.getCommBidInfo(), vo.getTransferInvestList(), newInvestList, vo.getTransferTime(), adminUserId);
			}
		}
		if(CommonUtils.isNotEmpty(matchList)){
			bidMatchDao.insertBatch(BidMatch.class, matchList);
		}
		if(CommonUtils.isNotEmpty(updateGoodInvestList)){
			bidInvestDao.updateBatch(BidInvest.class, updateGoodInvestList, updateGoodInvestList.size());
		}
		if(CommonUtils.isNotEmpty(bidInfoDetailList)){
			bidInfoDetailDao.updateBatch(BidInfoDetail.class, bidInfoDetailList, bidInfoDetailList.size());
		}
		ResponseEntity<?> resultRes = new ResponseEntity<>();
		Map<String, Object> params = new HashMap<>(2);
		params.put("investListForCommonContract", investListForCommonContract);
		params.put("investListForTransferContract", investListForTransferContract);
		resultRes.setParams(params);
		resultRes.setResStatus(Constants.SUCCESS);
		return resultRes;
	}
	/**
	 *  @Description    : 债权转让（后台匹配）
	 *  另：默认散标每次都能够匹配满，所以每次优选到期的转让都会是完全转让
	 *  @Method_Name    : addTransferAuto
	 *  @param comnBidInfo        	散标
	 *  @param transferInvestList   原债权投资记录
	 *  @param receiveInvestList    新债权投资记录
	 *  @return
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年6月17日 上午11:22:35 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public ResponseEntity<?> addTransferAuto(BidInfo comnBidInfo,
			List<BidInvest> transferInvestList,
			List<BidInvest> receiveInvestList,Date transferTime,int adminUserId) {
		//对投资记录排序 按照标的id
		transferInvestList.sort((BidInvest b1,BidInvest b2)->b1.getBidInfoId().compareTo(b2.getBidInfoId()));
		receiveInvestList.sort((BidInvest b1,BidInvest b2)->b1.getBidInfoId().compareTo(b2.getBidInfoId()));
		//新债权剩余金额
		BigDecimal newResuAmount = new BigDecimal(0) ;
		//老债权剩余金额
		BigDecimal oldResuAmount = new BigDecimal(0) ;
		int index = 0 ;
		//新债权<=老债权
		for(BidInvest newInvest:receiveInvestList){
			//新债权金额
			BigDecimal investAmount = newInvest.getInvestAmount();
			//新债权剩余匹配金额
			if(CompareUtil.eZero(newResuAmount)){
				newResuAmount = investAmount;
			}
			for(;index<=transferInvestList.size()-1;){
				BidInvest oldInvest = transferInvestList.get(index);
				//老债权投资金额
				BigDecimal oldInvestAmount =  new BigDecimal(0) ;
				if(CompareUtil.eZero(oldResuAmount)){
					oldInvestAmount = oldInvest.getInvestAmount().subtract(oldInvest.getTransAmount());
				}else{
					oldInvestAmount = oldResuAmount;
				}
				//老债权金额<=新债权金额  全部转让
				if(CompareUtil.gte(newResuAmount, oldInvestAmount)){
					//1、全部转让 债权转让关系
					addCreditorTransfer(oldInvest, newInvest, comnBidInfo, oldInvestAmount,transferTime,adminUserId);
					//2、修改投资状态
					BidInvest condition = new BidInvest();
					condition.setId(oldInvest.getId());
					condition.setState(InvestConstants.INVEST_STATE_AUTO);
					bidInvestDao.update(condition);
					newResuAmount = newResuAmount.subtract(oldInvestAmount);
					index++;
					oldResuAmount =  new BigDecimal(0) ;
					if(CompareUtil.eZero(newResuAmount)){
						break;
					}
				}else{
					//部分转让
					addCreditorTransfer(oldInvest, newInvest, comnBidInfo, newResuAmount,transferTime,adminUserId);
					oldResuAmount = oldInvestAmount.subtract(newResuAmount);
					newResuAmount = new BigDecimal(0) ;
					break;
				}
			}
		}
		return new ResponseEntity<String>(Constants.SUCCESS,"转让成功");
	}
	/**
	 *  @Description    : 添加债权转让记录
	 *  @Method_Name    : addCreditorTransfer
	 *  @param oldInvest
	 *  @param newInvest
	 *  @param comnBidInfo
	 *  @param creditorAmount
	 *  @param adminUserId
	 *  @param transferTime 转让时间
	 *  @return         : void
	 *  @Creation Date  : 2017年6月17日 下午2:42:17 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void addCreditorTransfer(BidInvest oldInvest, BidInvest newInvest,
			BidInfo comnBidInfo, BigDecimal creditorAmount,Date transferTime,int adminUserId) {
		//原债权人code 
		BidTransferAuto transferAuto = new BidTransferAuto();
		transferAuto.setBidInfoId(comnBidInfo.getId());
		//新债权信息
		transferAuto.setNewInvestId(newInvest.getId());
		transferAuto.setBuyUserId(newInvest.getRegUserId());
		//原债权信息
		transferAuto.setOldInvestId(oldInvest.getId());
		transferAuto.setInvestUserId(oldInvest.getRegUserId());
		//数据
		transferAuto.setCreditorAmount(creditorAmount);
		transferAuto.setTransferAmount(creditorAmount);
		transferAuto.setUserRate(comnBidInfo.getInterestRate());
		//其他
		transferAuto.setCreateUserId(adminUserId);
		transferAuto.setTransferRate(comnBidInfo.getInterestRate());
		transferAuto.setTransferDays(1);
		//已转让
		transferAuto.setState(InvestConstants.INVEST_TRANSFER_AUTO_STATE_MATCH);
		//时间
		transferAuto.setCreateTime(new Date());
		transferAuto.setTransferTime(transferTime);
		bidTransferAutoDao.save(transferAuto);
	}
	
}
