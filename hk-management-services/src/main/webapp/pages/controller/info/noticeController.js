app.controller('noticeCtrl',function ($scope,$injector,$timeout,$http,$location,$routeParams,$CommonService,$sce,$rootScope,$checkBoxService){
    var editor = elementOperateUtil.createKindEditor(CONFIG.PLATFORMS.HKJF,CONFIG.FILEPATHS.INFO_FILEPATH,'content');
	//初始化页面下拉列表
    $scope.positions =DIC_CONSTANT.getValueAndName("info", "notice_position");
    $scope.channels =DIC_CONSTANT.getValueAndName("info", "channel");
	$routeParams.id = $routeParams.param;
	 //对公告进行修改操作
	 if($routeParams.id!='undefined' && $routeParams.id!=undefined){
		    $scope.showPage=false;//用于判断是新增还是修改的标识 false:修改
			var params={'id':$routeParams.id};
			$CommonService.httpPost(params,CONFIG.interface.findInfomationById)
			.success(function(data) {
				$scope.title = data.resMsg.title;
				$("#channel").val(data.resMsg.channel);
				$checkBoxService.showCheckInfo($scope.channels,$("#channel").val());
				$scope.position=data.resMsg.position+'';
				$scope.source=data.resMsg.source;
				editor.html(data.resMsg.content);
			});
	 }else{
		 $scope.showPage=true;
	 }
	//表单提交事件
	var $validationProvider = $injector.get('$validation');
	//新增公告
	$scope.submit = function(form){
		//获取表单信息，并验证
    	var content= editor.html();
    	$("#contents").val(content);
    	$("#infomationId").val(0);
    	$("#channel").val($checkBoxService.check($scope.channels));
		$validationProvider.validate(form).success(function(){
			    //内容不为空判断
			     if(commonUtil.isEmpty($("#contents").val())){
			    	$("#contentError").css('display', 'block').html("内容不能为空！");
			    	return;
			     }
			     var params = $("#infoForm").serializeObject();
			      $http.post(CONFIG.interface.insertInfomations,params).success(function (data) { 
			          if(data.resStatus==CONFIG.CONSTANTS.SUCCESS_STATE){
                          commonUtil.createNotifyAndRedirect("添加成功!",ALERT_SUC, $location, "/noticeList");
			          }
			      }).error(function () {
                      commonUtil.createNotifyAndRedirect("添加失败!",ALERT_ERR, $location, "/noticeList");
			      });
		}).error(function(){
		});
	 };
	 //更新公告
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
				     var params = $("#infoForm").serializeObject();
				      $http.post(CONFIG.interface.updateInformationNews,params).success(function (data) { 
				          if(data.resStatus==CONFIG.CONSTANTS.SUCCESS_STATE){
                              commonUtil.createNotifyAndRedirect("修改成功!",ALERT_SUC, $location, "/noticeList");
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
			 editor.html("");
		 };
		 //预览
		 $scope.preview=function(){
		     $("#contents").val(editor.html());
//		     $rootScope.infopreview=$('#infoForm').serializeObject();
//		     $location.path("/noticeDeatil").search({show:true});
		     cookieUtil.setCookie("infopreview",angular.toJson($('#infoForm').serializeObject(), true));
		     var data= encryptAndDecryptUtil.encrypt(JSON.stringify({"target":"noticeDeatil","show":true}))
		     commonUtil.directRequest(data,1);
		 };
		 //返回
		 $scope.back=function(){
		     $location.path("/noticeList");
		 };
});


