/**
 * 渲染图片效果，添加mouse事件
 * @param imgId
 * @param opts
 */
function _addImgMouseEvent(imgId, opts){
	$("#" + imgId).css("width", opts.Width + "px").css("height", opts.Height + "px");
   	//添加鼠标事件
   	$("#" + imgId). mouseenter(function(){
   		$("#" + imgId + "Big").remove();
   		$("#" + imgId).after('<div id="' + imgId +'Big" style="position: absolute;top:-1px;left: 54px;width: 300px;height: 300px;"><img style="width:100%;height:100%;" src="'+ $("#" + imgId).attr("src") +'"></div>');
   	});
   	$("#" + imgId).mouseleave(function(){
   		$("#" + imgId + "Big").remove();
   	});
   	opts.Callback();
}

/** get img Render params */
function _getOpts(id, opts){
	return jQuery.extend({
        Img: id + "Img",
        Width: 50,
        Height: 50,
        ImgType: ["jpeg", "jpg", "bmp", "png"],
        dynamic : 1, //动态添加<img标签>
        Callback: function () {}
    }, opts || {});
}

jQuery.fn.extend({
    uploadPreview: function (opts) {
    	var _self = this,
            _this = $(this);
//    	opts = jQuery.extend({
//            Img: this[0].id + "Img",
//            Width: 50,
//            Height: 50,
//            ImgType: ["jpeg", "jpg", "bmp", "png"],
//            dynamic : 1, //动态添加<img标签>
//            Callback: function () {}
//        }, opts || {});
        
    	opts = _getOpts(this[0].id, opts);
    	
        _self.getObjectURL = function (file) {
            var url = null;
            if (window.createObjectURL != undefined) {
                url = window.createObjectURL(file)
            } else if (window.URL != undefined) {
                url = window.URL.createObjectURL(file)
            } else if (window.webkitURL != undefined) {
                url = window.webkitURL.createObjectURL(file)
            }
            return url
        };
        _this.change(function () {
        	var imgId = opts.Img;
            $("#" + imgId + "Div").remove();
            if (this.value) {
                if (!RegExp("\.(" + opts.ImgType.join("|") + ")$", "i").test(this.value.toLowerCase())) {
                    //alert("选择文件错误,图片类型必须是" + opts.ImgType.join("，") + "中的一种");
                    this.value = '';
                    return false
                }
                
                var url = _self.getObjectURL(this.files[0]);
                if(opts.dynamic == 1){//动态在file元素后添加img标签用来预览图片
                	$("#" + this.id).after('<div id="' + imgId + 'Div" style="display:inline-block;position:relative;"><img alt="" src="" id="' + imgId + '"></div>');
                }
               	if ($.support) {
                    try {
                        $("#" + imgId).attr('src', url);
                    } catch (e) {
                        var src = "";
                        var obj = $("#" + opts.Img);
                        var div = obj.parent("div")[0];
                        _self.select();
                        if (top != self) {
                            window.parent.document.body.focus()
                        } else {
                            _self.blur()
                        }
                        src = document.selection.createRange().text;
                        document.selection.empty();
                        obj.hide();
                        obj.parent("div").css({
                            'filter': 'progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale)',
                            'width': opts.Width + 'px',
                            'height': opts.Height + 'px'
                        });
                        div.filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = src
                    }
                } else {
                    $("#" + imgId).attr('src', url);
                }
               	_addImgMouseEvent(imgId, opts);
            }
        })
    },
    displayBack:function(id, opts, url){//图片回显函数
    	if(url == "" || url == undefined){
    		return;
    	}
    	opts = _getOpts(id, opts)
    	var imgId = opts.Img;
        $("#" + imgId + "Div").remove();
        if(opts.dynamic == 1){//动态在file元素后添加img标签用来预览图片
        	$("#" + id).after('<div id="' + imgId + 'Div" style="display:inline-block;position:relative;"><img alt="" src="" id="' + imgId + '"></div>');
        }
       	if ($.support) {
            try {
                $("#" + imgId).attr('src', url);
            } catch (e) {
            	console.info("图片回显失败");
            	console.info(e);
            }
        } else {
            $("#" + imgId).attr('src', url);
        }
       	_addImgMouseEvent(imgId, opts);
    }
});