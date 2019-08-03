/**
 * 股权咨询信息前端控制器
 */
app.controller('fundAdvisoryCtrl',function ($scope,$http,$location, $CommonService,$routeParams,$injector) {

    $scope.fundAdvisoryInfo={};
    //初始化状态
    $scope.projectParentTypes = DIC_CONSTANT.getValueAndName("fund","project_parent_type");
    $scope.fundAdvisoryInfo.advisor = 0;
    $scope.fundAdvisoryInfo.sex = 1;

    $scope.isSaveBtn= true;
    if($routeParams.param === "update" && commonUtil.isNotEmpty(containerUtil.get("updateAdvisory"))){
        $scope.isSaveBtn= false;
        $scope.fundAdvisoryInfo = containerUtil.get("updateAdvisory");
        $http.post(CONFIG.interface.searchFundInfoList,{id:$scope.fundAdvisoryInfo.infoIds}).success(function (data) {
            $scope.infos = data.resMsg.data;
            $scope.fundAdvisoryInfo.infoIds = data.resMsg.data[0].id;
        });
    }

    // 根据父类型查询项目名称
    $scope.typeChange = function(){
        var postParam = {
            parentType:$scope.fundAdvisoryInfo.projectParentType,
            state:2,
            subscribeState:1,
            infoExist:1
           };
        $http.post(CONFIG.interface.searchFundInfoList,postParam).success(function (data) {
            $scope.infos = data.resMsg.data;
            // 初始化
            if (commonUtil.isNotEmpty($scope.infos)){
               $scope.fundAdvisoryInfo.infoIds = data.resMsg.data[0].id;
            }

        });
    }

    //返回事件
    $scope.back = function(){
        $location.path("/fundAdvisoryList");
    }


    //处理表单提交事件
    var $validationProvider = $injector.get('$validation');
    $scope.submit=function (formName) {
        $validationProvider.validate(formName).success(function () {
            var params = $("#fundAdvisoryForm").serializeObject();
            params.projectParentType = params.projectParentType.split(":")[1];
            params.infoIds = params.infoIds.split(":")[1];
            if ($scope.isSaveBtn) {
                //新建
                postUrl = CONFIG.interface.saveFundAdvisory;
                errStrMsg = "新建失败,请重试";
                callBackFun = function (data) {
                    if (data.resStatus == CONFIG.CONSTANTS.SUCCESS_STATE) {
                        commonUtil.createNotifyAndRedirect("录入咨询信息成功!", ALERT_SUC, $location, "/fundAdvisoryList");
                    }
                }

            }else {
                //修改
                postUrl = CONFIG.interface.updateFundAdvisory;
                errStrMsg = "修改失败,请重试";
                callBackFun = function (data) {
                    if (data.resStatus == CONFIG.CONSTANTS.SUCCESS_STATE) {
                        commonUtil.createNotifyAndRedirect("更新咨询信息成功!", ALERT_SUC, $location, "/fundAdvisoryList");
                    }
                }
             }

            //执行http请求：新建，或者更新
            $http.post(postUrl, params).success(callBackFun).error(function () {
                commonUtil.createNotifyAndRedirect(errStrMsg, ALERT_ERR, $location, "/fundAdvisoryList");
            });

        }).error(function () {
            //表单校验失败
        })

    }


});
