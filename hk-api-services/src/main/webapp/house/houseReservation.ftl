<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>房产预约</title>
  <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
   <link rel="stylesheet" href="${request.contextPath}/jslib/house/css/swiper.min.css">
  <link rel="stylesheet" href="${request.contextPath}/jslib/house/css/house_detail.css">
  <script type="text/javascript" src="${request.contextPath}/jslib/jquery-3.2.1.min.js"></script>
  <script src="${request.contextPath}/jslib/house/swiper.min.js"></script>
  <script>
      (function(doc, win) { // font-size.js
          var docEl = doc.documentElement,
              resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize',
              recalc = function() {
                  var clientWidth = docEl.clientWidth;
                  if (!clientWidth) return;
                  docEl.style.fontSize = (clientWidth >= 640 ? 100 : clientWidth / 6.4) + 'px';
              };

          if (!doc.addEventListener) return;
          win.addEventListener(resizeEvt, recalc, false);
          doc.addEventListener('DOMContentLoaded', recalc, false);
      })(document, window);
  </script>
</head>
<body>
<div style="margin-bottom: 1.5rem;">
    <div class="y_box">
    <input id="code" type="hidden"  />
	<input id="msg" type="hidden" />
    <input id="infoId" type="hidden" value="${infoId}" />
	<input id="access_token" type="hidden" value="${access_token}" />
        <input id="sessionId" type="hidden" value="${sessionId}" />
	<input id="source" type="hidden" value="${source}" />
      <span style="float:left;font-size:.3rem;display:block;height:.96rem;line-height:.96rem;margin-right:.2rem;">请输入预约报名看房人数:</span><input id="personNum" type="text" placeholder="" style="float:left;width:20%;border-radius:10%;-webkit-appearance: none;"><span style="float:left;font-size:0.3rem;height:.96rem;line-height:.96rem;margin-left:.2rem;">人</span>
      <div class="clearfix y_check">
        <input type="checkbox" class="fl" checked="checked" id="check_activity">
        <p class="fl">我已经看过并同意<span style="color:#00a0e9;" class="y_span">《看房活动声明》</span></p>
      </div>
      
      <div class="y_btn"><a href="javascript:void(0)">确定预约</a></div>
    </div>

    <!-- 看房活动声明弹出框 -->
    <div class="jd_win" style="display: none;" id="y_state">
        <div class="jd_win_box">
            <div class="jd_win_box_text">
                    <h3>看房活动声明<span class="sp_dele">×</span></h3>
                    <div style="height:6.8rem;overflow: hidden;overflow-y: auto;">
                    	<div>为规范房活动，明确每位购房网友权责，敬请购房网友仔细阅读本声明内容，自愿选择参加本次看房活动。凡参与者均示为认同并自愿遵守本声明内容。</div>
						<div>一、本次免费看房活动均由所在项目的开发商进行组织，以参与者自愿参加、自愿退出为原则。鸿坤金服只提供线上预约看房服务。</div>
						<div>二、活动参与者确认，免费看房活动期间的风险及责任均自行承担。但鉴于，免费看房活动存有不可预见的危险，诸如道路行驶、自身身体健康、自然灾害等等，均有可能造成对自己生命财产的伤害和损失。参加者应当先行积极主动购买保险，降低损失。一旦发生意外事故和人身伤害，均由保险公司和自己负责赔偿，鸿坤金服及任何非事故当事人将不承担事故任何责任。</div>
						<div>三、免费看房活动都应该遵守国家相关法律、法规，一切与活动直接或间接相关的法律责任均由活动参与者自行承担，而与鸿坤金服无涉。</div>
						<div>四、所有免费看房活动的参与者应发扬团结互助、助人为乐的精神，在力所能及的范围内尽量给予他人便利和帮助。但任何便利和帮助的给予并不构成法律上的义务，更不构成对其他参与者损失或责任在法律上分担的根据。</div>
						<div>五、活动参与者事先对本声明条款的含义及相关法律后果已全部通晓并充分理解，凡参加本次活动者均视为接受本声明。</div>
                    </div>
            </div>
            </div>
        </div>

<!-- 预约成功 -->
    <div class="jd_win" style="display: none;"  id="y_success">
        <div class="jd_win_box">
            <div class="jd_win_box_text jd_win_box_text1" id="dataMsg">
                预约成功，客服人员会尽快与您取得联系并确认看房时间，请您保持电话畅通，谢谢！
            </div>
            <div class="jd_btn_box">
                <a href="javascript:;" class="">知道了</a>
            </div>
        </div>
    </div>
    </div>
</body>
<script>

$(function(){
    $(".y_span").click(function(){
     $("#y_state").show();
    })
    $(".sp_dele").click(function(){
      $("#y_state").hide();
    })
    $(".y_btn a").click(function(){
    	var infoId = $("#infoId").val();
    	var personNum = $("#personNum").val();
    	var sessionId = $("#sessionId").val();
        var access_token = $("#access_token").val();
    	var regPos = /^\d+$/;
    	if(!regPos.test(personNum)){
            $("#dataMsg").html("请填写正确的人数");
            $("#y_success").css("display","block");
    		return false;
    	}
    	if (!$("#check_activity").is(':checked')) {
            $("#dataMsg").html("请先阅读《活动声明》");
            $("#y_success").css("display","block");
            return false;
        }
    	$.ajax({
    		type: "POST",
    		url:"reservation.do",
    		data:{'infoId':infoId,"sessionId":sessionId,"access_token":access_token,"personNum":personNum},
    		dataType:"json",
    		cache: false,
    		success: function(data){
    			$("#code").val(data.code);
    			$("#msg").val(data.msg);
    			if(data.code == "1000" ){
    				 $("#dataMsg").html(" 预约成功，客服人员会尽快与您取得联系并确认看房时间，请您保持电话畅通，谢谢！");
    			}else if(data.code == "2003"){
    				if(data.source == "1"){
    					//ios
    					window.webkit.messageHandlers.houseProResult.postMessage(JSON.stringify(data));
    				}else{
    					window.Android.houseProResult(JSON.stringify(data));
    				}
    				return ;
    			}else{
    				$("#dataMsg").html(data.msg);
    			}
    			$("#y_success").css("display","block");
    		},
    		error: function(data) {
    			$("#dataMsg").html(" 预约失败，请稍后再试，您也可以联系客服人员。");
    			$("#y_success").css("display","block");
    		}
    	});
    })
	$(".jd_btn_box a").click(function(){
        $("#y_success").hide();
        var code = $("#code").val();
        if( code == "200"){
            if(data.source == "3"){
                //ios
                window.webkit.messageHandlers.houseProResult.postMessage(JSON.stringify(data));
            }else{
                //android
                window.Android.houseProResult(JSON.stringify(data));
            }
        }
    })
    
})
</script>
</html>