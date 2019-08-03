//维护系统菜单的controller
app.controller('sysPrivilege', function($scope,$http){

    //过滤权限
    $scope.filterOptions=function (filter) {
        //alert( $("#serachUrl"))
        if ($scope.currentPriType == 1){
            $scope.scanedUrls = $scope.managePris;
        } else if ($scope.currentPriType == 2){
            $scope.scanedUrls = $scope.hkjfPris;
        }else{
            $scope.scanedUrls = $scope.biPris;
        }
        filter= filter == null ? false : filter;
        var options = '';
        for(var i=0;i<$scope.scanedUrls.length;i++){
            if(filter){
                var seachStr=$("#serachUrl").val();
                if($scope.scanedUrls[i].indexOf(seachStr)==-1){
                    continue;
                }
            }
            options += '<option value="'+$scope.scanedUrls[i]+'">'+$scope.scanedUrls[i]+'</option>';

        }
        $("#privUrl").empty();
        $("#privUrl").append(options);
    }


    //验证权限添加输入
    $scope.validateCheck = function () {
        if (commonUtil.isEmpty($("#privName").val())) {
            $("#checkPacketErro").text('请填写权限名称!');
            return false;
        }
        if (commonUtil.isEmpty($("#privDesc").val())) {
            $("#checkPacketErro").text('请填写权限描述!');
            return false;
        }
        if (commonUtil.isEmpty($("#privUrl").val())) {
            $("#checkPacketErro").text('请选择权限URL!');
            return false;
        }

        return true;
    }


    //创建新增菜单面板
    $scope.createPrivilegePanel = function () {
        $http.post(CONFIG.interface.scannedPrivileges,{}).success(function (data) {
            //$scope.scanedUrls=data.resMsg;
            $scope.managePris = data.params.managePris;
            $scope.hkjfPris = data.params.hkjfPris;
            $scope.biPris = data.params.biPris;
            $scope.currentPriType = 1;
            if(data.resStatus==CONFIG.CONSTANTS.SUCCESS_STATE){
                var contents = '';
                contents += '权限名称：<input id="privName" name="privName" type="text"  class="confirmChildren"/> <br><br>';
                contents += '权限描述：<input id="privDesc" name="privDesc" type="text" class="confirmChildren"/> <br><br>';
                contents += '查找权限：<input id="serachUrl" type="text"  class="confirmChildren"/><button onclick="angular.element(this).scope().filterOptions('+true+')">查找</button><br><br>';
                contents +='所属平台：<input   type="radio" name="type"  value="1" checked="true" onclick="angular.element(this).scope().changeRedioType()"/>鸿坤金服后台';
                contents += '<input  type="radio" name="type"  value="2" onclick="angular.element(this).scope().changeRedioType()"/>我的账户 ';
                contents += '<input  type="radio" name="type"  value="3" onclick="angular.element(this).scope().changeRedioType()"/>BI管理平台 <br><br>';
                contents += '选择权限：<select id="privUrl" name="privUrl" style="width: auto" class="confirmChildren"></select><br><br>';
                contents += '<span id="checkPacketErro" style="color: red" ></span>'
                commonUtil.createCustomConfirmBox("添加权限", contents,CONFIG.interface.savePrivilege, $scope.validateCheck, commonUtil.customConfirmStandardAfterExecuteFn);
            }
            $scope.filterOptions();
        }).error(function () {
           //do nothing..
        });

    }

    $scope.changeRedioType = function (){
       $scope.currentPriType = $("input[type=radio]:checked").val();
       $scope.filterOptions();
    }
});