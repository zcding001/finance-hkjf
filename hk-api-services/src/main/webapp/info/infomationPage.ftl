<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <title></title>
    <style type="text/css">
    *{margin:0;padding:0;}
    header h3{font-size: 20px; line-height: .5rem; color: #20262D;padding: .5rem .35rem .2rem .35rem; }
    header > p{text-align: center;color:#91979E;font-size:14px;margin-bottom: .2rem;}
    .main_cont {width: 90%;color:#61676E;margin: 0 auto;text-align: justify;font-size: 16px;line-height: 24px; margin-bottom: .15rem;margin-top:.15rem; word-break: break-all;}
    .div_img{width:90%;margin:0 auto;text-align: center;}
    .div_img img{width:100%;margin-top:.2rem;margin-bottom: .15rem;display: table-cell;vertical-align: middle;}
    .div_img p{text-align: center;font-size: 16px;}
    .main_cont img{width:100%;margin-top:.2rem;margin-bottom: .15rem;display: table-cell;vertical-align: middle;}
    .main_cont > p{font-size: 16px;line-height: 24px;color:#61676E;}
    </style>

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
    <!-- 标题 -->
    <header style="text-align:center;">
        <h3> <#if infoInformationNews?? > ${infoInformationNews.title} <#else> </#if></h3>
        <p><#if infoInformationNews?? > ${infoInformationNews.createTime?date}<#else> </#if></p>
    </header>
    <!-- 内容 -->
<div style="margin-bottom: .5rem;">
    <div class="main_cont"><#if infoInformationNews?? >${infoInformationNews.content}<#else> </#if></div>
    
</div>
</body>

</html>