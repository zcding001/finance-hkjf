package com.hongkun.finance.invest.service.investvalidators;

import com.yirun.framework.core.model.ResponseEntity;

/**
 * 抽象投资验证器
 */
public abstract class AbstractInvestValidator implements InvestValidator{
    /**
     * 前一个验证器
     */
    private InvestValidator nextValidator;


    public void setNextValidator(InvestValidator nextValidator) {
        this.nextValidator = nextValidator;
    }


    @Override
    public ResponseEntity doVlidate(Object agrs) {
        ResponseEntity validateStatus = vliadatorLogic(agrs);
        if ((!validateStatus.validSuc())||nextValidator==null) {
            return validateStatus;
        }
        //验证下一步逻
        return nextValidator.doVlidate(agrs);
    }

    /**
     * 投资逻辑
     * @param args
     * @return
     */
    protected abstract ResponseEntity vliadatorLogic(Object args);

}
