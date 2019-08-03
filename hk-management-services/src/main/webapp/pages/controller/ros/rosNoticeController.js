app.controller('rosNoticeController', function ($scope,$injector, $timeout, $http, $CommonService, $compile, $location) {
	$scope.rosNotice = {}
	$scope.types = DIC_CONSTANT.getValueAndName("ros", "notice_type");
    $scope.noticeWays = DIC_CONSTANT.getValueAndName("ros", "notice_way");
    $scope.showPage = false;
    $scope.showPage2 = true;
	//表单提交事件
	var $validationProvider = $injector.get('$validation');
	$scope.submit = function(form){
		$validationProvider.validate(form).success(function(){
			$http.post(CONFIG.interface.addRosNotice, $scope.rosNotice).success(function (data) {
				if(data.resStatus==CONFIG.CONSTANTS.SUCCESS_STATE){
                    commonUtil.createNotifyAndRedirect("添加成功!",ALERT_SUC, $location, "/rosNoticeList");
				}else{
                    commonUtil.createSimpleNotify(data.resMsg, ALERT_ERR);
				}
			}).error(function () {
                commonUtil.createSimpleNotify(data.resMsg, ALERT_ERR);
			});
		}).error(function(){
			alert(111);
		});
	};

	$scope.showTelOrEmail = function () {
        var options=$("#changeNoticeWay").val(); //获取选中的项
		if (options=='number:2'){
            $scope.showPage = true;
            $scope.showPage2 = false;
            // $("#receTel").removeClass("ng-not-empty");
            // $("#receEmail").addClass("ng-not-empty");
		}else{
            $scope.showPage = false;
            $scope.showPage2 = true;
            // $("#receEmail").removeClass("ng-not-empty");
            // $("#receTel").addClass("ng-not-empty");
		}

    }

});

