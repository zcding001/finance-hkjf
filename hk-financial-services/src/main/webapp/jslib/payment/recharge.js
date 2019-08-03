var index = 0;
var payChannelMap="";
var datas;
var uType = 1;
$(document).ready(function(){
		ajaxUtil.post("/finPaymentRechargeController/toRecharge.do", null , function(data){
			if(data.resStatus != CONSTANTS.SUCCESS){
				dialogUtil.confirm("系统提示",data.resMsg,jump,null,goToMyAccount);
    			return;
			}
			datas = data;
			var fnAccount = containerUtil.get(CONSTANTS.LOGIN_FIN_ACCOUNT);
			var regUser = containerUtil.get(CONSTANTS.LOGIN_REG_USER);
			//可用余额
			$("#useableMoney").html(moneyUtil.round(fnAccount.useableMoney,2) + "&nbsp;");
			//支付渠道渲染
			$("#payChannel").empty();
			if(validUtil.validNotEmpty(data.params.payChannelList)){
				$("#payChannel").append('<ul class="clearfix"></ul>');
		        for (var i = 0; i <= eval(data.params.payChannelList).length - 1; i++) {
		        	if(i == 0){
			            $("#payChannel ul").append('<li class="fl"><input type="radio" name="pay_type" checked="checked" value="' + dictionaryUtil.getName("payment","pay_channel",eval(data.params.payChannelList)[i].channelNameCode) + '"><img src="${project_base_path}/src/img/account/' + dictionaryUtil.getName("payment","pay_channel",eval(data.params.payChannelList)[i].channelNameCode) + '.png" class="lisolid"></li>');
		        	}else{
			            $("#payChannel ul").append('<li class="fl"><input type="radio" name="pay_type" value="' + dictionaryUtil.getName("payment","pay_channel",eval(data.params.payChannelList)[i].channelNameCode) + '"><img src="${project_base_path}/src/img/account/' + dictionaryUtil.getName("payment","pay_channel",eval(data.params.payChannelList)[i].channelNameCode) + '.png" class="lisolid"></li>');
		        	}
		        }
			}
			payChannelMap =eval(data.params)[$('input:radio[name="pay_type"]:checked').val()];
			//获取当前选中的支付渠道对应的支付方式
			$("#payStyle").empty();
			var payStyleMap = payChannelMap.payStyleMap[$('input:radio[name="pay_type"]:checked').val()];
			for(var j = 0; j<= payStyleMap.length - 1;j++){
				if(j == 0){
		            $("#payStyle").append('<li class="zf_hover fl">' + payStyleMap[j].paywayName + '</li>');
	        	}else{
		            $("#payStyle").append('<li class="fl">' + payStyleMap[j].paywayName + '</li>');
	        	}
			}
			if(regUser.type != '1'){
				index = 2;
				uType = regUser.type;
			}
			//初始化银行卡列表信息
			_initBankCard(index);
		},"json",false);
		
	   //支付方式点击事件
	    $("#payStyle  li").on("mouseover", function() {
	        $this = $(this);
	        var index = $this.index();
	        $this.addClass("zf_hover").siblings("li").removeClass("zf_hover");
	        $(".card_box").eq(index).addClass("show").siblings(".card_box").removeClass("show");
	        $("#tranAmt").val("");
	        $("#wypay .zf-xzyhk").css({'height':'192px'});
	        $("#wypay  img").attr("src", "${project_base_path}/src/img/account/yhzk.png");
    		$("#wypay  img").attr("id","showMore");
    		if(uType !=1 ){
	        	$("#qywypay .zf-xzyhk").css({'height':'192px'});
		        $("#qywypay  img").attr("src", "${project_base_path}/src/img/account/yhzk.png");
	    		$("#qywypay  img").attr("id","showMoreqywy");
	    		initQywy();
	        	return;
	        }
	        _initBankCard(index);
	    })
	    //支付渠道点击事件
	    $("#payChannel ul li").on("click", function(){
	    	$("#payStyle li").eq(0).addClass("zf_hover").siblings("li").removeClass("zf_hover");
		    $(".card_box").eq(0).addClass("show").siblings(".card_box").removeClass("show");
		    $("#input_card_no").val("");
		    $("#banksLimit").hide();
		    $("#banksLimit").empty();
		    if(uType !=1 ){
	        	$("#qywypay .zf-xzyhk").css({'height':'192px'});
		        $("#qywypay  img").attr("src", "${project_base_path}/src/img/account/yhzk.png");
	    		$("#qywypay  img").attr("id","showMoreqywy");
	    		initQywy();
	        	return;
	        }
		    _initBankCard(index);
	    });
	    function initQywy(){
	    	$("#con_bt_1").hide();
			$("#wypay").hide();
			$("#qywypay").show();
	    	payChannelMap =eval(datas.params)[$('input:radio[name="pay_type"]:checked').val()];
	    	var bankMap = datas.params.bankMap;
	    	var basePath = datas.params.basePath;
			$("#qywypay .zf-yhlist").empty();
			var finBankReferMapWy = payChannelMap.finBankReferMap[$('input:radio[name="pay_type"]:checked').val() + "_QYWY"];
			if(finBankReferMapWy.length <= 9){
				$("#showMoreqywy").hide();
			}
			for(var i = 0; i<= finBankReferMapWy.length - 1; i++){
				$("#qywyzfxr").append('<label><input name="qywyxzyinbank" type="radio" value="' + finBankReferMapWy[i].bankCode + '" class="zf-yinhicon"><img src="${project_base_path}/' + finBankReferMapWy[i].bankIconAddress +'" alt="" class="yinhangbank"></label>');
			}
			 //企业网银支付选择银行卡事件
			$('#qywyzfxr label').on("click",function(){
	            $(this).children('.yinhangbank').addClass('yinhangbank-on').parent().siblings().children('.yinhangbank').removeClass('yinhangbank-on')
	            $(this).children('.yinhangbank').next().nextAll().remove();
	            $(this).append('<img src="${project_base_path}/src/img/account/yhxzbg.png" alt="" class="yhxzbg">');
	            var result=eval($('input:radio[name="pay_type"]:checked').val())[$(this).children("input[name='qywyxzyinbank']").val()];
			});
			
	    }
	    //初始化认证和网银支付的银行卡列表
	    function _initBankCard(index2){
	    	index = index2;
	    	payChannelMap =eval(datas.params)[$('input:radio[name="pay_type"]:checked').val()];
	    	var bankMap = datas.params.bankMap;
	    	var basePath = datas.params.basePath;
	    	//认证充值，银行限额
	    	var bankData=eval(bankMap)[$('input:radio[name="pay_type"]:checked').val() +"_bankRefer"];
	    	//认证充值，第三方协议号
	    	var bankBindingData=eval(bankMap)[$('input:radio[name="pay_type"]:checked').val() +"_thirdAccount"];
			//如果index为0，代表是认证支付
			if(index == 0){
				$("#con_bt_1").show();
				$("#wypay").hide();
				//如果用户已经绑过卡
				if(bankMap.tiedCardFlag != "0"){
					$(".weibk").hide();
					$(".inm_bd").show();
					$(".inm_bd").empty();
					var cardNoKey ="";//卡号的KEY
					var showCardNo ="";//加密卡号
					for(var key in bankMap.entityNo){
						$("#cardNo").val(key);
						showCardNo = bankMap.entityNo[key];
					}
					$(".inm_bd").append('<ul class="pay_ul1"><li class="clearfix"><span class="fl">银行卡号:</span><span class="ml-10 fl"><input type="radio" name="no_agree" value="' + bankBindingData + '" checked="" class="mt0" />' + showCardNo + '</span></li></ul>');
					$(".inm_bd ul li").append(' <div class="chl_item fl w_160"><img src="${project_base_path}/' + bankData.bankIconAddress + '" alt="" class="bangka-img"><span class="card_nse card_select  w_160"></span></div>');
					$(".inm_bd ul li").append('<div class="dopost_bankcardlimit fl clearfix"><span class="dopost_bankcard_name fl">' + bankData.bankThirdName+ '支付限额</span><span class="dopost_bankcard_limit_c fl">单笔限额 '+ bankData.singleLimit +'元、单日限额'+ bankData.singleDayLimit +'元、单月限额'+ bankData.singleMonthLimit +'元</span></div>');
				}else{
					//如果未绑卡
					$("#weibk").empty();
					$(".weibk").show();
					$(".inm_bd").hide();
					var finBankReferMapRz = payChannelMap.finBankReferMap[$('input:radio[name="pay_type"]:checked').val() + "_RZ"];
					if(finBankReferMapRz.length <= 9){
						$("#showCard").hide();
					}
					for(var k = 0; k<= finBankReferMapRz.length - 1; k++){
						$("#weibk").append('<label><input name="wyxzyinbank" type="radio" value="' + finBankReferMapRz[k].bankCode + '" class="zf-yinhicon"> <img src="${project_base_path}/' + finBankReferMapRz[k].bankIconAddress + '" alt="" class="yinhangbank"></label>');
					}
			    }
			}else if(index == 2){//企业网银
				initQywy();
			}else { 
				$("#con_bt_1").hide();
				//如果index为1,代表网银支付
				$("#wypay .zf-yhlist").empty();
				var finBankReferMapWy = payChannelMap.finBankReferMap[$('input:radio[name="pay_type"]:checked').val() + "_WY"];
				if(finBankReferMapWy.length <= 9){
					$("#showMore").hide();
				}
				for(var i = 0; i<= finBankReferMapWy.length - 1; i++){
					$("#wypay .zf-yhlist").append('<label><input name="wyxzyinbank" type="radio" value="' + finBankReferMapWy[i].bankCode + '" class="zf-yinhicon"><img src="${project_base_path}/' + finBankReferMapWy[i].bankIconAddress +'" alt="" class="yinhangbank"></label>');
				}
			}
			
			//网银支付选择银行卡事件
			$('#wyzfxr label').on("click",function(){
	            $(this).children('.yinhangbank').addClass('yinhangbank-on').parent().siblings().children('.yinhangbank').removeClass('yinhangbank-on')
	            $(this).children('.yinhangbank').next().nextAll().remove();
	            $(this).append('<img src="${project_base_path}/src/img/account/yhxzbg.png" alt="" class="yhxzbg">');
	            var result=eval($('input:radio[name="pay_type"]:checked').val())[$(this).children("input[name='wyxzyinbank']").val()];
	            var str='';
	            $('.wangy_c').show();
	            $('.wang-rexian').html(result.name + '客服热线：' + result.tel)
                for(var i = 0; i < result.banklimit.length; i++){
                    str += '<tr><td>'+ result.banklimit[i].type + '</td><td>' + result.banklimit[i].Single + '</td><td>' + result.banklimit[i].day +'</td></tr>'
                }
                $('.zf-wangyinxe').html(str)
			});
			//快捷支付未认证状态选择银行卡事件
			$('#weibk label').click(function(){
				_initBankLimit();
	            $(this).children('.yinhangbank').addClass('yinhangbank-on').parent().siblings().children('.yinhangbank').removeClass('yinhangbank-on')
	            $(this).children('.yinhangbank').next().nextAll().remove();
	            $(this).append('<img src="${project_base_path}/src/img/account/yhxzbg.png" alt="" class="yhxzbg">');
			});
			// 银行卡限额超出换行
		    var test = $(".dopost_bankcard_limit_c").html();
		    if (_getBLen(test) >= 30) {
		        $(".dopost_bankcard_limit_c").css("line-height","18px");
		    }
	    }
	    
	   //跳转到实名认证页面
	    function jump(){
      	  commonUtil.jumpAccountMenu("securityCenter.html");
        }
	    //跳转到我的账户
	    function goToMyAccount(){
	    	window.location.href = commonUtil.getRequestUrl("account/actIndex.html")
	    }
	   //快捷支付未绑卡状态，银行卡获取焦点事件
	    $("#input_card_no").focus(function (){
	    	$('.otshixx').html("");
	    });
	   //快捷支付未绑卡状态，输入银行卡号验证及联想银行图标事件
		$("#input_card_no").blur(function (){
			   var card_no = $("#input_card_no").val();
		        var payChannel = $('input:radio[name="pay_type"]:checked').val();
			    //判断用户是否输入银行卡号
			    if(!validUtil.validNotEmpty(card_no)){
			    	 $('.otshixx').html("请输入银行卡号.");
			    	 return;
			    }
		        var reqData = {cardNo : card_no,payChannel : payChannel};
		        ajaxUtil.get("/finPaymentRechargeController/queryBankCardBin.do", reqData , function(data){
		        	if(data.resStatus == CONSTANTS.SUCCESS){
		        		  $('.otshixx').html("");
		        		   var a_bank_name = data.params.bankName;
			       	       var a_bank_code = data.params.bankCode;
			       	       $("#weibk input[name='wyxzyinbank']").each(function(){
		       	       	      $(this).next().nextAll().remove();
			       	    	    if($(this).attr("checked")){
			       	       		  this.checked=false;
			       	       		  $(this).next().removeClass('yinhangbank-on');
			       	       		}
			       	    	   if(a_bank_code == this.value){
			       	    		   this.checked=true;
			       	    		   $(this).next().addClass('yinhangbank-on');
			       	    		   $(this).next().after('<img src="${project_base_path}/src/img/account/yhxzbg.png" alt="" class="yhxzbg">');
			       	    	   }
			       	    	 _initBankLimit();
			       	       });
		        	}else{
				       	 if(data.resMsg.split("|")[0] == '7777'){
				       		  $('.otshixx').html(data.resMsg);
				       		  $("#weibk input[name='wyxzyinbank']").each(function(){
				       			     $(this).attr("checked",false);
				       	       		 $(this).next().removeClass('yinhangbank-on');
				       	       	     $(this).next().nextAll().remove();
				       	       });
				       		   return;
				       	 }else{
				       		 $('.otshixx').html("银行卡输入错误，请重新输入.");
				       		  $("#weibk input[name='wyxzyinbank']").each(function(){
				       			  $(this).attr("checked",false);
				       	       	  $(this).next().removeClass('yinhangbank-on');
				       	          $(this).next().nextAll().remove();
				       	       });
				       		  return;
				       	 }
		        	}
		        },"json",false);
		    });
	   //快捷支付银行卡展开更多事件
	    $('.weibk .zhankaicz').click(function(){//展开更多
	    	if($(this)[0].id == "showCard"){
	    		$(".weibk .zf-xzyhk").css({'height':'auto'});
	    		$(this)[0].src = '${project_base_path}/src/img/account/yhsq.png';
	    		$(this)[0].id = 'hideCard';
	    	}else{
	    		$(".weibk .zf-xzyhk").css({'height':'192px'});
	    		$(this)[0].src = '${project_base_path}/src/img/account/yhzk.png';
	    		$(this)[0].id = 'showCard';
	    	}
        });
	  //企业支付银行卡展开更多事件
	    $('#qywypay .zhankaicz').click(function(){//展开更多
	    	if($(this)[0].id == "showMoreqywy"){
	    		$("#qywypay .zf-xzyhk").css({'height':'auto'});
	    		$(this)[0].src = '${project_base_path}/src/img/account/yhsq.png';
	    		$(this)[0].id = 'hideMore';
	    	}else{
	    		$("#qywypay .zf-xzyhk").css({'height':'192px'});
	    		$(this)[0].src = '${project_base_path}/src/img/account/yhzk.png';
	    		$(this)[0].id = 'showMoreqywy';
	    	}
        });
	    //网银支付银行卡展开更多事件
	    $('#wypay .zhankaicz').click(function(){//展开更多
	    	if($(this)[0].id == "showMore"){
	    		$("#wypay .zf-xzyhk").css({'height':'auto'});
	    		$(this)[0].src = '${project_base_path}/src/img/account/yhsq.png';
	    		$(this)[0].id = 'hideMore';
	    	}else{
	    		$("#wypay .zf-xzyhk").css({'height':'192px'});
	    		$(this)[0].src = '${project_base_path}/src/img/account/yhzk.png';
	    		$(this)[0].id = 'showMore';
	    	}
        });
	    //充值金额失去焦点事件
	    $("#tranAmt").blur(function(){
	    	$("#errorMsg").html("");
	    });
	    //点击去充值
	    $("#toRecharge").on("click",function(){
	    	//充值金额校验
	    	if(!validUtil.validNotEmpty($("#tranAmt").val())){
	    		$("#errorMsg").html("请输入充值金额.");
	    	    return;
	    	}
	    	//充值金额格式正确性校验
	    	if(!moneyUtil.isMoney($("#tranAmt").val())){
	    		$("#errorMsg").html("请输入正确的充值金额！");
	    	    return;
	    	}
	    	//获取当前支付渠道下的银行卡信息
	    	var payChannelMaps = datas.params.bankMap;
	    	//如果用户未绑过卡，并且选择是认证支付，且没有输入卡号
	    	if(payChannelMaps.tiedCardFlag == "0"  && index == 0 && !validUtil.validNotEmpty($("#input_card_no").val())){
	    		$(".otshixx").html("请输入银行卡号");
	    		return;
	    	}
	    	//如果用户未绑过卡，并且选择是认证支付，且卡号长度小于11位
	    	if(payChannelMaps.tiedCardFlag == "0"  && index == 0 && $("#input_card_no").val().length < 11){
	    		$(".otshixx").html("请输入正确银行卡号");
	    		return;
	    	}
	    	//如果用户未绑过卡，并且选择是认证支付，且没有选择银行
	    	if(payChannelMaps.tiedCardFlag == "0"  && index == 0 && !validUtil.validNotEmpty($("#weibk input[name='wyxzyinbank']:checked").val())){
	    		dialogUtil.msg("系统提示","请选择银行","show");
	    		return;
	    	}
	    	//如果用户选择的是网银支付，且没有选择银行
	    	if(index == 1 && !validUtil.validNotEmpty($("#wyzfxr input[name='wyxzyinbank']:checked").val())){
	    		dialogUtil.msg("系统提示","请选择银行","show");
	    		return;
	    	}
	    	//如果用户选择的是企业网银支付，且没有选择银行
	    	if(index == 2 && !validUtil.validNotEmpty($("#qywyzfxr input[name='qywyxzyinbank']:checked").val())){
	    		dialogUtil.msg("系统提示","请选择银行","show");
	    		return;
	    	}
	    	var bankCode = "";//银行CODE
	    	var bankCard = "";//银行卡号
	    	//如果用户已经绑过卡，并且选择的是认证支付
	    	if(payChannelMaps.tiedCardFlag != "0" && index == 0){
	    		bankCode = payChannelMaps.bankCode;
	    		bankCard = $("#cardNo").val();
	    	}
	    	//如果用户未绑过卡，并且选择是的认证支付
	    	if(payChannelMaps.tiedCardFlag == "0" && index == 0){
	    		bankCode = $("#weibk input[name='wyxzyinbank']:checked").val();
	    		bankCard = $("#input_card_no").val();
	    	}
	    	//如果是网银支付
	    	if(index == 1){
	    		bankCode = $("#wyzfxr input[name='wyxzyinbank']:checked").val();
	    	}
	    	//如果是企业网银支付
	    	if(index == 2){
	    		bankCode = $("#qywyzfxr input[name='qywyxzyinbank']:checked").val();
	    	}
	    	//认证充值，第三方协议号
	    	var thirdAccount=eval(payChannelMaps)[$('input:radio[name="pay_type"]:checked').val() +"_thirdAccount"];
	    	//提交到后台，拼接支付数据
	    	var reqData ={
		    			     systemTypeName : "HKJF", 
		    			     platformSourceName : "PC",
		    			     payChannel : $('input:radio[name="pay_type"]:checked').val(),
		    			     payStyle : (index == 0 ? "RZ":"WY"),
		    			     thirdAccount : thirdAccount,
		    			     bankCode : bankCode,
		    			     bankCard : bankCard,
		    			     tiedCardFlag : payChannelMaps.tiedCardFlag,
		    			     transMoney :$("#tranAmt").val()
	    			     };
	    	var jumpWindow = false;
	    	var isProtocolPay = false;
	    	var isProtocolCashier = 0;
	    	var portocolData = {};
	    	//提交后台进行支付参数校验，跳转第三方支付
	    	ajaxUtil.post("/finPaymentRechargeController/accountRecharge.do", reqData , function(data){
	    		if(data.resStatus == CONSTANTS.SUCCESS){
	    		  //如果是协议支付，直接跳转到相应的收银台
	    		  if(data.resMsg.isgoToCashier == '1'){ //协议支付
	    			  isProtocolPay = true;
	    			  portocolData = data.resMsg;
	    			  isProtocolCashier = 1;
	    		  }else if (data.resMsg.isgoToCashier == '2'){ //易宝收银台
	    			  isProtocolPay = true;
	    			  portocolData = data.resMsg;
	    			  isProtocolCashier = 2;
	    		  }else{ //第三方收银台
	    			  var formStr="<form id='myForm' action='"+ data.resMsg.reqUrl +"' method='post' targe>";
	    			   for (var key in data.resMsg.submitForm) {
	    				   formStr += "<input type='hidden' name='" + key + "' value='" + data.resMsg.submitForm[key] + "'/>";
	    			   }
		    		   formStr = formStr +"</form>";
		    			cookieUtil.setCookie("reqStr",formStr);
	    		  }
	    		  	//弹出询问支付结果模态框
	    			//$("#myModal").removeClass("fade");
	                //$("#myModal").show();
	                jumpWindow = true;
	    		}else{
	    			dialogUtil.msg("系统提示",data.resMsg);
	    			return;
	    		}
	    	},"json",false);
	    	//跳转到空白页请求第三方
	    	if(isProtocolPay && jumpWindow ){
	    		containerUtil.add('cashierData', portocolData);
	    		containerUtil.add('basePath', datas.params.basePath);
	    		if(isProtocolCashier == 2){
	    			openWindow(commonUtil.getRequestUrl("account/cashier_yeepay.html"));
	    		} else {
	    			openWindow(commonUtil.getRequestUrl("account/cashier.html"));
	    		}
	    	}
	    	if(jumpWindow && isProtocolPay == false ){
	    		openWindow(commonUtil.getRequestUrl("account/paySuccess.html"));
	    	}
	    });
	    //打开一个新窗口
	    function openWindow(url){ 
	        var link = $("<a></a>").attr("href",url).attr("target","_blank");  
	        $("body").append(link);  
	        link[0].click();  
	        link.remove();  
	    } 
	    //初始化快捷支付的银行限额
	    function _initBankLimit(){
	    	$("#banksLimit").show();
            $("#banksLimit").empty();
            payChannelMap =eval(datas.params)[$('input:radio[name="pay_type"]:checked').val()];
            var rzMap = payChannelMap.finBankReferMap[$('input:radio[name="pay_type"]:checked').val() + "_RZ"]
            for(var i = 0;i< rzMap.length - 1; i++){
            	if(rzMap[i].bankCode == $("#weibk input[name='wyxzyinbank']:checked").val()){
            		  $("#banksLimit").append('<div class="bank_name fl">'+ rzMap[i].bankThirdName +'支付限额</div><div class="bank_limit_c fl">单笔限额'+ rzMap[i].singleLimit +'元、单日限额'+ rzMap[i].singleDayLimit +'元、单月限额'+ rzMap[i].singleMonthLimit +'元</div>');
            	      break;
            	}
            }
	    }
	    //关闭提示
	    $(".close").on("click", function() {
	    	 $("#myModal").hide();
	    });
	    //支付已完成事件
	    $("#finish").on("click", function() {
	    	window.location.href = commonUtil.getRequestUrl("account/actIndex.html")
	    });
	    //支付失败，重新发起支付事件
	    $("#payFail").on("click", function() {
	    	 commonUtil.jumpAccountMenu("recharge.html");
	    });
        function _getBLen(str) {
	        if (str == null) return 0;
	        if (typeof str != "string") {
	            str += "";
	        }
	        return str.replace(/[^\x00-\xff]/g, "01").length;
	    }
});
