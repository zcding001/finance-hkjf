package com.hongkun.finance.user.support.security.component.schema;


import com.hongkun.finance.user.support.security.AuthorityIntercepter;
import com.hongkun.finance.user.support.security.AuthClassScaner;
import com.hongkun.finance.user.support.security.OperationTypeEnum;
import com.hongkun.finance.user.support.security.component.AuthCallBackHandler;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.support.*;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.web.servlet.handler.MappedInterceptor;
import org.w3c.dom.Element;

import java.util.Arrays;

/**
 * @Description : 标签解析器, 解析如auth标签,格式如
 * <auth:enableControl url="****" defalutLevel="DISCRIMINATE_LOGIN" workInConsole="false"/>
 * @Project : finance
 * @Program Name : com.hongkun.finance.user.support.security.component.schema.AuthEditorParser
 * @Author : zhongpingtang@hongkun.com.cn
 */
public class AuthEditorParser
        extends AbstractSingleBeanDefinitionParser  {


    /**
     *url属性,表示要拦截的url
     */
    private static final String URL = "url";
    /**
     *默认的级别
     */
    private static final String DEFAULT_LEVEL = "defalutLevel";
    /**
     *拦截器是否工作在后台
     */
    private static final String WORK_IN_CONSOLE = "workInConsole";

    /**
     *权限拦截解析器的名称
     */
    static final String BEAN_NAME = "AuthIntercepterParser";
    /**
     *权限拦截器在spring容器中的名称
     */
    static final String INTERCEPTER_NAME = "authIntercepter_inner";


    //扫描默认包
    static final String basePackages =StringUtils.join(
            Arrays.asList(
                    //加载操作命令
                    "com.hongkun.finance.user.support.security.component.operationcommands",
                    //加载命令接受者
                    "com.hongkun.finance.**.operationreceivers",
                    //加载菜单路由器
                    "com.hongkun.finance.**.menurouters",
                    //加载权限验证器
                    "com.hongkun.finance.**.authvalidators"), ",");


    protected String resolveId(Element element,
                               AbstractBeanDefinition definition,
                               ParserContext parserContext)
            throws BeanDefinitionStoreException {
       //注册相应的组件-接受者， 命令
        registerAuthCompont(parserContext);
        return BEAN_NAME;
    }

    protected Class<Object> getBeanClass(Element element) {
        return Object.class;
    }

    @Override
    protected void doParse(Element element,
                           ParserContext parserContext,
                           BeanDefinitionBuilder builder) {
        //注册权限拦截器
        RootBeanDefinition mappedInterceptorDef = new RootBeanDefinition(MappedInterceptor.class);
        mappedInterceptorDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);

        ManagedList<String> includePatterns = new ManagedList<>();
        ManagedList<String> excludePatterns = new ManagedList<>();


        includePatterns.add(element.getAttribute(URL));
        BeanDefinitionHolder interceptorBean = new BeanDefinitionHolder(new RootBeanDefinition(AuthorityIntercepter.class), INTERCEPTER_NAME, null);


        //设置相应的属性
        String defluatLevel = element.getAttribute(DEFAULT_LEVEL);
        interceptorBean.getBeanDefinition().getConstructorArgumentValues().addIndexedArgumentValue(0, OperationTypeEnum.valueOf(defluatLevel));


        String workInConsole = element.getAttribute(WORK_IN_CONSOLE);
        interceptorBean.getBeanDefinition().getConstructorArgumentValues().addIndexedArgumentValue(1,Boolean.valueOf(workInConsole).booleanValue());


        mappedInterceptorDef.getConstructorArgumentValues().addIndexedArgumentValue(0, includePatterns);
        mappedInterceptorDef.getConstructorArgumentValues().addIndexedArgumentValue(1, excludePatterns);
        mappedInterceptorDef.getConstructorArgumentValues().addIndexedArgumentValue(2, interceptorBean);

        String beanName = parserContext.getReaderContext().registerWithGeneratedName(mappedInterceptorDef);
        parserContext.registerComponent(new BeanComponentDefinition(mappedInterceptorDef, beanName));

    }

    //注册权限相关的组件
    private void registerAuthCompont(ParserContext parserContext) {
         //扫描,注册相关的组件
        BeanDefinitionRegistry registry = parserContext.getRegistry();
        AuthClassScaner.scan(basePackages, null)
                       .forEach(clazz -> {
                    RootBeanDefinition definition = new RootBeanDefinition(clazz);
                    //设置为基础bean
                    definition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
                    BeanDefinitionReaderUtils.registerWithGeneratedName(definition, registry);
                });

        //注册权限回调组件
        RootBeanDefinition definition = new RootBeanDefinition(AuthCallBackHandler.class);
        definition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
        BeanDefinitionReaderUtils.registerWithGeneratedName(definition, registry);

    }



}