/**
 * 信托基金主页js
 * @type {{}}
 */
var trustFund={};
(function () {
    //创建列表函数 div的className使用自定义避免和样式混在一起
    trustFund.buildPartFundInfos = function(className , type , fundInfos) {
        //获取对应加载列表的div
        var div = $("." + className);
        //遍历项目根据类型加载对应的产品
        var contents = '';
        for (var i=0;i < fundInfos.length;i++){
            var temp = fundInfos[i];
            var content = '';
            if(temp.projectId == type){
                //海外基金至少有一个产品
                if(temp.infoExist == 0){
                    continue;
                }
                content += '<li class="xtul_li">'
                    +'<h3 class="xtcpname" title="{name}">{name}</h3>'
                    +'<ul class="xtliebiao">'
                    +'<li class="xtleix">'
                    +'<span>{revenueType}</span>'
                    +'<em>收益类型</em>'
                    +'</li>'
                    +'<li class="xtleix" style="width: 22%">'
                    +'<span style="color: #f39200;font-size: 30px;margin-top: -9px;margin-bottom: 5px;">{termValue}</span>'
                    +'<em>期限（';
                if(temp.termUnit == 1){
                    content +='年';
                }else if(temp.termUnit == 2){
                    content +='月';
                }
                else if(temp.termUnit == 3){
                    content +='日';
                }
                content +='）</em>'
                    +'</li>'
                    +'<li class="xtleix" style="width: 28%">'
                    +'<span>' + dateUtil.date(temp.establishedTime) + '</span>'
                    +'<em>成立日期</em>'
                    +'</li>'
                    +'<li class="xtleix">';
                if(temp.subscribeState == 0){
                    content += '<a class="xsb_tzbt noyuyue" style="background:#ccc;border:none;color:#fff;">立即预约</a>';
                }else{
                    content += '<a onclick="fundCommon.advisoryJump({id},3)" class="xsb_tzbt">立即预约</a>';
                }
                content += '</li>'
                +'</ul>'
                    +'</li>';
                content = content.format(temp);
                contents += content;
            }
        }
        $("."+className).empty();
        $("."+className).append(contents);
    }

    //加载各部分的列表
    trustFund.getTrustInfos = function() {
        //查询信托基金项目list 循环拼接元素
        $.ajax({
            type : 'POST',
            url : commonUtil.getRequestUrl("/fundIndexController/fundInfoList.do"),
            data : {parentType : 3},//信托基金
            dataType : "json",
            success : function(data) {
                if(data.resStatus == CONSTANTS.SUCCESS){
                    var fundInfos = data.resMsg;
                    trustFund.buildPartFundInfos('trustFund', 6, fundInfos);
                }
            },
            error : function(request) {}
        });
    }
}());

$(function () {
    containerUtil.set("fund_utype" , 3);
    //绑定退出操作事件
    $(".logoutBtn").each(function(){
        $(this).unbind("click").click(function () {
            fundCommon.logout(3);
        });
    });
    /*************主流程****************/
    //加载轮播图
    fundCommon.loadInformations(5);
    $(".trustFund").empty();
    //判断是否登录状态，展示登录遮罩层
    isLogin = commonUtil.isLogin();
    if(!isLogin){
        fundCommon.xianshizezhao();
    }else{
        fundCommon.guanbizezhao();
    }
    // 跳转登录绑定
    $('.denglubut').click(function(){

        if($('.denglubut').hasClass("denglubut")){
            fundCommon.loginJump(3);
        }else{
            return false;
        }
    });

    // 跳转注册绑定
    $('.zhucebut').click(function(){

        if($('.zhucebut').hasClass("zhucebut")){
            fundCommon.registJump(3);
        }else{
            return false;
        }
    });
    //异步查询实名测评状态,完成回调加载各列表
    fundCommon.getFundAuthentication(trustFund.getTrustInfos);

    //实名测评判断前置
    //alert("标识：" + fundAuthFlag);
    fundCommon.authenJump(fundAuthFlag , 3);
});