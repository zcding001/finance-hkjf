$(document).ready(function(){
	ajaxUtil.post("/informationController/searchInfomationsById.do",{id : commonUtil.getUrlParam("id")},function(data){
		$(".p_news_con").empty();
		if(!validUtil.validNotEmpty(data.resMsg)){
			$(".p_news_con").append("<div class='p_news_t'>" + '暂无信息' + "</div>");
		}else{
			$("#positions").html(dictionaryUtil.getName("info","info_type", data.resMsg.type ));
			$("#positions").attr("href",commonUtil.getRequestUrl("/common/noticeList.html")+"?type=" + data.resMsg.type + "&position=" + data.resMsg.position);
			$("#back").attr("href",commonUtil.getRequestUrl("/common/noticeList.html")+"?type=" + data.resMsg.type +"&position=" + data.resMsg.position);
			$(".p_news_con").append("<div class='p_news_t'>" + data.resMsg.title + "</div>");
	        $(".p_news_con").append("<p class='p_news_fb'>发布时间：" + dateUtil.date(data.resMsg.createTime) + "</p>");
	        $(".p_news_con").append("<div class='pnews_p'>" + data.resMsg.content + "</div>");
		}
	});
});