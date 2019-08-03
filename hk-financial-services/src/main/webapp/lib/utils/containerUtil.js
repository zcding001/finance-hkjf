/**
 * Created by dzc on 17.11.27.
 * 容器工具类
 */

var containerUtil = {};
(function(){
    'use strict';
    //容器缓存
    var _container = {};
    /**
     * 初始化浏览器容器
     */
    containerUtil.initContainer = function(){
        if (window.localStorage.getItem(CONSTANTS.CONTAINER_DEFAULT_KEY)){
            _container = JSON.parse(window.localStorage.getItem(CONSTANTS.CONTAINER_DEFAULT_KEY));
        }else {
            window.localStorage.setItem(CONSTANTS.CONTAINER_DEFAULT_KEY,JSON.stringify(_container));
        }
    };

    /**
     * 向容器中添加内容，存储在_container中
     * @param key
     * @param value
     */
    containerUtil.set = function(key, value){
        if(_container.length === undefined || _container.length <= 0){
            containerUtil.initContainer();
        }
        _container[key] = value;
        window.localStorage.setItem(CONSTANTS.CONTAINER_DEFAULT_KEY,JSON.stringify(_container));
    };

    /**
     * 从元素中获得key对应的值
     * @param key
     * @returns {*}
     */
    containerUtil.get = function(key){
        if(_container.length === undefined || _container.length <= 0){
            containerUtil.initContainer();
        }
        return _container[key];
    };

    /**
     * 删除容器中的key值
     * @param key
     */
    containerUtil.delete = function(key){
        delete _container[key];
        window.localStorage.setItem(CONSTANTS.CONTAINER_DEFAULT_KEY, JSON.stringify(_container));
        //更新config.js当前页面用户获取的缓存内容为最新
        // var js = document.createElement('script');
        // js.src='jslib/common/config.js?rnd=' + Math.random();
        // document.body.appendChild(js);
    };

    /**
     * 向浏览器缓存中存入key值
     * @param key
     * @param value
     */
    containerUtil.add = function(key, value){
        window.localStorage.setItem(key, JSON.stringify(value));
    };

    /**
     * 直接从浏览器中获得key对应的值
     * @param key
     */
    containerUtil.take = function(key){
        return JSON.parse(window.localStorage.getItem(key));
    };

    /**
     * 删除缓存中keys
     * @param keys 可以是key的集合数组
     */
    containerUtil.remove = function(keys){
        window.localStorage.removeItem(keys);
    };

    /**
     * 取出并删除缓存
     * @param key
     */
    containerUtil.getTransient = function(){
    	var obj =  containerUtil.take(CONSTANTS.CONTAINER_TRANSIENT_KEY);
    	containerUtil.remove(CONSTANTS.CONTAINER_TRANSIENT_KEY);
        return obj;
    };

    //========== public method ============
    // ========= private method ============

})();

$(function(){
    /*关闭浏览器，重新打开网站时，清空localStorage*/
    if (!cookieUtil.getCookie(CONSTANTS.CONTAINER_FLAG)){
        window.localStorage.setItem(CONSTANTS.CONTAINER_DEFAULT_KEY,JSON.stringify({}));
        cookieUtil.setCookieWithoutExpires(CONSTANTS.CONTAINER_FLAG, CONSTANTS.CONTAINER_FLAG);
    }
    containerUtil.initContainer();
});