package com.hongkun.finance.info.service;

import java.util.List;

import com.yirun.framework.core.utils.pager.Pager;
import com.hongkun.finance.info.model.InfoBrowsingRecord;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.info.service.InfoBrowsingRecordService.java
 * @Class Name : InfoBrowsingRecordService.java
 * @Description : GENERATOR SERVICE类
 * @Author : generator
 */
public interface InfoBrowsingRecordService {

	/**
	 * @Described : 单条插入
	 * @param infoBrowsingRecord
	 *            持久化的数据对象
	 * @return : void
	 */
	int insertInfoBrowsingRecord(InfoBrowsingRecord infoBrowsingRecord);

	/**
	 * @Described : 批量插入
	 * @param List<InfoBrowsingRecord>
	 *            批量插入的数据
	 * @return : void
	 */
	void insertInfoBrowsingRecordBatch(List<InfoBrowsingRecord> list);

	/**
	 * @Described : 批量插入
	 * @param List<InfoBrowsingRecord>
	 *            批量插入的数据
	 * @param count
	 *            多少条数提交一次
	 * @return : void
	 */
	void insertInfoBrowsingRecordBatch(List<InfoBrowsingRecord> list, int count);

	/**
	 * @Described : 更新数据
	 * @param infoBrowsingRecord
	 *            要更新的数据
	 * @return : void
	 */
	int updateInfoBrowsingRecord(InfoBrowsingRecord infoBrowsingRecord);

	/**
	 * @Described : 批量更新数据
	 * @param infoBrowsingRecord
	 *            要更新的数据
	 * @param count
	 *            多少条数提交一次
	 * @return : void
	 */
	void updateInfoBrowsingRecordBatch(List<InfoBrowsingRecord> list, int count);

	/**
	 * @Described : 通过id查询数据
	 * @param id
	 *            id值
	 * @return InfoBrowsingRecord
	 */
	InfoBrowsingRecord findInfoBrowsingRecordById(int id);

	/**
	 * @Described : 条件检索数据
	 * @param infoBrowsingRecord
	 *            检索条件
	 * @return List<InfoBrowsingRecord>
	 */
	List<InfoBrowsingRecord> findInfoBrowsingRecordList(InfoBrowsingRecord infoBrowsingRecord);

	/**
	 * @Described : 条件检索数据
	 * @param infoBrowsingRecord
	 *            检索条件
	 * @param start
	 *            起始页
	 * @param limit
	 *            检索条数
	 * @return List<InfoBrowsingRecord>
	 */
	List<InfoBrowsingRecord> findInfoBrowsingRecordList(InfoBrowsingRecord infoBrowsingRecord, int start, int limit);

	/**
	 * @Described : 条件检索数据
	 * @param infoBrowsingRecord
	 *            检索条件
	 * @param pager
	 *            分页数据
	 * @return Pager
	 */
	Pager findInfoBrowsingRecordList(InfoBrowsingRecord infoBrowsingRecord, Pager pager);

	/**
	 * @Described : 统计条数
	 * @param infoBrowsingRecord
	 *            检索条件
	 * @param pager
	 *            分页数据
	 * @return int
	 */
	int findInfoBrowsingRecordCount(InfoBrowsingRecord infoBrowsingRecord);

	/**
	 * @Description : 根据类型查询资讯点赞或浏览的个数
	 * @Method_Name : findInfoBrowsingRecordCount;
	 * @param type
	 *            类型 浏览-1 点赞-2
	 * @param infomationsNewsId
	 *            资讯ID
	 * @return
	 * @return : int;
	 * @Creation Date : 2017年8月21日 下午5:32:42;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	int findInfoBrowsingRecordCount(int type, int infomationsNewsId);

	/**
	 * @Description :
	 * @Method_Name : findInfoBrowsingRecordCount;
	 * @param type
	 *            类型 浏览-1 点赞-2
	 * @param infomationsNewsId
	 *            资讯ID
	 * @param regUserId
	 *            用户Id
	 * @return
	 * @return : int;
	 * @Creation Date : 2017年8月21日 下午6:16:53;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	int findInfoBrowsingRecordCount(int type, int infomationsNewsId, int regUserId);
}
