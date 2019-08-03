package com.hongkun.finance.web.controller.bi;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.bi.facade.BalAccountFacade;
import com.hongkun.finance.bi.model.StaFunRecommendBonus;
import com.hongkun.finance.bi.model.StaFunRecommendSend;
import com.hongkun.finance.bi.service.StaFunRecommendBonusService;
import com.hongkun.finance.bi.service.StaFunRecommendSendService;
import com.hongkun.finance.qdz.facade.QdzConsoleFacade;
import com.hongkun.finance.qdz.vo.QdzTransferRecordVo;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description : 推荐奖相关统计
 * @Project : finance
 * @Program Name  : com.hongkun.finance.web.controller.bi
 * @Author : yanbinghuang
 */
@Controller
@RequestMapping("/recommendCountController")
public class RecommendCountController {


    @Reference
    private StaFunRecommendSendService staFunRecommendSendService;
    @Reference
    private StaFunRecommendBonusService staFunRecommendBonusService;
    /**
     *  @Description    : 查询推荐奖发放统计信息
     *  @Method_Name    : findRecomendSendCountList;
     *  @param staFunRecommendSend
     *  @param pager
     *  @return
     *  @return         : ResponseEntity<?>;
     *  @Creation Date  : 2018年9月19日 下午1:58:23;
     *  @Author         : yanbinghuang@hongkun.com.cn 黄艳兵;
     */
    @RequestMapping("/findRecomendSendCountList")
    @ResponseBody
    ResponseEntity<?> findRecomendSendCountList(StaFunRecommendSend staFunRecommendSend, Pager pager) {
        return new ResponseEntity<>(Constants.SUCCESS,staFunRecommendSendService.findStaFunRecommendSendList(staFunRecommendSend, pager));
    }
    /**
     *  @Description    : 查询推荐奖金统计信息
     *  @Method_Name    : findRecomendEarnCountList;
     *  @param staFunRecommendBonus
     *  @param pager
     *  @return
     *  @return         : ResponseEntity<?>;
     *  @Creation Date  : 2018年9月19日 下午2:03:05;
     *  @Author         : yanbinghuang@hongkun.com.cn 黄艳兵;
     */
    @RequestMapping("/findRecomendEarnCountList")
    @ResponseBody
    ResponseEntity<?> findRecomendEarnCountList(StaFunRecommendBonus staFunRecommendBonus, Pager pager) {
        return new ResponseEntity<>(Constants.SUCCESS,staFunRecommendBonusService.findStaFunRecommendBonusList(staFunRecommendBonus, pager));
    }

}
