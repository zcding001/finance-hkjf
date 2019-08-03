package com.hongkun.finance.web.controller.vas;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.vas.constants.VasConstants;
import com.hongkun.finance.vas.model.VasVipGrowRule;
import com.hongkun.finance.vas.service.VasVipGrowRuleService;
import com.yirun.framework.core.model.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

import static com.yirun.framework.core.commons.Constants.ERROR;
import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * @Description : 会员等级成长值规则管理Controller
 * @Project : framework
 * @Program Name  : com.hongkun.finance.web.controller.vas.VipGrowRuleController
 * @Author : pengwu@hongkun.com.cn 吴鹏
 */
@Controller
@RequestMapping("/vipGrowRuleController")
public class VipGrowRuleController {

    @Reference
    private VasVipGrowRuleService vasVipGrowRuleService;

    /**
     *  @Description    : 获取所有的会员等级成长值规则记录
     *  @Method_Name    : vipGrowRuleList
     *  @return         : ResponseEntity
     *  @Creation Date  : 2017年6月30日 上午11:14:50
     *  @Author         : pengwu@hongkun.com.cn
     */
    @RequestMapping("/vipGrowRuleList")
    @ResponseBody
    public ResponseEntity vipGrowRuleList(){
        VasVipGrowRule vasVipGrowRule = new VasVipGrowRule();
        List<VasVipGrowRule> list = vasVipGrowRuleService.findVasVipGrowRuleList(vasVipGrowRule);
        return new ResponseEntity(SUCCESS,list);
    }

    /**
     *  @Description    : 保存会员等级成长值规则记录
     *  @Method_Name    : addVipGrowRule
     *  @param vasVipGrowRule
     *  @return         : ResponseEntity
     *  @Creation Date  : 2017年6月30日 上午11:24:50
     *  @Author         : pengwu@hongkun.com.cn
     */
    @RequestMapping("/addVipGrowRule")
    @ResponseBody
    public ResponseEntity addVipGrowRule(@Valid VasVipGrowRule vasVipGrowRule){
        return vasVipGrowRuleService.addVipGrowRule(vasVipGrowRule);
    }

    /**
     *  @Description    : 更新会员成长值规则记录
     *  @Method_Name    : updateVipGrowRule
     *  @param vasVipGrowRule
     *  @return         : ResponseEntity
     *  @Creation Date  : 2017年6月30日 上午11:28:50
     *  @Author         : pengwu@hongkun.com.cn
     */
    @RequestMapping("/updateVipGrowRule")
    @ResponseBody
    public ResponseEntity updateVipGrowRule(VasVipGrowRule vasVipGrowRule){
        return vasVipGrowRuleService.updateVipGrowRule(vasVipGrowRule);
    }

}
