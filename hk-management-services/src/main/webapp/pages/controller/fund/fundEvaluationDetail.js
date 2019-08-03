/**
 * 风险测评答案前端控制器
 */
app.controller('fundEvaluationDetailCtrl',function ($scope,$injector,$timeout,$http,$location,$routeParams,$CommonService) {

    var params = {"id":$routeParams.id};
    debugger;
    $CommonService.httpPost(params,CONFIG.interface.fundEvaluationDetail)
        .success(function (data) {
            console.log(data);
            $scope.detail = data.resMsg;
            //$scope.detail.type=DIC_CONSTANT.getName("fund", "project_type", data.resMsg.type);
            //$("#type").html(data.resMsg.type);
        })

});
