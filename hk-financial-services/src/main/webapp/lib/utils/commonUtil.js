/***
 *  工具类
 */
var commonUtil = {};
(function(){
	'use strict';
	
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
	 * 拼接处理请求地址的全路径
	 * @param url 请求地址的url或是uri
	 * @returns {*}
	 */
	commonUtil.getRequestUrl = function(url){
		if(!url.startWith("/") && !url.startWith("http")){
			url = "/" + url;
		}
		if(!url.startWith("http")){
			url = CONSTANTS.BASE_PATH + url;
		}
		return url;
	}

	/**
	 * 页面跳转
	 * @param uri 以/开头
	 * @param blank 是否在新页面打开
	 */
	commonUtil.jump = function(uri, blank){
		var url = commonUtil.getRequestUrl(uri);
		if(blank != undefined){
			window.open(url);
		}else{
			window.location.href = url;
		}
	};

	/**
	 * 跳转到我的账户并打开指定菜单,默认进入我的账户
	 * @param menuUri 我的账户下的菜单uri
	 * @param data {"target" : "菜单地址 非必填，从根路径开始， 如account/xxxx,禁止使用相对路径"， 业务数据}
	 */
	commonUtil.jumpToAccount = function(menuUri, data){
        containerUtil.add(CONSTANTS.CONTAINER_TRANSIENT_MENU_URI_KEY, menuUri);
		if(data != undefined && !commonUtil.isEmpty(data)){
			containerUtil.add(CONSTANTS.CONTAINER_TRANSIENT_KEY, data);
		}
		window.location.href = commonUtil.getRequestUrl("account/actIndex.html");
	};

	/**
	 * 不刷新页面，在我的账户下随意打开任意菜单页面、传递参数
	 * @param menuUri 我的账户下的菜单uri
	 * @param data {"target" : "菜单地址 非必填，从根路径开始， 如account/xxxx,禁止使用相对路径"， 业务数据}
	 */
	commonUtil.jumpAccountMenu = function(menuUri, data){
		if(!commonUtil.isEmpty(data)){
			containerUtil.add(CONSTANTS.CONTAINER_TRANSIENT_KEY, data);
		}
		var target = commonUtil.getRequestUrl("account/" + menuUri);
		$("#includeContent").attr("file", target);
		commonUtil.includeOuterHtml(target);
		commonUtil.setAccountMenuStyle();
	};

	/**
	 * 不刷新页面，在我的账户下随意打开任意菜单页面、传递参数
	 * param存储在浏览器的临时缓冲中， 去除后直接删除临时元素
	 * @param param  {"target" : "菜单地址 必填，从根路径开始， 如account/xxxx,禁止使用相对路径"， 业务数据}
	 */
	commonUtil.openAccountMenu = function(param){
		var data = $.parseJSON(securityUtil.decrypt(param));
		var target = data.target;
		delete data.target;
		containerUtil.add(CONSTANTS.CONTAINER_TRANSIENT_KEY, data);
		$("#includeContent").attr("file", commonUtil.getRequestUrl(target));
		commonUtil.includeOuterHtml(target);
	};

	/**
	 * 设置子菜单展开样式
	 */
	commonUtil.setAccountMenuStyle = function(){
		//折叠所有的菜单
		$(".menu-one > li >ul >li >a").each(function() {
			$(this).parent().parent().slideUp(300);
			$(this).parent().parent().parent().removeClass("menu-show");
		});
		//展开指定菜单
		$(".menu-one > li >ul >li >a").each(function() {
			var href = $(this).attr("oldHref");
			if(commonUtil.endWith($("#includeContent").attr("file"), href)){
				$(this).parent().parent().slideDown(300);
				$(this).parent().parent().parent().addClass("menu-show");
			}
		});
	};

	/**
	 * 获得URL中的参数
	 * @param name
	 * @returns {null}
	 */
	commonUtil.getUrlParam = function(name) {
		var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
		var r = window.location.search.substr(1).match(reg);
		if(r!=null){
			return  unescape(r[2]);
		}
		return null;
	};

	/**
	 * 生成36位的UUID
	 * @returns {string}
	 */
	commonUtil.uuid = function() {
		return (_S4()+ _S4()+"-"+ _S4()+"-"+ _S4()+"-"+ _S4()+"-"+ _S4()+ _S4()+ _S4());
	};

	/**
	 * 加载|刷新验证码
	 * @param obj 	元素对象
	 * @param type 验证码类型 1：随机验证码 2：计算验证码 默认值-1
	 * @returns {String}
	 */
	commonUtil.loadValidateCode = function(obj, type) {
		if(type == null || type == "" || type == 'undefined'){
			type = "1";
		}
		if(obj instanceof jQuery){
			obj.attr("src", CONSTANTS.BASE_PATH + "/code/validateCodeServlet?" + Math.random() + "&type=" + type);
		}else{
			obj.src = CONSTANTS.BASE_PATH + "/code/validateCodeServlet?" + Math.random() + "&type=" + type;
		}
	};

	/**
	 * 通过元素的获得值
	 * @param name 元素name
	 * @returns {*|jQuery}
	 */
	commonUtil.getValueByName = function(name){
		return $("input[name='" + name + "']").val();
	};

	/**
	 *  判断登录状态
	 *  TODO : 必要时可以在服务端校验ticket的有效性  zc.ding  2017-11-23
	 * @returns {boolean} true:online false:offline
	 */
	commonUtil.isLogin = function(){
		if(validUtil.validNotEmpty(cookieUtil.getCookie(CONSTANTS.SSO_TICKET)) && containerUtil.get(CONSTANTS.LOGIN_REG_USER) != undefined){
			var lastLoginTime = {"lastLoginTime" : dateUtil.dateTime(containerUtil.get(CONSTANTS.LOGIN_LAST_TIME))};
			//渲染用户信息、账户信息、上次登录时间
			renderUtil.renderElement([containerUtil.get(CONSTANTS.LOGIN_REG_USER), containerUtil.get(CONSTANTS.LOGIN_FIN_ACCOUNT), lastLoginTime]);
			commonUtil.showOrHideElement();
			commonUtil.loadUnreadWebMsg();
			if($(".nickNameView").text() == ''){
				$(".nickNameView").text("请输入昵称");
			}
			return true;
		}
		return false;
	};

	/**
	 * 登录验证,判断用户是否已经登录，如果没有登录直接跳到登录页
	 */
	commonUtil.needLogin = function(){
		if(!commonUtil.isLogin()){
			window.location.href = commonUtil.getRequestUrl("register/login.html");
			return false;
		}
		return true;
	};

	/**
	 * 加载为读的站内信消息
	 */
	commonUtil.loadUnreadWebMsg = function(){
		ajaxUtil.post("smsController/getUnreadWebMsg.do", {}, function(data){
			if(data.resStatus == CONSTANTS.SUCCESS){
				renderUtil.renderElement(data.params);
				var totalNum = $(".totalNum").text();
				if(totalNum != undefined && Number(totalNum) <=0){
					$("#headInforms").hide();
				}else{
					$("#headInforms").show();
				}
			}
		});
	};

	/**
	 * 引入外部html元素
	 * @param urls 需要加载的指定的html页面
	 */
	commonUtil.includeOuterHtml  = function(urls){
		var obj = $(".include");
		if( obj != undefined && obj != 'undefined'){
			obj.each(function(){
				var flag = false;
				if(validUtil.validNotEmpty(urls)){
					if(urls instanceof Array){
						for(var i in urls){
							if(commonUtil.endWith($(this).attr("file"), urls[i])){
								flag = true;
								break;
							}
						}
					}else{
						if(commonUtil.endWith($(this).attr("file"), urls)){
							flag = true;
						}
					}
				}else{
					flag = true;
				}
				if(flag){
					if(!!$(this).attr("file")) {
						//务必进行临时变量的存储，防止下面调用html方法出现异常
						var $includeObj = $(this);
                        $includeObj.load($(this).attr("file"));
					}
				}
			});
		}
	};

	/**
	 * 添加防止重复提交的TOKEN
	 */
	commonUtil.addSubmitToken = function(){
		var st = cookieUtil.getCookie(CONSTANTS.SUBMIT_TOKEN);
		if(st == null || st == "undefined" || st == ""){
			$.post(CONSTANTS.BASE_PATH + "/indexController/addSubmitToken.do");
		}
	};

	/**
	 * 登录后显示class中含有loginShow的元素，隐藏loginHide的元素
	 * @param shows loginShow 登录后显示
	 * @param hides loginHide 未登录显示，登录后隐藏
	 */
	commonUtil.showOrHideElement = function(shows, hides){
		if(validUtil.validNotEmpty(shows)){
			if(shows instanceof Array){
				for(var i in shows){
					_showElement(shows[i]);
				}
			}else{
				_showElement(shows);
			}
		}else{
			_showElement("loginShow");
		}
		if(validUtil.validNotEmpty(hides)){
			if(hides instanceof Array){
				for(var j in hides){
					_hideElement(hides[j]);
				}
			}else{
				_hideElement(hides);
			}
		}else{
			_hideElement("loginHide");
		}
	};

    /**
     * 登录后显示class中含有accInvest的元素
     * @param type type ==1 具有定期投资权限用户可见元素
     */
	commonUtil.showOrHideAccInvest = function (type) {
		if(type == 1){
            _showElement("accInvest");
            _hideElement("noAccInvest");
		}else{
            _hideElement("accInvest");
            _showElement("noAccInvest");
		}
    }


	/**
	 * 显示showIds和showCls中的指定元素，隐藏hideIds和hideCls中指定的元素
	 * @param showIds id的集合
	 * @param showCls class的集合
	 * @param hideIds id的集合
	 * @param hideCls class的集合
	 * @param turn 翻转，将showIds和showCls中元素隐藏
	 */
	commonUtil.displayElement = function(showIds, showCls, hideIds, hideCls, turn){
		var block = "inline-block";
		var none = "none";
		if(turn != undefined && (turn + "").length > 0){
			block = "none";
			none = "inline-block";
		}
		if(showIds != undefined && showIds.length > 0){
			for(var i in showIds){
				$("#" + showIds[i]).attr("style", "display:" + block);
			}
		}
		if(showCls != undefined && showCls.length > 0){
			for(var i in showCls){
				$("." + showCls[i]).attr("style", "display:" + block);
			}
		}
		if(hideIds != undefined && hideIds.length > 0){
			for(var i in hideIds){
				$("#" + hideIds[i]).attr("style", "display:" + none);
			}
		}
		if(hideCls != undefined && hideCls.length > 0){
			for(var i in hideCls){
				$("." + hideCls[i]).attr("style", "display:" + none);
			}
		}
	};

	/**
	 * 判断source是否以str结尾
	 * @param source
	 * @param str
	 * @returns {boolean}
	 */
	commonUtil.endWith = function(source, str){
		var reg = new RegExp(str+"$");
		return reg.test(source);
	};

	/**
	 * 退出操作
	 */
	commonUtil.logout = function(){
		ajaxUtil.post("indexController/logout.do", {}, function(data){
			_cleanLoginCache();
			window.location.href = commonUtil.getRequestUrl("/index.html");

		});
	};

	/**
	 * session超时
	 */
	commonUtil.sessionTimeOut = function(){
		_cleanLoginCache();
		window.location.href = commonUtil.getRequestUrl("register/login.html");
	}

	/**
	 * 获取url中的参数集合
	 * @param url
	 * @returns
	 */
	commonUtil.getRequestParamByUrl = function (url) {
		if (commonUtil.isEmpty(url)){
			url = window.location.href; //获取url中"?"符后的字串
		}
		var theRequest = {};
		if (url.indexOf("?") != -1) {
			var strs = url.split("?")[1];
			strs = strs.split("&");
			for(var i = 0; i < strs.length; i ++) {
				theRequest[strs[i].split("=")[0]] = unescape(strs[i].split("=")[1]);
			}
		}
		return theRequest;
	};

	/**
	 * 表单input元素添加获得焦点、失去焦点事件
	 * @param formId 表单id
	 * @param arrs	不需要绑定事件的id集合
	 */
	commonUtil.addFocusBlurEvent = function(formId, arrs){
		if(formId == undefined || formId == ''){
			formId = "dataForm";
		}
		var dataForm = $("#" + formId);
		dataForm.find("input").focus(function(){
			$(this).css("color", "#333");
			var val = $(this).val();
			var nullmsg = $(this).attr("nullmsg");
			if(val != "请输入有效值" && nullmsg != undefined && (nullmsg + "").indexOf(val) > -1){
				$(this).val("");
			}
			if($(this).attr("isPasswd")){
				$(this).attr("type", "password");
			}
		});
		dataForm.find("input").blur(function(){
			var val = $(this).val();
			var nullmsg = $(this).attr("nullmsg");
			if(val == "" || val == undefined){
				if(nullmsg != undefined && nullmsg != ''){
					$(this).css("color", "#ccc");
					$(this).val(nullmsg);
				}else{
					$(this).val("请输入有效值");
				}
			}
			if($(this).attr("isPasswd") && (val == "请输入有效值" || (nullmsg != undefined && (nullmsg + "").indexOf(val) > -1))){
				$(this).attr("type", "text");
			}
		});
		if(arrs != undefined){
			for(var i in  arrs){
				$("#" + arrs[i]).unbind();
			}
		}
	};

	/**
	 * 倒计时计数器
	 *
	 * @param destUrl 时间结束后的目的地址
	 * @param countDownId 显示倒计时的id 默认值：countDown
	 * @param num 初始值  默认值 3秒
	 */
	commonUtil.countDown = function(destUrl, countDownId, num){
		countDownId = countDownId == undefined ? 'countDown' : countDownId;
		num = num == undefined ? 3 : num;
		var countDown = num;
		var countDownObj = window.setInterval(function(){
			countDown = countDown - 1;
			if(countDown == 0){
				window.clearInterval(countDownObj);
				commonUtil.jump(destUrl);
			}else{
				$("#" + countDownId).text(countDown);
			}
		}, 1000);
	};

	/**
	 * 验证密码强度
	 * @param pwd
	 */
	commonUtil.pwdStrong = function(pwd){
		var level = 1;
		if (/[1-9]/.test(pwd) || /[a-z]/.test(pwd) || (pwd + "").length < 8 || (/[a-z1-9]/.test(pwd) && (pwd + "").length < 8)) {
			level = 1;
		}
		if((/[a-z1-9]/.test(pwd) && (pwd + "").length >= 8) || (/[A-Za-z1-9]/.test(pwd) && (pwd + "").length < 8)){
			level = 2;
		}
		if((/[A-Za-z1-9]/.test(pwd) && (pwd + "").length >= 8)){
			level = 3;
		}
		containerUtil.set(CONSTANTS.PWD_LEVEL, level);
		return level;
	};

	/**
	 * 显示指定长度的数据信息
	 * @param text				文本内容
	 * @param maxLength			根据长度截取文本内容
	 * @returns
	 */
	commonUtil.getTextByLength =  function(text, maxLength){
		var _maxLength = 10;
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
	 html中使用方法:
	 <div id="timer"  style="font-size:20px">
     剩余天数：
     <span id="timer_d">0</span>天
     <span id="timer_h">0</span>时
     <span id="timer_m">0</span>分
     <span id="timer_s">0</span>秒
     </div>
     *  //时分秒倒计时方法
     */
	commonUtil.timer=function timer(eleId,endTimer){
        var element=document.getElementById(eleId);
        if(element){
            var endTime = endTimer;
           // var endTimeMonth=endTime.getMonth()-1;
            //endTime.setMonth(endTimeMonth);
            var endDate = new Date();
            endDate.setTime(endTimer)
            var ts = endDate - new Date();
            if(ts>0){
                var dd = parseInt(ts / 1000 / 60 / 60 / 24, 10);
                var hh = parseInt(ts / 1000 / 60 / 60 % 24, 10);
                var mm = parseInt(ts / 1000 / 60 % 60, 10);
                var ss = parseInt(ts / 1000 % 60, 10);
                dd = dd<10?("0" + dd):dd;   //天
                hh = hh<10?("0" + hh):hh;   //时
                mm = mm<10?("0" + mm):mm;   //分
                ss = ss<10?("0" + ss):ss;   //秒
                document.getElementById("timer_d"+eleId).innerHTML=dd;
                document.getElementById("timer_h"+eleId).innerHTML=hh;
                document.getElementById("timer_m"+eleId).innerHTML=mm;
                document.getElementById("timer_s"+eleId).innerHTML=ss;
                setTimeout(function(){timer(eleId,endTimer);},1000);
            }else{
                document.getElementById("timer_d"+eleId).innerHTML=0;
                document.getElementById("timer_h"+eleId).innerHTML=0;
                document.getElementById("timer_m"+eleId).innerHTML=0;
                document.getElementById("timer_s"+eleId).innerHTML=0;
            }
        }

    }

	/**
	 * 将网页滚动到指定元素位置，比如页面表单标签校验较多时，方便用户定位到错误信息
	 * @param id 目标元素id
     */
	commonUtil.moveHtml = function (id) {
		var scroll_offset = $("#" + id).offset().top;
		$("body,html").animate({
			scrollTop : scroll_offset
			//让body的scrollTop等于pos的top，就实现了滚动
		}, 500);
	};

    commonUtil.open = function (url) {
		window.open(url);
    };

    //========== public method ============


	// ========= private method ============
	/**
	 * 生成4位随机数
	 * @returns {string}
	 * @private
	 */
	function _S4(){
		return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
	}

	/**
	 * 显示所有class含所有loginShow的元素
	 * @param className
	 * @private
	 */
	function _showElement(className){
		var obj = $("." + className);
		if(obj != undefined && obj != 'undefined'){
			obj.each(function(){
				$(this).removeClass("hide").addClass("show");
			});
		}
	}

	/**
	 * 隐藏所有class含有loginHide的元素
	 * @param className
	 * @private
	 */
	function _hideElement(className){
		var obj = $("." + className);
		if(obj != undefined && obj != 'undefined'){
			obj.each(function(){
				$(this).removeClass("show").addClass("hide");
			});
		}
	}

	/**
	 * 清空登录状态信息
	 * @private
	 */
	function _cleanLoginCache(){
		cookieUtil.delCookie(CONSTANTS.SSO_TICKET);
		//清楚缓存的登录信息
		containerUtil.delete(CONSTANTS.LOGIN_REG_USER);
		containerUtil.delete(CONSTANTS.LOGIN_FIN_ACCOUNT);
		containerUtil.delete(CONSTANTS.LOGIN_LAST_TIME);
        containerUtil.delete(CONSTANTS.ACC_INVEST);
        //同步登录状态
         //      ssoUtil.syncState(CONSTANTS.SSO_STATE_RETRY_TIMES, CONSTANTS.SSO_STATE_OFFLINE);
	}


})();

/*********************************************************************
 * ********************* 页面加载时需要立即执行的函数 ******************
 *********************************************************************/
$(function(){
	//添加submit token
	commonUtil.addSubmitToken();
	//引入外部的html页面
	commonUtil.includeOuterHtml();
});










