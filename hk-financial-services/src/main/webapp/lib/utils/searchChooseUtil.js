/**
 * 通用搜索选择框
 * @type {{}}
 */

var searchChooseUtil = {};
(function () {

    searchChooseUtil.commonClick = function (event) {
        //设置样式
        $(this).css("color","#f39200")
            .siblings().css("color","#888");

        //标记参数
        var selectParam = "search-by-select-param";
        $(this).attr(selectParam, true).siblings().removeAttr(selectParam);

        var param={};
        //构造搜索参数
        $("[" + selectParam + "]").each(function () {
            var name= $(this).attr("name");
           if(!commonUtil.isEmpty(name)){
               var value=$(this).attr("value");
               param[name] = value;
           }
        })
        console.log(param);

        //调用分页查询事件
        var searchFn=event.data.searchFn;
        //清除原来的内容
        searchFn(param);
    };
[]
    /**
     * 构造搜索框的循环逻辑
     * @param elementId
     * @param dicArray
     * @param name
     * @param elementValueAttr
     * @param elementTitleAttr
     * @param needUL
     * @param callback li元素的点击事件
     */
    searchChooseUtil.loopLogic = function (elementId, dicArray, name, elementValueAttr,
                                           elementTitleAttr, needUL,needNoLimitEle, callback,searchFn) {
        needUL = needUL == null ? true : needUL;
        needNoLimitEle = needNoLimitEle == null ? true : needNoLimitEle;
        name = name == null ? '' : name;
        elementValueAttr = elementValueAttr == null ? "value" : elementValueAttr;
        elementTitleAttr = elementTitleAttr == null ? "name" : elementTitleAttr;
        var content = '';
        if (dicArray != null && dicArray.length > 0) {
            if (needUL) {
                content += '<ul>'
            }
            if(needNoLimitEle){
                content += '<li class="select-all selected" value="0">不限</li>';
            }

            for (var i = 0; i < dicArray.length; i++) {
                var tempElement = dicArray[i];
                //<li  name="someName" value="1">新手专享标</li>
                content += '<li  name="' + name + '" value="' + tempElement[elementValueAttr] + '">' + tempElement[elementTitleAttr] + '</li>';
            }
            if (needUL) {
                content += '</ul>'
            }

        }

        //黏贴
        $("#" + elementId).append(content);
        //绑定事件 只绑定没有onclick函数
       // $("#" + elementId).find("li").click(callback);
        $("#" + elementId).find("li").unbind("click").bind("click",{searchFn:searchFn},callback);



    };
    /**
     * 黏贴带UL标签的li
     * @param elementId
     * @param dicArray
     * @param name
     * @param elementValueAttr
     * @param elementTitleAttr
     */
    searchChooseUtil.stickULAndLI = function (elementId, dicArray, name, elementValueAttr, elementTitleAttr,searchFn) {
        searchChooseUtil.loopLogic(elementId, dicArray, name, elementValueAttr, elementTitleAttr,
            true, true,searchChooseUtil.commonClick,searchFn);
    };

    /**
     * 黏贴不带UL标签的li
     * @param elementId
     * @param dicArray
     * @param name
     * @param elementValueAttr
     * @param elementTitleAttr
     */
    searchChooseUtil.stickLIWithNOLIMITELE = function (elementId, dicArray, name, elementValueAttr, elementTitleAttr,searchFn) {
        searchChooseUtil.loopLogic(elementId, dicArray, name, elementValueAttr, elementTitleAttr, false, true,searchChooseUtil.commonClick,searchFn);
    };

    /**
     * 黏贴不带UL标签的li
     * @param elementId
     * @param dicArray
     * @param name
     * @param elementValueAttr
     * @param elementTitleAttr
     */
    searchChooseUtil.stickLI = function (elementId, dicArray, name, elementValueAttr, elementTitleAttr,searchFn) {
        searchChooseUtil.loopLogic(elementId, dicArray, name, elementValueAttr, elementTitleAttr, false, false,searchChooseUtil.commonClick,searchFn);
    };


})();