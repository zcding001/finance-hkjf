/**
 * 首页标的信息的controller
 */

var indexBids = {};
(function() {
    'use strict';

    /**
     * 转换标的开始时间
     * */
    indexBids.bidStartTime=function (bidId,startTime) {
        var contents = '';
        var timeId = 'timer' + bidId;
        contents+='<div id="'+timeId+'" >'
            +'<span id="timer_d'+timeId+'">0</span>天'
            +'<span id="timer_h'+timeId+'">0</span>时'
            +'<span id="timer_m'+timeId+'">0</span>分'
            +'<span id="timer_s'+timeId+'">0</span>秒'
            +'</div>';
        indexBids.timerArrayTostart[bidId] = startTime;
        return contents;
    }
    //去往标的详情页
    indexBids.toBidInfoDetail=function(bidId, productId) {
        //登录验证
        if(commonUtil.needLogin()){
        	containerUtil.add(CONSTANTS.TO_INVEST_BID_ID, bidId);
            //不显示保障计划
            containerUtil.remove("securityPlanContract");
            var dstUrl = '';
            if(productId == 0){
                dstUrl = "individualDetails.html";
            }else if(productId == 5){
                dstUrl = "experienceDetails.html";
            }else if(productId ==6||productId ==9){
                dstUrl = "gfbDetails.html";
            }else {
                dstUrl = "investDetails.html";
            }
            commonUtil.jump("invest/" + dstUrl );
        }
    }


    //待启动的数组
    indexBids.timerArrayTostart=[];

    //启动时间组件
    indexBids.startTimerArray=function() {
        for(var i=0;i<indexBids.timerArrayTostart.length;i++){
            if(indexBids.timerArrayTostart[i]!=null){
                commonUtil.timer('timer'+i, indexBids.timerArrayTostart[i]+'');
            }
        }

    }

    //转换日期单位
    indexBids.transferTermUnit=function(termUnit) {
        if(termUnit==1){
            return "年";
        }
        if(termUnit==2){
            return "个月";
        }
        if(termUnit==3){
            return "天";
        }
    }

    //转换支持卡券
    indexBids.transferSupportCoupon=function(allowCoupon) {
        var span=''
        if(allowCoupon==1||allowCoupon==3){
            span+='<span>红包</span>'
        }
        if(allowCoupon==1||allowCoupon==2){
            span+='<span>加息券</span>'
        }
        return span;
    }
    //初始化单个标的信息
    indexBids.initSingleBidInfo=function (prefix,bidInfo,oneTwoThree/*一栏有多少标的*/) {
        oneTwoThree = (oneTwoThree == null ? 'one' : oneTwoThree);
        if(bidInfo!=null){
            var classPrefix="."+prefix;
            //标的名称
            $(classPrefix+"name").html(bidInfo.name);
            //初始化预期年化收益
            if(bidInfo.raiseRate!=0){
                $(classPrefix+"InterestRate").html(moneyUtil.round(bidInfo.baseRate,2));
            }else{
                $(classPrefix+"InterestRate").html(moneyUtil.round(bidInfo.interestRate,2));
            }

            //初始化加息率
            if(bidInfo.raiseRate!=0){
                // var raiseRateContents='<i class="f-16">+</i> <em class="f-20">'+moneyUtil.round(bidInfo.raiseRate,2)+'</em><i class="f-16">%</i>'
                var raiseRateContents='<em class="f-20">+'+moneyUtil.round(bidInfo.raiseRate,2)+'</em><i class="f-16">%</i>'
                $(classPrefix+"RaiseRate").html(raiseRateContents);
            }
            //初始化万元收益
            $(classPrefix+"TenThousandIncome").html(moneyUtil.formatAndRoundMoney(bidInfo.tenThousandIncome,2));

            //初始化期数
            $(classPrefix+"TermValue").html(bidInfo.termValue);
            //初始化时间单位
            $(classPrefix+"TermUnit").html(indexBids.transferTermUnit(bidInfo.termUnit));
            //初始化进度条
            var twoDotProgress=moneyUtil.round(bidInfo.progress,2);
            $(classPrefix+"Progress-width").css("width",bidInfo.progress+'%');
            $(classPrefix+"Progress-left").css("left",bidInfo.progress+'%');
            $(classPrefix+"Progress-text").html(twoDotProgress+'%')

            //投资总额
            $(classPrefix+"TotalAmount").html(moneyUtil.formatAndRoundMoney(bidInfo.totalAmount,2))

             //初始化剩余可投
            $(classPrefix+"ResidueAmount").html(moneyUtil.formatAndRoundMoney(bidInfo.residueAmount,2))

            //处理立即投资和已经售罄
            if(oneTwoThree=='one'){
                //处理状态
                if(bidInfo.state==2){
                    if(bidInfo.startTime!=null&& !bidInfo.hasStarted){
                        //倒计时
                        var tempContents='<a href="javascript:void(0);" class="xsb_tzbt">'+indexBids.bidStartTime(bidInfo.id,bidInfo.startTime)+'</a>';
                        $(classPrefix+"Detail").empty().append(tempContents)
                    }else{
                        $(classPrefix+"Detail").empty().append('<a href="javascript:void(0)"  class="xsb_tzbt" onclick="indexBids.toBidInfoDetail('+bidInfo.id+','+bidInfo.productType+')">立即投资</a>')
                    }
                }else{
                    $(classPrefix+"Detail").empty().append('<button class="btn_end">已售罄</button>')
                }
            }

            //处理立即投资和已经售罄
            if(oneTwoThree=='three'){
                //处理加息券红包等等
                $(classPrefix+"supportCoupon").html(indexBids.transferSupportCoupon(bidInfo.allowCoupon));
                //处理投资状态
                if(bidInfo.state==2){
                    if(bidInfo.startTime!=null&& !bidInfo.hasStarted){
                        //倒计时
                        $(classPrefix+"Detail").empty().append('<button type="button" class="lj_button" style="background: #f7931e;">'+indexBids.bidStartTime(bidInfo.id,bidInfo.startTime)+'</button>');
                    }else{
                        $(classPrefix+"Detail").empty().append('<div class="ljtz_btn "><a href="javascript:void(0)"  onclick="indexBids.toBidInfoDetail('+bidInfo.id+','+bidInfo.productType+')">立即投资</a></div>')
                    }
                }else{
                    $(classPrefix+"Detail").empty().append('<button type="button" class="lj_button">已售罄</button>')
                }
            }


        }else{
            //隐藏该div
        }

    }
    //初始化首页标的信息
    indexBids.initIndexBidInfo=function () {
        //加载首页标的数据
        ajaxUtil.post("bidInfoController/findIndexBidInfo.do", {}, function(data){

            if(data.resStatus == CONSTANTS.SUCCESS){
                //初始化新手专享
                var grennHandBid=data.resMsg.greenhandBid;
                indexBids.initSingleBidInfo("grennHand",grennHandBid)
                //初始化体验金
                var experienceBid=data.resMsg.experienceBid;
                if(experienceBid!=null){
                    indexBids.initSingleBidInfo("experience",experienceBid)
                }else{
                    $('#experienceDiv').addClass('hide');
                }

                //初始化按月付息，一次本息
                //按月付息
                var repayByMonthBid=data.resMsg.repayByMonthBid;
                if(repayByMonthBid!=null){
                    //月月盈
                    indexBids.initSingleBidInfo("repayByMonth-month-", repayByMonthBid[0],'three');
                    //季季盈
                    indexBids.initSingleBidInfo("repayByMonth-quarter-", repayByMonthBid[1],'three');
                    //年年盈
                    indexBids.initSingleBidInfo("repayByMonth-year-", repayByMonthBid[2],'three');
                }

                //一次本息
                var repayOnceBid=data.resMsg.repayOnceBid;
                if(repayOnceBid!=null){
                    //月月盈
                    indexBids.initSingleBidInfo("repayOnceBid-month-", repayOnceBid[0],'three');
                    //季季盈
                    indexBids.initSingleBidInfo("repayOnceBid-quarter-", repayOnceBid[1],'three');
                    //年年盈
                    indexBids.initSingleBidInfo("repayOnceBid-year-", repayOnceBid[2],'three');
                }else{
                    $('#repayOnceBidDiv').addClass("hide");
                }

                //推荐，爆款
                var recommendBid=data.resMsg.recommendBid;
                var hotBid=data.resMsg.hotBid;
                $("#recommendBidInfoDiv").addClass("hide")
                $("#hotBidInfoDiv").addClass("hide")
                if(recommendBid!=null&&hotBid!=null){
                    $("#recommendBidInfoDiv").removeClass("hide")
                    $("#hotBidInfoDiv").removeClass("hide")
                    indexBids.initSingleBidInfo("recommend", recommendBid);
                    indexBids.initSingleBidInfo("hot", hotBid);
                }else if(recommendBid!=null&&hotBid==null){
                    $("#recommendBidInfoDiv").removeClass("hide")
                    indexBids.initSingleBidInfo("recommend", recommendBid);
                }else if(recommendBid==null&&hotBid!=null){
                    $("#hotBidInfoDiv").removeClass("hide")
                    indexBids.initSingleBidInfo("hot", hotBid);
                }

            }
            //启动时间组件
            indexBids.startTimerArray();
        });
    }



})();


