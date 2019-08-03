package com.hongkun.finance.payment.service;

import java.util.List;

import com.hongkun.finance.payment.model.FinCityRefer;
import com.yirun.framework.core.utils.pager.Pager;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.payment.service.FinCityReferService.java
 * @Class Name : FinCityReferService.java
 * @Description : GENERATOR SERVICE类
 * @Author : generator
 */
public interface FinCityReferService {

    /**
     * @Described : 单条插入
     * @param finCityRefer 持久化的数据对象
     * @return : void
     */
    void insertFinCityRefer(FinCityRefer finCityRefer);

    /**
     * @Described : 批量插入
     * @param List<FinCityRefer> 批量插入的数据
     * @return : void
     */
    void insertFinCityReferBatch(List<FinCityRefer> list);

    /**
     * @Described : 批量插入
     * @param List<FinCityRefer> 批量插入的数据
     * @param count 多少条数提交一次
     * @return : void
     */
    void insertFinCityReferBatch(List<FinCityRefer> list, int count);

    /**
     * @Described : 更新数据
     * @param finCityRefer 要更新的数据
     * @return : void
     */
    void updateFinCityRefer(FinCityRefer finCityRefer);

    /**
     * @Described : 批量更新数据
     * @param finCityRefer 要更新的数据
     * @param count 多少条数提交一次
     * @return : void
     */
    void updateFinCityReferBatch(List<FinCityRefer> list, int count);

    /**
     * @Described : 通过id查询数据
     * @param id id值
     * @return FinCityRefer
     */
    FinCityRefer findFinCityReferById(int id);

    /**
     * @Described : 条件检索数据
     * @param finCityRefer 检索条件
     * @return List<FinCityRefer>
     */
    List<FinCityRefer> findFinCityReferList(FinCityRefer finCityRefer);

    /**
     * @Described : 条件检索数据
     * @param finCityRefer 检索条件
     * @param start 起始页
     * @param limit 检索条数
     * @return List<FinCityRefer>
     */
    List<FinCityRefer> findFinCityReferList(FinCityRefer finCityRefer, int start, int limit);

    /**
     * @Described : 条件检索数据
     * @param finCityRefer 检索条件
     * @param pager 分页数据
     * @return List<FinCityRefer>
     */
    Pager findFinCityReferList(FinCityRefer finCityRefer, Pager pager);

    /**
     * @Described : 统计条数
     * @param finCityRefer 检索条件
     * @param pager 分页数据
     * @return int
     */
    int findFinCityReferCount(FinCityRefer finCityRefer);
}
