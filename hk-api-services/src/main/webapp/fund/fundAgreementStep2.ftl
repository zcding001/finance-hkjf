<!DOCTYPE html>
<html>
<head  lang="en">
    <meta charset="UTF-8">
    <title>鸿坤金服签署认购协议</title>
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <style>
        *{margin:0;padding:0;}
        html,body{min-height:100%;font-family:"Microsoft YaHei",sans-serif;box-sizing:border-box;}
        html{-ms-text-size-adjust:100%;-webkit-text-size-adjust:100%;-webkit-tap-highlight-color:rgba(0,0,0,0);}
        body,button,input,select,textarea{outline:0;}
        img{border:0;vertical-align:middle;}
        a{text-decoration: none;color: #000;}
        a:hover, a:visited, a:link, a:active{ text-decoration:none;color:#fff;}
        ul{ list-style: none;}
        i,em{font-style: normal;}
        input,textarea,button,select{border: none;border-radius:0;outline: none;/*清除选中效果*/ -webkit-appearance: none;/*清楚浏览器默认的样*/}
        input::-webkit-input-placeholder{color:#ccc;}
        .clearfix::before,.clearfix::after{content:".";display:block;height:0;line-height:0;visibility:hidden;clear:both;}
        .fl{float:left;}.fr{float:right;}
        .sub_head{width: 100%;height: .7rem;background:#fff7ea;font-size: .24rem;line-height: .7rem;color:#ffb540;text-align: center;}
        .sub_main ul {margin-top:.36rem;padding: 0 .28rem 1.5rem .28rem;}
        .sub_main ul li{font-size: 0;}
        .sub_main ul .list_one{height:auto; line-height: .7rem;display: flex;}
        .sub_main ul li i{color:#ed5345;position: relative;left: -0.13rem;width: .1rem;display:  inline-block;}
        .sub_main ul li span:first-child{color:#6e6b64;font-size: .28rem;width: 1.8rem;display: inline-block; }
        .sub_main ul li span:last-child{color:#312e27; font-size: .30rem; flex: 1;}
        .sp_cont{border-bottom: 1px solid #ddd;}
        .sub_main ul li  input[type="text"]{font-size: .22rem;line-height: .28rem;color: #312e27;width: 100%;padding-bottom: .26rem;padding-top:.22rem;border-bottom: 1px solid #ddd !important;box-sizing: border-box; }
        .sub_main ul li .sub_div1{font-size: .28rem;color:#6e6b64;line-height: .36rem;margin-bottom: .28rem; }
        .sub_main ul li .sub_div_next{font-size: .30rem;color:#312e27;text-align:  justify;}
        .liabel{font-size: .30rem; color:#312e27;}
        .liabel_2{font-size: .28rem;color:#312e27;margin-top: .38rem;}
        .liabel_1{font-size: .26rem;display: flex;}
        .flex_em{flex: 1;padding-left: .1rem;line-height: .36rem;text-align:  justify;}
        input[type='radio'].radio {
            opacity: 0;
            display: inline-block;
            height: .32rem;
        }
        label.radio {background: url(${request.contextPath}/imageLib/fund/icon_radio.png) no-repeat;width: .32rem;height: .32rem;background-size: 100% 100%;position: relative; top: .05rem;display:  inline-block;}
        input[type='radio'].radio:checked+ .radio {background: url(${request.contextPath}/imageLib/fund/icon_checked.png) no-repeat center center;width: .32rem;height: .32rem;background-size: 100%;position:  relative;top: .05rem;}
        .sub_div2{width: 100%;}
        .sub_div2 input{margin-top:.22rem;border-bottom: 1px solid #ddd;color:#312e27;font-size: .28rem;line-height: .7rem;display: block;}
        .js-signature canvas{background: transparent !important;}
        .div_next{width: 100%;}
        .div_next a{width: 100%;position: fixed;bottom: 0;height: .96rem;background-image: linear-gradient(90deg,rgba(221, 188, 124, 1) 0%,rgba(193, 155, 83, 1) 100%),linear-gradient(rgba(255, 255, 255, 1), rgba(255, 255, 255, 1));font-size: .34rem;line-height: .96rem;color: #ffffff;text-align: center;display: block;}
        .div_input{width: 100%;height: auto;line-height:.24rem;font-size:.18rem; border-bottom: 1px solid #312e27;outline:  none;padding-top:.26rem;padding-bottom: .1rem;padding-left: .52rem; position:  relative;box-sizing:  border-box;word-wrap: break-word;word-break: break-all;}
        .div_input:before{content: "";width: .52rem;height: 10px;background: #fff;position: absolute;top: .56rem;left: 0;display: inline-block;}

        /*弹出层*/
        .jd_win{position:fixed;top:0;left:0;height:100%;width:100%;background:rgba(0,0,0,.4);z-index:1001}
        .jd_win .jd_win_box{width:5.6rem;background:#fff;border:1px solid #e0e0e0;border-radius:.1rem;margin:0 auto;margin-top:40%}
        .jd_win .jd_win_box .jd_win_box_text{font-size:.3rem;color:#333;line-height:.4rem;padding:.53rem .53rem;border-bottom:1px solid #eee}
        .jd_win .jd_win_box .jd_btn_box{width:100%;margin:0 auto;font-size:0;    text-align: center;margin: .3rem 0;}
        .jd_win .jd_win_box .jd_btn_box .a1, .jd_win .jd_win_box .jd_btn_box .a2{width:1.5rem;height:.6rem;font-size:.3rem;border-radius: 4px;border: 1px solid rgba(193, 155, 83, 1);text-align: center;line-height: .6rem;display:inline-block;}
        .jd_win .jd_win_box .jd_btn_box .a1{ margin-right: 10%;background: #fff;color: rgba(193, 155, 83, 1);}
        .jd_win .jd_win_box .jd_btn_box .a2{background: rgba(193, 155, 83, 1);color: #fff;}
        @-webkit-keyframes jumpOut{0%{opacity:0;transform:translateY(-2000px);-webkit-transform:translateY(-2000px)}60%{opacity:1;transform:translateY(30px);-webkit-transform:translateY(30px)}75%{transform:translateY(-10px);-webkit-transform:translateY(-10px)}90%{transform:translateY(5px);-webkit-transform:translateY(5px)}to{opacity:1;transform:none;-webkit-transform:none}}
        .jumpOut{animation:jumpOut 1s ease}
        /*异常弹出层*/
        .jd_win_err{position:fixed;top:0;left:0;height:100%;width:100%;background:rgba(0,0,0,.4);z-index:1001}
        .jd_win_err .jd_win_box_err{width:5.6rem;background:#fff;border:1px solid #e0e0e0;border-radius:.1rem;margin:0 auto;margin-top:40%}
        .jd_win_err .jd_win_box_err .jd_win_box_text_err{font-size:.3rem;color:#333;text-align:center;line-height:.3rem;padding:.53rem 0;border-bottom:1px solid #eee}
        .jd_win_err .jd_win_box_err .jd_btn_box_err{width:100%;margin:0 auto}
        .jd_win_err .jd_win_box_err .jd_btn_box_err a{text-align:center;line-height:.8rem;font-size:.3rem;width:100%;color:#f39200;display:block}
    </style>
    <script src="${request.contextPath}/jslib/fund/jquery-1.11.0.min.js"></script>
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

<form method="post" action="" name="fundAgreementForm2" id = "fundAgreementForm2">
    <input id = "sessionId" name = "sessionId" type="hidden" value="${sessionId}">
    <input id = "agreementId" name = "agreementId" type="hidden" value="${agreementId}">
    <input id = "state" name = "state" type="hidden" value="">
    <input id = "reason" name = "reason" type="hidden" value="">

    <!-- 第二步开始 -->
    <div class="sub_head ">客户填写相应的认购信息，认购完成生成认购协议。</div>
    <div class="sub_main ">
        <ul>
            <li style="margin-top:.5rem;">
                <div class="sub_div1 sub_div_next"><i>＊</i><font>投资者代表并保证：</font><em style="color:#6e6b64;">（仅可选择一项）</em></div>
                <label class="liabel_2 liabel_1">
                    <input type="radio"  name="controlStatementFlag" id="radio_0_1" class="radio" value="0">
                    <label for="radio_0_1"  class="radio"></label>
                    <em class="flex_em">担任受益人的受托人，代理人，代表人或代名人</em>
                </label>
                <label class="liabel_2 liabel_1">
                    <input type="radio"  name="controlStatementFlag" id="radio_1_1" class="radio" checked="checked" value="1">
                    <label for="radio_1_1"  class="radio"></label>
                    <em class="flex_em">以实益拥有人的身份行事</em>
                </label>
                <input id = "state_1" type="hidden" value="">
            </li>

            <li  style="margin-top:.65rem;">
                <input id = "state_2" type="hidden" value="">
                <div class="sub_div1 sub_div_next"><i>＊</i><font>投资者声明并保证它是:</font><em style="color:#6e6b64;">（仅可选择一项）</em></div>
                <label class="liabel_2 liabel_1">
                    <input type="radio"  name="investStatementFlag" id="radio_0_2" class="radio"  value="0">
                    <label for="radio_0_2"  class="radio"></label>
                    <em class="flex_em">个人为美国人士（定义见认购协议） （包括任何此类人的信托）</em>
                </label>
                <label class="liabel_2 liabel_1">
                    <input type="radio"  name="investStatementFlag" id="radio_1_2" class="radio" checked="checked" value="1">
                    <label for="radio_1_2"  class="radio"></label>
                    <em class="flex_em">个人不是美国人人士（包括任何此类个人的信托）</em>
                </label>
                <label class="liabel_2 liabel_1">
                    <input type="radio"  name="investStatementFlag" id="radio_2_2" class="radio" value="2">
                    <label for="radio_2_2" class="radio"></label>
                    <em class="flex_em">经纪商</em>
                </label>
                <label class="liabel_2 liabel_1">
                    <input type="radio"  name="investStatementFlag" id="radio_3_2" class="radio" value="3">
                    <label for="radio_3_2" class="radio"></label>
                    <em class="flex_em">保险公司</em>
                </label>
                <label class="liabel_2 liabel_1">
                    <input type="radio"  name="investStatementFlag" id="radio_4_2" class="radio" value="4">
                    <label for="radio_4_2" class="radio"></label>
                    <em class="flex_em">根据1940年证券法案在证券交易委员会登记的投资公司（定义见认购协议）</em>
                </label>
                <label class="liabel_2 liabel_1">
                    <input type="radio"  name="investStatementFlag" id="radio_5_2" class="radio" value="5">
                    <label for="radio_5_2" class="radio"></label>
                    <em class="flex_em">根据1940年法案第3节所界定的投资公司的发行人，除3（c）（1）或3（c）（7）之外</em>
                </label>
                <label class="liabel_2 liabel_1">
                    <input type="radio"  name="investStatementFlag" id="radio_6_2" class="radio" value="6">
                    <label for="radio_6_2" class="radio"></label>
                    <em class="flex_em"> 非营利组织</em>
                </label>
                <label class="liabel_2 liabel_1">
                    <input type="radio"  name="investStatementFlag" id="radio_7_2" class="radio" value="7">
                    <label for="radio_7_2" class="radio"></label>
                    <em class="flex_em">退休金计划（不包括政府退休金计划）</em>
                </label>
                <label class="liabel_2 liabel_1">
                    <input type="radio"  name="investStatementFlag" id="radio_8_2" class="radio" value="8">
                    <label for="radio_8_2" class="radio"></label>
                    <em class="flex_em">银行或储蓄机构（控股）</em>
                </label>
                <label class="liabel_2 liabel_1">
                    <input type="radio"  name="investStatementFlag" id="radio_9_2" class="radio" value="9">
                    <label for="radio_9_2" class="radio"></label>
                    <em class="flex_em">任何州或州以下政治分区，包括（i）州或政治分区的任何机构，当局和部门； （ii）由州或政治分区或其任何机构，当局和部门控制的资产计划或资金； （iii）州或政治分区或其任何机构，当局和部门的任何官员，代理人或雇员，以其官方身份行事（不包括政府养老金计划）</em>
                </label>
                <label class="liabel_2 liabel_1">
                    <input type="radio"  name="investStatementFlag" id="radio_a_2" class="radio" value="10">
                    <label for="radio_a_2" class="radio"></label>
                    <em class="flex_em">州或市政府的养老金计划</em>
                </label>
                <label class="liabel_2 liabel_1">
                    <input type="radio"  name="investStatementFlag" id="radio_b_2" class="radio" value="11">
                    <label for="radio_b_2" class="radio"></label>
                    <em class="flex_em">主权财富基金或外国官方机构</em>
                </label>
                <div>
                    <label class="liabel_2 liabel_1">
                        <input type="radio"  name="investStatementFlag" id="radio_c_2" class="radio" value="12">
                        <label for="radio_c_2" class="radio"></label>
                        <em class="flex_em" style="text-align: left;">其他（请详细说明）</em>
                    </label>
                    <div class="div_input"  contenteditable="plaintext-only" ></div>
                </div>
            </li>
            <li  style="margin-top:.65rem;">
                <input id = "state_3" type="hidden" value="">
                <div class="sub_div1 sub_div_next"><i>＊</i><font>根据投资者最大知悉情况，投资者是否与该基金其他任何投资者存在控制、被控制及同一控制关系？（请勾选“否”）</font></div>
                <label class="liabel_2" style="margin-right: 1.9rem;">
                    <input type="radio"  name="investAwareFlag" id="radio_1_3" class="radio" value="1">
                    <label for="radio_1_3" class="radio" style="    top: .06rem;"></label> 是
                </label>
                <label class="liabel_2">
                    <input type="radio"  name="investAwareFlag" id="radio_0_3" class="radio" checked="checked" value="0">
                    <label for="radio_0_3" class="radio" style="top: .06rem;"></label> 否
                </label>
            </li>
            <li  style="margin-top:.53rem;">
                <input id = "state_4" type="hidden" value="">
                <div class="sub_div1 sub_div_next"><i>＊</i><font>投资者是否与其他人基于本基金的整体或部分回报签订掉期，结构化票据或其他衍生工具？（请勾选“否”）</font> </div>
                <label class="liabel_2" style="margin-right: 1.9rem;">
                    <input type="radio"  name="signedSwapFlag" id="radio_1_4" class="radio" value="1">
                    <label for="radio_1_4" class="radio" style="top: .06rem;"></label> 是
                </label>
                <label class="liabel_2">
                    <input type="radio"  name="signedSwapFlag" id="radio_0_4" class="radio" checked="checked" value="0">
                    <label for="radio_0_4" class="radio" style="top: .06rem;"></label> 否
                </label>
            </li>
            <li  style="margin-top:.53rem;">
                <input id = "state_5" type="hidden" value="">
                <div class="sub_div1 sub_div_next"><i>＊</i><font>任何其他人士是否根据本协议获得的A类权益有获益权（除投资者的股东，合伙人，保单拥有人或投资者其他股权的受益人）或投资者是否作为他方的代理人，代表或提名人?</font> </div>
                <label class="liabel_2" style="margin-right: 1.9rem;">
                    <input type="radio"  name="proxyFlag" id="radio_1_5" class="radio" value="1">
                    <label for="radio_1_5" class="radio" style="top: .06rem;"></label> 是
                </label>
                <label class="liabel_2">
                    <input type="radio"  name="proxyFlag" id="radio_0_5" class="radio" checked="checked" value="0">
                    <label for="radio_0_5" class="radio" style="top: .06rem;"></label> 否
                </label>
            </li>
            <li  style="margin-top:.53rem;">
                <input id = "state_6" type="hidden" value="">
                <div class="sub_div1 sub_div_next"><i>＊</i><font>美国税务居民：</font><em style="color:#6e6b64;">（仅可选择一项）</em></div>
                <div>
                    <label class="liabel_2 liabel_1">
                        <input type="radio"  name="americanTaxFlag" id="radio_3_6" class="radio" value="3">
                        <label for="radio_3_6" class="radio"></label>
                        <em class="flex_em">
                            我是美国公民并且/或者是美国税务居民（绿卡持有人或符合实际居留标准）。我的美国联邦纳税人识别号是：
                        </em>
                    </label>
                    <div class="div_input"  contenteditable="plaintext-only" ></div>
                </div>
                <label class="liabel_2 liabel_1">
                    <input type="radio"  name="americanTaxFlag" id="radio_2_6" class="radio" value="2">
                    <label for="radio_2_6" class="radio"></label>
                    <em class="flex_em">我出生于美国（或美属领土）但我已放弃美国国籍，证明见附件</em>
                </label>
                <label class="liabel_2 liabel_1">
                    <input type="radio"  name="americanTaxFlag" id="radio_1_6" class="radio" checked="checked" value="1">
                    <label for="radio_1_6" class="radio"></label>
                    <em class="flex_em">我不是美国公民或美国税务居民</em>
                </label>
            </li>
            <li class="mess_error1">请填写完整的信息!</li>
        </ul>
    </div>
    <div class="div_next section02" id = "submitA"><a href="javascript:void(0);" onclick="submitProtocol()">下一步</a></div>
    <!-- 第二步结束 -->

</form>

<!--弹出框-->
<div class="jd_win" style="display: none;">
    <div class="jd_win_box">
        <div class="jd_win_box_text">
            如果更换选项，会导致认购失败，是否要继续选择？
        </div>
        <div class="jd_btn_box">
            <a href="javascript:;" class="a1">继续</a>
            <a href="javascript:;" class="a2">放弃</a>
        </div>
    </div>
</div>

<!--异常弹出框 -->
<div class="jd_win_err" style="display: none;">
    <div class="jd_win_box_err">
        <div class="jd_win_box_text_err">

        </div>
        <div class="jd_btn_box_err">
            <a href="javascript:;" class="">我知道了</a>
        </div>
    </div>
</div>

</body>

<script>
    var $source = '${source}';
    // 初始化页面
    $(function () {
        $(".jd_win").hide();
        $(".jd_win_err").hide();
        $(".jd_btn_box_err").find("a").on("click",function(){
            $(".jd_win_err").hide();
        })
    });

    // 提交表单
    function submitProtocol(){
        // 校验是否为默认选项
        var _temp = 0;
        for(var i = 1;i<7;i++){
            if($('#state_' + i).val() == 3){
                _temp = _temp + 1;
            }
        }
        if(_temp == 0){
            $('#state').val(0);
        }else{
            $('#state').val(3);
            $('#reason').val("您填写的购买协议不具备投资者购买资格，可以拨打客服电话：4009009630咨询");
        }
        $.ajax({
            type: 'post',
            url: "${request.contextPath}/fundController/toFundAgreementStep3",
            dataType:'json',
            data: $("#fundAgreementForm2").serialize(),
            success: function(data) {
                // OC 交互
                if (data.resStatus == "2003" || data.resStatus == "1000"){
                    if($source == "1"){
                        window.webkit.messageHandlers.getExtraUserInfo.postMessage(JSON.stringify(data));
                    }else if($source == "2"){
                        window.Android.getExtraUserInfo(JSON.stringify(data));
                    }
                }else{
                    $(".jd_win_err").show();
                    $(".jd_win_box_err").addClass("jumpOut");
                    $(".jd_win_box_text_err").text(data.resMsg);
                    return false
                }
            }
        });
    }

    // 默认id radio_defaultValue_index;
    var defaultIdArray = ["radio_1_0","radio_1_1","radio_1_2","radio_0_3","radio_0_4","radio_0_5","radio_1_6"];
    $(":input:radio[id^='radio']").on("click",function(){
        var _val = $(this).val();
        var index = $(this).attr('id').substring(8,9);
        var defalutVal = defaultIdArray[index].substring(6,7)
        if(_val != defalutVal){
            $(".jd_win").show();
            $(".jd_win_box").addClass("jumpOut")
                    .attr("name",defaultIdArray[index] +"|"+$(this).attr('id'));
        }else{
            $('#state_' + index).val(0);
        }
    });

    $(".jd_btn_box").find("a").on("click",function(){
        var def_cur = $(".jd_win_box").attr("name").split("|");
        var defaultId = def_cur[0];
        var index = defaultId.substring(8,9);
        var curId = def_cur[1];
        if($(this).text() == "继续"){
            $('#'+ curId).prop("checked",true);
            $('#state_' + index).val(3);
        }else if($(this).text() == "放弃"){
            $('#'+ defaultId).prop("checked",true);
            $('#state_' + index).val(0);
        }
        $(".jd_win").hide();
    })

</script>
</html>