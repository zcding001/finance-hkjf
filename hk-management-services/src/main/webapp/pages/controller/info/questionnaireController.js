app.controller('questionnaireCtrl',function ($scope,$injector,$timeout,$http,$location,$routeParams,$CommonService,$sce,$rootScope,$checkBoxService) {
	$scope.types =DIC_CONSTANT.getValueAndName("info", "question_type");
	$scope.visible=false;
	$scope.type="3";//默认为文本类型
	$scope.showPage =false;
	//更新调查问卷
	 if($routeParams.questionnaireId!='undefined' && $routeParams.questionnaireId!=undefined){
		 var params={'questionnaireId':$routeParams.questionnaireId,'infomationId': $routeParams.param};
			$CommonService.httpPost(params,CONFIG.interface.findQuestionAndItem)
			.success(function(data) {
				$scope.content=data.resMsg.content;
				$scope.type=data.resMsg.type+'';
				if($scope.type != '3'){
					$scope.visible=true;
				}else{
					$scope.visible=false;
				}
				$scope.options=data.resMsg.questionnaireItems==null?[{}]:data.resMsg.questionnaireItems;
			});
			$scope.showPage =true;
	 }else{
		$scope.showPage =false;
		$scope.options = [{}];
	 }
	
	//添加选项
	$scope.addOption = function($index){ 
	    $scope.options.splice($index + 1, 0,{});  
	}  
	//删除选项
	$scope.delOption = function($index){
	    $scope.options.splice($index, 1);  
	} 
	//问题类型事件
	$scope.changeQuestionType=function(type){
		if(type != 3){
			$scope.visible=true;
		}else{
			$scope.visible=false;
		}
	}
	//表单提交事件
	var $validationProvider = $injector.get('$validation');
	$scope.addProblems = function(form,submitType){
		$("#error").css('display', 'block').html("");
		$validationProvider.validate(form).success(function(){
		     var params = $("#questionForm").serializeObject();
		     //封装调查问卷的数组对象
		     var problemsItem=new Object();
		     problemsItem.content=params.content;
		     problemsItem.type=params.type;
		     
		     var problems = new Object();
		     problems.informationNewsId = $routeParams.param;
		     problems.problemsList=problemsItem;
		     
		    var titleArr = params.optionTitle;
		    var contentArr = params.optionContent;
		    var arr = new Array();
		   
		    if(!commonUtil.isEmpty(titleArr) && !commonUtil.isEmpty(contentArr)){
		    	if(angular.isArray(titleArr) && (titleArr.length != contentArr.length)){
			    	$("#error").css('display', 'block').html("请完整填写选项内容！");
			    	return;
		        }
		    	if(!angular.isArray(titleArr)){
		    		var option =new Object();
		    		option.optionTitle=titleArr;
			    	option.optionContent=contentArr;
			    	arr.push(option);
		        }else{
				    for(var i=0;i<titleArr.length;i++){
				    	var option =new Object();
				    	option.optionTitle=titleArr[i];
				    	option.optionContent=contentArr[i];
				    	arr.push(option);
				    }
		        }
		    }else{
		    	if(params.type == 1 || params.type==2){
		    		$("#error").css('display', 'block').html("请添加选项内容！");
			    	return;
		    	}
		    }
		    problemsItem.questionnaireItems=arr;
		    var dataParams={'questionnaireJson':angular.toJson(problems),"questionId":(submitType=='updateQuestion' && commonUtil.isEmpty($routeParams.questionnaireId))?0:$routeParams.questionnaireId};
		    var postUrl=CONFIG.interface.insertInfoQuestionnaire;
		    if(submitType=='updateQuestion' ){
		    	postUrl=CONFIG.interface.updateInfoQuestionnaire;
		    }
		      $http.post(postUrl,dataParams).success(function (data) { 
		          if(data.resStatus==CONFIG.CONSTANTS.SUCCESS_STATE){
		          	if(submitType=="save" || submitType=="updateQuestion"){
			          	 commonUtil.createNotifyAndRedirect("添加成功!",ALERT_SUC, $location, "/newsList");
		          	}else{
			          	 commonUtil.createNotifyAndRedirect("添加成功!",ALERT_SUC, $location, "/questionnaire","refresh");
		          	}
		          }
		      }).error(function () {
                  commonUtil.createNotifyAndRedirect("添加失败!",ALERT_ERR, $location, "/newsList");
		      });
		}).error(function(){
		});
	}
	 //返回
	 $scope.back=function(){
	     $location.path("/newsList");
	 };
});





