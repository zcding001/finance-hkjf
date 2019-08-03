package com.hongkun.finance.web.controller.vas;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.vas.facade.VasVipGrowRecordFacade;
import com.hongkun.finance.vas.model.vo.VasVipGrowRecordVO;
import com.hongkun.finance.vas.service.VasVipGrowRecordService;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description : 会员等级成长记录查询
 * @Project : framework
 * @Program Name : com.hongkun.finance.web.controller.vas.VipGrowRecordController
 * @Author : pengwu@hongkun.com.cn 吴鹏
 */
@Controller
@RequestMapping("/vipGrowRecordController")
public class VipGrowRecordController {

	@Reference
	private VasVipGrowRecordService vipRecordService;
	@Reference
	private VasVipGrowRecordFacade vasVipGrowRecordFacade;

	/**
	 * @Description : 获取所有用户成长值记录
	 * @Method_Name : vipGrowRecordList
	 * @param vasVipGrowRecordVO
	 * @param pager
	 * @return : ResponseEntity
	 * @Creation Date : 2017年7月03日 下午17:40:50
	 * @Author : pengwu@hongkun.com.cn
	 */
	@RequestMapping("/vipGrowRecordList")
	@ResponseBody
	public ResponseEntity vipGrowRecordList(VasVipGrowRecordVO vasVipGrowRecordVO, Pager pager) {
		return vasVipGrowRecordFacade.getAllUserVipGrowRecordList(vasVipGrowRecordVO,pager);
	}
	/**
	 * @Description : 获取用户成长值记录
	 * @Method_Name : userGrowRecordDetail
	 * @param userId
	 * @param pager
	 * @return : ResponseEntity
	 * @Creation Date : 2017年7月03日 下午17:40:50
	 * @Author : pengwu@hongkun.com.cn
	 */
	@RequestMapping("/userGrowRecordDetail")
	@ResponseBody
	public ResponseEntity userGrowRecordDetail(int userId,Pager pager) {
		return vipRecordService.getUserGrowRecordDetail(userId,pager);
	}

}
