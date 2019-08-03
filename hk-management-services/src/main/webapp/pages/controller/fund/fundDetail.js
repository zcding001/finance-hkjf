/**
 * 股权类型信息前端控制器
 */
app.controller('fundDetailCtrl',function ($scope,$injector,$timeout,$http,$location,$routeParams,$CommonService) {
    var params = {"id":$routeParams.id};
    $CommonService.httpPost(params,CONFIG.interface.findFundInfoById)
        .success(function (data) {
        	debugger;
            console.log(data);
            $scope.info = data.resMsg;
            $scope.info.type=DIC_CONSTANT.getName("fund", "project_type", data.resMsg.projectId);
            $("#introduction").html(data.resMsg.introduction);
            $scope.info.remark = data.resMsg.remark;
        })

});