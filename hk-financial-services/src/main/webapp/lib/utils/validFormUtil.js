/**
 * Created by dzc on 17.12.7.
 * 封装validForm应用
 */

var validFormUtil = {};
(function() {
    'use strict';

    /**
     * 初始化valid from元素
     * @param uri 异步请求的地址
     * @param callback 请求成功的后的回调函数
     * @param beforeSubmit 请求之前需要调用的函数
     * @param dataFormId 表单id 默认值 dataForm
     * @param btnId 表单提交的按钮 默认值submitBtn
     * @param errMsgFun 表单验证失败的显示模式，默认错误信息显示在resErrMsg元素上
     * @param errMsgId 显示错误信息的id
     * @param tipType 错误信息显示样式
     * @returns {*|jQuery} validForm对象
     */
    validFormUtil.initForm = function(uri, callback, beforeSubmit, dataFormId, btnId, errMsgFun, errMsgId, tipType){
        if(dataFormId == undefined || dataFormId == ""){
            dataFormId = "dataForm";
        }
        if(btnId == undefined || btnId == ""){
            btnId = "submitBtn";
        }
        if(errMsgId == undefined || errMsgId == ""){
            errMsgId = "resErrMsg";
        }
        if(tipType == undefined || (tipType + "").length <= 0){
            tipType = function(msg, o, cssctl){
                if(errMsgFun == undefined || errMsgFun == ''){
                    $("#" + errMsgId).text(msg);
                }else{
                    errMsgFun.call(undefined, msg);
                }
            };
        }
        var loginForm = $("#" + dataFormId).Validform({
            tiptype: tipType,
            showAllError: false,
            ajaxPost: true,
            btnSubmit: "#" + btnId,
            beforeSubmit:function(curForm){
                $("#" + errMsgId).text("");
                if(beforeSubmit == undefined || beforeSubmit == ""){
                    return true;
                }
                return beforeSubmit.call(undefined, curForm);
            },
            callback:function(data){}
        }).config({
            ajaxpost : {
                url : CONSTANTS.BASE_PATH + uri,
                success:function(data){
                    callback.call(undefined, data);
                },
                error:function(data,obj){
                    $("#" + errMsgId).text("操作失败，请联系管理员!");
                }
            }
        });

        $('#passwdTmp').bind('keypress',function(event){
            if(event.keyCode == "13") {
                loginForm.submitForm(false);
            }
        });
        return loginForm;
    };

    /**
     * 获得指定name的value，封装成o.name=value对象
     * @param names
     * @returns {{}}
     */
    validFormUtil.getFormData = function(names){
        var o = {};
        if(names != undefined && names.length > 0){
             for(var i in names){
                 o[names[i]] = $("*[name='"+ names[i] +"']").val();
             }
        }
        return o;
    }
})();
