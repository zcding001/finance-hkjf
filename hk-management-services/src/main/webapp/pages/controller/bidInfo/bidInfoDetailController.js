app.controller('bidInfoDeatil',function ($scope,$timeout,$http,$CommonService,$compile,$location){
	$("#leftMenu").empty();
	var params = {"bidInfoId":$location.search().param};
		$CommonService.httpPost(params,CONFIG.interface.findBidInfoDetails)
		.success(function(data) {
			$scope.bidVo = data.resMsg;
			$scope.bidVo.showPosition = DIC_CONSTANT.getNames("invest", "bid_show_position", data.resMsg.showPosition);
			$scope.bidVo.investPosition = DIC_CONSTANT.getNames("invest", "bid_invest_position", data.resMsg.investPosition);
			$scope.bidVo.assureType = DIC_CONSTANT.getNames("invest", "bid_assure_type", data.resMsg.assureType);
			$scope.bidVo.termUnit = DIC_CONSTANT.getName("invest", "bid_term_unit", data.resMsg.termUnit);
			$scope.bidVo.biddRepaymentWay = DIC_CONSTANT.getName("invest", "bid_repayment", data.resMsg.biddRepaymentWay);
			$scope.bidVo.loanUse = DIC_CONSTANT.getName("invest", "bid_loan_use", data.resMsg.loanUse);
            $scope.bidVo.bidScheme = data.resMsg.bidScheme;
            $scope.bidVo.bidSchemeValue = data.resMsg.bidSchemeValue;
            $scope.bidVo.stepValue = data.resMsg.stepValue;
			$scope.bidVo.type = DIC_CONSTANT.getName("invest", "bid_type", data.resMsg.type);
			$scope.bidVo.punishState = data.resMsg.punishState==1?'是':'否';
			$scope.bidVo.bourseFlg = data.resMsg.punishState==1?'是':'否';
			if(data.resMsg.creditorState==1){
                $scope.bidVo.creditorState='允许'
            }else if(data.resMsg.creditorState==0){
                $scope.bidVo.creditorState='不允许'
			}else{
                $scope.bidVo.creditorState='允许部分转让'
			}
			$scope.bidVo.advanceRepayState = data.resMsg.advanceRepayState==1?'是':'否';
			$scope.bidVo.returnCapWay = data.resMsg.returnCapWay==1?'按日计息':'按月计息';
			$scope.bidVo.givingPointState = data.resMsg.givingPointState==1?'是':'否';
			$scope.bidVo.recommendState = data.resMsg.recommendState==1?'是':'否';
			$scope.bidVo.allowCoupon = data.resMsg.allowCoupon;
			$scope.bidVo.withholdState = DIC_CONSTANT.getName("invest", "bid_withhold_state", data.resMsg.withholdState);
            $scope.bidVo.matchType = data.resMsg.matchType==0?'直投':'匹配';
			if(data.resMsg.printImgurl != ''){
				$scope.bidVo.printImgurl = CONFIG.CONSTANTS.IMG_URL_ROOT + data.resMsg.printImgurl;
				$("#Pic_printImgurl").displayBack("Pic_printImgurl", {}, $scope.bidVo.printImgurl);
			}
			if(data.resMsg.imgUrl != ''){
				$scope.bidVo.imgUrl = CONFIG.CONSTANTS.IMG_URL_ROOT + data.resMsg.imgUrl;
				$("#Pic_imgUrl").displayBack("Pic_imgUrl", {}, $scope.bidVo.imgUrl);
			}
		});
});

    



