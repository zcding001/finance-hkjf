package com.hongkun.finance.user.support.security;

import com.hongkun.finance.user.support.security.component.annotation.AskForPermission;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.exception.SecurityExpception;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.List;

import static com.hongkun.finance.user.support.security.OperationTypeEnum.DISCRIMINATE_AUTHORITY;


/**
 * @Description :
 *<p>
 * 用于对AuthorityIntercepter的父类
 * 作用:
 * 1.引入了MenuService,RegUserService
 * 2.执行初始化枚举执行器
 * 3.提供搜寻在请求的方法上面是否有AskForPermission注解的方法
 * 4.提供defalutLevel状态
 * @see com.hongkun.finance.user.support.security.AuthorityIntercepter
 * <p>
 * @Project : finance
 * @Program Name : com.hongkun.finance.user.support.security.AbstractAuthorityValidator
 * @Author : zhongpingtang@hongkun.com.cn
 */
public abstract class AbstractAuthorityValidator extends HandlerInterceptorAdapter  {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(AbstractAuthorityValidator.class);

    /**
     * 拦截器的默认级别
     */
    protected OperationTypeEnum defalutLevel = DISCRIMINATE_AUTHORITY;

    /**
     * 拦截器默认工作位置，如果是后台，开启root权限
     */
    protected boolean workInConsole = true;


    public AbstractAuthorityValidator() {/*empty*/}

    public AbstractAuthorityValidator(OperationTypeEnum defalutLevel) {
        this.defalutLevel = defalutLevel;
    }

    public AbstractAuthorityValidator(boolean workInConsole) {
        this.workInConsole = workInConsole;
    }

    public AbstractAuthorityValidator(OperationTypeEnum defalutLevel, boolean workInConsole) {
        this.defalutLevel = defalutLevel;
        this.workInConsole = workInConsole;
    }

    /**
    *  @Description    ：执行初始化枚举执行器,把执行器转载到命令中
    *  @Method_Name    ：setOperationExecutors
    *  @param operationCommand
    *  @return void
    *  @Creation Date  ：2018/4/12
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    @Autowired
    public void setOperationExecutors(List<AbstractOperationCommand> operationCommand) {
        if (BaseUtil.collectionIsEmpty(operationCommand)) {
            throw new SecurityException("没有处理器可以进行配置");
        }
        operationCommand.stream().forEach((command) -> {
            List<OperationTypeEnum> suportOpertionTypes = command.getSuportOpertionTypes();
            //存入缓存执行器
            suportOpertionTypes.stream().forEach((supportType)->supportType.setTypeExecutor(command));
        });
    }


    /**
    *  @Description    ：搜寻在请求的方法上面是否有AskForPermission注解
    *  @Method_Name    ：searchForAskForPermission
    *  @param hm          目标处理的类型
    *  @return com.hongkun.finance.user.support.security.OperationTypeEnum
    *  @Creation Date  ：2018/4/12
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    protected OperationTypeEnum searchForAskForPermission(HandlerMethod hm,HttpServletRequest request) {
        Class<?> clazz = hm.getBeanType();
        Class<AskForPermission> aClass = AskForPermission.class;
        Method m = hm.getMethod();

        try {
            //如果是一个方法映射
            if (clazz != null && m != null) {
                //检查类中是否有注解
                boolean isClzAnnotation = clazz.isAnnotationPresent(aClass);
                //检查方法中是否有注解
                boolean isMethodAnnotation = m.isAnnotationPresent(aClass);
                AskForPermission $permission;

                //如果方法和类声明中同时存在这个注解，那么方法中的会覆盖类中的设定。
                if (isMethodAnnotation) {
                    $permission = m.getAnnotation(aClass);
                    request.setAttribute("sysType",$permission.sysType());
                } else if (isClzAnnotation) {
                    $permission = clazz.getAnnotation(aClass);
                } else {
                    //默认返回一个默认级别，鉴别权限操作
                    return defalutLevel;
                }
                //使用注解上的类别
                return $permission.operation();
            }

        } catch (Exception e) {
            LOGGER.error("判断权限类型出现异常：{}", e.getMessage());
        }
        //出现问题，抛出异常
        throw new SecurityExpception();
    }


    /**
    *  @Description    ： 设置默认的工作级别
    *  @Method_Name    ：setDefalutLevel
    *  @param defalutLevel
    *  @return void
    *  @Creation Date  ：2018/4/12
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    public void setDefalutLevel(OperationTypeEnum defalutLevel) {
        this.defalutLevel = defalutLevel;
    }

    public void setWorkInConsole(boolean workInConsole) {
        this.workInConsole = workInConsole;
    }


}
