
var creditorTransferDetailController = {};
(function(){
    'use strict';
    var investColumns = [
                   {title:'序号', data:'_index', tdClass:'tac', thClass:'tac'},
                   {title:'投资人', data:'oldRealName', tdClass:'tac', thClass:'tac'},
                   {title:'投资金额', data:'oldInvestAmount', tdClass:'tac', thClass:'tac'},
                   {title:'转让金额', data:'creditorAmount', tdClass:'tac', thClass:'tac'},
                   {title:'投资时间', data:'oldInvestTime', tdClass:'tac', thClass:'tac',
                       render:function(value, row){
                           return dateUtil.dateTime(value);
                       }
                   },
                   {title:'转让时间', data:'transferTime', tdClass:'tac', thClass:'tac',
                       render:function(value, row){
                           return dateUtil.dateTime(value);
                       }
                   },
                   {title:'状态', data:'state', tdClass:'tac', thClass:'tac',
                		render:function(value,row){
                			return dictionaryUtil.getName('invest','transfer_manual_state',value);
                		}
                   }
           ]; 
    var repayPlanColumns = [
                         {title:'序号', data:'_index', tdClass:'tac', thClass:'tac'},
                         {title:'还款金额', data:'amount', tdClass:'tac', thClass:'tac'},
                         {title:'还款本金', data:'capitalAmount', tdClass:'tac', thClass:'tac'},
                         {title:'还款利息', data:'interestAmount', tdClass:'tac', thClass:'tac'},
                         {title:'罚息', data:'punishAmount', tdClass:'tac', thClass:'tac'},
                         {title:'应还款日期', data:'planTime', tdClass:'tac', thClass:'tac',
                             render:function(value, row){
                                 return dateUtil.date(value);
                             }
                         },
                         {title:'实际还款日期', data:'actualTime', tdClass:'tac', thClass:'tac',
                             render:function(value, row){
                            	 if(row.state==0 || row.state==1){
                            		 return "--";
                            	 }else{
                            		 return dateUtil.dateTime(value);
                            	 }
                             }
                         },
                         {title:'状态', data:'state', tdClass:'tac', thClass:'tac',
                      		render:function(value,row){
                      			return dictionaryUtil.getName("loan", "bid_repay_state", value);
                      		}
                         }
                 ];
    creditorTransferDetailController.loadToInvest = function(){
        ajaxUtil.post("/transferManualController/toBuyCreditor.do", {transferId : containerUtil.take(CONSTANTS.TO_INVEST_BID_ID)}, function(data){
            if(data.resStatus == CONSTANTS.SUCCESS){
            	var transferDetail = data.params.transferDetail;
            	var account  = data.params.account;
            	// var buyerRepayMoney = data.params.buyerRepayMoney;
            	transferDetail.biddRepaymentWay = dictionaryUtil.getName("invest", "bid_repayment", transferDetail.biddRepaymentWay);
            	if(account.regUserId==transferDetail.investUserId){
            		$("#mineStateBtn").show();
            	}else{
            		if(transferDetail.state==1){
                        if(account.useableMoney<transferDetail.transferAmount){
                            $("#incomeBtn").show();
                            $("#incomeMsg").html("余额不足，请充值");
                            $("#showModel").css("background-color","#ccc");
                        }else{
                            $("#showModel").attr("data-target","#gridSystemModal");
                        }
                        $("#submitBtn").bind("click",function(){
                            doSubmit(containerUtil.take(CONSTANTS.TO_INVEST_BID_ID));
                        });
                        $("#transferStateBtn").show();
                    }else{
                        $("#loseStateBtn").show();
                    }
            	}
            	transferDetail["stateShow"] = dictionaryUtil.getName('invest','transfer_manual_state',transferDetail.state);
            	transferDetail["buyerRepayMoney"] = transferDetail.expectAmount + transferDetail.creditorAmount;
            	renderUtil.renderElement([transferDetail,account]);
            	dateUtil.countDown(transferDetail.expireTime/1000);
            	renderUtil.renderList("/transferManualController/transferRecordList.do?firstInvestId="+transferDetail.firstInvestId, investColumns,'transferRecordTable');
            	renderUtil.renderList("/loanController/findReplanListByBidId.do?bidInfoId="+transferDetail.bidId, repayPlanColumns,'repayPlanTable');
            }
        });
    };
    
    function doSubmit(tid){
        var regUser = containerUtil.get(CONSTANTS.LOGIN_REG_USER);
        if(regUser.identify==0){//未实名认证
            dialogUtil.confirm("系统提示","请进行实名认证之后再投资!",jump,null,goToMyAccount);
            return;
        }
    	ajaxUtil.post("/transferManualController/buyCreditor.do", {transferId : tid}, function(data){
    		 if(data.resStatus == CONSTANTS.SUCCESS){
                 containerUtil.add(CONSTANTS.CONTAINER_TRANSIENT_KEY, {status : true, msg : "购买债权成功"});
    			 commonUtil.jump("common/success.html");
    		 }else{
    			 dialogUtil.alert("购买债权",data.resMsg);
    		 }
    	});
    }

    function jump(){
        commonUtil.jumpToAccount('securityCenter.html')
    }
    //跳转到我的账户
    function goToMyAccount(){
        window.location.href = commonUtil.getRequestUrl("account/actIndex.html")
    }
})();