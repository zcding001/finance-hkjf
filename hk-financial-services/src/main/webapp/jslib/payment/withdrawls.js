var withdrawlsController ={};
(function(){
    'use strict';
    var regUser = containerUtil.get(CONSTANTS.LOGIN_REG_USER);
    var fnAccount = containerUtil.get(CONSTANTS.LOGIN_FIN_ACCOUNT);
    var withdrawlsCouponCount = 0;
    var couponDetailId = "";
    withdrawlsController.initData = function () {
        if(regUser.identify==0){//未实名认证
			dialogUtil.confirm("系统提示","请进行实名认证之后再提现!",jump,null,goToMyAccount);
            return;
        }
        //获取用户绑定的银行卡
        ajaxUtil.post("/withdrawCashController/toWithDrawPage.do", null , _dataCallBack);
    };
    //跳转到实名认证页面
    function jump(){
    	  commonUtil.jumpAccountMenu("securityCenter.html");
    }
    //跳转到我的账户
    function goToMyAccount(){
    	window.location.href = commonUtil.getRequestUrl("account/actIndex.html")
    }
    function _dataCallBack(data){
        if (data.resStatus == CONSTANTS.SUCCESS) {
            if(data.params.bindCardCount== 0){
            	dialogUtil.msg("系统提示","请先绑定银行卡",null);
                commonUtil.jumpAccountMenu("directCard.html");
            }else{
                $("#wshow").show();
                renderUtil.renderElement(fnAccount);
                if(data.params.withdrawlsCoupons> 0){
                    withdrawlsCouponCount= data.params.withdrawlsCoupons;
                    couponDetailId = data.params.couponDetailId;
                    $("#usedCoupon").text("已使用1张提现优惠券");
                }
                else{
                    $("#usedCoupon").text("暂无可用优惠券");
                }
            }
        }else{
            //$("#loginErrId").text(data.resMsg);
            dialogUtil.alert("信息提示：",data.resMsg);
        }
    };
    $("#tranAmt").blur(function(){
        var tranAmt = commonUtil.getValueByName("tranAmt");
        if(withdrawlsCouponCount!=0){
            $("#kce").text((parseFloat(tranAmt)).toFixed(2)+"元    ");
        }else{
            $("#kce").text((parseFloat(tranAmt)+parseFloat(1)).toFixed(2)+"元    ");
        }
        var regex = /^(^(0\.0[1-9]$)|(^0\.[1-9][0-9]{0,1}$)$)|(^[1-9]{1}[0-9]{0,8}\.{0,1}[0-9]{0,2}$)$/;
        if(tranAmt == "" || !(moneyUtil.isMoney(tranAmt))) {
            $("#kce").text("");
            $("#err_tranAmt").text("请输入有效提现金额(保留两位小数)！");
            return;
        }else{
            $("#err_tranAmt").text("");
        }

        var index = tranAmt.indexOf(".");
        if(index == -1) {
            $("#tranAmt").val(tranAmt + ".00");
        } else if(tranAmt.length - 1 - index == 1) {
            $("#tranAmt").val(tranAmt + "0");
        }

    });
    //提现操作事件
    $("#btn_sub").click(function(){
        var tranAmt = commonUtil.getValueByName("tranAmt");
        if(checkOutMoney()){
            var param = {transMoney : tranAmt, couponDetailId : couponDetailId};
            ajaxUtil.post("/withdrawCashController/clientWithDraw.do", param, _withdrawCallBack);
        }

    });
    function _withdrawCallBack(data){
        if (data.resStatus == CONSTANTS.SUCCESS) {
            dialogUtil.alert("信息提示：","提现申请成功");
            commonUtil.jumpAccountMenu("paymentRecord.html");
        }else{
            dialogUtil.alert("信息提示：",data.resMsg);
        }
    };
//提现校验
    function checkOutMoney(){
        var nowmoney = fnAccount.useableMoney;
        var outMoney = commonUtil.getValueByName("tranAmt");
        var regex = /^(^(0\.0[1-9]$)|(^0\.[1-9][0-9]{0,1}$)$)|(^[1-9]{1}[0-9]{0,8}\.{0,1}[0-9]{0,2}$)$/;
        if(outMoney == '' || outMoney == null){
            $("#err_tranAmt").text("请输入提现金额！");
            return false;
        }else if(!moneyUtil.isMoney(outMoney)){
            $("#err_tranAmt").text("请输入有效提现金额(保留两位小数)！");
            return false;
        }else if((withdrawlsCouponCount==0) && (parseFloat((parseFloat(outMoney)+parseFloat(1)).toFixed(2)) > nowmoney)){
            $("#err_tranAmt").text("对不起，你的可用余额不足！");
            return false;
        }else if((withdrawlsCouponCount!=0)&& (parseFloat((parseFloat(outMoney)).toFixed(2)) > nowmoney)){
            $("#err_tranAmt").text("对不起，你的可用余额不足！");
            return false;
        }else{
            $("#err_tranAmt").text("");
            return true;
        }
        return false;
    }
})();


