package com.hongkun.finance.fund.facade.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.fund.facade.HouseInfoFacade;
import com.hongkun.finance.fund.model.FundAdvisory;
import com.hongkun.finance.fund.model.HouseProInfo;
import com.hongkun.finance.fund.model.HouseProPermit;
import com.hongkun.finance.fund.model.HouseProPic;
import com.hongkun.finance.fund.model.vo.HouseInfoAndDetail;
import com.hongkun.finance.fund.service.FundAdvisoryService;
import com.hongkun.finance.fund.service.HouseProInfoService;
import com.hongkun.finance.user.model.RegUserDetail;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.CommonUtils;
import com.yirun.framework.core.utils.PropertiesHolder;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hongkun.finance.fund.constants.FundConstants.FUND_PROJECT_PARENT_TYPE_HOUSE;
import static com.hongkun.finance.fund.constants.HouseConstants.HOUSE_PIC_TYPE_HOME;
import static com.hongkun.finance.fund.constants.HouseConstants.HOUSE_PIC_TYPE_OTHER;

/**
 * @Description : 房产facade
 * @Project : finance-hkjf
 * @Program Name  : com.hongkun.finance.fund.facade.impl
 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
 */
@Service
public class HouseInfoFacadeImpl implements HouseInfoFacade {

    @Reference
    private HouseProInfoService houseProInfoService;
    @Reference
    private FundAdvisoryService fundAdvisoryService;

    @Override
    public ResponseEntity showHouseInfoDetail(Integer regUserId,Integer infoId) {
            //1、查询基础信息
            HouseInfoAndDetail infoAndDetail =  houseProInfoService.findHouseInfoAndDetailById(infoId);
            if (infoAndDetail!=null){
                Map<String,Object> params = new HashMap<String,Object>();
                List<HouseProPic> picList = houseProInfoService.getHouseProPicList(infoId,null);
                List<HouseProPic> homeProPics = new ArrayList<HouseProPic>(); //2、查询户型图
                List<HouseProPic> otherProPics = new ArrayList<HouseProPic>();//3、查询相册
                HouseProPic indexPic =  null;
                if (CommonUtils.isNotEmpty(picList)){
                    String  basicUrl =  PropertiesHolder.getProperty("oss.url");
                    for (HouseProPic pic :picList){
                        pic.setUrl(basicUrl + "/"+pic.getUrl());
                        if (pic.getType() == HOUSE_PIC_TYPE_HOME){
                            homeProPics.add(pic);
                        }else if (pic.getType() == HOUSE_PIC_TYPE_OTHER){
                            otherProPics.add(pic);
                        }else {
                            indexPic = pic;
                        }
                    }
                }
            //默认封面图
            params.put("indexPic",indexPic);
            params.put("picList",picList);
            params.put("homeProPics",homeProPics);
            params.put("otherProPics",otherProPics);
            //预售证
            List<HouseProPermit> permitList = houseProInfoService.getHouseProPermitList(infoId);
            params.put("permitList",permitList);
            //4、是否维护了销售详情和小区
            Map<String,Object> checkResult =  checkHouseInfoPro(infoAndDetail,permitList);
            params.put("existSalPro",checkResult.get("existSalPro"));
            params.put("existDetailPro",checkResult.get("existDetailPro"));
            //5、是否已经预约
            if (regUserId == null ){
                params.put("isLogin",0);
            }else{
                int count = fundAdvisoryService.findFundAdvisoryCount(regUserId,infoId,FUND_PROJECT_PARENT_TYPE_HOUSE);
                params.put("isReservation",count);
                params.put("isLogin",1);
            }
            params.put("infoAndDetail",infoAndDetail);
            return new ResponseEntity(Constants.SUCCESS,"查询成功",params);
        }
        return new ResponseEntity(Constants.ERROR,"未查询到项目信息");
    }
    @Override
    public ResponseEntity reservation(RegUserDetail regUserDetail, Integer infoId, Integer personNum,String tel) {
        HouseProInfo proInfo = houseProInfoService.findHouseProInfoById(infoId);
        if (proInfo == null ){
            return  new ResponseEntity(Constants.ERROR,"预约项目不存在");
        }
        int count = fundAdvisoryService.findFundAdvisoryCount(regUserDetail.getId(),infoId,FUND_PROJECT_PARENT_TYPE_HOUSE);
        if (count>0){
            return  new ResponseEntity(Constants.ERROR,"此项目已经预约过，不可重复预约");
        }
        FundAdvisory advisory = new FundAdvisory();
        advisory.setTel(tel);
        if(StringUtils.isBlank(regUserDetail.getRealName())){
            advisory.setName("鸿坤金服用户");
        }else{
            advisory.setName(regUserDetail.getRealName());
        }
        advisory.setInfoIds(String.valueOf(infoId));
        advisory.setProjectParentType(FUND_PROJECT_PARENT_TYPE_HOUSE);
        advisory.setRegUserId(regUserDetail.getRegUserId());
        advisory.setModifyUserId(regUserDetail.getRegUserId());
        advisory.setRemark(String.valueOf(personNum));
        //无关参数  设置为默认值
        advisory.setSource(0);
        advisory.setState(1);
        advisory.setAdvisor(0);
        fundAdvisoryService.insertFundAdvisory(advisory);
        return new ResponseEntity(Constants.SUCCESS);
    }

    /**
    *  @Description    ：判断是否维护了销售详情和小区规划模块
    *  @Method_Name    ：checkHouseInfoPro
    *  @param info
    *  @param permitList
    *  @return java.util.Map<java.lang.String,java.lang.Object>
    *  @Creation Date  ：2018/10/10
    *  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
    */
    private Map<String,Object> checkHouseInfoPro(HouseInfoAndDetail info, List<HouseProPermit> permitList){
        Map<String,Object> result = new HashMap<String,Object>();
        result.put("existSalPro", "1");
        result.put("existDetailPro", "1");
        if(info.getSaleState()==null&&StringUtils.isEmpty(info.getStartSaleDate())&&StringUtils.isEmpty(info.getSaleAddress())
                &&StringUtils.isEmpty(info.getRoomType())&&StringUtils.isEmpty(info.getPrefer())&&StringUtils.isEmpty(info.getMakeHouseDate())
                &&StringUtils.isEmpty(info.getSalTel())&&CommonUtils.isEmpty(permitList)){
            result.put("existSalPro", "0");
        }
        if(info.getLandArea()==null && info.getCapRate() ==null && info.getParkSpace() == null && info.getDoorNum() == null
                && info.getProFee() == null && info.getBuildArea() == null && info.getGreenRate() == null
                && info.getFloorNum() == null && StringUtils.isEmpty(info.getProCompany())){
            result.put("existDetailPro", "0");
        }
        return result;
    }
}
