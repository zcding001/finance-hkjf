var elementOperateUtil = {};
(function () {
   'use strict';
    /**
     * 初始化select选择框
     * @param url：需要请求的后台地址(必填)
     * @param id : select标签id(必填)
     */
    elementOperateUtil.initSelectHtml = function(url,id){
        if (commonUtil.isEmpty(url) || commonUtil.isEmpty(id)){
            throw new Error("elementOperateUtil.initSelectHtml requires param is no satisfy!");
        }
        $.ajax({
            url : url,
            type : 'POST',
            dataType : 'JSON',
            async : false,
            success : function(result) {
                $("#"+id).html('');
                var option ="<option value='-999'>全部</option>";
                for(var i in result){
                    var o = result[i];
                    option += "<option value='" + o.value + "'>" + o.name + "</option>";
                }
                $("#"+id).append(option);
            },
            error : function(msg) {
            }
        });
    };
    /**
     * 页面元素添加样式及元素验证事件，controller初始化时直接调用
     * @param formId 表单的id，默认dataForm
     */
    elementOperateUtil.addFormValidElementAttr = function (formId) {
        if(formId == undefined){
            formId = "dataForm";
        }
        var arr = $("#" + formId).find("input, select,textarea");
        if(arr.length > 0){
            for(var i = 0; i < arr.length; i++){
                var name = arr[i].getAttribute("name");
                var ngModel = arr[i].getAttribute("ng-model");
                if(ngModel == undefined){
                    name = arr[i].getAttribute("name");
                }else{
                    name = ngModel.split(".")[1];
                }
                arr[i].setAttribute("name", name);
                arr[i].setAttribute("class", "form-control ng-pristine ng-valid ng-touched textstyle");
                var validator = arr[i].getAttribute("validator");
                //给label添加样式
                if($(arr[i]).prev("label").length > 0){
                    if (!$(arr[i]).prev("label")[0].getAttribute("class")){
                        $(arr[i]).prev("label")[0].setAttribute("class", "textright");
                    }
                    if(!$(arr[i]).prev("label")[0].getAttribute("style")){
                        $(arr[i]).prev("label")[0].setAttribute("style", "width: 160px");
                    }
                    if (validator != undefined && validator != 'undefined'){
                        //若元素是必填项，则给元素添加*号
                        if (validator.indexOf("required") > -1){
                            $(arr[i]).prev("label")[0].innerHTML = '<span class="font-red">*&nbsp;</span>' + $(arr[i]).prev("label")[0].innerHTML;
                        }
                    }
                }
                if (validator != undefined && validator != 'undefined'){
                    arr[i].setAttribute("valid-method", "blur");
                    arr[i].setAttribute("message-id", name + "Error");
                    //有元素校验则为其添加一个错误提示span
                    $(arr[i]).after("<span style='color:#F00' id='"+name+"Error'></span>");
                }
            }
        }
    };
    /**
     * 处理富文本编辑
     * @param bucket        存放桶名称
     * @param path          存放路径
     * @param elementId     富文本编辑器id 默认content
     * @returns {textarea[id="content"]|*}
     */
    elementOperateUtil.createKindEditor = function (bucket,path,elementId) {
        if (commonUtil.isEmpty(bucket) || commonUtil.isEmpty(path)){
            throw new Error("elementOperateUtil.createKindEditor requires param is no satisfy!");
        }
        if(elementId == null){
            elementId = 'content';
        }
        var editor = KindEditor.create('textarea[id="'+elementId+'"]', {
            themeType : 'default',
            uploadJson : 'uploadImageController.do?method=upload&platform='+bucket+"&filePath="+path,
            items : [
                'undo','redo','|','preview','print','template','cut','copy','paste',
                'plainpaste','wordpaste','|','justifyleft','justifycenter','justifyright',
                'justifyfull','insertorderedlist','insertunorderedlist','indent','outdent','subscript',
                'superscript','clearhtml','quickformat','selectall','|','fullscreen','/',
                'formatblock','fontname','fontsize','|','forecolor','hilitecolor','bold',
                'italic','underline','strikethrough','lineheight','removeformat','|','image','multiimage','media','insertfile',
                'table','hr','pagebreak',
                'anchor','link','unlink','|'
            ]
        });
        editor.create();
        var keContent = $($(".ke-edit-iframe")[0].contentDocument).find(".ke-content")[0];
        keContent.onblur = function(){
            console.log(keContent.innerHTML);
            $("#" + elementId).val(keContent.innerHTML);
            if (commonUtil.isEmpty(keContent.innerHTML)){
                $("#" +elementId+ "Error").css('display', 'block').html("内容不能为空！");
            }else {
                $("#" +elementId+ "Error").css('display', 'none').html("");
            }
        };
        return editor;
    };
    /**
     * 禁用相关页面元素
     * @param elementName   元素的name的值,多个值传递数组集合[type,memo]，例如name='type'(必填)
     * @param attr          要添加的属性，例如disabled(必填)
     */
    elementOperateUtil.disableElement = function(elementName){
        if (commonUtil.isEmpty(elementName)){
            throw new Error("elementOperateUtil.addElementAttr elementName can not be empty!");
        }
        if(elementName instanceof Array){
            for(var i in elementName){
                elementOperateUtil.disableElement(elementName[i]);
            }
        }else {
            $("[name='" + elementName + "']").attr("disabled",true);
        }
    };
}())