app.controller('rosDepositInfoController', function ($scope,$injector, $timeout, $http, $CommonService, $compile, $location, $routeParams) {
    $scope.data = {}
    $scope.types = DIC_CONSTANT.getValueAndName("ros", "deposit_type");
    $scope.bidAll = [];
    $scope.bids = [];
	//加载购房宝&物业宝项目
	$http.post(CONFIG.interface.depositBidList, {}).success(function (data) {
		$scope.bidAll = data;
		$scope.initBids($scope.bidAll, 6);
	}).error(function () {});
    //预更新意向金信息
    if($routeParams.id != undefined && $routeParams.id > 0){
        $http.post(CONFIG.interface.preUpdateRosDepositInfo, {id:$routeParams.id}).success(function (data) {
            $scope.data = data.resMsg;
            elementOperateUtil.disableElement(["login", "type", "bidId"]);
        }).error(function () {});
    }
    //意向金类型事件
	$scope.typeChanage = function(){
		if($scope.data.type == 1){
			$scope.initBids($scope.bidAll, 6);
		}else{
			$scope.initBids($scope.bidAll, 9);
		}
	}
	$scope.initBids = function(bidAll, type){
		var arr = [];
		bidAll.forEach(function(data, index){
			if(data.productType == type){
				arr.push(data);
			}
		});
		$scope.bids = arr;
	}
	//表单提交事件
	var $validationProvider = $injector.get('$validation');
	$scope.submit = function(form){
        delete $scope.data.createTime;
        delete $scope.data.modifyTime;
		$validationProvider.validate(form).success(function(){
			$http.post(CONFIG.interface.addRosDepositInfo, $scope.data).success(function (data) {
				if(data.resStatus==CONFIG.CONSTANTS.SUCCESS_STATE){
                    commonUtil.createNotifyAndRedirect("操作成功!",ALERT_SUC, $location, "/rosDepositInfoList");
				}else{
                    commonUtil.createSimpleNotify(data.resMsg, ALERT_ERR);
				}
			}).error(function () {
                commonUtil.createSimpleNotify(data.resMsg, ALERT_ERR);
			});
		}).error(function(){
            // addValidElementStyle();
		});
	};
});

