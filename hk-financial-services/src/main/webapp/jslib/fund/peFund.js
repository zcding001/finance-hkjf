/**
 * 私募基金主页js
 * @type {{}}
 */
var peFund={};
var hasEstateFund=false;
(function () {
    //创建列表函数 div的className使用自定义避免和样式混在一起
    peFund.buildPartFundInfos = function(className , type , fundInfos) {
        //获取对应加载列表的div
        var div = $("." + className);
        //遍历项目根据类型加载对应的产品
        var contents = '';
        for (var i=0;i < fundInfos.length;i++){
            var temp = fundInfos[i];
            //console.info(temp);
            var content = '';
            //私募基金分类型id加载列表
            if(temp.projectId == type){
                if (type == 4) {hasEstateFund = true;}
                //注：系列介绍和项目详情不同时出现！
                if(temp.infoExist == 0){
                    if(contents == ''){
                        //第一个如果遇到无项目的就加载，只此一个,且应该可以预约
                        contents +='<div class="tl_xsb fund_mar fr" style="height: 204px;width:820px;">'
                            +'<div class="ulli-ziliao">'
                            +'<div class="meiyuanxildiv">'
                            +temp.introduction
                            +'</div>';
                        if(temp.subscribeState == 0){
                            contents += '<div class="pl-30"><a class="xsb_tzbt noyuyue" style="background:#ccc;border:none;color:#fff;">立即预约</a></div>';
                        }else{
                            contents += '<div class="pl-30"><a onclick="fundCommon.advisoryJump('+ temp.id +',1)" class="xsb_tzbt">立即预约</a></div>';
                        }
                        contents += '</div>'
                            +'</div>';
                        //contents = contents.format(temp);
                        break;
                    }else{
                        continue;
                    }
                }
                content += '<div class="tl_xsb fund_mar" style="height: 204px;width:820px;">'
                    +'<h3>{name}</h3>'
                    +'<ul class="clearfix tyb_box tyb_box_1">'
                    +'<li class="fl" style="padding-left:34px;">';
                if(fundAuthFlag == 2){
                    //content +='	<span style="color:#F39200;font-size:24px;" class="span_high">{termDescribe}</span>';
                    content +='<span style="color:#F39200;font-size:16px;text-decoration: none" class="span_high" ><b style="font-size: 30px;">{termValue}</b>';
                    if(temp.termUnit == 1){
                        content += '&nbsp;年';
                    }else if(temp.termUnit == 2){
                        content += '&nbsp;个月';
                    }else if(temp.termUnit == 3){
                        content += '&nbsp;日';
                    }
                    content += '</span>';
                }else{
                    content +='	<span onclick="fundCommon.authenJump('+ fundAuthFlag +',1)" style="color:#F39200;font-size:24px;text-decoration: underline;" class="span_high">认证可见</span>';
                }
                content +='	<p style="font-size:14px;">存续期限</p>'
                    +'</li>'
                    +'<li class="fl" style="padding-left:75px;">'
                    +'	<span class="span_high" style="font-size:30px;color:#F39200;font-weight: bold;">{lowestAmount}</span><i class="colr-org">&nbsp;万</i>'
                    +'	<p style="font-size:14px;">起投金额（';
                if(temp.lowestAmountUnit == 1 ){
                    content += '人民币';
                }else{
                    content += '美元';
                }
                content +='）</p>'
                    +'</li>'
                    +'<li class="fl" style="padding-left:75px;">'
                    +'	<span class="span_high span_name">{management}</span>'
                    +'	<p style="font-size:14px;">管理机构</p>'
                    +'</li>';
                if(temp.subscribeState == 0){
                    content +='<li class="fr"><a class="xsb_tzbt noyuyue" style="background:#ccc;border:none;color:#fff;">立即预约</a></li>';
                }else{
                    content +='<li class="fr"><a onclick="fundCommon.advisoryJump({id},1)" class="xsb_tzbt" >立即预约</a></li>';
                }
                content += '</ul>'
                    +'<div class="ulli-kaifangri">'
                    +'<p>开放日：{openDateDescribe}</p>'
                    +'<p style="margin-left: 40px;">产品亮点：{highlights}</p>'
                    +'</div>'
                    +'</div>';
                content = content.format(temp);
                contents += content;
            }
        }
        $("."+className).empty();
        $("."+className).append(contents);
    }

    //加载各部分的列表
    peFund.getPrivateInfos = function() {
        //查询私募基金项目list 循环拼接元素
        $.ajax({
            type : 'POST',
            url : commonUtil.getRequestUrl("/fundIndexController/fundInfoList.do"),
            data : {parentType : 1 , startNum : 0 , pageSize : 20},
            dataType : "json",
            success : function(data) {
                if(data.resStatus == CONSTANTS.SUCCESS){
                    var fundInfos = data.resMsg;
                    //地产 type=4
                    peFund.buildPartFundInfos('estateFund','4',fundInfos);
                    //明星独角兽 type=1
                    peFund.buildPartFundInfos('unicornFund','1', fundInfos);
                    //PRE-Pro type=2
                    peFund.buildPartFundInfos('preIpoFund','2',fundInfos);
                    //产品协同 type=3
                    peFund.buildPartFundInfos('induSynergyFund','3' ,fundInfos);
                    //如果地成基金没有产品或系列介绍就隐藏整个div
                    if(hasEstateFund == false){
                        $(".hiuhuangxl.container.clearfix").first().hide();
                    }else{
                        $(".hiuhuangxl.container.clearfix").first().show();
                    }

                    //重新设置左侧背景图高度
                    var huihuangxil=document.getElementsByClassName('hiuhuangxl');
                    for(var i=0;i<huihuangxil.length;i++){
                        var oghti=huihuangxil[i].clientHeight;
                        huihuangxil[i].getElementsByClassName('qdz_left')[0].style.height=oghti+'px'
                    }
                }
            },
            error : function(request) {}
        });
    }
}());

$(function () {
    containerUtil.set("fund_utype" , 1);
    //绑定退出操作事件
    $(".logoutBtn").each(function(){
        $(this).unbind("click").click(function () {
            fundCommon.logout(1);
        });
    });
    /*************主流程****************/
    //加载轮播图
    fundCommon.loadInformations(3);
    $(".estateFund").empty();
    $(".unicornFund").empty();
    $(".preIpoFund").empty();
    $(".induSynergyFund").empty();
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
            fundCommon.loginJump(1);
        }else{
            return false;
        }
    });

    // 跳转注册绑定
    $('.zhucebut').click(function(){

        if($('.zhucebut').hasClass("zhucebut")){
            fundCommon.registJump(1);
        }else{
            return false;
        }
    });
    //异步查询实名测评状态,完成回调加载各列表
    fundCommon.getFundAuthentication(peFund.getPrivateInfos);

    //实名测评判断前置
    fundCommon.authenJump(fundAuthFlag , 1);
});