//合同模板内容维护controller
app.controller('contractTemplateCtrl',function ($scope,$injector,$http,$location,$routeParams) {
    elementOperateUtil.addFormValidElementAttr();
    $scope.contractTypeAndName = CONTRACTTYPE_CONSTANT.getData(1);//默认添加获取已启用的合同类型
    var editor = elementOperateUtil.createKindEditor(CONFIG.PLATFORMS.HKJF,CONFIG.FILEPATHS.INFO_FILEPATH,'content');
    $scope.contractTemplate = {};//初始化合同模板内容
    var formUrl = CONFIG.interface.addContractTemplate;//默认新增合同模板内容
    document.getElementById("contractType").disabled = false;
    if($routeParams.param === "update" && commonUtil.isNotEmpty(containerUtil.get("updateTemplate"))){
        $scope.contractTypeAndName = CONTRACTTYPE_CONSTANT.data;//修改合同时获取所有的合同类型
        formUrl = CONFIG.interface.updateContractTemplate;//传递过参数则修改合同模板内容
        $scope.contractTemplate = containerUtil.get("updateTemplate");
        editor.html($scope.contractTemplate.content);//将合同模板内容添加至kindeditor中，用于展示合同内容
        $("#content").val($scope.contractTemplate.content);//将合同模板内容添加至content元素中，用于传递合同内容
        document.getElementById("contractType").disabled = true;//编辑时合同类型下拉框不允许进行编辑
    }
    //表单提交事件
    var $validationProvider = $injector.get('$validation');
    $scope.saveAndUpdate = function (form) {
        //获取合同模板内容进行校验
        if (commonUtil.isEmpty($("#content").val())){
            $("#contentError").css('display', 'block').html("内容不能为空！");
            return;
        }
        $validationProvider.validate(form).success(function () {
            var param = $("#dataForm").serializeObject();
            $http.post(formUrl,param).success(function (data) {
                if (data.resStatus === CONFIG.CONSTANTS.SUCCESS_STATE){
                    commonUtil.createNotifyAndRedirect("保存成功!",ALERT_SUC,$location,"/contractTemplateList");
                    containerUtil.delete("updateTemplate");
                }else {
                    commonUtil.createSimpleNotify(data.resMsg,ALERT_ERR);
                }
            }).error(function () {
                commonUtil.createNotifyAndRedirect("保存失败!",ALERT_ERR,$location,"/contractTemplateList");
            })
        }).error(function () {
            //表单校验失败
        })
    }
    
    $scope.back = function () {
        $location.path("/contractTemplateList");
    }
})