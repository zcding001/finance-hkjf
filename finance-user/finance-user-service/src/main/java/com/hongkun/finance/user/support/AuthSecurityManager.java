package com.hongkun.finance.user.support;

import com.hongkun.finance.user.dao.RegUserDao;
import com.hongkun.finance.user.dao.SysRoleMenuRelDao;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static com.hongkun.finance.user.constants.UserConstants.USER_TYPE_ROOT;
import static com.hongkun.finance.user.support.security.AuthUtil.getRegUserService;
import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * @Description : 权限分配时的安全管理器
 * @Project : finance
 * @Program Name : com.hongkun.finance.user.support.AuthSecurityManager
 * @Author : zhongpingtang@yiruntz.com 唐忠平
 */
public class AuthSecurityManager {
    private static final Logger logger = LoggerFactory.getLogger(AuthSecurityManager.class);

    /**
     * 校验当前在操作的用户是否是在当前用户的可以操作范围内
     * @param regUserDao
     * @param userId
     * @param operatorMenus
     * @return
     */
    public static ResponseEntity checkUserMenusWithOprateMenus(RegUserDao regUserDao, Integer userId, Collection<Integer> operatorMenus){
        return doMenusValidate(regUserDao.findByPK(Long.valueOf(userId), RegUser.class), regUserDao, operatorMenus);
    }


    /**
    *  @Description    ：检验当前用户在操作角色时候是否有权限
    *  @Method_Name    ：checkUserMenusWithOprateRoles
    *  @param regUserDao
    *  @param sysRoleMenuRelDao
    *  @param userId
    *  @param roleIds
    *  @return com.yirun.framework.core.model.ResponseEntity
    *  @Creation Date  ：2018/6/13
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    public static  ResponseEntity checkUserMenusWithOprateRoles(RegUserDao regUserDao,
                                                                SysRoleMenuRelDao sysRoleMenuRelDao,
                                                                Integer userId, Collection<Integer> roleIds){
        //找到当前操作的menuids
        Set<Integer> operateMenuIds = new HashSet<>();
        roleIds.stream().forEach(roleid->operateMenuIds.addAll(sysRoleMenuRelDao.findRolesBindMenuIds(roleid)));
        return doMenusValidate(regUserDao.findByPK(Long.valueOf(userId), RegUser.class), regUserDao, operateMenuIds);
    }


    /**
    *  @Description    ：检验把权限绑定到菜单，前提是用户具有该权限和菜单
    *  @Method_Name    ：checkUserMenusAndAuthsWithPris
    *
    *  @return com.yirun.framework.core.model.ResponseEntity
    *  @Creation Date  ：2018/6/13
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    public static ResponseEntity checkUserMenusAndAuthsWithPris(RegUserDao regUserDao,Integer currentUserId,Integer menuId,Collection<Integer> pris){
        RegUser currentUser = regUserDao.findByPK(Long.valueOf(currentUserId), RegUser.class);
        ResponseEntity<List<Integer>> validateResult = doMenusValidate(currentUser, regUserDao, Arrays.asList(menuId));
        if (validateResult.validSuc()) {
            //继续验证是否有该权限
            if (!userIsROOT(currentUser)) {
                //验证当前用户是否有这个权限
                List<Integer> userAuths = getRegUserService().findUserAuthIdByMenus(validateResult.getResMsg());
                if (BaseUtil.collectionIsEmpty(userAuths)||(!userAuths.containsAll(pris))) {
                    return new ResponseEntity(Constants.ERROR,"对不起,您无法操作不属于您的权限!");
                }
            }
        }
        return validateResult;
    }


    /**
    *  @Description    ：判断用户是否是ROOT用户
    *  @Method_Name    ：userIsROOT
    *  @param currentUser
    *  @return boolean
    *  @Creation Date  ：2018/6/13
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    private static boolean userIsROOT(RegUser currentUser){
        return currentUser != null && (BaseUtil.equelsIntWraperPrimit(currentUser.getType(), USER_TYPE_ROOT));
    }


    /**
    *  @Description    ：验证当前用户是否有权操作菜单
    *  @Method_Name    ：doMenusValidate
    *  @param currentUser
    *  @param regUserDao
    *  @param operatorMenus
    *  @return com.yirun.framework.core.model.ResponseEntity
    *  @Creation Date  ：2018/6/13
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    private static ResponseEntity<List<Integer>> doMenusValidate(RegUser currentUser, RegUserDao regUserDao, Collection<Integer> operatorMenus) {
        //非root用户判定权限
        List<Integer> currentUsersMenusIds = null;
        if (!userIsROOT(currentUser)) {
				/*检验当前操作用户是否有这些角色所拥有的菜单(菜单与权限相绑定,因此,菜单在此代表权限),如果分配了自己没有
			     的菜单,返回越权操作。*/
            currentUsersMenusIds = regUserDao.findUserMenuByLogin(String.valueOf(currentUser.getLogin()), currentUser.getType(),null);
            if (logger.isInfoEnabled()) {
                logger.info("当前操作用户: {}, 验证菜单权限, 当前用户菜单: {}, 需要操作的菜单: {}", currentUser.getNickName(), currentUsersMenusIds, operatorMenus);
            }
            //越权使用
            if (BaseUtil.collectionIsEmpty(currentUsersMenusIds)
                    || (!currentUsersMenusIds.containsAll(operatorMenus))) {
                return new ResponseEntity(Constants.ERROR, "对不起,您所操作的菜单中有您无法分配的权限!");
            }

        }
        return new ResponseEntity(SUCCESS, currentUsersMenusIds);
    }


}
