package com.hongkun.finance.vas.service;

import java.util.List;
import java.util.Map;

import com.hongkun.finance.vas.model.RcommendEarnInfo;
import com.hongkun.finance.vas.model.VasBidRecommendEarn;
import com.hongkun.finance.vas.model.vo.RecommendEarnForAppVo;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;

/**
 * @Project : finance
 * @Program Name :
 *          com.hongkun.finance.vas.service.VasBiddRecommendEarnService.java
 * @Class Name : VasBiddRecommendEarnService.java
 * @Description : GENERATOR SERVICE类
 * @Author : generator
 */
public interface VasBidRecommendEarnService {

	/**
	 * @Described : 单条插入
	 * @param vasBiddRecommendEarn
	 *            持久化的数据对象
	 * @return : void
	 */
	int insertVasBidRecommendEarn(VasBidRecommendEarn vasBidRecommendEarn);

	/**
	 * @Described : 批量插入
	 * @param List<VasBiddRecommendEarn>
	 *            批量插入的数据
	 * @return : void
	 */
	void insertVasBidRecommendEarnBatch(List<VasBidRecommendEarn> list);

	/**
	 * @Described : 批量插入
	 * @param List<VasBiddRecommendEarn>
	 *            批量插入的数据
	 * @param count
	 *            多少条数提交一次
	 * @return : void
	 */
	void insertVasBidRecommendEarnBatch(List<VasBidRecommendEarn> list, int count);

	/**
	 * @Described : 更新数据
	 * @param vasBiddRecommendEarn
	 *            要更新的数据
	 * @return : void
	 */
	int updateVasBidRecommendEarn(VasBidRecommendEarn vasBidRecommendEarn);

	/**
	 * @Described : 批量更新数据
	 * @param vasBiddRecommendEarn
	 *            要更新的数据
	 * @param count
	 *            多少条数提交一次
	 * @return : void
	 */
	void updateVasBidRecommendEarnBatch(List<VasBidRecommendEarn> list, int count);

	/**
	 * @Described : 通过id查询数据
	 * @param id
	 *            id值
	 * @return VasBiddRecommendEarn
	 */
	VasBidRecommendEarn findVasBidRecommendEarnById(int id);

	/**
	 * @Described : 条件检索数据
	 * @param vasBiddRecommendEarn
	 *            检索条件
	 * @return List<VasBiddRecommendEarn>
	 */
	List<VasBidRecommendEarn> findVasBidRecommendEarnList(VasBidRecommendEarn vasBidRecommendEarn);

	/**
	 * @Described : 条件检索数据
	 * @param vasBiddRecommendEarn
	 *            检索条件
	 * @param start
	 *            起始页
	 * @param limit
	 *            检索条数
	 * @return List<VasBiddRecommendEarn>
	 */
	List<VasBidRecommendEarn> findVasBidRecommendEarnList(VasBidRecommendEarn vasBidRecommendEarn, int start,
			int limit);

	/**
	 * @Described : 条件检索数据
	 * @param vasBiddRecommendEarn
	 *            检索条件
	 * @param pager
	 *            分页数据
	 * @return List<VasBiddRecommendEarn>
	 */
	Pager findVasBidRecommendEarnList(VasBidRecommendEarn vasBidRecommendEarn, Pager pager);

	/**
	 * @Described : 统计条数
	 * @param vasBiddRecommendEarn
	 *            检索条件
	 * @param pager
	 *            分页数据
	 * @return int
	 */
	int findVasBidRecommendEarnCount(VasBidRecommendEarn vasBidRecommendEarn);

	/**
	 * @Description : 通过投资记录ID， 查询该投资记录对应的推荐奖记录的数量,如果>0,则认为已经发放推荐奖，否则没有发放
	 * @Method_Name : findVasBiddRecommendEarnCountByBidInvestId;
	 * @param bidInvestId
	 *            投资记录ID
	 * @return
	 * @return : int;
	 * @Creation Date : 2017年7月31日 下午2:27:34;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	int findVasBidRecommendEarnCountByBidInvestId(Integer bidInvestId);

	/**
	 * @Description :根据条件分页检索
	 * @Method_Name : findVasBiddRecommendEarnListByInfo;
	 * @param biddRecommendEarnMap
	 *            参数map的KEY：regUserId,stateList, recommend_reg_user_id,
	 *            biddIdList,state, createTimeBegin,createTimeEnd
	 * @param pager
	 * @return
	 * @return : Pager;
	 * @Creation Date : 2017年6月27日 下午3:52:04;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	Pager findVasBidRecommendEarnListByInfo(Map<String, Object> bidRecommendEarnMap, Pager pager);

	/**
	 * @Description :用于生成推荐奖记录
	 * @Method_Name : insertVasBiddRecommendEarn;
	 * @param bidRcommendEarnInfo
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年6月30日 下午2:06:56;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	ResponseEntity<?> insertVasBidRecommendEarn(RcommendEarnInfo bidRcommendEarnInfo);

	/**
	 * @Description : 根据条件统计好友推荐奖信息
	 * @Method_Name : bidRecommendEarnInfoCount;
	 * @param vasBiddRecommendEarn
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年10月19日 上午9:41:55;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	ResponseEntity<?> bidRecommendEarnInfoCount(VasBidRecommendEarn vasBidRecommendEarn);

	/**
	 * @Description : 立即推荐好友信息封装
	 * @Method_Name : recommendFriendInfo;
	 * @param basePath
	 * @param recommendNo
	 *            推荐码
	 * @param regUserId
	 *            用户Id
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年1月3日 上午11:36:04;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	ResponseEntity<?> recommendFriendInfo(String basePath, String recommendNo, Integer regUserId);

	/**
	 * 
	 * @Description : 查询推荐奖励金额
	 * @Method_Name : friendInviteCommision
	 * @param regUserId
	 * @return
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2018年3月17日 下午3:47:45
	 * @Author : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	ResponseEntity<?> friendInviteCommision(Integer regUserId);

	Pager recommendEarnListForApp(RecommendEarnForAppVo vo, Pager pager);

}
