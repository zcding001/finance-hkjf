app.controller('matchedDetailsController',function ($scope,$timeout,$http,$CommonService,$routeParams,$location){
	var params = {"bidMatchId":$routeParams.bidMatchId};
	$CommonService.httpPost(params,CONFIG.interface.matchedDetails)
	.success(function(data) {
		$scope.goodBid = data.params.goodBid;
		$scope.commonBid = data.params.commonBid;
		$scope.goodBid.loanUse = DIC_CONSTANT.getName("invest", "bid_loan_use", $scope.goodBid.loanUse);
		$scope.goodBid.bidScheme = DIC_CONSTANT.getName("invest", "bid_scheme", $scope.goodBid.bidScheme);
		$scope.goodBid.termUnit = DIC_CONSTANT.getName("invest", "bid_term_unit", $scope.goodBid.termUnit);
		$scope.goodBid.assureType = DIC_CONSTANT.getName("invest", "bid_assure_type", $scope.goodBid.assureType);
		$scope.goodBid.biddRepaymentWay = DIC_CONSTANT.getName("invest", "bid_repayment", $scope.goodBid.biddRepaymentWay);
		
		$scope.commonBid.loanUse = DIC_CONSTANT.getName("invest", "bid_loan_use", $scope.commonBid.loanUse);
		$scope.commonBid.bidScheme = DIC_CONSTANT.getName("invest", "bid_scheme", $scope.commonBid.bidScheme);
		$scope.commonBid.termUnit = DIC_CONSTANT.getName("invest", "bid_term_unit", $scope.commonBid.termUnit);
		$scope.commonBid.assureType = DIC_CONSTANT.getName("invest", "bid_assure_type", $scope.commonBid.assureType);
		$scope.commonBid.biddRepaymentWay = DIC_CONSTANT.getName("invest", "bid_repayment", $scope.commonBid.biddRepaymentWay);
	});
});

    



