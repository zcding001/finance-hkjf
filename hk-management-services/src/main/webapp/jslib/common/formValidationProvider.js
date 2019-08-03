/**
 * 表单校验--js
 */
//jquery post 提交数据是以 form-data 的形式提交的，而 AngularJs 以 json 格式提交的，所以后台获取不到。
app.config(["$httpProvider", function ($httpProvider) {
    //更改 Content-Type
    $httpProvider.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded;charset=utf-8';
    $httpProvider.defaults.headers.post['Accept'] = 'application/json, text/javascript, */*; q=0.01';
    $httpProvider.defaults.headers.post['X-Requested-With'] = 'XMLHttpRequest';
    /**
     * The workhorse; converts an object to x-www-form-urlencoded serialization.
     * @param {Object} obj
     * @return {String}
     */
    var param = function (obj) {
        var query = '', name, value, fullSubName, subName, subValue, innerObj, i;

        for (name in obj) {
            value = obj[name];

            if (value instanceof Array) {
                for (i = 0; i < value.length; ++i) {
                    subValue = value[i];
                    fullSubName = name + '[' + i + ']';
                    innerObj = {};
                    innerObj[fullSubName] = subValue;
                    query += param(innerObj) + '&';
                }
            }
            else if (value instanceof Object) {
                for (subName in value) {
                    subValue = value[subName];
                    fullSubName = name + '[' + subName + ']';
                    innerObj = {};
                    innerObj[fullSubName] = subValue;
                    query += param(innerObj) + '&';
                }
            }
            else if (value !== undefined && value !== null)
                query += encodeURIComponent(name) + '=' + encodeURIComponent(value) + '&';
        }

        return query.length ? query.substr(0, query.length - 1) : query;
    };
    // Override $http service's default transformRequest
    $httpProvider.defaults.transformRequest = [function (data) {
        return angular.isObject(data) && String(data) !== '[object File]' ? param(data) : data;
    }];
}]);
/**
 * @desc 公共表单校验类
 * @author yanbinghuang
 */
app.config(['$validationProvider', function ($validationProvider) {
    var defaultMsg;
    var expression;
    //IP格式校验
    $validationProvider.setDefaultMsg({
        ip: {
            error: 'IP格式非法!',
            success: ''
        }
    }).setExpression({
        ip: /^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/
    });
    //100个字符以内的汉字、英文字母或数字校验
    $validationProvider.setDefaultMsg({
        title: {
            error: '请输入100个字符以内的汉字、英文字母或数字!',
            success: ''
        }
    }).setExpression({
        title: /^[\u4e00-\u9fa5A-Za-z0-9,，（）()。！,.!--]{1,100}$/
    });
    //7个字符以内的汉字、英文字母或数字
    $validationProvider.setDefaultMsg({
        apptext: {
            error: '请输入7个字符以内的汉字、英文字母或数字!',
            success: ''
        }
    }).setExpression({
        apptext: /^[\u4e00-\u9fa5A-Za-z0-9,，（）()。！,.!--]{0,7}$/
    });
    //正整数校验
    $validationProvider.setDefaultMsg({
        integertext: {
            error: '请输入正整数！',
            success: ''
        }
    }).setExpression({
        integertext: /^[0-9]*[0-9][0-9]*$/
    });
    //正整数校验
    $validationProvider.setDefaultMsg({
        integertextIf: {
            error: '请输入正整数！',
            success: ''
        }
    }).setExpression({
        integertextIf:  function (value, scope, element, attrs, param) {

            if(commonUtil.isEmpty(value)){
                return true;
            }
            return /^[0-9]*[0-9][0-9]*$/.test(value)
        }
    });

    //数额(带金额性质的)最大值校验
    $validationProvider.setDefaultMsg({
        moneyMaxValue: {
            error: '金额超出最大范围！',
            success: ''
        }
    }).setExpression({
        moneyMaxValue: function (value, scope, element, attrs, param) {
            return value <= 999999999;
        }
    });


    //两位小数校验
    $validationProvider.setDefaultMsg({
        rate: {
            error: '请输入两位小数的数值',
            success: ''
        }
    }).setExpression({
        rate: /^(\d{1,2}|\d{1,2}\.\d{1,2})$/
    });

    //两位小数或者整数校验
    $validationProvider.setDefaultMsg({
        rateOrInteger: {
            error: '请输入整数或者至多两位小数',
            success: ''
        }
    }).setExpression({
        rateOrInteger: /^(\d{1,2}|\d{1,2}\.\d{1,2})$/
    });

    //4位小数或者整数校验
    $validationProvider.setDefaultMsg({
        rate4OrInteger: {
            error: '请输入整数或者至多四位小数',
            success: ''
        }
    }).setExpression({
        rate4OrInteger: function (value, scope, element, attrs, param) {
            return /^(\d{1,2}|\d{1,2}\.\d{1,4})$/.test(value)
        }
    });
    //4位小数或者整数校验
    $validationProvider.setDefaultMsg({
        rate4OrIntegerIf: {
            error: '请输入整数或者至多四位小数',
            success: ''
        }
    }).setExpression({
        rate4OrIntegerIf: function (value, scope, element, attrs, param) {
            if(commonUtil.isEmpty(value)){
                return true;
            }
            return /^(\d{1,2}|\d{1,2}\.\d{1,4})$/.test(value)
        }
    });

    //两位小数或者整数校验
    $validationProvider.setDefaultMsg({
        rateOrIntegerIf: {
            error: '请输入整数或者至多两位小数',
            success: ''
        }
    }).setExpression({
        rateOrIntegerIf: function (value, scope, element, attrs, param) {
            if(commonUtil.isEmpty(value)){
                return true;
            }
            return /^(\d{1,2}|\d{1,2}\.\d{1,2})$/.test(value)
        }
    });
    //长度验证
    $validationProvider.setDefaultMsg({
        lengtext: {
            error: '',
            success: ''
        }
    }).setExpression({
        lengtext: function (value, scope, element, attrs, param) {
            return value.length >= parseInt(attrs.min) && value.length <= parseInt(attrs.max);
        }
    });
    //min between max
    $validationProvider.setExpression({
        range: function (value, scope, element, attrs) {
            if (commonUtil.isNotEmpty(attrs.min) && commonUtil.isNotEmpty(attrs.max)) {
                if (value >= parseFloat(attrs.min) && value <= parseFloat(attrs.max)) {
                    return true;
                }
            } else {
                if (commonUtil.isNotEmpty(attrs.min)) {
                    return value >= parseFloat(attrs.min)
                }
                if (commonUtil.isNotEmpty(attrs.max)) {
                    return value <= parseFloat(attrs.max);
                }
                throw new Error("请定义该值的范围");
            }
        }
    }).setDefaultMsg({
        range: {
            error: '',
            success: ''
        }
    });

    //min between max
    $validationProvider.setExpression({
        rangeIf: function (value, scope, element, attrs) {
            if(commonUtil.isEmpty(value)){
                return true;
            }
            if (commonUtil.isNotEmpty(attrs.min) && commonUtil.isNotEmpty(attrs.max)) {
                if (value >= parseFloat(attrs.min) && value <= parseFloat(attrs.max)) {
                    return true;
                }
            } else {
                if (commonUtil.isNotEmpty(attrs.min)) {
                    return value >= parseFloat(attrs.min)
                }
                if (commonUtil.isNotEmpty(attrs.max)) {
                    return value <= parseFloat(attrs.max);
                }
                throw new Error("请定义该值的范围");
            }
        }
    }).setDefaultMsg({
        rangeIf: {
            error: '数值范围不合法!',
            success: ''
        }
    });

    //关键词以豆号分隔，至少5个字符
    $validationProvider.setDefaultMsg({
        keywords: {
            error: '',
            success: ''
        }
    }).setExpression({
        keywords: /((\s*[\u4e00-\u9fa5\w]+\s*[,，\s*]){4,})/
    });
    $validationProvider.setDefaultMsg({
        tel: {
            error: '请输入正确的手机号',
            success: ''
        }
    }).setExpression({
        tel: /^1[3|4|5|6|7|8][0-9]\d{4,8}$/
    });
    $validationProvider.setDefaultMsg({
        email: {
            error: '请输入正确的邮箱',
            success: ''
        }
    }).setExpression({
        email: /^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/
    });

    $validationProvider.setDefaultMsg({
        bankCard: {
            error: '请输入正确的银行卡号',
            success: ''
        }
    }).setExpression({
        bankCard: function (bankno) {
            var lastNum = bankno.substr(bankno.length - 1, 1); //取出最后一位（与luhn进行比较）
            var first15Num = bankno.substr(0, bankno.length - 1); //前15或18位
            var newArr = new Array();
            for (var i = first15Num.length - 1; i > -1; i--) { //前15或18位倒序存进数组
                newArr.push(first15Num.substr(i, 1));
            }
            var arrJiShu = new Array(); //奇数位*2的积 <9
            var arrJiShu2 = new Array(); //奇数位*2的积 >9
            var arrOuShu = new Array(); //偶数位数组
            for (var j = 0; j < newArr.length; j++) {
                if ((j + 1) % 2 == 1) { //奇数位
                    if (parseInt(newArr[j]) * 2 < 9) arrJiShu.push(parseInt(newArr[j]) * 2);
                    else arrJiShu2.push(parseInt(newArr[j]) * 2);
                } else //偶数位
                    arrOuShu.push(newArr[j]);
            }

            var jishu_child1 = new Array(); //奇数位*2 >9 的分割之后的数组个位数
            var jishu_child2 = new Array(); //奇数位*2 >9 的分割之后的数组十位数
            for (var h = 0; h < arrJiShu2.length; h++) {
                jishu_child1.push(parseInt(arrJiShu2[h]) % 10);
                jishu_child2.push(parseInt(arrJiShu2[h]) / 10);
            }

            var sumJiShu = 0; //奇数位*2 < 9 的数组之和
            var sumOuShu = 0; //偶数位数组之和
            var sumJiShuChild1 = 0; //奇数位*2 >9 的分割之后的数组个位数之和
            var sumJiShuChild2 = 0; //奇数位*2 >9 的分割之后的数组十位数之和
            var sumTotal = 0;
            for (var m = 0; m < arrJiShu.length; m++) {
                sumJiShu = sumJiShu + parseInt(arrJiShu[m]);
            }

            for (var n = 0; n < arrOuShu.length; n++) {
                sumOuShu = sumOuShu + parseInt(arrOuShu[n]);
            }

            for (var p = 0; p < jishu_child1.length; p++) {
                sumJiShuChild1 = sumJiShuChild1 + parseInt(jishu_child1[p]);
                sumJiShuChild2 = sumJiShuChild2 + parseInt(jishu_child2[p]);
            }
            //计算总和
            sumTotal = parseInt(sumJiShu) + parseInt(sumOuShu) + parseInt(sumJiShuChild1) + parseInt(sumJiShuChild2);

            //计算luhn值
            var k = parseInt(sumTotal) % 10 == 0 ? 10 : parseInt(sumTotal) % 10;
            var luhn = 10 - k;

            if (lastNum == luhn) {
                return true;
            }
            return false;
        }
    });
    //100的整数倍
    $validationProvider.setDefaultMsg({
        hundredMultiple: {
            error: '请输入100的整数倍',
            success: ''
        }
    }).setExpression({
        hundredMultiple: function (value, scope, element, attrs, param) {
            return value % 100 == 0;
        }
    });

    //100的整数倍
    $validationProvider.setDefaultMsg({
        minValueHundred: {
            error: '额度最小值为100',
            success: ''
        }
    }).setExpression({
        minValueHundred: function (value, scope, element, attrs, param) {
            return value >= 100;
        }
    });


    $validationProvider.setExpression({
        required: function (value, scope, element, attrs) {
            if(attrs['mustvalidate']==null||attrs['mustvalidate']==undefined){
                attrs['mustvalidate'] = 'true';
            }
            if(attrs['mustvalidate']==='true'){
                //避免值为0时返回false
                if (typeof value === "number") {
                    return true;
                }
                return !!value;
            }
            return true;

        }
    }).setDefaultMsg({
        required: {
            error: '此元素不能为空!',
            success: ''
        }
    })
    //判断是否是大于零的数
    $validationProvider.setExpression({
        overZero: function (value, scope, element, attrs) {
            //判断是否为数字
            if (!isNaN(value - 0)) {
                if ((value - 0) > 0) {
                    return true;
                }
            }
            return false;
        }
    }).setDefaultMsg({
        overZero: {
            error: '请输入大于零的数值！',
            success: ''
        }
    })
   //大于零校验
    $validationProvider.setDefaultMsg({
        overZeroIf: {
            error: '请输入大于零的数值！',
            success: ''
        }
    }).setExpression({
        overZeroIf:  function (value, scope, element, attrs, param) {

            if(commonUtil.isEmpty(value)){
                return true;
            }

            //判断是否为数字
            if (!isNaN(value - 0)) {
                if ((value - 0) > 0) {
                    return true;
                }
            }
            return false;
        }
    });
    /*********************mysql字段类型最大值对照表(带符号最大值)*****************************/
    //tinyint数字验证
    $validationProvider.setDefaultMsg({
        tinyint: {
            error: '数值不能大于127',
            success: ''
        }
    }).setExpression({
        smallint: function (value, scope, element, attrs, param) {
            return value >=0 && value <= 127;
        }
    });
    //smallint数字验证
    $validationProvider.setDefaultMsg({
        smallint: {
            error: '数值不能大于32767',
            success: ''
        }
    }).setExpression({
        smallint: function (value, scope, element, attrs, param) {
            return value >=0 && value <= 32767;
        }
    });

    //int 数字验证
    $validationProvider.setDefaultMsg({
        smallint: {
            error: '数值不能大于2147483647',
            success: ''
        }
    }).setExpression({
        smallint: function (value, scope, element, attrs, param) {
            return value >=0 && value <= 2147483647;
        }
    });






}]);
