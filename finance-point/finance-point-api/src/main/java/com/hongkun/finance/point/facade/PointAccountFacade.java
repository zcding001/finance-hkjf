package com.hongkun.finance.point.facade;

import com.hongkun.finance.point.model.vo.PointVO;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;

/**
 * @Description ：积分账户处理逻辑Facade
 * @Project : finance
 * @Program Name  : com.hongkun.finance.point.facade.PointAccountFacade
 * @Author : zhongpingtang@yiruntz.com 唐忠平
 */
public interface PointAccountFacade {

    /**
     *  @Description    : 查询用户积分账户列表
     *  @Method_Name    : findPointAccountList
     *  @param pointVO        :条件积分VO信息
     *  @param pager : 分页信息
     *  @return           ResponseEntity
     *  @Creation Date  : 2018年4月8日10:26:34
     *  @Author         : zhongpingtang@hongkun.com.cn
     */
    ResponseEntity findPointAccountList(PointVO pointVO, Pager pager);


}
