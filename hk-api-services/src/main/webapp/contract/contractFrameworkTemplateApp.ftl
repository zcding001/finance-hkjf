<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">

    <!-- Sets initial viewport load and disables zooming  -->
    <meta name="viewport" content="initial-scale=1, maximum-scale=1, user-scalable=no, minimal-ui">

    <!-- Makes your prototype chrome-less once bookmarked to your phone's home screen -->
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <script type="text/javascript" src="../jslib/jquery-3.2.1.min.js"></script>
    <link rel="stylesheet" type="text/css" href="../csslib/contract/contract.css">
</head>

<body style="background-color: rgb(247, 247, 247);">
<!-- PC端合同内容 -->
<div class="contract_box">
    <div id="content" style="margin:0 auto;position:relative;">
        ${content}
    	<div id="seal"></div>
    </div>
</div>
</body>
<script>
    'use strict';
    var contractType = ${contractType} - 0;
    var from = ${from} - 0;
    var content = '${content}';
    $(function(){
        /**
         * 1-优选计划，4-定向融资认购协议，5-收益权转让认购协议，6-债权转让协议，9-借款合同（供应链），10-借款合同（非供应链）,
         * 11-票据贴现协议,12-债权转让-应收账款
         * 设置公章
         */
        var contractArr = [1,9,10,11,12];
        var loanArr = [9,10,11,12];
        var _seal = $("#seal");
        //只有从投资记录或还款计划进入的时候才展示公章
        if(from != 1){
            if (contractType == 2){
                _seal.addClass("hkjt_gz");
            }else if (contractArr.includes(contractType)){
                //判断显示深圳理想的章还是理想霍尔果斯的章
                if (content.includes("鸿坤理想（深圳）科技发展有限公司")){
                    _seal.addClass("szlx_gz");
                } else {
                    _seal.addClass("hrgs_gz");
                }
                if(loanArr.includes(contractType)){
                    _seal.css("top","400px");
                }
            }
        }
    })
</script>
</html>