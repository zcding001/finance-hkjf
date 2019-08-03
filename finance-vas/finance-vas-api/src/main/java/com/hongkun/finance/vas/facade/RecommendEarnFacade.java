package com.hongkun.finance.vas.facade;

import java.util.List;

import com.hongkun.finance.user.model.vo.UserVO;
import com.hongkun.finance.vas.model.QdzRecommendConditionVo;
import com.hongkun.finance.vas.model.RcommendEarnInfo;
import com.hongkun.finance.vas.model.RecommendEarnBuild;
import com.hongkun.finance.vas.model.vo.RecommendEarnVO;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;

public interface RecommendEarnFacade {
	/**
	 * @Description : 推荐奖统计查询
	 * @Method_Name : findRecommendEarnInfo;
	 * @param recommendEarnVO
	 * @param page
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年7月4日 上午10:53:55;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	ResponseEntity<?> findRecommendEarnInfo(RecommendEarnVO recommendEarnVO, Pager page);

	/**
	 * @Description : 生成推荐奖记录信息
	 * @Method_Name : createRecommendRecordInfo;
	 * @param recommendEarnBuild
	 * @return : void;
	 * @Creation Date : 2017年7月31日 下午6:02:57;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	void createRecommendRecordInfo(RecommendEarnBuild recommendEarnBuild);

	/**
	 * @Description : 条件检索推荐奖记录
	 * @Method_Name : recommendEarnList
	 * @param recommendEarnVO
	 * @param pager
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年9月25日 下午4:07:03
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	ResponseEntity<?> recommendEarnList(RecommendEarnVO recommendEarnVO, Pager pager);

	/**
	 * @Description : 好友推荐审核
	 * @Method_Name : updateRecommendEarnByIds;
	 * @param List<Integer>
	 *            recommendEarnIds
	 * @param state
	 * @param note
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年10月17日 下午6:43:24;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	ResponseEntity<?> updateRecommendEarnByIds(List<Integer> recommendEarnIds, Integer state, String note);

	/**
	 * @Description : 好友推荐奖金发放流程
	 * @Method_Name : sendReconmmendEarn;
	 * @param List<Integer>
	 *            recommendEarnIds
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年10月18日 下午2:41:13;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	ResponseEntity<?> sendReconmmendEarn(List<Integer> recommendEarnIds);

	/**
	 * @Description : 佣金查询统计功能
	 * @Method_Name : findRecommendEarnCountInfo;
	 * @param recommendEarnVO
	 * @param page
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年10月19日 上午9:58:03;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	ResponseEntity<?> findRecommendEarnCountInfo(RecommendEarnVO recommendEarnVO, Pager page);

	/**
	 * @Description : 前台查询我的推荐奖金明细
	 * @Method_Name : findMyRecommendEarn;
	 * @param recommendEarnVO
	 * @param pager
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年1月9日 下午3:17:52;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	ResponseEntity<?> findMyRecommendEarn(RecommendEarnVO recommendEarnVO, Pager pager);

	/**
	 * 
	 * @Description : 查询用户奖励明细列表
	 * @Method_Name : recommendEarnListForApp
	 * @param regUserId
	 * @param pager
	 * @return
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2018年3月17日 下午4:29:38
	 * @Author : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	ResponseEntity<?> recommendEarnListForApp(Integer regUserId, Pager pager);
	/**
	 *  @Description    : 物业业绩查询
	 *  @Method_Name    : findPropertyAchievement;
	 *  @param recommendEarnVO
	 *  @param pager
	 *  @return
	 *  @return         : ResponseEntity<?>;
	 *  @Creation Date  : 2018年8月20日 下午5:53:57;
	 *  @Author         : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	ResponseEntity<?> findPropertyAchievement(RecommendEarnVO recommendEarnVO,Pager pager);
	/**
	 *  @Description    : 查询审核好友推荐和查询好友推荐奖金发放
	 *  @Method_Name    : findRecommendEarn;
	 *  @param recommendEarnVO
	 *  @param page
	 *  @return
	 *  @return         : ResponseEntity<?>;
	 *  @Creation Date  : 2018年9月27日 上午11:04:43;
	 *  @Author         : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	ResponseEntity<?> findRecommendEarn(RecommendEarnVO recommendEarnVO, Pager page);
	/**
	 *  @Description    : 生成钱袋子推荐奖记录
	 *  @Method_Name    : buildQdzRecommendEarn;
	 *  @param rcommendEarnInfo
	 *  @return
	 *  @return         : ResponseEntity<?>;
	 *  @Creation Date  : 2018年11月14日 下午3:01:56;
	 *  @Author         : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	ResponseEntity<?> buildQdzRecommendEarn(RcommendEarnInfo rcommendEarnInfo)throws Exception;
	/**
	 *  @Description    : 查询推荐人奖金信息
	 *  @Method_Name    : searchRecommendEarnByUserId;
	 *  @param recommendEarnVO
	 *  @param page
	 *  @return
	 *  @return         : ResponseEntity<?>;
	 *  @Creation Date  : 2018年12月14日 上午10:27:54;
	 *  @Author         : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
    ResponseEntity<?> searchRecommendEarnByUserId(RecommendEarnVO recommendEarnVO, Pager page);
}
