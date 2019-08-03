/**
 * Created by dzc on 18.1.23.
 */

var autoInvestController = {};

(function(){
    'use strict';
    var columns = [
        {
            title: '最低年化</br>收益率', data: 'minRate', tdClass: 'tac', thClass: 'tac',thStyle: 'line-height:16px',
            render: function (value, row) {
                return value + "%";
            }
        },
        {
            title: '投资周期', tdClass: 'tac', thClass: 'tac',thStyle: 'line-height:34px',
            render: function (value, row) {
                return row.investTermMin + "-" + row.investTermMax + "个月";
            }
        },
        {
            title: '还款方式', data:'repayType', tdClass: 'tac blh-34', thClass: 'tac',thStyle: 'line-height:34px',
            render: function (value, row) {
                if(value == 0){
                    return "按月付息，到期还本</br>到期一次还本付息"
                }
                return dictionaryUtil.getName("invest", "bid_repayment", value);
            }
        },
        {
            title: '账户预留</br>金额(元)', data: 'reserveAmount', tdClass: 'tac', thClass: 'tac',thStyle: 'line-height:16px'
        },
        {
            title: '当前排名', data: 'currIndex', tdClass: 'tac', thClass: 'tac',thStyle: 'line-height:34px',
            render: function (value) {
                if(value == 0){
                    return '<span id = "currIndex">--</span>';
                }else{
                    return '<span id = "currIndex">' + value + '</span>';
                }
            }
        },
        {
            title: '有效期', data: 'effectiveType', tdClass: 'tac', thClass: 'tac', thStyle: 'line-height:34px',
            render: function (value, row) {
                if(value == 1){
                    return "长期有效";
                }else{
                    if(new Date().getTime() > row.effectiveEndTime){
                        return "失效";
                    }else{
                        return dateUtil.date(row.effectiveStartTime) + "至" + dateUtil.date(row.effectiveEndTime);
                    }
                }
            }
        },
        {
            title: '状态', data: 'state', tdClass: 'tac pt-10', thClass: 'tac', thStyle: 'line-height:34px',
            render: function (value, row) {
                var content = '<div class="slideunlock-wrapper"><input type="hidden" value="1" class="slideunlock-lockable">';
                if(value == 1){
                    content += '<div id="open" class="slideunlock-slider success"><span class="slideunlock-label" onclick="autoInvestController.chgState(\'' + row.id + '\')" style="left: 75px;"></span><span class="b">已启用</span></div></div>'
                }else{
                    content += '<div id="open" class="slideunlock-slider"><span class="slideunlock-label" onclick="autoInvestController.chgState(\'' + row.id + '\')"  style="left: 5px;"></span><span class="b">已禁用</span></div></div>'
                }
                return content;
            }
        },
        {
            title: '操作', tdClass: 'tac', thClass: 'tac blh-34',thStyle: 'line-height:34px',
            render: function (value, row) {
                return "<a href='javascript:void(0)' onclick='autoInvestController.delOpe(\"" + row.id+ "\")' class='colr-red'>删除</a>" +
                    "&nbsp;<a href='javascript:void(0)'  onclick='autoInvestController.preUpdateOpe(\"" + row.id+ "\")' class='colr-blue'>修改</a>";
            }
        }
        
    ];

    /**
     * 加载自动投资配置信息
     */
    autoInvestController.loadBidAutoSchemeList = function(){
        renderUtil.renderList("/bidInfoController/bidAutoSchemeList.do", columns, "", function(data){
            var list = data.resMsg.data;
            if(list == null || list == undefined || list.length <= 0){
                $("#noDataDiv").html('<div class="set_autovestmentmethod" onclick="autoInvestController.toAdd()"><img src="../src/img/account/icon_add.png"> 设置自动投资方案</div>')
            }
            renderUtil.renderElement(data.params);
        });
    };

    /**
     * 显示添加页面
     */
    autoInvestController.toAdd = function(){
        $(".addBidAutoSchemeDiv").show();
        $(".bidAutoSchemeListDiv").hide();
        $("#yyy").empty();
        $("#yyy").append(dictionaryUtil.getOption("invest", "auto_invest_priority", 9, null, null, null, "--请选择优先级--"));
        $("#jjy").empty();
        $("#jjy").append(dictionaryUtil.getOption("invest", "auto_invest_priority", 9, null, null, null, "--请选择优先级--"));
        $("#nny").empty();
        $("#nny").append(dictionaryUtil.getOption("invest", "auto_invest_priority", 9, null, null, null, "--请选择优先级--"));


        // xsbFlag: 0为展示 1为隐藏
        if($("#xsbFlag").val() == 0){
            $("#xsbId").show();
            $("#xsb").empty();
            $("#xsb").append(dictionaryUtil.getOption("invest", "auto_invest_priority", 9, null, null, null, "--请选择优先级--"));
        }

        /**验证年化收益*/
        $("#minRate").blur(function(){
            _checkMinRate($(this).val());
        });

        /**投资范围添加校验*/
        $("#investTermMax, #investTermMin").blur(function(){
            _checkInvestTerm($(this).val(), $(this).attr("id"))
        });

        /**预留金额校验*/
        $("#reserveAmount").blur(function(){
            _checkReserveAmount($(this).val());
        });

        /**验证投资有效期*/
        $("#customRange, #longRange").focus(function(){
            if($(this).val() == 2){
                $("#custTimeDiv").show();
                $("#effectiveStartTime, #effectiveEndTime").blur(function(){
                    _checkEffectiveTime($(this).val());
                });
            }else{
                $("#custTimeDiv").hide();
                $("#effectiveStartTime, #effectiveEndTime").unbind("blur");
            }
        });

        /**验证标的优先级*/
        $("#yyy, #jjy, #nny, #xsb").blur(function(){
            var idValue = $(this).attr("id");
            var errId = "err_priority";
            var value = $(this).val();
            var arr = [$("#yyy"), $("#jjy"), $("#nny"), $("#xsb")];
            for(var i in arr){
                if(value == arr[i].val() && idValue != arr[i].attr("id") && value != 9){
                    return _addErrMsg(errId, "优先级不能重复!");
                }
            }
            return _addErrMsg(errId, "");
        });

        function checkBidPriority(priorityArr){
            for(var i = 0; i < priorityArr.length;i++){
                for(var j = 0; j < priorityArr.length;j++){
                    if(i != j && priorityArr[i] == priorityArr[j] && priorityArr[i] != "9"){
                        _addErrMsg("err_priority", "优先级不能重复！");
                        return false;
                    }
                }
            }
             _addErrMsg("err_priority","");
            return true;
        }


        //查看协议
        $(".hkjf_Protocol").click(function(){
            commonUtil.jump("contract/autoInvestAgreement.html",1);
        });
        //提交事件
        $("#submitBtn").unbind("click").click(function(){
            if(_checkMinRate($("#minRate").val()) && _checkInvestTerm($("#investTermMin").val(), "investTermMin")
                && _checkInvestTerm($("#investTermMax").val(), "investTermMax")
                && _checkReserveAmount($("#reserveAmount").val())
            ){
                //判断还款方式是否选中
                if(!$('#a1').is(':checked') && !$('#a2').is(':checked')){
                    return _addErrMsg("err_repayType", "至少选择一种还款方式!");
                }
                //自定义有效时间，检查输入的有效时间
                if($('input:radio[name="effectiveType"]:checked').val() == 2 &&
                    (!_checkEffectiveTime($("#effectiveStartTime").val()) || !_checkEffectiveTime($("#effectiveEndTime").val()))){
                    return false;
                }
                //判断标的优先级是否选中
                var yyy = $("#yyy").val();
                var jjy = $("#jjy").val();
                var nny = $("#nny").val();
                var xsb = $("#xsb").val();

                if(($("#xsbFlag").val() != 0 && yyy == "9" && jjy == "9" && nny == "9") ||
                    ($("#xsbFlag").val() == 0 && yyy == "9" && jjy == "9" && nny == "9" && xsb == "9")){
                        return _addErrMsg("err_priority", "请您至少选择一个优先级类型！");
                }
                var arrContainXsb = [yyy, jjy, nny, xsb];
                var arrWithoutXsb = [yyy, jjy, nny];
                if(($("#xsbFlag").val() == 0 && !checkBidPriority(arrContainXsb)) ||
                    ($("#xsbFlag").val() != 0 && !checkBidPriority(arrWithoutXsb))){
                    return ;
                }

                //判断协议是否选中
                _addErrMsg("err_conformContract", "");
                if(!$('#conformContract').is(':checked')){
                    return _addErrMsg("err_conformContract", "请勾选协议");
                }
                //提交表单
                var param = $("#dataForm").serializeObject();
                param["bidPriority"] = $("#yyy").val() + $("#jjy").val() + $("#nny").val() + (($("#xsb").val() == null || $("#xsb").val() == "") ? 0 : $("#xsb").val()) ;
                if($('#a1').is(':checked') && !$('#a2').is(':checked')){
                    param["repayType"] = 2;
                }else if(!$('#a1').is(':checked') && $('#a2').is(':checked')){
                    param["repayType"] = 3;
                }else if($('#a1').is(':checked') && $('#a2').is(':checked')){
                    param["repayType"] = 0;
                }
                dialogUtil.confirm("温馨提示", "是否马上启动自动投标方案?", function(){_doSubmit(param, 1);}, "不启用", function(){_doSubmit(param, 0);}, "", "启用");
            }
        });
    };

    /**
     * 更新配置状态
     * @param id
     */
    autoInvestController.chgState = function(id){

        var state = $(".b").text() == "已启用" ? 0 : 1;
        ajaxUtil.post("/bidInfoController/updateBidAutoSchemeState.do", {id : id, state : state}, function(data){
            if(data.resStatus == CONSTANTS.SUCCESS){
                if(state == 0){
                    $("#open").removeClass("success");
                    $(".slideunlock-label").css({left: '5px'});
                    $("#state").val(1);
                    $(".b").text("已禁用");
                    $("#currIndex").html("--");
                }else{
                    $("#open").addClass("success");
                    $(".slideunlock-label").css({left: '75px'});
                    $("#state").val(0);
                    $(".b").text("已启用");
                    $("#currIndex").html(data.params.currIndex);
                }
            }else{
                dialogUtil.alert("更新自动投资状态", data.resMsg);
            }
        });
    };

    /**
     * 删除配置
     * @param id
     */
    autoInvestController.delOpe = function(id){
        dialogUtil.confirm("系统提示", "确定要删除该方案吗?", function(){_doDelOpe(id, 1);}, "取消", function(){_doDelOpe(id, 0);}, "", "确定");
    };

    function _doDelOpe (id,state){
        if(state == 0){
            return false;
        }
        ajaxUtil.post("/bidInfoController/delBidAutoScheme.do", {id : id}, function(data){
            if(data.resStatus == CONSTANTS.SUCCESS){
                document.getElementById("dataForm").reset();
                $(".idNum").val("");
                $(".Validform_right").removeClass("Validform_right");
                renderUtil.refresh();
            }else{
                dialogUtil.alert("删除自动投资配置", data.resMsg);
            }
        });
    }



    /**
     * 预更新自动投资配置
     * @param id
     */
    autoInvestController.preUpdateOpe = function(id){
        ajaxUtil.post("/bidInfoController/preUpdateBidAutoScheme.do", {id : id}, function(data){
            autoInvestController.toAdd();
            renderUtil.renderElement(data);
            var repayType = data.repayType;
            if(repayType == 2){
                $('#a1').attr("checked","checked");
                $('#a2').removeAttr("checked");
            }else if(repayType == 3){
                $('#a1').removeAttr("checked");
            }
            if(data.effectiveType == 2){
                $("#customRange").attr("checked", "checked");
                $("#custTimeDiv").show();
                $("#effectiveStartTime").val(dateUtil.date(data.effectiveStartTime));
                $("#effectiveEndTime").val(dateUtil.date(data.effectiveEndTime));
            }
            if(data.useCouponFlag == 0){
                $("#a6").attr("checked", "checked");
            }
            var bidPriority = data.bidPriority + "";
            $("#yyy").val(bidPriority.substring(0, 1));
            $("#jjy").val(bidPriority.substring(1, 2));
            $("#nny").val(bidPriority.substring(2, 3));
            $("#xsb").val(bidPriority.substring(3, 4));
            if(data.priorityType == 2){
                $("#a8").attr("checked", "checked");
            }

        });
    };

    function _doSubmit(param, state){
        param["state"] = state;
        ajaxUtil.post("/bidInfoController/updateBidAutoScheme.do", param, function(data){
            if(data.resStatus == CONSTANTS.SUCCESS){
                $(".addBidAutoSchemeDiv").hide();
                $(".bidAutoSchemeListDiv").show();
                renderUtil.refresh();
            }else{
                dialogUtil.alert("自动投资", data.resMsg);
            }
        });
    }

    /**
     * 校验预期年化收益率
     * @param value
     * @returns {*}
     * @private
     */
    function _checkMinRate(value){
        var errId = "err_rateAtm";
        if(value == '' || value == null){
            return _addErrMsg(errId, "请输入最低预期年化收益率！");
        }else if(!/^[0-9]+([.]{1}[0-9]{1,2})?$/.test(value) || parseFloat(value) < 5 || parseFloat(value) > 100){
            return _addErrMsg(errId, "最低年化收益率：最大值100%，最小不能低于5%，最多保留两位小数");
        }
        return _addErrMsg(errId, "");
    }

    /**
     * 校验投资周期
     * @param value
     * @param idValue
     * @returns {*}
     * @private
     */
    function _checkInvestTerm(value, idValue){
        var errId = "err_investTerm";
        if(value == '' || value == null){
            return _addErrMsg(errId, "请输入最小投资期限!");
        }else if(!/^[0-9]+$/.test(value) || parseFloat(value) < 1 || parseFloat(value) > 36){
            return _addErrMsg(errId, "投资期限最小1个月,投资期限最大36个月!");
        }
        if(idValue == "investTermMax"){
            var investTermMin = $("#investTermMin").val();
            var min = (investTermMin == undefined || investTermMin == '') ? 1 : investTermMin;
            if(parseFloat(value) < parseFloat(min)){
                return _addErrMsg(errId, "期限最小值不能大于最大值!");
            }
        }else{
            var investTermMax = $("#investTermMax").val();
            var max = (investTermMax == undefined || investTermMax == '') ? 36 : investTermMax;
            if(parseFloat(value) > parseFloat(max)){
                return _addErrMsg(errId, "期限最小值不能大于最大值!");
            }
        }
        return _addErrMsg(errId, "");
    }

    /**
     * 校验预留金额
     * @param value
     * @returns {*}
     * @private
     */
    function _checkReserveAmount(value){
        var errId = "err_reserveAmount";
        if(value == '' || value == null){
            return _addErrMsg(errId, "请输入预留金额!");
        }else if(!/^[0-9]+([.]{1}[0-9]{1,2})?$/.test(value)){
            return _addErrMsg(errId, "账户预留金额输入有误（最多保留到小数点后两位）");
        }
        return _addErrMsg(errId, "");
    }

    function _addErrMsg(id, msg){
        if(msg != undefined && msg.length > 0){
            $("#" + id).addClass("Validform_wrong").text(msg);
            return false;
        }else{
            $("#" + id).removeClass("Validform_wrong").addClass("Validform_right").text("");
            return true;
        }
    }

    function _checkEffectiveTime(value){
        var errId = "err_time";
        if(value == '' || value == null || value == undefined){
            return _addErrMsg(errId, "请输入自动投资有效期!");
        }
        return _addErrMsg(errId, "");
    }
})();

$(function(){
    //用户未实名，直接去实名页面
    var regUser = containerUtil.get(CONSTANTS.LOGIN_REG_USER);
    if(regUser == undefined || regUser.identify == 0){
        commonUtil.jumpAccountMenu("securityCenter.html");
    }else{
        autoInvestController.loadBidAutoSchemeList();
        $(".wenhao").mouseover(function(){
            $(".system").fadeIn().siblings(".wenhao").fadeOut();
        });
        $(".system").mouseout(function(){
            $(".system").fadeOut().siblings(".wenhao").fadeIn();
        });
    }
});