package com.hongkun.finance.roster.service;

import java.util.List;

import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;
import com.hongkun.finance.roster.model.RosNotice;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.roster.service.RosNoticeService.java
 * @Class Name    : RosNoticeService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface RosNoticeService {
	
	/**
	 * @Described			: 单条插入
	 * @param rosNotice 持久化的数据对象
	 * @return				: void
	 */
	void insertRosNotice(RosNotice rosNotice);
	
	/**
	 * @Described			: 批量插入
	 * @param List<RosNotice> 批量插入的数据
	 * @return				: void
	 */
	void insertRosNoticeBatch(List<RosNotice> list);
	
	/**
	 * @Described			: 批量插入
	 * @param List<RosNotice> 批量插入的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void insertRosNoticeBatch(List<RosNotice> list, int count);
	
	/**
	 * @Described			: 更新数据
	 * @param rosNotice 要更新的数据
	 * @return				: void
	 */
	void updateRosNotice(RosNotice rosNotice);
	
	/**
	 * @Described			: 批量更新数据
	 * @param rosNotice 要更新的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void updateRosNoticeBatch(List<RosNotice> list, int count);
	
	/**
	 * @Described			: 通过id查询数据
	 * @param id id值
	 * @return	RosNotice
	 */
	RosNotice findRosNoticeById(int id);
	
	/**
	 * @Described			: 条件检索数据
	 * @param rosNotice 检索条件
	 * @return	List<RosNotice>
	 */
	List<RosNotice> findRosNoticeList(RosNotice rosNotice);
	
	/**
	 * @Described			: 条件检索数据
	 * @param rosNotice 检索条件
	 * @param start	起始页
	 * @param limit 检索条数
	 * @return	List<RosNotice>
	 */
	List<RosNotice> findRosNoticeList(RosNotice rosNotice, int start, int limit);
	
	/**
	 * @Described			: 条件检索数据
	 * @param rosNotice 检索条件
	 * @param pager	分页数据
	 * @return	List<RosNotice>
	 */
	Pager findRosNoticeList(RosNotice rosNotice, Pager pager);

	/**
	 * @Described			: 统计条数
	 * @param rosNotice 检索条件
	 * @param pager	分页数据
	 * @return	int
	 */
	int findRosNoticeCount(RosNotice rosNotice);
	
	/**
	 * 自定义sql查询count
	 * @param obj
	 * @param pager
	 * @param sqlName
	 * @return
	 */
	Pager findRosNoticeList(RosNotice rosNotice, Pager pager, String sqlName/*添加一个自定义的sql的名字*/);
	
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
	Integer findRosNoticeCount(RosNotice rosNotice, String sqlName);
	/**
	*  @Description    ：查询某个功能需要通知得邮箱集合（邮箱之间用逗号隔开）
	*  @Method_Name    ：getEmailsByType
	*  @param type
	*  @return java.lang.String
	*  @Creation Date  ：2018/5/30
	*  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
	*/
	String  getEmailsByType(Integer type);

	ResponseEntity<?> addRosNotice(RosNotice rosNotice);

	ResponseEntity<?> editRosNotice(RosNotice rosNotice);

	ResponseEntity<?> delRosNotice(Integer id);
}
