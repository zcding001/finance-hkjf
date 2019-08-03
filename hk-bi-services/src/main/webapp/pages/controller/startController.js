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

    /** 还款计划、回款计划路由 **/
    routeProvider.when('/repayPlanList', {
        /*还款计划路由*/
        templateUrl: 'pages/template/bidInfo/plan/repayPlanList.html?_v='+LazyLoadConfig.VERSION
    }).when('/receiptPlanList', {
        /*回款计划路由*/
        templateUrl: 'pages/template/bidInfo/plan/receiptPlanList.html?_v='+LazyLoadConfig.VERSION
    });

   
   
    
    

    

    
    

    

    /** 统计管理路由*/
    routeProvider.when('/dashboard',{
        templateUrl: 'pages/template/dashboard/dashboard.html?_v='+LazyLoadConfig.VERSION
    }).when('/staIncomeList', {
        /*收入统计*/
        templateUrl: 'pages/template/bi/staIncomeList.html?_v='+LazyLoadConfig.VERSION
    }).when('/balanceList',{
        templateUrl: 'pages/template/bi/balanceList.html?_v='+LazyLoadConfig.VERSION
    }).when('/recommendEarnCountList',{
        /*投资奖励*/
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
    }).when('/paymentCountList',{
    	/*充值提现统计*/
        templateUrl: 'pages/template/bi/paymentCountList.html?_v='+LazyLoadConfig.VERSION
    }).when('/recommendEarnSendCountList',{
    	 /*推荐奖发放统计*/
        templateUrl: 'pages/template/bi/recommendEarnSendCountList.html?_v='+LazyLoadConfig.VERSION
    }).when('/qdzTransRecordCountList',{
    	 /*钱袋子统计*/
        templateUrl: 'pages/template/bi/qdzTransRecordCountList.html?_v='+LazyLoadConfig.VERSION
    }).when('/staInvite',{
        /*邀请统计*/
        templateUrl: 'pages/template/bi/staInvite.html?_v='+LazyLoadConfig.VERSION
    }).when('/staUserCvr',{
        /*转化率*/
        templateUrl: 'pages/template/bi/staUserCrv.html?_v='+LazyLoadConfig.VERSION
    }).when('/staInvestDis',{
        /*新老用户投资分布*/
        templateUrl: 'pages/template/bi/staInvestDis.html?_v='+LazyLoadConfig.VERSION
    }).when('/staFunProDis',{
        /*标的募集统计*/
        templateUrl: 'pages/template/bi/staFunProDis.html?_v='+LazyLoadConfig.VERSION
    }).when('/staFunInvest',{
        /*客户投资信息查询*/
        templateUrl: 'pages/template/bi/staFunInvest.html?_v='+LazyLoadConfig.VERSION
    }).when('/staFunBid',{
        /*标的信息查询*/
        templateUrl: 'pages/template/bi/staFunBid.html?_v='+LazyLoadConfig.VERSION
    }).when('/offLinePacketCountList',{
        /*红包统计*/
        templateUrl: 'pages/template/bi/offLinePacketCountList.html?_v='+LazyLoadConfig.VERSION
    }).when('/pointPayCountList',{
        /*积分消费统计*/
        templateUrl: 'pages/template/bi/pointPayCountList.html?_v='+LazyLoadConfig.VERSION
    }).when('/staFunPlatformAddup',{
        /*平台运营*/
        templateUrl: 'pages/template/bi/staFunPlatformAddup.html?_v='+LazyLoadConfig.VERSION
    }).when('/platformDataCountList',{
        /*平台数据统计*/
        templateUrl: 'pages/template/bi/platformDataCountList.html?_v='+LazyLoadConfig.VERSION
    }).when('/transferCountList',{
        /*资金流动统计*/
        templateUrl: 'pages/template/bi/transferCountList.html?_v='+LazyLoadConfig.VERSION
    }).when('/staCouponList',{
        /*卡券统计*/
        templateUrl: 'pages/template/bi/staCouponList.html?_v='+LazyLoadConfig.VERSION
    }).when('/staCouponProfitList',{
        /*卡券收益统计*/
        templateUrl: 'pages/template/bi/staCouponProfitList.html?_v='+LazyLoadConfig.VERSION
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

