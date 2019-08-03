package com.hongkun.finance.web.controller.fund;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.fund.model.FundAdvisory;
import com.hongkun.finance.fund.service.FundEvaluationService;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.service.RegUserService;
import com.hongkun.finance.user.support.security.annotation.ActionLog;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.model.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * @Description : 股权基金注册、实名、测评相关controller
 * @Project : framework
 * @Program Name  : com.hongkun.finance.web.controller.fund.FundRegistController
 * @Author : yuzegu@hongkun.com.cn googe
 */
@Controller
@RequestMapping("/fundRegistController")
public class FundRegistController {

    @Reference
    private FundEvaluationService fundEvaluationService;

    @Reference
    private RegUserService regUserService;

    @RequestMapping("/saveRiskEvaluation")
    @ResponseBody
    public ResponseEntity<?> saveRiskEvaluation(@RequestParam("answers") String answers, @RequestParam("score") Integer score){
        RegUser regUser = BaseUtil
                .getRegUser(() -> this.regUserService.findRegUserById(BaseUtil.getLoginUser().getId()));
        return fundEvaluationService.saveRiskEvalution(regUser.getId(),answers, score);
    }

}
