package com.hongkun.finance.web.controller.point;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.point.facade.PointAccountFacade;
import com.hongkun.finance.point.facade.PointRecordFacade;
import com.hongkun.finance.point.model.vo.PointVO;
import com.hongkun.finance.point.service.PointCommonService;
import com.hongkun.finance.point.service.PointRecordService;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.annotation.Token;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * @Description : 处理积分相关的controller
 * @Project : finance
 * @Program Name  : com.hongkun.finance.web.controller.point.PointController
 * @Author : zhongpingtang@yiruntz.com 唐忠平
 */

@Controller
@RequestMapping("/pointController")
public class PointController {

    private static final Logger logger = LoggerFactory.getLogger(PointController.class);

    @Reference
    private PointRecordService recordService;
    @Reference
    private PointRecordFacade pointRecordFacade;
    @Reference
    private PointAccountFacade pointAccountFacade;
    @Reference
    private PointCommonService pointCommonService;


    /**
     * 积分账户列表
     *
     * @param pointVO
     * @param pager
     * @return
     * @Author : zhongpingtang@yiruntz.com 唐忠平
     */
    @RequestMapping("/pointAccountList")
    @ResponseBody
    public ResponseEntity pointAccountList(PointVO pointVO, Pager pager) {
        return pointAccountFacade.findPointAccountList(pointVO, pager);
    }
    /**
     * 列出积分记录
     *
     * @param pager
     * @return
     * @Author : zhongpingtang@yiruntz.com 唐忠平
     */
    @RequestMapping("/pointRecordList")
    @ResponseBody
    public ResponseEntity listPoint(PointVO pointVO, Pager pager) {
        return new ResponseEntity(SUCCESS, this.pointRecordFacade.listPointRecord(pointVO, pager));
    }
    /**
     * 给用户赠送积分
     *
     * @param pointVO
     * @return
     * @Author : zhongpingtang@yiruntz.com 唐忠平
     */
    @RequestMapping("/givePointToUser")
    @ResponseBody
    @Token
    public ResponseEntity givePointToUser(@Validated({PointVO.ValidateGivePoint.class}) PointVO pointVO) {
        return pointCommonService.givePointToUser(pointVO, BaseUtil.getLoginUser().getId());
    }
    /**
     * 审核积分记录
     *
     * @param pointVO
     * @return
     * @Author : zhongpingtang@yiruntz.com 唐忠平
     */
    @RequestMapping("/checkPoint")
    @ResponseBody
    @Token
    public ResponseEntity checkPoint(@Validated({PointVO.ValidateCheckPoint.class}) PointVO pointVO) {
        return recordService.updateRecordState(pointVO, BaseUtil.getLoginUser().getId());
    }

    /**
     * 积分支付兑现记录
     *
     * @param pager
     * @return
     * @Author : guyuze@yiruntz.com
     */
    @RequestMapping("/pointPayRecordList")
    @ResponseBody
    public ResponseEntity listPointPay(PointVO pointVO, Pager pager) {
        return new ResponseEntity(SUCCESS, this.pointRecordFacade.listPointPayRecord(pointVO, pager));
    }

}
