//角色维护
app.controller('roles', function($scope,$http,$location,$routeParams){
    $scope.validateCheck = function () {
        if (commonUtil.isEmpty($("#roleName").val())) {
            $("#checkPacketErro").text('请填写角色名称!');
            return false;
        }
        if (commonUtil.isEmpty($("#roleDesc").val())) {
            $("#checkPacketErro").text('请填写角色编号!');
            return false;
        }

        return true;
    }

    //创建新建或者更新的面板
$scope.createAddOrUpdateRolePanel=function (id,roleName,roleDesc) {
    var url = id == null ? CONFIG.interface.addRole : CONFIG.interface.updateRole;
    id = id == null ? 0 : id;
    roleName = roleName == null ? '' : roleName;
    roleDesc = roleDesc == null ? '' : roleDesc;

    var contents = '';
    contents += '<input id="id"  name="id" type="hidden" value="' + id + '" checked="checked"/>';
    contents += '角色名称：<input id="roleName" name="roleName" type="text" value="' + roleName + '" class="confirmChildren"/> <br><br>';
    contents += '角色描述：<input id="roleDesc" name="roleDesc" type="text" value="' + roleDesc + '" class="confirmChildren"/> <br><br>';
    contents += '<span id="checkPacketErro" style="color: red"></span>'
    commonUtil.createCustomConfirmBox(id==0?'添加角色':'修改角色', contents, url, $scope.validateCheck, commonUtil.customConfirmStandardAfterExecuteFn);
}


});
//为角色授权
app.controller('bindMenusToRole', function($scope,$http,$location,$routeParams){

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
                if(commonUtil.isNotEmpty(searchFilter)){
                    if(!((op.menuName.indexOf(searchFilter)>=0)||(op.menuDesc.indexOf(searchFilter)>=0)||(op.menuUrl.indexOf(searchFilter)>=0))){
                        continue;
                    }
                }
            }
            opts+='<option value="'+op.id+'" >'+op.menuName+'【'+op.menuDesc+'->'+op.menuUrl+'】</option>'
        }
        $('#'+bindSelect).empty();
        $('#'+bindSelect).append(opts)
    }
    //获取所菜单
    $http.post(CONFIG.interface.listAllMenusNoPager,{}).success(function (data) {
        if(data.resStatus==CONFIG.CONSTANTS.SUCCESS_STATE){
            $scope.allMenus=data.resMsg;
            $scope.filterOptions()
        }
    }).error(function () {
        //do nothing..
    });

    //获取角色已经绑定的菜单
    $http.post(CONFIG.interface.findMenuBindToRole,{'id':$routeParams.id}).success(function (data) {
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
            commonUtil.createNotifyAndRedirect("角色授权成功!",ALERT_SUC, $location, "/roleManage");
        }else {
            commonUtil.createNotifyAndRedirect(data.resMsg,ALERT_ERR, $location, "/roleManage");
        }
    }
    //保存权限
    $scope.saveAuths=function(){
        var selectedValues=[];
        //选择所有的右侧菜单
        $("#multiselect_to option").each(function () {
            selectedValues.push($(this).attr("value"))
        });
        $("#ids").attr("name", "menus");
        $("#ids").val(selectedValues);
        var contents = '确定授权到角色?';
        commonUtil.createCustomConfirmBox("角色授权", contents, CONFIG.interface.bindMenuToRole+"?roleId="+$routeParams.id,null,_afterExecuteFn);
    }
});