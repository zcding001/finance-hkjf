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

	});
},"json",false);