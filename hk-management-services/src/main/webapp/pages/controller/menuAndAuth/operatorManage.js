//用户维护
app.controller('operatorManage', function($scope,$http,$location,$routeParams){
    $scope.validateCheck = function () {
        if (commonUtil.isEmpty($("#nickName").val())) {
            $("#checkPacketErro").text('请填写用户名称!');
            return false;
        }
        if (commonUtil.isEmpty($("#login").val())) {
            $("#checkPacketErro").text('请填写手机/登录号!');
            return false;
        }
        var reg = /^0?1[3-5|7-8][0-9]\d{8}$/;
        if (!reg.test($("#login").val())){
            $("#checkPacketErro").text('请输入正确的手机号!');
            return false;
        }

        if($("#id").val()=='') {
            if (commonUtil.isEmpty($("#passWord1").val()) || commonUtil.isEmpty($("#passWord2").val())) {
                $("#checkPacketErro").text('请填写密码/确认密码!');
                return false;
            }
            if ($("#passWord1").val() != $("#passWord2").val()) {
                $("#checkPacketErro").text('两次密码不一致!');
                return false;
            }
            if (commonUtil.isEmpty($("#email").val())) {
                $("#checkPacketErro").text('请填写邮件地址!');
                return false;
            }
            //encode
            var passWdTmp = rsaUtil.encryptData($("#passWord1").val());
            $("#passwd").val(passWdTmp);
        }


        return true;
    }

    //创建新建或者更新的面板
$scope.createAddOrUpdateUserPanel=function (id,nickName,login,email) {
    var url = (id == null) ? CONFIG.interface.addOperator : CONFIG.interface.updateOperator;
    id = id == null ? '' : id;
    nickName = nickName == null ? '' : nickName;
    email = email == null ? '' : email;
    login = login == null ? '' : login;

    var contents = '';
    contents += '<input id="id"  name="id" type="hidden" value="' + id + '" />';
    contents += '<input id="passwd"  name="passwd"  type="hidden" />';
    contents += ' 用户姓名  ：<input id="nickName" name="nickName" type="text" value="' + nickName + '" class="confirmChildren"/> <br><br>';
    contents += '登录/手机：<input id="login" name="login" type="text" value="' + login + '" class="confirmChildren"/> <br><br>';
    if(id==''){
        contents += '&nbsp;&nbsp;密码&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;：<input id="passWord1" type="password"  class="confirmChildren"/> <br><br>';
        contents += '确认密码;：<input id="passWord2"    type="password"  class="confirmChildren"/> <br><br>';
        contents += '  Email&nbsp;&nbsp;&nbsp;&nbsp;：<input id="email" name="email" type="email" value="' + email + '" class="confirmChildren"/> <br><br>';
    }

    contents += '<span id="checkPacketErro" style="color: red"></span>'
    commonUtil.createCustomConfirmBox(id==0?'添加后台用户':'修改后台用户', contents, url, $scope.validateCheck, commonUtil.customConfirmStandardAfterExecuteFn);
}


});
//为用户授权
app.controller('bidRolesToOperator', function($scope,$http,$location,$routeParams){

    //过滤搜索框
    $scope.filterOptions=function (filter,dataList,bindSelect) {
        bindSelect=bindSelect == null ? 'multiselect' : bindSelect;
        dataList=dataList == null ? $scope.allMenus : dataList;
        filter=(filter == null ? false : filter);
        var opts = '';
        for(var i=0;i<dataList.length;i++){
            var op=dataList[i]
            if(filter){
                var searchFilter=$("#searchFilter").val();
                if(!isEmpty(searchFilter)){
                    if(!((op.menuName.indexOf(searchFilter)>0)||(op.menuDesc.indexOf(searchFilter)>0)||(op.menuUrl.indexOf(searchFilter)>0))){
                        continue;
                    }
                }
            }
            opts+='<option value="'+op.id+'" >'+op.roleName+'【'+op.roleDesc+'】</option>'
        }
        $('#'+bindSelect).empty();
        $('#'+bindSelect).append(opts)
    }
    //获取所有角色
    $http.post(CONFIG.interface.findAllRolesNoPager,{}).success(function (data) {
        if(data.resStatus==CONFIG.CONSTANTS.SUCCESS_STATE){
            $scope.allMenus=data.resMsg;
            $scope.filterOptions()
        }
    }).error(function () {
        //do nothing..
    });

    //获取用户已经分配的角色
    $http.post(CONFIG.interface.findRoleBindToUser,{'userId':$routeParams.id}).success(function (data) {
        if(data.resStatus==CONFIG.CONSTANTS.SUCCESS_STATE){
            $scope.areadyAssignedPris=data.resMsg;
            $scope.filterOptions(null, $scope.areadyAssignedPris,'multiselect_to')
        }
    }).error(function () {
        //do nothing..
    });
    function _afterExecuteFn(data) {
        $("#modalText").modal('hide');
        if(commonUtil.isNotEmpty(data) && data.resStatus==CONFIG.CONSTANTS.SUCCESS_STATE){
            commonUtil.createNotifyAndRedirect("用户授权成功!",ALERT_SUC, $location, "/operatorManage");
        }else {
            commonUtil.createNotifyAndRedirect(data.resMsg,ALERT_ERR, $location, "/operatorManage");
        }
    }
    //保存权限
    $scope.saveAuths=function(){
        var selectedValues=[];
        //选择所有的右侧菜单
        $("#multiselect_to option").each(function () {
            selectedValues.push($(this).attr("value"))
        });
        $("#ids").attr("name", "roleIds");
        $("#ids").val(selectedValues);
        var contents = '确定授权到用户?';
        commonUtil.createCustomConfirmBox("用户授权", contents, CONFIG.interface.bindRolesToUser+"?id="+$routeParams.id,null,_afterExecuteFn);
    }
});