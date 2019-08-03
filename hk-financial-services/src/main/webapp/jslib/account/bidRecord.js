/**
 * Created by dzc on 18.1.5.
 * 还款计划、回款计划、投资记录业务js
 */
var bidRecordController = {};
(function(){
    'use strict';

    /**
     * 投资记录列定义
     * @type {*[]}
     */
    var investRecordColumns = [
        {
            title: '项目名称', data: 'bidName', tdClass: 'tac', thClass: 'tac',
            render: function (value, row) {
                return "<a href='javascript:void(0)' onclick='bidRecordController.toInvestDetail(\"" + row.bidInfoId + "\", \"" + row.bidProductType + "\", \"2\", \"" + row.bidInvestId + "\", \"0\")'>" + commonUtil.getTextByLength(value, 15) + "</a>";
            }
        },{
            title: '预期年化收益率(%)', data: 'interestRate', tdClass: 'tac', thClass: 'tac',
            render: function (value, row) {
                return value;
            }
        },{
            title: '投资周期', data: 'termValue', tdClass: 'tac', thClass: 'tac',
            render: function (value, row) {
                return value + "(" +dictionaryUtil.getName("invest", "bid_term_unit", row.termUnit) + ")";
            }
        },{
            title: '投资金额', data: 'investAmount', tdClass: 'tac', thClass: 'tac'
        },{
            title: '状态', data: 'bidState', tdClass: 'tac', thClass: 'tac',
            render: function (value, row) {
                if(value == 5){
                    return "成功";
                }else if(value == 8){
                    return "失败";
                }else{
                    return "已申请";
                }
            }
        },{
            title: '項目渠道', data: 'actionScope', tdClass: 'tac', thClass: 'tac',
            render: function (value, row) {
                return dictionaryUtil.getName("invest", "action_scope", value);
            }
        }
    ];

    /**
     * 回款计划记录列定义
     * @type {*[]}
     */
    var receiptPlanColumns = [
        {
            title: '项目名称', data: 'bidName', tdClass: 'tac', thClass: 'tac',
            render: function (value, row) {
                // return "<a href='javascript:void(0)'>" + commonUtil.getTextByLength(value, 15) + "</a>";
                return "<a href='javascript:void(0)' onclick='bidRecordController.toInvestDetail(\"" + row.bidId + "\", \"" + row.productType + "\", \"2\", \"" + row.investId + "\",\"0\")'>" + commonUtil.getTextByLength(value, 15) + "</a>";
            }
        },
        {
            title: '总金额', data: '', tdClass: 'tac', thClass: 'tac',
            render: function (value, row) {
                return parseFloat(row.amount).toFixed(2);
            }
        },{
            title: '本金', data: 'capitalAmount', tdClass: 'tac', thClass: 'tac'
        },{
            title: '利息', data: 'interestAmount', tdClass: 'tac', thClass: 'tac'
        },{
            title: '加息收益', data: 'increaseAmount', tdClass: 'tac', thClass: 'tac'
        },{
            title: '计划回款时间', data: 'planTime', tdClass: 'tac', thClass: 'tac',
            render: function (value, row) {
                return dateUtil.date(value);
            }
        },{
            title: '项目渠道', data: 'actionScope', tdClass: 'tac', thClass: 'tac',
            render: function (value, row) {
                return dictionaryUtil.getName("invest", "action_scope", value);
            }
        }
    ];

    /**
     * 还款计划记录列定义
     * @type {*[]}
     */
    var repayPlanColumns = [
        {
            title: '项目名称', data: 'bidName', tdClass: 'tac', thClass: 'tac'
            // render: function (value, row) {
            // 	return "<a href='javascript:void(0)' onclick='bidRecordController.toInvestDetail(\"" + row.bidId + "\", \"" + row.productType + "\", \"3\", \"0\", \"" + row.bidId + "\")'>" + commonUtil.getTextByLength(value, 15) + "</a>";
            // }
        },
        {
            title: '总金额', data: 'amount', tdClass: 'tac', thClass: 'tac'
        },{
            title: '本金', data: 'capitalAmount', tdClass: 'tac', thClass: 'tac'
        },{
            title: '利息', data: 'interestAmount', tdClass: 'tac', thClass: 'tac'
        },{
            title: '服务费', data: 'serviceCharge', tdClass: 'tac', thClass: 'tac'
        },{
            title: '罚息', data: 'punishAmount', tdClass: 'tac', thClass: 'tac'
        },
        {
            title: '应还款日期', data: 'planTime', tdClass: 'tac', thClass: 'tac',
            render: function (value, row) {
                return dateUtil.date(value);
            }
        },
       {
           title: '实际还款日期', data: 'actualTime', tdClass: 'tac', thClass: 'tac',
           render: function (value, row) {
               if(value > 0){
                   return dateUtil.date(value);
               }
               return "";
           }
       },
        {
            title: '状态', data: 'state', tdClass: 'tac', thClass: 'tac',
            render: function (value, row) {
                return dictionaryUtil.getName("loan", "bid_repay_state", value)
            }
        },
        {
            title: '操作', data: '', tdClass: 'tac rel', thClass: 'tac', thStyle: 'width:200px',
            render: function (value, row) {
            	var btn = "";
            	if(row.punishAmount > 0){
            		btn = "<i class='iconfont icon-yiyuqi-copy'></i>";
            	}else{
            		btn = "";
            	}
                if(row.state != 1){
                    return btn + "-";
                }
                if(row.state == 1 && row.currRepayFlag == 1){
                    btn += "<a href='javascript:void(0)' style='color:#f39200' onclick='bidRecordController.doRepay(\"" + row.id + "\",\"" + row.allowWithholdFlag + "\")'>还款</a>";
                    if(row.advanceRepayFlag == 1){
                        var capitalTmp = row.residueAmount;
                        if(capitalTmp == 0){
                            capitalTmp = row.capitalAmount;
                        }
                        btn += " || <a href='javascript:void(0)' style='color:#f39200' onclick='bidRecordController.doAdvanceRepay(\"" + row.id + "\",\"" + capitalTmp + "\")'>提前还款</a>";
                    }else if(row.advanceRepayFlag == 2){
                        btn += " || <a href='javascript:void(0)' style='color:#cccccc'>提前还款</a>";
                    }
                }
                return btn;
            }
        }
    ];

    
    
    /**
     * 我的借款记录列定义
     */
    var borrowerRecordColumns = [
	       {
	           title: '项目名称', data: 'name', tdClass: 'tac', thClass: 'tac'
	           // render: function (value, row) {
	           //     return "<a href='javascript:void(0)' onclick='bidRecordController.toInvestDetail(\"" + row.id + "\", \"" + row.productType + "\")'>" + commonUtil.getTextByLength(value, 15) + "</a>";
	           // }
	       },{
	           title: '借款金额', data: 'totalAmount', tdClass: 'tac', thClass: 'tac'
	       },{
	           title: '借款期限', data: 'termValue', tdClass: 'tac', thClass: 'tac',
	           render: function (value, row) {
	               return value + "(" +dictionaryUtil.getName("invest", "bid_term_unit", row.termUnit) + ")";
	           }
	       },{
	           title: '借款费率(年化)', data: 'interestRate', tdClass: 'tac', thClass: 'tac',
	           render: function (value, row) {
	               return value+'%';
	           }
	       },{
	           title: '状态', data: 'state', tdClass: 'tac', thClass: 'tac',
	           render: function (value, row) {
	               return dictionaryUtil.getName("invest", "bid_state", value) ;
	           }
	       },{
	           title: '还款方式', data: 'biddRepaymentWay', tdClass: 'tac', thClass: 'tac',
	           render: function (value, row) {
	               return dictionaryUtil.getName("invest", "bid_repayment", value) ;
	           }
	       },{
	           title: '还款截止日期', data: 'endDate', tdClass: 'tac', thClass: 'tac',
	           render: function (value, row) {
	        	   if(row.state!=5&&row.state!=6){
	        		   return '--';
	        	   }
	               return dateUtil.date(value);
	           }
	       },{
                title:'合同',data:null,tdClass:'tac',thClass:'tac',render:function (value,row) {
                //只有还款中的借款标才能看到合同，其他状态没有合同查看操作
                if (row.state == 5){
                    return "<a href='javascript:void(0)' onclick='bidRecordController.showBorrowContracts(\"" + row.id + "\")'>合同</a>";
                }
                return '--';
            }
            }
	   ];
    /**
     * 初始化投资记录
     */
    bidRecordController.initInvestRecord = function(){
        $("#start").val(dateUtil.dateByFormat(dateUtil.addMonth(-1), "yyyy-MM-dd"));
        $("#end").val(dateUtil.currDate("yyyy-MM-dd"));
        var o = containerUtil.getTransient();
        if(o != undefined && o["investType"] != undefined && o["investType"] != ""){
            $("#investType").val(o["investType"]);
        }
        renderUtil.renderList("/bidInfoController/investList.do", investRecordColumns);
    };

    /**
     * 初始化回款计划记录
     */
    bidRecordController.initReceiptPlanRecord = function(){
        $("#start").val(dateUtil.currDate("yyyy-MM-dd"));
        $("#end").val(dateUtil.dateByFormat(dateUtil.addMonth(1), "yyyy-MM-dd"));
        renderUtil.renderList("/loanController/replanList.do", receiptPlanColumns, '', function(data){
            //设置统计信息
            renderUtil.renderElement(data.params);
        });
    };

    bidRecordController.initBorrowerRecord = function(){
    	 //对筛选条件添加事件
        $(".f-16 >li >span >a").each(function(i){
            if($(this).attr("id") != 'dataTableSearchBtn'){
                $(this).click(function(){
                    $(".on").removeClass("on");
                    $(this).parent().addClass("on");
                    $("#state").val($(this).attr("val"));
                    renderUtil.refresh();
                });
            }
        });
        renderUtil.renderList("/bidInfoController/borrowerRecordList.do", borrowerRecordColumns);
    }
    /**
     * 初始化还款计划记录
     */
    bidRecordController.initRepayPlanRecord = function(){
        //对筛选条件添加事件
        $(".f-16 >li >span >a").each(function(i){
            if($(this).attr("id") != 'dataTableSearchBtn'){
                $(this).click(function(){
                    $(".on").removeClass("on");
                    $(this).parent().addClass("on");
                    $("#state").val($(this).attr("val"));
                    renderUtil.refresh();
                });
            }
        });
        renderUtil.renderList("/loanController/replanList.do", repayPlanColumns, '', function(data){
            //设置统计信息
            var finishRepay = parseFloat(data.params.repayState2) + parseFloat(data.params.repayState3) + parseFloat(data.params.repayState4) + parseFloat(data.params.repayState8)
            data.params["finishRepay"] = finishRepay;
            renderUtil.renderElement(data.params);
        });
    };

    /**
     * 普通还款
     * @param id
     * @private
     */
    bidRecordController.doRepay = function(id, allowWithholdFlag){
        if(allowWithholdFlag == 1){
            var msg = "<div style='font-size:12px;padding:0 15px;line-height:20px;text-indent:2em;padding-top:10px;'>尊敬的用户您好！为了节省您的还款时间和精力，降低您的逾期风险，鸿坤金服现已开通银行卡还款代扣业务，开通此模式后<br/>";
            msg += "系统将在每期还款日当天自动向您的银行卡所在银行（绑定鸿坤金服账户的银行卡）发起扣款，无需再登录系统手动还款。您是否同意开通此还款业务？";
            msg += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color='red' size='1px'>提醒：还款日当天银行卡里的余额不得少于当期还款金额。</font></div>";
            dialogUtil.confirm("还款操作", msg, function(){
                ajaxUtil.post("/loanController/repay.do", {repayId:id, capital:0, withHoldflag:1}, function(data){
                    if(data.resStatus == CONSTANTS.SUCCESS){
                        renderUtil.refresh();
                    }else{
                        dialogUtil.alert("还款操作", data.resMsg);
                    }
                });
            }, "拒绝", function(){
                bidRecordController.doRepay(id, 0);
            });
        }else{
            dialogUtil.confirm("还款操作", "<div style='text-align: center;'>确定还款?</div>", function(){
                ajaxUtil.post("/loanController/repay.do", {repayId:id, capital:0, withHoldflag:0}, function(data){
                    if(data.resStatus == CONSTANTS.SUCCESS){
                        renderUtil.refresh();
                    }else{
                        dialogUtil.alert("还款操作", data.resMsg);
                    }
                })
            });
        }
    }

    /**
     * 提前还款
     * @param id
     * @private
     */
    bidRecordController.doAdvanceRepay = function(id, residueAmount){
        var msg = "<div style='text-align: center;'>待还本金：" + residueAmount + "<br/><input type='text' id='capital' name='capital' style='border: 1px solid #ddd;margin-top: 10px;'></div>";
        dialogUtil.confirm("还款操作", msg, function(dialogFormData){
            ajaxUtil.post("/loanController/calcAdvanceReapyAmount.do", {repayId:id, capital:dialogFormData.capital}, function(data){
                if(data.resStatus == CONSTANTS.SUCCESS){
                    console.info(data);
                    msg = "待还本金：" + residueAmount + "<br/>";
                    msg += "本期还本：" + dialogFormData.capital + "<br/>";
                    msg += "本期还息: " + data.params.interest + "<br/>";
                    msg += "本期还服务费: " + data.params.serviceCharge + "<br/>";
                    msg += "总还款金额: " + data.params.repayAmount  + "<br/>";
                    //msg += "总还款金额: " + (parseFloat(dialogFormData.capital) + parseFloat(data.params.interest) + parseFloat(data.params.serviceCharge)) + "<br/>";
                    msg += "确认提前还款?";
                    dialogUtil.confirm("还款操作", msg, function(){
                        ajaxUtil.post("/loanController/repay.do", {repayId:id, capital:dialogFormData.capital, withHoldflag:0}, function(data){
                            if(data.resStatus == CONSTANTS.SUCCESS){
                                renderUtil.refresh();
                            }else{
                                dialogUtil.alert("还款操作", data.resMsg);
                            }
                        });
                    });
                }else{
                    dialogUtil.alert("还款操作", data.resMsg);
                }
            });
        });
    }


    /**
     * 通过项目名查看项目详情
     * @param bidId
     * @param bidProductType
     */
    bidRecordController.toInvestDetail = function(bidId, bidProductType, location, investId, contractBidId){
        //显示保障计划
        containerUtil.add("securityPlanContract", 1);
        containerUtil.add("location", location);
        containerUtil.add("investId", investId);
        containerUtil.add("contractBidId", contractBidId);
        _toInvestDetail(bidId, bidProductType);
    };


    /**
     * 去查看投资详情页
     * @param bidId
     * @param bidProductType
     * @private
     */
    function _toInvestDetail(bidId, bidProductType){
        containerUtil.add(CONSTANTS.TO_INVEST_BID_ID, bidId);
        var dstUrl = '';
        if(bidProductType == 1 || bidProductType == 2 || bidProductType == 3 || bidProductType == 4){    //初始化页面数据
            dstUrl = "investDetails.html";
        }else if(bidProductType == 5){  //加载体验金标列表
            dstUrl = "experienceDetails.html";
        }else if(bidProductType == 0){  //加载散表列表
            dstUrl = "individualDetails.html";
        }else if(bidProductType == 6 || bidProductType == 7){
            dstUrl = "gfbDetails.html";
        }else{//加载债权转让的标
            dstUrl = "creditorTransferDetails.html";
        }
        commonUtil.jump("invest/" + dstUrl);
    }
    /**
     * created by pengwu
     * 根据借款标id获取其对应投资人的合同记录
     * @param id
     */
    bidRecordController.showBorrowContracts = function (id) {
        ajaxUtil.post("/contractController/showBorrowerContract.do",{bidId:id},function (data) {
            if(data.resStatus == CONSTANTS.SUCCESS){
                var contractList = data.resMsg;
                if (contractList.length > 0){
                    //拼接合同信息列表
                    var thStyle = "border-top: 1px solid #e4e4e4;background-color: #f6f6f6;padding: 5px;text-align: center;border-bottom: 1px solid #e4e4e4;";
                    var tdStyle = "border-bottom: 1px solid #e4e4e4;text-align: center;";
                    var borrowContractTable =  "<div class='iv_list mt-20'><table width='100%'><thread><tr><th width='40%' style='"+thStyle+"'>合同编号</th><th width='20%' style='"+thStyle+"'>合同名称</th><th width='20%' style='"+thStyle+"'>投资人</th><th width='20%' style='"+thStyle+"'>操作</th></tr></thread>" +
                        "<tbody>";
                        contractList.forEach(function (value,i) {
                            borrowContractTable += "<tr>";
                            borrowContractTable += "<td style='"+tdStyle+"'>" + value.contractCode + "</td>";
                            borrowContractTable += "<td style='"+tdStyle+"'>" + value.contractName + "</td>";
                            borrowContractTable += "<td style='"+tdStyle+"'>" + value.investName + "</td>";
                            borrowContractTable += "<td style='"+tdStyle+"'>" + "<a href='javascript:void(0)' onclick='bidRecordController.showContract(\"" + value.contractType + "\",\"" + value.bidInvestId + "\")'>查看</a>" + "</td>";
                            borrowContractTable += "</tr>";
                        });
                        //合同记录循环操作
                    borrowContractTable += "</tbody></table></div>";
                    $("#includeContent").html(borrowContractTable);
                }else {
                    dialogUtil.alert("查看合同","没有合同数据！");
                }
            }else{
                dialogUtil.alert("查看合同", data.resMsg);
            }
        });
        // renderUtil.renderList("/contractController/showBorrowerContract.do?bidId=" + id, borrowContractColumn);
    };
    /**
     * created by pengwu
     * @param contractType  合同类型
     * @param bidInvestId   投资记录id
     */
    bidRecordController.showContract = function (contractType,bidInvestId) {
        //将查看合同参数放到containUtil中
        containerUtil.add("location",4);
        containerUtil.add("investId",bidInvestId);
        //新创建一个标签页跳转至查看合同页面
        investDetailController.showContract(contractType);
    };
})();