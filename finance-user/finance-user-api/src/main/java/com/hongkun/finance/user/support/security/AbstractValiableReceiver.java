package com.hongkun.finance.user.support.security;

import com.hongkun.finance.user.model.MenuNode;
import com.hongkun.finance.user.model.RegUser;
import com.yirun.framework.core.utils.CommonUtils;
import com.yirun.framework.redis.JedisClusterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.hongkun.finance.user.support.security.AuthUtil.getMenuKey;
import static com.hongkun.finance.user.support.security.SecurityConstants.AUTH_MENU_EXPIRE_TIME;
import static com.hongkun.finance.user.utils.BaseUtil.tryLoadDataFromRedis;

/**
 * 抽象接收器实现类
 * 实现通用版本的接受者的功能
 * @author zhongping
 * @date 2018/4/11
 */

public abstract class AbstractValiableReceiver implements OperationReceiver{

    private static final Logger logger = LoggerFactory.getLogger(AbstractValiableReceiver.class);

    private List<Integer> operationReceiverSupportType;
    /**
     * 菜单加载策略
     */
    private Function<RegUser, List<String>> menuLoader;

    /**
     * 过滤菜单策略
     */
    private Predicate<MenuNode> defaultFilterMenuStrategy = (e) -> true;

    public AbstractValiableReceiver(List<Integer> userTypes) {
        operationReceiverSupportType = userTypes;
    }

    @Override
    public List<Integer> getReceiverSupportType() {
        return operationReceiverSupportType;
    }


    @Override
    public List<MenuNode> loadUserSendUserMenus(RegUser loginUser,Integer sysType) {
        //执行加载菜单
        List<String> menusStr = (List<String>) tryLoadDataFromRedis(
                getMenuKey(loginUser,sysType),
                true,
                () ->{
                    //决定菜单加载器
                    decideMenuLoaderForReceiver(loginUser);
                    //构造用户菜单
                    return AuthUtil.constructUserMenu(getMenuLoader().apply(loginUser),sysType);
                },
                (jsonedMenus) -> cacheUserMenus(loginUser, (List<String>) jsonedMenus,sysType)
        );
        return AuthUtil.JsonedMenuToMenuNode(menusStr,getFilterMenuStrategy(loginUser));
    }

    @Override
    public boolean validateUserHasPermission(Object resultResponseEntity, RegUser user, OperationTypeEnum operationType, boolean workInConsole, HttpServletRequest request, HttpServletResponse response) {
        return false;
    }


    /**
     * 为Receiver决定MenuLoder
     * web程序中可以自定义menuRoueter在扩展加载菜单的行为
     * @param loginUser
     */
    @Override
    public abstract void decideMenuLoaderForReceiver(RegUser loginUser);



    /**
     * 覆盖默认的权限加载器
     * @param menuLoader
     */
    @Override
    public void setMenuLoader(Function<RegUser, List<String>> menuLoader) {
        this.menuLoader = menuLoader;
    }

    /**
     * 获取当前菜单加载器
     * @return
     */
    public Function<RegUser, List<String>> getMenuLoader() {
        Assert.notNull(this.menuLoader,"当前处理类:"+this+"没有可用的菜单加载策略");
        return this.menuLoader;
    }

    /**
     * 获取菜单过滤策略
     * @return
     */
    public Predicate<MenuNode> getFilterMenuStrategy(RegUser regUser){
        Assert.notNull(regUser,"当前处理类："+this+"使用默认的过滤策略");
        return this.defaultFilterMenuStrategy;
    }

    /**
     * 缓存用户的菜单
     * @param currentLoginUser
     * @param jesonedMenus
     */
    protected void cacheUserMenus(RegUser currentLoginUser, List<String> jesonedMenus,Integer sysType) {
        //step 1:确定menuKey
        String menuKey = AuthUtil.getMenuKey(currentLoginUser,sysType);
        //step 2:存储相应的菜单数据
        try {
            JedisClusterUtils.delete(menuKey);
            JedisClusterUtils.setList(menuKey, jesonedMenus);
            JedisClusterUtils.setExpireTime(menuKey, AUTH_MENU_EXPIRE_TIME);
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("缓存菜单出错\n{}", CommonUtils.printStackTraceToString(e));
            }
        }
        return;
    }

    /**
     * 缓存用户的权限
     *
     * @param currentLoginUser
     * @param authStr
     * @return
     */
    protected void cacheUserPrivileges(RegUser currentLoginUser,String authStr) {
        try {
            //构造authkey
            String authKey =AuthUtil.getAuthKey(currentLoginUser);
            JedisClusterUtils.delete(authKey);
            JedisClusterUtils.set(authKey, authStr);
            JedisClusterUtils.setExpireTime(authKey, AUTH_MENU_EXPIRE_TIME);
            if (logger.isInfoEnabled()) {
                logger.info("缓存用户操作权限, 用户id: {}, 姓名: {}, 操作权限: {}", currentLoginUser.getId(), currentLoginUser.getNickName(), authStr);
            }
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("缓存权限出错, 原因\n{}", CommonUtils.printStackTraceToString(e));
            }
        }
        return;
    }



}
