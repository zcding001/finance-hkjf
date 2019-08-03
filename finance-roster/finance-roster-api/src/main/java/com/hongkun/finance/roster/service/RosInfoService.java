package com.hongkun.finance.roster.service;

import com.hongkun.finance.roster.constants.RosterFlag;
import com.hongkun.finance.roster.constants.RosterType;
import com.hongkun.finance.roster.model.RosInfo;
import com.yirun.framework.core.utils.pager.Pager;

import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.roster.service.RosInfoService.java
 * @Class Name    : RosInfoService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface RosInfoService {
	
	/**
	 * @Described			: 单条插入
	 * @param rosInfo 持久化的数据对象
	 * @return				: void
	 */
	void insertRosInfo(RosInfo rosInfo);
	
	/**
	 * @Described			: 批量插入
	 * @param List<RosInfo> 批量插入的数据
	 * @return				: void
	 */
	void insertRosInfoBatch(List<RosInfo> list);
	
	/**
	 * @Described			: 批量插入
	 * @param List<RosInfo> 批量插入的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void insertRosInfoBatch(List<RosInfo> list, int count);
	
	/**
	 * @Described			: 更新数据
	 * @param rosInfo 要更新的数据
	 * @return				: void
	 */
	void updateRosInfo(RosInfo rosInfo);
	
	/**
	 * @Described			: 批量更新数据
	 * @param rosInfo 要更新的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void updateRosInfoBatch(List<RosInfo> list, int count);
	
	/**
	 * @Described			: 通过id查询数据
	 * @param id id值
	 * @return	RosInfo
	 */
	RosInfo findRosInfoById(int id);
	
	/**
	 * @Described			: 条件检索数据
	 * @param rosInfo 检索条件
	 * @return	List<RosInfo>
	 */
	List<RosInfo> findRosInfoList(RosInfo rosInfo);
	
	/**
	 * @Described			: 条件检索数据
	 * @param rosInfo 检索条件
	 * @param start	起始页
	 * @param limit 检索条数
	 * @return	List<RosInfo>
	 */
	List<RosInfo> findRosInfoList(RosInfo rosInfo, int start, int limit);
	
	/**
	 * @Described			: 条件检索数据
	 * @param rosInfo 检索条件
	 * @param pager	分页数据
	 * @return	List<RosInfo>
	 */
	Pager findRosInfoList(RosInfo rosInfo, Pager pager);

	/**
	 * @Described			: 统计条数
	 * @param rosInfo 检索条件
	 * @param pager	分页数据
	 * @return	int
	 */
	int findRosInfoCount(RosInfo rosInfo);
	
	/**
	 * 自定义sql查询count
	 * @param obj
	 * @param pager
	 * @param sqlName
	 * @return
	 */
	Pager findRosInfoList(RosInfo rosInfo, Pager pager, String sqlName/*添加一个自定义的sql的名字*/);
	
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
	Integer findRosInfoCount(RosInfo rosInfo, String sqlName);
	
	/**
	 *  @Description    : 删除用户功能权限名单
	 *  @Method_Name    : delRosInfo
	 *  @param id
	 *  @return
	 *  @return         : Integer
	 *  @Creation Date  : 2017年9月28日 下午2:40:42 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	Integer delRosInfo(Integer id);
	
	/**
	 *  @Description    : 验证用户功能黑白名单
	 *  @Method_Name    : validateRoster
	 *  @param regUserId 用户id
	 *  @param rosterType 功能类型
	 *  @param rosterFlag 黑白名单标识 0：黑名单 1：白名单
	 *  @return         : boolean true表示存在查询结果，false表示查询结果为空
	 *  @Creation Date  : 2017年7月18日 上午9:20:17 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	boolean validateRoster(Integer regUserId, RosterType rosterType, RosterFlag rosterFlag);

    /**
     *  @Description    : 验证用户功能黑白名单
     *  @Method_Name    : validateRoster
     *  @param login    : 手机号
     *  @param rosterType ： 功能类型
     *  @param rosterFlag ： 黑白名单标识 0：黑名单 1：白名单
     *  @return         : boolean true表示存在查询结果，false表示查询结果为空
     *  @Creation Date  : 2017年7月18日 上午9:20:17 
     *  @Author         : zhichaoding@hongkun.com zc.ding
     */
    boolean validateRoster(Long login, RosterType rosterType, RosterFlag rosterFlag);
    
	/**
	 *  @Description    : 验证短信通知控制权限
	 *  @Method_Name    : validateSmsRoster
	 *  @param regUserId
	 *  @param rosterType
	 *  @param rosterFlag
	 *  @return         : boolean
	 *  @Creation Date  : 2018年1月26日 下午4:13:53 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	boolean validateSmsRoster(Integer regUserId, RosterType rosterType, RosterFlag rosterFlag);

}
