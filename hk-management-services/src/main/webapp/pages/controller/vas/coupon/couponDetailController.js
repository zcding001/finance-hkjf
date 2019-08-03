//卡券详情管理controller
app.controller('couponDetailCtrl',function ($scope,$injector,$http,$location,$routeParams,$CommonService) {
    //获取卡券产品列表
    $CommonService.httpPost({pageSize:100000},CONFIG.interface.giveCouponProductList).success(function (data) {
        $scope.couponProducts = data.resMsg.data;
    }).error(function () {
       commonUtil.createSimpleNotify("系统繁忙！",ALERT_ERR);
    });

    $scope.exportCouponDetailList = function () {
        //列表没有数据，提示用户先生成卡券详情再进行导出
        if ($("#mDataTable").dataTable().fnGetData().length == 0){
            commonUtil.createNotifyAndRedirect("请选生成卡券，再进行导出！",ALERT_WARN);
            return;
        }
        exportExcel("生成的卡券详情");
    }

    $scope.generateCouponDetailList = function () {
        if (commonUtil.isEmpty($("#couponId").val())){
            commonUtil.createSimpleNotify("请选择卡券产品进行生成！",ALERT_WARN);
            return;
        }
        var result = $("#num").val().match(/^\d+$/);
        if (result == null || result[0] == "0"){
            commonUtil.createSimpleNotify("请输入生成数量！",ALERT_WARN);
            return;
        }
        $("#mDataTable").DataTable().search("").draw();
    }
})