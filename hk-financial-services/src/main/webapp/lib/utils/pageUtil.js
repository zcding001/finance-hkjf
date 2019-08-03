/***
 * @Description  分页的JS工具类型
 * 在需要分页的列表页加上 <div class="paging"></div>元素，就可以自动渲染分页
 * Created by yanbinghuang
 */
var pageUtil = {};
var curPage = 1; //当前页
var totalPage = 0; //总页数
var pageSize = 10; //每页显示多少条
var count;
var outStr = ""; //输出到页面的分页
var showCount = 5;//每个列表显示多少条分页
var pageFlag = 0;//0：表格分页，1：普通的分页
(function () {
	var _obj={};
	/***
	 * @Description 初始化列表分页
	 * @Method_Name  pageUtil.initPage
     * @param url 加载数据的地址
     * @param tableId  tableId
     * @param columns 列明
     * @param columnsCount 总列数
     * @param cbOpts 是否含所有checkbok
     * @param currentPage 当前页
     * @param totalPages 总页数
     * Created by yanbinghuang
     */
    pageUtil.initPage = function (url, tableId, columns, columnsCount, cbOpts, currentPage, totalPages, pageClass) {
    	pageClass=validUtil.validNotEmpty(pageClass) ? pageClass : "paging" ;
    	
    	if(!$("." + pageClass)){
			return;
		}
    	pageFlag = 0;
		if(totalPages <= 0){
			$("." + pageClass).hide();
			return;
		}
		$("." + pageClass).show();
    	_obj.url = url;
    	_obj.tableId = tableId;
    	_obj.columns = columns;
    	_obj.columnsCount = columnsCount;
    	_obj.cbOpts = cbOpts;
    	_obj.pageClass = pageClass;
    	totalPage = totalPages;
    	curPage = currentPage;
    	if(pageClass == "paging"){
    		_setpage(null);
    	}else{
    		_setpage(pageClass);
    	}
    	
    };
    /***
	 * @Description 初始化列表分页
	 * @Method_Name  pageUtil.initPage
     * @param currentPage 当前页
     * @param totalPages 总页数
     * Created by yanbinghuang
     */
    pageUtil.initPaging = function (reqUrl, params, callBack, currentPage,pageClass) {
        pageClass=validUtil.validNotEmpty(pageClass) ? pageClass : "paging" ;
    	pageFlag = 1;
    	var resultData = null;
    	var totalPages=0;
    	ajaxUtil.post(reqUrl,params,function(data){
    		 totalPages = data.resMsg.totalPages;
    		resultData = data;
    	},"json",false);

		if(totalPages <= 0){
			$("."+pageClass).hide();
			return;
		}
		$("."+pageClass).show();
		_obj.reqUrl = reqUrl;
    	_obj.params = params;
    	_obj.callBack = callBack;
    	totalPage = totalPages;
    	curPage = currentPage;
    	_setpage(pageClass);
    	return resultData;
    };

    /***
	 * @Description 跳转到第几页
	 * @Method_Name  pageUtil.goPage
     * @param currentPage 页码
     * Created by yanbinghuang
     */
    pageUtil.goPage = function (currentPage) {  
    	curPage = currentPage;
	    _setpage(); 
	    if(pageFlag == 1){
	    	_obj.params.currentPage = curPage;
	    	_obj.params.totalPages = totalPage;
	    	ajaxUtil.post(_obj.reqUrl,_obj.params,function(data){
	    		_obj.callBack(data);
	    	});
	    }else{
	    	//调用显示页面函数显示第几页,这个功能是用在页面内容用ajax载入的情况 
		    renderUtil.loadData(_obj.url, _obj.tableId, _obj.columns, _obj.columnsCount, _obj.cbOpts, curPage,null,_obj.pageClass); 
	    }
	      
	};
	 /***
	 * @Description 渲染分页
	 * @Method_Name  _setpage
     * Created by yanbinghuang
     */
	function _setpage(pageClass) {

         pageClass=validUtil.validNotEmpty(pageClass) ?pageClass : "paging" ;
		//前一页
		if(curPage != 1){
            outStr = outStr + "<a href='javascript:void(0)' class='prev_buttn' onclick='pageUtil.goPage("+(curPage-1)+")'>Prev</a>";

		}else{
			outStr = outStr + "<span class='disabled prev_buttn'>Prev</span>";
		}
		//中间页
	    if(totalPage <= showCount){ //总页数小于showCount页 
	        for (count = 1;count <= totalPage;count++) {
	        	if(count != curPage) { 
	                outStr = outStr + "<a href='javascript:void(0)' onclick='pageUtil.goPage(" + count + ")'>" + count + "</a>";

	            }else{ 
	                outStr = outStr + "<span class='current' >" + count + "</span>"; 
	            } 
	        }
	    } 
	    if(totalPage > showCount){//总页数大于showCount页 
	    	for (count = 1;count <= showCount; count++) {
	    		if(curPage < (parseInt(showCount / 2) + 2)){
	    			if(count == curPage){
	    				outStr = outStr + "<span class='current' >" + curPage + "</span>";
	    			}else{
		   				 outStr = outStr + "<a href='javascript:void(0)' onclick='pageUtil.goPage(" + count + ")'>" + count + "</a>";

	    			}
	    		}else if(curPage >= (parseInt(showCount / 2) + 2)){
		    		if(count == (parseInt(showCount / 2) + 1)){
		    			 outStr = outStr + "<span class='current' >" + curPage + "</span>";
		    		}else if(count < (parseInt(showCount / 2) + 1)){
		   				 outStr = outStr + "<a href='javascript:void(0)' onclick='pageUtil.goPage(" + (curPage - ((parseInt(showCount / 2) + 1) - count)) + ")'>" + (curPage - ((parseInt(showCount / 2) + 1) - count)) + "</a>";
		    		}else{
		    			 if(curPage != totalPage){
			   				 outStr = outStr + "<a href='javascript:void(0)' onclick='pageUtil.goPage(" + (curPage + (count-(parseInt(showCount / 2) + 1))) + ")'>" + (curPage + (count-(parseInt(showCount / 2) + 1))) + "</a>";
		    			     if(totalPage -curPage == 1){
		    			    	 break;
		    			     }
		    			 }
		    		}
	    		}
	        }
	    }
	    //后一页
	    if(curPage == totalPage){
            outStr = outStr + "<span class='disabled next_buttn'>Next</span>";
        }else{
            outStr = outStr + "<a href='javascript:void(0)'  class='next_buttn' onclick='pageUtil.goPage(" + (curPage + 1) + ")'> Next </a>";
        }
	    
		$("."+pageClass).html("<div id='setpage' style='text-align:center;'><span id='info'>" +  outStr + "<\/div>");
		  outStr = ""; 
		} 
})();
