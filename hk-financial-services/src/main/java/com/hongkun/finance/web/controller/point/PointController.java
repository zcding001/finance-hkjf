package com.hongkun.finance.web.controller.point;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.point.constants.PointConstants;
import com.hongkun.finance.point.facade.PointRecordFacade;
import com.hongkun.finance.point.model.PointRecord;
import com.hongkun.finance.point.service.PointAccountService;
import com.hongkun.finance.point.service.PointRecordService;
import com.hongkun.finance.user.constants.UserConstants;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.model.RegUserDetail;
import com.hongkun.finance.user.service.RegUserDetailService;
import com.hongkun.finance.user.service.RegUserService;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;

import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * @Description : 处理积分相关controller
 * @Project : framework
 * @Program Name  : com.hongkun.finance.web.controller.point.PointController
 * @Author : pengwu@hongkun.com.cn 吴鹏
 */
@Controller
@RequestMapping("/pointController")
public class PointController {

    @Reference
    private PointRecordService pointRecordService;
    @Reference
    private PointAccountService pointAccountService;
    @Reference
    private PointRecordFacade pointRecordFacade;
    @Reference
    private RegUserDetailService regUserDetailService;
    @Reference
    private RegUserService regUserService;
    /**
     *  @Description    : 用户积分转赠
     *  @Method_Name    : userPointTransfer
     *  @param point             积分
     *  @param acceptUsers      积分接收人
     *  @return
     *  @Creation Date  : 2017年12月28日 上午10:55:55
     *  @Author         : pengwu@hongkun.com.cn
     */
    @RequestMapping("/userPointTransfer")
    @ResponseBody
    public ResponseEntity userPointTransfer (@RequestParam int point,@RequestParam List<Integer> acceptUsers){
        //判断积分转赠用户是否实名
        RegUser regUser = BaseUtil.getLoginUser();
        if (regUser.getIdentify() == 0){
            return new ResponseEntity(UserConstants.NO_IDENTIFY,"为了保障您的财产安全，请您先实名后再进行积分转赠！");
        }
        RegUserDetail regUserDetail = BaseUtil.getRegUserDetail(() -> regUserDetailService.findRegUserDetailByRegUserId(regUser.getId()));
        return pointRecordFacade.userPointTransfer(regUser.getId(),point,acceptUsers,regUserDetail.getRealName());
    }
    /**
     *  @Description    : 获取当前用户的积分和积分转赠利率
     *  @Method_Name    : getUserPointAndRate
     *  @return
     *  @Creation Date  : 2017年12月28日 下午17:57:55
     *  @Author         : pengwu@hongkun.com.cn
     */
    @RequestMapping("/getUserPointAndRate")
    @ResponseBody
    public ResponseEntity getUserPointAndRate(){
        RegUser regUser = BaseUtil.getLoginUser();
        return pointAccountService.getUserPointAndRate(regUser.getId());
    }
    /**
     *  @Description    : 获取用户积分记录信息
     *  @Method_Name    : getUserPointRecord
     *  @return
     *  @Creation Date  : 2018年01月02日 下午16:57:55
     *  @Author         : pengwu@hongkun.com.cn
     */
    @RequestMapping("/getUserPointRecord")
    @ResponseBody
    public ResponseEntity getUserPointRecord(PointRecord pointRecord,Pager pager){
        RegUser regUser = BaseUtil.getLoginUser();
        pointRecord.setRegUserId(regUser.getId());
        pointRecord.setSortColumns("create_time desc");
        pointRecord.setStateList(Arrays.asList(PointConstants.POINT_STATE_CONFIRMED));
        return new ResponseEntity(SUCCESS,pointRecordService.findPointRecordList(pointRecord,pager));
    }
    /**
     *  @Description    : 收款统计（积分商户）
     *  @Method_Name    : getCollectionPointStatistics
     *  @return
     *  @Creation Date  : 2018-12-11 11:29:31
     *  @Author         : binliang@hongkun.com.cn 梁彬
     */
    @RequestMapping("/getCollectionPointStatistics")
    @ResponseBody
    public ResponseEntity<?> getCollectionPointStatistics(PointRecord pointRecord,Pager pager){
        RegUser regUser = BaseUtil.getLoginUser();
        RegUserDetail regUserDetail = BaseUtil.getRegUserDetail(() -> regUserDetailService.findRegUserDetailByRegUserId(regUser.getId()));
        pointRecord.setBusinessId(regUser.getId());
        pointRecord.setType(PointConstants.POINT_TYPE_PAY);
        if(StringUtils.isNotBlank(pointRecord.getBuyersTel())){
        	RegUser buyersUser = this.regUserService.findRegUserByLogin(Long.decode(pointRecord.getBuyersTel()));
        	pointRecord.setRegUserId(buyersUser.getId());
        }
        pointRecord.setSortColumns("create_time desc");
        pointRecord.setStateList(Arrays.asList(PointConstants.POINT_STATE_CONFIRMED));
        pager = pointRecordService.findPointRecordList(pointRecord,pager);
        if (!(pager == null || pager.getData() == null || pager.getData().size() == 0)) {
			pager.getData().stream().forEach((e) -> {
				PointRecord temp = (PointRecord)e;
				RegUser buyersUserTemp = this.regUserService.findRegUserById(temp.getRegUserId());
				temp.setBussinessName(regUserDetail.getRealName());
				temp.setBussinessTel(regUser.getLogin().toString());
				temp.setBuyersTel(buyersUserTemp.getLogin().toString());
			});
        }
        return new ResponseEntity<>(Constants.SUCCESS, pager);
    }
}
