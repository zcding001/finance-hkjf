/**
 * 股权类型信息前端控制器
 */
app.controller('fundAdvisoryDetailCtrl',function ($scope,$injector,$timeout,$http,$location,$routeParams,$CommonService) {
	debugger;
    var params = {"id":$routeParams.id};
    $CommonService.httpPost(params,CONFIG.interface.findFundAdvisoryInfo)
        .success(function (data) {
        	debugger;
            console.log(data);
            $scope.info = data.resMsg;
            $scope.info.remark = data.resMsg.remark;
        })

});