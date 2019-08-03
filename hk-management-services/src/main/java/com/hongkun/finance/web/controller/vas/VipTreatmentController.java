package com.hongkun.finance.web.controller.vas;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.vas.model.VasCouponProduct;
import com.hongkun.finance.vas.model.VasVipTreatment;
import com.hongkun.finance.vas.service.VasCouponProductService;
import com.hongkun.finance.vas.service.VasVipTreatmentService;
import com.yirun.framework.core.model.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.List;

import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * @Description : 会员等级会员待遇管理Controller
 * @Project : framework
 * @Program Name  : com.hongkun.finance.web.controller.vas.VipTreatmentController
 * @Author : pengwu@hongkun.com.cn 吴鹏
 */

@Controller
@RequestMapping("/vipTreatmentController")
public class VipTreatmentController {

    @Reference
    private VasVipTreatmentService vasVipTreatmentService;
    @Reference
    private VasCouponProductService vasCouponProductService;

    /**
     *  @Description    : 获取所有的会员待遇记录
     *  @Method_Name    : vipTreatmentList
     *  @return         : ResponseEntity
     *  @Creation Date  : 2017年6月30日 上午11:45:50
     *  @Author         : pengwu@hongkun.com.cn
     */
    @RequestMapping("/vipTreatmentList")
    @ResponseBody
    public ResponseEntity vipTreatmentList(){
        VasVipTreatment vasVipTreatment = new VasVipTreatment();
        List<VasVipTreatment> list = vasVipTreatmentService.findVasVipTreatmentList(vasVipTreatment);
        return new ResponseEntity(SUCCESS,list);
    }

    /**
     *  @Description    : 保存会员等待遇记录
     *  @Method_Name    : addVipTreatment
     *  @param vasVipTreatment
     *  @return         : ResponseEntity
     *  @Creation Date  : 2017年6月30日 上午10:24:50
     *  @Author         : pengwu@hongkun.com.cn
     */
    @RequestMapping("/addVipTreatment")
    @ResponseBody
    public ResponseEntity addVipTreatment(@Valid VasVipTreatment vasVipTreatment){
        return vasVipTreatmentService.addVipTreatment(vasVipTreatment);
    }

    /**
     *  @Description    : 更新会员待遇记录
     *  @Method_Name    : update
     *  @param vasVipTreatment
     *  @return         : ResponseEntity
     *  @Creation Date  : 2017年6月30日 下午13:45:50
     *  @Author         : pengwu@hongkun.com.cn
     */
    @RequestMapping("/updateVipTreatment")
    @ResponseBody
    public ResponseEntity updateVipTreatment(VasVipTreatment vasVipTreatment){
        return vasVipTreatmentService.updateVipTreatment(vasVipTreatment);
    }

    /**
     *  @Description    : 获取会员待遇中的卡券产品
     *  @Method_Name    : getCouponProduct
     *  @param vasCouponProduct
     *  @return         : ResponseEntity
     *  @Creation Date  : 2017年11月10日 下午17:12:50
     *  @Author         : pengwu@hongkun.com.cn
     */
    @RequestMapping("/getCouponProduct")
    @ResponseBody
    public ResponseEntity getCouponProduct(VasCouponProduct vasCouponProduct){
        return vasCouponProductService.getCouponProduct(vasCouponProduct);
    }

}
