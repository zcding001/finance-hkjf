//会员等级标准管理controller
app.controller('vipGradeStandardCtrl',function ($scope,$injector,$http,$location,$routeParams) {
    elementOperateUtil.addFormValidElementAttr();//初始化label样式和错误提示样式
    $scope.vipGradeStandard = {};//初始化会员等级标准对象
    $scope.vipGrade = DIC_CONSTANT.getValueAndName("vas","vip_grade");
    var formUrl = CONFIG.interface.addVipGradeStandard;//默认为新增会员等级标准
    elementOperateUtil.disableElement(["level","growthValMin"]);//禁用等级和等级下限
    if (commonUtil.isNotEmpty($routeParams.param)){
        formUrl = CONFIG.interface.updateVipGradeStandard;
        $scope.vipGradeStandard = JSON.parse($routeParams.param);
    }else {
        $scope.vipGradeStandard.level = $routeParams.level - 0;
        $scope.vipGradeStandard.growthValMin = $routeParams.growthValMin - 0;
    }
    //表单提交事件
    var $validationProvider = $injector.get('$validation');
    $scope.saveAndUpdate = function (form) {
        $validationProvider.validate(form).success(function () {
            var param = $("#dataForm").serializeObject();
            //由于disable的属性值无法获取，获取disable属性的值
            param.level = $("#level").val();
            param.growthValMin = $("input[name='growthValMin']").val();
            $http.post(formUrl,param).success(function (data) {
                if (data.resStatus === CONFIG.CONSTANTS.SUCCESS_STATE){
                    commonUtil.createNotifyAndRedirect("保存成功！",ALERT_SUC,$location,"/vipGradeStandardList");
                }else {
                    commonUtil.createSimpleNotify(data.resMsg,ALERT_ERR);
                }
            }).error(function () {
                commonUtil.createNotifyAndRedirect("保存失败！",ALERT_ERR,$location,"/vipGradeStandardList");
            })
        }).error(function () {
            //表单校验失败
        })
    }
    $scope.back = function () {
        $location.path("/vipGradeStandardList");
    }
})