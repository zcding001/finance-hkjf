package com.hongkun.finance.web.controller.house;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.fund.facade.HouseInfoFacade;
import com.hongkun.finance.fund.service.HouseProInfoService;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.model.RegUserDetail;
import com.hongkun.finance.user.service.RegUserDetailService;
import com.hongkun.finance.user.support.security.OperationTypeEnum;
import com.hongkun.finance.user.support.security.component.annotation.AskForPermission;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description : 房产投资
 * @Project : finance-hkjf
 * @Program Name  : com.hongkun.finance.web.controller.house
 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
 */
@Controller
@RequestMapping("/houseController")
public class HouseController {
    @Reference
    private HouseProInfoService houseProInfoService;
    @Reference
    private HouseInfoFacade houseInfoFacade;
    @Reference
    private RegUserDetailService regUserDetailService;
    /**
     *  @Description    ：查询房产投资列表（分页）
     *  @Method_Name    ：investHouseList
     *  @return com.yirun.framework.core.model.ResponseEntity
     *  @Creation Date  ：2018/10/9
     *  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
     */
    @RequestMapping("/investHouseList")
    @ResponseBody
    @AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
    public ResponseEntity investHouseList(Pager pager){
        return new ResponseEntity<>(Constants.SUCCESS, houseProInfoService.findHouseInfoVoPageList(pager,null));
    }

    /**
    *  @Description    ：展示房产详情
    *  @Method_Name    ：showHouseInfoDetail
    *  @param infoId  房产项目id
    *  @return com.yirun.framework.core.model.ResponseEntity
    *  @Creation Date  ：2018/10/9
    *  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
    */
    @RequestMapping("/showHouseInfoDetail")
    @ResponseBody
    public ResponseEntity showHouseInfoDetail(Integer infoId){
        return houseInfoFacade.showHouseInfoDetail(BaseUtil.getLoginUserId(),infoId);
    }

    /**
    *  @Description    ：项目预约
    *  @Method_Name    ：reservation
    *  @param infoId  项目id
    *  @param personNum 参与人数
    *  @return com.yirun.framework.core.model.ResponseEntity
    *  @Creation Date  ：2018/10/9
    *  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
    */
    @RequestMapping("/reservation")
    @ResponseBody
    public ResponseEntity reservation(Integer infoId,Integer personNum){
        RegUser regUser = BaseUtil.getLoginUser();
        RegUserDetail regUserDetail = BaseUtil.getRegUserDetail(() -> this.regUserDetailService.findRegUserDetailByRegUserId(regUser.getId()));
        return houseInfoFacade.reservation(regUserDetail,infoId,personNum,String.valueOf(regUser.getLogin()));
    }
 }
