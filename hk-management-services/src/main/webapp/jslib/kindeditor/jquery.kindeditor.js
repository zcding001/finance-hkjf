/**
 *	<p>插件：KindEditor富文本编辑器通用性使用封装
 *	<p>示例：<textarea id="xxx" class="editor"></textarea>
 *	<p>说明：只要textarea元素拥有editor这个class，系统会页面加载时自动检测并渲染成KindEditor富文本编辑器，无需做其它处理；
 *			<p>若需要获取KindEditor对象，需要调用：$.fn.editor.getEditor("xxx"), 如：
 *			<p>alert($.fn.editor.getEditor("xxx").html()); 即可在alert中显示富文本内容
 *	<p>注意：textarea元素必须存在id属性，否则不会将其渲染成KindEditor编辑器。
 *	
 *	@version 1.0
 *	@since 	 2014-08-30 16:31
 *	@author	 wq 
 *	@email   wangqiang@why-e.com.cn
 */
 if(typeof(KindEditorMgr) == "undefined") KindEditorMgr = new Object();
(function($){
	var MGR_EDITOR_KEY_PREFIX = "__editor__";
	$.fn.editor = function(options){
		//debug(this);
		var opts = $.extend({}, $.fn.editor.defaults, options);
		
		return this.each(function(){
			var $this = $(this);
			
			var eid = this.id;
			if(!(eid && typeof(eid) != "undefined")) {
				return false;
			}
			
			var isMini = $this.hasClass("mini")||false;
			var miniMenus = [
					'undo','redo','|','formatblock','fontname','fontsize','|','forecolor','hilitecolor','bold',
     				'italic','underline','strikethrough','lineheight','removeformat','|','link','unlink','|','emoticons'];
			var fullMenus = [
					'undo','redo','|','preview','print','template','cut','copy','paste',
     				'plainpaste','wordpaste','|','justifyleft','justifycenter','justifyright',
    				'justifyfull','insertorderedlist','insertunorderedlist','indent','outdent','subscript',
    				'superscript','clearhtml','quickformat','selectall','|','fullscreen','/',
     				'formatblock','fontname','fontsize','|','forecolor','hilitecolor','bold',
     				'italic','underline','strikethrough','lineheight','removeformat','|','image',
     				'table','hr','emoticons','pagebreak',
    				'anchor','link','unlink','|']
			
			var	editor = KindEditor.create('#'+eid, {
				themeType : 'default',
				items : isMini ? miniMenus : fullMenus
			});
			prettyPrint();
			// 存储KindEditor对象，key为HTML元素的ID
			putEditor2Mgr(eid, editor);
		});
	};
	
	/***** 定义私有函数 *****/
	function debug(message) {
		if(window.console && window.console.log) {
			window.console.log(message);
		}
	}
	
	function putEditor2Mgr(eid, obj) {
        KindEditorMgr[MGR_EDITOR_KEY_PREFIX + eid] = obj;
	};
	
	/***** 定义公有函数 *****/
	/* 根据textarea元素ID获取已创建好的KindEditor对象 */
	$.fn.editor.getEditor = function(eid) {
		return KindEditorMgr[MGR_EDITOR_KEY_PREFIX + eid];
	}
	
	/* 插件的default */
	$.fn.editor.defaults = {
	};

})(jQuery);
