package com.hongkun.finance.web.controller.bid;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.hongkun.finance.contract.constants.ContractConstants;
import com.hongkun.finance.invest.constants.InvestConstants;
import com.hongkun.finance.invest.facade.BidInfoFacade;
import com.hongkun.finance.invest.model.dto.InvestPointDTO;
import com.hongkun.finance.invest.model.vo.BidInfoVO;
import com.hongkun.finance.invest.service.BidInfoService;
import com.hongkun.finance.invest.service.BidProductService;
import com.hongkun.finance.loan.constants.RepayConstants;
import com.hongkun.finance.loan.facade.MakeLoanFacade;
import com.hongkun.finance.point.constants.PointConstants;
import com.hongkun.finance.user.constants.UserConstants;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.service.RegUserService;
import com.hongkun.finance.user.support.security.OperationTypeEnum;
import com.hongkun.finance.user.support.security.annotation.ActionLog;
import com.hongkun.finance.user.support.security.component.annotation.AskForPermission;
import com.hongkun.finance.user.utils.BaseUtil;
import com.hongkun.finance.vas.constants.VasConstants;
import com.hongkun.finance.vas.model.RecommendEarnBuild;
import com.hongkun.finance.vas.model.vo.VasVipGrowRecordMqVO;
import com.hongkun.finance.vas.utils.VipGrowRecordUtil;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.CommonUtils;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.jms.JmsService;
import com.yirun.framework.jms.enums.DestinationType;
import com.yirun.framework.jms.enums.JmsMessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static com.yirun.framework.core.commons.Constants.ERROR;

/**
 * @Description   : 后台借款相关管理控制接口，包含借款、放款等业务
 * @Project       : hk-management-services
 * @Program Name  : com.hongkun.finance.web.controller.bid.LoanController.java
 * @Author        : xuhuiliu@hongkun.com.cn 劉旭輝
 */
@Controller
@RequestMapping("/loanController")
@AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
public class LoanController {

    private final Logger logger = LoggerFactory.getLogger(LoanController.class);

    @Reference
    private MakeLoanFacade makeLoanFacade;
    @Reference
    private BidInfoService bidInfoService;
    @Reference
    private BidInfoFacade bidInfoFacade;
    @Reference
    private BidProductService bidProductService;
    @Autowired
    private JmsService jmsService;
    @Reference
    private RegUserService regUserService;

    /**
     * @Description : 放款
     * @Method_Name : makeLoans
     * @param bidInfoId
     * @return
     * @return : ResponseEntity<?>
     * @Creation Date : 2017年6月21日 上午9:08:04
     * @Author : xuhuiliu@hongkun.com.cn 刘旭辉
     */
    @RequestMapping("/makeLoans")
    @ResponseBody
	@ActionLog(msg="放款, 标的id: {args[0]}")
    public ResponseEntity<?> makeLoans(Integer bidInfoId) {
    	ResponseEntity<?> result = null;
		if (!CommonUtils.gtZero(bidInfoId)) {
			return new ResponseEntity<>(ERROR, "未指定需要放款的标的");
		}
		RegUser regUser = BaseUtil.getRegUser(() -> this.regUserService.findRegUserById(BaseUtil.getLoginUser().getId()));
		result = makeLoanFacade.makeLoans(bidInfoId,regUser.getId());
		if(!BaseUtil.error(result)){
			//发送消息处理 成长值、好友推荐、合同 
			try {
				sendMqForMakeLoan(result, bidInfoId);	
			} catch (Exception e) {
				logger.error("makeLoans, 放款, 发送消息异常", e);
			}
			
		}
        return result;
    }
    
    /**
     *  @Description    : 放款--发送消息处理 成长值、好友推荐、合同
     *  @Method_Name    : sendMqForMakeLoan
     *  @param result
     *  @param bidInfoId
     *  @return         : void
     *  @Creation Date  : 2017年10月27日 下午1:43:39 
     *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
     */
    @SuppressWarnings("unchecked")
    private void  sendMqForMakeLoan(ResponseEntity<?> result,Integer bidInfoId){
    		logger.info("sendMqForMakeLoan, 放款发送消息, bidInfoId: {}",bidInfoId);
			RecommendEarnBuild recommend = (RecommendEarnBuild) result.getParams().get("recommend");
			List<VasVipGrowRecordMqVO> vasVipList = 
					(List<VasVipGrowRecordMqVO>) result.getParams().get("vasVipList");
			InvestPointDTO investPointDTO = (InvestPointDTO) result.getParams().get("investPointDTO");
			List<Integer> invests = (List<Integer>) result.getParams().get("bidInvestIds");
			List<Integer> regUserIds = (List<Integer>) result.getParams().get("regUserIds");
			//放款发送消息（生成还款/回款计划）
			jmsService.sendMsg(RepayConstants.MQ_QUEUE_REPAYANDRECEIPTPLAN, DestinationType.QUEUE, 
					bidInfoId,JmsMessageType.OBJECT);
			if(recommend!=null){
				logger.info("sendMqForMakeLoan, 放款发送消息(推荐奖), bidInfoId：{}, recommend: {}",bidInfoId,JSON.toJSON(recommend));
				jmsService.sendMsg(VasConstants.MQ_QUEUE_RECOMMEND_EARN_RECORD, DestinationType.QUEUE, 
						recommend,JmsMessageType.OBJECT);
			}
			if(CommonUtils.isNotEmpty(vasVipList)){
				logger.info("sendMqForMakeLoan, 放款发送消息(成长值), bidInfoId：{}, vasVipList: {}",bidInfoId,JSON.toJSON(vasVipList));
				VipGrowRecordUtil.sendVipGrowRecordListToQueue(vasVipList);
			}
			if(investPointDTO !=null){
				logger.info("sendMqForMakeLoan, 放款发送消息(积分), bidInfoId：{}, investPointDTO: {}",bidInfoId,investPointDTO.toString());
				jmsService.sendMsg(PointConstants.MQ_QUEUE_POINT_RECORD, DestinationType.QUEUE, investPointDTO,
						JmsMessageType.OBJECT);
			}
			if(CommonUtils.isNotEmpty(invests)){
				logger.info("sendMqForMakeLoan, 放款发送消息(合同), bidInfoId：{}, 投资记录invests: {}",bidInfoId,JSON.toJSON(invests));
				jmsService.sendMsg(ContractConstants.MQ_QUEUE_CONINFO, DestinationType.QUEUE,invests,
						JmsMessageType.OBJECT);
			}
			if(CommonUtils.isNotEmpty(regUserIds)){
				logger.info("sendMqForMakeLoan, 放款发送消息(好友关系), bidInfoId：{}, regUserIds: {}",bidInfoId,JSON.toJSON(regUserIds));
				jmsService.sendMsg(UserConstants.MQ_QUEUE_USER_FRIEND_INVEST, DestinationType.QUEUE,regUserIds,
						JmsMessageType.OBJECT);
			}
    }
    /**
     *  @Description    : 查询待放款标的列表
     *  @Method_Name    : loanBidInfoList
     *  @param vo 查询条件
     *  @param pager     分页信息
     *  @return
     *  @return         : ResponseEntity<?>
     *  @Creation Date  : 2017年8月28日 下午1:46:53 
     *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
     */
    @RequestMapping("/loanBidInfoList")
    @ResponseBody
    public ResponseEntity<?> loanBidInfoList( BidInfoVO vo,
    		 Pager pager){
    	vo.setState(InvestConstants.BID_STATE_WAIT_LOAN);
    	return this.bidInfoFacade.findBidInfoDetailVoList(pager, vo);
    }
}
