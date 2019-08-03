app.controller('updateSpreadCtrl',function ($scope,$injector,$timeout,$http,$CommonService,$compile,$location,$routeParams){
	$scope.enterpriseUsers =[]; 
	//加载企业名称
	$http.post(CONFIG.interface.findGroupCodeInfo,{regUserId : $routeParams.id}).success(function (data) {
		if(data.resStatus==CONFIG.CONSTANTS.SUCCESS_STATE){
			$scope.enterpriseUsers = data.params.groupCodeList;
			$("#login").html(data.params.login);
			$scope.groupCode = data.params.groupCode;
		}
	}).error(function () {
	});

	//表单提交事件
	var $validationProvider = $injector.get('$validation');
	$scope.submit = function(form){
		$validationProvider.validate(form).success(function(){
			$http.post(CONFIG.interface.updateSpreadSource, {regUserId : $routeParams.id,groupCode : $scope.groupCode }).success(function (data) {
				if(data.resStatus==CONFIG.CONSTANTS.SUCCESS_STATE){
                    commonUtil.createNotifyAndRedirect("更新成功!",ALERT_SUC, $location, "/spreadSourceList");
				}else{
                    commonUtil.createSimpleNotify(data.resMsg, ALERT_ERR);
				}
			}).error(function () {
                commonUtil.createSimpleNotify(data.resMsg, ALERT_ERR);
			});
		}).error(function(){
		});
	};

	//返回事件
	$scope.back=function(){
		$location.path("/spreadSourceList");
	}
	
});
