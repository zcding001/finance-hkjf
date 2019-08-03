package com.hongkun.finance.invest.service.investvalidators;

import com.yirun.framework.core.model.ResponseEntity;

/**
 * 投资状态验证器
 */
//@Component  注册到容器中
public class InvestStatusValidator extends AbstractInvestValidator {


    @Override
    protected ResponseEntity vliadatorLogic(Object args) {
        /*****
         * validateStatus 方法逻辑copy过来
         */

       /* //匹配投资直接放行
        if (bidInfoDetail.getMatchType() == InvestConstants.BID_MATCH_TYPE_YES) {
            return new ResponseEntity<>(SUCCESS);
        }
        // 活期产品只能第三方账户投资
        if (bidProduct.getType() == InvestConstants.BID_PRODUCT_CURRENT) {
            // 第三方账户&标的时未上架状态
            if (regUser.getType() != UserConstants.USER_TYPE_ESCROW_ACCOUNT
                    && bidInfo.getState() != InvestConstants.BID_STATE_WAIT_NEW) {
                return ResponseEntity.error("非活期用户不能投资");
            }
        } else {
            // 标的的状态不为2都认为是无效的投资项目
            if (bidInfo.getState() != InvestConstants.BID_STATE_WAIT_INVEST) {
                return ResponseEntity.error("该项目不是有效的投资项目");
            }
        }
        if (System.currentTimeMillis() > bidInfo.getEndTime().getTime()) {
            return ResponseEntity.error("项目已过期");
        }
        if (System.currentTimeMillis() < bidInfo.getStartTime().getTime()) {
            return ResponseEntity.error("项目未开始");
        }
        //投资来源是PC但标的只允许在APP端进行投资
        if (platformSourceEnums.getValue() == 10 && bidInfo.getInvestPosition().indexOf('1') < 0) {
            return ResponseEntity.error("该项目只允许在APP端投资");
        }
        return ResponseEntity.SUCCESS;*/
        return ResponseEntity.SUCCESS;
    }

    @Override
    public int getOrder() {
        //第一个验证
        return 0;
    }
}
