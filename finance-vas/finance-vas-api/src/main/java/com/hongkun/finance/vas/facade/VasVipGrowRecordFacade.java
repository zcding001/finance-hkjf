package com.hongkun.finance.vas.facade;

import com.hongkun.finance.vas.model.vo.VasVipGrowRecordVO;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;

import java.util.Map;

/**
 * @Description : 会员成长值记录facade接口
 * @Project : framework
 * @Program Name  : com.hongkun.finance.vas.facade.VasVipGrowRecordFacade
 * @Author : pengwu@hongkun.com.cn 吴鹏
 */
public interface VasVipGrowRecordFacade {

    /**
     * @Description : 获取所有用户成长值记录
     * @Method_Name : vipGrowRecordList
     * @param vasVipGrowRecordVO
     * @param pager
     * @return : ResponseEntity
     * @Creation Date : 2017年7月03日 下午17:40:50
     * @Author : pengwu@hongkun.com.cn
     */
    ResponseEntity getAllUserVipGrowRecordList(VasVipGrowRecordVO vasVipGrowRecordVO, Pager pager);

    /**
     *  @Description    ：获取用户会员等级信息
     *  @Method_Name    ：getUserVipInfo
     *  @param id
     *  @return java.util.Map<java.lang.String,java.lang.Object>
     *  @Creation Date  ：2018/9/27
     *  @Author         ：pengwu@hongkun.com.cn
     */
    Map<String,Object> getUserVipInfo(Integer id);
}
