app.controller('qdzRuleRecommendEarnCtrl',function ($scope,$injector,$timeout,$http,$CommonService,$compile,$location,$routeParams){
	//alert($routeParams.id);
	$scope.qdzVasRuleItem = {};
	//如果是修改标的进入此方法
	if($routeParams.id!='undefined' && $routeParams.id!=undefined){
		$scope.showPage=false;//用于判断是新增还是修改的标识 false:修改
		//根据ID，查询规则信息
		var params={'id':$routeParams.id};
		$CommonService.httpPost(params,CONFIG.interface.getQdzRule)
			.success(function(data) {
				$("#qdzRuleId").val($routeParams.id);
				var content = data.resMsg.content;
				$scope.qdzVasRuleItem= angular.fromJson(content);
			});
	}else{
		$scope.showPage=true;//用于判断是新增还是修改的标识 true:新增
	}
	//表单提交事件
	var $validationProvider = $injector.get('$validation');
	$scope.submit = function(form){

		$validationProvider.validate(form).success(function(){
			var params = $("#qdzRecommendEarnForm").serializeObject();
			$http.post(CONFIG.interface.saveQdzRecommendEarnRule,params).success(function (data) {
				if(data.resStatus==CONFIG.CONSTANTS.SUCCESS_STATE){
                    commonUtil.createNotifyAndRedirect("添加成功!",ALERT_SUC, $location, "/searchQdzRecommendEarnRule");
				}
			}).error(function () {
                commonUtil.createNotifyAndRedirect("添加失败!",ALERT_ERR, $location, "/searchQdzRecommendEarnRule");
			});
		}).error(function(){
		});
	};
	$scope.update = function(form){
		$validationProvider.validate(form).success(function(){
			var params = $("#qdzRecommendEarnForm").serializeObject();
			$http.post(CONFIG.interface.updateQdzRecommendEarnRule,params).success(function (data) {
				if(data.resStatus==CONFIG.CONSTANTS.SUCCESS_STATE){
                    commonUtil.createNotifyAndRedirect("更新成功!",ALERT_SUC, $location, "/searchQdzRecommendEarnRule");
				}
			}).error(function () {
                commonUtil.createNotifyAndRedirect("更新失败!",ALERT_ERR, $location, "/searchQdzRecommendEarnRule");
			});
		}).error(function(){
		});
	};
	$scope.back=function(){
		$location.url("/searchRecommendEarnRule");
	}
});

    



