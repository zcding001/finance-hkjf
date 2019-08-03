//积分商品分类维护的controller
app.controller('pointProductCategory',function ($scope,$injector,$http,$location,$routeParams) {
    //刷新菜单树
    function refreshTree(result) {

       if(result!=null){
           $("#modalText").modal('hide');
           if(result.resStatus==999){
               commonUtil.createSimpleNotify(result.resMsg,ALERT_ERR);
           }
           if (result.resStatus == CONFIG.CONSTANTS.SUCCESS_STATE) {
               commonUtil.createSimpleNotify("操作成功！", ALERT_SUC);
           }

       }
        //获取所有的菜单数据
        $http.post(CONFIG.interface.listCategories,{}).success(function (data) {
            $scope.tree2 = data.resMsg;
        }).error(function () {
            commonUtil.createNotifyAndRedirect("查询失败，请重试",ALERT_ERR, $location, "/pointMerchantInfoList");
        });

    } ;



    $scope.toggle = function (scope) {
        scope.toggle();
    };
    //删除分类菜单
    $scope.removeCate = function (scope) {
        var nodeValue=scope.$modelValue;
        var contents = '确定删除商品分类<label style="color: red">【'+nodeValue.title+'】</label>,该菜单以及下面子商品分类会被删除<br><br>';
        contents += '<input  name="id" type="hidden" value="'+nodeValue.id+'"/><br><br>';
        contents+='<span id="checkPacketErro" style="color: red"></span>'
        commonUtil.createCustomConfirmBox("刪除菜单", contents, CONFIG.interface.deleteCategory,null,refreshTree);
    };

    //校验菜单
    var validateCheck=function() {
        if(commonUtil.isEmpty($("#title").val())){
            $("#checkPacketErro").text('请填写商品分类名称!');
            return false;
        }
        return true;
    }

    $scope.changeSort=function (scope) {
        var nodeValue=scope.$modelValue;
        var contents = '确定改为:<label style="color: red">【'+nodeValue.sort+'】</label>?<br><br>';
        contents += '<input  name="id" type="hidden" value="'+nodeValue.id+'"/><br><br>';
        contents += '<input  name="sort" type="hidden" value="'+nodeValue.sort+'"/><br><br>';
        commonUtil.createCustomConfirmBox("修改菜单", contents, CONFIG.interface.updateCategory,null,refreshTree);
    }
    $scope.changCateName=function (scope) {
        var nodeValue=scope.$modelValue;
        var contents = '确定改为:<label style="color: red">【'+nodeValue.title+'】</label>?<br><br>';
        contents += '<input  name="id" type="hidden" value="'+nodeValue.id+'"/><br><br>';
        contents += '<input  name="name" type="hidden" value="'+nodeValue.title+'"/><br><br>';
        commonUtil.createCustomConfirmBox("修改菜单", contents, CONFIG.interface.updateCategory,null,refreshTree);
    }
    //新增菜单
    $scope.newSubItem = function (scope) {
        var contents=null;
        var titleStr=null;
        if(scope!=null){
            var nodeValue=scope.$modelValue;
            titleStr='添加菜单';
             contents = '在菜单<label style="color: red">【'+nodeValue.title+'】</label>下添加商品分类<br><br>';
            contents += '<input  name="parentId" type="hidden" value="'+nodeValue.id+'"/><br><br>';

        }else{
            //添加一级菜单
            titleStr='添加一级菜单';
            contents = '<label>添加一级商品分类</label><br><br>';
            contents += '<input  name="parentId" type="hidden" value="0"/><br><br>';
        }
        contents += '请输入菜单名称：<input id="title"  name="name" /><br><br>';
        contents += '请输入排序号：<input id="sort"  name="sort" /><br><br>';
        contents+='<span id="checkPacketErro" style="color: red"></span>'

        commonUtil.createCustomConfirmBox(titleStr, contents, CONFIG.interface.saveCategory,validateCheck,refreshTree);
    };
    //刷新redis中的商品分类
    $scope.refreshCategories = function (scope) {
        $http.post( CONFIG.interface.refreshCategories,{}).success(function (data) {
            if(data.resStatus==CONFIG.CONSTANTS.SUCCESS_STATE){
                commonUtil.createSimpleNotify("刷新成功!",ALERT_SUC);
            }
        }).error(function () {
            //do nothing...
        });

    };

    refreshTree();



})