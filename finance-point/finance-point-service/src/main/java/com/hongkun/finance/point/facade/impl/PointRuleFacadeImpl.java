package com.hongkun.finance.point.facade.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.point.constants.PointConstants;
import com.hongkun.finance.point.facade.PointRuleFacade;
import com.hongkun.finance.point.model.PointRule;
import com.hongkun.finance.point.service.PointRuleService;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.model.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static com.hongkun.finance.point.constants.PointConstants.POINT_RULE_CHANGE_LOCK_KEY;

/**
 * @Description :
 * @Project : finance
 * @Program Name  : com.hongkun.finance.point.facade.impl.PointRuleFacadeImpl
 * @Author : zhongpingtang@yiruntz.com 唐忠平
 */
@Service
public class PointRuleFacadeImpl implements PointRuleFacade {
    private static final Logger logger = LoggerFactory.getLogger(PointRuleFacadeImpl.class);

    @Reference
    private PointRuleService pointRuleService;

   /**
   *  @Description    ：启用规则，这里需要用到分布式锁
   *  @Method_Name    ：initiateRule
   *  @param ruleId
   *  @return com.yirun.framework.core.model.ResponseEntity
   *  @Creation Date  ：2018/4/24
   *  @Author         ：zhongpingtang@hongkun.com.cn
   */
    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public ResponseEntity initiateRule(Integer ruleId) {
        if (logger.isInfoEnabled()) {
            logger.info("方法名: initiateRule, 启用积分规则, 规则ID: {}", ruleId);
        }
        try {
            return (ResponseEntity) BaseUtil.redisLock(POINT_RULE_CHANGE_LOCK_KEY,
                    null,
                    "启用积分申请redis锁失败, 启用规则ID: " + ruleId,
                    (whatever) -> {
                        //找出目前的启用的规则
                        PointRule currentOnUseRule = new PointRule();
                        currentOnUseRule.setState(PointConstants.POINT_RULE_STATE_ON_USE);

                        //更新此条状态
                        PointRule invalidRule = pointRuleService.getCurrentOnUseRule();
                        if (invalidRule!=null) {
                            invalidRule.setState(PointConstants.POINT_RULE_STATE_INVALID);
                            pointRuleService.updatePointRule(invalidRule);
                        }

                        //启用传入的这条规则
                        PointRule onUsePointRule = new PointRule(ruleId);
                        onUsePointRule.setState(PointConstants.POINT_RULE_STATE_ON_USE);
                        pointRuleService.updatePointRule(onUsePointRule);
                        return ResponseEntity.SUCCESS;
                    });
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("启用积分规则失败, 规则ID: {}\n异常信息: ", ruleId, e);
            }
            throw new GeneralException("启用积分规则失败,请重试");
        }

    }
}
