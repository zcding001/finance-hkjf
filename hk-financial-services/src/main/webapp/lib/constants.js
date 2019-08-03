/**
 * Created by dzc on 17.11.23.
 * 常量类
 */

var CONSTANTS = {};
(function(){
    /**成功标识*/
    CONSTANTS.SUCCESS = 1000;
    /**用户未实名*/
    CONSTANTS.NO_IDENTIFY = 2001;
    /**登录超时*/
    CONSTANTS.SESSION_TIME_OUT = 2003;
    /**防重复提交token*/
    CONSTANTS.SUBMIT_TOKEN = "submitToken";

    /**
     * 项目根路径 在html使用${basePath},只有在被引用的面中才能使用，
     *  eg  a.html中通过<div class="include" file="${basePath}/b.html"></div>
     *      而且b.html中引入的静态资源、为防止出现404都可以加上${basePath} 如上面${basePath}/xxx.jpg
     */
    CONSTANTS.DEFAULT_BASE_PATH = "DEFAULT_BASE_PATH";

    /**登录票据*/
    CONSTANTS.SSO_TICKET = "ticket";
    /**SSO登录状态-失败同步次数*/
    CONSTANTS.SSO_STATE_RETRY_TIMES = 2;
    /**SSO登录状态-在线*/
    CONSTANTS.SSO_STATE_ONLINE = "online";
    /**SSO登录状态-离线*/
    CONSTANTS.SSO_STATE_OFFLINE = "offline";

    /**浏览器容器开启标识*/
    CONSTANTS.CONTAINER_FLAG = "container_flag";
    CONSTANTS.CONTAINER_DEFAULT_KEY = "_container";
    /**取完值后需要删除的容器*/
    CONSTANTS.CONTAINER_TRANSIENT_KEY = "_transient";
    /**我的账户的子菜单的uri地址*/
    CONSTANTS.CONTAINER_TRANSIENT_MENU_URI_KEY = "_transient_menu_uri";


    /**上次登录时间*/
    CONSTANTS.LOGIN_LAST_TIME = "last_login_time";
    /**登录用户*/
    CONSTANTS.LOGIN_REG_USER = "login_reg_user";
    /**登录用的账户*/
    CONSTANTS.LOGIN_FIN_ACCOUNT = "login_fin_account";
    /**密码强度*/
    CONSTANTS.PWD_LEVEL = "pwd_level";
    /**定期投资可见标识*/
    CONSTANTS.ACC_INVEST = "acc_invest";
    /**海外投资标识*/
    CONSTANTS.OVERSEA_INVEST = "oversea_invest";
    /**房产投资标识*/
    CONSTANTS.HOUSE_INVEST = "house_invest";
    /**注册定向投资者测评答案**/
    CONSTANTS.INVEST_ANSWER = "invest_answer";

    /**默认数据表格的ID*/
    CONSTANTS.DEFAULT_TABLE_ID = "dataTable";

    /**短信验证码默认超时时间*/
    CONSTANTS.TIME_OUT = 60;
    /**找回密码是需要存储的手机号*/
    CONSTANTS.FIND_PASSWD_LOGIN = "find_passwd_login";
    /**找回密码是需要存储短信验证码*/
    CONSTANTS.FIND_PASSWD_SMSCODE = "find_passwd_smsCode";

    /**进入投资业所需要的存储的标的ID*/
    CONSTANTS.TO_INVEST_BID_ID = "toInvestBidId";

    /**项目根路径，有项目名称的时候必填,格式：http://127.0.0.1:9090/hkjf*/
    CONSTANTS.BASE_PATH ="${project_base_path}";

    /** OSS存储路径 **/
    CONSTANTS.IMG_URL_ROOT  = "${oss.url}";

    /** 定向投资者测评默认勾选答案 AAACDC */
    CONSTANTS.DEFAULT_ANSWER = "0,0,0,2,3,2";

})();