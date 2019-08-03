var  dialogUtil = {};
(function () {
	/***
	 * @Description 弹出框
	 * @Method_Name  Msg
	 * @param title 标题
	 * @param msg 显示的内容
	 * @param show 是否自动关闭
	 * Created by yanbinghuang
	 */
	dialogUtil.msg = function (title, msg, show) {
			_generateHtml("msg", title, msg);
            if(!validUtil.validNotEmpty(show)){
            	setTimeout(function () { 
            		$("#mb_box,#mb_con").remove();
            	}, 2000);
            }
        _btnNo();
    };
    /***
	 * @Description alert弹出框
	 * @Method_Name  Alert
	 * @param title 标题
	 * @param msg 显示的内容
	 * Created by yanbinghuang
	 */
    dialogUtil.alert = function (title, msg) {
    		_generateHtml("alert", title, msg);
            _btnOk();  //alert只是弹出消息，因此没必要用到回调函数callback
            _btnNo();
    };

    /***
     * @Description alert弹出框只有确认按钮，callback为点击确认按钮执行的方法
     * @Method_Name  Alert
     * @param title 标题
     * @param msg 显示的内容
     * Created by yanbinghuang
     */
    dialogUtil.alert = function (title, msg, callback) {
        _generateHtml("alert", title, msg);
        _btnOk(callback);
        _btnNo();
    };

    /***
	 * @Description Confirm提示框
	 * @Method_Name  Confirm
	 * @param title 标题
	 * @param msg 显示的内容
	 * @param callback 点击确定的回调函数
	 * @param cancelValue 取消按钮描述信息 默认值取消
	 * @param cancelCallBack 点击确定的回调函数 取消后回调函数
	 * @param formId 对话框中的form的id
	 * @param okBtnValue 正确按钮描述信息，默认值确定
	 * Created by yanbinghuang
	 */
    dialogUtil.confirm = function (title, msg, callback, cancelValue, cancelCallBack, formId, okBtnValue) {
            _generateHtml("confirm", title, msg, formId);
            _btnOk(callback, formId, okBtnValue);
            _btnNo(cancelValue, cancelCallBack, formId);
     };
    //生成Html
    var _generateHtml = function (type, title, msg, formId) {
        formId = _getFormId(formId);
        var _html = "";
        _html += '<div id="mb_box"></div><div id="mb_con"><form id="' + formId + '"><span id="mb_tit">' + title + '</span>';
        _html += '<a id="mb_ico">X</a><div id="mb_msg">' + msg + '</div><div id="mb_btnbox">';
        if (type == "alert") {
            _html += '<input id="mb_btn_ok" type="button" value="确定" />';
        }
        if (type == "confirm") {
            _html += '<input id="mb_btn_ok" type="button" value="确定" />';
            _html += '<input id="mb_btn_no" type="button" value="取消" />';
        }
        _html += '</div></form></div>';
        //必须先将_html添加到body，再设置Css样式
        $("body").append(_html); 
        _generateCss();
    };
    //生成Css
    var _generateCss = function () {
        $("#mb_box").css({ width: '100%', height: '100%', zIndex: '99999', position: 'fixed',
            filter: 'Alpha(opacity=60)', backgroundColor: 'black', top: '0', left: '0', opacity: '0.6'
        });
        $("#mb_con").css({ zIndex: '999999', width: '300px', position: 'fixed',
            backgroundColor: 'White', borderRadius: '5px'
        });
        $("#mb_tit").css({ display: 'block', fontSize: '14px', color: '#444', padding: '10px 15px',
            backgroundColor: '#DDD', borderRadius: '5px 5px 0 0'
        });
        $("#mb_msg").css({ padding: '20px', lineHeight: '20px', fontSize: '13px',textAlign:'center'
        });
        $("#mb_ico").css({ display: 'block', position: 'absolute', right: '10px', top: '9px',
             width: '18px', height: '18px', textAlign: 'center',color:'#aaa',
            lineHeight: '18px', cursor: 'pointer', borderRadius: '12px', fontFamily: '微软雅黑'
        });
        $("#mb_btnbox").css({ margin: '15px 0 15px 0', textAlign: 'center' });
        $("#mb_btn_ok,#mb_btn_no").css({ width: '85px', height: '30px', color: 'white', border: 'none',borderRadius:'5px' });
        $("#mb_btn_ok").css({ backgroundColor: '#f39200' });
        $("#mb_btn_no").css({ backgroundColor: '#ccc', marginLeft: '20px' });
        //右上角关闭按钮hover样式
        $("#mb_ico").hover(function () {
            $(this).css({color: '#333' });
        }, function () {
            $(this).css({color: '#aaa' });
        });
        var _widht = document.documentElement.clientWidth;  //屏幕宽
        var _height = document.documentElement.clientHeight; //屏幕高
        var boxWidth = $("#mb_con").width();
        var boxHeight = $("#mb_con").height();
        //让提示框居中
        $("#mb_con").css({ top: (_height - boxHeight) / 2 + "px", left: (_widht - boxWidth) / 2 + "px" });
    };
    //确定按钮事件
    var _btnOk = function (callback, formId, okBtnValue) {
        formId = _getFormId(formId);
        if(okBtnValue != undefined && okBtnValue != ''){
            $("#mb_btn_ok").val(okBtnValue);
        }
        $("#mb_btn_ok").click(function () {
            //务必自此处获得表单数据
            var data = $("#" + formId).serializeObject();
            $("#mb_box,#mb_con").remove();
            if (typeof (callback) == 'function') {
                callback.call(undefined, data);
            }
        });
    };
    //取消按钮事件
    var _btnNo = function (cancelValue, cancelCallBack, formId) {
        formId = _getFormId(formId);
        if(cancelValue != undefined && cancelValue != ''){
            $("#mb_btn_no").val(cancelValue);
        }
        $("#mb_btn_no,#mb_ico").click(function () {
            //务必自此处获得表单数据
            var data = $("#" + formId).serializeObject();
            $("#mb_box,#mb_con").remove();
            if (cancelCallBack != undefined && typeof (cancelCallBack) == 'function') {
                cancelCallBack.call(undefined, data);
            }
        });
    }

    function _getFormId(formId){
        if(formId == undefined || formId == ''){
            formId = "confirmFormId";
        }
        return formId;
    }
})();