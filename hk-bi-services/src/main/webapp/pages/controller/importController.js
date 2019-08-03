/**
 * 文件上传Controller
 */
app.controller('importController',function ($scope, $http) {
    $scope.importForm = {};
    $scope.uploadFile = function(){
        var data = new FormData();
        data.append('uploadFile', $("#importFile")[0].files[0]);
        $http.post(HOST + $("#url").val(), data, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        }).success(function(data) {
            if (data.resStatus == CONFIG.CONSTANTS.SUCCESS_STATE) {
                commonUtil.createSimpleNotify("文件上传成功!", ALERT_SUC)
            }else{
                commonUtil.createSimpleNotify(data.resMsg,  ALERT_ERR);
            }
        });
    }
});
