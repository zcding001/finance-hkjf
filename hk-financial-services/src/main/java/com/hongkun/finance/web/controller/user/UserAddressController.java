package com.hongkun.finance.web.controller.user;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.model.RegUserAddress;
import com.hongkun.finance.user.service.RegUserAddressService;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.model.ResponseEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.yirun.framework.core.commons.Constants.ERROR;
import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * @Description : 用户收货地址controller类
 * @Project : framework
 * @Program Name  : com.hongkun.finance.web.controller.user.UserAddressController
 * @Author : pengwu@hongkun.com.cn 吴鹏
 */
@Controller
@RequestMapping("/userAddressController")
public class UserAddressController {

    @Reference
    RegUserAddressService regUserAddressService;

    /**
     * 保存用户的收货地址
     * @param regUserAddress     收货地址信息
     * @return : ResponseEntity
     * @Description : 保存用户的收货地址
     * @Method_Name : saveUserAddress
     * @Creation Date  : 2018年01月05日 下午17:45:50
     * @Author : pengwu@hongkun.com.cn
     */
    @RequestMapping("/saveUserAddress")
    @ResponseBody
    public ResponseEntity saveUserAddress(RegUserAddress regUserAddress){
        RegUser regUser = BaseUtil.getLoginUser();
        regUserAddress.setRegUserId(regUser.getId());
        regUserAddressService.insertRegUserAddress(regUserAddress);
        return new ResponseEntity(SUCCESS,"保存成功");
    }
    /**
     * 更新用户的收货地址
     * @param regUserAddress     收货地址信息
     * @return : ResponseEntity
     * @Description : 更新用户的收货地址
     * @Method_Name : updateUserAddress
     * @Creation Date  : 2018年01月10日 上午11:26:50
     * @Author : pengwu@hongkun.com.cn
     */
    @RequestMapping("/updateUserAddress")
    @ResponseBody
    public ResponseEntity updateUserAddress(RegUserAddress regUserAddress){
        RegUser regUser = BaseUtil.getLoginUser();
        if (regUserAddress.getId() == null){
            return new ResponseEntity(ERROR,"不是有效的用户地址！");
        }
        return regUserAddressService.updateRegUserAddress(regUser.getId(),regUserAddress);
    }
    /**
     * 获取用户的收货地址
     * @return : ResponseEntity
     * @Description : 获取用户的收货地址
     * @Method_Name : getUserAddressList
     * @Creation Date  : 2018年01月05日 下午17:45:50
     * @Author : pengwu@hongkun.com.cn
     */
    @RequestMapping("/getUserAddressList")
    @ResponseBody
    public ResponseEntity getUserAddressList(){
        RegUser regUser = BaseUtil.getLoginUser();
        RegUserAddress condition = new RegUserAddress();
        condition.setRegUserId(regUser.getId());
        condition.setSortColumns("state desc");
        return new ResponseEntity(SUCCESS,regUserAddressService.findRegUserAddressList(condition));
    }
    /**
     * 设置用户默认地址
     * @param addressId         收货地址id
     * @return : ResponseEntity
     * @Description : 设置用户默认地址
     * @Method_Name : setDefaultAddress
     * @Creation Date  : 2018年01月09日 下午17:45:50
     * @Author : pengwu@hongkun.com.cn
     */
    @RequestMapping("/setDefaultAddress")
    @ResponseBody
    public ResponseEntity setDefaultAddress(int addressId){
        RegUser regUser = BaseUtil.getLoginUser();
        return regUserAddressService.setDefaultAddress(regUser.getId(),addressId);
    }
    /**
     * 删除用户收货地址
     * @param addressId         收货地址id
     * @return : ResponseEntity
     * @Description : 设置用户默认地址
     * @Method_Name : delAddress
     * @Creation Date  : 2018年01月10日 上午11:13:50
     * @Author : pengwu@hongkun.com.cn
     */
    @RequestMapping("/delAddress")
    @ResponseBody
    public ResponseEntity delAddress(int addressId){
        RegUser regUser = BaseUtil.getLoginUser();
        return regUserAddressService.delAddress(regUser.getId(),addressId);
    }

}
