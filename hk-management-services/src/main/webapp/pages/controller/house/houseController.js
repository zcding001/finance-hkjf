//房产信息管理controller
app.controller('houseCtrl',function ($scope,$injector,$http,$location,$routeParams,$compile,$CommonService) {
    var editor = elementOperateUtil.createKindEditor(CONFIG.PLATFORMS.HKJF,CONFIG.FILEPATHS.INFO_FILEPATH,'showText');
    $scope.house = {};//初始化房产信息对象
    $scope.saleStates = DIC_CONSTANT.getValueAndName("house","sale_state");//销售状态
    $scope.prefers = DIC_CONSTANT.getValueAndName("house","prefer");//楼盘优惠

    //判断是否为编辑操作，有param参数即认为编辑
    if (commonUtil.isNotEmpty($routeParams.param)){
        //获取房产信息
        $CommonService.httpPost({'id': $routeParams.param}, CONFIG.interface.findHouse)
            .success(function (data) {
                //房产信息
                $scope.house = data.params.infoAndDetail;
                _initCheckks();
                //预售证信息
                $scope.permitList = data.params.permitList;
                //封面图片
                $scope.indexPic = data.params.indexPic;
                //根据是否存在封面图片隐藏或显示其对应的添加按钮
                if ($scope.indexPic != null){
                    $("#indexPicBtn").hide();
                }
                //户型图片
                $scope.roomPics = data.params.roomPics;
                //楼盘图片
                $scope.otherPics = data.params.otherPics;
                //展示房产描述信息
                editor.html($scope.house.showText);//将合同模板内容添加至kindeditor中，用于展示合同内容
                $("#showText").val($scope.house.showText);
            }).error(function () {
                commonUtil.createSimpleNotify("系统繁忙!", ALERT_ERR);
        });
    }else {
        _initCheckks();
    }
    //表单提交事件
    var $validationProvider = $injector.get('$validation');
    $scope.save = function (form) {
        //获取项目简介内容进行校验
        if (commonUtil.isEmpty($("#showText").val())){
            $("#showTextError").css('display', 'block').html("项目简介不能为空！");
            return;
        }
        $validationProvider.validate(form).success(function () {
            //处理预售证信息集合
            var result = _initPermits();
            if (result.status == 999){
                commonUtil.createSimpleNotify(result.msg);
                return;
            }
            $("#permitList").val(JSON.stringify(result.data));
            var picResult = _initPics();
            if (picResult.status == 999){
                commonUtil.createSimpleNotify(picResult.msg);
                return;
            }
            //处理房产图片信息集合
            $("#picList").val(JSON.stringify(picResult.data));

            var form = new FormData(document.getElementById("houseForm"));
            $http({
                url: CONFIG.interface.addHouse,
                method: "post",
                data: form,
                transformRequest: angular.identity,
                headers:{
                    'Content-Type' : undefined
                }
            }).success(function (data) {
                if (data.resStatus === CONFIG.CONSTANTS.SUCCESS_STATE){
                    commonUtil.createNotifyAndRedirect("保存成功！",ALERT_SUC,$location,"/houseList");
                } else {
                    commonUtil.createSimpleNotify(data.resMsg,ALERT_ERR);
                }
            }).error(function () {
                commonUtil.createNotifyAndRedirect("保存失败！",ALERT_ERR,$location,"/houseList");
            });
        }).error(function () {
            //表单校验失败
        })
    };

    $scope.back = function () {
        $location.path("/houseList");
    };

    //删除当前行元素
    $scope.removePermit = function ($event) {
        var event = $event || window.event;
        $(event.target).parent().parent().remove();
    };
    var $permits = $("#permits");
    var addPermitLast = $permits.children("div:last-child");
    //添加预售许可证
    $scope.addPermit = function () {
        $permits.append($compile(addPermitLast.prop('outerHTML'))($scope));
    };
    //户型图table及其元素
    var $room_pic_table = $("#room_pic_table");
    var addRoomLastPic = $room_pic_table.children("tr:last-child");
    //封面图片
    var $index_pic_table = $("#index_pic_table");
    var addIndexLastPic = $index_pic_table.children("tr:last-child");
    //楼盘相册
    var $other_pic_table = $("#other_pic_table");
    var addOtherLastPic = $other_pic_table.children("tr:last-child");
    //添加图片
    $scope.addPic = function (id) {
        if(id == 'room_pic_table'){
            $room_pic_table.append($compile(addRoomLastPic.prop('outerHTML'))($scope));
        }
        if(id == 'index_pic_table'){
            $index_pic_table.append($compile(addIndexLastPic.prop('outerHTML'))($scope));
        }
        if(id == 'other_pic_table'){
            $other_pic_table.append($compile(addOtherLastPic.prop('outerHTML'))($scope));
        }
    };

    /**
     * 初始化预售证集合信息
     * @return {Object}
     * @private
     */
    function _initPermits() {
        var map = new Object();
        var permitList = new Array();
        $(".permit_show").each(function(){
            var data = new Object();
            var permitShows = $(this).find("input");
            var permitName = $(permitShows.get(0)).val();
            var permitSendTime = $(permitShows.get(1)).val();
            var permitFloor = $(permitShows.get(2)).val();
            //预售证号不为空，则进行保存
            if (commonUtil.isNotEmpty(permitName)){
                data.name = permitName;
                data.sendTime = permitSendTime;
                data.floor = permitFloor;
                permitList.push(data);
            }else if (commonUtil.isNotEmpty(permitSendTime) || commonUtil.isNotEmpty(permitFloor)) {
                map.status = 999;
                map.msg = "预售证号不能为空!";
                return map;
            }
        });
        map.status = 1000;
        map.data = permitList;
        return map;
    }

    /**
     * 初始化房产信息图片
     * @return {Object}
     * @private
     */
    function _initPics() {
        var picArray = ["roomPics","indexPics","otherPics"];
        var map = new Object();
        var picList = new Array();
        picArray.forEach(function (value) {
           var picEle = $("#" + value).find("input[type='file']");
            picEle.each(function () {
                var url = $(this).val();
                if (commonUtil.isEmpty(url)){
                    return true;
                }
                var fileSize = $(this)[0].files[0].size;
                //图片大小不能超过1M
                if(fileSize > 1024*1024){
                    map.status = 999;
                    map.gmsg = url + "图片大小不能超过1M";
                    return map;
                }
                var picNameObj = $($(this).parent().parent().siblings(":first").children("input").get(0));
                var picName = picNameObj.val();
                var pic = new Object();

                pic.url = url;
                pic.name = picName;
                if (value == "roomPics"){
                    pic.type = 1;
                }else if (value == "indexPics"){
                    pic.type = 3;
                } else {
                    pic.type = 2;
                }
                picList.push(pic);
            });
        });
        map.data = picList;
        map.status = 1000;
        return map;
    }

    /**房产信息编辑相关方法**/
    $scope.deletePermit = function ($event) {
        _deletePermitAndPic(CONFIG.interface.deleteHousePermit,$event);
    };

    $scope.saveHousePermit = function ($event) {
        var event = $event || window.event;
        var $this = $(event.target);
        var $parent = $this.parent().parent();
        var permitShows = $parent.find("input");
        var permitName = $(permitShows.get(0)).val();
        if (commonUtil.isEmpty(permitName)){
            commonUtil.createSimpleNotify("预售证名称不能为空!", ALERT_ERR);
            return;
        }
        var permitSendTime = $(permitShows.get(1)).val();
        var permitFloor = $(permitShows.get(2)).val();
        //获取保存的对象
        var housePermit = {};
        housePermit.infoId = $scope.house.id;
        housePermit.floor = permitFloor;
        housePermit.sendTime = permitSendTime;
        housePermit.name = permitName;
        $CommonService.httpPost(housePermit, CONFIG.interface.saveHousePermit)
            .success(function (data) {
                if(data.resStatus == 1000){
                    var btn = '<button name="btn" id="'+data.resMsg+'" ng-click="deletePermit($event)"style="margin:0' +
                        ' 15px;padding:1px 8px;" >删除</button>';
                    //将保存按钮修改为删除
                    $this.parent().append($compile(btn)($scope));
                    $this.remove();
                }else {
                    commonUtil.createSimpleNotify(data.resMsg, ALERT_ERR);
                }
            }).error(function () {
            commonUtil.createSimpleNotify("系统繁忙!", ALERT_ERR);
        });
    };

    $scope.addPermitToSave = function () {
        var addPermitHtml =  '<div class="table-tr permit_show">  '+
            '<div class="table-td">'+
            '<input type="text" style="width:80%" placeholder="（廊）房预售证第 20180026号" value="" name="permit_permitName"/>'+
            '</div>'+
            '<div class="table-td">'+
            '<input type="text" style="width:80%" placeholder="2018-07-26" value="" name="permit_sendTime"/>'+
            '</div>'+
            '<div class="table-td">'+
            '<input type="text" style="width:80%" placeholder="15#"  value="" name="permit_permitFloor" />'+
            '</div>'+
            '<div class="table-td">'+
            '<button name="btn" ng-click="saveHousePermit($event);" style="margin:0 15px;padding:1px 8px;"' +
            ' >保存</button>'+
            '</div>'+
            '</div>';
        $permits.append($compile(addPermitHtml)($scope));
    };

    $scope.update = function (form) {
        $validationProvider.validate(form).success(function () {
            //获取项目简介内容进行校验
            if (commonUtil.isEmpty($("#showText").val())){
                $("#showTextError").css('display', 'block').html("项目简介不能为空！");
                return;
            }
            var param = $("#houseForm").serializeObject();
            param.id = $scope.house.id;
            //处理复选框的值
            param.buildType = param.buildType.toLocaleString();
            param.feature = param.feature.toLocaleString();
            param.redecorate = param.redecorate.toLocaleString();
            param.roomType = param.roomType.toLocaleString();
            $http.post(CONFIG.interface.updateHouse,param).success(function (data) {
                if (data.resStatus === CONFIG.CONSTANTS.SUCCESS_STATE){
                    commonUtil.createNotifyAndRedirect("保存成功！",ALERT_SUC,$location,"/houseList");
                } else {
                    commonUtil.createSimpleNotify(data.resMsg,ALERT_ERR);
                }
            }).error(function () {
                commonUtil.createNotifyAndRedirect("保存失败！",ALERT_ERR,$location,"/houseList");
            });
        }).error(function () {
            //表单校验失败
        })
    };

    $scope.isSelected = function (type,value) {
        return $scope.house[type].toLocaleString().indexOf(value) != -1;
    };

    //编辑时复选框渲染需要在获取房产信息之后，否则复选框选择无法判断
    function _initCheckks() {
        $scope.roomTypes = DIC_CONSTANT.getValueAndName("house","room_type");//主力户型
        $scope.proTypes = DIC_CONSTANT.getValueAndName("house","pro_type");//物业类别
        $scope.features = DIC_CONSTANT.getValueAndName("house","feature");//项目特色
        $scope.buildTypes = DIC_CONSTANT.getValueAndName("house","build_type");//建筑类别
        $scope.redecorates = DIC_CONSTANT.getValueAndName("house","redecorate");//装修状况
    }
    
    $scope.removePic = function ($event) {
        _deletePermitAndPic(CONFIG.interface.deleteHousePic,$event);
    };

    function _deletePermitAndPic(url,$event) {
        var event = $event || window.event;
        $this = $(event.target);
        var id = $this.attr("id");
        $CommonService.httpPost({'id': id}, url)
            .success(function (data) {
                if (data.resStatus == 1000){
                    //封面图片按钮显示
                    if ($this.parent().parent().parent().attr("id") == "index_pic_table"){
                        $("#indexPicBtn").show();
                    }
                    //去除删除的元素
                    $this.parent().parent().remove();
                }else {
                    commonUtil.createSimpleNotify(data.resMsg, ALERT_ERR);
                }
            }).error(function () {
            commonUtil.createSimpleNotify("系统繁忙!", ALERT_ERR);
        });
    }
    
    $scope.addPicToUpload = function (id,type) {
        var element = '<tr>'+ '<td>'+
            '<label style="position:relative;">'+
            '<input  type="file"/>'+
            '</label>'+
            '</td>'+
            '<td ><input type="text" name="picName" style="height:25px;padding-left:5px;"/></td>'+
            '<td style="line-height:25px;"><a href="javascript:void(0);" ng-click="savePicForm('+type+',$event)"' +
            ' >上传</a></td>'+
            '</tr>';
        var $tbody = $("#" + id);
        $tbody.append($compile(element)($scope));
    };

    $scope.savePicForm = function (type,$event) {
        var event = $event || window.event;
        var $this = $(event.target);
        //获取图片信息
        var picName = $this.parent().parent().find("input[name='picName']");
        var imgFile = $this.parent().parent().find("input[type='file']");
        var fileObj = $(imgFile)[0].files[0];
        //没有上传文件不继续往下处理
        if (commonUtil.isEmpty(fileObj)){
            commonUtil.createSimpleNotify("请先上传图片！");
            return false;
        }
        var fileSize = fileObj.size;
        if(fileSize>1024*1024){
            commonUtil.createSimpleNotify("上传图片不能超过1M！");
            return false;
        }

        var formData = new FormData();
        formData.append("file",fileObj);
        formData.append("name",$(picName).val());
        formData.append("infoId",$scope.house.id);
        formData.append("type",type);

        $http({
            url: CONFIG.interface.saveHousePic,
            method: "post",
            data: formData,
            transformRequest: angular.identity,
            headers:{
                'Content-Type' : undefined
            }
        }).success(function (data) {
            if (data.resStatus === CONFIG.CONSTANTS.SUCCESS_STATE){
                //修改上传按钮为删除按钮
                var btn = '<td style="line-height:25px;"><a href="javascript:void(0);" id= '+data.resMsg+' ' +
                    'ng-click="removePic($event)">删除</a></td>';
                $this.parent().parent().append($compile(btn)($scope));
                $this.parent().remove();
                //如果为封面图片则隐藏添加按钮
                if (type == 3){
                    $("#indexPicBtn").hide();
                }
            } else {
                commonUtil.createSimpleNotify(data.resMsg,ALERT_ERR);
            }
        }).error(function () {
            commonUtil.createSimpleNotify("保存失败！");
        });

    }
});