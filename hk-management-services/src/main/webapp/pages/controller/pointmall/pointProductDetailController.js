app.controller('pointProDeatils',function ($scope,$timeout,$http,$CommonService,$compile,$location){
	$("#leftMenu").empty();
	var params = {"id":$location.search().param};
		$CommonService.httpPost(params,CONFIG.interface.pointProductInfo)
		.success(function(data) {
			$scope.product = data.resMsg;
			$scope.product.sendWay = DIC_CONSTANT.getName("point_product", "send_way", data.resMsg.sendWay);
			$scope.product.flashSales=data.resMsg.flashSale;
			$scope.product.iosShow = data.resMsg.iosShow==1?'是':'否';
			$scope.product.recommend = data.resMsg.recommend==1?'是':'否';
			$scope.product.flashSale = data.resMsg.flashSale==1?'是':'否';
			$("#goodsDetails").append($scope.product.goodsDetails);
			$("#prductInfo").append($scope.product.prductInfo);
			$scope.product.showTimeStart = dateUtil.dateTime(data.resMsg.showTimeStart);
	        $scope.product.showTimeEnd = dateUtil.dateTime(data.resMsg.showTimeEnd);
			if(data.resMsg.imgs != ''){
				var str = data.resMsg.imgs;
				var images ="<img src='"+ CONFIG.CONSTANTS.IMG_URL_ROOT + data.resMsg.firstImg+"' style='width:100px;height:100px;'/>&nbsp;&nbsp;&nbsp;&nbsp;";
				for(var i=0;i<str.length;i++){
					images = images + "<img src='"+ CONFIG.CONSTANTS.IMG_URL_ROOT + str[i]+"' style='width:100px;height:100px;'/>&nbsp;&nbsp;&nbsp;&nbsp;";
				}
				$("#images").html(images);
			}
			
		});
});

    



