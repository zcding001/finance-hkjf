app.controller('payChannelCtrl',function ($scope,$injector,$timeout,$http,$location,$routeParams,$CommonService,$checkBoxService) {
    $routeParams.id = $routeParams.param;
    $scope.payChannelModel = {};//初始化支付模式对象
	$scope.channelNames = DIC_CONSTANT.getValueAndName("payment", "channel");
    $scope.platformSources =DIC_CONSTANT.getValueAndName("payment", "platform_source");
    $scope.systemNames =DIC_CONSTANT.getValueAndName("payment", "sys_code");

	 //注入字符串转数组并且判断是否在其中的函数
    $scope.strConvertArrContains=$CommonService.strConvertArrContains;
    var formUrl = CONFIG.interface.insertPaymentChannel;
    //修改操作
	 if($routeParams.id!='undefined' && $routeParams.id!=undefined){
		  $scope.showPage=false;//用于判断是新增还是修改的标识 false:修改
		  var params={'id':$routeParams.id};
			$CommonService.httpPost(params,CONFIG.interface.searchPaymentChannelById)
			.success(function(data) {
				formUrl = CONFIG.interface.updatePaymentChannel;
			    $scope.payChannelModel = data.resMsg;
				$("#platformSource").val(data.resMsg.platformSource);
				$checkBoxService.showCheckInfo($scope.platformSources,$("#platformSource").val());
			    $scope.payChannelModel.channelNameCode =data.resMsg.channelNameCode+'';
			    $scope.payChannelModel.sysName= DIC_CONSTANT.getValue("payment", "sys_code",data.resMsg.sysName)+'';
			});
	       
	 }else{
		 $scope.showPage=true;//用于判断是新增还是修改的标识 false:修改
	 }
	 init($scope.payChannelModel);
	//表单提交事件
	var $validationProvider = $injector.get('$validation');
	//新增
	$scope.submit = function(form){
		//获取表单信息，并验证
		$validationProvider.validate(form).success(function(){
			   $("#platformSource").val($checkBoxService.check($scope.platformSources));
			     var params = $("#payChannelModelForm").serializeObject();
			     $http.post(formUrl,params).success(function (data) {
			          if(data.resStatus==CONFIG.CONSTANTS.SUCCESS_STATE){
                          commonUtil.createNotifyAndRedirect(data.resMsg,ALERT_SUC, $location, "/paymentChannelList");
			          }else{
                          commonUtil.createSimpleNotify(data.resMsg,ALERT_ERR);
			          }
			      }).error(function () {
                     commonUtil.createNotifyAndRedirect(data.resMsg,ALERT_ERR, $location, "/paymentChannelList");
			      });
		}).error(function(){
		});
	 };
	//返回事件
	 $scope.back=function(){
	 	 $location.path("/paymentChannelList");
	 }
});
//初始化参数
function init(modelObject){
	modelObject.sort=9999;
	modelObject.state =0;
	modelObject.payStyle=10;
}
