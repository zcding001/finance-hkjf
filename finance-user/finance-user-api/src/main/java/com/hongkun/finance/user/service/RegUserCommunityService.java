package com.hongkun.finance.user.service;

import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.model.RegUserCommunity;
import com.hongkun.finance.user.model.vo.OfflineStoreVO;
import com.hongkun.finance.user.model.vo.RegUserCommunityVO;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;

import java.util.List;
import java.util.Map;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.user.service.RegUserCommunityService.java
 * @Class Name    : RegUserCommunityService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface RegUserCommunityService {
	
	/**
	 * @Described			: 单条插入
	 * @param regUserCommunity 持久化的数据对象
	 * @return				: void
	 */
	Integer insertRegUserCommunity(RegUserCommunity regUserCommunity);
	
	/**
	 * @Described			: 批量插入
	 * @param List<RegUserCommunity> 批量插入的数据
	 * @return				: void
	 */
	void insertRegUserCommunityBatch(List<RegUserCommunity> list);
	
	/**
	 * @Described			: 批量插入
	 * @param List<RegUserCommunity> 批量插入的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void insertRegUserCommunityBatch(List<RegUserCommunity> list, int count);
	
	/**
	 * @Described			: 更新数据
	 * @param regUserCommunity 要更新的数据
	 * @return				: void
	 */
	void updateRegUserCommunity(RegUserCommunity regUserCommunity);
	
	/**
	 * @Described			: 批量更新数据
	 * @param regUserCommunity 要更新的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void updateRegUserCommunityBatch(List<RegUserCommunity> list, int count);
	
	/**
	 * @Described			: 通过id查询数据
	 * @param id id值
	 * @return	RegUserCommunity
	 */
	RegUserCommunity findRegUserCommunityById(int id);
	
	/**
	 * @Described			: 条件检索数据
	 * @param regUserCommunity 检索条件
	 * @return	List<RegUserCommunity>
	 */
	List<RegUserCommunity> findRegUserCommunityList(RegUserCommunity regUserCommunity);
	
	/**
	 * @Described			: 条件检索数据
	 * @param regUserCommunity 检索条件
	 * @param pager	分页数据
	 * @return	List<RegUserCommunity>
	 */
	Pager findRegUserCommunityList(RegUserCommunity regUserCommunity, Pager pager);

	/**
	 * @Described			: 统计条数
	 * @param regUserCommunity 检索条件
	 * @param pager	分页数据
	 * @return	int
	 */
	int findRegUserCommunityCount(RegUserCommunity regUserCommunity);

	/**
	 * 
	 *  @Description    : 获取小区列表（用于数据字典）
	 *  @Method_Name    : findCommunityDicDataList
	 *  @return
	 *  @return         : List<Map<String,Object>>
	 *  @Creation Date  : 2017年10月26日 下午1:48:19 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	List<Map<String, Object>> findCommunityDicDataList(Integer regUserId);


	/**
	 *  @Description    : 加载线下门店地址
	 *  @Method_Name    : loadOfflineStoreAddress
	 *  @return          :  List<OfflineStoreVO>
	 *  @Creation Date  : 2018年4月8日10:26:34
	 *  @Author         : zhongpingtang@hongkun.com.cn
	 */
    List<OfflineStoreVO> loadOfflineStoreAddress();

	/**
	 * 查询分页的小区list数据
	 * @param communityVO
	 * @param pager
	 * @return
	 */
	Pager findCommunityList(RegUserCommunityVO communityVO, Pager pager);


	/**
	 *  @Description    : 级联删除小区
	 *  @Method_Name    : deleteCommunityOnCascade
	 *   @param id: 小区ID
	 *  @return          : ResponseEntity
	 *  @Creation Date  : 2018年4月8日10:26:34
	 *  @Author         : zhongpingtang@hongkun.com.cn
	 */
	ResponseEntity deleteCommunityOnCascade(Integer id);

	/**
	 *  @Description    : 查询不带分页的物业列表
	 *  @Method_Name    : findUserTypeTenementNoPage
	 *  @return          : List<RegUser>
	 *  @Creation Date  : 2018年4月8日10:26:34
	 *  @Author         : zhongpingtang@hongkun.com.cn
	 */
	List<RegUser> findUserTypeTenementNoPage();


	/**
	 *  @Description    : 查询物业下的小区
	 *  @Method_Name    : findTenementsCommunity
	 *  @return          : List<RegUser>
	 * @param id         :物业ID
	 *  @Creation Date  : 2018年4月8日10:26:34
	 *  @Author         : zhongpingtang@hongkun.com.cn
	 */
	List<RegUserCommunity> findTenementsCommunity(Integer id);


	/**
	 *  @Description    : 查询所有未被物业绑定的小区
	 *  @Method_Name    : findTenementsCommunity
	 *  @return          :List<RegUserCommunity>
	 *  @Creation Date  : 2018年4月8日10:26:34
	 *  @Author         : zhongpingtang@hongkun.com.cn
	 */
	List<RegUserCommunity> findCommunityAvailable();

	/**
	 *  @Description    : 绑定小区到物业
	 *  @Method_Name    : bindCommunityToTenement
	 *  @return          : ResponseEntity
	 * @param communityVO         :小区VO
	 *  @Creation Date  : 2018年4月8日10:26:34
	 *  @Author         : zhongpingtang@hongkun.com.cn
	 */
	ResponseEntity bindCommunityToTenement(RegUserCommunityVO communityVO);


	/**
	 *  @Description    : 加载所有的物业
	 *  @Method_Name    : loadProperties
	 *  @return          : List<Map<String, Object>>
	 *  @Creation Date  : 2018年4月8日10:26:34
	 *  @Author         : zhongpingtang@hongkun.com.cn
	 */
	List<Map<String, Object>> loadProperties();

	/**
	 *  @Description    : 根据propertyId加载物业下的线下门店
	 *  @Method_Name    : loadOfflineStoreAddressByPropertyId
	 *  @return          : List<Map<String, Object>>
	 *  @param propertyId         :物业ID
	 *  @Creation Date  : 2018年4月8日10:26:34
	 *  @Author         : zhongpingtang@hongkun.com.cn
	 */
	List<Map<String, Object>> loadOfflineStoreAddressByPropertyId(Integer propertyId);

	/**
	 *  @Description    : 查询可用小区列表
	 *  @Method_Name    : findAllCommunityList
	 *  @return          :List<RegUserCommunity>
	 *  @Creation Date  : 2018年4月8日10:26:34
	 *  @Author         : zhongpingtang@hongkun.com.cn
	 */
	List<RegUserCommunity> findAllCommunityList();
}
