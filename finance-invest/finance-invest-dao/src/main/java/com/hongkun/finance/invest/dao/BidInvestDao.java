package com.hongkun.finance.invest.dao;

import com.hongkun.finance.invest.model.BidInvest;
import com.hongkun.finance.invest.model.vo.BidInvestDetailVO;
import com.hongkun.finance.invest.model.vo.BidInvestForRecommendVo;
import com.hongkun.finance.invest.model.vo.BidInvestVoForApp;
import com.hongkun.finance.invest.model.vo.StaFunInvestVO;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.invest.dao.BidInvestDao.java
 * @Class Name : BidInvestDao.java
 * @Description : GENERATOR DAO类
 * @Author : generator
 */
public interface BidInvestDao extends MyBatisBaseDao<BidInvest, java.lang.Long> {

	List<BidInvest> findByGoodBidId(Integer id);

	/**
	*  查询标的不同投资渠道的投资总金额
	*  @param bidInfoId 标的标识
	*  @return java.util.Map<java.lang.String,java.lang.Object>
	*  @date                    ：2018/12/3
	*  @author                  ：zc.ding@foxmail.com
	*/
    Map<String, Object> findSumAmountByBidId(int bidInfoId);

	/**
	 * 
	 * @Description : 获取产品类型有效投资总金额
	 * @Method_Name : findQdzThirdInvestSumAmount
	 * @return
	 * @return : BigDecimal
	 * @Creation Date : 2017年7月17日 下午6:11:19
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	BigDecimal findInvestSumAmount(Integer productType, Integer bidState, Integer regUserId);

	/**
	 * 
	 * @Description : 获取该产品类型用户有效投资记录
	 * @Method_Name : findInvests
	 * @return
	 * @return : BigDecimal
	 * @Creation Date : 2017年7月17日 下午6:11:19
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	List<BidInvest> findInvests(Integer productType, Integer bidState, Integer regUserId);

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
	 * @return
	 * @return : int;
	 * @Creation Date : 2017年7月31日 下午3:39:22;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	int findBidInvestCount(Integer bidId, Integer investUserId, Date createTime, Date endTime, Integer recommendState);

	/**
	 * @Description : 根据标的ID及投资记录状态查询投资记录信息
	 * @Method_Name : findInvestRecord;
	 * @param bidId
	 *            标的ID
	 * @param state
	 *            投资状态
	 * @return
	 * @return : BidInvest;
	 * @Creation Date : 2017年8月4日 上午9:51:22;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	BidInvest findInvestRecord(Integer bidId, List<Integer> state);

	/**
	 * @Description : 查询用户投资总金额
	 * @Method_Name : findSumInvestAmountByUserId
	 * @param regUserId
	 * @return
	 * @return : BigDecimal
	 * @Creation Date : 2017年8月15日 下午2:56:13
	 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	BigDecimal findSumInvestAmountByUserId(Integer regUserId);

	/**
	 * @Description : 查询用户投资总金额(折价后)
	 * @Method_Name : findSumNiggerAmountByUserId
	 * @param regUserId
	 * @return
	 * @return : BigDecimal
	 * @Creation Date : 2017年8月15日 下午3:26:02
	 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	BigDecimal findSumNiggerAmountByUserId(Integer regUserId);

	/**
	 * 
	 * @Description : 查询某些投资人的投资记录（优选）
	 * @Method_Name : findBidInvestByRegUserIds
	 * @param ids
	 * @return
	 * @return : List<BidInvestForRecommendVo>
	 * @Creation Date : 2018年1月12日 上午9:02:09
	 * @Author : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	List<BidInvestForRecommendVo> findBidInvestByRegUserIds(List<Integer> ids);

	/**
	 * @Description : 查询用户投资记录，作为判断用户投资新手标依据
	 * @Method_Name : findBidInvestCountForPrefered
	 * @param regUserId:
	 *            用户标识
	 * @return : Integer
	 * @Creation Date : 2018年1月25日 下午2:23:19
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	Integer findBidInvestCountForPrefered(Integer regUserId);

	List<BidInvestVoForApp> findBidInvestListByIds(List<Integer> investIds);

	/**
	 * @Description : 查询已投用户
	 * @Method_Name : findBidInvestPreferedList
	 * @return : Pager
	 * @Creation Date : 2018年4月2日 上午11:36:17
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
	 * @Description ：根据投资id批量删除投资记录
	 * @Method_Name ：delBatchByIds
	 * @param delIds
	 * @return void
	 * @Creation Date ：2018/4/24
	 * @Author ：xuhui.liu@hongkun.com.cn 刘旭辉
	 */
	void delBatchByIds(List<Integer> delIds);

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
	*  统计投资人数、金额、数量
	*  @Method_Name             ：findStaFunInvestAddup
	*  @param staFunInvestVO
	*  @return com.hongkun.finance.invest.model.vo.StaFunInvestVO
	*  @Creation Date           ：2018/9/20
	*  @Author                  ：zc.ding@foxmail.com
	*/    
    Map<String, Object> findStaFunInvestAddup(StaFunInvestVO staFunInvestVO);

    /**
    *  条件检索投资VO
    *  @param bidInvestDetailVO 检索条件
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
	 * @Date: 2018/11/26 10:55
	 */
	Integer getSelfAndInvitorInvestCount(List<Integer> userIds);

	/**
	 * @Description: 查询用户和用户邀请人是否有钱袋子转入记录
	 * @param userIdList
	 * @return: java.lang.Integer
	 * @Author: hanghe@hongkunjinfu.com
	 * @Date: 2018/11/26 10:59
	 */
	Integer getSelfAndInvitorTransferCount(List<Integer> userIds);

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
