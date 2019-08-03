var commonUtil = {};
(function () {
	'use strict';
    commonUtil.INDEX = 'index.html#/';
    commonUtil.SIGN = "#/";
    commonUtil.HIDE_LEFT_MENU = "&lm=0";
    /**
     * 判断参数是否为空(null,undefined,"")
     * @param arg
     * return 为空返回true,否则返回false
     */
	commonUtil.isEmpty = function (arg) {
        if (typeof(arg) === "string"){
            return !arg.trim();
        }
        if (Array.isArray(arg)){
            return !(arg.length > 0);
        }
        //避免输入值为0时返回true
        if (typeof(arg) === "number"){
            return false;
        }
        return !arg;
    };
    /**
     * 判断参数是否不为空(null,undefined,"")
     * @param arg
     * return 不为空返回true,否则返回false
     */
	commonUtil.isNotEmpty = function (arg) {
        return !commonUtil.isEmpty(arg);
    };
    /**
     * 校验是否为数字，且是num的整数倍
     * @param value
     * @returns {Boolean}
     */
    commonUtil.isNumberByValue = function (value) {
        var re = /^[0-9]*[0-9]$/i;//校验是否为数字的正则
		if (re.test(value)){
			return true;
		}
		return false;
    };

    /**
     * 检验是否为至少两位小数的数字
     * @param value
     * @returns {Boolean}
     */
    commonUtil.isLeastTwoDigitNumber = function(value){
        //step 1:判断是否为数字类型
        if (!isNaN(value)){
            //step2:确定为数字之后，确保小数点位数不大于2
            if (commonUtil.isEmpty(value.toString().split(".")[1]) || value.toString().split(".")[1].length <= 2){
                return true;
            }
        }
        return false;
    }
    /**
     * 判断该值是否能整除某个数
     * @param value
     * @param divisor
     * @returns {Boolean}
     */
    commonUtil.isDivideByValue = function(value,divisor) {
    	if (commonUtil.isNumberByValue(value)){
    		return value%divisor != 0 ? false : true;
		}
		return false;
    };
    /**
     * 显示指定长度的数据信息
     * @param text				文本内容
     * @param maxLength			根据长度截取文本内容
     * @returns
     */
    commonUtil.getTextByLength =  function(text, maxLength){
        var _maxLength = 5;
        if(maxLength != undefined){
            _maxLength = maxLength;
        }
        if(text == undefined){
            return text;
        }
        if(text.length > _maxLength ){
            return "<span title='" + text + "'>" + text.substr(0, _maxLength) + "...</span>";
        }
        return text;
    };
    /**
     * 小数点后保留len位数字,默认保留两位
     * @param  value		数字
     * @param  len			保留的位数
     * @returns
     */
    commonUtil.toFixed = function(value, len){
        var s = value + "";
        if(len == undefined){
            len = 3;
        }else{
            len += 1;
        }
        if(s.indexOf(".") > -1){
            return s.substring(0,s.indexOf(".") + len);
        }
        return value;
    };
    /**
     * 小数点后保留len位数字,默认保留两位,并且每3位加","
     * @param value
     * @param len
     * @returns
     */
    commonUtil.toFormatFixed = function(value,len){
		return (parseFloat(commonUtil.toFixed(value,len))).toLocaleString();
	};
    /**
     * 获得请求的URl地址
     * @params type 地址中是否包含项目名称
     * 			1：http(https)://ip:port/
     * 			无 : http(https)://ip:port/xxx/
     * @author zc.ding
     * @date 2016年11月12日
     */
    commonUtil.getUrl = function (type) {
        var prefix = "http://";
        var start = 7;
        var curWwwPath = window.document.location.href;
        if(curWwwPath.indexOf("https://") == 0){
            prefix = "https://";
            start = 8;
        }
        curWwwPath = curWwwPath.substring(start);
        var pathName = window.document.location.pathname;
        var pos = curWwwPath.indexOf(pathName);
        var localhostPath = curWwwPath.substring(0, pos);
        var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
        if(type !=undefined && type === 1){
            return prefix + localhostPath + "/";
        }
        return prefix + (localhostPath + projectName+"/");
    };
    /**
     * 获取url中的参数集合
     * @param url
     * @returns
     */
    commonUtil.getRequestParamByUrl = function (url) {
    	if (commonUtil.isEmpty(url)){
    		url = window.location.href; //获取url中"?"符后的字串
        }
        var theRequest = new Object();
        if (url.indexOf("?") != -1) {
            var strs = url.split("?")[1];
            strs = strs.split("&");
            for(var i = 0; i < strs.length; i ++) {
                if(i == strs.length-1 && url.split("?").length > 2){
                    theRequest[strs[i].split("=")[0]] = unescape(strs[i].split("=")[1]) + url.substring(url.lastIndexOf("?"));
                }else{
                    theRequest[strs[i].split("=")[0]] = unescape(strs[i].split("=")[1]);
                }
            }
        }
        return theRequest;
    };
    /**
     * 根据参数组装其对应的请求地址
     * @param opts 			参数对象，1.target为其跳转到路由中对应的页面,2.除去清除的参数，其他参数均为url上的参数
     * @returns {String}
     */
	commonUtil.getRouteUrl = function (opts) {
        var obj = opts;
        var target = obj.target;
        obj = commonUtil.removeCustomParam(obj);
        var arr = Object.keys(obj);
        var param = "?";
        $.each(arr, function(index, name){
            param += name + "=" + obj[name] + "&";
        });
        if(param == "?"){
            param = "";
        }
        if(param.endWith("&")){
            param = param.substring(0, param.length - 1);
        }
        var url = commonUtil.getUrl();
        if(window.document.location.pathname != "/" && window.document.location.pathname != ""){
            url += commonUtil.INDEX;
        }else{
            url += commonUtil.SIGN;
        }
        return url + target + encodeURI(param);
    };
    /**
     * 删除自定义参数
     * @param obj
     * @returns
     */
	commonUtil.removeCustomParam = function (obj) {
        delete obj.target;
        delete obj.ajaxUrl;
        delete obj.modal;
        delete obj.blank;
        delete obj.fn;
        delete obj.row;
        return obj;
    };
    /**
     * 删除自定义参数
     * @param uri			地址栏后的参数,跳转的目标页target及相关参数
     * @param blank			为1时开启一个新标签页请求，否则在当前页请求
     * @returns
     */
	commonUtil.goPage = function (uri,blank) {
        var url;
        if(window.document.location.pathname != "/" && window.document.location.pathname != ""){
            url = commonUtil.getUrl() + commonUtil.INDEX + uri;
        }else{
            url = commonUtil.getUrl() + commonUtil.SIGN + uri;
        }
        if(blank == 1){
            window.open(url + commonUtil.HIDE_LEFT_MENU);
        }else{
            window.location.href = url;
        }
    };
    /**
     * 创建直接请求的按钮
     * @param data:{
     * 	target:路由地址(必填)
     * 	其他:请求路由地址携带的参数(非必填)
     * }
     * @param msg		按钮的提示信息(必填)
     * @param icon		按钮的图标样式(必填)
     * @param blank		1时开启新页面进行请求，否则当前页面进行请求(非必填)
     * @returns
     */
	commonUtil.createDirectRequestBtn = function (data,msg,icon,blank) {
        if (commonUtil.isEmpty(data.target) || commonUtil.isEmpty(msg) || commonUtil.isEmpty(icon)){
        	throw new Error("commonUtil.createDirectRequestBtn requires param is not satisfy!");
		}
        return '<a href="javascript:void(0)" title="' + msg + '" onClick="commonUtil.directRequest(\''+encryptAndDecryptUtil.encrypt(JSON.stringify(data))+'\', ' + blank + ')" class="fa ' + icon + ' fa-fw hide-underline" style="padding-left: 3px;padding-right: 3px;"></a>';
    };
    /**
     * 发起直接请求
     * @param data:{
     * 	target:路由地址(必填)
     * 	其他:请求路由地址携带的参数(非必填)
     * }
     * @param blank		1时开启新页面进行请求，否则当前页面进行请求
     * @returns
     */
	commonUtil.directRequest = function (data,blank) {
        var obj = $.parseJSON(encryptAndDecryptUtil.decrypt(data));
        if (commonUtil.isEmpty(obj.target)){
            throw new Error("commonUtil.directRequest data.target can not be empty!");
        }
        //判断是否显示提示框，直接跳转
        if(blank == 1){
            window.open(commonUtil.getRouteUrl(obj) + commonUtil.HIDE_LEFT_MENU);
        }else{
            window.location.href = commonUtil.getRouteUrl(obj);
        }
    };
    /**
     * 创建需要再次确认才发起请求的按钮
     * @param data:{
     * 	ajaxUrl:确认后发起的请求地址(必填)
     * 	其他:请求路由地址携带的参数(非必填)
     * }
     * @param msg			按钮提示信息(必填)
     * @param title			确认框标题(必填)
     * @param content		确认框内容(必填)
     * @param icon			按钮图标(必填)
     * @param fn			请求成功后执行的回调函数(非必填)
     * @returns
     */
    commonUtil.createConfirmRequestBtn = function (data,msg,title,content,icon,fn) {
		if (commonUtil.isEmpty(data.ajaxUrl) || commonUtil.isEmpty(msg) || commonUtil.isEmpty(title) || commonUtil.isEmpty(content) || commonUtil.isEmpty(icon)){
			throw new Error("commonUtil.createConfirmRequestBtn requires param is not satisfy!");
		}
        return '<a href="javascript:void(0)" title="' + msg + '" onclick="commonUtil.confirmRequest(\'' + encryptAndDecryptUtil.encrypt(JSON.stringify(data)) + '\',\'' + title + '\', \'' + content + '\',' +fn+ ');" class="fa ' + icon + ' fa-fw hide-underline" style="padding-left: 3px;padding-right: 3px;"></a>';
    };
    /**
     * 请求展示确认框
     * @param data:{
     * 	ajaxUrl:确认后发起的请求地址(必填)
     * 	其他:请求路由地址携带的参数(非必填)
     * }
     * @param title			确认框标题(必填)
     * @param content		确认框内容(必填)
     * @param fn			请求成功后执行的回调函数(非必填)
     * @returns
     */
    commonUtil.confirmRequest = function (data,title,content,fn) {
        var obj = $.parseJSON(encryptAndDecryptUtil.decrypt(data));
        if (commonUtil.isEmpty(obj.ajaxUrl) || commonUtil.isEmpty(title) || commonUtil.isEmpty(content)){
            throw new Error("commonUtil.confirmRequest requires param is not satisfy!");
        }
        var uri = obj.ajaxUrl;
        //给确认窗口赋值
		$("#confirmHeader").html(title);
		$("#confirmBody").html(content);
        $("#modalConfirm").modal('show');
        $("#modalUri").val(uri);
        $("#modalData").val(data);

        obj = commonUtil.removeCustomParam(obj);
        //点击确认按钮操作
        $("#confirmOkBtn").unbind("click").click(function () {
            $.ajax({
                url : $("#modalUri").val(),
                data : obj,
                type : 'POST',
                dataType : 'JSON',
                async : false,
                success : function(result) {
                    $("#modalConfirm").modal('hide');
                    if(result.resStatus == CONFIG.CONSTANTS.SUCCESS_STATE){
                        if(commonUtil.isNotEmpty(fn) && typeof fn === "function"){
                            fn.call();
                        }
                        $("#alertBody").html("操作成功");
                        $("#modalAlert").modal('show');
                        $('#mDataTable').DataTable().search("").draw();
                    }else{
                        //判断是不是参数绑定异常：如：{"resStatus":999,"resMsg":["请填写积分数量"],"params":{}}
                        //只显示第一个信息
                        if (result.resStatus === 999){
                            var message = Array.isArray(result.resMsg) ? result.resMsg[0] : result.resMsg;
                            $("#alertBody").html("操作失败:[" + message + "]");
                            $("#modalAlert").modal('show');
                        }
                    }
                },
                error : function(msg) {
                    $("#modalConfirm").modal('hide');
                    $("#alertBody").html("系统异常:[" + msg + "]");
                    $("#modalAlert").modal('show');
                }
            });
        });
    };
    /**
     * 自定义可输入信息的对话框,使用见userList.html的更新验证码操作
     * @param title             对话框标题(必填)
     * @param content           需要自行的拼接的输入框内容(必填)
     * @param url               对话框点击确认后请求的地址(必填)
     * @param beforeExecuteFn   请求url前执行的函数，可校验自定义输入框值的正确性(非必填)
     * @param afterExecuteFn    请求url成功后执行的函数，可以自定义方法内容，例如成功后跳转(必填)
     * @param ajaxFun           传入制定ajax请求，只有当对ajax有定制要求的时候传入，比如需要上传图片的时候
     * @returns {string}
     */
    commonUtil.createCustomConfirmBox = function (title,content,url,beforeExecuteFn,afterExecuteFn,ajaxFun) {
        if (commonUtil.isEmpty(title) || commonUtil.isEmpty(content) || commonUtil.isEmpty(url) ||
            commonUtil.isEmpty(afterExecuteFn) || typeof afterExecuteFn !== "function"){
            throw new Error("commonUtil.createCustomConfirmBox require param is not satisfy!");
        }
        //拼接文本框提示头
        $("#textHeader").html(title);
        $("#textBody").html(content);
        $("#modalText").modal('show');
        //先取消绑定的click事件，在重新绑定click事件，防止重复绑定click事件
        $("#textOkBtn").unbind("click").click(function(){
            var result = true;
            if(commonUtil.isNotEmpty(beforeExecuteFn) && typeof beforeExecuteFn === "function"){
                result = beforeExecuteFn.call();
            }
            if(result){
                $("#_confirmError").text("");
                if(ajaxFun==null){
                    $.ajax({
                        url : url,
                        type : 'POST',
                        dataType:"json",
                        data : $("#_confirmForm").serialize(),
                        success : function(data) {
                            afterExecuteFn(data);
                        }
                    });
                }else{
                    ajaxFun.call();
                }

            }
        });
    };
    /**
     * 自定义对话框操作成功后通用的提示、操作方法，注意：只适用于操作成功后刷新当前列表页且tableId为mDataTable的table
     * @param data    请求后台返回的数据(必填)
     * @returns
     */
    commonUtil.customConfirmStandardAfterExecuteFn = function (data) {
        if(commonUtil.isEmpty(data)){
            throw new Error("commonUtil.customConfirmStandardAfterExecuteFn require param is not satisfy!");
        }
        $("#modalText").modal('hide');
        if(data.resStatus==CONFIG.CONSTANTS.SUCCESS_STATE){
            $('#mDataTable').DataTable().search("").draw();
            commonUtil.createSimpleNotify("操作成功！", ALERT_SUC);
        }else{
            commonUtil.createSimpleNotify(data.resMsg, ALERT_ERR);
        }
    };
    /**
     * 创建简单的消息提示窗口
     * @param msg    消息提示内容(必填)
     * @param type   消息类型 ALERT_SUC|ALERT_ERR|ALERT_WARN(非必填)，默认为提示
     * @returns
     */
    function _createSimpleNotify(msg, type) {
        var header = "提示";
        if(type == ALERT_SUC){
            header = "<font color='green' font-weight='bold'>正确</font>";
        }else if(type == ALERT_ERR){
            header = "<font color='red' font-weight='bold'>错误</font>";
        }else if(type == ALERT_WARN){
            header = "<font color='#FFD700' font-weight='bold'>警告</font>";
        }
        $("#alertHeader").html(header);
        $("#alertBody").html(msg);
        $("#modalAlert").modal('show');
    };
    /**
     * 创建简单的消息提示窗口
     * @param msg    消息提示内容(非必填)
     * @param type   消息类型 ALERT_SUC|ALERT_ERR|ALERT_WARN(非必填)，默认为提示
     * @returns
     */
    commonUtil.createSimpleNotify = function (msg, type) {
        _createSimpleNotify(msg,type);
    };
    /**
     * 创建消息提示窗口确认后进行跳转
     * @param msg           消息提示内容(非必填)
     * @param type          消息类型 ALERT_SUC|ALERT_ERR|ALERT_WARN(非必填)，默认为提示
     * @param $location
     * @param uri           跳转的路径
     * @param refresh 
     * @returns
     */
    commonUtil.createNotifyAndRedirect = function (msg,type,$location, uri,refresh) {
        if (commonUtil.isEmpty($location) || commonUtil.isEmpty(uri)){
            throw new Error("commonUtil.createNotifyAndRedirect requires param is not satisfy!");
        }
        _createSimpleNotify(msg,type);
        $("#alertOkBtn").unbind("click").click(function(){
            $("#modalAlert").modal('hide');
            $(".modal-backdrop").remove();
            $("body").removeClass("modal-open");
            if(!commonUtil.isEmpty(refresh)){
            	 location.reload();
            }
            $location.path(uri);
            window.location.href = $location.absUrl();
        });
    };
    /**
     * 创建将后台查询出的数据展示在弹出窗上的按钮
     * @param param             请求时传递的参数(非必填)
     * @param title             弹出窗标题(必填)
     * @param url               请求的url地址(必填)
     * @param afterExecuteFn    请求成功执行的函数(必填),例如操作请求回来的数据并进行展示
     * @param msg               按钮的提示信息(必填)
     * @param icon              按钮的图标(必填)
     * @returns
     */
    commonUtil.createGetDetailByUrlOnWindowBtn = function (param,title,url,afterExecuteFn,msg,icon) {
        if (commonUtil.isEmpty(title) || commonUtil.isEmpty(url) || commonUtil.isEmpty(afterExecuteFn) ||
            typeof afterExecuteFn !== "function" || commonUtil.isEmpty(msg) || commonUtil.isEmpty(icon)){
            throw new Error("commonUtil.createShowDetailOnWindowBtn requires param is not satisfy!");
        }
        return '<a href="javascript:void(0)" title="' + msg + '" onClick="commonUtil.getDetailByUrlOnWindow(\''+encryptAndDecryptUtil.encrypt(JSON.stringify(param))+'\', \'' + title + '\', \'' + url + '\', ' + afterExecuteFn + ')" class="fa ' + icon + ' fa-fw hide-underline" style="padding-left: 3px;padding-right: 3px;"></a>';
    };
    /**
     * 发送请求并将请求回来的数据展示在窗口
     * @param param             请求时传递的参数(非必填)
     * @param title             弹出窗标题(必填)
     * @param url               请求的url地址(必填)
     * @param afterExecuteFn    请求成功执行的函数(必填),例如操作请求回来的数据并进行展示
     * @returns
     */
    commonUtil.getDetailByUrlOnWindow = function (param,title,url,afterExecuteFn) {
        if (commonUtil.isEmpty(title) || commonUtil.isEmpty(url) || commonUtil.isEmpty(afterExecuteFn) ||
            typeof afterExecuteFn !== "function"){
            throw new Error("commonUtil.showDetailOnWindow requires param is not satisfy!");
        }
        var opts = $.parseJSON(encryptAndDecryptUtil.decrypt(param));
        $("#detailHeader").html(title);
        $.ajax({
            url : url,
            data : opts,
            type : 'POST',
            dataType : 'JSON',
            async : false,
            success : function(result) {
                afterExecuteFn.call(this, opts, result);
            }
        });
    };
    /**
     * 初始化submitToken
     */
    commonUtil.initSubmitToken = function () {
        var st = cookieUtil.getCookie("submitToken_admin");
        if(st == null || st == "undefined" || st == ""){
            $.ajax({
                type: 'POST',
                url: commonUtil.getUrl() + 'managementLoginController/addSubmitToken',
                dataType: "json",
                success: function(data){
                    // cookieUtil.setCookieWithoutEncode("submitToken_admin",data.params.submitToken);
                },
                error: function(request){}
            });
        }
    };

    commonUtil.showTextForCopy = function (showId,showHtml) {
        var showDiv = document.getElementById(showId);
        showDiv.style.left = event.clientX;
        showDiv.style.top = event.clientY;
        showDiv.style.display = 'block';
        showDiv.innerHTML =showHtml;
    };

    commonUtil.hideTextForCopy = function (showId) {
        var showDiv = document.getElementById(showId);
        showDiv.style.display = 'none';
        showDiv.innerHTML = '';
    };

    commonUtil.copyText = function (showId) {
        var showDiv = document.getElementById(showId);
        window.clipboardData.setData("Text",showDiv.value);
        alert("复制成功!");
    };

    /**
     * 获取卡券详情单位
     * @param type 卡券类型
     */
    commonUtil.getCouponDetailUnit = function (type) {
        //加息券或好友券结尾加%，投资红包结尾加元
        let unit = "";
        if(type == 0 || type == 3){
            unit = "%";
        }
        if (type == 1){
            unit = "元";
        }
        return unit;
    };

    /**
     *  判断登录状态
     *  TODO : 必要时可以在服务端校验ticket的有效性  hh  2018-10-9
     * @returns {boolean} true:online false:offline
     */
    commonUtil.isLogin = function(){
        console.log(containerUtil.get(CONFIG.CONSTANTS.LOGIN_REG_USER));
        if(containerUtil.get(CONFIG.CONSTANTS.LOGIN_REG_USER) != undefined){
            return true;
        }
        return false;
    };

    /**
     * 登录失效弹框提示，并跳转登录页面
     * @param content 弹框内容
     */
    commonUtil.loginOvertime = function (content) {
        $("#alertBody").html(content);
        $("#modalAlert").modal('show');
        $("#alertOkBtn").unbind("click").click(function() {
            $("#modalAlert").modal('hide');
            $(".modal-backdrop").remove();
            $("body").removeClass("modal-open");
            window.location.href = commonUtil.getUrl()+"login.html";
        });
    }

}());
