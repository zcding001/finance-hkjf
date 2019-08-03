app.controller('carouselFigureDeatil',function ($scope,$timeout,$http,$CommonService,$compile,$location,$sce,$routeParams,$rootScope){
	$("#leftMenu").empty();
	    var show =$routeParams.show;//预览标识
	    var params = {"id":$location.search().param};
	    //预览
	    if(show){
	    	$scope.info=angular.fromJson(cookieUtil.getCookie("infoNewsCarousel"));
	    	$scope.info.position=DIC_CONSTANT.getName("info", "position",parseInt($scope.info.position));
	    	$scope.info.type=DIC_CONSTANT.getName("info", "carousel_figure_type",parseInt($scope.info.type));
			$scope.info.content=$sce.trustAsHtml($scope.info.content);
			cookieUtil.delCookie("infoNewsCarousel");
	    }else{
	    	//查看详情
	    	$CommonService.httpPost(params,CONFIG.interface.findInfomationById)
			.success(function(data) {
				$scope.info = data.resMsg;
		    	$scope.info.position=DIC_CONSTANT.getName("info", "position", data.resMsg.position);
				$scope.info.type=DIC_CONSTANT.getName("info", "carousel_figure_type", data.resMsg.type);
				$scope.info.createTime= dateUtil.date(data.resMsg.createTime);
				$scope.info.content=$sce.trustAsHtml(data.resMsg.content);
			});
	    }
});

    



