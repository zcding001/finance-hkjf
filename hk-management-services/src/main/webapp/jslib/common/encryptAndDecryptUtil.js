/**加密解密工具类**/
var encryptAndDecryptUtil = {};
(function () {
    'use strict';
    /**
     * 对内容进行加密操作
     * @param content
     * @returns
     */
    encryptAndDecryptUtil.encrypt = function (content) {
        var c = String.fromCharCode(content.charCodeAt(0) + content.length);
        for (var i = 1; i < content.length; i++) {
            c += String.fromCharCode(content.charCodeAt(i) + content.charCodeAt(i - 1));
        }
        return escape(c);
    };
    /**
     * 对内容进行解密操作
     * @param content
     * @returns
     */
    encryptAndDecryptUtil.decrypt = function (content) {
        content = unescape(content);
        var c = String.fromCharCode(content.charCodeAt(0) - content.length);
        for (var i = 1; i < content.length; i++) {
            c += String.fromCharCode(content.charCodeAt(i) - c.charCodeAt(i - 1));
        }
        return c;
    };
}())