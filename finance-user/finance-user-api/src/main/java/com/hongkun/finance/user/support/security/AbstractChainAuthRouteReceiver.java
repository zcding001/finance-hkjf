package com.hongkun.finance.user.support.security;

import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.utils.BaseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @Description : 主要负责加载注册的权限AuthValidateAble链
 * @Project : finance
 * @Program Name : com.hongkun.finance.user.support.security.AbstractChainAuthRouteReceiver
 * @Author : zhongpingtang@hongkun.com.cn
 */
public abstract class AbstractChainAuthRouteReceiver extends AbstractRouteAbleAuthReceiver {
    /**
     * key:url  value:验证器
     */
    protected Map<String, List<TimeRuledAuthValidator>> urlMappedAuthValidators = new HashMap<>();

    /**
     *通用的验证
     */
    List<TimeRuledAuthValidator> commonAuthValidators = new ArrayList<>();

    public AbstractChainAuthRouteReceiver(List<Integer> userTypes) {
        super(userTypes);
    }

   /**
   *  @Description    ：初始化权限验证链
   *  @Method_Name    ：initAuthValidators
   *  @param authValidateAbles 已经注册过的 AuthValidateAble
   *  @return void
   *  @Creation Date  ：2018/4/12
   *  @Author         ：zhongpingtang@hongkun.com.cn
   */
    @Autowired(required = false)
    protected void initAuthValidators(List<TimeRuledAuthValidator> authValidateAbles) {
        if (!CollectionUtils.isEmpty(authValidateAbles)) {

            //设置验证链
            //只有此处使用add,不会有并发问题
            authValidateAbles.stream().forEach(abls-> {
                String targetUrl = abls.getValidateUrl();
                if (StringUtils.isEmpty(targetUrl)) {
                    commonAuthValidators.add(abls);
                }else{
                    List<TimeRuledAuthValidator> urlChain = getUrlChain(targetUrl);
                    urlChain.add(abls);
                    urlMappedAuthValidators.putIfAbsent(targetUrl,urlChain);
                }
            });

            //为每条验证链添加通用的验证
            urlMappedAuthValidators.values().stream().forEach(chain ->{
                //为每个验证都设置通用链条
                chain.addAll(commonAuthValidators);
                //为验证的优先级排序
                AnnotationAwareOrderComparator.sort(chain);
                //当前验证置顶
                chain.add(0, this);
            });
            //把当前验证加入通用验证,并且置顶
            commonAuthValidators.add(0, this);

        }
    }

    /**
    *  @Description    ：获取url上面对应的chain
    *  @Method_Name    ：getUrlChain
    *  @param targetUrl
    *  @return java.util.List<com.hongkun.finance.user.support.security.AuthValidateAble>
    *  @Creation Date  ：2018/4/16
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    protected  List<TimeRuledAuthValidator> getUrlChain(String targetUrl){
        List<TimeRuledAuthValidator> authValidateAbles = urlMappedAuthValidators.get(targetUrl);
        return authValidateAbles == null ? new ArrayList<>() : authValidateAbles;
    }

    /**
    *  @Description    ：触发验证链
    *  @Method_Name    ：validateUserHasPermission
    *  @param resultResponseEntity 验证结果
    *  @param user 当前用户
    *  @param operationType 操作类型
    *  @param workInConsole 在后台工作
    *  @param request
    *  @param response
    *  @return boolean   验证是否通过
    *  @Creation Date  ：2018/4/12
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    @Override
    public boolean validateUserHasPermission(Object resultResponseEntity, RegUser user, OperationTypeEnum operationType, boolean workInConsole, HttpServletRequest request, HttpServletResponse response) {
        //先取出,免得被覆盖
        boolean atPostTime = AuthUtil.getCallPostExecutorFlag(request);
        List<TimeRuledAuthValidator> targetValidators = urlMappedAuthValidators.getOrDefault(AuthUtil.cleanUserRequestResource(request), commonAuthValidators);
        //验证
        if (!doInvokeValidators(resultResponseEntity, user, operationType, workInConsole, request, response, atPostTime,targetValidators)) {
            return false;
        }
        if (!atPostTime) {
            //如果没有验证了, 并且当前不处于前置验证,去通知后置调用
            AuthUtil.notifyPostCallExecutor(request, operationType);
        }
        //所有的验证均通过,返回验证通过
        return true;
    }

    /**
    *  @Description    ：启动验证链
    *  @Method_Name    ：doInvokeValidators
    *  @param resultResponseEntity
    *  @param user
    *  @param operationType
    *  @param workInConsole
    *  @param request
    *  @param response
    *  @param atPostTime
    *  @param timeRuledAuthValidators
    *  @return boolean
    *  @Creation Date  ：2018/4/16
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    private boolean doInvokeValidators(Object resultResponseEntity, RegUser user, OperationTypeEnum operationType, boolean workInConsole, HttpServletRequest request, HttpServletResponse response, boolean atPostTime, List<TimeRuledAuthValidator> timeRuledAuthValidators) {
        if (BaseUtil.collectionIsEmpty(timeRuledAuthValidators)) {
            //没有验证器直接返回
            return true;
        }
        for (TimeRuledAuthValidator currentValidator : timeRuledAuthValidators) {
            //判断是否需要验证此方法
            if (currentValidator.canValidateOn(user, operationType, workInConsole, request, response)) {
                //执行分类型验证的逻辑
                boolean postType = currentValidator.isPostValidate();
                //Controller方法执行后校验
                if (atPostTime&&postType) {
                    if (!currentValidator.doPostAuthValidate(resultResponseEntity,user, operationType, workInConsole, request, response)) {
                        //验证不通过中断循环
                        return false;
                    }
                    //如果满足此次条件,必然不满足下面的条件,跳过判断
                    continue;
                }

                if((!atPostTime)&&(!postType)) {
                    //Controller方法执行前校验
                    if (!currentValidator.doPreAuthValidate(user, operationType, workInConsole, request, response)) {
                        //验证不通过中断循环
                        return false;
                    }
                }
            }
        }
        return true;
    }


}
