package com.hongkun.finance.user.support.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

import static com.hongkun.finance.user.support.security.AuthUtil.loginIntercepte;
import static com.hongkun.finance.user.support.security.OperationTypeEnum.*;

/**
 * @Description : 默认的是否需要登录的计算类
 * @Project : finance
 * @Program Name : com.hongkun.finance.user.support.security.DefaultLoginCalculator
 * @Author : zhongpingtang@hongkun.com.cn
 */
public class DefaultLoginCalculator implements LoginCalculator {

    /**
     * 需要登录的集合
     */
    public static final String NEED_LOGIN = "needLoginCollection";

    /**
     * 不需要登录的集合
     */
    public static final String NEED_NO_LOGIN = "needNoLoginCollection";

    /**
     * 支持处理的类型
     */
    private static final Map<String, List<OperationTypeEnum>> SUPPORT_HAND_TYPES = new HashMap(2);

    {
        SUPPORT_HAND_TYPES.put(NEED_LOGIN, Arrays.asList(DISCRIMINATE_AUTHORITY/*执行鉴权*/, DISCRIMINATE_LOGIN/*登录鉴权*/, DO_LOAD_MENU/*加载菜单*/));
        SUPPORT_HAND_TYPES.put(NEED_NO_LOGIN, Arrays.asList(DISCRIMINATE_NO_LOGIN/*不需要鉴权*/));
    }

    /**
     * 默认需要登录的权限
     */
    private List<OperationTypeEnum> defaultNeedLoginCollections = SUPPORT_HAND_TYPES.get(NEED_LOGIN);

    /**
     * 默认不需要登录的权限
     */
    private List<OperationTypeEnum> defaultNeedNoLoginCollections = SUPPORT_HAND_TYPES.get(NEED_NO_LOGIN);

    @Override
    public boolean calculatorIsNeedLogin(OperationTypeEnum operationType) {
        return defaultNeedLoginCollections.contains(operationType);
    }

    @Override
    public boolean userHasLogin(HttpServletRequest request, HttpServletResponse response) {
        return loginIntercepte(request, response);
    }


    @Override
    public boolean canSkipLogin(OperationTypeEnum operationType) {
        //不需要鉴权的可以跳过登录
        return defaultNeedNoLoginCollections.contains(operationType);
    }

    @Override
    public boolean isSupportCurrentTypes(List<OperationTypeEnum> types) {
        return SUPPORT_HAND_TYPES.values().stream().flatMap(Collection::stream).collect(Collectors.toList()).containsAll(types);
    }

    @Override
    public List<OperationTypeEnum> getSupportTypes() {
        return SUPPORT_HAND_TYPES.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
    }

    @Override
    public boolean userHasLogin(HttpServletRequest request) {
        return AuthUtil.userHasLogin(request);
    }
}
