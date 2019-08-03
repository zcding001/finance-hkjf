package com.hongkun.finance.web.controller.contract;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.contract.facade.ConInfoFacade;
import com.hongkun.finance.contract.model.ConTemplate;
import com.hongkun.finance.contract.service.ConTemplateService;
import com.hongkun.finance.user.support.security.OperationTypeEnum;
import com.hongkun.finance.user.support.security.annotation.ActionLog;
import com.hongkun.finance.user.support.security.component.annotation.AskForPermission;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.annotation.Token;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.support.validate.SAVE;
import com.yirun.framework.core.support.validate.UPDATE;
import com.yirun.framework.core.utils.pager.Pager;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * @Description : 合同内容模板的controller
 * @Project : framework
 * @Program Name  : com.hongkun.finance.web.controller.contract.ContractTemplateController
 * @Author : pengwu@hongkun.com.cn 吴鹏
 */
@Controller
@RequestMapping("/contractTemplateController")
@AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
public class ContractTemplateController {

    @Reference
    private ConTemplateService conTemplateService;
    @Reference
    private ConInfoFacade conInfoFacade;

    /**
     *  @Description    : 查询合同内容模板数据列表
     *  @Method_Name    : searchByCondition
     *  @param conTemplate
     *  @param pager
     *  @return
     *  @Creation Date  : 2017年6月28日 下午5:51:50
     *  @Author         : pengwu@hongkun.com.cn
     */
    @RequestMapping("/contractTemplateList")
    @ResponseBody
    public ResponseEntity searchByCondition(ConTemplate conTemplate, Pager pager){
        conTemplate.setSortColumns("modify_time DESC");
        return new ResponseEntity(SUCCESS,conTemplateService.findConTemplateList(conTemplate,pager));
    }
    /**
     *  @Description    : 添加合同内容模板记录
     *  @Method_Name    : addContractTemplate
     *  @param conTemplate
     *  @return
     *  @Creation Date  : 2017年6月28日 上午11:13:50
     *  @Author         : pengwu@hongkun.com.cn
     */
    @RequestMapping("/addContractTemplate")
    @ResponseBody
    @Token
    @ActionLog(msg = "添加合同内容模板, 合同类型: {args[0].contractType}")
    public ResponseEntity addContractTemplate(@Validated(SAVE.class) ConTemplate conTemplate){
        return conTemplateService.addContractTemplate(BaseUtil.getLoginUser().getId(),conTemplate);
    }
    /**
     *  @Description    : 修改合同内容模板记录
     *  @Method_Name    : updateContractTemplate
     *  @param conTemplate
     *  @return
     *  @Creation Date  : 2017年6月28日 上午10:15:30
     *  @Author         : pengwu@hongkun.com.cn
     */
    @RequestMapping("/updateContractTemplate")
    @ResponseBody
    @ActionLog(msg = "修改合同内容模板, 合同模板id: {args[0].id}, 合同类型: {args[0].contractType}")
    public ResponseEntity updateContractTemplate(@Validated(UPDATE.class) ConTemplate conTemplate){
        return conTemplateService.updateContractTemplate(BaseUtil.getLoginUser().getId(),conTemplate);
    }
    /**
     *  @Description    : 启用合同模板
     *  @Method_Name    : enableContractTemplate
     *  @param id
     *  @return
     *  @Creation Date  : 2017年10月24日 上午09:42:30
     *  @Author         : pengwu@hongkun.com.cn
     */
    @RequestMapping("/enableContractTemplate")
    @ResponseBody
    @ActionLog(msg = "启用合同模板, 合同模板id: {args[0]}")
    public ResponseEntity enableContractTemplate(int id){
        return conTemplateService.enableContractTemplate(BaseUtil.getLoginUser().getId(),id);
    }

    /**
     *  @Description    ：禁用合同模板
     *  @Method_Name    ：disableContractTemplate
     *  @param id  合同模板id
     *  @return com.yirun.framework.core.model.ResponseEntity<?>
     *  @Creation Date  ：2018/5/10
     *  @Author         ：pengwu@hongkun.com.cn
     */
    @RequestMapping("/disableContractTemplate")
    @ResponseBody
    @ActionLog(msg = "禁用合同模板, 合同模板id: {args[0]}")
    public ResponseEntity<?> disableContractTemplate(int id){
        return conTemplateService.disableContractTemplate(BaseUtil.getLoginUser().getId(),id);
    }
    /**
     *  @Description    : 获取合同模板内容详情
     *  @Method_Name    : findContractTemplateDetail
     *  @param id
     *  @return
     *  @Creation Date  : 2017年10月09日 下午17:25:30
     *  @Author         : pengwu@hongkun.com.cn
     */
    @RequestMapping("/findContractTemplateDetail")
    @ResponseBody
    public ResponseEntity findContractTemplateDetail(int id){
        return conInfoFacade.findContractTemplateDetail(id);
    }

}
