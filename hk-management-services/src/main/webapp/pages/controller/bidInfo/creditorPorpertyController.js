//标的controller
app.controller('creditorProperty',function ($scope,$injector,$timeout,$http,$location,$CommonService,$routeParams){
	$scope.bidInfo={};//页面初始化对象
	$scope.strConvertArrContains=$CommonService.strConvertArrContains;
	 if($routeParams.bidInfoId!='undefined' && $routeParams.bidInfoId!=undefined){
			//根据ID，查询标的信息
			var params={'bidInfoId':$routeParams.bidInfoId};
			$CommonService.httpPost(params,CONFIG.interface.findBidInfoDetails)
			.success(function(data) {
				$("#bidId").val($routeParams.bidInfoId);
				$scope.bidInfo= data.resMsg;
				$scope.termUnit=DIC_CONSTANT.getName("invest","bid_term_unit",data.resMsg.termUnit)
				$scope.biddRepaymentWay = DIC_CONSTANT.getName("invest", "bid_repayment", data.resMsg.biddRepaymentWay);
                if($scope.bidInfo.creditorState==0){
                    $("#propertyBody").hide();
				};

			});
	 }

	//表单提交事件
	var $validationProvider = $injector.get('$validation');
	//更新表单
	$scope.update=function(form){
		   $("#biddInfoId").val($routeParams.bidInfoId);
		   $validationProvider.validate(form).success(function(){
				  var params = $("#bidForm").serializeObject();
				      $http.post(CONFIG.interface.updateCreditorProperty,params).success(function (data) { 
				    	  if(data.resStatus==CONFIG.CONSTANTS.SUCCESS_STATE){
                              commonUtil.createNotifyAndRedirect("更新成功!",ALERT_SUC, $location, "/bidInfoAfterLoanList");
				          }else{
				        	  commonUtil.createSimpleNotify(data.resMsg,ALERT_ERR);
				          }
				      }).error(function () {
                          commonUtil.createNotifyAndRedirect("更新失败!",ALERT_ERR, $location, "/bidInfoAfterLoanList");
				      });
			}).error(function(){
				    
			});
	};
	 //返回事件
	 $scope.back=function(){
		 $location.path("/bidInfoAfterLoanList");
	 }
	 
	 $scope.creditorStateChange = function(state){
		 if(state==0){
			 $("#propertyBody").hide();
		 }else{
			 $("#propertyBody").show();
		 }
	 }
	 
});