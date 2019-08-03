package com.hongkun.finance.vas.facade;

import com.hongkun.finance.vas.model.SimGoldVo;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;

public interface FindSimRecordInfoFacade {
	/**
	 * @Description :用户体验金查询
	 * @Method_Name : findSimRecordInfo;
	 * @param simGoldVo
	 * @param pager
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年7月4日 上午11:36:00;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	ResponseEntity<?> findSimRecordInfo(SimGoldVo simGoldVo, Pager pager);

	/**
	 * @Description : 查询体验金统计信息
	 * @Method_Name : findSimGoldCountInfo;
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年10月16日 上午10:41:14;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	ResponseEntity<?> findSimGoldCountInfo();

	/**
	 * @Description : 批量导入体验金
	 * @Method_Name : simGoldimport;
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年05月28日 上午10:11:14;
	 * @Author : guyuzeg@hongkun.com.cn 谷玉泽;
	 */
	ResponseEntity<?> simGoldimport(String filePath);

}
