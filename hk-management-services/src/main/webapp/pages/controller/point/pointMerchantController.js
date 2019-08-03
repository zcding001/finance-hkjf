/**
 * 积分商户管理controller
 * Created by zhongpingtang on 2017/10/31.
 */

app.controller('pointMerchantController',function ($scope,$http,$location, $CommonService,$routeParams,$injector) {

    //初始化提交btn
    $scope.isSaveBtn=commonUtil.isEmpty($routeParams.id)?true:false;
    //是否是修改的状态
    $scope.isUptate=commonUtil.isEmpty($routeParams.isUptate)?false:$routeParams.isUptate;

    //共有的初始化资源

    //处理地址省市下拉框
    $scope.registerProvinces = AREA_CONSTANT.getData(0);
    $scope.registerCitys = [];
    $scope.registerCountys = [];

    $scope.provincesChange = function(){
        if(commonUtil.isNotEmpty($scope.selectedProvince)){
            $scope.registerCitys = AREA_CONSTANT.getData($scope.selectedProvince);
        }
    }

    $scope.citysChange = function(){
        if(commonUtil.isNotEmpty($scope.selectedCity)){
            $scope.registerCountys = AREA_CONSTANT.getCountysByCityName($scope.selectedCity);
        }
    }
    //选择联系人
    $scope.selectUser=function(value){
        $("#usertype").val(value);
        $("#userInfoModal").modal('show');

    }

    //初始化状态下拉框
    $("#state").append(DIC_CONSTANT.getOption("pointMerchant","state","-999"))

    //返回事件
    $scope.back=function(){
        $location.path("/pointMerchantInfoList");
    }
    //图片预览
    $scope.picPreview=function(id, opts){
        $CommonService.picPreview(id, opts);
    };
    //图片异步上传
    $scope.upload=function(id, opts){
        $CommonService.upload(id, opts);
    };

    //初始化卫生许可证是否需要验证
    $scope.neddValidateHygineLicenseUrl=false;
    //定义页面信息
    $scope.pointMerchantInfo={};
    if($scope.isSaveBtn){
        //默认非餐饮行业
        $scope.pointMerchantInfo.businessType = 0;
    }else{
        //请求原来的信息
        $http.post(CONFIG.interface.selectPointMerchantInfoDetail,{id:$routeParams.id}).success(function (data) {
            $scope.pointMerchantInfo=data.resMsg;
            //图片回显
            $("#Pic_businessLicenseUrl").displayBack("Pic_businessLicenseUrl", {}, CONFIG.CONSTANTS.IMG_URL_ROOT + $scope.pointMerchantInfo.businessLicenseUrl);
            $("#Pic_permitUrl").displayBack("Pic_permitUrl", {}, CONFIG.CONSTANTS.IMG_URL_ROOT + $scope.pointMerchantInfo.permitUrl);
            $("#Pic_idcardFrontUrl").displayBack("Pic_idcardFrontUrl", {}, CONFIG.CONSTANTS.IMG_URL_ROOT + $scope.pointMerchantInfo.idcardFrontUrl);
            $("#Pic_idcardBackUrl").displayBack("Pic_idcardBackUrl", {}, CONFIG.CONSTANTS.IMG_URL_ROOT + $scope.pointMerchantInfo.idcardBackUrl);
            $("#Pic_hygieneLicenseUrl").displayBack("Pic_hygieneLicenseUrl", {}, CONFIG.CONSTANTS.IMG_URL_ROOT + $scope.pointMerchantInfo.hygieneLicenseUrl);
            //判断是否是修改页面
            if($scope.isUptate){
                //处理地址
                if(commonUtil.isEmpty($scope.pointMerchantInfo.address)){
                    $scope.pointMerchantInfo.address = '暂无';
                }
                //处理姓名回显
                $("#borrowName").html("["+$scope.pointMerchantInfo.userName+"]");
                //处理是否需要验证卫生许可证
                $scope.orginState=$scope.pointMerchantInfo.businessType;
                $("#orginAddress").show();
            }else{
                //处理详情页的数据
                //是否餐饮行业
                $scope.pointMerchantInfo.businessType = (data.resMsg.businessType == 1) ? '是' : '否';
                //状态转换
                $scope.pointMerchantInfo.state = DIC_CONSTANT.getName("pointMerchant", "state", $scope.pointMerchantInfo.state);
            }

        }).error(function () {
            commonUtil.createNotifyAndRedirect("查询失败，请重试",ALERT_ERR, $location, "/pointMerchantInfoList");
        });

    }


    //处理表单提交事件
    $scope.submit=function (formName) {
        var params = $("#pointMerchantForm").serializeObject();
        //检验表单
        var $validationProvider = $injector.get('$validation');
        $validationProvider.validate(formName).success(function(){
            //验证通过之后，进行请求
            var postUrl=null;
            var callBackFun=null;
            var errStrMsg = null;
            //处理商户类型
            params.businessType = $scope.pointMerchantInfo.businessType ? 1 : 0;
            //判断卫生许可证是否要验证是否要验证
            if(($scope.isUptate&&$scope.orginState==0&&params.businessType==1)||(!$scope.isUptate)){
                $scope.neddValidateHygineLicenseUrl=true;
            }
            if($scope.neddValidateHygineLicenseUrl&&!validateHygieneLicenseUrl()){
                return ;
            }

            //验证选择的用户
            validateSelectedUserCount(function () {
                if($scope.isSaveBtn){
                    if(!validateBeforeSave()){
                        return ;
                    }
                    //新建
                    postUrl = CONFIG.interface.savePointMerchant;
                    errStrMsg = "新建失败,请重试";
                    callBackFun=function (data) {
                        if(data.resStatus==CONFIG.CONSTANTS.SUCCESS_STATE){
                            commonUtil.createNotifyAndRedirect("添加商户成功!",ALERT_SUC, $location, "/pointMerchantInfoList");
                        }
                    }

                }else{
                    //修改
                    postUrl = CONFIG.interface.updatePointMerchant;
                    errStrMsg = "修改失败,请重试";
                    callBackFun=function (data) {
                        if(data.resStatus==CONFIG.CONSTANTS.SUCCESS_STATE){
                            commonUtil.createNotifyAndRedirect("修改商户成功!",ALERT_SUC, $location, "/pointMerchantInfoList");
                        }
                    }

                }
                //执行http请求：新建，或者更新
                $http.post(postUrl,params).success(callBackFun).error(function () {
                    commonUtil.createNotifyAndRedirect(errStrMsg,ALERT_ERR, $location, "/pointMerchantInfoList");
                });
            })



        }).error(function(errData){});
    }

    function validateBeforeSave(){
           //联系人
           if(commonUtil.isEmpty($("#borrowerId").val())){
               var errorMsg = "请选择商铺联系人";
               commonUtil.createSimpleNotify(errorMsg,ALERT_WARN);
               return false;
           }
           //营业执照
           if(commonUtil.isEmpty($("#businessLicenseUrl").val())){
               var errorMsg = "请选择营业执照或者等待营业执照上传完成!";
               commonUtil.createSimpleNotify(errorMsg,ALERT_WARN);
               $("#businessLicenseUrlError").html(errorMsg);
               return false;
           }
           //开户许可证
           if(commonUtil.isEmpty($("#permitUrl").val())){
               var errorMsg = "请选择开户许可证或者等待开户许可证上传完成!";
               commonUtil.createSimpleNotify(errorMsg,ALERT_WARN);
               $("#permitUrlError").html(errorMsg);
               return false;
           }
           //法人身份证正面
           if(commonUtil.isEmpty($("#idcardFrontUrl").val())){
               var errorMsg = "请选择法人身份证正面或者等待法人身份证正面上传完成!";
               commonUtil.createSimpleNotify(errorMsg,ALERT_WARN);
               $("#idcardFrontUrlError").html(errorMsg);
               return false;
           }
           //法人身份证反面
           if(commonUtil.isEmpty($("#idcardBackUrl").val())){
               var errorMsg = "请选择法人身份证反面或者法人身份证反面上传完成!";
               commonUtil.createSimpleNotify(errorMsg,ALERT_WARN);
               $("#idcardBackUrlError").html(errorMsg);
               return false;
           }
        return true;

    }
    //验证卫生许可证
    function validateHygieneLicenseUrl() {
        if($scope.pointMerchantInfo.businessType){
            //卫生许可证
            if(commonUtil.isEmpty($("#hygieneLicenseUrl").val())){
                var errorMsg = "请选择卫生许可证或者卫生许可证卫生许可证上传完成!";
                commonUtil.createSimpleNotify(errorMsg,ALERT_WARN);
                $("#hygieneLicenseUrlError").html(errorMsg);
                return false;
            }
        }
        return true;
    }

    //验证说选参数
    function validateSelectedUserCount(goOnFn) {
        //验证联系人
        var queryMerchantObj = {};
        queryMerchantObj.limitCount = 0;
        queryMerchantObj.regUserId=$("#borrowerId").val();
        if($scope.isUptate){
            queryMerchantObj.id = $routeParams.id;
            queryMerchantObj.limitCount = 1;
        }
        //去后台验证商户联系人数量
        //执行http请求：新建，或者更新
        $http.post(CONFIG.interface.pointMerchantCount,queryMerchantObj).success(function (data) {
            if(data.resMsg>queryMerchantObj.limitCount){
                commonUtil.createSimpleNotify("该联系人已经绑定商户，请更换联系人",ALERT_ERR);
                $("#borrowerId").val('');
                $("#borrowName").html('');
            }else{
                if(goOnFn!=null){
                    goOnFn();
                }

            }
        }).error(function () {
            commonUtil.createNotifyAndRedirect("请求联系人出错，请重试",ALERT_ERR, $location, "/pointMerchantInfoList");
            return false;
        });
    }








});
