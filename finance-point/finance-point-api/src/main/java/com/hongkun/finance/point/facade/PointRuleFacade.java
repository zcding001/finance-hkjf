package com.hongkun.finance.point.facade;

import com.yirun.framework.core.model.ResponseEntity;

/**
 * @Description :
 * @Project : finance
 * @Program Name  : com.hongkun.finance.point.facade.PointRuleFacade
 * @Author : zhongpingtang@yiruntz.com 唐忠平
 */
public interface PointRuleFacade {
    /**
     *  @Description    : 启用规则
     *  @Method_Name    : initiateRule
     *  @param ruleId  :规则ID
     *  @return           ResponseEntity
     *  @Creation Date  : 2018年4月8日10:26:34
     *  @Author         : zhongpingtang@hongkun.com.cn
     */
    ResponseEntity initiateRule(Integer ruleId);
}
