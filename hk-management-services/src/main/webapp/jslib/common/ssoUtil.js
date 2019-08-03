/**
 * Created by dzc on 17.11.23.
 * SSO单点登录工具类
 */

var ssoUtil = {};
(function(){
    'use strict';
    /**
     * 同步登录状态
     * @param times 同步次数
     * @param state 同步状态
     */
    ssoUtil.syncState = function(times, state){
        if(commonUtil.isLogin()){
            $.ajax({
                type: "POST",
                url: commonUtil.getUrl() + 'managementLoginController/syncStateList',
                dataType : "json",
                success: function(data) {
                    if(data.resStatus == CONFIG.CONSTANTS.SUCCESS_STATE){
                        var urls = data.resMsg;
                        var ticket = cookieUtil.getCookie(CONFIG.CONSTANTS.SSO_TICKET);
                        console.log("取得ticket,并且同步到外部地址" + ticket);
                        for(var i in urls){
                            _doSyncState(times, ticket, state, urls[i]);
                        }
                    }
                },
                error: function() {
                    console.info("ERROR:同步登录状态失败!");
                }
            });
        }
    };
    //========== public method ============
    // ========= private method ============
    /**
     * 执行登录状态同步操作
     * @param times 重试次数
     * @param ticket 登录状态的Token
     * @param state 登录状态
     * @param url 状态同步地址
     * @private
     */
    function _doSyncState(times, ticket, state, url){
        console.info("失败次数：" + times);
        $.ajax({
            type: "POST",
            jsonp: "callback",
            jsonpCallback: "jsonpcallback",
            url: url,
            async: false,
            data:{state : state, ticket : ticket},
            dataType:'JSONP',
            success: function(data) {},
            error: function() {
                if(times > 0){
                    //3s后重试同步状态操作
                    setTimeout(_doSyncState(times - 1, ticket, state, url), 5000);
                }
            }
        });
    }
})();