//维护系统菜单的controller
app.controller('menuBindPri', function($scope,$http,$location,$routeParams){

    //过滤搜索框
    $scope.filterOptions=function (filter,dataList,bindSelect) {
        bindSelect=bindSelect == null ? 'multiselect' : bindSelect;
        dataList=dataList == null ? $scope.allPris : dataList;
        filter=(filter == null ? false : filter);
        var opts = '';
        for(var i=0;i<dataList.length;i++){
            var op=dataList[i]
            if(filter){
                var searchFilter=$("#searchFilter").val();
                if(commonUtil.isNotEmpty(searchFilter)){
                    if(!((op.privName.indexOf(searchFilter)>=0)||(op.privDesc.indexOf(searchFilter)>=0)||(op.privUrl.indexOf(searchFilter)>=0))){
                        continue;
                    }
                }
            }
            opts+='<option value="'+op.id+'" >'+op.privName+'【'+op.privDesc+'->'+op.privUrl+'】</option>'
        }
        $('#'+bindSelect).empty();
        $('#'+bindSelect).append(opts)
    }
    //获取所权限
    $http.post(CONFIG.interface.listPrivilegesNoPager,{}).success(function (data) {
        if(data.resStatus==CONFIG.CONSTANTS.SUCCESS_STATE){
           $scope.allPris=data.resMsg;
            $scope.filterOptions()
        }
    }).error(function () {
        //do nothing..
    });

    //获取菜单已经绑定的权限

    $http.post(CONFIG.interface.findPrivigeIdBindToMenu,{'menuId':$routeParams.id}).success(function (data) {
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
            commonUtil.createNotifyAndRedirect("权限绑定成功!",ALERT_SUC, $location, "/sysMenus");
        }else {
            commonUtil.createNotifyAndRedirect(data.resMsg,ALERT_ERR, $location, "/sysMenus");
        }
    }
    //保存权限
    $scope.saveAuths=function(){
        var selectedValues=[];
        //选择所有的右侧菜单
        $("#multiselect_to option").each(function () {
            selectedValues.push($(this).attr("value"))
        });
        $("#ids").attr("name", "pris");
        $("#ids").val(selectedValues);
        var contents = '确定绑定权限到菜单?';
        commonUtil.createCustomConfirmBox("绑定权限到菜单", contents,CONFIG.interface.bindPrisToMenu+"?menuId="+$routeParams.id,null,_afterExecuteFn);
    }



});