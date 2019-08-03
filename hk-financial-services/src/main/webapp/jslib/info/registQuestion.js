
// JavaScript Document
/*问题数据绑定*/
var unchoose = 0;
var isExistAnswer=0;
var informationId = 0;

var danxuandiv=document.getElementsByClassName('danxuandiv')[0]
var danxuanann=document.getElementsByClassName('danxuanann')[0]
danxuandiv.onclick=function(){//单选框选择操作
    danxuanann.classList.toggle('danxuanxz')
    if(hasclass()){
        $(".sure_btna").unbind("click").css('background','linear-gradient(to right, #ffb400, #f39200)');
    }else{
        $(".sure_btna").unbind("click").css('background','#999');
    }
}

function hasclass(){//检查默认是否选择,return true or false
    return danxuanann.classList.contains('danxuanxz')
}

/**
 * 定向投资测评
 */
$("#toFxinvest").unbind("click").click(function () {
    searchInfomation();
    $("#fxinvest").modal('show');
});
/**
 * 查询注册调查问卷
 */
function searchInfomation() {
    var reqData = {type : 7, channel : 1, position : 7};
    ajaxUtil.post("informationController/searchInfomation.do",reqData,
        function (data) {
            informationId = data.resMsg[0].id;
            findQuestionnaireInfo(informationId);
        },"json",false);
}


/**
 * 查询调查问卷问题
 * @param informationId
 * @param regUserId
 */
function findQuestionnaireInfo(informationId){
    var reqData = {infomationId : informationId};
    ajaxUtil.post("informationController/findQuestionnaireInfo.do",reqData,function(data){
        if(data.resStatus == CONSTANTS.SUCCESS){
            parseDate(data.params);
            defaultAnswer();
        }else{
            dialogUtil.alert("",data.resMsg);
        }

    },"json",false);
};
function parseDate(json){
    var html_title = "<div class='question_title'><div class='case_title'>"+
        json.diaochaContent+"</div></div>";
    var html_introduction = "<div style='font-size: 14px;line-height: 24px;text-indent: 28px;'>本问卷旨在了解定向及合格投资者（合作物业员工或业主）可承受的风险程度等情况，借此协助您选择合适的产品或服务类别，以符合您的风险承受能力, 其目的是使本公司所提供的产品或服务与您的风险承受能力等级相匹配。</div>"
    +"<div style='font-size: 14px;line-height: 24px;text-indent: 28px;margin-top: 10px;'>本公司在此承诺，对于您在本问卷中所提供的一切信息，本公司将严格按照法律法规要求承担保密义务。本公司保证不会将涉及您的任何信息提供、泄露给任何第三方，或者将相关信息用于违法、不当用途。</div>"
    var problems = json.problems;
    var answerUser = '';
    var html_all = '';
    for(var i = 0 ; i < problems.length ; i++){
        var problem = problems[i];
        answerUser = problem.answerUser;
        var problemConten = (i+1) + ":" + problem.problemContent;
        var html_p = "<p>" + problemConten + "</p>";

        var type = problem.problemAnswerType;
        var typeR = '';
        if(type == '1'){
            typeR = 'radio';
        }else if(type == '2'){
            typeR = 'checkbox';
        }else{
            typeR = 'input';
        }
        var html_ul = "<div class='question_chose'><input  type='hidden' name='problemId' value='"+ problem.problemId +"'>"+
            "<input type='hidden' name='answerId' value='"+ problem.answerId +"'>"+
            "<input type='hidden' name='problemType' value='"+ problem.problemAnswerType +"'>";
        var answer = problem.answer;
        if(type != 3){
            html_ul += "<ul class='xh'>";
            var options = problem.options;
            for(var j = 0 ; j < options.length ; j++){
                var option = options[j];
                var content = option.optionContent;
                var title = option.optionTitle;
                var checked = '';
                if(type == 1 && answer == option.optionId){//单选
                    checked = " checked='checked' ";
                }
                if(answer.length >0 && type == 2){//多选
                    var checkanswers = answer.split('-');
                    for(var k = 0 ; k<checkanswers.length ; k++){
                        if(option.optionId == checkanswers[k]){
                            checked = " checked='checked' ";
                        }
                    }
                }
                var id_name=typeR +"_"+i+"_"+j +'A';
                html_ul +=
                    ("<li><input class='radio' id='"+id_name +"' name='"+ typeR + i +"A' class='"+
                        typeR +"_chose' type='"+
                        typeR +"' value='"+ option.optionId +"' "+ checked +" style='float:left;margin-top:0;'><lable class='radio' onClick="+"selQ('"+typeR+"','"+id_name+"')"+">"+
                        title +":"+
                        content +"</lable></li>");
            }
            html_ul += "</ul>";
        }else{
            html_ul += "<input type='text' maxlength='255' name='answerText' value='"+ problem.answer +"' style='width:92%;height:2em;line-height:2em;padding:0 0.5em;'>";
        }
        html_ul += "</div>";
        html_all += (html_p+html_ul);
    }
    var html_user = "<input type='hidden' id='answerUser' value='"+ answerUser +"'>";
    $('.question_in').html(html_user+html_title+html_introduction+html_all);
    $("#nowdate").html(dateUtil.date(new Date()));
}
function submitA(){
    // var reqData = {infomationId : informationId, regUserId : regUserId};
    // ajaxUtil.post("/informationController/findExistQuestion.do",reqData,function(data){
    //     if(data.resStatus == CONSTANTS.SUCCESS){
    //         submitForm();
    //     }else{
    //         dialogUtil.alert("","不能重复提交");
    //     }
    // },"json",false);
}

function saveAnswer(wjJSON) {
    var reqData = {questionnaireAnswer : JSON.stringify(wjJSON)};
    ajaxUtil.post("/informationController/saveInfoQuestionnaireAnswer.do",reqData,function(data){
        if(data.resStatus == CONSTANTS.SUCCESS){
            dialogUtil.alert("","保存成功！",function () {
                location.reload();
            });
        }else{
            dialogUtil.alert("",data.resMsg);
        }
    },"json",false);
}

function submitForm(){
    var wjJSON = checkwj();
    if(unchoose == 1){
        dialogUtil.alert("","您有未答完的题目！");
        return;
    }

    //未勾选同意
    if(!hasclass()){
        return ;
    }

    containerUtil.set(CONSTANTS.INVEST_ANSWER,wjJSON);
    $("#fxinvest").modal('hide');
}
function checkwj(){
    //拼接字符串
    unchoose = 0;
    var renturntt = {};
    var questions = $('.question_chose');
    var answerUser = $('#answerUser').val();
    var problems = new Array(questions.length);
    var ans;
    for(var i = 0 ; i<questions.length ; i++){
        ans = {};
        //answerid
        var answerid = $(questions[i]).children("input[name='answerId']").val();
        //problemid
        var problemId = $(questions[i]).children("input[name='problemId']").val();
        //answer
        var answer = '';
        var problemType = $(questions[i]).children("input[name='problemType']").val();
        if(problemType == 1){//radio
            var check = $(questions[i]).find("input[type='radio']:checked");
            if(check.length > 0){
                answer = $(questions[i]).find("input[type='radio']:checked").val();
            }
        }
        if(problemType == 2){//check   [A-B-C-]
            var check = $(questions[i]).find("input[type='checkbox']:checked");
            for(var j = 0 ; j<check.length ; j++){
                answer += $(check[j]).val() + "-";
            }
        }
        if(problemType == 3){//txt
            answer = $(questions[i]).find("input[name='answerText']").val();
        }
        ans.answer = answer;
        if(answer!=''){
            isExistAnswer = 1;
        }
        if(answer == ''){
            unchoose = 1;
        }
        ans.source = $("#channel").val();
        ans.infoQuestionnaireId = problemId;
        problems[i] = ans;
    }
    return problems;
}
function showwindow(val){
    $("#alertMsg").html(val);
    $(".floor").show();
}
function toiOS(){
    $("#alertMsg").html('');
    $(".floor").hide();
}
function selQ(type,id){
    if($('#'+id).attr('checked')){
        $("[id='"+id+"']").removeAttr("checked");//全选
    }else{
        $("[id='"+id+"']").attr("checked",'true');//全选
    }
}

/**
 * 默认勾选答案
 */
function defaultAnswer() {
    var answers = CONSTANTS.DEFAULT_ANSWER.split(',');
    for (var i = 0; i < answers.length; i++) {
        var htmlName = "radio" + i + "A";
        $(".question_chose:eq("+i+")").find("input[name="+htmlName+"]").eq(answers[i]).click();
    }
}

/**
 * 判断答题分数是否合格
 */
function checkAnswer() {
    
}
