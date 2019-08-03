package com.hongkun.finance.invest.service;

import com.hongkun.finance.invest.model.*;
import com.hongkun.finance.invest.model.vo.*;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;
import org.mengyun.tcctransaction.api.Compensable;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.invest.service.BidInfoService.java
 * @Class Name : BidInfoService.java
 * @Description : GENERATOR SERVICE类
 * @Author : generator
 */
public interface BidInfoService {
	/**
	 * @Described : 单条插入
	 * @param bidInfo
	 *            持久化的数据对象
	 * @return : void
	 */
	int insertBidInfo(BidInfo bidInfo);

	/**
	 * @Described : 批量插入
	 * @param List<BidInfo>
	 *            批量插入的数据
	 * @return : void
	 */
	void insertBidInfoBatch(List<BidInfo> list);

	/**
	 * @Described : 批量插入
	 * @param List<BidInfo>
	 *            批量插入的数据
	 * @param count
	 *            多少条数提交一次
	 * @return : void
	 */
	void insertBidInfoBatch(List<BidInfo> list, int count);

	/**
	 * @Described : 更新数据
	 * @param bidInfo
	 *            要更新的数据
	 * @return : void
	 */
	int updateBidInfo(BidInfo bidInfo);

	/**
	 * @Described : 批量更新数据
	 * @param bidInfo
	 *            要更新的数据
	 * @param count
	 *            多少条数提交一次
	 * @return : void
	 */
	void updateBidInfoBatch(List<BidInfo> list, int count);

	/**
	 * @Described : 通过id查询数据
	 * @param id
	 *            id值
	 * @return BidInfo
	 */
	BidInfo findBidInfoById(int id);

	/**
	 * @Described : 条件检索数据
	 * @param bidInfo
	 *            检索条件
	 * @return List<BidInfo>
	 */
	List<BidInfo> findBidInfoList(BidInfo bidInfo);

	/**
	 * @Described : 条件检索数据
	 * @param bidInfo
	 *            检索条件
	 * @param start
	 *            起始页
	 * @param limit
	 *            检索条数
	 * @return List<BidInfo>
	 */
	List<BidInfo> findBidInfoList(BidInfo bidInfo, int start, int limit);

	/**
	 * @Described : 条件检索数据
	 * @param bidInfo
	 *            检索条件
	 * @param pager
	 *            分页数据
	 * @return List<BidInfo>
	 */
	Pager findBidInfoList(BidInfo bidInfo, Pager pager);

	/**
	 * @Described : 统计条数
	 * @param bidInfo
	 *            检索条件
	 * @param pager
	 *            分页数据
	 * @return int
	 */
	int findBidInfoCount(BidInfo bidInfo);

	/**
	 * 根据条件查询标的信息
	 * 
	 * @param bidInfo
	 * @param pager
	 * @return
	 */
	Pager findConditionBidInfo(BidInfo bidInfo, Pager pager);


	/**
	 *  @Description    : 同时插入标的信息和标的详细信息
	 *  @Method_Name    : insertBidInfoWithBidDetail
	 *  @param bidInfo                标的信息
	 *  @param bidInfoDetail       标的详情信息
	 *  @return                  插入是否成功
	 *  @Creation Date  : 2018年4月8日10:26:34
	 *  @Author         : zhongpingtang@hongkun.com.cn
	 */
	boolean insertBidInfoWithBidDetail(BidInfo bidInfo, BidInfoDetail bidInfoDetail);

	/**
	 * 更新标的状态
	 * 
	 * @param id
	 * @param state
	 * @return
	 */
	int updateState(int id, int state);

	/**
	 * 
	 * @Description : 校验标的是否存在，如果存在直接返回
	 * @Method_Name : validateBidInfo
	 * @return
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年6月27日 下午5:21:05
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	ResponseEntity<?> validateBidInfo(int id);

	/**
	 * @Description : 更新标的信息和标的详情
	 * @Method_Name : updateBidInfo
	 * @param bidInfo
	 *            标的详情
	 * @param bidInfoDetail
	 *            标的详情
	 * @return
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年7月4日 下午2:29:49
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	ResponseEntity<?> updateBidInfo(BidInfo bidInfo, BidInfoDetail bidInfoDetail);

	/**
	 * 
	 * @Description : 根据产品类型&状态，获取标的信息
	 * @Method_Name : findBidInfoList
	 * @param productType
	 * @param state
	 * @return
	 * @return : List<BidInfo>
	 * @Creation Date : 2017年7月17日 下午5:16:12
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	List<BidInfo> findBidInfoList(Integer productType, Integer state);

	/**
	 * @Description : 根据产品类型&状态&标的名称，获取标的信息
	 * @Method_Name : findBidInfoList;
	 * @param productType
	 *            产品类型
	 * @param state
	 *            标的名称
	 * @param bidName
	 *            标的名称
	 * @return
	 * @return : List<BidInfo>;
	 * @Creation Date : 2017年7月19日 下午5:09:53;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	List<BidInfo> findBidInfoList(Integer productType, Integer state, String bidName);

	/**
	 * @Description : 分页查询标的信息
	 * @Method_Name : findBidInfoList;
	 * @param productType
	 *            产品类型
	 * @param stateList
	 *            状态
	 * @param bidName
	 *            标的名称
	 * @param pager
	 * @return
	 * @return : Pager;
	 * @Creation Date : 2017年7月20日 上午11:48:22;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	Pager findBidInfoList(Integer productType, List<Integer> stateList, String bidName, Pager pager);

	/**
	 * @Description : 查询待匹配的优选标的列表（到下次匹配时间距离10天内）
	 * @Method_Name : findMatchBidInfoList
	 * @param contidion
	 * @param pager
	 * @return
	 * @return : Pager
	 * @Creation Date : 2017年7月19日 下午5:28:12
	 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	Pager findMatchBidInfoList(BidInfoVO contidion, Pager pager);



	/**
	 * @Description : 查询待匹配的标的列表（到下次匹配时间距离10天内）
	 * @Method_Name : findMatchBidInfoList
	 * @param contidion
	 * @return
	 * @return : Pager
	 * @Creation Date : 2017年7月19日 下午5:28:12
	 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	List<BidInfoVO> findMatchBidInfoList(BidInfoVO contidion);

	/**
	 * @Description : 根据条件查询所有符合查询条件的标的信息
	 * @Method_Name : findBidInfoVOByCondition
	 * @param contidion
	 * @param pager
	 * @return
	 * @return : Pager
	 * @Creation Date : 2017年7月20日 下午2:02:28
	 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	Pager findBidInfoVOByCondition(BidInfoVO contidion, Pager pager);

	/**
	 *  @Description    : 查询优选标列表
	 *  @Method_Name    : findGoodList
	 *  @param state
	 *  @return
	 *  @return         : List<BidInfo>
	 *  @Creation Date  : 2017年7月21日 下午5:04:34 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	List<BidInfo> findGoodList(int state);
	/**
	 *  @Description    : 查询匹配的散标
	 *  @Method_Name    : findMatchCommonList
	 *  @param List<Integer> bidStates 标的状态
	 *  @return
	 *  @return         : List<BidInfo>
	 *  @Creation Date  : 2017年7月21日 下午5:08:13 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	List<BidInfo> findMatchCommonList(List<Integer> bidStates);
	
	/**
	 *  @Description    : 根据下次匹配时间查询标的列表
	 *  @Method_Name    : findBidInfoDetailsByEndDate
	 *  @param endDate
	 *  @return
	 *  @return         : List<BidInfoDetailVo>
	 *  @Creation Date  : 2017年7月24日 下午1:55:27 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	List<BidInfoVO> findBidInfoDetailsByEndDate(Date endDate);
	/**
	 * @Description : 更新标的相关信息（还款使用）
	 * @Method_Name : updateForRepay
	 * @param bidInfo
	 *            标的信息
	 * @param updateBidInfoDetailList
	 *            要更新的标的详情
	 * @param insertBidMatchList
	 *            需要插入的新的匹配记录
	 * @param updateBidMatchList
	 *            需要更新的匹配记录
	 * @param insertBidInvestList
	 *            需要插入的新的投资记录
	 * @param updateBidInvestList
	 *            需要更新的投资记录
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年7月20日 下午5:56:39
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	ResponseEntity<?> updateForRepay(BidInfo bidInfo, List<BidInfoDetail> updateBidInfoDetailList,
			List<BidMatch> insertBidMatchList, List<BidMatch> updateBidMatchList, List<BidInvest> insertBidInvestList,
			List<BidInvest> updateBidInvestList);
	/**
	 *  @Description    : 放款时-更新标的状态和放款时间
	 *  @Method_Name    : updateStateAndLendingTime
	 *  @param id
	 *  @param state
	 *  @return
	 *  @return         : int
	 *  @Creation Date  : 2017年7月27日 上午10:54:09 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	int updateStateAndLendingTime(int id, int state);
	/**
	 *  @Description    : 更新标的状态和下次匹配时间
	 *  @Method_Name    : updateBidInfoForMakeLoan
	 *  @param updateDetail
	 *  @return         : void
	 *  @Creation Date  : 2017年7月31日 下午3:41:57 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	@Compensable
	void updateBidInfoForMakeLoan(BidInfoDetail updateDetail);
	
	/**
	 *  @Description    : 通过标的id查询标的详情和投资记录【用于标的后台查看功能】
	 *  @Method_Name    : findBidInfoDetailVo
	 *  @param bidInfoId
	 *  @return
	 *  @return         : BidInfoDetailVo
	 *  @Creation Date  : 2017年9月5日 下午2:04:59 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	BidInfoVO findBidInfoDetailVo(Integer bidInfoId);
	/**
	 *  @Description    : 条件检索标的信息
	 *  @Method_Name    : findBidInfoDetail
	 *  @param pager
	 *  @param vo
	 *  @return         : Pager
	 *  @Creation Date  : 2017年9月5日 下午1:42:06 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	Pager findBidInfoDetailVoList(Pager pager, BidInfoVO vo);
	
	/**
	 *  @Description    : 获得满足条件的所有BidInfoVO
	 *  @Method_Name    : findBidInfoVoList
	 *  @param vo
	 *  @return         : List<BidInfoVO>
	 *  @Creation Date  : 2017年9月30日 下午2:59:09 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	List<BidInfoVO> findBidInfoVoList(BidInfoVO vo);



	/**
	 *  @Description    : 保存标的信息以及标的详情信息
	 *  @Method_Name    : saveInfoAndDetail
	 *  @param bidInfoVO:          标的详情vo
	 *  @param userId   :          操作用户ID
	 *  @param vasRebatesRuleId :  推荐规则ID
	 *  @return                  ResponseEntity
	 *  @Creation Date  : 2018年4月8日10:26:34
	 *  @Author         : zhongpingtang@hongkun.com.cn
	 */
    ResponseEntity saveInfoAndDetail(BidInfoVO bidInfoVO, Integer userId, Integer vasRebatesRuleId);


	/**
	 *  @Description    : 更新标的信息和detail
	 *  @Method_Name    : updateBidInfoAndDetail
	 *  @param bidInfoVO:          标的详情vo
	 *  @param userId    :         操作用户ID
	 *  @param vasRebatesRuleId :   推荐规则ID
	 *  @return                  ResponseEntity
	 *  @Creation Date  : 2018年4月8日10:26:34
	 *  @Author         : zhongpingtang@hongkun.com.cn
	 */
	ResponseEntity updateBidInfoAndDetail(BidInfoVO bidInfoVO, Integer userId, Integer vasRebatesRuleId);
	
	/**
	 *  @Description    : 查询用于自动投资的标的数据
	 *  @Method_Name    : findAutoInvestBidList
	 *  @param bidAuthScheme
	 *  @return         : List<BidInfo>
	 *  @Creation Date  : 2018年1月24日 下午4:52:14 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	List<BidInfoVO> findAutoInvestBidList(BidAutoScheme bidAutoScheme);
	/**
	 * 
	 *  @Description    : 查询标的列表（关联其他表简单信息）
	 *  @Method_Name    : findBidInfoSimpleVoList
	 *  @param pager
	 *  @param bidInfoSimpleVo
	 *  @return
	 *  @return         : Pager
	 *  @Creation Date  : 2018年1月30日 下午2:01:13 
	 *  @Author         : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	Pager findBidInfoSimpleVoList(Pager pager, BidInfoSimpleVo bidInfoSimpleVo);


	/**
	 * 查询符合条件的bidInfoVo
	 * @param queryBidInfoVO
	 * @return
	 */
	List<BidInfoVO> findBidInfoVOByCondition(BidInfoVO queryBidInfoVO);

	/**
	 *  @Description    ：根据标地id集合获取标地信息
	 *  @Method_Name    ：findBidInfoDetailVoByIdList
	 *  @param bidIdSet   标地id集合
	 *  @return java.util.Map<java.lang.Integer,com.hongkun.finance.invest.model.vo.BidInfoVO>
	 *  @Creation Date  ：2018/4/18
	 *  @Author         ：pengwu@hongkun.com.cn
	 */
	Map<Integer,BidInfoVO> findBidInfoDetailVoByIdList(Set<Integer> bidIdSet);
	/**
	*  @Description    ：查看是否允许查看源项目
	*  @Method_Name    ：getViewOriginProjectSwitch
	*  @param bidInfoVO
	*  @return java.lang.String
	*  @Creation Date  ：2018/7/3
	*  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
	*/
    String getViewOriginProjectSwitch(BidInfoVO bidInfoVO);
    /**
     *  @Description    : 查询购房宝，物业宝标的信息
     *  @Method_Name    : findPurchaseBidInfoList;
     *  @param productTypeList 产品类型
     *  @param state 状态
     *  @return
     *  @return         : List<BidInfo>;
     *  @Creation Date  : 2018年8月10日 上午10:16:18;
     *  @Author         : yanbinghuang@hongkun.com.cn 黄艳兵;
     */
     List<BidInfo> findPurchaseBidInfoList(List<Integer> productTypeList, Integer state);

     /**
     *  标的统计查询
     *  @Method_Name             ：findStaFunBid
     *  @param pager
     *  @param staFunBidVO
     *  @return com.yirun.framework.core.utils.pager.Pager
     *  @Creation Date           ：2018/9/19
     *  @Author                  ：zc.ding@foxmail.com
     */
     Pager findStaFunBid(Pager pager, StaFunBidVO staFunBidVO);
     
     /**
     *  查询标的数量&标的总金额&借款人数
     *  @Method_Name             ：findStaBidUserAmountCount
     *  @param staFunBidVO
     *  @return java.util.Map<java.lang.String,java.lang.Object>
     *  @Creation Date           ：2018/9/21
     *  @Author                  ：zc.ding@foxmail.com
     */
     Map<String, Object> findStaBidUserAmountCount(StaFunBidVO staFunBidVO);
	/**
	*  @Description    ：查询交易所匹配标的列表
	*  @Method_Name    ：findBidInfoExchangeListForApp
	*
	*  @return java.util.List<com.hongkun.finance.invest.model.vo.BidInfoExchangeForAppVo>
	*  @Creation Date  ：2019/1/23
	*  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
	*/
    List<BidInfoExchangeForAppVo> findBidInfoExchangeListForApp();
}
