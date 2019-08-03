package com.hongkun.finance.web.controller.bi;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.vas.facade.VasRedpacketFacade;
import com.hongkun.finance.vas.model.VasCouponProduct;
import com.hongkun.finance.vas.model.VasCouponProfitRecord;
import com.hongkun.finance.vas.model.vo.VasRedpacketVO;
import com.hongkun.finance.vas.service.VasCouponProductService;
import com.hongkun.finance.vas.service.VasCouponProfitRecordService;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * @Description : 增值服务相关统计
 * @Project : finance
 * @Program Name  : com.hongkun.finance.web.controller.bi
 * @Author : yanbinghuang
 */
@Controller
@RequestMapping("/vasCountController")
public class VasCountController {
    @Reference
    private VasRedpacketFacade vasRedpacketFacade;
    @Reference
    private VasCouponProductService couponProductService;
    @Reference
    private VasCouponProfitRecordService couponProfitRecordService;
    /**
     *  @Description    : 红包统计
     *  @Method_Name    : findOfflinePacketCountList;
     *  @param vasRedpacketVO
     *  @param pager
     *  @return
     *  @return         : ResponseEntity<?>;
     *  @Creation Date  : 2018年9月19日 下午5:36:48;
     *  @Author         : yanbinghuang@hongkun.com.cn 黄艳兵;
     */
    @RequestMapping("/findOfflinePacketCountList")
    @ResponseBody
    public ResponseEntity<?> findOfflinePacketCountList(VasRedpacketVO vasRedpacketVO, Pager pager) {
        //过滤不合法的状态
        if (vasRedpacketVO.getType()!=null&&vasRedpacketVO.getType()<0) {
            vasRedpacketVO.setType(null);
        }
        return vasRedpacketFacade.listPage(vasRedpacketVO, pager);
    }

    /**
     * @Description      ：卡券统计 
     * @Method_Name      ：staCouponList 
     * @param vasCouponProduct
     * @param pager
     * @return com.yirun.framework.core.model.ResponseEntity<?>    
     * @Creation Date    ：2019/1/7        
     * @Author           ：pengwu@hongkunjinfu.com
     */
    @RequestMapping("/staCouponList")
    @ResponseBody
    public ResponseEntity<?> staCouponList(VasCouponProduct vasCouponProduct,Pager pager){
        return new ResponseEntity<>(SUCCESS,couponProductService.staCouponList(vasCouponProduct,pager));
    }

    /**
     * @Description      ：卡券收益统计 
     * @Method_Name      ：staCouponProfitList 
     * @param vasCouponProfitRecord
     * @param pager
     * @return com.yirun.framework.core.model.ResponseEntity<?>    
     * @Creation Date    ：2019/1/8        
     * @Author           ：pengwu@hongkunjinfu.com
     */
    @RequestMapping("/staCouponProfitList")
    @ResponseBody
    public ResponseEntity<?> staCouponProfitList(VasCouponProfitRecord vasCouponProfitRecord,Pager pager){
        vasCouponProfitRecord.setSortColumns("vas_coupon_detail_used_time desc,bid_receipt_plan_actual_time desc");
        return new ResponseEntity<>(SUCCESS,couponProfitRecordService.findVasCouponProfitRecordList(vasCouponProfitRecord,pager));
    }
}
