app.controller('friendlyurlDeatil',function ($scope,$timeout,$http,$CommonService,$compile,$location,$sce,$routeParams,$rootScope) {
	  $("#leftMenu").empty();
	    var show =$routeParams.show;//预览标识
	    var params = {"id":$location.search().param};
	    //预览
	    if(show){
	    	$scope.info=angular.fromJson(cookieUtil.getCookie("infoFriendly"));
	    	$scope.info.type=DIC_CONSTANT.getName("info", "type",parseInt($scope.info.position));
	    	cookieUtil.delCookie("infoFriendly");
	    }else{
	    	//查看详情
	    	$CommonService.httpPost(params,CONFIG.interface.findInfomationById)
			.success(function(data) {
				$scope.info = data.resMsg;
				$scope.info.position=DIC_CONSTANT.getName("info", "type", data.resMsg.position);
			});
	    }
});





