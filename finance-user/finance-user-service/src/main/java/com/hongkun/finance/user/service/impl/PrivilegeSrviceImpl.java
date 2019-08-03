package com.hongkun.finance.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.user.dao.SysMenuDao;
import com.hongkun.finance.user.dao.SysMenuPriRelDao;
import com.hongkun.finance.user.dao.SysPrivilegeDao;
import com.hongkun.finance.user.dto.MenuPriDTO;
import com.hongkun.finance.user.dto.PrivilgeDTO;
import com.hongkun.finance.user.model.SysPrivilege;
import com.hongkun.finance.user.service.PrivilegeSrvice;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.BeanPropertiesUtil;
import com.yirun.framework.core.utils.pager.Pager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.yirun.framework.core.model.ResponseEntity.ERROR;
import static com.yirun.framework.core.model.ResponseEntity.SUCCESS;
import static org.apache.commons.lang.math.NumberUtils.INTEGER_ZERO;

/**
 * @Description : 权限操作实现类
 * @Project : finance
 * @Program Name  : com.hongkun.finance.user.service.impl.PrivilegeSrviceImpl
 * @Author : zhongpingtang@yiruntz.com 唐忠平
 */
@Service
public class PrivilegeSrviceImpl implements PrivilegeSrvice {
    private static final Logger logger = LoggerFactory.getLogger(PrivilegeSrviceImpl.class);
    @Autowired
    private SysPrivilegeDao privilegeDao;

    @Autowired
    private SysMenuPriRelDao menuPriRelDao;

    @Autowired
    private SysMenuDao menuDao;

    @Override
    public Pager listPrivileges(SysPrivilege sysPrivilege, Pager pager) {
        sysPrivilege.setSortColumns("modify_time desc");
        return privilegeDao.findByCondition(sysPrivilege, pager);
    }

    @Override
    public ResponseEntity saveOrUpdatePrivilege(SysPrivilege sysPrivilege, Boolean saveFlag) {
        if (logger.isInfoEnabled()) {
            logger.info("方法名: saveOrUpdatePrivilege, 保存或者修改一个权限, 权限信息: {}, saveFlag: {}", sysPrivilege, saveFlag);
        }
        try {
            return (saveFlag ?
                    (privilegeDao.save(sysPrivilege) > INTEGER_ZERO) : (privilegeDao.update(sysPrivilege) > INTEGER_ZERO)) ?
                    SUCCESS : ERROR;
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("保存或者修改一个权限, 权限信息: {}, saveFlag: {}\n异常信息: ", sysPrivilege, saveFlag, e);
            }
            throw new GeneralException("保存或者修改一个权限,请重试");
        }

    }

    @Override
    public List<PrivilgeDTO> listPrivilegesNoPager() {
        List<SysPrivilege> allPrivileges = privilegeDao.findByCondition(new SysPrivilege());
        //转换
        List<PrivilgeDTO> allDto = new ArrayList<>();
        if (!BaseUtil.collectionIsEmpty(allPrivileges)) {
            allDto.addAll(allPrivileges.stream().map(e -> {
                PrivilgeDTO dto = new PrivilgeDTO();
                BeanPropertiesUtil.splitProperties(e, dto);
                return dto;
            }).collect(Collectors.toList()));
        }
        return allDto;
    }

    @Override
    public ResponseEntity deletePrivilege(SysPrivilege sysPrivilege) {
        if (logger.isInfoEnabled()) {
            logger.info("方法名: deletePrivilege, 删除权限, 权限信息: {}", sysPrivilege);
        }
        try {
            /**
             * step 1:解除权限与菜单的关联关系
             */
            menuPriRelDao.clearMenuPrisRelByMenuIdOrPriId(new MenuPriDTO(null, Arrays.asList(sysPrivilege.getId())));
            /**
             * step 3:删除权限
             */
            sysPrivilege.setState(INTEGER_ZERO);
            return saveOrUpdatePrivilege(sysPrivilege, false);
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("删除权限, sysMenu: {}\n异常信息: ", sysPrivilege, e);
            }
            throw new GeneralException("删除权限失败,请重试");
        }
    }

    @Override
    public List<String> finAuthUrlsByIds(List<String> authIds) {
        return privilegeDao.finAuthUrlsByIds(authIds);
    }
}
