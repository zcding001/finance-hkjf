 
// JavaScript Document
/*问题数据绑定*/
var isExistAnswer=0;
function getdata(id,usercode,channel,url){
	$("#channel").val(channel);
	$("#infomationId").val(id);
	$("#regUserId").val(usercode);
		$.ajax({  
	        url :url+ '/informationController/findQuestionnaireInfo?infomationId='+id+'&regUserId='+usercode,
	        async : false, // 注意此处需要同步，因为返回完数据后，下面才能让结果的第一条selected  
	        type : "POST",  
	        dataType : "json",  
	        success : function(data) {  
	        	if(data.resStatus == 1000){
	        		parseDate(data.params);	        		
	        	}else{
	        		showwindow(data.resMsg);
	        	}
	        }  
	    }); 
}
function parseDate(json){
			   var html_title = "<div class='question_title'><div class='case_title'>"+
								json.diaochaContent+"</div></div>";
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
							typeR +"' value='"+ option.optionId +"' "+ checked +" style='float:left;margin-top:0.2em;'><lable class='radio' onClick="+"selQ('"+typeR+"','"+id_name+"')"+">"+ 
							title +":"+ 
							content +"</lable></li>");
					   }
					   html_ul += "</ul>";
					}else{
						html_ul += "<input type='text' maxlength='255' name='answerText' value='"+ problem.answer +"' style='width:92%;height:2em;line-height:2em;'>";
					}
					html_ul += "</div>";
					html_all += (html_p+html_ul);
			   }
			   var html_user = "<input type='hidden' id='answerUser' value='"+ answerUser +"'>";
			   $('.question_in').html(html_user+html_title+html_all);
}
function submitA(url){
	var id = $("#infomationId").val();
	var userCode = $("#regUserId").val();
	$.ajax({  
        url 		: url+'/informationController/findExistQuestion?infomationId='+id+'&regUserId='+userCode,
        async 		: false,
        type 		: "POST",
        dataType 	: "json",  
        success 	: function(data) {  
        				if(data.resStatus == 1000){
        					submitForm(url);
        				}else{
        					showwindow("不能重复提交");        					
        				}
        			}  
    }); 
	
}

function submitForm(url){
	var wjJSON = checkwj();
	var userCode = $("#regUserId").val();
	if(isExistAnswer==0){
		showwindow("至少填写一项答案");        	
		return;
	}
	$.ajax({  
        url 		: url+'/informationController/saveInfoQuestionnaireAnswer',
        async 		: false,
        type 		: "POST",
        data 		: {'questionnaireAnswer':JSON.stringify(wjJSON),'regUserId':userCode},
        dataType 	: "json",  
        success 	: function(data) {  
        				if(data.resStatus == 1000){
        					showwindow("保存成功");
        				}else{
        					showwindow(data.resMsg);        					
        				}
        			}  
    }); 
}
function checkwj(){
	//拼接字符串
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