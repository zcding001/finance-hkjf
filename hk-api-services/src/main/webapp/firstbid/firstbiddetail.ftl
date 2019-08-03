<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <title>功能指引</title>
      <script type="text/javascript" src="${request.contextPath}/jslib/jquery-3.2.1.min.js"></script>
     <link rel="stylesheet" type="text/css" href="${request.contextPath}/csslib/appdetail.css">
     <script type="text/javascript" src="${request.contextPath}/jslib/font-size.js"></script>
  </head>
  <body style="background:#d81c0f;">
    <input type="hidden" value="${source}" id="source"/>
    <input type="hidden" value="${bidId}" id="bidId"/>
    <input type="hidden" value="${bidName}" id="bidName"/>
    <div class="xs_main">
       <img src="${request.contextPath}/imageLib/images/ic_a.png">
       <div class="xs_a">
         <div class="xs_rg"><a href="javascript:void(0)" class="rg_btn" id="jumpUrl">马上享福利</a></div>
         <div class="xs_wz">新注册的用户，首笔可有机会体验1个月收益10%的体验标（限投56800元）仅有一次机会!</div>
       </div>
         <div class="xs_sb">
            <ul>  
               <li>登录(注册)<br/>实名邦卡</li>
               <li>充值，选择心仪<br/>产品投资</li>
               <li>期满结束<br/>拿本金和收益</li>
            </ul>     
         </div>
        <div class="xs_fk">
            <ul>
               <li>安全稳健的项目投资，包括定期、活期私募美元基金等多样产品；</li>
               <li style="margin-top:1.5rem;">
                新社区金融模式下的衍生工具，积分抵扣物业费、停车费等便民服务，积分换购商品
               </li>
               <li style="margin-top:1.6rem;">
               新型的自动投资工具和邀请好友得佣金，躺着也赚钱的福利；</li>
            </ul>
        </div>
        <div class="xs_hkjj">
          <div class="xs_hkc">
           由 <font>鸿坤集团</font> 与 <font>鸿坤亿润投资</font> 联合打造的社区金融综合服务平台，与多家金融机构合作，推出多种创新型金融产品和服务，满足客户多层次的投资理财需求。<br/><br/><br/>
           平台提供 <font> 6 </font>大安全保障措施，坚持严格的风险控制，为用户提供灵活的选择，使用户享受方便快捷的个性化金融服务。
           </div>
        </div>       
    </div>
  </body>
</html>
<script> 
      var source = $("#source").val();
       var bidId = $("#bidId").val();
        var bidName = $("#bidName").val();
     var data = {'bidId':bidId,'bidName':bidName}
    $("#jumpUrl").click(function(){
            if(source == "3"){
                //ios
               window.webkit.messageHandlers.firstBidJump.postMessage(JSON.stringify(data));
            }else{
                //android
                window.Android.firstBidJump(JSON.stringify(data));
            }
    });

</script>
