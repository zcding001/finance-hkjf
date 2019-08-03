<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <title>查看合同</title>
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <style>
    * {
        margin: 0;
        padding: 0;
    }

    html,
    body {
        min-height: 100%;
        font-family: "Microsoft YaHei", sans-serif;
        box-sizing: border-box;
    }

    html {
        -ms-text-size-adjust: 100%;
        -webkit-text-size-adjust: 100%;
        -webkit-tap-highlight-color: rgba(0, 0, 0, 0);
    }

    li,
    ol,
    ul,
    dl,
    dt,
    dd {
        list-style: none
    }

    body,
    button,
    input,
    select,
    textarea {
        outline: 0;
    }

    button,
    input[type=button],
    input[type=password],
    input[type=submit],
    input[type=text],
    textarea {
        -webkit-appearance: none
    }

    a:hover,
    a:visited,
    a:link,
    a:active {
        text-decoration: none;
        color:#333;
    }

    .clearfix::before,
    .clearfix::after {
        content: ".";
        display: block;
        height: 0;
        line-height: 0;
        visibility: hidden;
        clear: both;
    }

    .fl {
        float: left;
    }

    .fr {
        float: right;
    }

    .contract_box {
        margin-top: .3rem;
        margin-bottom: .5rem;
    }

    .contract_list {
        margin-left: .30rem;
        margin-right: .33rem;
        border-bottom: 1px solid #ddd;
        /*text-align:  justify;*/
    }

    .contract_list h3 {
        height: .29rem;
        font-size: .3rem;
        color: #333333;
        font-weight: normal;
        margin-top: .43rem;
        line-height: .29rem;
    }

    .tz_name {
        height: .29rem;
        font-size: .3rem;
        color: #312e27;
        margin-top: .46rem;
        line-height: .29rem;
    }

    .tz_money {
        height: .25rem;
        font-size: .26rem;
        font-weight: normal;
        line-height: .25rem;
        color: #6e6b64;
        margin-top: .28rem;
        margin-bottom: .48rem;
    }

    .contract_list .check_cont{
        height: .28rem;
        font-size: .3rem;
        line-height: .28rem;
        margin-top: 1.14rem;
    }
    .contract_list .check_cont a{
      color: #c08b33;
      height: .28rem;
      display:  inline-block;
    }
    .contract_list .check_cont img{
      width: .12rem;
      height: .18rem;
      padding-left: .15rem;
      position: relative;
      top: -.02rem;
    }
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
    <div class="contract_box">
        <#if conInfoList?? && (conInfoList?size > 0)>
            <#list conInfoList as conInfo>
                <div class="contract_list">
                    <ul class="clearfix">
                        <#if conInfo.contractType != 13 && conInfo.contractType != 6>
                            <li class="fl">
                                <h3>${conInfo.contractName}</h3>
                                <div class="tz_name">借款人：<span>${conInfo.borrowName}</span></div>
                                <div class="tz_money">金额：<span>${conInfo.investAmount}</span>元</div>
                            </li>
                        <#else>
                            <li class="fl">
                                <div class="tz_money"><span>${conInfo.contractName}</span></div>
                            </li>
                        </#if>
                        <li class="fr check_cont" style="margin-top: .28rem;"><a href="javascript:void(0);" onclick="getContractInfo(${conInfo
                        .contractType},${conInfo.bidInvestId},'${sessionId}')">查看合同<img src="../imageLib/contract/zuojiantou.png" alt="箭头"></a></li>
                    </ul>
                </div>
            </#list>
        <#else>
            <div style="font-size: small">
                暂无合同
            </div>
        </#if>
    </div>
</body>
<script>
    function getContractInfo(contractType,bidInvestId,sessionId) {
        window.location.href = "getContractInfo?contractType=" + contractType + "&bidInvestId=" +
                bidInvestId + "&location=3&sessionId=" + sessionId;
    }
</script>
</html>