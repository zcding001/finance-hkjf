package com.hongkun.finance.web.controller.bi;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.bi.facade.StaFacade;
import com.hongkun.finance.bi.model.StaFunInvite;
import com.hongkun.finance.bi.service.StaFunInviteService;
import com.hongkun.finance.bi.service.StaUserFirstService;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.DateUtils;
import com.yirun.framework.core.utils.pager.Pager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.Collections;

/**
 * 用户统计
 *
 * @author zc.ding
 * @create 2018/9/17
 */
@Controller
@RequestMapping("/userStaController/")
public class UserStaController {

    @Reference
    private StaFunInviteService staFunInviteService;
    @Reference
    private StaUserFirstService staUserFirstService;
    @Reference
    private StaFacade staFacade;
    
    
    /**
    *  邀请统计
    *  @Method_Name             ：staInvite
    *  @param pager
    *  @param staFunInvite
    *  @return com.yirun.framework.core.model.ResponseEntity<?>
    *  @Creation Date           ：2018/9/17
    *  @Author                  ：zc.ding@foxmail.com
    */
    @RequestMapping("/staInvite")
    @ResponseBody
    public ResponseEntity<?> staInvite(Pager pager, StaFunInvite staFunInvite){
        if(staFunInvite.getRegTimeEnd() != null){
            staFunInvite.setRegTimeEnd(DateUtils.addDays(staFunInvite.getRegTimeEnd(), 1));
        }
        return new ResponseEntity<>(Constants.SUCCESS, this.staFunInviteService.findStaFunInviteList(staFunInvite, pager));
    }

    /**
    *  用户转化率
    *  @Method_Name             ：staUserCvr
    *  @param period            : 时间周期
    *  @param startTime         ：开始时间
    *  @param endTime           : 结束时间
    *  @return com.yirun.framework.core.model.ResponseEntity<?>
    *  @Creation Date           ：2018/9/17
    *  @Author                  ：zc.ding@foxmail.com
    */
    @RequestMapping("/staUserCvr")
    @ResponseBody
    public ResponseEntity<?> staUserCvr(Integer period, String startTime, String endTime){
        return this.staUserFirstService.findUserCvr(period, startTime, endTime);
    }
    
    /**
    *  新老用户投资分布
    *  @Method_Name             ：staInvestDis
    *  @param period            : 时间周期
    *  @param startTime         ：开始时间
    *  @param endTime           : 结束时间
    *  @return com.yirun.framework.core.model.ResponseEntity<?>
    *  @Creation Date           ：2018/9/17
    *  @Author                  ：zc.ding@foxmail.com
    */
    @RequestMapping("/staInvestDis")
    @ResponseBody
    public ResponseEntity<?> staInvestDis(Integer period, String startTime, String endTime){
        return new ResponseEntity<>(Constants.SUCCESS, Collections.singleton(this.staFacade.findInvestDis(period, 
                startTime, endTime)));
    }
}
