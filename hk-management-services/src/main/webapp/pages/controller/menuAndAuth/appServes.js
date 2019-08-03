//维护系统菜单的controller
app.controller('appServes', function($scope,$http){

    //添加菜单前审核
    $scope.validateCheck = function () {
        if (commonUtil.isEmpty($("#typeArea").val())) {
            $("#checkPacketErro").text('请填写分类名称!');
            return false;
        }
        if (commonUtil.isEmpty($("#serviceNameArea").val())) {
            $("#checkPacketErro").text('请填写菜单名称!');
            return false;
        }
        // if (commonUtil.isEmpty($("#seqNoArea").val())) {
        //     $("#checkPacketErro").text('请填写排序编号!');
        //     return false;
        // }

        if ($("#pid").val() != 0 && commonUtil.isEmpty($("#serviceUrlArea").val())) {
            $("#checkPacketErro").text('请填写菜单链接地址!');
            return false;
        }

        return true;
    }

    //添加App菜单前审核
    $scope.validateAppCheck = function () {
        if (commonUtil.isEmpty($("#typeArea").val())) {
            $("#checkPacketErro").text('请填写分类名称!');
            return false;
        }
        if (commonUtil.isEmpty($("#serviceNameArea").val())) {
            $("#checkPacketErro").text('请填写菜单名称!');
            return false;
        }
        // if (commonUtil.isEmpty($("#seqArea").val())||!commonUtil.isNumberByValue($("#seqArea").val())) {
        //     $("#checkPacketErro").text('请正确排序编号!');
        //     return false;
        // }

        if ($("#id").val()== 0) {
            if($("#icoUrlArea")[0].files[0]==null||$("#icoUrlArea")[0].files[0]==undefined){
                $("#checkPacketErro").text('制定菜单图片!');
                return false;
            }
        }

        return true;
    }

    //列出一级菜单下面有哪些子菜单
    $scope.listChildMenus=function (id) {
        $("#forChildMenusSearchId").val(id);
        $("#childMenusTable").DataTable().search("").draw()
        $("#childMenusAlertModal").modal('show');

    }

    //创建新增菜单面板
    $scope.createAddOrUpdateMenuPanel = function (id,pid, menuName, menuDesc, menuUrl, type, seqNo) {
        //隐藏其他的弹框
        $("#childMenusAlertModal").modal('hide');

        pid = (pid == null ? 0 : pid);
        id = (id == null ? 0 : id);
        menuName = (menuName == null ? '' : menuName);
        menuDesc = (menuDesc == null ? '' : menuDesc);
        menuUrl = (menuUrl == null ? '' : menuUrl);
        seqNo = (seqNo == null ? 0 : seqNo);
        var str = '添加菜单';
        var url=CONFIG.interface.addMenu;
        if (menuName != '') {
            str = '修改菜单'
            url=CONFIG.interface.updateMenu;
        }
        var contents = '<input id="pid"  name="pid" type="hidden" value="' + pid + '" checked="checked"/>';
        contents += '<input id="id"  name="id" type="hidden" value="' + id + '" checked="checked"/>';
        contents += '菜单名称：<input id="menuNameArea" name="menuName" type="text" value="' + menuName + '" class="confirmChildren"/> <br><br>';
        contents += '菜单描述：<input id="menuDescArea" name="menuDesc" type="text" value="' + menuDesc + '" class="confirmChildren"/> <br><br>';
        if (pid != 0) {
            if(type==1 || type == 3 ){
                contents += '链接地址：<input id="menuUrlArea" name="menuUrl" type="text" value="' + menuUrl + '" class="confirmChildren"/>&nbsp;&nbsp;示例: /appMsgPushList <br><br>';
            }
            if(type==2){
                contents += '链接地址：<input id="menuUrlArea" name="menuUrl" type="text" value="' + menuUrl + '" class="confirmChildren"/>&nbsp;&nbsp;示例: borrowerRecordList.html <br><br>';
            }
        }
        contents += '排序编号：<input id="seqNoArea" name="seqNo" type="text" value="' + seqNo + '" class="confirmChildren"/> <br><br>';
        if(type==1){
            contents +='所属平台：<input id="type2" type="radio" name="type"  value="1"/>鸿坤金服后台<br><br>';
        }else if(type==2){
            contents += '所属平台：<input  type="radio" name="type"  value="2"/>我的账户 <br><br>';
        }else if(type==3){
            contents += '所属平台：<input  type="radio" name="type"  value="3"/>BI管理平台 <br><br>';
        }else if(type==null){
            contents +='所属平台：<input id="type2" type="radio" name="type"  value="1" checked="checked"/>鸿坤金服后台 &nbsp; ' +
                '<input  type="radio" name="type"  value="2"/>我的账户 <br><br>' + '<input  type="radio" name="type"  value="3"/>BI账户 <br><br>';
        }


        contents += '<span id="checkPacketErro" style="color: red"></span>'
        commonUtil.createCustomConfirmBox(str, contents, url, $scope.validateCheck, commonUtil.customConfirmStandardAfterExecuteFn);
        $("input[name='type'][value='"+type+"']").attr("checked",true);
    }

    /**
     * 上传APp端图片的ajax
     */
    $scope.uploadAppMenu=function () {
        var url=CONFIG.interface.addAppServe;
        if ($("#id").val()!= 0) {
            url=CONFIG.interface.updateAppServe;
        }
        //执行上传
        var data = new FormData();
        data.append('type', $("#typeArea").val());
        data.append('serviceName', $("#serviceNameArea").val());
        data.append('serviceUrl', $("#serviceUrlArea").val());
        data.append('isShow', $("#isShowArea").val());
        data.append('seq', $("#seqArea").val());
        if ($("#id").val()!= 0) {
            data.append('id', $("#id").val());
            data.append('updateFile', $("#icoUrlArea")[0].files[0]);
        }else{
            data.append('unSavedFile', $("#icoUrlArea")[0].files[0]);
        }
        $http.post(url, data, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        }).success(function(data) {
            commonUtil.customConfirmStandardAfterExecuteFn(data)
        });
    }
    //创建或者修改APp端Menu的Panl
    $scope.createAddOrUpdateAppServePanel = function (id, type, serviceName, serviceUrl, icoUrl, isShow,seq) {
        //隐藏其他的弹框
        $("#childMenusAlertModal").modal('hide');

        id = (id == null ? 0 : id);
        type = (type == null ? '' : type);
        serviceName = (serviceName == null ? '' : serviceName);
        serviceUrl = (serviceUrl == null ? '' : serviceUrl);
        icoUrl = (icoUrl == null ? '' : icoUrl);
        isShow = (isShow == null ? 0 : isShow);
        seq = (seq == null ? 0 : seq);
        var str = '添加菜单';
        if (id != 0) {
            str = '修改菜单'
        }
        var contents = '';
        contents += '<input id="id"  name="id" type="hidden" value="' + id + '" checked="checked"/>';
        contents += '<span style="color:red">*&nbsp;</span>分类名称：<select id="typeArea" name="type" type="text" value="' + type + '" class="confirmChildren"></select> <br><br>';
        contents += '<span style="color:red">*&nbsp;</span>菜单名称：<input id="serviceNameArea" name="serviceName" type="text" value="' + serviceName + '" class="confirmChildren"/> <br><br>';
        contents += '<span style="color:red">*&nbsp;</span>排序号：&nbsp&nbsp&nbsp&nbsp<input id="seqArea" name="seq" type="text" value="' + seq + '" class="confirmChildren"/> <br><br>';
        contents += '<span style="color:red">*&nbsp;</span>菜单链接：<input id="serviceUrlArea" name="serviceUrl" type="text" value="' + serviceUrl + '" class="confirmChildren"/> <br><br>';
        contents += '<span style="color:red">*&nbsp;</span>是否显示：<select id="isShowArea" name="isShow" type="text" value="' + isShow + '" class="confirmChildren"><option value="0">显示</option><option value="1">隐藏</option> </select> <br><br>';
        contents += '<span style="color:red">*&nbsp;</span>请选择菜单图片：<input id="icoUrlArea" name="icoUrl" type="file" /> <br><br>';
        contents += '<span id="checkPacketErro" style="color: red"></span>';
        commonUtil.createCustomConfirmBox(str, contents, "igonreUrl", $scope.validateAppCheck,commonUtil.customConfirmStandardAfterExecuteFn,$scope.uploadAppMenu);
        DIC_CONSTANT.initSelect("type", "appServe", "type");
    }

    //刷新菜单
    $scope.refreshAuthAndMenu=function () {
        $http.post(CONFIG.interface.refreshAuthAndMenu,{}).success(function (data) {
            if(data.resStatus==CONFIG.CONSTANTS.SUCCESS_STATE){
                commonUtil.createSimpleNotify("刷新成功!",ALERT_SUC);
            }
        }).error(function () {
            //do nothing...
        });
    }

    //菜单回显
    $("#selectType").append(DIC_CONSTANT.getOption("menu", "platform",'empty'))

});