$(document).ready(function(){
	var reqData ={position : 1}
	ajaxUtil.post("/informationController/findInfomations.do",reqData,function(data){
		/**
		 * 公告渲染
		 */

		$("#gg_scroll").empty();
		if(!validUtil.validNotEmpty(data.resMsg.noticeList)){
			$("#gg_scroll").append("<ul><li>暂无公告</li></ul>");
		}else{
			$("#gg_scroll").append("<ul></ul>");
	        for (var i = 0; i <= eval(data.resMsg.noticeList).length - 1; i++) {
	            $("#gg_scroll ul").append("<li><a href='common/noticeDetail.html?id=" + eval(data.resMsg.noticeList)[i].id +"'>" + (i + 1) + '、'+ eval(data.resMsg.noticeList)[i].title + "</a></li>")
	        }
		}

	});
},"json",false);