/**
 * Created by dzc on 17.11.23.
 * JS定义及使用规范
 *  1：驼峰是命名
 *  2：私有方法以“_”开头，在===private method ===区域之后扩展
 *	3：共有方法以demo.函数名定义，========== public method ============之前扩展共有方法
 *  4：util内部使用的常量在闭包内定义，公用的常量在constants.js中定义
 *  5：具体代表性操作单独抽取为一个独立了的js工具类，如果validUtil.js、ssoUtil.js、rasUtil.js等。
 *  6：lib存放公用js，jslib文件夹存放与html对应的业务操作的js
 *  7: jslib中业务js也建议使用闭包进行定义，业务层定义建议以xxxController或xxxCtl结尾，示例见：loginReg.js
 *
 *  PS:如果遗留自行补充，及时通知！
 */

/**
 * 工具的用途
 * @type {{}}
 */
var demo = {}
(function(){
    'use strict';
    /**
     *
     */
    demo.publicMethod = function(){
        console.info("公共方法")
    }
    //========== public method ============

    // ========= private method ============
    var PRIVATE_CONSTANTS = "私有常量";
    /**
     * 私有方法定义实例
     * @private
     */
    function _privateMethod(){
        console.info("私有方法")
    }
})();