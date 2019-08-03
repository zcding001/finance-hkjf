package com.hongkun.finance.web.controller.fund;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.fund.facade.FundInfoFacade;
import com.hongkun.finance.fund.model.FundAgreement;
import com.hongkun.finance.fund.model.vo.FundInfoVo;
import com.hongkun.finance.fund.service.FundAgreementService;
import com.hongkun.finance.fund.service.FundInfoService;
import com.hongkun.finance.fund.util.FundUtils;
import com.hongkun.finance.info.constant.InfomationConstants;
import com.hongkun.finance.info.model.InfoInformationNews;
import com.hongkun.finance.info.service.InfoInformationNewsService;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.service.DicDataService;
import com.hongkun.finance.user.support.security.OperationTypeEnum;
import com.hongkun.finance.user.support.security.component.annotation.AskForPermission;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.CompareUtil;
import com.yirun.framework.core.utils.pager.Pager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static com.hongkun.finance.fund.constants.FundConstants.*;
import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * @Description : 股权基金首页相关controller
 * @Project : framework
 * @Program Name  : com.hongkun.finance.web.controller.fund.FundIndexController
 * @Author : yuzegu@hongkun.com.cn googe
 */
@Controller
@RequestMapping("/fundIndexController")
public class FundIndexController {

    private static final Logger logger = LoggerFactory.getLogger(FundIndexController.class);

    @Reference
    private FundInfoService fundInfoService;

    @Reference
    private FundInfoFacade fundInfoFacade;

    @Reference
    private DicDataService dicDataService;

    @Reference
    private FundAgreementService fundAgreementService;

    @Reference
    private InfoInformationNewsService infoInformationNewsService;

    /**
     * 首页按类型id查询股权基金项目数据
     * @return ResponseEntity
     */
    @RequestMapping("/fundInfoList")
    @ResponseBody
    public ResponseEntity fundInfoList(FundInfoVo fiv , Pager pager) {
        //return new ResponseEntity(SUCCESS, fundInfoService.findFundInfoList(fundInfo));
        logger.info("fundInfoList获取产品股权信息列表,股权父类型:{}" + (fiv.getParentType() != null?fiv.getParentType():"无"));
        RegUser regUser = BaseUtil.getLoginUser();
        FundInfoVo condition = new FundInfoVo();
        condition.setState(FUND_INFO_STATE_SHELF);
        //condition.setInfoExist(FUND_INFO_EXIST_YES);
        //condition.setParentType(parentType);
        if(fiv.getParentType() != null){
            condition.setParentType(fiv.getParentType());
        }
        if(fiv.getId() != null){
            condition.setId(fiv.getId());
        }
        condition.setSortColumns("info.create_time DESC");
        Pager resultPager = fundInfoService.findFundInfoVoByCondition(condition, pager);
        List<FundInfoVo> resultList = new ArrayList<FundInfoVo>();
        resultPager.getData().forEach(fundInfoVo -> {
            FundInfoVo infoVo = (FundInfoVo) fundInfoVo;
            if(infoVo.getInfoExist() == FUND_INFO_EXIST_YES){
                //开放日描述
                infoVo.setOpenDateDescribe(FundUtils.getFundOpenTime(infoVo));
                //是否在开放日 注：不在开放日设置成停约
                if(!FundUtils.checkOpenDate(infoVo)){
                    infoVo.setSubscribeState(0);
                }
                //起投金额
                if(CompareUtil.gtZero(infoVo.getLowestAmount())) {
                    infoVo.setLowestAmount(infoVo.getLowestAmount().divide(BigDecimal.valueOf(10000),2, RoundingMode.HALF_UP));
                }
                //存续期限
                infoVo.setTermDescribe(FundUtils.getTermDes(infoVo));
            }else{
                infoVo.setName(dicDataService.findNameByValue("fund" ,"project_type", infoVo.getProjectId()));
            }
            //海外基金查询此用户项目预约情况
            if(regUser != null && infoVo.getParentType() != null && infoVo.getParentType() == FUND_PROJECT_PARENT_TYPE_ABROAD){
                FundAgreement fa = new FundAgreement();
                fa.setRegUserId(regUser.getId());
                fa.setFundInfoId(infoVo.getId());
                FundAgreement myFa = fundAgreementService.findFundAgreementInfo(fa);
                if(null != myFa){
                    if(myFa.getState().equals(FUND_ADVISORY_STATE_UNDER_AUDIT)
                            || myFa.getState().equals(FUND_ADVISORY_STATE_QULICATION_PASS)){
                        infoVo.setAuditState(FUND_ADVISORY_STATE_UNDER_AUDIT);
                    }
                    if(myFa.getFundAdvisoryId() == 0){
                        infoVo.setAuditState(FUND_ADVISORY_STATE_INIT);
                    }
                }
            }
            resultList.add(infoVo);
        });
        return new ResponseEntity(SUCCESS, resultList);
    }

    /***
     *
     *  @Description    : 获取股权信息详情认证权限
     *  @Method_Name    : getFundAuthentication;
     *  @return
     *  @return         : ResponseEntity;
     *  @Creation Date  : 2018年8月10日14:28:28
     *  @Author         : yuzegu@hongkun.com.cn ;
     */
    @RequestMapping("/getFundAuthentication")
    @ResponseBody
    public ResponseEntity getFundAuthentication() {
        RegUser regUser = BaseUtil.getLoginUser();
        return new ResponseEntity(SUCCESS, fundInfoFacade.getFundAuthentication(regUser));
    }

    /** 
    * @Description: 查询首页股权和房产的新闻
     * @param 
    * @return: com.yirun.framework.core.model.ResponseEntity<?> 
    * @Author: hanghe@hongkunjinfu.com
    * @Date: 2019/1/3 14:21
    */
    @RequestMapping("searchFundInformations")
    @AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
    @ResponseBody
    public ResponseEntity<?> searchFundInformations() {
        InfoInformationNews infoInformationNews = new InfoInformationNews();
        infoInformationNews.setSortColumns("sort asc,create_time desc limit 6");
        infoInformationNews.setChannel(InfomationConstants.CHANNEL_PC);
        infoInformationNews.setState(InfomationConstants.INFO_VALID);
        infoInformationNews.setPosition(InfomationConstants.INFO_POSITION_FIRST_PAGE);
        infoInformationNews.setType(InfomationConstants.INFO_TYPE_PE);
        List<InfoInformationNews> privateList = infoInformationNewsService.findInfoInformationNewsList(infoInformationNews);
        infoInformationNews.setSortColumns("sort asc,create_time desc limit 2");
        infoInformationNews.setType(InfomationConstants.INFO_TYPE_OVERSEA_FUND);
        List<InfoInformationNews> overseaList = infoInformationNewsService.findInfoInformationNewsList(infoInformationNews);
        infoInformationNews.setType(InfomationConstants.INFO_TYPE_FOT);
        List<InfoInformationNews> trustList = infoInformationNewsService.findInfoInformationNewsList(infoInformationNews);
        infoInformationNews.setType(InfomationConstants.INFO_TYPE_HOUSE);
        List<InfoInformationNews> houseList = infoInformationNewsService.findInfoInformationNewsList(infoInformationNews);

        ResponseEntity<?> result = new ResponseEntity<>(Constants.SUCCESS);
        result.getParams().put("privateList", privateList);
        result.getParams().put("overseaList", overseaList);
        result.getParams().put("trustList", trustList);
        result.getParams().put("houseList", houseList);
        return result;
    }
}
