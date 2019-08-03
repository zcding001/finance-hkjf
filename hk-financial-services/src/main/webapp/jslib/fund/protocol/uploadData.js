/**
 * 海外基金签署协议第一步
 * @type {{}}
 */
var uploadData={};
(function () {

    /**
     * 回显表单项各图片
     */
    uploadData.initUserDataForm = function (aId) {
        $.ajax({
            type : 'POST',
            url : commonUtil.getRequestUrl("/fundAgreementController/initUserData.do?aId="+aId),
            data : {},
            dataType : "json",
            async : false,
            success : function(data) {
                if(data.resStatus == CONSTANTS.SUCCESS){
                    var _userInfo = data.resMsg;
                    //回显图片
                    var imgurlPix = "${oss.url}";
                    if(_userInfo["idUpUrl"] != null && _userInfo["idUpUrl"] != undefined && _userInfo["idUpUrl"] != ''){
                        $("#imghead").attr("src",imgurlPix + _userInfo["idUpUrl"]);
                        $("#idUpUrl").val(_userInfo["idUpUrl"]);
                    }
                    if(_userInfo["idDownUrl"] != null && _userInfo["idDownUrl"] != undefined && _userInfo["idDownUrl"] != ''){
                        $("#imgheada").attr("src",imgurlPix + _userInfo["idDownUrl"]);
                        $("#idDownUrl").val(_userInfo["idDownUrl"]);
                    }
                    if(_userInfo["passportUrl"] != null && _userInfo["passportUrl"] != undefined && _userInfo["passportUrl"] != ''){
                        $("#imgheadb").attr("src",imgurlPix + _userInfo["passportUrl"]);
                        $("#passportUrl").val(_userInfo["passportUrl"]);
                    }
                    if(_userInfo["id"] != null && _userInfo["id"] != undefined && _userInfo["id"] != ''){
                        $("input[name='userInfoId']").val(_userInfo["id"]);
                    }
                }else if(data.resStatus == CONSTANTS.SESSION_TIME_OUT){
                    commonUtil.jump("/fund/overseasFund.html");
                }else{
                    //commonUtil.jump("/fund/advisory/pefundInfo.html?utype=2");
                }
            },
            error : function(request) {alert("系统繁忙，请稍后再试~")}
        });
    }

    /**
     * 校验协议表单及提交保存
     * @param aId 协议id
     */
    uploadData.submitUserDataForm = function (aId) {
        //校验签名图片无需滚动
        var signatureUrlFlag = true;
        //如果页面没有回显原来上传的图片，说明需要上传图片校验
        var idUpUrlFlag = true;
        var idDownUrlFlag = true;
        var passportUrlFlag = true;
        //页面回显原来图片，可以不校验
        if($("#idUpUrl").val() == null || $("#idUpUrl").val() == ""){
            idUpUrlFlag = fundCommon.preImg("previewImg","idCard","身份证");
        }
        //但是重新上传了图片也需要校验
        if(document.getElementById("previewImg").files[0] != "undefined" && document.getElementById("previewImg").files[0] != null){
            idUpUrlFlag = fundCommon.preImg("previewImg","idCard","身份证");
        }
        if($("#idDownUrl").val() == null || $("#idDownUrl").val() == ""){
            idDownUrlFlag = fundCommon.preImg("previewImga","idCard","身份证");
        }
        if(document.getElementById("previewImga").files[0] != "undefined" && document.getElementById("previewImga").files[0] != null){
            idUpUrlFlag = fundCommon.preImg("previewImga","idCard","身份证");
        }
        if($("#passportUrl").val() == null || $("#passportUrl").val() == ""){
            passportUrlFlag = fundCommon.preImg("previewImgb","passportUl","护照"); //passportUl ul对应的id
        }
        if(document.getElementById("previewImgb").files[0] != "undefined" && document.getElementById("previewImgb").files[0] != null){
            idUpUrlFlag = fundCommon.preImg("previewImgb","passportUl","护照");
        }
        if(!(idUpUrlFlag && idDownUrlFlag && passportUrlFlag)){
            signatureUrlFlag = false;
        }
        if(!signatureUrlFlag){
            return false;
        }
        $("#loading").modal('show');
        $(".reg_tjbtn").unbind("click");
        var data = new FormData($('#userDataForm')[0]);
        data.append("aId",aId);
        if($("input[name='userInfoId']").val()!= "" && $("input[name='userInfoId']").val() != undefined){
            data.append("userInfoId",$("input[name='userInfoId']").val());
        }
        $.ajax({
            type : 'POST',
            url : commonUtil.getRequestUrl("/fundAgreementController/uploadOrUpdateUserInfo.do"),
            dataType : 'json',
            data : data,
            cache: false,
            async : false,
            processData: false,
            contentType: false,
            success : function(data) {
                if(data.resStatus == CONSTANTS.SUCCESS){
                    console.info(aId);
                    commonUtil.jump("/fund/protocol/submitSuccess.html?utype=2");
                }else if(data.resStatus == 201 || data.resStatus == 202){
                    $(".reg_tjbtn").click(function () {
                        uploadData.submitUserDataForm(aId);
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
                    uploadData.submitUserDataForm(aId);
                });
                alert("系统繁忙，请稍后刷新页面再试~");
            }
        });

    }

}());

$(function () {
    //绑定海外基金页面的退出事件
    $(".logoutBtn").each(function(){
        $(this).unbind("click").click(function () {
            fundCommon.logout(2);
        });
    });

    //获取预约页面传过来的产品id
    var aId = commonUtil.getUrlParam("aId");//检查url中是否指定，优先级1
    if(aId == undefined || aId == null || aId ==''){
        //TODO 回到预约详情页
        commonUtil.jump("/fund/advisory/pefundInfo.html?utype=2");
    }

    //顶部登录信息切换
    isLogin = commonUtil.isLogin();
    if(!isLogin){
        fundCommon.jumpIndex(2);
    }

    //获取用户签署信息并初始化表单
    uploadData.initUserDataForm(aId);


    //注册失去焦点校验事件，页面元素直接绑定
    //表单提交事件注册
    $(".reg_tjbtn").click(function () {
        uploadData.submitUserDataForm(aId);
    });

    //防止页面后退
    history.pushState(null, null, document.URL);
    window.addEventListener('popstate', function () {
        history.pushState(null, null, document.URL);
    });

});