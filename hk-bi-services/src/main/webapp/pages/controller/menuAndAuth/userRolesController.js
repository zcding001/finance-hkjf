app.controller('userRolesController', function ($scope,$injector, $timeout, $http, $CommonService, $compile, $location) {
	$scope.userRole = {};
    $http.post(CONFIG.interface.myAccountRoles, {}).success(function (data) {
        $scope.roleTypes = data.resMsg;
    }).error(function () {});
	//表单提交事件
	var $validationProvider = $injector.get('$validation');
	$scope.submit = function(form){
		$validationProvider.validate(form).success(function(){
			$http.post(CONFIG.interface.addUserRoles, $scope.userRole).success(function (data) {
				if(data.resStatus==CONFIG.CONSTANTS.SUCCESS_STATE){
                    commonUtil.createNotifyAndRedirect("添加成功!",ALERT_SUC, $location, "/userRoles");
				}else{
                    commonUtil.createSimpleNotify(data.resMsg, ALERT_ERR);
				}
			}).error(function () {
                commonUtil.createSimpleNotify(data.resMsg, ALERT_ERR);
			});
		}).error(function(){
		});
	};
});

