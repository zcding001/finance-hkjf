package com.hongkun.finance.roster.service;

import java.util.List;

import com.hongkun.finance.roster.model.RosStaffInfo;
import com.hongkun.finance.roster.vo.RosStaffInfoVo;
import com.yirun.framework.core.utils.pager.Pager;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.roster.service.RosStaffInfoService.java
 * @Class Name : RosStaffInfoService.java
 * @Description : GENERATOR SERVICE类
 * @Author : generator
 */
public interface RosStaffInfoService {

	/**
	 * @Described : 单条插入
	 * @param rosStaffInfo
	 *            持久化的数据对象
	 * @return : void
	 */
	int insertRosStaffInfo(RosStaffInfo rosStaffInfo);

	/**
	 * @Described : 批量插入
	 * @param List<RosStaffInfo>
	 *            批量插入的数据
	 * @return : void
	 */
	void insertRosStaffInfoBatch(List<RosStaffInfo> list);

	/**
	 * @Described : 批量插入
	 * @param List<RosStaffInfo>
	 *            批量插入的数据
	 * @param count
	 *            多少条数提交一次
	 * @return : void
	 */
	void insertRosStaffInfoBatch(List<RosStaffInfo> list, int count);

	/**
	 * @Described : 更新数据
	 * @param rosStaffInfo
	 *            要更新的数据
	 * @return : void
	 */
	int updateRosStaffInfo(RosStaffInfo rosStaffInfo);

	/**
	 * @Described : 批量更新数据
	 * @param rosStaffInfo
	 *            要更新的数据
	 * @param count
	 *            多少条数提交一次
	 * @return : void
	 */
	void updateRosStaffInfoBatch(List<RosStaffInfo> list, int count);

	/**
	 * @Described : 通过id查询数据
	 * @param id
	 *            id值
	 * @return RosStaffInfo
	 */
	RosStaffInfo findRosStaffInfoById(int id);

	/**
	 * @Described : 条件检索数据
	 * @param rosStaffInfo
	 *            检索条件
	 * @return List<RosStaffInfo>
	 */
	List<RosStaffInfo> findRosStaffInfoList(RosStaffInfo rosStaffInfo);

	/**
	 * @Described : 条件检索数据
	 * @param rosStaffInfo
	 *            检索条件
	 * @param start
	 *            起始页
	 * @param limit
	 *            检索条数
	 * @return List<RosStaffInfo>
	 */
	List<RosStaffInfo> findRosStaffInfoList(RosStaffInfo rosStaffInfo, int start, int limit);

	/**
	 * @Described : 条件检索数据
	 * @param rosStaffInfo
	 *            检索条件
	 * @param pager
	 *            分页数据
	 * @return List<RosStaffInfo>
	 */
	Pager findRosStaffInfoList(RosStaffInfo rosStaffInfo, Pager pager);

	/**
	 * @Described : 统计条数
	 * @param rosStaffInfo
	 *            检索条件
	 * @param pager
	 *            分页数据
	 * @return int
	 */
	int findRosStaffInfoCount(RosStaffInfo rosStaffInfo);

	/**
	 * 自定义sql查询count
	 * 
	 * @param obj
	 * @param pager
	 * @param sqlName
	 * @return
	 */
	Pager findRosStaffInfoList(RosStaffInfo rosStaffInfo, Pager pager, String sqlName/* 添加一个自定义的sql的名字 */);

	/**
	 * 
	 * @Description :
	 * @Method_Name : getTotalCount
	 * @param sqlName
	 * @return
	 * @return : Integer
	 * @Creation Date : 2017年6月15日 上午10:14:01
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	Integer findRosStaffInfoCount(RosStaffInfo rosStaffInfo, String sqlName);

	/**
	 * @Description : 根据用户ID，及类型、状态查询内部员工信息
	 * @Method_Name : findRosStaffInfo;
	 * @param regUserId
	 *            : 用户id 必填
	 * @param type
	 * @param state
	 * @return
	 * @return : RosStaffInfo;
	 * @Creation Date : 2017年7月31日 下午4:58:38;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	RosStaffInfo findRosStaffInfo(Integer regUserId, Integer type, Integer state,Integer recommendState);

	/**
	 * @Description : 根据条件查询员工列表
	 * @Method_Name : findRosStaffInfoList
	 * @param rosStaffInfoContidion
	 * @return
	 * @return : List<RosStaffInfoVo>
	 * @Creation Date : 2018年1月8日 下午2:09:07
	 * @Author : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	List<RosStaffInfoVo> findRosStaffInfoList(RosStaffInfoVo rosStaffInfoContidion);

	/**
	 * @Description : 根据用户ID，及类型、状态查询员工信息
	 * @Method_Name : findRosStaffInfo;
	 * @param List<Integer>
	 *            regUserIdList 用户ID
	 * @param type
	 *            用户类型 '类型 1：物业员工，2：销售员工，3：内部员工',
	 * @param state
	 *            '0:删除，1：正常',
	 * @return
	 * @return : List<Integer>;
	 * @Creation Date : 2018年5月7日 下午4:59:16;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	List<Integer> findRosStaffInfoLists(List<Integer> regUserIdList, Integer type, Integer state);

	/**
	 * @Description : 通过类型查询用户 ID集合
	 * @Method_Name : findRosStaffInfoByTypes;
	 * @param types
	 * @return
	 * @return : List<Integer>;
	 * @Creation Date : 2018年5月7日 下午6:01:03;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	List<Integer> findRosStaffInfoByTypes(List<Integer> types);
}
