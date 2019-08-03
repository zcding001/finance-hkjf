package com.hongkun.finance.bi.service;

import java.util.List;

import com.yirun.framework.core.utils.pager.Pager;
import com.hongkun.finance.bi.model.StaReceipt;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.bi.service.StaReceiptService.java
 * @Class Name    : StaReceiptService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface StaReceiptService {
	
	/**
	 * @Described			: 条件检索数据
	 * @param staReceipt 检索条件
	 * @return	List<StaReceipt>
	 */
	List<StaReceipt> findStaReceiptList(StaReceipt staReceipt);
	
	/**
	 * @Described			: 条件检索数据
	 * @param staReceipt 检索条件
	 * @param pager	分页数据
	 * @return	List<StaReceipt>
	 */
	Pager findStaReceiptList(StaReceipt staReceipt, Pager pager);

    /**
     *  回款累计查询
     *  @Method_Name             ：findStaReceiptAddup
     *  @param staReceipt
     *  @return com.hongkun.finance.bi.model.StaReceipt
     *  @Creation Date           ：2018/9/20
     *  @Author                  ：zc.ding@foxmail.com
     */
    StaReceipt findStaReceiptAddup(StaReceipt staReceipt);

    /**
     *  查询未来回款计划统计
     *  @Method_Name             ：findStaReceiptFuture
     *
     *  @return com.hongkun.finance.bi.model.StaReceipt
     *  @Creation Date           ：2018/9/20
     *  @Author                  ：zc.ding@foxmail.com
     */
    StaReceipt findStaReceiptFuture();
}
