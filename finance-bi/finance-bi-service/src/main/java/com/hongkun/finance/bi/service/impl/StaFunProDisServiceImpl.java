package com.hongkun.finance.bi.service.impl;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.hongkun.finance.bi.service.StaFunProDisService;
import com.yirun.framework.core.enums.PeriodEnum;
import com.yirun.framework.core.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yirun.framework.core.utils.pager.Pager;

import com.hongkun.finance.bi.model.StaFunProDis;

import com.alibaba.dubbo.config.annotation.Service;

import com.hongkun.finance.bi.dao.StaFunProDisDao;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.bi.service.impl.StaFunProDisServiceImpl.java
 * @Class Name    : StaFunProDisServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class StaFunProDisServiceImpl implements StaFunProDisService {

	private static final Logger logger = LoggerFactory.getLogger(StaFunProDisServiceImpl.class);
	
	/**
	 * StaFunProDisDAO
	 */
	@Autowired
	private StaFunProDisDao staFunProDisDao;
	
	@Override
	public List<StaFunProDis> findStaFunProDisList(StaFunProDis staFunProDis) {
		return this.staFunProDisDao.findByCondition(staFunProDis);
	}
	
	@Override
	public Pager findStaFunProDisList(StaFunProDis staFunProDis, Pager pager) {
		return this.staFunProDisDao.findByCondition(staFunProDis, pager);
	}

    @Override
    public List<Map<String, Object>> findStaFunProDisListByBidProType(Integer period, String startTime, String endTime) {
	    Map<String, Date> dateMap = DateUtils.getPeriod(PeriodEnum.getPeriodEnum(period), startTime, endTime); 
	    StaFunProDis staFunProDis = new StaFunProDis();
	    staFunProDis.setDaBegin(dateMap.get(DateUtils.START));
	    staFunProDis.setDaEnd(dateMap.get(DateUtils.END));
        List<StaFunProDis> list = this.staFunProDisDao.findStaFunProDisListByBidProType(staFunProDis);
	    List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> investMap = new HashMap<>(8);
        Map<String, Object> repayMap = new HashMap<>(8);
        investMap.put("type", "invest");
        //新手标
        investMap.put("proType1", list.stream().filter(o -> o.getBidProType().equals(1)).map(StaFunProDis::getInvestAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
        //月月盈
        investMap.put("proType2", list.stream().filter(o -> o.getBidProType().equals(2)).map(StaFunProDis::getInvestAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
        //季季盈
        investMap.put("proType3", list.stream().filter(o -> o.getBidProType().equals(3)).map(StaFunProDis::getInvestAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
        //年年盈
        investMap.put("proType4", list.stream().filter(o -> o.getBidProType().equals(4)).map(StaFunProDis::getInvestAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
        //钱袋子(活期产品)
        investMap.put("proType7", list.stream().filter(o -> o.getBidProType().equals(7)).map(StaFunProDis::getInvestAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
        //其他(体验金&购房宝&物业宝&活动产品&直投散标)
        investMap.put("proTypeOther", list.stream().filter(o -> o.getBidProType().equals(5) || o.getBidProType()
                .equals(6) || o.getBidProType().equals(8) || o.getBidProType().equals(9) || o.getBidProType()
                .equals(10)).map(StaFunProDis::getInvestAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
        result.add(investMap);
        repayMap.put("type", "repay");
        //新手标
        repayMap.put("proType1", list.stream().filter(o -> o.getBidProType().equals(1)).map(StaFunProDis::getRepayAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
        //月月盈
        repayMap.put("proType2", list.stream().filter(o -> o.getBidProType().equals(2)).map(StaFunProDis::getRepayAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
        //季季盈
        repayMap.put("proType3", list.stream().filter(o -> o.getBidProType().equals(3)).map(StaFunProDis::getRepayAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
        //年年盈
        repayMap.put("proType4", list.stream().filter(o -> o.getBidProType().equals(4)).map(StaFunProDis::getRepayAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
        //钱袋子(活期产品)
        repayMap.put("proType7", list.stream().filter(o -> o.getBidProType().equals(7)).map(StaFunProDis::getRepayAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
        //其他(体验金&购房宝&物业宝&活动产品&直投散标)
        repayMap.put("proTypeOther", list.stream().filter(o -> o.getBidProType().equals(5) || o.getBidProType()
                .equals(6) || o.getBidProType().equals(8) || o.getBidProType().equals(9) || o.getBidProType()
                .equals(10)).map(StaFunProDis::getRepayAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
        result.add(repayMap);
        return result;
    }
    
    
}
