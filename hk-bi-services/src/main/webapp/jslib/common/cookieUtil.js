var cookieUtil = {};
(function () {
    'use strict';
    /**
     * 根据name获取cookie中的值
     * @param name   cookie中的key
     */
    cookieUtil.getCookie = function (name) {
        var arr,reg = new RegExp("(^| )"+name+"=([^;]*)(;|$)");
        if(arr = document.cookie.match(reg))
            return decodeURI(arr[2]);
        else
            return null;
    };
    /**
     * 将值放到cookie中,默认过期时间为一个月
     * @param name   cookie中的key
     * @param value  cookie中key对应的值
     */
    cookieUtil.setCookie = function (name,value) {
        var Days = 30;
        var exp = new Date();
        exp.setTime(exp.getTime() + Days*24*60*60*1000);
        document.cookie = name + "=" + encodeURI(value) + ";expires=" + exp.toGMTString();
    };
    /**
     * 浏览器关闭，设置的cookie值被清空
     * @param name   cookie中的key
     * @param value  cookie中key对应的值
     */
    cookieUtil.setCookieWithoutExpires = function (name,value) {
        document.cookie = name + "=" + encodeURI(value);
    };
    /**
     * 将值放到cookie中，不对value进行编码处理
     * @param name   cookie中的key
     * @param value  cookie中key对应的值
     */
    cookieUtil.setCookieWithoutEncode = function (name,value) {
        document.cookie = name + "=" + value;
    };
    /**
     * 根据name删除cookie中的值
     * @param name   cookie中的key
     */
    cookieUtil.delCookie = function (name) {
        var exp = new Date();
        exp.setTime(exp.getTime() - 1);
        var value = cookieUtil.getCookie(name);
        if(value != null){
            document.cookie = name + "=" + encodeURI(value) + ";expires=" + exp.toGMTString();
        }
    };
}());