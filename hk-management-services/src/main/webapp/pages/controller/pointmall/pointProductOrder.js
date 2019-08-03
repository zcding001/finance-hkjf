//积分商品订单分类维护的controller
app.controller('pointProductOrder',function ($scope,$injector,$http,$location,$routeParams) {
    //初始化配送方式
    DIC_CONSTANT.initSelect('sendWay','point_product','send_way');
    //初始化订单状态
    DIC_CONSTANT.initSelect('state','point_order','state');


    //审核订单操作
    $scope.createCheckPanel=function (id) {
        if (id == null){
            id = dataTableUtil.getCheckedVal();
        }
        if (commonUtil.isEmpty(id)){
            commonUtil.createSimpleNotify("请至少选择一个订单!", ALERT_WARN);
            return;
        }
        $("#ids").attr("name", "checkIds");
        $("#ids").val(id);
        var contents = '';
        contents += '审核结果：<input name="state" type="radio"  value="1" checked="checked" /> 通过     ' +
            '<input  name="state" type="radio"   value="4" />拒绝<br><br>';
        contents += "<span id='refuseSpan'></span>";
        contents += '<span id="productCheckError" style="color: red"></span>'
        commonUtil.createCustomConfirmBox("审核商品订单", contents, CONFIG.interface.checkOrder,null,function (data) {
            $("#modalText").modal('hide');
            $('#mDataTable').DataTable().search("").draw();
            commonUtil.createSimpleNotify(data.resMsg, ALERT_SUC);
        });
    }

    //填写快递单号
    $scope.createCourierNoPanel=function (id) {
        var contents = '';
        contents += '填写快递单号：<input name="courierNo"/> <input type="hidden" name="id" value="'+id+'"/> ';
        contents += "<span id='refuseSpan'></span>";
        contents += '<span id="productCheckError" style="color: red"></span>'
        commonUtil.createCustomConfirmBox("填写快递单号", contents, CONFIG.interface.updateCourierNo,null,commonUtil.customConfirmStandardAfterExecuteFn);
    }

})