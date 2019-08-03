package com.hongkun.finance.vas.dao.impl;

import com.hongkun.finance.vas.dao.VasCouponDetailDao;
import com.hongkun.finance.vas.model.VasCouponDetail;
import com.hongkun.finance.vas.model.vo.VasCouponDetailVO;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Project : finance
 * @Program Name  : com.hongkun.finance.loan.dao.impl.VasCouponDetailDaoImpl.java
 * @Class Name    : VasCouponDetailDaoImpl.java
 * @Description : GENERATOR DAO实现类
 * @Author : generator
 */
@Dao
public class VasCouponDetailDaoImpl extends MyBatisBaseDaoImpl<VasCouponDetail, Long> implements VasCouponDetailDao {

    private final String GET_USER_COUPONDETAIL_LIST = ".getUserCouponDetailList";

    private final String GET_USER_COUPONDETAIL_LIST_COUNT = ".getUserCouponDetailListCount";

    private final String FIND_VAS_COUPON_DETAIL_BY_KEY = ".findVasCouponDetailByKey";

    private final String GET_USER_COUPON_DETAIL_COUNT_GROUP_BY_TYPE = ".getUserCouponDetailCountGroupByType";

    private final String PAGER = "pager";

    @Override
    public List<VasCouponDetailVO> getUserCouponDetailList(Map<String, Object> param) {
        //没有分页则，查询所有
        Pager pager = new Pager();
        if (param.get(PAGER) != null){
            pager = (Pager) param.get(PAGER);
        }else {
            pager.setCurrentPage(0);
            pager.setPageSize(100000);
        }
        return (List<VasCouponDetailVO>)this.findByCondition(param,pager,VasCouponDetail.class,GET_USER_COUPONDETAIL_LIST).getData();
    }

    @Override
    public Integer getUserCouponDetailListCount(Map<String, Object> param){
        return Integer.valueOf(this.getCurSqlSessionTemplate().selectOne(VasCouponDetail.class.getName() +
                GET_USER_COUPONDETAIL_LIST_COUNT,param).toString());
    }

    @Override
    public VasCouponDetail findVasCouponDetailByKey(String avtKey) {
        return this.getCurSqlSessionTemplate().selectOne(VasCouponDetail.class.getName() +
                FIND_VAS_COUPON_DETAIL_BY_KEY, avtKey);
    }


    @Override
    public List<Map<String,Object>> getUserCouponDetailCountGroupByType(int regUserId) {
        return this.getCurSqlSessionTemplate().selectList(VasCouponDetail.class.getName() + GET_USER_COUPON_DETAIL_COUNT_GROUP_BY_TYPE, regUserId);
    }

    @Override
    public List<VasCouponDetail> getExpiredCouponDetailList(Date currentDate) {
        return this.getCurSqlSessionTemplate().selectList(VasCouponDetail.class.getName() +
                ".getExpiredCouponDetailList", currentDate);
    }

    @Override
    public Map<Integer, VasCouponDetail> findVasCouponDetailByIds(Set<Integer> couponIds) {
        return this.getCurSqlSessionTemplate().selectMap(VasCouponDetail.class.getName() + ".findVasCouponDetailByIds",couponIds,"id");
    }
}
