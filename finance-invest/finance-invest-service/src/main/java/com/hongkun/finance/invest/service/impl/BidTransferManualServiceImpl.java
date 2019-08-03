package com.hongkun.finance.invest.service.impl;

import java.math.BigDecimal;
import java.util.*;

import com.alibaba.fastjson.JSON;
import com.hongkun.finance.invest.dao.BidInfoDao;
import com.hongkun.finance.invest.model.vo.*;
import com.hongkun.finance.invest.service.BidInfoService;
import com.hongkun.finance.invest.util.BidInfoUtil;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.utils.CompareUtil;
import org.apache.commons.collections.ArrayStack;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.Serializers;
import org.mengyun.tcctransaction.api.Compensable;
import org.mengyun.tcctransaction.dubbo.context.DubboTransactionContextEditor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.invest.constants.InvestConstants;
import com.hongkun.finance.invest.dao.BidInvestDao;
import com.hongkun.finance.invest.dao.BidTransferAutoDao;
import com.hongkun.finance.invest.dao.BidTransferManualDao;
import com.hongkun.finance.invest.model.BidInfo;
import com.hongkun.finance.invest.model.BidInvest;
import com.hongkun.finance.invest.model.BidTransferAuto;
import com.hongkun.finance.invest.model.BidTransferManual;
import com.hongkun.finance.invest.service.BidTransferManualService;
import com.hongkun.finance.user.model.RegUserDetail;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.exception.BusinessException;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.CommonUtils;
import com.yirun.framework.core.utils.pager.Pager;

import static com.hongkun.finance.invest.constants.InvestConstants.*;
import static com.hongkun.finance.qdz.constant.QdzConstants.CREDITOR_FLAG_BUY;
import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * @Project : finance
 * @Program Name :
 *          com.hongkun.finance.invest.service.impl.BidTransferManualServiceImpl.
 *          java
 * @Class Name : BidTransferManualServiceImpl.java
 * @Description : GENERATOR SERVICE实现类
 * @Author : generator
 */
@Service
public class BidTransferManualServiceImpl implements BidTransferManualService {

	private static final Logger logger = LoggerFactory.getLogger(BidTransferManualServiceImpl.class);

	/**
	 * BidTransferManualDAO
	 */
	@Autowired
	private BidTransferManualDao bidTransferManualDao;

	@Autowired
	private BidInvestDao bidInvestDao;

	@Autowired
	private BidTransferAutoDao bidTransferAutoDao;
	@Autowired
	private BidInfoDao bidInfoDao;

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertBidTransferManual(BidTransferManual bidTransferManual) {
		this.bidTransferManualDao.save(bidTransferManual);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertBidTransferManualBatch(List<BidTransferManual> list) {
		this.bidTransferManualDao.insertBatch(BidTransferManual.class, list);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertBidTransferManualBatch(List<BidTransferManual> list, int count) {
		if (logger.isDebugEnabled()) {
			logger.debug("default batch insert size is " + count);
		}
		this.bidTransferManualDao.insertBatch(BidTransferManual.class, list, count);
	}

	@Override
	@Compensable(cancelMethod = "updateBidTransferManualCancel", transactionContextEditor = DubboTransactionContextEditor.class)
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public int updateBidTransferManual(BidTransferManual bidTransferManual) {
		try {
			logger.info("{}, 更新转让记录信息, bidTransferManual: {}",BaseUtil.getTccTryLogPrefix(),bidTransferManual.toString());
			return this.bidTransferManualDao.update(bidTransferManual);
		} catch (Exception e) {
			logger.error("{}, 更新转让记录信息, bidTransferManual: {}, 异常信息:\n",BaseUtil.getTccTryLogPrefix(),bidTransferManual.toString(),e);
			throw  new GeneralException("更新转让记录信息失败");
		}
	}

	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateBidTransferManualCancel(BidTransferManual bidTransferManual){
		try {
			logger.info("{}, 更新转让记录信息回滚方法, bidTransferManual: {}",BaseUtil.getTccTryLogPrefix(),bidTransferManual.toString());
			bidTransferManual.setState(INVEST_TRANSFER_MANUAL_STATE_IN);
			bidTransferManual.setOldState(INVEST_TRANSFER_MANUAL_STATE_OVER);
			this.bidTransferManualDao.update(bidTransferManual);
		} catch (Exception e) {
			logger.error("{}, 更新转让记录信息回滚失败, bidTransferManual: {}, 异常信息:\n",BaseUtil.getTccTryLogPrefix(),bidTransferManual.toString(),e);
			throw  new GeneralException("更新转让记录信息回滚失败");
		}
	}
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateBidTransferManualBatch(List<BidTransferManual> list, int count) {
		this.bidTransferManualDao.updateBatch(BidTransferManual.class, list, count);
	}

	@Override
	public BidTransferManual findBidTransferManualById(int id) {
		return this.bidTransferManualDao.findByPK(Long.valueOf(id), BidTransferManual.class);
	}

	@Override
	public List<BidTransferManual> findBidTransferManualList(BidTransferManual bidTransferManual) {
		return this.bidTransferManualDao.findByCondition(bidTransferManual);
	}

	@Override
	public List<BidTransferManual> findBidTransferManualList(BidTransferManual bidTransferManual, int start,
			int limit) {
		return this.bidTransferManualDao.findByCondition(bidTransferManual, start, limit);
	}

	@Override
	public Pager findBidTransferManualList(BidTransferManual bidTransferManual, Pager pager) {
		return this.bidTransferManualDao.findByCondition(bidTransferManual, pager);
	}

	@Override
	public int findBidTransferManualCount(BidTransferManual bidTransferManual) {
		return this.bidTransferManualDao.getTotalCount(bidTransferManual);
	}

	@Override
	@Compensable(cancelMethod = "buyCreditorCancel", transactionContextEditor = DubboTransactionContextEditor.class)
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public ResponseEntity<?> buyCreditor(BidTransferManual bidTransfer, int investUserId, String investUserName,
			BigDecimal userRate) {
		try {
			logger.info("{}, 购买债权处理投资记录&转让记录, 转让信息bidTransfer: {}, 认购方用户id: {}, 认购方用户姓名: {}, 认购方年化率: {}", BaseUtil.getTccTryLogPrefix(),bidTransfer.toString(), investUserId, investUserName, userRate);
			if (bidTransfer == null) {
				return new ResponseEntity<>(Constants.ERROR, "转让关系不存在");
			}
			BidInvest bidInvest = new BidInvest();
			bidInvest.setRegUserId(investUserId);
			bidInvest.setRealName(investUserName);
			bidInvest.setInvestAmount(bidTransfer.getCreditorAmount());
			bidInvest.setBidInfoId(bidTransfer.getBidInfoId());
			bidInvest.setPayAmount(bidTransfer.getTransferAmount());
			bidInvest.setState(INVEST_STATE_SUCCESS_BUYER);
			bidInvest.setCreateTime(new Date());
			bidInvestDao.save(bidInvest);
			BidTransferManual bidTransferManual = new BidTransferManual();
			bidTransferManual.setId(bidTransfer.getId());
			bidTransferManual.setTransferTime(new Date());
			bidTransferManual.setUserRate(userRate);
			bidTransferManual.setState(InvestConstants.INVEST_TRANSFER_MANUAL_STATE_OVER);
			bidTransferManual.setNewInvestId(bidInvest.getId());
			bidTransferManual.setBuyUserId(investUserId);
			bidTransferManual.setBuyerHoldDays(bidTransfer.getBuyerHoldDays());
			this.bidTransferManualDao.update(bidTransferManual);

			BidInvest oldBidInvest = bidInvestDao.findByPK(Long.valueOf(bidTransfer.getOldInvestId()),BidInvest.class);
			BidInvest oldGoodInvest = new BidInvest();
			oldGoodInvest.setId(bidTransfer.getOldInvestId());
			oldGoodInvest.setTransAmount(bidTransfer.getCreditorAmount());
			if (CompareUtil.eq(oldBidInvest.getInvestAmount(),oldBidInvest.getTransAmount().add(bidTransfer.getCreditorAmount()))){
				oldGoodInvest.setState(InvestConstants.INVEST_STATE_MANUAL);
			}else{
				oldGoodInvest.setState(InvestConstants.INVEST_STATE_SUCCESS);
			}
			bidInvestDao.update(oldGoodInvest);

			BidInvest bidInvestContidion = new BidInvest();
			bidInvestContidion.setGoodInvestId(bidTransfer.getOldInvestId());
			bidInvestContidion.setSortColumns("id asc");
			BigDecimal creditorAmount= bidTransfer.getCreditorAmount();

			List<BidInvest> oldUpdateBidInvests = new ArrayList<BidInvest>();
			List<Integer> creditorInvestIds = new ArrayList<Integer>();
			List<Integer> buyerInvestIds = new ArrayList<Integer>();
			List<BidTransferAuto> transferAutoList = new ArrayList<BidTransferAuto>();
			List<BidInvest> oldSanList = bidInvestDao.findByCondition(bidInvestContidion);
			Set<Integer> sanBidIds = new HashSet<>();
			if (oldSanList != null && oldSanList.size() > 0) {
				BigDecimal sumMoney = BigDecimal.ZERO;
				for (BidInvest oldInvest:oldSanList){
					BidInfo bidInfo = bidInfoDao.findByPK(Long.valueOf(oldInvest.getBidInfoId()),BidInfo.class);
					sanBidIds.add(bidInfo.getId());
					BigDecimal newInvestAmount = oldInvest.getInvestAmount().subtract(oldInvest.getTransAmount());
					sumMoney = sumMoney.add(newInvestAmount);
					BidInvest sanInvest = new BidInvest();
					BeanUtils.copyProperties(oldInvest, sanInvest);
					sanInvest.setId(null);
					sanInvest.setCreateTime(null);
					sanInvest.setModifyTime(null);
					sanInvest.setRegUserId(investUserId);
					sanInvest.setRealName(investUserName);
					sanInvest.setTransAmount(BigDecimal.ZERO);
					sanInvest.setInvestTerm(oldInvest.getInvestTerm());
					sanInvest.setGoodInvestTerm(oldInvest.getGoodInvestTerm());
					sanInvest.setGoodInvestId(bidInvest.getId());
					sanInvest.setInvestAmount(newInvestAmount);
					sanInvest.setPayAmount(newInvestAmount);
					if (CompareUtil.gt(sumMoney,creditorAmount)){
						//此笔部分转让
						newInvestAmount = creditorAmount.subtract(sumMoney.subtract(newInvestAmount));
						sanInvest.setInvestAmount(newInvestAmount);
						sanInvest.setPayAmount(newInvestAmount);
						BidInvest updateInvest = new BidInvest();
						updateInvest.setTransAmount(newInvestAmount);
						updateInvest.setId(oldInvest.getId());
						oldUpdateBidInvests.add(updateInvest);
					}else{
						// 更新旧的投资记录为已转让
						BidInvest updateInvest = new BidInvest();
						updateInvest.setTransAmount(newInvestAmount);
						updateInvest.setState(InvestConstants.INVEST_STATE_MANUAL);
						updateInvest.setId(oldInvest.getId());
						oldUpdateBidInvests.add(updateInvest);
					}
					creditorInvestIds.add(oldInvest.getId());
					bidInvestDao.save(sanInvest);
					buyerInvestIds.add(sanInvest.getId());
					// 保存债权转让关系
					bidTransfer.setBuyUserId(investUserId);
					BidTransferAuto tranferAuto = initBidTransferAuto(bidTransfer, oldInvest.getBidInfoId(),
							sanInvest.getInvestAmount(), oldInvest.getId(), sanInvest.getId());
					transferAutoList.add(tranferAuto);
					if (CompareUtil.gte(sumMoney,bidTransfer.getCreditorAmount())){
						break;
					}
				}

				if (CommonUtils.isNotEmpty(oldUpdateBidInvests)){
					bidInvestDao.updateBatch(BidInvest.class,oldUpdateBidInvests,50);
				}
				if (CommonUtils.isNotEmpty(transferAutoList)){
					bidTransferAutoDao.insertBatch(BidTransferAuto.class,transferAutoList);
				}
			}
			Map<String,Object> params = new HashMap<String,Object>();
			List<Integer> goodInvestIds = new ArrayList<Integer>();
			goodInvestIds.add(bidTransfer.getOldInvestId());
			goodInvestIds.add(bidInvest.getId());
			//如果是散标直投，把投资记录加入到债权转让列表中
			BidInfoVO bidInfoVO = bidInfoDao.findBidInfoDetailVoById(bidTransfer.getBidInfoId());
			if(BidInfoUtil.isCommonBid(bidInfoVO.getProductType())){
				buyerInvestIds.add(bidInvest.getId());
				creditorInvestIds.add(bidTransfer.getOldInvestId());
			}
			//用于生成优选计划
			params.put("goodInvestIds",goodInvestIds);
			//用于生成债权转让协议
			params.put("buyerInvestIds",buyerInvestIds);
			params.put("creditorInvestIds",creditorInvestIds);
			params.put("newInvestId",bidInvest.getId());
			params.put("sanBidIds",sanBidIds);
			logger.info("购买债权,涉及的散标标的id: {}",JSON.toJSON(sanBidIds));
			return new ResponseEntity<>(Constants.SUCCESS, "购买债权成功",params);
		} catch (BeansException e) {
			logger.info("{}, 购买债权处理投资记录&转让记录, 转让信息bidTransfer: {}, 认购方用户id: {}, 认购方用户姓名: {}, 认购方年化率: {}, 异常信息: \n",
					BaseUtil.getTccTryLogPrefix(),bidTransfer.toString(), investUserId, investUserName, userRate,e);
			throw new BusinessException("购买债权失败");
		}
	}
	/**
	*  @Description    ：购买债权--处理投资记录和转让记录tcc回滚方法
	*  @Method_Name    ：buyCreditorCancel
	*  @param bidTransfer
	*  @param investUserId
	*  @param investUserName
	*  @param userRate
	*  @return void
	*  @Creation Date  ：2018/4/23
	*  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
	*/
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void buyCreditorCancel(BidTransferManual bidTransfer, int investUserId, String investUserName,BigDecimal userRate){
		logger.info("{}, 购买债权处理投资记录&转让记录, 转让信息bidTransfer: {}, 认购方用户id: {}, 认购方用户姓名: {}, 认购方年化率: {}", BaseUtil.getTccTryLogPrefix(),bidTransfer.toString(), investUserId, investUserName, userRate);
		try {
			List<Integer> delIds = new ArrayList<Integer>();
			List<Integer> autoIds = new ArrayList<Integer>();
			List<Integer> oldInvestIds = new ArrayList<Integer>();
			List<BidInvest> oldUpdateBidInvests = new ArrayList<BidInvest>();
			//1、优选
			BidTransferManual bidTransferManual = bidTransferManualDao.findByPK(Long.valueOf(bidTransfer.getId()),BidTransferManual.class);
			//删除新的投资记录
			if(CommonUtils.gtZero(bidTransferManual.getNewInvestId())){
				delIds.add(bidTransferManual.getNewInvestId());
			}
			//更新旧的投资记录
			BidInvest oldGoodInvest = new BidInvest();
			oldGoodInvest.setId(bidTransfer.getOldInvestId());
			oldGoodInvest.setTransAmount(bidTransfer.getCreditorAmount().multiply(new BigDecimal(-1)));
			oldGoodInvest.setState(InvestConstants.INVEST_STATE_SUCCESS);
			bidInvestDao.update(oldGoodInvest);

			BidTransferManual transferCdt = new BidTransferManual();
			transferCdt.setId(bidTransfer.getId());
			transferCdt.setTransferTime(new Date());
			transferCdt.setUserRate(BigDecimal.ZERO);
			transferCdt.setState(InvestConstants.INVEST_TRANSFER_MANUAL_STATE_IN);
			transferCdt.setNewInvestId(0);
			transferCdt.setBuyUserId(0);
			this.bidTransferManualDao.update(transferCdt);
			//2、散标
			BidInvest bidInvestContidion = new BidInvest();
			bidInvestContidion.setGoodInvestId(bidTransfer.getOldInvestId());
			bidInvestContidion.setSortColumns("id asc");
			List<BidInvest> oldSanList = bidInvestDao.findByCondition(bidInvestContidion);
			if(CommonUtils.isNotEmpty(oldSanList)){
				BigDecimal sumMoney = BigDecimal.ZERO;
				BigDecimal creditorMoney = bidTransferManual.getCreditorAmount();
				for (BidInvest oldInvest:oldSanList){
					sumMoney = sumMoney.add(oldInvest.getTransAmount());
					if (CompareUtil.gte(creditorMoney,sumMoney)){
						//释放已转让得债权 <= 本次转让总金额  此笔投资记录全部释放
						BidInvest updateInvest = new BidInvest();
						updateInvest.setTransAmount(BigDecimal.ZERO);
						updateInvest.setState(InvestConstants.INVEST_STATE_SUCCESS);
						updateInvest.setId(oldInvest.getId());
						oldUpdateBidInvests.add(updateInvest);
					}else{
						//释放已转让得债权 > 本次转让总金额  此笔投资记录部分释放
						BidInvest updateInvest = new BidInvest();
						updateInvest.setTransAmount(creditorMoney.subtract(sumMoney.subtract(oldInvest.getTransAmount())));
						updateInvest.setState(InvestConstants.INVEST_STATE_SUCCESS);
						updateInvest.setId(oldInvest.getId());
						oldUpdateBidInvests.add(updateInvest);
					}
					if (CompareUtil.gte(sumMoney,creditorMoney)){
						break;
					}
				}
			}
			if (CommonUtils.gtZero(bidTransferManual.getNewInvestId())){
				BidInvest newBidInvestContidion = new BidInvest();
				newBidInvestContidion.setGoodInvestId(bidTransferManual.getNewInvestId());
				List<BidInvest> newSanList = bidInvestDao.findByCondition(newBidInvestContidion);
				if(CommonUtils.isNotEmpty(newSanList)){
					newSanList.forEach(sanInvest->{
						delIds.add(sanInvest.getId());
						autoIds.add(sanInvest.getId());
					});
				}
			}
			if (CommonUtils.isNotEmpty(delIds)){
				bidInvestDao.delBatchByIds(delIds);
			}
			if(CommonUtils.isNotEmpty(autoIds)){
				bidTransferAutoDao.delByNewInvestIds(autoIds);
			}
			if(CommonUtils.isNotEmpty(oldUpdateBidInvests)){
				bidInvestDao.updateBatch(BidInvest.class,oldUpdateBidInvests,50);
			}
		}catch (Exception e){
			logger.info("{}, 购买债权处理投资记录&转让记录回滚, 转让信息bidTransfer: {}, 认购方用户id: {}, 认购方用户姓名: {}, 认购方年化率: {}, 异常信息: \n",
					BaseUtil.getTccTryLogPrefix(),bidTransfer.toString(), investUserId, investUserName, userRate,e);
			throw new BusinessException("购买债权失败");
		}
	}
	/**
	 * @Description : 根据手动转让关系建立散标投资记录之间转让关系
	 * @Method_Name : initBidTransferAuto
	 * @param bidTransfer
	 * @param bidInfoId
	 * @param amount
	 *            转让金额
	 * @param oldInvestId
	 * @param newInvestId
	 * @return
	 * @return : BidTransferAuto
	 * @Creation Date : 2017年6月13日 下午2:37:47
	 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	private BidTransferAuto initBidTransferAuto(BidTransferManual bidTransfer, Integer bidInfoId, BigDecimal amount,
			Integer oldInvestId, Integer newInvestId) {
		BidTransferAuto tranferAuto = new BidTransferAuto();
		tranferAuto.setBidInfoId(bidInfoId);
		tranferAuto.setBuyUserId(bidTransfer.getBuyUserId());
		tranferAuto.setInvestUserId(bidTransfer.getInvestUserId());
		tranferAuto.setNewInvestId(newInvestId);
		tranferAuto.setOldInvestId(oldInvestId);
		tranferAuto.setCreditorAmount(amount);
		tranferAuto.setTransferAmount(amount);
		tranferAuto.setPayAmount(amount);
		tranferAuto.setState(InvestConstants.INVEST_TRANSFER_AUTO_STATE_MANUAL);
		tranferAuto.setUserRate(bidTransfer.getUserRate());
		return tranferAuto;
	}

	@Override
	public Pager myCreditor(BidTransferManual contidion, Pager pager) {
		if (contidion.getState()==INVEST_TRANSFER_MANUAL_STATE_INIT){
			//可转让债权
			return bidTransferManualDao.findInvestForTransfer(contidion.getInvestUserId(),pager);
		}
		//转让中债权&已转让债权
		return bidTransferManualDao.findInvestTransfering(contidion,pager);
	}

	@Override
	public TransferManualAppPreVo findBidTransferDetailByInvestId(Integer investId) {
		return bidTransferManualDao.findBidTransferDetailByInvestId(investId);
	}

	@Override
	public TransferManualDetailAppVo findBidTransferDetailByTransferId(Integer transferId) {
		return bidTransferManualDao.findBidTransferDetailByTransferId(transferId);
	}

	@Override
	public BidTransferManual findBidTransferManualByUnique(Integer id, String field) {
		if (StringUtils.isBlank(field) || id == null) {
			return null;
		}
		BidTransferManual contidion = new BidTransferManual();
		if (InvestConstants.INVEST_MANUAL_FIELD_NEWINVESTID.equals(field)) {
			contidion.setNewInvestId(id);
		}
		if (InvestConstants.INVEST_MANUAL_FIELD_OLDINVESTID.equals(field)) {
			contidion.setOldInvestId(id);
		}
		List<BidTransferManual> resultList = this.bidTransferManualDao.findByCondition(contidion);
		if (resultList != null && resultList.size() > 0) {
			return resultList.get(0);
		}
		return null;
	}

	@Override
	public BigDecimal findSumTransferedMoney(Integer investId) {
		return this.bidTransferManualDao.findSumTransferableMoney(investId);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public ResponseEntity<?> updateTransferManualRecords(BidInvest insertInvest, List<BidInvest> updateInvests,
			BidTransferAuto tranferAuto) throws Exception {
		int bidInvestId = 0;
		ResponseEntity<?> entity = new ResponseEntity<>();
		entity.setResStatus(Constants.SUCCESS);
		try {
			Map<String, Object> recordIds = new HashMap<>();
			// 插入新债券人的投资记录
			if (insertInvest != null) {
				this.bidInvestDao.save(insertInvest);
				bidInvestId = insertInvest.getId();
				recordIds.put("bidInvestId", bidInvestId);
			}
			// 更新源债券人的投资记录
			if (updateInvests != null && !updateInvests.isEmpty()) {
				this.bidInvestDao.updateBatch(BidInvest.class, updateInvests, 100);
			}
			// 插入转让关系
			if (tranferAuto != null) {
				if (tranferAuto.getNewInvestId() == null) {
					tranferAuto.setNewInvestId(bidInvestId);
				}
				this.bidTransferAutoDao.save(tranferAuto);
				recordIds.put("bidTransferAutoId", tranferAuto.getId());
			}
			entity.setParams(recordIds);
		} catch (Exception e) {
			logger.error("债券关系、投资记录更新异常！", e);
			throw new BusinessException("债券关系、投资记录更新异常！");
		}
		return entity;
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void rollBackManualRecords(BidInvest thirdBiddInvest, BidInvest qdzBidInvest, BidTransferAuto tranferAuto,
			int creditorFlag) throws Exception {
		try {
			this.bidInvestDao.update(thirdBiddInvest);
			// 如果购买债权，则删除个人投资记录，否则释放债权，则更新投资记录
			if (creditorFlag == CREDITOR_FLAG_BUY) {
				this.bidInvestDao.delete(qdzBidInvest.getId().longValue(), BidInvest.class);
			} else {
				this.bidInvestDao.update(qdzBidInvest);
			}
			this.bidTransferAutoDao.delete(tranferAuto.getId().longValue(), BidTransferAuto.class);
		} catch (Exception e) {
			throw new BusinessException("回滚投资记录和债权转让关系失败" + e.getMessage());
		}
	}

	@Override
	public Pager myCreditorForTransfer(BidTransferManual tranferContidion, Pager pager) {
		return bidTransferManualDao.myCreditorForTransfer(tranferContidion, pager);
	}

	@Override
	public ResponseEntity<?> findTransferRecordList(Integer investId) {
		List<BidInvest> list = bidTransferManualDao.findTransferRecordList(investId);
		if (CommonUtils.isNotEmpty(list)) {
			Pager pager = new Pager();
			pager.setData(list);
			return new ResponseEntity<>(Constants.SUCCESS, pager);
		}
		return new ResponseEntity<>(Constants.ERROR, "暂无记录");
	}

	@Override
	public BidTransferManual findBidTransferByNewInvestId(Integer newInvestId) {
		return bidTransferManualDao.findBidTransferByNewInvestId(newInvestId);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void saveBidTransferManual(BidTransferManual saveManual) {
		bidTransferManualDao.save(saveManual);
		BidInvest updateInvest = new BidInvest();
		updateInvest.setId(saveManual.getOldInvestId());
		updateInvest.setState(INVEST_STATE_TRANSFER);
		bidInvestDao.update(updateInvest);
	}

	@Override
	@Compensable(cancelMethod = "dealCreditorMatchRecordsCancel", transactionContextEditor = DubboTransactionContextEditor.class)
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRES_NEW, readOnly = false)
	public ResponseEntity<?> dealCreditorMatchRecords(BidInvest qdzBidInvest, BidInvest thirdBidInvest,BidTransferAuto tranferAuto, BidInfo bidInfo,
			BigDecimal userTransMoney, BigDecimal qdzRate, Date repairTime,int creditorFlag) throws Exception {
		logger.info("{},钱袋子债券匹配，处理投资记录&债券关系:记录标识 {}, 入参:钱袋子用户id:{}, 用户购买金额:{}, 第三方投资记录id:{},转让标识:{}",BaseUtil.getTccTryLogPrefix(),
				qdzBidInvest.getRegUserId(), userTransMoney,thirdBidInvest.getId(),creditorFlag);
		List<BidInvest> updateInvests = new ArrayList<>();
		ResponseEntity<?> responseEntity = null;
		//用户购买新债券
		if(creditorFlag == CREDITOR_FLAG_BUY) {
			// 生成新债权人的投资记录
			BidInvest qdzNewBidInvest = new BidInvest();
			qdzNewBidInvest.setBidInfoId(bidInfo.getId());
			qdzNewBidInvest.setCreateTime(repairTime);
			qdzNewBidInvest.setInvestAmount(userTransMoney);
			qdzNewBidInvest.setRegUserId(qdzBidInvest.getRegUserId());
			qdzNewBidInvest.setPayAmount(userTransMoney);
			qdzNewBidInvest.setInvestChannel(BID_INVEST_CHANNEL_QDZ);
			qdzNewBidInvest.setModifyTime(repairTime);
			qdzNewBidInvest.setState(INVEST_STATE_SUCCESS);
			qdzNewBidInvest.setRealName(qdzBidInvest.getRealName());
			// 更新第三方债权人的投资记录
			BidInvest newThirdBidInvest = new BidInvest();// 第三方变更后投资记录
			newThirdBidInvest.setId(thirdBidInvest.getId());
			newThirdBidInvest.setModifyTime(repairTime);
			if(thirdBidInvest.getInvestAmount().compareTo(thirdBidInvest.getTransAmount().add(userTransMoney))==0) {
				newThirdBidInvest.setState(INVEST_STATE_AUTO);	
			}
			newThirdBidInvest.setTransAmount(userTransMoney);
			updateInvests.add(newThirdBidInvest);
			// 生成债权转让关系记录
			tranferAuto.setBuyUserId(qdzNewBidInvest.getRegUserId());
			tranferAuto.setInvestUserId(thirdBidInvest.getRegUserId());
			tranferAuto.setOldInvestId(thirdBidInvest.getId());
			tranferAuto.setCreditorAmount(userTransMoney);
			tranferAuto.setTransferAmount(userTransMoney);
			tranferAuto.setPayAmount(userTransMoney);
			tranferAuto.setState(INVEST_TRANSFER_AUTO_STATE_MATCH);
			tranferAuto.setUserRate(qdzRate);
			tranferAuto.setTransferRate(BigDecimal.valueOf(100));
			// 插入购买用户投资记录、修改第三方投资记录、插入债券转让关系
			responseEntity = this.updateTransferManualRecords(qdzNewBidInvest,
					updateInvests, tranferAuto);
			if (responseEntity != null && responseEntity.getResStatus() == SUCCESS) {
				Map<String, Object> records = responseEntity.getParams();
				qdzNewBidInvest.setId((Integer) records.get("bidInvestId"));
				tranferAuto.setId((Integer) records.get("bidTransferAutoId"));
			}
		}else {
			BidInvest qdzNewBidInvest = new BidInvest(qdzBidInvest.getId());
			if (qdzBidInvest.getInvestAmount().subtract(userTransMoney).compareTo(BigDecimal.ZERO) == 0) {
				qdzNewBidInvest.setState(INVEST_STATE_AUTO);
			}
			qdzNewBidInvest.setTransAmount(userTransMoney);
			qdzNewBidInvest.setModifyTime(repairTime);
			updateInvests.add(qdzNewBidInvest);
			BidInvest thirdNewBidInvest = new BidInvest(thirdBidInvest.getId());
			thirdNewBidInvest.setModifyTime(repairTime);
			//第三方回购债券
			thirdNewBidInvest.setTransAmount(userTransMoney.multiply(new BigDecimal(-1)));
			thirdNewBidInvest.setState(INVEST_STATE_SUCCESS);
			updateInvests.add(thirdNewBidInvest);
			// 封装债权转让关系信息
			tranferAuto.setBuyUserId(thirdBidInvest.getRegUserId());
			tranferAuto.setInvestUserId(qdzBidInvest.getRegUserId());
			tranferAuto.setNewInvestId(thirdBidInvest.getId());
			tranferAuto.setOldInvestId(qdzNewBidInvest.getId());
			tranferAuto.setCreditorAmount(userTransMoney);
			tranferAuto.setTransferAmount(userTransMoney);
			tranferAuto.setPayAmount(userTransMoney);
			tranferAuto.setTransferRate(BigDecimal.valueOf(100));
			tranferAuto.setState(INVEST_TRANSFER_AUTO_STATE_MATCH);
			tranferAuto.setUserRate(bidInfo.getInterestRate());
			responseEntity = this.updateTransferManualRecords(null, updateInvests,
					tranferAuto);
			if (responseEntity.getResStatus() == Constants.SUCCESS) {
				tranferAuto.setId(Integer.parseInt(responseEntity.getParams().get("bidTransferAutoId").toString()));
			}
		}
		return responseEntity;
	}

	@Override
	@Compensable(cancelMethod = "dealSellCreditorMatchRecordsCancel", transactionContextEditor = DubboTransactionContextEditor.class)
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRES_NEW, readOnly = false)
	public ResponseEntity<?> dealSellCreditorMatchRecords(BidInvest qdzOldBidInvest, BidInvest thirdOldBidInvest,
			BidTransferAuto tranferAuto, RegUserDetail regUserDetail, BidInfo bidInfo, BigDecimal userTransMoney, BigDecimal qdzRate,
			Date repairTime,int creditorFlag) throws Exception {
		// 更新转出用户投资记录、修改第三方投资记录、插入债券转让关系
		List<BidInvest> updateInvests = new ArrayList<>();
		BidInvest qdzNewBidInvest = new BidInvest(qdzOldBidInvest.getId());
		if (qdzOldBidInvest.getInvestAmount().subtract(userTransMoney).compareTo(BigDecimal.ZERO) == 0) {
			qdzNewBidInvest.setState(INVEST_STATE_AUTO);
		}
		qdzNewBidInvest.setTransAmount(userTransMoney);
		qdzNewBidInvest.setModifyTime(repairTime);
		updateInvests.add(qdzNewBidInvest);
		BidInvest thirdNewBidInvest = new BidInvest(thirdOldBidInvest.getId());
		thirdNewBidInvest.setModifyTime(repairTime);
		//第三方回购债券
		thirdNewBidInvest.setTransAmount(userTransMoney.multiply(new BigDecimal(-1)));
		thirdNewBidInvest.setState(INVEST_STATE_SUCCESS);
		updateInvests.add(thirdNewBidInvest);
		// 封装债权转让关系信息
		tranferAuto.setBuyUserId(thirdNewBidInvest.getRegUserId());
		tranferAuto.setInvestUserId(qdzNewBidInvest.getRegUserId());
		tranferAuto.setOldInvestId(qdzNewBidInvest.getId());
		tranferAuto.setCreditorAmount(userTransMoney);
		tranferAuto.setTransferAmount(userTransMoney);
		tranferAuto.setPayAmount(userTransMoney);
		tranferAuto.setTransferRate(BigDecimal.valueOf(100));
		tranferAuto.setState(INVEST_TRANSFER_AUTO_STATE_MATCH);
		tranferAuto.setUserRate(bidInfo.getInterestRate());
		
		ResponseEntity<?> responseEntity = this.updateTransferManualRecords(null, updateInvests,
				tranferAuto);
		if (responseEntity.getResStatus() == Constants.SUCCESS) {
			tranferAuto.setId(Integer.parseInt(responseEntity.getParams().get("bidTransferAutoId").toString()));
		}
		return responseEntity;
	}

	@Override
	public Pager findInvestTransfering(BidTransferManual contidion,Pager pager) {
		return this.bidTransferManualDao.findInvestTransfering(contidion,pager);
	}

	@Override
	public void dealTransferTimeOut(Set<Integer> transferIds, List<BidInvest> updateInvests) {
		bidTransferManualDao.updateTimeOutStateByIds(transferIds);
		if (CommonUtils.isNotEmpty(updateInvests)){
			bidInvestDao.updateBatch(BidInvest.class,updateInvests,50);
		}
	}


	/**
	 * 
	 *  @Description    : 回退钱袋子债转关系
	 *  @Method_Name    : dealBuyCreditorMatchManualRecordsCancel;
	 *  @param qdzBidInvest
	 *  @param tranferAuto
	 *  @param creditorFlag
	 *  @param bidInfo
	 *  @param qdzRate
	 *  @throws Exception
	 *  @return         : void;
	 *  @Creation Date  : 2018年4月20日 下午2:41:11
	 *  @Author         : xinbangcao@hongkun.com.cn 曹新帮
	 */
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void dealCreditorMatchRecordsCancel(BidInvest qdzBidInvest, BidInvest thirdBidInvest,
			BidTransferAuto tranferAuto,BidInfo bidInfo,
			BigDecimal userTransMoney, BigDecimal qdzRate, Date repairTime,int creditorFlag) throws Exception {
        logger.info("tcc cancel dealCreditorMatchManualRecordsCancel, 回退钱袋子债转关系, 用户标识: {}, 入参: 债转关系id: {}, 转让金额: {}, 转债标识:{}", 
        		qdzBidInvest.getRegUserId(), tranferAuto.getId(),userTransMoney,creditorFlag);
		BidInvest qdzUpdate = new BidInvest();
		qdzUpdate.setId(qdzBidInvest.getId());
		BidInvest thirdUpdate = new BidInvest();
		thirdUpdate.setId(thirdBidInvest.getId());
		try {
		// 如果是购买债权，回滚时删除个人投资记录，更新第三方投资记录(转让金额减少)，删除债权转让关系。
		if (CREDITOR_FLAG_BUY == creditorFlag) {
			thirdUpdate.setTransAmount(userTransMoney.multiply(new BigDecimal(-1)));
			thirdUpdate.setState(INVEST_STATE_SUCCESS);
		} else {
			// 如果是释放债权，则更新个人投资记录(转让金额减少),更新第三方投资记录(转让金额金额增加),删除债权转让关系
			qdzUpdate.setTransAmount(userTransMoney.multiply(new BigDecimal(-1)));
			qdzUpdate.setState(INVEST_STATE_SUCCESS);
			thirdUpdate.setTransAmount(userTransMoney);
			if(thirdBidInvest.getInvestAmount().compareTo(thirdBidInvest.getTransAmount().add(userTransMoney))==0) {
				thirdUpdate.setState(INVEST_STATE_AUTO);	
			}
		}
		this.rollBackManualRecords(thirdUpdate, qdzUpdate, tranferAuto, creditorFlag);
	} catch (Exception e) {
		logger.error("tcc error dealCreditorMatchManualRecordsCancel回退钱袋子债转关系异常,第三方投资记录:{}, 钱袋子投资记录:{},债转关系:{},"
				+ " 债转标识:{}, 债转金额:{}, 标的信息:{}, 钱袋子利率:{}",thirdBidInvest.toString(),qdzBidInvest.toString(),tranferAuto.toString(),
				creditorFlag,bidInfo.toString(),qdzRate,e);
		throw new BusinessException("dealCreditorMatchManualRecordsCancel回退钱袋子债转关系异常");
	}
	}
	/**
	 * 
	 *  @Description    : 回退钱袋子债转关系
	 *  @Method_Name    : dealSellCreditorMatchRecordsCancel;
	 *  @param qdzBidInvest
	 *  @param tranferAuto
	 *  @param creditorFlag
	 *  @param bidInfo
	 *  @param qdzRate
	 *  @throws Exception
	 *  @return         : void;
	 *  @Creation Date  : 2018年4月20日 下午2:41:11
	 *  @Author         : xinbangcao@hongkun.com.cn 曹新帮
	 */
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void dealSellCreditorMatchRecordsCancel(BidInvest qdzBidInvest, BidInvest thirdBidInvest,
			BidTransferAuto tranferAuto, RegUserDetail regUserDetail, BidInfo bidInfo,
			BigDecimal userTransMoney, BigDecimal qdzRate, Date repairTime,int creditorFlag) throws Exception {
        logger.info("tcc cancel dealSellCreditorMatchRecordsCancel, 回退钱袋子债转关系, 用户标识: {}, 入参: 债转关系id: {}, 转让金额: {}, 转债标识:{}", 
        		regUserDetail.getRegUserId(), tranferAuto.getId(),userTransMoney,creditorFlag);
		BidInvest qdzUpdate = new BidInvest();
		qdzUpdate.setId(qdzBidInvest.getId());
		BidInvest thirdUpdate = new BidInvest();
		thirdUpdate.setId(thirdBidInvest.getId());
		try {
		//回滚钱袋子用户投资记录
			qdzUpdate.setTransAmount(userTransMoney.multiply(new BigDecimal(-1)));
			qdzUpdate.setState(INVEST_STATE_SUCCESS);
		//回滚第三方账户投资记录
		thirdUpdate.setTransAmount(userTransMoney);
		if(thirdBidInvest.getInvestAmount().compareTo(thirdBidInvest.getTransAmount().add(userTransMoney))==0) {
				thirdUpdate.setState(INVEST_STATE_AUTO);	
		}
		this.rollBackManualRecords(thirdUpdate, qdzUpdate, tranferAuto, creditorFlag);
	} catch (Exception e) {
		logger.error("dealSellCreditorMatchRecordsCancel 回退钱袋子债转关系异常,第三方投资记录:{}, 钱袋子投资记录:{},债转关系:{},"
				+ " 债转标识:{}, 债转金额:{}, 标的信息:{}, 钱袋子利率:{}",thirdBidInvest.toString(),qdzBidInvest.toString(),tranferAuto.toString(),
				creditorFlag,bidInfo.toString(),qdzRate,e);
		
		throw new BusinessException("dealSellCreditorMatchRecordsCancel 回退钱袋子债转关系异常");
	}
	}


}
