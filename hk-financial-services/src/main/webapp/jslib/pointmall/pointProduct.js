/**
 * 积分商品
 * @type {{}}
 */
var pointProductController = {};
pointProductController.allCates=null;
(function () {
    'use strict';
    //加载限时栏目函数
    pointProductController.loadIndexFlashSales=function () {
        ajaxUtil.post("/pointMallController/findIndexFlashSales.do", {pageSize:5}, function (data) {

            var flashSales = data.resMsg.data;
            if(flashSales.length>0){
                var content = ''
                content+='<div class="biaoqilan_1">'
                    + '鸿坤金服限时兑换'
                    + '<span class="word_sm f_l">多款超低折扣商品，意想不到的低价'
                    + '<a href="${project_base_path}/pointmall/goodsList.html?enginSource=flashSell"><img src="${project_base_path}/src/img/navBar/icon_jfsc_b.png"></a></span>'
                    + '</div>';
                $("#flashSalesBanner").append(content);

                content = '';
                for(var i=0;i<flashSales.length;i++){
                    var fs=flashSales[i];
                    content+='<li onclick="pointProductCommons.toProDetail('+fs.id+',true)">'
                        +'<img src="${oss.url}'+(fs.firstImg)+'"  alt="">'
                        +'<p class="s-renmp">'+fs.name+'</p>'
                        +'<div class="s-renmdivo">'
                        +'<div class="jifenqx fl"><span>'+fs.discountPoint+'</span>&nbsp;积分</div>'
                        +'<div class="jifenqx" style="float:right;"><a href="javascript:void()" class="right_num" style="color: #999 !important;text-decoration: line-through;">'+fs.point+'</a></div>'
                        +'</div>'
                        +'</li>'
                }

                $(".flashSales").empty();
                $(".flashSales").append(content);
            }else {
                //隐藏限时栏目
                $("#flashSalesBanner").hide();
                $(".flashSales").hide();
            }


        }, null, false);
    }

    //构造推荐、其他商品的函数
    pointProductController.buildPartProudcts=function (className, productList) {
        if(productList==null||productList.length==0){
            $("."+className).empty();
            $("." + className+"Unio").addClass("hide");
            return ;
        }
        $("." + className+"Unio").removeClass("hide");
        var contents = '';
        for (var i=0;i<productList.length;i++){
            var temp = productList[i];
            contents += '<li style="padding-bottom: 7px;" onclick="pointProductCommons.toProDetail('+temp.id+',false)">'
                +'<img src="${oss.url}'+(temp.firstImg)+'" alt="" />'
                + '<p class="s-renmp" style="line-height: 20px;margin-bottom: 2px;">'+temp.name+'</p>'
                + '<div class="s-renmdivo">'
                + '<div class="jifenqx" style="text-align: center;float: inherit"><span>'+temp.point+'</span> 积分</div>'
                + '</div>'
                + '</li>';
        }
        $("."+className).empty();
        $("."+className).append(contents);
    }
    //构造热门兑换商品的函数
    pointProductController.buildPartProudctsHot=function (className, productList) {
        if(productList==null||productList.length==0){
            $("."+className).empty();
            $("." + className+"Unio").addClass("hide");
            return ;
        }
        $("." + className+"Unio").removeClass("hide");
        var contents = '';
        for (var i=0;i<productList.length;i++){
            var temp = productList[i];
            contents += '<li style="padding-bottom: 7px;" onclick="pointProductCommons.toProDetail('+temp.id+',false)">'
                +'<img src="${oss.url}'+(temp.firstImg)+'" alt="" />'
                + '<p class="s-renmp" style="line-height: 20px;margin-bottom: 2px;">'+temp.name+'</p>'
                + '<div class="s-renmdivo">'
                + '<div class="jifenqx" style="float: left"><span>'+temp.point+'</span> 积分</div>'
                +'<div class="jifenqx" style="float:right;"><a href="javascript:void()" class="s-rembtn" style="width:80px;border:1px solid #f39200;display:block;">点击兑换</a></div>'
                + '</div>'
                + '</li>';
        }
        $("."+className).empty();
        $("."+className).append(contents);
    }

    //热门兑换   推荐商品   其他商品
    pointProductController.loadOtherPartProducts=function () {
            ajaxUtil.post("/pointMallController/findIndexProducts.do", {}, function (data) {
                var products = data.resMsg;
                //热门兑换
                pointProductController.buildPartProudctsHot('bestSeller',products.bestSellProducts);
                //推荐商品
                pointProductController.buildPartProudcts('recommendProducts',products.recommendIndexProducts);
                //其他商品
                pointProductController.buildPartProudcts('otherProducts',products.otherProducts);
            }, null, false);
     }
    //积分商城渲染轮播图、公告
    pointProductCommons.loadPointInformations = function () {
        var reqData = {position: 2}
        ajaxUtil.post("/informationController/findInfomations.do", reqData, function (data) {
            $(".Homebanner ul").empty();
            $(".Homedot").empty();
            //轮播图渲染
            if (validUtil.validNotEmpty(data.resMsg.carsouleFigureList)) {
                for (var i = 0; i <= eval(data.resMsg.carsouleFigureList).length - 1; i++) {
                    $(".Homebanner ul").append('<li class="Load cur" style="z-index:99">' + eval(data.resMsg.carsouleFigureList)[i].content + '</li>');
                    $(".Homedot").append('<a href="javascript:;"></a>');
                }
            }
            //公告图渲染
            $(".bd ul").empty();
            if (!validUtil.validNotEmpty(data.resMsg.noticeList)) {
                $(".bd ul").append("<li>暂无公告</li>");
            } else {
                for (var i = 0; i <= eval(data.resMsg.noticeList).length - 1; i++) {
                    $(".bd ul").append("<li><a href='../common/noticeDetail.html?id=" + eval(data.resMsg.noticeList)[i].id + "'>" + (i + 1) + '、' + eval(data.resMsg.noticeList)[i].title + "</a></li>")
                }
            }
        }, null, false);
    };

    //处理搜索框
    pointProductCommons.doSearch = function () {
        var searchKeys=$("#searchInput").val().trim();
       if(!commonUtil.isEmpty(searchKeys)){
           commonUtil.jump('/pointmall/goodsList.html?enginSource=doSearch&keyword='+searchKeys);
       }
    };

    //判断是否登录，如果登录，显示现有积分
    pointProductCommons.isLoginAndgetPoint(function () {
        $(".jifengxqdL").removeClass('hide');
        $("#pointUserHas").html(containerUtil.get("userPoint"));
    }, function () {
        $(".jifengxq").removeClass('hide');
    });
    //加载公告的函数
    pointProductCommons.loadPointInformations();
})();

$(function () {
    /*************主流程****************/
    var s_tx = document.getElementsByClassName('s-tx')
    $(s_tx).css({ 'height': 450 / s_tx.length + 'px', 'line-height': 450 / s_tx.length + 'px' })
    jQuery(".txtMarquee-left").slide({ mainCell: ".bd ul", autoPlay: true, effect: "leftMarquee", vis: 2, interTime: 30 });

    $('#buttonjs').on('click', function() { //计算积分
        $("#suanqujif").html(Math.floor($('#syueshu').val() * $('#sjine').val() / 2400));
    })

    $(".btn1").click(function() {
        $("p").slideToggle();
    });

    $('.s-titie').click(function() {

        if ($(this).next().css("display") == 'none') {
            $(this).addClass('jiantouxza')
            $(this).removeClass('jiantouxzafan')
        } else {
            $(this).addClass('jiantouxzafan')
            $(this).removeClass('jiantouxza')
        }
        $(this).next().slideToggle(500);
    })

    //初始化菜单
    //初始化首页菜单数据
    pointProductCommons.loadPointProductCategories(function (content) {
        $(".jspopbox").empty();
        $(".jspopbox").append(content);
        pointProductCommons.bindFunctionToMenu(false);
    })

    //加载限时栏目
    pointProductController.loadIndexFlashSales();

    //热门兑换   推荐商品   其他商品
    pointProductController.loadOtherPartProducts();





});