/**
 * 产品介绍
 * @type {{}}
 */
var pefundInfo = {};
(function () {

    //创建左侧产品名字列表 如果有id则选择此项
    pefundInfo.loadLeftList = function(utype , id){
        $.ajax({
            type : 'POST',
            url : commonUtil.getRequestUrl("/fundIndexController/fundInfoList.do"),
            data : {parentType : utype , startNum : 0 , pageSize : 20},
            dataType : "json",
            success : function(data) {
                if(data.resStatus == CONSTANTS.SUCCESS){
                    fundInfos = data.resMsg;
                    pefundInfo.buildLeftList('leftList' , fundInfos , id);
                }
            },
            error : function(request) {}
        });
    }

    pefundInfo.buildLeftList = function(className , fundInfos , infoId){
        var div = $("." + className);
        //遍历项目获取name和id创建列表
        var contents = '';
        var showCounts = 0;
        var isHasSelected = false;
        for (var i=0;i < fundInfos.length;i++){
            //停约和不在开放日的不显示
            var temp = fundInfos[i];
            if(temp.subscribeState == 0){
                continue;
            }
            showCounts ++;
            contents += '<li ';
            //默认先将第一项选择状态并加载右侧(不管有没有传来指定id)
            if(showCounts == 1){
                contents += 'class="A_visited"';
                //加载第一个的右侧
                pefundInfo.buildRightInfoById(temp.id);
            }
            //如果传来id，且列表中有对应产品则选中此项 此时列表并未加载buildRightInfoById中修改样式无效
            if(temp.id == infoId){
                contents += 'class="A_visited"';
                //加载选中的产品右侧
                pefundInfo.buildRightInfoById(temp.id);
                isHasSelected = true;
            }
            contents += '><a href="javascript:void(0);" id="'+ temp.id+'" '
                    +'onclick="pefundInfo.buildRightInfoById('+ temp.id+')">'+ temp.name +'</a></li>';

        }
        $("."+className).empty();
        $("."+className).append(contents);
        //加载完成后，如果在列表集合中，有选中项(包括第一项),再移除其他选中样式
        if(isHasSelected){
            $("#" + infoId).parent().siblings().removeClass("A_visited");
        }
    }

    //创建右侧内容显示区域
    pefundInfo.buildRightInfoById = function(id){
        console.info($("#" + id));
        //遍历fundInfos集合，获取对应id的产品显示在右侧
        $("#" + id).parent().removeClass("A_visited").addClass("A_visited").siblings().removeClass("A_visited");
        var div = $(".rightInfo");
        var contents = '';
        for (var i=0;i < fundInfos.length;i++){
            var temp = fundInfos[i];
            var content = '';
            if(temp.id == id){
                //根据infoExist 显示右侧详情与否
                if(temp.infoExist == 0){
                    content += '<span id="projectCode" hidden="">'+ temp.id +'</span>';
                    <!--产品介绍-->
                    content += '<dl style="" id="noProject">'
                                    +'<div style="width: 700px;white-space: nowrap;overflow: hidden;text-overflow: ellipsis;height: 30px;"><i> </i> '
                                        +'<span id="typeName">'
                                        + temp.name
                                        +'</span>'
                                    +'</div>'
                                    +'<p class="pro_intro_p">系列简介</p>'
                                    +'<ul style="padding-top: 15px;line-height:25px;" id="typeDesc">'
                                        +'<br>'+ temp.introduction +'<br>'
                                    +'</ul>'
                                +'</dl>';
                }else{
                    var lowestAmountUnitStr = "人民币";
                    if(temp.lowestAmountUnit == 2){
                        lowestAmountUnitStr = '美元';
                    }
                    content += '<span id="projectCode" hidden="">{id}</span>';
                    //根据parentType或utype显示何种字段
                    if(temp.parentType == 1 || temp.parentType == 2){
                        content += '<dl style="" id="hasProject">'
                                        +'<div class="ellipsis" style="width: 700px;"><i></i> '+
                                            '<span id="name">{name}</span>'+
                                        '</div>'+
                                        '<p class="pro_intro_p">产品简介</p>'+
                                        '<ul>'+
                                            '<li class="ellipsis" style="margin-top:8px;">'+
                                                '<span>'+
                                                '<span>产品名称：</span> '+
                                                '</span> '+
                                                ' <span id="projectName" title="{name}">{name}</span>'+
                                            '</li> '+
                                            '<li><span>起投金额：</span> '+
                                                ' <span> <span id="bottomAmount">{lowestAmount}</span>万（'+ lowestAmountUnitStr + '）</span>'+
                                            '</li> '+
                                            '<li><span>基金管理人：</span> '+
                                                ' <span id="management">{management}</span>'+
                                            '</li> '+
                                            '<li><span>开放日：</span> '+
                                                ' <span id="openday">{openDateDescribe} </span>'+
                                            '</li> '+
                                            '<li><span>存续年限：</span> '+
                                            ' <span id="termDescribe">{termDescribe} </span>'+
                                            '</li> '+
                                            '<li><span>运作方式：</span> '+
                                                ' <span id="operationMode">{operationStyle}</span>'+
                                            '</li> '+
                                            '<li style="margin-bottom:8px;">'+
                                                '<span style="vertical-align: top;">项目介绍：</span>'+
                                                ' <span style="width: 544px;display: inline-block;" id="investRange">{investRange}</span>'+
                                            '</li>'+
                                        '</ul>'+
                                    '</dl>';
                    }else if(temp.parentType == 3){
                        content += '<dl style="" id="hasProject">'
                                        +'<div style="width: 700px;white-space: nowrap;overflow: hidden;text-overflow: ellipsis;height: 30px;"><i></i> '+
                                            '<span id="name">{name}</span>'+
                                        '</div>'+
                                            '<p class="pro_intro_p">产品简介</p>'+
                                        '<ul>'+
                                            '<li style="margin-top:8px;white-space: nowrap;overflow: hidden;text-overflow: ellipsis">'+
                                                '<span>'+
                                                '<span>产品名称：</span> '+
                                                '</span>'+
                                                ' <span id="projectName" title="{name}">{name}</span>'+
                                            '</li> '+
                                            '<li><span>认购起点：</span> '+
                                                ' <span><span id="bottomAmount">{lowestAmount}</span>万（'+ lowestAmountUnitStr + '）起</span>'+
                                            '</li> '+
                                            '<li><span>收益类型：</span> '+
                                                ' <span id="revenueType">{revenueType}</span>'+
                                            '</li> '+
                                            '<li><span>成立日期：</span> '+
                                                ' <span id="establishedTime">' + dateUtil.date(temp.establishedTime) + '</span>'+
                                            '</li> '+
                                            '<li><span>信托年限：</span> '+
                                            ' <span id="termDescribe">{termDescribe} </span>'+
                                            '</li> '+
                                            '<li style="margin-bottom:8px;"><span>产品状态：</span> '+
                                                ' <span id="operationMode">在售</span>'+
                                            '</li> '+
                                        '</ul>'+
                                        '<ul style="padding-top: 8px;font-size: 14px;">'+
                                        '<li style="color: #888888;"> {investRange}</li>'+
                                        '</ul>'+
                                    '</dl>';
                    }

                    content = content.format(temp);
                }
                //更新预约按钮状态
                if(temp.auditState == 1){
                    //预约中
                    $("#noUse").show();
                    $("#isUse").hide();
                }else{
                    $("#isUse").show();
                    $("#noUse").hide();
                }
                //修改id隐藏域
                contents += content;
                break;
            }
        }
        div.empty();
        div.append(contents);
    }

    //海外基金跳转到协议第一步,带过去选择的产品id
    pefundInfo.toFundAdvisory = function(utype , id){
        //alert("go海外基金协议啊"+id);
        commonUtil.jump("/fund/protocol/protocolInfo.html?id=" + id + "&utype=" + utype);
    }

    pefundInfo.advisoryTips = function (ResultType) {
        $(".bgzz").show();
        $("."+ResultType).show();
    }

    pefundInfo.advisoryProcess = function (gqInfoInp , utype , checkFlag) {
        var authenResult = fundCommon.authenJump(fundAuthFlag , utype , 2);
        if(!authenResult){
            return;
        }
        $(gqInfoInp[3]).val(($("#projectCode").html() + "").trim());
        if(utype != 2){
            if(!checkFlag){
                return false;
            }
        }else{
            pefundInfo.toFundAdvisory(utype , $(gqInfoInp[3]).val());
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
            pefundInfo.advisoryTips("adviTip");
        }else{
            var result = fundCommon.reservationFund(gqInfoInp);
            if(result == 'success'){
                advisoryCount = advisoryCount + 1;
                containerUtil.set("fund_advisory_"+ utype + "_" +tel , (advisoryCount));
                pefundInfo.advisoryTips("adviSuccess");
            }else if(result == 'more'){
                pefundInfo.advisoryTips("adviTip");
                advisoryCount = advisoryCount + 1;
                containerUtil.set("fund_advisory_"+ utype + "_" +tel, (advisoryCount));
            }else if(result == 'noselect'){
                pefundInfo.advisoryTips("adviFail");
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
    var gqInfoInp = $('.gq_info_inp');
    //fundCommon.getFundAuthentication();  初始化公共导航时已经查询
    //首先获取utype
    utype = commonUtil.getUrlParam("utype");//检查url中是否指定，优先级1
    utype = fundCommon.initUtype(utype);//检查参数中是否指定，优先级2
    
    //获取海外基金可能传过来id
    var infoId = commonUtil.getUrlParam("id");

    if(utype == 1){
        $(".pro_content_2").show();
    }
    $(gqInfoInp[2]).val(utype);
    if(commonUtil.isLogin()){
        //回显手机号和姓名
        var user = containerUtil.get(CONSTANTS.LOGIN_REG_USER);
        $(gqInfoInp[1]).val(user.login)
    }else{
        fundCommon.jumpIndex(utype);
    }
    
    /**
     * 海外基金特殊处理流程
     */
    var nameFlag = true;
    var telFlag = true;
    if(utype ==2){
        //1.隐藏表单区域
        $(".info_tel").hide();
        $(".gq_info_box").hide();
        //2.后面提交不校验
    }else{
        //绑定失去焦点事件
        $(gqInfoInp[0]).blur(function () {
            nameFlag = fundCommon.checkTelOrName("name" , $(this).val() , "gqUsername");
        });
        $(gqInfoInp[1]).blur(function () {
            telFlag = fundCommon.checkTelOrName("tel" , $(this).val() , "qgTel");
        });
    }

    //清空左侧和右侧区域
    $(".leftList").empty();
    $(".rightInfo").empty();
    //加载左侧列表
    pefundInfo.loadLeftList(utype , infoId);

    //提示关闭事件绑定
    $('.quedingbut').click(function () {//预约弹框关闭
        $('.yuyuecg,.bgzz').hide()
    })

    //预约事件绑定
    $("#gqSubmit").click(function () {
        $(gqInfoInp[0]).blur();
        $(gqInfoInp[1]).blur();
        pefundInfo.advisoryProcess(gqInfoInp , utype , nameFlag&&telFlag);
    });

});