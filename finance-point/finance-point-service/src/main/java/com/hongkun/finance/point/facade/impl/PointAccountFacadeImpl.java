package com.hongkun.finance.point.facade.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.point.facade.PointAccountFacade;
import com.hongkun.finance.point.model.vo.PointVO;
import com.hongkun.finance.point.service.PointAccountService;
import com.hongkun.finance.point.service.PointCommonService;
import com.hongkun.finance.point.service.PointRecordService;
import com.hongkun.finance.user.model.vo.UserVO;
import com.hongkun.finance.user.service.RegUserService;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.model.StateList;
import com.yirun.framework.core.utils.pager.Pager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import static com.yirun.framework.core.commons.Constants.SUCCESS;
import static com.yirun.framework.core.utils.BeanPropertiesUtil.getLimitConditions;

/**
 * @Description : 积分账户处理逻辑FacadeImpl
 * @Project : finance
 * @Program Name  : com.hongkun.finance.point.facade.impl.PointAccountFacadeImpl
 * @Author : zhongpingtang@yiruntz.com 唐忠平
 */
@Service
public class PointAccountFacadeImpl implements PointAccountFacade {

    private static final Logger logger = LoggerFactory.getLogger(PointAccountFacadeImpl.class);

    @Autowired
    private PointAccountService pointAccountService;

    @Autowired
    private PointRecordService pointRecordService;

    @Reference
    private RegUserService userService;

    @Reference
    private PointCommonService pointCommonService;

    @Override
    public ResponseEntity findPointAccountList(PointVO pointVO, Pager pager) {
        //step 1:找到用户相关条件所过滤出来的ids
        StateList stateList = getLimitConditions(pointVO, UserVO.class, userService::findUserIdsByUserVO);
        if (!stateList.isProceed()) {
            return new ResponseEntity(SUCCESS,pager);
        }
        pointVO.getLimitUserIds().addAll(stateList);
       //step 2:根据条件找到所有的PointAccount
        pointVO.setSortColumns("create_time desc");
        Pager pointAccountList = pointAccountService.findPointAccountList(pointVO, pager);
        //step 3如果根据条件查询到了用户去补全数据
        if (!BaseUtil.resultPageHasNoData(pointAccountList)) {
            pointAccountList.getData().stream().forEach((e) -> {
                PointVO pointAccount = (PointVO) e;
                //查询用户相关信息
                UserVO user = userService.findRegUserTelAndRealNameById(pointAccount.getRegUserId());
                if (user != null) {
                    //设置用户真实姓名
                    pointAccount.setRealName(user.getRealName());
                    //设置用户电话
                    pointAccount.setLogin(user.getLogin());
                }

            });
        }
        return new ResponseEntity(SUCCESS, pointAccountList);
    }


}
