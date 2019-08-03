/**
 * 打印汽车合同前端控制器
 */
app.controller('printCarContractCtrl',function ($scope,$injector,$timeout,$http,$location,$routeParams,$CommonService) {
    var params = {"id":$routeParams.id,"page":$routeParams.page,"optType":"printPage"};
    $scope.carContract = {};//页面初始化对象
    $scope.num = 123;
    $CommonService.httpPost(params,CONFIG.interface.findCarContractById)
        .success(function (data) {
            console.log(data);
            $scope.carContract = data.params.carContract;
            $scope.partyA = data.params.partyA;
            $scope.partyB = data.params.partyB;

            containerUtil.add("carContract", JSON.stringify($scope.carContract));
            containerUtil.add("partyA", JSON.stringify($scope.partyA));
            containerUtil.add("partyB", JSON.stringify($scope.partyB));
            containerUtil.add("optType", "printPage");

            //加载合同页
            var gotoUrl=$routeParams.page;
            //初始化
            $("#printPage").load("pages/template/car/contract/"+gotoUrl+".html");

        })

});