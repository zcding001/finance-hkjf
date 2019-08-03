app.controller('simgoldCtrl',function ($scope,$injector,$http,$location,$routeParams,$CommonService,$rootScope,$checkBoxService){
	 $routeParams.id = $routeParams.param;
	 //将体验金状态设置为失效
	 if($routeParams.id!='undefined' && $routeParams.id!=undefined){
			var params={'id':$routeParams.id};
			$CommonService.httpPost(params,CONFIG.interface.updateSimGold)
			.success(function(data) {
				if(data.resStatus==CONFIG.CONSTANTS.SUCCESS_STATE){
                    commonUtil.createNotifyAndRedirect("修改成功!",ALERT_SUC, $location, "/findSimGrant");
				}else{
                    commonUtil.createNotifyAndRedirect("修改失败!",ALERT_ERR, $location, "/findSimGrant");
				}
			});
	 }
});


