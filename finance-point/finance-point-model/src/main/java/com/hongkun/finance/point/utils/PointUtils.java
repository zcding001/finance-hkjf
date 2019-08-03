package com.hongkun.finance.point.utils;

import java.math.BigDecimal;

/**
 * @Description : 积分相关工具类
 * @Project : framework
 * @Program Name  : com.hongkun.finance.point.utils.PointUtils
 * @Author : pengwu@hongkun.com.cn 吴鹏
 */
public class PointUtils {

    public static BigDecimal pointToMoney(int point,BigDecimal perMoneyToPoint){
        return new BigDecimal(point).divide(perMoneyToPoint,2,BigDecimal.ROUND_HALF_UP);
    }

    public static int moneyToPoint(BigDecimal money,BigDecimal perMoneyToPoint){
        if (money == null || money.compareTo(BigDecimal.ZERO) <= 0){
            return 0;
        }
        return (int) Math.ceil(money.multiply(perMoneyToPoint).doubleValue());
    }
}
