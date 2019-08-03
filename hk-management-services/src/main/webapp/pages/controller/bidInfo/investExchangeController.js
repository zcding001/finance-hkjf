//标的controller
app.controller('investExchangeController', function ($scope, $injector, $timeout, $http, $filter, $window, $checkBoxService, $location, $CommonService, $routeParams, $rootScope) {
    var params = {"bidId":$routeParams.bidId};
    var bidId = $routeParams.bidId;
    var columns =  [
        { title:"姓名",data: "investUserName", width:140,
            render: function (value, type, row, meta) {
                /* return commonUtil.getTextByLength(value, 10);*/
                return value;
            }
        },
        { title:"电话",data: "tel", width:100},
        { title:"金额(元)",className:"dt-simple",data: "investAtm"}
    ];
    //创建dataTable列表
    dataTableUtil.createDTNoPageByList(CONFIG.interface.investExchangeList+"?bidId="+bidId, columns);
    // dataTableUtil.createDTForCallBack(CONFIG.interface.investExchangeList+"?bidId="+bidId,columns,"",function(data){
    //     alert(data.matchState);
    // });

    //表单提交事件
    $scope.doMatch = function () {
        alert(params);
        $CommonService.httpPost(params,CONFIG.interface.matchInvestExchange)
            .success(function(data) {
                if(data.resStatus==CONFIG.CONSTANTS.SUCCESS_STATE){
                    commonUtil.createNotifyAndRedirect("匹配成功!",ALERT_SUC, $location, "/bidExchangeList");
                }else{
                    commonUtil.createSimpleNotify(data.resMsg,ALERT_ERR);
                }
            }).error(function () {
            commonUtil.createSimpleNotify("匹配失败!",ALERT_ERR);
        });
    };

});

function dataTables_result_callback(data) {
    if(data.params.bidState == 5){
        $("#doMatchBtn").hide();
        $("#showMsg").html("完全匹配");
    }else{
        if(data.params.matchState = 'success'){
            $("#showMsg").html("<span>完全匹配</span>");
        }else{
            $("#showMsg").html("<span>部分匹配（剩余金额：data.params.subWatingMoney）</span>");
        }

    }
}