var experienceGoldController = {};
(function(){
    'use strict';
    //初始化体验金记录
    experienceGoldController.simGoldList = function(){
    		var params ={id :$("#flowId").val() ,state : $("#state").val() ,createTimeBegin : $("#createTimeBegin").val() , createTimeEnd : $("#createTimeEnd").val()};
        	var result = pageUtil.initPaging("/experienceGoldController/findSimRecord.do", params, _renderSimGoldPage, 1,"simGoldList_paging");
        	_renderSimGoldPage(result);
    };
    //渲染我的体验金记录
    function _renderSimGoldPage(data){
    	$("#simgoldlist tbody").empty();
    	$(".noMes").remove();
    	if (data == undefined || data == null || data.resMsg==null || data.resMsg.data == null)  {
        	$("#simgoldlist tbody").append("<tr><td class='noMes' colspan='5' style='text-align:center;border-bottom: 1px solid #ccc;'>" + "暂无记录" + "</td></tr>");
            return;
        }else{
			for (var i = 0 ;i<data.resMsg.data.length; i++){
				$("#simgoldlist tbody").append('<tr><td>' + data.resMsg.data[i].id + '</td><td>' + moneyUtil.round(data.resMsg.data[i].money,2) + '</td><td>' + dateUtil.dateTime(data.resMsg.data[i].expireTime) + '</td><td>' + dictionaryUtil.getName("vas", "sim_state",data.resMsg.data[i].state) + '</td><td>' + dateUtil.dateTime(data.resMsg.data[i].createTime) + '</td></tr>');
			}
        }
    }
    //搜索事件
    $("#search").click(function(){
    	experienceGoldController.simGoldList();
    });
   
})();

