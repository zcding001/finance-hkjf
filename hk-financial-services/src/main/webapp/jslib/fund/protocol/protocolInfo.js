/**
 * 海外基金签署协议第一步
 * @type {{}}
 */
var protocolInfo={};
(function () {
    //校验非空的
    protocolInfo.checkNullValue = function(id,label,all){
        var msg = "";
        var regex = /^[0-9]+$/;
        var isNum = false;
        var isEmail = false;
        var regexEmail = /^\w+([-+.']\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
        var isRemove = true;
        var isName = false;
        if(label == undefined || label == null || label == ""){
            msg = "必填项不能为空";
        }else{
            msg = "请填写正确" + label+"";
            if(label.indexOf("数字") > -1){
                isNum = true;
            }
            if(label.indexOf("邮箱") > -1){
                isEmail = true;
            }
            if(label.indexOf("实名") > -1){
                isName = true;
            }
        }
        var $li = $('#'+id);  //li
        var $ele = $('input[name='+ id + ']');
        //如果有多个共用一个
        if(all != undefined && all != null && all != ""){
            $li = $('#'+all);
            id = all;
        }
        //获取旧的错误提示 判断是移除还是追加
        var oldErrorMsg = $('#error_'+id).html();
        if( oldErrorMsg != null && oldErrorMsg != undefined){
            //只要包含其他的提示就不能直接移除  indexOf不包含返回-1
            if(oldErrorMsg.indexOf(label) == -1){
                //追加或扣减
                isRemove = false;
            }
            if(oldErrorMsg.indexOf(label) > -1 &&  oldErrorMsg.indexOf("和") > -1){
                isRemove = false;
            }
            if(oldErrorMsg.indexOf(label) > -1 &&  oldErrorMsg.indexOf("和") == -1){
                isRemove = true;
            }
            if(oldErrorMsg == '请填写正确'){
                isRemove = true;
            }

        }
        if( $ele.val() == undefined || $ele.val() == null || $.trim($ele.val()) == ""){
            if(isRemove){
                $('#error_'+id).remove();
                $li.after(" <p id = 'error_"+id+"' class = 'noticeword' >"+ msg +"</p>")
            }else{
                if(oldErrorMsg.indexOf(label) == -1){
                    $('#error_'+id).html(oldErrorMsg + "和" + label);
                }
            }
            return false;
        }
        if(isNum){
            var number = parseFloat($.trim($ele.val()));
            if(number.toString() == "NaN" || !regex.test($.trim($ele.val()))){
                if(isRemove){
                    $('#error_'+id).remove();
                    $li.after(" <p id = 'error_"+id+"' class = 'noticeword' >"+ msg +"</p>")
                }else{
                    if(oldErrorMsg.indexOf(label) == -1){
                        $('#error_'+id).html(oldErrorMsg + "和" + label);
                    }
                }
                return false;
            }
        }
        if(isName){
            if( $userName != undefined && $userName != "" && $userName.indexOf($.trim($ele.val())) == -1){
                //输入姓名不在实名信息中
                if(isRemove){
                    $('#error_'+id).remove();
                    $li.after(" <p id = 'error_"+id+"' class = 'noticeword' >"+ msg +"</p>")
                }else{
                    if(oldErrorMsg.indexOf(label) == -1){
                        $('#error_'+id).html(oldErrorMsg + "和" + label);
                    }
                }
                return false;
            }
        }
        if(isEmail){
            if(!regexEmail.test($.trim($ele.val()))){
                //TODO 如果多个共用提示信息中有邮箱输入需要修改
                $('#error_'+id).remove();
                $li.after(" <p id = 'error_"+id+"' class = 'noticeword' >"+ msg +"</p>")
                return false;
            }
        }
        //$('#error_'+id).remove();
        if(isRemove){
            $('#error_'+id).remove();
        }else{
            //验证通过扣减此提示关键字
            var newMsg = "";
            newMsg = oldErrorMsg.replace(eval("/和" + label +"/g"),"");
            newMsg = newMsg.replace(eval("/" + label + "和/g"),"");
            newMsg = newMsg.replace(eval("/" + label + "/g"),"");
            $('#error_'+id).html(newMsg);
        }
        return true;
    }

    /**
     * 校验起投金额最小值及步长
     * @param id
     * @returns {boolean}
     */
    protocolInfo.checkInvestAmount = function(id){
        var $li = $('#'+id);  //li
        //var $ele = $('#' + id);
        var $ele = $('input[name='+ id + ']');
        var msg = "";
        var unValidateNumber = parseFloat($.trim($ele.val()));
        var _bottomAmount = parseFloat($bottomAmount);
        var _stepValue = parseFloat($stepValue);

        var regex = /^[0-9]+([.]{1}[0-9]{1,2})?$/;
        if (unValidateNumber.toString() == "NaN" || !regex.test(unValidateNumber)) {
            msg = "请输入正确的金额";
            $('#error_'+id).remove();
            $li.after(" <p id = 'error_"+id+"' class = 'noticeword' >"+ msg +"</p>")
            return false;
        }
        if(_bottomAmount > unValidateNumber){
            msg = "最低认购金额为" + $bottomAmount + $lowestAmountUnitStr ;
            $('#error_'+id).remove();
            $li.after(" <p id = 'error_"+id+"' class = 'noticeword' >"+ msg +"</p>")
            return false;
        }
        if(_stepValue != "NaN" && _stepValue != 0 && (unValidateNumber - _bottomAmount) % _stepValue != 0){
            msg = $bottomAmount + $lowestAmountUnitStr +"起投，" + $stepValue + $lowestAmountUnitStr +"整数倍递增";
            $('#error_'+id).remove();
            $li.after(" <p id = 'error_"+id+"' class = 'noticeword' >"+ msg +"</p>")
            return false;
        }
        $('#error_'+id).remove();
        return true;
    }

    /**
     * 回显表单项及设置关键元素
     * @param infoId
     */
    protocolInfo.initUserAgreementForm = function (infoId) {
        $.ajax({
            type : 'POST',
            url : commonUtil.getRequestUrl("/fundAgreementController/initUserAgreement.do"),
            data : {fundInfoId : infoId},
            dataType : "json",
            async : false,
            success : function(data) {
                if(data.resStatus == CONSTANTS.SUCCESS){
                    //回显表单内容
                    var _agreement = data.resMsg;
                    $.each(_agreement , function (name , value) {
                        $("input[name='"+ name +"']").val(value);
                    });
                    //设置关键变量
                    $bottomAmount = _agreement["lowestAmount"];
                    $stepValue = _agreement["stepValue"];
                    $userName = _agreement["realName"];
                    var lowestAmountUnit = _agreement["lowestAmountUnit"];
                    if(lowestAmountUnit == 1){
                        $lowestAmountUnitStr = "元";
                    }else{
                        $lowestAmountUnitStr = "美元";
                    }
                    $("#lowestAmountUnitId").html($lowestAmountUnitStr);
                    $("#birthday").html(_agreement["birthday"]);
                    //设置起投和步长的显示提示信息
                    if($stepValue == null || $stepValue == undefined){
                        $stepValue = 0;
                    }
                    var tip = $bottomAmount + $lowestAmountUnitStr + "起投，" + $stepValue + $lowestAmountUnitStr +"递增";
                    $("input[name='investAmount']").attr("placeholder",tip);
                    //签名图片
                    if(_agreement["signature"] != null && _agreement["signature"] != undefined && _agreement["signature"] != ''){
                        var imgurlPix = "${oss.url}";
                        $("#imghead").attr("src",imgurlPix + _agreement["signature"]);
                    }
                }else if(data.resStatus == CONSTANTS.SESSION_TIME_OUT){
                    commonUtil.jump("/fund/overseasFund.html");
                }else{
                    commonUtil.jump("/fund/advisory/pefundInfo.html?utype=2");
                }
            },
            error : function(request) {alert("系统繁忙，请稍后再试~")}
        });
    }

    /**
     * 校验协议表单及提交保存
     * @param infoId
     */
    protocolInfo.submitUserAgreementForm = function (infoId) {
        //触发校验所有表单元素
        $(":input").each(function(a,b){
            $(this).blur();
        });

        //单独校验金额
        var investAmountFlag = protocolInfo.checkInvestAmount("investAmount");
        $('#error_signatureLi').remove();
        //获取错误元素并滚动到所在位置
        var errors = $(".noticeword");
        if(errors != null && errors != undefined && errors.length > 0){
            var scroll_offset = errors[0].offsetTop;
            $("body,html").animate({
                scrollTop : scroll_offset - 40
            }, 500);
            return false;
        }
        if(!investAmountFlag){
            return false;
        }

        //校验签名图片无需滚动
        var signatureUrlFlag = true;
        //页面回显原来图片，可以不校验
        if($("#signatureBytes").attr("value") == null || $("#signatureBytes").attr("value") == ""){
            signatureUrlFlag = fundCommon.preImg("previewImg","signatureLi","签名");
        }
        //但是重新上传了图片也需要校验
        if(document.getElementById("previewImg").files[0] != "undefined" && document.getElementById("previewImg").files[0] != null){
            signatureUrlFlag = fundCommon.preImg("previewImg","signatureLi","签名");
        }
        if(!signatureUrlFlag){
            return false;
        }
        $("#loading").modal('show');
        $(".reg_tjbtn").unbind("click");
        var data = new FormData($('#agreementForm')[0]);
        $.ajax({
            type : 'POST',
            url : commonUtil.getRequestUrl("/fundAgreementController/saveUserAgreement.do"),
            dataType : 'json',
            data : data,
            cache: false,
            async : false,
            processData: false,
            contentType: false,
            success : function(data) {
                if(data.resStatus == CONSTANTS.SUCCESS){
                    console.info(data.params.agreementId);
                    commonUtil.jump("/fund/protocol/protocolSign.html?utype=2&aId=" + data.params.agreementId);
                }else if(data.resStatus == 201 || data.resStatus == 202){
                    $(".reg_tjbtn").click(function () {
                        protocolInfo.submitUserAgreementForm(infoId);
                    });
                    alert(data.resMsg);
                }else{
                    alert("预约失败，请重新选择要预约的产品");
                    commonUtil.jump("/fund/advisory/pefundInfo.html?utype=2");
                }
                $("#loading").modal('hide');
            },
            error : function(request) {
                $("#loading").modal('hide');
                //重新绑定点击事件
                $(".reg_tjbtn").click(function () {
                    protocolInfo.submitUserAgreementForm(infoId);
                });
                alert("系统繁忙，请稍后刷新页面再试~");
            }
        });

    }

}());

var $bottomAmount = 0;
var $stepValue = 0;
var $userName = '';
var $lowestAmountUnitStr = "美元";
$(function () {
    //绑定海外基金页面的退出事件
    $(".logoutBtn").each(function(){
        $(this).unbind("click").click(function () {
            fundCommon.logout(2);
        });
    });

    isLogin = commonUtil.isLogin();
    if(!isLogin){
        fundCommon.jumpIndex(2);
    }

    //获取预约页面传过来的产品id
    var infoId = commonUtil.getUrlParam("id");//检查url中是否指定，优先级1
    if(infoId == undefined || infoId == null){
        //TODO 回到预约详情页
        commonUtil.jump("/fund/advisory/pefundInfo.html?utype=2");
    }

    //获取用户签署信息并初始化表单
    protocolInfo.initUserAgreementForm(infoId);


    //注册失去焦点校验事件，页面元素直接绑定
    //表单提交事件注册
    $(".reg_tjbtn").click(function () {
    	//$("#loading").modal('show');
        protocolInfo.submitUserAgreementForm(infoId);
    });

});