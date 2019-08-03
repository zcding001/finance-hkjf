package com.hongkun.finance.api.controller.house;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.contract.model.vo.CommonInvestConInfoVO;
import com.hongkun.finance.fund.constants.FundConstants;
import com.hongkun.finance.fund.facade.HouseInfoFacade;
import com.hongkun.finance.fund.model.FundAdvisory;
import com.hongkun.finance.fund.model.HouseProInfo;
import com.hongkun.finance.fund.model.vo.HouseInfoAndDetail;
import com.hongkun.finance.fund.model.vo.HouseProInfoVo;
import com.hongkun.finance.fund.service.FundAdvisoryService;
import com.hongkun.finance.fund.service.HouseProInfoService;
import com.hongkun.finance.roster.model.SysFunctionCfg;
import com.hongkun.finance.roster.service.SysFunctionCfgService;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.model.RegUserDetail;
import com.hongkun.finance.user.service.DicDataService;
import com.hongkun.finance.user.support.security.OperationTypeEnum;
import com.hongkun.finance.user.support.security.annotation.ActionLog;
import com.hongkun.finance.user.support.security.component.annotation.AskForPermission;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.AppResultUtil;
import com.yirun.framework.core.utils.PropertiesHolder;
import com.yirun.framework.core.utils.pager.Pager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hongkun.finance.roster.constants.RosterConstants.SYS_FUNCTION_CFG_HOUSE_INVEST;
import static com.hongkun.finance.roster.constants.RosterConstants.SYS_FUNCTION_CFG_INABLE;

/**
 * @Description : TODO
 * @Project : finance-hkjf
 * @Program Name  : com.hongkun.finance.api.controller.house
 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
 */
@Controller
@RequestMapping("/houseController")
public class HouseController {
    @Reference
    private SysFunctionCfgService sysFunctionCfgService;
    @Reference
    private HouseProInfoService houseProInfoService;
    @Reference
    private HouseInfoFacade houseInfoFacade;
    @Reference
    private DicDataService dicDataService;
    @Reference
    private FundAdvisoryService fundAdvisoryService;


    /**
    *  @Description    ：首页--展示房产信息
    *  @Method_Name    ：findIndexFundInfos
    *
    *  @return java.util.Map<java.lang.String,java.lang.Object>
    *  @Creation Date  ：2018/10/24
    *  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
    */
    @RequestMapping("/getHouseInfoForIndex")
    @ResponseBody
    public Map<String, Object> getHouseInfoForIndex() {
        //查询房产开关
        SysFunctionCfg cfg = new SysFunctionCfg();
        cfg.setFuncCode(SYS_FUNCTION_CFG_HOUSE_INVEST);
        cfg.setIsEnable(SYS_FUNCTION_CFG_INABLE);
        int  count = sysFunctionCfgService.findSysFunctionCfgCount(cfg);
        if (count>0){
            return AppResultUtil.successOfMsg("查询成功")
                    .addResParameter("showInfo",
                            "集中展示了鸿坤集团地产项目，为意向购买鸿坤房产项目的用户，提供了在线了解项目信息及预约看房服务。");
        }
        return AppResultUtil.errorOfMsg("不需要展示房产信息");
    }


    /**
    *  @Description    ：获取房产产品列表
    *  @Method_Name    ：getHouseProInfoList
    *
    *  @return java.util.Map<java.lang.String,java.lang.Object>
    *  @Creation Date  ：2018/10/24
    *  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
    */
    @RequestMapping("/getHouseProInfoList")
    @ResponseBody
    public Map<String, Object> getHouseProInfoList(Pager pager) {
        String hkjfBasicUrl =  PropertiesHolder.getProperty("hkjf_basic_url");
        Pager resultPager = houseProInfoService.findHouseInfoVoPageList(pager,hkjfBasicUrl);
        return AppResultUtil.successOfPager(resultPager,"systemName","sortColumns","queryColumnId","homeArea","proType");
    }


    /**
    *  @Description    ：根据房产id跳转详情页面
    *  @Method_Name    ：showHouseDetailById
    *  @param infoId
    *  @return org.springframework.web.servlet.ModelAndView
    *  @Creation Date  ：2018/11/12
    *  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
    */
    @RequestMapping("/showHouseDetailById")
    @ResponseBody
    @AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
    public ModelAndView showHouseDetailById(Integer infoId, Integer source, String sessionId,String access_token){
        ModelAndView result = new ModelAndView("house/houseDetail");
        RegUser regUser = StringUtils.isNotBlank(sessionId)?BaseUtil.getRegUser(null):null;
        ResponseEntity responseEntity  =  houseInfoFacade.showHouseInfoDetail(regUser==null?null:regUser.getId(),infoId);
        if(!BaseUtil.error(responseEntity)){
            Map<String,Object> data = responseEntity.getParams();
            HouseInfoAndDetail infoAndDetail = (HouseInfoAndDetail) data.get("infoAndDetail");
            if (infoAndDetail!=null){
                if (StringUtils.isNotBlank(infoAndDetail.getBuildType())){
                    infoAndDetail.setBuildType(dicDataService.findNamesByValues("house","build_type",infoAndDetail.getBuildType(),","));
                }
                if (StringUtils.isNotBlank(infoAndDetail.getFeature())){
                    infoAndDetail.setFeature(dicDataService.findNamesByValues("house","feature",infoAndDetail.getFeature(),","));
                }
                if (StringUtils.isNotBlank(infoAndDetail.getRedecorate())){
                    infoAndDetail.setRedecorate(dicDataService.findNamesByValues("house","redecorate",infoAndDetail.getRedecorate(),","));
                }
                if (StringUtils.isNotBlank(infoAndDetail.getRoomType())){
                    infoAndDetail.setRoomType(dicDataService.findNamesByValues("house","room_type",infoAndDetail.getRoomType(),","));
                }
                if (infoAndDetail.getSaleState() != null ){
                    infoAndDetail.setSaleStateStr(dicDataService.findNamesByValues("house","sale_state",String.valueOf(infoAndDetail.getSaleState()),","));
                }
                data.put("infoAndDetail",infoAndDetail);
            }
            result.addObject("data",data);
            result.addObject("infoId",infoId);
            result.addObject("source",source);
            result.addObject("sessionId",sessionId);
            result.addObject("access_token",access_token);
        }else{
            result.addObject("errorMsg","房产信息不存在");
        }
        return result;
    }

    /**
    *  @Description    ：跳转预约页面
    *  @Method_Name    ：toReservation
    *  @param infoId
    *  @param source
    *  @return org.springframework.web.servlet.ModelAndView
    *  @Creation Date  ：2018/11/13
    *  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
    */
    @RequestMapping("/toReservation")
    @ResponseBody
    public ModelAndView toReservation(Integer infoId,Integer source,String sessionId,String access_token){
        ModelAndView result = new ModelAndView("house/houseReservation");
        result.addObject("infoId",infoId);
        result.addObject("source",source);
        result.addObject("sessionId",sessionId);
        result.addObject("access_token",access_token);
        return result;
    }


    /**
    *  @Description    ：预约房产
    *  @Method_Name    ：reservation
    *  @param infoId
    *  @param personNum
    *  @return java.util.Map<java.lang.String,java.lang.Object>
    *  @Creation Date  ：2018/11/13
    *  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
    */
    @RequestMapping("/reservation")
    @ResponseBody
    public Map<String,Object> reservation(Integer infoId,Integer personNum){
        Map<String,Object> result = new HashMap<String,Object>();
        HouseProInfo proInfo =  houseProInfoService.findHouseProInfoById(infoId);
        if (proInfo == null){
            result.put("code",500);
            result.put("msg","房产项目不存在");
        }
        RegUser regUser = BaseUtil.getRegUser(null);
        RegUserDetail regUserDetail = BaseUtil.getRegUserDetail(null);

        int funCount = fundAdvisoryService.findFundAdvisoryCount(regUser.getId(),infoId,
                FundConstants.FUND_PROJECT_PARENT_TYPE_HOUSE);
        if (funCount>0){
            result.put("code",201);
            result.put("msg","此项目已经预约过，不能重复预约");
            return result;
        }

        FundAdvisory advisory = new FundAdvisory();
        advisory.setRegUserId(regUserDetail.getRegUserId());
        advisory.setState(1);
        advisory.setProjectParentType(FundConstants.FUND_PROJECT_PARENT_TYPE_HOUSE);
        advisory.setInfoIds(String.valueOf(infoId));
        advisory.setName(regUserDetail.getRealName());
        advisory.setTel(String.valueOf(regUser.getLogin()));
        advisory.setRemark(String.valueOf(personNum));
        fundAdvisoryService.insertFundAdvisory(advisory);
        result.put("code",1000);
        result.put("msg","预约成功");
        return result;
    }
}
