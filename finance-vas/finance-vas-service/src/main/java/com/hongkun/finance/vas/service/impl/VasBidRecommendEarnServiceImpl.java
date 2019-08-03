package com.hongkun.finance.vas.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.invest.util.CalcInterestUtil;
import com.hongkun.finance.user.utils.BaseUtil;
import com.hongkun.finance.vas.constants.VasConstants;
import com.hongkun.finance.vas.dao.VasBidRecommendEarnDao;
import com.hongkun.finance.vas.dao.VasRebatesRuleChildDao;
import com.hongkun.finance.vas.dao.VasRebatesRuleDao;
import com.hongkun.finance.vas.enums.VasRuleTypeEnum;
import com.hongkun.finance.vas.model.RcommendEarnInfo;
import com.hongkun.finance.vas.model.RebatesRuleChildItem;
import com.hongkun.finance.vas.model.RuleItemVo;
import com.hongkun.finance.vas.model.VasBidRecommendEarn;
import com.hongkun.finance.vas.model.VasRebatesRule;
import com.hongkun.finance.vas.model.VasRebatesRuleChild;
import com.hongkun.finance.vas.model.vo.RecommendEarnForAppVo;
import com.hongkun.finance.vas.service.VasBidRecommendEarnService;
import com.hongkun.finance.vas.utils.ClassReflection;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.img.QRCodeUtil;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.DateUtils;
import com.yirun.framework.core.utils.json.JsonUtils;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.oss.OSSLoader;
import com.yirun.framework.oss.model.FileInfo;
import com.yirun.framework.oss.model.FileState;
import com.yirun.framework.oss.model.FileType;
import com.yirun.framework.oss.model.OSSBuckets;

import org.apache.commons.lang3.StringUtils;
import org.mengyun.tcctransaction.api.Compensable;
import org.mengyun.tcctransaction.dubbo.context.DubboTransactionContextEditor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Project : finance
 * @Program Name :
 *          com.hongkun.finance.vas.service.impl.VasBiddRecommendEarnServiceImpl.
 *          java
 * @Class Name : VasBiddRecommendEarnServiceImpl.java
 * @Description : GENERATOR SERVICE实现类
 * @Author : generator
 */
@Service
public class VasBidRecommendEarnServiceImpl implements VasBidRecommendEarnService {

	private static final Logger logger = LoggerFactory.getLogger(VasBidRecommendEarnServiceImpl.class);

	/**
	 * VasBiddRecommendEarnDAO
	 */
	@Autowired
	private VasBidRecommendEarnDao vasBidRecommendEarnDao;
	/**
	 * VasRebatesRuleDao
	 */
	@Autowired
	private VasRebatesRuleDao vasRebatesRuleDao;

	@Autowired
	private VasRebatesRuleChildDao vasRebatesRuleChildDao;

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public int insertVasBidRecommendEarn(VasBidRecommendEarn vasBiddRecommendEarn) {
		return this.vasBidRecommendEarnDao.save(vasBiddRecommendEarn);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertVasBidRecommendEarnBatch(List<VasBidRecommendEarn> list) {
		this.vasBidRecommendEarnDao.insertBatch(VasBidRecommendEarn.class, list);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertVasBidRecommendEarnBatch(List<VasBidRecommendEarn> list, int count) {
		if (logger.isDebugEnabled()) {
			logger.debug("default batch insert size is " + count);
		}
		this.vasBidRecommendEarnDao.insertBatch(VasBidRecommendEarn.class, list, count);
	}

	@Override
	@Compensable(cancelMethod = "updateVasBiddRecommendEarnForCancel", transactionContextEditor = DubboTransactionContextEditor.class)
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public int updateVasBidRecommendEarn(VasBidRecommendEarn vasBiddRecommendEarn) {
		logger.info("{}, 更新推荐奖记录, 入参: vasBiddRecommendEarn: {}", BaseUtil.getTccTryLogPrefix(),
				vasBiddRecommendEarn == null ? "" : vasBiddRecommendEarn.toString());
		try {
			return this.vasBidRecommendEarnDao.update(vasBiddRecommendEarn);
		} catch (Exception e) {
			logger.error("{}, 更新推荐奖记录, 更新失败: ", BaseUtil.getTccTryLogPrefix(), e);
			throw new GeneralException("更新推荐奖记录失败!");
		}
	}

	/**
	 * @Description : TCC更新推荐奖信息回滚方法
	 * @Method_Name : updateVasBiddRecommendEarnForCancel;
	 * @param vasBiddRecommendEarn
	 * @return
	 * @return : int;
	 * @Creation Date : 2017年10月25日 上午10:10:41;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public int updateVasBidRecommendEarnForCancel(VasBidRecommendEarn vasBiddRecommendEarn) {
		logger.info("try cancel updateVasBidRecommendEarn, 入参: vasBiddRecommendEarn: {}",
				vasBiddRecommendEarn.toString());
		vasBiddRecommendEarn.setState(VasConstants.RECOMMEND_EARN_STATE_REVIEW_SUCCESS);
		return this.vasBidRecommendEarnDao.update(vasBiddRecommendEarn);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateVasBidRecommendEarnBatch(List<VasBidRecommendEarn> list, int count) {
		this.vasBidRecommendEarnDao.updateBatch(VasBidRecommendEarn.class, list, count);
	}

	@Override
	public VasBidRecommendEarn findVasBidRecommendEarnById(int id) {
		return this.vasBidRecommendEarnDao.findByPK(Long.valueOf(id), VasBidRecommendEarn.class);
	}

	@Override
	public List<VasBidRecommendEarn> findVasBidRecommendEarnList(VasBidRecommendEarn vasBidRecommendEarn) {
		return this.vasBidRecommendEarnDao.findByCondition(vasBidRecommendEarn);
	}

	@Override
	public List<VasBidRecommendEarn> findVasBidRecommendEarnList(VasBidRecommendEarn vasBidRecommendEarn, int start,
			int limit) {
		return this.vasBidRecommendEarnDao.findByCondition(vasBidRecommendEarn, start, limit);
	}

	@Override
	public Pager findVasBidRecommendEarnList(VasBidRecommendEarn vasBidRecommendEarn, Pager pager) {
		return this.vasBidRecommendEarnDao.findByCondition(vasBidRecommendEarn, pager);
	}

	@Override
	public int findVasBidRecommendEarnCount(VasBidRecommendEarn vasBidRecommendEarn) {
		return this.vasBidRecommendEarnDao.getTotalCount(vasBidRecommendEarn);
	}

	@Override
	public Pager findVasBidRecommendEarnListByInfo(Map<String, Object> biddRecommendEarnMap, Pager pager) {
		return this.vasBidRecommendEarnDao.findVasBidRecommendEarnListByInfo(biddRecommendEarnMap, pager);
	}

	@Override
	public ResponseEntity<?> insertVasBidRecommendEarn(RcommendEarnInfo bidRcommendEarnInfo) {
		logger.info("方法: insertVasBidRecommendEarn, 生成好友推荐奖操作, 入参: bidRcommendEarnInfo: {}",
				bidRcommendEarnInfo.toString());
		String rebatesRate1 = "";// 一级好友利率
		String rebatesRate2 = "";// 二级好友利率
		BigDecimal recommendMoney = BigDecimal.ZERO;// 推荐金额
		try {
			// 校验参数合法性
			ResponseEntity<?> responseEntity = checkBidRecommendEarnInfo(bidRcommendEarnInfo);
			if (responseEntity.getResStatus() == Constants.ERROR) {
				logger.error("生成好友推荐奖操作, 推荐人标识: {}, 被推荐人标识: {}, 校验参数异常: {}",
						bidRcommendEarnInfo.getRecommendRegUserId(), bidRcommendEarnInfo.getRegUserId(),
						responseEntity.getResMsg().toString());
				return responseEntity;
			}
			// 查询推荐奖规则
			VasRebatesRule vasRebatesRule = null;
			if (bidRcommendEarnInfo.getVasRuleId() != null) {
				vasRebatesRule = this.vasRebatesRuleDao.findByPK(Long.valueOf(bidRcommendEarnInfo.getVasRuleId()),
						VasRebatesRule.class);
			} else {
				String key = String.valueOf(bidRcommendEarnInfo.getType()) + VasConstants.VAS_RULE_STATE_START;
				vasRebatesRule = vasRebatesRuleDao.findVasRebatesRule(key, VasConstants.VAS_RULE_STATE_START,
						bidRcommendEarnInfo.getType());
			}
			if (vasRebatesRule == null) {
				logger.error("生成好友推荐奖操作, 推荐人标识: {}, 被推荐人标识: {}, 生成推荐奖异常: {}",
						bidRcommendEarnInfo.getRecommendRegUserId(), bidRcommendEarnInfo.getRegUserId(),
						"没有查询到对应推荐规则记录!");
				return new ResponseEntity<>(Constants.ERROR, "没有查询到对应规则记录!");
			}
			logger.info("生成好友推荐奖操作, 推荐人标识: {}, 被推荐人标识: {}, 好友推荐规则 vasRebatesRule: {}",
					bidRcommendEarnInfo.getRecommendRegUserId(), bidRcommendEarnInfo.getRegUserId(),
					vasRebatesRule.toString());
			// 判断规则时间，是否在规则推荐时间之内
			if (vasRebatesRule.getBeginTime() != null && !"0001-01-01 00:00:00"
					.equals(DateUtils.format(vasRebatesRule.getBeginTime(), DateUtils.DATE_HH_MM_SS))) {
				// 推荐规则开始时间
				String beginTime = DateUtils.format(vasRebatesRule.getBeginTime(), DateUtils.DATE);
				// 推荐规则结束时间
				String endTime = DateUtils.format(vasRebatesRule.getEndTime(), DateUtils.DATE);
				if (StringUtils.isBlank(beginTime) || StringUtils.isBlank(endTime)) {
					logger.error("生成好友推荐奖操作, 推荐人标识: {}, 被推荐人标识: {}, 生成推荐奖异常: {}",
							bidRcommendEarnInfo.getRecommendRegUserId(), bidRcommendEarnInfo.getRegUserId(),
							"规则生成有误,规则时间为空！");
					return new ResponseEntity<>(Constants.ERROR, "规则生成有误,推荐规则时间为空！");
				}
				// 1：如果规则时间，小于推荐规则开始时间,则没有推荐奖记录；2：如果规则时间，大于推荐规则结束时间，也不生成推荐奖记录
				if (bidRcommendEarnInfo.getRuleTime().getTime() < DateUtils.parse(beginTime).getTime()
						|| bidRcommendEarnInfo.getRuleTime().getTime() > DateUtils.parse(endTime).getTime()) {
					logger.error("生成好友推荐奖操作, 推荐人标识: {}, 被推荐人标识: {}, 生成推荐奖异常: {}",
							bidRcommendEarnInfo.getRecommendRegUserId(), bidRcommendEarnInfo.getRegUserId(),
							"规则时间,不在推荐奖发放时间之内！");
					return new ResponseEntity<>(Constants.ERROR, "规则时间,不在推荐奖发放时间之内！");
				}
			}
			// 查询每个角色对应的规则
			VasRebatesRuleChild vasRebatesRuleChild = vasRebatesRuleChildDao
					.findRuleChildByUserTypeAndRuleId(bidRcommendEarnInfo.getUserType(), vasRebatesRule.getId());
			if (vasRebatesRuleChild == null) {
				logger.error("生成好友推荐奖操作, 推荐人标识: {}, 被推荐人标识: {}, 用户角色: {}, 生成推荐奖异常: {}",
						bidRcommendEarnInfo.getRecommendRegUserId(), bidRcommendEarnInfo.getRegUserId(),
						bidRcommendEarnInfo.getUserType(), "该用户角色没有对应的推荐规则不生成推荐奖！");
				return new ResponseEntity<>(Constants.ERROR, "该用户角色没有对应的推荐规则不生成推荐奖！");
			}
			logger.info("生成好友推荐奖操作, 推荐人标识: {}, 被推荐人标识: {}, 角色规则: {}", bidRcommendEarnInfo.getRecommendRegUserId(),
					bidRcommendEarnInfo.getRegUserId(), vasRebatesRuleChild);
			// 用户注册时间大于转换的周期，不生成推荐奖
			long diff = DateUtils.betDate(bidRcommendEarnInfo.getInvestTime(), bidRcommendEarnInfo.getRegistTime());
			long days = diff / (1000 * 60 * 60 * 24);
			logger.info("生成好友推荐奖操作, 推荐人标识: {}, 被推荐人标识: {}, 转换周期与注册时间相差天数: {}",
					bidRcommendEarnInfo.getRecommendRegUserId(), bidRcommendEarnInfo.getRegUserId(), days);
			if (vasRebatesRuleChild.getSwitchCycle() > 0) {
				if (days > vasRebatesRuleChild.getSwitchCycle()) {
					logger.error("生成好友推荐奖操作, 推荐人标识: {}, 被推荐人标识: {},  用户角色: {}, 生成推荐奖异常: {}",
							bidRcommendEarnInfo.getRecommendRegUserId(), bidRcommendEarnInfo.getRegUserId(),
							vasRebatesRuleChild.getUserType(), "注册时间大于转换期，不生成推荐奖!");
					return new ResponseEntity<>(Constants.ERROR, "注册时间大于转换期，不生成推荐奖!");
				}
			}
			// 将字符串的JSON格式，转换为对象，获取一、二级好友的最大投资笔数，和利率的限额，
			RebatesRuleChildItem rebatesRuleChildItem = JsonUtils.json2Object(vasRebatesRuleChild.getContent(),
					RebatesRuleChildItem.class, null);
			if (rebatesRuleChildItem == null) {
				logger.error("生成好友推荐奖操作, 推荐人标识: {}, 被推荐人标识: {}, 生成推荐奖异常: {}",
						bidRcommendEarnInfo.getRecommendRegUserId(), bidRcommendEarnInfo.getRegUserId(), "规则内容获取为空！");
				return new ResponseEntity<>(Constants.ERROR, "规则内容获取为空！");
			} else {
				logger.info("生成好友推荐奖操作, 推荐人标识: {}, 被推荐人标识: {}, 用户角色: {}", bidRcommendEarnInfo.getRecommendRegUserId(),
						bidRcommendEarnInfo.getRegUserId(), vasRebatesRuleChild.getUserType());
				// 如果是一级好友，则获取对应一级推荐好友的规则信息
				if (StringUtils.equalsIgnoreCase(String.valueOf(VasConstants.RECOMMEND_FRIEND_LEVEL_ONE),
						String.valueOf(bidRcommendEarnInfo.getFriendLevel()))) {
					// 遍历循环一级好友的投资笔数是否符合，如果符合，则获取对应的执行利率
					List<RuleItemVo> friendLevelOne = rebatesRuleChildItem.getFriendLevelOne();
					boolean countLevelOneFlag = false;
					if (friendLevelOne != null && friendLevelOne.size() > 0) {
						for (RuleItemVo ruleItemVo : friendLevelOne) {
							if (ruleItemVo.getInvestStrokeCount()
									.equals(bidRcommendEarnInfo.getInvestNum().toString())) {
								countLevelOneFlag = true;
								rebatesRate1 = ruleItemVo.getRate();
								// 一级好友获取佣金金额
								recommendMoney = CalcInterestUtil.calRecommendAmount(
										bidRcommendEarnInfo.getInvestAmount(), bidRcommendEarnInfo.getTermUnit(),
										bidRcommendEarnInfo.getTermValue(), new BigDecimal(rebatesRate1));
								break;
							}
						}
					}
					// 如果没有符合的投资笔数，则判断默认的投资笔数是否在规则之内，如果符合，则获取默认的执行利率
					if (!countLevelOneFlag
							&& bidRcommendEarnInfo.getInvestNum()
									.compareTo(Integer.valueOf(rebatesRuleChildItem.getFromInvNumOne())) >= 0
							&& bidRcommendEarnInfo.getInvestNum()
									.compareTo(Integer.valueOf(rebatesRuleChildItem.getToInvNumOne())) <= 0) {
						countLevelOneFlag = true;
						//推荐规则利率为0不生成推荐奖
						rebatesRate1 = StringUtils.isBlank(rebatesRuleChildItem.getRebatesRateLevelOne())?"0":rebatesRuleChildItem.getRebatesRateLevelOne();
						if(new BigDecimal(rebatesRate1).compareTo(BigDecimal.ZERO) == 0){
							logger.error("生成好友推荐奖操作, 推荐人标识: {}, 被推荐人标识: {}, 生成推荐奖异常: {}",
									bidRcommendEarnInfo.getRecommendRegUserId(), bidRcommendEarnInfo.getRegUserId(),
									"一级好友,好友推荐规则利率为0,没有推荐奖!");
							return new ResponseEntity<>(Constants.ERROR, "一级好友,好友推荐规则利率为0,没有推荐奖!");
						}
						// 一级好友获取佣金金额
						recommendMoney = CalcInterestUtil.calRecommendAmount(bidRcommendEarnInfo.getInvestAmount(),
								bidRcommendEarnInfo.getTermUnit(), bidRcommendEarnInfo.getTermValue(),
								new BigDecimal(rebatesRate1));
					}
					if (!countLevelOneFlag) {
						logger.error("生成好友推荐奖操作, 推荐人标识: {}, 被推荐人标识: {}, 生成推荐奖异常: {}",
								bidRcommendEarnInfo.getRecommendRegUserId(), bidRcommendEarnInfo.getRegUserId(),
								"一级好友,投资笔数不在发放推荐规则之内,没有推荐奖!");
						return new ResponseEntity<>(Constants.ERROR, "一级好友,投资笔数不在发放推荐规则之内,没有推荐奖!");
					}
				}
				// 如果是二级好友，则获取对应二级推荐好友的规则信息
				if (StringUtils.equalsIgnoreCase(String.valueOf(VasConstants.RECOMMEND_FRIEND_LEVEL_TWO),
						String.valueOf(bidRcommendEarnInfo.getFriendLevel()))) {
					// 遍历循环二级好友的投资笔数是否符合，如果符合，则获取对应的执行利率
					List<RuleItemVo> friendLevelTwo = rebatesRuleChildItem.getFriendLevelTwo();
					boolean countLevelTwoFlag = false;
					if (friendLevelTwo != null && friendLevelTwo.size() > 0) {
						for (RuleItemVo ruleItemVo : friendLevelTwo) {
							if (ruleItemVo.getInvestStrokeCount()
									.equals(bidRcommendEarnInfo.getInvestNum().toString())) {
								countLevelTwoFlag = true;
								rebatesRate2 = ruleItemVo.getRate();
								// 二级好友获取佣金金额
								recommendMoney = CalcInterestUtil.calRecommendAmount(
										bidRcommendEarnInfo.getInvestAmount(), bidRcommendEarnInfo.getTermUnit(),
										bidRcommendEarnInfo.getTermValue(), new BigDecimal(rebatesRate2));
								break;
							}
						}
					}
					// 如果没有符合的投资笔数，则判断默认的投资笔数是否在规则之内，如果符合，则获取默认的执行利率
					if (!countLevelTwoFlag
							&& bidRcommendEarnInfo.getInvestNum()
									.compareTo(Integer.valueOf(rebatesRuleChildItem.getFromInvNumTwo())) >= 0
							&& bidRcommendEarnInfo.getInvestNum()
									.compareTo(Integer.valueOf(rebatesRuleChildItem.getToInvNumTwo())) <= 0) {
						countLevelTwoFlag = true;
						rebatesRate2 = StringUtils.isBlank(rebatesRuleChildItem.getRebatesRateLevelTwo())?"0":rebatesRuleChildItem.getRebatesRateLevelTwo();
						//推荐规则利率为0不生成推荐奖
						if(new BigDecimal(rebatesRate2).compareTo(BigDecimal.ZERO) == 0){
						    logger.error("生成好友推荐奖操作, 推荐人标识: {}, 被推荐人标识: {}, 生成推荐奖异常: {}",
	                                bidRcommendEarnInfo.getRecommendRegUserId(), bidRcommendEarnInfo.getRegUserId(),
	                                "二级好友,推荐规则利率为0,没有推荐奖!");
	                        return new ResponseEntity<>(Constants.ERROR, "二级好友,推荐规则利率为0,没有推荐奖!");
						}
						// 二级好友获取佣金金额
						recommendMoney = CalcInterestUtil.calRecommendAmount(bidRcommendEarnInfo.getInvestAmount(),
								bidRcommendEarnInfo.getTermUnit(), bidRcommendEarnInfo.getTermValue(),
								new BigDecimal(rebatesRate2));
					}
					if (!countLevelTwoFlag) {
						logger.error("生成好友推荐奖操作, 推荐人标识: {}, 被推荐人标识: {}, 生成推荐奖异常: {}",
								bidRcommendEarnInfo.getRecommendRegUserId(), bidRcommendEarnInfo.getRegUserId(),
								"二级好友,投资笔数不在发放推荐规则之内,没有推荐奖!");
						return new ResponseEntity<>(Constants.ERROR, "二级好友,投资笔数不在发放推荐规则之内,没有推荐奖!");

					}
				}
			}
			// 封装保存推荐奖记录对象，插入到数据库中
			VasBidRecommendEarn vasBiddRecommendEarn = new VasBidRecommendEarn();
			ClassReflection.reflectionAttr(bidRcommendEarnInfo, vasBiddRecommendEarn);
			if (StringUtils.isBlank(bidRcommendEarnInfo.getNote())) {
				vasBiddRecommendEarn.setNote("好友推荐");
			}
			vasBiddRecommendEarn.setBidId(bidRcommendEarnInfo.getBiddId());
			vasBiddRecommendEarn.setEarnAmount(recommendMoney);
			vasBiddRecommendEarn.setRuleId(vasRebatesRuleChild.getId());
			vasBidRecommendEarnDao.save(vasBiddRecommendEarn);
			return new ResponseEntity<>(Constants.SUCCESS, "推荐奖生成成功");
		} catch (Exception e) {
			logger.error("生成好友推荐奖操作, 推荐人标识: {},被推荐人标识: {}, 入参: bidRcommendEarnInfo: {}, 生成推荐奖失败信息: ",
					bidRcommendEarnInfo.getRecommendRegUserId(), bidRcommendEarnInfo.getRegUserId(),
					bidRcommendEarnInfo.toString(), e);
			return new ResponseEntity<>(Constants.ERROR, "生成推荐奖失败!");

		}
	}

	/**
	 * @Description :校验推荐奖参数有效性
	 * @Method_Name : checkBidRecommendEarnInfo;
	 * @param bidRcommendEarnInfo
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年7月3日 上午9:48:12;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	private ResponseEntity<?> checkBidRecommendEarnInfo(RcommendEarnInfo bidRcommendEarnInfo) {
		if (bidRcommendEarnInfo.getRegUserId() == null || bidRcommendEarnInfo.getRegUserId() == 0) {
			return new ResponseEntity<>(Constants.ERROR, "被推荐人ID不能为空！");
		}
		if (bidRcommendEarnInfo.getRecommendRegUserId() == null || bidRcommendEarnInfo.getRecommendRegUserId() == 0) {
			return new ResponseEntity<>(Constants.ERROR, "推荐人ID不能为空！");
		}
		if (bidRcommendEarnInfo.getResourceId() == null || bidRcommendEarnInfo.getResourceId() == 0) {
			return new ResponseEntity<>(Constants.ERROR, "来源ID不能为空！");
		}
		if (bidRcommendEarnInfo.getBiddId() == null || bidRcommendEarnInfo.getBiddId() == 0) {
			return new ResponseEntity<>(Constants.ERROR, "标的ID不能为空！");
		}
		if (bidRcommendEarnInfo.getInvestAmount() == null
				|| bidRcommendEarnInfo.getInvestAmount().compareTo(BigDecimal.ZERO) <= 0) {
			return new ResponseEntity<>(Constants.ERROR, "投资金额不能为空！");
		}
		if (bidRcommendEarnInfo.getFriendLevel() == null || bidRcommendEarnInfo.getFriendLevel() == 0) {
			return new ResponseEntity<>(Constants.ERROR, "推荐好友级别不能为空！");
		}
		if (bidRcommendEarnInfo.getType() == null) {
			return new ResponseEntity<>(Constants.ERROR, "推荐奖类型不能为空！");
		}
		if (bidRcommendEarnInfo.getRegUserId().equals(bidRcommendEarnInfo.getRecommendRegUserId())) {
			return new ResponseEntity<>(Constants.ERROR, "推荐人是自己不发推荐奖！");
		}
		return new ResponseEntity<>(Constants.SUCCESS);
	}

	@Override
	public int findVasBidRecommendEarnCountByBidInvestId(Integer bidInvestId) {
		VasBidRecommendEarn vasBiddRecommendEarn = new VasBidRecommendEarn();
		vasBiddRecommendEarn.setResourceId(bidInvestId);
		return this.vasBidRecommendEarnDao.getTotalCount(vasBiddRecommendEarn);
	}

	@Override
	public ResponseEntity<?> bidRecommendEarnInfoCount(VasBidRecommendEarn vasBiddRecommendEarn) {
		return new ResponseEntity<>(Constants.SUCCESS,
				this.vasBidRecommendEarnDao.bidRecommendEarnInfoCount(vasBiddRecommendEarn));
	}

	@Override
	public ResponseEntity<?> recommendFriendInfo(String basePath, String recommendNo, Integer regUserId) {
		ResponseEntity<?> result = new ResponseEntity<>(Constants.SUCCESS);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("basePath", basePath);
		resultMap.put("recommendNo", recommendNo);
		/**
		 * step 1:生成图片URL
		 */
//		String content = PropertiesHolder.getProperty("recommend_regist_link") + "&recno=" + recommendNo;
		String content = basePath + "wap/inviteRegistStep1.html?inviteNo=" + recommendNo;
		File orginFile = null;
		try {
			orginFile = QRCodeUtil.getInstance().encodeURL(content, 200, "recCode.png");
		} catch (Exception e) {
			logger.error("用户标识: {}, 立即推荐好友,生成二维码失败: ", regUserId, e);
		}
		FileInfo unUploadFile = new FileInfo();
		unUploadFile.setFileContent(orginFile);
		unUploadFile.setName(orginFile.getName());
		/**
		 * step 2:上传阿里云
		 */
		FileInfo fileInfo = OSSLoader.getInstance().bindingUploadFile(unUploadFile).setUseRandomName(true)
				.setAllowUploadType(FileType.EXT_TYPE_IMAGE).setFileState(FileState.UN_UPLOAD)
				.setBucketName(OSSBuckets.HKJF)/* 存放平台桶 */
				.setFilePath(VasConstants.RECOMMEND_CODE_FILE_PATH).doUpload();

		/**
		 * step 3:校验文件状态，确定是否上传成功
		 */
		if (fileInfo.getFileState() == FileState.SAVED) {
			resultMap.put("recUrl", fileInfo.getSaveKey());
		}
		/**
		 * step 4:查询佣金总额
		 */
		VasBidRecommendEarn biddRecommendEarn = new VasBidRecommendEarn();
		biddRecommendEarn.setRecommendRegUserId(regUserId);
		List<Integer> stateList = new ArrayList<Integer>();
        stateList.add(VasConstants.RECOMMEND_EARN_STATE_NOT_REVIEW_TRANT);
        stateList.add(VasConstants.RECOMMEND_EARN_STATE_GRANT_SUCCESS);
        stateList.add(VasConstants.RECOMMEND_EARN_STATE_NOT_REVIEW_WAIT_TRANT);
//		stateList.add(VasConstants.RECOMMEND_EARN_STATE_REVIEW_SUCCESS);
        biddRecommendEarn.setStateList(stateList);
		resultMap.put("sumEarnAmount", this.vasBidRecommendEarnDao.bidRecommendEarnInfoCount(biddRecommendEarn));
		/**
		 * step 5:查询推荐规则
		 */
		VasRebatesRule vasRebatesRule = new VasRebatesRule();
		vasRebatesRule.setState(VasConstants.VAS_RULE_STATE_START);
		vasRebatesRule.setType(VasRuleTypeEnum.QDZRECOMMONEY.getValue());
		List<VasRebatesRule> qdzRuleList =this.vasRebatesRuleDao.findByCondition(vasRebatesRule);
		resultMap.put("qdzRule",(qdzRuleList !=null && qdzRuleList.size()>0)?qdzRuleList.get(0):null);
		vasRebatesRule.setType(VasRuleTypeEnum.RECOMMEND.getValue());
		List<VasRebatesRule> recomRuleList = this.vasRebatesRuleDao.findByCondition(vasRebatesRule);
		if(recomRuleList !=null && recomRuleList.size() > 0){
		    VasRebatesRule vasRule = recomRuleList.get(0);
		    VasRebatesRuleChild ruleChild = this.vasRebatesRuleChildDao.findRuleChildByUserTypeAndRuleId(VasConstants.RECOMMEND_USER_TYPE_COMMON,vasRule.getId());
		    vasRule.setContent(ruleChild.getContent());
		    resultMap.put("recommendRule",vasRule);
		}else{
		    resultMap.put("recommendRule",null);
		}
		result.setParams(resultMap);
		return result;
	}

	@Override
	public ResponseEntity<?> friendInviteCommision(Integer regUserId) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 累计 今日 平台奖励 用户类型
		BigDecimal todayCommission = BigDecimal.ZERO;
		BigDecimal totalCommission = BigDecimal.ZERO;
		VasBidRecommendEarn biddRecommendEarn = new VasBidRecommendEarn();
		biddRecommendEarn.setRecommendRegUserId(regUserId);
		List<Integer> stateList = new ArrayList<Integer>();
		stateList.add(VasConstants.RECOMMEND_EARN_STATE_NOT_REVIEW_TRANT);
		stateList.add(VasConstants.RECOMMEND_EARN_STATE_GRANT_SUCCESS);
//		stateList.add(VasConstants.RECOMMEND_EARN_STATE_NOT_REVIEW_WAIT_TRANT);
//		stateList.add(VasConstants.RECOMMEND_EARN_STATE_REVIEW_SUCCESS);
		biddRecommendEarn.setStateList(stateList);
		Map<String, Object> totalMap = this.vasBidRecommendEarnDao.bidRecommendEarnInfoCount(biddRecommendEarn);
		if (totalMap != null) {
			totalCommission = (BigDecimal) totalMap.get("sumEarnAmount");
		}
		biddRecommendEarn.setGrantTimeBegin(DateUtils.getFirstTimeOfDay());
		biddRecommendEarn.setGrantTimeEnd(DateUtils.getLastTimeOfDay());
		Map<String, Object> todayMap = this.vasBidRecommendEarnDao.bidRecommendEarnInfoCount(biddRecommendEarn);
		if (todayMap != null) {
			todayCommission = (BigDecimal) todayMap.get("sumEarnAmount");
		}
		resultMap.put("todayCommission", todayCommission);
		resultMap.put("totalCommission", totalCommission);
		return new ResponseEntity<>(Constants.SUCCESS, resultMap);
	}

	@Override
	public Pager recommendEarnListForApp(RecommendEarnForAppVo vo, Pager pager) {
		vo.setSortColumns("create_time desc");
		return this.vasBidRecommendEarnDao.findByCondition(vo, pager, VasBidRecommendEarn.class,
				".recommendEarnListForApp");
	}

}
