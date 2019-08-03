package com.hongkun.finance.web.controller.bi;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.bi.facade.StaFacade;
import com.hongkun.finance.bi.service.StaFunProDisService;
import com.hongkun.finance.invest.model.vo.StaFunBidVO;
import com.hongkun.finance.invest.service.BidInfoService;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.DateUtils;
import com.yirun.framework.core.utils.pager.Pager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 产品相关统计
 *
 * @author zc.ding
 * @create 2018/9/19
 */
@Controller
@RequestMapping("/staProController/")
public class StaProController {
    
    @Reference
    private StaFunProDisService staFunProDisService;
    @Reference
    private StaFacade staFacade;
    @Reference
    private BidInfoService bidInfoService;
    
    /**
    *  产品分布(标的募集统计)
    *  @Method_Name             ：staFunProDis
    *  @param period            ：时间周期 link PeriodNuem
    *  @param startTime         : 日期开始时间
    *  @param endTime           : 日期截止时间
    *  @return com.yirun.framework.core.model.ResponseEntity<?>
    *  @Creation Date           ：2018/9/19
    *  @Author                  ：zc.ding@foxmail.com
    */
    @RequestMapping("staFunProDis")
    @ResponseBody
    public ResponseEntity<?> staFunProDis(Integer period, String startTime, String endTime){
        return new ResponseEntity<>(Constants.SUCCESS, staFunProDisService.findStaFunProDisListByBidProType(period, startTime, endTime));
    }

    /**
    *  客户端投资查询
    *  @Method_Name             ：staFunInvest
    *  @param realName
    *  @param login
    *  @param startTime
    *  @param endTime
    *  @return com.yirun.framework.core.model.ResponseEntity<?>
    *  @Creation Date           ：2018/9/19
    *  @Author                  ：zc.ding@foxmail.com
    */
    @RequestMapping("staFunInvest")
    @ResponseBody
    public ResponseEntity<?> staFunInvest(Pager pager, String realName, Long login, String startTime, String endTime){
        return new ResponseEntity<>(Constants.SUCCESS, this.staFacade.findStaFunInvest(pager, realName, login, startTime, endTime));
    }

    /**
    *  标的信息功能查询
    *  @Method_Name             ：staFunBid
    *  @param pager
    *  @param staFunBidVO
    *  @return com.yirun.framework.core.model.ResponseEntity<?>
    *  @Creation Date           ：2018/9/19
    *  @Author                  ：zc.ding@foxmail.com
    */
    @RequestMapping("staFunBid")
    @ResponseBody
    public ResponseEntity<?> staFunBid(Pager pager, StaFunBidVO staFunBidVO){
        if(StringUtils.isNotBlank(staFunBidVO.getCreateTimeEnd())){
            staFunBidVO.setCreateTimeEnd(DateUtils.format(DateUtils.addDays(DateUtils.parse(staFunBidVO.getCreateTimeEnd(), DateUtils.DATE), 1), DateUtils.DATE));
        }
        return new ResponseEntity<>(Constants.SUCCESS, this.staFacade.findStaFunBid(pager, staFunBidVO));
    }

    /**
    *  平台运营统计
    *  @Method_Name             ：staPlatformAddup
    *  @param startTime
    *  @param endTime
    *  @return com.yirun.framework.core.model.ResponseEntity<?>
    *  @Creation Date           ：2018/9/21
    *  @Author                  ：zc.ding@foxmail.com
    */
    @RequestMapping("staFunPlatformAddup")
    @ResponseBody
    public ResponseEntity<?> staFunPlatformAddup(String startTime, String endTime){
        return this.staFacade.findStaPlatformAddup(startTime, endTime);
    }
}