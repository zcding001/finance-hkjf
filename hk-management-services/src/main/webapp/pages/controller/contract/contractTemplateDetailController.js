//合同模板内容详情查看controller
app.controller('contractTemplateDetailCtrl',function ($scope,$http,$CommonService,$location,$routeParams) {
    var contractTemplateDetail = "合同不见了";//初始化合同模板内容详情
    var params = {"id":$routeParams.param};
    $CommonService.httpPost(params,CONFIG.interface.findContractTemplateDetail)
        .success(function (data) {
            contractTemplateDetail = data.resMsg;
            $("#contractTemplateDetail").html(contractTemplateDetail);
        })
})