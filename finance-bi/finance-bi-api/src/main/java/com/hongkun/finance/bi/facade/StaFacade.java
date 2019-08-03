package com.hongkun.finance.bi.facade;

import java.util.List;
import java.util.Map;


import com.hongkun.finance.bi.model.vo.StaUserFirstVO;
import com.hongkun.finance.invest.model.vo.StaFunBidVO;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;

/**
 * 统计
 *
 * @author zc.ding
 * @create 2018/9/17
 */
public interface StaFacade {

    /**
     *  查询用户转化率
     *  @Method_Name             ：findUserCvr
     *  @param period            : 时间周期
     *  @param startTime         ：开始时间
     *  @param endTime           : 结束时间
     *  @return com.hongkun.finance.bi.model.vo.StaUserFirstVO
     *  @Creation Date           ：2018/9/17
     *  @Author                  ：zc.ding@foxmail.com
     */
    StaUserFirstVO findInvestDis(Integer period, String startTime, String endTime);
    
    /**
    *  客户投资查询
    *  @Method_Name             ：findStaFunInvest
    *  @param pager
    *  @param realName
    *  @param login
    *  @param startTime
    *  @param endTime
    *  @return com.yirun.framework.core.utils.pager.Pager
    *  @Creation Date           ：2018/9/19
    *  @Author                  ：zc.ding@foxmail.com
    */
    Pager findStaFunInvest(Pager pager, String realName, Long login, String startTime, String endTime);

    /**
     *  标的统计查询
     *  @Method_Name             ：findStaFunBid
     *  @param pager
     *  @param staFunBidVO
     *  @return com.yirun.framework.core.utils.pager.Pager
     *  @Creation Date           ：2018/9/19
     *  @Author                  ：zc.ding@foxmail.com
     */
    Pager findStaFunBid(Pager pager, StaFunBidVO staFunBidVO);
    /**
     *  @Description    : 资金流程统计查询
     *  @Method_Name    : findTransferCountList;
     *  @param period
     *  @param startTime
     *  @param endTime
     *  @return
     *  @return         : List<Map<String,Object>>;
     *  @Creation Date  : 2018年9月20日 下午6:10:32;
     *  @Author         : yanbinghuang@hongkun.com.cn 黄艳兵;
     */
    List<Map<String, Object>> findTransferCountList(Integer period, String startTime, String endTime);

    
    ResponseEntity<?> findStaPlatformAddup(String startTime, String endTime);
}
