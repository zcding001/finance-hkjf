/***
 * 小区的前端Controller
 * @Author : zhongpingtang@yiruntz.com 唐忠平
 */
app.controller('CommunityController', function ($scope, $timeout, $http, $CommonService, $compile, $location) {

    //初始化函数区
    /**
     * 构造option
     */
    $scope.constructOption=function(messArr, attrName, attrValue) {
        var options = '<option value="none" >--请选择---</option>';
        if(messArr.length>0){
            for(var i=0;i<messArr.length;i++){
                options+= '<option value='+messArr[i][attrValue]+' >'+messArr[i][attrName]+'</option>'
            }
        }
        return options;
    }

    //根据选择的物业公司选择下面的小区
    $scope.changeCommunityOpt=function () {
        var tenementValue=$("#tenementSelect").val();
        if(tenementValue!='none'){
            //请求小区下拉框
            $http.post(CONFIG.interface.findTenementsCommunity,{id:tenementValue}).success(function (data) {
                $("#communitySelect").empty();
                $("#communitySelect").append($scope.constructOption(data.resMsg,"communityName","id"));
            });
        }else{
            $("#communitySelect").empty();
            $("#communitySelect").append('<option value="none" >--请选择---</option>');
        }
    }

    /**
     * 隐藏或者显示面板
     */
    $scope.showPanel=function(type) {
        var tempContents = '';
        //添加小区
        if(type==0){
            // 小区span
            $("#contentsSpan").empty();
            tempContents += '<span id="addCommuntiy" >小区名称：<input id="communityNameInput" name="communityName" type="text" class="confirmChildren"/><br><br></span>';
            $("#contentsSpan").append(tempContents);
        }

        //添加门店
        if(type==1){
            $("#addCommuntiy").hide();
            $("#addTenement").show();
            //门店span
            $("#contentsSpan").empty();

            tempContents+='<span id="addTenement" >' +
                '物业公司：<select id="tenementSelect" name="regUserId" onchange="angular.element(this).scope().changeCommunityOpt()"></select><br><br>'+
                '小区名称：<select id="communitySelect" name="pid" ></select><br><br>'+
                '门店名称：<input id="tenementInput" name="communityName" type="text" class="confirmChildren"/><br><br>' +
                '</span>';

            tempContents+='<input name="communityType" type="hidden" value="1"/>'
            $("#contentsSpan").append(tempContents);

            //请求物业下拉框
            $http.post(CONFIG.interface.findUserTypeTenementNoPage).success(function (data) {
                //物业下拉框
                $("#tenementSelect").empty();
                $("#tenementSelect").append($scope.constructOption(data.resMsg,"nickName","id"));

                //小区下拉框
                $("#communitySelect").empty();
                $("#communitySelect").append('<option value="none" >--请选择---</option>');
            });
        }
    }
    //定义添加小区或者门店面板
    $scope.createAddCommunityPanal =function () {
        var contents = '添加类型：<input  type="radio" name="addType" onclick="angular.element(this).scope().showPanel(0)" />小区 &nbsp;&nbsp;<input  type="radio" name="addType" onclick="angular.element(this).scope().showPanel(1)" />门店 <br><br>';
         contents+='<span id="contentsSpan"></span>'
        commonUtil.createCustomConfirmBox("添加小区/门店", contents, CONFIG.interface.saveCommunity,null,commonUtil.customConfirmStandardAfterExecuteFn);

    }


});
