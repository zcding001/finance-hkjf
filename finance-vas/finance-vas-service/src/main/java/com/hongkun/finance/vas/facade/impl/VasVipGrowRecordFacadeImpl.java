package com.hongkun.finance.vas.facade.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.user.model.vo.UserVO;
import com.hongkun.finance.user.service.DicDataService;
import com.hongkun.finance.user.service.RegUserService;
import com.hongkun.finance.vas.constants.VasConstants;
import com.hongkun.finance.vas.constants.VasVipConstants;
import com.hongkun.finance.vas.facade.VasVipGrowRecordFacade;
import com.hongkun.finance.vas.model.VasVipGradeStandard;
import com.hongkun.finance.vas.model.vo.VasVipGrowRecordVO;
import com.hongkun.finance.vas.service.VasVipGradeStandardService;
import com.hongkun.finance.vas.service.VasVipGrowRecordService;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.AppResultUtil;
import com.yirun.framework.core.utils.pager.Pager;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * @Description : 会员成长值记录facade接口实现类
 * @Project : framework
 * @Program Name  : com.hongkun.finance.vas.facade.impl.VasVipGrowRecordFacadeImpl
 * @Author : pengwu@hongkun.com.cn 吴鹏
 */
@Service
public class VasVipGrowRecordFacadeImpl implements VasVipGrowRecordFacade{

    @Reference
    private VasVipGrowRecordService vipRecordService;
    @Reference
    private VasVipGradeStandardService vipStandardService;
    @Reference
    private RegUserService userService;
	@Reference
	DicDataService dicDataService;

    @Override
    public ResponseEntity getAllUserVipGrowRecordList(VasVipGrowRecordVO vasVipGrowRecordVO, Pager pager) {
        /** 用户id集合条件 */
		List<Integer> userIds = null;
		/** 该等级最小成长值 */
		Integer growthValMin = null;
		/** 该等级最大成长值 */
		Integer growthValMax = null;
		// 若用户相关过滤条件存在
		if (StringUtils.isNotBlank(vasVipGrowRecordVO.getUserName()) || vasVipGrowRecordVO.getUserTel() != null) {
			UserVO userVO = new UserVO();
			userVO.setLogin(vasVipGrowRecordVO.getUserTel());
			userVO.setRealName(vasVipGrowRecordVO.getUserName());
			userIds = userService.findUserIdsByUserVO(userVO);
			if (userIds == null || userIds.size() == 0){
				Pager result = new Pager();
				result.setData(new ArrayList<>());
				return new ResponseEntity(SUCCESS,result);
			}
		}
		// 若传递会员等级值，获取该等级范围
		int errorFlag = -999;
		if (vasVipGrowRecordVO.getLevel() != null && !vasVipGrowRecordVO.getLevel().equals(errorFlag)) {
			VasVipGradeStandard standard = vipStandardService
					.findVasVipGradeStandardByLevel(vasVipGrowRecordVO.getLevel());
			if (standard != null) {
				growthValMin = standard.getGrowthValMin();
				growthValMax = standard.getGrowthValMax();
			}
		}
		Pager resultPager = vipRecordService.findVasVipGrowRecordList(userIds, growthValMin, growthValMax, pager);

		// 组装数据
		resultPager.getData().stream().forEach((record) -> {
			VasVipGrowRecordVO recordVo = (VasVipGrowRecordVO) record;
			UserVO userVO = userService.findRegUserTelAndRealNameById(recordVo.getRegUserId());
			if (userVO != null){
				// 设置用户手机号
				recordVo.setUserTel(userVO.getLogin());
				// 设置用户名称
				recordVo.setUserName(userVO.getRealName());
			}
			// 设置用户等级
			if (vasVipGrowRecordVO.getLevel() != null && !vasVipGrowRecordVO.getLevel().equals(errorFlag)) {
				recordVo.setLevel(vasVipGrowRecordVO.getLevel());
			} else {
				recordVo.setLevel(vipStandardService.findLevelByGrowValue(recordVo.getCurrentGrowValue()));
			}
		});

		ResponseEntity result = new ResponseEntity(SUCCESS,resultPager);
		return result;
    }

	@Override
	public Map<String, Object> getUserVipInfo(Integer id) {
		Map<String,Object> result = vipRecordService.findUserGrowValueAndLevel(id);
		result.put("levelName",dicDataService.findNameByValue(VasConstants.VAS, VasVipConstants
                .VAS_VIP_GRADE_NAME,(int) result.get("level")));
		//离下个会员等级描述信息为
		int nextLevel = (int)result.get("level") + 1;
		//①获取会员等级信息
		List<VasVipGradeStandard> list = vipStandardService.getAllGradeList();
		if (list == null || list.size() == 0){
			return AppResultUtil.errorOfMsg("会员等级配置信息异常!");
		}
		//②获取其中等级最大值
		int maxLevel = list.stream()
				.max(Comparator.comparing(VasVipGradeStandard::getLevel))
				.get().getLevel();
		//③判断是否为最大等级
		if (maxLevel >= nextLevel){
			//获取离下个等级成长值差额
			int growValue = list.stream()
					.filter(vasVipGradeStandard -> vasVipGradeStandard.getLevel() == nextLevel)
					.findFirst().get().getGrowthValMin();
			result.put("nextLevelGrowValue",growValue);
			result.put("nextLevel",nextLevel);
			result.put("isMaxLevel",0);
		}else {
			result.put("isMaxLevel",1);
		}

		return AppResultUtil.successOf(result);
	}
}
