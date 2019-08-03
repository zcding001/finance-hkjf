//会员待遇管理controller
app.controller('vipTreatmentCtrl',function ($scope,$injector,$http,$location,$routeParams,$CommonService) {
    $scope.vipTreatment = {};//初始化会员待遇
    $scope.vipTreatmentDic = DIC_CONSTANT.getValueAndName("vas","vip_treatment_type");//获取会员待遇数据字典
    $scope.vipGradeDic = DIC_CONSTANT.getValueAndName("vas","vip_grade");//获取会员等级数据字典
    $scope.vipTreatmentValueDic = [];
    let formUrl = CONFIG.interface.addVipTreatment;//默认为新增会员待遇
    if (commonUtil.isNotEmpty($routeParams.param)){
        elementOperateUtil.disableElement("type");
        formUrl = CONFIG.interface.updateVipTreatment;
        $scope.vipTreatment = JSON.parse($routeParams.param);
        $scope.vipTreatment.registBeginTime = dateUtil.date($scope.vipTreatment.registBeginTime);
        $scope.vipTreatment.registEndTime = dateUtil.date($scope.vipTreatment.registEndTime);
        //编辑的时候将数据拆分
        let ruleContentsArr = JSON.parse($scope.vipTreatment.ruleContents);
        for (let i in vipGradeDic){
            let ruleContent = getRuleContent(vipGradeDic[i].value,ruleContentsArr);
            if ($scope.vipTreatment.type == 2 || $scope.vipTreatment.type == 5 || $scope.vipTreatment.type == 6){
                $scope.vipTreatment["rulem"+vipGradeDic[i].value] = ruleContent.rulem;
            }
            $scope.vipTreatment["rulem"+vipGradeDic[i].value] = ruleContent.code;
            $scope.vipTreatment["rulen"+vipGradeDic[i].value] = ruleContent.rulen;
        }
    }

    //表单提交事件
    let $validationProvider = $injector.get('$validation');
    $scope.saveAndUpdate = function (form) {
        $validationProvider.validate(form).success(function () {
            if (!validateRegistTime()){
                return;
            }
            var param = $("#dataForm").serializeObject();
            //保存会员待遇内容处理
            var vipGradeDic = DIC_CONSTANT.getValueAndName("vas","vip_grade");
            var contentArr = [];
            for (var i in vipGradeDic){
                var grade = vipGradeDic[i].value + "";//对应的等级
                //根据等级获取其对应的值
                var value = $scope.vipTreatment["rulem" + grade];
                var num = value == "-1" ? "-1" : $scope.vipTreatment["rulen" + grade];
                switch ($("select[name='type']").val()){
                    case "3"://生日礼券
                    case "4"://提现优惠
                    case "7"://投资红包
                    case "8"://好友券
                    case "1":{//投资加息;1,3,4,7,8使用同一套处理逻辑
                        var content = {rulen:num,code:value,grade:grade,rulem:getWorth(value)};
                        contentArr.push(content);
                        break;
                    }
                    case "2"://签到升级
                    case "5"://专属客服
                    case "6":{//线下活动;2、5、6使用同一套处理逻辑
                        var code = value != "-1" ? "1" : "-1";
                        var content = {rulen:"-1",code:code,grade:grade,rulem:value};
                        contentArr.push(content);
                        break;
                    }
                }
            }
            param.ruleContents = JSON.stringify(contentArr);
            $http.post(formUrl,param).success(function (data) {
                if (data.resStatus === CONFIG.CONSTANTS.SUCCESS_STATE){
                    commonUtil.createNotifyAndRedirect("保存成功！",ALERT_SUC,$location,"/vipTreatmentList");
                }else {
                    commonUtil.createSimpleNotify(data.resMsg,ALERT_ERR);
                }
            }).error(function () {
                commonUtil.createNotifyAndRedirect("保存失败！",ALERT_ERR,$location,"/vipTreatmentList");
            })
        }).error(function () {
            //表单校验失败
        })
    };
    $scope.back = function () {
        $location.path("/vipTreatmentList");
    };
    $scope.typeSelect = function () {
        const value = $scope.vipTreatment.type + "";
        switch (value){
            case "1":{//投资加息
                getCouponProduct(0);
                break;
            }
            case "2":{//签到升级
                $scope.vipTreatmentValueDic = [{value:"-1",name:"无"}];
                for (var i = 1;i < 10;i++){
                    var dic = {value:i+"",name:i + "积分/天"};
                    $scope.vipTreatmentValueDic.push(dic);
                }
                break;
            }
            case "3":{//生日礼券
                getCouponProduct(0);
                break;
            }
            case "4":{//提现优惠
                getCouponProduct(2);
                break;
            }
            case "5":{//专属服务
                $scope.vipTreatmentValueDic = [{value:"-1",name:"无"},{value:"1",name:"QQ专属服务"}];
                break;
            }
            case "6":{//线下活动
                $scope.vipTreatmentValueDic = [{value:"-1",name:"报名参加"},{value:"1",name:"专人通知"}];
                break;
            }
            case "7":{//投资红包
                getCouponProduct(1);
                break;
            }
            case "8":{//好友券
               getCouponProduct(3);
               break;
            }
        }
        initNumSelect();
        //判断是否为添加且类型不为空则初始化下拉框的默认值为-1
        if (commonUtil.isEmpty($routeParams.param) && $scope.vipTreatment.type){
            initSelect();
        }
    };
    //获取会员等级对应的记录
    function getRuleContent(level,ruleContentsArr) {
        for (let i in ruleContentsArr){
            if (ruleContentsArr[i].grade == level){
                return ruleContentsArr[i];
            }
        }
    }
    //不传值默认取vipTreatmentValueDic第一个值，否则默认值为-1
    function  initSelect() {
        for (let i in vipGradeDic){
            $scope.vipTreatment["rulem"+vipGradeDic[i].value] = "-1";
        }
    }
    //默认选择数量为1
    function initNumSelect() {
        for (let i in vipGradeDic){
            //添加或编辑时下拉值为无时张数赋值为1
            if ($scope.vipTreatment["rulem"+vipGradeDic[i].value]  == null || $scope.vipTreatment["rulem"+vipGradeDic[i].value] == "-1"){
                $scope.vipTreatment["rulen"+vipGradeDic[i].value] = "1";
            }
        }
    }
    //获取会员待遇对应的卡券产品
    function getCouponProduct(type) {
        $CommonService.httpPost({type:type,couponScenes:2},CONFIG.interface.getCouponProduct).success(function (data) {
            if (data.resStatus == CONFIG.CONSTANTS.SUCCESS_STATE){
                //获取当前会员待遇对应的卡券产品
                $scope.vipTreatmentValueDic = data.resMsg;
                //给卡产产品列表第一个值赋值"无"
                $scope.vipTreatmentValueDic.splice(0,0,{value:"-1",name:"无",worth:"-1"});
            }
        }).error(function () {
            commonUtil.createSimpleNotify("加载数据列表失败！",ALERT_WARN);
        })
    }

    //获取卡券产品对应的价值
    function getWorth(value) {
        const dic = $scope.vipTreatmentValueDic;
        for (let i = 0;i < dic.length;i++){
            if (dic[i].value == value){
                return dic[i].worth;
            }
        }
    }
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