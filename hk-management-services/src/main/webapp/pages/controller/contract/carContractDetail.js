/**
 * 汽车合同前端控制器
 */
app.controller('carContractDetailCtrl',function ($scope,$injector,$timeout,$http,$location,$routeParams,$CommonService) {
    var params = {"id":$routeParams.id};
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
            containerUtil.add("optType", "showPage");

            //加载合同页
            var uselist=$(".oli1");
            var gotoUrl='carbuysellcontract';
            //初始化
            $("#showContract").load("pages/template/car/contract/"+gotoUrl+".html");
            uselist.click(function(){
                gotoUrl=$(this).children("a").attr("gotoUrl");
                $("#showContract").load("pages/template/car/contract/"+gotoUrl+".html");

                $(this).children().css({"color":"#f08300","border-bottom":"#f7931e 1px solid"});
                uselist.not(this).children().css({"color":"#343434","border-bottom":"none"})
            });

            //打印合同
            $scope.toPrintPage = function () {
                $location.path("/toPrintCarContract").search({"id":$routeParams.id,"page":gotoUrl});
            }
        })

});