package com.hongkun.finance.qdz.service;

import java.util.Map;

import com.hongkun.finance.qdz.vo.QdzTransferInOutCondition;
import com.yirun.framework.core.model.ResponseEntity;

public interface QdzTransferService {
	/**
	 * @Description : 用于处理钱袋子转入转出，更新或插入钱袋子账户信息及生成转入转出记录
	 * @Method_Name : dealTransferInOut;
	 * @param qdzTransferInOutCondition
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年7月18日 上午10:12:21;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	ResponseEntity<?> dealTransferInOut(QdzTransferInOutCondition qdzTransferInOutCondition);

	/**
	 * @Description : 用户删除或更新钱袋子账户信息，以及删除转入转出记录
	 * @Method_Name : cancelTransferInOut;
	 * @param qdzTransferInOutCondition
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年7月18日 上午11:09:04;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	ResponseEntity<?> cancelTransferInOut(QdzTransferInOutCondition qdzTransferInOutCondition);

	/**
	 * @Description : 根据用户ID,查询钱袋子回款计划信息
	 * @Method_Name : findQdzReceiptInfo;
	 * @param regUserId
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年3月12日 下午2:51:19;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	Map<String, Object> findQdzReceiptInfo(Integer regUserId);
}
