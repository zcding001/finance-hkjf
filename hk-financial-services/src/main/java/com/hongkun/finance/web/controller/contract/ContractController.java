package com.hongkun.finance.web.controller.contract;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.contract.facade.ConInfoFacade;
import com.hongkun.finance.contract.service.ConTypeService;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.support.security.OperationTypeEnum;
import com.hongkun.finance.user.support.security.annotation.ActionLog;
import com.hongkun.finance.user.support.security.component.annotation.AskForPermission;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.model.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * @Description : 合同服务接口
 * @Project : hk-financial-services
 * @Program Name  : com.hongkun.finance.web.controller.ContractController
 * @Author : pengwu@hongkun.com.cn 吴鹏
 */
@Controller
@RequestMapping("contractController")
public class ContractController {

    private static final Logger logger = LoggerFactory.getLogger(ContractController.class);

    @Reference
    private ConInfoFacade conInfoFacade;
    @Reference
    private ConTypeService conTypeService;

    /**
     *  @Description    : 获取合同展示内容
     *  @Method_Name    : getContractContent
     *  @param contractType        合同类型
     *  @param location            查看合同位置，1-从标地信息位置查看合同、2-从投资记录位置查看合同、3-查看源项目查看合同，4-借款人查看合同
     *  @param bidInvestId         投资记录Id
     *  @param bidId               标地Id
     *  @return  ResponseEntity
     *  @Creation Date  : 2017年12月27日 上午10:55:55
     *  @Author         : pengwu@hongkun.com.cn
     */
    @RequestMapping("/getContractContent")
    @ResponseBody
    @AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
    public ResponseEntity getContractContent(@RequestParam int contractType,
                                             @RequestParam int location,
                                             @RequestParam(required = false) Integer bidInvestId,
                                             @RequestParam(required = false) Integer bidId){
        RegUser regUser = BaseUtil.getLoginUser();
        return new ResponseEntity(SUCCESS,conInfoFacade.getContractContent(regUser,contractType,location,bidInvestId,bidId));
    }

    /**
     *  @Description    ：获取合同的数据字典信息
     *  @Method_Name    ：getContractTypeAndName
     *
     *  @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     *  @Creation Date  ：2018年01月11日 下午17:55:55
     *  @Author         ：pengwu@hongkun.com.cn
     */
    @RequestMapping("/getContractTypeAndName")
    @ResponseBody
    @AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
    public List<Map<String,Object>> getContractTypeAndName(){
        return conTypeService.getContractTypeAndName();
    }

    /**
     *  @Description    ：PC端借款人查看合同
     *  @Method_Name    ：showBorrowerContract
     *  @param bidId  标的id
     *  @return ResponseEntity
     *  @Creation Date  ：2018/12/24
     *  @Author         ：pengwu@hongkun.com.cn
     */
    @RequestMapping("/showBorrowerContract")
    @ResponseBody
    @ActionLog(msg = "PC端借款人查看合同,标的id: {args[0]}")
    public ResponseEntity showBorrowerContract(Integer bidId){
        //根据优选投资记录获取其对应的的散标记录集合
        return new ResponseEntity(SUCCESS,conInfoFacade.findBorrowerContractInfo(BaseUtil.getLoginUser().getId(),bidId));
    }
}
