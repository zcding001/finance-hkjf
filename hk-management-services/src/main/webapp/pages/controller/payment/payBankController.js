app.controller('payBankReferCtrl',function ($scope,$injector,$timeout,$http,$location,$routeParams,$CommonService,$checkBoxService) {
    $routeParams.id = $routeParams.param;
    $scope.bank = {};//初始化支付银行对象
    //修改操作
	 if($routeParams.id!='undefined' && $routeParams.id!=undefined){
		  $scope.showPage=false;//用于判断是新增还是修改的标识 false:修改
		  var params={'id':$routeParams.id};
			$CommonService.httpPost(params,CONFIG.interface.findBankReferById)
			.success(function(data) {
			    $scope.bank = data.resMsg;
			});
	 }
	//表单提交事件
	var $validationProvider = $injector.get('$validation');
	//修改银行限额
	$scope.submit = function(form){
		//获取表单信息，并验证
		$validationProvider.validate(form).success(function(){
			     var params = $("#payBankModelForm").serializeObject();
			     params.id = $routeParams.id;
			     $http.post(CONFIG.interface.updateBankReferById,params).success(function (data) {
			          if(data.resStatus==CONFIG.CONSTANTS.SUCCESS_STATE){
                          commonUtil.createNotifyAndRedirect(data.resMsg,ALERT_SUC, $location, "/payBankList");
			          }else{
                          commonUtil.createSimpleNotify(data.resMsg,ALERT_ERR);
			          }
			      }).error(function () {
                     commonUtil.createNotifyAndRedirect(data.resMsg,ALERT_ERR, $location, "/payBankList");
			      });
		}).error(function(){
		});
	 };
	//返回事件
	 $scope.back=function(){
	 	 $location.path("/payBankList");
	 }
});
