/**
 * 积分商品
 * @type {{}}
 */
var orderHandle = {};
$(function () {
        //同步订单栏目地址
        orderHandle.copyToOrderInfoAddress = function (orderAddress) {
            if (orderAddress == null) {
                //说明是门店地址
                orderAddress = $("#propertyId").val() + ',' + $("#communityId").val();
            }
            $("#tdAddress").html(orderAddress);
        }

        //初始化用地址
        orderHandle.initUserAddress = function () {
            //取到省市信息
            dictionaryUtil.initBankCardAreaSelect("addressProvince", "addressCity",'', '');
            /************收货地址部分****************/
            //请求用户地址
            ajaxUtil.post('/userAddressController/getUserAddressList.do', {}, function (data) {
                if (data.resStatus == CONSTANTS.SUCCESS && data.resMsg.length > 0) {
                    var addressList = data.resMsg;
                    if (addressList != null && addressList.length > 0) {
                        var address = '';
                        for (var i = addressList.length - 1; i >= 0; i--) {
                            var orderAddress = dictionaryUtil.getAreaName(addressList[i].province) + ',' + dictionaryUtil.getAreaName(addressList[i].city) + "," + addressList[i].district;
                            var addressValue = orderAddress +'   '+addressList[i].consignee+'  '+addressList[i].mobilePhone + "<br/>";
                            address += '<input type="radio" name="chooseAddress" value="' + addressValue + '"  onclick="orderHandle.copyToOrderInfoAddress(this.value)" style="margin-bottom:10px;">' + addressValue
                        }
                    }
                    $("#addressRadioLi").empty();
                    $("#addressRadioLi").append(address);
                    //默认第一个勾选
                   // $("input[name='chooseAddress']:first").attr("checked", 'checked');
                    $("input[name='chooseAddress']:first").click();
                } else {
                    //没有任何地址信息
                }

            }, null, false);
        };

        //添加用户地址
        orderHandle.addUserAddress = function () {
            var address = {};
            //收货人
            if (commonUtil.isEmpty($("#reciviceUserName").val())) {
                dialogUtil.alert("提示","请填写收货人");
                return;
            } else {
                address.consignee = $("#reciviceUserName").val().trim();
            }
            //所在地区
            if ($("#province").val() == "-999") {
                dialogUtil.alert("提示","请选择省");
                return;
            } else {
                address.province = $("#province").val();
            }
            if ($("#city").val() == "-999") {
                dialogUtil.alert("提示","请选择市");
                return;
            } else {
                address.city = $("#city").val();
            }

            //详细地址
            if (commonUtil.isEmpty($("#addrDetail").val())) {
                dialogUtil.alert("提示","请填写详细地址");
                return;
            } else {
                address.district = $("#addrDetail").val().trim();
            }
            //手机号码
            if (commonUtil.isEmpty($("#userTel").val())||!validUtil.validTel($("#userTel").val())) {
                dialogUtil.alert("提示","请填写正确的电话号码");
                return;
            } else {
                address.mobilePhone = $("#userTel").val().trim();
            }

            //增加用户地址
            ajaxUtil.post('/userAddressController/saveUserAddress.do', address, function (data) {
                if (data.resStatus == CONSTANTS.SUCCESS) {
                    $('.jfzf-tc-1').hide();
                    orderHandle.initUserAddress();
                }

            }, null, false);

        }

        //初始化小区
        orderHandle.initPropertites = function () {
            //请求用户小区
            ajaxUtil.post('pointProductOrderController/loadOfflineStoreAddress.do', {}, function (data) {
                if (data.resStatus == CONSTANTS.SUCCESS && data.resMsg.length > 0) {
                    var communitiesStores = data.resMsg;
                    //加入缓存
                    containerUtil.set("communitiesStores", communitiesStores);
                    var contents = '';
                    contents += '<option value="" selected="selected">--请选择--</option>';
                    for (var i = 0; i < communitiesStores.length; i++) {
                        var tempContents = communitiesStores[i];
                        contents += '<option value="' + tempContents.tenementName + '" >' + tempContents.tenementName + '</option>';
                    }
                    //拼接小区
                    $("#propertyId").empty();
                    $("#propertyId").append(contents);

                    //拼接门店
                    contents = '<option value="" selected="selected">--请选择--</option>';
                    $("#communityId").empty();
                    $("#communityId").append(contents);

                } else {
                    //没有任何地址信息  do noting..
                }

            }, null, false);
        }

        //初始化订单操作
        orderHandle.initOrder = function () {

            /************商品信息部分****************/
                //取到商品信息
            var product = containerUtil.get("pointProduct-" + pointProductCommons.getUrlParam("product"));
            var productId = product.id;

            var imgurlPix = "${oss.url}";
            //设置商品图片,名
            $("#productImg").attr("src", imgurlPix + product.headImg.smallImgUrl);
            $("#productTitle").html(product.name);

            //设置数量,所需积分
            var productOrderNum = containerUtil.get("userOrderNum-" + productId);
            var productNeedPoint = containerUtil.get("userOrderNeedPoint-" + productId);
            $("#productOrderNum").html(productOrderNum);
            $("#productNeedPoint").html(productNeedPoint);

            $("#orderTotalSpan").html(productNeedPoint);

            //判断商品支持的收货方式
            var supportSendWay = product.sendWay;
            containerUtil.set("userOrderSupportSendWay-" + productId,supportSendWay);

            if (supportSendWay == 0) {
                //0-邮寄或自提
                $(".sendWay1").show();
                $(".sendWay2").show();
                $("#sw1").click();

            }
            if (supportSendWay == 1) {
                //1-自提
                $(".sendWay1").show();
                $(".sendWay2").hide();
                $("#sw1").click();
            }
            if (supportSendWay == 2) {
                //2-邮寄
                $(".sendWay1").hide();
                $(".sendWay2").show();
                $("#sw2").click();
            }

            if (supportSendWay == 3) {
                //2-兑换码
                $(".shop-kuaidi").hide();
                orderHandle.copyToOrderInfoAddress('此商品无需配送地址')
            }



        }
        orderHandle.selectproperty = function (value) {
            if (!commonUtil.isEmpty(value)) {
                var csource = containerUtil.get("communitiesStores");
                for (var i = 0; i < csource.length; i++) {
                    var temp = csource[i];
                    if (temp.tenementName == value) {
                        var storeList = temp.offlineStoreName;
                        var contents = '';
                        contents += '<option value="" selected="selected">--请选择--</option>';
                        for (var k = 0; k < storeList.length; k++) {
                            var tempStore = storeList[k];
                            contents += '<option value="' + tempStore + '" >' + tempStore + '</option>';
                        }

                    }
                }
            } else {
                //把下个选择框变成请选择
                var contents = '<option value="" selected="selected">--请选择--</option>';
            }
            //拼接
            $("#communityId").empty();
            $("#communityId").append(contents);
        }

        //选择配送方式信息
        orderHandle.selectSendWay = function (sendWay) {
            if (sendWay == 1) {
                //自取
                //初始化小区
                orderHandle.initPropertites();
                $("#selectPropertyLi").show();
                $("#selectAddress").hide();
                //初始化订单地址
                orderHandle.copyToOrderInfoAddress('请选择地址')
            }
            if (sendWay == 2) {
                //邮寄
                //初始化用户地址
                orderHandle.initUserAddress();
                $("#selectPropertyLi").hide();
                $("#selectAddress").show();
                //同步第一个地址到订单地址
                $("input[name='chooseAddress']:first").click();
            }
        }

        //创建订单操作
        orderHandle.createOrder = function () {
            var orderInfo = {};
            //创建订单前置检查
            //商品id
            orderInfo.productId = pointProductCommons.getUrlParam("product");
            //兑换数量
            orderInfo.number =containerUtil.get("userOrderNum-" + orderInfo.productId);
            //地址信息
            orderInfo.address = $("#tdAddress").html().replace('<br>', '');
            if (containerUtil.get("userOrderSupportSendWay-" + orderInfo.productId) != 3&&commonUtil.isEmpty(orderInfo.address)||orderInfo.address=='请选择地址') {
                $('.bs-example-modal-sm').hide();
                $('.modal-backdrop').remove();
                dialogUtil.alert("提示","请指定地址");
                return;
            }
            //创建订单
            ajaxUtil.post('/pointProductOrderController/createOrderInfo.do', orderInfo, function (data) {
                if (data.resStatus == CONSTANTS.SUCCESS) {
                    //order created.
                    $('.mask').show()
                    $('.bs-example-modal-sm').hide()
                }
                if (data.resStatus ==  999) {
                    //order 创建失败.
                    $('.bs-example-modal-sm').hide();
                    $('.modal-backdrop').remove();
                    $('body').removeClass('modal-open');
                    dialogUtil.alert("提示",data.resMsg);
                }

            }, null, false);


        }


        /*****************主流程*****************************/
        //初始化订单
        orderHandle.initOrder();
        $('#chakandio').on('click', function (e) {
            commonUtil.jumpToAccount("pointProductOrders.html")
        });
        $('#zhengtigbaa').on('click', function (e) {
            $('.mask').hide()
            commonUtil.jump('pointmall/index.html');
        });
        //新增收货地址弹窗显示隐藏
        $(".new_address").on("click", function () {
            $(".jfzf-tc-1").css("display", "block");
        })
        $(".jf-quxiao").on("click", function () {
            $(".jfzf-tc-1").css("display", "none");
        })
    }
)