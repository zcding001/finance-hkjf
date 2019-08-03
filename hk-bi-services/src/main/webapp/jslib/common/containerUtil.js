/**
 * Created by dzc on 17.11.27.
 * 容器工具类
 */

var containerUtil = {};
(function(){
    //容器缓存
    var _container = {};
    /**
     * 初始化浏览器容器
     */
    containerUtil.initContainer = function(){
        /*关闭浏览器，重新打开网站时，清空localStorage*/
        if (!cookieUtil.getCookie(CONFIG.CONSTANTS.CONTAINER_FLAG)){
            window.localStorage.clear();
            cookieUtil.setCookieWithoutExpires(CONFIG.CONSTANTS.CONTAINER_FLAG, CONFIG.CONSTANTS.CONTAINER_FLAG);
        }
        if (window.localStorage.getItem("_container")){
            _container = JSON.parse(window.localStorage.getItem("_container"));
        }else {
            window.localStorage.setItem("_container",JSON.stringify(_container));
        }
    };

    /**
     * 像容器中添加内容
     * @param key
     * @param value
     */
    containerUtil.set = function(key, value){
        _container[key] = value;
        window.localStorage.setItem("_container",JSON.stringify(_container));
    };
    /**
     * 从元素中获得key对应的值
     * @param key
     * @returns {*}
     */
    containerUtil.get = function(key){
        return _container[key];
    };
    /**
     * 删除容器中的key值
     * @param key
     */
    containerUtil.delete = function(key){
        // debugger;
        delete _container[key];
        window.localStorage.setItem("_container",JSON.stringify(_container));
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
