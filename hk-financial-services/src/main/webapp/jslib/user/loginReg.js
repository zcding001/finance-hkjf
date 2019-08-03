/**
 * Created by dzc on 17.12.8.
 * 登录、注册控制层
 */

var loginRegController = {};
(function() {
    'use strict';

    var loginFlag = false;
    var loginFailMsg = "";
    /**
     * 快速登录
     */
    loginRegController.fastLogin = function(){
        //显示登录页
        _toLogin();
        commonUtil.addFocusBlurEvent();
        //登录操作事件
        var validForm = validFormUtil.initForm("/indexController/fasterLogin.do", _fastLoginCallBack, _loginBeforeSubmit, "dataForm", "loginBtn");
        validForm.resetForm();
        //绑定退出操作事件
        $(".logoutBtn").each(function(){
            $(this).unbind("click").click(commonUtil.logout);
        });
        //判断是不登录的状态
        commonUtil.isLogin();
        $(".toRegistBtn").click(_toRegist);
        $(".toLoginBtn").click(_toLogin);
    };


    /**
     * ticket登录
     */
    loginRegController.ticketLogin = function () {
        //查询登录信息
        $.ajax({
            type : 'POST',
            url : commonUtil.getRequestUrl("/indexController/validTicket.do"),
            dataType : "json",
            async: true,
            success : function(data) {
                if(data.resStatus == CONSTANTS.SUCCESS){
                    console.info("ticket验证登录成功，渲染页面。。。")
                    _fastLoginCallBack(data);
                    loginFlag = true;
                }else{
                    loginFlag = false;
                    loginFailMsg = data.resMsg;
                    console.info("ticket验证登录失败，原因："+data.resMsg);
                }
            },
            error : function(request) {}
        });


    }

    /**
     * 登录页登录
     */
    loginRegController.login = function(){
        commonUtil.addFocusBlurEvent();
        //登录操作事件
        validFormUtil.initForm("/indexController/login.do", _loginCallBack, _loginBeforeSubmit);
        //设置记住的登录名
        var login = cookieUtil.getCookie("login");
        if(validUtil.validNotEmpty(login)){
            $("#rememberMe").attr('checked','true');
            $("#loginTmp").val(login);
            $("#loginTmp").css("color", "#333");
        }
        //添加验证码图片
        commonUtil.loadValidateCode($("#validCode"), 1);
    };

    var _fastRregValidForm;
    var _regValidForm;
    /**
     * 快速注册
     */
    loginRegController.fastReg = function () {
        commonUtil.addFocusBlurEvent("fastRegForm");
        _fastRregValidForm = validFormUtil.initForm("/indexController/reg.do", _fastRegCallBack, _fastRegBeforeSubmit, "fastRegForm", "regSubmitBtn");
        _addValidCode("regLoginTmp");
        $("#registBtn").click(function () {
            if (_fastRregValidForm.check(false, $("#regLoginTmp")) && _fastRregValidForm.check(false, $("#regPasswdTmp"))) {
                if(!loginFlag){
                    $("#resErrMsg").text(loginFailMsg);
                    return;
                }
                _registImediate();
            }
        });
        _validLogin(_fastRregValidForm, "regLoginTmp");
        $("#backToReg").click(function () {
            _toRegist();
        });

        $("#hkjfAgreement").unbind("click").click(function(){
        	commonUtil.includeOuterHtml("registHkjfAgreement.html");
            $("#registHkjfAgreement").modal('show');
        });
        $("#investAgreement").unbind("click").click(function(){
        	commonUtil.includeOuterHtml("registInvestAgreement.html");
        	$("#registInvestAgreement").modal('show');
        });
        $("#toFxinvest").unbind("click").click(function () {
            commonUtil.includeOuterHtml("fxinvest.html");
            $("#fxinvest").modal('show');
        })
        $("#riskNotify").unbind("click").click(function () {
            commonUtil.includeOuterHtml("registRiskNotify.html");
            $("#registRiskNotify").modal('show');
        })
    };

    /**
     * 注册页注册操作
     */
    loginRegController.reg = function(){
    	$("#commendNo").val(commonUtil.getUrlParam("recno"));
        commonUtil.addFocusBlurEvent();
        //保存url中的推广码
        var extenSource = commonUtil.getUrlParam("extenSource");
        if(validUtil.validNotEmpty(extenSource)){
            $("#extenSource").val(extenSource);
        }
        _regValidForm = validFormUtil.initForm("/indexController/reg.do", _regCallBack, _regBeforeSubmit).resetForm();
        var reco = commonUtil.getUrlParam("recno");
        if(reco != null || reco != undefined){
            $("#commendNo").val(commonUtil.getUrlParam("recno"));
            $("#commendNo").attr("readonly","readonly");
            $("#isByInviteUrl").val(true);
        }

        _validLogin(_regValidForm, "loginTmp");
        _addValidCode("loginTmp");
        //添加协议提示框
        $("#hkjfAgreement").unbind("click").click(function(){
            commonUtil.includeOuterHtml("registHkjfAgreement.html");
            $("#registHkjfAgreement").modal('show');
        });
        //投资风险告知书提示框
        $("#riskNotify").unbind("click").click(function(){
            commonUtil.includeOuterHtml("registRiskNotify.html");
            $("#registRiskNotify").modal('show');
        });
        //定向投资认证提示框
        $("#toFxinvest").unbind("click").click(function () {
            commonUtil.includeOuterHtml("fxinvest.html");
            $("#fxinvest").modal('show');
        })
        $("#registerbody").removeAttr("style");
        $("#agreeOn").attr("checked",'true');
    };

    /**
     * 实名
     */
    loginRegController.regForRealName = function(){
        commonUtil.addFocusBlurEvent();
        validFormUtil.initForm("/userController/realName.do", _realNameCallBack).resetForm();
    };

    loginRegController.getSmsData = function(loginId){
       return {"login" : $("#" + loginId).val(),"calcCode" : $("#calcCode").val(), "type" : 1};
    };
    /**
     * 添加图片验证码几短信验证码事件
     * @param loginId
     * @private
     */
    function _addValidCode(loginId){
        //添加验证码图片
        commonUtil.loadValidateCode($("#validCode"), 2);
        //获取验证码时间
        $("#smsCodeBtn").click(function(){
            $("#resErrMsg").text("");
            if(!validUtil.validNotEmpty($("#calcCode").val())){
                $("#resErrMsg").text("请输入计算结果");
            }else{
                smsCodeUtil.getSmsCode({"getDataFn" : loginRegController.getSmsData, "param0" : loginId});
            }
        });
    }

    /**
     * 快速注册回调函数
     * @param data
     * @private
     */
    function _fastRegCallBack(data){
        if(data.resStatus == CONSTANTS.SUCCESS){
            commonUtil.includeOuterHtml("fastLoginReg.html");
            //缓存登录信息
            _cacheLoginData(data);
            commonUtil.includeOuterHtml("header.html");
        }else{
            $("#resErrMsg").text(data.resMsg);
            commonUtil.loadValidateCode($("#validCode"), 2);
        }
    }

    /**
     * 注册回调函数
     * @param data
     * @private
     */
    function _regCallBack(data){
        if(data.resStatus == CONSTANTS.SUCCESS){
            //缓存登录信息
            _cacheLoginData(data);
            commonUtil.jump("/register/register_1.html");
        }else{
            $("#resErrMsg").text(data.resMsg);
            commonUtil.loadValidateCode($("#validCode"), 2);
            if($("#nickName").val() == ""){
                $("#nickName").val("请输入一个昵称");
            }
            if($("#commendNo").val() == ""){
                $("#commendNo").val("请输入您的推荐码(推荐人手机号后8位)");
            }
        }
        // 保存注册时的投资定向合格者测评
        // saveAnswer(containerUtil.get(CONSTANTS.INVEST_ANSWER));
        // 清除测评信息
        containerUtil.delete(CONSTANTS.INVEST_ANSWER);
    }

    /**
     * 注册前表单数据处理
     * @returns {boolean}
     * @private
     */
    function _regBeforeSubmit(){
        if(!$("#agreeOn").is(":checked")){
            $("#resErrMsg").text("请选择协议!");
            return false;
        }
        if(!loginFlag){
            $("#resErrMsg").text(loginFailMsg);
            return false;
        }
        $("#passwd").val(rsaUtil.encryptData($("#passwdTmp").val()));
        commonUtil.pwdStrong($("#passwdTmp").val());
        $("#login").val($("#loginTmp").val());

        if($("#nickName").val() == "请输入一个昵称"){
            $("#nickName").val("");
        }
        if($("#commendNo").val().indexOf("请输入") > -1){
           $("#commendNo").val("");
        }
        return loginFlag;
    }

    /**
     * 快速注册提交前数据处理
     * @returns {boolean}
     * @private
     */
    function _fastRegBeforeSubmit(){
        if(!$("#agreeOn").is(":checked")){
            $("#resErrMsg").text("请选择协议!");
            return false;
        }
        $("#regPasswd").val(rsaUtil.encryptData($("#regPasswdTmp").val()));
        commonUtil.pwdStrong($("#regPasswdTmp").val());
        $("#regLogin").val($("#regLoginTmp").val());
        if($("#commendNo").val().indexOf("请输入") > -1){
            $("#commendNo").val("");
        }
        return loginFlag;
    }

    /**
     * 实名操作回调 
     * @param data
     * @private
     */
    function _realNameCallBack(data){
        if(data.resStatus == CONSTANTS.SUCCESS){
            var regUser = containerUtil.get(CONSTANTS.LOGIN_REG_USER);
            regUser["identify"] = 1;
            containerUtil.set(CONSTANTS.LOGIN_REG_USER, regUser);
            commonUtil.jump("/common/registSuccess.html");
        }else{
            $("#resErrMsg").text(data.resMsg);
        }
    }

    /**
     * 显示登录页
     */
    function _toLogin() {
        $(".resErrMsg0").attr("id", "resErrMsg");
        $(".resErrMsg1").removeAttr("id");
        $(".resErrMsg2").removeAttr("id");
        $(".box-front").css({
            "transform": "rotateY(180deg)",
            "-webkit-transform": "rotateY(180deg)"
        });
        $(".box-back").css({
            "transform": "rotateY(0deg)",
            "-webkit-transform": "rotateY(0deg)"
        });
        $(".box-regist").css({
            "transform": "rotateY(90deg)",
            "-webkit-transform": "rotateY(90deg)"
        });
    };

    /**
     * 去注册页
     */
    function _toRegist() {
        $(".resErrMsg0").removeAttr("id");
        $(".resErrMsg1").attr("id", "resErrMsg");
        $(".resErrMsg2").removeAttr("id");
        $(".box-back").css({
            "transform": "rotateY(180deg)",
            "-webkit-transform": "rotateY(180deg)"
        });
        $(".box-front").css({
            "transform": "rotateY(0deg)",
            "-webkit-transform": "rotateY(0deg)"
        });
        $(".box-regist").css({
            "transform": "rotateY(90deg)",
            "-webkit-transform": "rotateY(90deg)"
        });
    };

    /**
     * 显示注册详细内容
     */
    function _registImediate() {
        $(".resErrMsg0").removeAttr("id");
        $(".resErrMsg1").removeAttr("id");
        $(".resErrMsg2").attr("id", "resErrMsg");
        $(".box-regist").css({
            "transform": "rotateY(0deg)",
            "-webkit-transform": "rotateY(0deg)"
        });
        $(".box-back").css({
            "transform": "rotateY(90deg)",
            "-webkit-transform": "rotateY(90deg)"
        });
        $(".box-front").css({
            "transform": "rotateY(180deg)",
            "-webkit-transform": "rotateY(180deg)"
        });
    };

    function _skipage(origin, target) {
        $("#" + origin).css({
            "transform": "rotateY(180deg)",
            "-webkit-transform": "rotateY(180deg)"
        }).hide();
        $("#" + target).show().css({
            "transform": "rotateY(0deg)",
            "-webkit-transform": "rotateY(0deg)"
        });
    };

    /**
     * 处理表单提交后的响应结果
     * @param data
     */
    function _fastLoginCallBack(data){
        if(data.resStatus == CONSTANTS.SUCCESS){
            _cacheLoginData(data);
            // window.location.href = commonUtil.getRequestUrl("index.html");

            // 进行部分刷新 header.html中无法获取containerUtil 中缓存的值
            commonUtil.includeOuterHtml("header.html");
            var accInvest = data.params.accInvest;
            var overseaInvest = data.params.overseaInvest;
            if( accInvest == 1 && overseaInvest == 0){
            	commonUtil.includeOuterHtml("bannerPic.html");
                commonUtil.includeOuterHtml("post.html");
                commonUtil.includeOuterHtml("bidInfoList.html");
                commonUtil.includeOuterHtml("information.html");
            }
            commonUtil.showOrHideAccInvest(accInvest);
            indexBids.initIndexBidInfo();
        }else{
            $("#resErrMsg").text(data.resMsg);
        }
    }

    /**
     * 处理表单提交后的响应结果
     * @param data
     */
    function _loginCallBack(data){
        if(data.resStatus == CONSTANTS.SUCCESS){
            _cacheLoginData(data);
            window.location.href = commonUtil.getRequestUrl("index.html");

        }else{
            $("#resErrMsg").text(data.resMsg);
            commonUtil.loadValidateCode($("#validCode"), 1);
        }
    }

    /**
     * 异步验证手机号是否被占用
     * @param loginId
     * @private
     */
    function _validLogin(validForm, loginId){
        $("#" + loginId).blur(function(){
            if(validForm.check(false, $("#" + loginId))){
                loginFlag = false;
                $.ajax({
                    type : 'POST',
                    url : commonUtil.getRequestUrl("/indexController/validLogin.do?login=" + $("#" + loginId).val()),
                    dataType : "json",
                    success : function(data) {
                        if(data.resStatus == CONSTANTS.SUCCESS){
                            loginFlag = true;
                        }else{
                            loginFlag = false;
                            loginFailMsg = data.resMsg;
                            $("#resErrMsg").text(data.resMsg);
                        }
                    },
                    error : function(request) {}
                });
            }
        });
    }

    /**
     * 表单条件前函数处理
     * @returns {boolean}
     */
    function _loginBeforeSubmit(){
        // var arr = rsaUtil.loadPublicKey();
        // $("#passwd").val(rsaUtil.encrypt(arr, $("#passwdTmp").val()));
        $("#passwd").val(rsaUtil.encryptData($("#passwdTmp").val()));
        commonUtil.pwdStrong($("#passwdTmp").val());
        $("#login").val($("#loginTmp").val());
        var rememberMe = $("#rememberMe");
        if(rememberMe){
            if(rememberMe.is(":checked")){
                cookieUtil.setCookie("login", $("#login").val());
            }else{
                cookieUtil.delCookie("login");
            }
        }
        return true;
    }


    // 我要投资跳转
    loginRegController.toInvest = function () {
        var _accInvest = containerUtil.get(CONSTANTS.ACC_INVEST);
        $("#toInvestBtn").click(function () {
            if (_accInvest != undefined && _accInvest == 1) {
                commonUtil.jump("/invest/investList.html");
            } else {
                commonUtil.jump("/index.html");
            }
        })
    }

    /**
     * 存储登录信息
     * @param data
     * @private
     */
    function _cacheLoginData(data){
        //缓存登录信息
        containerUtil.set(CONSTANTS.LOGIN_REG_USER, data.params.regUser);
        containerUtil.set(CONSTANTS.LOGIN_FIN_ACCOUNT, data.params.finAccount);
        containerUtil.set(CONSTANTS.LOGIN_LAST_TIME, data.params.lastLoginTime);
        containerUtil.set(CONSTANTS.ACC_INVEST,data.params.accInvest);
        containerUtil.set(CONSTANTS.HOUSE_INVEST,data.params.houseInvest);
        containerUtil.set(CONSTANTS.OVERSEA_INVEST,data.params.overseaInvest);
        renderUtil.renderElement([data.params.regUser, data.params.finAccount]);
        commonUtil.showOrHideElement();
        //同步登录状态
        ssoUtil.syncState(CONSTANTS.SSO_STATE_RETRY_TIMES, CONSTANTS.SSO_STATE_ONLINE);
    }

})();