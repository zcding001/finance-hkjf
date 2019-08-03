package com.hongkun.finance.point.facade;

import com.hongkun.finance.point.model.vo.PointVO;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;

import java.util.List;

/**
 * @Description :
 * @Project : finance
 * @Program Name  : com.hongkun.finance.point.facade.PointRecordFacade
 * @Author : zhongpingtang@yiruntz.com 唐忠平
 */
public interface PointRecordFacade {

    /**
     *  @Description    : 列出所有的积分记录
     *  @Method_Name    : listPointRecord
     *  @param pointVO  :积分VO
     *  @param pager  :分页信息
     *  @return           ResponseEntity
     *  @Creation Date  : 2018年4月8日10:26:34
     *  @Author         : zhongpingtang@hongkun.com.cn
     */
    Pager listPointRecord(PointVO pointVO, Pager pager);

    /**
     *  @Description    : 列出所有的积分支付记录
     *  @Method_Name    : listPointRecord
     *  @param pointVO  :积分VO
     *  @param pager  :分页信息
     *  @return           ResponseEntity
     *  @Creation Date  : 2018年8月7日
     *  @Author         : guyuze@hongkun.com.cn
     */
    Pager listPointPayRecord(PointVO pointVO, Pager pager);
    /**
     *  @Description    : 查询积分消费信息
     *  @Method_Name    : findPointPayCountList;
     *  @param pointVO
     *  @param pager
     *  @return
     *  @return         : Pager;
     *  @Creation Date  : 2018年9月20日 上午11:12:52;
     *  @Author         : yanbinghuang@hongkun.com.cn 黄艳兵;
     */
    Pager findPointPayCountList(PointVO pointVO, Pager pager);

    /**
     *  @Description    : 用户积分转赠
     *  @Method_Name    : userPointTransfer
     *  @param userId            转赠人id
     *  @param point             积分
     *  @param acceptUsers      积分接收人
     *  @param senderName       发送人名称
     *  @return
     *  @Creation Date  : 2017年12月28日 上午10:55:55
     *  @Author         : pengwu@hongkun.com.cn
     */
    ResponseEntity userPointTransfer(int userId, int point, List<Integer> acceptUsers, String senderName);
}
