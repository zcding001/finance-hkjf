<script src="jslib/jquery-3.2.1.min.js" charset="utf-8"></script>
<script src="jslib/paging/jquery.paging.min.js" charset="utf-8"></script>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div>
<div id="paging"></div>
每页<select id="perPage" onchange="chgPerPage()">
	<option value="10">10</option>
	<option value="20">20</option>
	<option value="50">50</option>
	<option value="100">100</option>
</select>
<br/>
页数<input type="text" id="toPage" > <a href="javascript:void(0)" onclick="jump()"> GO </a>
</div>

<script>
var _paging;
var _first = true;
var _formId = "pageForm";
var _async;
var _total;
/**
 * 跳转到哪一页
 */
function jump(){
	_paging.setPage($("#toPage").val());
}
/**
 * 改变每页条数
 */
function chgPerPage(){
	page(_total, $("#perPage").val(), 1, _async, _formId);
}

/**
 * total : 总条数
 * perpage: 每页条数
 * page : 当前页
 * async : 异步请求   非必须
 */
function page(total, perpage, page, async, formId){
	if(formId != null && formId != "" && formId != 'undefined'){
		_formId = formId;
	}
	_async = async;
	_total = total;
	_paging = $("#" + _formId).paging(total, {
		format: '[< nncnn >] -',
		perpage: perpage,
		page: page,
		onSelect: function (page) {
			console.info(this);
			console.info("标识：" + _first);
			if(_first){
				_first = !_first;
				return;
			}
			if(_async === null || !_async || _async === 'undefined'){ //页面跳转
				$("#" + _formId)[0].action = $("#pageForm")[0].action + '?startNum=' + this.slice[0] + '&endNum=' + this.slice[1] + '&currentPage=' + page;
				$("#" + _formId).submit();
			}else{
				$.ajax({	//异步请求
		             type: "POST",
		             url: $("#" + _formId)[0].action + '?startNum=' + this.slice[0] + '&endNum=' + this.slice[1] + '&currentPage=' + page,
		             data: $("#" + _formId).serializeObject(),
		             dataType: "json",
		             success: function(data){
		            	 console.info(data);
		            	 _total = data.totalRows;
		            	 _paging.setNumber(data.totalRows);
		            	 _paging.setPage(data.currentPage);
		             }
		         });
			}
		},
		onFormat: function (type) {
			switch (type) {
			case 'block':
				if (!this.active)
					return '<em><span class="disabled">[' + this.value + ']&nbsp;</span></em>';
				else if (this.value != this.page)
					return '<a href="#' + this.value + '">[' + this.value + ']&nbsp;</a>';
				return '<em><span class="current">[' + this.value + ']&nbsp;</span></em>';
			case 'next':
				return '<a><font color="green">&nbsp;下一页&nbsp;</font></a>';
			case 'prev': 
				return '<a><font color="green">&nbsp;上一页&nbsp;</font></a>';
			case 'first':
				return '<a><font color="red">&nbsp;首页&nbsp;</font></a>';
			case 'last':
				return '<a><font color="red">&nbsp;末页&nbsp;</font></a>';
			case 'fill':
				console.info(this);
				if (this.active){
					console.info(this.pos);
					return "&nbsp;&nbsp;&nbsp;当前 [" + this.page + "]共[" + this.pages + "]页";
				}
				return "";
			}
		},
	});
}

/**
 * 序列化表单，需要抽到工具类中
 */
$.fn.serializeObject = function()
{
    var o = {};
    var a = this.serializeArray();
    $.each(a, function() {
        if (o[this.name] !== undefined) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};
</script>
