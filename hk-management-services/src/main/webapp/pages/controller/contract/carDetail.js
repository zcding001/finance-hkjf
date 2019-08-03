/**
 * 车辆信息前端控制器
 */
app.controller('carDetailCtrl',function ($scope,$injector,$timeout,$http,$location,$routeParams,$CommonService) {
	debugger;
    var params = {"id":$routeParams.id};
    $CommonService.httpPost(params,CONFIG.interface.findCarInfoById)
        .success(function (data) {
            console.log(data);
            $scope.info = data.resMsg;
        })

});