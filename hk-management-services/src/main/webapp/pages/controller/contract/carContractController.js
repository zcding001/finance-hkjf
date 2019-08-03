app.controller('carContractCtrl',function ($scope,$injector,$timeout,$http,$location,$routeParams,$CommonService,$sce,$rootScope,$checkBoxService) {
    $routeParams.id = $routeParams.param;
    $scope.carContract = {};//页面初始化对象
    $scope.partyA = {};//页面初始化对象
    $scope.partyB = {};//页面初始化对象
    var urlCarContract = CONFIG.interface.addCarContract;
    $scope.strConvertArrContains = $CommonService.strConvertArrContains;

    //填写手机号其余自动填充事件
    $scope.autofill=function(tel,utype){
        if(commonUtil.isEmpty(tel)){
            return;
        }
        var params={'tel':tel,"utype":utype};
        $CommonService.httpPost(params,CONFIG.interface.selectUserInfoByTel)
            .success(function(data) {
                console.log(data);
                if(data.resStatus != CONFIG.CONSTANTS.SUCCESS_STATE){
                    commonUtil.createSimpleNotify(data.resMsg);
                    return;
                }
                if(utype == 'b'){   //乙方信息
                    $scope.partyB = data.resMsg;
                    $scope.carContract.partyBtel = data.resMsg.userTel;
                    $scope.carContract.partyBid = data.resMsg.id;
                    $scope.carContract.partyBname = data.resMsg.userName;
                }else{              //甲方信息
                    $scope.partyA = data.resMsg;
                    $scope.carContract.partyAtel = data.resMsg.userTel;
                    $scope.carContract.partyAid = data.resMsg.id;
                    $scope.carContract.partyAname = data.resMsg.userName;
                }
            });
    };

    //自动回显借款截止日，委托截止日
    $scope.calLoanEndTime=function(X){
        var duration = $("#duration").val();
        var loanStartTime = $("#loanStartTime").val();

        var start = new Date(loanStartTime);
        start.add("m", duration);
        start.add("d", 1);
        loanEndTime = start.Format("yyyy-MM-dd");
        $("#loanEndTime").val(loanEndTime);

        start.add("m",12)
        endTime = start.Format("yyyy-MM-dd");
        $("#endTime").val(endTime);

        //格式化时间
        $scope.carContract.loanStartTime = dateUtil.date(loanStartTime);
        $scope.carContract.loanEndTime = dateUtil.date(loanEndTime);
        $scope.carContract.endTime = dateUtil.date(endTime);

    }


    //修改操作
    if($routeParams.id!='undefined' && $routeParams.id!=undefined){
        if($routeParams.operate == 'copy'){
            urlCarContract = CONFIG.interface.addCarContract;
        }else{
            urlCarContract = CONFIG.interface.updateCarContract;
        }
        $scope.showPage=false;//用于判断是新增还是修改的标识 false:修改
        $scope.editable=false;//用于判断是否下载过，false未下载
        var params={'id':$routeParams.id};
        $CommonService.httpPost(params,CONFIG.interface.findCarContractById)
            .success(function(data) {
                console.log(data);
                $scope.carContract = data.params.carContract;
                $scope.partyA = data.params.partyA;
                $scope.partyB = data.params.partyB;

                //下载过禁止修改
                if($scope.carContract.downloadNum > 0){
                    $scope.editable=true;
                    $("#partyAtel").attr("readonly","readonly");
                    $("#partyBtel").attr("readonly","readonly");
                    $("#amount").attr("readonly","readonly");
                    $("#duration").attr("readonly","readonly");
                    $("#rate").attr("readonly","readonly");
                }

                //格式化时间
                $scope.carContract.loanStartTime = dateUtil.date(data.params.carContract.loanStartTime);
                $scope.carContract.loanEndTime = dateUtil.date(data.params.carContract.loanEndTime);
                $scope.carContract.endTime = dateUtil.date(data.params.carContract.endTime);
                $scope.carContract.createTime = dateUtil.dateTime(data.params.carContract.createTime);
                $scope.carContract.modifyTime = dateUtil.dateTime(data.params.carContract.modifyTime);
            });
    }else{
        //初始化数据
        $scope.showPage=true;
        init($scope.carContract);
    }
    //表单提交事件
    var $validationProvider = $injector.get('$validation');
    //新增
    $scope.submit = function(form){
        //获取表单信息，并验证
        $validationProvider.validate(form).success(function(){
            var params =$scope.carContract;
            console.log(params);
            $http.post(urlCarContract,params).success(function (data) {
                console.log(data);
                if(data.resStatus==CONFIG.CONSTANTS.SUCCESS_STATE){
                    console.log(data);
                    commonUtil.createNotifyAndRedirect("保存成功!",ALERT_SUC, $location, "/searchCarContractList");
                }
            }).error(function () {
                commonUtil.createNotifyAndRedirect("添加失败!",ALERT_ERR, $location, "/searchCarContractList");
            });
        }).error(function(){
        });
    };
    //重置
    $scope.reset=function(form){
        $validationProvider.reset(form);
    };
    //返回
    $scope.back=function(){
        $location.path("/searchCarContractList");
    };
    $scope.timeChange = function (id) {
        $scope.carContract[id] = $("#" + id).val();
    }

});
//初始化radio选中
function init(Object) {

}
