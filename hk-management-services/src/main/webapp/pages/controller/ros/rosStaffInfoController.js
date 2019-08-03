app.controller('rosStaffInfoController', function ($scope,$injector, $timeout, $http, $CommonService, $compile, $location) {
	$scope.rosStaffInfo = {}
	$scope.types = DIC_CONSTANT.getValueAndName("ros", "staff_type");
	$scope.recomStates = DIC_CONSTANT.getValueAndName("ros", "staff_recommend_state");
	$scope.enterpriseUsers = [];
	//加载物业员工
	$http.post(CONFIG.interface.enterpriseUserList, {}).success(function (data) {
		$scope.enterpriseUsers = data;
	}).error(function () {});
	//表单提交事件
	var $validationProvider = $injector.get('$validation');
	$scope.submit = function(form){
		$validationProvider.validate(form).success(function(){
			if($scope.rosStaffInfo.type == 1){
				if($scope.rosStaffInfo.enterpriseRegUserId == '' || $scope.rosStaffInfo.enterpriseRegUserId == undefined){
					$("#enterpriseRegUserIdError").css('display', 'block').html("请选择企业！");
					scrollTo(0,0);
					return;
				}
			}
			//加载企业名称
			if($scope.rosStaffInfo.enterpriseRegUserId != ''){
				for(var i in $scope.enterpriseUsers){
					var data = $scope.enterpriseUsers[i];
					if(data.userId == $scope.rosStaffInfo.enterpriseRegUserId){
						$scope.rosStaffInfo.enterpriseRealName = data.enterpriseName;
					}
				}
			}
			$http.post(CONFIG.interface.addRosStaffInfo, $scope.rosStaffInfo).success(function (data) {
				if(data.resStatus==CONFIG.CONSTANTS.SUCCESS_STATE){
                    commonUtil.createNotifyAndRedirect("添加成功!",ALERT_SUC, $location, "/rosStaffInfoList");
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

