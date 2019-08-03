
// 保存风险测评信息
function toEvalution(){
    // 校验是否选中承诺
    var _acceptBoo = $("#btn").hasClass("tj_btn");
    if(!_acceptBoo){
        return;
    }
    // 拼接 有效信息
    var answers = "";
    var scores = "";
    for(var i = 0; i < 6; i++){
        var _temp =  $("#question"+ (i+1)).find(".selected").attr("name");
        var _tempLength = $("#question"+ (i+1)).find(".selected").length;
        if(_tempLength == 0){
            alert("您有未答完的题目！");
            return ;
        }
        var _tempStr = _temp.split("|");
        answers += _tempStr[0] + "|";
        scores += _tempStr[1] + "|";
    }
    if(!$("#r1s").hasClass("selected")){
        alert("您不是我们的定向及合格投资者，如有疑问请拨打客服电话：400-900-9630/400-099-0229");
        return false;
    }
    answers = answers.substring(0,answers.length-1);
    scores = scores.substring(0,scores.length-1);

    $("body").removeAttr("style");
    //关闭弹窗
    $('#fxinvest').modal({
        keyboard: false
    })

    $("#fxinvest").modal('hide');
    $(".modal-backdrop.fade.in").hide();


}

$(function() {
    $("#radio01").JQRadio({});
    //签署日期
    $("#nowdate").html(new Date().Format("yyyy-MM-dd"));

    // 回显默认答案
    var answer = "A|A|A|C|D|C";
    if(answer != null && answer != ""){
        var arr = answer.split("|");
        for(var i=0; i<arr.length; i++){
            $("#question" + (i+1) + " li").each(function(index, e){
                var name = $(this).attr("name");
                if(name.indexOf(arr[i]) > -1){
                    $(this).addClass("selected");
                }
            });
        }
    }

    // 判断选中情况
    $("#r22").click(function(){
        var _this = $(this);
        var _btn = $("#btn");
        if(_this.hasClass('selected')){
            _this.removeClass("selected");
            _btn.removeClass('tj_btn').addClass('tj_btn_gray');
        }else{
            _this.addClass("selected");
            _btn.removeClass('tj_btn_gray').addClass('tj_btn');
        }
    });


})

function load(){
    // 设置底部按钮高度
    var imgJsq1 = $(".img_jsq_1").height();
    $(".jsq-a").height(imgJsq1) + "px";
    $(".jsq-a").css('line-height', imgJsq1 + 'px');
}

$("#img_jsq").click(function() {
    //弹出框
    $(".jd_win").show();
    $(".inp-tel").focus();
})

$("#btn").click(function () {
    toEvalution();
    // $("#fxinvest").modal('hide');
    $("#registerbody").removeAttr("style");
    $("#registerbody").removeAttr("style"," ");
})
