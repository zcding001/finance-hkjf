
app.controller('lotteryActivityController',function ($scope,$http,$location, $CommonService,$routeParams,$injector) {

    //初始化提交btn
    $scope.isSaveBtn=commonUtil.isEmpty($routeParams.id)?true:false;
    //是否是修改的状态
    $scope.isUptate=commonUtil.isEmpty($routeParams.isUptate)?false:$routeParams.isUptate;

    //返回事件
    $scope.back=function(){
        $location.path("/lotteryActivityList");
    }
    //图片预览
    $scope.picPreview=function(id, opts){
        $CommonService.picPreview(id, opts);
    };
    //图片异步上传
    $scope.upload=function(id, opts){
        $CommonService.upload(id, opts);
    };

    var activityRuleEditor = elementOperateUtil.createKindEditor(CONFIG.PLATFORMS.HKJF, CONFIG.FILEPATHS.POINT_PRODUCT_FILEPATH, 'activityRule');
    var introductionEditor = elementOperateUtil.createKindEditor(CONFIG.PLATFORMS.HKJF, CONFIG.FILEPATHS.POINT_PRODUCT_FILEPATH, 'introduction');
    //定义页面信息
    $scope.lotteryActivityInfo={};
    if($scope.isUptate){
        //请求原来的信息
        $http.post(CONFIG.interface.lotteryActivityDetail,{id:$routeParams.id}).success(function (data) {
            $scope.lotteryActivityInfo=data.resMsg;
            //图片回显
            $("#Pic_backgroundUrl").displayBack("Pic_backgroundUrl", {}, CONFIG.CONSTANTS.IMG_URL_ROOT + $scope.lotteryActivityInfo.backgroundUrl);
            $("#Pic_itemsUrl").displayBack("Pic_itemsUrl", {}, CONFIG.CONSTANTS.IMG_URL_ROOT + $scope.lotteryActivityInfo.itemsUrl);
            $("#Pic_pointerUrl").displayBack("Pic_pointerUrl", {}, CONFIG.CONSTANTS.IMG_URL_ROOT + $scope.lotteryActivityInfo.pointerUrl);
            activityRuleEditor.html($scope.lotteryActivityInfo.activityRule);
            introductionEditor.html($scope.lotteryActivityInfo.introduction);
            $scope.lotteryActivityInfo.startTime = dateUtil.dateByFormat($scope.lotteryActivityInfo.startTime,"yyyy-MM-dd HH:mm:ss");
            $scope.lotteryActivityInfo.endTime = dateUtil.dateByFormat($scope.lotteryActivityInfo.endTime,"yyyy-MM-dd HH:mm:ss");
        }).error(function () {
            commonUtil.createNotifyAndRedirect("查询失败，请重试",ALERT_ERR, $location, "/lotteryActivityList");
        });
    }


    //处理表单提交事件
    $scope.submit=function (formName) {
        var params = $("#lotteryActivityForm").serializeObject();
        params.activityRule = activityRuleEditor.html();
        params.introduction = introductionEditor.html();
        //检验表单
        var $validationProvider = $injector.get('$validation');
        $validationProvider.validate(formName).success(function(){
            //验证通过之后，进行请求
            var postUrl=null;
            var callBackFun=null;
            var errStrMsg = null;
            if(params.activityRule.length > 2000){
                $("#activityRuleError").css('display', 'block').html("长度不能大于2000！");
                return;
            }
            if(params.introduction.length > 2000){
                $("#introductionError").css('display', 'block').html("长度不能大于2000！");
                return;
            }

            if($scope.isSaveBtn){
                if(!validateBeforeSave()){
                    return ;
                }
                //新建
                postUrl = CONFIG.interface.saveLotteryActivity;
                errStrMsg = "新建失败,请重试";
                callBackFun=function (data) {
                    if(data.resStatus==CONFIG.CONSTANTS.SUCCESS_STATE){
                        commonUtil.createNotifyAndRedirect("添加成功!",ALERT_SUC, $location, "/lotteryActivityList");
                    }
                }
            }else{
                //修改
                postUrl = CONFIG.interface.updateLotteryActivity;
                errStrMsg = "修改失败,请重试";
                callBackFun=function (data) {
                    if(data.resStatus==CONFIG.CONSTANTS.SUCCESS_STATE){
                        commonUtil.createNotifyAndRedirect("修改成功!",ALERT_SUC, $location, "/lotteryActivityList");
                    }
                }
            }
            //执行http请求：新建，或者更新
            $http.post(postUrl,params).success(callBackFun).error(function () {
                commonUtil.createNotifyAndRedirect(errStrMsg,ALERT_ERR, $location, "/lotteryActivityList");
            });


        }).error(function(errData){});
    }

    function validateBeforeSave(){
           //背景图片
           if(commonUtil.isEmpty($("#backgroundUrl").val())){
               var errorMsg = "请选择背景图片或者等待背景图片上传完成!";
               commonUtil.createSimpleNotify(errorMsg,ALERT_WARN);
               $("#backgroundUrlError").html(errorMsg);
               return false;
           }
           //转盘图片
           if(commonUtil.isEmpty($("#itemsUrl").val())){
               var errorMsg = "请选择转盘图片或者等待转盘图片上传完成!";
               commonUtil.createSimpleNotify(errorMsg,ALERT_WARN);
               $("#itemsUrlError").html(errorMsg);
               return false;
           }
           //指针图片
           if(commonUtil.isEmpty($("#pointerUrl").val())){
               var errorMsg = "请选择指针图片或者等待指针图片上传完成!";
               commonUtil.createSimpleNotify(errorMsg,ALERT_WARN);
               $("#pointerUrlError").html(errorMsg);
               return false;
           }
        return true;

    }

    $scope.timeChange = function (id) {
        $scope.lotteryActivityInfo[id] = $("#" + id).val();
    }



});
