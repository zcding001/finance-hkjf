//合同类型维护controller
app.controller('contractTypeCtrl',function ($scope,$injector,$http,$location,$routeParams) {
    $scope.contractType = {};//初始化合同类型对象
    var formUrl = CONFIG.interface.addContractType;//默认为新增合同类型URL
    $scope.typeAndName = DIC_CONSTANT.getValueAndName("contract","type");
    if (commonUtil.isNotEmpty($routeParams.param)){
        formUrl = CONFIG.interface.updateContractType;
        $scope.contractType = JSON.parse($routeParams.param);
        elementOperateUtil.disableElement("type");
    }
    //表单提交事件
    var $validationProvider = $injector.get('$validation');
    $scope.saveAndUpdate = function (form) {
        $validationProvider.validate(form).success(function () {
            var param = $("#contractTypeForm").serializeObject();
            if (commonUtil.isNotEmpty(param.type)){
                param.name = DIC_CONSTANT.getName("contract","type",param.type);
            }
            $http.post(formUrl,param).success(function (data) {
                if (data.resStatus === CONFIG.CONSTANTS.SUCCESS_STATE){
                    //删除容器中合同类型及名称，便于获取时实时更新该数据集合
                    containerUtil.delete("contractTypeAndName");
                    commonUtil.createNotifyAndRedirect("保存成功！",ALERT_SUC,$location,"/contractTypeList");
                }else {
                    commonUtil.createSimpleNotify(data.resMsg,ALERT_ERR);
                }
            }).error(function () {
                commonUtil.createNotifyAndRedirect("保存失败！",ALERT_ERR,$location,"/contractTypeList");
            })
        }).error(function () {
            //表单校验失败
        })
    };
    $scope.back = function () {
        $location.path("/contractTypeList");
    }
});