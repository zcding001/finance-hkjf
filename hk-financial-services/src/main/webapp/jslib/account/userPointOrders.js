/**
 * Created by dzc on 18.1.5.
 * 还款计划、回款计划、投资记录业务js
 */
var pointProductOrdersController = {};
(function(){
    'use strict';

    /**
     * 订单记录列定义
     * @type {*[]}
     */
    var pointProductOrdersColumns = [
        {
            title: '兑换商品', data: 'name', tdClass: 'tac', thClass: 'tac',
            render: function (value, row) {
                var content = '';
                content+='<img src="${oss.url}'+row.smallImgUrl+'" height="80" width="80">'
                content += '<p class="mt-5"  style="word-wrap: break-word;">'+value+'</p>';
                return content;
            }
        },{
            title: '数量', data: 'number', tdClass: 'tac', thClass: 'tac',
            render: function (value, row) {
                return value;
            }
        },{
            title: '使用积分', data: 'point', tdClass: 'tac', thClass: 'tac'
        },{
            title: '兑换时间', data: 'createTime', tdClass: 'tac', thClass: 'tac',
            render:function (value) {
                return dateUtil.dateTime(value);
            }
        },{
            title: '收货地址', data: 'address', thClass: 'tac', thStyle: 'width: 260px;',
        },{
            title: '状态', data: 'state', tdClass: 'tac', thClass: 'tac',
            render: function (value, row) {
                return dictionaryUtil.getName("point_order", "state", value)
            }
        }

    ];


    /**
     * 初始化订单页
     */
    pointProductOrdersController.initOrders = function(){
        renderUtil.renderList("/pointProductOrderController/listUserPointProductOrder.do", pointProductOrdersColumns);
    };



})();