$(document).ready(function(){
	var reqData ={position : 1}
	ajaxUtil.post("/informationController/findInfomations.do",reqData,function(data){
		/**
		 * banner图渲染
		 */
		$('#carousel-example-generic').carousel();

		$(".carousel-inner").empty();
		$(".carousel-indicators").empty();
		if(!validUtil.validNotEmpty(data.resMsg.carsouleFigureList)){
			$(".carousel-inner").append('<div class="item active item_width" style="z-index: 10;"><img src="src/img/index/banner-pc.jpg" alt="鸿坤金服banner图" style="height:450px;"></div>');
            $(".carousel-indicators").append('<li data-target="#carousel-example-generic" data-slide-to="' + i + '" class="active"></li>');
		}else{
	        for (var i = 0; i <= eval(data.resMsg.carsouleFigureList).length - 1; i++) {
	        	if(i == 0){
		            $(".carousel-inner").append('<div class="item active item_width" style="z-index: 10;"><a href="' + eval(data.resMsg.carsouleFigureList)[i].url + '">' + eval(data.resMsg.carsouleFigureList)[i].content + '</a></div>');
		            $(".carousel-indicators").append('<li data-target="#carousel-example-generic" data-slide-to="' + i + '" class="active"></li>');
	        	}else{
		            $(".carousel-inner").append('<div class="item item_width" style="z-index: 10;"><a href="' + eval(data.resMsg.carsouleFigureList)[i].url + '">' + eval(data.resMsg.carsouleFigureList)[i].content + '</a></div>');
		            $(".carousel-indicators").append('<li data-target="#carousel-example-generic" data-slide-to="' + i + '"></li>');
	        	}
	        }
	        $(".carousel-inner div img").css("height","450px");
		}
		
		/**
		 * 1、公告渲染
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
		/**
		 * 2、公司动态渲染
		 */
		$("#companyDynamic").empty();
		if(!validUtil.validNotEmpty(data.resMsg.dynamicList)){
			$("#companyDynamic").append('<ul class="zxdt_list"><li class="clearfix">暂无公司动态</li></ul>');
		}else{
			$("#companyDynamic").append('<ul class="zxdt_list"></ul>');
	        for (var i = 0; i <= eval(data.resMsg.dynamicList).length - 1; i++) {
	            $("#companyDynamic ul").append('<li class="clearfix"><a class="fl " href="common/noticeDetail.html?id=' + eval(data.resMsg.dynamicList)[i].id +'">' + eval(data.resMsg.dynamicList)[i].title + '</a>' + '<span class="fr " style="color:#999; ">' + dateUtil.date(eval(data.resMsg.dynamicList)[i].createTime) + '</span></li>')
	        }
		}
		/**
		 * 3、媒体报道渲染
		 */
		$("#mediaReport").empty();
		if(!validUtil.validNotEmpty(data.resMsg.mediaReportList)){
			$("#mediaReport").append('<ul class="zxdt_list"><li class="clearfix">暂无媒体报道</li></ul>');
		}else{
			$("#mediaReport").append('<ul class="zxdt_list"></ul>');
	        for (var i = 0; i <= eval(data.resMsg.mediaReportList).length - 1; i++) {
	            $("#mediaReport ul").append('<li class="clearfix"><a class="fl " href="common/noticeDetail.html?id=' + eval(data.resMsg.mediaReportList)[i].id +'">' + eval(data.resMsg.mediaReportList)[i].title + '</a>' + '<span class="fr " style="color:#999; ">' + dateUtil.date(eval(data.resMsg.mediaReportList)[i].createTime) + '</span></li>')
	        }
		}

	});
},"json",false);