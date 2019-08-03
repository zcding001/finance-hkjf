package com.hongkun.finance.bi.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yirun.framework.core.utils.pager.Pager;

import com.hongkun.finance.bi.model.StaRepay;

import com.alibaba.dubbo.config.annotation.Service;

import com.hongkun.finance.bi.dao.StaRepayDao;
import com.hongkun.finance.bi.service.StaRepayService;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.bi.service.impl.StaRepayServiceImpl.java
 * @Class Name    : StaRepayServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class StaRepayServiceImpl implements StaRepayService {

	private static final Logger logger = LoggerFactory.getLogger(StaRepayServiceImpl.class);
	
	/**
	 * StaRepayDAO
	 */
	@Autowired
	private StaRepayDao staRepayDao;
	
	@Override
	public List<StaRepay> findStaRepayList(StaRepay staRepay) {
		return this.staRepayDao.findByCondition(staRepay);
	}
	
	@Override
	public Pager findStaRepayList(StaRepay staRepay, Pager pager) {
		return this.staRepayDao.findByCondition(staRepay, pager);
	}

    @Override
    public StaRepay findStaRepayAddup(StaRepay staRepay) {
        return this.staRepayDao.findStaRepayAddup(staRepay);
    }

    @Override
    public StaRepay findStaRepayFuture() {
        return this.staRepayDao.findStaRepayFuture();
    }
}
