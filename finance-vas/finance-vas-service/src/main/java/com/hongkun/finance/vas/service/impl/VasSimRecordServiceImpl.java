package com.hongkun.finance.vas.service.impl;

import static com.yirun.framework.core.commons.Constants.ERROR;
import static com.yirun.framework.core.commons.Constants.SUCCESS;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mengyun.tcctransaction.api.Compensable;
import org.mengyun.tcctransaction.dubbo.context.DubboTransactionContextEditor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.hongkun.finance.user.utils.BaseUtil;
import com.hongkun.finance.vas.constants.VasConstants;
import com.hongkun.finance.vas.dao.VasGoldRuleDao;
import com.hongkun.finance.vas.dao.VasSimRecordDao;
import com.hongkun.finance.vas.model.SimGoldVo;
import com.hongkun.finance.vas.model.VasGoldRule;
import com.hongkun.finance.vas.model.VasSimRecord;
import com.hongkun.finance.vas.service.VasSimRecordService;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.DateUtils;
import com.yirun.framework.core.utils.json.JsonUtils;
import com.yirun.framework.core.utils.pager.Pager;

/**
 * @Project : finance
 * @Program Name :
 *          com.hongkun.finance.vas.service.impl.VasSimRecordServiceImpl.java
 * @Class Name : VasSimRecordServiceImpl.java
 * @Description : GENERATOR SERVICE实现类
 * @Author : generator
 */
@Service
public class VasSimRecordServiceImpl implements VasSimRecordService {

	private static final Logger logger = LoggerFactory.getLogger(VasSimRecordServiceImpl.class);

	/**
	 * VasSimRecordDAO
	 */
	@Autowired
	private VasSimRecordDao vasSimRecordDao;
	@Autowired
	private VasGoldRuleDao vasGoldRuleDao;

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public int insertVasSimRecord(VasSimRecord vasSimRecord) {
		return this.vasSimRecordDao.save(vasSimRecord);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertVasSimRecordBatch(List<VasSimRecord> list) {
		this.vasSimRecordDao.insertBatch(VasSimRecord.class, list);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertVasSimRecordBatch(List<VasSimRecord> list, int count) {
		if (logger.isDebugEnabled()) {
			logger.debug("default batch insert size is " + count);
		}
		this.vasSimRecordDao.insertBatch(VasSimRecord.class, list, count);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public int updateVasSimRecord(VasSimRecord vasSimRecord) {
		return this.vasSimRecordDao.update(vasSimRecord);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateVasSimRecordBatch(List<VasSimRecord> list, int count) {
		this.vasSimRecordDao.updateBatch(VasSimRecord.class, list, count);
	}

	@Override
	public VasSimRecord findVasSimRecordById(int id) {
		return this.vasSimRecordDao.findByPK(Long.valueOf(id), VasSimRecord.class);
	}

	@Override
	public List<VasSimRecord> findVasSimRecordList(VasSimRecord vasSimRecord) {
		return this.vasSimRecordDao.findByCondition(vasSimRecord);
	}

	@Override
	public List<VasSimRecord> findVasSimRecordList(VasSimRecord vasSimRecord, int start, int limit) {
		return this.vasSimRecordDao.findByCondition(vasSimRecord, start, limit);
	}

	@Override
	public Pager findVasSimRecordList(VasSimRecord vasSimRecord, Pager pager) {
		return this.vasSimRecordDao.findByCondition(vasSimRecord, pager);
	}

	@Override
	public int findVasSimRecordCount(VasSimRecord vasSimRecord) {
		return this.vasSimRecordDao.getTotalCount(vasSimRecord);
	}

	@Override
	public Pager findVasSimRecordListByInfo(Map<String, Object> vasSimRecordMap, Pager pager) {
		return this.vasSimRecordDao.findVasSimRecordListByInfo(vasSimRecordMap, pager);
	}

	@Override
	public ResponseEntity<?> findVasSimRecordByRegUserId(Integer regUserId) {
		logger.info("方法: findVasSimRecordByRegUserId, 通过用户ID查询体验金记录信息, 入参: regUserId: {}", regUserId);
		try {
			VasSimRecord vasSimRecord = new VasSimRecord();
			vasSimRecord.setExpireTimeBegin(new Date());
			vasSimRecord.setState(VasConstants.VAS_SIM_STATE_INIT);
			vasSimRecord.setRegUserId(regUserId);
			List<VasSimRecord> vasSimRecordList = this.vasSimRecordDao.findByCondition(vasSimRecord);
			BigDecimal totalMoney = this.vasSimRecordDao.findSimSumMoney(vasSimRecord);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("vasSimRecordList", vasSimRecordList);
			resultMap.put("totalMoney", totalMoney);
			ResponseEntity<?> responseEntity = new ResponseEntity<>(Constants.SUCCESS);
			responseEntity.setParams(resultMap);
			return responseEntity;
		} catch (Exception e) {
			logger.error("用户标识: {}, 查询体验金异常: ", regUserId, e);
			return new ResponseEntity<>(Constants.ERROR, "查询体验金数据异常!");
		}
	}

	@Override
	@Compensable(cancelMethod = "rollBackUpdateVasSimRecordById", transactionContextEditor = DubboTransactionContextEditor.class)
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public int updateBatchVasSimRecordById(Integer state, List<VasSimRecord> vasSimRecordList) {
		logger.info("{}, 通过ID更新体验金记录状态入参state：{}, vasSimRecordList: {}", BaseUtil.getTccTryLogPrefix(), state,
				vasSimRecordList);
		try {
			List<VasSimRecord> vasSimRecordLists = new ArrayList<VasSimRecord>();
			// 遍历循环体验金记录，组装待更新记录状态的LIST集合
			for (VasSimRecord vasRecord : vasSimRecordList) {
				VasSimRecord vasSimRecord = new VasSimRecord();
				vasSimRecord.setId(vasRecord.getId());
				vasSimRecord.setState(state);
				vasSimRecordLists.add(vasSimRecord);
			}
			// 如果待更的体验金记录集合不为空，则批量更新
			return this.vasSimRecordDao.updateBatch(VasSimRecord.class, vasSimRecordLists, vasSimRecordLists.size());
		} catch (Exception e) {
			logger.error("{}, 通过ID更新体验金记录状态入参state：{}, vasSimRecordList: {}\n", BaseUtil.getTccTryLogPrefix(), state,
					vasSimRecordList, e);
			throw new GeneralException("更新体验金失败");
		}
	}

	/***
	 * @Description :对根据ID，更新体验金记录状态的回滚方法
	 * @Method_Name : rollBackUpdateVasSimRecordById;
	 * @param state
	 *            : 状态
	 * @param vasSimRecordList
	 *            :体验金记录集合
	 * @return : int;
	 * @Creation Date : 2018年3月30日 下午3:45:33;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public int rollBackUpdateVasSimRecordById(Integer state, List<VasSimRecord> vasSimRecordList) {
		logger.info("tcc cancel updateBatchVasSimRecordById. 通过ID更新体验金记录状态入参state: {}, vasSimRecordList: {}", state,
				vasSimRecordList);
		try {
			// 遍历循环体验金记录
			List<VasSimRecord> vasSimRecordLists = new ArrayList<VasSimRecord>();
			for (VasSimRecord vasSimRecord : vasSimRecordList) {
				// 根据ID，查询体验金记录，如果查询的体验金记录状态等于待更新的状态，则为需要回滚的记录
				VasSimRecord simRecord = this.vasSimRecordDao.findByPK(vasSimRecord.getId().longValue(),
						VasSimRecord.class);
				if (simRecord != null && simRecord.getState() == state) {
					VasSimRecord newSimRecord = new VasSimRecord();
					newSimRecord.setId(vasSimRecord.getId());
					newSimRecord.setState(vasSimRecord.getState());
					vasSimRecordLists.add(newSimRecord);
				}
			}
			// 批量更新需要回滚的体验金记录
			return this.vasSimRecordDao.updateBatch(VasSimRecord.class, vasSimRecordLists, vasSimRecordLists.size());
		} catch (Exception e) {
			logger.error(
					"tcc cancel updateBatchVasSimRecordById error. 通过ID，更新体验金记录状态入参state: {}, vasSimRecordList: {}\n{}",
					state, vasSimRecordList, e);
			throw new GeneralException("体验金回滚失败");
		}
	}

	@Override
	public BigDecimal findSimSumMoney(VasSimRecord vasSimRecord) {
		return this.vasSimRecordDao.findSimSumMoney(vasSimRecord);
	}

	@Override
	public ResponseEntity<?> findSimGoldCountInfo() {
		return new ResponseEntity<>(Constants.SUCCESS, vasSimRecordDao.findSimGoldCountInfo());
	}

	public ResponseEntity<?> insertSimGrant(SimGoldVo simGoldVo) {
		logger.info("方法: insertSimGrant, 保存体验金记录, 入参: simGoldVo: {}", simGoldVo.toString());
		try {
			List<Integer> userIds = simGoldVo.getUserIds();
			if (userIds == null || userIds.size() <= 0) {
				return new ResponseEntity<>(ERROR, "请选择要发放的用户！");
			}
			// 判断发放体验金金额必须是100的整数倍
			if (simGoldVo.getMoney() == null
					|| simGoldVo.getMoney().divideAndRemainder(new BigDecimal(100))[1] != BigDecimal.ZERO) {
				return new ResponseEntity<>(ERROR, "请输入发放的体验金,且是100的整数倍！");
			}
			// 根据类型和状态查询体验金当前有效规则
			VasGoldRule vasGoldRule = this.vasGoldRuleDao.findVasGoldRuleByTypeAndState(simGoldVo.getRuleType(),
					VasConstants.VAS_RULE_STATE_START);
			if (vasGoldRule == null) {
				return new ResponseEntity<>(ERROR, "体验金规则获取异常！");
			}
			// 遍历循环，批量生成体验金记录
			List<VasSimRecord> vasSimRecordList = new ArrayList<VasSimRecord>();
			for (Integer userId : userIds) {
				VasSimRecord vasSimRecord = new VasSimRecord();
				vasSimRecord.setRegUserId(userId);
				vasSimRecord.setMoney(simGoldVo.getMoney());
				vasSimRecord.setSource(vasGoldRule.getId());
				vasSimRecord.setExpireTime(DateUtils.addDays(new Date(), vasGoldRule.getPeriod()));
				vasSimRecordList.add(vasSimRecord);
			}
			logger.info("保存体验金记录集合: vasSimRecordList: {}", JsonUtils.toJson(vasSimRecordList));
			vasSimRecordDao.insertBatch(VasSimRecord.class, vasSimRecordList, vasSimRecordList.size());
			return new ResponseEntity<>(SUCCESS);
		} catch (Exception e) {
			logger.error("保存体验金记录, 发放体验金事件类型: {}, 发放体验金异常: ", simGoldVo.getRuleType(), e);
			throw new GeneralException("发放体验金异常!");
		}
	}

	public ResponseEntity<?> updateSimGold(SimGoldVo simGoldVo) {
		logger.info("方法: updateSimGold, 更新体验金记录状态, 入参: simGoldVo: {}", simGoldVo.toString());
		try {
			VasSimRecord vasSimRecord = vasSimRecordDao.findByPK(Long.valueOf(simGoldVo.getId()), VasSimRecord.class);
			if (vasSimRecord.getState() == VasConstants.VAS_SIM_STATE_USES) {
				return new ResponseEntity<>(ERROR, "体验金已经使用不允许作废！");
			}
			VasSimRecord vasSimRecordVo = new VasSimRecord();
			vasSimRecordVo.setId(simGoldVo.getId());
			vasSimRecordVo.setState(VasConstants.VAS_SIM_STATE_INVALID);
			this.vasSimRecordDao.update(vasSimRecordVo);
			return new ResponseEntity<>(SUCCESS, "更新成功！");
		} catch (Exception e) {
			logger.error("更新体验金记录状态, 体验金记录ID: {}, 更新失败: ", simGoldVo.getId(), e);
			throw new GeneralException("更新失败");
		}

	}

    @Override
    public void simGoldOverDue(Date currentDate, int shardingItem) {
        String currentJobDate = DateUtils.format(currentDate, DateUtils.DATE_HH_MM_SS);
        try{
            //1.获取已过期的体验金记录集合；当前时间超过使用截止时间且状态不为已使用状态和失效状态
            List<VasSimRecord> list = this.vasSimRecordDao.getExpiredSimgoldList(currentDate);
            //2.组装更新记录的条件
            list.forEach((vasSimRecord)->{
                vasSimRecord.setState(VasConstants.VAS_SIM_STATE_EXPIRE);
                vasSimRecord.setModifyTime(DateUtils.getCurrentDate());
            });
            logger.info("simGoldOverDue, 设置已过期的体验金记录为已过期状态, 处理时间: {}, 处理分片项: {}, " +
                    "要处理的卡券集合: {}", currentJobDate, shardingItem, JSON.toJSON(list));
            //3.批量更新记录为已过期状态
            if(list.size() > 0){
                this.vasSimRecordDao.updateBatch(VasSimRecord.class, list, list.size());
            }
        }catch (Exception e){
            logger.error("simGoldOverDue, 设置已过期的体验金记录为已过期状态异常, 处理时间: {}, 处理分片项: {}, 异常信息: ",
                    currentJobDate, shardingItem, e);
        }        
    }
}
