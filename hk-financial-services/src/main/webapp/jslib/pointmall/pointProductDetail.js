/**
 * 积分商品
 * @type {{}}
 */
var pointProductDetail = {};
(function () {
    $(function () {
        $(".jifenShop-nav li").click(function () {
            var $this = $(this);
            index = $this.index();
            $this.addClass("Shop-nav-active").siblings("li").removeClass("Shop-nav-active");
            $(".hot-products .hot-main").eq(index).addClass("hot-selected").siblings(".hot-main").removeClass("hot-selected");
        });

        //获取url中的参数
        pointProductDetail.getUrlParam = function (name) {
            var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
            var r = window.location.search.substr(1).match(reg);  //匹配目标参数
            if (r != null) return unescape(r[2]);
            return null; //返回参数值
        }

        // 处理倒计时
        pointProductDetail.getRTime = function () {
            var EndTime = new Date(pointProductDetail.showEndTime); //获取结束时间
            var NowTime = new Date();
            var t = EndTime.getTime() - NowTime.getTime();
            var d = '00';
            var h = '00';
            var m = '00';
            var s = '00';
            if (t >= 0) {
                d = Math.floor(t / 1000 / 60 / 60 / 24);
                h = Math.floor(t / 1000 / 60 / 60 % 24);
                m = Math.floor(t / 1000 / 60 % 60);
                s = Math.floor(t / 1000 % 60);
            } else {
                $("#dhbtn1").hide();
                $("#dhbtn2").show();
                clearInterval(id);
            }
            document.getElementById("t_d").innerHTML = d;
            document.getElementById("t_d").style.color = "#f7931e";
            document.getElementById("t_h").innerHTML = h;
            document.getElementById("t_h").style.color = "#f7931e";
            document.getElementById("t_m").innerHTML = m;
            document.getElementById("t_m").style.color = "#f7931e";
            document.getElementById("t_s").innerHTML = s;
            document.getElementById("t_s").style.color = "#f7931e";
        }
        //加载商品详情数据
        ajaxUtil.post("/pointMallController/findProduct.do", {id: pointProductDetail.getUrlParam("product")}, function (data) {
            var product = data.resMsg;
            //加入缓存
            var cacheKey = "pointProduct-" + product.id;
            containerUtil.set(cacheKey, product);

            //设置进来时候的首图
            var imgurlPix = "${oss.url}";
            $("#preview2").attr("jqimg", imgurlPix + product.headImg.bigImgUrl);
            $("#preview2").attr("src", imgurlPix + product.headImg.smallImgUrl);

            //设置图片列表
            $("#imgList").empty();
            for (var i = product.productImgList.length-1; i >=0 ; i--) {
                var tempImg = product.productImgList[i];
                $("#imgList").append(' <li><img alt="图片缺失" bimg="' + imgurlPix + tempImg.bigImgUrl + '" src="' + imgurlPix + tempImg.smallImgUrl + '" onmousemove="preview(this);"></li>')
            }

            if (pointProductDetail.getUrlParam("commonProduct") != null) {
                //设置商品其他信息
                //商品名称
                $("#productTitle").html(product.name);
                //价格
                $("#needPoint").val(product.point);
                //价格
                $("#point").val(product.point);
                //数量
                $("#productNumber").html(product.number);
                //产品详情，产品细节
                $("#productDetail").html(product.goodsDetails);
                $("#productInfo").html(product.prductInfo);

                $("#commonProduct").show();
                $("#flashSale").hide();
            }

            if (pointProductDetail.getUrlParam("flashSale") != null) {
                //设置商品其他信息
                $("#discountPoint").html(product.discountPoint);
                $("#orginPoint").html(product.point + '积分');
                $("#flashNumber").html('仅限' + product.number + '件，兑换完成自动恢复原价');
                $("#sellsCount").html(product.salesCount);
                //产品详情，产品细节
                $("#productDetail").html(product.goodsDetails);
                $("#productInfo").html(product.prductInfo);
                pointProductDetail.showEndTime = product.showTimeEnd;
                setInterval(pointProductDetail.getRTime, 1000);
                //------------------------活动计时结束 -----------------------------

                $("#commonProduct").hide();
                $("#flashSale").show();
                $("#flashSellBtn")
                    .empty()
                    .append(' <a id="dhbtn1" href="#" onclick="pointProductDetail.exchange('+product.discountPoint+')" class="btn btn-warning btn-duihuan">立即兑换</a>');
            }


        }, null, false);

        //图片放大镜效果
        $(".jqzoom").jqueryzoom({xzoom: 380, yzoom: 410});
    });

    //点击兑换函数
    //containerUtil.set("pointProduct-"+temp.id,temp);

    //点击兑换
    pointProductDetail.exchange = function exchange(buyPoint) {
        pointProductCommons.isLoginAndgetPoint(null);
        //前置验证积分是否足够
        var havePoints = containerUtil.get("userPoint");
        var buyNum = $("#number").val();
        var number = (buyNum==null||buyNum==''||buyNum==undefined)?1:$.trim(buyNum);
        var point = ($("#point").val()==undefined||$("#point").val()==null||$("#point").val()=='')?buyPoint:$("#point").val();
        var needPoint = parseInt(number) * parseInt(point)
        if (needPoint > parseInt(havePoints)) {
            dialogUtil.alert("提示", "您的现有积分 "+havePoints+" 不足以兑换!");
        } else {
            var productId = pointProductCommons.getUrlParam("product");
            containerUtil.set("userOrderNum-" + productId, number);
            containerUtil.set("userOrderNeedPoint-" + productId, needPoint);
            commonUtil.jump("/pointmall/confirmExchange.html?product=" + productId);
        }

    }


})();