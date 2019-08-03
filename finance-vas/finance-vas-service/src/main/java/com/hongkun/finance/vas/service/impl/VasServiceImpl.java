package com.hongkun.finance.vas.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.vas.constants.VasConstants;
import com.hongkun.finance.vas.dao.*;
import com.hongkun.finance.vas.enums.VasRuleTypeEnum;
import com.hongkun.finance.vas.model.VasRebatesRule;
import com.hongkun.finance.vas.model.VasRedpacketInfo;
import com.hongkun.finance.vas.model.VasSimRecord;
import com.hongkun.finance.vas.service.VasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description : 增值服务实现类
 * @Project : framework
 * @Program Name : com.hongkun.finance.vas.service.impl.VasServiceImpl
 * @Author : pengwu@hongkun.com.cn 吴鹏
 */
@Service
public class VasServiceImpl implements VasService {

	@Autowired
	VasRebatesRuleDao vasRebatesRuleDao;

	@Autowired
	VasVipGrowRecordDao vasVipGrowRecordDao;

	@Autowired
	VasVipGradeStandardDao vasVipGradeStandardDao;

	@Autowired
	VasRedpacketInfoDao vasRedpacketInfoDao;

	@Autowired
	VasGoldRuleDao vasGoldRuleDao;

	@Autowired
	VasSimRecordDao vasSimRecordDao;

	@Autowired
	VasCouponDetailDao vasCouponDetailDao;

	@Override
	public Map<String, Object> getVasInfo(int regUserId, String items) {
		Assert.notNull(items, "查询items不能为null");
		Map<String, Object> result = new HashMap<>();
		if (items.length() > 0 && "1".equals(items.substring(0, 1))) {
			// 获取钱袋子规则
			int qdzEnable = 0;// 默认未启动
			String key = String.valueOf(VasRuleTypeEnum.QDZ.getValue()) + VasConstants.VAS_RULE_STATE_START;
			VasRebatesRule vasRebatesRule = vasRebatesRuleDao.findVasRebatesRule(key, VasRuleTypeEnum.QDZ.getValue(),
					VasConstants.VAS_RULE_STATE_START);
			if (vasRebatesRule != null) {
				qdzEnable = 1;// 存在规则，则返回钱袋子规则已启动
			}
            // 钱袋子规则开关
			result.put("qdzEnable", qdzEnable);
		}
		if (items.length() > 1 && "1".equals(items.substring(1, 2))) {
			// 获取用户等级
			int growValue = vasVipGrowRecordDao.findUserCurrentGrowValue(regUserId);
			int level = vasVipGradeStandardDao.findLevelByGrowValue(growValue);
			result.put("growValue", growValue);// 用户会员成长值
			result.put("level", level);// 用户会员等级
		}
		if (items.length() > 2 && "1".equals(items.substring(2, 3))) {
			// 获取用户可领取红包数量
			VasRedpacketInfo condition = new VasRedpacketInfo();
			condition.setAcceptorUserId(regUserId);
			condition.setState(VasConstants.REDPACKET_STATE_UNRECEIVED);
			int redPacketCount = vasRedpacketInfoDao.getRedPacketInfoListCount(condition);
			result.put("redPacketCount", redPacketCount);// 用户可领取红包数量
		}
		if (items.length() > 3 && "1".equals(items.substring(3, 4))) {
			// 获取用户体验金金额
			VasSimRecord vasSimRecord = new VasSimRecord();
			vasSimRecord.setRegUserId(regUserId);
			vasSimRecord.setState(0);
			BigDecimal money = vasSimRecordDao.findSimSumMoney(vasSimRecord);
			result.put("simSum", money);// 用户可使用体验金
		}
		if (items.length() > 4 && "1".equals(items.substring(4, 5))) {
			// 查询投资红包&加息券&好友券数量
			List<Map<String,Object>> list = this.vasCouponDetailDao.getUserCouponDetailCountGroupByType(regUserId);
			Map<String, Long> m = new HashMap<>();
			list.stream().forEach((map)->{
				m.put("couponType" + map.get("type").toString(),(long)map.get("count"));
			});
			m.put("couponCount", m.values().stream().mapToLong(o -> o).sum());
			result.putAll(m);
		}
		return result;
	}
}
