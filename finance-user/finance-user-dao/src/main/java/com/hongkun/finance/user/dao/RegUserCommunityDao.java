package com.hongkun.finance.user.dao;

import com.hongkun.finance.user.model.RegUserCommunity;
import com.hongkun.finance.user.model.vo.RegUserCommunityVO;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

import java.util.List;
import java.util.Map;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.user.dao.RegUserCommunityDao.java
 * @Class Name    : RegUserCommunityDao.java
 * @Description   : GENERATOR DAO类
 * @Author        : generator
 */
public interface RegUserCommunityDao extends MyBatisBaseDao<RegUserCommunity, java.lang.Long> {
	/**
	 *  @Description    : 查询小区列表（用于数据字典）
	 *  @Method_Name    : findCommunityDicDataList
	 *  @return
	 *  @return         : List<Map<String,Object>>
	 *  @Creation Date  : 2017年10月26日 下午1:53:36 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	List<Map<String, Object>> findCommunityDicDataList(Integer regUserId);

	/**
	 * 加载线下门店的地址
	 * @return
	 */
    List<RegUserCommunityVO> loadOfflineStoreAddress();

	/**
	 * 找到小区的list
	 * @param communityVO
	 * @param pager
	 * @return
	 */
    Pager findCommunityList(RegUserCommunityVO communityVO, Pager pager);

	/**
	 * 级联删除小区
	 * @param id
	 * @return
	 */
	boolean delectCommunityOnCascade(Integer id);

	/**
	 * 查询所有可用的小区
	 * @return
	 */
    List<RegUserCommunity> findCommunityAvailable();

	/**
	 * 解绑小区
	 * @param communityRelShouldDelete
	 */
	void unBindCommunity(List<Integer> communityRelShouldDelete);

	/**
	 * 绑定小区关系
	 * @param communityVO
	 */
	void bindCommunityToTenument(RegUserCommunityVO communityVO);

	List<RegUserCommunity> findAllCommunityList();
}
