<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8" />
        <script type="text/javascript" src="${request.contextPath}/jslib/fund/jquery-1.11.0.min.js"></script>
		<title></title>
	</head>
	<body>
	</body>
	<script>
		var path = '${request.contextPath}';
		$(function(){
//			window.open("http://www.baidu.com","_self");
			//window.open('http://127.0.0.1:8081/pdfview/web/viewer.html?from=' + encodeURIComponent('guAdvisoryConsoleController.do?method=ossPdfToInputStream&url=' + '${pdfUrl}'),'_self');
			window.open(path + '/web/viewer.html?from=' + encodeURIComponent(path + '/fundController/ossPdfToInputStream?url=' + escape('${pdfUrl}')),'_self');
		});
	</script>
</html>