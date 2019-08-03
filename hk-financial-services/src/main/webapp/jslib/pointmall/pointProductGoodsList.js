/**
 * 积分商品
 * @type {{}}
 */
var pointProductGoodList = {};
(function () {
    $(function () {

        //初始化商品列表页菜单数据
        pointProductCommons.loadPointProductCategories(function (content) {
            $("#menu").empty();
            $("#menu").append(content);
            pointProductCommons.bindFunctionToMenu(false);

        });

        pointProductGoodList.clickNoLimitElement = function (currentLevel) {
            //处理当前行
            $(".searchElement[currentLevel=" + currentLevel + "]").removeClass("xzspan searchSelected")
            $(".noLimitElement[currentLevel=" + currentLevel + "]").addClass("show spanxuanzhong9")

            //处理子行，隐藏子行,以及删除子行的选中状态
            while ($(".searchElement[currentLevel=" + (++currentLevel) + "]").length > 0) {
                $(".searchElement[currentLevel=" + (currentLevel) + "]").removeClass("show searchSelected").addClass("hide")
                $(".noLimitElement[currentLevel=" + currentLevel + "]").removeClass("show").addClass("hide")

            }
            //启动搜索引擎
            pointProductGoodList.startSearchEngine();
        }

        /**
         * 渲染商品列表页
         * @private
         */
        pointProductGoodList.renderGoodsListPage= function renderGoodsListPage(result,flashProduct) {
            flashProduct = flashProduct == null ? false : flashProduct;
            var contents = '';
            var currentPageNo=0;
            var totalPageNo=0;
            if(result!=null&&result.resMsg!=null && result.resMsg.data!=null &&  result.resMsg.data.length>0){
                $("#productShow").show();
                $("#nodata").hide();
                $(".paging").show();
                currentPageNo = result.resMsg.currentPage;
                totalPageNo = result.resMsg.totalPages;
                var products=result.resMsg.data;

                for(var i=0;i<products.length;i++){
                    var temp = products[i];
                    flashProduct = (temp.flashSale==1 || temp.flashSale=='1')?true:false;
                    if(!flashProduct){
                        contents += '<li style="padding-bottom: 7px;" onclick="pointProductCommons.toProDetail('+temp.id+',false)">'
                            +'<img src="${oss.url}'+(temp.firstImg)+'" alt="" />'
                            + '<p class="s-renmp" >'+temp.name+'</p>'
                            + '<div class="s-renmdivo">'
                            + '<div class="jifenqx" style="text-align: center;float: inherit"><span>'+temp.point+'</span> 积分</div>'
                            + '</div>'
                            + '</li>';
                    }else{
                        contents+='<li onclick="pointProductCommons.toProDetail('+temp.id+',true)">'
                            +'<img src="${oss.url}'+(temp.firstImg)+'"  alt="">'
                            +'<p class="s-renmp">'+temp.name+'</p>'
                            +'<div class="s-renmdivo">'
                            +'<div class="jifenqx fl"><span>'+temp.discountPoint+'</span>&nbsp;积分</div>'
                            +'<div class="jifenqx" style="float:right;"><a href="javascript:void()" class="right_num" style="color: #999 !important;text-decoration: line-through;">'+temp.point+'</a></div>'
                            +'</div>'
                            +'</li>'
                    }

                }

                //拼接商品
                $("#productShow").empty();
                $("#productShow").append(contents);

            }else{
                $("#productShow").hide();
                $("#nodata").show();
                $(".paging").hide();
            }

            // 处理右上角分页信息
            $("#currentPageNo").html(currentPageNo);
            $("#totalPageNo").html(totalPageNo);

            //同步分页事件到右上角前后款
            $("#right_pre").attr("onclick", $(".prev_buttn").attr("onclick"));
            $("#right_next").attr("onclick", $(".next_buttn").attr("onclick"));

        }

        //初始化分类搜索条件
        pointProductGoodList.initSearchElement = function () {
            $(".menuDiv").empty();
            //决定商品中会有几栏
            var leveledMap = pointProductCommons.initLeveledCates();
            var count = 1;
            var filterArr;
            while ((filterArr = leveledMap.get(count++)) != null) {
                var contents = '<li class="ul-lirq cutoumro" style="display: block">'
                    + '<span class="ul-lispan noLimitElement"  currentLevel="' + (count - 1) + '"  onclick="pointProductGoodList.clickNoLimitElement(' + (count - 1) + ')">不限</span>'
                    + '<div class="s-fenlei ">'
                for (var i = 0; i < filterArr.length; i++) {
                    var temp = filterArr[i];
                    //拼接分类
                    contents += '<span onclick="pointProductGoodList.initPointProductFilter(' + temp.id + ')"  class="searchElement" currentLevel="' + (count - 1) + '" currentId="' + temp.id + '"  currentPid="' + temp.parentId + '" >' + temp.title + '</span>'
                }
                contents += '</div>';
                contents += '<div class="s-titie s-titiefan hide moreElement "   currentLevel="' + (count - 1) + '"><em>更多</em><i></i></div>';
                contents += '</li>';
                //拼接上
                $("#menuDiv").append(contents);
                contents = '';
            }

            var s_fenlei = document.getElementsByClassName('s-fenlei')

            for (var i = 0; i < s_fenlei.length; i++) {    /*获取级别实际高度*/
            	//console.info(s_fenlei[i]);
                s_fenlei[i].oheight = s_fenlei[i].offsetHeight
                s_fenlei[i].style.height = '20px'
                s_fenlei[i].oflg = true
            }
            $('.s-titie').click(function () {
                var othis = $(this)
                var shijine = othis.prev()
                if (shijine[0].oflg) { //true是收缩状态
                    //shijine.css({'height': shijine[0].oheight})
                	//shijine.removeClass();
                	shijine.removeAttr("style");
                    othis.find("em").html('收起')
                    shijine[0].oflg = false
                } else {
                    shijine.css({'height': '20px'})
                    othis.find("em").html('更多')
                    shijine[0].oflg = true
                }
                othis.toggleClass('s-titiefan')
            })

        }

        //搜索引擎
        pointProductGoodList.startSearchEngine = function (enginSource/*启动引擎页面：普通:common，我能兑换:iCanPay，限时兑换:flashSell ，手动输入积分上下限：fromPointRange*/) {
            //确定启动源
            var enginSource1=pointProductCommons.getUrlParam("enginSource");//检查url中是否指定，优先级1
            if(enginSource1 != null && enginSource1 !='undefined' && enginSource1 !=undefined){
            	enginSource = enginSource1;
            }
            enginSource = enginSource == null ? 'common' : enginSource;//检查参数中是否指定，优先级2

            var searchParams = {};
            var needCateId=false;
            var needPointRange=false;
            var needSortType=false;
            var flashSearch = false;
            var url='/pointMallController/filterProduct.do'
            searchParams.pageSize=40;

            /**
             step 2:取到积分范围
             */
            var pointStart = null;
            var pointEnd = null;
            //检查启动方式
            if (enginSource=='fromPointRange'/*手动选择积分范围启动*/) {
                //去除积分范围选中
                $(".pointRange").removeClass("spanxuanzhong9");
                searchParams.pointStart = $("#pointStart").val();
                searchParams.pointEnd = $("#pointEnd").val();
                needCateId=true;
            } else if(enginSource=='flashSell'/*限时兑换页面启动*/){
                //请求另外一个接口
                url="/pointMallController/findIndexFlashSales.do"
                needSortType=true;
                flashSearch=true;
                $("#flashPayHref").addClass("zhusediao")
            } else if(enginSource=='iCanPay'/*我能兑换页面启动*/){
                //请求用户的积分
                searchParams.pointEnd = containerUtil.get("userPoint");
                needSortType=true;
                $("#pointMallUser").html(containerUtil.get("pointMallUser"))
                $("#userPointValue").html(containerUtil.get("userPoint"))
                $("#userPointInfo").removeClass("hide");
                $("#iCanPayHref").addClass("zhusediao")

            }else if(enginSource=='otherMoreGoods'/*点击其他栏目的更多商品启动*/){
                $("#filterProductDiv").removeClass("hide");
                //关键字提示
                $("#keyShow").removeClass("hide");
                $("#keyShow").empty();
                $("#keyShow").append('<span><b>全部结果 &gt;其他商品</b></span>');

                searchParams.recommend = 0;
                searchParams.flashSale = 0;
                //搜索条件全部要
                needCateId=true;
                needPointRange=true;
                needSortType=true;

            }else if(enginSource=='doSearch'/*点击搜索启动*/){
                $("#filterProductDiv").removeClass("hide");
                $(".shaixuanrongqi").addClass("hide");
                searchParams.productName =  pointProductCommons.getUrlParam("keyword");
                //关键字提示
                $("#keyShow").removeClass("hide");
                $("#keyShow").empty();
                $("#keyShow").append('<span><b>全部结果 &gt;\''+searchParams.productName+'\'</b></span>');

                //搜索条件全部要
                needCateId=true;
                needPointRange=true;
                needSortType=true;

            }else/*普通启动方式：遍历所有的选择条件*/{

                $("#filterProductDiv").removeClass("hide");
                 needCateId=true;
                 needPointRange=true;
                 needSortType=true;
            }

            /***********判断选择条件*******************/
            //判断选择条件
            if(needCateId){
                /**
                 step 1:取到分类Id
                 */
                searchParams.cateId = $(".searchSelected").attr("currentId");
                var xuanzhong =  $(".spanxuanzhong9").attr("currentlevel");
                if((searchParams.cateId == null || searchParams.cateId=='' || searchParams.cateId =='undefined' || searchParams.cateId ==undefined) && xuanzhong !='1'){
                	searchParams.cateId =$(".xzspan").eq(parseInt(xuanzhong)-2).attr("currentId");
                }
                
            }

            //1.判断积分范围
            if(needPointRange){
                var pointSelected = $(".pointRange").filter(".spanxuanzhong9");

                if(pointSelected.length==0){
                    //说明已经填写的积分范围，采用积分返回
                    searchParams.pointStart = $("#pointStart").val();
                    searchParams.pointEnd = $("#pointEnd").val();
                }else{
                    //获取选择的范围
                    searchParams.pointStart = pointSelected.attr("pointStart");
                    searchParams.pointEnd = pointSelected.attr("pointEnd");
                    $("#pointStart").val('');
                    $("#pointEnd").val('');
                }
            }

            //2.判断排序方式
            if(needSortType){
                /**
                 step 3:取到排序方式
                 */
                searchParams.sortType = pointProductGoodList.sortType;
            }

            // 3.执行搜索
            var result = pageUtil.initPaging(url, searchParams, pointProductGoodList.renderGoodsListPage, 1);

            if(flashSearch){
                pointProductGoodList.renderGoodsListPage(result,flashSearch);
            }else{
                pointProductGoodList.renderGoodsListPage(result);
            }


        }

        //初始化第一行的选择
        pointProductGoodList.initPointProductFilter = function (productId) {

            /***************初始化商品筛选框************************/
            //先隐藏所有的元素
            $(".searchElement").removeClass("show").addClass("hide");
            $(".noLimitElement").removeClass("show").addClass("hide");
            $(".noLimitElement").parent().removeClass("show").addClass("hide");
            //删除上一次的选择结果
            $(".searchElement").removeClass("xzspan")
            $(".searchElement").removeClass("show")
            $(".searchElement").removeClass("searchSelected")

            $(".noLimitElement").removeClass("show").addClass("hide")
            $(".noLimitElement").parent().removeClass("show").addClass("hide");
            $(".noLimitElement").removeClass("spanxuanzhong9")


            //尝试取到searchCate
            var searchCate = pointProductCommons.getUrlParam("searchCate");
            if (productId == null && searchCate != null) {
                productId = searchCate;
            }
            //处理第一行的更多按钮
            if ($(".searchElement[currentLevel=1]").length > 6) {
                $(".moreElement[currentLevel=1]").removeClass("hide").addClass("show");
            } else {
                $(".moreElement[currentLevel=1]").removeClass("show").addClass("hide");
            }
            if (productId == null) {
                //不指定productId，把第一行的不限选中，其他的隐藏
                $(".searchElement[currentLevel=1]").removeClass("hide").addClass("show")
                $(".noLimitElement[currentLevel=1]").addClass("show spanxuanzhong9")
                $(".noLimitElement[currentLevel=1]").parent().addClass("show");
            } else {

                //指定productId:把当前元素选中，显示本级以及上一级所有的兄弟元素，显示当前运输下一级
                var $this = $(".searchElement[currentId=" + productId + "]");

                var currentId = $this.attr("currentId");
                var currentLevel = $this.attr("currentLevel");
                /**
                 step 1:处理父
                 */
                var loopPid/*用于循环的pid*/ = $this.attr("currentPid");
                var pid = loopPid;
                var $parent;
                while (($parent = $(".searchElement[currentId=" + loopPid + "]")).attr("currentId") != null) {
                    var grandParentId = $parent.first().attr("currentPid");
                    var thisLineLevel = $parent.first().attr("currentLevel");
                    //把父类的兄弟显示
                    $(".searchElement[currentPid=" + grandParentId + "]").removeClass("hide").addClass("show")
                    //把当前行的不限显示
                    $(".noLimitElement[currentLevel=" + thisLineLevel + "]").removeClass("hide").addClass("show")
                   $(".noLimitElement[currentLevel=" + thisLineLevel + "]").parent().removeClass("hide").addClass("show")

                    //高亮父元素
                    $parent.addClass("xzspan");
                    loopPid = grandParentId;

                    //处理更多
                    if ($(".searchElement[currentPid=" + grandParentId + "]").length > 6) {
                        $(".moreElement[currentLevel=" + thisLineLevel + "]").removeClass("hide").addClass("show");
                    } else {
                        $(".moreElement[currentLevel=" + thisLineLevel + "]").removeClass("show").addClass("hide");
                    }

                }

                /**
                 step 2:处理当前行
                 */
                //把同一个分类下的兄弟显示
                $(".searchElement[currentPid=" + pid + "]").removeClass("hide").addClass("show")
                //高亮当前元素
                $this.addClass("xzspan searchSelected");
                //显示当前不限制
                $(".noLimitElement[currentLevel=" + currentLevel + "]").removeClass("hide").addClass("show");
                 $(".noLimitElement[currentLevel=" + currentLevel + "]").parent().removeClass("hide").addClass("show");
                /**
                 step 2:处理子元素
                 */
                //显示子元素
                $(".searchElement[currentPid=" + currentId + "]").removeClass("hide").addClass("show")
                //高亮子不限制
                var nextLenvel = parseInt(currentLevel) + 1;
                if ($(".searchElement[currentPid=" + currentId + "]").length > 0) {
                    $(".noLimitElement[currentLevel=" + nextLenvel + "]").addClass("show spanxuanzhong9 ");
                   $(".noLimitElement[currentLevel=" + nextLenvel + "]").parent().addClass("show")
                }

                //处理更多
                if ($(".searchElement[currentPid=" + currentId + "]").length > 6) {
                    $(".moreElement[currentLevel=" + nextLenvel + "]").removeClass("hide").addClass("show");
                } else {
                    $(".moreElement[currentLevel=" + nextLenvel + "]").removeClass("show").addClass("hide");
                }

            }

            /*********************初始化积分范围框************************************/
            $(".pointRange").click(function () {
                var $this = $(this);
                $(".pointRange").removeClass("spanxuanzhong9");
                $this.addClass("spanxuanzhong9");
                //启动搜索引擎
                pointProductGoodList.startSearchEngine();
            })

            /**********************初始化排序方式***************************************/
            $('.titleh3fan em').click(function () {  /*默认兑换排行点击*/
                $(this).addClass('zhusediao').siblings().removeClass('zhusediao')
                $('.topbutjiant').html('积分值<img src="../src/img/navBar/doushihse.png" alt=""/>')
                $('.topbutjiant')[0].oflg = false
                pointProductGoodList.sortType =parseInt($(this).attr("sort-type"));
                //启动搜索引擎
                pointProductGoodList.startSearchEngine();
            })

            $('.topbutjiant').click(function () {
                if (this.oflg) {
                    $('.titleh3fan em').removeClass('zhusediao')

                    $(this).html('积分值<img src="../src/img/navBar/topjiant.png" alt=""/>')
                    this.oflg = false

                    /*升序*/
                    pointProductGoodList.sortType = 3;
                } else {
                    $('.titleh3fan em').removeClass('zhusediao')
                    $(this).html('积分值<img src="../src/img/navBar/botjiant.png" alt=""/>')
                    this.oflg = true;

                    /*降序*/
                    pointProductGoodList.sortType = 4;
                }

                //启动搜索引擎
                pointProductGoodList.startSearchEngine();
            });
            pointProductGoodList.sortType = 1;
            /*默认方式*/

            //启动搜索引擎
            pointProductGoodList.startSearchEngine();

        }

        //初始化搜索元素
        pointProductGoodList.initSearchElement();
        //初始化商品筛选行
        pointProductGoodList.initPointProductFilter(null);

    });

})();