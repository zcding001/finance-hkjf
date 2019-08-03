/**
 * Created by dzc on 17.12.7.
 * 短信验证码倒计时工具类
 */

var smsCodeUtil = {};
(function(){
    var _countDownObj;
    var _countDown = CONSTANTS.TIME_OUT;
    var _smsCodeBtnId;
    var _dialogId;
    var _data = "";
    'use strict';

    /**
     * 获得短信验证码
     * @param data ajax需要提交到后台的数据
     * @param smsCodeBtnId 获取验证码按钮
     * @param dialogId 显示获得验证码提示框
     */
    smsCodeUtil.getSmsCode = function(data, smsCodeBtnId, dialogId){
        _data = data;
        _smsCodeBtnId = smsCodeBtnId;
        _dialogId = dialogId;
        if(!validUtil.validNotEmpty(smsCodeBtnId)){
            _smsCodeBtnId = "smsCodeBtn";
        }
        if(!validUtil.validNotEmpty(_dialogId)){
            _dialogId = "dialog";
        }
        _doGetSmsCode();
    };

    /**
     * 获取验证码具体操作
     * @private
     */
    function _doGetSmsCode(){
        var params = {};
        if(_data != undefined && _data != ''){
            params = _data.getDataFn.call(undefined, _data.param0);
        }
        ajaxUtil.post("/indexController/sendSmsCodeWithCode.do", params, function(data){
            if(data.resStatus == CONSTANTS.SUCCESS){
                $("#" + _dialogId).modal('show');
                $("#" + _smsCodeBtnId).html("重新获取(" + _countDown + ")");
                _countDownObj = window.setInterval(_chgCountDown, 1000);
            }else{
                $("#resErrMsg").text(data.resMsg);
            }
        });
    }

    /**
     * 计数器
     * @private
     */
    function _chgCountDown(){
        _countDown = _countDown - 1;
        var getSmsCodeObj = $("#" + _smsCodeBtnId);
        if(_countDown == 0){
            getSmsCodeObj.html("重新获取");
            _countDown = CONSTANTS.TIME_OUT;
            getSmsCodeObj.click(_doGetSmsCode);
            if(_countDownObj != undefined && _countDownObj != ''){
                window.clearInterval(_countDownObj);
            }
        }else{
            getSmsCodeObj.unbind();
            getSmsCodeObj.html("重新获取(" + _countDown +")");
        }
    }
})();
