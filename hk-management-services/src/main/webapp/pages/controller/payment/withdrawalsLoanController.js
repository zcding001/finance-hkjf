app.controller('withdrawsLoanCtrl',function ($scope,$injector,$timeout,$http,$location,$routeParams,$CommonService) {
	$scope.paymentVo = {};
	if($routeParams.id!='undefined' && $routeParams.id!=undefined){
		$scope.showPage=false;//用于判断是新增还是修改的标识 false:修改
		//根据ID，查询规则信息
		var params={'id':$routeParams.id};
		$CommonService.httpPost(params,CONFIG.interface.getPayRecord)
			.success(function(data) {
				$("#recordId").val($routeParams.id);
				$scope.paymentVo=angular.fromJson(data.resMsg);
				$scope.paymentVo.bankProvince=AREA_CONSTANT.getAreaName(data.resMsg.bankProvince);
				$scope.paymentVo.bankCity=AREA_CONSTANT.getAreaName(data.resMsg.bankCity);
				$scope.paymentVo.type = DIC_CONSTANT.getName("user", "type", data.resMsg.type);

			});
	}
	//表单提交事件
	var $validationProvider = $injector.get('$validation');
	$scope.submit = function(form){
		$validationProvider.validate(form).success(function(){
			var id = $("#recordId").val();
			var params={'ids':id};
			$CommonService.httpPost(params,CONFIG.interface.loanWithdrawals).success(function (data) {
				if(data.resStatus==CONFIG.CONSTANTS.SUCCESS_STATE){
                    commonUtil.createNotifyAndRedirect("审核成功!",ALERT_SUC, $location, "/searchWithdrawalsLoan");
				}
				else{
                    commonUtil.createSimpleNotify(data.resMsg);
				}
			}).error(function () {
                commonUtil.createNotifyAndRedirect("审核失败!",ALERT_ERR, $location, "/searchWithdrawalsLoan");
			});
		}).error(function(){
		});
	};

	$scope.back=function(){
		$location.url("/searchWithdrawalsLoan");
	}
	function _afterExecuteFn(data) {
        $("#modalText").modal('hide');
        if(commonUtil.isNotEmpty(data) && data.resStatus==CONFIG.CONSTANTS.SUCCESS_STATE){
            commonUtil.createNotifyAndRedirect("审核成功!",ALERT_SUC, $location, "/searchWithdrawalsLoan");
        }else {
            commonUtil.createNotifyAndRedirect(data.resMsg,ALERT_ERR, $location, "/searchWithdrawalsLoan");
        }
    }
	$scope.reject = function(){
        var content = '拒绝理由：<textArea name="rejectInfo"></textArea>';
        var idValue = $("#recordId").val();
        var url = CONFIG.interface.loanRejectWithdrawals;
        $("#ids").attr("name", "ids");
        $("#ids").val(idValue);
        commonUtil.createCustomConfirmBox('提现放款审核拒绝',content,url,null,_afterExecuteFn);
	}
});