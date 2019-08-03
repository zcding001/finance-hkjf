package com.hongkun.finance.invest.dao.impl;

import com.hongkun.finance.invest.dao.BidInfoDao;
import com.hongkun.finance.invest.dao.BidInvestDao;
import com.hongkun.finance.invest.model.BidAutoScheme;
import com.hongkun.finance.invest.model.BidInfo;
import com.hongkun.finance.invest.model.vo.BidInfoExchangeForAppVo;
import com.hongkun.finance.invest.model.vo.BidInfoSimpleVo;
import com.hongkun.finance.invest.model.vo.BidInfoVO;
import com.hongkun.finance.invest.model.vo.StaFunBidVO;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.core.exception.BusinessException;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.invest.dao.impl.BidInfoDaoImpl.java
 * @Class Name : BidInfoDaoImpl.java
 * @Description : GENERATOR DAO实现类
 * @Author : generator
 */
@Dao
public class BidInfoDaoImpl extends MyBatisBaseDaoImpl<BidInfo, java.lang.Long> implements BidInfoDao {

	/**
	 * 根据条件 分页查询
	 */

	public String FINDCONDITIONPAGE = ".findConditionPage";
	/**
	 * 根据产品类型，标的状态查询
	 */

	public String FIND_BIDINFO_LIST = ".findBidInfoList";
	/**
	 * 分页查询标的信息
	 */
	public String FIND_BIDD_INFO_LIST_BY_CONDITION = ".findBidInfoListByCondition";

	/** 根据条件查询待匹配的标的列表 */
	public String FIND_MATCH_BIDINFO_LIST = ".findMatchBidInfoList";

	/** 根据条件查询所有符合查询条件的标的信息 */
	public String FIND_ALL_MATCH_BIDINFO_LIST = ".findAllMatchBidInfoList";
	/** 根据条件查询散标或者优选*/
	public String FIND_GOOD_OR_COMMON_LIST = ".findGoodOrCommonList";
	/** 根据下次匹配时间范围查询标的列表*/
	public String FIND_BIDINFODETAILS_BY_ENDDATE = ".findBidInfoDetailsByEndDate";
	/** 根据标的id查询标的详情*/
	public String FIND_BIDINFODETAILVO_BY_Id = ".findBidInfoDetailVoById";
	/** 查询标的信息*/
	public String FIND_BIDINFO_SIMPLE_LIST = ".findBidInfoSimpleVoList";
	/**查询购房宝，物业宝标的信息**/
	public String FIND_PURCHASE_BIDINFO_LIST = ".findPurchaseBidInfoList";
	
	@Override
	public Pager findConditionBidInfo(BidInfo bidInfo, Pager pager) {
		if (bidInfo == null) {
			throw new BusinessException(" condition can't null!");
		}
		return this.findByCondition(bidInfo, pager, FINDCONDITIONPAGE);
	}

	@Override
	public List<BidInfo> findBidInfoList(Integer productType, Integer state, String bidName) {
		HashMap<String, Object> parameter = new HashMap<>();
		parameter.put("productType", productType);
		parameter.put("bidState", state);
		parameter.put("bidName", bidName);
		return getCurSqlSessionTemplate().selectList(BidInfo.class.getName() + FIND_BIDINFO_LIST, parameter);

	}

	@Override
	public Pager findBidInfoList(Integer productType, List<Integer> stateList, String bidName, Pager pager) {
		HashMap<String, Object> parameter = new HashMap<>();
		parameter.put("productType", productType);
		parameter.put("bidStateList", stateList);
		parameter.put("bidName", bidName);
		return this.findByCondition(parameter, pager, BidInfo.class, FIND_BIDD_INFO_LIST_BY_CONDITION);
	}

	@Override
	public Pager findMatchBidInfoList(BidInfoVO contidion, Pager pager) {
		return this.findByCondition(contidion, pager, BidInfo.class, FIND_MATCH_BIDINFO_LIST);
	}

	@Override
	public List<BidInfoVO> findMatchBidInfoList(BidInfoVO contidion) {
		return getCurSqlSessionTemplate().selectList(BidInfo.class.getName() +FIND_MATCH_BIDINFO_LIST,contidion );
	}

	@Override
	public Pager findBidInfoVOByCondition(BidInfoVO contidion, Pager pager) {
		return this.findByCondition(contidion, pager, BidInfo.class, FIND_ALL_MATCH_BIDINFO_LIST);
	}

	@Override
	public List<BidInfo> findGoodOrCommonList(List<Integer> state,int matchType, List<Integer> productTypes) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("state",state);
		params.put("matchType", matchType);
		params.put("productTypes", productTypes);
		return getCurSqlSessionTemplate().selectList(BidInfo.class.getName() + FIND_GOOD_OR_COMMON_LIST, params);
	}

	@Override
	public List<BidInfoVO> findBidInfoDetailsByEndDate(Date endDate) {
		return getCurSqlSessionTemplate().selectList(BidInfo.class.getName() + FIND_BIDINFODETAILS_BY_ENDDATE, endDate);
	}

	@Override
	public BidInfoVO findBidInfoDetailVoById(int bidInfoId) {
		return getCurSqlSessionTemplate().selectOne(BidInfo.class.getName()+FIND_BIDINFODETAILVO_BY_Id, bidInfoId);
	}

	@Override
	public List<BidInfoVO> findBidInfoVOList(BidInfoVO vo) {
		return getCurSqlSessionTemplate().selectList(BidInfo.class.getName() + ".findBidInfoDetailVoList", vo);
	}

	@Override
	public List<BidInfoVO> findAutoInvestBidList(BidAutoScheme bidAutoScheme) {
		return getCurSqlSessionTemplate().selectList(BidInfo.class.getName() + ".findAutoInvestBidList", bidAutoScheme);
	}

	@Override
	public Pager findBidInfoSimpleVoList(BidInfoSimpleVo bidInfoSimpleVo, Pager pager) {
		return this.findByCondition(bidInfoSimpleVo, pager, BidInfo.class, FIND_BIDINFO_SIMPLE_LIST);
	}

	@Override
	public List<BidInfoVO> findBidInfoVOByCondition(BidInfoVO queryBidInfoVO) {
		return getCurSqlSessionTemplate().selectList(BidInfo.class.getName() + FIND_ALL_MATCH_BIDINFO_LIST, queryBidInfoVO);
	}

	@Override
	public Integer updateBidInfoForMakeLoan(BidInfo bidInfo) {
		return getCurSqlSessionTemplate().update(BidInfo.class.getName() + ".updateBidInfoForMakeLoan", bidInfo);
	}

	@Override
	public Map<Integer, BidInfoVO> findBidInfoDetailVoByIdList(Set<Integer> bidIdSet) {
		return this.getCurSqlSessionTemplate().selectMap(BidInfo.class.getName() +
						".findBidInfoDetailVoByIdList", bidIdSet, "id");
	}
	@Override
	public List<BidInfo> findPurchaseBidInfoList(List<Integer> productTypeList, Integer state) {
		HashMap<String, Object> parameter = new HashMap<>();
		parameter.put("productTypeList", productTypeList);
		parameter.put("bidState", state);
		return getCurSqlSessionTemplate().selectList(BidInfo.class.getName() + FIND_PURCHASE_BIDINFO_LIST, parameter);
	}

    @Override
    public Pager findStaFunBid(Pager pager, StaFunBidVO staFunBidVO) {
        return this.findByCondition(staFunBidVO, pager, BidInfo.class, ".findStaFunBid");
    }

    @Override
    public Map<String, Object> findStaBidUserAmountCount(StaFunBidVO staFunBidVO) {
        return getCurSqlSessionTemplate().selectOne(BidInfo.class.getName() + ".findStaBidUserAmountCount", staFunBidVO);
    }

	@Override
	public Pager findExchangeBidList(String bidName, Pager pager) {
		BidInfo cdt = new BidInfo();
		cdt.setName(bidName);
		return this.findByCondition(cdt,pager,".findExchangeBidList");
	}

	@Override
	public List<BidInfoExchangeForAppVo> findBidInfoExchangeListForApp() {
		return getCurSqlSessionTemplate().selectList(BidInfo.class.getName() + ".findBidInfoExchangeListForApp");
	}
}
