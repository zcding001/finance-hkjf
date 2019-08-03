//积分规则维护的controller
app.controller('pointRuleCtrl',function ($scope,$injector,$http,$location,$routeParams) {
    $scope.pointRule = {};//初始化积分规则对象
    var formUrl = CONFIG.interface.addPointRule;//默认为新增积分规则URL
    if (commonUtil.isNotEmpty($routeParams.param)){
        formUrl = CONFIG.interface.updatePointRule;
        $scope.pointRule = JSON.parse($routeParams.param);
    }
    //表单提交事件
    var $validationProvider = $injector.get('$validation');
    $scope.saveAndUpdate = function (form) {
        $validationProvider.validate(form).success(function () {
            var param = $("#pointRuleForm").serializeObject();
            console.debug(param)
            $http.post(formUrl,param).success(function (data) {
                console.debug(data);
                if (data.resStatus === CONFIG.CONSTANTS.SUCCESS_STATE){
                    commonUtil.createNotifyAndRedirect("保存成功！",ALERT_SUC,$location,"/pointRuleList");
                }
            }).error(function () {
                commonUtil.createNotifyAndRedirect("保存失败！",ALERT_ERR,$location,"/pointRuleList");
            })
        }).error(function () {
            //表单校验失败
        });
    }
    $scope.back = function () {
        $location.path("/pointRuleList");
    }
})