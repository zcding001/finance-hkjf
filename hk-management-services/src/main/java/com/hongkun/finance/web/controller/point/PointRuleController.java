package com.hongkun.finance.web.controller.point;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.point.facade.PointRuleFacade;
import com.hongkun.finance.point.model.PointRule;
import com.hongkun.finance.point.service.PointRuleService;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.yirun.framework.core.commons.Constants.ERROR;
import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * @Description : 积分规则对应的controller
 * @Project : finance
 * @Program Name  : com.hongkun.finance.web.controller.point.PointRuleController
 * @Author : zhongpingtang@yiruntz.com 唐忠平
 */
@Controller
@RequestMapping("/pointRuleController")
public class PointRuleController {

    @Reference
    private PointRuleService pointRuleService;
    @Reference
    private PointRuleFacade pointRuleFacade;

    /**
     * 添加积分规则
     * @param pointRule
     * @return
     */
    @RequestMapping("/addPointRule")
    @ResponseBody
    public ResponseEntity addPointRule(PointRule pointRule){
        if (pointRuleService.insertPointRule(pointRule)>0) {
            return new ResponseEntity(SUCCESS, "保存积分成功");
        }
        return new ResponseEntity(ERROR, "保存积分失败");
    }

    /**
     * 修改积分规则
     * @param pointRule
     * @return
     */
    @RequestMapping("/updatePointRule")
    @ResponseBody
    public ResponseEntity updatePointRule(PointRule pointRule){
        if (pointRuleService.updatePointRule(pointRule)>0) {
            return new ResponseEntity(SUCCESS, "修改积分成功");
        }
        return new ResponseEntity(ERROR, "修改积分失败");
    }

    /**
     * 列出当前的规则
     * @param pointRule
     * @param pager
     * @return
     */
    @RequestMapping("/pointRuleList")
    @ResponseBody
    public ResponseEntity pointRuleList(PointRule pointRule,Pager pager){
        Pager resultPage = pointRuleService.findPointRuleList(pointRule, pager);
        return new ResponseEntity(SUCCESS, resultPage);
    }

    /**
     * 启用规则
     * @param ruleId
     * @return
     */
    @RequestMapping("/initiateRule")
    @ResponseBody
    public ResponseEntity initiateRule(@RequestParam("ruleId") Integer ruleId) {
        return pointRuleFacade.initiateRule(ruleId);
    }

}
