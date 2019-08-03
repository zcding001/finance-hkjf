app.controller('newsCtrl',function ($scope,$injector,$timeout,$http,$location,$routeParams,$CommonService,$sce,$rootScope,$checkBoxService) {
	var editor= elementOperateUtil.createKindEditor(CONFIG.PLATFORMS.HKJF,CONFIG.FILEPATHS.INFO_FILEPATH);
	//初始化页面下拉列表
	$scope.positions =DIC_CONSTANT.getValueAndName("info", "position");
    $scope.types =DIC_CONSTANT.getValueAndName("info", "type");
    $scope.resideSelects =DIC_CONSTANT.getValueAndName("info", "resideselect");
    $scope.channels =DIC_CONSTANT.getValueAndName("info", "channel");

    $routeParams.id = $routeParams.param;
    
     //新闻类型选择事件
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
	//新闻所属板块选择事件
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
	 //图片异步上传
	 $scope.upload = function (id, opts) {
	        $CommonService.upload(id, opts);
	 };
    //对新闻进行修改操作
	 if($routeParams.id!='undefined' && $routeParams.id!=undefined){
		    $scope.showPage=false;//用于判断是新增还是修改的标识 false:修改
			var params={'id':$routeParams.id};
			$CommonService.httpPost(params,CONFIG.interface.findInfomationById)
			.success(function(data) {
				$scope.title = data.resMsg.title;
				$("#channel").val(data.resMsg.channel);
				$checkBoxService.showCheckInfo($scope.channels,$("#channel").val());
				$scope.source = data.resMsg.source;
                $("#Pic_imgUrl").displayBack("Pic_imgUrl", {}, CONFIG.CONSTANTS.IMG_URL_ROOT + data.resMsg.imgUrl);
				$scope.keyword = data.resMsg.keyword;
				$scope.sort = data.resMsg.sort;
				$scope.type=data.resMsg.type+'';
				$scope.position=data.resMsg.position+'';
				$scope.resideSelect=data.resMsg.resideSelect+'';
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
   	$("#channel").val($checkBoxService.check($scope.channels));
	$("#infomationId").val(0);
		$validationProvider.validate(form).success(function(){
			    //内容不为空判断
			     if(commonUtil.isEmpty($("#contents").val())){
			    	$("#contentError").css('display', 'block').html("内容不能为空！");
			    	return;
			     }
			     var params = $("#infoNewsForm").serializeObject();
			      $http.post(CONFIG.interface.insertNews,params).success(function (data) { 
			          if(data.resStatus==CONFIG.CONSTANTS.SUCCESS_STATE){
                          commonUtil.createNotifyAndRedirect("添加成功!",ALERT_SUC, $location, "/newsList");
			          }
			      }).error(function () {
                      commonUtil.createNotifyAndRedirect("添加失败!",ALERT_ERR, $location, "/newsList");
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
				     var params = $("#infoNewsForm").serializeObject();
				      $http.post(CONFIG.interface.updateNews,params).success(function (data) { 
				          if(data.resStatus==CONFIG.CONSTANTS.SUCCESS_STATE){
                              commonUtil.createNotifyAndRedirect("修改成功!",ALERT_SUC, $location, "/newsList");
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
//		     $rootScope.infoNews=$('#infoNewsForm').serializeObject();
//		     $location.path("/newsDeatil").search({show:true});
		     cookieUtil.setCookie("infoNews",angular.toJson($('#infoNewsForm').serializeObject(), true));
		     var data= encryptAndDecryptUtil.encrypt(JSON.stringify({"target":"newsDeatil","show":true}))
		     commonUtil.directRequest(data,1);
		 };
		 //返回
		 $scope.back=function(){
		     $location.path("/newsList");
		 };
});





