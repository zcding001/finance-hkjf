package com.hongkun.finance.invest.service;

import com.hongkun.finance.invest.model.BidInfo;
import com.hongkun.finance.invest.model.BidInvest;
import com.hongkun.finance.invest.model.vo.*;
import com.yirun.framework.core.enums.PlatformSourceEnums;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;
import org.mengyun.tcctransaction.api.Compensable;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.invest.service.BidInvestService.java
 * @Class Name : BidInvestService.java
 * @Description : GENERATOR SERVICE类
 * @Author : generator
 */
public interface BidInvestService {

	/**
	 * @Description : 投资操作
	 * @Method_Name : insertBidInvest
	 * @param regUserId
	 *            : 用户id
	 * @param realName
	 *            : 用户真实姓名
	 * @param money
	 *            : 投资金额
	 * @param bidInfo
	 *            : 标的信息
	 * @param investRedPacketId
	 *            : 投资红包
	 * @param investRaiseInterestId
	 *            : 加息券
	 * @param jworth
	 *            : 加息券利率用于计算加息收益
	 * @param currTime
	 *            : 当前操作时间
	 * @param investType
	 *            : 投资方式 1 自动投资 2手动投资
	 * @param platformSourceEnums
	 *            : 平台标识
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年6月15日 上午9:02:33
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	@Compensable
	ResponseEntity<?> insertBidInvest(int regUserId, String realName, BigDecimal money, BidInfo bidInfo,
			int investRedPacketId, int investRaiseInterestId, BigDecimal jworth, Date currTime, Integer investType,
			PlatformSourceEnums platformSourceEnums);

	/**
	 * @Description :投资记录操作失败后回滚操作
	 * @Method_Name : updateBidInvestForRollback
	 * @param bidInvest
	 * @param bidInfo
	 * @return
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年6月20日 下午3:16:28
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	ResponseEntity<?> updateBidInvestForRollback(BidInvest bidInvest, BidInfo bidInfo);

	/**
	 * @Description : 分页检索投资记录详情
	 * @Method_Name : findBidInvestDetailList
	 * @param bidInvestDetailVO
	 * @param pager
	 * @return
	 * @return : Pager
	 * @Creation Date : 2017年6月19日 下午1:59:15
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	Pager findBidInvestDetailList(BidInvestDetailVO bidInvestDetailVO, Pager pager);

	/**
	 * @Described : 单条插入
	 * @param bidInvest
	 *            持久化的数据对象
	 * @return : void
	 */
	void insertBidInvest(BidInvest bidInvest);

	/**
	 * @Described : 批量插入
	 * @param list
	 *            批量插入的数据
	 * @return : void
	 */
	void insertBidInvestBatch(List<BidInvest> list);

	/**
	 * @Described : 批量插入
	 * @param list
	 *            批量插入的数据
	 * @param count
	 *            多少条数提交一次
	 * @return : void
	 */
	void insertBidInvestBatch(List<BidInvest> list, int count);

	/**
	 * @Described : 更新数据
	 * @param bidInvest
	 *            要更新的数据
	 * @return : void
	 */
	@Compensable
	void updateBidInvest(BidInvest bidInvest);

	/**
	 * @Described : 批量更新数据
	 * @param list
	 *            要更新的数据
	 * @param count
	 *            多少条数提交一次
	 * @return : void
	 */
	void updateBidInvestBatch(List<BidInvest> list, int count);

	/**
	 * @Described : 通过id查询数据
	 * @param id
	 *            id值
	 * @return BidInvest
	 */
	BidInvest findBidInvestById(int id);

	/**
	 * @Described : 条件检索数据
	 * @param bidInvest
	 *            检索条件
	 * @return List<BidInvest>
	 */
	List<BidInvest> findBidInvestList(BidInvest bidInvest);

	/**
	 * @Described : 条件检索数据
	 * @param bidInvest
	 *            检索条件
	 * @param start
	 *            起始页
	 * @param limit
	 *            检索条数
	 * @return List<BidInvest>
	 */
	List<BidInvest> findBidInvestList(BidInvest bidInvest, int start, int limit);

	/**
	 * @Described : 条件检索数据
	 * @param bidInvest
	 *            检索条件
	 * @param pager
	 *            分页数据
	 * @return List<BidInvest>
	 */
	Pager findBidInvestList(BidInvest bidInvest, Pager pager);

	/**
	 * @Described : 统计条数
	 * @param bidInvest
	 *            检索条件
	 * @return int
	 */
	int findBidInvestCount(BidInvest bidInvest);

	/**
	 * @Description : 查询某个标的不同投资渠道的投资总金额
	 * @Method_Name : findSumAmountByBidId
	 * @param bidInfoId
	 * @return : Map<String, Object>
	 * @Creation Date : 2017年6月22日 下午2:04:42
	 * @Author : xuhuiliu@hongkun.com.cn 刘旭辉
	 */
    Map<String, Object> findSumAmountByBidId(int bidInfoId);

	/**
	 * @Description : 根据标的id查询投资记录列表
	 * @Method_Name : findBidInvestListByBidId
	 * @param bidInfoId
	 * @return : List<BidInvest>
	 * @Creation Date : 2017年6月22日 下午2:24:32
	 * @Author : xuhuiliu@hongkun.com.cn 刘旭辉
	 */
	List<BidInvest> findBidInvestListByBidId(Integer bidInfoId);

	/**
	 * 
	 * @Description : 获取该产品类型用户有效投资总金额
	 * @Method_Name : findQdzThirdInvestSumAmount
	 * @return : BigDecimal
	 * @Creation Date : 2017年7月17日 下午6:11:19
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	BigDecimal findInvestSumAmount(Integer productType, Integer bidState, Integer regUserId);


	List<BidInvest> findInvests(Integer productType, Integer bidState, Integer regUserId);

	/**
	 * @Description : 根据优选标的code和散标标的code 查询优选匹配到散标上的投资记录
	 * @Method_Name : findMatchBidInvestList
	 * @param goodBidId
	 * @param commonBidId
	 * @return : Pager
	 * @Creation Date : 2017年7月21日 上午10:00:10
	 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	Pager findMatchBidInvestList(Integer goodBidId, Integer commonBidId, Pager pager);

	/**
	 * @Description :查询个人投资数量
	 * @Method_Name : findBidInvestCount;
	 * @param bidId
	 *            标的ID
	 * @param investUserId
	 *            投资用户ID
	 * @param createTime
	 *            投资推荐奖规则活动开始时间
	 * @param endTime
	 *            投资时间
	 * @param recommendState
	 *            标的推荐状态
	 * @return : int;
	 * @Creation Date : 2017年7月31日 下午3:39:22;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	int findBidInvestCount(Integer bidId, Integer investUserId, Date createTime, Date endTime, Integer recommendState);

	/**
	 * @Description :查询用户是否投资了某个标的
	 * @Method_Name : findBidInvestCount;
	 * @param investUserId
	 *            用户Id
	 * @param bidType
	 *            标的类型
	 * @return
	 * @return : int;
	 * @Creation Date : 2018年5月28日 下午3:54:52;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	int findBidInvestCount(Integer investUserId, Integer bidType);

	/**
	 * @Description : 根据标的ID及投资记录状态查询投资记录信息
	 * @Method_Name : findInvestRecord;
	 * @param bidId
	 *            标的ID
	 * @param state
	 *            投资状态
	 * @return : BidInvest;
	 * @Creation Date : 2017年8月4日 上午9:51:22;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	BidInvest findInvestRecord(Integer bidId, List<Integer> state);

	/**
	 * @Description : 根据userId查询用户投资总金额
	 * @Method_Name : findSumInvestAmountByUserId
	 * @param regUserId
	 * @return : BigDecimal
	 * @Creation Date : 2017年8月15日 下午2:53:53
	 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	BigDecimal findSumInvestAmountByUserId(Integer regUserId);

	/**
	 * @Description : 根据userId查询用户投资总金额(折价后)
	 * @Method_Name : findSumNiggerAmountByUserId
	 * @param regUserId
	 * @return : BigDecimal
	 * @Creation Date : 2017年8月15日 下午3:25:23
	 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	BigDecimal findSumNiggerAmountByUserId(Integer regUserId);

	/**
	 * 
	 * @Description : 查询用户投资金额
	 * @Method_Name : findSumInvestAmount
	 * @param friendIds
	 *            <用户id,投资金额>
	 * @return : Map<Integer,BidInvestForRecommendVo>
	 * @Creation Date : 2018年1月8日 下午5:25:05
	 * @Author : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	Map<Integer, BidInvestForRecommendVo> findSumInvestAmount(List<Integer> friendIds);

	/**
	 * 
	 * @Description :
	 * @Method_Name : findBidInvestByRegUserIds
	 * @param ids
	 * @return
	 * @return : List<BidInvestForRecommendVo>
	 * @Creation Date : 2018年1月11日 下午6:59:29
	 * @Author : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	List<BidInvestForRecommendVo> findBidInvestByRegUserIds(List<Integer> ids);

	/**
	 * @Description : 查询用户的投资记录，用于作为判断新手标投资的依据(体验金&活期不作为新手标判断依据)
	 * @Method_Name : findBidInvestCountForPreferred
	 * @param regUserId:
	 *            用户标识
	 * @return : Integer
	 * @Creation Date : 2018年1月25日 下午2:21:31
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	Integer findBidInvestCountForPrefered(Integer regUserId);

	/**
	 * 
	 * @Description :放款操作查询基本数据（产品信息、投资记录、投资总金额）
	 * @Method_Name : findInvestAndProduct
	 * @param bidInfo
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2018年2月2日 上午9:30:36
	 * @Author : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	ResponseEntity<?> findInvestAndProduct(BidInfo bidInfo);

	/**
	 * 
	 * @Description : 根据投资id查询投资记录
	 * @Method_Name : findBidInvestListByIds
	 * @param investIds
	 * @return : List<BidInvestVoForApp>
	 * @Creation Date : 2018年3月16日 下午4:21:59
	 * @Author : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	List<BidInvestVoForApp> findBidInvestListByIds(List<Integer> investIds);

	/**
	 * 
	 * @Description : 查询好友投资信息（只针对销售）
	 * @Method_Name : friendInvestListForSales
	 * @param friendUserId
	 * @param pager
	 * @return : Pager
	 * @Creation Date : 2018年3月21日 下午4:49:58
	 * @Author : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	/**
	 * @Description    ：方法描述信息 
	 * @Method_Name    ：friendInvestListForSales 
	* @param friendUserId
	* @param state
	* @param pager
	 * @return com.yirun.framework.core.utils.pager.Pager    
	 * @Creation Date  ：2018/12/25        
	 * @Author         ：pengwu@hongkunjinfu.com
	 */
	Pager friendInvestListForSales(Integer friendUserId,Integer state, Pager pager);

	/**
	 * @Description : 查询已投资过的用户id，包括投资体验金&活期
	 * @Method_Name : findBidInvestPreferedList
	 * @return : List<Integer>
	 * @Creation Date : 2018年4月2日 下午1:37:37
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	List<Integer> findBidInvestPreferedList();

	/**
	 * @Description ：获取优选标对应可匹配的投资记录集合
	 * @Method_Name ：findToBeMatchedBidInvestList
	 * @param goodBidId
	 *            优选标id
	 * @return java.util.List<com.hongkun.finance.invest.model.BidInvest>
	 * @Creation Date ：2018/4/13
	 * @Author ：pengwu@hongkun.com.cn
	 */
	List<BidInvest> findToBeMatchedBidInvestList(Integer goodBidId);

	/**
	 * @Description ：根据投资记录id集合获取投资记录集合
	 * @Method_Name ：findBidInvestListByIdList
	 * @param investIds
	 *            投资记录id集合
	 * @return java.util.List<com.hongkun.finance.invest.model.BidInvest>
	 * @Creation Date ：2018/4/18
	 * @Author ：pengwu@hongkun.com.cn
	 */
	List<BidInvest> findBidInvestListByIdList(List<Integer> investIds);

	/**
	 * @Description ：根据投资记录id获取投资记录集合和标地信息集合
	 * @Method_Name ：findBidInvestAndBidInfoByIdList
	 * @param investIdList
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * @Creation Date ：2018/4/19
	 * @Author ：pengwu@hongkun.com.cn
	 */
	Map<String, Object> findBidInvestAndBidInfoByIdList(List<Integer> investIdList);

	/**
	 * @Description ：通过散标投资记录查询匹配的优选投资记录
	 * @Method_Name ：findGoodBidInvestListByIdList
	 * @param investIds
	 * @return java.util.List<com.hongkun.finance.invest.model.BidInvest>
	 * @Creation Date ：2018/4/27
	 * @Author ：xuhui.liu@hongkun.com.cn 刘旭辉
	 */
	List<BidInvest> findGoodBidInvestListByIdList(Set<Integer> investIds);

	/**
	 * @Description ：近三个月有投资记录的用户
	 * @Method_Name ：findUserThreeMonthInvest
	 * @param userIdList
	 *            用户id集合
	 * @return java.util.List<java.lang.Integer>
	 * @Creation Date ：2018/5/2
	 * @Author ：pengwu@hongkun.com.cn
	 */
	List<Integer> findUserThreeMonthInvest(Set<Integer> userIdList);

	/**
	 * @Description ：查询匹配生成得投资记录
	 * @Method_Name ：findMatchBidInvestListByBidId
	 * @param bidId
	 * @return java.util.List<com.hongkun.finance.invest.model.BidInvest>
	 * @Creation Date ：2018/5/4
	 * @Author ：xuhui.liu@hongkun.com.cn 刘旭辉
	 */
	List<BidInvest> findMatchBidInvestListByBidId(Integer bidId);

	/**
	 * @Description ：获取优选标对应已放款的散标投资记录集合
	 * @Method_Name ：findGoodInvestMatchCommonInvestList
	 * @param condition
	 * @return java.util.List<com.hongkun.finance.invest.model.BidInvest>
	 * @Creation Date ：2018/6/4
	 * @Author ：pengwu@hongkun.com.cn
	 */
	List<BidInvest> findGoodInvestMatchCommonInvestList(BidInvest condition);

	/**
	 * @Description :根据条件查询bidinfoId集合
	 * @Method_Name : findBidInfoIdByCondition;
	 * @param bidInvest
	 * @return
	 * @return : List<Integer>;
	 * @Creation Date : 2018年6月12日 下午2:00:03;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	List<Integer> findBidInfoIdByCondition(BidInvest bidInvest);
	
	/**
	*  查询累计投资统计
	*  @Method_Name             ：findStaFunInvestSum
	*  @param staFunInvestVO
	*  @return Map<String, Object>
	*  @Creation Date           ：2018/9/18
	*  @Author                  ：zc.ding@foxmail.com
	*/
    Map<String, Object> findStaFunInvestAddup(StaFunInvestVO staFunInvestVO);
	
	/**
	*  分页检索客户投资统计
	*  @Method_Name             ：findStaFunInvestList
	*  @param pager
	*  @param staFunInvestVO
	*  @return com.yirun.framework.core.utils.pager.Pager
	*  @Creation Date           ：2018/9/20
	*  @Author                  ：zc.ding@foxmail.com
	*/
	Pager findStaFunInvestList(Pager pager, StaFunInvestVO staFunInvestVO);
	
	/**
	*  条件检索投资VO
	*  @param bidInvestDetailVO
	*  @return java.util.List<com.hongkun.finance.invest.model.vo.BidInvestDetailVO>
	*  @date                    ：2018/10/23
	*  @author                  ：zc.ding@foxmail.com
	*/
	List<BidInvestDetailVO> findBidInvestDetailList(BidInvestDetailVO bidInvestDetailVO);

	/**
	 * @Description: 查询当前用户和其邀请人的投资记录数量
	 * @param userIdList
	 * @return: java.lang.Integer
	 * @Author: hanghe@hongkunjinfu.com
	 * @Date: 2018/11/26 10:51
	 */
	Integer getSelfAndInvitorInvestCount(List<Integer> userIdList);

	/**
	 * @Description: 查询用户和用户邀请人是否有钱袋子转入记录
	 * @param userIdList
	 * @return: java.lang.Integer
	 * @Author: hanghe@hongkunjinfu.com
	 * @Date: 2018/11/26 10:56
	 */
	Integer getSelfAndInvitorTransferCount(List<Integer> userIdList);

	/**
	 * @Description      ：根据条件获取标地所有投资记录集合,默认条件(investAmount-transAmount > 0)的记录集合 
	 * @Method_Name      ：findBidInvestListByCondition 
	 * @param condition
	 * @return java.util.List<com.hongkun.finance.invest.model.BidInvest>    
	 * @Creation Date    ：2018/12/25        
	 * @Author           ：pengwu@hongkunjinfu.com
	 */
	List<BidInvest> findBidInvestListByCondition(BidInvest condition);
}
