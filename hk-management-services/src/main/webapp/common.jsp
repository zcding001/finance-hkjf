<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>

<!-- 时间控件  在js公共头文件中引入即可 -->
<script type="text/javascript" src="jslib/My97DatePicker/WdatePicker.js"></script>

<!-- 文本编辑器依赖 -->
<script charset="utf-8" src="jslib/kindeditor/kindeditor-all-min.js"></script>
<script charset="utf-8" src="jslib/kindeditor/lang/zh-CN.js"></script>

</head>
<body>
<hr>
分页控件
<hr>
<div>
<!-- 分页的检索条件，form的Id默认为pageForm，或在page方法中第5个参数指定formid -->
<form id="pageForm" action="rosterController/loadRosInfo.json">
	类型<input type="text" name="type">
	标识<input type="text" name="flag">
</form>
</div>
<div>
<!-- 引入分页 -->
<jsp:include page="page.jsp"></jsp:include>
</div>

<br/><br/><br/><br/><br/><br/>
<hr>
时间插件
<hr>
<div>
日期:<br/>
<input type="text" id="d241" onclick="WdatePicker()" class="Wdate" style="width:300px"/>
<br/>
<input id="time1" type="text"/>
<img onclick="WdatePicker({el:'time1'})" src="jslib/My97DatePicker/skin/datePicker.gif" width="16" height="22" align="absmiddle">
<br/>
日期时间:<br/>
<input type="text" id="d241" onclick="WdatePicker({dateFmt:'yyyy年MM月dd日 HH时mm分ss秒'})" class="Wdate" style="width:300px"/>
<br/>
<input id="time2" type="text"/>
<img onclick="WdatePicker({el:'time2',dateFmt:'yyyy年MM月dd日 HH时mm分ss秒'})" src="jslib/My97DatePicker/skin/datePicker.gif" width="16" height="22" align="absmiddle">

<br/><br/><br/><br/><br/><br/>
<hr>
文本编辑器
<hr>
<form id="form" name="form" method="post" target="_self">
<textarea id="editor_id" name="content" style="width:700px;height:300px;">
</textarea>
</form>
</div>
</body>

<script type="text/javascript">
//分页初始化脚本
$(function(){
	//通过查询结果初始化分页
	page(1000, 10, 1, true);
	initEditor("editor_id");
});

/**
 * 初始化文本标记器
 */
function initEditor(editorId){
	var editor;
	//文本编辑器脚本
	KindEditor.ready(function(K) {
	    editor = K.create('#' + editorId, {
	            uploadJson : 'js/kindeditor/jsp/upload_json.jsp',//文件中地址需要改成阿里云的地址
	            fileManagerJson : 'js/kindeditor/jsp/file_manager_json.jsp',//文件中地址需要改成阿里云的地址
	            allowFileManager : true
	    });
	});
	//获得文本标记器的内容
	//editor.html();
	return editor;
}
</script>

</html>

