
app.controller('lotteryItemsController',function ($scope,$http,$location, $CommonService,$routeParams,$injector,$compile) {


    // 添加用户组 在头部添加
    $scope.teams = [{group: "",locationFlag:"",opts:""}];
    $scope.addTeam = function(){
        debugger;
        $scope.teams.splice(0, 0,
            {group:"", locationFlag:"",opts:""});
    }
    //删除用户组
    $scope.delTeam = function($index){
        $scope.teams.splice($index, 1);
    }

    // 添加选项 在头部添加
    $scope.opts = [{port0: "",port1:"",port2:"",port3:"",port4:"",port5:""}];
    $scope.addOpts = function(){
        debugger;
        $scope.opts.splice(0, 0,
            {port0: "",port1:"",port2:"",port3:"",port4:"",port5:""});
    }
    // 删除选项
    $scope.delOpts = function($index){
        debugger;
        $scope.opts.splice($index, 1);
    }

    //返回事件
    $scope.back=function(){
        $location.path("/lotteryActivityList");
    }

    $scope.addTeamTemp=function(){
       alert($("#tempDiv").html());
    }




    //定义页面信息
    $scope.lotterytiemsInfo={};
    //请求原来的信息
  /*  $http.post(CONFIG.interface.lotteryItemsInfo,{id:$routeParams.id}).success(function (data) {
        $scope.lotterytiemsInfo=data.resMsg;
    }).error(function () {
        commonUtil.createNotifyAndRedirect("查询失败，请重试",ALERT_ERR, $location, "/lotteryActivityList");
    });
*/

    //处理表单提交事件
    $scope.submit=function (formName) {
        debugger;
        var params = $("#lotteryItemsForm").serializeObject();
        //检验表单
        var $validationProvider = $injector.get('$validation');
        $validationProvider.validate(formName).success(function(){
            //验证通过之后，进行请求
            var postUrl=null;
            var callBackFun=null;
            var errStrMsg = null;
            if($scope.isSaveBtn){
                if(!validateBeforeSave()){
                    return ;
                }
                //新建
                postUrl = CONFIG.interface.saveLotteryActivity;
                errStrMsg = "新建失败,请重试";
                callBackFun=function (data) {
                    if(data.resStatus==CONFIG.CONSTANTS.SUCCESS_STATE){
                        commonUtil.createNotifyAndRedirect("添加成功!",ALERT_SUC, $location, "/lotteryActivityList");
                    }
                }
            }else{
                //修改
                postUrl = CONFIG.interface.updateLotteryActivity;
                errStrMsg = "修改失败,请重试";
                callBackFun=function (data) {
                    if(data.resStatus==CONFIG.CONSTANTS.SUCCESS_STATE){
                        commonUtil.createNotifyAndRedirect("修改成功!",ALERT_SUC, $location, "/lotteryActivityList");
                    }
                }
            }
            //执行http请求：新建，或者更新
            $http.post(postUrl,params).success(callBackFun).error(function () {
                commonUtil.createNotifyAndRedirect(errStrMsg,ALERT_ERR, $location, "/lotteryActivityList");
            });


        }).error(function(errData){});
    }

    var params = {
        groupModel : $compile('<div class="alert alert-info fade in" name = "groups">'
                    +'用户群: <select class="form-control ng-pristine ng-valid ng-touched textstyle" style=" margin-bottom: 3px;width: 10%;" >'
                    + '<option value="0">全部</option><option value="1" selected="selected">新用户</option>'
                    + '<option value="2">老用户</option><option value="3">男用户</option><option value="4">女用户</option><option value="5">已投资用户</option><option value="6">未投资用户</option> </select>'
                    + '京籍/非京籍<select  class="form-control ng-pristine ng-valid ng-touched textstyle" style=" margin-bottom: 3px;width: 8%;" ><option value="0" selected="selected">全部</option>'
                    + '<option value="1">京籍</option><option value="2">非京籍</option> </select>&nbsp;'
                    + ' <button class="btn" type="button"  ng-click="removeGroup($event,-1,-1)">移除用户群</button>&nbsp;<button class="btn" type="button" ng-click="addItems($event)">添加选项</button></div>')($scope),

        itemModel: $compile('<div class="alert alert-info fade in"><input type="hidden"  value="">'
                    + '奖品类型:<select  class="form-control ng-pristine ng-valid ng-touched textstyle" style=" margin-bottom: 3px;width: 8%;">'
                    + '<option value="1">红包</option><option value="2">积分</option> <option value="3">投资红包</option> <option value="4">加息券</option> <option value="5">兑换奖品</option><option value="6" selected="selected">其他奖品</option></select>'
                    + '价值:<input type="text" class="form-control ng-pristine ng-valid ng-touched textstyle" style="width: 8%;" >元/分/%名称:<input type="text" class="form-control ng-pristine ng-valid ng-touched textstyle" style="width: 8%;" >&nbsp;&nbsp;'
                    + '总数:<input type="text" class="form-control ng-pristine ng-valid ng-touched textstyle" style="width: 8%;"  >个/张&nbsp;&nbsp;中奖概率:<input type="text" class="form-control ng-pristine ng-valid ng-touched textstyle"  style="width: 8%;"  >% &nbsp;&nbsp;'
                    + '序号:<input type="text" class="form-control ng-pristine ng-valid ng-touched textstyle"  style="width: 8%;" > &nbsp;&nbsp;'
                    + '<button class="btn remove_chose" type="button" style="float: right;" ng-click="removeGroup($event,2,-1)">移除选项</button> </div>')($scope),
        delGroup	:'',
        delLocation :'',
        delItems	:''
    }

    // $compile()($scope)

    $scope.addGroup = function () {
        angular.element('#containerDiv').append(params.groupModel);
    }

     $scope.removeGroup = function(event,idGroup,id,location){
         var $this = $(event.target);
         if(idGroup == 1 && id > -1){//groups & locationFlag
             params.delGroup += (id + '-');
             params.delLocation += (location + '-');
         }
         if(idGroup == 2 && id > -1){//items
             params.delItems += (id + '-');
         }
         $this.parent().remove();
    }

    $scope.addItems = function(event){
        var $this = $(event.target);
        $this.parent().append(params.itemModel);
    }



});
