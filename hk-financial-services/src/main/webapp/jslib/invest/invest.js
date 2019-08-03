/**
 * Created by dzc on 17.12.14.
 */

var investController = {};
(function () {
    $(function () {
        /**
         * 转换标的开始时间
         * */
        investController.bidStartTime=function (bidId,startTime) {
            var contents = '';
            var timeId = 'timer' + bidId;
            contents+='<div id="'+timeId+'" >'
                +'<span id="timer_d'+timeId+'">0</span>天'
                +'<span id="timer_h'+timeId+'">0</span>时'
                +'<span id="timer_m'+timeId+'">0</span>分'
                +'<span id="timer_s'+timeId+'">0</span>秒'
                +'</div>';
            investController.timerArrayTostart[bidId] = startTime;
            return contents;
        }

        //待启动的数组
        investController.timerArrayTostart=[];

        //启动时间组件
        investController.startTimerArray=function() {
            for(var i=0;i<investController.timerArrayTostart.length;i++){
                if(investController.timerArrayTostart[i]!=null){
                    commonUtil.timer('timer'+i, investController.timerArrayTostart[i]+'');
                }
            }
        }


        //加载项目列表
        investController.goodBidList = function (params) {
            params = (params == null ? {} : params);
            var result = pageUtil.initPaging("/bidInfoController/bidInfoList.do", params, _renderInvestPage, 1,"bidList_paging");
            _renderInvestPage(result,"bidList" );
        };
        //加载散表列表
        investController.commonBidList = function () {
            var result = pageUtil.initPaging("/bidInfoController/commonBidList.do", {}, _renderInvestPage, 1,"commonBidList_paging");
            _renderInvestPage(result, "commonBidList");
        };
        //加载债权转让
        investController.investTransferManualList = function () {
            var result = pageUtil.initPaging("/bidInfoController/investTransferManualList.do", {}, _renderInvestPageForCreditor, 1,"investTransferManualList_paging");
            _renderInvestPageForCreditor( result,"investTransferManualList");
        };
        //加载体验表列表
        investController.experienceBidList = function () {
            var result = pageUtil.initPaging("/bidInfoController/experienceBidList.do", {}, _renderInvestPage, 1,"experienceBidList_paging");
            _renderInvestPage(result, "experienceBidList");

        };

        //加载钱坤宝(购房宝&物业宝)
        investController.qkbBidList = function () {
            var result = pageUtil.initPaging("/bidInfoController/qkbBidList.do", {}, _renderInvestPage, 1,"qkbBidList_paging");
            _renderInvestPage(result, "qkbBidList");
        };

        //转换日期单位
        investController.transferTermUnit=function(termUnit) {
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

        /**
         * 跳转到投资详情页
         * @param id
         */
        investController.toInvestDetail = function(id){
            if(commonUtil.needLogin()){
                //其他参数清空
                containerUtil.add("location",1);
                containerUtil.remove("investId");
                containerUtil.add(CONSTANTS.TO_INVEST_BID_ID, id);
                //不显示保障计划
                containerUtil.remove("securityPlanContract");
                commonUtil.jump("invest/" + $("#dstUrl").val());
            }
        };

        //渲染页面
        function _renderInvestPage(data, stickId ) {
            stickId = (stickId == null ? 'bidList' : stickId);
            $(".kk").remove();
            $("." + stickId).siblings(".procont").remove();
            var content = '';
            if (data == null || data.resMsg==null||data.resMsg.data == null || data.resMsg.data.length == 0)  {
                content = '<span class="kk">'+$(".experienceBidList_class_no").html()+'</span>';
                $("." + stickId).after(content);
                return;
            }

            // 债券转让显示逻辑
            var creditFlag = data.params.creditFlag;
            if(creditFlag != undefined && creditFlag > 0){
                $("#pro2").removeClass("hide");
            }

            // 散标显示逻辑
            var commonFlag = data.params.commonFlag;
            if(commonFlag != undefined && commonFlag > 0 ){
                $("#pro4").removeClass("hide");
            }

            // 购房宝显示逻辑
            var gfbFlag = data.params.gfbFlag;
            if(gfbFlag != undefined && gfbFlag > 0 ){
                $("#pro5").removeClass("hide");
            }

            // 海外投资标识
            var overseaInvest = containerUtil.get(CONSTANTS.OVERSEA_INVEST);
            if(overseaInvest == 1 || data.params.isExchangeBid == 1) {
                $("#pro2").addClass("hide");
                $("#pro3").addClass("hide");
                $("#pro4").addClass("hide");
                $("#pro5").addClass("hide");
            }else{
                $("#pro3").removeClass("hide");
                // 产品类型&还款方式显示
                $("#productAndRepayway").removeClass("hide");
            }

            if (data != null&&data.resMsg!=null&&data.resMsg.data != null) {
                var list = data.resMsg.data;
                if (list.length > 0) {
                    if (data.params.isExchangeBid == 1){
                        $("#dstUrl").val("exchangeDetails.html");
                        //交易所匹配标的特殊处理
                        for (var i in list) {
                            var o = list[i];
                            content+='<div class="procont">'
                            content+='<div class="procont_title">'
                            //标题
                            content += '<div class="fl sm_title">' + o.bidName + '</div>';
                            //还款方式
                            content += '<div class="sm_k fl">' + dictionaryUtil.getName('invest', 'bid_repayment', o.biddRepaymentWay) + '</div>';
                            content+='</div>';
                            content += '<div class="procontent clearfix"><ul><li><div>' + moneyUtil.round(o.interestRate,2) + '<span>%</span></div><p>预期年化收益率</p></li>';
                            //投资期限
                            content += '<li><div>' + o.termValue + '<span>' + investController.transferTermUnit( o.termUnit) + '</span></div><p>投资期限</p></li>';
                            //剩余可投金额
                            content += '<li><div>' + 0.00 + '<span>元</span></div><p>剩余可投金额</p></li>';
                            content += '<li><span class="jxing">还款中</span><a href="javascript:void(0)" onclick="investController.toInvestDetail(\'' + o.id + '\')" class="xsb_tzbt">查看详情</a></li></ul></div></div>';
                        }
                    }else{
                        for (var i in list) {
                            var o = list[i];
                            content+='<div class="procont">'
                            content+='<div class="procont_title">'
                            //标题
                            content += '<div class="fl sm_title">' + o.name + '</div>';
                            //还款方式
                            content += '<div class="sm_k fl">' + dictionaryUtil.getName('invest', 'bid_repayment', o.biddRepaymentWay) + '</div>';
                            //标签内容
                            if (!commonUtil.isEmpty(o.labelText)){
                                if (!commonUtil.isEmpty(o.labelUrl)){
                                    content += '<a name="labelText" onclick="_labelUrlHandler(\'' + o.labelUrl + '\')"' +
                                        ' class="z_pro_info  fr">' + o.labelText + '</a>';
                                }else {
                                    content += '<a name="labelText" href="javascript:void(0)" class="z_pro_info  fr">' + o.labelText + '</a>';
                                }
                            }
                            content+='</div>'
                            //判断是否含有加息年化率
                            if(o.raiseRate+''=='0'){
                                content += '<div class="procontent clearfix"><ul><li><div>' + moneyUtil.round(o.interestRate,2) + '<span>%</span></div><p>预期年化收益率</p></li>';
                            } else {
                                content += '<div class="procontent clearfix"><ul><li><div>' + moneyUtil.round(o.baseRate,2) + '<span>%+</span><em class="f-20">'+moneyUtil.round(o.raiseRate,2)+'</em><span>%</span></div><p>预期年化收益率</p></li>';
                            }
                            //投资期限
                            content += '<li><div>' + o.termValue + '<span>' + investController.transferTermUnit( o.termUnit) + '</span></div><p>投资期限</p></li>';
                            //剩余可投金额
                            content += '<li><div>' + moneyUtil.formatAndRoundMoney(o.residueAmount,2) + '<span>元</span></div><p>剩余可投金额</p></li>';
                            if (o.state == 1) {
                                // 暂时没用到
                            } else if (o.state == 2) {
                                if(o.startTime!=null&& !o.hasStarted){
                                    //倒计时
                                    content += '<li><span class="yme">未开始</span><a class="xsb_tzbt">'+investController.bidStartTime(o.id,o.startTime)+'</a></li></ul></div></div>';
                                }else{
                                    content += '<li><span class="jxing">进行中</span><a href="javascript:void(0)" onclick="investController.toInvestDetail(\'' + o.id + '\')" class="xsb_tzbt">立即投资</a></li></ul></div></div>';
                                }
                            }else {
                                content += '<li><span class="yme">已满额</span><button class="btn_end">已售罄</button></li></ul></div></div>';
                            }
                        }
                    }
                }
            }

            $("." + stickId).empty().after(content);
            investController.startTimerArray();

            function labelUrlHandler(labelUrl) {
                window.open(labelUrl);
            }
        }


        //债权转让
        function _renderInvestPageForCreditor(data , stickId) {
            stickId = (stickId == null ? 'investTransferManualList' : stickId);
            $("." + stickId).siblings(".procont").remove();
            $(".kk").remove();
            var content = '';
            if (data == null || data.resMsg==null||data.resMsg.data == null)  {
                content = '<span class="kk">'+$(".experienceBidList_class_no").html()+'</span>';
                $("." + stickId).after(content);
                return;
            }
            if (data != null&&data.resMsg!=null&&data.resMsg.data != null) {
                var list = data.resMsg.data;
                if (list.length > 0) {
                    for (var i in list) {
                        var o = list[i];
                        //标题
                        content += '<div class="procont"><div class="procont_title"><div class="fl sm_title">' + o.bidName + '</div>';
                        //还款方式
                        content += '<div class="sm_k fl">' + dictionaryUtil.getName('invest', 'bid_repayment', o.biddRepaymentWay) + '</div></div>';
                        //原预期年化收益率
                        content += '<div class="procontent clearfix"><ul><li style="width:16%;"><div style="font-size:26px;">' + moneyUtil.round(o.rate,2) + '<span>%</span></div><p>原预期年化收益率</p>';
                        //认购后年化收益率（购买者）
                        content += '<li style="width:16%;"><div style="font-size:26px;">' + moneyUtil.round(o.buyerRate,2) + '<span>%</span></div><p>认购后年化收益率</p></li>';
                        //投资期限
                        content += '<li style="width:16%;"><div style="font-size:26px;">' + o.endDateNum + '<span>天</span></div><p>投资期限</p></li>';
                        //转让金额
                        content += '<li style="width:16%;"><div style="font-size:26px;">' + moneyUtil.formatAndRoundMoney(o.creditorAmount,2) + '<span>元</span></div><p>转让金额</p></li>';
                        //转让价格
                        content += '<li style="width:18%;"><div style="font-size:26px;">' + moneyUtil.formatAndRoundMoney(o.transferPrice,2) + '<span>元</span></div><p>转让价格</p></li>';
                        //购买
                        content += '<li style="width:16%;"><span class="jxing">转让中</span><a href="javascript:void(0)" onclick="investController.toInvestDetail(\'' + o.transferId + '\')" class="xsb_tzbt">立即购买</a></li></ul></div></div>';
                    }
                }
            }
            //$(".").after(content);
            $("." + stickId).after(content);
        }
        /**
         * 项目列表添加tab触发事件
         * @private
         */
        function _addBidTabEvent(){
            $(".list_in li").on("click", function() {
                var index = $(this).index();
                $(this).addClass("list_active").siblings().removeClass("list_active");
                $(".list_box").eq(index).addClass("show").siblings().removeClass("show");
                var proId = $(this).attr("id");
                var dstUrl = "investDetails.html";
                if(proId == "pro1"){    //初始化页面数据
                    dstUrl = "investDetails.html";
                    investController.goodBidList(null);
                }else if(proId == "pro2"){  //加载债权转让的标
                    dstUrl = "creditorTransferDetails.html";
                    investController.investTransferManualList();
                }else if(proId == "pro3"){  //加载体验金标列表
                    dstUrl = "experienceDetails.html";
                    investController.experienceBidList();
                }else if(proId == "pro4"){  //加载散表列表
                    dstUrl = "individualDetails.html";
                    investController.commonBidList();
                }else if(proId == "pro5"){  //加载乾坤宝列表
                    dstUrl = "gfbDetails.html";
                    investController.qkbBidList();
                }
                $("#dstUrl").val(dstUrl);
            });
            $(".icon-wenhao").on("click", function() {
                $(".ty_question").slideDown();
            });
            $(".icon-guanbi4").on("click", function() {
                $(".ty_question").slideUp();
            });
        }
        //获取url中的参数
        investController.getUrlParam = function (name) {
            var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
            var r = window.location.search.substr(1).match(reg);  //匹配目标参数
            if (r != null) return unescape(r[2]);
            return null; //返回参数值
        }

        /**
         * 初始化项目列表
         */
        investController.loadBidList = function(){
            //初始化产品搜索选项
            var productType = dictionaryUtil.getDicObjInLimitValues('invest', 'product_type', "1,2,3,4");
            searchChooseUtil.stickLIWithNOLIMITELE("product_type", productType, "productType", null, null, investController.goodBidList);
            var type = dictionaryUtil.getDicObjInLimitValues('invest', 'bid_type', "1,2");
            searchChooseUtil.stickLI("product_type", type, "type", null, null, investController.goodBidList);
            //还款方式过滤框
            var repayment = dictionaryUtil.getDicObjInLimitValues('invest', 'bid_repayment', "2,3");
            searchChooseUtil.stickLIWithNOLIMITELE("bid_repayment", repayment, "biddRepaymentWay", null, null, investController.goodBidList);
            _addBidTabEvent();
            //url参数前置检查,应对从首页进来的数据
            var param_biddRepaymentWay=investController.getUrlParam('biddRepaymentWay');
            var param_type=investController.getUrlParam('type');

            var query_type=investController.getUrlParam('queryType');
            
            var invest_type=investController.getUrlParam('investType');
            if(param_biddRepaymentWay!=null){
                $("#bid_repayment li[name='biddRepaymentWay'][value='"+param_biddRepaymentWay+"']").click()
            }else  if(param_type!=null){
                $("#product_type li[name='type'][value='"+param_type+"']").click()
            }else if(query_type!=null&&query_type=='experienceBidList'){
                $("#pro3").click()
            }else if(invest_type!=null && invest_type == 'gfb'){
            	$("#pro5").click()
            }else{
                //没有任何参数那么加载其他的
                investController.goodBidList(null);
            }
        };

    });
})();