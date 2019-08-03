
var productSearchController = {};
(function () {
    $(function () {
        //加载项目列表
        productSearchController.commenProductList = function (params) {
            params = (params == null ? {} : params);
            ajaxUtil.post(commonUtil.getRequestUrl("/indexController/productSearch.do"),params,function(data){
                _renderProductList(data,null,params);
            },"json",true);
        };

        //转换日期单位
        productSearchController.transferTermUnit=function(termUnit) {
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
         * 去查看投资详情页
         * @param bidId
         * @param bidProductType
         * @private
         */
        productSearchController.toInvestDetail = function(bidId, bidProductType){
            containerUtil.add(CONSTANTS.TO_INVEST_BID_ID, bidId);
            //不显示保障计划
            containerUtil.remove("securityPlanContract");
            var dstUrl = '';
            if(bidProductType == 1 || bidProductType == 2 || bidProductType == 3 || bidProductType == 4){    //初始化页面数据
                dstUrl = "investDetails.html";
            }else if(bidProductType == 5){  //加载体验金
                dstUrl = "experienceDetails.html";
            }else if(bidProductType == 0){  //加载散表
                dstUrl = "individualDetails.html";
            }else if(bidProductType == 6 || bidProductType == 7){
                dstUrl = "gfbDetails.html";
            }else{//加载债权转让的标
                dstUrl = "creditorTransferDetails.html";
            }
            commonUtil.jump("invest/" + dstUrl);
        }


        /**
         * 跳转到股权首页
         * @param parentType
         */
        productSearchController.toFundIndex = function(parentType,id){
            var dstUrl = '';
            if(parentType == 1 ){    //私募基金
                dstUrl = "advisory/pefundInfo.html?utype=1";
            }else if(parentType == 2 ){  //海外基金
                dstUrl = "advisory/pefundInfo.html?utype=2";
            }else {
                dstUrl = "advisory/pefundInfo.html?utype=3"; //信托产品
            }
            commonUtil.jump("fund/" + dstUrl +"&id=" + id,1);
        }


        //渲染页面
        function _renderProductList(data, stickId,params) {
            stickId = (stickId == null ? 'productDiv' : stickId);
            $("#keyWordDesc").html(params.keyWord);

            // 定期理财显示逻辑
            var fixFlag = data.params.fixFlag;
            $("#type_10").addClass("hide");
            if(fixFlag != undefined && fixFlag > 0 ){
                $("#type_10").removeClass("hide");
            }
            var authenticationFlag = data.params.authenticationFlag;
            var _type = params.type;
            var content = '';
            if (data != null&&data.resMsg!=null) {
                var list = data.resMsg;
                $('#productSize').html(list.length == undefined ? 0 : list.length);
                if (list.length > 0) {
                    // 私募、海外基金
                    if(_type == 1 || _type == 2 || (fixFlag == 0 && _type == null) ) {
                        content += ' <li class="s_ulli s_ssjgtou s_ssjg2">'
                        content += '<span>名称</span><span>存续期限</span><span>起投金额</span><span>投资方向</span><span>管理机构</span><span class="ss_toucz" >操作</span>'
                        content += ' </li>'
                        for (var i in list) {
                            var o = list[i];
                            content += '<li class="s_ulli s_ssjg2">'
                            content += '<span>' + o.name + '</span>'
                            if (authenticationFlag == 2) {
                                content += '<span>' + o.termDescribe + '</span>'
                            } else {
                                content += '<span><a href="javascript:void(0)" onclick="fundCommon.authenJump(\'' + authenticationFlag + '\', \'' + o.parentType + '\')" class="toRZ1" style="width: 10%;color: #f39200;text-decoration: underline;cursor: pointer;">认证可见</a></span>';
                            }
                            content += '<span>' + o.lowestAmount + '万</span>'
                            content += '<span>' + o.investRange + '</span>'
                            content += '<span>' + o.management + '</span>'
                            // 预约中
                            if (o.subscribeState != 0) {
                                content += '<span><a href="javascript:void(0)" onclick="productSearchController.toFundIndex(\'' + o.parentType + '\', \'' + o.id + '\')" style="color: #f39200;text-decoration: underline;cursor: pointer;">查看</a></span>'
                            } else {
                                content += '<span><a style="color: gray;text-decoration: underline;cursor: pointer;">查看</a></span>'
                            }
                            content += '</li>'
                        }
                    // 信托产品
                    }else if(_type == 3){
                        content += ' <li class="s_ulli s_ssjgtou s_ssjg4">'
                        content += '<span>名称</span><span>预期年化收益率</span><span>投资期限</span><span>起投金额</span><span>发行机构</span><span class="ss_toucz">操作</span>'
                        content += ' </li>'
                        for (var i in list) {
                            var o = list[i];
                            content += '<li class="s_ulli s_ssjg4">'
                            content += '<span>' + commonUtil.getTextByLength(o.name,12) + '</span>'
                            if(o.maxRate == 0){
                                content += '<span>' + moneyUtil.formatAndRoundMoney(o.minRate,2)+ '%</span>'
                            }else {
                                content += '<span>' + moneyUtil.formatAndRoundMoney(o.minRate,2)+ "% ~" + moneyUtil.formatAndRoundMoney(o.maxRate,2)+ '%</span>'
                            }
                            if (authenticationFlag == 2) {
                                content += '<span>' + o.termDescribe + '</span>'
                            } else {
                                content += '<span><a href="javascript:void(0)" onclick="fundCommon.authenJump(\'' + authenticationFlag + '\', \'' + o.parentType + '\')" class="toRZ1" style="width: 10%;color: #f39200;text-decoration: underline;cursor: pointer;">认证可见</a></span>';
                            }
                            content += '<span>' + o.lowestAmount + '万</span>'
                            content += '<span>' + o.management + '</span>'
                            // 预约中
                            if (o.subscribeState != 0) {
                                content += '<span><a href="javascript:void(0)" onclick="productSearchController.toFundIndex(\'' + o.parentType + '\',\'' + o.id + '\')" style="color: #f39200;text-decoration: underline;cursor: pointer;">查看</a></span>'
                            } else {
                                content += '<span><a style="color: gray;text-decoration: underline;cursor: pointer;">查看</a></span>'
                            }
                            content += '</li>'
                        }

                    // 定期理财
                    }else{
                        content += ' <li class="s_ulli s_ssjgtou s_ssjg1">'
                        content += '<span>名称</span><span>预期年化收益率</span><span>投资期限</span><span>剩余可投金额</span><span>状态</span><span>还款方式</span><span class="ss_toucz">操作</span>'
                        content += ' </li>'
                        for(var i in list){
                            var o = list[i];
                            content += '<li class="s_ulli s_ssjg1">'
                            content += '<span>' + o.name +'</span>'
                            content += '<span>' + moneyUtil.round(o.interestRate,2) +'%</span>'
                            content += '<span>' + o.termValue + productSearchController.transferTermUnit( o.termUnit) +'</span>'
                            content += '<span>' + moneyUtil.formatAndRoundMoney(o.residueAmount,2) +'万</span>'
                            if(o.state == 1){
                                content += '<span>即将开始</span>'
                            }else if(o.state == 2){
                                content += '<span>进行中</span>'
                            }else{
                                content += '<span>已满额</span>'
                            }

                            content += '<span>' + dictionaryUtil.getName('invest', 'bid_repayment', o.biddRepaymentWay) +'</span>'
                            if(o.state == 2){
                                content += '<span><a href="javascript:void(0)" onclick="productSearchController.toInvestDetail(\'' + o.id + '\',\'' + o.productType + '\')" style="color: #f39200;text-decoration: underline;cursor: pointer;">查看</a></span>'
                            }else{
                                content += '<span><a style="color: gray;text-decoration: underline;cursor: pointer;">查看</a></span>'
                            }
                            content += '</li>'
                        }
                    }
                    $("#" + stickId).show();
                    $("#" + stickId).empty().append(content);
                    $(".s_nochanp").hide();
                }else{
                    $("#" + stickId).hide();
                    $(".s_nochanp").show();
                }
            }

        }

    });
})();