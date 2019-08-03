/**
 * 首页标的信息的controller
 */

var indexFunds = {};
//判断是否登录状态
var isLogin = commonUtil.isLogin();
//获取股权基金实名测评标识
fundCommon.getFundAuthentication();
(function() {
    'use strict';

    //初始化股权信息
    indexFunds.initIndexFundInfo=function () {
        //加载首页股权数据
        ajaxUtil.post("fundIndexController/searchFundInformations.do", {}, function(data){
            if(data.resStatus == CONSTANTS.SUCCESS) {
                $("#sm_index").append(initFundHtml(data.params.privateList));
                $("#hw_index").append(initFundHtml(data.params.overseaList));
                $("#xt_index").append(initFundHtml(data.params.trustList));
                $("#fc_index").append(initFundHtml(data.params.houseList));
            }
        });
    }

    function initFundHtml(fundList){
        var newsContent = "";
        for(var i in fundList){
            var o = fundList[i];
            if(isLogin){
                if(fundAuthFlag == 2){
                    newsContent += '<li><a style="float: left;" href="common/noticeDetail.html?id='+o.id+'">'+o.title+'</a>'+'<div style="float: right">'+dateUtil.date(o.createTime)+'</div><br/></li>';
                }else{
                    newsContent += '<li><a style="float: left;" onclick="fundCommon.authenJump('+ fundAuthFlag +')" href="javascript:void(0);">'+o.title+'</a>'+'<div style="float: right">'+dateUtil.date(o.createTime)+'</div><br/></li>';
                }
            }else{
                newsContent += '<li><a style="float: left;" href="register/login.html">'+o.title+'</a>'+'<div style="float: right">'+dateUtil.date(o.createTime)+'</div><br/></li>';
            }
        }
        return newsContent;
    }


})();


