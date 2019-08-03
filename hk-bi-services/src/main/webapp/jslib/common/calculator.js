/**
 * 本集合定义一些需要数学计算的常用方法
 *
 */
var calculator = {};
(function () {
    'use strict';
    /**
     * 返回本息
     * @param capital   本金
     * @param rate      年化率
     * @param time      时间
     */
    calculator.culInterests = function (capital,rate,time) {
        return capital + ((capital*(rate/100))/12)*time;
    }
}());