app.controller('simgoldDetailCtrl',function ($scope,$http,$CommonService,$location,$routeParams,$rootScope) {
	       $("#leftMenu").empty();
	        var params = {"id":$location.search().param};
	    	//查看详情
	    	$CommonService.httpPost(params,CONFIG.interface.searchSimGold)
			.success(function(data) {
				$scope.gold = data.resMsg.data[0];
				$scope.gold.state=DIC_CONSTANT.getName("vas", "sim_state", data.resMsg.data[0].state);
			});
});





