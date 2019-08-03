package com.hongkun.finance.fund.service;

import com.hongkun.finance.fund.model.*;
import com.hongkun.finance.fund.model.vo.HouseInfoAndDetail;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;

import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.fund.service.HouseProInfoService.java
 * @Class Name    : HouseProInfoService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface HouseProInfoService {
	
	/**
	 * @Described			: 单条插入
	 * @param houseProInfo 持久化的数据对象
	 * @return				: void
	 */
	void insertHouseProInfo(HouseProInfo houseProInfo);
	
	/**
	 * @Described			: 批量插入
	 * @param List<HouseProInfo> 批量插入的数据
	 * @return				: void
	 */
	void insertHouseProInfoBatch(List<HouseProInfo> list);
	
	/**
	 * @Described			: 批量插入
	 * @param List<HouseProInfo> 批量插入的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void insertHouseProInfoBatch(List<HouseProInfo> list, int count);
	
	/**
	 * @Described			: 更新数据
	 * @param houseProInfo 要更新的数据
	 * @return				: void
	 */
	void updateHouseProInfo(HouseProInfo houseProInfo);
	
	/**
	 * @Described			: 批量更新数据
	 * @param houseProInfo 要更新的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void updateHouseProInfoBatch(List<HouseProInfo> list, int count);
	
	/**
	 * @Described			: 通过id查询数据
	 * @param id id值
	 * @return	HouseProInfo
	 */
	HouseProInfo findHouseProInfoById(int id);
	
	/**
	 * @Described			: 条件检索数据
	 * @param houseProInfo 检索条件
	 * @return	List<HouseProInfo>
	 */
	List<HouseProInfo> findHouseProInfoList(HouseProInfo houseProInfo);
	
	/**
	 * @Described			: 条件检索数据
	 * @param houseProInfo 检索条件
	 * @param start	起始页
	 * @param limit 检索条数
	 * @return	List<HouseProInfo>
	 */
	List<HouseProInfo> findHouseProInfoList(HouseProInfo houseProInfo, int start, int limit);
	
	/**
	 * @Described			: 条件检索数据
	 * @param houseProInfo 检索条件
	 * @param pager	分页数据
	 * @return	List<HouseProInfo>
	 */
	Pager findHouseProInfoList(HouseProInfo houseProInfo, Pager pager);

	/**
	 * @Described			: 统计条数
	 * @param houseProInfo 检索条件
	 * @param pager	分页数据
	 * @return	int
	 */
	int findHouseProInfoCount(HouseProInfo houseProInfo);
	
	/**
	 * 自定义sql查询count
	 * @param obj
	 * @param pager
	 * @param sqlName
	 * @return
	 */
	Pager findHouseProInfoList(HouseProInfo houseProInfo, Pager pager, String sqlName/*添加一个自定义的sql的名字*/);
	
	/**
	 * 
	 *  @Description    : 
	 *  @Method_Name    : getTotalCount
	 *  @param object
	 *  @param sqlName
	 *  @return
	 *  @return         : Integer
	 *  @Creation Date  : 2017年6月15日 上午10:14:01 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	Integer findHouseProInfoCount(HouseProInfo houseProInfo, String sqlName);

	Pager findHouseInfoVoPageList(Pager pager,String basicDetailUrl);

    HouseInfoAndDetail findHouseInfoAndDetailById(Integer infoId);
	/**
	*  @Description    ：查询某个项目某种类型图片
	*  @Method_Name    ：getHouseProPicList
	*  @param infoId
	*  @param type  1-户型图 2-项目相册 3-封面图
	*  @return java.util.List<com.hongkun.finance.fund.model.HouseProPic>
	*  @Creation Date  ：2018/10/10
	*  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
	*/
	List<HouseProPic> getHouseProPicList(Integer infoId, Integer type );

	List<HouseProPermit> getHouseProPermitList(Integer infoId);

	/**
	 *  @Description    ：添加房产信息
	 *  @Method_Name    ：addHouse
	 *  @param houseProInfo
	 *  @param houseProDetail
	 *  @param houseProIntroduce
	 *  @param houseDTO
	 *  @return com.yirun.framework.core.model.ResponseEntity<?>
	 *  @Creation Date  ：2018/10/11
	 *  @Author         ：pengwu@hongkun.com.cn
	 */
    ResponseEntity<?> addHouse(HouseProInfo houseProInfo, HouseProDetail houseProDetail,
							   HouseProIntroduce houseProIntroduce, HouseDTO houseDTO);

    /**
     *  @Description    ：上架房产信息
     *  @Method_Name    ：upHouse
     *  @param id 房产信息id
     *  @return com.yirun.framework.core.model.ResponseEntity<?>
     *  @Creation Date  ：2018/10/11
     *  @Author         ：pengwu@hongkun.com.cn
     */
	ResponseEntity<?> upHouse(int id);

	/**
	 *  @Description    ：下架房产信息
	 *  @Method_Name    ：downHouse
	 *  @param id 房产信息id
	 *  @return com.yirun.framework.core.model.ResponseEntity<?>
	 *  @Creation Date  ：2018/10/11
	 *  @Author         ：pengwu@hongkun.com.cn
	 */
	ResponseEntity<?> downHouse(int id);

	/**
	 *  @Description    ：获取房产信息
	 *  @Method_Name    ：findHouse
	 *  @param id 房产id
	 *  @return com.yirun.framework.core.model.ResponseEntity<?>
	 *  @Creation Date  ：2018/10/15
	 *  @Author         ：pengwu@hongkun.com.cn
	 */
	ResponseEntity<?> findHouse(int id);

	/**
	 *  @Description    ：修改房产信息
	 *  @Method_Name    ：updateHouse
	 *  @param houseProInfo
	 *  @param houseProDetail
	 *  @param houseProIntroduce
	 *  @return com.yirun.framework.core.model.ResponseEntity<?>
	 *  @Creation Date  ：2018/10/15
	 *  @Author         ：pengwu@hongkun.com.cn
	 */
	ResponseEntity<?> updateHouse(HouseProInfo houseProInfo, HouseProDetail houseProDetail, HouseProIntroduce houseProIntroduce);
}
