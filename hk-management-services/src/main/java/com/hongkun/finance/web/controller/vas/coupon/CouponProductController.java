package com.hongkun.finance.web.controller.vas.coupon;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.user.support.security.annotation.ActionLog;
import com.hongkun.finance.vas.constants.VasConstants;
import com.hongkun.finance.vas.model.VasCouponProduct;
import com.hongkun.finance.vas.service.VasCouponDetailService;
import com.hongkun.finance.vas.service.VasCouponProductService;
import com.yirun.framework.core.annotation.Token;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.DateUtils;
import com.yirun.framework.core.utils.pager.Pager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.Date;

import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * @Description : 处理卡券产品相关controller
 * @Project : finance
 * @Program Name  : com.hongkun.finance.web.controller.vas.coupon.CouponProductController
 * @Author : zhongpingtang@yiruntz.com 唐忠平
 */
@Controller
@RequestMapping("/couponProductController")
public class CouponProductController {

    @Reference
    private VasCouponProductService couponProductService;
    @Reference
    private VasCouponDetailService couponDetailService;

    /**
     *  @Description    : 添加卡券产品
     *  @Method_Name    : couponProductList
     *  @param vasCouponProduct
     *  @return
     *  @Creation Date  : 2017年6月27日 下午5:51:50
     *  @Author         : zhongpingtang@yiruntz.com
     */
    @RequestMapping("/addCouponProduct")
    @ResponseBody
    @Token
    @ActionLog(msg = "添加卡券产品, 卡券产品信息: {args[0].name}")
    public ResponseEntity addCouponProduct(@Valid VasCouponProduct vasCouponProduct) {
        if (vasCouponProduct.getEndTime() != null){
            vasCouponProduct.setEndTime(DateUtils.getLastTimeOfDay(vasCouponProduct.getEndTime()));
        }
        if (couponProductService.insertVasCouponProduct(vasCouponProduct) == 0) {
            return new ResponseEntity(201, "新建卡券产品失败，请重试");
        }
        return new ResponseEntity(SUCCESS, "新建卡券产品成功");
    }
    /**
     *  @Description    : 查询卡券产品数据列表
     *  @Method_Name    : couponProductList
     *  @param vasCouponProduct
     *  @param pager
     *  @return
     *  @Creation Date  : 2017年6月27日 下午5:51:50
     *  @Author         : zhongpingtang@yiruntz.com
     */
    @RequestMapping("/couponProductList")
    @ResponseBody
    public ResponseEntity couponProductList(VasCouponProduct vasCouponProduct, Pager pager) {
        vasCouponProduct.setSortColumns("create_time desc");
        Pager returnPage = this.couponProductService.findVasCouponProductList(vasCouponProduct, pager);
        return new ResponseEntity(SUCCESS, returnPage);
    }
    /**
     *  @Description    : 更新卡券产品记录
     *  @Method_Name    : updateCouponProduct
     *  @param vasCouponProduct
     *  @return         : ResponseEntity
     *  @Creation Date  : 2017年10月31日 下午15:28:50
     *  @Author         : pengwu@hongkun.com.cn
     */
    @RequestMapping("/updateCouponProduct")
    @ResponseBody
    @ActionLog(msg = "修改卡券产品, 卡券产品信息: {args[0].name}")
    public ResponseEntity updateCouponProduct(VasCouponProduct vasCouponProduct){
        return couponProductService.updateCouponProduct(vasCouponProduct);
    }
    /**
     *  @Description    : 禁用卡券产品记录
     *  @Method_Name    : disableCouponProduct
     *  @param id
     *  @return         : ResponseEntity
     *  @Creation Date  : 2017年11月02日 上午09:48:50
     *  @Author         : pengwu@hongkun.com.cn
     */
    @RequestMapping("/disableCouponProduct")
    @ResponseBody
    @ActionLog(msg = "禁用卡券产品, 卡券产品id: {args[0]}")
    public ResponseEntity disableCouponProduct(int id){
        return couponProductService.disableCouponProduct(id);
    }
    /**
     *  @Description    : 查询可赠送卡券产品数据列表
     *  @Method_Name    : giveCouponProductList
     *  @param vasCouponProduct
     *  @param pager
     *  @return
     *  @Creation Date  : 2017年11月03日 下午3:51:50
     *  @Author         : pengwu@hongkun.com.cn
     */
    @RequestMapping("/giveCouponProductList")
    @ResponseBody
    public ResponseEntity giveCouponProductList(VasCouponProduct vasCouponProduct, Pager pager) {
        vasCouponProduct.setSortColumns("create_time desc");
        vasCouponProduct.setState(VasConstants.VAS_STATE_Y);
        vasCouponProduct.setEndTimeEndForGive(new Date());
        Pager returnPage = this.couponProductService.findVasCouponProductList(vasCouponProduct, pager);
        return new ResponseEntity(SUCCESS, returnPage);
    }

}
