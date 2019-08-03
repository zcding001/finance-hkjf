/**
 * Created by dzc on 17.12.12.
 */

var actIndexController = {};
(function() {
    'use strict';

    /**
     * 加载我的账户首页数据
     */
    actIndexController.loadData = function(){
        ajaxUtil.post("/userController/loadMyAccount.do", {}, function(data){
            var o = {};
            $(".percentView").attr("style", "width:" + data.params.scurityLevel + "%");
            var regUser = data.params.regUser;
            var regUserInfo = data.params.regUserInfo;
            var finAccount = data.params.finAccount;
            //刷新浏览器端用户缓存
            containerUtil.set(CONSTANTS.LOGIN_REG_USER, data.params.regUser);
            containerUtil.set(CONSTANTS.LOGIN_FIN_ACCOUNT, data.params.finAccount);
            delete regUser.lastLoginTime;
            o["couponType0"] = data.params.couponType0 == undefined ? 0 : data.params.couponType0;
            o["couponType1"] = data.params.couponType1 == undefined ? 0 : data.params.couponType1;
            o["couponType2"] = data.params.couponType2 == undefined ? 0 : data.params.couponType2;
            o["couponType3"] = data.params.couponType3 == undefined ? 0 : data.params.couponType3;
            $(".nickName").val(regUser.nickName);
            if(data.params.depositInfoCount != undefined && data.params.depositInfoCount > 0){
                $("#gfb1").removeClass("hide");
            }
            renderUtil.renderElement([regUser, regUserInfo, finAccount, data.params, o]);
            var infoObj = {};
            if(regUser.identify == 1){
                $(".toRealNameBtn").addClass("rz_cont");
                infoObj["identifyState"] = "已认证";
            }
            if(
                !(regUserInfo.emergencyContact == undefined || regUserInfo.emergencyContact == '' ||
                regUserInfo.emergencyTel == undefined || regUserInfo.emergencyTel == '' ||
                regUserInfo.email == undefined || regUserInfo.email == '' ||
                regUserInfo.contactAddress == undefined || regUserInfo.contactAddress == '')
            ){
                $(".toUserInfoBtn").addClass("rz_cont");
                infoObj["userInfoState"] = "已完善";
            }
            if(regUserInfo.emailState == 1){
                $(".toBindEmailBtn").addClass("rz_cont");
                infoObj["emailStateState"] = "已绑定";
            }
            renderUtil.renderElement([infoObj]);
            //生成柱状图
            _doHightChart(finAccount.nowMoney, data.params.capitalAmount, data.params.interestAmount, data.params.increaseAmount);
        });
        //编辑按钮事件
        $(".editBtn").click(function(){
            $(".nickDiv1").hide();
            $(".nickDiv2").removeClass("hide");
            $(".updateNickName").val($(".nickName").val());
        });
        //昵称保存事件
        $(".saveBtn").click(function(){
            var newNickName = $(".updateNickName").val();
            if(!validUtil.validNotEmpty(newNickName)){
                dialogUtil.alert("更新昵称", "请输入正确的昵称!");
            }else{
                ajaxUtil.post("/userController/updateNicName.do", {nickName : newNickName}, function(data){
                    if(data.resStatus == CONSTANTS.SUCCESS){
                        $(".nickDiv1").show();
                        $(".nickDiv2").addClass("hide");
                        $(".nickName").val(newNickName);
                        renderUtil.renderElement([{nickName: newNickName}]);
                        var cacheUser = containerUtil.get(CONSTANTS.LOGIN_REG_USER);
                        cacheUser.nickName = newNickName;
                        containerUtil.set(CONSTANTS.LOGIN_REG_USER, cacheUser);
                    }else{
                        dialogUtil.alert("更新昵称", data.resMsg);
                    }
                })
            }
        });
        //充值事件
        $("#rechargeBtn").click(function(){
            var regUser = containerUtil.get(CONSTANTS.LOGIN_REG_USER);
	        if(regUser.identify==0 || !validUtil.validNotEmpty(regUser.identify)){//未实名认证
	        	dialogUtil.confirm("系统提示","请进行实名认证之后再充值!",jump);
	            return;
	        }else{
	        	 commonUtil.jumpAccountMenu("recharge.html");
	        }
        });
        //实名
        $(".toRealNameBtn").click(function(){
            commonUtil.jumpAccountMenu("securityCenter.html");
        });
        //完善信息
        $(".toUserInfoBtn").click(function(){
            commonUtil.jumpAccountMenu("userInfo.html");
        });
        //邮箱绑定
        $(".toBindEmailBtn").click(function(){
            commonUtil.jumpAccountMenu("securityCenter.html", {"bandEmail" : 1});
        });
        //跳转到实名认证页面
        function jump(){
        	  commonUtil.jumpAccountMenu("securityCenter.html");
        }
        //提现事件
        $("#withdrawBtn").click(function(){
            commonUtil.jumpAccountMenu("Withdrawals.html");
        });
        //兑换好礼
        $(".exchangeBtn").click(function(){
            commonUtil.jump("pointmall/index.html");
        });
        //安全级别提升
        $(".enhanceBtn").click(function(){
            commonUtil.jumpAccountMenu("securityCenter.html");
        });
        //卡券详情事件
        $(".coupontCountDiv a").each(function(){
            $(this).click(function(){
                commonUtil.jumpAccountMenu("couponRecord.html");
            });
        });
        //好友推荐图片事件
        $(".hytjBtn").click(function(){
            commonUtil.jumpAccountMenu("hytj.html");
        });
        $(".buyHourseBtn").click(function(){
            commonUtil.jump("invest/investList.html?investType=gfb");
        });
    };

    function _doHightChart(nowMoney, capitalAmount, interestAmount, increaseAmount){
        Highcharts.theme = {
            colors: ["#f45b5b", "#8085e9", "#8d4654", "#7798BF", "#aaeeee", "#ff0066", "#eeaaee",
                "#55BF3B", "#DF5353", "#7798BF", "#aaeeee"
            ],
            chart: {
                backgroundColor: null,
                style: {
                    fontFamily: "Signika, serif"
                }
            },
            title: {
                style: {
                    color: 'black',
                    fontSize: '16px',
                    fontWeight: 'bold'
                }
            },
            subtitle: {
                style: {
                    color: 'black'
                }
            },
            tooltip: {
                borderWidth: 0
            },
            legend: {
                itemStyle: {
                    fontWeight: 'bold',
                    fontSize: '13px'
                }
            },
            xAxis: {
                labels: {
                    style: {
                        color: '#6e6e70'
                    }
                }
            },
            yAxis: {
                labels: {
                    style: {
                        color: '#6e6e70'
                    }
                }
            },
            plotOptions: {
                series: {
                    shadow: true
                },
                candlestick: {
                    lineColor: '#404048'
                },
                map: {
                    shadow: false
                }
            },
            navigator: {
                xAxis: {
                    gridLineColor: '#D0D0D8'
                }
            },
            rangeSelector: {
                buttonTheme: {
                    fill: 'white',
                    stroke: '#C0C0C8',
                    'stroke-width': 1,
                    states: {
                        select: {
                            fill: '#D0D0D8'
                        }
                    }
                }
            },
            scrollbar: {
                trackBorderColor: '#f39200'
            },
            // General
            background2: '#E0E0E8'

        };
        Highcharts.setOptions(Highcharts.theme);

        Highcharts.wrap(Highcharts.Chart.prototype, 'getContainer', function(proceed) {
            proceed.call(this);
            this.container.style.background = 'url(../src/img/account/zzt2.png)';
        });

        var colors = Highcharts.getOptions().colors,
            categories = ['账户总额', '待收本金', '待收利益', '账户余额', '待加息收益'],
            name = 'Browser brands',
            data = [{
                y: parseFloat((parseFloat(nowMoney + capitalAmount + interestAmount + increaseAmount)).toFixed(2)),
                color: {
                    linearGradient: { x1: 0, y1: 1, x2: 0, y2: 0 },
                    stops: [
                        [0, 'rgba(243, 150, 0,0)'],
                        [1, 'rgba(243, 150, 0, 1)']
                    ]
                },
                drilldown: {
                    data: [10.85, 7.35, 33.06, 2.81],
                    color: colors[0]
                }
            }, {
                y:  capitalAmount,
                color: {
                    linearGradient: { x1: 0, y1: 1, x2: 0, y2: 0 },
                    stops: [
                        [0, 'rgba(243, 150, 0,0)'],
                        [1, 'rgba(243, 150, 0, 1)']
                    ]
                },
                drilldown: {
                    data: [0.20, 0.83, 1.58, 13.12, 5.43],
                    color: colors[1]
                }
            }, {
                y: interestAmount,
                color: {
                    linearGradient: { x1: 0, y1: 1, x2: 0, y2: 0 },
                    stops: [
                        [0, 'rgba(243, 150, 0,0)'],
                        [1, 'rgba(243, 150, 0, 1)']
                    ]
                },
                drilldown: {
                    data: [0.12, 0.19, 0.12, 0.36, 0.32, 9.91, 0.50, 0.22],
                    color: colors[2]
                }
            }, {
                y: nowMoney,
                color: {
                    linearGradient: { x1: 0, y1: 1, x2: 0, y2: 0 },
                    stops: [
                        [0, 'rgba(243, 150, 0,0)'],
                        [1, 'rgba(243, 150, 0, 1)']
                    ]
                },
                drilldown: {
                    data: [4.55, 1.42, 0.23, 0.21, 0.20, 0.19, 0.14],
                    color: colors[3]
                }
            }, {
                y: increaseAmount,
                color: {
                    linearGradient: { x1: 0, y1: 1, x2: 0, y2: 0 },
                    stops: [
                        [0, 'rgba(243, 150, 0,0.5)'],
                        [1, 'rgba(243, 150, 0, 1)']
                    ]
                },
                drilldown: {
                    data: [0.12, 0.37, 1.65],
                    color: colors[4]
                }
            }];

        function setChart(name, categories, data, color) {
            chart.xAxis[0].setCategories(categories, false);
            chart.series[0].remove(false);
            chart.addSeries({
                name: name,
                data: data,
                color: color || 'white'
            }, false);
            chart.redraw();
        }

        var chart = $('#container').highcharts({
            chart: {
                type: 'column'
            },
            title: {
                text: null
            },
            subtitle: {
                text: null
            },
            xAxis: {
                categories: categories,
                labels: {
                    enabled: false
                },
                gridLineWidth: 0,
                tickWidth: 0,
                lineColor: '#fff' //设置与背景色相同色
            },
            yAxis: {
                title: {
                    text: null
                },
                labels: {
                    enabled: false
                },
                gridLineWidth: 0
            },
            credits: {
                enabled: false
            },
            legend: {
                enabled: false
            },
            plotOptions: {
                column: {
                    cursor: 'pointer',
                    pointWidth: 54,
                    dataLabels: {
                        enabled: true,
                        color: '#ff6c00',
                        style: {
                            fontWeight: 'bold'
                        },
                        formatter: function() {
                            var strVal = this.y + '';
                            if (strVal.indexOf('.') == -1) {
                                return strVal + '.00';
                            } else {
                                var arr = strVal.split('.');
                                if (arr[1].length == 2) {
                                    return strVal;
                                } else {
                                    return strVal + '0';
                                }
                            }
                        }
                    }
                }
            },
            tooltip: {
                enabled: false
            },
            series: [{
                name: name,
                data: data,
                color: 'white'
            }],
            exporting: {
                enabled: false
            }
        }).highcharts();
    }
})();


$(document).ready(function(){
    actIndexController.loadData();
});
