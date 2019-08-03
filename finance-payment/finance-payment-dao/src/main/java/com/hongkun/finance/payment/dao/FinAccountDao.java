package com.hongkun.finance.payment.dao;

import java.util.List;

import com.hongkun.finance.payment.model.FinAccount;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

/**
 * @param <T>
 * @Project : finance
 * @Program Name : com.hongkun.finance.payment.dao.FinAccountDao.java
 * @Class Name : FinAccountDao.java
 * @Description : GENERATOR DAO类
 * @Author : generator
 */
public interface FinAccountDao extends MyBatisBaseDao<FinAccount, java.lang.Long> {
    /***
     * @Description 根据用户ID获取用户对应账户信息
     * @param regUserId 用户Id
     * @return FinAccount
     * @author yanbinghuang
     */
    FinAccount findByRegUserId(Integer regUserId);

    /**
     * @Description : 根据用户ID批量更新账户信息
     * @Method_Name : updateAccountByUserId;
     * @param list
     * @param count
     * @return : void;
     * @Creation Date : 2017年7月4日 下午2:29:23;
     * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
     */
    int updateAccountByUserId(List<FinAccount> list, Integer count);
    /**
     * @Description : 根据用户ID集合查询账户集合
     * @Method_Name : updateAccountByUserId;
     * @param list
     * @param count
     * @return : void;
     * @Creation Date : 2017年7月4日 下午2:29:23;
     * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
     */
	List<FinAccount> findFinAccountByRegUserIds(List<Integer> regUserIds);
}
