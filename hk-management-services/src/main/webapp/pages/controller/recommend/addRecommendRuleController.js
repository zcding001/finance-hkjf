app.controller('recommendRuleCtrl',function ($scope,$injector,$timeout,$http,$CommonService,$compile,$location,$routeParams){
	$routeParams.id = $routeParams.id;
	$scope.userTypes = DIC_CONSTANT.getValueAndName("vas", "user_type");
	$('#vasRebatesRuleId').val(cookieUtil.getCookie("vasRebatesRuleId"));
	cookieUtil.delCookie("vasRebatesRuleId");
	
	$scope.recommendRule={};
	$scope.oneLevelInvNumOnes=[];
	$scope.oneLevelRebatesRateOnes=[];
	$scope.twoLevelInvNumTwos=[];
	$scope.twoLevelRebatesRateTwos=[];
	//如果是修改好友推荐规则则进入此方法
	if($routeParams.id!='undefined' && $routeParams.id!=undefined){
		$scope.showPage=false;//用于判断是新增还是修改的标识 false:修改
		//根据ID，查询规则信息
		var params={'vasRuleId':$routeParams.id};
		$CommonService.httpPost(params,CONFIG.interface.searchVasRuleById)
			.success(function(data) {
				$scope.recommendRule = angular.fromJson(data.resMsg[0]);
				$scope.userType = $scope.recommendRule.userType+'';
				$scope.options = $scope.recommendRule.friendLvelOneList==null?[]:$scope.recommendRule.friendLvelOneList;
				$scope.optionsTwo = $scope.recommendRule.friendLvelTwoList==null?[]:$scope.recommendRule.friendLvelTwoList;
				$scope.recommendRule.beginTime = dateUtil.date($scope.recommendRule.beginTime);
				$scope.recommendRule.endTime = dateUtil.date($scope.recommendRule.endTime);
				$("#vasRebatesRuleId").val($routeParams.id);
			});
	}else{
		$scope.userType='0';//默认为普通用户
		$scope.options = [{}];//一级好友选项
		$scope.optionsTwo =[{}];//二级好友选项
		$scope.showPage=true;//用于判断是新增还是修改的标识 true:新增
	}
	//表单提交事件
	$scope.submit = function(){
			var params = $("#recommendRuleForm").serializeObject();
			if ($scope.RecommendRuleForm.$valid) {
				//修改推荐奖规则
				if($routeParams.id!='undefined' && $routeParams.id!=undefined){
					$http.post(CONFIG.interface.updateVasRuleById,params).success(function (data) {
						if(data.resStatus==CONFIG.CONSTANTS.SUCCESS_STATE){
		                    commonUtil.createNotifyAndRedirect("更新成功!",ALERT_SUC, $location, "/recommendEarnRuleList");
						}else{
							  commonUtil.createSimpleNotify(data.resMsg,ALERT_ERR);
						}
					}).error(function () {
		                commonUtil.createNotifyAndRedirect("更新失败!",ALERT_ERR, $location, "/recommendEarnRuleList");
					});
				}else{
					//添加推荐奖规则
					$http.post(CONFIG.interface.addRecommendRule,params).success(function (data) {
						if(data.resStatus==CONFIG.CONSTANTS.SUCCESS_STATE){
							$scope.vasRebatesRuleId=data.resMsg;
							 cookieUtil.setCookie("vasRebatesRuleId",data.resMsg);
							 commonUtil.createNotifyAndRedirect("添加成功!",ALERT_SUC, $location, "/addRecommendEarnRule","refresh");
						}else{
							  commonUtil.createSimpleNotify(data.resMsg,ALERT_ERR);
						}
					}).error(function () {
		                commonUtil.createNotifyAndRedirect("添加失败!",ALERT_ERR, $location, "/recommendEarnRuleList");
					});
				}
			}else{
				  commonUtil.createSimpleNotify("请检查参数信息",ALERT_ERR);
			}
	};

	//返回事件
	$scope.back=function(){
		$location.path("/recommendEarnRuleList");
	}
	
	//添加选项
	$scope.addOption = function($index){
		 $scope.options.splice($index + 1, 0, {}); 
	}  
	//删除选项
	$scope.delOption = function($index){
	    $scope.options.splice($index, 1);  
	}
	//添加选项
	$scope.addFriendTwoOption = function($index){ 
	    $scope.optionsTwo.splice($index + 1, 0, {}); 
	}  
	//删除选项
	$scope.delFriendTwoOption = function($index){
	    $scope.optionsTwo.splice($index, 1);  
	}
		
	//用户类型事件
	$scope.changeUserType=function(type){
		if(type != 0){
			$scope.visible=true;
		}else{
			$scope.visible=false;
		}
		var flag=false;
		if($routeParams.id){
			//根据ID，查询规则信息
			var params={'vasRuleId':$routeParams.id};
			$CommonService.httpPost(params,CONFIG.interface.searchVasRuleById)
				.success(function(data) {
					for(var j=0;j<data.resMsg.length;j++){
						if(type == data.resMsg[j].userType-0){
							flag=true;
							$scope.userType=data.resMsg[j].userType;
							$scope.options=data.resMsg[j].friendLvelOneList;
							$scope.optionsTwo=data.resMsg[j].friendLvelTwoList;
							$scope.recommendRule=angular.fromJson(data.resMsg[j]);
							$scope.recommendRule.beginTime=dateUtil.date($scope.recommendRule.beginTime);
							$scope.recommendRule.endTime=dateUtil.date($scope.recommendRule.endTime);
							$("#vasRebatesRuleId").val($routeParams.id);
						}
					}
				});
			if(flag==false){
				$scope.recommendRule={};
				$scope.options=[{}];
				$scope.optionsTwo=[{}];
			}
		}
	}
});
