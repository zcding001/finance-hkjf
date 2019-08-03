/**
 * Created by dzc on 17.12.1.
 * 找回密码
 */

var passwdController = {};
(function(){
    'use strict';

    /**
     * 找回密码第1步-验证手机号
     */
    passwdController.stepOne = function(){
        commonUtil.addFocusBlurEvent();
        validFormUtil.initForm("/indexController/forgotPasswdStep1.do", _stepOneCallBack);
        //添加验证码图片
        commonUtil.loadValidateCode($("#validCode"), 1);
    };

    /**
     * 找回密码第2步-验证手机短信验证码
     */
    passwdController.stepTwo = function(){
        commonUtil.addFocusBlurEvent();
        var loginView = containerUtil.get(CONSTANTS.FIND_PASSWD_LOGIN);
        if(loginView != undefined && loginView.length > 0){
            $("#loginView").text(loginView.substring(0, 6) + "****" + loginView.substring(10));
            $("#login").val(loginView);
        }
        validFormUtil.initForm("/indexController/forgotPasswdStep2.do", _stepTwoCallBack);
        //添加验证码图片
        commonUtil.loadValidateCode($("#validCode"), 2);
        //获取验证码时间
        $("#smsCodeBtn").click(function(){
            if(!validUtil.validNotEmpty($("#calcCode").val())){
                $("#resErrMsg").text("请输入计算结果");
            }else{
                smsCodeUtil.getSmsCode({"getDataFn" : passwdController.getSmsData, "param0" : "login"});
            }
        });
    };

    /**
     * 加载验证码需要的前提数据
     * @returns {{login: *, calcCode: (*|jQuery)}}
     */
    passwdController.getSmsData = function(){
        return {"login" : containerUtil.get(CONSTANTS.FIND_PASSWD_LOGIN),"calcCode" : $("#calcCode").val()};
    };

    /**
     * 找回密码第3步-重置密码
     */
    passwdController.stepThree = function(){
        commonUtil.addFocusBlurEvent();
        $("#login").val(containerUtil.get(CONSTANTS.FIND_PASSWD_LOGIN) == undefined ? "" : containerUtil.get(CONSTANTS.FIND_PASSWD_LOGIN));
        $("#smsCode").val(containerUtil.get(CONSTANTS.FIND_PASSWD_SMSCODE) == undefined ? "" : containerUtil.get(CONSTANTS.FIND_PASSWD_SMSCODE));
        validFormUtil.initForm("/indexController/forgotPasswdStep3.do", _stepThreeCallBack, _stepThreeBeforeSubmit);
        //添加验证码图片
        commonUtil.loadValidateCode($("#validCode"), 1);
    };


    function _stepOneCallBack(data){
        if(data.resStatus == CONSTANTS.SUCCESS){
            containerUtil.set(CONSTANTS.FIND_PASSWD_LOGIN, $("#login").val());
            commonUtil.jump("/register/password_1.html");
        }else{
            $("#resErrMsg").text(data.resMsg);
        }
    }

    function _stepTwoCallBack(data){
        if(data.resStatus == CONSTANTS.SUCCESS){
            containerUtil.set(CONSTANTS.FIND_PASSWD_SMSCODE, $("#smsCode").val());
            commonUtil.jump("/register/password_2.html");
        }else{
            $("#resErrMsg").text(data.resMsg);
            commonUtil.loadValidateCode($("#validCode"), 2);
        }
    }

    function _stepThreeCallBack(data){
        if(data.resStatus == CONSTANTS.SUCCESS){
            containerUtil.delete(CONSTANTS.FIND_PASSWD_LOGIN);
            containerUtil.delete(CONSTANTS.FIND_PASSWD_SMSCODE);
            commonUtil.jump("/register/login.html");
        }else{
            $("#resErrMsg").text(data.resMsg);
            commonUtil.loadValidateCode($("#validCode"), 1);
        }
    }

    function _stepThreeBeforeSubmit(){
        $("#passwd").val(rsaUtil.encryptData($("#pwd1").val()));
        $("#rePasswd").val(rsaUtil.encryptData($("#pwd2").val()));
        commonUtil.pwdStrong($("#pwd1").val());
        return true;
    }
})();

