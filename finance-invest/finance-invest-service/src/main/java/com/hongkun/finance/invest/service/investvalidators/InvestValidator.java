package com.hongkun.finance.invest.service.investvalidators;

import com.yirun.framework.core.model.ResponseEntity;

public interface InvestValidator {

    /**
     * 投资前置验证
     * @param agrs
     * @return
     */
    ResponseEntity doVlidate(Object  agrs);

    /**
     * 设置下一个验证器
     * @param nextValidator
     */
    void setNextValidator(InvestValidator nextValidator);

    /**
     * 验证级别
     * @return
     */
    int getOrder();
}
