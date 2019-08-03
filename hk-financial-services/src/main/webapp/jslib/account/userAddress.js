/**
 * 用户收获地址js
 */
var userAddressController = {};
(function () {
    'use strict';
    var _addressList;//用户地址集合
    /**
     * 初始化用户收货地址列表
     */
    userAddressController.initUserAddressList = function () {
        var callBack = function (result) {
            if (result.resStatus == CONSTANTS.SUCCESS){
                _addressList = result.resMsg;
                $("#addressCount").text(_addressList.length);
                _addressList.forEach(function (address) {
                    var li = "<li class='biaogeli1' style='font-weight: normal'>" +
                        "<span style='width: 80px;min-height:1px;'>"+address.alias+"</span>" +
                        "<span>"+address.consignee+"</span>" +
                        "<span>"+dictionaryUtil.getAreaName(address.province)+"&nbsp"+dictionaryUtil.getAreaName(address.city)+"&nbsp"+dictionaryUtil.getAreaName(address.county)+"</span>" +
                        "<span>"+address.district+"</span>" +
                        "<span>"+address.mobilePhone+"</span>" +
                        "<span><em class='modify'><a" +
                        " href='javascript:void(0)'onclick='userAddressController.toUpdateAddress("+address.id+")'" +
                        " class='mr_a'>修改</a></em> | <i class='delete'><a href=javascript:void(0) onclick='userAddressController.delAddress("+address.id+")' class='mr_del'>删除</a></i></span>";
                    if(address.state == 3){
                        li = li + "<span class='colr-org' style='margin-left: 10px'>默认地址</span></li>";
                    }else {
                        li = li + "<span class='default-address' style='margin-left: 5px;display: none'" +
                            " onclick='userAddressController.setDefaultAddress("+address.id+")'>设为默认</span></li>";
                    }
                    $("#userAddressList").append(li);
                });
                /**鼠标滑过不为默认地址的记录，显示设为默认按钮*/
                $(".biaogeli1").hover(function () {
                    $(this).find(".default-address").show();
                },function () {
                    $(this).find(".default-address").hide();
                });
            }else {
                dialogUtil.alert("提示","用户收货地址获取失败！");
            }
        };
        ajaxUtil.post("/userAddressController/getUserAddressList.do",null,callBack);
    };

    /**
     * 初始化用户收货地址校验元素
     */
    userAddressController.initValidateElement = function () {
        //校验收货人姓名
        $("#consignee").on("blur",_validateConsignee);
        //校验地区
        $("select").on("blur",_validateArea);
        //校验详细地址
        $("#district").on("blur",_validateDistrict);
        //校验手机号码
        $("#mobilePhone").on("blur",_validateMobilePhone);
    };
    /**
     * 校验收货人姓名
     */
    var _validateConsignee = function () {
        var consignee = $("#consignee").val();
        if (commonUtil.isEmpty(consignee)){
            $("#consignee").next().text("请输入姓名！");
            return false;
        }
        if (!(18 >= consignee.length && consignee.length >= 2)){
            $("#consignee").next().text("姓名2-18个字符！");
            return false;
        }
        $("#consignee").next().text("");
        return true;
    };

    /**
     * 校验地区
     */
    var _validateArea = function () {
        var province = $("#province").val();
        var city = $("#city").val();
        var county = $("#county").val();
        if (province == "-999" || city == "-999" || county == "-999"){
            $("#county").next().text("请选择完整的地区！");
            return false;
        }
        $("#county").next().text("");
        return true;
    };

    /**
     * 校验详细地址
     */
    var _validateDistrict = function () {
        var district = $("#district").val();
        if (commonUtil.isEmpty(district)){
            $("#district").next().text("请输入详细地址！");
            return false;
        }
        if (district.length > 100){
            $("#district").next().text("详细地址不能大于100个字符！");
            return false;
        }
        $("#district").next().text("");
        return true;
    };

    /**
     * 校验手机号码
     */
    var _validateMobilePhone = function () {
        var mobilePhone = $("#mobilePhone").val();
        if (commonUtil.isEmpty(mobilePhone)){
            $("#mobilePhone").next().text("请输入手机号码！");
            return false;
        }
        var reg = /^0?1[3-5|7-8][0-9]\d{8}$/;
        if (!reg.test(mobilePhone)){
            $("#mobilePhone").next().text("请输入正确的手机号码！");
            return false;
        }
        $("#mobilePhone").next().text("");
        return true;
    };

    /**
     * 保存用户收货地址
     */
    userAddressController.saveUserAddress = function () {
        //先校验收货人、所在地区、详细地址、手机号码
        if(_validateConsignee() && _validateArea() && _validateDistrict() && _validateMobilePhone()){
            var ids = ["consignee","province","city","county","district","mobilePhone","telephone","alias"];
            var param = {};
            ids.forEach(function (id) {
                param[id] = $("#"+id).val();
            });
            //设置地址别名默认值
            if (commonUtil.isEmpty(param["alias"])){
                param["alias"] = param["consignee"] + "-" + dictionaryUtil.getAreaName(param["province"]);
            }
            ajaxUtil.post("/userAddressController/saveUserAddress.do",param,_callBackCommon);
        }
    };

    /**
     * 更新用户收货地址
     */
    userAddressController.updateUserAddress = function () {
        //先校验收货人、所在地区、详细地址、手机号码
        if(_validateConsignee() && _validateArea() && _validateDistrict() && _validateMobilePhone()){
            var ids = ["id","consignee","province","city","county","district","mobilePhone","telephone","alias"];
            var param = {};
            ids.forEach(function (id) {
                param[id] = $("#"+id).val();
            });
            //设置地址别名默认值
            if (commonUtil.isEmpty(param["alias"])){
                param["alias"] = param["consignee"] + "-" + dictionaryUtil.getAreaName(param["province"]);
            }
            ajaxUtil.post("/userAddressController/updateUserAddress.do",param,_callBackCommon);
        }
    };
    /**
     * 设置地址为默认地址
     * @param addressId  地址id
     */
    userAddressController.setDefaultAddress = function (addressId) {
        ajaxUtil.post("/userAddressController/setDefaultAddress.do",{"addressId":addressId},_callBackCommon);
    };

    /**
     * 删除收货地址
     * @param addressId  地址id
     */
    userAddressController.delAddress = function (addressId) {
        var callBack = function () {
            ajaxUtil.post("/userAddressController/delAddress.do",{"addressId":addressId},_callBackCommon);
        };
        dialogUtil.confirm("提示","确定要删除么？",callBack);
    };

    /**
     * 设置别名
     */
    userAddressController.setAlias = function () {
        $(".b_radius.ho").on("click",function () {
            var text = $(this).text();
            $("#alias").val(text);
        });
    };
    /**
     * 跳转至用户地址编辑页面
     * @param addressId  地址id
     */
    userAddressController.toUpdateAddress = function (addressId) {
        var address;
        _addressList.forEach(function (data) {
            if (data.id == addressId){
                address = data;
                return false;
            }
        });
        if (address == null){
            dialogUtil.alert("提示","请选择要进行修改的地址！");
        }
        commonUtil.jumpAccountMenu("updateUserAddress.html",address);
    };

    var _callBackCommon = function (result) {
        if (result.resStatus == CONSTANTS.SUCCESS){
            //刷新我的收货地址页面
            commonUtil.jumpAccountMenu("userAddress.html");
        }else {
            dialogUtil.alert("提示",result.resMsg);
        }
    };

}());