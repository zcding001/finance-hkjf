package com.hongkun.finance.invest.facade.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.invest.constants.InvestConstants;
import com.hongkun.finance.invest.facade.BidInvestExchangeFacade;
import com.hongkun.finance.invest.model.BidInfo;
import com.hongkun.finance.invest.model.BidInvestExchange;
import com.hongkun.finance.invest.model.vo.BidInfoExchangeVo;
import com.hongkun.finance.invest.service.BidInfoService;
import com.hongkun.finance.invest.service.BidInvestExchangeService;
import com.hongkun.finance.invest.util.CalcInterestUtil;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.model.vo.UserVO;
import com.hongkun.finance.user.service.RegUserService;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.CommonUtils;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.redis.JedisClusterUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Description : 交易所标的匹配服务
 * @Project : finance-hkjf
 * @Program Name  : com.hongkun.finance.invest.facade.impl
 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
 */
@Service
public class BidInvestExchangeFacadeImpl implements BidInvestExchangeFacade {
    @Autowired
    private BidInfoService bidInfoService;
    @Autowired
    private BidInvestExchangeService bidInvestExchangeService;
    @Reference
    private RegUserService regUserService;
    @Override
    public ResponseEntity<?> initBiddInvest(Integer bidId) {

        Map<String,Object> params = new HashMap<String,Object>();
        //查询标的信息，如果是还款中标的，查询投资记录直接返回
        BidInfo bidInfo = bidInfoService.findBidInfoById(bidId);
        if (bidInfo.getState() == InvestConstants.BID_STATE_WAIT_REPAY){
            List<BidInvestExchange>  investExchangeList =  bidInvestExchangeService.findExchangeInvestListByBidId(bidId);
            List<Integer>  userIds = new ArrayList<Integer>();
            if (CommonUtils.isNotEmpty(investExchangeList)){
               for (BidInvestExchange invest:investExchangeList){
                   userIds.add(invest.getInvestUserId());
               }
               RegUser regUser = new RegUser();
               regUser.setUserIds(userIds);
               List<RegUser> users =  regUserService.findRegUserList(regUser);
                investExchangeList.forEach(invest->{
                    for (RegUser ru :users){
                        if (ru.getId().equals(invest.getInvestUserId()) ){
                            invest.setTel(String.valueOf(ru.getLogin()));
                            break;
                        }
                    }
                });
            }
            params.put("bidState",InvestConstants.BID_STATE_WAIT_REPAY);
            return new ResponseEntity<>(Constants.SUCCESS,investExchangeList,params);
        }
        List<BidInvestExchange>  resultList = new ArrayList<BidInvestExchange>();
        //查询待匹配投资记录
        List<BidInvestExchange> investLists =  bidInvestExchangeService.findInvestWating();

        //本次已匹配过的用户id  --- 限制标的投资记录不能出现同一个用户投资多笔
        List<Integer> userIds = new ArrayList<Integer>();
        //标的金额
        BigDecimal biddAmount = bidInfo.getTotalAmount();
        //已匹配金额
        BigDecimal sumWatingInvestAtm = BigDecimal.ZERO;
        //金额大于250000的投资记录匹配之后剩余待匹配金额
        BigDecimal subWatingMoney =  BigDecimal.ZERO;
        if(CommonUtils.isNotEmpty(investLists)){
            for(BidInvestExchange invest:investLists){
                BigDecimal investAtm = invest.getInvestAtm();
                BigDecimal sumAtm = investAtm.add(sumWatingInvestAtm);
                if(sumAtm.compareTo(biddAmount)<=0){
                    if(!userIds.contains(invest.getInvestId())){
                        sumWatingInvestAtm = sumWatingInvestAtm.add(investAtm);
                        userIds.add(invest.getInvestId());
                        resultList.add(invest);
                    }
                }else{
                    if(biddAmount.subtract(sumWatingInvestAtm).compareTo(new BigDecimal(250000)) >=0){
                        continue;
                    }
                    //3、当金额超过标的金额时退出循环
                    subWatingMoney = biddAmount.subtract(sumWatingInvestAtm);
                    break;
                }
            }
        }
        //未完全匹配，查询投资记录进行补全
        if(subWatingMoney.compareTo(BigDecimal.ZERO)>0){
            //还需要其他投资记录进行匹配 查询state=0的BiddInvestExchangesub待匹配数据200条 金额小于剩余金额 按照时间 金额倒叙排序
            List<BidInvestExchange> subInvestLists = bidInvestExchangeService.findInvestSubWating(subWatingMoney);
            //5、循环list，直到投资记录总金额和标的金额相等
            if(CommonUtils.isNotEmpty(subInvestLists)){
                for(BidInvestExchange invest:subInvestLists){
                    BigDecimal investAtm = invest.getInvestAtm();
                    if(investAtm.compareTo(subWatingMoney)<=0){
                        if(!userIds.contains(invest.getInvestId())){
                            subWatingMoney = subWatingMoney.subtract(investAtm);
                            userIds.add(invest.getInvestId());
                            resultList.add(invest);
                        }
                    }else{
                        if(subWatingMoney.compareTo(BigDecimal.ZERO) != 0){
                            continue;
                        }
                        break;
                    }
                }
            }
        }

        //把投资记录放入缓存，用于匹配
        if(subWatingMoney.compareTo(BigDecimal.ZERO) >= 0){
            //返回投资列表，把匹配的投资id存入redis，便于匹配时候使用
            params.put("matchState", "success");
            JedisClusterUtils.setAsJson("exchange_match_invest_"+bidId,resultList);
        }else{
            params.put("matchState", "fail");
            params.put("subWatingMoney", subWatingMoney);
        }
        return new ResponseEntity<>(Constants.SUCCESS,resultList,params);
    }

    @Override
    public ResponseEntity<?> matchInvestExchange(Integer bidId) {
        //1从redis中取出投资记录列表
        try {
            List<BidInvestExchange> investLists =
                    JedisClusterUtils.getObjectForJson("exchange_match_invest_"+bidId, new TypeReference< List<BidInvestExchange> >(){});
            if (CommonUtils.isNotEmpty(investLists)){
                investLists.forEach(invest->{
                    invest.setCreatedTime(new Date());
                    invest.setState(1);
                    invest.setBidId(bidId);
                });
                //批量插入投资记录
                bidInvestExchangeService.addBatch(investLists);
                //修改标的状态为已放款
                bidInfoService.updateState(bidId,InvestConstants.BID_STATE_WAIT_REPAY);
                return new ResponseEntity<>(Constants.SUCCESS,"匹配成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(Constants.ERROR,"未查询到相关投资记录,稍后重试");
    }

    @Override
    public ResponseEntity<?> investExchangeListForPager(Integer bidId, Pager pager) {
       Pager resultPager =  bidInvestExchangeService.investExchangeListForPager(bidId,pager);
       if(!BaseUtil.resultPageHasNoData(resultPager)){
           List<BidInvestExchange> resultList = (List<BidInvestExchange>) resultPager.getData();
           List<Integer>  userIds = new ArrayList<Integer>();
           for (BidInvestExchange invest:resultList){
               userIds.add(invest.getInvestUserId());
           }
           RegUser regUser = new RegUser();
           regUser.setUserIds(userIds);
           List<RegUser> users =  regUserService.findRegUserList(regUser);
           resultList.forEach(invest->{
               for (RegUser ru :users){
                   if (ru.getId().equals(invest.getInvestUserId()) ){
                       invest.setTel(String.valueOf(ru.getLogin()));
                       break;
                   }
               }
           });
           resultPager.setData(resultList);
       }
        return new ResponseEntity<>(Constants.SUCCESS,resultPager);
    }

    @Override
    public ResponseEntity<?> findExchangeBidList(String bidName, Pager pager) {
        Pager resultPager = bidInvestExchangeService.findExchangeBidList(bidName,pager);
        if(!BaseUtil.resultPageHasNoData(resultPager)){
            List<BidInfoExchangeVo> resultList = (List<BidInfoExchangeVo>) resultPager.getData();
            List<Integer>  userIds = new ArrayList<Integer>();
            for (BidInfoExchangeVo invest:resultList){
                userIds.add(invest.getBorrowerId());
            }
            List<UserVO> users = regUserService.findUserWithDetailByInfo(userIds);
            resultList.forEach(invest->{
                for (UserVO ru :users){
                    if (ru.getUserId().equals(invest.getBorrowerId()) ){
                        invest.setBorrowerName(ru.getRealName());
                        BigDecimal interest = CalcInterestUtil.calcInterest(invest.getTermUnit(),
                                invest.getTermValue(),invest.getTotalAmount(),invest.getInterestRate());
                        invest.setRepayAmount(invest.getTotalAmount().add(interest));
                        break;
                    }
                }
            });
            resultPager.setData(resultList);
        }
        return new ResponseEntity<>(Constants.SUCCESS,resultPager);
    }
}
