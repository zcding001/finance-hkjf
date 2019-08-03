<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>房产详情</title>
  <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
   <link rel="stylesheet" href="${request.contextPath}/jslib/house/css/swiper.min.css">
  <link rel="stylesheet" href="${request.contextPath}/jslib/house/css/house_detail.css">
  <script type="text/javascript" src="${request.contextPath}/jslib/jquery-3.2.1.min.js"></script>
  <script src="${request.contextPath}/jslib/house/swiper.min.js"></script>
<style>

</style>
  <script>
      (function(doc, win) { // font-size.js
          var docEl = doc.documentElement,
              resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize',
              recalc = function() {
                  var clientWidth = docEl.clientWidth;
                  if (!clientWidth) return;
                  docEl.style.fontSize = (clientWidth >= 640 ? 100 : clientWidth / 6.4) + 'px';
              };

          if (!doc.addEventListener) return;
          win.addEventListener(resizeEvt, recalc, false);
          doc.addEventListener('DOMContentLoaded', recalc, false);
      })(document, window);
  </script>
</head>
<body>
<div style="margin-bottom: 1.5rem;">
  <!-- 首页轮播图start -->
    <div class="swiper-container swiper-container-horizontal">
        <div class="swiper-wrapper">
        	<div class="swiper-slide"><img src="${data.indexPic.url}"></div>
        	<#list data.picList as result>
        		<div class="swiper-slide"><img src="${result.url}"></div>
           	</#list>
        </div>
        <!-- Add Pagination -->
      <div class="swiper-pagination swiper-pagination-fraction" style="color:#fff;"><</div>
    </div>
    <!-- 首页轮播图end-->
    <div class="h_box">
        <!-- 项目名称 -->
        <div class="h_box_header">
          <h3>${data.infoAndDetail.name}</h3>
          <p>均价：&nbsp;<span>${data.infoAndDetail.price}</span>&nbsp;<span style="font-size:14px;"></span></p>
          <p style="color:#999">开盘日期：<span></span>${data.infoAndDetail.startSaleDate}</p>
          <p>项目地址：<span></span>${data.infoAndDetail.address}</p>
        </div>
    </div>

            <!-- 项目介绍 -->
        <div class="h_box_main">
          <h3>项目介绍</h3>
          <div>${data.infoAndDetail.showText}</div>
        </div>

        <!-- 基本信息 -->
        <div class="h_box_main">
          <h3>基本信息</h3>
          <ul>
            <li>
              <span>物业类别：</span>
              	<#if data.infoAndDetail.proType == 0> 普通住宅<#else> 其他 </#if>
            </li>
            <#if data.infoAndDetail.buildType >
	            <li class="clearfix">
	              <span class="fl">建筑类别：</span>
	              <p class="fl" style="width:75%;">${data.infoAndDetail.buildType}</p>
	            </li>
            </#if>
            <#if data.infoAndDetail.proYears >
	            <li class="clearfix">
	              <span class="fl">产权年限：</span>
	              <p class="fl" style="width:75%;">${data.infoAndDetail.proYears}</p>
	            </li>
            </#if>
            <#if data.infoAndDetail.feature >
            <li class="clearfix">
              <span class="fl">项目特色：</span>
               <p class="fl" style="width:75%;">${data.infoAndDetail.feature}</p>
            </li>
            </#if>
            <#if data.infoAndDetail.redecorate >
            <li class="clearfix">
              <span class="fl">装修状况：</span>
            	<p class="fl" style="width:75%;">${data.infoAndDetail.redecorate}</p>
            </li>
            </#if>
            <#if data.infoAndDetail.developers >
            <li class="clearfix">
              <span class="fl">开发商：</span>
               <p class="fl" style="width:75%;">${data.infoAndDetail.developers}</p>
            </li>
            </#if>
          </ul>
        </div>
        <!-- 销售信息 -->
        <#if data.existDetailPro == "1">
        <div class="h_box_main">
          <h3>销售信息</h3>
          <ul>
          	<#if data.infoAndDetail.saleState >
           		<li class="clearfix"><span class="fl">销售状态：</span><p class="fl" style="width:75%;">${data.infoAndDetail.saleStateStr}</li>
            </#if>
            <#if data.infoAndDetail.startSaleDate >
            	<li class="clearfix"><span class="fl">开盘时间：</span> <p class="fl" style="width:75%;">${data.infoAndDetail.startSaleDate}</p></li>
            </#if>
            <#if data.infoAndDetail.saleAddress >
            	<li class="clearfix"><span class="fl">售楼地址：</span> <p class="fl" style="width:75%;">${data.infoAndDetail.saleAddress}</p></li>
            </#if>
            <#if data.infoAndDetail.roomType >
            	<li class="clearfix"><span class="fl">主力户型：</span><p class="fl" style="width:75%;">${data.infoAndDetail.roomType}</p></li>
            </#if>
            <#if data.infoAndDetail.prefer >
            	<li><span>楼盘优惠：</span><#if data.infoAndDetail.prefer == 0>暂无<#else>金服增值卡</#if></li>
            </#if>
            <#if data.infoAndDetail.makeHouseDate >
            	<li class="clearfix"><span class="fl">交房时间：</span><p class="fl" style="width:75%;">${data.infoAndDetail.makeHouseDate}</p></li>
            </#if>
            <#if data.infoAndDetail.salTel >
             	<li class="clearfix"><span class="fl">咨询电话：</span><p class="fl" style="width:75%;">${data.infoAndDetail.salTel}</p></li>
            </#if>
             <#if data.permitList>
           		<li class="clearfix"><span class="fl" style="width:30%;">预售许可证：</span> 
           		<p class="fl" style="width:70%;">
	           		<#list data.permitList as result>
	           			<font style="word-break:break-all;">许可证号：${result.name}
	           			<#if result.sendTime>
	           			,发证时间：${result.sendTime}
	           			</#if>
	           			<#if result.floor>
	           			,绑定楼栋：${result.floor}
	           			</#if>
	           			</font>
	           			<br/><br/>
<!-- 	           			<#if result.sendTime> -->
<!-- 	           			<em style="font-style:normal;color:#ed5345;font-size:.24rem;">,发证时间：${result.sendTime},</em> -->
<!-- 	           			</#if> -->
<!-- 	           			<#if result.floor> -->
<!-- 	           			<em style="font-style:normal;color:#ed5345;font-size:.24rem;">绑定楼栋：${result.floor}</em></font><br/> -->
<!-- 	           			</#if> -->
	           		</#list>
	           	</p>
           		</li>
           	</#if>
            
          </ul>
        </div>
        </#if>
        
        <!-- 小区规划 -->
        <#if data.existSalPro == 1>
        <div class="h_box_main">
          <h3>小区规划</h3>
          <ul>
          	<#if data.infoAndDetail.landArea >
            	<li class="clearfix"><span class="fl">占地面积：</span><p class="fl" style="width:75%;">${data.infoAndDetail.landArea}</p></li>
            </#if>
            <#if data.infoAndDetail.capRate >
            	<li class="clearfix"><span class="fl">容积率：</span><p class="fl" style="width:75%;"> ${data.infoAndDetail.capRate}</p></li>
            </#if>
            <#if data.infoAndDetail.parkSpace >
            	<li class="clearfix"><span class="fl">停车位：</span><p class="fl" style="width:75%;">${data.infoAndDetail.parkSpace}</p></li>
            </#if>
            <#if data.infoAndDetail.doorNum >
           		<li class="clearfix"><span class="fl">总户数：</span><p class="fl" style="width:75%;">${data.infoAndDetail.doorNum}</p></li>
            </#if>
            <#if data.infoAndDetail.proFee >
            	<li class="clearfix"><span class="fl">物业费：</span><p class="fl" style="width:75%;">${data.infoAndDetail.proFee}</p></li>
            </#if>
            <#if data.infoAndDetail.buildArea >
            	<li class="clearfix"><span class="fl">建筑面积：</span><p class="fl" style="width:75%;">${data.infoAndDetail.buildArea}</p></li>
            </#if>
            <#if data.infoAndDetail.greenRate >
            	<li class="clearfix"><span class="fl">绿化率：</span><p class="fl" style="width:75%;">${data.infoAndDetail.greenRate}</p></li>
            </#if>
            <#if data.infoAndDetail.floorNum >
            	<li class="clearfix"><span class="fl">楼栋总数：</span><p class="fl" style="width:75%;">${data.infoAndDetail.floorNum}</p></li>
            </#if>
            
            <#if data.infoAndDetail.proCompany >
            <li class="clearfix"><span class="fl">物业公司：</span><p class="fl" style="width:75%;">${data.infoAndDetail.proCompany}</p></li>
          	</#if>
          </ul>
        </div>
        </#if>
        <!-- 户型图 -->
        <#if data.homeProPics>
        <div class="h_box_main">
          <h3>户型图</h3>
          <#list data.homeProPics as result>
          	<img src="${result.url}" alt="户型图" style="margin-bottom: 5px">
          </#list>
        </div>
        </#if>
</div>
<div class="h_btn">
    <#if data.isLogin == 1>
        <#if data.isReservation == 1>
        <a href="#" class="notV_h_btn">已预约</a>
        <#elseif data.isReservation == 2>
        <a href="https://a.app.qq.com/o/simple.jsp?pkgname=com.yrtz.qiankundai">预约看房</a>
        <#else>
        <a href="${request.contextPath}/houseController/toReservation.do?infoId=${infoId}&source=${source}&sessionId=${sessionId}&access_token=${access_token}">预约看房</a>
        </#if>
    <#else >
        <a href="https://a.app.qq.com/o/simple.jsp?pkgname=com.yrtz.qiankundai">去下载APP</a>
    </#if>

</div>
</div>
</body>
<script>
//var proC = ${data.info.floorNum};
var swiper = new Swiper('.swiper-container', { // 轮播图js
    pagination: '.swiper-pagination',
    paginationType: 'fraction',
    nextButton: '.swiper-button-next',
    prevButton: '.swiper-button-prev',
    paginationClickable: true,
    spaceBetween: 30,
    centeredSlides: true,
    autoplay: 2500,
    autoplayDisableOnInteraction: false
});
</script>
</html>