//卡券产品管理controller
app.controller('couponProductCtrl',function ($scope,$injector,$http,$location,$routeParams,$checkBoxService) {
    $scope.couponProduct = {};//初始化卡券产品
    $scope.couponScenesDic = DIC_CONSTANT.getValueAndName("vas","coupon_use_scene");//获取卡券使用场景数据字典
    $scope.actionScopeDic = DIC_CONSTANT.getValueAndName("invest","action_scope");//获取卡券作用范围数据字典
    $scope.couponTypeDic = DIC_CONSTANT.getValueAndName("vas","coupon_product_type");//获取卡券产品类型数据字典
    $scope.couponBidProductTypeDic = DIC_CONSTANT.getValueAndName("vas","coupon_bid_product_type");//获取适用卡券类型的标地产品
    $scope.couponProduct.type = 0;//默认选中加息券
    $scope.couponProduct.deadlineType = 1;//默认为截止日期
    $scope.couponProduct.bidProductTypeRange = "";//默认标的产品范围为空
    $scope.couponProduct.beginTime = dateUtil.date(new Date());//初始化卡券使用开始时间
    $scope.couponProduct.endTime = dateUtil.date(new Date());//初始化卡券使用截止时间
    $scope.couponProduct.amountMin = 100;//初始化卡券最小投资金额
    $scope.couponProduct.amountMax = 99999999.99;//初始化卡券使最大投资金额
    if (commonUtil.isNotEmpty($scope.couponScenesDic)){
        $scope.couponProduct.couponScenes = $scope.couponScenesDic[0].value;//添加时默认场景为第一个
    }
    var formUrl = CONFIG.interface.addCouponProduct;//默认为新增卡券产品
    if (commonUtil.isNotEmpty($routeParams.param)){
        formUrl = CONFIG.interface.updateCouponProduct;
        $scope.couponProduct = JSON.parse($routeParams.param);
        $checkBoxService.showCheckInfo($scope.couponBidProductTypeDic,$scope.couponProduct.bidProductTypeRange)//卡券标的产品范围回显
        $scope.couponProduct.beginTime = $scope.couponProduct.beginTime > 0 ? dateUtil.date($scope.couponProduct.beginTime) : dateUtil.date(new Date());//格式化卡券使用有效开始时间
        $scope.couponProduct.endTime = $scope.couponProduct.endTime > 0 ? dateUtil.date($scope.couponProduct.endTime) : dateUtil.date(new Date());//格式化卡券使用有效截止时间
    }
    //表单提交事件
    var $validationProvider = $injector.get('$validation');
    $scope.saveAndUpdate = function (form) {
        //不为提现券，校验卡券标的产品范围是否为空
        if ($("#bidProductTypeRange").length != 0){
            $("#bidProductTypeRange").val($checkBoxService.check($scope.couponBidProductTypeDic));
            if (commonUtil.isEmpty($("#bidProductTypeRange").val())){
                $("#bidProductTypeRangeError").html("此元素不能为空！");
                return;
            }else {
                $("#bidProductTypeRangeError").html("");
            }
        }
        $scope.couponProduct.bidProductTypeRange = $checkBoxService.check($scope.couponBidProductTypeDic);
        $validationProvider.validate(form).success(function () {
            var param = $("#dataForm").serializeObject();
            // var param = $scope.couponProduct;
            param.couponScenes = param.couponScenes.split(":")[1];
            $http.post(formUrl,param).success(function (data) {
                if (data.resStatus === CONFIG.CONSTANTS.SUCCESS_STATE){
                    commonUtil.createNotifyAndRedirect("保存成功！",ALERT_SUC,$location,"/couponProductList");
                }else {
                    commonUtil.createSimpleNotify(data.resMsg,ALERT_ERR);
                }
            }).error(function () {
                commonUtil.createNotifyAndRedirect("保存失败！",ALERT_ERR,$location,"/couponProductList");
            })
        }).error(function () {
            //表单校验失败
        })
    };

    $scope.back = function () {
        $location.path("/couponProductList");
    };
    
    $scope.timeChange = function (id) {
        $scope.couponProduct[id] = $("#" + id).val();
    };

    $scope.giveCouponDetailToUser = function () {
        if (commonUtil.isEmpty(dataTableUtil.getCheckedVal())){
            commonUtil.createSimpleNotify("请选择至少一个卡券产品进行赠送!", ALERT_WARN);
            return;
        }
        $("#couponProductIds").val(dataTableUtil.getCheckedVal());
        //触发选择用户列表清空按钮
        $("#usersTable-searchForm-cleanBtn").click();
        $("#userInfoModal").modal('show');
    };
    
    $scope.giveUserCouponDetail = function () {
        if (commonUtil.isEmpty(dataTableUtil.getCheckedVal())){
            $("#showErroSpan").text("请选择至少一个卡券产品进行赠送!");
            return;
        }
        if (commonUtil.isEmpty(dataTableUtil.getCheckedVal("usersTable"))){
            $("#showErroSpan").text("请选择至少一个用户进行赠送!");
            return;
        }
        if (commonUtil.isEmpty($("#reason").val())){
            $("#showErroSpan").text("请输入赠送原因!");
            return;
        }

        //赠送成功后，清空选择用户赠送卡券页面的值
        function clear() {
            $("#userInfoModal").modal('hide');
            $("#couponProductIds").val('');
            //撤销选中的用户
            $(".iCheck:checked").prop("checked",false);
            $("#reason").val('');
            $("#showErroSpan").text('');
        }
        commonUtil.confirmRequest(encryptAndDecryptUtil.encrypt(JSON.stringify({
            userIds:dataTableUtil.getCheckedVal("usersTable").toString(),
            couponIds:dataTableUtil.getCheckedVal().toString(),
            reason:$("#reason").val(),
            ajaxUrl:CONFIG.interface.distributeCouponToUser
        })),"赠送卡券","确定赠送卡券？");
        clear();
    };


});