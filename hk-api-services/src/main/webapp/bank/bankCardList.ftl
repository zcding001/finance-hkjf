<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <title>查看银行</title>
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <style>
      *{margin:0;padding:0;}
      html,body{min-height:100%;font-family:"Microsoft YaHei",sans-serif;box-sizing:border-box;}
      html{-ms-text-size-adjust:100%;-webkit-text-size-adjust:100%;-webkit-tap-highlight-color:rgba(0,0,0,0);}
      li,ol,ul,dl,dt,dd{list-style:none}
      body,button,input,select,textarea{outline:0;}
      button,input[type=button],input[type=password],input[type=submit],input[type=text],textarea{-webkit-appearance:none}
      .clearfix::before,.clearfix::after{content:".";display:block;height:0;line-height:0;visibility:hidden;clear:both;}
      .fl{float:left;}
      .fr{float:right;}
      .bank_tab{width:100%;height:1.04rem;font-size:0;}
      .bank_tab span{display:inline-block;width:50%;text-align:center;font-size:.32rem;height:.3rem;line-height:1.04rem;color:#312e27;}
      .bank_list{width:100%;height:.96rem;color:#6e6b64;}
      .bank_list:nth-child(odd){background:#fafafa;}
      .bank_img{font-size:0;margin-left:.3rem;}
      .bank_list img{width:.56rem;height:.56rem;margin-top:.2rem;vertical-align:middle;}
      .bank_name{font-size:.3rem;height:.3rem;line-height:.3rem;position:relative;top:.23rem;padding-left:.25rem;}
      .bank_limit{font-size:.28rem;margin-right:.32rem;line-height:.96rem;}
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
    <div class="bank_tab">
        <span>储蓄卡银行</span>
        <span>限额</span>
    </div>
    <!-- bank -->
    <div>
        <ul>
            <#if bankReferList?? && (bankReferList?size gt 0)>
                <#list bankReferList as result >
                    <li class="clearfix bank_list">
                        <div class="fl bank_img">
                            <img src="${basePath}imageLib/bank/${result.bankIconAddress}" alt="${result.bankThirdName}">
                            <span class="bank_name">${result.bankThirdName}</span>
                        </div>
                        <div class="fr bank_limit">单笔<span>${result.singleLimit}</span>万元，单日<span>${result.singleDayLimit}</span>万元</div>
                    </li>
                </#list>
            </#if>
        </ul>
    </div>
</body>

</html>