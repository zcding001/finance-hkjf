/**
 * 日期处理工具类
 * */
var dateUtil = {};
(function () {
    'use strict';
    /**
     * 时间格式化工具
     * @param fmt   日期格式
     */
    Date.prototype.Format = function (fmt) {
        var o = {
            "M+": this.getMonth() + 1, //月份
            "d+": this.getDate(), //日
            "H+": this.getHours(), //小时
            "m+": this.getMinutes(), //分
            "s+": this.getSeconds(), //秒
            "q+": Math.floor((this.getMonth() + 3) / 3), //季度
            "S": this.getMilliseconds() //毫秒
        };
        if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o)
            if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        return fmt;
    };
     /**
     * 返回yyyy-MM-dd格式日期
     * @param date   日期
     */
    dateUtil.date = function (date) {
        if(date<0){
            return '--';
        }
        return new Date(date).Format("yyyy-MM-dd");
    };
    /**
     * 返回yyyy-MM-dd HH:mm:ss格式日期
     * @param date   日期
     */
    dateUtil.dateTime = function (date) {
        if(date<0){
            return '--';
        }
        return new Date(date).Format("yyyy-MM-dd HH:mm:ss");
    };
    /**
     * 返回yyyy-MM-dd HH:mm:ss格式日期
     * @param date     日期
     * @param format   日期格式
     */
    dateUtil.dateByFormat = function (date,format) {
        return new Date(date).Format(format);
    };

    /**
     *js中更改日期
     * y年， m月， d日， h小时， n分钟，s秒
     */
    Date.prototype.add = function (part, value) {
        value *= 1;
        if (isNaN(value)) {
            value = 0;
        }
        switch (part) {
            case "y":
                this.setFullYear(this.getFullYear() + value);
                break;
            case "m":
                this.setMonth(this.getMonth() + value);
                break;
            case "d":
                this.setDate(this.getDate() + value);
                break;
            case "h":
                this.setHours(this.getHours() + value);
                break;
            case "n":
                this.setMinutes(this.getMinutes() + value);
                break;
            case "s":
                this.setSeconds(this.getSeconds() + value);
                break;
            default:

        }
    }

}());