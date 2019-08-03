//app版本更新规则管理controller
app.controller('appVersionRuleCtrl',function ($scope,$injector,$http,$location,$routeParams,$checkBoxService) {
    $scope.appVersionRule = {};//初始化app版本更新规则
    $scope.appVersionRule.platform = "1,2";//默认选中两个平台
    $scope.appVersionPlatformDic = DIC_CONSTANT.getValueAndName("vas","app_version_platform");//获取app版本更新平台数据字典
    let editor = elementOperateUtil.createKindEditor(CONFIG.PLATFORMS.HKJF,CONFIG.FILEPATHS.INFO_FILEPATH,'content');
    let formUrl = CONFIG.interface.addAppVersionRule;

    //表单提交事件
    let $validationProvider = $injector.get('$validation');
    $scope.saveAndUpdate = function (form) {
        let $platform = $("#platform");
        $platform.val($checkBoxService.check($scope.appVersionPlatformDic));
        if (commonUtil.isEmpty($platform.val())){
            $("#platformError").html("此元素不能为空！");
            return;
        }else {
            $("#platformError").html("");
        }
        $validationProvider.validate(form).success(function () {
           let param = $("#dataForm").serializeObject();
           $http.post(formUrl,param).success(function (data) {
               if (data.resStatus === CONFIG.CONSTANTS.SUCCESS_STATE){
                   commonUtil.createNotifyAndRedirect("保存成功！",ALERT_SUC,$location,"/appVersionRuleList");
               }else {
                   commonUtil.createSimpleNotify(data.resMsg,ALERT_ERR);
               }
           }).error(function () {
               commonUtil.createNotifyAndRedirect("保存失败！",ALERT_ERR,$location,"/appVersionRuleList");
           })
        }).error(function () {
            //表单校验失败
        })
    };

    $scope.back = function () {
        $location.path("/appVersionRuleList");
    };
});