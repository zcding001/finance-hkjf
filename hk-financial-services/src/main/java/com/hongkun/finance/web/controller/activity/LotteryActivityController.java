package com.hongkun.finance.web.controller.activity;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.activity.facade.LotteryActivityFacade;
import com.hongkun.finance.activity.service.LotteryActivityService;
import com.hongkun.finance.activity.service.LotteryItemService;
import com.hongkun.finance.activity.service.LotteryRecordService;
import com.hongkun.finance.activity.util.ActivityUtils;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.support.security.OperationTypeEnum;
import com.hongkun.finance.user.support.security.component.annotation.AskForPermission;
import com.hongkun.finance.user.utils.BaseUtil;
import com.hongkun.finance.web.controller.bid.BidInfoController;
import com.yirun.framework.core.model.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 抽奖活动前端控制器
 * @Program: com.hongkun.finance.web.controller.activity.LotteryActivityController
 * @Author: yunlongliu@hongkun.com.cn
 * @Date: 2018-10-15 09:36
 **/
@RequestMapping("/lotteryActivityController")
@Controller
public class LotteryActivityController {

    private static final Logger logger = LoggerFactory.getLogger(BidInfoController.class);

    @Reference
    private LotteryActivityService lotteryActivityService;

    @Reference
    private LotteryRecordService lotteryRecordService;

    @Reference
    private LotteryItemService lotteryItemService;

    @Reference
    private LotteryActivityFacade lotteryActivityFacade;


    /**
     *  @Description    ：跳转到抽奖活动页
     *  @Method_Name    ：lotteryActivity
     *  @param activityId
     *  @return com.yirun.framework.core.model.ResponseEntity<?>
     *  @Creation Date  ：2018年10月15日 09:43
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    @RequestMapping("/lotteryActivity")
    @ResponseBody
    @AskForPermission(operation = OperationTypeEnum.DO_LOGIN)
    public ResponseEntity<?> lotteryActivity(@RequestParam(value = "activityId") String activityId) throws Exception{
        return this.lotteryActivityFacade.findLotteryActivity(activityId);
    }


    /**
     *  @Description    ：抽奖
     *  @Method_Name    ：lottery
     *  @param tel
     *  @param activityId
     *  @return com.yirun.framework.core.model.ResponseEntity<?>
     *  @Creation Date  ：2018年10月15日 10:31
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    @RequestMapping("/lottery")
    @ResponseBody
    @AskForPermission(operation = OperationTypeEnum.DO_LOGIN)
    public ResponseEntity<?> lottery(@RequestParam(value = "tel")String tel,
                                     @RequestParam(value = "activityId")String activityId, HttpServletRequest request){
        // Integer userLocationFlag = ActivityUtils.getUserLocationFlag(request);
        Integer userLocationFlag = 1;
        return this.lotteryActivityFacade.lottery(tel,activityId,userLocationFlag);
    }



    /**
     *  @Description    ：获取我的抽奖记录
     *  @Method_Name    ：getMyPrizeList
     *  @return com.yirun.framework.core.model.ResponseEntity<?>
     *  @Creation Date  ：2018年10月15日 11:06
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    @RequestMapping("/getMyPrizeList")
    @ResponseBody
    @AskForPermission(operation = OperationTypeEnum.DO_LOGIN)
    public ResponseEntity<?> getMyPrizeList(){
        RegUser loginUser = BaseUtil.getLoginUser();
        return this.lotteryActivityFacade.getMyPrizeList(loginUser);
    }


    /**
     *  @Description    ：获取我的活动中奖详情
     *  @Method_Name    ：getMyPrizeInfo
     *  @param activityId
     *  @return com.yirun.framework.core.model.ResponseEntity<?>
     *  @Creation Date  ：2018年10月15日 11:27
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    @RequestMapping("/getMyPrizeInfo")
    @ResponseBody
    @AskForPermission(operation = OperationTypeEnum.DO_LOGIN)
    public ResponseEntity<?> getMyPrizeInfo(@RequestParam(value = "activityId")String activityId){
        RegUser loginUser = BaseUtil.getLoginUser();
        return this.lotteryActivityFacade.getMyPrizeInfo(activityId,loginUser);
    }



    /**
     *  @Description    ：兑换奖品
     *  @Method_Name    ：exchangePrize
     *  @param activityId
     *  @param lotteryRecordId
     *  @return com.yirun.framework.core.model.ResponseEntity<?>
     *  @Creation Date  ：2018年10月15日 11:31
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    @RequestMapping("/exchangePrize")
    @ResponseBody
    @AskForPermission(operation = OperationTypeEnum.DO_LOGIN)
    public ResponseEntity<?> exchangePrize(@RequestParam(value = "activityId")String activityId,
                              @RequestParam(value = "lotteryRecordId")String lotteryRecordId) {
        RegUser loginUser = BaseUtil.getLoginUser();
        return this.lotteryActivityFacade.exchangePrize(activityId,lotteryRecordId,loginUser);

    }











































}
