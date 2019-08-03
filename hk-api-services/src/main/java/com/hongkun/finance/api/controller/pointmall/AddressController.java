package com.hongkun.finance.api.controller.pointmall;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.user.constants.UserAddressConstants;
import com.hongkun.finance.user.model.DicArea;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.model.RegUserAddress;
import com.hongkun.finance.user.service.DicAreaService;
import com.hongkun.finance.user.service.RegUserAddressService;
import com.hongkun.finance.user.service.RegUserCommunityService;
import com.hongkun.finance.user.support.security.OperationTypeEnum;
import com.hongkun.finance.user.support.security.component.annotation.AskForPermission;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.support.validate.SAVE;
import com.yirun.framework.core.utils.AppResultUtil;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.hongkun.finance.user.constants.UserAddressConstants.ADDRESS_STATE_DEFAULT;

/**
 * 地址相关接口
 *
 * @author :zhongpingtang
 */
@Controller
@RequestMapping("/addressController")
public class AddressController {


    @Reference
    private RegUserAddressService regUserAddressService;

    @Reference
    private RegUserCommunityService communityService;

    @Reference
    private DicAreaService dicAreaService;

    /**
     * 保存用户的收货地址
     *
     * @param regUserAddress 收货地址信息
     */
    @RequestMapping("/saveUserAddress")
    @ResponseBody
    public Map<String, Object> saveUserAddress(@Validated({SAVE.class}) RegUserAddress regUserAddress) {
        RegUser regUser = BaseUtil.getLoginUser();
        regUserAddress.setRegUserId(regUser.getId());
        String province = regUserAddress.getProvince();
        //特殊省市处理
        String city = regUserAddress.getCity();
        if(isAddressCode(province)){
            regUserAddress.setCity(addressMap.get(province));
            regUserAddress.setCounty(city);
        }
        regUserAddressService.insertRegUserAddress(regUserAddress);
        return AppResultUtil.successOfMsg("保存成功");
    }

    /**
     * 更新用户的收货地址
     *
     * @param regUserAddress 收货地址信息
     */
    @RequestMapping("/updateUserAddress")
    @ResponseBody
    public Map<String, Object> updateUserAddress(RegUserAddress regUserAddress) {
        RegUser regUser = BaseUtil.getLoginUser();
        if (regUserAddress.getId() == null) {
            return AppResultUtil.errorOfMsg("不是有效的用户地址！");
        }
        String province = regUserAddress.getProvince();
        //特殊省市处理
        String city = regUserAddress.getCity();
        if(isAddressCode(province)){
            regUserAddress.setCity(addressMap.get(province));
            regUserAddress.setCounty(city);
        }
        ResponseEntity<?> responseEntity = regUserAddressService.updateRegUserAddress(regUser.getId(), regUserAddress);
        if (BaseUtil.isResponseSuccess(responseEntity)) {
            return AppResultUtil.successOfMsg("更新成功");
        }
        return AppResultUtil.errorOfMsg((String) responseEntity.getResMsg());
    }

    /**
     * 获取用户的收货地址
     */
    @RequestMapping("/getUserAddressList")
    @ResponseBody
    public Map<String, Object> getUserAddressList() {
        RegUser regUser = BaseUtil.getLoginUser();
        RegUserAddress condition = new RegUserAddress();
        condition.setRegUserId(regUser.getId());
        condition.setSortColumns("state desc");
        String[] includeProperteis = {"id", "state", "consignee", "mobilePhone", "district", "province", "city","county"};
        List<RegUserAddress> regUserAddressList = regUserAddressService.findRegUserAddressList(condition);
        if (!BaseUtil.collectionIsEmpty(regUserAddressList)) {
            Optional<RegUserAddress> defaultAdress = regUserAddressList.stream().filter(e -> ADDRESS_STATE_DEFAULT == e.getState()).findAny();
            if (defaultAdress.isPresent()) {
                RegUserAddress dfa = defaultAdress.get();
                regUserAddressList.remove(dfa);
                //置顶默认地址
                regUserAddressList.add(0, dfa);
            }
        }
        return AppResultUtil.successOfListInProperties(regUserAddressList, null, includeProperteis)
                            .processObjInList((Map<String, Object> tempMap) -> {
                                String provinceId = (String) tempMap.get("province");
                                String cityId = (String) tempMap.get("city");
                                //直辖市编码
                                if (isSpecialAddressCode(cityId)) {
                                    cityId = (String) tempMap.get("county");
                                }
                                DicArea tempArea = new DicArea();
                                List<DicArea> tempAreaList;
                                tempArea.setAreaCode(provinceId);
                                if (!BaseUtil.collectionIsEmpty(tempAreaList = dicAreaService.findDicAreaList(tempArea))) {
                                    tempArea = tempAreaList.get(0);
                                    tempMap.put("province", tempArea.getAreaName());
                                }
                                tempArea = new DicArea();
                                tempArea.setAreaCode(cityId);
                                if (!BaseUtil.collectionIsEmpty(tempAreaList = dicAreaService.findDicAreaList(tempArea))) {
                                    tempArea = tempAreaList.get(0);
                                    tempMap.put("city", tempArea.getAreaName());
                                }
                                tempMap.put("provinceId", provinceId);
                                tempMap.put("cityId", cityId);


                            });
    }

    /**
     * 设置用户默认地址
     *
     * @param addressId 收货地址id
     */
    @RequestMapping("/setDefaultAddress")
    @ResponseBody
    public Map<String, Object> setDefaultAddress(@RequestParam("addressId") Integer addressId) {
        RegUser regUser = BaseUtil.getLoginUser();
        if (BaseUtil.isResponseSuccess(regUserAddressService.setDefaultAddress(regUser.getId(), addressId))) {
            return AppResultUtil.successOfMsg("设置成功");
        }
        return AppResultUtil.errorOfMsg("设置失败，请重试");
    }

    /**
     * 删除用户收货地址
     *
     * @param addressId 收货地址id
     */
    @RequestMapping("/delAddress")
    @ResponseBody
    public Map<String, Object> delAddress(@RequestParam("addressId") Integer addressId) {
        RegUser regUser = BaseUtil.getLoginUser();
        ResponseEntity<?> resultResponse = regUserAddressService.delAddress(regUser.getId(), addressId);
        if (BaseUtil.isResponseSuccess(resultResponse)) {
            return AppResultUtil.successOfMsg("删除成功");
        }
        return AppResultUtil.errorOfMsg((String) resultResponse.getResMsg());
    }


    /**
     * 查询物业地址
     *
     * @return
     */
    @RequestMapping("/loadProperties")
    @ResponseBody
    public Map<String, Object> loadProperties() {
        List<Map<String, Object>> maps = communityService.loadProperties();
        return AppResultUtil.successOfList(maps).reNameParameterInList("value", "id");

    }


    /**
     * 查询小区地址
     *
     * @return
     */
    @RequestMapping("/loadOfflineStoreAddress")
    @ResponseBody
    public Map<String, Object> loadOfflineStoreAddressByPropertyId(@RequestParam("propertyId") Integer propertyId) {
        List<Map<String, Object>> maps = communityService.loadOfflineStoreAddressByPropertyId(propertyId);
        return AppResultUtil.successOfList(maps).reNameParameterInList("value", "id")
                            .reNameParameterInList("name", "communityName");

    }

    private static final Map<String, String> special_AddressMap = new HashMap() {
        {
            //北京
            put("110100", "110000");
            //上海
            put("310100", "310000");
            //重庆
            put("500100", "500000");
            //天津
            put("120100", "120000");
        }
    };
    
    private static final Map<String, String> addressMap = new HashMap() {
        {
            //北京
            put("110000", "110100");
            //上海
            put("310000", "310100");
            //重庆
            put("500000", "500100");
            //天津
            put("120000", "120100");
        }
    };

    /**
     * 是否是直辖市code
     * @param code
     * @return
     */
    private boolean isAddressCode(String code) {
        return addressMap.containsKey(code);
    }
    /**
     * 是否是直辖市code
     * @param code
     * @return
     */
    private boolean isSpecialAddressCode(String code) {
        return special_AddressMap.containsKey(code);
    }
    /**
     *  @Description    : 获取用户默认地址
     *  @Method_Name    : getUserDefaultAddress;
     *  @return
     *  @return         : Map<String,Object>;
     *  @Creation Date  : 2018年11月5日 下午4:36:28;
     *  @Author         : yanbinghuang@hongkun.com.cn 黄艳兵;
     */
    @RequestMapping("/getUserDefaultAddress")
    @ResponseBody
    public Map<String, Object> getUserDefaultAddress() {
       RegUser regUser = BaseUtil.getLoginUser();
        RegUserAddress condition = new RegUserAddress();
        condition.setRegUserId(regUser.getId());
        condition.setState(UserAddressConstants.ADDRESS_STATE_DEFAULT);
        condition.setSortColumns("state desc");
        String[] includeProperteis = {"id", "state", "consignee", "mobilePhone", "district", "province", "city","county"};
        List<RegUserAddress> regUserAddressList = regUserAddressService.findRegUserAddressList(condition);
        if(regUserAddressList != null && regUserAddressList.size()>0){
            RegUserAddress regUserAddress = regUserAddressList.get(0);
            String provinceId = regUserAddress.getProvince();
            String cityId = regUserAddress.getCity();
            //直辖市编码
            if (isSpecialAddressCode(cityId)) {
                cityId = regUserAddress.getCounty();
            }
            DicArea tempArea = new DicArea();
            List<DicArea> tempAreaList;
            tempArea.setAreaCode(provinceId);
            if (!BaseUtil.collectionIsEmpty(tempAreaList = dicAreaService.findDicAreaList(tempArea))) {
                tempArea = tempAreaList.get(0);
                regUserAddress.setProvince(tempArea.getAreaName());
            }
            tempArea = new DicArea();
            tempArea.setAreaCode(cityId);
            if (!BaseUtil.collectionIsEmpty(tempAreaList = dicAreaService.findDicAreaList(tempArea))) {
                tempArea = tempAreaList.get(0);
                regUserAddress.setCity(tempArea.getAreaName());
            }
            return AppResultUtil.successOfInProperties(regUserAddress, null, includeProperteis)
                    .addResParameter("provinceId", provinceId)
                    .addResParameter("cityId",cityId);
        }else{
            return AppResultUtil.errorOfMsg("用户还没有设置默认地址！");
        }
        
    }


}
