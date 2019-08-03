//package com.hongkun.finance.web.support.menurouters;
//
//import com.alibaba.dubbo.config.annotation.Reference;
//import com.hongkun.finance.roster.constants.RosterFlag;
//import com.hongkun.finance.roster.constants.RosterType;
//import com.hongkun.finance.roster.service.RosInfoService;
//import com.hongkun.finance.user.model.RegUser;
//import com.hongkun.finance.user.service.RegUserService;
//import com.hongkun.finance.user.support.security.OperationReceiver;
//import com.hongkun.finance.user.support.security.component.MenuRouter;
//import org.springframework.beans.factory.annotation.Value;
//
///**
// * @Description : 债券转让白名单上面的用户,指向债权转让模板角色
// * @Project : finance
// * @Program Name : com.hongkun.finance.web.support.menurouters.RosBondTransferMenuRuter
// * @Author : zhongpingtang@hongkun.com.cn
// */
//public class RosBondTransferMenuRuter implements MenuRouter {
//
//    @Value("${rosBondTransferId}")
//    private String rosBondTransferId;
//    @Reference
//    private RosInfoService rosInfoService;
//    @Reference
//    private RegUserService regUserService;
//
//    @Override
//    public int getOrder() {
//        //限制为优先级最高
//        return Integer.MIN_VALUE;
//    }
//
//    @Override
//    public boolean useCurrentRouter(RegUser user) {
//        //在白名单上面有用户使用此路由器
//        return rosInfoService.validateRoster(user.getId(), RosterType.BOND_TRANSFER, RosterFlag.WHITE);
//    }
//
//    @Override
//    public void doRoute(OperationReceiver operationReceiver) {
//        operationReceiver.setMenuLoader((user)->regUserService.findRolesBindMenuIds(Integer.valueOf(rosBondTransferId)));
//    }
//
//
//    public void setRosBondTransferId(String rosBondTransferId) {
//        this.rosBondTransferId = rosBondTransferId;
//    }
//}
