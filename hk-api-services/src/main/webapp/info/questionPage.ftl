<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes" />
<meta name="apple-mobile-web-app-status-bar-style" content="black" />
<link rel="stylesheet" type="text/css" href="${request.contextPath}/csslib/info/ques.css"/>
<script type="text/javascript" src="${request.contextPath}/jslib/jquery-3.2.1.min.js"></script>
<script type="text/javascript" src="${request.contextPath}/jslib/info/question.js"></script>
<title>问卷调查</title>
<script> 
</script>
</head>
<body class="all" onload="getdata(${infomationId},'${regUserId}',${channel},'${url}')">
<input type="hidden" id="isRepeatAns"/>
<input type="hidden" id="infomationId" value='${infomationId}'/>
<input type="hidden" id="regUserId" value='${regUserId}'/>
<input type="hidden" id="channel" value='${channel}'/>
<div class="question_whole" style="position:relative;">
    <div class="floor" id="litte_window" style="display: none;">
      <div class="floora"></div>
      <div class="floor_window" >
         <div class="tj_success" id='alertMsg' style="height:50px;line-height:50px;"></div>
         <div class="tj_sure" onclick='toiOS()'>确定</div>
      </div>
    </div> 
   <div class="question_in">
   </div>
   <#if type==1>
   		<div style="padding:2em 0;"><input name="" type="button" value="不允许提交" class="Btn_tj" onclick='javascript:alert("不允许提交");'></div>
   <#else>
        <!-- <div style="padding:2em 0;" class="sure_btn"><input name="" type="button" value="提交" class="Btn_tj sure_btna" onclick='submitA()'></div> -->  
       <div style="padding:2em 0;" class="sure_btn"><input name="" type="button" value="提交"  style="background:#f39200;" class="Btn_tj sure_btna" onclick='submitA("${url}")'></div>
   </#if>
   
</div>
</body>
</html>
