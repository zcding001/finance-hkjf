<!DOCTYPE html>
<html>
<head  lang="en">
    <meta charset="UTF-8">
    <title>鸿坤金服签署认购协议</title>
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <style>
        *{margin:0;padding:0;}
        html,body{width:100%;min-height:100%;font-family:"Microsoft YaHei",sans-serif;box-sizing:border-box;}
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
        label.radio {background: url(${request.contextPath}/imageLib/fund/img_quanj_mexz.png) no-repeat;width: .32rem;height: .32rem;background-size: 100% 100%;position: relative; top: .05rem;display:  inline-block;}
        input[type='radio'].radio:checked+ .radio {background: url(${request.contextPath}/imageLib/fund/img_quanj_xuanz.png) no-repeat center center;width: .32rem;height: .32rem;background-size: 100%;position:  relative;top: .05rem;}
        .sub_div2{width: 100%;}
        .sub_div2 input{margin-top:.22rem;border-bottom: 1px solid #ddd;color:#312e27;font-size: .28rem;line-height: .7rem;display: block;}
        .js-signature{background: url(${request.contextPath}/imageLib/fund/img_name_1.png) no-repeat center center;background-size: 100%;}
        .js-signature canvas{background: transparent !important;}
        .div_next{width: 100%;}
        .div_next a{width: 100%;position: fixed;bottom: 0;height: .96rem;background:#f39200;font-size: .34rem;line-height: .96rem;color: #ffffff;text-align: center;display: block;}
        .mess_error{font-size: .16rem;color:#ed5345;line-height: .30rem;margin-top: .15rem;display:none;}
        .mess_error_1{font-size: .16rem;color:#ed5345;line-height: .30rem;margin-top: .15rem;display:block;}

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
    <script src="${request.contextPath}/jslib/fund/jq-signature.js"></script>
    <script src="${request.contextPath}/jslib/fund/jSignature.min.noconflict.js"></script>
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

<form method="post" action="" name="fundAgreementForm1" id = "fundAgreementForm1">
    <input id = "regUserId" name = "regUserId" type="hidden" value="${regUserId}">
    <input id = "fundInfoId" name = "fundInfoId" type="hidden" value="${fundInfoId}">
    <input id = "sessionId" name = "sessionId" type="hidden" value="${sessionId}">
    <input id = "state" name = "state" type="hidden" value="0">
    <input id = "reason" name = "reason" type="hidden" value="">

    <!-- 第一步开始 -->
    <div  class="sub_head">客户填写相应的认购信息，认购完成生成认购协议。</div>
    <div  class="sub_main" style="overflow:scroll;overflow-x:hidden;">
        <ul>
            <li class="list_one" style="margin-top:.23rem;height: auto;">
                <span style="width:1.9rem;"><i>＊</i>投资者姓名：</span>
                <span>
	                <input class="inp_info va" id = "userName" name = "userName" type="text" placeholder="请输入名" value="${info.userName}" onblur="checkNullValue('userName')" >
                    <input class="inp_info va" id = "userSurname" name = "userSurname" type="text" placeholder="请输入姓" value="${info.userSurname}" onblur="checkNullValue('userSurname')" >
                </span>
            </li>
            <li class="list_one" style="margin-top:.23rem;">
                <span><i>＊</i>出生日期：</span>
                <span>
                    <input class = "va" type="text" id = "birthday" name = "birthday" placeholder="例如03/15/1970" value="${birthday}"  nullmsg="请填写出生日期！" onblur="checkNullValue('birthday')">
                </span>
            </li>
            <li class="list_one" style="margin-top:.23rem;">
                <span><i>＊</i>国籍：</span>
                <span>
                    <input class = "va" type="text" id = "nationality" name = "nationality" placeholder="请输入国籍,例：中国" value="中国"  nullmsg="请填写国籍！" onblur="checkNullValue('nationality')">
                </span>
            </li>
            <li class="list_one" style="margin-top:.23rem;">
                <span><i>＊</i>出生地：</span>
                <span>
                    <input class = "va" type="text" id = "birthPlace" name = "birthPlace" placeholder="需和护照出生地一致,例：湖北" value="${info.birthPlace}"  onblur="checkNullValue('birthPlace')">
                </span>
            </li>
            <li class="list_one" style="margin-top: .23rem;">
                <span style="width:2.7rem;"><i>＊</i>认购金额(<#if lowestAmountUnit == 2>美元<#else>元</#if>)：</span>
                     <span class="">
	                    <form action="">
                            <#if stepValue != 0 && stepValue??>
                                <input type="text" id = "investAmount" name = "investAmount" onblur="checkInvestAmount('investAmount')" placeholder="${bottomAmount}<#if lowestAmountUnit == 2>美元<#else>元</#if>起投，${stepValue}<#if lowestAmountUnit == 2>美元<#else>元</#if>递增" value = ${info.investAmount} >
                            <#else>
                                <input type="text" id = "investAmount" name = "investAmount" onblur="checkInvestAmount('investAmount')" placeholder="${bottomAmount}<#if lowestAmountUnit == 2>美元<#else>元</#if>起投" value = ${info.investAmount} >
                            </#if>
                                <p id = "error_investAmount" class="mess_error"></p>
                        </form>
	                </span>
            </li>
            <li class="list_one" style="margin-top:.23rem;height: auto;">
                <span><i>＊</i>联系地址：</span>
                <span>
	          <input class="inp_info va" type="text" id = "streetNumber" name = "streetNumber" placeholder="街道门牌号(例：海淀大街3号，7号楼，25楼A)" onblur="checkNullValue('streetNumber')" value = "${info.streetNumber}">
	          <input class="inp_info va" type="text" id = "region" name = "region" placeholder="行政区(例：北京市海淀区)" onblur="checkNullValue('region')"  value = "${info.region}" >
	          <input class="inp_info va" type="text" id = "province" name = "province" placeholder="省份/州(例：北京)" onblur="checkNullValue('province')" value = "${info.province}">
	          <input class="inp_info va" type="text" id = "country" name = "country" placeholder="国家(例：中国)" onblur="checkNullValue('country')" value = "${info.country}">
	          <input class="inp_info va" type="text" id = "postCode" name = "postCode" placeholder="邮编(例：100070)" onblur="checkNullValue('postCode')" value = "${info.postCode}">
	        </span>
            </li>
            <li class="list_one" style="margin-top:.23rem;">
                <span><i>＊</i>联系电话：</span>
                <span><input class = "va" type="text" id = "tel" name = "tel" placeholder="例：+86 18512345567" nullmsg="请填写联系电话！"  onblur="checkNullValue('tel')" value = "${info.tel}" > </span>
            </li>
            <li class="list_one" style="margin-top:.23rem;">
                <span><i>＊</i>联系邮箱：</span>
                <span>
                    <input type="text" id = "email" name = "email" placeholder="例：zhangsanfeng@gamil.com" onblur="checkEmail('email')" value = "${info.email}"  >
                    <p id = "error_email" class="mess_error"></p>
                </span>
            </li>
            <li class="list_one" style="margin-top:.23rem;">
                <span><i>＊</i>职业：</span>
                <span><input class = "va" type="text" id = "profession" name = "profession" placeholder="例：律师" onblur="checkNullValue('profession')" value = "${info.profession}" ></span>
            </li>
            <li class="list_one" style="margin-top:.23rem;">
                <span><i>＊</i>护照号：</span>
                <span><input class = "va"  type="text" id = "passportNo" name = "passportNo" placeholder="例：G######" onblur="checkNullValue('passportNo')" value = "${info.passportNo}"></span>
            </li>
            <li class="list_one" style="margin-top:.23rem;">
                <span style="width: 2.3rem;"><i>＊</i>收款银行名称：</span>
                <span><input class = "va" type="text" id = "bankName" name = "bankName" placeholder="例：JP MORGAN CHASE BANK NA" onblur="checkNullValue('bankName')" value = "${info.bankName}"></span>
            </li>
            <li class="list_one" style="margin-top:.23rem;height:auto;">
                <span style="width: 2.3rem;"><i>＊</i>收款银行地址：</span>
                <span>
	         <input class = "va" type="text" id = "bankStreetNumber" name = "bankStreetNumber" placeholder="街道门牌号(例：111 POLARIS PARKWAY)" onblur="checkNullValue('bankStreetNumber')" value = "${info.bankStreetNumber}" >
	         <input class = "va" type="text" id = "bankRegion" name = "bankRegion" placeholder="银行所在地行政区(例：COLUMBUS)" onblur="checkNullValue('bankRegion')" value = "${info.bankRegion}">
             <input class = "va" type="text" id = "bankProvince" name = "bankProvince" placeholder="银行所在地省份/州(例：OH)" onblur="checkNullValue('bankProvince')" value = "${info.bankProvince}" >
	         <input class = "va" type="text" id = "bankCountry" name = "bankCountry" placeholder="银行所在地国家(例：USA)" onblur="checkNullValue('bankCountry')" value = "${info.bankCountry}">
            <input class = "va" type="text" id = "bankPost" name = "bankPost" placeholder="银行所在地邮编(例：43240)" onblur="checkNullValue('bankPost')" value = "${info.bankPost}">

	        </span>
            </li>
            <li class="list_one" style="margin-top:.23rem;">
                <span style="width: 2.96rem;"><i>＊</i>收款银行SWIFT编码：</span>
                <span><input class = "va"  type="text" id = "seiftCode" name = "seiftCode" placeholder="例：CHUSAS33" onblur="checkNullValue('seiftCode')" value = "${info.seiftCode}" ></span>
            </li>
            <li class="list_one" style="margin-top:.23rem;">
                <span style="width: 2.96rem;"><i>＊</i>收款银行ABA编码：</span>
                <span><input class = "va" type="text" id = "abaCode" name = "abaCode" placeholder="例：234342243" onblur="checkNullValue('abaCode')" value = "${info.abaCode}" ></span>
            </li>
            <li class="list_one" style="margin-top:.23rem;">
                <span style="width: 2.3rem;"><i>＊</i>收款账号号码：</span>
                <span><input class = "va" type="text" id = "recipientAccountCode" name = "recipientAccountCode" placeholder="例：611283944323423555" onblur="checkNullValue('recipientAccountCode')" value = "${info.recipientAccountCode}" ></span>
            </li>
            <li  style="margin-top:.6rem;">
                <div class="sub_div1"><i>＊</i>投资者的电汇付款是否来自上述投资者姓名本人的银行的账户？</div>
                <label class="liabel" style="margin-right: 1.9rem;">
                    <input type="radio"  id = "radio_1_0" name="payFlag" id="radio0" class="radio" checked="checked" value="1">
                    <label for="radio_1_0" class="radio"></label> 是
                </label>
                <label class="liabel">
                    <input type="radio"  name="payFlag" id="radio_2_0" class="radio" value = "0">
                    <label for="radio_2_0" class="radio"></label> 否
                </label>
            </li>
            <li style="margin-top:.5rem;">
                <div class="sub_div1">永久地址<em style="color:#9c9992;">（身份证地址，如果和联系地址相同则可跳过）：</em>
                </div>
                <div class="sub_div2">
                    <input type="text" id = "permanentStreetNumber" name = "permanentStreetNumber" placeholder="街道门牌号（如海淀大街3号，7-25-A)" value = "${info.permanentStreetNumber}" >
                    <input type="text" id = "permanentRegion" name = "permanentRegion" placeholder="行政区（例如北京市海淀区）" value = "${info.permanentRegion}" >
                    <input type="text" id = "permanentProvince" name = "permanentProvince" placeholder="省份/州（例如北京）" value = "${info.permanentProvince}" >
                    <input type="text" id = "permanentCountry" name = "permanentCountry" placeholder="国家（例如中国）" value = "${info.permanentCountry}" >
                    <input type="text" id = "permanentPostCode" name = "permanentPostCode" placeholder="邮编（例如100070）" value = "${info.permanentPostCode}" >
                </div>
            </li>
            <li style="margin-top:.5rem;">
                <div class="sub_div1">(选填)中间行<em style="color:#9c9992;">（如有中间行，请填写以下中间行信息）：</em>
                </div>
                <div class="sub_div2">
                    <input type="text" id = "midBankName" name = "midBankName" placeholder="中间行名称(例：BANK OF AMERICA)" value = "${info.midBankName}" >
                    <input type="text" id = "midBankStreetNumber" name = "midBankStreetNumber" placeholder="街道门牌号(例：600 ANTON BOULEVARD,SUITE 150)" value = "${info.midBankStreetNumber}" >
                    <input type="text" id = "midBankRegion" name = "midBankRegion" placeholder="中间行所在地行政区(例：COSTA MESA)" value = "${info.midBankRegion}" >
                    <input type="text" id = "midBankProvince" name = "midBankProvince" placeholder="中间行所在地省份/州(例：CA)" value = "${info.midBankProvince}" >
                    <input type="text" id = "midBankCountry" name = "midBankCountry" placeholder="中间行所在地国家(例：USA)" value = "${info.midBankCountry}" >
                    <input type="text" id = "midBankPostCode" name = "midBankPostCode" placeholder="中间行所在地邮编(例：43240)" value = "${info.midBankPostCode}" >
                </div>
            </li>
            <li class="list_one" style="margin-top:.23rem;">
                <span style="width: 2.96rem;">中间行SWIFT编码：</span>
                <span><input type="text" id = "midBankSwiftCode" name = "midBankSwiftCode" placeholder="例：CHUSAS33"  value = "${info.midBankSwiftCode}" ></span>
            </li>
            <li class="list_one" style="margin-top:.23rem;">
                <span style="width: 2.96rem;">中间行ABA编码：</span>
                <span><input type="text" id = "midBankAbaCode" name = "midBankAbaCode" placeholder="例：234342243"  value = "${info.midBankAbaCode}" ></span>
            </li>

            <li class="list_two">
                <span style="margin-top:.6rem;margin-bottom: .26rem;"><i>＊</i>客户签名：</span>
                <input type="hidden" id="signatureBytes" name="signatureBytes">
                <div class="js-signature" style="width: 100%;height: 150px;border: 1px dashed #f39200;">
                </div>
                <a href="javascript:void(0)" style="position:relative;height:.4rem;line-height:.4rem;display:inline-block;font-size:0.28rem;padding:0.1rem .15rem;background: #f39200;top:0.3rem;left:0;border-radius: 0.05rem;" id="resetSignature">重置签名</a>
            </li>
        </ul>
        <div class="div_next"><a href="javascript:void(0);" onclick="toNextPage()">下一步</a></div>
    </div>
    <!-- 第一步结束 -->

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
    var $bottomAmount = '${bottomAmount}';
    var $stepValue = '${stepValue}';
    var $sigdiv = $(".js-signature");
    var $signatureBytes = $("#signatureBytes");
    var $resetSignature = $("#resetSignature");
    var $source = '${source}';
    var $sessionId = '${sessionId}';
    // 初始化页面
    $(function () {
        $(".jd_win").hide();
        $(".jd_win_err").hide();
        $(".jd_btn_box_err").find("a").on("click",function(){
            $(".jd_win_err").hide();
        })
    });

    $(document).on('ready', function() {
        //初始化签名区域
        var dWidth = $sigdiv.width();
        var dHeight = $sigdiv.height();
        $sigdiv.jSignature({height:dHeight,width:dWidth});
    });

    //重置签名方法
    $resetSignature.click(function () {
        $sigdiv.jSignature("reset");
    });

    // 跳转到下一页
    function toNextPage(){
        // 校验参数
        var inputs = $('input.va');
        for(var j=0;j<inputs.length;j++){
            var _id = inputs.eq(j).attr("id");
            $('#error_'+ _id).remove();
            if(inputs.eq(j).val() == ""){
                $('#error_'+ _id).remove();
                inputs.eq(j).after(" <p id = 'error_"+_id+"' class = 'mess_error_1' >必填项不能为空！</p>");
                inputs.eq(j).focus();
                return false;
            }else{
                $('#error_'+ _id).remove();
            }
        }

        if(!checkInvestAmount('investAmount')){
            $("#investAmount").focus();
            return false;
        }
        if(!checkEmail('email')){
            $('#email').focus();
            return false;
        }
        //判断签名不能为空
        if (!$sigdiv.jSignature("isModified")){
            $("#error_signature").remove();
            $resetSignature.after("<p id = 'error_signature' class = 'mess_error_1' style='position:relative;left:1.5rem;top:-.1rem'>请进行签名！</p>");
            $resetSignature.focus();
            return false;
        }else {
            $("#error_signature").remove();
        }

        //将签名值赋予对应的input对象
        $signatureBytes.val($sigdiv.jSignature("getData").split(",")[1]);
        submitProtocol();
    }

    // 校验非空方法
    function checkNullValue(id){
        var $ele = $('#'+id);
        if( $ele.val() == undefined || $ele.val() == null || $.trim($ele.val()) == ""){
            $('#error_'+id).remove();
            $ele.after(" <p id = 'error_"+id+"' class = 'mess_error_1' >必填项不能为空！</p>")
            return false;
        }
        $('#error_'+id).remove();
        return true;
    }


    // 校验投资金额
    function checkInvestAmount(id){
        var $ele = $('#' + id);
        var msg = "";
        if($ele.val() == undefined || $ele.val() == null || $.trim($ele.val()) == ""){
            msg = "请填写认购金额！";
            $('#error_'+id).removeClass('mess_error_1').addClass('mess_error_1').text(msg);
            return false;
        }
        var unValidateNumber = parseFloat($.trim($ele.val()));
        var _bottomAmount = parseFloat($bottomAmount);
        var _stepValue = parseFloat($stepValue);

        var regex = /^[0-9]+([.]{1}[0-9]{1,2})?$/;
        if (!regex.test($.trim($ele.val()))) {
            msg = "请输入正确的金额！";
            $('#error_'+id).removeClass('mess_error_1').addClass('mess_error_1').text(msg);
            return false;
        }
        if(_bottomAmount > unValidateNumber){
            msg = "最低认购金额为" + $bottomAmount;
            $('#error_'+id).removeClass('mess_error_1').addClass('mess_error_1').text(msg);
            return false;
        }
        if($stepValue !=null && $stepValue != 0 && (unValidateNumber - _bottomAmount) % _stepValue != 0){
            msg = $stepValue +"整数倍递增！";
            $('#error_'+id).removeClass('mess_error_1').addClass('mess_error_1').text(msg);
            return false;
        }
        $('#error_'+id).removeClass('mess_error_1').text("");
        return true;
    }

    function checkEmail(id){
        var $ele = $('#' + id);
        var msg = "";
        if($ele.val() == undefined || $ele.val() == null || $.trim($ele.val()) == ""){
            msg = "请填写联系邮箱！";
            $('#error_'+id).removeClass('mess_error_1').addClass('mess_error_1').text(msg);
            return false;
        }
        var regex = /^\w+([-+.']\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
        if (!regex.test($ele.val())) {
            msg = "请输入正确的联系邮箱！";
            $('#error_'+id).removeClass('mess_error_1').addClass('mess_error_1').text(msg);
            return false;
        }
        $('#error_'+id).removeClass('mess_error_1').text("");
        return true;
    }

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
            url: "${request.contextPath}/fundController/initFundAgreement",
            dataType:'json',
            data: $("#fundAgreementForm1").serialize(),
            success: function(data) {
                // OC 交互 跳转登录
                if (data.resStatus == "2003" ) {
                    if ($source == "1") {
                        window.webkit.messageHandlers.getExtraUserInfo.postMessage(JSON.stringify(data));
                    } else if ($source == "2") {
                        window.Android.getExtraUserInfo(JSON.stringify(data));
                    }
                }else if(data.resStatus == "1000"){
                    window.location.href = "${request.contextPath}/fundController/toFundAgreementStep2?agreementId=" + data.agreementId
                            + "&sessionId=" + $sessionId + "&source=" + $source
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
            $('#state').val(0);
            $('#reason').val("");
        }
    });

    $(".jd_btn_box").find("a").on("click",function(){
        var def_cur = $(".jd_win_box").attr("name").split("|");
        var defaultId = def_cur[0];
        var curId = def_cur[1];
        if($(this).text() == "继续"){
            $('#'+ curId).prop("checked",true);
            $('#state').val("3");
            $('#reason').val("您填写的购买协议不具备投资者购买资格，可以拨打客服电话：4009009630咨询");
        }else if($(this).text() == "放弃"){
            $('#'+ defaultId).prop("checked",true);
            $('#state').val(0);
            $('#reason').val("");
        }
        $(".jd_win").hide();
    })



</script>
</html>