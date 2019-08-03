/***
 *  工具类
 */
var lotteryActivityUtil = {};
(function(){
	'use strict';
	
	 /**
     * 判断参数是否为空(null,undefined,"")
     * @param arg
     * return 为空返回true,否则返回false
     */
	lotteryActivityUtil.isEmpty = function (arg) {
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
	lotteryActivityUtil.getRequestUrl = function(url){
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
	lotteryActivityUtil.jump = function(uri, blank){
		var url = lotteryActivityUtil.getRequestUrl(uri);
		if(blank != undefined){
			window.open(url);
		}else{
			window.location.href = url;
		}
	};


	/**
	 * 获得URL中的参数
	 * @param name
	 * @returns {null}
	 */
	lotteryActivityUtil.getUrlParam = function(name) {
		var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
		var r = window.location.search.substr(1).match(reg);
		if(r!=null){
			return  unescape(r[2]);
		}
		return null;
	};



	/**
	 * 引入外部html元素
	 * @param urls 需要加载的指定的html页面
	 */
	lotteryActivityUtil.includeOuterHtml  = function(urls){
		var obj = $(".include");
		if( obj != undefined && obj != 'undefined'){
			obj.each(function(){
				var flag = false;
				if(validUtil.validNotEmpty(urls)){
					if(urls instanceof Array){
						for(var i in urls){
							if(lotteryActivityUtil.endWith($(this).attr("file"), urls[i])){
								flag = true;
								break;
							}
						}
					}else{
						if(lotteryActivityUtil.endWith($(this).attr("file"), urls)){
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
	 * 获取url中的参数集合
	 * @param url
	 * @returns
	 */
	lotteryActivityUtil.getRequestParamByUrl = function (url) {
		if (lotteryActivityUtil.isEmpty(url)){
			url = window.location.href; //获取url中"?"符后的字串
		}
		var theRequest = {};
		if (url.indexOf("?") != -1) {
			var strs = url.split("?")[1];
			strs = strs.split("&");
			for(var i = 0; i < strs.length; i ++) {
                var stts = strs[i].split("=");
                var attrValue = stts[1];
				if(stts[1].indexOf("#") != -1){
                    attrValue = stts[1].split("#")[0];
				}
				theRequest[stts[0]] = attrValue;
			}
		}
		return theRequest;
	};


})();

/*********************************************************************
 * ********************* 页面加载时需要立即执行的函数 ******************
 *********************************************************************/










