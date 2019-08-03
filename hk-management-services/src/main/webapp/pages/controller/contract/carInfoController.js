app.controller('carInfoCtrl',function ($scope,$injector,$timeout,$http,$location,$routeParams,$CommonService,$sce,$rootScope,$checkBoxService) {
    $routeParams.id = $routeParams.param;
    $scope.carInfo = {};//页面初始化对象
    var urlCarInfo = CONFIG.interface.addCarInfo;
    $scope.strConvertArrContains = $CommonService.strConvertArrContains;
    //修改操作
	 if($routeParams.id!='undefined' && $routeParams.id!=undefined){
		    $scope.showPage=false;//用于判断是新增还是修改的标识 false:修改
         	urlCarInfo = CONFIG.interface.updateCarInfo;
			var params={'id':$routeParams.id};
			$CommonService.httpPost(params,CONFIG.interface.findCarInfoById)
			.success(function(data) {
				console.log(data);
				$scope.carInfo = data.resMsg;
                //格式化时间
                $scope.carInfo.createTime = dateUtil.dateTime(data.resMsg.createTime);
                $scope.carInfo.modifyTime = dateUtil.dateTime(data.resMsg.modifyTime);
			});
	 }else{
	     //初始化数据
		 $scope.showPage=true;
		 init($scope.carInfo);
	 }
	//表单提交事件
	var $validationProvider = $injector.get('$validation');
	//新增
	$scope.submit = function(form){
		//获取表单信息，并验证
	$validationProvider.validate(form).success(function(){
        var $carBrand = $scope.carInfo.carBrand;
        var $carType = $scope.carInfo.carType;
        var $frameNum = $scope.carInfo.frameNum;
        var $carColor = $scope.carInfo.carColor;
        var $keyNum = $scope.carInfo.keyNum;
			     if(commonUtil.isEmpty($carBrand)){
					$("#carBrandError").css('display', 'block').html("机动车品牌不能为空！");
			    	return;
			     }else if(commonUtil.isEmpty($carType)){
			    	 $("#carTypeError").css('display', 'block').html("车型号不能为空！");
				    	return;
			     }else if(commonUtil.isEmpty($frameNum)){
			    	 $("#frameNumError").css('display', 'block').html("车架号不能为空！");
				    	return;
			     }else if(commonUtil.isEmpty($carColor)){
			    	 $("#carColorError").css('display', 'block').html("颜色不能为空！");
				    	return;
			     }else if(commonUtil.isEmpty($keyNum)){
			    	 $("#keyNumError").css('display', 'block').html("钥匙数量不能为空！");
				    	return;
			     }else if($keyNum > 127){
			    	 $("#keyNumError").css('display', 'block').html("钥匙数量不正确！");
				    	return;
			     }

			     var params =$scope.carInfo;
			     console.log(params);
			      $http.post(urlCarInfo,params).success(function (data) {
			      	  console.log(data);
			          if(data.resStatus==CONFIG.CONSTANTS.SUCCESS_STATE){
			          	console.log(data);
                          commonUtil.createNotifyAndRedirect("保存成功!",ALERT_SUC, $location, "/searchCarInfoList");
			          }
			      }).error(function () {
                      commonUtil.createNotifyAndRedirect("添加失败!",ALERT_ERR, $location, "/searchCarInfoList");
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
		 $location.path("/searchCarInfoList");
	};
	$scope.timeChange = function (id) {
		 $scope.carInfo[id] = $("#" + id).val();
	}
	
});
//初始化radio选中
function init(Object) {

}
