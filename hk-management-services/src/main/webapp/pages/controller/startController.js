/**
 * Created by xuhui.liu on 2017/08/26.
 */
var app = angular.module('appModule', ['validation', 'validation.rule', 'ngRoute', 'oc.lazyLoad', 'ui.tree']);
app.config(["$provide", "$compileProvider", "$controllerProvider", "$filterProvider",
    function ($provide, $compileProvider, $controllerProvider, $filterProvider) {
        app.controller = $controllerProvider.register;
        app.directive = $compileProvider.directive;
        app.filter = $filterProvider.register;
        app.factory = $provide.factory;
        app.service = $provide.service;
        app.constant = $provide.constant;
    }]);

//注册errorInterceptor工厂
app.factory('errorInterceptor', function ($q, $rootScope) {
    return {
        // optional method
        'response': function (response) {
            if (!response.data) {
                return response|| $q.when(response);
            }else if(response.data.resStatus!=undefined&&response.data.resStatus!=null){
                if (response.data.resStatus==2003/*没有登录*/) {
                    //没有登录，跳转到登录页面
                    window.location.href = commonUtil.getUrl() + "login.html?targetUrlAfterLogin="+window.location.href;
                    return response|| $q.when(response);
                }else if (response.data.resStatus==2005/*没有权限*/) {
                    //没有权限
                    commonUtil.createSimpleNotify("对不起，您没有相应的权限",ALERT_ERR);
                    return response|| $q.when(response);
                }
            }
            return response|| $q.when(response);
        },


        'responseError': function (rejection) {
            // do something on error
            if (rejection.status == -1) {
                $rootScope.$broadcast("warn", {
                    type: 'error',
                    desc: '连接服务器失败'
                });
                return $q.reject(rejection);
            }
            if (rejection.status == 500) {
                $rootScope.$broadcast("warn", {
                    type: 'error',
                    desc: '服务器内部错误'
                });
                return $q.reject(rejection);
            }
            if (rejection.status == 404) {
                $rootScope.$broadcast("warn", {
                    type: 'error',
                    desc: '请求的页面不存在'
                });
                return $q.reject(rejection);
            }
            if (rejection.status == 401) {
                window.location = "/login.html";
            }
            if (rejection.status == 403) {
                $rootScope.$broadcast("warn", {
                    type: 'error',
                    desc: '对不起，您没有访问权限'
                });
                return $q.reject(rejection);
            }
            $rootScope.$broadcast("warn", {
                type: 'error',
                desc: '服务器发生未知错误（status' + rejection.status + '）'
            });
            return $q.reject(rejection);
        }
    };
});

//把errorInterceptor注册到$httpProvider
app.config(['$httpProvider', function ($httpProvider) {
    $httpProvider.interceptors.push('errorInterceptor');
}]);

app.run(function ($rootScope, $http) {
    $rootScope.$on('$locationChangeStart', function (evt, next, current) {
        /* $http.post(CONFIG.interface.userIsLogin,{}).success(function (data) {
             if(data.resStatus==402){
                 //没有登录，跳转到登录页面
                 window.location.href = commonUtil.getUrl() + "login.html";
             }
         }).error(function () {
             //do nothing..
         });*/

        $.ajax({
            url: CONFIG.interface.userIsLogin,
            data: {},
            type: 'POST',
            dataType: 'JSON',
            async: false,
            success: function (result) {
                if (result.resStatus == 2003) {
                    //没有登录，跳转到登录页面
                    window.location.href = commonUtil.getUrl() + "login.html?_v="+LazyLoadConfig.VERSION+"&targetUrlAfterLogin="+next;
                }
            },
            error: function (msg) {
                //do nothing...
            }
        });

    })
});
/** 页面请求路由 */
app.config(function ($routeProvider) { //$route【router.js监控】
    /**公共路由*/
    var routeProvider = $routeProvider
        .when('/sysMenus', {
            /*系统管理->菜单配置*/
            templateUrl: 'pages/template/menuAndAuth/sysMenus.html?_v='+LazyLoadConfig.VERSION,
            controller: 'sysMenus',
            resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.LOAD_JS_SYS_MENU)
        }).when('/sysAppMenus', {
            /*系统管理->权限配置*/
            templateUrl: 'pages/template/menuAndAuth/sysAppMenus.html?_v='+LazyLoadConfig.VERSION,
            controller: 'sysMenus',
            resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.LOAD_JS_SYS_MENU)
        }).when('/sysAppServes', {
            /*系统管理->权限配置*/
            templateUrl: 'pages/template/menuAndAuth/sysAppServes.html?_v='+LazyLoadConfig.VERSION,
            controller: 'appServes',
            resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.LOAD_JS_APP_SERVES)
        }).when('/sysPrivilege', {
            /*系统管理->权限配置*/
            templateUrl: 'pages/template/menuAndAuth/sysPrivilege.html?_v='+LazyLoadConfig.VERSION,
            controller: 'sysPrivilege',
            resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.LOAD_JS_SYS_PRIVILEGE)
        }).when('/bindPrivigeToMenu', {
            /*系统管理->给菜单绑定权限*/
            templateUrl: 'pages/template/menuAndAuth/bindPrivigeToMenu.html?_v='+LazyLoadConfig.VERSION,
            controller: 'menuBindPri',
            resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.LOAD_JS_SYS_MENU_BIND_PRI)
        }).when('/roleManage', {
            /*系统管理->角色管理*/
            templateUrl: 'pages/template/menuAndAuth/roles.html?_v='+LazyLoadConfig.VERSION,
            controller: 'roles',
            resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.LOAD_JS_SYS_ROLES)
        }).when('/bindPrivigeToRole', {
            /*系统管理->角色管理*/
            templateUrl: 'pages/template/menuAndAuth/bindPrivigeToRole.html?_v='+LazyLoadConfig.VERSION,
            controller: 'bindMenusToRole',
            resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.LOAD_JS_SYS_ROLES)
        }).when('/operatorManage', {
            /*系统管理->后台用户管理*/
            templateUrl: 'pages/template/menuAndAuth/operatorManage.html?_v='+LazyLoadConfig.VERSION,
            controller: 'operatorManage',
            resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.LOAD_JS_OPERATOR_MANAGE)
        }).when('/bindRolesToUser', {
            /*系统管理->角色授权*/
            templateUrl: 'pages/template/menuAndAuth/bindRoleToUser.html?_v='+LazyLoadConfig.VERSION,
            controller: 'bidRolesToOperator',
            resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.LOAD_JS_OPERATOR_MANAGE)
        }).when('/userRoles', {
            /*系统管理->菜单配置*/
            templateUrl: 'pages/template/menuAndAuth/userRoles.html?_v='+LazyLoadConfig.VERSION
        }).when('/addUserRoles', {
            templateUrl: 'pages/template/menuAndAuth/addUserRoles.html?_v='+LazyLoadConfig.VERSION,
            controller: "userRolesController",
            resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.LOAD_JS_USER_ROLES)
        });
    /**标的服务理由*/
    routeProvider.when('/loanBidInfoList', {
        /*筹款中的标的列表*/
        templateUrl: 'pages/template/bidInfo/bidInfoList.html?_v='+LazyLoadConfig.VERSION
    }).when('/auditBidInfoList', {
        /*待审核标的列表*/
        templateUrl: 'pages/template/bidInfo/auditBidInfoList.html?_v='+LazyLoadConfig.VERSION
    }).when('/bidInfoAfterLoanList', {
        /*已完成和进行中标的列表*/
        templateUrl: 'pages/template/bidInfo/bidInfoAfterLoanList.html?_v='+LazyLoadConfig.VERSION
    }).when('/addBidInfo', {
        /*借款标管理-》新增借款标&修改借款标*/
        templateUrl: 'pages/template/bidInfo/addBidInfo.html?_v='+LazyLoadConfig.VERSION,
        controller: 'bidCtrl',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.LOAD_JS_OPERATEBIDINFO)
    }).when('/bidInfoDeatil', {
        /*借款标管理-》查看借款标详情*/
        templateUrl: 'pages/template/bidInfo/bidInfoDetail.html?_v='+LazyLoadConfig.VERSION,
        controller: 'bidInfoDeatil',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.LOAD_JS_BIDINFODETAIL)
    }).when('/exchangeBidInfoDeatil', {
        /*借款标管理-》查看借款标详情*/
        templateUrl: 'pages/template/bidInfo/exchangeBidInfoDetail.html?_v='+LazyLoadConfig.VERSION,
        controller: 'bidInfoDeatil',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.LOAD_JS_BIDINFODETAIL)
    }).when('/bidProductList', {
        /*借款标管理-》标的产品列表*/
        templateUrl: 'pages/template/bidInfo/product/bidProductList.html?_v='+LazyLoadConfig.VERSION
    }).when('/addBidProduct', {
        /*借款标管理-》添加标的产品*/
        templateUrl: 'pages/template/bidInfo/product/addBidProduct.html?_v='+LazyLoadConfig.VERSION,
        controller: 'bidProduct',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.LOAD_JS_BID_PRODUCT)
    }).when('/bidProductDetail', {
        /*借款标管理-》查看标的产品详情*/
        templateUrl: 'pages/template/bidInfo/product/bidProductDetail.html?_v='+LazyLoadConfig.VERSION,
        controller: 'bidProduct',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.LOAD_JS_BID_PRODUCT)
    });

    /** 匹配管理 路由*/
    routeProvider.when('/matchBidList', {
        /*待匹配标的列表*/
        templateUrl: 'pages/template/bidInfo/match/matchBidList.html?_v='+LazyLoadConfig.VERSION
    }).when('/matchBidDetail', {
        /*待匹配标的详情*/
        templateUrl: 'pages/template/bidInfo/match/matchBidDetail.html?_v='+LazyLoadConfig.VERSION,
        controller: 'matchBidController',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.LOAD_JS_MATCHBIDDETAIL)
    }).when('/allMatchBidQuery', {
        /*待匹配标的列表*/
        templateUrl: 'pages/template/bidInfo/match/allMatchBidQuery.html?_v='+LazyLoadConfig.VERSION
    }).when('/bidMatchRecord', {
        /*待匹配标的列表*/
        templateUrl: 'pages/template/bidInfo/match/bidMatchRecord.html?_v='+LazyLoadConfig.VERSION
    }).when('/matchedBidList', {
        /*待匹配标的列表*/
        templateUrl: 'pages/template/bidInfo/match/matchedBidList.html?_v='+LazyLoadConfig.VERSION
    }).when('/matchedDetails', {
        /*待匹配标的详情*/
        templateUrl: 'pages/template/bidInfo/match/matchedDetails.html?_v='+LazyLoadConfig.VERSION,
        controller: 'matchedDetailsController',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.LOAD_JS_MATCHEDDETAILS)
    }).when('/matchBidStatistics', {
        /*待匹配标的详情*/
        templateUrl: 'pages/template/bidInfo/match/matchBidStatistics.html?_v='+LazyLoadConfig.VERSION
    }).when('/bidExchangeList', {
        /*交易所标的匹配列表*/
        templateUrl: 'pages/template/bidInfo/match/bidExchangeList.html?_v='+LazyLoadConfig.VERSION
    }).when('/investExchangeList', {
        /*交易所标的匹配列表*/
        templateUrl: 'pages/template/bidInfo/match/investExchangeList.html?_v='+LazyLoadConfig.VERSION,
        controller: 'investExchangeController',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.LOAD_JS_INVEST_EXCHANGE)
    })

    /** 财务管理路由 **/
    routeProvider.when('/makeLoanList', {
        /*待放款列表*/
        templateUrl: 'pages/template/bidInfo/loan/makeLoanList.html?_v='+LazyLoadConfig.VERSION
    }).when('/offLinePacketForCheck', {
        /*增值服务-》红包线下推广*/
        templateUrl: 'pages/template/vas/offLinePacketForCheck.html?_v='+LazyLoadConfig.VERSION,
        controller: 'redPacketOffLine',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.REDPACKET_OFFLINE)
    }).when('/payCheckList', {
        /*财务管理-》支付对账*/
        templateUrl: 'pages/template/payment/payCheckList.html?_v='+LazyLoadConfig.VERSION
    });


    /**资讯服务路由*/
    routeProvider.when('/notice', {
        /*资讯管理-》公告管理*/
        templateUrl: 'pages/template/info/notice.html?_v='+LazyLoadConfig.VERSION,
        controller: 'noticeCtrl',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.INFO_JS_NOTICE)
    }).when('/noticeList', {
        /*资讯管理-》公告管理列表*/
        templateUrl: 'pages/template/info/noticeList.html?_v='+LazyLoadConfig.VERSION
    }).when('/newsList', {
        /*资讯管理-》新闻管理列表*/
        templateUrl: 'pages/template/info/newsList.html?_v='+LazyLoadConfig.VERSION
    }).when('/friendlyurlList', {
        /*资讯管理-》公告管理*/
        templateUrl: 'pages/template/info/friendlyUrlList.html?_v='+LazyLoadConfig.VERSION
    }).when('/news', {
        /*资讯管理-》新闻管理*/
        templateUrl: 'pages/template/info/news.html?_v='+LazyLoadConfig.VERSION,
        controller: 'newsCtrl',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.INFO_JS_NEWS)
    }).when('/questionnaire', {
        /*资讯管理-》新闻管理->>添加调查问题*/
        templateUrl: 'pages/template/info/questionnaire.html?_v='+LazyLoadConfig.VERSION,
        controller: 'questionnaireCtrl',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.INFO_JS_QUESTIONNAIRE)
    }).when('/updateQuestionnaire', {
        /*资讯管理-》新闻管理->>添加调查问题*/
        templateUrl: 'pages/template/info/updateQuestionnaire.html?_v='+LazyLoadConfig.VERSION,
        controller: 'updateQuestionnaireCtrl',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.INFO_JS_UPDATE_QUESTIONNAIRE)

    }).when('/friendlyurl', {
        /*资讯管理-》友情链接*/
        templateUrl: 'pages/template/info/friendlyUrl.html?_v='+LazyLoadConfig.VERSION,
        controller: 'friendlyurlCtrl',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.INFO_JS_FRIENDURL)
    }).when('/settingPages/systemManage', {
        /*系统管理->权限配置*/
        templateUrl: 'pages/template/systemSettings/authConfig/authConfig.html?_v='+LazyLoadConfig.VERSION,
        controller: 'loanBidInfoList'
    }).when('/addBidInfo', {
        /*借款标管理-》新增借款标&修改借款标*/
        templateUrl: 'pages/template/bidInfo/addBidInfo.html?_v='+LazyLoadConfig.VERSION,
        controller: 'bidCtrl',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.LOAD_JS_OPERATEBIDINFO)
    }).when('/noticeDeatil', {
        /*资讯管理-》公告管理详情*/
        templateUrl: 'pages/template/info/noticeDetail.html?_v='+LazyLoadConfig.VERSION,
        controller: 'noticeDeatil',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.INFO_JS_NOTICEDETAIL)
    }).when('/newsDeatil', {
        /*资讯管理-》新闻管理详情*/
        templateUrl: 'pages/template/info/newsDetail.html?_v='+LazyLoadConfig.VERSION,
        controller: 'newsDeatil',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.INFO_JS_NEWSDETAIL)
    }).when('/friendlyurlDeatil', {
        /*资讯管理-》友情链接详情*/
        templateUrl: 'pages/template/info/friendlyUrlDetail.html?_v='+LazyLoadConfig.VERSION,
        controller: 'friendlyurlDeatil',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.INFO_JS_FRIENDURLDETAIL)
    }).when('/carouselFigureList', {
        /*资讯管理-》广告轮播列表*/
        templateUrl: 'pages/template/info/carouselFigureList.html?_v='+LazyLoadConfig.VERSION
    }).when('/carouselFigureDeatil', {
        /*资讯管理-》友情链接详情*/
        templateUrl: 'pages/template/info/carouselFigureDetail.html?_v='+LazyLoadConfig.VERSION,
        controller: 'carouselFigureDeatil',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.INFO_JS_CAROUSELFIGUREDETAIL)
    }).when('/carouselFigure', {
        /*资讯管理-》友情链接详情*/
        templateUrl: 'pages/template/info/carouselFigure.html?_v='+LazyLoadConfig.VERSION,
        controller: 'carouselFigure',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.INFO_JS_CAROUSELFIGURE)
    }).when('/creditorListByBidId', {
        /*标的查询（设置债权转让）--查询某标的下的投资记录*/
        templateUrl: 'pages/template/bidInfo/creditorList.html?_v='+LazyLoadConfig.VERSION
//         controller:'crEDITORLIST',
//         RESOLVE:ocLazyLoadJs(LazyLoadConfig.CONSTANTS.LOAD_JS_CREDITORLIST)
    }).when('/creditorProperty', {
        templateUrl: 'pages/template/bidInfo/creditorProperty.html?_v='+LazyLoadConfig.VERSION,
        controller: 'creditorProperty',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.LOAD_JS_CREDITOR_PORPERTY)
    });


    /** 还款计划、回款计划路由 **/
    routeProvider.when('/repayPlanList', {
        /*还款计划路由*/
        templateUrl: 'pages/template/bidInfo/plan/repayPlanList.html?_v='+LazyLoadConfig.VERSION
    }).when('/receiptPlanList', {
        /*回款计划路由*/
        templateUrl: 'pages/template/bidInfo/plan/receiptPlanList.html?_v='+LazyLoadConfig.VERSION
    });

    /**增值服务**/
    routeProvider.when('/offLinePacket', {
        /*增值服务-》红包线下推广*/
        templateUrl: 'pages/template/vas/offLinePacketList.html?_v='+LazyLoadConfig.VERSION,
        controller: 'redPacketOffLine',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.REDPACKET_OFFLINE)
    });

    /**积分模块**/
    routeProvider.when('/pointRuleList', {
        /*积分模块->积分规则列表*/
        templateUrl: 'pages/template/pointRule/pointRuleList.html?_v='+LazyLoadConfig.VERSION
    }).when('/addPointRule', {
        /*积分模块->积分规则列表-》添加或修改积分规则*/
        templateUrl: 'pages/template/pointRule/addPointRule.html?_v='+LazyLoadConfig.VERSION,
        controller: 'pointRuleCtrl',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.LOAD_JS_POINTRULE)
    }).when('/pointAccount', {
        /*积分模块->积分账户*/
        templateUrl: 'pages/template/point/pointAccount.html?_v='+LazyLoadConfig.VERSION,
        controller: 'pointController',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.POINT_ACCOUNT)
    }).when('/pointRecordCheckList', {
        /*积分模块->赠送积分审核*/
        templateUrl: 'pages/template/point/pointRecordCheckList.html?_v='+LazyLoadConfig.VERSION,
        controller: 'pointController',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.POINT_ACCOUNT)
    }).when('/pointMerchantInfoList', {
        /*积分模块->积分商户*/
        templateUrl: 'pages/template/point/pointMerchantInfoList.html?_v='+LazyLoadConfig.VERSION,
        controller: 'pointMerchantController',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.POINT_MERCHANT)
    }).when('/pointMerchantInfoEdit', {
        /*积分模块->添加或者修改积分商户*/
        templateUrl: 'pages/template/point/pointMerchantInfoEdit.html?_v='+LazyLoadConfig.VERSION,
        controller: 'pointMerchantController',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.POINT_MERCHANT)
    }).when('/pointMerchantInfoDetail', {
        /*积分模块->添加或者修改积分商户*/
        templateUrl: 'pages/template/point/pointMerchantInfoDetail.html?_v='+LazyLoadConfig.VERSION,
        controller: 'pointMerchantController',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.POINT_MERCHANT)
    }).when('/pointRecordList', {
        /*积分模块->积分记录*/
        templateUrl: 'pages/template/point/pointRecordList.html?_v='+LazyLoadConfig.VERSION,
        controller: 'pointController',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.POINT_ACCOUNT)
    }).when('/pointPayRecordList', {
        /*积分模块->积分支付兑现记录*/
        templateUrl: 'pages/template/point/pointPayRecordList.html?_v='+LazyLoadConfig.VERSION,
        controller: 'pointController',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.POINT_ACCOUNT)
    });

    /**积分商城*/
    routeProvider.when('/pointProductCategory', {
        /*积分模块->积分规则列表*/
        templateUrl: 'pages/template/pointmall/pointProductCategory.html?_v='+LazyLoadConfig.VERSION,
        controller: 'pointProductCategory',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.POINT_PRODUCT_CATEGORY)
    }).when('/pointProductList', {
        /*积分模块->积分商品管理*/
        templateUrl: 'pages/template/pointmall/pointProductList.html?_v='+LazyLoadConfig.VERSION,
        controller: 'pointProductList',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.POINT_PRODUCT)
    }).when('/pointProductOrder', {
        /*积分模块->积分订单管理*/
        templateUrl: 'pages/template/pointmall/pointProductOrder.html?_v='+LazyLoadConfig.VERSION,
        controller: 'pointProductOrder',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.POINT_PRODUCT_ORDER)
    }).when('/pointProductAdd', {
        /*积分模块->积分商品添加*/
        templateUrl: 'pages/template/pointmall/pointProductAdd.html?_v='+LazyLoadConfig.VERSION,
        controller: 'pointProduct',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.POINT_PRODUCT)
    }).when('/pointProductDeatil', {
    	/*积分模块->促俏记录*/
        templateUrl: 'pages/template/pointmall/pointProductRecordList.html?_v='+LazyLoadConfig.VERSION
    }).when('/pointProDeatils', {
    	/*积分模块->商品详情记录*/
        templateUrl: 'pages/template/pointmall/pointProductDetail.html?_v='+LazyLoadConfig.VERSION,
        controller: 'pointProDeatils',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.POINT_PRODUCT_RECORD)
    });

    /** 用户管理 **/
    routeProvider.when('/userList', {
        /*用户列表*/
        templateUrl: 'pages/template/user/userList.html?_v='+LazyLoadConfig.VERSION
    }).when('/userDetail', {
        /*用户列表*/
        templateUrl: 'pages/template/user/userDetail.html?_v='+LazyLoadConfig.VERSION,
        controller: 'userDetail',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.LOAD_JS_USER_DETAIL)
    }).when('/tenement', {
        /*物业列表*/
        templateUrl: 'pages/template/user/tenement.html?_v='+LazyLoadConfig.VERSION
    }).when('/community', {
        /*小区列表*/
        templateUrl: 'pages/template/user/community.html?_v='+LazyLoadConfig.VERSION,
        controller: 'CommunityController',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.LOAD_JS_COMMUNITY_CONTROLLER)
    }).when('/TenementBindCommunities', {
        /*把小区绑定到物业的页面*/
        templateUrl: 'pages/template/user/bindCommunitiesToTenement.html?_v='+LazyLoadConfig.VERSION,
        controller: 'TenementBindCommunitiesController',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.LOAD_JS_TENEMENT_BIND_COMMUNITIES)
    }).when('/userAccountMoney', {
        /*用户账户资金*/
        templateUrl: 'pages/template/user/userAccountMoney.html?_v='+LazyLoadConfig.VERSION,
        controller: 'userAccountMoney',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.LOAD_JS_USER_ACCOUNT_MONEY)
    }).when('/addCompanyUser', {
        /*添加企业账户*/
        templateUrl: 'pages/template/user/addCompanyUser.html?_v='+LazyLoadConfig.VERSION,
        controller: 'companyUser',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.LOAD_JS_COMPANY_USER)
    }).when('/importUsers', {
        /*批量导入用户*/
        templateUrl: 'pages/template/user/importUsers.html?_v='+LazyLoadConfig.VERSION,
        controller: 'importController',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.LOAD_JS_IMPORT_FILE)
    }).when('/bidAutoSchemeList', {
        /*用户自动投资设置*/
        templateUrl: 'pages/template/bidInfo/bidAutoSchemeList.html?_v='+LazyLoadConfig.VERSION
    }).when('/appLoginLogList', {
        /*用户自动投资设置*/
        templateUrl: 'pages/template/user/appLoginLogList.html?_v='+LazyLoadConfig.VERSION
    }).when('/userAudit', {
        /*用户审核资料维护*/
        templateUrl: 'pages/template/user/userAudit.html?_v='+LazyLoadConfig.VERSION,
        controller: 'userAuditController',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.LOAD_JS_COMPANY_USER_AUDIT)
    }).when('/vipRecordsList', {
        /*vip记录*/
        templateUrl: 'pages/template/user/vipRecordsList.html?_v='+LazyLoadConfig.VERSION
    }).when('/vipRecordsDetailList', {
        /*vip记录*/
        templateUrl: 'pages/template/user/vipRecordsDetailList.html?_v='+LazyLoadConfig.VERSION
    });

    /** 钱袋子管理 **/
    routeProvider.when('/searchHqProductInfo', {
        /*活期产品列表*/
        templateUrl: 'pages/template/qdz/searchHqProductInfo.html?_v='+LazyLoadConfig.VERSION
    }).when('/searchInterestDayFailList', {
        /*每日计息错误记录*/
        templateUrl: 'pages/template/qdz/searchInterestDayFailList.html?_v='+LazyLoadConfig.VERSION
    }).when('/searchQdzBillWater', {
        /*账单流水查询*/
        templateUrl: 'pages/template/qdz/searchQdzBillWater.html?_v='+LazyLoadConfig.VERSION
    }).when('/searchTransRecordFailList', {
        /*交易失败查询*/
        templateUrl: 'pages/template/qdz/searchTransRecordFailList.html?_v='+LazyLoadConfig.VERSION
    }).when('/searchInterestBalance', {
        /*利息对账查询*/
        templateUrl: 'pages/template/qdz/searchInterestBalance.html?_v='+LazyLoadConfig.VERSION
    }).when('/searchCreditorInfo', {
        /*当期债权查询*/
        templateUrl: 'pages/template/qdz/searchCreditorInfo.html?_v='+LazyLoadConfig.VERSION,
        controller: 'searchCreditorInfo',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.LOAD_JS_QDZ_CREDITORINFO)
    }).when('/searchCreditorBalanceInfo', {
        /*债权对账查询*/
        templateUrl: 'pages/template/qdz/searchCreditorBalanceInfo.html?_v='+LazyLoadConfig.VERSION
    }).when('/searchQdzRule', {
        /*钱袋子规则查询*/
        templateUrl: 'pages/template/qdz/searchQdzRule.html?_v='+LazyLoadConfig.VERSION
    }).when('/addQdzRule', {
        /*增加钱袋子规则页面*/
        templateUrl: 'pages/template/qdz/addQdzRule.html?_v='+LazyLoadConfig.VERSION,
        controller: 'qdzRuleCtrl',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.LOAD_JS_QDZ_RULE)
    }).when('/searchQdzRecommendEarnRule', {
        /*钱袋子推荐奖规则查询*/
        templateUrl: 'pages/template/qdz/searchQdzRecommendEarnRule.html?_v='+LazyLoadConfig.VERSION
    }).when('/addQdzRecommendEarnRule', {
        /*增加钱袋子推荐奖规则*/
        templateUrl: 'pages/template/qdz/addQdzRecommendEarnRule.html?_v='+LazyLoadConfig.VERSION,
        controller: 'qdzRuleRecommendEarnCtrl',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.LOAD_JS_QDZ_RULE_RECOMMEND_EARN)
    }).when('/searchQdzCreditorRule', {
        /*钱袋子债券池规则查询*/
        templateUrl: 'pages/template/qdz/searchQdzCreditorRule.html?_v='+LazyLoadConfig.VERSION,
        controller: 'qdzRuleCreditorCtrl',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.LOAD_JS_QDZ_RULE_CREDITOR)
    }).when('/updateQdzCreditorRule', {
        /*增加债权池规则*/
        templateUrl: 'pages/template/qdz/updateQdzCreditorRule.html?_v='+LazyLoadConfig.VERSION,
        controller: 'qdzRuleCreditorCtrl',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.LOAD_JS_QDZ_RULE_CREDITOR)
    }).when('/interestDetail', {
        /*钱袋子利息明细*/
        templateUrl: 'pages/template/qdz/interestDetail.html?_v='+LazyLoadConfig.VERSION
    });
    /** 体验金管理 **/
    routeProvider.when('/simGrantList', {
        /*体验金发放列表*/
        templateUrl: 'pages/template/sim/simGrantList.html?_v='+LazyLoadConfig.VERSION
    }).when('/simRuleList', {
        /*体验金规则列表*/
        templateUrl: 'pages/template/sim/simRuleList.html?_v='+LazyLoadConfig.VERSION,
    }).when('/addSimRule', {
        /*体验金添加*/
        templateUrl: 'pages/template/sim/addSimRule.html?_v='+LazyLoadConfig.VERSION,
        controller: 'simRuleCtrl',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.VAS_JS_GOLDRULE)
    }).when('/findSimGrant', {
        /*体验金查询*/
        templateUrl: 'pages/template/sim/findSimGrant.html?_v='+LazyLoadConfig.VERSION
    }).when('/simgold', {
        /*体验金作废*/
        templateUrl: 'pages/template/sim/findSimGrant.html?_v='+LazyLoadConfig.VERSION,
        controller: 'simgoldCtrl',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.VAS_JS_SIMGOLD)
    }).when('/simGoldDetail', {
        /*体验金查询详情*/
        templateUrl: 'pages/template/sim/simGoldDetail.html?_v='+LazyLoadConfig.VERSION,
        controller: 'simgoldDetailCtrl',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.VAS_JS_GOLDRULEDETAIL)
    }).when('/simGoldImport', {
        /*体验金批量导入*/
        templateUrl: 'pages/template/sim/simGoldImport.html',
        controller: 'simgoldImportCtrl',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.VAS_JS_GOLDIMPORT)
    });
    /**合同管理路由**/
    routeProvider.when('/contractTypeList', {
        /*合同类型列表*/
        templateUrl: 'pages/template/contract/contractTypeList.html?_v='+LazyLoadConfig.VERSION
    }).when('/addContractType', {
        /*合同管理=》合同类型管理=》添加或修改合同类型*/
        templateUrl: 'pages/template/contract/addContractType.html?_v='+LazyLoadConfig.VERSION,
        controller: 'contractTypeCtrl',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.LOAD_JS_CONTRACTTYPE)
    }).when('/contractTemplateList', {
        /*合同模板列表*/
        templateUrl: 'pages/template/contract/contractTemplateList.html?_v='+LazyLoadConfig.VERSION
    }).when('/addContractTemplate', {
        /*合同管理=》合同模板管理=》添加或修改合同模板*/
        templateUrl: 'pages/template/contract/addContractTemplate.html?_v='+LazyLoadConfig.VERSION,
        controller: 'contractTemplateCtrl',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.LOAD_JS_CONTRACTTEMPLATE)
    }).when('/contractTemplateDetail', {
        /*合同管理=》合同模板管理=》查看合同模板内容详情*/
        templateUrl: 'pages/template/contract/contractTemplateDetail.html?_v='+LazyLoadConfig.VERSION,
        controller: 'contractTemplateDetailCtrl',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.LOAD_JS_CONTRACTTEMPLATEDETAIL)
    }).when('/searchCarInfoList', {
        /*汽车金融=》车辆信息列表*/
        templateUrl: 'pages/template/car/carInfoList.html?_v='+LazyLoadConfig.VERSION
    }).when('/addCarInfo', {
        /*汽车金融=》添加或修改车辆信息*/
        templateUrl: 'pages/template/car/addCarInfo.html?_v='+LazyLoadConfig.VERSION,
        controller: 'carInfoCtrl',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.CAR_INFO)
    }).when('/carInfo', {
        /*车辆信息详情*/
        templateUrl: 'pages/template/car/carInfo.html?_v='+LazyLoadConfig.VERSION,
        controller: 'carDetailCtrl',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.CAR_DETAIL)
    }).when('/searchCarContractList', {
        /*汽车金融=》合同管理=》合同列表*/
        templateUrl: 'pages/template/car/carContractList.html?_v='+LazyLoadConfig.VERSION
    }).when('/addCarContract',{
        /*汽车金融=》合同管理=》添加合同*/
        templateUrl: 'pages/template/car/addCarContract.html?_v='+LazyLoadConfig.VERSION,
        controller: 'carContractCtrl',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.CAR_CONTRACT)
    }).when('/carContractInfo',{
        /*汽车金融=》合同管理=》查看合同详情*/
        templateUrl: 'pages/template/car/carContractInfo.html?_v='+LazyLoadConfig.VERSION,
        controller: 'carContractDetailCtrl',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.CAR_CONTRACT_DETAIL)
    }).when('/toPrintCarContract',{
        /*汽车金融=》合同管理=》打印合同*/
        templateUrl: 'pages/template/car/printCarContract.html?_v='+LazyLoadConfig.VERSION,
        controller: 'printCarContractCtrl',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.CAR_CONTRACT_PRINT)
    }).when('/carInfoImport', {
        /*车辆信息批量导入*/
        templateUrl: 'pages/template/car/carInfoImport.html?_v='+LazyLoadConfig.VERSION,
        controller: 'carInfoImportCtrl',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.CAR_INFO_IMPORT)
    });

    /** 黑白名单管理(功能权限名单&用工关系名单&意向金名单) **/
    routeProvider.when('/rosInfoList', {
        /**用户功能权限黑白名单*/
        templateUrl: 'pages/template/ros/rosInfoList.html?_v='+LazyLoadConfig.VERSION
    }).when('/addRosInfo', {
        /**添加用户功能权限黑白名单*/
        templateUrl: 'pages/template/ros/addRosInfo.html?_v='+LazyLoadConfig.VERSION,
        controller: "rosInfoController",
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.LOAD_JS_ROSINFO)
    }).when('/rosStaffInfoList', {
        /**企业员工关系名单*/
        templateUrl: 'pages/template/ros/rosStaffInfoList.html?_v='+LazyLoadConfig.VERSION
    }).when('/addRosStaffInfo', {
        /**添加用企业员工关系名单*/
        templateUrl: 'pages/template/ros/addRosStaffInfo.html?_v='+LazyLoadConfig.VERSION,
        controller: "rosStaffInfoController",
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.LOAD_JS_ROS_STAFF_INFO)
    }).when('/rosDepositInfoList', {
        /**购房宝&物业宝意向金名单*/
        templateUrl: 'pages/template/ros/rosDepositInfoList.html?_v='+LazyLoadConfig.VERSION
    }).when('/addRosDepositInfo', {
        /**添加购房宝&物业宝意向金名单*/
        templateUrl: 'pages/template/ros/addRosDepositInfo.html?_v='+LazyLoadConfig.VERSION,
        controller: "rosDepositInfoController",
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.LOAD_JS_ROS_DEPOSIT_INFO)
    }).when('/sysFunctionCfgList', {
        /**用户功能权限黑白名单*/
        templateUrl: 'pages/template/ros/sysFunctionCfgList.html?_v='+LazyLoadConfig.VERSION
    });

    routeProvider.when('/rosNoticeList', {
        /**功能邮件or短信通知管理*/
        templateUrl: 'pages/template/ros/rosNoticeList.html?_v='+LazyLoadConfig.VERSION
    }).when('/addRosNotice', {
        /**添加功能邮件or短信通知*/
        templateUrl: 'pages/template/ros/addRosNotice.html?_v='+LazyLoadConfig.VERSION,
        controller: "rosNoticeController",
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.LOAD_JS_ROSNOTICE)
    })

    /** 支付路由管理 **/
    routeProvider.when('/paymentChannelList', {
        /*支付模式设置*/
        templateUrl: 'pages/template/payment/paymentChannelList.html?_v='+LazyLoadConfig.VERSION
    }).when('/addPayChannel', {
        /*添加、修改支付模式*/
        templateUrl: 'pages/template/payment/addpaychannel.html?_v='+LazyLoadConfig.VERSION,
        controller: 'payChannelCtrl',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.LOAD_JS_PAYMENT_PAYCHANNELDETAIL)
    }).when('/paymentFundtransferList', {
        templateUrl: 'pages/template/payment/fundtransferRecordList.html?_v='+LazyLoadConfig.VERSION
    }).when('/payBankList', {
        templateUrl: 'pages/template/payment/payBankList.html?_v='+LazyLoadConfig.VERSION
    }).when('/updatePayBank', {
        templateUrl: 'pages/template/payment/updatePayBank.html?_v='+LazyLoadConfig.VERSION,
        controller: 'payBankReferCtrl',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.LOAD_JS_PAYMENT_BANKREFER)
    }).when('/findBankCardUpdateList', {
        /**解绑银行卡信息查询*/
        templateUrl: 'pages/template/payment/bankCardUpdateList.html?_v='+LazyLoadConfig.VERSION
    }).when('/bankCardUpdateAudit', {
        templateUrl: 'pages/template/payment/bankCardUpdateAudit.html?_v='+LazyLoadConfig.VERSION,
        controller: 'bankCardUpdateAuditCtrl',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.LOAD_JS_BANK_CARD_UPDATE_AUDIT),
    }).when('/bankCardUpdateDetail', {
        templateUrl: 'pages/template/payment/bankCardUpdateDetail.html?_v='+LazyLoadConfig.VERSION,
        controller: 'bankCardUpdateAuditCtrl',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.LOAD_JS_BANK_CARD_UPDATE_AUDIT)
    });

    /** 好友推荐管理 **/
    routeProvider.when('/recommendEarnRuleList', {
        /*好友推荐管理规则设置列表*/
        templateUrl: 'pages/template/recommend/recommendEarnRuleList.html?_v='+LazyLoadConfig.VERSION
    }).when('/addRecommendEarnRule', {
        /*好友推荐管理规则设置添加*/
        templateUrl: 'pages/template/recommend/addRecommendEarnRule.html?_v='+LazyLoadConfig.VERSION,
        controller: 'recommendRuleCtrl',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.RECOMMEND_JS_RULE)
    }).when('/recommendEarnList', {
        /*好友推荐管理佣金发放*/
        templateUrl: 'pages/template/recommend/recommendEarnList.html?_v='+LazyLoadConfig.VERSION,
    }).when('/recommendEarnAuditingList', {
        /*好友推荐管理佣金审核*/
        templateUrl: 'pages/template/recommend/recommendEarnAuditingList.html?_v='+LazyLoadConfig.VERSION,
    }).when('/recommendList', {
        /*好友推荐管理佣金查询*/
        templateUrl: 'pages/template/recommend/recommendList.html?_v='+LazyLoadConfig.VERSION,
    }).when('/recommendInfoDetail', {
        /*好友推荐管理佣金查询*/
        templateUrl: 'pages/template/recommend/bidRecommendEarnInfoDetail.html?_v='+LazyLoadConfig.VERSION,
    }).when('/propertyAchievementList', {
        /*好友推荐管理物业业绩查询*/
        templateUrl: 'pages/template/recommend/propertyAchievementList.html?_v='+LazyLoadConfig.VERSION,
    }).when('/spreadSourceList', {
        /*好友推荐管理推广来源查询*/
        templateUrl: 'pages/template/recommend/spreadSourceList.html?_v='+LazyLoadConfig.VERSION,
    }).when('/updateSpreadSource', {
        /*好友推荐管理推广来源更新*/
        templateUrl: 'pages/template/recommend/updateSpreadSource.html?_v='+LazyLoadConfig.VERSION,
        controller: 'updateSpreadCtrl',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.SPREAD_JS_RULE)
    });
    


    /** 消息管理 **/
    routeProvider.when('/sendTelMsgList', {
        /*发送短信通知*/
        templateUrl: 'pages/template/sms/sendTelMsgList.html?_v='+LazyLoadConfig.VERSION
    }).when('/sendTelMsgBatch', {
        /*一键发送*/
        templateUrl: 'pages/template/sms/sendTelMsgBatch.html?_v='+LazyLoadConfig.VERSION
    }).when('/authCodeTelMsgList', {
        /*验证码消息检索*/
        templateUrl: 'pages/template/sms/authCodeTelMsgList.html?_v='+LazyLoadConfig.VERSION
    }).when('/webMsgList', {
        /*站内信消息检索*/
        templateUrl: 'pages/template/sms/webMsgList.html?_v='+LazyLoadConfig.VERSION
    }).when('/appMsgPushList', {
        /*app端消息推送检索*/
        templateUrl: 'pages/template/sms/appMsgPushList.html?_v='+LazyLoadConfig.VERSION
    }).when('/addAppMsgPush', {
        /*添加app端消息推送*/
        templateUrl: 'pages/template/sms/addAppMsgPush.html?_v='+LazyLoadConfig.VERSION,
        controller: 'appMsgPushCtr',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.SMS_JS_APP_MSG_PUSH)
    }).when('/telMsgManageList', {
        /*手机短信消息检索*/
        templateUrl: 'pages/template/sms/telMsgManageList.html?_v='+LazyLoadConfig.VERSION
    });


    /** 物业管理 **/
    routeProvider.when('/auditProPayRecordList', {
        /*物业缴费待审核列表*/
        templateUrl: 'pages/template/propertyPayment/auditProPayRecordList.html?_v='+LazyLoadConfig.VERSION
    });
    /**会员等级管理**/
    routeProvider.when('/vipGradeStandardList', {
        /*会员等级标准列表*/
        templateUrl: 'pages/template/vas/vipGradeStandardList.html?_v='+LazyLoadConfig.VERSION
    }).when('/addVipGradeStandard', {
        /*添加或修改会员等级标准*/
        templateUrl: 'pages/template/vas/addVipGradeStandard.html?_v='+LazyLoadConfig.VERSION,
        controller: 'vipGradeStandardCtrl',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.LOAD_JS_VIP_GRADE_STANDARD)
    }).when('/vipGrowRuleList', {
        /*会员成长值规则列表*/
        templateUrl: 'pages/template/vas/vipGrowRuleList.html?_v='+LazyLoadConfig.VERSION
    }).when('/addVipGrowRule', {
        /*添加或修改会员成长值规则*/
        templateUrl: 'pages/template/vas/addVipGrowRule.html?_v='+LazyLoadConfig.VERSION,
        controller: 'vipGrowRuleCtrl',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.LOAD_JS_VIP_GROW_RULE)
    }).when('/vipTreatmentList', {
        /*会员待遇列表*/
        templateUrl: 'pages/template/vas/vipTreatmentList.html?_v='+LazyLoadConfig.VERSION
    }).when('/addVipTreatment', {
        /*添加或修改会员待遇*/
        templateUrl: 'pages/template/vas/addVipTreatment.html?_v='+LazyLoadConfig.VERSION,
        controller: 'vipTreatmentCtrl',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.LOAD_JS_VIP_TREATMENT)
    }).when('/vipGrowRecordList', {
        /*查询会员成长记录列表*/
        templateUrl: 'pages/template/vas/vipGrowRecordList.html?_v='+LazyLoadConfig.VERSION
    }).when('/userGrowRecordDetail', {
        /*查询用户成长记录详情列表*/
        templateUrl: 'pages/template/vas/userGrowRecordDetail.html?_v='+LazyLoadConfig.VERSION
    });
    /**财务管理路由**/
    routeProvider.when('/searchWithdrawalsLoan', {
        /*提现放款审核*/
        templateUrl: 'pages/template/payment/withdrawalsListLoan.html?_v='+LazyLoadConfig.VERSION
    }).when('/searchWithdrawalsAudit', {
        /*提现审核*/
        templateUrl: 'pages/template/payment/withdrawalsListAudit.html?_v='+LazyLoadConfig.VERSION
    }).when('/toWithdrawsAudit', {
        /*跳转到提现审核页面*/
        templateUrl: 'pages/template/payment/withdrawalsAudit.html?_v='+LazyLoadConfig.VERSION,
        controller: 'withdrawsAuditCtrl',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.LOAD_JS_PAYMENT_WITHDRAWALSAUDIT)
    }).when('/toWithdrawsLoan', {
        /*跳转到提现放款审核页面*/
        templateUrl: 'pages/template/payment/withdrawalsLoan.html?_v='+LazyLoadConfig.VERSION,
        controller: 'withdrawsLoanCtrl',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.LOAD_JS_PAYMENT_WITHDRAWALSLOAN)
    });
    /**卡券管理路由**/
    routeProvider.when('/couponProductList', {
        /*卡券产品列表*/
        templateUrl: 'pages/template/vas/coupon/couponProductList.html?_v='+LazyLoadConfig.VERSION
    }).when('/addCouponProduct', {
        /*添加获取修改卡券产品*/
        templateUrl: 'pages/template/vas/coupon/addCouponProduct.html?_v='+LazyLoadConfig.VERSION,
        controller: 'couponProductCtrl',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.COUPON_PRODUCT)
    }).when('/giveCouponProductList', {
        /*查询可赠送卡券列表*/
        templateUrl: 'pages/template/vas/coupon/giveCouponProductList.html?_v='+LazyLoadConfig.VERSION,
        controller: 'couponProductCtrl',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.COUPON_PRODUCT)
    }).when('/couponDetailList', {
        /*查询卡券详情列表*/
        templateUrl: 'pages/template/vas/coupon/couponDetailList.html?_v='+LazyLoadConfig.VERSION
    }).when('/couponDonationRecordList', {
        /*查看卡券转赠记录列表*/
        templateUrl: 'pages/template/vas/coupon/couponDonationRecordList.html?_v='+LazyLoadConfig.VERSION
    }).when('/generateCouponDetailList', {
        /*生成卡券详情数据列表*/
        templateUrl: 'pages/template/vas/coupon/generateCouponDetailList.html?_v='+LazyLoadConfig.VERSION,
        controller: 'couponDetailCtrl',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.COUPON_DETAIL)
    });

    /** 监控管理 **/
    routeProvider.when('/jmsFailMsgList', {
        /*JMS异常消息列表*/
        templateUrl: 'pages/template/monitor/jmsFailMsgList.html?_v='+LazyLoadConfig.VERSION
    }).when('/actionLogList', {
        /*JMS异常消息列表*/
        templateUrl: 'pages/template/monitor/actionLogList.html?_v='+LazyLoadConfig.VERSION
    });

    /** app版本规则管理 **/
    routeProvider.when('/appVersionRuleList', {
        /*app版本更新规则列表*/
        templateUrl: 'pages/template/vas/appVersionRuleList.html?_v='+LazyLoadConfig.VERSION
    }).when('/addAppVersionRule', {
        /*添加app版本更新规则*/
        templateUrl: 'pages/template/vas/addAppVersionRule.html?_v='+LazyLoadConfig.VERSION,
        controller: 'appVersionRuleCtrl',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.APP_VERSION_RULE)
    });

    /** 股权中心 **/
    routeProvider.when('/fundTypeList',{
        /* 股权类型列表 */
        templateUrl:'pages/template/fund/fundProjectList.html?_v='+LazyLoadConfig.VERSION
    }).when('/fundProjectInfo',{
        /* 股权类型详情 */
        templateUrl:'pages/template/fund/fundProjectInfo.html?_v='+LazyLoadConfig.VERSION,
        controller: 'fundProjectCtrl',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.FUND_PROJECT)
    }).when('/fundAdvisoryList',{
        /* 股权咨询列表 */
        templateUrl:'pages/template/fund/fundAdvisoryList.html?_v='+LazyLoadConfig.VERSION
    }).when('/fundAdvisoryInfo',{
        /* 股权咨询详情 */
        templateUrl:'pages/template/fund/fundAdvisoryInfo.html?_v='+LazyLoadConfig.VERSION,
        controller: 'fundAdvisoryDetailCtrl',
        resolve:ocLazyLoadJs(LazyLoadConfig.CONSTANTS.FUND_ADVISORY_DETAIL)
    }).when('/fundAdvisoryEdit', {
        /* 添加或者修改股权信息*/
        templateUrl: 'pages/template/fund/fundAdvisoryEdit.html?_v='+LazyLoadConfig.VERSION,
        controller: 'fundAdvisoryCtrl',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.FUND_ADVISORY)
    }).when('/searchFundInfoList',{
        templateUrl:'pages/template/fund/fundInfoList.html?_v='+LazyLoadConfig.VERSION
    }).when('/addFundInfo', {
        /*添加、更新股权项目信息*/
        templateUrl: 'pages/template/fund/addFundInfo.html?_v='+LazyLoadConfig.VERSION,
        controller: 'fundInfoCtrl',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.FUND_INFO)
    }).when('/fundEvaluationList', {
        /*风险测评答案信息*/
        templateUrl: 'pages/template/fund/fundEvaluationList.html?_v='+LazyLoadConfig.VERSION
    }).when('/fundInfo', {
    	/* 股权项目详情 */
    	templateUrl: 'pages/template/fund/fundInfo.html?_v='+LazyLoadConfig.VERSION,
    	controller: 'fundDetailCtrl',
    	resolve:ocLazyLoadJs(LazyLoadConfig.CONSTANTS.FUND_DETAIL)
    }).when('/fundEvaluationDetail', {
        /* 风险测评答案详情 */
        templateUrl: 'pages/template/fund/fundEvaluationDetail.html?_v='+LazyLoadConfig.VERSION,
        controller: 'fundEvaluationDetailCtrl',
        resolve:ocLazyLoadJs(LazyLoadConfig.CONSTANTS.FUND_EVALUATION_DETAIL)
    });

    /** 统计管理路由*/
    routeProvider.when('/staIncomeList', {
        /*app版本更新规则列表*/
        templateUrl: 'pages/template/bi/staIncomeList.html?_v='+LazyLoadConfig.VERSION
    }).when('/balanceList',{
        templateUrl: 'pages/template/bi/balanceList.html?_v='+LazyLoadConfig.VERSION
    }).when('/recommendEarnCountList',{
        templateUrl: 'pages/template/bi/recommendEarnCountList.html?_v='+LazyLoadConfig.VERSION
    }).when('/staRepayPlanList',{
        /*还款计划*/
        templateUrl: 'pages/template/bi/staRepayPlanList.html?_v='+LazyLoadConfig.VERSION
    }).when('/staReceiptPlanList',{
        /*回款计划*/
        templateUrl: 'pages/template/bi/staReceiptPlanList.html?_v='+LazyLoadConfig.VERSION
    }).when('/staReceiptPlanInfos',{
        /*查看回款计划详情*/
        templateUrl: 'pages/template/bi/staReceiptPlanInfos.html?_v='+LazyLoadConfig.VERSION
    }).when('/paymentRecordCountList',{
        templateUrl: 'pages/template/bi/paymentRecordCountList.html?_v='+LazyLoadConfig.VERSION
    });
    /** 购房宝管理路由*/
    routeProvider.when('/purchaseCustomerList', {
        /*购房宝管理-客户信息列表*/
        templateUrl: 'pages/template/bidInfo/purchase/purchaseCustomerList.html?_v='+LazyLoadConfig.VERSION
    }).when('/addPurchaseCustomer',{
        templateUrl: 'pages/template/bidInfo/purchase/addPurchaseCustomer.html?_v='+LazyLoadConfig.VERSION,
        controller: 'purchaseCustomerCtrl',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.PURCHASE_COSTOMER)
    });

    /** 活动管理路由*/
    routeProvider.when('/lotteryActivityList', {
        /*抽奖活动管理*/
        templateUrl: 'pages/template/activity/lotteryActivityList.html?_v='+LazyLoadConfig.VERSION
    }).when('/lotteryActivityEdit', {
        /*抽奖活动添加及修改*/
        templateUrl: 'pages/template/activity/lotteryActivityEdit.html?_v='+LazyLoadConfig.VERSION,
        controller: 'lotteryActivityController',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.LOTTERY_ACTIVITY)
    }).when('/lotteryItemsEdit', {
        /*抽奖活动奖项添加或修改*/
        templateUrl: 'pages/template/activity/lotteryItemsEdit.html?_v='+LazyLoadConfig.VERSION
    }).when('/lotteryRecordList', {
        /*抽奖记录查询*/
        templateUrl: 'pages/template/activity/lotteryRecordList.html?_v='+LazyLoadConfig.VERSION
    });

    /** 房产信息管理路由*/
    routeProvider.when('/houseList',{
        /*房产信息管理*/
        templateUrl: 'pages/template/house/houseList.html?_v' + LazyLoadConfig.VERSION
    }).when('/addHouse',{
        /*添加房产信息*/
        templateUrl: 'pages/template/house/addHouse.html?_v' + LazyLoadConfig.VERSION,
        controller: 'houseCtrl',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.HOUSE)
    }).when('/editHouse',{
        /*编辑房产信息*/
        templateUrl: 'pages/template/house/editHouse.html?_v' + LazyLoadConfig.VERSION,
        controller: 'houseCtrl',
        resolve: ocLazyLoadJs(LazyLoadConfig.CONSTANTS.HOUSE)
    });

});

/**
 * 动态加载js
 * param 文件地址，可以为字符串/数组
 */
function ocLazyLoadJs(param) {
    return {
        load: ["$ocLazyLoad", function ($ocLazyLoad) {
            return $ocLazyLoad.load(param);
        }]
    };
}

