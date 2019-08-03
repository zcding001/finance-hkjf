package com.hongkun.finance.activity.facade;

import com.hongkun.finance.user.model.RegUser;
import com.yirun.framework.core.model.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public interface LotteryActivityFacade {


    /**
     *  @Description    ：获取活动详情
     *  @Method_Name    ：findLotteryActivity
     *  @param activityId
     *  @return java.util.Map<java.lang.String,java.lang.Object>
     *  @Creation Date  ：2018年10月15日 10:13
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    ResponseEntity<?> findLotteryActivity(String activityId);

    /**
     *  @Description    ：抽奖
     *  @Method_Name    ：lottery
     *  @param tel
     *  @param activityId
     *  @param userLocationFlag
     * @return com.yirun.framework.core.model.ResponseEntity<?>
     *  @Creation Date  ：2018年10月15日 10:33
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    ResponseEntity<?> lottery(String tel, String activityId, Integer userLocationFlag);

    /**
     *  @Description    ：获取抽奖记录
     *  @Method_Name    ：getMyPrizeList
     *  @param loginUser
     *  @return com.yirun.framework.core.model.ResponseEntity<?>
     *  @Creation Date  ：2018年10月15日 11:03
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    ResponseEntity<?> getMyPrizeList(RegUser loginUser);

    /**
     *  @Description    ：兑换奖品
     *  @Method_Name    ：exchangePrize
     *  @param activityId
     *  @param lotteryRecordId
     *  @param loginUser
     *  @return com.yirun.framework.core.model.ResponseEntity<?>
     *  @Creation Date  ：2018年10月15日 11:32
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    ResponseEntity<?>  exchangePrize(String activityId, String lotteryRecordId, RegUser loginUser);

    /**
     *  @Description    ：获取我的活动中奖详情
     *  @Method_Name    ：getMyPrizeInfo
     *  @param activityId
     *  @param loginUser
     *  @return com.yirun.framework.core.model.ResponseEntity<?>
     *  @Creation Date  ：2018年10月15日 11:32
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    ResponseEntity<?> getMyPrizeInfo(String activityId, RegUser loginUser);
}
