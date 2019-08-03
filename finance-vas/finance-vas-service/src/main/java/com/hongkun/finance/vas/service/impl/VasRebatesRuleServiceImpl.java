package com.hongkun.finance.vas.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.vas.constants.VasConstants;
import com.hongkun.finance.vas.dao.VasRebatesRuleChildDao;
import com.hongkun.finance.vas.dao.VasRebatesRuleDao;
import com.hongkun.finance.vas.enums.VasRuleTypeEnum;
import com.hongkun.finance.vas.model.*;
import com.hongkun.finance.vas.service.VasRebatesRuleService;
import com.hongkun.finance.vas.utils.ClassReflection;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.CompareUtil;
import com.yirun.framework.core.utils.DateUtils;
import com.yirun.framework.core.utils.StringUtilsExtend;
import com.yirun.framework.core.utils.json.JsonUtils;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.redis.JedisClusterLock;
import com.yirun.framework.redis.JedisClusterUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisCluster;

import java.math.BigDecimal;
import java.util.*;

import static com.yirun.framework.core.commons.Constants.LOCK_EXPIRES;
import static com.yirun.framework.core.commons.Constants.LOCK_PREFFIX;

/**
 * @Project : finance
 * @Program Name :
 *          com.hongkun.finance.vas.service.impl.VasRebatesRuleServiceImpl.java
 * @Class Name : VasRebatesRuleServiceImpl.java
 * @Description : GENERATOR SERVICE实现类
 * @Author : generator
 */
@Service
public class VasRebatesRuleServiceImpl implements VasRebatesRuleService {

	private static final Logger logger = LoggerFactory.getLogger(VasRebatesRuleServiceImpl.class);

	/**
	 * VasRebatesRuleDAO
	 */
	@Autowired
	private VasRebatesRuleDao vasRebatesRuleDao;

	@Autowired
	private VasRebatesRuleChildDao vasRebatesRuleChildDao;

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public int insertVasRebatesRule(VasRebatesRule vasRebatesRule) {
		logger.info("方法: insertVasRebatesRule, 保存规则信息, 入参: vasRebatesRule: {}", vasRebatesRule.toString());
		try {
			return this.vasRebatesRuleDao.save(vasRebatesRule);
		} catch (Exception e) {
			logger.error("保存规则信息, 保存失败: ", e);
			throw new GeneralException("保存规则信息失败!");
		}
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertVasRebatesRuleBatch(List<VasRebatesRule> list) {
		this.vasRebatesRuleDao.insertBatch(VasRebatesRule.class, list);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertVasRebatesRuleBatch(List<VasRebatesRule> list, int count) {
		if (logger.isDebugEnabled()) {
			logger.debug("default batch insert size is " + count);
		}
		this.vasRebatesRuleDao.insertBatch(VasRebatesRule.class, list, count);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public int updateVasRebatesRule(VasRebatesRule vasRebatesRule) {
		logger.info("方法: updateVasRebatesRule, 更新规则信息, 入参: vasRebatesRule: {}", vasRebatesRule.toString());
		try {
			return this.vasRebatesRuleDao.update(vasRebatesRule);
		} catch (Exception e) {
			logger.error("更新规则信息, 更新失败: ", e);
			throw new GeneralException("更新规则信息失败!");
		}
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateVasRebatesRuleBatch(List<VasRebatesRule> list, int count) {
		this.vasRebatesRuleDao.updateBatch(VasRebatesRule.class, list, count);
	}

	@Override
	public VasRebatesRule findVasRebatesRuleById(int id) {
		return this.vasRebatesRuleDao.findByPK(Long.valueOf(id), VasRebatesRule.class);
	}

	@Override
	public List<VasRebatesRule> findVasRebatesRuleList(VasRebatesRule vasRebatesRule) {
		return this.vasRebatesRuleDao.findByCondition(vasRebatesRule);
	}

	@Override
	public List<VasRebatesRule> findVasRebatesRuleList(VasRebatesRule vasRebatesRule, int start, int limit) {
		return this.vasRebatesRuleDao.findByCondition(vasRebatesRule, start, limit);
	}

	@Override
	public Pager findVasRebatesRuleList(VasRebatesRule vasRebatesRule, Pager pager) {
		return this.vasRebatesRuleDao.findByCondition(vasRebatesRule, pager);
	}

	@Override
	public int findVasRebatesRuleCount(VasRebatesRule vasRebatesRule) {
		return this.vasRebatesRuleDao.getTotalCount(vasRebatesRule);
	}

	@Override
	public VasRebatesRule findVasRebatesRuleByTypeAndState(int type, int state) {
		String key = String.valueOf(type) + state;
		return this.vasRebatesRuleDao.findVasRebatesRule(key, state, type);
	}

	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	@Override
	public ResponseEntity<?> updateCreditorMoney(BigDecimal creditorMoney) {
		// 从redis中获取当前规则，如果没有，则从数据库中查询
		String key = String.valueOf(VasRuleTypeEnum.CREDITORMONEY.getValue()) + VasConstants.VAS_RULE_STATE_START;
		VasRebatesRule creditorRule = this.vasRebatesRuleDao.findVasRebatesRule(key, VasConstants.VAS_RULE_STATE_START,
				VasRuleTypeEnum.CREDITORMONEY.getValue());
		if (creditorRule == null) {
			return new ResponseEntity<>(Constants.ERROR, "获取当前债权池异常!");
		}
		logger.info("当前债权金额: {}, 要更新的债权金额: {}", creditorRule.getContent(), creditorMoney);
		boolean resultFlag = false;
		String lockKey = LOCK_PREFFIX + VasRebatesRule.class.getSimpleName() + creditorRule.getId();
		JedisClusterLock jedisLock = new JedisClusterLock();
		try {
			boolean result = jedisLock.lock(lockKey, LOCK_EXPIRES, Constants.LOCK_WAITTIME);
			if (result) {
				resultFlag = true;
				// 获取当前债权池限额
				BigDecimal currentMoney = creditorRule.getContent() != null ? new BigDecimal(creditorRule.getContent())
						: BigDecimal.ZERO;
				if (CompareUtil.gt(creditorMoney, currentMoney)) {
					return new ResponseEntity<>(Constants.ERROR, "转入失败，已经超过最大债权池金额!");
				}
				creditorRule.setContent(currentMoney.subtract(creditorMoney).toString());
				// 将改变的后债权池限额重新放回redis中
				String vasKey = VasConstants.VAR_RULE_KEY + String.valueOf(VasRuleTypeEnum.CREDITORMONEY.getValue())
						+ VasConstants.VAS_RULE_STATE_START;
				JedisClusterUtils.setAsJson(vasKey, creditorRule);
			}
		} catch (Exception e) {
			logger.error("更新钱袋子债权异常: ", e);
			throw new GeneralException("更新钱袋子债权池异常!");
		} finally {
			if (resultFlag) {
				jedisLock.freeLock(lockKey);
			}
		}
		return new ResponseEntity<>(Constants.SUCCESS);
	}

	@Override
	public int insertVasRebatesRule(String content, VasRuleTypeEnum vasRuleTypeEnum, String startTime, String endTime) {
		logger.info(
				"方法: insertVasRebatesRule, 插入规则信息, 入参: content: {}, vasRuleTypeEnum: {}, startTime: {}, endTime: {}",
				content, vasRuleTypeEnum, startTime, endTime);
		try {
			VasRebatesRule vasRebatesRule = new VasRebatesRule();
			vasRebatesRule.setContent(content);
			vasRebatesRule.setType(vasRuleTypeEnum.getValue());
			if (startTime != null) {
				vasRebatesRule.setBeginTime(DateUtils.parse(startTime, DateUtils.DATE));
			}
			if (endTime != null) {
				vasRebatesRule.setEndTime(DateUtils.parse(endTime, DateUtils.DATE));
			}
			vasRebatesRule.setState(VasConstants.VAS_RULE_STATE_INIT);
			return this.vasRebatesRuleDao.save(vasRebatesRule);
		} catch (Exception e) {
			logger.error("插入规则信息失败: ", e);
			throw new GeneralException("保存失败");
		}
	}

	@Override
	public int updateVasRebatesRule(String content, String startTime, String endTime, Integer vasRebatesRuleId,
			Integer state) {
		logger.info(
				"方法: updateVasRebatesRule, 通过规则ID,更新规则, 入参: content: {}, startTime: {}, endTime: {}, vasRebatesRuleId: {}, state: {}",
				content, startTime, endTime, vasRebatesRuleId, state);
		try {
			VasRebatesRule vasRebatesRule = new VasRebatesRule();
			if (StringUtils.isNotBlank(content)) {
				vasRebatesRule.setContent(content);
			}
			if (state != null) {
				vasRebatesRule.setState(state);
			}
			if (startTime != null) {
				vasRebatesRule.setBeginTime(DateUtils.parse(startTime, DateUtils.DATE));
			}
			if (endTime != null) {
				vasRebatesRule.setEndTime(DateUtils.parse(endTime, DateUtils.DATE));
			}
			vasRebatesRule.setId(vasRebatesRuleId);
			return this.vasRebatesRuleDao.update(vasRebatesRule);
		} catch (Exception e) {
			logger.error("通过规则ID,更新规则, 更新失败: ", e);
			throw new GeneralException("通过规则ID更新规则失败!");
		}
	}

	@Override
	public ResponseEntity<?> enableRuleById(Integer vasRebatesRuleId) {
		// 根据id查询出该规则
		VasRebatesRule rule = findVasRebatesRuleById(vasRebatesRuleId);
		if (rule == null) {
			return new ResponseEntity<>(Constants.ERROR, "没有查询到该规则(" + vasRebatesRuleId + ")");
		}
		Integer type = rule.getType();
		// 将该类型的，且已启用的置为失效
		VasRebatesRule currentRule = this.findVasRebatesRuleByTypeAndState(type, VasConstants.VAS_RULE_STATE_START);
		if (currentRule != null) {
			this.updateVasRebatesRule(null, null, null, currentRule.getId(), VasConstants.VAS_RULE_STATE_INVALID);
		}
		// 将该规则置为启用
		this.updateVasRebatesRule(null, null, null, vasRebatesRuleId, VasConstants.VAS_RULE_STATE_START);
		// 重置缓存
		rule.setState(VasConstants.VAS_RULE_STATE_START);
		String vasKey = VasConstants.VAR_RULE_KEY + String.valueOf(type) + VasConstants.VAS_RULE_STATE_START;
		JedisClusterUtils.setAsJson(vasKey, rule);
		return new ResponseEntity<>(Constants.SUCCESS);
	}

	@Override
	public ResponseEntity<?> insertVasRebatesRule(RebatesRuleChildItem rebatesRuleChildItem,
			RecommendRuleVo recommendRuleVo) {
		logger.info("方法: insertVasRebatesRule, 保存好友推荐规则信息, 入参: rebatesRuleChildItem: {}, recommendRuleVo: {}",
				rebatesRuleChildItem.toString(), recommendRuleVo.toString());
		Integer vasRuleId = recommendRuleVo.getVasRebatesRuleId();
		try {
			// 1、如果规则ID为空，则认为是新增规则，保存主规则
			if (recommendRuleVo.getVasRebatesRuleId() == null) {
				VasRebatesRule vasRebatesRule = new VasRebatesRule();
				vasRebatesRule.setContent("0");
				vasRebatesRule.setNote("好友推荐");
				vasRebatesRule.setType(VasRuleTypeEnum.RECOMMEND.getValue());
				if (recommendRuleVo.getBeginTime() != null) {
					vasRebatesRule.setBeginTime(DateUtils.parse(recommendRuleVo.getBeginTime(), DateUtils.DATE));
				}
				if (recommendRuleVo.getEndTime() != null) {
					vasRebatesRule.setEndTime(DateUtils.parse(recommendRuleVo.getEndTime(), DateUtils.DATE));
				}
				vasRebatesRule.setState(VasConstants.VAS_RULE_STATE_INIT);
				// 插入规则
				this.vasRebatesRuleDao.save(vasRebatesRule);
				vasRuleId = vasRebatesRule.getId();
			}
			// 2、通过规则ID，及角色类型查询，判断此角色是否已经添加了规则，如果已经添加，不允许重复添加子规则
			VasRebatesRuleChild vasRuleChild = vasRebatesRuleChildDao
					.findRuleChildByUserTypeAndRuleId(Integer.parseInt(recommendRuleVo.getUserType()), vasRuleId);
			if (vasRuleChild != null) {
				return new ResponseEntity<>(Constants.ERROR, "该用户角色的规则已经添加，不允许重复添加!");
			}
			// 3、保存角色规则
			VasRebatesRuleChild ruleChild = new VasRebatesRuleChild();
			ruleChild.setVasRebatesRuleId(vasRuleId);
			ruleChild.setContent(JsonUtils.toJson(rebatesRuleChildItem));
			ruleChild.setSwitchCycle(Integer.parseInt(
					StringUtils.isBlank(recommendRuleVo.getSwitchCycle()) ? "0" : recommendRuleVo.getSwitchCycle()));
			ruleChild.setUserType(Integer.parseInt(recommendRuleVo.getUserType()));
			vasRebatesRuleChildDao.save(ruleChild);
			return new ResponseEntity<>(Constants.SUCCESS, vasRuleId);
		} catch (Exception e) {
			logger.error("保存好友推荐规则信息失败: ", e);
			return new ResponseEntity<>(Constants.ERROR, vasRuleId);
		}
	}

	@Override
	public ResponseEntity<?> findRulePager(VasRebatesRule vasRebatesRule, Pager pager) {
		try {
			Pager pagerInfo = this.vasRebatesRuleDao.findByCondition(vasRebatesRule, pager);
			if (pagerInfo != null) {
				List<VasRebatesRule> ruleList = (List<VasRebatesRule>) pagerInfo.getData();
				for (VasRebatesRule vasRebatesRule2 : ruleList) {
					List<VasRebatesRuleChild> ruleChildList = vasRebatesRuleChildDao
							.findVasRebatesRuleChildByRuleId(vasRebatesRule2.getId());
					vasRebatesRule2.setRuleChildList(ruleChildList);
				}
				pagerInfo.setData(ruleList);
			}
			return new ResponseEntity<>(Constants.SUCCESS, pagerInfo);
		} catch (Exception e) {
			logger.error("查询好友推荐规则分页列表失败: ", e);
			return new ResponseEntity<>(Constants.ERROR, "查询失败");
		}
	}

	@Override
	public ResponseEntity<?> findRuleById(int vasRuleId) {
		logger.info("方法: findRuleById, 通过规则ID查询规则信息, 入参: vasRuleId: {}", vasRuleId);
		try {
			List<VasRecommendRuleVo> recommendRuleVoList = new ArrayList<VasRecommendRuleVo>();
			List<VasRebatesRuleChild> ruleChildList = vasRebatesRuleChildDao.findVasRebatesRuleChildByRuleId(vasRuleId);
			logger.info("通过规则ID查询规则信息, 角色规则集合: {}", ruleChildList == null ? "" : JsonUtils.toJson(ruleChildList));
			for (VasRebatesRuleChild vasRebatesRuleChild : ruleChildList) {
				VasRebatesRule vasRebatesRule = this.vasRebatesRuleDao.findByPK(Long.valueOf(vasRuleId),
						VasRebatesRule.class);
				VasRecommendRuleVo vecommendRuleVo = new VasRecommendRuleVo();
				ClassReflection.reflectionAttr(vasRebatesRule, vecommendRuleVo);
				vecommendRuleVo.setBeginTime(vasRebatesRule.getBeginTime());
				vecommendRuleVo.setEndTime(vasRebatesRule.getEndTime());
				RebatesRuleChildItem rebatesRuleChildItem = JsonUtils.json2GenericObject(
						vasRebatesRuleChild.getContent(), new TypeReference<RebatesRuleChildItem>() {
						}, "");
				ClassReflection.reflectionAttr(rebatesRuleChildItem, vecommendRuleVo);
				vecommendRuleVo.setUserType(vasRebatesRuleChild.getUserType().toString());
				vecommendRuleVo.setSwitchCycle(vasRebatesRuleChild.getSwitchCycle());
				List<RuleItemVo> ruleItemVoOne = rebatesRuleChildItem.getFriendLevelOne();
				List<VasRuleItemLevelOne> friendLevelOne = new ArrayList<VasRuleItemLevelOne>();
				// 组装一级好友规则信息
				if (ruleItemVoOne != null) {
					for (RuleItemVo ruleItemVo : ruleItemVoOne) {
						VasRuleItemLevelOne vasRuleItemLevelOne = new VasRuleItemLevelOne();
						vasRuleItemLevelOne.setInvNumOne(ruleItemVo.getInvestStrokeCount());
						vasRuleItemLevelOne.setRebatesRateOne(ruleItemVo.getRate());
						friendLevelOne.add(vasRuleItemLevelOne);
						vecommendRuleVo.setFriendLvelOneList(friendLevelOne);
					}
				}
				// 组装二级好友推荐规则信息
				List<VasRuleItemLevelTwo> friendLevelTwo = new ArrayList<VasRuleItemLevelTwo>();
				List<RuleItemVo> ruleItemVoTwo = rebatesRuleChildItem.getFriendLevelTwo();
				if (ruleItemVoTwo != null) {
					for (RuleItemVo ruleItemVo2 : ruleItemVoTwo) {
						VasRuleItemLevelTwo vasRuleItemLevelTwo = new VasRuleItemLevelTwo();
						vasRuleItemLevelTwo.setInvNumTwo(ruleItemVo2.getInvestStrokeCount());
						vasRuleItemLevelTwo.setRebatesRateTwo(ruleItemVo2.getRate());
						friendLevelTwo.add(vasRuleItemLevelTwo);
						vecommendRuleVo.setFriendLvelTwoList(friendLevelTwo);
					}
				}
				recommendRuleVoList.add(vecommendRuleVo);
			}
			logger.info("通过规则ID查询规则信息, 展示给前台的规则列表信息: {}",
					recommendRuleVoList == null ? "" : JsonUtils.toJson(recommendRuleVoList));
			return new ResponseEntity<>(Constants.SUCCESS, recommendRuleVoList);
		} catch (Exception e) {
			logger.error("查询推荐规则信息失败: ", e);
			return new ResponseEntity<>(Constants.ERROR, "查询失败");
		}
	}

	@Override
	public ResponseEntity<?> updateVasRuleById(RebatesRuleChildItem rebatesRuleChildItem,
			RecommendRuleVo recommendRuleVo) {
		logger.info("方法: updateVasRuleById, 通过规则ID修改角色规则信息, 入参: rebatesRuleChildItem: {}, recommendRuleVo: {}",
				rebatesRuleChildItem.toString(), recommendRuleVo.toString());
		Integer vasRuleId = recommendRuleVo.getVasRebatesRuleId();
		try {
			// 根据主规则ID，修改规则的开始时间及结束时间信息
			VasRebatesRule vasRebatesRule = new VasRebatesRule();
			vasRebatesRule.setId(vasRuleId);
			if (recommendRuleVo.getBeginTime() != null) {
				vasRebatesRule.setBeginTime(DateUtils.parse(recommendRuleVo.getBeginTime(), DateUtils.DATE));
			}
			if (recommendRuleVo.getEndTime() != null) {
				vasRebatesRule.setEndTime(DateUtils.parse(recommendRuleVo.getEndTime(), DateUtils.DATE));
			}
			vasRebatesRule.setModifyTime(new Date());
			this.vasRebatesRuleDao.update(vasRebatesRule);
			// 查询规则对应的角色的规则信息是否存在，如果不存在，则不允许修改
			VasRebatesRuleChild vasRuleChild = vasRebatesRuleChildDao
					.findRuleChildByUserTypeAndRuleId(Integer.parseInt(recommendRuleVo.getUserType()), vasRuleId);
			if (vasRuleChild == null) {
				return new ResponseEntity<>(Constants.ERROR, "该用户角色还没有添加规则,不能修改!");
			}
			// 修改规则下的对应的角色规则信息
			VasRebatesRuleChild ruleChild = new VasRebatesRuleChild();
			ruleChild.setId(vasRuleChild.getId());
			ruleChild.setContent(JsonUtils.toJson(rebatesRuleChildItem));
			ruleChild.setSwitchCycle(Integer.parseInt(
					StringUtils.isBlank(recommendRuleVo.getSwitchCycle()) ? "0" : recommendRuleVo.getSwitchCycle()));
			vasRebatesRuleChildDao.update(ruleChild);
			return new ResponseEntity<>(Constants.SUCCESS, vasRuleId);
		} catch (Exception e) {
			logger.error("通过规则ID修改角色规则信息, 更新失败: ", e);
			return new ResponseEntity<>(Constants.ERROR, vasRuleId);
		}
	}

    @Override
    /**
     * @Description : 校验钱袋子转入转出规则
     * @Method_Name : checkQdzRule;
     * @return
     * @return : ResponseEntity<?>;
     * @Creation Date : 2017年7月17日 下午2:35:10;
     * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
     */
    public ResponseEntity<?> checkQdzRule() {
        Map<String, Object> resultMap = new HashMap<>();
        ResponseEntity<?> result = new ResponseEntity<>(Constants.SUCCESS);
        // 查询钱袋子规则，进行校验
        VasRebatesRule vasRebatesRule = findVasRebatesRuleByTypeAndState(VasRuleTypeEnum.QDZ.getValue(), VasConstants.VAS_RULE_STATE_START);
        if (vasRebatesRule == null) {
            logger.error("获取钱袋子信息，系统没有设置规则!");
            return new ResponseEntity<>(Constants.ERROR, "系统没有设置规则!");
        }
        QdzVasRuleItem qdzVasRuleItem = JsonUtils.json2Object(vasRebatesRule.getContent(), QdzVasRuleItem.class, null);
        // 默认抢购中
        qdzVasRuleItem.setState(VasConstants.QDZ_RULR_STATE_BUYING);
        // 获取钱袋子转入转出总开关,如果查询不到，则认为不能转入转出
        VasRebatesRule vasRebatesRuleOnOff = findVasRebatesRuleByTypeAndState(
                VasRuleTypeEnum.QDZINOUTONOFF.getValue(), VasConstants.VAS_RULE_STATE_START);
        // 判断钱袋子转入转出是否在设置的时间段内
        boolean qdzValidTime = DateUtils.isValidTime(qdzVasRuleItem.getNoInOutStartTimes(),
                qdzVasRuleItem.getNoInOutEndTimes());
        if (vasRebatesRuleOnOff == null  || qdzValidTime) {
            qdzVasRuleItem.setState(VasConstants.QDZ_RULR_STATE_COUNTDOWN);
            String startTime =qdzVasRuleItem.getNoInOutStartTimes();
            String endTime =qdzVasRuleItem.getNoInOutEndTimes();
            if (startTime.compareTo(endTime) >= 0) {
                qdzVasRuleItem.setNextBuyTime(DateUtils.addDays(DateUtils.parse(DateUtils.format(new Date()) + " " + endTime), 1).getTime());
             }
             if (startTime.compareTo(endTime) <= 0) {
                 qdzVasRuleItem.setNextBuyTime(DateUtils.parse(DateUtils.format(new Date()) + " " + endTime).getTime());
             }
        }
        VasRebatesRule residueBuyCreditor = findVasRebatesRuleByTypeAndState(
                VasRuleTypeEnum.CREDITORMONEY.getValue(), VasConstants.VAS_RULE_STATE_START);
        if (residueBuyCreditor != null && !StringUtilsExtend.isEmpty(residueBuyCreditor.getContent())) {
            qdzVasRuleItem.setResidueBuyAmount(new BigDecimal(residueBuyCreditor.getContent()));
        }
        // 查询钱袋子利息加息的开关
        VasRebatesRule qdzRaiseInterestOnOff = findVasRebatesRuleByTypeAndState(
                VasRuleTypeEnum.QDZINTERESTADDONOFF.getValue(), VasConstants.VAS_RULE_STATE_START);
        if (qdzRaiseInterestOnOff != null) {
            QdzRaiseInterestVo qdzRaiseInterestVo = JsonUtils.json2Object(qdzRaiseInterestOnOff.getContent(),
                    QdzRaiseInterestVo.class, null);
            resultMap.put("qdzRaiseInterest", qdzRaiseInterestVo);
        }
        //如果债权池金额小于等于0，则设置状态已售完
        if(CompareUtil.lteZero(qdzVasRuleItem.getResidueBuyAmount())){
            qdzVasRuleItem.setState(VasConstants.QDZ_RULR_STATE_SOLDOUT);
        }
        qdzVasRuleItem.setSystemTime(new Date().getTime());
        resultMap.put("qdzVasRuleItem", qdzVasRuleItem);
        resultMap.put("vasRebatesRule", vasRebatesRule);
        result.setParams(resultMap);
        return result;
    }

    @Override
    public ResponseEntity<?> getQdzRule(Integer type) {
        ResponseEntity<?> res = new ResponseEntity<>(Constants.SUCCESS);
        Map<String,Object> resultMap = new HashMap<String,Object>();
        //获取钱袋子规则
        VasRebatesRule vasRebatesRule = findVasRebatesRuleByTypeAndState(VasRuleTypeEnum.QDZ.getValue(), VasConstants.VAS_RULE_STATE_START);
        QdzVasRuleItem qdzVasRuleItem = JsonUtils.json2Object(vasRebatesRule.getContent(), QdzVasRuleItem.class, null);
        //获取钱袋子加息开关规则
    /*    VasRebatesRule qdzRaiseInterestOnOff = findVasRebatesRuleByTypeAndState(
                VasRuleTypeEnum.QDZINTERESTADDONOFF.getValue(), VasConstants.VAS_RULE_STATE_START);
        QdzRaiseInterestVo qdzRaiseInterestVo = JsonUtils.json2Object(qdzRaiseInterestOnOff.getContent(),
                QdzRaiseInterestVo.class, null);*/
        //转入
        if(type == 1){
            List<Object> transInList = new ArrayList<Object>();
            transInList.add("最低转入金额"+qdzVasRuleItem.getInvestLowest()+"元，并以"+qdzVasRuleItem.getInvestLowest()+"元的整数倍递增。");
            transInList.add("每人每天累计最高可转入"+qdzVasRuleItem.getInMaxMoneyPPPD()+"元，转入不收费。");
            transInList.add("本金每天17:00之前转入当天可产生收益，收益将直接打入鸿坤金服账户。");
            transInList.add("每天的收益按当天本金单利计算，并直接打入鸿坤金服账户中。具体计算公式为：当天本金×当天钱袋子预期年化收益率÷360。");
            transInList.add("每天晚上"+qdzVasRuleItem.getNoInOutStartTimes()+"至第二天凌晨"+qdzVasRuleItem.getNoInOutEndTimes()+"期间为钱袋子结算时间，不接受转入申请。");
            transInList.add("一次性转入100万（含）以上的用户，需提前3天进行预约，预约电话：400-900-9630/400-099-0229。");
            transInList.add("本产品不能使用加息券、投资红包且不享受积分奖励。");

            resultMap.put("title", "转入规则");
            resultMap.put("qdzRuleInfo", transInList);
        }else{
            //转出规则
            List<Object> transOutList = new ArrayList<Object>();
            transOutList.add("最低转出金额100元，并以100元的整数倍递增。");
            transOutList.add("每人每天累计最高可转出"+qdzVasRuleItem.getOutMaxMoneyPPPD()+"元，每月前"+qdzVasRuleItem.getOutOPPPerMonth()+"次转出免费，之后按转出金额"+qdzVasRuleItem.getOutPayRate()+"%收取转出手续费。");
            transOutList.add("每天晚上"+qdzVasRuleItem.getNoInOutStartTimes()+"至第二天凌晨"+qdzVasRuleItem.getNoInOutEndTimes()+"为钱袋子结算时间，不接受转出申请。");
            transOutList.add("一次性转出100万（含）以上的用户，需提前3天进行预约，预约电话：400-900-9630/400-099-0229。");
            transOutList.add("转出到银行卡每次将收取1元手续费，提现券的将自动优先使用提现券。");
            resultMap.put("title","转出规则");
            resultMap.put("qdzRuleInfo",transOutList);
        }
        res.setParams(resultMap);
        return res;
    }
}
