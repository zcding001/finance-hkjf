app.controller('updateQuestionnaireCtrl',function ($scope,$injector,$timeout,$http,$location,$routeParams,$CommonService,$sce,$rootScope,$checkBoxService) {
	var params={'informationNewsId':$routeParams.param};
	$CommonService.httpPost(params,CONFIG.interface.findInfoQuesetionById)
	.success(function(data) {
		$scope.info=angular.fromJson(data.resMsg);
		$scope.problems=$scope.info.problemsList;
	});
	//更新调查问卷
	$scope.updateQuestion =function(option){
		 window.location.href = commonUtil.getRouteUrl({param:$routeParams.param,questionnaireId:option.opt.questionnaireId,target:'questionnaire'});
	}
	 //返回
	 $scope.back=function(){
	     $location.path("/newsList");
	 };
});





