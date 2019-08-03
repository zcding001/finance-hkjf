package com.hongkun.finance.api.controller.contract;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.contract.facade.ConInfoFacade;
import com.hongkun.finance.contract.model.vo.CommonInvestConInfoVO;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.support.security.annotation.ActionLog;
import com.hongkun.finance.user.utils.BaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @Description : 合同相关controller
 * @Project : framework
 * @Program Name  : com.hongkun.finance.api.controller.contract.ContractController
 * @Author : pengwu@hongkun.com.cn 吴鹏
 */
@Controller
@RequestMapping("/contractController")
public class ContractController {

    private static final Logger logger = LoggerFactory.getLogger(ContractController.class);

    @Reference
    private ConInfoFacade conInfoFacade;

    /**
     *  @Description    ：展示app端合同信息
     *  @Method_Name    ：getContractInfo
     *  @param contractType  合同类型
     *  @param location      查看合同位置，1-从标地信息位置查看合同、2-从投资记录位置查看合同、3-查看源项目查看合同，4-借款人查看合同
     *  @param bidInvestId   投资记录Id
     *  @param bidId         标地Id
     *  @return org.springframework.web.servlet.ModelAndView
     *  @Creation Date  ：2018/5/9
     *  @Author         ：pengwu@hongkun.com.cn
     */
    @RequestMapping("/getContractInfo")
    @ActionLog(msg = "用户查看合同, 合同类型: {args[0]}, 查看位置: {args[1]}, 投资记录id: {args[2]}, 标地id: {args[3]}")
    public ModelAndView getContractInfo(@RequestParam int contractType,
                                        @RequestParam int location,
                                        @RequestParam(required = false) Integer bidInvestId,
                                        @RequestParam(required = false) Integer bidId){
        RegUser regUser = BaseUtil.getLoginUser();
        ModelAndView mav = new ModelAndView("contract/contractFrameworkTemplateApp");
        String content = conInfoFacade.getContractContent(regUser,contractType,location,bidInvestId,bidId);
        if ("".equals(content)){
            content = "暂无合同内容";
            mav.addObject("contractType","error");
        }else {
            mav.addObject("contractType",contractType);
        }
        mav.addObject("from",location);
        mav.addObject("content",content);
        return mav;
    }


    /**
     *  @Description    ：查看源项目
     *  @Method_Name    ：viewOriginProject
     *  @param bidId     标的id
     *  @param investId  投资记录id
     *  @param sessionId  投资记录id
     *  @return org.springframework.web.servlet.ModelAndView
     *  @Creation Date  ：2018/6/1
     *  @Author         ：pengwu@hongkun.com.cn
     */
    @RequestMapping("/viewOriginProject")
    @ResponseBody
    @ActionLog(msg = "用户查看源项目,投资记录id: {args[0]}")
    public ModelAndView viewOriginProject(Integer bidId,Integer investId,String sessionId){
        ModelAndView result = new ModelAndView("contract/viewOriginProject");
        RegUser regUser = BaseUtil.getLoginUser();
        //根据优选投资记录获取其对应的的散标记录集合
        List<CommonInvestConInfoVO> conInfoList = conInfoFacade.findGoodInvestMatchContractInfo(regUser.getId(),bidId, investId);
        result.addObject("conInfoList",conInfoList);
        result.addObject("sessionId",sessionId);
        return result;
    }

    /**
     *  @Description    ：APP端借款人查看合同
     *  @Method_Name    ：showBorrowerContract
     *  @param bidId  标的id
     *  @param sessionId  用户sessionId
     *  @return org.springframework.web.servlet.ModelAndView
     *  @Creation Date  ：2018/6/1
     *  @Author         ：pengwu@hongkun.com.cn
     */
    @RequestMapping("/showBorrowerContract")
    @ResponseBody
    @ActionLog(msg = "APP端借款人查看合同,标的id: {args[0]}")
    public ModelAndView showBorrowerContract(Integer bidId,String sessionId){
        ModelAndView result = new ModelAndView("contract/showBorrowerContract");
        RegUser regUser = BaseUtil.getLoginUser();
        //根据优选投资记录获取其对应的的散标记录集合
        List<CommonInvestConInfoVO> conInfoList = conInfoFacade.findBorrowerContractInfo(regUser.getId(),bidId);
        result.addObject("conInfoList",conInfoList);
        result.addObject("sessionId",sessionId);
        return result;
    }
}
