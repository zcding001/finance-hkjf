package com.hongkun.finance.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.user.dao.*;
import com.hongkun.finance.user.dto.RoleMenuDTO;
import com.hongkun.finance.user.dto.UserRoleDTO;
import com.hongkun.finance.user.model.*;
import com.hongkun.finance.user.model.vo.UserRolesVo;
import com.hongkun.finance.user.service.RoleService;
import com.hongkun.finance.user.support.AuthSecurityManager;
import com.hongkun.finance.user.support.security.AuthUtil;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.CommonUtils;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.redis.JedisClusterUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

import static com.yirun.framework.core.model.ResponseEntity.ERROR;
import static com.yirun.framework.core.model.ResponseEntity.SUCCESS;
import static org.apache.commons.lang.math.NumberUtils.INTEGER_ZERO;

/**
 * @Description : 权限服务实现类
 * @Project : finance
 * @Program Name  : com.hongkun.finance.user.service.impl.AuthServiceImpl
 * @Author : zhongpingtang@yiruntz.com 唐忠平
 */
@Service
public class RoleServiceImpl implements RoleService {

    private static final Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);

    @Autowired
    private SysRoleDao sysRoleDao;

    @Autowired
    private SysUserRoleRelDao userRoleRelDao;

    @Autowired
    private SysRoleMenuRelDao roleMenuRelDao;

    @Autowired
    private SysMenuDao menuDao;

    @Autowired
    private RegUserDao regUserDao;

    @Autowired
    private SysRoleMenuRelDao sysRoleMenuRelDao;
    @Autowired
    private SysUserRoleRelDao sysUserRoleRelDao;






    @Override
    public Pager listAllRoles(SysRole sysRole, Pager pager) {
        return sysRoleDao.findByCondition(sysRole, pager);
    }

    @Override
    public ResponseEntity saveOrUpdateRole(SysRole sysRole, boolean saveFlag) {
        if (logger.isInfoEnabled()) {
            logger.info("方法名: saveOrUpdateRole, 更新或者删除角色, 角色信息: {}, saveFlag: {}", sysRole,saveFlag);
        }
        try {
            return (saveFlag?
                    (sysRoleDao.save(sysRole)> INTEGER_ZERO):(sysRoleDao.update(sysRole)>INTEGER_ZERO))?
                    SUCCESS:ERROR;
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("更新或者删除角色失败, 角色信息: {}, saveFlag: {}\n异常信息: ", sysRole,saveFlag, e);
            }
            throw new GeneralException("更新或者删除角色失败,请重试");
        }
    }


    @Override
    public Pager findAllRoles(SysRole sysRole, Pager pager) {
        return sysRoleDao.findByCondition(sysRole, pager);
    }

    @Override
    public Integer roleUnioToUserCount(Integer userId) {
        return userRoleRelDao.getTotalCount(new SysUserRoleRel(userId,null));
    }

    @Override
    public List<SysMenu> findRolesMenu(SysRole sysRole) {
        return roleMenuRelDao.findRolesBindMenuIds(sysRole.getId()).stream()
                .map(k -> menuDao.findByPK(Long.valueOf(k), SysMenu.class))
                .collect(Collectors.toList());

    }

    @Override
    public ResponseEntity deleteRole(SysRole sysRole) {
        if (logger.isInfoEnabled()) {
            logger.info("方法名: deleteRole, 删除一个角色, 角色信息: {}", sysRole);
        }
        try {
            if(allowDeleteRole(sysRole)){
                /**
                 * step 1:删除与角色与菜单的关联关系
                 */
                roleMenuRelDao.clearRoleMenuRelByroleIdOrMenuId(new RoleMenuDTO(Arrays.asList(sysRole.getId()), null));

                /**
                 * step 2:解除角色与用户的关联关系
                 */
                userRoleRelDao.clearUserRoleRelByUserIdOrRoleId(new UserRoleDTO(null, Arrays.asList(sysRole.getId())));

                /**
                 * step 2:删除角色的信息
                 */
                //置为删除位置
                sysRole.setState(INTEGER_ZERO);
                return saveOrUpdateRole(sysRole,false);
            }
            return new ResponseEntity(Constants.ERROR,"权限已经与角色相关联，不允许删除");
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("删除一个角色失败, 角色信息: {}\n异常信息: ", sysRole, e);
            }
            throw new GeneralException("删除一个角色失败,请重试");
        }
    }

    @Override
    public ResponseEntity bindMenuToRole(Set<Integer> menus, Integer roleId,Integer userId) {
        if (logger.isInfoEnabled()) {
            logger.info("方法名: bindMenuToRole, 绑定菜单到角色中, 角色ID: {}, 菜单IDS: {}", roleId,menus);
        }
        try {
            //验权
            ResponseEntity validateResult = AuthSecurityManager.checkUserMenusWithOprateMenus(regUserDao, userId, menus);
            if (!validateResult.validSuc()) {
                //检验为越权使用,驳回请求
                return validateResult;
            }
            return ((BaseUtil.collectionIsEmpty(menus) ?
                    roleMenuRelDao.clearRoleMenuRelByroleIdOrMenuId(new RoleMenuDTO(Arrays.asList(roleId),null)) : dobindMenuToRole(roleId, menus))> INTEGER_ZERO) ?
                    ResponseEntity.SUCCESS : ResponseEntity.ERROR;
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("绑定菜单到角色失败, 角色ID: {}, 菜单IDS: {}\n异常信息: ", roleId,menus, e);
            }
            throw new GeneralException("绑定菜单到角色失败,请重试");
        }

    }

    @Override
    public List<SysRole> findAllRolesNoPager() {
        return sysRoleDao.findByCondition(new SysRole());
    }

    @Override
    public Pager findUserRolesVoList(UserRolesVo userRolesVo,Pager pager) {
        return sysRoleDao.findByCondition(userRolesVo,pager,SysRole.class,".findUserRolesVoList");
    }

    @Override
    public ResponseEntity saveUserRoles(String login, Integer sysRoleId) {
        //1、校验参数准确性  用户是否存在，角色是否存在
        if (StringUtils.isNotBlank(login) && sysRoleId != null ){
            RegUser regUser = regUserDao.findRegUserByLogin(Long.valueOf(login));
            if (regUser == null){
                return new ResponseEntity(Constants.ERROR,"手机号不存在");
            }
            SysRole sysRole = sysRoleDao.findByPK(Long.valueOf(sysRoleId),SysRole.class);
            if (sysRole == null){
                return new ResponseEntity(Constants.ERROR,"角色不存在");
            }
            SysUserRoleRel cdt = new SysUserRoleRel();
            cdt.setRegUserId(regUser.getId());
            cdt.setSysRoleId(sysRoleId);
            List<SysUserRoleRel> relResults = sysUserRoleRelDao.findByCondition(cdt);
            if (CommonUtils.isNotEmpty(relResults)){
                return new ResponseEntity(Constants.ERROR,"该用户已绑定过此角色");
            }
            SysUserRoleRel sysUserRoleRel = new SysUserRoleRel();
            sysUserRoleRel.setRegUserId(regUser.getId());
            sysUserRoleRel.setSysRoleId(sysRoleId);
            sysUserRoleRel.setState(1);
            sysUserRoleRelDao.save(sysUserRoleRel);

            //把前台缓存清除
            String menuKey = AuthUtil.getMenuKey(regUser);
            if(JedisClusterUtils.exists(menuKey)){
                JedisClusterUtils.delete(menuKey);
            }
            return new ResponseEntity(Constants.SUCCESS,"添加成功");
        }
        return new ResponseEntity(Constants.ERROR,"手机号或者角色不存在");
    }

    @Override
    public ResponseEntity delUserRoles(Integer id) {
        if (id==null || id.intValue() <=0){
            return new ResponseEntity(Constants.ERROR,"请选择需要删除的关系");
        }
        SysUserRoleRel sysUserRoleRel = sysUserRoleRelDao.findByPK(Long.valueOf(id),SysUserRoleRel.class);
        if (sysUserRoleRel == null){
            return new ResponseEntity(Constants.ERROR,"用户角色关系不存在");
        }
        sysUserRoleRelDao.delete(Long.valueOf(id),SysUserRoleRel.class);

        //把前台缓存清除
        RegUser regUser = regUserDao.findByPK(Long.valueOf(sysUserRoleRel.getRegUserId()),RegUser.class);
        String menuKey = AuthUtil.getMenuKey(regUser);
        if(JedisClusterUtils.exists(menuKey)){
            JedisClusterUtils.delete(menuKey);
        }
        return new ResponseEntity(Constants.SUCCESS,"删除成功");
    }

    @Override
    public ResponseEntity myAccountRoles() {
        List<SysRole> list =  sysRoleDao.findByCondition(new SysRole());
        List<Map<String,Object>>  result = new ArrayList<Map<String,Object>>();
        if (CommonUtils.isNotEmpty(list)){
            list.forEach(sysRole -> {
                Map<String,Object> map = new HashMap<String,Object>();
                map.put("name",sysRole.getRoleName());
                map.put("value",sysRole.getId());
                result.add(map);
            });
        }
        return new ResponseEntity(Constants.SUCCESS,result);
    }

    /**
   *  @Description    ：绑定菜单到角色
   *  @Method_Name    ：dobindMenuToRole
   *  @param roleId
   *  @param newMenus
   *  @return java.lang.Integer
   *  @Creation Date  ：2018/4/24
   *  @Author         ：zhongpingtang@hongkun.com.cn
   */
    private Integer dobindMenuToRole(Integer roleId, Set<Integer> newMenus) {
        SysRoleMenuRel sysRoleMenuRel = new SysRoleMenuRel();
        sysRoleMenuRel.setSysRoleId(roleId);
       return  RelFunction.
                reBindRel(
                roleMenuRelDao.findByCondition(sysRoleMenuRel).stream().map(e->e.getSysMenuId()).collect(Collectors.toList()),
                newMenus,
                unDeleteIds->roleMenuRelDao.clearRoleMenuRelByroleIdOrMenuId(new RoleMenuDTO(null,unDeleteIds)),
                unSaveId->roleMenuRelDao.save(new SysRoleMenuRel(roleId, unSaveId))
                );

    }




  /**
  *  @Description    ：检查是否允许删除角色，如果角色与用户相关联，不允许删除
  *  @Method_Name    ：allowDeleteRole
  *  @param sysRole
  *  @return boolean
  *  @Creation Date  ：2018/4/24
  *  @Author         ：zhongpingtang@hongkun.com.cn
  */
    private boolean allowDeleteRole(SysRole sysRole) {
        //检查角色是否与用户相关联，如果相互关联，则不允许删除
        return INTEGER_ZERO.equals(roleUnioToUserCount(sysRole.getId()));
    }


}
