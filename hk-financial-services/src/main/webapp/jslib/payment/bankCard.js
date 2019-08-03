var bankController ={};
(function(){
    'use strict';
    var regUser = containerUtil.get(CONSTANTS.LOGIN_REG_USER);
    var fnAccount = containerUtil.get(CONSTANTS.LOGIN_FIN_ACCOUNT);
    var bankCardId = "";
    bankController.initData = function () {
        if(regUser.identify==0){//未实名认证
            commonUtil.jumpAccountMenu("securityCenter.html");
        }
        //获取用户绑定的银行卡
        ajaxUtil.post("/bankCardBindingController/toBankCard.do", null , _bankBingCallBack);
    };
    bankController.viewBankInfo = function () {

       bankCardId = containerUtil.getTransient();
        if(regUser.identify==0){//未实名认证
            commonUtil.jumpAccountMenu("securityCenter.html");
        }
        $("#useableMoney").text("￥"+fnAccount.useableMoney);

        //获取url参数
        if(bankCardId != undefined && bankCardId != ''){
            //银行卡信息
            ajaxUtil.post("/bankCardBindingController/getBankCard.do", "id="+bankCardId , _bankCardInfoCallBack);
        }else{
            //实名信息
            ajaxUtil.post("/bankCardBindingController/getRegUserDetail.do",null , _realNameCallBack);
            //初始化省市县、及更新操作
            dictionaryUtil.initBankCardAreaSelect("bankProvince", "bankCity","","");
        }


    };
    function _bankBingCallBack(data){
        if (data.resStatus == CONSTANTS.SUCCESS) {
            if(data.resMsg== null){
                $(".bc_add").show();
                $(".bc_list").hide();
            }else{
                $(".bc_add").hide();
                $(".bc_list").show();
                bankCardId = data.resMsg.id;
                data.resMsg.state = dictionaryUtil.getName('bank', 'state', data.resMsg.state);
                data.resMsg.bankCity = dictionaryUtil.getAreaName(data.resMsg.bankCity);
                data.resMsg.bankProvince = dictionaryUtil.getAreaName(data.resMsg.bankProvince);
                renderUtil.renderElement(data.resMsg);

            }
        }else{
            dialogUtil.alert("信息提示：",data.resMsg);
        }
    };
    function _bankCardInfoCallBack(data){
        $("#id").val(data.resMsg.id);
        $("#realName").val(data.params.realName);
        $("#bankCard").val(data.resMsg.bankCard);
        if(data.resMsg.state !=0 && data.resMsg.state != 1){
            $("#bankCard").addClass("disabled").attr("readonly","readonly");
        }
        $("#branchName").val(data.resMsg.branchName);
        //初始化省市县、及更新操作
        dictionaryUtil.initBankCardAreaSelect("bankProvince", "bankCity",data.resMsg.bankProvince,"");
        $("#bankProvince").val(data.resMsg.bankProvince);
        $("#bankCity").val(data.resMsg.bankCity);

    };
    function _realNameCallBack(data){
        if (data.resStatus == CONSTANTS.SUCCESS) {
            $("#realName").val(data.resMsg);
        }else{
            dialogUtil.alert("信息提示：",data.resMsg);
        }
    };
    $("#bankAdd").click(function(){
        commonUtil.jumpAccountMenu("directCard_1.html");
    });
    $("#bankUpdate").click(function(){
        commonUtil.jumpAccountMenu("directCard_1.html",''+bankCardId);
    });
    //绑定银行卡
    $("#bank_save_bt").click(function(){
        if(validateSubmit()){
            ajaxUtil.post("/bankCardBindingController/bindingCard.do",  $("#bankForm").serialize() , function(data){
                if (data.resStatus == CONSTANTS.SUCCESS) {
                    dialogUtil.alert("信息提示：",data.resMsg);
                    $("#includeContent").attr("file", "directCard.html");
                    commonUtil.includeOuterHtml("directCard.html");
                }else{
                    dialogUtil.alert("信息提示：",data.resMsg);
                }
            });
        }

    });
    function validateSubmit(){
        var bankCard = $("#bankCard").val();
        if (bankCard == '') {
            dialogUtil.alert("信息提示：","请输入银行卡号");
            return false;
        }
        if ($("#bankProvince option:selected").attr("value") == -999) {
            dialogUtil.alert("信息提示：","请选择银行开户省份");
            return false;
        }
        if ($("#bankCity option:selected").attr("value") == -999) {
            dialogUtil.alert("信息提示：","请选择银行开户城市");
            return false;
        }

        var branchName = $("#branchName").val();
        if (branchName == '') {
            dialogUtil.alert("信息提示：","请输入支行名称");
            return false;
        }

        var reg = /^[\u4E00-\u9FFFa-zA-Z0-9\d-]+$/;//中文，数字，字母，-
        if(!reg.test(branchName)){
            dialogUtil.alert("信息提示：","请输入正确的支行名称");
            return false;
        }
        return true;
    }
})();


