package com.hongkun.finance.web.controller.bi;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.bi.facade.StaFacade;
import com.hongkun.finance.bi.model.StaSourcePlatform;
import com.hongkun.finance.bi.service.StaSourcePlatformService;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description : 平台相关统计
 * @Project : finance
 * @Program Name  : com.hongkun.finance.web.controller.bi
 * @Author : yanbinghuang
 */
@Controller
@RequestMapping("/platformCountController")
public class PlatformCountController {


    @Reference
    private StaSourcePlatformService staSourcePlatformService;
    @Reference
    private StaFacade staFacade;
    
   /**
    *  @Description    : 查询平台数据统计信息
    *  @Method_Name    : findPlatformCountList;
    *  @param staSourcePlatform
    *  @param pager
    *  @return
    *  @return         : ResponseEntity<?>;
    *  @Creation Date  : 2018年9月20日 下午4:10:00;
    *  @Author         : yanbinghuang@hongkun.com.cn 黄艳兵;
    */
    @RequestMapping("/findPlatformCountList")
    @ResponseBody
    ResponseEntity<?> findPlatformCountList(StaSourcePlatform staSourcePlatform, Pager pager) {
        staSourcePlatform.setSortColumns("da desc");
        return new ResponseEntity<>(Constants.SUCCESS,staSourcePlatformService.findStaSourcePlatformList(staSourcePlatform, pager));
    }
    /**
     *  @Description    : 平台资金流动统计
     *  @Method_Name    : findTransfterCountList;
     *  @param period
     *  @param startTime
     *  @param endTime
     *  @return
     *  @return         : ResponseEntity<?>;
     *  @Creation Date  : 2018年9月21日 下午2:49:57;
     *  @Author         : yanbinghuang@hongkun.com.cn 黄艳兵;
     */
    @RequestMapping("/findTransfterCountList")
    @ResponseBody
    ResponseEntity<?> findTransfterCountList(Integer period, String startTime, String endTime) {
        return new ResponseEntity<>(Constants.SUCCESS, staFacade.findTransferCountList(period, startTime, endTime));
    }

}
