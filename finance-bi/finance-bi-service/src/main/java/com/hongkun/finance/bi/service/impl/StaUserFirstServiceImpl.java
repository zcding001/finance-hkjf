package com.hongkun.finance.bi.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.enums.PeriodEnum;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yirun.framework.core.utils.pager.Pager;

import com.hongkun.finance.bi.model.StaUserFirst;

import com.alibaba.dubbo.config.annotation.Service;

import com.hongkun.finance.bi.dao.StaUserFirstDao;
import com.hongkun.finance.bi.service.StaUserFirstService;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.bi.service.impl.StaUserFirstServiceImpl.java
 * @Class Name    : StaUserFirstServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class StaUserFirstServiceImpl implements StaUserFirstService {

	private static final Logger logger = LoggerFactory.getLogger(StaUserFirstServiceImpl.class);
	
	/**
	 * StaUserFirstDAO
	 */
	@Autowired
	private StaUserFirstDao staUserFirstDao;
	
	@Override
	public List<StaUserFirst> findStaUserFirstList(StaUserFirst staUserFirst) {
		return this.staUserFirstDao.findByCondition(staUserFirst);
	}
	
	@Override
	public List<StaUserFirst> findStaUserFirstList(StaUserFirst staUserFirst, int start, int limit) {
		return this.staUserFirstDao.findByCondition(staUserFirst, start, limit);
	}
	
	@Override
	public Pager findStaUserFirstList(StaUserFirst staUserFirst, Pager pager) {
		return this.staUserFirstDao.findByCondition(staUserFirst, pager);
	}
	
	@Override
	public Pager findStaUserFirstList(StaUserFirst staUserFirst, Pager pager, String sqlName){
		return this.staUserFirstDao.findByCondition(staUserFirst, pager, sqlName);
	}
	
	@Override
	public Integer findStaUserFirstCount(StaUserFirst staUserFirst, String sqlName){
		return this.staUserFirstDao.getTotalCount(staUserFirst, sqlName);
	}

    @Override
    public ResponseEntity<?> findUserCvr(Integer period, String startTime, String endTime) {
        Map<String, Date> map = DateUtils.getPeriod(PeriodEnum.getPeriodEnum(period), startTime, endTime);
        StaUserFirst staUserFirst = new StaUserFirst();
        staUserFirst.setDaBegin(map.get(DateUtils.START));
        staUserFirst.setDaEnd(map.get(DateUtils.END));
        List<StaUserFirst> list = this.staUserFirstDao.findStaUserFirstByType(staUserFirst);
        if(list == null){
            list = new ArrayList<>();
        }
        StaUserFirst total = new StaUserFirst();
        total.setRegSource(0);
        total.setUserCount(list.stream().mapToInt(StaUserFirst::getUserCount).sum());
        total.setRealNameCount(list.stream().mapToInt(StaUserFirst::getRealNameCount).sum());
        total.setRechangeCount(list.stream().mapToInt(StaUserFirst::getRechangeCount).sum());
        total.setInvestCount(list.stream().mapToInt(StaUserFirst::getInvestCount).sum());
        total.setInvestAmountSum(BigDecimal.valueOf(list.stream().mapToDouble(o -> o.getInvestAmountSum().doubleValue()).sum()));
        list.add(0, total);
        return new ResponseEntity<>(Constants.SUCCESS, list);
    }

    @Override
    public StaUserFirst findStaUserFirstCount(StaUserFirst staUserFirst) {
        return this.staUserFirstDao.findStaUserFirstCount(staUserFirst);
    }
}
