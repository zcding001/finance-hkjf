var dataTableUtil = {};
(function () {
	'use strict';
    dataTableUtil.LANGUAGE = {
        "sLengthMenu" : "每页 _MENU_ 条",
        "sInfo" : "从 _START_ 到 _END_ /共 _TOTAL_ 条数据",
        "sInfoEmpty" : "没有数据",
        "sInfoFiltered" : "(从 _MAX_ 条数据中检索)",
        "sZeroRecords" : "没有检索到数据",
        "oPaginate" : {
            "sFirst" : "首页",
            "sPrevious" : "前一页",
            "sNext" : "后一页",
            "sLast" : "尾页"
        }
    };
    /**checkbox常量**/
    var _CHECKBOX = {
        title : "CHECKBOX",
        orderable : false,
        class : "dt-checkbox",
        data : null
    };
    /**
     * 判断dataTable记录rowConditionAttr属性的值是否包含在condition中
     * @param condition   		    用于判断是否为该记录创建复选框的值，多个值用","分隔
     * @param rowConditionAttr   	属性
     * @param row   		        数据记录
     */
    var _isIncludes = function (condition,rowConditionAttr,row) {
        var conditionArr = condition.split(",");
        return conditionArr.includes(row[rowConditionAttr].toString());
    };
    /**
     * 创建dataTable记录的复选框
     * @param rowAttr   		    数据记录的属性-复选框的值
     */
    dataTableUtil.createCheckBox = function (rowAttr) {
        return jQuery.extend({
            render : function(data, type, row) {
                return '<input type="checkbox" class="iCheck" name="cb-children"' + ' value="' + row[rowAttr] + '">';
            }
        },_CHECKBOX);
    };
    /**
     * 符合条件创建dataTable记录的复选框，不符合条件的不创建复选框
     * @param rowAttr   		    数据记录的属性-复选框的值
     * @param condition   		    用于判断是否为该记录创建复选框的值，多个值用","分隔
     * @param rowConditionAttr     数据记录的属性-用于判断该属性值是否包含于condition中
     */
    dataTableUtil.createCheckBoxByCondition = function (rowAttr,condition,rowConditionAttr) {
        return jQuery.extend({
            render : function(data, type, row) {
                if (_isIncludes(condition,rowConditionAttr,row)){
                    return '<input type="checkbox" class="iCheck" name="cb-children" value="' + row[rowAttr] + '">';
                }
                return '';
            }
        },_CHECKBOX);
    };
    /**
     * 符合条件的记录不创建复选框，不符合的条件的创建复选框
     * @param rowAttr   		    数据记录的属性-复选框的值
     * @param condition   		    用于判断是否为该记录创建复选框的值，多个值用","分隔
     * @param rowConditionAttr     数据记录的属性-用于判断该属性值是否包含于condition中
     */
    dataTableUtil.createNoneCheckBoxByCondition = function (rowAttr,condition,rowConditionAttr) {
        return jQuery.extend({
            render : function(data, type, row) {
                if (!_isIncludes(condition,rowConditionAttr,row)){
                    return '<input type="checkbox" class="iCheck" name="cb-children" value="' + row[rowAttr] + '">';
                }
                return '';
            }
        },_CHECKBOX);
    };
    /**
     * 将dataTables复选框所有选中的值赋值到objectId对应的对象中
     * @param   objectId    赋值对象id
     * @returns {boolean}
     */
    dataTableUtil.setCheckedValToObject = function (objectId) {
        var checkedArr = this.getCheckedVal();
        if (commonUtil.isNotEmpty(checkedArr)){
            if($("#" + objectId)){
                $("#" + objectId).val(checkedArr);
            }
            return true;
        }
        return false;
    };
    /**
     * 获得dataTables复选框所有选中的值
     * @param   dataTableId     dataTable的id，默认为mDataTable
     * @returns 选中记录值的集合
     */
    dataTableUtil.getCheckedVal = function (dataTableId) {
        if (commonUtil.isEmpty(dataTableId)){
            dataTableId = "mDataTable";
        }
        var checkedArr = [];
        $("#" + dataTableId).find('input[name="cb-children"]:checked').each(function(){
            checkedArr.push($(this).val());
        });
        return checkedArr;
    };
    /**
     * 判断数据表格是否含有checkbox
     * @param  columns  构建dataTable的列
     * @return boolean
     */
    function _getCheckBoxFlag(columns){
        var cbFlag = false;
        //复选框时设置head上的复选框
        for(var i in columns){
            if(columns[i].title == "CHECKBOX"){
                cbFlag = true;
                break;
            }
        }
        return cbFlag;
    };
    /**
     * 初始化dataTable中CheckBox及其操作
     * @param   columns    dataTable对应的列
     * @param   tableId    用户展示dataTable的id
     */
    function _initDTCheckBox(columns,tableId) {
        if(_getCheckBoxFlag(columns)){
            //复选框时设置head上的复选框
            for(var j in columns){
                if(columns[j].title == "CHECKBOX"){
                    $("#"+tableId+" thead tr th:eq(0)").html('<input type="checkbox" name="cb-check-all">');
                    break;
                }
            }
            //复选框事件
            $("#" + tableId).on("change",":checkbox",function() {
                if ($(this).is("[name='cb-check-all']")) {
                    //全选
                    $(":checkbox",$("#" + tableId)).prop("checked",$(this).prop("checked"));
                }else{
                    //一般复选
                    var checkbox = $("tbody :checkbox",$("#" + tableId));
                    $(":checkbox[name='cb-check-all']",$("#" + tableId)).prop('checked', checkbox.length == checkbox.filter(':checked').length);
                }
            });
            //清空复选框全选状态
            $(":checkbox[name='cb-check-all']").prop('checked', false);
        }
    };
    /**
     * 跳转到dataTable数据列表指定页
     * @param   tableId    用户展示dataTable的id
     */
    dataTableUtil.toPage = function(tableId){
        var pages = $('#'+tableId).DataTable().page.info().pages;
        var toPage = $("#toPage").val()
        if(toPage > pages || toPage < 1){
            //隐藏模态框，如果有的话
            $("#userInfoModal").modal('hide');
            commonUtil.createSimpleNotify("请输入正确的页数", ALERT_WARN);
            return;
        }
        $('#'+tableId).DataTable().page(toPage - 1).draw('page');
    }
    /**
     * 初始化dataTable的分页组件
     * @param   tableId    用户展示dataTable的id
     */
    function _initDTPage(tableId) {
        //添加跳转到指定页方法
        $("#"+tableId+"_last").after("<li class='paginate_button' aria-controls='"+tableId+"' tabindex='0'" +
            " id='"+tableId+"_goPage'><input type='text' style='width:35px;float:left;height: 34px;margin-left:" +
            " 10px;border: 1px solid #ddd;margin-right: 1px;' id='toPage'><a href='javascript:void(0)'" +
            " onclick='dataTableUtil.toPage(\""+tableId+"\")'>跳转</a></li>");
        $("#"+tableId+"_info").parent().removeClass("col-sm-6").addClass("col-sm-4");
        $("#"+tableId+"_paginate").parent().removeClass("col-sm-6").addClass("col-sm-8");

        //将分页栏位置移动至分页信息后
        $($("#"+tableId+"_info")[0].parentElement).after($("#"+tableId+"_length")[0].parentElement);
        //调整样式
        $("#"+tableId+"_info").parent().css('width','25%');
        $("#"+tableId+"_length").parent().css('width','8%').css('marginTop','4px');
        $("#"+tableId+"_paginate").parent().css('width','67%');
    };
    /**
     * 检验用户条件查询form的输入值是否符合条件，不符合进行相应的提示
     * 用法：在输入值元素中加入validate属性，例如：校验手机号，validate="tel",多个校验用","分隔，validate="int,tel"
     * @param   formId    用户查询条件form的id
     */
    function _validateDTSearchForm(formId){
        var flag = true;
        //1.获取searchForm中的所有包含validate属性的元素
        var elementArr = $("#" + formId).find("*[validate]");
        for (var i = 0;i < elementArr.length;i++){
            //2.判断每个元素的校验是否正确
            var validate = $(elementArr[i]).attr("validate");
            var value = $(elementArr[i]).val();
            //空值不进行校验
            if (commonUtil.isEmpty(value)){
                continue;
            }
            var validateArr = [validate];
            if (validate.includes(",")){
                var validateArr = validate.split(",");
            }
            var message = "";
            for (var j in validateArr){
                var regex = "";
                switch (validateArr[j]){
                    case "int":{regex = /^\d+$/;message = "请输入正整数!";break;};
                    case "tel":{regex = /^1[3|4|5|8|7]/;message = "请输入正确的手机号!";break;};//校验手机号开头的数字
                    default : {throw validateArr[j] + "类型未定义"};
                }
                var result = value.match(regex);
                if (result == null){
                    //将输入框变红，在搜索框最后展示提示信息
                    elementArr.removeClass("redBorder");
                    $(elementArr[i]).addClass("redBorder");
                    //增加提示信息层
                    if (!$("#" + formId).find("#errorMsg").length){
                        $("#" + formId).append("<span style='color: red' id='errorMsg'> " + message + "<span>");
                    }else {
                        $("#errorMsg").html(message);
                    }
                    flag = false;
                    break;
                }
            }
            if (!flag){
                break;
            }
        }
        //3.所有元素校验正确返回true，否则返回false并进行提示
        //去除输入框红线，去除提示信息
        if (flag){
            elementArr.removeClass("redBorder");
            $("#errorMsg").empty();
        }
        return flag;
    };

    /**
     * 清空查询条件form的条件值
     * @param   formId              查询条件form的id
     * @param   initSelectValue     清空select下拉框后的值
     */
    function _clearDTSearchForm(formId,initSelectValue) {
        $("#"+formId+" input[type != 'hidden']").val("");
        $("#"+formId+" select").val(initSelectValue);
    };
    /**
     * 获取dataTable公共属性
     * @param uri
     * @param columns
     * @param opts      添加新的属性或覆盖原有的属性
     */
    function _getDataTableTemplate(uri, columns,opts) {
        return jQuery.extend({
            "searching":false,
            "ordering": false,
            "responsive": true,
            "language" : dataTableUtil.LANGUAGE,
            "autoWidth": false,
            "aoColumnDefs" : false,
            "bProcessing": false,
            "bServerSide": true,	//服务端交互
            "paging":false,
            "bInfo":false,          //展示列表数据条数信息
            "bPaginate":false,
            "sAjaxSource": uri,//这个是请求的地址
            "fnServerData": _retrieveDataByPager, // 获取数据的处理函数
            "columns": columns,
            "buttons":[
                {
                    extend: 'excel',
                    text: 'Save current page',
                    exportOptions: {
                        modifier: {
                            page: 'current'
                        }
                    }
                }
            ]
        },opts || {});
    };



    /**
     * dataTable请求数据和数据渲染,返回数据集合为list
     * @param url                 请求url
     * @param paramData           请求参数
     * @param renderFnCallback      数据渲染
     */
    function _retrieveDataByList(url,paramData,renderFnCallback) {
        var data = {};
        for(var p in paramData){
            //组装draw计数器
            if(paramData[p].name == "sEcho"){
                data.draw = paramData[p].value;
            }
        }
        var customFnCallBack = function (resultData) {
            var returnData = {};
            var result = resultData.resMsg;
            if(result!=null){
                returnData.data = result;
            }else{
                returnData.data={};
            }
            //渲染响应结果
            renderFnCallback(returnData);
            try {
                if(typeof dataTables_result_callback === "function") { //是函数    其中 FunName 为函数名称
                    dataTables_result_callback.call(this,resultData);
                }
            } catch(e) {}
        };
        _dataRenderDT(url,data,customFnCallBack);
    };
    /**
     * dataTable请求数据和数据渲染,返回数据集合为pager对象
     * @param url                 请求url
     * @param paramData           请求参数
     * @param renderFnCallback      数据渲染
     */
    function _retrieveDataByPager(url,paramData,renderFnCallback) {
        var data = {};
        data.currentPage = 0;
        data.pageSize = 100000;

        for(var p in paramData){
            //组装当前页
            if(paramData[p].name == "iDisplayStart" && paramData[p].value > 0){
                data.currentPage = paramData[p].value;
            }
            //组装每页条数
            if(paramData[p].name == "iDisplayLength" && paramData[p].value > 0){
                data.pageSize = paramData[p].value;
            }
            //组装draw计数器
            if(paramData[p].name == "sEcho"){
                data.draw = paramData[p].value;
            }
        }
        data.currentPage = (data.currentPage/data.pageSize) + 1;
        var customFnCallBack = function (resultData) {
            //成功，渲染数据
            if(resultData.resStatus==CONFIG.CONSTANTS.SUCCESS_STATE){
                var returnData = {};
                var result = resultData.resMsg;
                returnData.draw = data.draw;//这里直接自行返回了draw计数器,应该由后台返回
                returnData.recordsTotal = result.totalRows;
                returnData.recordsFiltered = result.totalRows;
                returnData.data = result.data;
                //渲染响应结果
                renderFnCallback(returnData);
                if($("#datatableCallBack").val() == 'datatableCallBack'){
                    datatableCallBack(resultData.params);
                }
            }else {
                if(!commonUtil.isLogin){
                    console.info("登录信息失效，自动跳转登录页");
                    commonUtil.loginOvertime("警告："+ resultData.resMsg);
                }else{
                    commonUtil.createSimpleNotify("警告："+ resultData.resMsg);
                }
            }
        }
        _dataRenderDT(url,data,customFnCallBack);
    };
    function _dataRenderDT(url,data,customFnCallBack) {
        var searchFormTable = commonUtil.getRequestParamByUrl(url)["searchTable"];
        if(searchFormTable==null){
            searchFormTable = 'searchForm';
        }else{
            searchFormTable = searchFormTable+'-searchForm';
        }
        //序列化额外的初始化请求参数
        //设置extendsValues
        var extendsValues=null;
        if(commonUtil.isNotEmpty($("#extendsValues").val())){
            extendsValues= JSON.parse(encryptAndDecryptUtil.decrypt($("#extendsValues").val()));
        }
        if(extendsValues!=null){
            dataTableUtil.addParamsToObject(extendsValues, data);
        }
        //使用完之后清理
        $("#extendsValues").val('');

        //序列化查询form
        var searchForm = $("#"+searchFormTable).serializeObject();
        dataTableUtil.addParamsToObject(searchForm, data);
        $.ajax({
            url : url,
            data : data,
            type : 'post',
            dataType : 'json',
            async : false,
            success : function(resultData) {
                customFnCallBack(resultData);
            },
            error : function(msg) {
                commonUtil.createSimpleNotify("数据加载失败，请重试", ALERT_ERR);
            }
        });
    };
    /**
     * 创建dataTable，带分页
     * @param   uri         dataTable请求后台获取数据的地址(必填)
     * @param   columns     dataTable展示的列(必填)
     * @param   tableId     用户展示dataTable的id(非必填)
     */
    dataTableUtil.createDT = function (uri,columns,tableId) {
        if (commonUtil.isEmpty(uri) || commonUtil.isEmpty(columns)){
            throw new Error("dataTableUtil.createDT uri and columns can not be empty!");
        }
        //默认tableId为"mDataTable"
        if (commonUtil.isEmpty(tableId)){
            tableId = "mDataTable";
        }
        //执行请求数据
        $("#" + tableId).DataTable(_getDataTableTemplate(uri,columns,{
            "paginationType" : "full_numbers",
            "lengthMenu":[10, 20, 30, 50, 100],
            "drawCallback": function() {
                //初始化dataTable的复选框
                _initDTCheckBox(columns,tableId);
                //初始化dataTable的分页组件及样式
                _initDTPage(tableId);
            },
            "paging":true,
            "bInfo":true  //是否展示列表数据条数信息
        }));
    };


    /**
     * 带回调函数的table处理
     * @param uri
     * @param columns
     * @param tableId
     * @param fnFooterCallback   回调函数，参数是uri请求之后的data
     */
    dataTableUtil.createDTForCallBack = function (uri,columns,tableId,fnFooterCallback) {
        if (commonUtil.isEmpty(uri) || commonUtil.isEmpty(columns)){
            throw new Error("dataTableUtil.createDT uri and columns can not be empty!");
        }
        //默认tableId为"mDataTable"
        if (commonUtil.isEmpty(tableId)){
            tableId = "mDataTable";
        }
        //执行请求数据
        $("#" + tableId).DataTable(_getDataTableTemplate(uri,columns,{
            "paginationType" : "full_numbers",
            "lengthMenu":[10, 20, 30, 50, 100],
            "drawCallback": function() {
                //初始化dataTable的复选框
                _initDTCheckBox(columns,tableId);
                //初始化dataTable的分页组件及样式
                _initDTPage(tableId);
            },
            "paging":true,
            "bInfo":true  //是否展示列表数据条数信息
        }));
    };


    /**
     * 带回调函数的table处理(不带分页)
     * @param uri
     * @param columns
     * @param tableId
     * @param fnFooterCallback   回调函数，参数是uri请求之后的data
     */
    dataTableUtil.createDTNoPagerForCallBack = function (uri,columns,tableId,fnFooterCallback) {
        if (commonUtil.isEmpty(uri) || commonUtil.isEmpty(columns)){
            throw new Error("dataTableUtil.createDT uri and columns can not be empty!");
        }
        //默认tableId为"mDataTable"
        if (commonUtil.isEmpty(tableId)){
            tableId = "mDataTable";
        }
        //执行请求数据
        $("#" + tableId).DataTable(_getDataTableTemplate(uri,columns,{
            "drawCallback": function() {
                //初始化dataTable的复选框
                _initDTCheckBox(columns,tableId);
            },
            "fnServerData": _retrieveDataByList // 获取数据的处理函数,
        }));
    };




    dataTableUtil.createDT2 = function (uri,columns,tableId,$compile,$scope) {
        if (commonUtil.isEmpty(uri) || commonUtil.isEmpty(columns)){
            throw new Error("dataTableUtil.createDT uri and columns can not be empty!");
        }
        //默认tableId为"mDataTable"
        if (commonUtil.isEmpty(tableId)){
            tableId = "mDataTable";
        }
        //执行请求数据
        $("#" + tableId).DataTable(_getDataTableTemplate(uri,columns,{
            "paginationType" : "full_numbers",
            "lengthMenu":[10, 20, 30, 50, 100],
            "drawCallback": function() {
                //初始化dataTable的复选框
                _initDTCheckBox(columns,tableId);
                //初始化dataTable的分页组件及样式
                _initDTPage(tableId);
            },
            "paging":true,
            "bInfo":true  //是否展示列表数据条数信息
        }));
        var tableHtml = $("#"+tableId).html();
        $("#"+tableId).html($compile(tableHtml)($scope));
    };


    /**
     * 创建dataTable，不带分页，通过设置分页参数中一页100000条实现查询全部数据，查询出的数据集合需包装在pager对象中
     * @param   uri         dataTable请求后台获取数据的地址(必填)
     * @param   columns     dataTable展示的列(必填)
     * @param   tableId     用户展示dataTable的id(非必填)
     */
    dataTableUtil.createDTNoPageByPager = function (uri,columns,tableId) {
        if (commonUtil.isEmpty(uri) || commonUtil.isEmpty(columns)){
            throw new Error("dataTableUtil.createDTNoPageByPager uri and columns can not be empty!");
        }
        //默认tableId为"mDataTable"
        if (commonUtil.isEmpty(tableId)){
            tableId = "mDataTable";
        }
        //执行请求数据
        $("#" + tableId).DataTable(_getDataTableTemplate(uri,columns,{
            "drawCallback": function() {
                //初始化dataTable的复选框
                _initDTCheckBox(columns,tableId);
            },
            "bInfo":true,  //是否展示列表数据条数信息
        }));
    };
    /**
     * 创建dataTable，不带分页，将查询出的数据集合直接返回即可
     * @param   uri         dataTable请求后台获取数据的地址(必填)
     * @param   columns     dataTable展示的列(必填)
     * @param   tableId     用户展示dataTable的id(非必填)
     */
    dataTableUtil.createDTNoPageByList = function (uri,columns,tableId) {
        if (commonUtil.isEmpty(uri) || commonUtil.isEmpty(columns)){
            throw new Error("dataTableUtil.createDTNoPageByList uri and columns can not be empty!");
        }
        //默认tableId为"mDataTable"
        if (commonUtil.isEmpty(tableId)){
            tableId = "mDataTable";
        }
        //执行请求数据
        $("#" + tableId).DataTable(_getDataTableTemplate(uri,columns,{
            "drawCallback": function() {
                //初始化dataTable的复选框
                _initDTCheckBox(columns,tableId);
            },
            "fnServerData": _retrieveDataByList // 获取数据的处理函数,
        }));
    };
    /**
     * 初始化dataTable的查询条件form
     * @param   initSelectValue   点击清除按钮
     * @param   tableId           用户展示dataTable的id
     * @param   formId            查询条件form的id
     */
    dataTableUtil.initDTSearchForm = function (initSelectValue,tableId,formId) {
        //clearBtnValue默认值为-999
        if(initSelectValue == null){
            initSelectValue = -999 + '';
        };
        //tableId默认值为mDataTable
        if (commonUtil.isEmpty(tableId)){
            tableId = 'mDataTable';
        };
        if(commonUtil.isEmpty(formId)){
            formId = "searchForm";
        }
        //查询条件form的查询按钮事件
        $("#"+formId+"-searchBtn").on('keyup click', function () {
            //校验通过
            if (_validateDTSearchForm(formId)){
                $("#" + tableId).DataTable().search("").draw();
            }
        });
        //查询条件form的清空按钮事件
        $("#"+formId+"-cleanBtn").on('keyup click', function () {
            _clearDTSearchForm(formId,initSelectValue);
            $("#" + tableId).DataTable().search("").draw();
        });
    };
    /**
     * 将params中的参数合并到object中
     * @param   params
     * @param   object
     */
    dataTableUtil.addParamsToObject = function (params,object) {
        if(params != undefined && params != null){
            for(var o in params){
                var param = params[o] + '';
                object[o] = commonUtil.isEmpty(param) ? null : param.trim();
            }
        }
    };
}());