//会员成长值规则管理controller
app.controller('vipGrowRuleCtrl',function ($scope,$injector,$http,$location,$routeParams) {
    $scope.vipGrowRule = {};//初始化会员成长值规则
    $scope.vipGrowRuleDic = DIC_CONSTANT.getValueAndName("vas","vip_grow_type");//获取会员成长值规则数据字典
    //去除平台赠送、会员等级
    $scope.vipGrowRuleDic = $scope.vipGrowRuleDic.filter(function (dic) {
       if (dic.value != 9 && dic.value != 10){
           return dic;
       }
    });
    var formUrl = CONFIG.interface.addVipGrowRule;//默认为新增会员成长规则
    if (commonUtil.isNotEmpty($routeParams.param)){
        formUrl = CONFIG.interface.updateVipGrowRule;
        $scope.vipGrowRule = JSON.parse($routeParams.param);
        $scope.vipGrowRule.registBeginTime = dateUtil.date($scope.vipGrowRule.registBeginTime);
        $scope.vipGrowRule.registEndTime = dateUtil.date($scope.vipGrowRule.registEndTime);
        //若为公式，将值拆分为money和day
        if ($scope.vipGrowRule.formulaEnable == 1){
            var formula = $scope.vipGrowRule.growValue.split("*");
            var money = formula[0].split("/")[1];
            money = money.replace(")","");
            var day = formula[1].split("/")[1];
            day = day.replace(")","");
            $scope.vipGrowRule.money = money - 0;
            $scope.vipGrowRule.day = day - 0;
        }
        elementOperateUtil.disableElement("type");//修改时禁用类型编辑,不传递是否为成长值公式至后台
    }
    //表单提交事件
    var $validationProvider = $injector.get('$validation');
    $scope.saveAndUpdate = function (form) {
        $validationProvider.validate(form).success(function () {
            if (!validateRegistTime()){
                return;
            }
            var param = $("#dataForm").serializeObject();
            //如果为公式
            if(param.formulaEnable == 1){
                param.growValue = "(money/"+param.money+")*(day/"+param.day+")";
            }
            $http.post(formUrl,param).success(function (data) {
                if (data.resStatus === CONFIG.CONSTANTS.SUCCESS_STATE){
                    commonUtil.createNotifyAndRedirect("保存成功！",ALERT_SUC,$location,"/vipGrowRuleList");
                }else {
                    commonUtil.createSimpleNotify(data.resMsg,ALERT_ERR);
                }
            }).error(function () {
                commonUtil.createNotifyAndRedirect("保存失败！",ALERT_ERR,$location,"/vipGrowRuleList");
            })
        }).error(function () {
            //表单校验失败
        })
    }
    $scope.back = function () {
        $location.path("/vipGrowRuleList");
    }
    $scope.typeSelect = function () {
        //成长值类型为投资或邀请好友投资时为成长值公式
        if ($scope.vipGrowRule.type === "1" || $scope.vipGrowRule.type === "6"){
            $("input[name='formulaEnable']").val(1);
        }else {
            $("input[name='formulaEnable']").val(0);
        }
    };
    //获取用户注册时间范围、错误提示信息jq对象
    let registBeginTime = $("#registBeginTime");
    let registEndTime = $("#registEndTime");
    let registTimeError = $("#registTimeError");

    registBeginTime.on("blur", validateRegistTime);
    registEndTime.on("blur", validateRegistTime);
    function validateRegistTime() {
        if (commonUtil.isNotEmpty(registBeginTime.val()) && commonUtil.isNotEmpty(registEndTime.val())){
            registTimeError.text("");
            return true;
        }
        registTimeError.text("注册时间范围不能为空！");
        return false;
    }
});