//标的controller
app.controller('bidCtrl', function ($scope, $injector, $timeout, $http, $filter, $window, $checkBoxService, $location, $CommonService, $routeParams, $rootScope) {
    $scope.showPositions = DIC_CONSTANT.getValueAndName("invest", "bid_show_position");
    $scope.investpositions = DIC_CONSTANT.getValueAndName("invest", "bid_invest_position");
    $scope.assureTypes = DIC_CONSTANT.getValueAndName("invest", "bid_assure_type");
    $scope.types = DIC_CONSTANT.getValueAndName("invest", "bid_type");
    $scope.loanUses = DIC_CONSTANT.getValueAndName("invest", "bid_loan_use");
    $scope.punishStates = DIC_CONSTANT.getValueAndName("invest", "bid_punish_state");
    $scope.allowCoupons = [{value: 0, name: "红包"}, {value: 1, name: "加息券"}];
    $scope.companyIds=[];
    $scope.bidInfo = {};//页面初始化对象
    $scope.bidInfo.buyHouseFlag = 0;
    var arr = "1,2";//初始化checkbox选中状态
    //注入字符串转数组并且判断是否在其中的函数
    $scope.strConvertArrContains = $CommonService.strConvertArrContains;
    $routeParams.id = $routeParams.param;

    //全局保存一份操作员已经选择的用户
    window.userHasSelected = 0;

    function getProductsData(callBackFun) {
        //获取产品列表信息
        $CommonService.httpPost({'id': 0}, CONFIG.interface.findBidInfos)
            .success(function (data) {
                $scope.bidProducts = data.params.bidProducts;
                if (callBackFun != null) {
                    return callBackFun();
                }
            }).error(function () {
            commonUtil.createSimpleNotify("系统繁忙!", ALERT_ERR);
        });
    }

    //获取
    getProductsData();
    //定义找到产品的函数
    var findProduct = function (x, doLogic) {
        var findObj = null;
        var findFun = function () {
            angular.forEach($scope.bidProducts, function (data, index, array) {
                //data等价于array[index]
                if (data.id == x) {
                    findObj = data;
                }
            });
            //设置全局变量
            window.bidDeadline = findObj.bidDeadline;
            //设置产品描述
            $scope.productDesc = findObj.description
            //设置相隔天数
            $scope.bidDeadline = findObj.bidDeadline
            $scope.biddRepaymentWays = DIC_CONSTANT.getDicObjInLimitValues("invest", "bid_repayment", findObj.repaymentway);

        }
        if ($scope.bidProducts == null || $scope.bidProducts.length == 0) {
            //alert('后台获取')
            //获取
            getProductsData(findFun);
        } else {
            findFun();
        }

        return findObj;
    };

    //默认有启用的规则
    $scope.hasRecommendEarnEnable = true;
    //请求此时的好友推荐信息
    $CommonService.httpPost({'id': 0}, CONFIG.interface.hasRecommendEarnEnable)
        .success(function (data) {
            $scope.hasRecommendEarnEnable = data.resMsg;
        }).error(function () {
        commonUtil.createSimpleNotify("系统繁忙!", ALERT_ERR);
    });

    /**
     * 初始化推荐状态
     */
    function initRecommendState(value) {
        //初始化是否允许可以进行好友推荐
        if ($scope.hasRecommendEarnEnable) {
            $scope.bidInfo.recommendState = value;
            $("#recommendStateSpan").removeClass("hide");
        } else {
            //当前没有启用任何规则
            $scope.bidInfo.recommendState = 0;
            $("#recommendStateSpan").addClass("hide");

        }
    }

    //如果是修改标的进入此方法
    if ($routeParams.id != 'undefined' && $routeParams.id != undefined) {
        $scope.showPage = false;//用于判断是新增还是修改的标识 false:修改
        //根据ID，查询标的信息 bidInfo.advanceRepayState
        var params = {'bidInfoId': $routeParams.id};
        $CommonService.httpPost(params, CONFIG.interface.findBidInfoDetails)
            .success(function (data) {
                $("#bidId").val($routeParams.id);
                console.log(data.resMsg)
                $scope.bidInfo = data.resMsg;
                //初始化推荐状态
                initRecommendState(data.resMsg.recommendState);
                $scope.bidInfo.loanUse = data.resMsg.loanUse + '';
                if (commonUtil.isNotEmpty(data.resMsg.receiptUserName)) {
                    $scope.bidInfo.receiptUserName = "[" + data.resMsg.receiptUserName + "]";
                }
                if (commonUtil.isNotEmpty(data.resMsg.borrowerName)) {
                    $scope.bidInfo.borrowerName = "[" + data.resMsg.borrowerName + "]";
                }
                $scope.bidInfo.bidProductId = data.resMsg.bidProductId + '';
                $scope.bidScheme = data.resMsg.bidScheme == 0 ? "平均金额招标" : "最低金额招标";
                //设置匹配类型
                $scope.bidInfo.matchType = data.resMsg.matchType;

                //alert('advanceRepayState:'+$scope.bidInfo.advanceRepayState)
                //设置是否允许提前还本
                $scope.bidInfo.advanceRepayState = data.resMsg.advanceRepayState;
                //设置投资步长
                $scope.bidInfo.stepValue = data.resMsg.stepValue;

                //设置逾期是否罚款
                $scope.bidInfo.punishState = data.resMsg.punishState;
                // 设置交易所信息
                $scope.bidInfo.bourseFlg = data.resMsg.bourseFlg;
                //如果有逾期罚款设置罚款相关字段
                if($scope.bidInfo.punishState==1){
                    $scope.bidInfo.advanceServiceRate = data.resMsg.advanceServiceRate;
                    $scope.bidInfo.liquidatedDamagesRate = data.resMsg.liquidatedDamagesRate;
                }

                //是否有允许债权转让
                $scope.creditorState = data.resMsg.creditorState;
                if($scope.creditorState==1){
                    //如果允许债券转让，回显债权转让相关字段
                    $scope.bidInfo.creditorDays=data.resMsg.creditorDays;
                    $scope.bidInfo.mostTransferCount=data.resMsg.mostTransferCount;
                    $scope.bidInfo.dealRepayDays=data.resMsg.dealRepayDays;

                    $scope.bidInfo.convertRateStart=data.resMsg.convertRateStart;
                    $scope.bidInfo.convertRateEnd=data.resMsg.convertRateEnd;
                    $scope.bidInfo.overflowRateStart=data.resMsg.overflowRateStart;
                    $scope.bidInfo.overflowRateEnd=data.resMsg.overflowRateEnd;
                }



                //设置标的属性
                $scope.bidInfo.type = data.resMsg.type + '';
                //设置交易所信息
                $scope.bidInfo.bourseFlg = data.resMsg.bourseFlg + '';
                //显示总付利息
                $("#interestAmountSpan").removeClass("hide");

                //格式化时间addBidProduct
                $scope.bidInfo.startTime = dateUtil.dateTime(data.resMsg.startTime);
                $scope.bidInfo.endTime = dateUtil.dateTime(data.resMsg.endTime);
                $scope.termUnit = DIC_CONSTANT.getName("invest", "bid_term_unit", data.resMsg.termUnit)
                //图片回显
                $("#Pic_imgUrl").displayBack("Pic_imgUrl", {}, CONFIG.CONSTANTS.IMG_URL_ROOT + $scope.bidInfo.imgUrl);
                $("#Pic_legalFiles").displayBack("Pic_legalFiles", {}, CONFIG.CONSTANTS.IMG_URL_ROOT + $scope.bidInfo.legalFiles);
                $("#Pic_printImgurl").displayBack("Pic_printImgurl", {}, CONFIG.CONSTANTS.IMG_URL_ROOT + $scope.bidInfo.printImgurl);

                //checkbox回显
                $scope.bidInfo.showPosition = data.resMsg.showPosition;
                $scope.bidInfo.assureType = data.resMsg.assureType;
                $scope.bidInfo.investPosition = data.resMsg.investPosition;
                $checkBoxService.showCheckInfo($scope.investpositions, $scope.bidInfo.investPosition);
                $checkBoxService.showCheckInfo($scope.assureTypes, $scope.bidInfo.assureType);
                $checkBoxService.showCheckInfo($scope.showPositions, $scope.bidInfo.showPosition);
                //redio还款方式的回显
                findProduct($scope.bidInfo.bidProductId);
                //设置总付利息
                $scope.interestAmount = data.resMsg.interestAmount
                $scope.bidInfo.buyHouseFlag = 0;

                //如果是普通产品，初始化提前还款相关内容
                if (data.resMsg.productType == 0) {
                    //如果不允许提前还本,清空提前还本的值
                    if ($scope.bidInfo.advanceRepayState == 0) {
                        $scope.cleanRepayCapArea();
                    }
                    //如果不允许允许债券转让,清除债券转让相关
                    if ($scope.creditorState == 0) {
                        $scope.cleanCreditorStateAbout()
                    }
                    //展示提前还本字段
                    $("#repayCapArea").removeClass("hide");
                    //如果是普通产品，显示匹配类型
                    $("#matchTypeSpan").removeClass("hide");

                } else {
                	if(data.resMsg.productType == 6 || data.resMsg.productType == 9){
	               		 $("#companySpan").removeClass("hide");
	               		 $scope.bidInfo.buyHouseFlag = 1;
	               		//加载企业员工
	               		$http.post(CONFIG.interface.enterpriseUserList, {}).success(function (data) {
	               				$scope.companyIds = data;
	               		}).error(function () {});
	                    $scope.bidInfo.companyId = data.resMsg.companyId + '';
                 	}
                    //非普通产品，不允许提前还本，所以此处手动设置0
                    $scope.bidInfo.advanceRepayState = 0;
                    $scope.cleanRepayCapArea();

                    if($scope.creditorState!=1){
                        $scope.cleanCreditorStateAbout();
                    }

                }

            });
    } else {
        $scope.creditorState = 0;
        //用于判断是新增还是修改的标识 true:新增
        $scope.showPage = true;

        //初始化可以选择的还款方式
        $scope.biddRepaymentWays = null;
        init($scope.bidInfo);//radio初始化
        //初始化推荐状态
        initRecommendState(1);
        $checkBoxService.showCheckInfo($scope.investpositions, arr);
        $checkBoxService.showCheckInfo($scope.showPositions, arr);
    }

    //修改产品相关参数
    function changeProductAbout(product) {
        //金额范围
        $scope.bidInfo.amountMin = product.amountMin
        $scope.bidInfo.amountMax = product.amountMax
        //借款期限
        $scope.bidInfo.termValueMin = product.termValueMin
        $scope.bidInfo.termValueMax = product.termValueMax
        //初始化投资步长
        $scope.bidInfo.stepValue=100;
        //招标方案
        $scope.bidInfo.bidScheme = product.bidScheme;
        $scope.bidScheme = product.bidScheme == 0 ? "平均金额招标" : "最低金额招标";
        $scope.bidInfo.bidSchemeValue = product.bidSchemeValue
        //年利率范围
        $scope.bidInfo.rateMin = product.rateMin
        $scope.bidInfo.rateMax = product.rateMax
        //还款方式：已经默认修改了
        //提示：已经默认修改了
        //总付利息
        //$scope.interestAmount=product.interestAmount
        //产品描述：已经默认修改了

    }

    // 清空是否允许提前还本字段
    $scope.cleanRepayCapArea = function () {
        //清空提前还本的值
        $scope.bidInfo.returnCapWay = 1;
        $scope.bidInfo.returnCapDays = null;
    }
    // 清空是否逾期罚款相关
    $scope.cleanPunishStateAbout = function () {
        //清空提前还本的值
        $scope.bidInfo.advanceServiceRate = '';
        $scope.bidInfo.liquidatedDamagesRate = '';
    }

    //清空债券转让相关字段
    $scope.cleanCreditorStateAbout = function () {
        $scope.bidInfo.creditorDays = '';
        $scope.bidInfo.mostTransferCount = '';
        $scope.bidInfo.dealRepayDays = '';
        $scope.bidInfo.convertRateStart = '';
        $scope.bidInfo.convertRateEnd = '';
        $scope.bidInfo.overflowRateEnd = '';
        $scope.bidInfo.overflowRateStart = '';
    }
    //产品选择事件
    $scope.change = function (x) {
        //找到对应的产品
        var dataTemp = findProduct(x);
        //$scope.bidInfo=dataTemp;
        //只改变跟产品相关的值
        changeProductAbout(dataTemp)
        $scope.bidInfo.bidProductId = dataTemp.id + '';
        $scope.termUnit = DIC_CONSTANT.getName("invest", "bid_term_unit", dataTemp.termUnit);
        init($scope.bidInfo);
        $checkBoxService.showCheckInfo($scope.investpositions, arr);
        $checkBoxService.showCheckInfo($scope.showPositions, arr);
        //清空时间
        $("#startDate").val('')
        $("#endDate").val('')
        $scope.bidInfo.biddRepaymentWay = $scope.biddRepaymentWays[0].value;
        $("#companySpan").addClass("hide");
        $scope.bidInfo.buyHouseFlag = 0;
        //判断是否为普通产品，如果是普通产品，那么应该显示提前还款相关字段
        if (dataTemp.type == 0) {
            //展示提前还本字段
            $("#repayCapArea").removeClass("hide");
            //初始化相关的变量
            $scope.bidInfo.returnCapWay = 1;
            //如果是普通产品，显示匹配类型
            $("#matchTypeSpan").removeClass("hide");

        } else {
        	if(dataTemp.type == 6 || dataTemp.type == 9){
        	    $scope.bidInfo.buyHouseFlag = 1;
        		 $("#companySpan").removeClass("hide");
        		//加载企业员工
        		$http.post(CONFIG.interface.enterpriseUserList, {}).success(function (data) {
        				$scope.companyIds = data;
        		}).error(function () {});
        	}
            if (!$("#repayCapArea").hasClass("hide")) {
                $("#repayCapArea").addClass("hide");
            }
            $scope.bidInfo.returnCapWay = 1;
            if (!$("#matchTypeSpan").hasClass("hide")) {
                $("#matchTypeSpan").addClass("hide");
            }
            $scope.bidInfo.matchType = 0;

        }

    };

    //表单提交事件
    var $validationProvider = $injector.get('$validation');
    $scope.submit = function (form) {
        $("#showPosition").val($checkBoxService.check($scope.showPositions));
        $("#assureType").val($checkBoxService.check($scope.assureTypes));
        $("#investPosition").val($checkBoxService.check($scope.investpositions));
        $("#bidId").val(null);
        $validationProvider.validate(form).success(function () {
            //显示平台与投资平台校验
            if (!$CommonService.checkStrEq($("#showPosition").val(), $("#investPosition").val())) {
                commonUtil.createSimpleNotify("显示平台与投资平台必须匹配!", ALERT_WARN);
                $("#showpositionError").css('display', 'block').html("显示平台与投资平台必须匹配！");
                return;
            }
            if (commonUtil.isEmpty($("#borrowerId").val())) {
                commonUtil.createSimpleNotify("请选择借款人信息!", ALERT_WARN);
                $("#borrowerError").css('display', 'block').html("请选择借款人信息！");
                return;
            }
            if (commonUtil.isEmpty($scope.bidInfo.companyId) &&  $scope.bidInfo.buyHouseFlag == 1) {
                $("#companyIdError").css('display', 'block').html("选择企业账户信息！");
                return;
            }
            
            if($("#startDate").val()=='--'){
                $("#startDate").val(null)
            }
            if($("#endDate").val()=='--'){
                $("#endDate").val(null)
            }
            // //校验投标与放标时间
            // if (commonUtil.isEmpty($("#startDate").val())) {
            //     commonUtil.createSimpleNotify("请选择投标开始时间!", ALERT_WARN);
            //     return;
            // }
            //
            // if (commonUtil.isEmpty($("#endDate").val())) {
            //     commonUtil.createSimpleNotify("请选择投标结束时间!", ALERT_WARN);
            //     return;
            // }

            //如果是普通产品，检查提前还本字段
            if ($scope.bidInfo.advanceRepayState == 1) {
                if (commonUtil.isEmpty($("#returnCapDays").val())) {
                    commonUtil.createSimpleNotify("请填写借款满n天后可提前还款字段!", ALERT_WARN);
                    return;
                }
                if ($("#returnCapDays").val() < 0) {
                    commonUtil.createSimpleNotify("满n天后为正数!", ALERT_WARN);
                    return;
                }
            }
            var params = $("#bidForm").serializeObject();
            if(String(params.companyId).indexOf("number") != -1){
                params.companyId = 0;
            }
            $http.post(CONFIG.interface.saveBidInfos, params).success(function (data) {
                console.log(data);
                if (data.resStatus == CONFIG.CONSTANTS.SUCCESS_STATE) {
                    commonUtil.createNotifyAndRedirect("添加成功!", ALERT_SUC, $location, "/loanBidInfoList");
                } else {
                    commonUtil.createNotifyAndRedirect(data.resMsg, ALERT_ERR, $location, "/addBidInfo");
                }
            }).error(function () {
                commonUtil.createNotifyAndRedirect("添加失败!", ALERT_ERR, $location, "/loanBidInfoList");
            });
        }).error(function () {
        });
    };
    //更新表单
    $scope.update = function (form) {
        $("#bidId").val($routeParams.id);
        $("#showPosition").val($checkBoxService.check($scope.showPositions));
        $("#assureType").val($checkBoxService.check($scope.assureTypes));
        $("#investPosition").val($checkBoxService.check($scope.investpositions));
        $validationProvider.validate(form).success(function () {
            //显示平台与投资平台校验
            if (!$CommonService.checkStrEq($("#showPosition").val(), $("#investPosition").val())) {
                commonUtil.createSimpleNotify("显示平台与投资平台必须匹配!", ALERT_WARN);
                $("#showpositionError").css('display', 'block').html("显示平台与投资平台必须匹配！");
                return;
            }
            if (commonUtil.isEmpty($scope.bidInfo.companyId) &&  $scope.bidInfo.buyHouseFlag == 1) {
                $("#companyIdError").css('display', 'block').html("选择企业账户信息！");
                return;
            }
            //时间处理
            if($("#startDate").val()=='--'){
                $("#startDate").val(null)
            }
            if($("#endDate").val()=='--'){
                $("#endDate").val(null)
            }
            var params = $("#bidForm").serializeObject();
            if(String(params.companyId).indexOf("number") != -1){
                params.companyId = 0;
            }
            if(!commonUtil.isEmpty(params.printImgurl)){
                params.printImgurl = CONFIG.CONSTANTS.IMG_URL_ROOT + params.printImgurl;
            }
            $http.post(CONFIG.interface.updateBidInfos, params).success(function (data) {
                console.log(data);
                if (data.resStatus == CONFIG.CONSTANTS.SUCCESS_STATE) {
                    commonUtil.createNotifyAndRedirect("更新成功!", ALERT_SUC, $location, "/loanBidInfoList");
                }
            }).error(function () {
                commonUtil.createNotifyAndRedirect("更新失败!", ALERT_ERR, $location, "/loanBidInfoList");
            });
        }).error(function () {

        });
    };
    $scope.radioChecked = function (value) {
        //刷新已经选择的用户
        $("#extendsValues").val(encryptAndDecryptUtil.encrypt(JSON.stringify({notIncludeIds: window.userHasSelected})));
        $("#mDataTable").DataTable().search("").draw();

        $("#usertype").val(value);
        $("#userInfoModal").modal('show');
    }
    //返回事件
    $scope.back = function () {
        $location.path("/loanBidInfoList");
    }
    //图片预览
    $scope.picPreview = function (id, opts) {
        $CommonService.picPreview(id, opts);
    };
    //图片异步上传
    $scope.upload = function (id, opts) {
        $CommonService.upload(id, opts);
    };
});

//初始化radio选中
function init(bidObject) {
    bidObject.withholdState = 2;
    bidObject.maturityRemind = 1;
    bidObject.recommendState = 0;
    bidObject.reserveInterest = 0;
    bidObject.matchType = 0;
    bidObject.allowCoupon = 1;
    bidObject.givingPointState = 0;
    bidObject.punishState = 0;
    bidObject.biddRepaymentWay = 1;
    bidObject.advanceRepayState = 0;
    bidObject.bourseFlg = 0;

}
