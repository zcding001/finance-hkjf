package com.hongkun.finance.web.controller.bid;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.invest.constants.InvestConstants;
import com.hongkun.finance.invest.model.BidProduct;
import com.hongkun.finance.invest.service.BidProductService;
import com.hongkun.finance.user.support.security.annotation.ActionLog;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.annotation.Token;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.support.validate.SAVE;
import com.yirun.framework.core.support.validate.UPDATE;
import com.yirun.framework.core.utils.pager.Pager;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.yirun.framework.core.commons.Constants.ERROR;
import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * @Description : 处理标的产品的controller * @Project : finance
 * @Program Name : com.hongkun.finance.web.controller.bid.BidProductController
 * @Author : zhongpingtang@yiruntz.com 唐忠平
 */
@Controller
@RequestMapping("/bidProductController")
public class BidProductController {

    @Reference
    private BidProductService bidProductService;


    /**
     * 保存一个标的产品
     *
     * @param bidProduct
     * @return
     * @Author : zhongpingtang@yiruntz.com 唐忠平
     */
    @RequestMapping("/save")
    @ResponseBody
    @Token
    @ActionLog(msg = "添加标的产品, 标的产品信息: {args[0].name}")
    public ResponseEntity save(@Validated(SAVE.class) BidProduct bidProduct) {

       bidProduct.setCreateUserId(BaseUtil.getLoginUser().getId());
        // 尝试插入
        return bidProductService.saveBidProduct(bidProduct);
    }


    /***
     * 更新一个标的产品
     *
     * @param bidProduct
     * @return
     * @Author : zhongpingtang@yiruntz.com 唐忠平
     */
    @RequestMapping("/update")
    @ResponseBody
    @Token
    @ActionLog(msg = "更新标的产品, 标的产品信息: {args[0].id}")
    public ResponseEntity update(@Validated(UPDATE.class) BidProduct bidProduct) {
        return this.bidProductService.updateBidProductInfo(bidProduct);
    }


    /**
     *  @Description    : 条件检索标的产品信息
     *  @Method_Name    : bidProductList
     *  @param pager
     *  @param bidProduct
     *  @return         : ResponseEntity<?>
     *  @Creation Date  : 2017年10月12日 下午5:09:12
     *  @Author         : zhichaoding@hongkun.com zc.ding
     */
    @RequestMapping("/bidProductList")
    @ResponseBody
    public ResponseEntity<?> bidProductList(Pager pager, BidProduct bidProduct) {
        return new ResponseEntity<>(Constants.SUCCESS, bidProductService.findBidProductWithCondition(bidProduct, pager));
    }


    /**
     * 查询某一个标的产品的详细信息
     *
     * @param id
     * @return
     * @Author : zhongpingtang@yiruntz.com 唐忠平
     */
    @RequestMapping("/select")
    @ResponseBody
    public ResponseEntity selectBidProductById(@RequestParam("id") Integer id) {
        BidProduct bidProduct = this.bidProductService.findBidProductById(id);
        if (bidProduct == null) {
            return new ResponseEntity(ERROR, "您所查询的标的产品不存在");
        }
        return new ResponseEntity(SUCCESS, bidProduct);
    }

    /**
     * 上架标的产品
     * @param productId
     * @return
     * @Author : zhongpingtang@yiruntz.com 唐忠平
     */
    @RequestMapping("/turnOnBidProduct")
    @ResponseBody
    @Token
    @ActionLog(msg = "上架标的产品, 标的产品信息: {args[0]}")
    public ResponseEntity turnOnBidProduct(@RequestParam("id")Integer productId){
         return bidProductService.turnOnOrDownProduct(productId, InvestConstants.PRODUCT_STATE_ON);

    }

    /**
     * 下架标的产品
     * @param productId
     * @return
     * @Author : zhongpingtang@yiruntz.com 唐忠平
     */
    @RequestMapping("/turnOffBidProduct")
    @ResponseBody
    @Token
    @ActionLog(msg = "下架标的产品, 标的产品信息: {args[0]}")
    public ResponseEntity turnOffBidProduct(@RequestParam("id")Integer productId){
        return bidProductService.turnOnOrDownProduct(productId,  InvestConstants.PRODUCT_STATE_OFF);
    }


}
