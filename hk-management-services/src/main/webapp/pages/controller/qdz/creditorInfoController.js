app.controller('searchCreditorInfo',function ($scope,$timeout,$http,$CommonService,$compile,$location){
	//$("#leftMenu").empty();
		$CommonService.httpPost({},CONFIG.interface.searchCreditorInfo)
		.success(function(data) {
			$scope.residueQqMoney =   data.params.residueQqMoney;
			$scope.transFailSumMoney = data.params.transFailSumMoney;
			$scope.transInSumMoney =   data.params.transInSumMoney;
			$scope.currentCreditMoney =   data.params.currentCreditMoney;
			$scope.hqWaitSumMoney =   data.params.hqWaitSumMoney;
			$scope.lackCreditorMoney =   data.params.lackCreditorMoney;


		});
});

    



