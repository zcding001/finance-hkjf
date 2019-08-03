package com.hongkun.finance.invest.facade;

import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;

/**
 * @Description : 交易所匹配服务
 * @Project : finance-hkjf
 * @Program Name  : com.hongkun.finance.invest.facade
 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
 */
public interface BidInvestExchangeFacade {

    /**
    *  @Description    ：交易所标的预匹配投资记录
    *  @Method_Name    ：initBiddInvest
    *  @param bidId
    *  @return com.yirun.framework.core.model.ResponseEntity<?>
    *  @Creation Date  ：2019/1/16
    *  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
    */
    ResponseEntity<?> initBiddInvest(Integer bidId);
    /**
    *  @Description    ：为标的匹配投资记录
    *  @Method_Name    ：matchInvestExchange
    *  @param bidId
    *  @return com.yirun.framework.core.model.ResponseEntity<?>
    *  @Creation Date  ：2019/1/17
    *  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
    */
    ResponseEntity<?> matchInvestExchange(Integer bidId);
    /**
    *  @Description    ：投资记录分页查询
    *  @Method_Name    ：investExchangeListForPager
    *  @param bidId
    *  @return com.yirun.framework.core.model.ResponseEntity<?>
    *  @Creation Date  ：2019/1/18
    *  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
    */
    ResponseEntity<?> investExchangeListForPager(Integer bidId,Pager pager);
    /**
    *  @Description    ：查询交易所匹配标的列表
    *  @Method_Name    ：findExchangeBidList
    *  @param bidName
    *  @param pager
    *  @return com.yirun.framework.core.model.ResponseEntity<?>
    *  @Creation Date  ：2019/1/18
    *  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
    */
    ResponseEntity<?> findExchangeBidList(String bidName, Pager pager);
}
