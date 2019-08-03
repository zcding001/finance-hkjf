package com.hongkun.finance.vas.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.mengyun.tcctransaction.api.Compensable;

import com.hongkun.finance.vas.model.SimGoldVo;
import com.hongkun.finance.vas.model.VasSimRecord;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.vas.service.VasSimRecordService.java
 * @Class Name : VasSimRecordService.java
 * @Description : GENERATOR SERVICE类
 * @Author : generator
 */
public interface VasSimRecordService {

	/**
	 * @Described : 单条插入
	 * @param vasSimRecord
	 *            持久化的数据对象
	 * @return : void
	 */
	int insertVasSimRecord(VasSimRecord vasSimRecord);

	/**
	 * @Described : 批量插入
	 * @param List<VasSimRecord>
	 *            批量插入的数据
	 * @return : void
	 */
	void insertVasSimRecordBatch(List<VasSimRecord> list);

	/**
	 * @Described : 批量插入
	 * @param List<VasSimRecord>
	 *            批量插入的数据
	 * @param count
	 *            多少条数提交一次
	 * @return : void
	 */
	void insertVasSimRecordBatch(List<VasSimRecord> list, int count);

	/**
	 * @Described : 更新数据
	 * @param vasSimRecord
	 *            要更新的数据
	 * @return : void
	 */
	int updateVasSimRecord(VasSimRecord vasSimRecord);

	/**
	 * @Described : 批量更新数据
	 * @param vasSimRecord
	 *            要更新的数据
	 * @param count
	 *            多少条数提交一次
	 * @return : void
	 */
	void updateVasSimRecordBatch(List<VasSimRecord> list, int count);

	/**
	 * @Described : 通过id查询数据
	 * @param id
	 *            id值
	 * @return VasSimRecord
	 */
	VasSimRecord findVasSimRecordById(int id);

	/**
	 * @Described : 条件检索数据
	 * @param vasSimRecord
	 *            检索条件
	 * @return List<VasSimRecord>
	 */
	List<VasSimRecord> findVasSimRecordList(VasSimRecord vasSimRecord);

	/**
	 * @Described : 条件检索数据
	 * @param vasSimRecord
	 *            检索条件
	 * @param start
	 *            起始页
	 * @param limit
	 *            检索条数
	 * @return List<VasSimRecord>
	 */
	List<VasSimRecord> findVasSimRecordList(VasSimRecord vasSimRecord, int start, int limit);

	/**
	 * @Described : 条件检索数据
	 * @param vasSimRecord
	 *            检索条件
	 * @param pager
	 *            分页数据
	 * @return List<VasSimRecord>
	 */
	Pager findVasSimRecordList(VasSimRecord vasSimRecord, Pager pager);

	/**
	 * @Described : 统计条数
	 * @param vasSimRecord
	 *            检索条件
	 * @param pager
	 *            分页数据
	 * @return int
	 */
	int findVasSimRecordCount(VasSimRecord vasSimRecord);

	/**
	 * @Description :根据条件分页查询记录
	 * @Method_Name : findVasSimRecordListByInfo;
	 * @param vasSimRecordMap
	 * @param pager
	 * @return
	 * @return : Pager;
	 * @Creation Date : 2017年6月28日 下午2:46:19;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	Pager findVasSimRecordListByInfo(Map<String, Object> vasSimRecordMap, Pager pager);

	/**
	 * @Description : 通过用户ID，查询体验金额，及LIST
	 * @Method_Name : findVasSimRecordByRegUserId;
	 * @param regUserId
	 * @return
	 * @return : ResponseEntity<?>; result1:"vasSimRecordList"
	 *         result2:"totalMoney"
	 * @Creation Date : 2017年7月12日 上午10:48:20;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	ResponseEntity<?> findVasSimRecordByRegUserId(Integer regUserId);

	/**
	 * @Description : 统计体验金发放信息
	 * @Method_Name : findSimGoldCountInfo;
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年10月16日 上午11:22:37;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	ResponseEntity<?> findSimGoldCountInfo();

	/**
	 * @Description : 通过 ID,更新体验金记录状态
	 * @Method_Name : updateBatchVasSimRecordById;
	 * @param state
	 * @param vasSimRecordList
	 * @return : int;
	 * @Creation Date : 2017年7月12日 上午10:52:18;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@Compensable
	int updateBatchVasSimRecordById(Integer state, List<VasSimRecord> vasSimRecordList);

	/**
	 * @Description : 查询用户体验金总和
	 * @Method_Name : findSimSumMoney;
	 * @param vasSimRecord
	 * @return
	 * @return : BigDecimal;
	 * @Creation Date : 2017年7月12日 上午11:07:30;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	BigDecimal findSimSumMoney(VasSimRecord vasSimRecord);

	/**
	 * @Description : 发放体验金
	 * @Method_Name : insertSimGrant;
	 * @param SimGoldVo
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年9月27日 下午6:30:29;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	ResponseEntity<?> insertSimGrant(SimGoldVo simGoldVo);

	/**
	 * @Description : 根据体验金ID，将体验金设置为失效
	 * @Method_Name : updateSimGold;
	 * @param simGoldVo
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年9月29日 上午10:25:50;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	ResponseEntity<?> updateSimGold(SimGoldVo simGoldVo);
	/**
	 *  @Description    : 将过期的体验金设置为过期
	 *  @Method_Name    : simGoldOverDue;
	 *  @param currentDate
	 *  @param shardingItem
	 *  @return         : void;
	 *  @Creation Date  : 2018年12月6日 下午2:58:37;
	 *  @Author         : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	void simGoldOverDue(Date currentDate, int shardingItem);

}
