package com.hongkun.finance.web.controller.vas;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.user.service.DicDataService;
import com.hongkun.finance.vas.model.VasVipGradeStandard;
import com.hongkun.finance.vas.service.VasVipGradeStandardService;
import com.yirun.framework.core.model.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.List;

import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * @Description : 会员等级标准管理Controller
 * @Project : framework
 * @Program Name  : com.hongkun.finance.web.controller.vas.VipGradeStandardController
 * @Author : pengwu@hongkun.com.cn 吴鹏
 */

@Controller
@RequestMapping("/vipGradeStandardController")
public class VipGradeStandardController {

    @Reference
    private VasVipGradeStandardService vasVipGradeStandardService;
    @Reference
    private DicDataService dicDataService;

    /**
     *  @Description    : 获取所有的会员等级标准记录
     *  @Method_Name    : findAll
     *  @return         : ResponseEntity
     *  @Creation Date  : 2017年6月30日 上午10:16:50
     *  @Author         : pengwu@hongkun.com.cn
     */
    @RequestMapping("/vipGradeStandardList")
    @ResponseBody
    public ResponseEntity vipGradeStandardList(){
        VasVipGradeStandard vasVipGradeStandard = new VasVipGradeStandard();
        List<VasVipGradeStandard> list = vasVipGradeStandardService.findVasVipGradeStandardList(vasVipGradeStandard);
        return new ResponseEntity(SUCCESS,list);
    }
    /**
     *  @Description    : 保存会员等级标准记录
     *  @Method_Name    : save
     *  @param vasVipGradeStandard
     *  @return         : ResponseEntity
     *  @Creation Date  : 2017年6月30日 上午10:24:50
     *  @Author         : pengwu@hongkun.com.cn
     */
    @RequestMapping("/addVipGradeStandard")
    @ResponseBody
    public ResponseEntity addVipGradeStandard(@Valid VasVipGradeStandard vasVipGradeStandard){
        return vasVipGradeStandardService.addVipGradeStandard(vasVipGradeStandard);
    }
    /**
     *  @Description    : 更新会员等级标准记录
     *  @Method_Name    : update
     *  @param vasVipGradeStandard
     *  @return         : ResponseEntity
     *  @Creation Date  : 2017年6月30日 上午10:45:50
     *  @Author         : pengwu@hongkun.com.cn
     */
    @RequestMapping("/updateVipGradeStandard")
    @ResponseBody
    public ResponseEntity updateVipGradeStandard(VasVipGradeStandard vasVipGradeStandard){
        return vasVipGradeStandardService.updateVipGradeStandard(vasVipGradeStandard);
    }

}
