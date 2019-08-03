package com.hongkun.finance.invest.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.invest.constants.InvestConstants;
import com.hongkun.finance.invest.dao.BidInfoDao;
import com.hongkun.finance.invest.dao.BidInfoDetailDao;
import com.hongkun.finance.invest.dao.BidInvestDao;
import com.hongkun.finance.invest.dao.BidProductDao;
import com.hongkun.finance.invest.model.BidInfo;
import com.hongkun.finance.invest.model.BidInfoDetail;
import com.hongkun.finance.invest.model.BidInvest;
import com.hongkun.finance.invest.model.BidProduct;
import com.hongkun.finance.invest.model.vo.*;
import com.hongkun.finance.invest.service.BidInvestService;
import com.hongkun.finance.invest.util.CalcInterestUtil;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.enums.PlatformSourceEnums;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.CommonUtils;
import com.yirun.framework.core.utils.CompareUtil;
import com.yirun.framework.core.utils.pager.Pager;
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

import static com.hongkun.finance.invest.constants.InvestConstants.*;
import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * @Project : finance
 * @Program Name :
 *          com.hongkun.finance.invest.service.impl.BidInvestServiceImpl.java
 * @Class Name : BidInvestServiceImpl.java
 * @Description : GENERATOR SERVICE实现类
 * @Author : generator
 */
@Service
public class BidInvestServiceImpl implements BidInvestService {

	private static final Logger logger = LoggerFactory.getLogger(BidInvestServiceImpl.class);

	/**
	 * BidInvestDAO
	 */
	@Autowired
	private BidInvestDao bidInvestDao;
	@Autowired
	private BidInfoDao bidInfoDao;
	@Autowired
	private BidProductDao bidProductDao;
	@Autowired
	private BidInfoDetailDao bidInfoDetailDao;

	@Override
	@Compensable(cancelMethod = "insertBidInvestForCancel", transactionContextEditor = Compensable.DefaultTransactionContextEditor.class)
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public ResponseEntity<?> insertBidInvest(int regUserId, String realName, BigDecimal money, BidInfo bidInfo,
			int investRedPacketId, int investRaiseInterestId, BigDecimal jworth, Date currTime, Integer investType,
			PlatformSourceEnums platformSourceEnums) {
		logger.info("{}. 插入投资记录, 用户: {}, 投资金额: {}, 标的: {}, 投资类型: {}, 投资平台: {}, 投资红包: {}, 加息券: {}",
				BaseUtil.getTccTryLogPrefix(), regUserId, money, bidInfo.getId(), investType,
				platformSourceEnums.getType(), investRedPacketId, investRaiseInterestId);
		ResponseEntity<?> result = new ResponseEntity<>(SUCCESS);
		try {
			// 更新标的状态
			BidInfo bidInfoTmp = new BidInfo();
			bidInfoTmp.setId(bidInfo.getId());
			bidInfoTmp.setResidueAmount(money.negate());
			bidInfoTmp.setModifyTime(currTime);
			bidInfoTmp.setPreResidueAmount(bidInfo.getResidueAmount());
			if (CompareUtil.eq(bidInfo.getResidueAmount(), money)) {
				bidInfoTmp.setState(BID_STATE_WAIT_AUDIT);
			}
			if (this.bidInfoDao.update(bidInfoTmp) > 0) {
				// 插入投资记录
				result.getParams().put("investRaiseInterestAtm", BigDecimal.ZERO);
				BidInvest bidInvest = new BidInvest();
				bidInvest.setBidInfoId(bidInfo.getId());
				bidInvest.setRegUserId(regUserId);
				bidInvest.setRealName(realName);
				bidInvest.setInvestAmount(money);
				bidInvest.setCreateTime(currTime);
				bidInvest.setInvestType(investType);
				bidInvest.setPayAmount(money);
				bidInvest.setActionScope(bidInfo.getActionScope());
				if (investRedPacketId > 0) {
					bidInvest.setCouponIdK(investRedPacketId);
				}
				if (investRaiseInterestId > 0) {
					bidInvest.setCouponIdJ(investRaiseInterestId);
					// 加息券收益金额
					result.getParams().put("investRaiseInterestAtm", CalcInterestUtil
							.calcInterest(bidInfo.getTermUnit(), bidInfo.getTermValue(), money, jworth));
				}
				bidInvest.setInvestSource(platformSourceEnums.getValue());
				this.bidInvestDao.save(bidInvest);
				result.getParams().put("bidInvest", bidInvest);
				return result;
			} else {
				return ResponseEntity.ERROR;
			}
		} catch (Exception e) {
			logger.error(
					"tcc try insertBidInvest error. 插入投资记录, 用户: {}, 投资金额: {}, 标的: {}, 投资类型: {}, 投资平台: {}, 投资红包: {}, 加息券: {}\n",
					regUserId, money, bidInfo.getId(), investType, platformSourceEnums.getType(), investRedPacketId,
					investRaiseInterestId, e);
			throw new GeneralException("更新标的失败");
		}
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public ResponseEntity<?> updateBidInvestForRollback(BidInvest bidInvest, BidInfo bidInfo) {
		try {
			logger.info("updateBidInvestForRollback, 还原标的金额及状态、删除投资记录, bidInvest: {}, bidInfo: {}",
					bidInvest.toString(), bidInfo.toString());
			// 还原标的金额及状态
			BidInfo bidInfoTmp = new BidInfo();
			bidInfoTmp.setId(bidInfo.getId());
			bidInfoTmp.setResidueAmount(bidInvest.getTransAmount());
			bidInfoTmp.setState(BID_STATE_WAIT_INVEST);
			this.bidInfoDao.update(bidInfoTmp);
			// 删除投资记录
			this.bidInvestDao.delete(Long.valueOf(bidInvest.getId()), BidInvest.class);
		} catch (Exception e) {
			logger.error("updateBidInvestForRollback, 还原标的金额及状态、删除投资记录异常, bidInvest: {}, bidInfo: {}",
					bidInvest.toString(), bidInfo.toString());
			throw new GeneralException("还原标的金额及状态、删除投资记录异常！");
		}
		return new ResponseEntity<>(SUCCESS);
	}

	@Override
	public Pager findBidInvestDetailList(BidInvestDetailVO bidInvestDetailVO, Pager pager) {
		return this.bidInfoDao.findByCondition(bidInvestDetailVO, pager, BidInvest.class, ".findBidInvestDetailList");
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertBidInvest(BidInvest bidInvest) {
		this.bidInvestDao.save(bidInvest);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertBidInvestBatch(List<BidInvest> list) {
		this.bidInvestDao.insertBatch(BidInvest.class, list);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertBidInvestBatch(List<BidInvest> list, int count) {
		if (logger.isDebugEnabled()) {
			logger.debug("default batch insert size is " + count);
		}
		this.bidInvestDao.insertBatch(BidInvest.class, list, count);
	}

	@Override
	@Compensable(cancelMethod = "updateBidInvestForCancel", transactionContextEditor = DubboTransactionContextEditor.class)
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateBidInvest(BidInvest bidInvest) {
		
		this.bidInvestDao.update(bidInvest);
	}

	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateBidInvestForCancel(BidInvest bidInvest) {
		try {
			logger.info("updateBidInvestForCancel, 还原投资记录, bidInvest: {}", bidInvest.toString());
			// 设置为已经使用
			// 投资记录设置为投资成功
			bidInvest.setState(INVEST_STATE_SUCCESS);
			this.bidInvestDao.update(bidInvest);
		} catch (Exception e) {
			logger.error("tcc cancel updateBidInvest error. , 回滚投资记录异常, 投资记录信息: {}, 异常信息: {}", bidInvest, e);
			throw new GeneralException("更新投资记录异常！");
		}

	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateBidInvestBatch(List<BidInvest> list, int count) {
		this.bidInvestDao.updateBatch(BidInvest.class, list, count);
	}

	@Override
	public BidInvest findBidInvestById(int id) {
		return this.bidInvestDao.findByPK(Long.valueOf(id), BidInvest.class);
	}

	@Override
	public List<BidInvest> findBidInvestList(BidInvest bidInvest) {
		return this.bidInvestDao.findByCondition(bidInvest);
	}

	@Override
	public List<BidInvest> findBidInvestList(BidInvest bidInvest, int start, int limit) {
		return this.bidInvestDao.findByCondition(bidInvest, start, limit);
	}

	@Override
	public Pager findBidInvestList(BidInvest bidInvest, Pager pager) {
		return this.bidInvestDao.findByCondition(bidInvest, pager);
	}

	@Override
	public int findBidInvestCount(BidInvest bidInvest) {
		return this.bidInvestDao.getTotalCount(bidInvest);
	}

	@Override
	public Map<String, Object> findSumAmountByBidId(int bidInfoId) {
		return this.bidInvestDao.findSumAmountByBidId(bidInfoId);
	}

	@Override
	public List<BidInvest> findBidInvestListByBidId(Integer bidInfoId) {
		BidInvest contidion = new BidInvest();
		contidion.setBidInfoId(bidInfoId);
		contidion.setState(1);
		contidion.setInvestChannel(InvestConstants.BID_INVEST_CHANNEL_IMMEDIATE);
		return this.bidInvestDao.findByCondition(contidion);
	}

	@Override
	public BigDecimal findInvestSumAmount(Integer productType, Integer bidState, Integer regUserId) {
		if (productType == null || bidState == null || regUserId == null) {
			return BigDecimal.ZERO;
		}
		return bidInvestDao.findInvestSumAmount(productType, bidState, regUserId);

	}

	@Override
	public List<BidInvest> findInvests(Integer productType, Integer bidState, Integer regUserId) {
		return this.bidInvestDao.findInvests(productType, bidState, regUserId);
	}

	@Override
	public Pager findMatchBidInvestList(Integer goodBidId, Integer commonBidId, Pager pager) {
		BidInvestDetailVO bidInvestDetailVO = new BidInvestDetailVO();
		bidInvestDetailVO.setBidInfoId(commonBidId);
		bidInvestDetailVO.setGoodBidId(goodBidId);
		return this.bidInfoDao.findByCondition(bidInvestDetailVO, pager, BidInvest.class, ".findMatchBidInvestList");
	}

	@Override
	public int findBidInvestCount(Integer bidId, Integer investUserId, Date createTime, Date endTime,
			Integer recommendState) {
		return this.bidInvestDao.findBidInvestCount(bidId, investUserId, createTime, endTime, recommendState);
	}

	@Override
	public BidInvest findInvestRecord(Integer bidId, List<Integer> state) {
		return this.bidInvestDao.findInvestRecord(bidId, state);
	}

	@Override
	public BigDecimal findSumInvestAmountByUserId(Integer regUserId) {
		return this.bidInvestDao.findSumInvestAmountByUserId(regUserId);
	}

	@Override
	public BigDecimal findSumNiggerAmountByUserId(Integer regUserId) {
		BigDecimal result = new BigDecimal(0);
		BidInvest contidion = new BidInvest();
		contidion.setState(1);
		contidion.setInvestChannel(InvestConstants.BID_INVEST_CHANNEL_IMMEDIATE);
		contidion.setRegUserId(regUserId);
		List<BidInvest> list = this.bidInvestDao.findByCondition(contidion);
		if (CommonUtils.isNotEmpty(list)) {
			for (BidInvest bidInvest : list) {
				// 查询标的信息
				BidInfo bidInfo = bidInfoDao.findByPK(Long.valueOf(bidInvest.getBidInfoId()), BidInfo.class);
				if (bidInfo != null) {
					BigDecimal niggerRate = CalcInterestUtil.calNiggerRate(bidInfo.getTermUnit(),
							bidInfo.getTermValue());
					BigDecimal investAtm = bidInvest.getInvestAmount().multiply(niggerRate);
					result = result.add(investAtm);
				}
			}
		}
		return result;
	}

	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public ResponseEntity<?> insertBidInvestForCancel(int regUserId, String realName, BigDecimal money, BidInfo bidInfo,
			int investRedPacketId, int investRaiseInterestId, BigDecimal jworth, Date currTime, Integer investType,
			PlatformSourceEnums platformSourceEnums) {
		logger.info("tcc cancel insertBidInvest. 用户: {}, 投资金额: {}, 标的: {}, 投资类型: {}, 投资平台: {}, 投资红包: {}, 加息券: {}",
				regUserId, money, bidInfo.getId(), investType, platformSourceEnums.getType(), investRedPacketId,
				investRaiseInterestId);
		ResponseEntity<?> result = new ResponseEntity<>(SUCCESS);
		BidInvest bidInvest = new BidInvest();
		bidInvest.setBidInfoId(bidInfo.getId());
		bidInvest.setRegUserId(regUserId);
		bidInvest.setRealName(realName);
		bidInvest.setInvestAmount(money);
		bidInvest.setCreateTime(currTime);
		bidInvest.setInvestType(investType);
		try {
			List<BidInvest> list = this.bidInvestDao.findByCondition(bidInvest);
			if (CommonUtils.isNotEmpty(list)) {
				list.forEach(obj -> this.bidInvestDao.delete(Long.valueOf(obj.getId()), BidInvest.class));
				// 找到投资记录，说明标的已经更新，因为插入投资记录和更新标的在同一个事务里，所以回复标的状态
				BidInfo bidInfoCdt = new BidInfo();
				bidInfoCdt.setId(bidInfo.getId());
				bidInfoCdt.setModifyTime(currTime);
				bidInfoCdt.setResidueAmount(money);
				bidInfoCdt.setState(BID_STATE_WAIT_INVEST);
				this.bidInfoDao.update(bidInfoCdt);
			}
			return result;
		} catch (Exception e) {
			logger.error(
					"tcc cancel insertBidInvest error. 用户: {}, 投资金额: {}, 标的: {}, 投资类型: {}, 投资平台: {}, 投资红包: {}, 加息券: {}\n",
					regUserId, money, bidInfo.getId(), investType, platformSourceEnums.getType(), investRedPacketId,
					investRaiseInterestId, e);
			throw new GeneralException("更新标的信息回复失败");
		}
	}

	@Override
	public Map<Integer, BidInvestForRecommendVo> findSumInvestAmount(List<Integer> friendIds) {
		Map<Integer, BidInvestForRecommendVo> result = new HashMap<Integer, BidInvestForRecommendVo>();
		List<BidInvestForRecommendVo> list = bidInvestDao.findBidInvestByRegUserIds(friendIds);
		if (CommonUtils.isNotEmpty(list)) {
			list.forEach(vo -> {
				BigDecimal investBackStepMoney = vo.getInvestAmount()
						.multiply(CalcInterestUtil.calNiggerRate(vo.getTermUnit(), vo.getTermValue()));
				vo.setInvestBackStepMoney(investBackStepMoney);
				if (result.containsKey(vo.getRegUserId())) {
					BidInvestForRecommendVo rvo = result.get(vo.getRegUserId());
					vo.setInvestAmount(vo.getInvestAmount().add(rvo.getInvestAmount()));
					vo.setInvestBackStepMoney(investBackStepMoney.add(rvo.getInvestBackStepMoney()));
				}
				result.put(vo.getRegUserId(), vo);
			});
		}
		return result;
	}

	@Override
	public List<BidInvestForRecommendVo> findBidInvestByRegUserIds(List<Integer> ids) {
		return bidInvestDao.findBidInvestByRegUserIds(ids);
	}

	@Override
	public Integer findBidInvestCountForPrefered(Integer regUserId) {
		return this.bidInvestDao.findBidInvestCountForPrefered(regUserId);
	}

	@Override
	public ResponseEntity<?> findInvestAndProduct(BidInfo bidInfo) {
		Map<String, Object> params = new HashMap<String, Object>();
		BidProduct bidProduct = this.bidProductDao.findByPK(Long.valueOf(bidInfo.getBidProductId()), BidProduct.class);
		BigDecimal sumAmount = (BigDecimal)(bidInvestDao.findSumAmountByBidId(bidInfo.getId()).get("investAmountChannel1"));
		logger.info("findInvestAndProduct, 放款, 标的信息: {}, 查询投资总金额 : {}", bidInfo.getId(), sumAmount);
		if (!CompareUtil.eq(sumAmount, bidInfo.getTotalAmount())) {
			return new ResponseEntity<String>(Constants.ERROR, "标的金额和投资总金额不相等");
		}
		List<BidInvest> bidInvests = findBidInvestListByBidId(bidInfo.getId());
		BidInfoDetail bidDetail = bidInfoDetailDao.findByBiddInfoId(bidInfo.getId());
		params.put(MAKELOAN_BIDPRODUCT, bidProduct);
		params.put(MAKELOAN_BIDINVESTS, bidInvests);
		params.put(MAKELOAN_BIDDETAIL, bidDetail);
		return new ResponseEntity<>(SUCCESS, "放款查询基础数据成功", params);
	}

	@Override
	public List<BidInvestVoForApp> findBidInvestListByIds(List<Integer> investIds) {
		return bidInvestDao.findBidInvestListByIds(investIds);
	}

	@Override
	public Pager friendInvestListForSales(Integer friendUserId,Integer state, Pager pager) {
		BidInvestVoForSales bidVo = new BidInvestVoForSales();
		bidVo.setRegUserId(friendUserId);
		if (state != null && state >0){
			bidVo.setBidState(state);
		}else{
			bidVo.setBidState(0);
		}
		return this.bidInvestDao.findByCondition(bidVo, pager, BidInvest.class, ".friendInvestListForSales");
	}

	@Override
	public List<Integer> findBidInvestPreferedList() {
		return this.bidInvestDao.findBidInvestPreferedList();
	}

	@Override
	public List<BidInvest> findToBeMatchedBidInvestList(Integer goodBidId) {
		return this.bidInvestDao.findToBeMatchedBidInvestList(goodBidId);
	}

	@Override
	public List<BidInvest> findBidInvestListByIdList(List<Integer> investIds) {
		return this.bidInvestDao.findBidInvestListByIdList(investIds);
	}

	@Override
	public Map<String, Object> findBidInvestAndBidInfoByIdList(List<Integer> investIdList) {
		Map<String, Object> result = new HashMap<>(2);
		// 需要获取标地信息的标地id集合
		Set<Integer> bidIdSet = new HashSet<>();
		// 投资人&借款人&本金接收人id集合
		Set<Integer> userIdSet = new HashSet<>();
		// 收款人id集合
		Set<Integer> payeeIdSet = new HashSet<>();
		List<BidInvest> bidInvestList = this.bidInvestDao.findBidInvestListByIdList(investIdList);
		bidInvestList.forEach((invest) -> {
			bidIdSet.add(invest.getBidInfoId());
			userIdSet.add(invest.getRegUserId());
		});
		// 根据标地id集合获取标地信息，key为标地id，value为标地信息BidInfoVO
		Map<Integer, BidInfoVO> bidInfoVOMap = this.bidInfoDao.findBidInfoDetailVoByIdList(bidIdSet);
		// 组装收款人id集合
		bidInfoVOMap.values().forEach((vo) -> {
			payeeIdSet.add(vo.getBorrowerId());
			payeeIdSet.add(vo.getReceiptUserId());
		});
		// userIdSet包括收款人id集合
		userIdSet.addAll(payeeIdSet);

		result.put("bidInvestList", bidInvestList);
		result.put("bidInfoVOMap", bidInfoVOMap);
		result.put("userIdSet", userIdSet);
		result.put("payeeIdSet", payeeIdSet);
		return result;
	}

	@Override
	public List<BidInvest> findGoodBidInvestListByIdList(Set<Integer> investIds) {
		return this.bidInvestDao.findGoodBidInvestListByIdList(investIds);
	}

	@Override
	public List<Integer> findUserThreeMonthInvest(Set<Integer> userIdList) {
		return this.bidInvestDao.findUserThreeMonthInvest(userIdList);
	}

	@Override
	public List<BidInvest> findMatchBidInvestListByBidId(Integer bidId) {
		BidInvest contidion = new BidInvest();
		contidion.setBidInfoId(bidId);
		contidion.setState(1);
		contidion.setInvestChannel(InvestConstants.BID_INVEST_CHANNEL_MATCH);
		return this.bidInvestDao.findByCondition(contidion);
	}

	@Override
	public int findBidInvestCount(Integer investUserId, Integer bidType) {
		return this.bidInvestDao.findBidInvestCount(investUserId, bidType);
	}

	@Override
	public List<BidInvest> findGoodInvestMatchCommonInvestList(BidInvest condition) {
		return this.bidInvestDao.findGoodInvestMatchCommonInvestList(condition);
	}

	@Override
	public List<Integer> findBidInfoIdByCondition(BidInvest bidInvest) {
		return this.bidInvestDao.findBidInfoIdByCondition(bidInvest);
	}

    @Override
    public Map<String, Object> findStaFunInvestAddup(StaFunInvestVO staFunInvestVO) {
        return this.bidInvestDao.findStaFunInvestAddup(staFunInvestVO);
    }

    @Override
    public Pager findStaFunInvestList(Pager pager, StaFunInvestVO staFunInvestVO) {
        return this.bidInvestDao.findByCondition(staFunInvestVO, pager, BidInvest.class, ".findStaFunInvest");
    }

    @Override
    public List<BidInvestDetailVO> findBidInvestDetailList(BidInvestDetailVO bidInvestDetailVO) {
        return this.bidInvestDao.findBidInvestDetailList(bidInvestDetailVO);
    }
	@Override
	public Integer getSelfAndInvitorInvestCount(List<Integer> userIdList) {
		return this.bidInvestDao.getSelfAndInvitorInvestCount(userIdList);
	}

	@Override
	public Integer getSelfAndInvitorTransferCount(List<Integer> userIdList) {
		return this.bidInvestDao.getSelfAndInvitorTransferCount(userIdList);
	}

	@Override
	public List<BidInvest> findBidInvestListByCondition(BidInvest condition) {
		return this.bidInvestDao.findBidInvestListByCondition(condition);
	}
}
