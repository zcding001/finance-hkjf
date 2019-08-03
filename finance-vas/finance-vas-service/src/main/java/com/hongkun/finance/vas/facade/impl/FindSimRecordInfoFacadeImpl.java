package com.hongkun.finance.vas.facade.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.invest.constants.InvestConstants;
import com.hongkun.finance.invest.model.vo.BidInfoVO;
import com.hongkun.finance.invest.service.BidInfoService;
import com.hongkun.finance.invest.util.CalcInterestUtil;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.model.vo.UserVO;
import com.hongkun.finance.user.service.RegUserService;
import com.hongkun.finance.user.utils.BaseUtil;
import com.hongkun.finance.user.utils.ValidateUtil;
import com.hongkun.finance.vas.constants.VasConstants;
import com.hongkun.finance.vas.facade.FindSimRecordInfoFacade;
import com.hongkun.finance.vas.model.SimGoldVo;
import com.hongkun.finance.vas.model.VasGoldRule;
import com.hongkun.finance.vas.model.VasSimRecord;
import com.hongkun.finance.vas.service.VasGoldRuleService;
import com.hongkun.finance.vas.service.VasSimRecordService;
import com.hongkun.finance.vas.utils.ClassReflection;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.CommonUtils;
import com.yirun.framework.core.utils.DateUtils;
import com.yirun.framework.core.utils.ExcelUtil;
import com.yirun.framework.core.utils.json.JsonUtils;
import com.yirun.framework.core.utils.pager.Pager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.*;

import static com.yirun.framework.core.commons.Constants.ERROR;
import static com.yirun.framework.core.commons.Constants.SUCCESS;

@Service
public class FindSimRecordInfoFacadeImpl implements FindSimRecordInfoFacade {
	private static final Logger logger = LoggerFactory.getLogger(FindSimRecordInfoFacadeImpl.class);
	@Reference
	private RegUserService regUserService;
	@Autowired
	private VasSimRecordService vasSimRecordService;
	@Reference
	private BidInfoService bidInfoService;
	@Reference
	private VasGoldRuleService vasGoldRuleService;

	@Override
	public ResponseEntity<?> findSimRecordInfo(SimGoldVo simGoldVo, Pager pager) {
		try {
			// 查询条件的MAP
			Map<String, Object> biddRecommendEarnMap = new HashMap<String, Object>();
			// 判断是否按手机号查询
			if (simGoldVo.getLogin() != null || StringUtils.isNotBlank(simGoldVo.getRealName())) {
				UserVO userVo = new UserVO();
				userVo.setRealName(simGoldVo.getRealName());
				userVo.setLogin(simGoldVo.getLogin());
				List<Integer> regUserIdList = regUserService.findUserIdsByUserVO(userVo);
				if (regUserIdList == null || regUserIdList.size() == 0) {
					regUserIdList = new ArrayList<Integer>();
					regUserIdList.add(0);
				}
				biddRecommendEarnMap.put("regUserIdList", regUserIdList);
			}
			// 如何体验金ID不为空，则查询条件加入体验金记录ID
			if (simGoldVo.getId() != null) {
				biddRecommendEarnMap.put("vasSimGoldId", simGoldVo.getId());
			}
			// 将查询条件放入MAP中，封装按条件查询
			biddRecommendEarnMap.put("createTimeBegin", simGoldVo.getCreateTimeBegin());
			biddRecommendEarnMap.put("createTimeEnd", simGoldVo.getCreateTimeEnd());
			biddRecommendEarnMap.put("sortColumns", "create_time desc");
			biddRecommendEarnMap.put("state", simGoldVo.getState());
			// 按条件查询体验金记录
			Pager pagerInfo = vasSimRecordService.findVasSimRecordListByInfo(biddRecommendEarnMap, pager);
			return new ResponseEntity<>(SUCCESS, buildSimGoldVo(pagerInfo));
		} catch (Exception e) {
			logger.error("用户体验金查询异常: ", e);
			return new ResponseEntity<>(ERROR, "查询异常");
		}
	}

	/**
	 * @Description :封装返回给页面的VO对象
	 * @Method_Name : buildSimGoldVo;
	 * @param result
	 * @return
	 * @throws Exception
	 * @return : Pager;
	 * @Creation Date : 2017年6月28日 下午3:27:48;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	private Pager buildSimGoldVo(Pager result) throws Exception {
		List<SimGoldVo> simGoldVoList = new ArrayList<SimGoldVo>();
		if (BaseUtil.resultPageHasNoData(result)) {
			return result;
		}
		List<VasSimRecord> vasSimRecordList = (List<VasSimRecord>) result.getData();
		// 遍历体验金记录，组装页面VO对象
		for (VasSimRecord vasSimRecord : vasSimRecordList) {
			// 查询用户信息
			UserVO userVO = regUserService.findUserWithDetailById(vasSimRecord.getRegUserId());
			SimGoldVo simGoldVo = new SimGoldVo();
			// 两个对象之间相同Bean属性赋值
			ClassReflection.reflectionAttr(vasSimRecord, simGoldVo);
			simGoldVo.setLogin(userVO.getLogin());
			simGoldVo.setRealName(userVO.getRealName());
			simGoldVoList.add(simGoldVo);
		}
		result.setData(simGoldVoList);
		return result;
	}

	@Override
	public ResponseEntity<?> findSimGoldCountInfo() {
		// 查询产品类型是体验金的，标的状态是投标中的，满标待审核的，待放款的，放款中的标的信息
		BidInfoVO bidInfoVO = new BidInfoVO();
		bidInfoVO.setProductType(InvestConstants.BID_PRODUCT_EXPERIENCE);
		List<Integer> states = new ArrayList<Integer>();
		// 投标中
		states.add(InvestConstants.BID_STATE_WAIT_INVEST);
		// 满标待审核
		states.add(InvestConstants.BID_STATE_WAIT_AUDIT);
		// 待放款
		states.add(InvestConstants.BID_STATE_WAIT_LOAN);
		// 放款中
		states.add(InvestConstants.BID_STATE_WAIT_REPAY);
		bidInfoVO.setStates(states);
		List<BidInfoVO> bidInfoList = bidInfoService.findBidInfoVoList(bidInfoVO);
		// 遍历统计待付体验金标的利息总金额
		BigDecimal sumInterestTotalMoney = BigDecimal.ZERO;// 待付利息总金额
		if (bidInfoList != null && bidInfoList.size() > 0) {
			for (BidInfoVO bidInfo : bidInfoList) {
			    sumInterestTotalMoney = sumInterestTotalMoney.add(CalcInterestUtil.calcInterest(bidInfo.getTermUnit(), bidInfo.getTermValue(),
						bidInfo.getTotalAmount(), bidInfo.getInterestRate()));
			}
		}
		// 统计体验金发放信息
		ResponseEntity<?> result = vasSimRecordService.findSimGoldCountInfo();
		Map<String, Object> resultSimGoldCountMap = (Map<String, Object>) result.getResMsg();
		resultSimGoldCountMap.put("sumInterestTotalMoney", sumInterestTotalMoney);
		resultSimGoldCountMap.put("sumNoInvestSimMoney",
				new BigDecimal(resultSimGoldCountMap.get("sumSimGrantToTalMoney") == null ? "0"
						: resultSimGoldCountMap.get("sumSimGrantToTalMoney").toString()).subtract(
								new BigDecimal(resultSimGoldCountMap.get("sumInvestSimTotalMoney") == null ? "0"
										: resultSimGoldCountMap.get("sumInvestSimTotalMoney").toString())));
		return new ResponseEntity<>(SUCCESS, resultSimGoldCountMap);
	}

	@Override
	public ResponseEntity<?> simGoldimport(String filePath) {
		List<List<String>> dataList = ExcelUtil.getDataList(filePath);
		if (CommonUtils.isEmpty(dataList)) {
			return new ResponseEntity<>(Constants.ERROR, "未找到有效的数据");
		}
		// 根据类型和状态查询体验金当前有效规则
		VasGoldRule vasGoldRule = this.vasGoldRuleService.findVasGoldRuleByTypeAndState(0,VasConstants.VAS_RULE_STATE_START);
		//有效期默认14天
		int day = 14;
		if (vasGoldRule != null) {
			day = vasGoldRule.getPeriod();
		}
		// 校验手机号和金额的合法性，不合法的记录下来,已注册金额合法的信息插入进去
		List<VasSimRecord> vasSimRecordList = new ArrayList<>();
		StringBuffer errorMsg = new StringBuffer();
		int errorNum = 0;
		//遍历结果不包含标题列 (手机号和金额)
		for (int i = 0; i < dataList.size(); i++) {
			List<String> l = dataList.get(i);
			String tel = l.get(0);
			ResponseEntity<?> result = ValidateUtil.validateLogin(tel);
			if (result.getResStatus() == Constants.ERROR){
				errorMsg.append(tel).append("[第").append(i+2).append("行]手机号格式有误；<br/>");
				errorNum++;
				continue;
			}
			RegUser user = regUserService.findRegUserByLogin(Long.valueOf(tel));
			if(user == null){
				errorMsg.append(tel).append("[第").append(i+2).append("行]手机号未注册；<br/>");
				errorNum++;
				continue;
			}
			double money = 0d;
			try {
				money = Double.valueOf(l.get(1));
				if(money <= 0 || money%100 != 0 ){
					errorMsg.append(tel).append("[第").append(i+2).append("行]金额必须为100整数倍；<br/>");
					errorNum++;
					continue;
				}
			} catch (Exception e) {
				e.printStackTrace();
				errorMsg.append(tel).append("[第").append(i+2).append("行]金额不合法；<br/>");
				errorNum++;
				continue;
			}
			//校验通过的构建体验金对象
			VasSimRecord vasSimRecord = new VasSimRecord();
			vasSimRecord.setRegUserId(user.getId());
			vasSimRecord.setMoney(BigDecimal.valueOf(money));
			vasSimRecord.setSource(vasGoldRule == null ? -1 : vasGoldRule.getId());
			vasSimRecord.setExpireTime(DateUtils.addDays(new Date(), day));
			vasSimRecordList.add(vasSimRecord);
		}
		if(!CommonUtils.isEmpty(vasSimRecordList)){
			logger.info("保存体验金记录集合: vasSimRecordList: {}", JsonUtils.toJson(vasSimRecordList));
			this.vasSimRecordService.insertVasSimRecordBatch(vasSimRecordList);
		}
		String msg = errorMsg.toString();
		if("".equals(msg)){
			msg = "excel全部导入成功!";
		}else{
			errorMsg.append("失败共计:" + errorNum + "个，其余导入成功!");
			msg = errorMsg.toString();
		}
		return new ResponseEntity<>(SUCCESS, msg);
	}

}
