$(document).ready(function(){
	var reqData ={type : 4, channel : 1, position : 1};
	ajaxUtil.post("/informationController/searchInfomation.do",reqData,function(data){
		$(".footer_bot_box p").empty();
		if(!validUtil.validNotEmpty(data.resMsg)){
			$(".footer_bot_box p").append('友情链接：暂无链接');
		}else{
			$(".footer_bot_box p").append('友情链接：  ');
	        for (var i = 0; i <= eval(data.resMsg).length - 1; i++) {
	        	$(".footer_bot_box p").append('<a href="'+ data.resMsg[i].url +'" target="_blank">'+ data.resMsg[i].title +'</a>');
	        }
		}
	});
});