/**
 * 积分账户管理controller
 * Created by zhongpingtang on 2017/8/29.
 */

app.controller('pointController',function ($scope,$http) {
    //加载业务操作
    $("#operationType").append(DIC_CONSTANT.getOption("point", "type"));

    //设置状态
    $("#recordState").append(DIC_CONSTANT.getOption("point", "state"));
    $("#recordStateForCheck").append(DIC_CONSTANT.getOption("point", "state","-3","1,2"));

    

});
