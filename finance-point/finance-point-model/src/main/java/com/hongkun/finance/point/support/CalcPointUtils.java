package com.hongkun.finance.point.support;

/**
 * @Description : 积分模块计算相关的公式
 * @Project : finance
 * @Program Name  : com.hongkun.finance.point.support.CalcPointUtil
 * @Author : zhongpingtang@yiruntz.com 唐忠平
 */
public class CalcPointUtils {

    /**
     * 投资获得积分计算
     *
     * @param investAtmTemp 投资金额
     * @param termvalue 投资期限
     * @param termunit 投资期限类型 1年，2月，3天
     * @param baseNum 计算积分基数
     * @return 计算所得收益金额 公式：投资金额*月数/1200
     */
    public static int calculateInvestPoint(double investAtmTemp, int termvalue, int termunit,int baseNum) {
        int investAtm = (int) investAtmTemp;
        if (termunit == 1) {
            return investAtm * termvalue * 12 / baseNum;
        } else if (termunit == 2) {
            return investAtm * termvalue / baseNum;
        } else {
            return (investAtm * termvalue / 30) / baseNum;
        }
    }

}
