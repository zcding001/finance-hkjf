var ajaxUtil = {};
(function () {
	/***
	 * @Description AJAX的POST请求
	 * @Method_Name  POST
	 * @param url 提交到后台的URL
	 * @param param 提交的数据
	 * @param callback 成功返回的方法
	 * @param dataType 数据类型 如果为空则默认为json格式
	 * @param async 同步还是异步(默认true)，false为同步
	 * Created by yanbinghuang
	 */
    ajaxUtil.post = function (url, param, callback, dataType, async) {
        _sendAjax(url, "post", dataType, param, async, callback);
    };

    /***
	 * @Description AJAX的GET请求
	 * @Method_Name  GET
	 * @param url 提交到后台的URL
	 * @param param 提交的数据
	 * @param callback 成功返回的方法
	 * @param dataType 数据类型 如果为空则默认为json格式
	 * @param async 同步还是异步(默认true)，false为同步
	 * Created by yanbinghuang
	 */
    ajaxUtil.get = function(url, param, callback, dataType, async){
    	_sendAjax(url, "get", dataType, param, async, callback);
    };
    /***
	 * @Description AJAX的FORM请求
	 * @Method_Name  submitForm
	 * @param url 提交到后台的URL
	 * @param formId 表单ID
	 * @param callback 成功返回的方法
	 * @param dataType 数据类型 如果为空则默认为json格式
	 * @param async 同步还是异步(默认true)，false为同步
	 * Created by yanbinghuang
	 */
    ajaxUtil.submitForm = function(url, formId, callback, dataType, async){
    	var $form = $("#" + formId);
    	var method = (validUtil.validNotEmpty($form.attr("method")) ?  $form.attr("method"):"post" );
    	_sendAjax(url, method, dataType, $form.serializeArray(), async, callback);
    };
    //调用AJAX
    var _sendAjax = function (url, type, dataType, param, async, callback) {
    	$.ajax({
            type: type,  
            url: commonUtil.getRequestUrl(url),
            data: validUtil.validNotEmpty(param) ? param : {},
            dataType: validUtil.validNotEmpty(dataType) ? dataType :"json" ,
            async:validUtil.validNotEmpty(async)? async :true ,
            cache: false,
            success: function(data){
            	if(data.resStatus == CONSTANTS.SESSION_TIME_OUT){
					commonUtil.sessionTimeOut();
				}else if(data.resStatus == CONSTANTS.NO_IDENTIFY) {
					commonUtil.jumpToAccount("securityCenter.html");
				}else{
					if(validUtil.validNotEmpty(callback) && typeof callback == 'function'){
						callback(data);
					}
				}
            },
            error: function () {  
            	//window.location.href = "/error.html";
            }  
        }); 
    }


    //调用AJAX
    ajaxUtil.sendAjaxNoRedictToLogin = function (url, param, callback) {
        $.ajax({
            type: 'post',
            url: commonUtil.getRequestUrl(url),
            data: validUtil.validNotEmpty(param) ? param : {},
            dataType:"json",
            async:true ,
            cache: false,
            success: function(data){
                if(validUtil.validNotEmpty(callback) && typeof callback == 'function'){
                    callback(data);
                }
            },
            error: function () {
                //window.location.href = "/error.html";
            }
        });
    }


	/**
	 * 异步下载文件
	 * @param url 下载文件的地址
	 * @param param 请求参数
	 */
	ajaxUtil.downloan = function (url, param) {
		$("#_hiddenDownloadForm").remove();
		var content = "<form id='_hiddenDownloadForm' method='post' action='" + commonUtil.getRequestUrl(url) + "'>";
		if(param != undefined){
			$.each(param, function(key) {
				content += "<input type='hidden' name='"+ key +"' value='" + param[key]+ "'>";
			});
		}
		content += "</form>";
		$("body").append(content);
		$("#_hiddenDownloadForm").submit();
	};
    
})();