package com.hongkun.finance.web.controller.ros;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.roster.model.SysFunctionCfg;
import com.hongkun.finance.roster.service.SysFunctionCfgService;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description : 系统全局开关管理
 * @Project : finance-hkjf
 * @Program Name  : com.hongkun.finance.web.controller.ros
 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
 */
@Controller
@RequestMapping("sysFunctionCfgController/")
public class SysFunctionCfgController {
    @Reference
    private SysFunctionCfgService sysFunctionCfgService;

    /**
    *  @Description    ：系统全局开关查询
    *  @Method_Name    ：rosNoticeList
    *  @param pager
    *  @param sysFunctionCfg
    *  @return com.yirun.framework.core.model.ResponseEntity<?>
    *  @Creation Date  ：2018/10/15
    *  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
    */
    @RequestMapping("sysFunctionCfgList")
    @ResponseBody
    public ResponseEntity<?> sysFunctionCfgList(Pager pager, SysFunctionCfg sysFunctionCfg){
        return new ResponseEntity<>(Constants.SUCCESS,sysFunctionCfgService.findSysFunctionCfgList(sysFunctionCfg,pager));
    }

    /**
    *  @Description    ：启用/禁用 开关
    *  @Method_Name    ：updateFunctionCfgState
    *  @param sysFunctionCfg
    *  @return com.yirun.framework.core.model.ResponseEntity<?>
    *  @Creation Date  ：2018/10/16
    *  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
    */
    @RequestMapping("updateFunctionCfgState")
    @ResponseBody
    public ResponseEntity<?> updateFunctionCfgState(SysFunctionCfg sysFunctionCfg){
        sysFunctionCfgService.updateSysFunctionCfg(sysFunctionCfg);
        return new ResponseEntity<>(Constants.SUCCESS,"更新状态成功");
    }
}
