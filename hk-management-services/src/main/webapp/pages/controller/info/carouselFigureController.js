app.controller('carouselFigure',function ($scope,$injector,$timeout,$http,$location,$routeParams,$CommonService,$sce,$rootScope,$checkBoxService) {
	var editor= elementOperateUtil.createKindEditor(CONFIG.PLATFORMS.HKJF,CONFIG.FILEPATHS.INFO_FILEPATH);
	//初始化页面下拉列表
	$scope.positions =DIC_CONSTANT.getValueAndName("info", "carousel_figure_position");
    $scope.types =DIC_CONSTANT.getValueAndName("info", "carousel_figure_type");
    $scope.resideSelects =DIC_CONSTANT.getValueAndName("info", "resideselect");
    $scope.channels =DIC_CONSTANT.getValueAndName("info", "channel");
    //注入字符串转数组并且判断是否在其中的函数
    $scope.strConvertArrContains = $CommonService.strConvertArrContains;
    $scope.showFlag = 0;
    $routeParams.id = $routeParams.param;
    
     //广告轮播类型选择事件
	 $scope.changeType=function(x){
		 var keepGoing = true;
		 angular.forEach($scope.types, function(data,index,array){
			//data等价于array[index]
		    if(keepGoing){
				if(data.value==x){
					$scope.type=data.value+'';
		        	keepGoing = false;
				}
		   }
		});
	 };
	//广告轮播所属板块选择事件
	 $scope.changeResideSelect=function(x){
		 var keepGoing = true;
		 angular.forEach($scope.resideSelects, function(data,index,array){
			//data等价于array[index]
		    if(keepGoing){
				if(data.value==x){
					$scope.resideSelect=data.value+'';
		        	keepGoing = false;
				}
		   }
		});
	 };
    
    //对广告轮播进行修改操作
	 if($routeParams.id!='undefined' && $routeParams.id!=undefined){
		    $scope.showPage=false;//用于判断是新增还是修改的标识 false:修改
			var params={'id':$routeParams.id};
			$CommonService.httpPost(params,CONFIG.interface.findInfomationById)
			.success(function(data) {
				$scope.title = data.resMsg.title;
				$("#channel").val(data.resMsg.channel);
				$checkBoxService.showCheckInfo($scope.channels,$("#channel").val());
				$scope.sort = data.resMsg.sort;
				$scope.url = data.resMsg.url;
				$scope.type=data.resMsg.type+'';
				$scope.position=data.resMsg.position+'';
				$scope.resideSelect=data.resMsg.resideSelect+'';
				$scope.showFlag = data.resMsg.showFlag;
				editor.html(data.resMsg.content);
			});
	 }else{
		 $scope.showPage=true;
	 }
	//表单提交事件
	var $validationProvider = $injector.get('$validation');
	//新增广告轮播
	$scope.submit = function(form){
		//获取表单信息，并验证
   	var content= editor.html();
   	$("#contents").val(content);
   	$("#channel").val($checkBoxService.check($scope.channels));
	$("#infomationId").val(0);
		$validationProvider.validate(form).success(function(){
			    //内容不为空判断
			     if(commonUtil.isEmpty($("#contents").val())){
			    	$("#contentError").css('display', 'block').html("内容不能为空！");
			    	return;
			     }
			     var params = $("#carouselFigureForm").serializeObject();
			      $http.post(CONFIG.interface.insertCarouselFigure,params).success(function (data) { 
			          if(data.resStatus==CONFIG.CONSTANTS.SUCCESS_STATE){
                          commonUtil.createNotifyAndRedirect("添加成功!",ALERT_SUC, $location, "/carouselFigureList");
			          }
			      }).error(function () {
                      commonUtil.createNotifyAndRedirect("添加失败!",ALERT_ERR, $location, "/carouselFigureList");
			      });
		}).error(function(){
		});
	 };
	 //更新广告轮播
	 $scope.update = function(form){
			//获取表单信息，并验证
	    	var content= editor.html();
	    	$("#contents").val(content);
	       	$("#channel").val($checkBoxService.check($scope.channels));
			$validationProvider.validate(form).success(function(){
				    //内容不为空判断
				     if(commonUtil.isEmpty($("#contents").val())){
				    	$("#contentError").css('display', 'block').html("内容不能为空！");
				    	return;
				     }
				     $("#infomationId").val($routeParams.id);
				     var params = $("#carouselFigureForm").serializeObject();
				      $http.post(CONFIG.interface.updateCarouselFigure,params).success(function (data) { 
				          if(data.resStatus==CONFIG.CONSTANTS.SUCCESS_STATE){
                              commonUtil.createNotifyAndRedirect("修改成功!",ALERT_SUC, $location, "/carouselFigureList");
				          }
				      }).error(function (data) {
                          commonUtil.createNotifyAndRedirect(data.resMsg, ALERT_ERR);
				      });
			}).error(function(){
			});
		 };
		 //重置
		 $scope.reset=function(form){
			 $validationProvider.reset(form);
			 editor.html("");
		 };
		 //预览
		 $scope.preview=function(){
		     $("#contents").val(editor.html());
//		     $rootScope.infoNews=$('#carouselFigureForm').serializeObject();
//		     $location.path("/carouselFigureDeatil").search({show:true});
//		     
		     cookieUtil.setCookie("infoNewsCarousel",angular.toJson($('#carouselFigureForm').serializeObject(), true));
		     var data= encryptAndDecryptUtil.encrypt(JSON.stringify({"target":"carouselFigureDeatil","show":true}))
		     commonUtil.directRequest(data,1);
		 };
		 //返回
		 $scope.back=function(){
		     $location.path("/carouselFigureList");
		 };
});





