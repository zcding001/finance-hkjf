/**########################################################
 * 业务公共方法
 * commonFactory.js
 * Created by xuhui.liu on 2017/08/30.\
 * ########################################################
 */

/**
 * http post请求
 * 调用：CommonService.list(postData,_url)
 * postData 请求参数（例如form表单）
 * _url     请求url
 * Created by xuhui.liu on 2017/08/30.
 */
app.factory('$CommonService', ['$http', function ($http) {
    return {
    	getOptions : function(opts){//文件上传默认参数
    		return jQuery.extend({
    			file_size:1048576,	//文件大小, 默认1M
    			platform:"hkjf",		//图片保存平台
    			filePath:"/hkjf/common",
    			fileType:"image",	//图片类型
    			createInput : 1,	//动态添加<input> 用于存储上传图片的名称
    			showSuc : 0,	//显示上传成功提示，默认是不显示
    			preview : 1		//上传后是否预览，默认预览
    	    }, opts || {});
    	},
        httpPost: function (postData,_url) {	//发送异步请求
        	return  $http({
     	        method: "POST",
     	        url: _url,//CONFIG.interface.loanBidInfoList,
     	        data:$.param(postData),
     	        headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8' }
     	     });
        },
        picPreview: function(id, opts){		//图片预览
        	if(opts == undefined){
   			 	opts = {};
	   		}
	   		$("#" + id).uploadPreview(opts);
        },
        upload:function(id, opts){			//图片上传、预览
    		if(($("#" + id).val() == "" || $("#" + id).val() == undefined)){
                commonUtil.createSimpleNotify("请选择图片", ALERT_WARN);
   			 	return;
   		 	}
        	var fileSize = $("#" + id)[0].files[0].size;
        	opts = this.getOptions(opts);
        	if(fileSize > opts.file_size){
                commonUtil.createSimpleNotify("图片不得超多" + opts.file_size + "字节，您上传的图片是" + fileSize + "字节", ALERT_WARN);
   			 	return;
        	}
        	var data = new FormData();      //以下为像后台提交图片数据
        	data.append('unUpFile', $("#" + id)[0].files[0]);
        	data.append('platform', opts.platform);
        	data.append('filePath', opts.filePath);
        	data.append('fileType', opts.fileType);
        	$http.post(CONFIG.interface.uploadFile, data, {
        		transformRequest: angular.identity,
        		headers: {'Content-Type': undefined}
        	}).success(function(data) {
        		if (data.resStatus == CONFIG.CONSTANTS.SUCCESS_STATE) {
        			if(opts.createInput == 1){//动态在file元素前添加<input> 用于存储上传图片的名称
                    	$("#" + id).before('<input type="hidden" name="' + id.split("_")[1] + '" id="' + id.split("_")[1] + '"/>');
                    }
        			$("#"+id.split("_")[1]).val(data.params.saveKey);
        			if(opts.showSuc == 1){
                        commonUtil.createSimpleNotify("图片已上传!", ALERT_SUC);
        			}
        			if(opts.preview == 1){
        				$("#" + id).displayBack(id, opts, CONFIG.CONSTANTS.IMG_URL_ROOT + data.params.saveKey);
        			}
        		}else{
                    commonUtil.createSimpleNotify("图片上传失败，请重新上传!",  ALERT_ERR);
        		}
        	});
        },
        strConvertArrContains:function (strArr, currentValue) {	//字符串转数组并且判断是否在其中的函数
	        var strArrs=strArr+'';
	        var arrs= strArrs.trim().split(',');
	        var currentValueStr=''+currentValue;
	        for(var i=0;i<arrs.length;i++){
	            if(arrs[i]==currentValueStr){
	                return true;
	            }
	        }
	        return false;
	    },
	    //判断两个值是否相等
	    checkStrEq:function (firstStr,secondStr) {
	        var firstArr = firstStr.trim().split(',');
	        var secondArr = secondStr.trim().split(',');
	        if(firstArr.length != secondArr.length){
	        	return false;
	        }
	        for(var i=0;i<firstArr.length;i++){
	        	if(firstArr[i] != secondArr[i]){
	        		return false;
	        	}
	        }
	        return true;
	    }
    }
}]);

/**
 * 导出excel
 * 调用：Excel.tableToExcel(tableId, worksheetName)
 * tableId：需要导出表格页面对应的table id属性
 * worksheetName：表格sheet名称
 * Created by xuhui.liu on 2017/08/30.
 */
app.factory('$Excel', function ($window) {
    var uri = 'data:application/vnd.ms-excel;base64,',
        template = '<html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel" xmlns="http://www.w3.org/TR/REC-html40"><head><!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet><x:Name>{worksheet}</x:Name><x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet></x:ExcelWorksheets></x:ExcelWorkbook></xml><![endif]--></head><body><table>{table}</table></body></html>',
        base64 = function (s) { return $window.btoa(unescape(encodeURIComponent(s))); },
        format = function (s, c) { return s.replace(/{(\w+)}/g, function (m, p) { return c[p]; }) };
    return {
        tableToExcel: function (tableId, worksheetName) {
            var table = $(tableId),
                ctx = { worksheet: worksheetName, table: table.html() },
                href = uri + base64(format(template, ctx));
            return href;
        }
    };
})
/**
 * 获取checkbox选中的值
 * 调用：checkBoxService.check(params)
 * params 请求参数
 * Created by yanbinghuang
 */
app.service('$checkBoxService',function ($filter) {
	return {
		//checkbox获取选中的值
	     check: function (params,key) {
	    	 var showNameArray=[];
             var findKey = 'value';
             if(commonUtil.isNotEmpty(key)){
                 findKey=key;
             }
	          var showsNameArray = $filter('filter')(params, { checked: true}, true);
		      angular.forEach(showsNameArray,function(obj){
		      if(obj)
		    	  showNameArray.push(obj[findKey]);
		      });
		      return showNameArray.join();
		},
		//checkbox回显转换
		showCheckInfo:function(originArr,str,key){
			var arrs=str.split(",");
            var findKey = 'value';
			if(commonUtil.isNotEmpty(key)){
                findKey=key;
			}
		   for(var i=0;i<arrs.length;i++){
			   originArr.forEach(function(ipt){
				if(parseInt(arrs[i])==parseInt(ipt[findKey]) ){
					 ipt.checked =true;
				}
			 });
		   }
		}
	}
});
