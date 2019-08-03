package com.hongkun.finance.web.controller.bi;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.point.constants.PointConstants;
import com.hongkun.finance.point.facade.PointRecordFacade;
import com.hongkun.finance.point.model.vo.PointVO;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;

import static com.yirun.framework.core.commons.Constants.SUCCESS;

import java.util.Arrays;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description : 积分相关统计
 * @Project : finance
 * @Program Name  : com.hongkun.finance.web.controller.bi
 * @Author : yanbinghuang
 */
@Controller
@RequestMapping("/pointCountController")
public class PointCountController {

    @Reference
    private PointRecordFacade pointRecordFacade;
    /**
     *  @Description    : 积分消费统计
     *  @Method_Name    : pointPayCountList;
     *  @param pointVO
     *  @param pager
     *  @return
     *  @return         : ResponseEntity<?>;
     *  @Creation Date  : 2018年9月20日 上午10:58:00;
     *  @Author         : yanbinghuang@hongkun.com.cn 黄艳兵;
     */
    @RequestMapping("/pointPayCountList")
    @ResponseBody
    public ResponseEntity<?> pointPayCountList(PointVO pointVO, Pager pager) {
        pointVO.setTypeList(Arrays.asList(PointConstants.POINT_TYPE_CONVERT,PointConstants.POINT_TYPE_PASS_OUT,PointConstants.POINT_TYPE_PAY,PointConstants.POINT_TYPE_TENEMENT));
        return new ResponseEntity<>(SUCCESS, this.pointRecordFacade.findPointPayCountList(pointVO, pager));
    }

}
