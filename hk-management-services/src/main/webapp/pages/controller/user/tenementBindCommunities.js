//维护物业与小区关系controller
app.controller('TenementBindCommunitiesController', function($scope,$http,$location,$routeParams){

    $("#tenementName").html($routeParams.name);
    //过滤搜索框
    $scope.filterOptions=function (filter,dataList,bindSelect) {
        bindSelect=bindSelect == null ? 'multiselect' : bindSelect;
        dataList=dataList == null ? $scope.communityAvailable : dataList;
        filter=(filter == null ? false : filter);
        var opts = '';
        for(var i=0;i<dataList.length;i++){
            var op=dataList[i]
            if(filter){
                var searchFilter=$("#searchFilter").val();
                if(commonUtil.isNotEmpty(searchFilter)){
                    if(!((op.communityName.indexOf(searchFilter)>=0))){
                        continue;
                    }
                }
            }
            opts+='<option value="'+op.id+'" >'+op.communityName+'</option>'
        }
        $('#'+bindSelect).empty();
        $('#'+bindSelect).append(opts)
    }
    //获取所有未被绑定的小区
    $http.post(CONFIG.interface.findCommunityAvailable,{}).success(function (data) {
        if(data.resStatus==CONFIG.CONSTANTS.SUCCESS_STATE){
           $scope.communityAvailable=data.resMsg;
            $scope.filterOptions()
        }
    }).error(function () {
        //do nothing..
    });

    //获取物业账户已经绑定的权限
    $http.post(CONFIG.interface.findTenementsCommunity,{'id':$routeParams.id}).success(function (data) {
        if(data.resStatus==CONFIG.CONSTANTS.SUCCESS_STATE){
            $scope.areadyAssignedComunities=data.resMsg;
            $scope.filterOptions(null, $scope.areadyAssignedComunities,'multiselect_to')
        }
    }).error(function () {
        //do nothing..
    });
    function _afterExecuteFn(data) {
        $("#modalText").modal('hide');
        if(commonUtil.isNotEmpty(data) && data.resStatus==CONFIG.CONSTANTS.SUCCESS_STATE) {
            commonUtil.createNotifyAndRedirect("小区绑定成功!", ALERT_SUC, $location, "/tenement");
        }else{
            if(data.resMsg==ALERT_WARN){    //未做操作提交警告
                commonUtil.createSimpleNotify("请选择要绑定的小区");
            }else {
                commonUtil.createNotifyAndRedirect(data.resMsg,ALERT_ERR, $location, "/tenement");
            }
        }

    }
    //保存权限
    $scope.saveCommunityRel=function(){
        var selectedValues=[];
        //选择所有的右侧菜单
        $("#multiselect_to option").each(function () {
            selectedValues.push($(this).attr("value"))
        });
        $("#ids").attr("name", "communitiesIdsNew");
        $("#ids").val(selectedValues);
        var contents = '确定绑定小区物业账号?';
        commonUtil.createCustomConfirmBox("绑定小区到物业账号", contents, CONFIG.interface.bindCommunityToTenement+"?regUserId="+$routeParams.id,null,_afterExecuteFn);
    }



});