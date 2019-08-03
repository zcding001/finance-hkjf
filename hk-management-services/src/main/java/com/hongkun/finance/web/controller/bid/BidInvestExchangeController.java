package com.hongkun.finance.web.controller.bid;


import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.invest.facade.BidInvestExchangeFacade;
import com.hongkun.finance.invest.service.BidInvestExchangeService;
import com.hongkun.finance.user.support.security.annotation.ActionLog;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description : 交易所匹配控制类
 * @Project : finance-hkjf
 * @Program Name  : com.hongkun.finance.web.controller.bid
 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
 */
@Controller
@RequestMapping("/bidInvestExchangeController")
public class BidInvestExchangeController {

    @Reference
    private BidInvestExchangeService bidInvestExchangeService;


    @Reference
    private BidInvestExchangeFacade bidInvestExchangeFacade;



    /**
    *  @Description    ：查询交易所标的信息
    *  @Method_Name    ：biddExchangeList
    *  @param bidName
    *  @param pager
    *  @return com.yirun.framework.core.model.ResponseEntity<?>
    *  @Creation Date  ：2019/1/14
    *  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
    */
    @RequestMapping("/bidExchangeList")
    @ResponseBody
    public ResponseEntity<?> bidExchangeList(String bidName, Pager pager){
        return bidInvestExchangeFacade.findExchangeBidList(bidName,pager);
    }

    /**
    *  @Description    ：交易所标的预匹配投资记录
    *  @Method_Name    ：investExchangeList
    *  @param bidId
    *  @return com.yirun.framework.core.model.ResponseEntity<?>
    *  @Creation Date  ：2019/1/16
    *  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
    */
    @RequestMapping("/investExchangeList")
    @ResponseBody
    public ResponseEntity<?> investExchangeList(Integer bidId){
          return bidInvestExchangeFacade.initBiddInvest(bidId);
    }

    /**
     *  @Description    ：匹配
     *  @Method_Name    ：matchInvestExchange
     *  @param bidId
     *  @return com.yirun.framework.core.model.ResponseEntity<?>
     *  @Creation Date  ：2019/1/16
     *  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
     */
    @RequestMapping("/matchInvestExchange")
    @ResponseBody
    @ActionLog(msg="交易所标的匹配, 标的id: {args[0]}")
    public ResponseEntity<?> matchInvestExchange(Integer bidId){
        return bidInvestExchangeFacade.matchInvestExchange(bidId);
    }

    /**
    *  @Description    ：交易所投资记录信息（查看详情页面调用）
    *  @Method_Name    ：investExchangeListForPager
    *  @param bidId
    *  @return com.yirun.framework.core.model.ResponseEntity<?>
    *  @Creation Date  ：2019/1/18
    *  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
    */
    @RequestMapping("/investExchangeListForPager")
    @ResponseBody
    public ResponseEntity<?> investExchangeListForPager(Integer bidId,Pager pager){
        return bidInvestExchangeFacade.investExchangeListForPager(bidId,pager);
    }
}
