/**
 * 海外基金主页js
 * @type {{}}
 */
var overseasFund={};
(function () {
    //创建列表函数 div的className使用自定义避免和样式混在一起
    overseasFund.buildPartFundInfos = function(className , type , fundInfos) {
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
                    +'<h3 class="xtcpname">{name}</h3>'
                    +'<ul class="xtliebiao">'
                    +'<li class="xtleix">';
                    if(fundAuthFlag == 2){
                        //content +='<span class="renzhengkj" style="cursor: text;text-decoration: none">{termDescribe}</span>'
                        content +='<span class="renzhengkj" style="font-size: 16px;cursor: text;text-decoration: none"><b style="font-size: 30px;">{termValue}</b>';
                        if(temp.termUnit == 1){
                            content += '&nbsp;年';
                        }else if(temp.termUnit == 2){
                            content += '&nbsp;个月';
                        }else if(temp.termUnit == 3){
                            content += '&nbsp;日';
                        }
                        content += '</span>';
                    }else{
                        content +='<span onclick="fundCommon.authenJump('+ fundAuthFlag +',2)" class="renzhengkj">认证可见</span>';
                    }
                content +='<em>存续期限</em>'
                    +'</li>'
                    +'<li class="xtleix">'
                    +'<span style="color: #f39200;font-size: 30px;margin-bottom: 4px;margin-top: -8px;">'
                    +'{lowestAmount}'
                    +'<i style="font-size: 16px;margin-left: 8px;">万</i>'
                    +'</span>'
                    +'<em>起投金额（';
                if(temp.lowestAmountUnit == 1 ){
                    content += '人民币';
                }else{
                    content += '美元';
                }
                content += '）</em>'
                    +'</li>'
                    +'<li class="xtleix">'
                    +'<span title="HC Global Fund Services">{management}</span>'
                    +'<em>管理机构</em>'
                    +'</li>'
                +'<li class="xtleix">';
                if(temp.subscribeState == 0){
                    content +='<a class="xsb_tzbt noyuyue" style="background:#ccc;border:none;color:#fff;cursor:auto;">立即预约</a>';
                }else{
                    content +='<a onclick="fundCommon.advisoryJump({id},2)" class="xsb_tzbt">立即预约</a>';
                }
                content += '</li>'
                +'</ul>'
                +'<p class="cpldian">'
                +'开放日：{openDateDescribe}'
                +'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;产品亮点：{highlights}</p>'
                +'</li>';
                content = content.format(temp);
                contents += content;
            }
        }
        $("."+className).empty();
        $("."+className).append(contents);
    }

    //加载各部分的列表
    overseasFund.getOverseasInfos = function() {
        //查询海外基金项目list 循环拼接元素
        $.ajax({
            type : 'POST',
            url : commonUtil.getRequestUrl("/fundIndexController/fundInfoList.do"),
            data : {parentType : 2},//海外基金
            dataType : "json",
            success : function(data) {
                if(data.resStatus == CONSTANTS.SUCCESS){
                    var fundInfos = data.resMsg;
                    overseasFund.buildPartFundInfos('overseasFund', 5, fundInfos);
                }
            },
            error : function(request) {}
        });
    }
}());

$(function () {
    containerUtil.set("fund_utype" , 2);
    //绑定退出操作事件
    $(".logoutBtn").each(function(){
        $(this).unbind("click").click(function () {
            fundCommon.logout(2);
        });
    });
    /*************主流程****************/
    //加载轮播图
    fundCommon.loadInformations(4);
    $(".overseasFund").empty();
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
            fundCommon.loginJump(2);
        }else{
            return false;
        }
    });

    // 跳转注册绑定
    $('.zhucebut').click(function(){

        if($('.zhucebut').hasClass("zhucebut")){
            fundCommon.registJump(2);
        }else{
            return false;
        }
    });
    //异步查询实名测评状态,完成回调加载各列表
    fundCommon.getFundAuthentication(overseasFund.getOverseasInfos);

    //实名测评判断前置
    fundCommon.authenJump(fundAuthFlag , 2);
});