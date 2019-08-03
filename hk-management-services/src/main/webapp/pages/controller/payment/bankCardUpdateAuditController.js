/**
 * 积分商户管理controller
 * Created by zhongpingtang on 2017/10/31.
 */

app.controller('bankCardUpdateAuditCtrl',function ($scope,$http,$location, $CommonService,$routeParams,$injector) {

    //返回事件
    $scope.back=function(){
        $location.path("/findBankCardUpdateList");
    }
    //图片预览
    $scope.picPreview=function(id, opts){
        $CommonService.picPreview(id, opts);
    };

    //定义页面信息
    $scope.bankCardUpdateInfo={};

    //请求回显的信息
    $http.post(CONFIG.interface.findBankCardUpdateInfo,{id:$routeParams.id}).success(function (data) {
        $scope.bankCardUpdateInfo=data.resMsg;
        //图片回显
        $("#Pic_cardUp").displayBack("Pic_cardUp", {}, CONFIG.CONSTANTS.IMG_URL_ROOT + $scope.bankCardUpdateInfo.cardUp);
        $("#Pic_cardDown").displayBack("Pic_cardDown", {}, CONFIG.CONSTANTS.IMG_URL_ROOT + $scope.bankCardUpdateInfo.cardDown);
        $("#Pic_holdingCardUp").displayBack("Pic_holdingCardUp", {}, CONFIG.CONSTANTS.IMG_URL_ROOT + $scope.bankCardUpdateInfo.holdingCardUp);
        $("#Pic_holdingCardDown").displayBack("Pic_holdingCardDown", {}, CONFIG.CONSTANTS.IMG_URL_ROOT + $scope.bankCardUpdateInfo.holdingCardDown);
        $("#Pic_householdRegister").displayBack("Pic_householdRegister", {}, CONFIG.CONSTANTS.IMG_URL_ROOT + $scope.bankCardUpdateInfo.householdRegister);
    }).error(function () {
        commonUtil.createNotifyAndRedirect("查询失败，请重试",ALERT_ERR, $location, "/findBankCardUpdateList");
    });

    //处理表单提交事件
    var $validationProvider = $injector.get('$validation');
    $scope.submit= function (formName,type) {
        $('#state').val(type);
        $validationProvider.validate(formName).success(function () {
            var params = $("#bankCardUpdateForm").serializeObject();
            var postUrl = CONFIG.interface.auditBankCardUpdate;
            var errStrMsg = "审核失败,请重试";
            var callBackFun = function (data) {
                if(data.resStatus==CONFIG.CONSTANTS.SUCCESS_STATE){
                    commonUtil.createNotifyAndRedirect(data.resMsg,ALERT_SUC, $location, "/findBankCardUpdateList");
                }else{
                    commonUtil.createSimpleNotify(data.resMsg,ALERT_ERR);
                }
            }
            //执行http请求
            $http.post(postUrl, params).success(callBackFun).error(function (data) {
                commonUtil.createNotifyAndRedirect(errStrMsg, ALERT_ERR, $location, "/findBankCardUpdateList");
            });

        }).error(function () {
            //表单校验失败
        })
    }


});
