package com.hongkun.finance.user.support.security;

import com.hongkun.finance.user.constants.UserConstants;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.redis.JedisClusterUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.hongkun.finance.user.support.security.AuthUtil.*;
import static com.hongkun.finance.user.support.security.OperationTypeEnum.DISCRIMINATE_LOGIN;
import static com.hongkun.finance.user.support.security.OperationTypeEnum.DISCRIMINATE_NO_LOGIN;
import static com.hongkun.finance.user.support.security.SecurityConstants.NO_AUTHORITY_MESSAGE;
import static com.hongkun.finance.user.support.security.SecurityConstants.URL_AUTH_PREFIX;
import static com.hongkun.finance.user.utils.BaseUtil.tryLoadDataFromRedis;
import static org.apache.commons.lang.StringUtils.split;
import static org.apache.commons.lang3.CharSetUtils.containsAny;

/**
 * 带有前后实现的接受者
 * @author zhongping
 * @date 2018/4/11
 */

public abstract class AbstractPreValiableReceiver extends AbstractValiableReceiver implements TimeRuledAuthValidator{

    public AbstractPreValiableReceiver(List<Integer> userTypes) {
        super(userTypes);
    }

    @Override
    public boolean validateUserHasPermission(Object resultResponseEntity, RegUser user, OperationTypeEnum operationType, boolean workInConsole, HttpServletRequest request, HttpServletResponse response) {
        return doPreAuthValidate(user, operationType, workInConsole, request, response);
    }

    @Override
    public boolean doPreAuthValidate(RegUser currentUser, OperationTypeEnum operationType, boolean workInConsole, HttpServletRequest request, HttpServletResponse response) {
        //仅仅需要登录的权限,或者不需要登录的权限(可能是为了做后置数据校验),直接验证通过
        if (operationType==DISCRIMINATE_LOGIN||operationType==DISCRIMINATE_NO_LOGIN) {
            return true;
        }
        //step 1:清洗url，得到最短权限集合
        String cleanedURL = cleanUserRequestResource(request);
        //step 2:转换为对应的权限id,验证用户权限
        if (StringUtils.isNotEmpty(cleanedURL)) {
            if (!thisManHasAuthority(cleanedURL,currentUser)) {
                //说明没有权限,中断请求,并返回无权限提示信息
                return responseRequestBreak(response, new ResponseEntity(UserConstants.REQUEST_FORBID, NO_AUTHORITY_MESSAGE));
            }
            //说明有权限,可以进行下一步操作
            return true;
        }
        //未知url请求,默认禁止请求
        return false;
    }

    /**
    *  @Description    ：验证用户是否有权限访指定url
    *  @Method_Name    ：thisManHasAuthority
    *  @param cleanedURL
    *  @param currentUser
     * @return boolean
    *  @Creation Date  ：2018/6/11
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    protected  boolean thisManHasAuthority(String cleanedURL, RegUser currentUser){
        if (cleanedURL.endsWith(".do")){
            cleanedURL = cleanedURL.substring(0,cleanedURL.lastIndexOf(".do"));
        }
        //加载所具有的权限
        String userAuths = (String) tryLoadDataFromRedis(
                AuthUtil.getAuthKey(currentUser),
                false,
                () -> createUserAuthStr(getRegUserService().findUserAuthIdByMenus(getMenuLoader().apply(currentUser).stream().map(Integer::valueOf).collect(Collectors.toList()))),
                (authStr) -> cacheUserPrivileges(currentUser, (String) authStr));

        if (StringUtils.isEmpty(userAuths)) {
            return false;
        }
        String[] auths = split(userAuths, "-");
        if (!containsAny(JedisClusterUtils.get((URL_AUTH_PREFIX + cleanedURL)), auths)) {
            //继续校验,有可能相同的url指向多个id(即存在重复的url)，所以进行下一步验证，找到该用户所有的权限id对应的url，进行匹配
            List<String> hasUrls = AuthUtil.getPrivilegeSrvice().finAuthUrlsByIds(Arrays.asList(auths));
            if (BaseUtil.collectionIsEmpty(hasUrls)||(!hasUrls.contains(cleanedURL))) {
                return false;
            }
        }
        //校验通过
        return true;
    }


}
