var recommendController = {};
(function(){
    'use strict';
    
    //立即推荐
    recommendController.atOnceRecommend = function(){
    	//commonUtil.getRequestUrl();
    	ajaxUtil.post("/recommendController/toRecommend.do", null , function(data){
    		$("#earnMoney").html(moneyUtil.round(data.params.sumEarnAmount.sumEarnAmount, 2));
    		$("#recommend_link").val( data.params.basePath + "register/register.html?recno="+ data.params.recommendNo);
    		$(".hytj_input2").val(data.params.recommendNo);
    		$('.hytj_copy2 img').attr("src","${oss.url}"+data.params.recUrl);
    		var recom = JSON.parse(data.params.recommendRule.content);
    		var qdz = JSON.parse(data.params.qdzRule.content);
    		$("#qdzRate").html(qdz.rebatesRateOne+"%");
    		$("#qdzRate1").html(qdz.rebatesRateOne+"%");
    		var recomDate = new Date(dateUtil.date(data.params.recommendRule.beginTime));
    		$("#recomDate").html(recomDate.getFullYear() + "年" + (recomDate.getMonth() + 1) +"月"+ recomDate.getDate()+"日");
    		$("#recomRate").html(recom.friendLevelOne[0].rate +"%");
    	},"json",false);
    };
    //我的推荐好友
    recommendController.myRecommendFriend = function(){
    	var params ={grade :$("#friendLevel").val() ,realName : $("#referraName").val()};
    	var result = pageUtil.initPaging("/recommendController/findMyRecommendFriends.do", params, _renderRecommendPage, 1,"recommendList_paging");
         _renderRecommendPage(result);
    };
    //推荐奖金明细
    recommendController.earnInfo = function(){
    	var params ={};
    	var result = pageUtil.initPaging("/recommendController/findMyRecommendEarn.do", params, _renderRecommendEarnPage, 1,"recommendEarnList_paging");
         _renderRecommendEarnPage(result);
    };
    //我的好友点击查询按钮事件
    $(".from_but").click(function() {
		recommendController.myRecommendFriend();
	});
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
    //渲染我的推荐好友页面
    function _renderRecommendPage(data) {
    	$(".hy_table tbody").empty();
    	$(".noMes").remove();
    	$(".hy_table tbody").append('<tr><th width="25%">好友关系</th><th width="25%">姓名</th><th width="25%" height="">手机号</th><th width="25%">推荐时间</th></tr>');
		if (data == undefined || data == null || data.resMsg==null || data.resMsg.data == null)  {
	    	$(".hy_table").after("<div class='noMes' width='100%' style='text-align:center; height: 80px;line-height: 80px;border: 1px solid #ccc;border-top: none;'>" + "暂无记录" + "</div>");
            return;
        }else{
			for (var i = 0 ;i<data.resMsg.data.length; i++){
				$(".hy_table tbody").append('<tr><td width="25%">' + dictionaryUtil.getName("user","friend_level",data.resMsg.data[i].grade) + '</td><td width="25%">' +  data.resMsg.data[i].realName + '</td><td width="25%" height="">' + data.resMsg.data[i].tel + '</td><td width="25%">' + dateUtil.dateTime(data.resMsg.data[i].createTime) + '</td></tr>');
			}
        }
    }
    //渲染我的奖金明细
    function _renderRecommendEarnPage(data){
    	$("#recommendEarn tbody").empty();
    	$(".noMes").remove();
    	$("#recommendEarn tbody").append('<tr><th>投资人</th><th>奖励时间</th><th>奖励金额（元）</th></tr>');
    	if (data == undefined || data == null || data.resMsg==null || data.resMsg.data == null)  {
        	$("#recommendEarn").after("<div class='noMes' width='100%' style='text-align:center; height: 80px;line-height: 80px;border: 1px solid #ccc;border-top: none;'>" + "暂无记录" + "</div>");
            return;
        }else{
			for (var i = 0 ;i < data.resMsg.data.length; i++){
				$("#recommendEarn tbody").append('<tr><td>' + data.resMsg.data[i].referraName + '</td><td>' + dateUtil.dateTime(data.resMsg.data[i].createTime) + '</td><td>' + moneyUtil.round(data.resMsg.data[i].earnAmount,2) + '</td></tr>');
			}
        }
    }
   
})();

