/**
 * 字符串工具类
 * @returns {String}
 */
var stringUtil = {};
(function () {
    //var str = "js实现用{two}自符串替换占位符{two} {three}  {one} ".format({one: "I",two: "LOVE",three: "YOU"});
    //var str2 = "js实现用{1}自符串替换占位符{1} {2}  {0} ".format("I","LOVE","YOU");
    String.prototype.format = function () {
        if (arguments.length == 0) return this;
        var param = arguments[0];
        var s = this;
        if (typeof(param) == 'object') {
            for (var key in param)
                s = s.replace(new RegExp("\\{" + key + "\\}", "g"), param[key]);
            return s;
        } else {
            for (var i = 0; i < arguments.length; i++)
                s = s.replace(new RegExp("\\{" + i + "\\}", "g"), arguments[i]);
            return s;
        }
    }
}());