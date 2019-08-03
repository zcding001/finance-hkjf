/**
 * Created by songpengfei on 2017/10/24.
 */

var flg=true
function showbubbleBtn(txt,fn){
    $('#bubbleBtn').show().html(txt)
    typeof fn=='function'?fn():void 0
    if(flg){
        flg=false
        var sto=window.setTimeout(function(){
            $('#bubbleBtn').hide(100)
            flg=true
        },2000)
    }

}


//输入保留两位小数
function clearNoNum(obj) {
    if(obj && obj.value){
        //先把非数字的都替换掉，除了数字和.
        obj.value = obj.value.replace(/[^\d.]/g,"");
        //保证只有出现一个.而没有多个.
        obj.value = obj.value.replace(/\.{2,}/g,".");
        //必须保证第一个为数字而不是.
        obj.value = obj.value.replace(/^\./g,"");
        //保证.只出现一次，而不能出现两次以上
        obj.value = obj.value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
        //只能输入两个小数
        obj.value = obj.value.replace(/^(\-)*(\d+)\.(\d\d).*$/,'$1$2.$3');
    }
}




//获取浏览器参数，如需解码第二个参数传入true
function queryURLParameter(url, isEncode) {
    url = url || window.location.href;
    var obj = {}, reg = /([^?&=]+)=([^?&=]+)/g;
    url.replace(reg, function () {
        var arg = arguments;
        obj[arg[1]] = isEncode ? decodeURIComponent(arg[2]) : arg[2];
    });
    return obj;
}



/*上拉弹窗*/
/*
* option：{
 Mask:'.mask-black',      遮罩
 Menu:'.s-zffs',          菜单
 Close:'.Closetc',        取消按钮
 Whole:'.s-actionsheet',  总体弹出
 oninput:'.s-zffangshixz' 选择菜单后要改变的input
 }
* */
function zonghanshu(option){
    option.Mask=option.Mask  || '.mask-black'
    option.Menu=option.Menu  || '.s-zffs'
    option.Close=option.Close|| '.Closetc'
    option.Whole=option.Whole|| '.s-actionsheet'
    option.oninput=option.oninput|| '.s-zffangshixz'
    $(option.Mask).show()
    $(option.Whole).addClass('s-actionsheetClose')
    $(option.Close).click(CloseTC)
    $(option.Mask).click(CloseTC)
    $( option.Menu).click(function(){
    	  $(option.oninput).val($(this).html()).attr('paymenttype',$(this).attr('paymenttype'))
    	  CloseTC()
    })
    function CloseTC(){//关闭弹层
        $(option.Mask).hide()
        $(option.Whole).removeClass('s-actionsheetClose')
    }

}


function getAttribute(that,attr){
    return that.getAttribute(attr)
}



/*关闭支持银行弹层*/
$('.DoubtSymbol').click(function(){ //支持银行弹层
    $('.bankmask').addClass('s-bankmaskClose')
    $('.gbBtnBiank').click(function(){
        $('.bankmask').removeClass('s-bankmaskClose')
    })
})





