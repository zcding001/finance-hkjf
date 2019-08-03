package com.hongkun.finance.user.support.security.component;

import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.support.security.OperationReceiver;
import org.springframework.core.Ordered;


/**
 * @Description :
 * 菜单策略接口(strategy interface),
 * 为了web项目中可能存在的不确定性的菜单功能而设计
 * 当默认的菜单策略无法满足用户的菜单的决定的时候,
 * 可以在web项目中配置新的菜单策略,然后使其Order
 * 高于默认的菜单策略(默认的菜单策略优先级最低)
 * @Project : finance
 * @Program Name : com.hongkun.finance.user.support.security.component.MenuRouter
 * @Author : zhongpingtang@hongkun.com.cn
 */
public interface MenuRouter extends Ordered{

    /**
    *  @Description    ：获取本菜单路由器的优先级
    *  @Method_Name    ：getOrder
    *  @return int     此菜单路由器的优先级, 数值越大, 优先级越低
    *  @Creation Date  ：2018/4/12
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    default int getOrder(){
        return Integer.MAX_VALUE;
    }





    /**
    *  @Description    ：决定加载菜单id的方式
    *  @Method_Name    ：doRoute
    *  @param operationReceiver   操作接受者
    *  @return void
    *  @Creation Date  ：2018/4/12
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
   void doRoute(OperationReceiver operationReceiver);


   /**
   *  @Description    ：决定是否使用当前的路由器
   *  @Method_Name    ：useCurrentRouter
   *  @param user
   *  @return boolean   是否使用当前的菜单策略
   *  @Creation Date  ：2018/4/12
   *  @Author         ：zhongpingtang@hongkun.com.cn
   */
    default boolean useCurrentRouter(RegUser user){
        return true;
    }


}
