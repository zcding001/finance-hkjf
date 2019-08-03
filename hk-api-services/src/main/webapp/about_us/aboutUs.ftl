<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
  <title>关于我们</title>
  <link rel="stylesheet" href="../csslib/reset.css">
  <script type="text/javascript" src="../jslib/jquery-3.2.1.min.js"></script>
  <style>
          body{background: #FAFAFA}
         img{display: block;}
        /*公司简介*/
        .wrapper{width:100%;margin: 0 auto;}
        .tab{justify-content: space-between;display: flex;display: -webkit-flex;width: 90%;margin:0 auto;}
        .tab li{position: relative;float: left;height: .52rem;line-height: .52rem;text-align: center;cursor: pointer;font-size: .30rem;margin: .3rem 0 .2rem 0;color:#61676E;}
        .border-em{height: .04rem;background: #f39200;display: block;margin: 0 .4rem;}
        .products{ width: 100%;font-size: .15rem;}
        .products .main{float: left;display: none;width:100%;}
        .products .main.selected{display: block;}
        .tab li.active{color: #f39200;}
        .main-ul{margin:0 auto;}
        .main-ul li{text-align: center;float: left;width:32%;}
        .main-ul img{width:.64rem;margin: .66rem  auto .40rem auto; }
        .main-ul li  p{font-size: .30rem;color:#20262D;height:.36rem;line-height: .36rem;}
        .main-ul li p:last-child{font-size: .24rem;color:#91979E;height:.30rem;line-height: .30rem;margin-top:.16rem;}
        .main-cont{margin: .64rem .32rem 0.32rem;background: #fff;border-radius: .1rem;padding:.32rem .45rem;box-shadow: 0px 8px 10px 7px #f5f5f5;}
        .main-cont p{font-size: .28rem;color:#20262D;line-height: .5rem;text-align:  justify;}
        /*股东背景*/
        .second-file {width: 90%;margin:0 auto;background: #fff;border:1px solid #e5e5e5;border-bottom:2px solid #ddd;}
        .file-top {border-bottom: 1px solid #eee;text-align: center;}
        .file-top img{width: 50%;vertical-align: middle;padding:15px 0;display:inline-block;}
        .file-bot p{padding:10px;line-height: 28px;}
        .second-file1{margin-top:15px;margin-bottom:20px;}
        .div-tab1{position: fixed;top:0;left:0;width: 100%;/* margin-bottom: 1rem; */}
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
 <div class="wrapper">
      <div style="background: #fff;" class="div-tab">
                <ul class="tab">
                  <li class="tab-item active"><font>公司简介</font><em class="border-em"></em></li>
                  <li class="tab-item"><font>股东背景</font><em></em></li>
                  <li class="tab-item"><font>合作伙伴</font><em></em> </li>
              </ul>
      </div>
        <div class="products">
            <div class="main selected">
              <!-- 公司简介 -->
                <main>
                    <img src="../imageLib/aboutsImg/img_jinfu.png" alt="公司简介图片" style="width: 100%;"/>
                    <ul class="main-ul clearfix">
                      <li>
                        <img src="../imageLib/aboutsImg/ic_shijian.png" alt="成立时间">
                        <p>2015年</p>
                        <p>成立时间</p>
                      </li>
                      <li>
                        <img src="../imageLib/aboutsImg/ic_dizhi.png" alt="总部地址">
                        <p>北京&nbsp;·&nbsp;大兴</p>
                        <p>总部地址</p>
                      </li>
                      <li style="width:36%;">
                        <img src="../imageLib/aboutsImg/ic_dianhua.png" alt="客服电话">
                        <p>400-900-9630</p>
                        <p>客服电话</p>
                      </li>
                    </ul>
                      <div class="main-cont">
                        <p>鸿坤金服定位于社区“金融超市”，为特定社区及特定人群提供金融服务。鸿坤金服平台上出售的产品均为持牌金融机构产品，可以满足特定人群固定收益投资、股权投资、海外投资等多种需求。另外，投资所得的积分可用于合作社区日常缴费、周边商业与生活消费。</p>
                      </div>
                </main>
            </div>
            <div class="main">
               <!-- 股东背景 -->
               <main>
                    <img src="../imageLib/aboutsImg/img_hkyrtz.png" alt="鸿坤亿润投资" style="width:100%;">
                    <img src="../imageLib/aboutsImg/ic_xiajiantou.png" alt="下箭头" style="width:.44rem;margin:.48rem auto .40rem auto;">
                    <div class="main-cont" style="margin:0 .48rem 1.3rem .48rem; ">
                        <img src="../imageLib/aboutsImg/IMG_7438882.png" alt="亿润投资集团" style="width:100%;">
                        <p style="margin:.48rem 0 .6rem 0;">鸿坤亿润投资成立于2007年，定位于中国新一代产融结合的创新型资产管理公司，致力于以金融投资推动城市经济发展。2007年成功发起北方地区第一支有限合伙基金。截至目前已成功投资50余家企业，10余家企业已经成功上市，资产管理规模已超100亿，为基金投资者获取了丰厚的投资回报。 </p>
                        <p>亿润结合鸿坤集团产融结合的优势资源，以“地产+”和泛社区服务为突围策略，布局经济新常态下的新消费、新服务、新技术、新金融领域，抓住消费升级浪潮契机，继建立“地产+”PE投资生态圈后深度结合鸿坤产业和鸿坤文旅的优势资源，在2017年又迭代升级布局了“特色产业+”PE投资生态。2018年，鸿坤亿润投资荣膺“投中2017年度中国最佳私募股权投资机构TOP100”。</p>
                    </div>
               </main>
               <main>
                    <img src="../imageLib/aboutsImg/img_hkjt.png" alt="鸿坤" style="width:100%;">
                    <img src="../imageLib/aboutsImg/ic_xiajiantou.png" alt="下箭头" style="width:.44rem;margin:.48rem auto .40rem auto;">
                    <div class="main-cont" style="margin:0 .48rem 1.3rem .48rem;">
                        <img src="../imageLib/aboutsImg/IMG_7438881.png" alt="鸿坤广场" style="width:100%;">
                        <p style="margin-top:.48rem;">2002年成立至今，鸿坤已经发展成为一家拥有鸿坤资本、鸿坤产业、鸿坤文旅和鸿坤地产四大业务板块的复合型控股集团。鸿坤地产已经连续多年位列中国地产百强、房地产开发企业创新能力10强和区域运营10强，业务覆盖住宅地产、商业地产、物业服务3大板块，截止目前，鸿坤地产累计总开发面积超1000万平方米，土地储备量超500万平方米，并获得AAA级公司债券信用评级，资产管理规模超过500亿。  </p>
                    </div>
               </main>
            </div>
            <div class="main">
              <!-- 合作伙伴 -->
              <main>
                      <div class="main-cont">
                           <img src="../imageLib/aboutsImg/img_ancun.png" alt="安存" style="width:100%;">
                            <p>杭州安存网络科技有限公司是一家专业致力于提供证据留存、证据获取、证据管理等法律服务产品的互联网运营商。安存公司打造了一种全新的司法信息化模式，拥有专业的安存电子保全管理系统，既符合现行的法律法规，又连接国家权威机构用户身份认证系统，并且获得多项国家专利。</p>
                      </div>
                      <div class="main-cont">
                           <img src="../imageLib/aboutsImg/img_lvmeng.png" alt="绿盟科技" style="width:100%;margin-bottom: .24rem;">
                            <p>绿盟科技成立于2000年4月，总部位于北京。在国内外设有40多个分支机构，为政府、运营商、金融、能源、互联网以及教育、医疗等行业用户，提供具有核心竞争力的安全产品及解决方案，帮助客户实现业务的安全顺畅运行。基于多年的安全攻防研究，绿盟科技在检测防御类、安全评估类、安全平台类、远程安全运维服务、安全SaaS服务等领域，为客户提供入侵检测/防护、抗拒绝服务攻击、远程安全评估以及Web安全防护等产品以及安全运营等专业安全服务。</p>
                      </div>
                      <div class="main-cont">
                           <img src="../imageLib/aboutsImg/img_bjdadi.png" alt="北京大地律师事务所" style="width:100%;">
                            <p>北京市大地律师事务所成立于1989年，是中国成立时间最长的合作制律师事务所之一。大地律师事务所十余年来创业和展业的历程不仅为本所积累了丰富的实务经验和社会资源，也造就了大地所对客户尽职尽责、对业务兢兢业业的专业素质以及精诚团结的团队精神。目前，大地律师事务所已发展成为以提供诉讼仲裁、保险、房地产、知识产权、金融、外商投资、公司业务、海商海事及劳动雇佣等多种法律服务见长的律师事务所。</p>
                      </div>
                      <div class="main-cont">
                           <img src="../imageLib/aboutsImg/img_baofu.png" alt="宝付" style="width:100%;">
                            <p>宝付网络科技（上海）有限公司是漫道金服旗下的一家第三方支付公司 ，2011年12月22日宝付荣获了由中国人民银行颁发的《支付业务许可证》 ，获得互联网支付业务许可。宝付凭借其强大的多重安全保障系统，对账户信息、交易信息、签名、信息传输等皆进行128位SSL加密，并具有VeriSign签发的全球安全证书，在支付系统的各个环节层层把控，通过领先技术、严密流程构建安全放心的支付平台。</p>
                      </div>
                      <div class="main-cont">
                           <img src="../imageLib/aboutsImg/img_zhaijisong.png" alt="宅急送" style="width:100%;">
                            <p>宅急送是一家物流公司，创建于1994年1月18日，宅急送经过二十几年的快速发展，宅急送已有员工逾2万人，车辆2000余台。全国共有30个分公司，7个航空基地，247个独立城市营业所，40个市内营业所，179个营业厅，1220个操作点，705个外网，共计网络机构2440个，网络已覆盖全国地级以上城市。</p>
                      </div>
                      <div class="main-cont">
                           <img src="../imageLib/aboutsImg/img_benlaishenghuo.png" alt="本来生活" style="width:100%; margin-bottom: .1rem;">
                            <p>本来生活网精选来自全世界的优质、健康、绿色、有机、品牌食材食品，基地直供、冷链配送、安全检测，服务到家。开展了“717生鲜购物节”和“回家吃饭”等食品打折、优惠、网购活动。</p>
                      </div>
              </main>
            </div>
        </div>
    </div>
</body>
  <script type="text/javascript">
        $(function(){
          //导航栏切换菜单
            $(".tab li").click(function(){
                var $this = $(this);
                    index = $this.index();
                $this.addClass("active").siblings("li").removeClass("active");
                $this.children("em").addClass("border-em").parent().siblings("li").children("em").removeClass("border-em");
                $(".products .main").eq(index).addClass("selected").siblings("div").removeClass("selected");
            });
            //屏幕滚动固定导航栏
          var heightTop =  $(".div-tab").height();
          $(window).scroll(function(){
              var scrollTop = $(document).scrollTop();
             if(scrollTop>heightTop){
              $(".div-tab").addClass("div-tab1");
              $("products").css("margin-top",heightTop);
            }else{
              $(".div-tab").removeClass("div-tab1");
              $("products").css("margin-top",0);
            }
          })
        });

   </script>
</html>