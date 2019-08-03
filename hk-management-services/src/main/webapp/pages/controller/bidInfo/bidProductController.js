app.controller('bidProduct', function ($scope,$injector,$timeout,$http,$filter,$window,$checkBoxService,$location,$CommonService,$routeParams) {
    $scope.data = {};
	$scope.detail = {};
    $scope.types = DIC_CONSTANT.getValueAndName("invest", "product_type");
    $scope.termUnits = DIC_CONSTANT.getValueAndName("invest", "bid_term_unit");
	$scope.repaymentways = DIC_CONSTANT.getValueAndName("invest", "bid_repayment");//还款方式
	$scope.contracts = CONTRACTTYPE_CONSTANT.getData(1);
	$scope.data.bidScheme = 1;
	$scope.data.repaymentway = 1;

	//预编辑或查看产品详情操作
	if($routeParams.id != undefined && $routeParams.id > 0){
		$http.post(CONFIG.interface.preUpdateBidProduct, {id:$routeParams.id}).success(function (data) {
			$scope.data = data.resMsg;
			if($routeParams.ope == "detail"){//查看详情
				$scope.detail = data.resMsg;
				$scope.detail.typeValue = DIC_CONSTANT.getName("invest", "product_type", $scope.detail.type);
				$scope.detail.amount = commonUtil.toFormatFixed($scope.detail.amountMin )+ "-" +commonUtil.toFormatFixed($scope.detail.amountMax) ;
				$scope.detail.rate = $scope.detail.rateMin + "-" + $scope.detail.rateMax;
                var unitName=DIC_CONSTANT.getName('invest','bid_term_unit',$scope.detail.termUnit);
				$scope.detail.timeLimitInfo = $scope.detail.termValueMin + "-" + $scope.detail.termValueMax +unitName;
				//翻译还款方式数据
				$scope.detail.repaymentway = DIC_CONSTANT.getNames("invest", "bid_repayment", $scope.detail.repaymentway," & ");
				$scope.detail.bidSchemeText = $scope.detail.bidScheme == 1 ? "最低招标方案" : "平均金额招标";
				//格式化招标方案值
                $scope.detail.bidSchemeValue = commonUtil.toFormatFixed($scope.detail.bidSchemeValue);
				//翻译合同数据
				var contractText = "";
				var arr = $scope.detail.contract.split(",");
				for(var i in arr){
					contractText += CONTRACTTYPE_CONSTANT.getName(arr[i])+' & ';
				}
				$scope.detail.contractText = contractText.substr(0,contractText.length-2);
				return;
			}else{
				//处理合同回显
				$checkBoxService.showCheckInfo($scope.contracts,$scope.data.contract,'type');
				//处理还款方式回显
                $checkBoxService.showCheckInfo($scope.repaymentways,$scope.data.repaymentway);

			}
		}).error(function () {});
	}

    //招标方案改变事件
	$scope.bidSchemeChg = function(){
		if($scope.data.bidScheme == 0){
            $("#bidSchemeValueLabel").text("平均每份");
        }else{
            $("#bidSchemeValueLabel").text("最低投标金额");
		}
	}


	//添加或者修改标的产品表单提交事件
	var $validationProvider = $injector.get('$validation');
	$scope.submit = function(form){
		//决定是更新或者修改操作
		var postUrl=CONFIG.interface.updateBidProduct;
		if($scope.data.id==undefined||$scope.data.id==null){
            postUrl= CONFIG.interface.addBidProduct
		}
		//拼接还款方式
		$scope.data.repaymentway = $checkBoxService.check($scope.repaymentways);
		//拼接合同
		$scope.data.contract = $checkBoxService.check($scope.contracts,'type');
        if(commonUtil.isEmpty($scope.data.contract)){
            commonUtil.createSimpleNotify("未选择合同!", ALERT_ERR);
            return;
		}
        delete $scope.data.createTime;
        delete $scope.data.modifyTime;
		$validationProvider.validate(form).success(function(){
			$http.post(postUrl, $scope.data).success(function (data) {
				if(data.resStatus==CONFIG.CONSTANTS.SUCCESS_STATE){
                    commonUtil.createNotifyAndRedirect("操作成功!",ALERT_SUC, $location, "/bidProductList");
				}else{
                    commonUtil.createSimpleNotify(data.resMsg, ALERT_ERR);
				}
			}).error(function () {
                commonUtil.createSimpleNotify(data.resMsg, ALERT_ERR);
			});
		}).error(function(){C
			//do nothing
		});
	};
});

