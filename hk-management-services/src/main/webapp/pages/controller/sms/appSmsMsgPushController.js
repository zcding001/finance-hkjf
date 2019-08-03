//app端消息推送controller
app.controller('appMsgPushCtr',function ($scope,$injector,$http,$location,$routeParams,$CommonService) {
    $scope.targetPlatforms = DIC_CONSTANT.getValueAndName("sms", "app_msg_target_platform");
    $scope.targetUsers = DIC_CONSTANT.getValueAndName("sms", "app_msg_target_user");
    $scope.targetUserTags = DIC_CONSTANT.getValueAndName("sms", "app_msg_target_user_tag");
    $scope.targetUserTagSets = DIC_CONSTANT.getValueAndName("sms", "app_msg_target_user_tag_set");
    $scope.pushModes = DIC_CONSTANT.getValueAndName("sms", "app_msg_push_mode");
    $scope.msgExpireSets = DIC_CONSTANT.getValueAndName("sms", "app_msg_expire_set");
    $scope.msgActions = DIC_CONSTANT.getValueAndName("sms", "app_msg_action");

    $scope.appMsgPush = {};//初始化消息推送
    var formUrl = CONFIG.interface.addAppMsgPush;//添加app消息推送

    //表单提交事件
    var $validationProvider = $injector.get('$validation');
    $scope.saveAndUpdate = function (form) {
        $validationProvider.validate(form).success(function () {
            var param = $("#dataForm").serializeObject();

            if (commonUtil.isEmpty(param.targetPlatform)){
                $("#targetPlatformError").html("目标平台不能为空!");
                return;
            }else {
                $("#targetPlatformError").html("");
                param.targetPlatform = param.targetPlatform.toLocaleString();
            }
            if (param.targetUser == 1 && commonUtil.isEmpty(param.targetUserTag)){
                $("#targetUserTagError").html("用户标签不能为空!");
                return;
            }else {
                $("#targetUserTagError").html("");
                if (commonUtil.isNotEmpty(param.targetUserTag)){param.targetUserTag = param.targetUserTag.toLocaleString()}
            }
            $http.post(formUrl,param).success(function (data) {
                if (data.resStatus === CONFIG.CONSTANTS.SUCCESS_STATE){
                    commonUtil.createNotifyAndRedirect("保存成功！",ALERT_SUC,$location,"/appMsgPushList");
                }else {
                    commonUtil.createSimpleNotify(data.resMsg,ALERT_ERR);
                }
            }).error(function () {
                commonUtil.createNotifyAndRedirect("保存失败！",ALERT_ERR,$location,"/appMsgPushList");
            });
        }).error(function () {
            //表单校验失败
        });
    };

    $scope.back = function () {
        $location.path("/appMsgPushList");
    };

    $scope.timeChange = function (id) {
        $scope.appMsgPush[id] = $("#" + id).val();
    };
});