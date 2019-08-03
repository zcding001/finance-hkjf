package com.hongkun.finance.user.support.security;

import com.hongkun.finance.user.model.MenuNode;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.model.SysMenu;
import com.hongkun.finance.user.service.MenuService;
import com.hongkun.finance.user.service.PrivilegeSrvice;
import com.hongkun.finance.user.service.RegUserService;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.*;
import com.yirun.framework.core.utils.json.JsonUtils;
import com.yirun.framework.core.utils.response.ResponseUtils;
import com.yirun.framework.redis.JedisClusterUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.hongkun.finance.user.constants.UserConstants.*;
import static com.hongkun.finance.user.support.security.SecurityConstants.*;
import static com.hongkun.finance.user.utils.BaseUtil.tryLoadDataFromRedis;
import static com.yirun.framework.core.commons.Constants.TICKET;
import static com.yirun.framework.core.commons.Constants.TICKET_EXPIRES_TIME;
import static java.lang.Boolean.FALSE;
import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.join;
import static org.springframework.util.StringUtils.arrayToDelimitedString;

/**
 * @Description : 权限处理工具类,用于权限处理模块
 * @Project : finance
 * @Program Name : com.hongkun.finance.user.support.security.AuthUtil
 * @Author : zhongpingtang@hongkun.com.cn
 */
public class AuthUtil {
    private static final Logger logger = LoggerFactory.getLogger(AuthUtil.class);

    private static String POSTCALLDISCRIMINATOR = "POSTCALLDISCRIMINATOR";
    private static String CHECKPAIR = "CHECKPAIR";


   /**
   *  @Description    ：初始化缓存map
   *  @Method_Name    ：initIfNullableCacheMap
   *  @param sourceList 源List
   *  @param consumer 执行逻辑
   *  @param synchronizedLock  执行锁
   *  @return java.util.Map
   *  @Creation Date  ：2018/4/13
   *  @Author         ：zhongpingtang@hongkun.com.cn
   */
    public synchronized static <K, V> Map initIfNullableCacheMap(List<?> sourceList, Consumer<Map<K, V>> consumer, Class<?> synchronizedLock) {
        Map<K, V> cacheMap = new ConcurrentHashMap<>(16);
        if (BaseUtil.collectionIsEmpty(sourceList)) {
            return cacheMap;
        }
        synchronized (synchronizedLock){
            //初始化执行器
            consumer.accept(cacheMap);
        }
        return cacheMap;

    }


    /**
    *  @Description    ：中断请求方法，当用户没有权限访问某个url时候，应该中断其请求,返回前端提示信息
    *  @Method_Name    ：responseRequestBreak
    *  @param response
    *  @param responseEntity
    *  @return java.lang.Boolean
    *  @Creation Date  ：2018/4/13
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    public static Boolean responseRequestBreak(HttpServletResponse response, ResponseEntity responseEntity) {
        String responseStr = JsonUtils.toJson(responseEntity);
        ResponseUtils.responseJson(response, responseStr);
        return FALSE;
    }

    /**
    *  @Description    ：创建用户的权限字符串
    *  @Method_Name    ：createUserAuthStr
    *  @param userPris
    *  @return java.lang.String
    *  @Creation Date  ：2018/4/13
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    public static String createUserAuthStr(List<Integer> userPris) {
        return BaseUtil.collectionIsEmpty(userPris) ? "0" : arrayToDelimitedString(userPris.toArray(), "-");

    }

    /**
    *  @Description    ：从spring容器中获取RegUserService
    *  @Method_Name    ：getRegUserService
    *
    *  @return com.hongkun.finance.user.service.RegUserService
    *  @Creation Date  ：2018/4/13
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    public static RegUserService getRegUserService() {
        RegUserService regUserService = ApplicationContextUtils.getBean(RegUserService.class);
        return regUserService;
    }


    /**
    *  @Description    ：从spring容器中获取MenuService
    *  @Method_Name    ：getMenuService
    *
    *  @return com.hongkun.finance.user.service.MenuService
    *  @Creation Date  ：2018/4/13
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    public static MenuService getMenuService() {
        MenuService menuService = ApplicationContextUtils.getBean(MenuService.class);
        return menuService;
    }

    /**
     *  @Description    ：从spring容器中获取MenuService
     *  @Method_Name    ：getMenuService
     *
     *  @return com.hongkun.finance.user.service.MenuService
     *  @Creation Date  ：2018/4/13
     *  @Author         ：zhongpingtang@hongkun.com.cn
     */
    public static PrivilegeSrvice getPrivilegeSrvice() {
        PrivilegeSrvice privilegeSrvice = ApplicationContextUtils.getBean(PrivilegeSrvice.class);
        return privilegeSrvice;
    }



    /**
   *  @Description    ：清洗url,使得其变成最短权限,
   *  @Method_Name    ：cleanUserRequestResource
   *  @param request
   *  @return java.lang.String   url,如：/bidInfoController/findIndexBidInfo.do
   *  @Creation Date  ：2018/4/13
   *  @Author         ：zhongpingtang@hongkun.com.cn
   */
    public static String cleanUserRequestResource(HttpServletRequest request) {
        String contextPath = request.getContextPath();
        String requestURI = request.getRequestURI();
        String cleanedRequest = null;

        if (StringUtils.isNotEmpty(requestURI)) {
            cleanedRequest = StringUtils.remove(requestURI, contextPath);
        }
        return cleanedRequest;
    }

   /**
   *  @Description    ：获取菜单源数据
   *  @Method_Name    ：getMenuDataSource
   *  @return java.util.Map<java.lang.Integer,com.hongkun.finance.user.model.SysMenu>
   *  @Creation Date  ：2018/4/13
   *  @Author         ：zhongpingtang@hongkun.com.cn
   */
    public static Map<Integer, SysMenu> getMenuDataSource() {
        final List<String> allSysMenus = (List<String>) tryLoadDataFromRedis(
                ALL_MENUS,
                true,
                () -> getMenuService()
                        .listAllMenusNoPager()
                        .stream()
                        .map(JsonUtils::toJson).collect(Collectors.toList()),
                (cacheMenus) -> deleteAuthListTotallyThenSet(ALL_MENUS,(List<String>) cacheMenus));

        //构造menuDataSource
        final Map<Integer, SysMenu> menuDataSource = allSysMenus.stream()
                .map(e -> JsonUtils.json2Object(e, SysMenu.class, DateUtils.DATE))
                .collect(Collectors.toMap(toKey -> toKey.getId(), toValue -> toValue));

        return menuDataSource;
    }


    /**
    *  @Description    ：通知后置调用拦截器
    *  @Method_Name    ：notifyPostCallExecutor
    *  @param request  当前请求
    *  @param typeEnum  操作类型
    *  @return void
    *  @Creation Date  ：2018/4/13
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    public static void notifyPostCallExecutor(HttpServletRequest request, OperationTypeEnum typeEnum) {
        Assert.notNull(request, "request不能为空");
        request.setAttribute(POSTCALLDISCRIMINATOR, true);
        request.setAttribute(CHECKPAIR, typeEnum);
    }

    /**
    *  @Description    ：获取调用拦截器请求信号,可以用来判断请求处于哪个时间点
    *  @Method_Name    ：getCallPostExecutorFlag
    *  @param request
    *  @return boolean
    *  @Creation Date  ：2018/4/13
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    public static boolean getCallPostExecutorFlag(HttpServletRequest request) {
        Assert.notNull(request, "request不能为空");
        Object flag = request.getAttribute(POSTCALLDISCRIMINATOR);
        return (flag !=null&&(boolean)flag);
    }

    /**
    *  @Description    ： 获取操作类型
    *  @Method_Name    ：getOperationType
    *  @param request
    *  @return com.hongkun.finance.user.support.security.OperationTypeEnum
    *  @Creation Date  ：2018/4/13
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    public static OperationTypeEnum getOperationType(HttpServletRequest request) {
        Assert.notNull(request, "request不能为空");
        OperationTypeEnum checkPair;
        if ((checkPair = (OperationTypeEnum) request.getAttribute(CHECKPAIR)) != null) {
            return checkPair;
        }

        return null;
    }


   /**
   *  @Description    ：判断用户是否登录
   *  @Method_Name    ：userHasLogin
   *  @param request
   *  @return boolean  登录状态
   *  @Creation Date  ：2018/4/13
   *  @Author         ：zhongpingtang@hongkun.com.cn
   */
    public static boolean userHasLogin(HttpServletRequest request) {
        //获得ticket,以redis中用户作为用户登录的判断唯一凭证
        Cookie cookie = CookieUtil.getCookie(request, TICKET + BaseUtil.getServerType());
        if (cookie == null) {
            return false;
        }
        String ticket = cookie.getValue();
        //从redis中加载ticket对应的用户信息
        RegUser regUser = JedisClusterUtils.getObjectForJson(ticket, RegUser.class);
        if (regUser == null) {
            return false;
        } else {
        	HttpSessionUtil.addLoginUser(regUser);
            RegUser r = JedisClusterUtils.getObjectForJson(RegUser.class.getSimpleName() + regUser.getId(), RegUser.class);
            if(r != null) {
            	HttpSessionUtil.addLoginUser(r);
            }
            RegUser seesionUser = (RegUser)HttpSessionUtil.getLoginUser();
            if(seesionUser != null) {
            	HttpSessionUtil.addLoginUser(seesionUser);
            }
            //刷新redis中会话的过期时间
            JedisClusterUtils.setExpireTime(ticket, TICKET_EXPIRES_TIME);
            Long currentLogin = regUser.getLogin();
            //刷新redis中权限的过期时间
            String separator = "_";
            JedisClusterUtils.setExpireTime(join(asList(USER_AUTH_PREFIX, regUser.getType(), currentLogin), separator), AUTH_MENU_EXPIRE_TIME);
            //刷新redis中菜单的过期时间
            JedisClusterUtils.setExpireTime(join(asList(USER_MENU_PREFIX, regUser.getType(), currentLogin), separator), AUTH_MENU_EXPIRE_TIME);
            //刷新token过期时间
            String token = CookieUtil.getCookieValue(BaseUtil.getSubmitTokenKey());
            if(StringUtils.isNotBlank(token)){
                JedisClusterUtils.setExpireTime(token, Constants.TICKET_EXPIRES_TIME);
            }
        }
        return true;
    }

    /**
    *  @Description    ：拦截用户登录,如果没有登录,返回没有登录提示
    *  @Method_Name    ：loginIntercepte
    *  @param request
    *  @param response
    *  @return java.lang.Boolean
    *  @Creation Date  ：2018/4/13
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    public static Boolean loginIntercepte(HttpServletRequest request, HttpServletResponse response) {
        if (!userHasLogin(request)) {
            //返回没有登录
            return responseRequestBreak(response, new ResponseEntity(NO_LOGIN, NO_LOGIN_MESSAGE));
        }
        return true;
    }

    /**
    *  @Description    ：构造用户菜单
    *  @Method_Name    ：constructUserMenu
    *  @param strMenusIds
    *  @param sysType
    *  @return java.util.List<java.lang.String>  已经json化的菜单
    *  @Creation Date  ：2018/4/13
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    public static List<String> constructUserMenu(List<String> strMenusIds,Integer sysType) {
        return initJsonedMenuFromSecondMenuIds(strMenusIds,sysType);
    }

    /**
    *  @Description    ：从用户二级菜单id中初始化用户的的菜单数据
    *  @Method_Name    ：initJsonedMenuFromSecondMenuIds
    *  @param userSecondMenuIds
    *  @param sysType
    *  @return java.util.List<java.lang.String>
    *  @Creation Date  ：2018/4/13
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    public static List<String> initJsonedMenuFromSecondMenuIds(List<String> userSecondMenuIds, Integer sysType) {
        //step 1:初始化菜单数据源,避免多次读redis
        final Map<Integer, SysMenu> menuDataSource = getMenuDataSource();
        List<String> userMenus = new ArrayList<>(30);
        List<SysMenu> userSecondMenus = null;
        //step 2:获取用户二级菜单数组信息
        if (!BaseUtil.collectionIsEmpty(userSecondMenuIds)) {
            userSecondMenus = new HashSet<>(userSecondMenuIds)
                    .stream()
                    .map(menuId -> menuDataSource.get(Integer.valueOf(menuId)))
                    .filter(e -> e != null&&sysType.equals(e.getType())/*找出符合菜单用户类型的菜单*/).collect(Collectors.toList());
        }
        //step 3:组装用户的菜单
        if (!BaseUtil.collectionIsEmpty(userSecondMenus)) {
            userSecondMenus.stream()
                    .collect(Collectors.groupingBy((e) -> e.getPid()))
                    .forEach((pid, list) -> {
                        SysMenu levelOneMenu = menuDataSource.get(pid);
                        MenuNode node = new MenuNode(levelOneMenu.getMenuName(), levelOneMenu.getSeqNo());
                        //排序
                        list.sort(Comparator.comparing(SysMenu::getSeqNo));
                        node.setChildNode(list);
                        userMenus.add(JsonUtils.toJson(node));
                    });

        }
        return userMenus;
    }

    /**
    *  @Description    ：彻底删除redis中key之后再设置
    *  @Method_Name    ：deleteAuthListTotallyThenSet
    *  @param key
    *  @param targetList
    *  @return void
    *  @Creation Date  ：2018/4/13
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    public static void deleteAuthListTotallyThenSet(String key,List<String> targetList){
        JedisClusterUtils.delete(key);
        if (!CollectionUtils.isEmpty(targetList)) {
            JedisClusterUtils.setList(key, targetList);
        }
    }


    /**
    *  @Description    ：把json话的Menu转换为MenuNode
    *  @Method_Name    ：JsonedMenuToMenuNode
    *  @param menusStr
    *  @return java.util.List<com.hongkun.finance.user.model.MenuNode>
    *  @Creation Date  ：2018/4/13
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    public static List<MenuNode> JsonedMenuToMenuNode(List<String> menusStr, Predicate<MenuNode> predicate) {
        List<MenuNode> menuNodes=null;
        if (!BaseUtil.collectionIsEmpty(menusStr)) {
            menuNodes = menusStr.stream().map(e -> JsonUtils.json2Object(e, MenuNode.class, DateUtils.DATE))
                    .filter((e) ->predicate.test(e)).collect(Collectors.toList());
            //排序
            Collections.sort(menuNodes, Comparator.comparing(MenuNode::getSeqNo));
        }
        return menuNodes;
    }
    /**
     *  @Description    ：把json话的Menu转换为MenuNode
     *  @Method_Name    ：JsonedMenuToMenuNode
     *  @param menusStr
     *  @return java.util.List<com.hongkun.finance.user.model.MenuNode>
     *  @Creation Date  ：2018/4/13
     *  @Author         ：zhongpingtang@hongkun.com.cn
     */
    public static List<MenuNode> JsonedMenuToMenuNode(List<String> menusStr) {
        List<MenuNode> menuNodes=null;
        if (!BaseUtil.collectionIsEmpty(menusStr)) {
            menuNodes = menusStr.stream().map(e -> JsonUtils.json2Object(e, MenuNode.class, DateUtils.DATE))
                     .collect(Collectors.toList());
            //排序
            Collections.sort(menuNodes, Comparator.comparing(MenuNode::getSeqNo));
        }
        return menuNodes;
    }

  /**
  *  @Description    ：获取菜单key
  *  @Method_Name    ：getMenuKey
  *  @param loginUser
  *  @return java.lang.String
  *  @Creation Date  ：2018/4/13
  *  @Author         ：zhongpingtang@hongkun.com.cn
  */
    public static String getMenuKey(RegUser loginUser) {
        return join(asList(USER_MENU_PREFIX, loginUser.getType(), loginUser.getLogin()), "_");
    }

    /** 
    * @Description: 获取菜单key
    * @param loginUser
    * @param sysType 菜单类型 1-鸿坤金服后台 ,2-我的账户，3-BI后台
    * @return: java.lang.String 
    * @Author: hanghe@hongkunjinfu.com
    * @Date: 2019/1/16 16:11
    */
    public static String getMenuKey(RegUser loginUser,Integer sysType) {
        return join(asList(USER_MENU_PREFIX, loginUser.getType(), loginUser.getLogin()), sysType,"_");
    }

   /**
   *  @Description    ：构造authKey
   *  @Method_Name    ：getAuthKey
   *  @param currentLoginUser
   *  @return java.lang.String
   *  @Creation Date  ：2018/4/13
   *  @Author         ：zhongpingtang@hongkun.com.cn
   */
    public static String getAuthKey(RegUser currentLoginUser) {
        return join(asList(USER_AUTH_PREFIX, currentLoginUser.getType(), currentLoginUser.getLogin()), "_");
    }

   /**
   *  @Description    ： 调用执行器封装方法
   *  @Method_Name    ：callExecutor
   *  @param resultResponseEntity
   *  @param request
   *  @param response
   *  @param operationTypeEnum
   *  @param workInConsole
   *  @return boolean
   *  @Creation Date  ：2018/4/13
   *  @Author         ：zhongpingtang@hongkun.com.cn
   */
    public static boolean callExecutor(Object resultResponseEntity, HttpServletRequest request, HttpServletResponse response, OperationTypeEnum operationTypeEnum, boolean workInConsole) {
        return Optional.ofNullable(operationTypeEnum.getTypeExecutor())
                       .map(operationCommand -> operationCommand.doOpertion(resultResponseEntity,operationTypeEnum, request, response, workInConsole))
                       .orElse(false);
    }

    /**
    *  @Description    ：如果request中没有后置调用信息,通知上
    *  @Method_Name    ：notifyPostCallExecutorIfNeed
    *  @param request
    *  @param operationType
    *  @return void
    *  @Creation Date  ：2018/4/13
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    public static void notifyPostCallExecutorIfNeed(HttpServletRequest request, OperationTypeEnum operationType) {
        if (!getCallPostExecutorFlag(request)) {
            notifyPostCallExecutor(request, operationType);
        }
    }
}
