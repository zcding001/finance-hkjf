/**
 * Created by dzc on 17.11.23.
 * 验证工具类
 */

var validUtil = {};
(function(){
    'use strict';
    var _telReg = /^0?1[3-5|7-8][0-9]\d{8}$/;

    var _passwordReg = new RegExp(/^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$/);
    /**
     * 验证非空数据
     * @param val
     * @param errId 用于显示错误信息的id
     * @param errMsg 错误提示信息
     * @returns {boolean}
     */
    validUtil.validNotEmpty = function(val, errId, errMsg){
        _clearErrMsg(errId);
        var isEmpty = _empty(val);
        if(isEmpty){
            _setErrMsg(!isEmpty, errId, errMsg);
        }
        return !isEmpty;
    };

    /**
     * 验证手机号正确格式
     * @param tel 手机号
     * @param errId 用于显示错误信息的id
     * @param errMsg 错误提示信息
     * @returns {boolean}
     */
    validUtil.validTel = function(tel, errId, errMsg){
        _clearErrMsg(errId);
        if(_empty(tel)){
            _setErrMsg(false, errId, "请输入手机号");
            return false;
        }
        if (!_telReg.test(tel)){
            _setErrMsg(false, errId, errMsg);
            return false;
        }
        return true;
    };

    /**
     * 简单验证手机号格式
     * @param tel
     */
    validUtil.validTelFormat = function (tel) {
        return _telReg.test(tel);
    };

    /**
     * 简单验证用户登录密码格式
     * @param pass
     * @return {boolean}
     */
    validUtil.validPassFormat = function (pass) {
        return _passwordReg.test(pass);
    };
    /**
     *  验证邮箱的准确性
     * @param email 邮箱地址
     * @param errId 用于显示错误信息的id
     * @param errMsg 错误提示信息
     * @returns {boolean}
     */
    validUtil.validEmail = function(email, errId, errMsg){
        _clearErrMsg(errId);
        var valid = !_empty(email);
        if(valid && !/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/.test(email)){
            valid = false;
        }
        _setErrMsg(valid, errId, errMsg);
        return valid;
    };

    /**
     * 验证val有效性
     * @param val
     * @param errId 用于显示错误信息的id
     * @param errMsg 错误提示信息
     * @param type 1:字母数字组合 2：含有特殊字符 3：纯数字 4：纯字母  默认值：1
     * @param min 最小长度 默认值：6
     * @param max 最大长度 默认值：16
     * @returns {boolean}
     */
    validUtil.validData = function(val, errId, errMsg, type, min, max){
        _clearErrMsg(errId);
        var valid = true;
        if(valid && _empty(val)){
            valid = false;
        }
        if(_empty(min)){
            min  = 6;
        }
        if(_empty(max)){
            max = 16;
        }
        if(_empty(type)){
            type = 1;
        }
        // "请输入" + min + "至" + max + "为字母数字组合值"
        if(valid && ((val + "").length > max || (val + "").length < min)){
            valid = false;
        }
        if(type == 1 && !/^([a-zA-Z0-9]+)$/.test(val)){
            valid = false;
        }else if(type == 2 && !/^([a-zA-Z0-9~!@#$%^&*]+)$/.test(val)){
            valid = false;
        }else if(type == 3 && !/^([0-9]+)$/.test(val)){
            valid = false;
        }else if(type ==  4 && !/^([a-zA-Z]+)$/.test(val)){
            valid = false;
        }
        _setErrMsg(valid, errId, errMsg);
        return valid;
    };


    /**
     * 验证是否是数字
     */
    validUtil.validNumber = function(val){
    	return /^[0-9]*$/.test(val);
    }
    //========== public method ============

    // ========= private method ============

    /**
     * 验证value是否为空
     * @param val
     * @returns {boolean} 返回true代表val为空，非法的数据，false代表非空，标识有效的数据
     * @private
     */
    function _empty(val){
        if(val == undefined||val==null){
            return true;
        }
        val += '';
        return val == '' ;
    }
    /**
     * 设置验证失败后的提示信息
     * @param valid 验证结果 true 合法
     * @param errId 用于显示错误信息的id
     * @param errMsg 错误提示信息
     * @param defaultErrMsg 默认错误提示信息
     * @private
     */
    function _setErrMsg(valid, errId, errMsg, defaultErrMsg){
        if(!valid){
            if(errId != '' && errId != undefined){
                if(errMsg != '' && errMsg != undefined){
                    defaultErrMsg = errMsg;
                }
                $("#" + errId).text(defaultErrMsg);
            }
        }
    }

    /**
     * 清空错误提示信息
     * @param errId
     * @private
     */
    function _clearErrMsg(errId){
        if(errId != '' && errId != undefined){
            $("#" + errId).text("");
        }
    }
    
})();



