//积分商品分类维护的controller
app.controller('pointProductList', function ($scope) {
    //状态下拉框
    $("#productState").append(DIC_CONSTANT.getOption('point_product', 'state'));

    //商品审核
    //验证审核积分

    //如果是拒绝积分需要填写拒绝理由
    $scope.addResonArea = function (value) {
        if (value == 3) {
            $("#refuseSpan").html('拒绝理由：<input id="checkReasonArea" name="refuseCause" type="text" class="confirmChildren"/> <br><br>');
        } else {
            $("#refuseSpan").html('');
        }

    }

    function validateCheck() {
        if ($("input[name='state']:checked").val() == 3) {
            //验证审核积分原因
            if (commonUtil.isEmpty($("#checkReasonArea").val())) {
                $("#productCheckError").text('请填写拒绝积分原因!');
                return false;
            }
        }
        return true;
    }

    //创建审核积分弹框
    $scope.createCheckPanel = function (id) {
        if (id == null) {
            id = dataTableUtil.getCheckedVal();
        }
        if (commonUtil.isEmpty(id)) {
            commonUtil.createSimpleNotify("请至少选择一个商品!", ALERT_WARN);
            return;
        }
        $("#ids").attr("name", "checkIds");
        $("#ids").val(id);
        var contents = '';
        contents += '审核结果：<input name="state" type="radio"  value="1" checked="checked" onclick="angular.element(this).scope().addResonArea(' + 0 + ')"/>通过   ' +
            '<input  name="state" type="radio"   value="3" onclick="angular.element(this).scope().addResonArea(' + 3 + ')"/>拒绝<br><br>';
        contents += "<span id='refuseSpan'></span>";
        contents += '<span id="productCheckError" style="color: red"></span>'
        commonUtil.createCustomConfirmBox("审核积分", contents, CONFIG.interface.checkPointProduct, validateCheck, commonUtil.customConfirmStandardAfterExecuteFn);
    }


});
app.controller('pointProduct', function ($scope, $injector, $http, $location, $routeParams, $CommonService) {
    /*************************初始化****************************************/
    //初始化提交btn
    $scope.isSaveBtn = commonUtil.isEmpty($routeParams.id) ? true : false;
    //是否是修改的状态
    $scope.isUptate = commonUtil.isEmpty($routeParams.isUptate) ? false : $routeParams.isUptate;

    //刷新菜单树
    function refreshTree() {
        //获取所有的菜单数据
        $http.post(CONFIG.interface.listCategories, {}).success(function (data) {
            $scope.data = data.resMsg;
        }).error(function () {
            commonUtil.createSimpleNotify("查询失败，请重试", ALERT_ERR);
        });
    };
    $scope.toggle = function (scope) {
        scope.toggle();
    };
    //隐藏限时商品的时候清除填写的限时字段
    $scope.cleanFlashSaleArea = function () {
        $scope.product.discountPoint = null;
        $scope.product.showTimeStart = '';
        $scope.product.showTimeEnd = '';
    }

    //图片预览
    $scope.picPreview = function (id, opts) {
        $CommonService.picPreview(id, opts);
    };
    //图片异步上传
    $scope.upload = function (id, opts) {
        $CommonService.upload(id, opts);
    };
    //返回事件
    $scope.back = function () {
        $location.path("/pointProductList");
    }
    //选择类目事件
    $scope.choiceCates = function () {
        //初始化类目选择
        $("#catesModal").modal('show');
    }
    //选择商品类目
    $scope.selectCate = function (scope) {
        var nodeValue = scope.$modelValue;
        $scope.product.productCategoryName = nodeValue.title;
        $scope.product.productCategoryId = nodeValue.id;
        $("#catesModal").modal('hide');
    };

    $scope.toggle = function (scope) {
        scope.toggle();
    };

    function uuidv4() {
        return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
            var r = Math.random() * 16 | 0,
                v = c === 'x' ? r : r & 0x3 | 0x8;
            return v.toString(16);
        });
    }

    //加载菜单
    refreshTree();

    function saveAddFileToArray(file, saveKay) {
        if (dropZopne.savedFiles == null) {
            dropZopne.savedFiles = [];
        }
        file.saveKey = saveKay;
        dropZopne.savedFiles.push(saveKay)
    }

    //初始化配送方式
    // $("#sendWay").append(DIC_CONSTANT.getOption("point_product", "send_way",null,null,null,false));
    $scope.sendWays = DIC_CONSTANT.getValueAndName("point_product", "send_way")
    //初始化富文本编辑器
    var goodsDetailsEditor = elementOperateUtil.createKindEditor(CONFIG.PLATFORMS.HKJF, CONFIG.FILEPATHS.POINT_PRODUCT_FILEPATH, 'goodsDetails');
    var prductInfoEditor = elementOperateUtil.createKindEditor(CONFIG.PLATFORMS.HKJF, CONFIG.FILEPATHS.POINT_PRODUCT_FILEPATH, 'prductInfo');
    if ($scope.isSaveBtn || $scope.isUptate) {
        //初始化图片编辑框
        var dropZopne = new Dropzone($("#dropz").get(0), {
            url: "pointProductController/saveAndResizeImg?platform=hkjf&filePath=pointmall/product&fileType=image", //必须填写
            method: "post",  //也可用put
            paramName: "unUpFile", //默认为file
            maxFiles: 10,//一次性上传的文件数量上限
            addRemoveLinks: true,
            maxFilesize: 20, //MB
            acceptedFiles: ".jpg,.gif,.png", //上传的类型
            previewsContainer: "#dropz", //显示的容器
            parallelUploads: 3,
            dictMaxFilesExceeded: "您最多只能上传10个文件！",
            dictResponseError: '文件上传失败!',
            dictInvalidFileType: "你不能上传该类型文件,文件类型只能是*.jpg,*.gif,*.png。",
            dictFallbackMessage: "浏览器不受支持",
            dictFileTooBig: "文件过大上传文件最大支持.",
            dictDefaultMessage: "点击此区域空白处或者把图片拖拽到此处.",
            dictRemoveFile: "移除文件",
            addSetFirst: true,
            //previewTemplate: document.querySelector('#preview-template').innerHTML,//设置显示模板
            init: function () {
                this.on("addedfile", function (file, data) {
                    //上传文件时触发的事件
                    //console.log(file.name)
                });
                this.on("success", function (file, data) {
                    if (data.resStatus == CONFIG.CONSTANTS.SUCCESS_STATE) {
                        //上传完成后触发的方法
                        saveAddFileToArray(file, data.params.saveKey);
                    }

                    if (data.resStatus == 999) {
                        //上传失败，删除文件
                        commonUtil.createSimpleNotify("保存图片失败，请重试", ALERT_ERR)
                        dropZopne.removeFile(file)
                    }
                });
                this.on("error", function (file) {
                    commonUtil.createSimpleNotify("上传失败，重试", ALERT_ERR);
                    //console.log(this)
                    this.removeFile(file)
                });

                this.on("removedfile", function (file) {
                    var sks = file.saveKey.split("-");
                    var tempsk = null;
                    if (sks.length == 1) {
                        tempsk = sks[0];
                    } else {
                        tempsk = sks[1];
                    }
                    //删除文件时触发的方法
                    $.each(dropZopne.savedFiles, function (index, item) {
                        if (item == tempsk) {
                            dropZopne.savedFiles.splice(index, 1);
                        }
                    });
                    //判断删除的是否为首图
                    if (Dropzone.firstImg == tempsk) {
                        Dropzone.firstImg = null;
                    }
                });
            }
        });
    }


    //区分修改或者保存
    if ($scope.isUptate) {
        //请求原来的数据
        $http.post(CONFIG.interface.pointProductInfo, {id: $routeParams.id}).success(function (data) {

            $scope.product = data.resMsg;
            //处理发送方式
            $scope.product.sendWay = $scope.product.sendWay + '';
            $scope.product.showTimeStart = dateUtil.dateTime(data.resMsg.showTimeStart);
            $scope.product.showTimeEnd = dateUtil.dateTime(data.resMsg.showTimeEnd);

            //处理图片回显
            $.each($scope.product.imgs, function (index, item) {
                // Create the mock file:
                var mockFile = {
                    name: item, //需要显示给用户的图片名
                    size: 12345, //图片尺寸
                    type: 'image/jpeg',//图片文件类型
                    upload: {},
                    status: Dropzone.SUCCESS
                };

                //模拟文件添加操作
                saveAddFileToArray(mockFile,item);
                mockFile.upload.uuid = uuidv4();
                var isFirst = (item == $scope.product.firstImg)
                dropZopne.emit("addedfile", mockFile, isFirst);
                dropZopne.emit("thumbnail", mockFile, CONFIG.CONSTANTS.IMG_URL_HKJF_ROOT + item);
                dropZopne.emit("complete", mockFile);
                dropZopne.files.push(mockFile)

            });
            //设置商品首图
            Dropzone.firstImg = $scope.product.firstImg;
            //处理富文本编辑
            goodsDetailsEditor.html($scope.product.goodsDetails);
            prductInfoEditor.html($scope.product.prductInfo);


        }).error(function () {
            commonUtil.createNotifyAndRedirect("请求数据失败，请重试", ALERT_ERR, $location, "/pointProductList");
        });
    }

    if ($scope.isSaveBtn) {
        $scope.product = {};
        //是否是推荐商品
        $scope.product.recommend = 1;
        //是否是推荐商品
        $scope.product.recommend = 1;
        //初始化是否在ios端展示
        $scope.product.iosShow = 1;
        //初始化展示开始时间
        $scope.product.showTimeStart = null;
        //初始化展示结束时间
        $scope.product.showTimeEnd = null;
        //初始化是否为限时抢购商品
        $scope.product.flashSale = 0;
        Dropzone.firstImg = null;

    }

    //表单提交事件(新增或者修改)
    var $validationProvider = $injector.get('$validation');
    $scope.submit = function (form, type) {
        $validationProvider.validate(form).success(function () {
            var params = $("#productForm").serializeObject();
            //检验各项参数
            //是否选择商品分类
            if ($scope.product.productCategoryId == null) {
                commonUtil.createSimpleNotify("请选择商品分类", ALERT_ERR)
                return;
            }
            if (commonUtil.isEmpty(params.sendWay)) {
                commonUtil.createSimpleNotify("请选择商品配送方式", ALERT_ERR)
                return;
            }
            if (commonUtil.isEmpty(goodsDetailsEditor.html())) {
                commonUtil.createSimpleNotify("请填写产品详情", ALERT_ERR)
                return;
            }
            if (commonUtil.isEmpty(prductInfoEditor.html())) {
                commonUtil.createSimpleNotify("请填写产品信息", ALERT_ERR)
                return;
            }
            //检查文件
            if (dropZopne.savedFiles == null || dropZopne.savedFiles.length == 0) {
                commonUtil.createSimpleNotify("请至少上传一张图片", ALERT_ERR)
                return;
            }
            //检查首图
            if (Dropzone.firstImg == null) {
                commonUtil.createSimpleNotify("请选择商品首图", ALERT_ERR)
                return;
            }
            //其他需要的参数
            params.productCategoryId = $scope.product.productCategoryId;
            params.goodsDetails = goodsDetailsEditor.html();
            params.prductInfo = prductInfoEditor.html();

            if (type == 'update') {
                //添加id
                params.id = $scope.product.id;
            }

            //检查活动相关字段
            if ($scope.product.flashSale == 1) {
                if (commonUtil.isEmpty(params.discountPoint)) {
                    commonUtil.createSimpleNotify("请填写活动时候积分", ALERT_ERR)
                    return;
                }

                if (commonUtil.isEmpty(params.showTimeStart)) {
                    commonUtil.createSimpleNotify("请填写商品展示开始时间", ALERT_ERR)
                    return;
                }
                if (commonUtil.isEmpty(params.showTimeEnd)) {
                    commonUtil.createSimpleNotify("请填写商品展示结束时间", ALERT_ERR)
                    return;
                }

            } else {
                //清空可能已经填写的字段
                params.discountPoint = null;
                params.showTimeStart = null;
                params.showTimeEnd = null;
            }

            //设置所上传的图片
            params.imgs = dropZopne.savedFiles;
            //商品首图
            params.firstImg = Dropzone.firstImg;

            var alertStr = '';
            var reqeustUrl = null;
            if (type == 'update') {
                reqeustUrl = CONFIG.interface.updatePointProduct;
            } else {
                reqeustUrl = CONFIG.interface.savePointProduct;
            }
            $http.post(reqeustUrl, params).success(function (data) {
                if (data.resStatus == CONFIG.CONSTANTS.SUCCESS_STATE) {
                    if (type == 'update') {
                        alertStr = "修改成功";
                    } else {
                        alertStr = "添加成功!"
                    }
                    commonUtil.createNotifyAndRedirect(alertStr, ALERT_SUC, $location, "/pointProductList");
                } else {
                    commonUtil.createNotifyAndRedirect(data.resMsg, ALERT_ERR, $location, "/pointProductAdd");
                }
            }).error(function () {
                if (type == 'update') {
                    alertStr = "修改失败"
                } else {
                    alertStr = "添加失败!";
                }

                commonUtil.createNotifyAndRedirect(alertStr, ALERT_ERR, $location, "/pointProductList");
            });

        }).error(function (msg,data) {
            commonUtil.createSimpleNotify("操作失败", msg);
        });
    };

})
