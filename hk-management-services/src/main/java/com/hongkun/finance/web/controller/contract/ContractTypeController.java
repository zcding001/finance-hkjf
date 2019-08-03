package com.hongkun.finance.web.controller.contract;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.contract.model.ConType;
import com.hongkun.finance.contract.service.ConTypeService;
import com.hongkun.finance.user.support.security.OperationTypeEnum;
import com.hongkun.finance.user.support.security.annotation.ActionLog;
import com.hongkun.finance.user.support.security.component.annotation.AskForPermission;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.annotation.Token;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.support.validate.SAVE;
import com.yirun.framework.core.utils.pager.Pager;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * @Description : 合同类型的controller
 * @Project : framework
 * @Program Name  : com.hongkun.finance.web.controller.contract.ContractTypeController
 * @Author : pengwu@hongkun.com.cn 吴鹏
 */
@Controller
@RequestMapping("/contractTypeController")
public class ContractTypeController {

    @Reference
    private ConTypeService conTypeService;

    /**
     *  @Description    : 查询合同类型数据列表
     *  @Method_Name    : contractTypeList
     *  @param conType
     *  @param pager
     *  @return
     *  @Creation Date  : 2017年6月27日 下午5:51:50
     *  @Author         : pengwu@hongkun.com.cn
     */
    @RequestMapping("/contractTypeList")
    @ResponseBody
    public ResponseEntity contractTypeList(ConType conType, Pager pager){
        Pager returnPage = conTypeService.findConTypeList(conType,pager);
        return new ResponseEntity(SUCCESS, returnPage);
    }
    /**
     *  @Description    : 添加合同类型记录
     *  @Method_Name    : addContractType
     *  @param conType
     *  @return
     *  @Creation Date  : 2017年6月27日 下午5:51:50
     *  @Author         : pengwu@hongkun.com.cn
     */
    @RequestMapping("/addContractType")
    @ResponseBody
    @Token
    @ActionLog(msg = "添加合同类型, 合同类型: {args[0].type},合同名称: {args[0].name}, 合同展示名称: {args[0].showName}")
    public ResponseEntity addContractType(@Validated(SAVE.class) ConType conType){
        return conTypeService.addContractType(BaseUtil.getLoginUser().getId(),conType);
    }
    /**
     *  @Description    : 获取合同类型的数量
     *  @Method_Name    : getContractTypeCount
     *  @param conType
     *  @return
     *  @Creation Date  : 2017年9月26日 上午09:23:50
     *  @Author         : pengwu@hongkun.com.cn
     */
    @RequestMapping("/getContractTypeCount")
    @ResponseBody
    public ResponseEntity getContractTypeCount(ConType conType){
        return new ResponseEntity(SUCCESS,conTypeService.findConTypeCount(conType));
    }
    /**
     *  @Description    : 修改合同类型记录
     *  @Method_Name    : updateContractType
     *  @param conType  合同信息
     *  @return
     *  @Creation Date  : 2017年6月28日 上午10:05:10
     *  @Author         : pengwu@hongkun.com.cn
     */
    @RequestMapping("/updateContractType")
    @ResponseBody
    @ActionLog(msg = "修改合同类型记录, 合同类型id: {args[0].id}, 合同类型: {args[0].type},合同名称: {args[0].name}, 合同展示名称: {args[0].showName}")
    public ResponseEntity updateContractType(ConType conType){
        return conTypeService.updateContractType(BaseUtil.getLoginUser().getId(),conType);
    }
    /**
     *  @Description    : 获取合同的数据字典信息
     *  @Method_Name    : getContractTypeAndName
     *  @return
     *  @Creation Date  : 2018年01月11日 下午17:55:55
     *  @Author         : pengwu@hongkun.com.cn
     */
    @RequestMapping("/getContractTypeAndName")
    @ResponseBody
    @AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
    public List<Map<String,Object>> getContractTypeAndName(){
        return conTypeService.getContractTypeAndName();
    }

}
