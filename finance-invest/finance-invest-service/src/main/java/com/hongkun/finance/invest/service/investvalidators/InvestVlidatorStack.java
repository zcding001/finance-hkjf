package com.hongkun.finance.invest.service.investvalidators;

import com.yirun.framework.core.model.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 为了逻辑清晰，我们可以采用一个辅助结构：验证器栈
 * 来管理目前的投资验证器.
 * 如果不用此结构，每个验证器需要自己维护自己的next验证器
 */
//@Component
public class InvestVlidatorStack {


    /**
     * 起始验证器
     */
    private InvestValidator validatorStarter;

    @Autowired(required = false)
    public void initValidtors( List<InvestValidator> validators){
        if (!CollectionUtils.isEmpty(validators)) {
            //验证排序
            Collections.sort(validators, Comparator.comparingInt(InvestValidator::getOrder));

            //生成验证链
            validators.stream().reduce((current, next) -> {
                current.setNextValidator(next);
                return null;
            });
            validatorStarter = validators.get(0);
        }
    }


    /**
     * 触发验证链
     * @param info
     * @return
     */
    public ResponseEntity doValidateBidInfo(Object info){
        ResponseEntity reuslt=ResponseEntity.SUCCESS;
        if (this.validatorStarter!=null) {
            reuslt=validatorStarter.doVlidate(info);
        }
        return reuslt;
    }


}
