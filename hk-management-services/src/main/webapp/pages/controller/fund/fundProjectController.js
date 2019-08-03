/**
 * 股权类型信息前端控制器
 */
app.controller('fundProjectCtrl',function ($scope,$injector,$timeout,$http,$location,$routeParams,$CommonService) {

    var params = {"id":$routeParams.param};
    $CommonService.httpPost(params,CONFIG.interface.fundProjectInfo)
        .success(function (data) {
            console.log(data);
            $scope.info = data.resMsg;
            $scope.info.type=DIC_CONSTANT.getName("fund", "project_type", data.resMsg.type);
            $scope.info.parentType=DIC_CONSTANT.getName("fund", "project_parent_type", data.resMsg.parentType);
            $("#introduction").html(data.resMsg.introduction);
            $scope.info.remark = data.resMsg.remark;
        })

});
