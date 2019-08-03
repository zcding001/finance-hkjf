package com.hongkun.finance.vas.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.vas.constants.VasConstants;
import com.hongkun.finance.vas.constants.VasVipConstants;
import com.hongkun.finance.vas.dao.VasVipGradeStandardDao;
import com.hongkun.finance.vas.dao.VasVipGrowRecordDao;
import com.hongkun.finance.vas.dao.VasVipGrowRuleDao;
import com.hongkun.finance.vas.enums.VasVipGrowRecordTypeEnum;
import com.hongkun.finance.vas.model.VasVipGrowRecord;
import com.hongkun.finance.vas.model.VasVipGrowRule;
import com.hongkun.finance.vas.model.vo.VasVipGrowRecordMqVO;
import com.hongkun.finance.vas.service.VasVipGrowRecordService;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.redis.JedisClusterLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.*;
import java.util.stream.Collectors;

import static com.yirun.framework.core.commons.Constants.*;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.vas.service.impl.VasVipGrowRecordServiceImpl.java
 * @Class Name    : VasVipGrowRecordServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class VasVipGrowRecordServiceImpl implements VasVipGrowRecordService {

	private static final Logger logger = LoggerFactory.getLogger(VasVipGrowRecordServiceImpl.class);

	private static ScriptEngine SE = new ScriptEngineManager().getEngineByName("JavaScript");
	/**
	 * VasVipGrowRecordDAO
	 */
	@Autowired
	private VasVipGrowRecordDao vasVipGrowRecordDao;
	@Autowired
	private VasVipGrowRuleDao vasVipGrowRuleDao;
	@Autowired
	private VasVipGradeStandardDao vasVipGradeStandardDao;
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public int insertVasVipGrowRecord(VasVipGrowRecord vasVipGrowRecord) {
		try{
			return this.vasVipGrowRecordDao.save(vasVipGrowRecord);
		}catch (Exception e){
			logger.error("insertVasVipGrowRecord, 插入会员成长值异常, 会员成长值信息: {}, 异常信息: ",
					vasVipGrowRecord.toString(), e);
			throw new GeneralException("添加会员等级标准异常！");
		}
	}

	@Override
	public List<VasVipGrowRecord> findVasVipGrowRecordList(VasVipGrowRecord vasVipGrowRecord) {
		return this.vasVipGrowRecordDao.findByCondition(vasVipGrowRecord);
	}

	@Override
	public Pager findVasVipGrowRecordList(VasVipGrowRecord vasVipGrowRecord, Pager pager) {
		return this.vasVipGrowRecordDao.findByCondition(vasVipGrowRecord, pager);
	}

	/**
	 *  @Description    : 通过传递的类型和成长值插入成长值记录
	 *  @Method_Name    : insertUserGrowRecord
	 *  @param userId
	 *  @param growType     	获取成长值渠道类型：1-投资，2-邀请好友注册，3-签到，4-首次充值，5-积分兑换，6-邀请好友投资，7-积分支付，8-积分转赠，9-平台赠送，10-会员降级
	 *                          类型9、10等无法通过规则获取成长值的类型可通过该方法记录成长值
	 *  @param growValue		成长值
	 *  @param note		    成长值记录备注
	 *  @return
	 *  @return         : int
	 *  @Creation Date  : 2017年6月29日 下午13:47:55
	 *  @Author         : pengwu@hongkun.com.cn
	 */
	private int insertUserGrowRecord(int userId,int growType,int growValue,String note){
			//组装用户成长值记录
			VasVipGrowRecord vasVipGrowRecord = new VasVipGrowRecord();
			vasVipGrowRecord.setType(growType);
			vasVipGrowRecord.setGrowValue(growValue);
			vasVipGrowRecord.setRegUserId(userId);
			vasVipGrowRecord.setNote(note);
			return insertUserGrowRecord(vasVipGrowRecord);
	}

	/**
	 *  @Description    : 插入用户成长值记录
	 *  @Method_Name    : insertUserGrowRecord
	 *  @param vasVipGrowRecord
	 *  @return
	 *  @return         : int
	 *  @Creation Date  : 2017年6月29日 下午14:01:05
	 *  @Author         : pengwu@hongkun.com.cn
	 */
	private int insertUserGrowRecord(VasVipGrowRecord vasVipGrowRecord){
		//添加redis分布式锁，防止并发导致数据不正确
		JedisClusterLock lock = new JedisClusterLock();
		String lockKey = LOCK_PREFFIX + VasVipGrowRecordServiceImpl.class.getSimpleName() + "_insertUserGrowRecord_" +
				vasVipGrowRecord.getRegUserId();
		try{
			if (!lock.lock(lockKey,LOCK_EXPIRES,Constants.LOCK_WAITTIME)){
				logger.info("insertUserGrowRecord, 插入用户成长值记录获取锁超时, 用户标识: {}, 成长值记录信息: {}",
						vasVipGrowRecord.getRegUserId(), vasVipGrowRecord.toString());
			}
			//获取用户当前成长值
			int userCurrentGrow = this.findUserCurrentGrowValue(vasVipGrowRecord.getRegUserId());
			//计算成长值
			int userAfterCalGrow = userCurrentGrow + vasVipGrowRecord.getGrowValue();
			//组装用户成长值记录，插入数据库中
			vasVipGrowRecord.setPreGrowValue(userCurrentGrow);
			vasVipGrowRecord.setCurrentGrowValue(userAfterCalGrow);
			vasVipGrowRecord.setState(VasConstants.VAS_STATE_Y);
			return this.vasVipGrowRecordDao.save(vasVipGrowRecord);
		} catch (Exception e){
			logger.error("insertUserGrowRecord, 插入用户成长值记录异常, 用户标识: {}, 成长值记录信息: {}, 异常信息: {}",
					vasVipGrowRecord.getRegUserId(), vasVipGrowRecord.toString(), e);
			throw new GeneralException("插入用户成长值记录异常！");
		} finally {
			lock.freeLock(lockKey);
		}
	}

	@Override
	public void insertVasVipGrowRecord(Date registTime,VasVipGrowRecordMqVO vasVipGrowRecordMqVO) {
		logger.info("insertVasVipGrowRecord, 插入用户成长值记录, 用户标识: {}, 用户注册时间: {}, 成长值记录信息: {}",
				vasVipGrowRecordMqVO.getUserId(), registTime, vasVipGrowRecordMqVO.toString());
		try{
			int growType = vasVipGrowRecordMqVO.getGrowType();
			//9-平台赠送，10-会员降级 没有成长值规则直接插入成长值记录
			if (growType == VasVipConstants.VAS_VIP_GROW_TYPE_POINT_GIVE || growType == VasVipConstants.VAS_VIP_GROW_TYPE_POINT_DEGRADE){
				this.insertUserGrowRecord(vasVipGrowRecordMqVO.getUserId(),growType,vasVipGrowRecordMqVO.getGrowValue(),
						VasVipGrowRecordTypeEnum.getNote(growType));
			}else {
				//1-投资，2-邀请好友注册，3-签到，4-首次充值，5-积分兑换，6-邀请好友投资，7-积分支付，8-积分转赠 从成长值规则中获取成长值
				VasVipGrowRule rule = vasVipGrowRuleDao.getVipGrowRuleByTypeAndRegistTime(growType,registTime);
				//有成长值规则时，插入成长值记录
				if (rule != null){
					//成长值
					int growthValue;
					//成长值记录备注
					String note = "";
					//如果为公式，则通过公式计算出成长值
					if (rule.getFormulaEnable() == 1){
						String formula = rule.getGrowValue().replaceAll("money", vasVipGrowRecordMqVO.getInvestMoney() + "")
								.replaceAll("day", vasVipGrowRecordMqVO.getInvestDay() + "");
						Double tmp = 0d;
						try {
							tmp = (Double)SE.eval(formula);
						} catch (ScriptException e) {
							e.printStackTrace();
						}
						//对成长值取整，等于0的成长值不进行处理
						if (tmp.intValue() == 0){
						    return;
                        }
						growthValue = tmp.intValue();
						if (growType == 1){
							note = "投资金额" + vasVipGrowRecordMqVO.getInvestMoney() + "元";
						}else if (growType == VasVipConstants.VAS_VIP_GROW_TYPE_INVITE_USER_INVEST){
							note = "邀请人投资金额" + vasVipGrowRecordMqVO.getInvestMoney() + "元";
						}
					}else {
						note = VasVipGrowRecordTypeEnum.getNote(growType);
						growthValue = Integer.valueOf(rule.getGrowValue());
					}
					this.insertUserGrowRecord(vasVipGrowRecordMqVO.getUserId(),growType,growthValue,note);
				}else{
					logger.info("用户标识：{}，没有获取到对应的成长值规则，成长值规则类型：{}",vasVipGrowRecordMqVO.getUserId(),
							vasVipGrowRecordMqVO.getGrowType());
				}
			}
		}catch (Exception e){
			logger.error("insertVasVipGrowRecord, 插入用户成长值记录异常, 用户标识: {}, 用户注册时间: {}, 成长值记录信息: {}, 异常信息: ",
					vasVipGrowRecordMqVO.getUserId(), registTime, vasVipGrowRecordMqVO.toString(), e);
			throw new GeneralException("插入用户成长值记录异常！");
		}
	}

	@Override
	public int findUserCurrentGrowValue(int userId) {
		return vasVipGrowRecordDao.findUserCurrentGrowValue(userId);
	}

	@Override
	public Pager findVasVipGrowRecordList(List<Integer> userIds, Integer growthValMin, Integer growthValMax, Pager pager) {
		Map paraMap = new HashMap();
		paraMap.put("userIds",userIds);
		paraMap.put("growthValMin",growthValMin);
		paraMap.put("growthValMax",growthValMax);
		return this.vasVipGrowRecordDao.findByCondition(paraMap,pager,VasVipGrowRecord.class,"" +
				".searchVipRecordByCondition");
	}

	@Override
	public Map<String, Object> findUserGrowValueAndLevel(Integer regUserId) {
		Map<String, Object> map = new HashMap<>();
		int growValue = this.findUserCurrentGrowValue(regUserId);
		map.put("growValue", growValue);
		map.put("level", vasVipGradeStandardDao.findLevelByGrowValue(growValue));
		return map;
	}

	@Override
	public ResponseEntity getUserGrowRecordDetail(int userId, Pager pager) {
		// 获取该用户的所有成长值记录
		VasVipGrowRecord vasVipGrowRecord = new VasVipGrowRecord();
		vasVipGrowRecord.setRegUserId(userId);
		vasVipGrowRecord.setSortColumns("create_time desc");
		//过滤成长值记录初始值
		Pager result = this.findVasVipGrowRecordList(vasVipGrowRecord,pager);
//		result.setData(result.getData().stream().filter((record) -> {return ((VasVipGrowRecord)record).getGrowValue() != 0;}).collect(Collectors.toList()));
		return new ResponseEntity(SUCCESS, result);
	}

	@Override
	public Map<Integer, VasVipGrowRecord> findUserGrowValueMap(List<Integer> userIdList) {
		return this.vasVipGrowRecordDao.findUserGrowValueMap(userIdList);
	}

	@Override
	public Map<Integer, VasVipGrowRecord> findUserLevelGtZeroMap() {
		return this.vasVipGrowRecordDao.findUserLevelGtZeroMap();
	}

	@Override
	public List<Integer> findUserThreeMonthHasDown(Set<Integer> userIdList) {
		return this.vasVipGrowRecordDao.findUserThreeMonthHasDown(userIdList);
	}
}
