package com.hongkun.finance.web.support.authvalidators;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.invest.constants.InvestConstants;
import com.hongkun.finance.invest.model.vo.BaseBidInfoVO;
import com.hongkun.finance.invest.model.vo.IndexBidInfoVO;
import com.hongkun.finance.roster.constants.RosterFlag;
import com.hongkun.finance.roster.constants.RosterType;
import com.hongkun.finance.roster.service.RosInfoService;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.support.security.AuthUtil;
import com.hongkun.finance.user.support.security.OperationTypeEnum;
import com.hongkun.finance.user.support.security.TimeRuledAuthValidator;
import com.yirun.framework.core.model.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description : 黑名单标的信息验证
 * @Project : finance
 * @Program Name : com.hongkun.finance.web.support.authvalidators.RosBidInfoValidator
 * @Author : zhongpingtang@hongkun.com.cn
 */
public class RosBidInfoValidator implements TimeRuledAuthValidator {
    private final Logger logger = LoggerFactory.getLogger(RosBidInfoValidator.class);

    @Reference
    private RosInfoService rosInfoService;

    //    @Value("${validatorSwitch}")
//    private Integer validatorSwitch;//控制开启关闭验证的开关

    @Override
    public boolean isPostValidate() {
        return true;
    }


    @Override
    public boolean doPostAuthValidate(Object resultResponseEntity, RegUser currentUser, OperationTypeEnum operationType, boolean workInConsole, HttpServletRequest request, HttpServletResponse response) {
        logger.info("\n\n\n--------------------->验证开始");
        logger.info("当验证操作:" + AuthUtil.cleanUserRequestResource(request)+"\n" +
                "验证时间:"+(isPostValidate()?"后置\n":"前置\n")+
                "优先级:"+getOrder());
        logger.info("--------------------->为禁止投资用户:【"+currentUser.getNickName()+"】屏蔽处理标的信息");
        logger.info("--------------------->验证结束\n\n\n");

        /**********************隐藏逻辑***********************************/
        IndexBidInfoVO indexBidInfoVO = (IndexBidInfoVO) ((ResponseEntity) resultResponseEntity).getResMsg();
        List<BaseBidInfoVO> unHidedBidInfo = new ArrayList<>(16);
        unHidedBidInfo.add(indexBidInfoVO.getExperienceBid());
        unHidedBidInfo.add(indexBidInfoVO.getGreenhandBid());

        //隐藏推荐和爆款标
        indexBidInfoVO.setHotBid(null);
        indexBidInfoVO.setRecommendBid(null);

        unHidedBidInfo.addAll(indexBidInfoVO.getRepayByMonthBid());
        unHidedBidInfo.addAll(indexBidInfoVO.getRepayOnceBid() == null ? new ArrayList<>():indexBidInfoVO.getRepayOnceBid());
        //此用户禁止投资，屏蔽标的信息
        hideBidInfoFromRoster(unHidedBidInfo);
        return true;
    }


    @Override
    public String getValidateUrl() {
        //只验证首页标的信息
        return "/bidInfoController/findIndexBidInfo.do";
    }

    @Override
    public boolean canValidateOn(RegUser user, OperationTypeEnum operationType, boolean workInConsole, HttpServletRequest request, HttpServletResponse response) {
        //如果在黑名单中有则处理
        return rosInfoService.validateRoster(user.getId(), RosterType.INVEST_CTL, RosterFlag.BLACK);
    }


    /**
     * 屏蔽标的信息
     *
     * @param unHidedBidInfo
     */
    private void hideBidInfoFromRoster(List<? extends BaseBidInfoVO> unHidedBidInfo) {
        unHidedBidInfo.stream().forEach(e -> {
            //设置剩余金额为0
            e.setResidueAmount(BigDecimal.ZERO);
            //设置进度为100
            e.setProgress(BigDecimal.valueOf(100));
            //设置状态
            e.setState(InvestConstants.BID_STATE_WAIT_AUDIT);
            //设置已经开始
            e.setHasStarted(true);
        });

    }
}
