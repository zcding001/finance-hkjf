package com.hongkun.finance.user.support.security;

import com.hongkun.finance.user.model.MenuNode;
import com.hongkun.finance.user.model.RegUser;

import java.util.List;
import java.util.function.Function;

/**
 * @Description : 用户鉴别器抽象,命令模式角色：接收者-Receiver
 * @Project : finance
 * @Program Name : com.hongkun.finance.user.support.security.OperationReceiver
 * @Author : zhongpingtang@hongkun.com.cn
 */
public interface OperationReceiver extends AuthValidateAble{

    /**
    *  @Description    ：获取接受者支持的类型
    *  @Method_Name    ：getReceiverSupportType
    *
    *  @return java.util.List<java.lang.Integer>
    *  @Creation Date  ：2018/4/12
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    List<Integer> getReceiverSupportType();


    /**
    *  @Description    ： 加载用户的的菜单的
    *  @Method_Name    ：loadUserSendUserMenus
    *  @param loginUser
    *  @return java.util.List<com.hongkun.finance.user.model.MenuNode>
    *  @Creation Date  ：2018/4/12
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    List<MenuNode> loadUserSendUserMenus(RegUser loginUser,Integer sysType);


    /**
    *  @Description    ：为Redceiver确定菜单加载器
    *  @Method_Name    ：decideMenuLoaderForReceiver
    *  @param loginUser
    *  @return void
    *  @Creation Date  ：2018/4/12
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    void decideMenuLoaderForReceiver(RegUser loginUser);

    /**
    *  @Description    ：设置菜单加载器
    *  @Method_Name    ：setMenuLoader
    *  @param menuLoader
    *  @return void
    *  @Creation Date  ：2018/4/12
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    void setMenuLoader(Function<RegUser, List<String>> menuLoader);
}
