app.controller('fundInfoCtrl',function ($scope,$injector,$timeout,$http,$location,$routeParams,$CommonService,$sce,$rootScope,$checkBoxService) {
	var editor= elementOperateUtil.createKindEditor(CONFIG.PLATFORMS.HKJF,CONFIG.FILEPATHS.INFO_FILEPATH);
	var introductionContentEditor= elementOperateUtil.createKindEditor(CONFIG.PLATFORMS.HKJF,CONFIG.FILEPATHS.INFO_FILEPATH,"introductionContent");
	//初始化页面下拉列表
	$scope.termUnits =DIC_CONSTANT.getValueAndName("invest", "bid_term_unit");
    $scope.projectTypes =DIC_CONSTANT.getValueAndName("fund", "project_type");
    $scope.payWays = DIC_CONSTANT.getValueAndName("fund","project_pay_way");
    $routeParams.id = $routeParams.param;
    $scope.fundInfo = {};//页面初始化对象
    var urlFundInfo = CONFIG.interface.addFundInfo;
    $scope.strConvertArrContains = $CommonService.strConvertArrContains;
    $scope.infoExistFlag = true;
    if (commonUtil.isNotEmpty($scope.projectTypes)){
        $scope.fundInfo.projectId = $scope.projectTypes[0].value;//添加时默认
    }
    if (commonUtil.isNotEmpty($scope.termUnits)){
        $scope.fundInfo.termUnit = $scope.termUnits[1].value;//添加时默认
    }

    var customizeData = [
        { "name" : "每周", "id" : 1, "children" : [
            { "name" : "周一", "id" : 1},
            { "name" : "周二", "id" : 2},
            { "name" : "周三", "id" : 3},
            { "name" : "周四", "id" : 4},
            { "name" : "周五", "id" : 5},
            { "name" : "周六", "id" : 6},
            { "name" : "周日", "id" : 7}
        ]},
        { "name" : "每日", "id" : 2, "children" : [
            { "name" : "1号", "id" : 1},
            { "name" : "2号", "id" : 2},
            { "name" : "3号", "id" : 3},
            { "name" : "4号", "id" : 4},
            { "name" : "5号", "id" : 5},
            { "name" : "6号", "id" : 6},
            { "name" : "7号", "id" : 7},
            { "name" : "8号", "id" : 8},
            { "name" : "9号", "id" : 9},
            { "name" : "10号", "id" : 10},
            { "name" : "11号", "id" : 11},
            { "name" : "12号", "id" : 12},
            { "name" : "13号", "id" : 13},
            { "name" : "14号", "id" : 14},
            { "name" : "15号", "id" : 15},
            { "name" : "16号", "id" : 16},
            { "name" : "17号", "id" : 17},
            { "name" : "18号", "id" : 18},
            { "name" : "19号", "id" : 19},
            { "name" : "20号", "id" : 20},
            { "name" : "21号", "id" : 21},
            { "name" : "22号", "id" : 22},
            { "name" : "23号", "id" : 23},
            { "name" : "24号", "id" : 24},
            { "name" : "25号", "id" : 25},
            { "name" : "26号", "id" : 26},
            { "name" : "27号", "id" : 27},
            { "name" : "28号", "id" : 28},
            { "name" : "29号", "id" : 29},
            { "name" : "30号", "id" : 30},
            { "name" : "31号", "id" : 31}
        ]}
    ];
    // 初始化协议类型
    var valueNameArr=[];
    var valueNameObj1={};
    var valueNameObj2={};
    valueNameObj1.value=1;
    valueNameObj1.name="Class A Subsciption";
    valueNameObj2.value=2;
    valueNameObj2.name="Class B Subsciption";
    valueNameArr.push(valueNameObj1);
    valueNameArr.push(valueNameObj2);
    $scope.contractTypes = valueNameArr;

    $scope.customizeTypes = customizeData;
    if (commonUtil.isNotEmpty($scope.customizeTypes)){
        $scope.fundInfo.customizeType = $scope.customizeTypes[0].id;//添加时默认
        $scope.customizeValues = $scope.customizeTypes[0].children;//添加时默认
        $scope.fundInfo.customizeValue =  $scope.customizeValues[0].id;//添加时默认
        $scope.fundInfo.contractType = $scope.customizeTypes[0].id;//添加时默认
    }
    $scope.changeCustomize = function (val) {
      $scope.customizeValues = $scope.customizeTypes[val-1].children;
    };
     //新闻类型选择事件
	 $scope.changeProjectType=function(x){
		 var keepGoing = true;
		 angular.forEach($scope.projectTypes, function(data,index,array){
			//data等价于array[index]
		    if(keepGoing){
				if(data.value==x){
					$scope.projectId=data.value+'';
		        	keepGoing = false;
				}
		   }
		});
	 };
	 $scope.changeTermUnit=function(x){
		 var keepGoing = true;
		 angular.forEach($scope.termUnits, function(data,index,array){
			//data等价于array[index]
		    if(keepGoing){
				if(data.value==x){
					$scope.termUnit=data.value+'';
		        	keepGoing = false;
				}
		   }
		});
	 };
    
    //修改操作
	 if($routeParams.id!='undefined' && $routeParams.id!=undefined){
		    $scope.showPage=false;//用于判断是新增还是修改的标识 false:修改
		    urlFundInfo = CONFIG.interface.updateFundInfo;
			var params={'id':$routeParams.id};
			$CommonService.httpPost(params,CONFIG.interface.findFundInfoById)
			.success(function(data) {
                    console.log(data);
				$scope.fundInfo = data.resMsg;
		        //格式化时间
			    $scope.fundInfo.startTime = dateUtil.dateTime(data.resMsg.startTime);
	            $scope.fundInfo.endTime = dateUtil.dateTime(data.resMsg.endTime);
                $scope.fundInfo.establishedTime = dateUtil.dateTime(data.resMsg.establishedTime);
                if(data.resMsg.stepValue == 0){
                    $scope.fundInfo.stepValue = null;
				}
				editor.html(data.resMsg.investRange);
			});
	 }else{
	     //初始化数据
		 $scope.showPage=true;
		 init($scope.fundInfo);
	 }
	//表单提交事件
	var $validationProvider = $injector.get('$validation');
	//新增
	$scope.submit = function(form){
		//获取表单信息，并验证
   	var investRange= editor.html();
   	var introductionContent= introductionContentEditor.html();
    $scope.fundInfo.investRange = investRange;
    $scope.fundInfo.introduction = introductionContent;
    $scope.fundInfo.modifyTime = new Date();
    $scope.fundInfo.createTime = null;
	$validationProvider.validate(form).success(function(){
			var infoExistFalg = $scope.fundInfo.infoExist;
			    //内容不为空判断
			     if(infoExistFalg==1 && commonUtil.isEmpty(investRange)){
			    	$("#investRangeError").css('display', 'block').html("正文内容不能为空！");
			    	return;
			     }else if(infoExistFalg==0 && commonUtil.isEmpty(introductionContent)){
			    	 $("#introductionError").css('display', 'block').html("项目描述内容不能为空！");
				    	return; 
			     }
		    var $stepValue = $scope.fundInfo.stepValue;
			var $projectId = $scope.fundInfo.projectId;
			     if(infoExistFalg==1 && $projectId==5 &&!commonUtil.isEmpty($stepValue) ){
			     	var reg = /^([1-9]\d*|[0]{1,1})$/;
			        if(!reg.test($stepValue)){
                        $("#stepValueError").css('display', 'block').html("投资步长为非负整数！");
                        return;
                    }
				 }

			     if($scope.fundInfo.startTime=='--'){
			    	 $scope.fundInfo.startTime = null;
			     }
			     if($scope.fundInfo.endTime=='--'){
			    	 $scope.fundInfo.endTime = null;
			     }
			     if($scope.fundInfo.establishedTime=='--'){
			    	 $scope.fundInfo.establishedTime = null;
			     }
			     var maxRate = $scope.fundInfo.maxRate;
			     var minRate = $scope.fundInfo.minRate;
			    if(!commonUtil.isEmpty(maxRate) && !commonUtil.isEmpty(maxRate)&& maxRate>0 &&
                    parseFloat(maxRate) <= parseFloat(minRate)){
			    	$("#rateError").css('display', 'block').html("年化率最大值必须大于最小值！");
			    	return; 
			    }
			    if($scope.fundInfo.minRate == 0 && maxRate > 0){
			    	$("#rateError").css('display', 'block').html("年化率只需填写一个值，规定必须最小值，最大值填0！");
			    	return; 
			    }
			     var params =$scope.fundInfo;
			     console.log(params);
			      $http.post(urlFundInfo,params).success(function (data) { 
			      	  console.log(data);
			          if(data.resStatus==CONFIG.CONSTANTS.SUCCESS_STATE){
			          	console.log(data);
                          commonUtil.createNotifyAndRedirect("保存成功!",ALERT_SUC, $location, "/searchFundInfoList");
			          }
			      }).error(function () {
                      commonUtil.createNotifyAndRedirect("添加失败!",ALERT_ERR, $location, "/searchFundInfoList");
			      });
		}).error(function(){
		});
	 };
	 //重置
	 $scope.reset=function(form){
		 $validationProvider.reset(form);
		 editor.html("");
		 introductionContentEditor.html("");
	 };
		 //返回
	$scope.back=function(){
		 $location.path("/searchFundInfoList");
	};
	$scope.timeChange = function (id) {
		 $scope.fundInfo[id] = $("#" + id).val();
	}
	
});
//初始化radio选中
function init(Object) {
	Object.infoExist = 1;
	Object.payWay = 1;
	Object.opendayType = 1;
	Object.lowestAmountUnit=1;
	Object.maxRate=0.0;
	Object.minRate=0.0;
}
