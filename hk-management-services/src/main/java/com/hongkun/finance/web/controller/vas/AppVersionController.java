package com.hongkun.finance.web.controller.vas;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.user.support.security.annotation.ActionLog;
import com.hongkun.finance.vas.constants.AppVersionConstants;
import com.hongkun.finance.vas.model.SysAppVersionRule;
import com.hongkun.finance.vas.service.SysAppVersionRuleService;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.yirun.framework.core.commons.Constants.ERROR;
import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * @Description : app版本更新控制类
 * @Project : framework
 * @Program Name  : com.hongkun.finance.web.controller.vas.AppVersionController
 * @Author : pengwu@hongkun.com.cn 吴鹏
 */
@Controller
@RequestMapping("/appVersionController")
public class AppVersionController {

    @Reference
    private SysAppVersionRuleService sysAppVersionRuleService;

    private final static Logger logger = LoggerFactory.getLogger(AppVersionController.class);

    @RequestMapping("/appVersionRuleList")
    @ResponseBody
    public ResponseEntity appVersionRuleList(SysAppVersionRule rule, Pager pager){
        rule.setSortColumns("state DESC,uptime DESC,create_time DESC");
        return new ResponseEntity(SUCCESS,sysAppVersionRuleService.findSysAppVersionRuleList(rule,pager));
    }

    @RequestMapping("/addAppVersionRule")
    @ResponseBody
    @ActionLog(msg = "添加app版本更新规则, 平台: {args[0].platform}, 最小版本: {args[0].minVersion}, 最大版本: {args[0].maxVersion}")
    public ResponseEntity addAppVersionRule(SysAppVersionRule rule){
        if (sysAppVersionRuleService.insertSysAppVersionRule(rule) > 0){
            return new ResponseEntity(SUCCESS);
        }
        return new ResponseEntity(ERROR);
    }

    @RequestMapping("/upAppVersionRule")
    @ResponseBody
    @ActionLog(msg = "上线app版本更新规则, 规则id: {args[0]}")
    public ResponseEntity upAppVersionRule(int id){
        return sysAppVersionRuleService.updateAppVersionRuleState(id,AppVersionConstants.APP_VERSION_STATE_UP);
    }

    @RequestMapping("/downAppVersionRule")
    @ResponseBody
    @ActionLog(msg = "下线app版本更新规则, 规则id : {args[0]}")
    public ResponseEntity downAppVersionRule(int id){
        return sysAppVersionRuleService.updateAppVersionRuleState(id,AppVersionConstants.APP_VERSION_STATE_DOWN);
    }
}
