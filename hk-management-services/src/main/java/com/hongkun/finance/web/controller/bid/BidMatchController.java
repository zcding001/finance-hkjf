package com.hongkun.finance.web.controller.bid;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.contract.constants.ContractConstants;
import com.hongkun.finance.contract.model.ConInfoSelfGenerateDTO;
import com.hongkun.finance.invest.facade.BidInvestFacade;
import com.hongkun.finance.invest.facade.BidMatchFacade;
import com.hongkun.finance.invest.model.BidInfo;
import com.hongkun.finance.invest.model.vo.BidInfoVO;
import com.hongkun.finance.invest.model.vo.BidMatchVo;
import com.hongkun.finance.invest.service.BidInvestService;
import com.hongkun.finance.invest.service.BidMatchService;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.service.DicDataService;
import com.hongkun.finance.user.support.security.annotation.ActionLog;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.annotation.Token;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.CommonUtils;
import com.yirun.framework.core.utils.CompareUtil;
import com.yirun.framework.core.utils.DateUtils;
import com.yirun.framework.core.utils.ExcelUtil;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.jms.JmsService;
import com.yirun.framework.jms.enums.DestinationType;
import com.yirun.framework.jms.enums.JmsMessageType;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.hongkun.finance.invest.constants.InvestConstants.*;
import static com.yirun.framework.core.commons.Constants.ERROR;
import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * @Description   : 标的匹配Controller
 * @Project       : management-financial-services
 * @Program Name  : com.hongkun.finance.web.controller.bid.BidMatchController.java
 * @Author        : xuhuiliu@hongkun.com.cn 劉旭輝
 */
@Controller
@RequestMapping("/bidMatchController")
public class BidMatchController {
	@Reference
	private BidMatchFacade bidMatchFacade;
	@Reference
	private BidInvestFacade bidInvestFacade;
	@Reference
	private BidInvestService bidInvestService;
	@Reference
	private BidMatchService bidMatchService;
	@Autowired
	private JmsService jmsService;
	@Reference
	private DicDataService dicDataService;

	private static final Logger logger = LoggerFactory.getLogger(BidMatchController.class.getName());

	/**
	 *  @Description    : 查询优选标/散标的列表
	 *  @Method_Name    : matchBidInfoList
	 *  @param title    标的标题
	 *  @param pager    分页信息
	 *  @param bidType  标的类型 1-优选（一个优选匹配多个散标入口），2-散标（一个散标匹配多个优选入口）
	 *  @return
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年7月19日 下午3:59:14 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	@RequestMapping("/matchBidInfoList")
	@ResponseBody
	public ResponseEntity<?> matchBidInfoList(String title,Integer bidType,Pager pager){
		bidType = bidType==null?MATCH_BID_TYPE_GOOD:bidType;
		if(bidType!= MATCH_BID_TYPE_GOOD&&bidType!= MATCH_BID_TYPE_COMMON){
			return new ResponseEntity<>(Constants.ERROR,"标的类型有误");
		}
		return bidMatchFacade.matchBidInfoList(title,bidType,pager);
	}
	/**
	*  @Description    ：待匹配列表导出excel
	*  @Method_Name    ：exportExcelMatchBidList
	*  @param bidType
	*  @param response
	*  @return void
	*  @Creation Date  ：2018/6/12
	*  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
	*/
	@RequestMapping("/exportExcelMatchBidList")
	@ResponseBody
	public void exportExcelMatchBidList(Integer bidType,HttpServletResponse response){
		try {
			List<BidInfoVO> resultList =  bidMatchFacade.findMatchBidListForExport(bidType);
			if(CommonUtils.isNotEmpty(resultList)){
				String now = DateUtils.getCurrentDate("yyyy-MM-dd");
				String fileName = "标的匹配列表"+now;
				String sheetName = now;
				ExcelUtil.exportExcel(fileName,sheetName,resultList,initExportMatchBidListParams(),65535,response);
			}
		} catch (Exception e) {
			logger.info("exportExcelMatchBidList 导出标的匹配列表异常, bidType: {},异常信息:\n", bidType, e);
		}
	}
	/**
	*  @Description    ：初始化导出匹配列表参数
	*  @Method_Name    ：initExportMatchBidListParams
	*  @return java.util.LinkedHashMap<java.lang.String,java.lang.String>
	*  @Creation Date  ：2018/6/12
	*  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
	*/
	private LinkedHashMap<String,String>  initExportMatchBidListParams(){
		LinkedHashMap<String,String> params = new LinkedHashMap<String,String>();
		params.put("title","标的名称");
		params.put("productType","产品类型");
		params.put("totalAmount","金额");
		params.put("interestRate","年化率");
		params.put("termValueStr","期限");
		params.put("matchingDaysStr","待匹配天数");
		params.put("matchingMoney","待匹配金额");
		params.put("matchingDetailsStr","待匹配情况");
		params.put("hasMatch","首次匹配");
		params.put("createTimeStr","创建时间");
		params.put("lendingTimeStr","放款时间");
		params.put("nextMatchDateStr","下次匹配时间");
		return params;
	}
	/**
	*  @Description    ：待匹配标的列表（不带分页）
	*  @Method_Name    ：miniMatchBidInfoList
	*  @param title
	*  @param bidType  标的类型 1-优选 2-散标
	*  @param matchingMoney    待匹配金额，如果是优选，查询出来的散标匹配金额不能高于此金额
	*  @return com.yirun.framework.core.model.ResponseEntity<?>
	*  @Creation Date  ：2018/5/28
	*  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
	*/
	@RequestMapping("/miniMatchBidInfoList")
	@ResponseBody
	public ResponseEntity<?> miniMatchBidInfoList(String title, Integer bidType, BigDecimal matchingMoney){
		bidType = bidType==null?MATCH_BID_TYPE_GOOD:bidType;
		if(bidType!= MATCH_BID_TYPE_GOOD&&bidType!= MATCH_BID_TYPE_COMMON){
			return new ResponseEntity<>(Constants.ERROR,"标的类型有误");
		}
		return bidMatchFacade.miniMatchBidInfoList(title,bidType,matchingMoney);
	}



	/**
	 * 
	 *  @Description    : 待匹配标的详情
	 *  @Method_Name    : matchBidDetail
	 *  @param bidId
	 *  @return
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年9月26日 上午10:20:11 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	@RequestMapping("/matchBidDetail")
	@ResponseBody
	public ResponseEntity<?> matchBidDetail(Integer bidId,Integer bidType){
		return bidMatchFacade.matchBidInfoDetail(bidId,bidType);
	} 
	
	@RequestMapping("/matchedDetails")
	@ResponseBody
	public ResponseEntity<?> matchedDetails(Integer bidMatchId){
		return bidMatchFacade.matchedDetails(bidMatchId);
	} 
	
	/**
	 *  @Description    : 标的匹配查询
	 *  @Method_Name    : selectMatchBidInfo
	 *  @param title
	 *  @param pager
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年7月20日 上午10:58:44 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	@RequestMapping("/allMatchBidInfoList")
	@ResponseBody
	public ResponseEntity<?> allMatchBidInfoList(String title,Pager pager){
		return bidMatchFacade.allMatchBidInfoList(title, pager);
	}

	public static void main(String[] args){
		String moreBidIds = "2,34,100";
		List<Integer> ids =Arrays.stream(moreBidIds.split(",")).map(s->Integer.parseInt(s.trim())).
				collect(Collectors.toList());
		ids.forEach(s -> System.out.println(s));
	}
	/**
	 *  @Description    : 匹配
	 *  @Method_Name    : doMatch
	 *  @param oneBidId  匹配标的id
	 *  @param moreBidIds  待匹配标的id集合，以","分隔
	 *  @param bidType  标地类型：1-优选标，优选匹配散标、2-散标，散标匹配优选
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年9月28日 下午1:43:27 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	@RequestMapping("/doMatch")
	@ResponseBody
	@Token( operate = Token.Ope.REFRESH)
	@ActionLog(msg = "匹配, 标的id（oneBidId）: {args[0]} , moreBidIds: {args[1]},bidType: {args[2]}")
	public ResponseEntity<?> doMatch(Integer oneBidId,String moreBidIds,Integer bidType){
		RegUser regUser = BaseUtil.getLoginUser();
		if (regUser == null){
			return new ResponseEntity<>(ERROR,"未查询到登录用户信息！");
		}
		if (oneBidId == null){
			return new ResponseEntity<>(ERROR,"匹配标不能为空！");
		}
		if (moreBidIds == null){
			return new ResponseEntity<>(ERROR,"待匹配标不能为空！");
		}
		if (bidType == null || (bidType != 1 && bidType != 2)){
			return new ResponseEntity<>(ERROR,"不是有效的标地类型！");
		}
		Integer currentUserId = regUser.getId();
		List<Integer> moreBidIdList =Arrays.stream(moreBidIds.split(",")).map(s->Integer.parseInt(s.trim())).
				collect(Collectors.toList());
		ResponseEntity<?> result =  bidInvestFacade.match(oneBidId,moreBidIdList,bidType,currentUserId);
		if(!BaseUtil.error(result)){
			List<Integer> investListForTransferContract =
					(List<Integer>) result.getParams().get("investListForTransferContract");
			List<Integer> investListForCommonContract =
					(List<Integer>) result.getParams().get("investListForCommonContract");
			try {
				if(CommonUtils.isNotEmpty(investListForCommonContract)){
					jmsService.sendMsg(ContractConstants.MQ_QUEUE_CONINFO, DestinationType.QUEUE,
							investListForCommonContract, JmsMessageType.OBJECT);

				}
				if(CommonUtils.isNotEmpty(investListForTransferContract)){
					ConInfoSelfGenerateDTO dto = new ConInfoSelfGenerateDTO();
					dto.setInvestIdList(investListForTransferContract);
					dto.setContractType(ContractConstants.CONTRACT_TYPE_CREDITOR_TRANSFER);
					jmsService.sendMsg(ContractConstants.MQ_QUEUE_SELF_CONINFO, DestinationType.QUEUE, dto,
							JmsMessageType.OBJECT);
				}
			}catch (Exception e){
				logger.error("doMatch, 匹配成功后生成合同信息异常, 用户标识: {}, oneBidId: {}, moreBidIds: {}, bidType: {}, 异常信息:",
						currentUserId, oneBidId, moreBidIds, bidType, e);
			}

		}
		return result;
	}
	
	/**
	 *  @Description    : 通过标的id 查询此标的匹配列表
	 *  @Method_Name    : matchListByBid
	 *  @param bidId
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年7月21日 上午9:28:26 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	@RequestMapping("/matchListByBid")
	@ResponseBody
	public ResponseEntity<?> matchListByBid(int bidId,Pager pager){
		if(bidId==0){
			return new ResponseEntity<>(Constants.ERROR,"传递参数有误");
		}
		return bidMatchFacade.findMatchListByBidId(bidId,pager);
	}
	
	/**
	 * 
	 *  @Description    : 查询匹配列表
	 *  @Method_Name    : bidMatchList
	 *  @param bidMatchVo
	 *  @param pager
	 *  @return
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年9月30日 上午11:38:40 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	@RequestMapping("/bidMatchList")
	@ResponseBody
	public ResponseEntity<?> bidMatchList(BidMatchVo bidMatchVo,Pager pager){
		pager = bidMatchService.findMatchVoListByContidion(bidMatchVo,pager);
		return new ResponseEntity<>(SUCCESS, pager);
	}
	/**
	 *  @Description    : 查询匹配投资列表
	 *  @Method_Name    : BidInvestListForMatch
	 *  @param goodBidId
	 *  @param commonBidId
	 *  @return
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年7月21日 上午9:33:40 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	@RequestMapping("/bidInvestListForMatch")
	@ResponseBody
	public ResponseEntity<?> BidInvestListForMatch(Integer goodBidId,Integer commonBidId,Pager pager){
		if(goodBidId==null||commonBidId==null||goodBidId==0||commonBidId==0){
			return new ResponseEntity<>(Constants.ERROR,"传递参数有误");
		}
		Pager resultPager = bidInvestService.findMatchBidInvestList(goodBidId,commonBidId,pager);
		return new ResponseEntity<>(Constants.SUCCESS,resultPager);
	}
	/**
	 *  @Description    : 匹配统计 每日散标、优选待匹配金额
	 *  @Method_Name    : BidMatchAmountStatistics
	 *  @param startDate
	 *  @param endDate
	 *  @param limitDays  限制天数--多少天内需要匹配的
	 *  @return
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年7月21日 下午2:54:43 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	@RequestMapping("/bidMatchAmountStatistics")
	@ResponseBody
	public ResponseEntity<?> BidMatchAmountStatistics(String startDate,String endDate,Integer limitDays){
		Date start = new Date();
		Date end = new Date();
		if(StringUtils.isNotBlank(startDate)){
			start = DateUtils.parse(startDate, "yyyy-MM-dd");
		}
		if(StringUtils.isNotBlank(endDate)){
			end = DateUtils.parse(endDate, "yyyy-MM-dd");
		}
		start = new Date(DateUtils.getLastTimeOfDay(start).getTime());
		return bidMatchFacade.bidMatchAmountStatistics(start,end,limitDays);
	}

}
