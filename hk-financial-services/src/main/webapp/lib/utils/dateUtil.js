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
        return new Date(date).Format("yyyy-MM-dd");
    };
    /**
     * 返回yyyy-MM-dd HH:mm:ss格式日期
     * @param date   日期
     */
    dateUtil.dateTime = function (date) {
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
     * 获得当前时间，默认返回yyyy-MM-dd HH:mm:ss
     * @param format 时间格式
     */
    dateUtil.currDate = function(format){
        if(format == undefined || format == ''){
            format = "yyyy-MM-dd HH:mm:ss";
        }
        return dateUtil.dateByFormat(new Date(), format);
    };

    /**
     * 对当前时间月份+-操作， 默认对当前时间进行月份的操作
     * @param months
     * @param date
     * @returns {*}
     */
    dateUtil.addMonth = function(months, date){
        if(date == undefined || date == ''){
            date = new Date();
        }
        date.setMonth(date.getMonth() + months);
        return date;
    };

    /**
     * 对当前时间天+-操作， 默认对当前时间进行天的操作
     * @param days
     * @param date
     * @returns {*}
     */
    dateUtil.addDay = function(days,date){
        if(date == undefined || date == ''){
            date = new Date();
        }
        date.setDate(date.getDate() + days);
        return date;
    };
    /**
     * 创建倒计时时间
     * @param totalTimes 总的倒计时时间
     * @param dayId 显示天数
     * @param hourId 显示小时
     * @param minuteId 显示分钟
     * @param secondId 显示秒
     */
    dateUtil.countDown = function(totalTimes, dayId, hourId, minuteId, secondId){
        if(dayId == undefined || dayId == ''){
            dayId = "day_show";
        }
        if(hourId == undefined || hourId == ''){
            hourId = "hour_show";
        }
        if(minuteId == undefined || minuteId == ''){
            minuteId = "minute_show";
        }
        if(secondId == undefined || secondId == ''){
            secondId = "second_show";
        }
        window.setInterval(function(){
            var day=0,
                hour=0,
                minute=0,
                second=0;//时间默认值
            if(totalTimes > 0){
                day = Math.floor(totalTimes / (60 * 60 * 24));
                hour = Math.floor(totalTimes / (60 * 60)) - (day * 24);
                minute = Math.floor(totalTimes / 60) - (day * 24 * 60) - (hour * 60);
                second = Math.floor(totalTimes) - (day * 24 * 60 * 60) - (hour * 60 * 60) - (minute * 60);
            }
            if (minute <= 9) minute = '0' + minute;
            if (second <= 9) second = '0' + second;
            $('#' + dayId).html(day+"天");
            $('#' + hourId).html('<s id="h"></s>'+hour+'时');
            $('#' + minuteId).html('<s></s>'+minute+'分');
            $('#' + secondId).html('<s></s>'+second+'秒');
            totalTimes--;
        }, 1000);
    }
}());