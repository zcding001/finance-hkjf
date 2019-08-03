app.controller('userDetail', function ($scope, $timeout, $http, $CommonService, $compile, $location) {
	var regUserId = $location.search().id;
	$scope.index = 1;
	$CommonService.httpPost({"regUserId":regUserId}, CONFIG.interface.userDetial)
		.success(function (data) {
			$scope.userVO = data.params.userVO;			//加载用户信息
            $scope.headPortrait = CONFIG.CONSTANTS.IMG_URL_UNIVERSAL + data.params.userVO.headPortrait;
			$scope.finAccount = data.params.finAccount;	//加载资金账户
			$scope.qdzAccount = data.params.qdzAccount;	//加载钱袋子资金账户
			$scope.loanVO = data.params.loanVO; 			//加载待收金额
			$scope.investTotal = data.params.investTotal;	//累计总额
		});
	$scope.findDetails = function(index){
		$("#bt" + $scope.index).removeClass("hover");
		var uri = "";
		if(index == 1){			//投资记录
			uri = "pages/template/bidInfo/investList.html";
		}else if(index == 2	){	//回款计划
			uri = "pages/template/bidInfo/plan/receiptPlanCountList.html";
		}else if(index == 3	) {	//还款计划
			uri = "pages/template/bidInfo/plan/repayPlanCountList.html";
		}else if(index == 4	){	//交易记录
			uri = "pages/template/payment/tradeRecordList.html";
		}else if(index == 5	){	//绑卡信息
            uri = "pages/template/payment/bankCardList.html";
		}else if(index == 6	){	//邀请信息
            uri = "pages/template/vas/recommendEarnList.html";
		}else if(index == 7	){	//物流信息
            uri = "pages/template/user/addressList.html";
		}
		$("#bt" + index).addClass("hover");
		$scope.index = index;
		$("#details").html($compile("<div ng-include=\"'" + uri + "'\"></div>")($scope));
	}

	$("#details").html($compile("<div ng-include=\"'pages/template/bidInfo/investList.html'\"></div>")($scope));
});
