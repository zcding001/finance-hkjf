/**
 * 积分商品的一些通用的配置
 * @type {{}}
 */
var pointProductCommons = {};
//分级的各个菜单
pointProductCommons.leveledCates=null;
(function () {
    //初始化从后台请求的数据
    //全部菜单的分页数据
    pointProductCommons.setIfNot=function (levelCount, tempLevel) {
        if(tempLevel==null){
            tempLevel=[];
            pointProductCommons.leveledCates.set(levelCount,tempLevel);
        }
        return tempLevel;
    }

    //对搜索条件进行分层
    pointProductCommons.putThisTreeToMap=function(singleTree,levelCount){
        //处理当前节点
        levelCount = levelCount == null ? 1 : levelCount;
        var tempLevelArr=pointProductCommons.setIfNot(levelCount,pointProductCommons.leveledCates.get(levelCount));
        tempLevelArr.push(singleTree);
        //处理子节点
       if(singleTree.nodes!=null&&singleTree.nodes.length>0){
           var nextLevel=++levelCount;
           for(var i=0;i<singleTree.nodes.length;i++){
               var tempNode=singleTree.nodes[i]
               pointProductCommons.putThisTreeToMap(tempNode,nextLevel);
           }

       }
       //出口
    }
    pointProductCommons.initPointProductCategories = function (callBack) {
        var allCates = null;
        ajaxUtil.post("/pointMallController/loadMallCategories.do", {}, function (data) {
            allCates=data.resMsg;
        }, null, false);
        return allCates;
    };

    pointProductCommons.initLeveledCates=function () {
        if(pointProductCommons.leveledCates==null){
            //给菜单进行分层
            pointProductCommons.leveledCates=new Map();
            var allCates=pointProductCommons.initPointProductCategories();
            //初始化所有的map
            for(var i=0;i<allCates.length;i++){
                pointProductCommons.putThisTreeToMap(allCates[i],null);
            }
        }
        return pointProductCommons.leveledCates;
    }

    //给菜单绑定事件
    pointProductCommons.bindFunctionToMenu=function (showAsComplete) {
        if(showAsComplete){
            $("#menu").css({ 'display': 'block' });
        }
        $('.s-tx').mouseenter(function() {
            var offsettop = this.offsetTop,
                $pop = $(this).children(".pop")
            $(this).css({ "background-color": "white", 'color': '#f39200' });
            if ($pop.children().length > 0) {
                $pop.css({ 'display': 'block' })

                $pop.css({ 'top': offsettop + 'px' })
            }
        });
        $('.s-tx').mouseleave(function() {
            $(this).css({ "background-color": "inherit", 'color': '#333333' });
            $(this).children(".pop").css({ 'display': 'none' })
        });
        $('.s-tx').css({ 'height': 450 / $('.s-tx').length + 'px', 'line-height': 450 / $('.s-tx').length + 'px' })

    }

    //加载商品分类的函数
    pointProductCommons.loadPointProductCategories=function (callBackFn) {
        //TODO：zhongping 无限级菜单构造
        var cates = pointProductCommons.initPointProductCategories();
        var content = ''
        //构造菜单
        for(var i=0;i<cates.length;i++){
            var levelOne=cates[i];
            content += '<li class="s-tx">';
            //一级菜单
            content+='<a href="goodsList.html?searchCate='+levelOne.id+'">'+levelOne.title+'</a>';
            content+='<div class="pop" style="display: none; top: 0px;">'
            if(levelOne.nodes!=null&&levelOne.nodes.length>0){
                content+='<dl style="display: block;">'
                var levelTwo=levelOne.nodes
                //二级菜单
                for(var n=0;n<levelTwo.length;n++){
                    var twoNode = levelTwo[n];
                    content+= '<dt style="display: block;margin: 15px 0;"><a href="goodsList.html?searchCate='+twoNode.id+'">'+twoNode.title+'&gt;</a></dt>'
                    if(twoNode.nodes!=null&&twoNode.nodes.length>0){
                        content+= '<dd>'
                        var threeNode=twoNode.nodes;
                        //三级菜单
                        for(var k=0;k<threeNode.length;k++){
                            var kk = threeNode[k];
                            content+='<a href="goodsList.html?searchCate='+kk.id+'">'+kk.title+'</a>'
                        }
                        content+='</dd>'
                    }
                }
                content+='</dl>'
            }
            content += '</div></li>';

        }

        //回调函数之类的
        callBackFn(content);

    }
    //验证登录然后返回积分
    pointProductCommons.isLoginAndgetPoint=function (callBack,noLoginCallBack){

        //检查是否登录
        ajaxUtil.sendAjaxNoRedictToLogin("pointMallController/loginOrReturnPoint.do", {}, function(data){
            if(data.resStatus == CONSTANTS.SUCCESS){
                containerUtil.set("pointMallUser", data.params.userName);
                containerUtil.set("userPoint", data.params.point);
                containerUtil.set("login_reg_user", data.params.login_reg_user);
                if(callBack!=null){
                    callBack();
                }
            }else{
                if(noLoginCallBack==null){
                    commonUtil.jump("/register/login.html");
                }
                if(noLoginCallBack!=null&&typeof (noLoginCallBack)=='function'){
                    noLoginCallBack();
                }
                return ;
            }
        });
    }

    //前往我能兑换页面
    pointProductCommons.toICanPay=function () {
        pointProductCommons.isLoginAndgetPoint(function () {
            commonUtil.jump('/pointmall/goodsList.html?enginSource=iCanPay');
        });

    }

    //获取url中的参数
    pointProductCommons.getUrlParam=function (name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
        var r = window.location.search.substr(1).match(reg);  //匹配目标参数
        if (r != null) return decodeURI(r[2]) ;
        return null; //返回参数值
    }


    //跳转到商品详情页的操作
    pointProductCommons.toProDetail= function toProDetail(code,isFlash) {
        if(isFlash){
            window.location.href = "commodityDetails.html?flashSale=true&&product=" + code;
        }else{
            window.location.href = "commodityDetails.html?commonProduct=true&&product=" + code;
        }

    }



})();