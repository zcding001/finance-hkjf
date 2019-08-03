package com.hongkun.finance.user.support.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description :操作类型描述枚举类
 * DO_LOGIN：表明该操作为登录操作，不需要鉴权
 * DO_LOAD_MENU：表明该操作为加载菜单操作，需要首先验证登录
 *
 * DISCRIMINATE_NO_LOGIN：表明操作是一个鉴权操作，级别：不需要登录
 * DISCRIMINATE_LOGIN：表明操作是一个鉴权操作，级别：需要登录
 * DISCRIMINATE_AUTHORITY：表明操作是一个鉴权操作，级别：需要验证权限
 * @Project : finance
 * @Program Name : com.hongkun.finance.user.support.security.OperationTypeEnum
 * @Author : zhongpingtang@hongkun.com.cn
 */
public enum OperationTypeEnum {
    /**操作类*/
    DO_LOGIN,
    DO_LOAD_MENU,

    /**权限验证类*/
    DISCRIMINATE_NO_LOGIN,
    DISCRIMINATE_LOGIN,
    DISCRIMINATE_AUTHORITY;

    private static final Logger logger = LoggerFactory.getLogger(OperationTypeEnum.class);

    /**
     * 类型对应的处理器
     */
    private AbstractOperationCommand typeExecutor;

    public AbstractOperationCommand getTypeExecutor() {
        return typeExecutor;
    }

    /**
     *  @Description    ：只允许初始化一次的方法
     *  @Method_Name    ：setTypeExecutor
     *  @param typeExecutor
     *  @return void
     *  @Creation Date  ：2018/4/12
     *  @Author         ：zhongpingtang@hongkun.com.cn
     */
    public synchronized void  setTypeExecutor(AbstractOperationCommand typeExecutor) {
        if (this.typeExecutor==null) {
            this.typeExecutor = typeExecutor;
            return ;
        }
        logger.warn("不允许重复给操作类型指定处理器!, 当前:{} 已有处理器: {}, 现更新为: {}",this.name(),this.typeExecutor,typeExecutor);
    }

}
