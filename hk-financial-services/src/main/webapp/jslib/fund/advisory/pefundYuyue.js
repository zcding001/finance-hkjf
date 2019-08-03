/**
 * 立即预约
 * @type {{}}
 */
var pefundYuyue = {};
(function () {

    //创建左侧产品名字列表 如果有id则选择此项
    pefundYuyue.loadBoxList = function(utype , id){
        $.ajax({
            type : 'POST',
            url : commonUtil.getRequestUrl("/fundIndexController/fundInfoList.do"),
            data : {parentType : utype , startNum : 0 , pageSize : 20},
            dataType : "json",
            success : function(data) {
                if(data.resStatus == CONSTANTS.SUCCESS){
                    fundInfos = data.resMsg;
                    pefundYuyue.buildBoxList('infoBoxes' , fundInfos , id);
                }
            },
            error : function(request) {}
        });
    }

    pefundYuyue.buildBoxList = function(className , fundInfos , infoId){
        var div = $("." + className);
        //遍历项目获取name和id创建列表
        var contents = '';
        for (var i=0;i < fundInfos.length;i++){
            //停约和不在开放日的不显示
            var temp = fundInfos[i];
            if(temp.subscribeState == 0){
                continue;
            }
            contents += '<li class="check-li">'+
                '<label title="'+ temp.name +'"><input type="checkbox" ';
            //如果传来id，且列表中有对应产品则选中此项
            if(temp.id == infoId){
                contents += 'checked ';
            }
            contents += 'class="input_check" value="'+ temp.id +'"> '+ temp.name +'</label>'+
            '</li>';
        }
        $("."+className).empty();
        $("."+className).append(contents);
    }

    pefundYuyue.advisoryTips = function (ResultType) {
        $(".bgzz").show();
        $("."+ResultType).show();
    }

    pefundYuyue.advisoryProcess = function (gqInfoInp , utype , checkFlag) {
        var authenResult = fundCommon.authenJump(fundAuthFlag , utype , 2);
        if(!authenResult){
            return;
        }
        var arrChk=$(".input_check:checked");
        if(arrChk.length==0) {//没有选中企业
            alert('请选择预约的产品');
            return;
        }else if(arrChk.length>3){
            alert('每次最多只能预约3个产品');
            return;
        }
        //遍历选中的项目，获取项目id以","连接起来
        var ids = "";
        $.each(arrChk,function(){
            //alert($(this).val());
            if(ids == ""){
                ids = $(this).val();
            }else{
                ids = ids + "," + $(this).val();
            }
        });
        //设置ids
        $(gqInfoInp[3]).val(ids.trim());
        //设置remark
        $(gqInfoInp[4]).val(($("#remark").val() + "").trim());
        if(utype != 2){
            if(!checkFlag){
                return false;
            }
        }else{
            //海外基金无此页面
            pefundInfo.toFundAdvisory(utype , $(gqInfoInp[4]).val());
            return;
        }
        //本地预约计数器
        var tel = containerUtil.get(CONSTANTS.LOGIN_REG_USER).login;
        var advisoryCount = containerUtil.get("fund_advisory_"+ utype + "_" +tel);
        if(advisoryCount == null || advisoryCount == undefined){
            advisoryCount = 0;
        }
        //实名测评又不是企业 三次以内 走预约
        if(advisoryCount >= 3){
            pefundYuyue.advisoryTips("adviTip");
        }else{
            var result = fundCommon.reservationFund(gqInfoInp);
            if(result == 'success'){
                advisoryCount = advisoryCount + 1;
                containerUtil.set("fund_advisory_"+ utype + "_" +tel , (advisoryCount));
                pefundYuyue.advisoryTips("adviSuccess");
            }else if(result == 'more'){
                pefundYuyue.advisoryTips("adviTip");
                advisoryCount = advisoryCount + 1;
                containerUtil.set("fund_advisory_"+ utype + "_" +tel , (advisoryCount));
            }else if(result == 'noselect'){
                pefundYuyue.advisoryTips("adviFail");
            }else{
                fundCommon.authenJump(fundAuthFlag , utype , 1);
            }
        }
    }

}());

var utype;
var fundInfos={}; //第一次加载时页面保存获取到全部产品
$(function () {
    /*******主流程********/
    var gqInfoInp = $('.xingininput');
    //fundCommon.getFundAuthentication();  初始化公共导航时已经查询
    //首先获取utype
    utype = commonUtil.getUrlParam("utype");//检查url中是否指定，优先级1
    utype = fundCommon.initUtype(utype);//检查参数中是否指定，优先级2
    /**
     * 海外基金特殊处理 跳转至主页
     */
    if(utype == 2){
        commonUtil.jump("/fund/overseasFund.html");
        return;
    }

    //获取传过来id
    var infoId = commonUtil.getUrlParam("id");
    $(gqInfoInp[2]).val(utype);
    if(commonUtil.isLogin()){
        //回显手机号和姓名
        var user = containerUtil.get(CONSTANTS.LOGIN_REG_USER);
        $(gqInfoInp[1]).val(user.login)
    }else{
        fundCommon.jumpIndex(utype);
    }

    var nameFlag = true;
    var telFlag = true;
    //绑定失去焦点事件
    $(gqInfoInp[0]).blur(function () {
        nameFlag = fundCommon.checkTelOrName("name" , $(this).val() , "gqUsername");
    });
    $(gqInfoInp[1]).blur(function () {
        telFlag = fundCommon.checkTelOrName("tel" , $(this).val() , "qgTel");
    });

    //清空区域
    $(".check-li").empty();
    //加载复选列表
    pefundYuyue.loadBoxList(utype , infoId);

    //提示关闭事件绑定
    $('.quedingbut').click(function () {//预约弹框关闭
        $('.yuyuecg,.bgzz').hide()
    })

    //预约事件绑定
    $(".lijiyuybuton").click(function () {
        $(gqInfoInp[0]).blur();
        $(gqInfoInp[1]).blur();
        pefundYuyue.advisoryProcess(gqInfoInp , utype , nameFlag&&telFlag);
    });

});