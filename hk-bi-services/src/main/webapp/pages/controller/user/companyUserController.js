app.controller('companyUser', function ($scope,$injector, $timeout, $http, $CommonService, $compile, $location) {
	$scope.companyUser = {}
	$scope.companyUser.type = 1;
	$scope.companyUser.bankName=null;
	//$scope.types = DIC_CONSTANT.getValueAndName("user", "type");
	$scope.types = DIC_CONSTANT.getDicObjInLimitValues("user", "type", "2,3,4");
	$scope.bankNames = BANK_NAMES;
	$scope.bankProvinces = AREA_CONSTANT.getData(0);
	$scope.bankCitys = AREA_CONSTANT.getData(-1);
	$scope.registerProvinces = AREA_CONSTANT.getData(0);
	$scope.registerCitys = AREA_CONSTANT.getData(-1);
	$scope.registerCountrys = AREA_CONSTANT.getData(-1);

	$scope.bankProvincesChange = function(){
		/*转化特殊的省市  获取1、3级*/
		var specialMap = new Map();
			//北京
			specialMap.set("110000", "110100");
			//上海
			specialMap.set("310000", "310100");
			//重庆
			specialMap.set("500000", "500100");
			//天津
			specialMap.set("120000", "120100");
		var flag = specialMap.has($scope.companyUser.bankProvince);
		if(flag){
            $scope.bankCitys = AREA_CONSTANT.getData(specialMap.get($scope.companyUser.bankProvince));
		}else{
            $scope.bankCitys = AREA_CONSTANT.getData($scope.companyUser.bankProvince);
		}

	}
	$scope.registerProvincesChange = function(){
		$scope.registerCitys = AREA_CONSTANT.getData($scope.companyUser.registerProvince);
	}
	$scope.registerCitysChange = function(){
		$scope.registerCountrys = AREA_CONSTANT.getData($scope.companyUser.registerCity);
	}
	//表单提交事件
	var $validationProvider = $injector.get('$validation');
	$scope.submit = function(form){
		$validationProvider.validate(form).success(function(){
			if($scope.companyUser.passwd != $scope.companyUser.rePasswd){
				$("#passwdError").css('display', 'block').html("密码与重复密码不一致！");
				scrollTo(0,0);
				return;
			}
			if($scope.companyUser.bankProvince == '' || $scope.companyUser.bankProvince == undefined || $scope.companyUser.bankCity == '' || $scope.companyUser.bankCity == undefined){
				$("#bankError").css('display', 'block').html("请选择开户行地址！");
				scrollTo(0,0);
				return;
			}
			$scope.companyUser.passwd = rsaUtil.encryptData($scope.companyUser.passwd);
			$scope.companyUser.rePasswd = rsaUtil.encryptData($scope.companyUser.rePasswd);
			for (var i in $scope.bankNames){
	            var o = $scope.bankNames[i];
	            if (o.value == $scope.companyUser.bankCode){
	            	$scope.companyUser.bankName=o.name;
	            }
	        }
			// var params = $("#companyUserForm").serializeObject();
			$http.post(CONFIG.interface.addCompanyUser, $scope.companyUser).success(function (data) {
				if(data.resStatus==CONFIG.CONSTANTS.SUCCESS_STATE){
                    commonUtil.createNotifyAndRedirect("添加成功!",ALERT_SUC, $location, "/userList");
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

