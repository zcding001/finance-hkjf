package com.hongkun.finance.web.controller.point;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.point.facade.PointMerchantFacade;
import com.hongkun.finance.point.model.PointMerchantInfo;
import com.hongkun.finance.point.service.PointMerchantInfoService;
import com.yirun.framework.core.annotation.Token;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.hongkun.finance.point.constants.PointConstants.POINT_MERCHANT_STATE_WAIT_DELETE;
import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * @Description : 积分商户Controller
 * @Project : finance
 * @Program Name  : com.hongkun.finance.web.controller.point.PointMerchantController
 * @Author : zhongpingtang@yiruntz.com 唐忠平
 */
@Controller
@RequestMapping("/pointMerchantController")
public class PointMerchantController {

    private static final Logger logger = LoggerFactory.getLogger(PointMerchantController.class);

    @Reference
    private PointMerchantFacade pointMerchantFacade;
    @Reference
    private PointMerchantInfoService pointMerchantInfoService;

    /**
     * 列出所有的商户信息
     *
     * @return
     * @Author : zhongpingtang@yiruntz.com 唐忠平
     */
    @RequestMapping("/selectPointMerchantInfoDetail")
    @ResponseBody
    public ResponseEntity selectPointMerchantInfoDetail(@RequestParam("id") Integer merchantId) {
        return pointMerchantFacade.selectPointMerchantInfoDetail(merchantId);
    }

    /**
     * 列出所有的商户信息
     *
     * @return
     * @Author : zhongpingtang@yiruntz.com 唐忠平
     */
    @RequestMapping("/pointMerchantList")
    @ResponseBody
    @Token(operate = Token.Ope.ADD)
    public ResponseEntity pointMerchantList(PointMerchantInfo pointMerchantInfo, Pager pager) {
        return pointMerchantFacade.pointMerchantList(pointMerchantInfo, pager);
    }

    /**
     * 保存商户信息
     *
     * @return
     * @Author : zhongpingtang@yiruntz.com 唐忠平
     */
    @RequestMapping("/save")
    @ResponseBody
    @Token
    public ResponseEntity save(PointMerchantInfo pointMerchantInfo) {
        return pointMerchantInfoService.savePointMerchantInfo(pointMerchantInfo);
    }

    /**
     * 更新商户信息
     *
     * @return
     * @Author : zhongpingtang@yiruntz.com 唐忠平
     */
    @RequestMapping("/update")
    @ResponseBody
    @Token
    public ResponseEntity update(PointMerchantInfo pointMerchantInfo) {
        //异常统一处理
        pointMerchantInfoService.updatePointMerchantInfo(pointMerchantInfo);
        return ResponseEntity.SUCCESS;

    }

    /**
     * 审核商户信息
     *
     * @return
     * @Author : zhongpingtang@yiruntz.com 唐忠平
     */
    @RequestMapping("/checkPointMerchant")
    @ResponseBody
    @Token
    public ResponseEntity checkPointMerchant(@Validated(value = PointMerchantInfo.ValidateCheck.class) PointMerchantInfo pointMerchantInfo) {
        return pointMerchantInfoService.checkPointMerchant(pointMerchantInfo);
    }

    /**
     * 删除商户信息
     *
     * @return
     * @Author : zhongpingtang@yiruntz.com 唐忠平
     */
    @RequestMapping("/delete")
    @ResponseBody
    @Token
    public ResponseEntity delete(@Validated(value = PointMerchantInfo.Delete.class) PointMerchantInfo pointMerchantInfo) {
        pointMerchantInfo.setState(POINT_MERCHANT_STATE_WAIT_DELETE);//设置删除状态位置
        pointMerchantInfoService.updatePointMerchantInfo(pointMerchantInfo);
        return ResponseEntity.SUCCESS;
    }


    /**
     * 检测商户联系人是否唯一
     *
     * @return
     * @Author : zhongpingtang@yiruntz.com 唐忠平
     */
    @RequestMapping("/pointMerchantCount")
    @ResponseBody
    public ResponseEntity pointMerchantCount(@Validated(value = PointMerchantInfo.PointMerchantCount.class) PointMerchantInfo pointMerchantInfo) {
        return new ResponseEntity(SUCCESS, pointMerchantInfoService.findPointMerchantInfoCount(pointMerchantInfo));
    }

}
