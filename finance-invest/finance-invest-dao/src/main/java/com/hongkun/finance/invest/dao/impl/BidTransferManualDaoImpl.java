package com.hongkun.finance.invest.dao.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import com.hongkun.finance.invest.model.BidInvest;
import com.hongkun.finance.invest.model.BidTransferManual;
import com.hongkun.finance.invest.model.vo.CreditorTransferDetailVo;
import com.hongkun.finance.invest.model.vo.CreditorTransferVo;
import com.hongkun.finance.invest.dao.BidTransferManualDao;
import com.hongkun.finance.invest.model.vo.TransferManualAppPreVo;
import com.hongkun.finance.invest.model.vo.TransferManualDetailAppVo;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.core.exception.BusinessException;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.invest.dao.impl.BidTransferManualDaoImpl.java
 * @Class Name    : BidTransferManualDaoImpl.java
 * @Description   : GENERATOR DAO实现类
 * @Author        : generator
 */
@Dao
public class BidTransferManualDaoImpl extends MyBatisBaseDaoImpl<BidTransferManual, java.lang.Long> implements BidTransferManualDao {
	
	@Override
	public Pager findMyCreditor(BidTransferManual contidion,Pager pager) {
		if (contidion == null) {
			throw new BusinessException(" condition can't null!");
		}
		return this.findByCondition(contidion, pager, BidTransferManual.class, ".findMyCreditor");
	}

	@Override
	public TransferManualAppPreVo findBidTransferDetailByInvestId(Integer investId) {
		if(investId==null||investId<=0){
			throw new BusinessException(" investId can't null!");
		}
		return super.getCurSqlSessionTemplate().selectOne(BidTransferManual.class.getName()+".findByInvestId", investId);
	}

	@Override
	public TransferManualDetailAppVo findBidTransferDetailByTransferId(
			Integer transferId) {
		if(transferId==null||transferId<=0){
			throw new BusinessException(" transferId can't null!");
		}
		return super.getCurSqlSessionTemplate().selectOne(BidTransferManual.class.getName()+".findByTransferId", transferId);
	}

	@Override
	public BigDecimal findSumTransferableMoney(Integer investId) {
		return super.getCurSqlSessionTemplate().selectOne(BidTransferManual.class.getName()+".findSumTransferableMoney", investId);
	}

	@Override
	public Pager myCreditorForTransfer(BidTransferManual tranferContidion, Pager pager) {
		if (tranferContidion == null) {
			throw new BusinessException(" condition can't null!");
		}
		return this.findByCondition(tranferContidion, pager, BidTransferManual.class, ".myCreditorForTransfer");
	}

	@Override
	public List<BidInvest> findTransferRecordList(Integer investId) {
		return super.getCurSqlSessionTemplate().selectList(BidTransferManual.class.getName()
				+".findTransferRecordList", investId);
	}

	@Override
	public BidTransferManual findBidTransferByNewInvestId(Integer newInvestId) {
		return super.getCurSqlSessionTemplate().selectOne(BidTransferManual.class.getName()
				+".findBidTransferByNewInvestId", newInvestId);
	}

	@Override
	public Pager findInvestForTransfer(Integer investUserId, Pager pager) {
		return this.findByCondition(investUserId, pager, BidTransferManual.class, ".findInvestForTransfer");
	}

	@Override
	public Pager findInvestTransfering(BidTransferManual contidion, Pager pager) {
		return this.findByCondition(contidion, pager, BidTransferManual.class, ".finInvestTransfering");
	}

	@Override
	public void updateTimeOutStateByIds(Set<Integer> transferIds) {
		super.getCurSqlSessionTemplate().update(BidTransferManual.class.getName()+".updateTimeOutStateByIds",transferIds);
	}

}
