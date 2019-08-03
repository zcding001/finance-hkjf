/**
 * Created by dzc on 17.12.14.
 */

var investDetailController = {};
var doQuestionnire = true;
(function(){
    'use strict';

    var _kList = "";
    var _jList = "";
    /**
     * 去投资优选
     */
    investDetailController.loadToInvest = function(){
        ajaxUtil.post("/bidInfoController/toInvest.do", {id : containerUtil.take(CONSTANTS.TO_INVEST_BID_ID)}, function(data){
            if(data.resStatus == CONSTANTS.SUCCESS){
                var bidInfo = _setCommData(data.params.bidInfo);
                bidInfo = _setImgBtn(bidInfo);
                //是否支持推荐奖
                if(bidInfo.recommendState == 0) {
                    $(".withActivity").hide();
                }
                renderUtil.renderElement([data.params, bidInfo], "dataForm");
                //设置卡券
                _setCouponData(data);
                //设置投资记录
                _setInvestRecord(data);
            }
        });
        _addTabEvent();
        $("#submitBtn").click(_doInvest);
    };

    /**
     * 去投资散标
     */
    investDetailController.loadToInvestIndividual = function(){
        ajaxUtil.post("/bidInfoController/toInvest.do", {id : containerUtil.take(CONSTANTS.TO_INVEST_BID_ID)}, function(data){
            if(data.resStatus == CONSTANTS.SUCCESS){
                var bidInfo = _setCommData(data.params.bidInfo);
                bidInfo = _setImgBtn(bidInfo);
                renderUtil.renderElement([data.params, bidInfo], "dataForm");
                //设置卡券
                _setCouponData(data);
                //设置投资记录
                _setInvestRecord(data);
                //设置还款计划
                _setRepayRecord(data, "repayPlanDataTable");
                //设置倒计时
                if(bidInfo.state == 2){
                    dateUtil.countDown((bidInfo.endTime - Date.parse(new Date()))/1000);
                }

            }
        });
        _addTabEvent();
        $("#submitBtn").click(_doInvest);
    };

    /**
     * 交易所匹配标的详情
     */
    investDetailController.loadToInvestExchange = function(){
        ajaxUtil.post("/bidInfoController/toInvest.do", {id : containerUtil.take(CONSTANTS.TO_INVEST_BID_ID)}, function(data){
            if(data.resStatus == CONSTANTS.SUCCESS){
                var bidInfo = _setCommData(data.params.bidInfo);
                bidInfo = _setImgBtn(bidInfo);
                renderUtil.renderElement([data.params, bidInfo], "dataForm");
                //设置卡券
                _setCouponData(data);
                //设置投资记录
                _setInvestRecordForExchange(data);
                //设置还款计划
                //_setRepayRecord(data, "repayPlanDataTable");
                //设置倒计时
                // if(bidInfo.state == 2){
                //     dateUtil.countDown((bidInfo.endTime - bidInfo.startTime)/1000);
                // }
            }
        });
        _addTabEvent();
        $("#submitBtn").click(_doInvest);
    };

    /**
     * 去投资体验金
     */
    investDetailController.loadToInvestExperience = function(){
        ajaxUtil.post("/bidInfoController/toInvest.do", {id : containerUtil.take(CONSTANTS.TO_INVEST_BID_ID)}, function(data){
            if(data.resStatus == CONSTANTS.SUCCESS){
                var bidInfo = _setCommData(data.params.bidInfo);
                var stateView = "进行中";
                if(bidInfo.state == 2){
                    stateView = "进行中";
                }else if(bidInfo.state == 3 || bidInfo.state == 4){
                	stateView = "已售罄";
                }else if(bidInfo.state == 5){
                    stateView = "还款中";
                }else if(bidInfo.state == 6){
                    stateView = "已完成";
                }
                bidInfo["stateView"] = stateView;
                renderUtil.renderElement([data.params, bidInfo], "dataForm");
                //设置投资记录
                _setInvestRecord(data);
                //设置倒计时
                dateUtil.countDown((bidInfo.endTime - Date.parse(new Date()))/1000);
                // $("#imgUrl").attr("src", "http://test-yr-platform-qsh.oss-cn-beijing.aliyuncs.com/" + bidInfo.imgUrl);
            }
        });
        _addTabEvent();
        $("#submitBtn").click(_doInvestExperience);
    };

    /**
     * 去投资购房宝&物业宝
     */
    investDetailController.loadToInvestGfb = function(){
        ajaxUtil.post("/bidInfoController/toInvest.do", {id : containerUtil.take(CONSTANTS.TO_INVEST_BID_ID)}, function(data){
            if(data.resStatus == CONSTANTS.SUCCESS){
                var bidInfo = _setCommData(data.params.bidInfo);
                bidInfo = _setImgBtn(bidInfo);
                var stateView = "进行中";
                if(bidInfo.state == 2 && new Date(bidInfo.endTime).getTime() > new Date().getTime()){
                    stateView = "进行中";
                }else if(bidInfo.state == 5){
                    stateView = "还款中";
                }else if(bidInfo.state == 6 || new Date(bidInfo.endTime).getTime() < new Date().getTime()){
                    stateView = "已完成";
                    $("#seeOtherBtn").show();
                    $("#submitBtn").hide();
                    $(".overdueDiv").show();
                    $(".moneyDiv").hide();
                }
                bidInfo["stateView"] = stateView;
                renderUtil.renderElement([data.params, bidInfo], "dataForm");
                //设置投资记录
                _setInvestRecord(data);
                //设置倒计时
                dateUtil.countDown((bidInfo.endTime - Date.parse(new Date()))/1000);
//                $("#imgUrl").attr("src", "http://test-yr-platform-qsh.oss-cn-beijing.aliyuncs.com/" + bidInfo.imgUrl);
            }
        });
        $("#rechargeBtn").click(function(){
            commonUtil.jumpToAccount("recharge.html");
        });
        _addTabEvent();
        $("#submitBtn").click(_doInvest);
    };

    /**
     * 查看合同
     * @param type
     */
    investDetailController.showContract = function(type){
        var param = "?contractType=" + type;
        var location = containerUtil.take("location");
        if(validUtil.validNotEmpty(location)){
            param += "&location=" + location;
        }else{
            param += "&location=1";
        }
        var investId = containerUtil.take("investId");
        if(validUtil.validNotEmpty(investId)){
            param += "&bidInvestId=" + investId;
        }
        var contractBidId = containerUtil.take("contractBidId");
        if(validUtil.validNotEmpty(contractBidId)){
            param += "&bidId=" + contractBidId;
        }
        commonUtil.jump("/contract/contractFrameworkTemplatePC.html" + param, 1);
    };

    /**
     * 设置公共数据
     * @param bidInfo
     * @returns {*}
     * @private
     */
    function _setCommData(bidInfo){
        //设置投资期限
        bidInfo["termUnitView"] = dictionaryUtil.getName("invest", "bid_term_unit", bidInfo.termUnit);
        //设置投资进度条
        _setPercent(bidInfo);
        //还款方式
        bidInfo["biddRepaymentWayView"] = dictionaryUtil.getName("invest", "bid_repayment", bidInfo.biddRepaymentWay);
        //设置招标方案
            var investStandard = "";//dictionaryUtil.getName("invest", "bid_scheme", bidInfo.bidScheme);
            if(bidInfo.bidScheme == 0){
                investStandard += "平均金额招标(建议5000.00元起投)";
            }else{
                if(bidInfo.stepValue > 0){
                    if(bidInfo.bidSchemeValue > 5000){
                        investStandard += "最低金额招标(建议" + bidInfo.bidSchemeValue + "元起投, 且以" + bidInfo.stepValue + "元倍数递增)";
                    }else{
                        investStandard += "最低金额招标(建议5000.00元起投, 且以" + bidInfo.stepValue + "元倍数递增)";
                    }
                }else{
                    if(bidInfo.bidSchemeValue > 5000){
                        investStandard += "最低金额招标(建议" + bidInfo.bidSchemeValue + "元起投)";
                    }else{
                        investStandard += "最低金额招标(建议5000.00元起投)";
                    }
                }


        }
        bidInfo["investStandard"] = investStandard;
        //设置合同
        var contracts = bidInfo.contract;
        if(contracts != undefined && contracts.length > 0){
            var arr = contracts.split(",");
            var content = "";
            for(var i in arr){
                var securityPlanContract = containerUtil.take("securityPlanContract");
                //不显示《保障计划》 2标识合同的字典唯一值
                if(securityPlanContract == undefined && arr[i] == 2){
                    continue;
                }
                content += '《<a onclick="investDetailController.showContract(' + arr[i] + ')" class="colr-blue">' + dictionaryUtil.getContractShowName(arr[i]) + '</a>》';
            }
            $("#contractView").html(content);
        }

        // 设置年化利率加息情况
        var rateContent = "";
        var interestRate = bidInfo.interestRate;
        var baseRate = bidInfo.baseRate;
        var raiseRate = bidInfo.raiseRate;
        if(raiseRate != undefined && raiseRate != "" && raiseRate > 0){
              rateContent = baseRate + '<span>%+</span>' + '<em style="font-size: 20px;font-style: normal;">' + moneyUtil.round(raiseRate,2)+ '</em><span>%</span>';
        }else{
              rateContent = '<em>'+ interestRate+'</em><span>%</span>';
        }
        $("#interestRateDiv").html(rateContent);
        return bidInfo;
    }

    /**
     * 执行投资操作
     * @private
     */
    function _doInvest(){
        //投资风险测评
        // if(!hasEvalueation()){
        //     dialogUtil.alert("","请先进行风险测评！",function () {
        //         $("#questionPage").modal('show');
        //     });
        //     return;
        // }

        $("#resErrMsg").text("");
        var money = $("#money").val();
        if(money == '' || money == undefined || money == 0){
            $("#resErrMsg").text("请输入投资金额!").show();
            return;
        }
        if(parseInt(money) >$("#useableMoney").val()){
            $("#resErrMsg").text("余额不足!").show();
            return;
        }
        if(parseInt(money) > Number($("#residueAmount").val())){
            $("#resErrMsg").text(money + "超过" + $("#residueAmount").val() + "剩余可投金额").show();
            return;
        }
        var bidScheme = $("#bidScheme").val();
        var bidSchemeValue = $("#bidSchemeValue").val();
        var stepValue = $("#stepValue").val();
        if(Number($("#residueAmount").val()) < bidSchemeValue && parseInt(money) != Number($("#residueAmount").val())) {
            $("#resErrMsg").text("剩余金额较少,请一次性投完!").show();
            return;
        }
        if(bidScheme == 1){
            //剩余金额 >= 最低起投金额 & ( 投资金额 < 起投金额 | (投资金额-起投金额)与步长取模 不等于 0)
            if(Number($("#residueAmount").val()) >= bidSchemeValue && (parseInt(money) < bidSchemeValue || (parseInt(money) > bidSchemeValue && (parseInt(money) - bidSchemeValue) % stepValue != 0))){
                $("#resErrMsg").text("最低起投金额为" + bidSchemeValue + "元,且必须为" + stepValue + "元整数倍递增!").show();
                return;
            }
        }else{
            if(parseInt(money) % $("#bidSchemeValue").val() != 0){
                $("#resErrMsg").text("请输入" + $("#bidSchemeValue").val() + "元整数倍!").show();
                return;
            }
        }
        if(!_validCoupon($("#investRaiseInterestId").val(), _jList) || !_validCoupon($("#investRedPacketId").val(), _kList)){
            return;
        }
        var msg = "确认投资?";
        if($("#biddRepaymentWay").val() == 3){
            msg = '尊敬的用户：<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;此标的还款方式为：到期一次还本付息（即到期后一次性返还本金和收益），您是否要继续投资？';
        }
       _doSumbit({bidId : $("#id").val(), money : money, investRedPacketId : $("#investRedPacketId").val(), investRaiseInterestId : $("#investRaiseInterestId").val()}, msg);
    }

    /**
     * 投资体验金操作
     * @private
     */
    function _doInvestExperience(){
        $("#resErrMsg").text("");
        var money = $("#simAmount").val();
        if(money == '' || money == undefined || money == 0){
            $("#resErrMsg").text("没有可用的体验金");
            return;
        }
        if(money < 100 || money > Number($("#residueAmount").val())){
            $("#resErrMsg").text("投资金额在100 - 15000元之间！");
            return;
        }
        _doSumbit({bidId : $("#id").val(), money : money}, "确认投资?");
    }

    /**
     * 提交表单操作
     * @param params
     * @param msg
     * @private
     */
    function _doSumbit(params, msg){
        dialogUtil.confirm("投资提醒", msg, function(){
            ajaxUtil.post("bidInfoController/invest.do", params,
                function(data){
                    if(data.resStatus == CONSTANTS.SUCCESS){
                        containerUtil.add(CONSTANTS.CONTAINER_TRANSIENT_KEY, {status : true, msg : '恭喜您投资' + $("#titleName").text() + "项目"});
                        commonUtil.jump("common/success.html");
                    }else if(data.resStatus == CONSTANTS.NO_IDENTIFY){
                        commonUtil.jumpToAccount("securityCenter.html");
                    }else{
                        $("#resErrMsg").text(data.resMsg).show();
                    }
                });
        });
    }

    /**
     * 验证使用的卡券
     * @param id
     * @param list
     * @returns {boolean}
     * @private
     */
    function _validCoupon(id, list){
        if(list != undefined && list.length > 0){
            var money = $("#money").val();
            if(id != -999){
                for(var i in list){
                    var o = list[i];
                    if(id == o.id){
                        if(money < o.amountMin){
                            $("#resErrMsg").text("投资金额不能少于" + o.amountMin + "元.方能使用此投资红包");
                            return false;
                        }
                        if(money > o.amountMax){
                            $("#resErrMsg").text("投资金额不能超过" + o.amountMax + "元.方能使用此加息券");
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * 设置进度条
     * @param bidInfo
     * @private
     */
    function _setPercent(bidInfo){
        //设置投资进度条
        var percent = 0;
        if((bidInfo.totalAmount - bidInfo.residueAmount) * 100/bidInfo.totalAmount > 99.99 &&
            (bidInfo.totalAmount - bidInfo.residueAmount) * 100/bidInfo.totalAmount < 100){
            percent = 99.99;
        }else{
            percent = (bidInfo.totalAmount - bidInfo.residueAmount) * 100/bidInfo.totalAmount;
        }
        percent = Math.round(percent * 100) / 100;
        percent = moneyUtil.round(percent,2)
        /**标的状态：满标、待放款、已完成*/
        if(bidInfo.state == 3 || bidInfo.state == 4 || bidInfo.state == 6){
            percent = 100;
        }
        bidInfo["percent"] = percent + " %";
        $(".percentView").attr("style", "width:" + percent + "%");
    }

    /**
     * 设置投资记录
     * @param data
     * @private
     */

    function _setInvestRecord(data, dataTableId){
        if(dataTableId == undefined || dataTableId == ''){
            dataTableId = "dataTable";
        }
        //投资记录
        var investList = data.params.bidInvestList;
        var tableContent = '<thead><tr><th>序号</th><th>投资人</th><th>投资金额（元）</th><th>投资时间</th></tr></thead>';
        tableContent += "<tbody>";
        if(investList.length > 0){
            for(var i in investList){
                var o = investList[i];
                tableContent += '<tr><td>' + (Number(i) + Number(1) ) + '</td><td>' + o.realName.substring(0, 1) + '**</td>' +
                    '<td><span class="ft_blue">' + o.investAmount + '</span></td><td>' + dateUtil.dateTime(o.createTime) + '</td></tr></tbody>';
            }
        }else{
            tableContent += '<tr><td colspan="4">暂无记录</td></tr></tbody>';
        }
        $("#" + dataTableId).append(tableContent);
    }


    /**
     * 设置投资记录
     * @param data
     * @private
     */

    function _setInvestRecordForExchange(data, dataTableId){
        if(dataTableId == undefined || dataTableId == ''){
            dataTableId = "dataTable";
        }
        //投资记录
        var investList = data.params.bidInvestList;
        var tableContent = '<thead><tr><th>序号</th><th>投资人</th><th>投资金额（元）</th><th>投资时间</th></tr></thead>';
        tableContent += "<tbody>";
        if(investList.length > 0){
            for(var i in investList){
                var o = investList[i];
                tableContent += '<tr><td>' + (Number(i) + Number(1) ) + '</td><td>' + o.investUserName.substring(0, 1) + '**</td>' +
                    '<td><span class="ft_blue">' + o.investAtm + '</span></td><td>' + dateUtil.date(o.createdTime) + '</td></tr></tbody>';
            }
        }else{
            tableContent += '<tr><td colspan="4">暂无记录</td></tr></tbody>';
        }
        $("#" + dataTableId).append(tableContent);
    }



    /**
     * 封装回款计划
     * @param data
     * @param dataTableId
     * @private
     */
    function _setRepayRecord(data, dataTableId){
        if(dataTableId == undefined || dataTableId == ''){
            dataTableId = "dataTable";
        }
        //投资记录
        var list = data.params.bidRepayPlanList;
        var tableContent = '<thead><tr><th>序号</th><th>还款金额(元)</th><th>本金（元）</th><th>利息(元)</th><th>还款日期</th><th>状态</th></tr></thead>';
        tableContent += "<tbody>";
        if(list != undefined && list.length > 0){
            for(var i in list){
                var o = list[i];
                tableContent += '<tr><td>' + (Number(i) + Number(1) ) + '</td>' +
                    '<td><span class="ft_blue">' + o.amount + '</span></td><td>' + o.capitalAmount + '</td><td>'
                    + o.interestAmount + '</td><td>' + dateUtil.date(o.planTime) + '</td><td>' + dictionaryUtil.getName("loan", "bid_repay_state", o.state); + '</td></tr></tbody>';
            }
        }else{
            tableContent += '<tr><td colspan="4">暂无记录</td></tr></tbody>';
        }
        $("#" + dataTableId).append(tableContent);
    }

    //tab添加事件
    function _addTabEvent(){
        $(".infor_title li").on("click", function() {
            var index = $(this).index();
            $(this).addClass("in_active").siblings().removeClass("in_active");
            $(".infor_cont").eq(index).addClass("show").siblings().removeClass("show");
        });
        //添加充值按钮事件
        $("#rechargeBtn").click(function(){
            commonUtil.jumpToAccount("recharge.html");
        });
    }

    /***
     * 根据标的状态设置图标、投资按钮
     * @private
     */
    function _setImgBtn(bidInfo){
        bidInfo["totalAmountView"] = parseFloat(bidInfo.totalAmount / 10000);
        if(bidInfo.state == 3 || bidInfo.state == 4){
            $("#stateImg").attr("src", "../src/img/invest/jb_04.png");
        }else if(bidInfo.state == 5){
            $("#stateImg").attr("src", "../src/img/invest/jb_02.png");
        }else if(bidInfo.state == 6){
            $("#stateImg").attr("src", "../src/img/invest/jb_01.png");
        }
        if(bidInfo.state != 2){
            $("#submitBtn").hide();
            $("#seeOtherBtn").show();
            $("#showByState2").hide();
        }
        return bidInfo;
    }

    /**
     * 设置卡券详情
     * @param data
     * @private
     */
    function _setCouponData(data){
        // 爆款标、推荐标 不显示加息券投资红包
        var _type = data.params.bidInfo.type;
        if(_type != undefined && _type == 0){
            //卡券
            _kList = data.params.kList;
            _jList = data.params.jList;
            var _allowCoupon = data.params.bidInfo.allowCoupon;
            if(_kList != undefined && _kList.length > 0 && (_allowCoupon == 1 || _allowCoupon == 3)){
                var option = "<option value='-999'>_请选择_</option>";
                for(var i in _kList){
                    option += "<option value='" + _kList[i].id + "'>" + _kList[i].name + "</option>";
                }
                $("select[name='investRedPacketId']").append(option).show();
                $("#kidDiv").show();
            }
            if(_jList != undefined && _jList.length > 0 && (_allowCoupon == 1 || _allowCoupon == 2)){
                var option = "<option value='-999'>_请选择_</option>";
                for(var i in _jList){
                    option += "<option value='" + _jList[i].id + "'>" + _jList[i].name + "</option>";
                }
                $("select[name='investRaiseInterestId']").append(option);
                $("#jidDiv").show();
            }
        }
    }
})();