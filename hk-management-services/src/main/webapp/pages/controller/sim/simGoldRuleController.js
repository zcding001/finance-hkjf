app.controller('simRuleCtrl',function ($scope,$injector,$timeout,$http,$location,$routeParams,$CommonService,$sce,$rootScope,$checkBoxService){
	//初始化页面下拉列表
    $scope.types =DIC_CONSTANT.getValueAndName("vas", "gold_rule_type");
	//表单提交事件
	var $validationProvider = $injector.get('$validation');
	//新增公告
	$scope.submit = function(form){
		//获取表单信息，并验证
		$validationProvider.validate(form).success(function(){
			     var params = $("#simRuleForm").serializeObject();
			      $http.post(CONFIG.interface.saveVasGoldRule,params).success(function (data) { 
			          if(data.resStatus==CONFIG.CONSTANTS.SUCCESS_STATE){
                          commonUtil.createNotifyAndRedirect("添加成功!",ALERT_SUC, $location, "/simRuleList");
			          }
			      }).error(function () {
                      commonUtil.createNotifyAndRedirect("添加失败!",ALERT_ERR, $location, "/simRuleList");
			      });
		}).error(function(){
		});
	 };
		 //重置
		 $scope.reset=function(form){
			 $validationProvider.reset(form);
		 };
		 //返回
		 $scope.back=function(){
		     $location.path("/simRuleList");
		 };
});


