var protocolData = {};
var transDate = '';
var transAmt = '';
var bankIconAddress = '';
var entityNo = '';
var idCard = '';
var isBind = '';
var bankTel = '';
var bankId = '';
var operateType = '';
var bankCard = '';
$(document).ready(function(){
	//获取从收银台来的信息
	protocolData = containerUtil.take("cashierData");
	transDate = protocolData.transDate;
	transAmt = protocolData.transAmt;
	bankIconAddress = containerUtil.take("basePath")+protocolData.bankIconAddress;
	entityNo = protocolData.entityNo;
	idCard = protocolData.idCard;
	isBind = protocolData.isBind;
	bankTel = protocolData.bankTel;
	bankId = protocolData.bankId;
	bankCard = protocolData.bankCard;
	$("#transDate").html(transDate);
	$(".yunsodf").html(transAmt+"元");
	$("#entityNo").html(entityNo);
	$("#idCard").val(idCard);
	$("#bankTel").val(bankTel);
	$("#bankimg").attr("src",bankIconAddress);
});

$(document).ready(function() {
    var huoquyzm=document.getElementsByClassName('huoquyzm')[0];
    var flg=true;
    var countdown=60;
    var kaitontj=document.getElementsByClassName('kaitontj')[0];
    var wenhao=document.getElementsByClassName('wenhao')[0];
    var opxieyi=document.getElementsByClassName('opxieyi')[0];
    var guanbihui=document.getElementsByClassName('guanbihui')[0];
    var onbutton=document.getElementsByClassName('onbutton')[0];
    var sintu3=document.getElementsByClassName('s-intu-3');
    var yibaopalydh=document.getElementsByClassName('yibaopalydh')[0];
    var reg=/^1[34578]\d{9}$/;
    huoquyzm.flg=true;
    
    sintu3[0].onblur=function(){  //手机号离开校验
        if(!reg.test(this.value) && this.value){
            showbubbleBtn('请输入正确手机号码',function () {
                sintu3[0].value='';
                sintu3[0].focus();
            })
        }
    };
    
    onbutton.onclick=function () {
        if(!sintu3[0].value || !reg.test(sintu3[0].value)){//验证手机号
            if(sintu3[0].value){
                showbubbleBtn('请输入正确手机号码')
            }else{
                showbubbleBtn('请输入手机号码')
            }
        }else if(!sintu3[1].value){//验证验证码
            showbubbleBtn('请输入验证码')
        }else{
            yibaopalydh.style.display='block'//loading动画显示
        	var flowId=$("#paymentFlowId").val();
        	var verificationCode=$("#verifyContent").val();
        	var payUnionCode = $("#unique_code").val();
        	
        	var reqData ={
        		 flowId : flowId,
        		 verificationCode : verificationCode,
        		 bankCardId : bankId,
   			     bankCard : bankCard,
   			     transMoney : transAmt, 
   			     operateType : operateType,
   			     payUnionCode : payUnionCode,
   			     payChannel : 4,
   			     platformSourceName :10
			};
        	
        	ajaxUtil.post("/finPaymentRechargeController/agreementRecharge.do", reqData , function(data){
        		yibaopalydh.style.display='none'//loading动画消失
	    		if(data.resStatus == CONSTANTS.SUCCESS && data.params.resStatus == CONSTANTS.SUCCESS){
	    			//跳转成功页面
	    			containerUtil.add("payResult","000000");
	    		}else{
	    			if(data.params != null){
	    				showbubbleBtn(data.params.resMsg);
	    				return;
	    			}
	    			showbubbleBtn(data.resMsg);
	    		}
	    	},"json",false);
        	
        }
    };
    
    opxieyi.onclick=function () { //协议打开
        $('.zhezhao,.tanchuango').show()
    };
    guanbihui.onclick=function () {//协议关闭
        $('.zhezhao,.tanchuango').hide()
    };

    wenhao.onmouseover=function () { //手机号后的问好
        kaitontj.style.display='block'
    };
    wenhao.onmouseout=function () {
        kaitontj.style.display='none'
    };
    huoquyzm.onclick=function () {  //发送短信
        if(this.flg){
        	if(!sintu3[0].value || !reg.test(sintu3[0].value)){//验证手机号
            if(sintu3[0].value){
                showbubbleBtn('请输入正确手机号码')
            }else{
                showbubbleBtn('请输入手机号码')
            }
        }else{
        	settime(this);
            this.flg=false;
            var bankTel=$("#bankTel").val();
          //提交到后台，拼接短验数据
	    	var reqData ={
		    			     tel : bankTel,
		    			     transMoney : transAmt,
		    			     bankCardId : bankId,
		    			     bankCard : bankCard,
		    			     payChannel : 4,
		    			     platformSourceName :10
	    			     };
	    	ajaxUtil.post("/finPaymentRechargeController/paymentVerificationCode.do", reqData , function(data){
	    		if(data.resStatus == CONSTANTS.SUCCESS && data.params.resStatus == CONSTANTS.SUCCESS){
	    			if(isBind == '2'){
						$("#paymentFlowId").val(data.params.paymentFlowId);
					}
					$("#unique_code").val(data.params.unique_code);
					operateType = data.params.operateType;
					containerUtil.add("payResult","000000");
					window.location.href = commonUtil.getRequestUrl("account/cashier_result.html")
	    		}else{
	    			if(data.params != null){
	    				showbubbleBtn(data.params.resMsg);
	    				return;
	    			}
	    			showbubbleBtn(data.resMsg);
	    		}
	    	},"json",false);
	    	
        }
       }
    };

    function settime(val) {  //倒计时
        daojishill(--countdown)
        function daojishill(countdown) {
            val.innerHTML=countdown+'s'
        }
        var setflg=setInterval(function() {
            if(--countdown<=0){
                val.innerHTML='重新发送'
                countdown=60
                val.flg=true
                clearInterval(setflg)
            }else{
                daojishill(countdown)
            }
        },1000);
    }

    function showbubbleBtn(txt,fn){  //气泡弹框
        $('#bubbleBtn').show().html(txt)
        typeof fn=='function'?fn():void 0
        if(flg){
            flg=false
            var sto=window.setTimeout(function(){
                $('#bubbleBtn').hide()
                flg=true
            },2000)
        }
    }
})

