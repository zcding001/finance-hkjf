/**
 * 车辆信息前端控制器
 */
app.controller('carInfoImportCtrl',function ($scope,$injector,$timeout,$http) {
    $scope.importForm = {};
    $scope.searchCarInfoList = function () {
        commonUtil.directRequest(encryptAndDecryptUtil.encrypt(JSON.stringify({target: 'searchCarInfoList'})));
    }
    $scope.uploadFile = function(form){
        if($("#photoCover").val() == undefined || $("#photoCover").val() == ''){
            $("#error").css('display', 'block').html("请选择要上传的Excel文件!");
            scrollTo(0,0);
            return;
        }else{
            $("#error").html("");
        }
        //校验文件格式
        var filePath =$("#photoCover").val();
        var pos = filePath.lastIndexOf(".");
        var postfixName = filePath.substring(pos,filePath.length)
        if(postfixName ==".xls" || postfixName ==".xlsx"){
            $("#error").html("");
        }else{
            $("#error").css('display', 'block').html("仅支持上传Excel文件(后缀名xls、xlsx)!");
            scrollTo(0,0);
            return;
        }
        var data = new FormData();
        data.append('uploadFile', $("#importFile")[0].files[0]);
        $http.post(CONFIG.interface.carInfoImport, data, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        }).success(function(data) {
            if (data.resStatus == CONFIG.CONSTANTS.SUCCESS_STATE) {
                commonUtil.createSimpleNotify(data.resMsg, ALERT_SUC)
            }else{
                commonUtil.createSimpleNotify(data.resMsg,  ALERT_ERR);
            }
        });
    }
});