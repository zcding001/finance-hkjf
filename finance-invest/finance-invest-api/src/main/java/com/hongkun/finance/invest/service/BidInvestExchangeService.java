package com.hongkun.finance.invest.service;

import java.math.BigDecimal;
import java.util.List;

import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;
import com.hongkun.finance.invest.model.BidInvestExchange;

/**
 * @Project       : finance_hkjf
 * @Program Name  : com.hongkun.finance.invest.service.BidInvestExchangeService.java
 * @Class Name    : BidInvestExchangeService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface BidInvestExchangeService {

	Pager findExchangeBidList(String bidName, Pager pager);
	/**
	*  @Description    ：查询预匹配投资记录
	*  @Method_Name    ：findInvestWating
	*  @return java.util.List<com.hongkun.finance.invest.model.BidInvestExchange>
	*  @Creation Date  ：2019/1/16
	*  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
	*/
	List<BidInvestExchange> findInvestWating();
	/**
	*  @Description    ：查询某个标的已匹配的投资记录
	*  @Method_Name    ：findExchangeInvestListByBidId
	*  @param bidId
	*  @return java.util.List<com.hongkun.finance.invest.model.BidInvestExchange>
	*  @Creation Date  ：2019/1/17
	*  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
	*/
	List<BidInvestExchange> findExchangeInvestListByBidId(Integer bidId);
	/**
	*  @Description    ：查询预匹配投资记录（小于250000）
	*  @Method_Name    ：findInvestSubWating
	*  @param subWatingMoney
	*  @return java.util.List<com.hongkun.finance.invest.model.BidInvestExchange>
	*  @Creation Date  ：2019/1/17
	*  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
	*/
	List<BidInvestExchange> findInvestSubWating(BigDecimal subWatingMoney);

    void addBatch(List<BidInvestExchange> investLists);

	Pager investExchangeListForPager(Integer bidId, Pager pager);
}
