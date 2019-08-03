package com.hongkun.finance.bi.service.impl;

import java.util.List;

import com.yirun.framework.core.utils.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yirun.framework.core.utils.pager.Pager;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.bi.dao.BalAccountDao;
import com.hongkun.finance.bi.dao.BalAccountRecordDao;
import com.hongkun.finance.bi.model.BalAccount;
import com.hongkun.finance.bi.model.BalAccountRecord;
import com.hongkun.finance.bi.service.BalAccountService;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.bi.service.impl.BalAccountServiceImpl.java
 * @Class Name    : BalAccountServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class BalAccountServiceImpl implements BalAccountService {

	private static final Logger logger = LoggerFactory.getLogger(BalAccountServiceImpl.class);
	
	/**
	 * BalAccountDAO
	 */
	@Autowired
	private BalAccountDao balAccountDao;
	@Autowired
	private BalAccountRecordDao balAccountRecordDao;
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertBalAccount(BalAccount balAccount) {
		this.balAccountDao.save(balAccount);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertBalAccountBatch(List<BalAccount> list) {
		this.balAccountDao.insertBatch(BalAccount.class, list);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertBalAccountBatch(List<BalAccount> list, int count) {
		if(logger.isDebugEnabled()){
			logger.debug("default batch insert size is " + count);
		}
		this.balAccountDao.insertBatch(BalAccount.class, list, count);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateBalAccount(BalAccount balAccount) {
		this.balAccountDao.update(balAccount);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateBalAccountBatch(List<BalAccount> list, int count) {
		this.balAccountDao.updateBatch(BalAccount.class, list, count);
	}
	
	@Override
	public BalAccount findBalAccountById(int id) {
		return this.balAccountDao.findByPK(Long.valueOf(id), BalAccount.class);
	}
	
	@Override
	public List<BalAccount> findBalAccountList(BalAccount balAccount) {
		return this.balAccountDao.findByCondition(balAccount);
	}
	
	@Override
	public List<BalAccount> findBalAccountList(BalAccount balAccount, int start, int limit) {
		return this.balAccountDao.findByCondition(balAccount, start, limit);
	}
	
	@Override
	public Pager findBalAccountList(BalAccount balAccount, Pager pager) {
		return this.balAccountDao.findByCondition(balAccount, pager);
	}
	
	@Override
	public int findBalAccountCount(BalAccount balAccount){
		return this.balAccountDao.getTotalCount(balAccount);
	}
	
	@Override
	public Pager findBalAccountList(BalAccount balAccount, Pager pager, String sqlName){
		return this.balAccountDao.findByCondition(balAccount, pager, sqlName);
	}
	
	@Override
	public Integer findBalAccountCount(BalAccount balAccount, String sqlName){
		return this.balAccountDao.getTotalCount(balAccount, sqlName);
	}

    @Override
    public BalAccount findBalAccountByRegUserId(Integer regUserId) {
        return this.balAccountDao.findBalAccountByRegUserId(regUserId);
    }

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void dealBatchForBalance(List<BalAccount> updateList, List<BalAccount> addList, List<BalAccountRecord> recordList) {
		if (CommonUtils.isNotEmpty(addList)){
			balAccountDao.insertBatch(BalAccount.class,addList,50);
		}
		if (CommonUtils.isNotEmpty(updateList)){
			balAccountDao.updateBatch(BalAccount.class,updateList,50);
		}
		if (CommonUtils.isNotEmpty(recordList)){
			balAccountRecordDao.insertBatch(BalAccountRecord.class,recordList,50);
		}
	}

	@Override
	public void insertBalAccountForBalance(BalAccount addAccount, BalAccount updateAccount, BalAccountRecord record) {
		if(addAccount!=null){
			balAccountDao.save(addAccount);
		}
		if (updateAccount!=null){
			balAccountDao.update(updateAccount);
		}
		if(record!=null){
			balAccountRecordDao.save(record);
		}
	}
}
