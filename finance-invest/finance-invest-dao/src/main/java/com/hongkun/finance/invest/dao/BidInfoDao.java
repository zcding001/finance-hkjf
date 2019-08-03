package com.hongkun.finance.invest.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hongkun.finance.invest.model.BidAutoScheme;
import com.hongkun.finance.invest.model.BidInfo;
import com.hongkun.finance.invest.model.vo.BidInfoExchangeForAppVo;
import com.hongkun.finance.invest.model.vo.BidInfoSimpleVo;
import com.hongkun.finance.invest.model.vo.BidInfoVO;
import com.hongkun.finance.invest.model.vo.StaFunBidVO;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.invest.dao.BidInfoDao.java
 * @Class Name : BidInfoDao.java
 * @Description : GENERATOR DAO类
 * @Author : generator
 */
public interface BidInfoDao extends MyBatisBaseDao<BidInfo, java.lang.Long> {

	/**
	 * 根据条件查询分页信息
	 * 
	 * @param bidInfo
	 * @param pager
	 * @return
	 */
	Pager findConditionBidInfo(BidInfo bidInfo, Pager pager);

	/**
	 * 
	 * @Description : 根据产品类型&状态，获取标的信息
	 * @Method_Name : findBidInfoList
	 * @param productType
	 * @param state
	 * @return
	 * @return : List<BidInfo>
	 * @Creation Date : 2017年7月17日 下午5:18:27
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	List<BidInfo> findBidInfoList(Integer productType, Integer state, String bidName);

	/**
	 * @Description : 分页查询标的信息
	 * @Method_Name : findBidInfoList;
	 * @param productType
	 *            产品类型
	 * @param List<Integer>
	 *            stateList 状态
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
	 * @Description : 查询待匹配标的列表（距下次匹配时间10天之内）
	 * @Method_Name : findMatchBidInfoList
	 * @param contidion
	 * @param pager
	 * @return
	 * @return : Pager
	 * @Creation Date : 2017年7月19日 下午5:33:00
	 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	Pager findMatchBidInfoList(BidInfoVO contidion, Pager pager);

	/**
	 * @Description : 查询待匹配标的列表（距下次匹配时间10天之内）
	 * @Method_Name : findMatchBidInfoList
	 * @param contidion
	 * @return
	 * @return : Pager
	 * @Creation Date : 2017年7月19日 下午5:33:00
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
	 * @Creation Date : 2017年7月20日 下午2:03:22
	 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	Pager findBidInfoVOByCondition(BidInfoVO contidion, Pager pager);
	/**
	 *  @Description    : 查询优选/散标
	 *  @Method_Name    : findGoodOrCommonList
	 *  @param matchTypes
	 *  @param productTypes
	 *  @return
	 *  @return         : List<BidInfo>
	 *  @Creation Date  : 2017年7月21日 下午4:35:29 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	List<BidInfo> findGoodOrCommonList(List<Integer> state,int matchType,List<Integer> productTypes);

	List<BidInfoVO> findBidInfoDetailsByEndDate(Date endDate);
	/**
	 *  @Description    : 通过标的id查询标的详情（包含info、detail、product等信息）
	 *  @Method_Name    : findBidInfoDetailVoById
	 *  @param bidInfoId
	 *  @return
	 *  @return         : BidInfoDetailVo
	 *  @Creation Date  : 2017年9月5日 下午2:08:38 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	BidInfoVO findBidInfoDetailVoById(int bidInfoId);
	
	/**
	 *  @Description    : 检索满足条件的所有BidInfoVO
	 *  @Method_Name    : findBidInfoVOList
	 *  @param vo
	 *  @return         : List<BidInfoVO>
	 *  @Creation Date  : 2017年9月30日 下午3:09:57 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	List<BidInfoVO> findBidInfoVOList(BidInfoVO vo);
	
	/**
	 *  @Description    : 查询用于自动投资的标的数据
	 *  @Method_Name    : findAutoInvestBidList
	 *  @param bidAutoScheme
	 *  @return
	 *  @return         : List<BidInfo>
	 *  @Creation Date  : 2018年1月24日 下午4:54:22 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	List<BidInfoVO> findAutoInvestBidList(BidAutoScheme bidAutoScheme);
	/**
	 * 
	 *  @Description    : 查询标的简单信息（bid_info表中所有字段和关联表中少许字段）
	 *  @Method_Name    : findBidInfoSimpleVoList
	 *  @param bidInfoSimpleVo
	 *  @param pager
	 *  @return
	 *  @return         : Pager
	 *  @Creation Date  : 2018年1月30日 下午2:03:28 
	 *  @Author         : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	Pager findBidInfoSimpleVoList(BidInfoSimpleVo bidInfoSimpleVo, Pager pager);

	/**
	 * 查询指定条件的bidINfoVo
	 * @param queryBidInfoVO
	 * @return
	 */
	List<BidInfoVO> findBidInfoVOByCondition(BidInfoVO queryBidInfoVO);
	/**
	 * 
	 *  @Description    : 放款更新标的信息（其他业务慎用）
	 *  @Method_Name    : updateBidInfoForMakeLoan
	 *  @param bidInfo
	 *  @return
	 *  @return         : Pager
	 *  @Creation Date  : 2018年1月30日 下午2:03:28 
	 *  @Author         : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	Integer updateBidInfoForMakeLoan(BidInfo bidInfo);

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
    *  @param staFunBidV
    *  @return com.yirun.framework.core.utils.pager.Pager
    *  @Creation Date           ：2018/9/19
    *  @Author                  ：zc.ding@foxmail.com
    */
    Pager findStaFunBid(Pager pager, StaFunBidVO staFunBidV);

    Map<String, Object> findStaBidUserAmountCount(StaFunBidVO staFunBidVO);

	Pager findExchangeBidList(String bidName, Pager pager);
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
