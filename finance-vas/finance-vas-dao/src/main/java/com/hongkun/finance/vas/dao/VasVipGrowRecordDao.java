package com.hongkun.finance.vas.dao;

import com.hongkun.finance.vas.model.VasVipGrowRecord;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.vas.dao.VasVipGrowRecordDao.java
 * @Class Name    : VasVipGrowRecordDao.java
 * @Description   : GENERATOR DAO类
 * @Author        : generator
 */
public interface VasVipGrowRecordDao extends MyBatisBaseDao<VasVipGrowRecord, Long> {

    /**
     *  @Description    : 获取用户成长值
     *  @Method_Name    : findUserCurrentGrowValue
     *  @param regUserId    用户id
     *  @return         : int
     *  @Creation Date  : 2018年03月12日 下午14:49:50
     *  @Author         : pengwu@hongkun.com.cn
     */
    int findUserCurrentGrowValue(int regUserId);

    /**
     *  @Description    ：根据用户id集合获取会员成长值
     *  @Method_Name    ：findUserGrowValueMap
     *  @param userIdList  用户id集合
     *  @return java.util.Map<java.lang.Integer,com.hongkun.finance.vas.model.VasVipGrowRecord>
     *  @Creation Date  ：2018/4/28
     *  @Author         ：pengwu@hongkun.com.cn
     */
    Map<Integer,VasVipGrowRecord> findUserGrowValueMap(List<Integer> userIdList);

    /**
     *  @Description    ：获取会员等级大于0级的用户集合
     *  @Method_Name    ：findUserLevelGtZeroMap
     *  @return java.util.Map<java.lang.Integer,com.hongkun.finance.vas.model.VasVipGrowRecord>
     *  @Creation Date  ：2018/5/2
     *  @Author         ：pengwu@hongkun.com.cn
     */
    Map<Integer, VasVipGrowRecord> findUserLevelGtZeroMap();

    /**
     *  @Description    ：近三个月进行过降级的用户id集合
     *  @Method_Name    ：findUserThreeMonthHasDown
     *  @param userIdList  用户id集合
     *  @return java.util.List<java.lang.Integer>
     *  @Creation Date  ：2018/5/2
     *  @Author         ：pengwu@hongkun.com.cn
     */
    List<Integer> findUserThreeMonthHasDown(Set<Integer> userIdList);
}
