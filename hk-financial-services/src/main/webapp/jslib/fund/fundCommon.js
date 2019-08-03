/**
 * 基金公共js
 * @type {{}}
 */
var fundCommon={};
var isLogin = false;
//获取用户实名或测评标识
var fundAuthFlag = 0; //默认0未登录 1未实名 3实名未测评 2实名已测评 4企业用户(企业账号暂不支持线上预约，如需要，请拨打客服电话：4009009630。)
(function () {
    //获取用户实名或测评标识
    fundCommon.getFundAuthentication = function(callback) {
        $.ajax({
            type : 'POST',
            url : commonUtil.getRequestUrl("/fundIndexController/getFundAuthentication.do"),
            dataType : "json",
            async : false,
            success : function(data) {
                if(data.resStatus == CONSTANTS.SUCCESS){
                    fundAuthFlag = data.resMsg;
                    //fundAuthFlag = 2;
                }
                //加载各列表
                if(validUtil.validNotEmpty(callback) && typeof callback == 'function'){
                    callback(data);
                }
            },
            error : function(request) {fundAuthFlag = 0;}
        });
    }

    //登录跳转
    fundCommon.loginJump = function (utype) {
        utype = fundCommon.initUtype(utype);
        commonUtil.jump("/register/login.html?utype="+utype);
    }

    //注册跳转
    fundCommon.registJump = function (utype) {
        utype = fundCommon.initUtype(utype);
        commonUtil.jump("/fund/regist/register.html?utype="+utype);
    }

    /**
     * 首页和预约页面认证跳转
     * AuthType：认证状态
     * utype:区分基金父类型
     * entrance：区分实名测评的流程图片 1-四步，2-三步（和setStepPic保持一致）
     */
    fundCommon.authenJump = function(AuthType , utype , entrance) {
        utype = fundCommon.initUtype(utype);
        if(entrance == null || entrance == undefined){entrance = 2;}
        if(AuthType == 4){
            alert("企业账号暂不支持线上预约，如需要，请拨打客服电话：4009009630。");
        }else if(AuthType == 0){
            commonUtil.jump("/register/login.html?utype=" + utype);
        }else if(AuthType == 1){
            //实名realName.html
            commonUtil.jump("/fund/regist/realName.html?utype=" + utype + '&entrance=' + entrance);
        }else if(AuthType == 3){
            commonUtil.jump("/fund/regist/riskInvestigation.html?utype=" + utype  + '&entrance=' + entrance);
        }else if(AuthType == 2){
            return true;
        }else{
            alert("系统繁忙，请稍后重试!");
        }
        return false;
    }

    // 首页预约跳转
    fundCommon.advisoryJump = function(id , utype) {
        utype = fundCommon.initUtype(utype);
        /**
         * 实名测评判断前置
         */
        //alert("标识：" + fundAuthFlag);
        if(!fundCommon.authenJump(fundAuthFlag,utype)){
            return false;
        }
        if(utype == 2){
            //海外基金跳转到详情
            commonUtil.jump("/fund/advisory/pefundInfo.html?id=" + id + "&utype=" + utype);
            return false;
        }
        //其他跳转到立即预约页面
        commonUtil.jump("/fund/advisory/pefundYuyue.html?id=" + id + "&utype=" + utype);
    }



    //校验初始化基金父类型utype
    fundCommon.initUtype = function(utype){
        if(utype == undefined || utype == 'undefined' || utype == null){
            var fund_type = containerUtil.get("fund_utype");
            if(fund_type == undefined || fund_type == 'undefined' || utype == null){
                utype = 1;
            }else{
                utype = fund_type;
            }
        }
        return utype;
    }

    //股权基金渲染轮播图
    fundCommon.loadInformations = function (position) {
        var reqData = {position: position}
        ajaxUtil.post("/informationController/findInfomations.do", reqData, function (data) {
            //轮播图渲染
            if (validUtil.validNotEmpty(data.resMsg.carsouleFigureList)) {
                $(".carousel ol").empty();
                $(".carousel-inner").empty();
                if(!validUtil.validNotEmpty(data.resMsg.carsouleFigureList)){
                    //$(".carousel-inner").append('<div class="item active item_width" style="z-index: 10;"><img src="src/img/index/banner-pc.jpg" alt="鸿坤金服banner图" style="height:450px;"></div>');
                    //$(".carousel-indicators").append('<li data-target="#carousel-example-generic" data-slide-to="' + i + '" class="active"></li>');
                    //使用静态页面原有的图片
                }else{
                    for (var i = 0; i <= eval(data.resMsg.carsouleFigureList).length - 1; i++) {
                        if(i == 0){
                            $(".carousel-inner").append('<div class="item active item_width" style="z-index: 10;"><a href="' + eval(data.resMsg.carsouleFigureList)[i].url + '">' + eval(data.resMsg.carsouleFigureList)[i].content + '</a></div>');
                            $(".carousel-indicators").append('<li data-target="#carousel-example-generic" data-slide-to="' + i + '" class="active"></li>');
                        }else{
                            $(".carousel-inner").append('<div class="item item_width" style="z-index: 10;"><a href="' + eval(data.resMsg.carsouleFigureList)[i].url + '">' + eval(data.resMsg.carsouleFigureList)[i].content + '</a></div>');
                            $(".carousel-indicators").append('<li data-target="#carousel-example-generic" data-slide-to="' + i + '"></li>');
                        }
                    }
                    $(".carousel-inner div img").css("height","450px");
                }
            }
        }, null, false);
    };

    fundCommon.guanbizezhao = function() {//关闭弹窗
        $('.bgzz ,.tanchuang').hide();
    }

    fundCommon.xianshizezhao = function() {//显示弹窗
        $('.bgzz ,.tanchuang').show();
    }

    //获取url中的参数
    fundCommon.getUrlParam=function (name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
        var r = window.location.search.substr(1).match(reg);  //匹配目标参数
        if (r != null) return decodeURI(r[2]) ;
        return null; //返回参数值
    }

    /**
     * 基金退出操作
     */
    fundCommon.logout = function(utype){
        ajaxUtil.post("indexController/logout.do", {}, function(data){
            cookieUtil.delCookie(CONSTANTS.SSO_TICKET);
            //清楚缓存的登录信息
            containerUtil.delete(CONSTANTS.LOGIN_REG_USER);
            containerUtil.delete(CONSTANTS.LOGIN_FIN_ACCOUNT);
            containerUtil.delete(CONSTANTS.LOGIN_LAST_TIME);
            containerUtil.delete(CONSTANTS.ACC_INVEST);
            if(utype == 1){
                window.location.href = commonUtil.getRequestUrl("/fund/peFund.html");
            }else if(utype == 2){
                window.location.href = commonUtil.getRequestUrl("/fund/overseasFund.html");
            }else if(utype == 3){
                window.location.href = commonUtil.getRequestUrl("/fund/trustFund.html");
            }
        });
    };

    /**
     * 跳转主页
     */
    fundCommon.jumpIndex = function(utype){
        if(utype == 1){
            window.location.href = commonUtil.getRequestUrl("/fund/peFund.html");
        }else if(utype == 2){
            window.location.href = commonUtil.getRequestUrl("/fund/overseasFund.html");
        }else if(utype == 3){
            window.location.href = commonUtil.getRequestUrl("/fund/trustFund.html");
        }
    };

    /**
     * 初始化公共头部页面导航相关信息
     * logo，首页、介绍、预约等链接
     * pageType : 链接选中类型 1-产品介绍 2-立即预约 其他则主页
     */
    fundCommon.initNavigation = function (utype , pageType) {
        var zlogo = $('.zlogo');//a
        var zlinks = $('.zlinks li');//li
        var zlogin = $('.zlogin a');
        if(utype == 1){
            //私募
            zlogo.attr("href" , "${project_base_path}/fund/peFund.html").children().first().attr("src" , "${project_base_path}/src/img/guquan/logo1.png");
            $(zlinks[0]).children().first().attr("href" , "${project_base_path}/fund/peFund.html").show();
            $(zlinks[1]).children().first().attr("href" , "${project_base_path}/fund/advisory/pefundInfo.html?utype=1").show();
            $(zlinks[2]).children().first().attr("href" , "${project_base_path}/fund/advisory/pefundYuyue.html?utype=1").show();
            //登录注册链接
            $(zlogin[0]).attr("href" , "${project_base_path}/register/login.html?utype=1");
            $(zlogin[1]).attr("href" , "${project_base_path}/fund/regist/register.html?utype=1");
        }else if(utype == 2){
            //海外
            zlogo.attr("href" , "${project_base_path}/fund/overseasFund.html").children().first().attr("src" , "${project_base_path}/src/img/guquan/logo2.png");
            $(zlinks[0]).children().first().attr("href" , "${project_base_path}/fund/overseasFund.html").hide();
            $(zlinks[1]).children().first().attr("href" , "${project_base_path}/fund/advisory/pefundInfo.html?utype=2").hide();
            $(zlinks[2]).children().first().attr("href" , "${project_base_path}/fund/advisory/pefundYuyue.html?utype=2").hide();
            $(zlogin[0]).attr("href" , "${project_base_path}/register/login.html?utype=2");
            $(zlogin[1]).attr("href" , "${project_base_path}/fund/regist/register.html?utype=2");
        }else if(utype == 3){
            //信托
            zlogo.attr("href" , "${project_base_path}/fund/trustFund.html").children().first().attr("src" , "${project_base_path}/src/img/guquan/logo3.png");
            $(zlinks[0]).children().first().attr("href" , "${project_base_path}/fund/trustFund.html").show();
            $(zlinks[1]).children().first().attr("href" , "${project_base_path}/fund/advisory/pefundInfo.html?utype=3").show();
            $(zlinks[2]).children().first().attr("href" , "${project_base_path}/fund/advisory/pefundYuyue.html?utype=3").show();
            $(zlogin[0]).attr("href" , "${project_base_path}/register/login.html?utype=3");
            $(zlogin[1]).attr("href" , "${project_base_path}/fund/regist/register.html?utype=3");
        }

        //链接的选择样式
        zlinks.each(function () {
            $(this).children().removeClass("nav_selected");
        });
        if(pageType == 1){
            $(zlinks[1]).children().first().addClass("nav_selected");
        }else if(pageType == 2){
            $(zlinks[2]).children().first().addClass("nav_selected");
        }else{
            $(zlinks[0]).children().first().addClass("nav_selected");
        }

        //判断是否实名测评
        fundCommon.getFundAuthentication();
        if(fundAuthFlag != 2){
            $(zlinks[0]).children().first().attr("href" , "javascript:void(0);");
            $(zlinks[1]).children().first().attr("href" , "javascript:void(0);");
            $(zlinks[2]).children().first().attr("href" , "javascript:void(0);");
        }

    }

    /**
     * 预约页面姓名和手机号校验
     * @param type 姓名还是手机号
     * @param inputVlaue 输入内容
     * @param errorId 错误提示域id
     * @returns {boolean} 校验结果
     */
    fundCommon.checkTelOrName = function (type ,inputVlaue, errorId){
        var gqReg = /^1[3456789]\d{9}$/; //手机号验证
        var gqUser = /^[\u4E00-\u9FA5A-Za-z·]{1,25}$/; //中英文姓名验证
        var flag = false;
        if(type == "tel"){
            if(gqReg.test(inputVlaue)){
                $("#" + errorId).css({ visibility: 'hidden' });
                flag = true;
            }else{
                $("#" + errorId).css({ visibility: 'inherit' });
                flag = false;
            }
        }else if(type == "name"){
            if(gqUser.test(inputVlaue)){
                $("#" + errorId).css({ visibility: 'hidden' });
                flag = true;
            }else{
                $("#" + errorId).css({ visibility: 'inherit' });
                flag = false;
            }

        }
        return flag;
    }

    /**
     * 预约请求同步返回结果，页面根据结果显示不同弹窗
     * @param inputs 需要提交的域值
     * @returns {*}
     */
    fundCommon.reservationFund = function (inputs){
        if(inputs == null || inputs == undefined){return "error";}
        var result = "success";
        $.ajax({
            type : 'POST',
            url : commonUtil.getRequestUrl("/fundAdvisoryController/reservationFund.do"),
            async : false,
            dataType : "json",
            data :{name : inputs[0].value ,
                tel : inputs[1].value ,
                projectParentType : inputs[2].value ,
                infoIds : inputs[3].value,
                remark : inputs[4].value},
            success : function(data) {
                result = data.resMsg;
            },
            error : function(request) {return "error"}
        });
        return result;
    }


    /**
     * 实名
     */
    fundCommon.regForRealName = function(){
        commonUtil.addFocusBlurEvent();
        validFormUtil.initForm("/userController/realName.do", _realNameCallBack,_realNameBeforeSubmit).resetForm();
    };

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
            //获取utype
            utype = commonUtil.getUrlParam("utype");
            utype = fundCommon.initUtype(utype);
            //获取entrance
            var entrance = commonUtil.getUrlParam("entrance");
            if(entrance == null || entrance == undefined){entrance = 2}
            commonUtil.jump("/fund/regist/riskInvestigation.html?utype="+utype + "&entrance="+entrance);
        }else{
            $("#resErrMsg").text(data.resMsg);
        }
    }

    /**
     * 实名前表单数据处理
     * @returns {boolean}
     * @private
     */
    function _realNameBeforeSubmit(){
        if(!$("#agreeOn").is(".neizhuanl")){
            $("#resErrMsg").text("请选择协议!");
            return false;
        }
    }

    /**
     * 股权注册页注册操作
     */
    fundCommon.reg=function () {
        commonUtil.addFocusBlurEvent();
        _regValidForm = validFormUtil.initForm("/indexController/reg.do", _regCallBack, _regBeforeSubmit).resetForm();
        _validLogin(_regValidForm, "loginTmp");
        _addValidCode("loginTmp");
        $("#agreeOn").attr("checked",'true');
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
            //获取utype
            utype = commonUtil.getUrlParam("utype");
            utype = fundCommon.initUtype(utype);
            commonUtil.jump("/fund/regist/realName.html?entrance=1&utype="+utype);
        }else{
            $("#resErrMsg").text(data.resMsg);
            commonUtil.loadValidateCode($("#validCode"), 2);
        }
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
        return loginFlag;
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

    fundCommon.getSmsData = function(loginId){
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
                smsCodeUtil.getSmsCode({"getDataFn" : fundCommon.getSmsData, "param0" : loginId});
            }
        });
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
        renderUtil.renderElement([data.params.regUser, data.params.finAccount]);
        commonUtil.showOrHideElement();
        //同步登录状态
        ssoUtil.syncState(CONSTANTS.SSO_STATE_RETRY_TIMES, CONSTANTS.SSO_STATE_ONLINE);
    }

    /**
     * 校验上传图片的格式和大小
     * @param fileid
     * @param previewid
     * @returns {boolean}
     */
    fundCommon.preImg = function (fileid, previewid ,type) {
        /**图片的大小要求，不超过5M，单位是KB*/
        var size = "5120";
        /**图片的类型要求（即文件后缀）*/
        var suffix = "jpg,png,bmp,jpeg,JPG,PNG,BMP,JPEG";
        var suffixList ="";
        var msg = "请先上传需要的图片";
        if(type != undefined && type != null){
            msg = "请先上传正确的"+type + "图片";
        }
        $('#error_'+ previewid).remove();
        if(document.getElementById(fileid).files[0] == "undefined" || document.getElementById(fileid).files[0] == null){
            $("#"+previewid).after("<p id = 'error_"+previewid+"' class = 'noticeword' style='padding-left:0px;margin-top: -10px;'>"+ msg +"</p>");
            return false;
        }
        var name = document.getElementById(fileid).files[0].name;
        var curSize = document.getElementById(fileid).files[0].size;
        var curSuffix = name.split(".")[1];
        console.info(document.getElementById(fileid).files[0])
        //1、判断图片大小
        if(curSize>size*1024)
        {
            msg="图片大小不能超过5M";
            $("#"+previewid).after("<p id = 'error_"+previewid+"' class = 'noticeword' style='padding-left:0px;margin-top: -10px;'>"+ msg +"</p>");
            return false;
        }
        //2、判断图片类型
        if(suffix.indexOf(curSuffix)==-1)
        {
            suffixList="图片格式只能以";
            for(var j=0;j<suffix.split(",").length;j++)
            {
                if(j==0)
                {
                    suffixList+="."+suffix.split(",")[j];
                }
                else
                {
                    suffixList+="，."+suffix.split(",")[j];
                }
            }
            suffixList+="结尾"
            $("#"+previewid).after("<p id = 'error_"+ previewid +"' class = 'noticeword' style='padding-left:0px;margin-top: -10px;'>"+ suffixList +"</p>");
            return false;
        }
        $('#error_'+ previewid).remove();
        return true;
    }

    /**
     * 注册实名流程根据不同入口设置分步图片
     * @see 下面判断和authenJump函数保持一致
     * @param entrance 1:4步图；2:3步图
     * @param picClass 两张图片公共class
     */
    fundCommon.setStepPic = function (entrance , picClass) {
        var pics = $("." + picClass);
        if(entrance == null || entrance == undefined) {entrance = 2}
        if(entrance == 1){
            $(pics[0]).show();
            $(pics[1]).hide();
        }else{
            $(pics[1]).show();
            $(pics[0]).hide();
        }
    }

}());

$(function () {

    //首先获取utype
    var utype4Common = commonUtil.getUrlParam("utype");//检查url中是否指定，优先级1
    utype4Common = fundCommon.initUtype(utype4Common);//检查参数中是否指定，优先级2
    //绑定退出操作事件
    //主页移至各自页面、其他共用导航页放置公共头页面
    
    //站内信消息提示
    $("#headInforms").click(function(){
        //if(!commonUtil.isLogin()){commonUtil.sessionTimeOut()};   //未登录具体页面根据utype跳转到相应主页
        var flag = false;
        $('.nav_c li a').each(function() {
            if($(this).hasClass("nav_selected")){
                if($(this).text() == "我的账户"){
                    flag = true;
                }
            }
        });
        if(flag){
            commonUtil.jumpAccountMenu("newsRecord.html");
        }else{
            commonUtil.jumpToAccount("newsRecord.html");
        }
    });
});