package com.hongkun.finance.bi.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yirun.framework.core.utils.pager.Pager;

import com.hongkun.finance.bi.model.StaReceipt;

import com.alibaba.dubbo.config.annotation.Service;

import com.hongkun.finance.bi.dao.StaReceiptDao;
import com.hongkun.finance.bi.service.StaReceiptService;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.bi.service.impl.StaReceiptServiceImpl.java
 * @Class Name    : StaReceiptServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class StaReceiptServiceImpl implements StaReceiptService {

	private static final Logger logger = LoggerFactory.getLogger(StaReceiptServiceImpl.class);
	
	/**
	 * StaReceiptDAO
	 */
	@Autowired
	private StaReceiptDao staReceiptDao;
	
	@Override
	public List<StaReceipt> findStaReceiptList(StaReceipt staReceipt) {
		return this.staReceiptDao.findByCondition(staReceipt);
	}
	
	
	
	@Override
	public Pager findStaReceiptList(StaReceipt staReceipt, Pager pager) {
		return this.staReceiptDao.findByCondition(staReceipt, pager);
	}

    @Override
    public StaReceipt findStaReceiptAddup(StaReceipt staReceipt) {
        return this.staReceiptDao.findStaReceiptAddup(staReceipt);
    }

    @Override
    public StaReceipt findStaReceiptFuture() {
        return this.staReceiptDao.findStaReceiptFuture();
    }
}
