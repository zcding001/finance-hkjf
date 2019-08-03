package com.hongkun.finance.web.support.operationreceivers;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.invest.facade.BidInvestFacade;
import com.hongkun.finance.roster.service.RosInfoService;
import com.hongkun.finance.user.model.MenuNode;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.support.security.AbstractChainAuthRouteReceiver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import static com.hongkun.finance.user.constants.UserConstants.*;

/**
 * @Description :
 * 普通的，基于模板角色的用户类型,
 * 比如:大部分前端用户，都是使用前端模板角色
 * 支持用户类型:
 * USER_TYPE_GENERAL:一般用户
 * USER_TYPE_TENEMENT:物业
 * USER_TYPE_ENTERPRISE:企业
 * USER_TYPE_ESCROW_ACCOUNT:第三方账户
 * @Project : finance
 * @Program Name : com.hongkun.finance.web.support.operationreceivers.CommonTemplateUserReceiver
 * @Author : zhongpingtang@hongkun.com.cn
 */
public class CommonTemplateUserReceiver extends AbstractChainAuthRouteReceiver {

    @Reference
    private RosInfoService rosInfoService;
    @Reference
    private BidInvestFacade bidInvestFacade;


    public CommonTemplateUserReceiver() {
        super(Arrays.asList(USER_TYPE_GENERAL,USER_TYPE_TENEMENT,USER_TYPE_ENTERPRISE,USER_TYPE_ESCROW_ACCOUNT));
    }

    @Override
    public Predicate<MenuNode> getFilterMenuStrategy(RegUser regUser) {
        // 定期投资限制或海外投资用户 隐藏自动投资
        List<String> levelOneMenuNames = new ArrayList<>();
        if(!bidInvestFacade.validInvestQualification(regUser.getId()).validSuc()
                || bidInvestFacade.validOverseaInvestor(regUser.getId()).validSuc()){
            levelOneMenuNames.add(MENU_AUTO_INVEST);
        }
        return e -> !levelOneMenuNames.contains(e.getLevelOneName());
    }
}
