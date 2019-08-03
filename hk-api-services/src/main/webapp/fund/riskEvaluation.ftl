<!DOCTYPE html>
<html>
<head>
    <title>投资者风险承受能力问卷</title>
    <meta name="keywords" content="互联网金融，鸿坤金服，投资，理财，个人理财，理财收益，鸿坤，鸿坤财富，鸿坤地产，银行理财">
    <meta name="description" content="鸿坤金服为社区金融综合服务平台，服务于特定社区及特定人群，被评定为国家AAA级信用等级平台、国家高新技术企业、诚信兴商示范单位、中国经济创新示范企业。">
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no">
    <!-- 默认依webkit内核渲染页面 -->
    <meta name="renderer" content="webkit">
    <!--ios禁止识别电话号-->
    <meta name="format-detection" content="telephone = no"/>
    <!--禁止识别邮箱-->
    <meta name="applicable-device" content="mobile">
    <!-- 禁止缓存-->
    <meta http-equiv="Cache-Control" content="no-store"/>
    <meta http-equiv="Cache-Control" content="no-transform" />
    <!-- 指定标题 -->
    <meta name="apple-mobile-web-app-title" content="鸿坤金服">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black-translucent">
    <meta content="email=no" name="format-detection"/>
    <link rel="stylesheet" href="../csslib/fund/reset.css">
    <script src="../jslib/jquery-3.2.1.min.js"></script>
    <script src="../jslib/fund/font-size.js"></script>
   <script src="../jslib/fund/win_alert.js" type="text/javascript"></script>
    <style>
        body{background-color:#f5f5f5;width: 7.5rem;margin: 0 auto;padding-top: 0.16rem;padding-bottom: 0.4rem;}
        .xieyinr{padding: 0.36rem 0.32rem 0;background-color: white;}
        .shenluoh{overflow: hidden; white-space: nowrap; text-overflow: ellipsis;}
        .oh1{font-size: 0.3rem;color: #333333;text-align: center;font-weight: bold;}
        .toubuname{overflow: hidden;font-size: 0.28rem;color: #333333;margin-top: 0.28rem;}
        .nameli{width: 50%;float: left;margin-top: 0.06rem;}
        .nameli h3,.nameli h4{display: inline-block;}
        .pjihe{margin-top: 0.44rem;}
        .pjihe p{line-height: 0.46rem;margin-top: 0.18rem;text-indent: 2em;font-size: 0.28rem;color: #333333;}
        .pjiheh2{font-size: 0.3rem;color: #C08B33;font-weight: bold;margin-top: 0.52rem;}
        .oname{font-size: 0.3rem;color: #333333;text-align: center;margin-top: 0.26rem;margin-bottom: 0.24rem;}
        .tiaojain{padding: 0.1rem 0;border-top: 1px #ffdba4 dashed;border-bottom: 1px #ffdba4 dashed}
        .tiaojain p:first-child{font-weight: bold;text-indent: inherit;}
        /*选择提*/
        .danxuanbut{margin-top: 0.2rem;background-color: white;padding:0.3rem 0.32rem 0.2rem;overflow: hidden;}
        .danxuanbut h2{font-size: 0.28rem;font-weight: bold;color: #333333}
        .oul-li{margin-top: 0.38rem;}
        .oul-li p{font-size: 0.28rem;color: #333333;font-weight: bold;}
        .feiji-li{margin-top: 0.22rem;}
        .feiji-li em{font-size: 0.28rem;color: #333333;float: right;vertical-align: 0.02rem;;width: 88%;}
        .danxuankuang{width: 0.32rem;height: 0.32rem;border: 1px solid #e0e0e0;border-radius: 50%;float: left;margin-top: 0.04rem;position: relative;}
        .feiji-li:after{content: '';height: 0;width: 0;display: block;clear: both;}
        .dxxzl{border-color: #C08B33;}
        .dxxzl strong{background-color: #C08B33;width: 0.16rem;height:  0.16rem;;display: block;border-radius: 50%;position: absolute;top: 0;bottom: 0;right: 0;left: 0;margin: auto}
        /*底部*/
        .chengnuop{font-size: 0.26rem;color: #666666;width: 6.7rem;margin: 0 auto;margin-top: 0.36rem;margin-bottom: 0.28rem;line-height: 0.48rem;}
        .obut{width: 6.7rem;margin: 0.22rem auto 0;height: 1rem;line-height: 1rem;text-align: center;background-color: #C08B33;color: white;font-size: 0.34rem;border-radius: 4px;display: block;}
        .obutxz{background-color: #cccccc;}

        /*承诺书*/
        .xuanzbu{width: 0.3rem;height: 0.3rem;line-height: 0.3rem;text-align: center;border-radius: 2px;border: 1px solid #00a2e6;position: relative;display: inline-block;}
        .cluoshu{width: 6.7rem;margin: 0 auto;padding: 0 0.2rem;}
        .xzlah{background-color: #00a2e6;}
        .xzlah:after{display: block;width: 0.2rem;height: 0.1rem;border-bottom: 1px solid white;border-left: 1px solid white;content: ''; transform:rotate(-45deg);-webkit-transform:rotate(-45deg);-o-transform:rotate(-45deg);-moz-transform:rotate(-45deg);-ms-transform:rotate(-45deg);position: absolute;top: 0.02rem;;bottom: 0;left: 0;right: 0;margin: 0 auto;}
        .cluoshu em{font-size: 0.26rem;color: #00a2e6;vertical-align: 0.06rem;;margin-left: 0.06rem;}
        .closegx{width: 0.32rem;position: absolute;right: 0.5rem;;top: 0.24rem;}
        .cengnuoshu{font-size: 0.32rem;color: #333333;font-weight: bold;text-align: center;margin-bottom: 0.22rem;}
        .qiandgc p{font-size: 0.28rem;color: #333333;line-height: 0.44rem;}
        .xmoding{width: 55%;float: right;margin-top: 0.42rem;}
        .xmoding h3,.xmoding h4{font-size: 0.28rem;color: #333333;}

        /*弹窗*/
        .tanchuang{width: 100%;height: 100%;position: fixed;top: 0;left: 0;background-color: rgba(0,0,0,0.5);z-index: 99;display: none;}
        .qiandgc{width: 6.87rem;height:8rem;border-radius: 6px;z-index: 100;background-color: white;padding: 0.42rem 0.38rem 0.46rem;position: fixed;top: 0;bottom: 0;right: 0;left: 0;margin:auto;display: none;}
        /*动画*/
        .animated {animation-duration: 1s;animation-fill-mode: both;}
        .animated.infinite {animation-iteration-count: infinite;}
        .animated.hinge {animation-duration: 2s;}
        .animated.flipOutX, .animated.flipOutY, .animated.bounceIn, .animated.bounceOut {animation-duration: .75s;}
        @keyframes bounceInDown {
            from, 60%, 75%, 90%, to {animation-timing-function: cubic-bezier(0.215, 0.610, 0.355, 1.000);}
            0% {opacity: 0;transform: translate3d(0, -3000px, 0);}
            60% {opacity: 1;transform: translate3d(0, 25px, 0);}
            75% {transform: translate3d(0, -10px, 0);}
            90% {transform: translate3d(0, 5px, 0);}
            to {transform: none;}
        }
        .bounceInDown {animation-name: bounceInDown;}
    </style>
</head>
<body>
<div class="xieyinr">
    <h1 class="oh1"></h1>
    <ul class="toubuname">
       <li class="nameli">
           <h3>投资者姓名：</h3><h4>${loginName}</h4>
       </li>
        <li class="nameli shenluoh">
            <h3>填写日期：</h3><h4>${currentTime}</h4>
        </li>
        <li class="nameli shenluoh">
            <h3>证件类型：</h3><h4>身份证</h4>
        </li>
        <li class="nameli shenluoh">
            <h3>联系方式：</h3><h4>${tel}</h4>
        </li>
        <li class="nameli shenluoh" style="width: 100%;">
            <h3>证件号码：</h3><h4>${idCard}</h4>
        </li>
    </ul>
    <div class="pjihe">
        <p>本问卷旨在了解您可承受的风险程度等情况，借此协助您选择合适的金融产品或金融服务类别，以符合您的风险承受能力。</p>
        <p>风险承受能力评估是本公司向客户履行适当性职责的一个环节，其目的是使本公司所提供的金融产品或金融服务与您的风险承受能力等级相匹配。</p>
        <p>本公司特别提醒您：本公司向客户履行风险承受能力评估等适当性职责，并不能取代您自己的投资判断，也不会降低金融产品或金融服务的固有风险。同时，与金融产品或金融服务相关的投资风险、履约责任以及费用等将由您自行承担。</p>
        <p>本公司提示您：本公司根据您提供的信息对贵单位进行风险承受能力评估，开展适当性工作。您应当如实提供相关信息及证明材料，并对所提供的信息和证明材料的真实性、准确性、完整性负责。</p>
        <p>本公司建议：当您的各项状况发生重大变化时，需对您所投资的金融产品及时进行重新审视，以确保您的投资决定与您可承受的投资风险程度等实际情况一致。</p>
        <h2 class="pjiheh2">风险提示：</h2>
        <p style="margin-top: 0.02rem;color: #666666;">基金投资需承担各类风险，本金可能遭受损失。同时，还要考虑市场风险、信用风险、流动风险、操作风险等各类投资风险。您在基金认购过程中应当注意核对自己的风险识别和风险承受能力，选择与自己风险识别能力和风险承受能力相匹配的私募基金。</p>
        <p>以下问题可在您选择合适的私募基金前，协助评估您的风险承受能力、理财方式及投资目标。</p>
        <p style="text-indent: inherit;font-weight: bold;margin-top: 0.02rem;">请签字承诺您是为自己购买私募基金产品：</p>
        <div class="oname" >${loginName}</div>
        <div class="tiaojain">
            <p>请签字确认您符合以下何种合格投资者财务条件：</p>
            <p style="margin-top: 0.02rem;">符合家庭金融净资产不低于300万元，家庭金融资产不低于500万元（金融资产包括银行存款、股票、债券、基金份额、资产管理计划、银行理财产品、信托计划、保险产品、期货权益等）或符合最近3年个人年均收入不低于40万元</p>
            <div class="oname" >${loginName}</div>
        </div>
    </div>
</div>
<div class="danxuanbut">
    <ul class="timuoul">
        <li id="answer1" class="oul-li" style="margin-top: 0.18rem;">
            <p>1．您的年龄介于以下哪个范围？</p>
            <ul class="feijia">
                <li class="feiji-li">
                    <span id="r1" name='A:1' class="danxuankuang"><strong></strong></span> <em>A.18-30岁</em>
                </li>
                <li class="feiji-li">
                    <span  id="r2" name='B:3' class="danxuankuang"><strong></strong></span> <em>B.31-50岁</em>
                </li>
                <li class="feiji-li">
                    <span  id="r3" name='C:5' class="danxuankuang"><strong></strong></span> <em>C.高于51岁</em>
                </li>
            </ul>
        </li>
        <li id="answer2" class="oul-li">
            <p>2．您的学历是？</p>
            <ul class="feijia">
                <li class="feiji-li">
                    <span  id="r1" name='A:1' class="danxuankuang"><strong></strong></span> <em>A.高中及以下</em>
                </li>
                <li class="feiji-li" >
                    <span id="r2" name='B:3' class="danxuankuang"><strong></strong></span> <em>B.中专或大专</em>
                </li>
                <li class="feiji-li" >
                    <span id="r3" name='C:5' class="danxuankuang"><strong></strong></span> <em>C.本科</em>
                </li>
                <li class="feiji-li" >
                    <span id="r4" name='D:7' class="danxuankuang"><strong></strong></span> <em>D.硕士及以上</em>
                </li>
            </ul>
        </li>
        <li id="answer3" class="oul-li">
            <p>3．您的职业为？</p>
            <ul class="feijia">
                <li class="feiji-li">
                    <span id="r1" name='A:1' class="danxuankuang"><strong></strong></span> <em>A.无固定职业者</em>
                </li>
                <li class="feiji-li">
                    <span id="r2" name='B:3' class="danxuankuang"><strong></strong></span> <em>B.专业技术人员</em>
                </li>
                <li  class="feiji-li">
                    <span id="r3" name='C:5' class="danxuankuang"><strong></strong></span> <em>C.一般企事业单位员工</em>
                </li>
                <li class="feiji-li">
                    <span id="r4" name='D:7' class="danxuankuang"><strong></strong></span> <em>D.金融行业一般从业人员</em>
                </li>
            </ul>
        </li>
        <li id="answer4" class="oul-li">
            <p>4．您的主要收入来源是？</p>
            <ul class="feijia">
                <li class="feiji-li">
                    <span id="r1" name='A:1' class="danxuankuang"><strong></strong></span> <em>A.工资、劳务报酬</em>
                </li>
                <li class="feiji-li">
                    <span id="r2" name='B:3' class="danxuankuang"><strong></strong></span> <em>B.生产经营所得</em>
                </li>
                <li class="feiji-li">
                    <span id="r3" name='C:5' class="danxuankuang"><strong></strong></span> <em>C.利息、股息、转让等金融性资产收入</em>
                </li>
                <li class="feiji-li">
                    <span id="r4" name='D:7' class="danxuankuang"><strong></strong></span> <em>D.出租、出售房地产等非金融性资产收入</em>
                </li>
                <li class="feiji-li">
                    <span id="r5" name='E:0' class="danxuankuang"><strong></strong></span> <em>E.无固定收入</em>
                </li>
            </ul>
        </li>
        <li id="answer5" class="oul-li">
            <p>5．您的家庭可支配年收入为（折合人民币）？</p>
            <ul class="feijia">
                <li class="feiji-li">
                    <span id="r1" name='A:1' class="danxuankuang"><strong></strong></span> <em>A.50万元以下</em>
                </li>
                <li class="feiji-li">
                    <span id="r2" name='B:3' class="danxuankuang"><strong></strong></span> <em>B.50—300万元</em>
                </li>
                <li class="feiji-li">
                    <span id="r3" name='C:5'class="danxuankuang"><strong></strong></span> <em>C.300万元以上</em>
                </li>
            </ul>
        </li>
        <li id="answer6" class="oul-li">
            <p>6．在您每年的家庭可支配收入中，可用于金融投资（储蓄存款除外）的比例为？</p>
            <ul class="feijia">
                <li class="feiji-li">
                    <span id="r1" name='A:1' class="danxuankuang"><strong></strong></span> <em>A.小于10%</em>
                </li>
                <li class="feiji-li">
                    <span id="r2" name='B:3' class="danxuankuang"><strong></strong></span> <em>B.10%至25%</em>
                </li>
                <li class="feiji-li">
                    <span id="r3" name='C:5' class="danxuankuang"><strong></strong></span> <em>C.25%至50%</em>
                </li>
                <li class="feiji-li">
                    <span id="r4" name='D:7' class="danxuankuang"><strong></strong></span> <em>D.大于50%</em>
                </li>
            </ul>
        </li>
        <li id="answer7" class="oul-li">
            <p>7．您是否有尚未清偿的数额较大的债务，如有，其性质是？</p>
            <ul class="feijia">
                <li class="feiji-li">
                    <span id="r1" name='A:1' class="danxuankuang"><strong></strong></span> <em>A.有，亲戚朋友借款</em>
                </li>
                <li class="feiji-li">
                    <span id="r2" name='B:3' class="danxuankuang"><strong></strong></span> <em>B.有，信用卡欠款、消费信贷等短期信用债务</em>
                </li>
                <li class="feiji-li">
                    <span id="r3" name='C:5' class="danxuankuang"><strong></strong></span> <em>C.有，住房抵押贷款等长期定额债务</em>
                </li>
                <li class="feiji-li">
                    <span id="r4" name='D:7' class="danxuankuang"><strong></strong></span> <em>D.没有</em>
                </li>
            </ul>
        </li>
        <li id="answer8" class="oul-li">
            <p>8．您的投资知识可描述为？</p>
            <ul class="feijia">
                <li class="feiji-li">
                    <span id="r1" name='A:1' class="danxuankuang"><strong></strong></span> <em>A.有限：基本没有金融产品方面的知识</em>
                </li>
                <li class="feiji-li">
                    <span id="r2" name='B:3' class="danxuankuang"><strong></strong></span> <em>B.一般：对金融产品及其相关风险具有基本的知识和理解</em>
                </li>
                <li class="feiji-li">
                    <span id="r3" name='C:5' class="danxuankuang"><strong></strong></span> <em>C.丰富：对金融产品及其相关风险具有丰富的知识和理解</em>
                </li>
            </ul>
        </li>
        <li id="answer9" class="oul-li">
            <p>9．您的投资经验可描述为？</p>
            <ul class="feijia">
                <li class="feiji-li">
                    <span id="r1" name='A:1' class="danxuankuang"><strong></strong></span> <em>A.除银行储蓄外，基本没有其他投资经验</em>
                </li>
                <li class="feiji-li">
                    <span id="r2" name='B:3' class="danxuankuang"><strong></strong></span> <em>B.购买过债券、保险等理财产品</em>
                </li>
                <li class="feiji-li">
                    <span id="r3" name='C:5' class="danxuankuang"><strong></strong></span> <em>C.参与过股票、基金等产品的交易</em>
                </li>
                <li class="feiji-li">
                    <span id="r4" name='D:7' class="danxuankuang"><strong></strong></span> <em>D.参与过权证、期货、期权等产品的交易</em>
                </li>
            </ul>
        </li>
        <li id="answer10" class="oul-li">
            <p>10．您有多少年投资基金、股票、信托、私募证券或金融衍生产品等风险投资品的经验？</p>
            <ul class="feijia">
                <li class="feiji-li">
                    <span id="r1" name='A:1' class="danxuankuang"><strong></strong></span> <em>A.少于2年</em>
                </li>
                <li class="feiji-li">
                    <span id="r2" name='B:3' class="danxuankuang"><strong></strong></span> <em>B.2至5年</em>
                </li>
                <li class="feiji-li">
                    <span id="r3" name='C:5' class="danxuankuang"><strong></strong></span> <em>C.5至10年</em>
                </li>
                <li class="feiji-li">
                    <span id="r4" name='D:7' class="danxuankuang"><strong></strong></span> <em>D.10年以上</em>
                </li>
                <li class="feiji-li">
                    <span id="r5" name='E:0' class="danxuankuang"><strong></strong></span> <em>E.没有经验</em>
                </li>
            </ul>
        </li>
        <li id="answer11" class="oul-li">
            <p>11．您计划的投资期限是多久？</p>
            <ul class="feijia">
                <li class="feiji-li">
                    <span id="r1" name='A:1' class="danxuankuang"><strong></strong></span> <em>A.1年以下</em>
                </li>
                <li class="feiji-li">
                    <span id="r2" name='B:3' class="danxuankuang"><strong></strong></span> <em>B.1至3年</em>
                </li>
                <li class="feiji-li">
                    <span id="r3" name='C:5' class="danxuankuang"><strong></strong></span> <em>C.3年以上</em>
                </li>
            </ul>
        </li>
        <li id="answer12" class="oul-li">
            <p>12．您的投资目的是？</p>
            <ul class="feijia">
                <li class="feiji-li">
                    <span id="r1" name='A:1' class="danxuankuang"><strong></strong></span> <em>A.资产保值</em>
                </li>
                <li class="feiji-li">
                    <span id="r2" name='B:3' class="danxuankuang"><strong></strong></span> <em>B.资产稳健增长</em>
                </li>
                <li class="feiji-li">
                    <span id="r3" name='C:5' class="danxuankuang"><strong></strong></span> <em>C.资产迅速增长</em>
                </li>
            </ul>
        </li>
        <li id="answer13" class="oul-li">
            <p>13．您打算重点投资于哪些种类的投资品种？</p>
            <ul class="feijia">
                <li class="feiji-li">
                    <span id="r1" name='A:1' class="danxuankuang"><strong></strong></span> <em>A.债券、货币市场基金、债券基金等固定收益类投资品种</em>
                </li>
                <li class="feiji-li">
                    <span id="r2" name='B:3' class="danxuankuang"><strong></strong></span> <em>B.股票、混合型基金、股票型基金等权益类投资品种</em>
                </li>
                <li class="feiji-li">
                    <span id="r3" name='C:5' class="danxuankuang"><strong></strong></span> <em>C.期货、期权等金融衍生品或其他产品和服务</em>
                </li>
            </ul>
        </li>
        <li id="answer14" class="oul-li">
            <p>14．以下哪项描述最符合您的投资态度？</p>
            <ul class="feijia">
                <li class="feiji-li">
                    <span id="r1" name='A:1' class="danxuankuang"><strong></strong></span> <em>A.厌恶风险，不希望本金损失，希望获得稳定回报</em>
                </li>
                <li class="feiji-li">
                    <span id="r2" name='B:3' class="danxuankuang"><strong></strong></span> <em>B.保守投资，不希望本金损失，愿意承担一定幅度的收益波动</em>
                </li>
                <li class="feiji-li">
                    <span id="r3" name='C:5' class="danxuankuang"><strong></strong></span> <em>C.寻求资金的较高收益和成长性，愿意为此承担有限本金损失</em>
                </li>
                <li class="feiji-li">
                    <span id="r4" name='D:7' class="danxuankuang"><strong></strong></span> <em>D.希望赚取高回报，愿意为此承担较大本金损失</em>
                </li>
            </ul>
        </li>
        <li id="answer15" class="oul-li">
            <p>15．假设有两种投资：投资A预期获得10%的收益，可能承担的损失非常小；投资B预期获得30%的收益，但可能承担较大亏损。您会怎么支配您的投资？</p>
            <ul class="feijia">
                <li class="feiji-li">
                    <span id="r1" name='A:1' class="danxuankuang"><strong></strong></span> <em>A.全部投资于收益较小且风险较小的A</em>
                </li>
                <li class="feiji-li">
                    <span id="r2" name='B:3' class="danxuankuang"><strong></strong></span> <em>B.同时投资于A和B，但大部分资金投资于收益较小且风险较小的A</em>
                </li>
                <li class="feiji-li">
                    <span id="r3" name='C:5' class="danxuankuang"><strong></strong></span> <em>C.同时投资于A和B，但大部分资金投资于收益较大且风险较大的B</em>
                </li>
                <li class="feiji-li">
                    <span id="r4" name='D:7' class="danxuankuang"><strong></strong></span> <em>D.全部投资于收益较大且风险较大的B</em>
                </li>
            </ul>
        </li>
        <li id="answer16" class="oul-li">
            <p>16．您认为自己能承受的最大投资损失是多少？</p>
            <ul class="feijia">
                <li class="feiji-li">
                    <span id="r1" name='A:1' class="danxuankuang"><strong></strong></span> <em>A.10%以内</em>
                </li>
                <li class="feiji-li">
                    <span id="r2" name='B:3' class="danxuankuang"><strong></strong></span> <em>B.10%-30%</em>
                </li>
                <li class="feiji-li">
                    <span id="r3" name='C:5' class="danxuankuang"><strong></strong></span> <em>C.30%-50%</em>
                </li>
                <li class="feiji-li">
                    <span id="r4" name='D:7' class="danxuankuang"><strong></strong></span> <em>D.超过50%</em>
                </li>
            </ul>
        </li>
        <li id="answer17" class="oul-li">
            <p>17．您是否为鸿坤业主？</p>
            <ul class="feijia">
                <li class="feiji-li">
                    <span id="r1" name='A:0' class="danxuankuang"><strong></strong></span> <em>A.是</em>
                </li>
                <li class="feiji-li">
                    <span id="r2" name='B:-100' class="danxuankuang"><strong></strong></span> <em>B.否</em>
                </li>
            </ul>
        </li>
    </ul>
</div>

<p class="chengnuop">本人已如实填写，并了解了自己的风险承受类型和适合购买的产品类型。</p>
<div class="cluoshu">
    <span class="xuanzbu"></span><em class="chennshu">《合格投资承诺书》</em>
</div>

<button class="obut obutxz" onclick="submitA()" >提交</button>  <!--obutxz是灰色按钮样式-->
<!--遮罩-->
<div class="tanchuang"></div>
<!--签到成功-->
<div class="qiandgc animated bounceInDown">
    <img src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgBAMAAACBVGfHAAAAFVBMVEUAAACZmZmZmZmZmZmZmZmZmZmZmZnaZv17AAAABnRSTlMAc3Vu29FdZNWcAAAAiUlEQVQoz2XSsQ2AIBCFYdQFqKitrGlkBBdwAsTc/iNISP48zJmoEb87fUA4c5iO7Q5XnQfSG4pFPS/W+vlMwPZxEej1M0mjXGS1KigA0Qu6ACAAkWMAEQCkFj4OARDbLPuBfwmJSNFoCogLhB8jEYBEyq9wpIAACM0dwkyK0AsCEHFL6RbbbYcPOLEpohgJ5M4AAAAASUVORK5CYII=" alt="" class="closegx">
    <h5 class="cengnuoshu">合格投资承诺书</h5>
    <p style="text-indent: 1em;">本人/本单位作为符合中国证券监督管理委员会规定的私募投资基金的合格投资者（即个人投资者的金融资产不低于300万元或者最近三年个人年均收入不低于50 万元，机构投资者的净资产不低于1000万元，或为监管机构认可的其他合格投资者），具有相应的风险识别能力和风险承受能力，投资资金来源合法，没有非法汇集他人资金投资私募基金。本人/本单位在参与贵公司发起设立的私募基金的投资过程中，如果因存在欺诈、隐瞒或其他不符合实际情况的陈述所产生的一切责任，由本人/本单位自行承担，与贵公司无关。</p>
    <p>特此承诺。</p>
    <div class="nameli xmoding">
        <h3>投资者姓名：</h3><h4>${loginName}</h4>
    </div>
    <div class="nameli xmoding" style="margin-top: 0.04rem">
        <h3>签署日期：</h3><h4>${currentTime}</h4>
    </div>
</div>

<script src="../jslib/fund/zepto.min.js"></script>
<script>
    // 选中选项
    $('.feiji-li').click(function(){
        $(this).children('.danxuankuang').addClass('dxxzl')
        $(this).siblings().children('.danxuankuang').removeClass('dxxzl')
    })
     $('.xuanzbu').click(function () {
        $(this).toggleClass('xzlah')
    })
    $('.closegx').click(function () {
        $('.qiandgc,.tanchuang').hide()
    })
    $('.chennshu').click(function () {
        $('.qiandgc,.tanchuang').show()
    })
    
    $('.xuanzbu').click(function(){
    	var _btn = $('.obut');
    	if($(this).hasClass('xzlah')){
    		_btn.removeClass('obutxz');
    	}else{
    		_btn.addClass('obutxz');
    	}
    });

     // 保存风险测评信息
	 function submitA(){
	     var _source = '${source}';
	     var _regUserId = '${regUserId}';

	     //校验最后一题是否正确
         var lastQuestionIsRight = $("#answer17").find("span[id='r2']").hasClass("dxxzl");
         if(lastQuestionIsRight){
             alert("您不是我们的定向及合格投资者");
             return;
         }

		 // 校验是否选中承诺
		 if($('.obut').hasClass('obutxz')){
			 return false;
		 }
		 // 拼接 有效信息 
		 var answers = "";
         var score = 0;
		 for(var i = 0; i < 17; i++){
			var _temp =  $("#answer"+ (i+1)).find(".dxxzl").attr("name");
			var _tempLength = $("#answer"+ (i+1)).find(".dxxzl").length;
			if(_tempLength == 0){
				alert("您有未答完的题目！");
				return ;
			}
			var _tempStr = _temp.split(":");
			answers += _tempStr[0] + "|";
			score += parseInt(_tempStr[1]);
		 }
		 answers = answers.substring(0,answers.length-1);
		 $.ajax({
				type:'post',
				data:{answers : answers,
					  score : score,
					  regUserId : _regUserId
				},
				url:'${request.contextPath}/fundController/saveRiskEvaluation',
				async:false,
				dataType:'json',
				success : function(data){
					if(_source == "1"){
					 	window.webkit.messageHandlers.riskEvaluation.postMessage(JSON.stringify(data));
					}else if(_source == "2"){
						window.Android.riskEvaluation(JSON.stringify(data));
					}
				}
		  });
       }
</script>
</body>
</html>
