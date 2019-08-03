package com.hongkun.finance.web.controller.bid;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.invest.constants.InvestConstants;
import com.hongkun.finance.invest.facade.BidInvestFacade;
import com.hongkun.finance.invest.model.BidAutoScheme;
import com.hongkun.finance.invest.model.BidInvest;
import com.hongkun.finance.invest.model.vo.BidInvestDetailVO;
import com.hongkun.finance.invest.service.BidAutoSchemeService;
import com.hongkun.finance.invest.service.BidInvestService;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.service.RegUserService;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.CommonUtils;
import com.yirun.framework.core.utils.pager.Pager;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.hongkun.finance.invest.constants.InvestConstants.BID_INVEST_CHANNEL_IMMEDIATE;

/**
 * @Description   : 投资controller（后台）
 * @Project       : management-financial-services
 * @Program Name  : com.hongkun.finance.web.controller.bid.BidInvestController.java
 * @Author        : xuhuiliu@hongkun.com.cn 刘旭辉
 */
@Controller
@RequestMapping("/bidInvestController")
public class BidInvestController {
	
	@Reference
	private BidInvestService bidInvestService;
	@Reference
	private RegUserService regUserService;
	@Reference
	private BidInvestFacade bidInvestFacade;
	@Reference
	private BidAutoSchemeService bidAutoSchemeService;
	
	/**
	 *  @Description    : 根据标的id查询该标的下投资列表
	 *  @Method_Name    : listAllBidInvest
	 *  @param pager
	 *  @param bidId
	 *  @return
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年6月17日 上午9:42:16 
	 *  @Author         : xuhuiliu@hongkun.com.cn 刘旭辉
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/listAllBidInvest")
    @ResponseBody
    public ResponseEntity<?> listAllBidInvest(Pager pager,Integer bidId){
		BidInvest contidion = new BidInvest();
		contidion.setBidInfoId(bidId);
//	        List<BidInvest> bidInvestList = bidInvestService.findBidInvestList(contidion);
        Pager p = bidInvestService.findBidInvestList(contidion, pager);
        List<BidInvest> bidInvestList = (List<BidInvest>) p.getData();
        List<BidInvestDetailVO> resultList = new ArrayList<BidInvestDetailVO>();
        if (CommonUtils.isNotEmpty(bidInvestList)) {
        	for(BidInvest bidInvest:bidInvestList){
        		RegUser investUser = regUserService.findRegUserById(bidInvest.getRegUserId());
        		BidInvestDetailVO vo = new BidInvestDetailVO();
        		vo.setRealName(bidInvest.getRealName());
        		vo.setInvestUserTel(String.valueOf(investUser.getLogin()));
        		vo.setInvestAmount(bidInvest.getInvestAmount());
        		vo.setBidInvestState(bidInvest.getState());
        		vo.setCreateTime(bidInvest.getCreateTime());
        		resultList.add(vo);
        	}
        }
        p.setData(resultList);
        return new ResponseEntity<>(Constants.SUCCESS,p);
    }
	
	/**
	 *  @Description    : 设置某笔债权可转让
	 *  @Method_Name    : updateTransState
	 *  @param bidInvestId
	 *  @return
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年6月17日 上午9:47:13 
	 *  @Author         : xuhuiliu@hongkun.com.cn 刘旭辉
	 */
	@RequestMapping("/updateTransState")
    @ResponseBody
	public ResponseEntity<?> updateTransState(Integer bidInvestId,Integer transState){
		if(bidInvestId==null||bidInvestId<=0||(transState!=0&&transState!=1)){
			return new ResponseEntity<String>(Constants.ERROR,"传递参数有误");
		}
		return bidInvestFacade.updateTransState(bidInvestId,transState);
	}
	
	/**
	 *  @Description    : 条件检索投资记录
	 *  @Method_Name    : investList
	 *  @param pager
	 *  @param vo
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年9月19日 下午5:08:01 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	@RequestMapping("/investList")
    @ResponseBody
	public ResponseEntity<?> investList(Pager pager, BidInvestDetailVO vo){
		vo.setInvestChannel(BID_INVEST_CHANNEL_IMMEDIATE);
		vo.setBidInvestStates(Arrays.asList(InvestConstants.INVEST_STATE_SUCCESS, InvestConstants.INVEST_STATE_DEL,InvestConstants.INVEST_STATE_TRANSFER,InvestConstants.INVEST_STATE_SUCCESS_BUYER));
        vo.setSortColumns("b1.create_time DESC");
		Pager result = this.bidInvestService.findBidInvestDetailList(vo, pager);
		if(!BaseUtil.resultPageHasNoData(result)){
			List<BidInvestDetailVO> list = (List<BidInvestDetailVO>)result.getData();
			list.stream().forEach(e -> {e.setInvestUserTel(String.valueOf(regUserService.findRegUserById(e.getRegUserId()).getLogin()));
										e.setInvestAmount(e.getInvestAmount().subtract(e.getTransAmount()));});
		}
		return new ResponseEntity<>(Constants.SUCCESS, result);
	}
	
	/**
	 *  @Description    : 条件检索自动投资配置
	 *  @Method_Name    : bidAutoSchemeList
	 *  @param pager
	 *  @param bidAutoScheme
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2018年1月23日 上午9:47:40 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	@RequestMapping("/bidAutoSchemeList")
    @ResponseBody
    public ResponseEntity<?> bidAutoSchemeList(Pager pager, BidAutoScheme bidAutoScheme) {
        List<Integer> ids = regUserService.findUserIdsByTel(bidAutoScheme.getTel());
        if (!CollectionUtils.isEmpty(ids)) {
            bidAutoScheme.setRegUserId(ids.get(0));
        }
        Pager result = this.bidAutoSchemeService.findBidAutoSchemeList(bidAutoScheme, pager);
        if (!BaseUtil.resultPageHasNoData(result)) {
            ((List<BidAutoScheme>) result.getData()).forEach(e -> e.setTel(BaseUtil.getRegUser(e.getRegUserId(),
                    () -> regUserService.findRegUserById(e.getRegUserId())).getLogin()));
        }
        return new ResponseEntity<>(Constants.SUCCESS, result);
    }
}
