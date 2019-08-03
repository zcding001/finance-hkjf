package com.hongkun.finance.user.support.security.component.schema;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;


/**
 * @Description : 注册auth标签处理类
 * @Project : finance
 * @Program Name : com.hongkun.finance.user.support.security.component.schema.AuthNameSpaceHandler
 * @Author : zhongpingtang@hongkun.com.cn
 */
public class AuthNameSpaceHandler extends NamespaceHandlerSupport {


    public AuthNameSpaceHandler() {
    }

    @Override
	public void init() {
		//注册auth解析器
		registerBeanDefinitionParser("enableControl",new AuthEditorParser());

	}



}
