/**
 * 请求首页需要初始化的数据
 */
$(function(){
    'use strict';
    //初始化submitToken
    commonUtil.initSubmitToken();
    //初始化容器
    containerUtil.initContainer();
    //初始化数据字典常量
    DIC_CONSTANT.init();
    //初始化区域字典常量
    AREA_CONSTANT.init();
    //初始化合同类型常量
    // CONTRACTTYPE_CONSTANT.init();
});

