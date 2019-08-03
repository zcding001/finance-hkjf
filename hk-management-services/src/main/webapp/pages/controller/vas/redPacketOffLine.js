//红包管理controller
app.controller('redPacketOffLine', function ($scope, $injector,$http, $location, $routeParams) {

    //初始化红包状态
    $("#packetStateFoCheck").append(DIC_CONSTANT.getOption('vas', 'redpacket_state',-1,"4,5"));
    $("#packetTypeFoCheck").append(DIC_CONSTANT.getOption('vas', 'redpacket_type',-1));
    $("#packetState").append(DIC_CONSTANT.getOption('vas', 'redpacket_state',-2));
    $("#packetType").append(DIC_CONSTANT.getOption('vas', 'redpacket_type',-2));


   //派发红包逻辑
    $scope.distributeRedPacketToUser = function () {
        $scope.userChecked = false;
        $("#userInfoModal").modal('show');
    };

    //添加线下红包逻辑
    $scope.validateRedPacket=function(){
        if(!commonUtil.isNumberByValue($("#produceNum").val())){
            $("#_confirmError").text("请正确输入红包个数");
            return false;
        }
        if(!commonUtil.isLeastTwoDigitNumber($("#produceValue").val())){
            $("#_confirmError").text("请正确输入红包金额");
            return false;
        }
        if(commonUtil.isEmpty($("#produceEndTime").val())){
            $("#_confirmError").text("请输入红包到期日期");
            return false;
        }
        return true;
    };
    $scope.addOffLineRedpacket=function() {
        var contents = '生成个数：<input id="produceNum" name="num" type="text" class="confirmChildren"/> 个<br><br>';
        contents+='红包金额：<input id="produceValue" name="value" type="text" class="confirmChildren"/> 元(金额不超过50元)<br><br>';
        contents+='到期日期：<input  id="produceEndTime"  type="text"  name="endTime" onclick="WdatePicker({dateFmt:\'yyyy-MM-dd HH:mm:ss\',minDate:\'%y-%M-#{%d+1}\'})" class="Wdate form-control" ng-model="bidInfo.startTime" name="startTime" readonly="readonly" style="width:40%;height:30px;display:inline-block;"/>';
        commonUtil.createCustomConfirmBox("生成线下红包", contents, CONFIG.interface.produceRedpacket, $scope.validateRedPacket,commonUtil.customConfirmStandardAfterExecuteFn);

    };


    //派发红包事件
    $scope.doDistribut = function () {
        var packetValue = $("#packetValue").val();
        var packetSendReason = $("#packetSendReason").val();
        var packetEndDate = $("#packetEndDate").val();
        var packetType = $("#redpt_type").val();

        if (!commonUtil.isLeastTwoDigitNumber(packetValue) || commonUtil.isEmpty(packetSendReason)) {
            $("#showErroSpan").text('错误：请输入正确的红包金额或派发原因!');
            return false;
        }

        if(commonUtil.isEmpty(packetEndDate)){
            $("#showErroSpan").text("请输入红包到期日期");
            return false;
        }
        var uids=dataTableUtil.getCheckedVal("usersTable");
        if(uids==null||uids.length==0){
            $("#showErroSpan").text('错误：请至少选择一个用户!');
            return false;
        }
        //转换为字符串，后台才能接受
        uids = uids.join(",");
        function clearDateToUse(){
            $("#userInfoModal").modal('hide');
            $("#packetValue").val('');
            $("#packetSendReason").val('');
            $("#packetEndDate").val(null);
            //撤销选中的用户
            $(".iCheck:checked").prop("checked",false)
            $("#showErroSpan").text('');
        }
        commonUtil.confirmRequest(encryptAndDecryptUtil.encrypt(JSON.stringify({value: packetValue,
            sendReason: packetSendReason,
            endTime: packetEndDate,
            redPacketType: packetType,
            userIds: uids,
            ajaxUrl:"redpacketController/distributeRedPacketToUser"})),
            "派发红包",
            "确定派发红包?");
        clearDateToUse();
    };
    /**文件上传操作**/
    $scope.importForm = {};
    $scope.uploadFile = function(){

        var data = new FormData();
        data.append('uploadFile', $("#importFile")[0].files[0]);
        $http.post(HOST + $("#url").val(), data, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        }).success(function(data) {
            //关闭上传窗口
            $("#modalExcel").modal("hide");
            if (data.resStatus == CONFIG.CONSTANTS.SUCCESS_STATE) {
                commonUtil.createSimpleNotify("文件上传成功!", ALERT_SUC)
                //刷新当前table
                $("#searchForm-searchBtn").click();
            }else{
                //提示上传错误信息
                commonUtil.createSimpleNotify(data.resMsg,  ALERT_ERR);
            }
        });
    };

});