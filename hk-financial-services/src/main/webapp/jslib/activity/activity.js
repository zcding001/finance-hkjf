$(document).ready(function(){
    	ajaxUtil.post("/recommendController/toRecommend.do", null , function(data){
    		$("#earnMoney").html(moneyUtil.round(data.params.sumEarnAmount.sumEarnAmount, 2));
    		$("#recommend_link").val( data.params.basePath + "register/register.html?recno="+ data.params.recommendNo);
    		$(".hytj_input2").html(data.params.recommendNo);
    		$('.hytj_copy2 img').attr("src","${oss.url}"+data.params.recUrl);
    		var recom = JSON.parse(data.params.recommendRule.content);
    		var qdz = JSON.parse(data.params.qdzRule.content);
    		$("#qdzRate").html(qdz.rebatesRateOne+"%");
    		$("#qdzRate1").html(qdz.rebatesRateOne+"%");
    		var recomDate = new Date(dateUtil.date(data.params.recommendRule.beginTime));
    		$("#recomDate").html(recomDate.getFullYear() + "年" + (recomDate.getMonth() + 1) +"月"+ recomDate.getDate()+"日");
    		$("#recomRate").html(recom.friendLevelOne[0].rate +"%");
    		$("#firstInvest").html(recom.friendLevelOne[0].rate);
    		$("#qdzInvest").html(qdz.rebatesRateOne);
    		$("#bidRate1").html(recom.friendLevelOne[0].rate);
    		$("#bidMoney").html(recom.friendLevelOne[0].rate * 10000 /100);
    		$("#qdzRate1").html(qdz.rebatesRateOne);
    		$("#qdzRate2").html(qdz.rebatesRateOne);
    	},"json",false);
   
    //复制链接
    $("#copy").click(function() {
		var clipboard = new Clipboard('#copy');
		clipboard.on('success', function(e) {
		    console.log(e);
		});
		clipboard.on('error', function(e) {
		    console.log(e);
		});
	});
   
});

