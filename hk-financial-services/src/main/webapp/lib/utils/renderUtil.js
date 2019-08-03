/**
 * Created by dzc on 17.11.24.
 * 元素渲染工具类
 * 约定规范：
 *         页面默认三要素数据表格、查询按钮，检索表单，
 *         table：id默认为dataTable
 *         form: id命名规则为 tableId + "Form"
 *         检索按钮: id的命名规则 tableId + "SearchBtn";
 *
 *         columns = [{
 *              title : "", 数据展示的名称
 *              data : '', 元素属性
 *              tdClass : 'tac', tbody元素样式
 *              tdStyle : '', tbody元素样式
 *              thClass : 'tac', thead元素样式
 *              thStyle : '', thead元素样式
 *              render : funciton(value, row){
 *                  value, 当前元素的值
 *                  row，整行数据对象
 *              }
 *         }]
 *         如果页面中有多数据表格及对应的检索form，那么直接指定不同tableId即可兼容。
 *
 * 用法：
 *      html中可以是input li text span div，赋值操作通过class中属性赋值，也支持id、name值的意义对应关系，如果是ID和name要指定
 *      所在的范围域名form div等
 *      其中后缀为String或无后缀表示如果值为空那么默认设置为''
 *      后缀为Num表示如果值为空那么默认设置为0
 *
 *
 *  场景1：
 *      html
 *          姓名<input class='nameString' />
 *          年龄<input class='ageNum' />
 *          注册日期<input class='createDateDate' />
 *          修改时间<input class='modifiedTimeTime' />
 *      js:
 *         结果:
 *          ResponseEntity中params可以是对象如
 *          RegUser:{name:'Hello', age:20}
 *          renderUtil.renderElement(result);可以将name和age赋值到html的input元素中。
 *
 *  场景2：
 *      html
 *          姓名<input class='nameString' />
 *          年龄<input class='ageNum' />
 *          注册日期<input class='createDateDate' />
 *          修改时间<input class='modifiedTimeTime' />
 *          <form id="dataTableForm>
 *                 姓名<input name='name' />
 *                 年龄<input name='age' />
 *                 注册日期<input class='createDate' />
 *          	   修改时间<input class='modifiedTime' />
 *          </form>
 *      js:
 *          renderUtil.renderElement(result, "dataTableForm");可以将name和age赋值到html的input元素和form中的input元素上。
 *
 *
 *  场景3：分页、数据表格，示例newsRecord.html
 *  
 *  
 *  
 *  
 *  渲染table列表 定义columns时候
 *  如果后台传递到前台的值有可能是null 可以在columns上加上对应的数据类型
 *  |NUMBER  数值类型，默认值是0
 *           默认的，如果值是null，默认显示'--'
 *   eg:
 *   {title:'预计收益', data:'buyerExpectAmount|NUMBER', tdClass:'tac', thClass:'tac'},
 */

var renderUtil = {};
(function(){
    'use strict';
    /**
     * 页面元素值渲染函数
     * @param obj 单个对象| 数组的集合
     * @param scopeId obj所有的域
     */
    //存储用于刷新的数据
    var _data = {};

    renderUtil.renderElement = function(obj, scopeId){
        if(obj != undefined){
            if(obj instanceof Array){
                for(var i in obj){
                    _doRenderElement(obj[i], scopeId);
                }
            }else{
                _doRenderElement(obj, scopeId);
            }
        }
    };

    /**
     * 渲染集合数据表格
     * @param url 异步加载数据的地址
     * @param columns 所有需要显示的数据列
     * @param tableId
     * @param callback ajax成功后回调函数
     * @param pageClass 分页CLASS名字
     */
    renderUtil.renderList = function(url, columns, tableId, callback,pageClass){
        if(tableId == undefined || tableId == ''){
            tableId =  CONSTANTS.DEFAULT_TABLE_ID;
        }
        //判断是否含有checkbox
        var cbOpts = _getCheckBokOptions(columns);
        // 创建thead
        var columnsCount = 0;
        var thead = "<thead><tr>";
        for(var i in columns){
            var obj = columns[i];
            var thClassName = (obj.thClass == undefined || obj.thClass == '') ? 'tac' : obj.thClass;
            var thStyleName = (obj.thStyle == undefined || obj.thStyle == '') ? 'tac' : obj.thStyle;
            if(cbOpts.checkBox && obj['checkBox'] != undefined){
                thead += "<th class='" + thClassName  + "' style='" + thStyleName + "'><input type='checkbox' name='cb-check-all'>" + obj.title + "</th>";
            }else{
                thead += "<th class='" + thClassName  + "' style='" + thStyleName + "'>" + obj.title + "</th>";
            }
            columnsCount++;
        }
        thead += "</tr></thead>";
        $("#" + tableId).append(thead);
        //异步加载要展示的数据，创建tbody
        renderUtil.loadData(url, tableId, columns, columnsCount, cbOpts, 1, callback,pageClass);
        //添加检索事件
        _addSearchEvent(url, tableId, columns, columnsCount, cbOpts, callback);
        //缓存表格数据用于执行刷新的操作
        _data[tableId] = tableId;
        _data[tableId + "_url"] = url;
        _data[tableId + "_columns"] = columns;
        _data[tableId + "_columnsCount"] = columnsCount;
        _data[tableId + "_cbOpts"] = cbOpts;
        _data[tableId + "_callback"] = callback;

    };

    /**
     * 刷新datatable
     * @param tableId
     */
    renderUtil.refresh = function(tableId){
        if(tableId == undefined || tableId == ''){
            tableId =  CONSTANTS.DEFAULT_TABLE_ID;
        }
        var url = _data[tableId + "_url"];
        var columns = _data[tableId + "_columns"];
        var columnsCount = _data[tableId + "_columnsCount"];
        var cbOpts = _data[tableId + "_cbOpts"];
        var callback = _data[tableId + "_callback"];
        //含有checkbox时刷新将header中checkbox设置为不选中状态
        if(cbOpts.checkBox){
            $("input[name=cb-check-all]").prop("checked",false);
        }
        renderUtil.loadData(url, tableId, columns, columnsCount, cbOpts, 1, callback);
    };

    /**
     *  初始化复选框
     * @param param 数据中作为checkbox值的属性
     * @param callback 回调函数，用于判断是否显示复选框
     */
    renderUtil.initCheckBox = function(param, callback){
        return jQuery.extend({
            render : function(value, row) {
                var flag = true;
                if(callback != undefined){
                    flag = callback.call(callback, row);
                }
                if(flag){
                    return '<input type="checkbox" class="iCheck" name="cb-children"' + ' value="' + row[param] + '" style="position: relative;left: 10px">';
                }
                return "";
            }
        },{
            title : "全选",
            data : '',
            tdClass : 'tac',
            tdStyle : '',
            thClass : 'tac',
            thStyle : '',
            checkBox : true
        });
    };
    /**
     * 异步加载数据并渲染到页面中
     * @param url 加载数据的地址
     * @param tableId  tableId
     * @param columns 列明
     * @param columnsCount 总列数
     * @param cbOpts 是否含所有checkbok
     * @param currentPage 当前页
     * @param callback 回调函数
     * @param pageClass 分页CLASS
     * @private
     */
    renderUtil.loadData = function(url, tableId, columns, columnsCount, cbOpts, currentPage, callback,pageClass){
        $("#" + tableId + " tbody").empty();
        var dataParam = {};
        //序列化查询form
        var searchForm = $("#" + tableId + "Form").serializeObject();
        if(searchForm != undefined){
            for(var o in searchForm){
                if(searchForm[o] != undefined && searchForm[o] != '' && searchForm[o] != null){
                    dataParam[o] = searchForm[o] == null ? null : searchForm[o] + "".trim();
                }
            }
        }
        //加载分页
        dataParam['currentPage'] = currentPage;
        dataParam['pageSize'] = 10;
        ajaxUtil.post(url, dataParam, function(data){
            var list = data.resMsg.data;
            var tbody = "<tbody>";
            if(list != null && list != undefined && list.length > 0){
                for(var i in list){
                    var rowData = list[i];
                    var tr = "<tr>";
                    for(var j in columns){
                        var obj = columns[j];
                        var tdClassName = (obj.tdClass == undefined || obj.tdClass == '') ? 'tac' : obj.tdClass;
                        var tdStyleName = (obj.tdStyle == undefined || obj.tdStyle == '') ? 'tac' : obj.tdStyle;
                        var pro = (obj.data == 'null' || obj.data ==undefined || obj.data == null) ? "EMPTY_NULL" : obj.data;
                        var proArray = pro.split('|');
                        var pro_value  = proArray[0];
                        var showValue = rowData[pro_value];
                        if(showValue == 'null' || showValue == undefined || showValue == null){
                        	if(proArray[1] != undefined && proArray[1] != null){
                        		var proType = proArray[1];
                        		if(proType == 'NUMBER'){
                        			showValue = 0;
                        		}else{
                        			showValue = '--';
                        		}
                        	}else{
                        		showValue = '--';
                        	}
                        }
                        if(obj.data == '_index'){
                        	showValue = parseInt(i)+parseInt(1);
                        }else{
                        	 if(obj.render != undefined){
                                 showValue = obj.render.call(obj, showValue, rowData);
                             }
                        }
                        tr += "<td class='" + tdClassName  + "' style='" + tdStyleName + "'>" + showValue + "</td>";
                    };
                    tr += "</tr>";
                    tbody += tr;
                }
            }else{
                tbody += "<tr><td colspan='" + columnsCount + "' style='text-align:center;font-size:15px; font-weight:100;'><div id='noDataDiv'>暂无数据</div></td></tr>";
            }
            if(validUtil.validNotEmpty(pageClass)){
                pageUtil.initPage(url, tableId, columns, columnsCount, cbOpts, data.resMsg.currentPage, data.resMsg.totalPages,pageClass);
            }else{
            	pageUtil.initPage(url, tableId, columns, columnsCount, cbOpts, data.resMsg.currentPage, data.resMsg.totalPages);
            }
            tbody += "</tbody>";
            $("#" + tableId + " tr:not(:first)").empty("");
            $("#" + tableId).append(tbody);
            //添加复选框事件
            if(cbOpts.checkBox){
                _addCheckBoxEvent(tableId);
            }
            if(callback != undefined && callback != ''){
                callback.call(undefined, data);
            }
        });
    };
    /**
     * 获得所有选中的复选框
     * @returns {Array}
     */
    renderUtil.getCheckBoxValues = function(){
        var arr =[];
        $('input[name="cb-children"]:checked').each(function(){
            arr.push($(this).val());
        });
        return arr;
    };
    //========== public method ============

    // ========= private method ============
    /**
     * 执行元素渲染操作
     * @param obj 数据值
     * @param scopeId obj所有的域
     * @private
     */
    function _doRenderElement(obj, scopeId){
        for(var x in obj){
            var classNameDefault = "." + x;     // 默认是字符串
            var className = "." + x + "String";
            var className1 = "." + x + "Num";
            var classNameDate = "." + x + "Date";
            var classNameTime = "." + x + "Time";
            if($(classNameDefault + "," + className + "," + className1+","+classNameDate+","+classNameTime) != undefined){
                $(classNameDefault + "," + className + "," + className1+","+classNameDate+","+classNameTime).each(function(){
                    var value = obj[x];
                    if((value == '' || value == null || value == undefined || value == 'null')
                        && $(this).hasClass(className1.substring(1))){
                        value = 0;
                    }
                    if((value == '' || value == null || value == undefined || value == 'null') &&
                        ($(this).hasClass(className.substring(1)) || $(this).hasClass(classNameDefault.substring(1)))){
                        value = '';
                    }
                    if($(this).hasClass(classNameDate.substring(1))){
                    	if($(this).is('input')){
                    		$(this).val(dateUtil.date(value));
                    	}else{
                    		$(this).text(dateUtil.date(value));
                    	}
                    }else if ($(this).hasClass(classNameTime.substring(1))){
                    	if($(this).is('input')){
                    		$(this).val(dateUtil.dateTime(value));
                    	}else{
                    		$(this).text(dateUtil.dateTime(value));
                    	}
                    }else{
                    	if($(this).is('input')){
                    		$(this).val(value);
                    	}else{
                    		$(this).text(value);
                    	}
                    }
                });
            }
            //处理name匹配赋值
            // var nameList = $("*[name=" + x + "]");
            if(scopeId != undefined){
                var nameList = $("#" + scopeId).find("*[name=" + x + "]");
                if(nameList != undefined){
                    nameList.each(function(){
                        var value = obj[x];
                        if((value != '' && value != null && value != undefined && value != 'null')){
                            $(this).val(value);
                        }

                    });
                }
                //处理ID匹配赋值
                // var nameList = $("#" + x);
                var nameList = $("#" + scopeId).find("*[id=" + x + "]");
                if(nameList != undefined){
                    nameList.each(function(){
                        var value = obj[x];
                        if((value != '' && value != null && value != undefined && value != 'null')){
                            $(this).val(value);
                        }

                    });
                }
            }
        }
    }


    /**
     * 判断是否含有checkbox
     * @param columns
     * @returns {*}
     * @private
     */
    function _getCheckBokOptions(columns){
        for(var i in columns){
            var obj = columns[i];
            if(obj['checkBox'] != undefined && obj['checkBox']){
                return obj;
            }
        }
        return {"checkBox" : false};
    }
    /**
     * 判读是否含有序号
     */
    function _getIndexOptions(columns){
        for(var i in columns){
            var obj = columns[i];
            if(obj['Index'] != undefined && obj['Index']){
                return obj;
            }
        }
        return {"Index" : false};
    }

    /**
     * 添加复选框事件
     * {
     * 清空全选状态
     * $(":checkbox[name='cb-check-all']").prop('checked', false);
     * }
     * @param tableId
     * @private
     */
    function _addCheckBoxEvent(tableId){
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
    }

    /**
     * 检索按钮添加事件
     * @param url 加载数据的地址
     * @param tableId  数据表格ID
     * @param columns 所有列
     * @param columnsCount 列的总数
     * @param cbOpts 是否含有复选框
     * @param callback table渲染后回调函数
     * @private
     */
    function _addSearchEvent(url, tableId, columns, columnsCount, cbOpts, callback){
        var btn = $("#" + tableId + "SearchBtn");
        if(btn != undefined && btn != "undefined"){
            //先解除原来的绑定事件，避免多次渲染时重复触发事件
            btn.unbind('click');        //解绑点击事件
            btn.unbind('keyup');        //解绑按键事件
            //搜索按钮事件
            btn.on('keyup click', function () {
                //校验通过
                // if (validateSearchForm()){
                renderUtil.loadData(url, tableId, columns, columnsCount, cbOpts, 1, callback);
                // }
            });
        }
    }
})();
