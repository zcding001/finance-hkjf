app.controller('matchBidController',function ($scope,$timeout,$http,$CommonService,$routeParams,$location,$compile){
    // var selectMoney = 0.0;
    var matchingMoney;
	var params = {"bidId":$routeParams.bidId,"bidType":$routeParams.bidType};
	$CommonService.httpPost(params,CONFIG.interface.matchBidDetail)
	.success(function(data) {
		$scope.bidVo = data.resMsg;
		$scope.bidVo.loanUse = DIC_CONSTANT.getName("invest", "bid_loan_use", data.resMsg.loanUse);
		$scope.bidVo.bidScheme = DIC_CONSTANT.getName("invest", "bid_scheme", data.resMsg.bidScheme);
		$scope.bidVo.termUnit = DIC_CONSTANT.getName("invest", "bid_term_unit", data.resMsg.termUnit);
		$scope.bidVo.biddRepaymentWay = DIC_CONSTANT.getName("invest", "bid_repayment", data.resMsg.biddRepaymentWay);
		if($routeParams.bidType==1){
            $scope.matchShowName = '散标';
		}else{
            $scope.matchShowName = '优选';
		}
        var tableUrl = CONFIG.interface.miniMatchBidInfoList;
        var bidType = $("#bidType").val();
        if(bidType==''){
            bidType = commonUtil.getRequestParamByUrl().bidType;
        }
        if(bidType==1){//优选详情页，查询散标待匹配列表
            $("#matchHeader").html("散标待匹配列表");
            tableUrl = tableUrl + '?bidType=2&matchingMoney='+$scope.bidVo.matchingMoney;
        }
        if(bidType==2){//散标详情页，查询优选标待匹配列表
            $("#matchHeader").html("优选标待匹配列表");
            tableUrl = tableUrl + '?bidType=1';
        }
        matchingMoney = $scope.bidVo.matchingMoney;
        var columns =  [
            {
                title : "选择",
                className : "td-checkbox",
                orderable : false,
                width : "30px",
                data : null,
                render : function(data, type, row, meta) {
                    if (row.startTerm>row.endTerm || row.resiDays>3){
                        return '';
                    }
                    if($("#showSelect_"+row.id).length >0){
                        return '<input checked="checked" id="checkbox_'+row.id+'"onClick="changeChecked('+ row.id+',\''+row.title+'\',\''+row.matchingMoney+'\',\''+matchingMoney +'\')"  type="checkbox" class="iCheck" name="cb-children" value="' + row.id + '">';
                    }
                    var retHtml =  '<input id="checkbox_'+row.id+'"onClick="changeChecked('+ row.id+',\''+row.title+'\',\''+row.matchingMoney+'\',\''+matchingMoney +'\')"  type="checkbox" class="iCheck" name="cb-children" value="' + row.id + '">';
                    return retHtml;
                }
            },
            { title:"标的名称",data: "title", width:140,
                render: function (value, type, row, meta) {
                    return value;
                }
            },
            {
                title:"产品类型",
                data: "productType",
                className:"dt-simple",
                render: function (value, type, row, meta) {
                    return DIC_CONSTANT.getName("invest", "product_type", value);
                }
            },
            { title:"金额(元)",className:"dt-simple",data: "totalAmount"},
            { title:"年化率(%)",className:"dt-simple",data: "interestRate"},
            { title:"期限",data: "termValue",className:"dt-simple",
                render: function (value, type, row, meta) {
                    return value+DIC_CONSTANT.getName("invest", "bid_term_unit", row.termUnit);
                }
            },
            { title:"待匹配天数",data: null,className:"dt-simple",
                render: function (value, type, row, meta) {
                    return row.startTerm + "--"+row.endTerm;
                }
            },
            { title:"待匹配金额",data: "matchingMoney",className:"dt-simple"},
            { title:"待匹配情况",data: "resiDays",className:"dt-simple",
                render: function (value, type, row, meta) {
                    if (row.startTerm>row.endTerm){
                        return '不可匹配';
                    }
                    if (value === 0){
                        return "<span style='color: red'>今天匹配</span>";
                    }else if(value<0){
                        return "<span style='color: red'>已过期"+Math.abs(value)+"天</span>";
                    }else{
                        return "<span style='color: red'>"+value+"天后可以匹配</span>";
                    }
                }
            },
            { title:"首次匹配",data:"hasMatch",className:"dt-simple",render:function (value) {
                    return value === 0 ? "<span style='color: green'>是<span>" : "否";
                }},
        ];
        //创建dataTable列表
        dataTableUtil.createDTNoPageByList(tableUrl, columns);
        //初始化查询条件form
        dataTableUtil.initDTSearchForm();
        // var tableHtml = $("#mDataTable").html();
        // $("#mDataTable").html($compile(tableHtml)($scope));
	});
	
	$scope.doMatch = function(){
		var bidIds = $("#bidIds").val();
		if(bidIds==undefined||bidIds==''){
			var errorHtml = '<font color="red" id="bidErrorMsg">请选择需要匹配的标的</font>';
			$("#confirmShowSelectBid").html(errorHtml);
			return;
		}
		var params = {	"oneBidId":$("#bidId").val(),
						"moreBidIds":$("#bidIds").val(),
						"bidType":$("#bidType").val()
					 };
		$CommonService.httpPost(params,CONFIG.interface.doMatch)
		.success(function(data) {
			 if(data.resStatus==CONFIG.CONSTANTS.SUCCESS_STATE){
                 commonUtil.createNotifyAndRedirect("匹配成功!",ALERT_SUC, $location, "/matchBidList");
			 }else{
                 commonUtil.createSimpleNotify(data.resMsg,ALERT_ERR);
			 }
		}).error(function () {
            commonUtil.createSimpleNotify("匹配失败!",ALERT_ERR);
	    });
		
	}
	
	 //返回事件
	 $scope.back=function(){
         $location.path("/matchBidList");
	 }




    /**
     * 确认选择需要匹配的标的
     */
/*    $scope.confirmSelect=function(){
        if($("#bidType").val()==2){
            var selectMoney = $("#selectMonySum").val();
            //如果是一个散标匹配多个优选，选中的总金额 必须大于带匹配金额
            if(parseInt(selectMoney)< parseInt(matchingMoney)){
                $("#errorMsg").html('选择的优选金额必须不能小于散标待匹配金额');
                $('#'+cid).prop('checked',false);
                return;
            }
        }

        var ids = "";
        //获取所有勾选的标的id
        $('input:checkbox[name=cb-children]:checked').each(function(i){
            if(0==i){
                ids = $(this).val();
            }else{
                ids += (","+$(this).val());
            }
        });
        $("#bidIds").val(ids);
        $("#matchModal").modal('hide');
    }*/
    /**
     * 取消所有选中
     */
 /*   $scope.cancelSelect=function(){
        //获取所有勾选的标的id
        $('input:checkbox[name=cb-children]:checked').each(function(i){
            var showSelectId = "showSelect_"+$(this).val();
            var cid = "checkbox_" + $(this).val();
            var confirmShowId = 'confirmShowSelect_'+$(this).val();
            $('#'+showSelectId).remove();
            $('#'+cid).prop('checked',false);
            $("#"+confirmShowId).remove();
        });
        $("#bidIds").val('');
        // selectMoney = 0;
        $("#selectMonySum").val(0);
        $("#matchModal").modal('hide');
    }*/
});



/**
 * 选中或取消待匹配的标的
 */
function changeChecked(id,name,money,matchingMoney){
    var cid = "checkbox_" + id;
    var spanId = 'showSelect_'+id;
    var confirmShowId = 'confirmShowSelect_'+id;
    var selectMoney = $("#selectMonySum").val();
    if($('#'+cid).is(':checked')){//选中
        $("#errorMsg").empty();
        if($("#bidType").val()==1){
            //如果是一个优选匹配多个散标，每个散标的金额都不能大于优选
            if(parseInt(money)>parseInt(matchingMoney)){
                $("#errorMsg").html('散标金额不能大于优选金额');
                $('#'+cid).prop('checked',false);
                return;
            }
        }
        if(parseInt(selectMoney)>=parseInt(matchingMoney)){
            $("#errorMsg").html('选择标的金额已超过待匹配标的的待匹配金额');
            $('#'+cid).prop('checked',false);
            return;
        }
        selectMoney = parseInt(selectMoney)+parseInt(money);
        $("#selectMonySum").val(selectMoney);
        var apHtml = '<div class="showSelect" onClick="removeDiv(\''+id+'\','+money+');" id="'+spanId+'"><span>'+name+'('+money+')</span><i>x</i></div>';
        $("#showSelectBid").append(apHtml);

// 		var confirmHtml = '<div class="showSelect" onclick="removeDiv(\''+id+'\','+money+');" id="'+confirmShowId+'"><span>'+name+'('+money+')</span><i>x</i></div>';
        var confirmHtml = '<span id="'+confirmShowId+'">'+name+'('+money+')</span>';
        $("#confirmShowSelectBid").append(confirmHtml);
    }else{//取消
        $("#errorMsg").empty();
        $("#"+spanId).remove();
        $("#"+confirmShowId).remove();
        selectMoney = parseInt(selectMoney)- parseInt(money);
        $("#selectMonySum").val(selectMoney);
    };

}


/**
 * 删除已选中的标的，并取消勾选
 */
function removeDiv(id,money){
    var selectMoney = $("#selectMonySum").val();
    var showSelectId = "showSelect_"+id;
    $('#'+showSelectId).remove();
    var cid = "checkbox_" + id;
    $('#'+cid).prop('checked',false);
    var confirmShowId = 'confirmShowSelect_'+id;
    $("#"+confirmShowId).remove();
    selectMoney = parseInt(selectMoney)- parseInt(money);
    $("#selectMonySum").val(selectMoney);
}

/**
 * 取消所有选中
 */
function  cancelSelect(){
    //获取所有勾选的标的id
    $('input:checkbox[name=cb-children]:checked').each(function(i){
        var showSelectId = "showSelect_"+$(this).val();
        var cid = "checkbox_" + $(this).val();
        var confirmShowId = 'confirmShowSelect_'+$(this).val();
        //$('#'+showSelectId).remove();
        $('#'+cid).prop('checked',false);
        //$("#"+confirmShowId).remove();
    });
    $("#showSelectBid").html("");
    $("#confirmShowSelectBid").html("");
    $("#bidIds").val('');
    // selectMoney = 0;
    $("#selectMonySum").val(0);
    $("#matchModal").modal('hide');
}

/**
 * 确认选择需要匹配的标的
 */
function confirmSelect(){
    if($("#bidType").val()==2){
        var selectMoney = $("#selectMonySum").val();
        var matchingMoney=$("#matchingMoney").val();
        //如果是一个散标匹配多个优选，选中的总金额 必须大于带匹配金额
        if(parseInt(selectMoney)< parseInt(matchingMoney)){
            $("#errorMsg").html('选择的优选金额必须不能小于散标待匹配金额');
            $('#'+cid).prop('checked',false);
            return;
        }
    }

    var ids = "";
    //获取所有勾选的标的id
    $('input:checkbox[name=cb-children]:checked').each(function(i){
        if(0==i){
            ids = $(this).val();
        }else{
            ids += (","+$(this).val());
        }
    });
    $("#bidIds").val(ids);
    $("#matchModal").modal('hide');
}