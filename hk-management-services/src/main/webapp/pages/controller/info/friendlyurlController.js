app.controller('friendlyurlCtrl',function ($scope,$injector,$timeout,$http,$location,$routeParams,$CommonService,$sce,$rootScope,$checkBoxService) {
    $routeParams.id = $routeParams.param;
    $scope.channels =DIC_CONSTANT.getValueAndName("info", "channel");

    //对公告进行修改操作
	 if($routeParams.id!='undefined' && $routeParams.id!=undefined){
		    $scope.showPage=false;//用于判断是新增还是修改的标识 false:修改
			var params={'id':$routeParams.id};
			$CommonService.httpPost(params,CONFIG.interface.findInfomationById)
			.success(function(data) {
				$("#channel").val(data.resMsg.channel);
				$checkBoxService.showCheckInfo($scope.channels,$("#channel").val());
				$scope.title = data.resMsg.title;
				$scope.url = data.resMsg.url;
				$scope.sort = data.resMsg.sort;
			});
	 }else{
		 $scope.showPage=true;
	 }
	//表单提交事件
	var $validationProvider = $injector.get('$validation');
	//新增公告
	$scope.submit = function(form){
		//获取表单信息，并验证
		$validationProvider.validate(form).success(function(){
			     $("#infomationId").val(0);
			 	 $("#channel").val($checkBoxService.check($scope.channels));
			     var params = $("#infoFriendForm").serializeObject();
			      $http.post(CONFIG.interface.insertInfomations,params).success(function (data) { 
			          if(data.resStatus==CONFIG.CONSTANTS.SUCCESS_STATE){
                          commonUtil.createNotifyAndRedirect("添加成功!",ALERT_SUC, $location, "/friendlyurlList");
			          }
			      }).error(function () {
                      commonUtil.createNotifyAndRedirect("添加失败!",ALERT_ERR, $location, "/friendlyurlList");
			      });
		}).error(function(){
		});
	 };
	 //更新公告
	 $scope.update = function(form){
			//获取表单信息，并验证
			$validationProvider.validate(form).success(function(){
				     $("#infomationId").val($routeParams.id);
				 	 $("#channel").val($checkBoxService.check($scope.channels));
				     var params = $("#infoFriendForm").serializeObject();
				      $http.post(CONFIG.interface.updateInformationNews,params).success(function (data) { 
				          if(data.resStatus==CONFIG.CONSTANTS.SUCCESS_STATE){
                              commonUtil.createNotifyAndRedirect("修改成功!",ALERT_SUC, $location, "/friendlyurlList");
				          }
				      }).error(function (data) {
                          commonUtil.createSimpleNotify(data.resMsg, ALERT_ERR);
				      });
			}).error(function(){
			});
		 };
		 //重置
		 $scope.reset=function(form){
			 $validationProvider.reset(form);
		 };
		 //预览
		 $scope.preview=function(){
		     cookieUtil.setCookie("infoFriendly",angular.toJson($('#infoFriendForm').serializeObject(), true));
		     var data= encryptAndDecryptUtil.encrypt(JSON.stringify({"target":"friendlyurlDeatil","show":true}))
		     commonUtil.directRequest(data,1);
		 };
		 //返回
		 $scope.back=function(){
		     $location.path("/friendlyurlList");
		 };
});





