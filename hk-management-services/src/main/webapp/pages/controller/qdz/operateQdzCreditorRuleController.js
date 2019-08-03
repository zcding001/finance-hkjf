app.controller('qdzRuleCreditorCtrl',function ($scope,$injector,$timeout,$http,$CommonService,$compile,$location,$routeParams){
	$scope.qdzVasRuleItem = {};
	if($routeParams.id!='undefined' && $routeParams.id!=undefined && $location.$$path !='/searchQdzCreditorRule'){
		//根据ID，查询规则信息
		var params={'id':$routeParams.id};
		$CommonService.httpPost(params,CONFIG.interface.getQdzRule)
			.success(function(data) {
				$("#id").val($routeParams.id);
				var content = data.resMsg.content;
				$scope.content= content;
			});
	}else{
		$CommonService.httpPost({},CONFIG.interface.searchQdzCreditorRule)
			.success(function(data) {
				$scope.ruleContent =   data.resMsg.ruleContent;
				$scope.remainMoney = data.resMsg.remainMoney;
				$scope.ruleId = data.resMsg.ruleId;
			});
	}
	var $validationProvider = $injector.get('$validation');
	$scope.update = function(form){
		$validationProvider.validate(form).success(function(){
			var params = $("#qdzCreditorForm").serializeObject();
			$http.post(CONFIG.interface.updateQdzCreditorRule,params).success(function (data) {
				if(data.resStatus==CONFIG.CONSTANTS.SUCCESS_STATE){
                    commonUtil.createNotifyAndRedirect("更新成功!",ALERT_SUC, $location, "/searchQdzCreditorRule");
				}
			}).error(function () {
                commonUtil.createNotifyAndRedirect("更新失败!",ALERT_ERR, $location, "/searchQdzCreditorRule");
			});
		}).error(function(){
		});
	};
	$scope.back=function(){
		$location.url("/searchQdzCreditorRule");
	}
});

    



