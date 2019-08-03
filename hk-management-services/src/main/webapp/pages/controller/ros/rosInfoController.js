app.controller('rosInfoController', function ($scope,$injector, $timeout, $http, $CommonService, $compile, $location) {
	$scope.rosInfo = {}
	$scope.types = DIC_CONSTANT.getValueAndName("ros", "type");
	//表单提交事件
	var $validationProvider = $injector.get('$validation');
	$scope.submit = function(form){
		$validationProvider.validate(form).success(function(){
			//增加确认框提示
			console.info($scope.rosInfo);
			var tip = "白名单";
			if($scope.rosInfo.flag == 1){
				tip = "黑名单";
			}
			$scope.rosInfo['ajaxUrl'] = CONFIG.interface.addRosInfo;
			commonUtil.confirmRequest(encryptAndDecryptUtil.encrypt(JSON.stringify($scope.rosInfo)),
				"添加名单", "如果存在该功能类型的【"+ tip +"】将会被自动删除，是否继续？" , function () {
					commonUtil.createNotifyAndRedirect("添加成功!",ALERT_SUC, $location, "/rosInfoList");
				});

			// $http.post(CONFIG.interface.addRosInfo, $scope.rosInfo).success(function (data) {
			// 	if(data.resStatus==CONFIG.CONSTANTS.SUCCESS_STATE){
             //        commonUtil.createNotifyAndRedirect("添加成功!",ALERT_SUC, $location, "/rosInfoList");
			// 	}else{
             //        commonUtil.createSimpleNotify(data.resMsg, ALERT_ERR);
			// 	}
			// }).error(function () {
             //    commonUtil.createSimpleNotify(data.resMsg, ALERT_ERR);
			// });
		}).error(function(){
		});
	};
});

