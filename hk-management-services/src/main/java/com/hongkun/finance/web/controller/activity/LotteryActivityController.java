package com.hongkun.finance.web.controller.activity;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.activity.facade.LotteryItemFacade;
import com.hongkun.finance.activity.model.LotteryActivity;
import com.hongkun.finance.activity.model.vo.LotteryRecordVo;
import com.hongkun.finance.activity.service.LotteryActivityService;
import com.hongkun.finance.activity.service.LotteryItemService;
import com.hongkun.finance.activity.service.LotteryRecordService;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * @Description: 转盘抽奖活动
 * @Program: com.hongkun.finance.web.controller.activity.LotteryActivityController
 * @Author: yunlongliu@hongkun.com.cn
 * @Date: 2018-09-26 16:13
 **/
@Controller
@RequestMapping("/lotteryActivityController")
public class LotteryActivityController {

    @Reference
    private LotteryActivityService lotteryActivityService;

    @Reference
    private LotteryItemService lotteryItemService;

    @Reference
    private LotteryRecordService lotteryRecordService;

    @Reference
    private LotteryItemFacade lotteryItemFacade;



    /**
     *  @Description    ：获取抽奖活动列表
     *  @Method_Name    ：findLotteryActivityList
     *  @param lotteryActivity
     *  @param pager
     *  @return com.yirun.framework.core.model.ResponseEntity<?>
     *  @Creation Date  ：2018年09月26日 17:18
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    @ResponseBody
    @RequestMapping("/findLotteryActivityList")
    public ResponseEntity<?> findLotteryActivityList(LotteryActivity lotteryActivity, Pager pager){
        return new ResponseEntity<>(SUCCESS,lotteryActivityService.findLotteryActivityWithCondition(lotteryActivity,pager));
    }


    /**
     *  @Description    ：更新活动状态
     *  @Method_Name    ：updateState
     *  @param lotteryActivity
     *  @return com.yirun.framework.core.model.ResponseEntity<?>
     *  @Creation Date  ：2018年09月27日 09:34
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    @ResponseBody
    @RequestMapping("/updateState")
    public ResponseEntity<?> updateState(LotteryActivity lotteryActivity){
        lotteryActivityService.updateLotteryActivity(lotteryActivity);
        return new ResponseEntity<>(SUCCESS);
    }


    /**
     *  @Description    ：更新抽奖活动
     *  @Method_Name    ：updateLotteryActivity
     *  @param lotteryActivity
     *  @return com.yirun.framework.core.model.ResponseEntity<?>
     *  @Creation Date  ：2018年09月27日 10:59
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    @ResponseBody
    @RequestMapping("/updateLotteryActivity")
    public ResponseEntity<?> updateLotteryActivity(LotteryActivity lotteryActivity){
        RegUser loginUser = BaseUtil.getLoginUser();
        lotteryActivity.setRegUserId(loginUser.getId());
        lotteryActivityService.updateLotteryActivity(lotteryActivity);
        return new ResponseEntity<>(SUCCESS);
    }


    /**
     *  @Description    ：保存抽奖活动
     *  @Method_Name    ：saveLotteryActivity
     *  @param lotteryActivity
     *  @return com.yirun.framework.core.model.ResponseEntity<?>
     *  @Creation Date  ：2018年09月27日 11:00
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    @ResponseBody
    @RequestMapping("/saveLotteryActivity")
    public ResponseEntity<?> saveLotteryActivity(LotteryActivity lotteryActivity){
        RegUser loginUser = BaseUtil.getLoginUser();
        lotteryActivity.setRegUserId(loginUser.getId());
        return lotteryActivityService.saveLotteryActivity(lotteryActivity);
    }


    /**
     *  @Description    ：获取抽奖活动详情
     *  @Method_Name    ：getLotteryActivityDetail
     *  @param id
     *  @return com.yirun.framework.core.model.ResponseEntity<?>
     *  @Creation Date  ：2018年09月27日 11:48
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    @ResponseBody
    @RequestMapping("/getLotteryActivityDetail")
    public ResponseEntity<?> getLotteryActivityDetail(@RequestParam("id") Integer id){
        return new ResponseEntity<>(SUCCESS,lotteryActivityService.findLotteryActivityById(id));
    }



    /**
     *  @Description    ：获取抽奖活动奖项
     *  @Method_Name    ：getLotteryActivityItems
     *  @param lotteryActivityId
     *  @return com.yirun.framework.core.model.ResponseEntity<?>
     *  @Creation Date  ：2018年10月08日 09:52
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    @RequestMapping("/getLotteryActivityItems")
    @ResponseBody
    public ResponseEntity<?> getLotteryActivityItems(@RequestParam(value = "lotteryActivityId") Integer lotteryActivityId){
        return new ResponseEntity<>(SUCCESS,lotteryItemService.getLotteryActivityItems(lotteryActivityId));
    }


    /**
     *  @Description    ：保存抽奖活动奖项
     *  @Method_Name    ：saveLotteryActivityItems
     *  @param groupsJSON
     *  @return com.yirun.framework.core.model.ResponseEntity<?>
     *  @Creation Date  ：2018年10月08日 09:25
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    @RequestMapping("/saveLotteryActivityItems")
    @ResponseBody
    public ResponseEntity<?> saveLotteryActivityItems(@RequestParam(value = "groups") String groupsJSON){
        RegUser loginUser = BaseUtil.getLoginUser();
        return lotteryItemFacade.saveLotteryItems(groupsJSON,loginUser);
    }


    /**
     *  @Description    ：获取抽奖记录列表
     *  @Method_Name    ：findLotteryActivityList
     *  @param vo
     *  @param pager
     *  @return com.yirun.framework.core.model.ResponseEntity<?>
     *  @Creation Date  ：2018年10月22日 16:27
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    @ResponseBody
    @RequestMapping("/lotteryRecordList")
    public ResponseEntity<?> findLotteryActivityList(LotteryRecordVo vo, Pager pager){
        return new ResponseEntity<>(SUCCESS,lotteryActivityService.findLotteryRecordWithCondition(vo,pager));
    }


}

