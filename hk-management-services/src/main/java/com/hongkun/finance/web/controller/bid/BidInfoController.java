package com.hongkun.finance.web.controller.bid;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.invest.constants.InvestConstants;
import com.hongkun.finance.invest.facade.BidInfoFacade;
import com.hongkun.finance.invest.model.BidInfo;
import com.hongkun.finance.invest.model.BidInfoDetail;
import com.hongkun.finance.invest.model.BidProduct;
import com.hongkun.finance.invest.model.vo.BidInfoVO;
import com.hongkun.finance.invest.service.BidInfoDetailService;
import com.hongkun.finance.invest.service.BidInfoService;
import com.hongkun.finance.invest.service.BidProductService;
import com.hongkun.finance.user.support.security.annotation.ActionLog;
import com.hongkun.finance.user.utils.BaseUtil;
import com.hongkun.finance.vas.enums.VasRuleTypeEnum;
import com.hongkun.finance.vas.model.VasRebatesRule;
import com.hongkun.finance.vas.service.VasRebatesRuleService;
import com.yirun.framework.core.annotation.Token;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.DateUtils;
import com.yirun.framework.core.utils.pager.Pager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.hongkun.finance.invest.constants.InvestConstants.*;
import static com.hongkun.finance.user.utils.BaseUtil.equelsIntWraperPrimit;
import static com.hongkun.finance.user.utils.BaseUtil.getLoginUser;
import static com.hongkun.finance.vas.constants.VasConstants.VAS_RULE_STATE_START;
import static com.yirun.framework.core.commons.Constants.ERROR;
import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * @Description : 标的信息的controller
 * @Project : finance
 * @Program Name : com.hongkun.finance.web.controller.bid.BidInfoController
 * @Author : zhongpingtang@yiruntz.com 唐忠平
 */
@Controller
@RequestMapping("/bidInfoController")
public class BidInfoController {

    @Reference
    private BidInfoService bidInfoService;

    @Reference
    private BidProductService bidProductService;

    @Reference
    private BidInfoDetailService bidInfoDetailService;

    @Reference
    private BidInfoFacade bidInfoFacade;

    @Reference
    private VasRebatesRuleService vasRebatesRuleService;


    /**
     * 新建一个标的
     *
     * @param bidInfoVO
     * @return
     * @Author : zhongpingtang@yiruntz.com 唐忠平
     */
    @RequestMapping("/save")
    @ResponseBody
    @Token
    @ActionLog(msg = "新建标的, 标的名称: {args[0].title}")
    public ResponseEntity save(@Valid BidInfoVO bidInfoVO) {
        return bidInfoFacade.saveBid(bidInfoVO, getLoginUser().getId());

    }

    /**
     * 更新标的
     *
     * @param bidInfoVO
     * @return
     * @Author : zhongpingtang@yiruntz.com 唐忠平
     */
    @RequestMapping("/updateBidInfo")
    @ResponseBody
    @Token
    @ActionLog(msg = "更新标的, 标的id: {args[0].id}")
    public ResponseEntity updateBidInfo(@Valid BidInfoVO bidInfoVO) {
        return  bidInfoFacade.updateBidInfoAndDetail(bidInfoVO,getLoginUser().getId());
    }
    
    @RequestMapping("/updateCreditorProperty")
    @ResponseBody
    @ActionLog(msg = "更新债权转让信息, 标的id: {args[0].biddInfoId}")
    public ResponseEntity updateCreditorProperty(@Valid BidInfoDetail bidInfoDetail) {
    	try {
			return this.bidInfoFacade.updateCreditorProperty(bidInfoDetail,getLoginUser());
		} catch (Exception e) {
			return new ResponseEntity<>(Constants.ERROR,"更新债权转让信息失败");
		}
    }
    /**
     * 根据标的id查询标的信息
     *
     * @param bidInfoId
     * @return
     * @Author : zhongpingtang@yiruntz.com 唐忠平
     */
    @RequestMapping("/findBidInfoDetails")
    @ResponseBody
    public ResponseEntity findBidInfoDetails(Integer bidInfoId) {
        return bidInfoFacade.findBidInfoDetailVo(bidInfoId);
    }

    /**
     * @param vo
     * @param pager
     * @return : ResponseEntity<?>
     * @Description : 条件检索标的信息
     * @Method_Name : bidInfoDetailList
     * @Creation Date  : 2017年9月20日 上午8:56:05
     * @Author : zhichaoding@hongkun.com zc.ding
     */
    @RequestMapping("/bidInfoDetailList")
    @ResponseBody
    public ResponseEntity<?> bidInfoDetailList(BidInfoVO vo, Pager pager) {
        if (vo.getState()==null||vo.getState()<=0){
            List<Integer> states = new ArrayList<Integer>();
            states.add(InvestConstants.BID_STATE_WAIT_NEW);
            states.add(InvestConstants.BID_STATE_WAIT_INVEST);
            states.add(InvestConstants.BID_STATE_CHECK_REJECT);
            vo.setStates(states);
        }
        return this.bidInfoFacade.findBidInfoDetailVoList(pager, vo);
    }

    /**
     * @param vo
     * @param pager
     * @return : ResponseEntity<?>
     * @Description : 查询所有标的列表
     * @Method_Name : allBidInfoList
     * @Creation Date  : 2017年9月29日 上午11:01:54
     * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
     */
    @RequestMapping("/allBidInfoList")
    @ResponseBody
    public ResponseEntity<?> allBidInfoList(BidInfoVO vo, Pager pager) {
        return new ResponseEntity<>(SUCCESS, this.bidInfoFacade.findBidVoListForMatch(pager, vo));
    }

    /**
     * @param vo
     * @param pager
     * @return : ResponseEntity<?>
     * @Description : 借款标查询（还款中和已放款）
     * @Method_Name : bidInfoAfterLoanList
     * @Creation Date  : 2017年9月21日 下午2:02:40
     * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
     */
    @RequestMapping("/bidInfoAfterLoanList")
    @ResponseBody
    public ResponseEntity<?> bidInfoAfterLoanList(BidInfoVO vo, Pager pager) {
        List<Integer> states = new ArrayList<Integer>();
        states.add(InvestConstants.BID_STATE_WAIT_FINISH);
        states.add(InvestConstants.BID_STATE_WAIT_REPAY);
//        states.add(InvestConstants.BID_STATE_CHECK_REJECT);
        vo.setStates(states);
        return this.bidInfoFacade.findBidInfoDetailVoList(pager, vo);
    }

    /**
     * @param vo
     * @param pager
     * @return : ResponseEntity<?>
     * @Description : 待审核标的列表
     * @Method_Name : auditBidInfoList
     * @Creation Date  : 2017年9月25日 下午2:07:53
     * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
     */
    @RequestMapping("/auditBidInfoList")
    @ResponseBody
    @Token
    public ResponseEntity<?> auditBidInfoList(BidInfoVO vo, Pager pager) {
        vo.setState(InvestConstants.BID_STATE_WAIT_AUDIT);
        return this.bidInfoFacade.findBidInfoDetailVoList(pager, vo);
    }


    /**
     * @param id
     * @return : ResponseEntity<?>
     * @Description : 通过标的id查询标的信息
     * @Method_Name : findBidInfo
     * @Creation Date  : 2017年9月20日 上午8:56:26
     * @Author : zhichaoding@hongkun.com zc.ding
     */
    @RequestMapping("/findBidInfo")
    @ResponseBody
    public ResponseEntity<?> findBidInfo(@RequestParam("id") Integer id) {
        ResponseEntity<?> responseParam = new ResponseEntity<>(SUCCESS, "查询成功");
        try {
            BidInfo bidInfo = bidInfoService.findBidInfoById(id);
            List<BidProduct> originProducts = bidProductService.findBidProductList(new BidProduct());
            responseParam.getParams().put("bidInfo",
                    bidInfo != null ? bidInfo : new BidInfo());
            responseParam.getParams().put("bidProducts",
                    originProducts.stream().filter(e -> !equelsIntWraperPrimit(e.getState(),PRODUCT_STATE_OFF/*过滤掉已经下架的标的*/)).collect(Collectors.toList()));
        } catch (Exception e) {
            responseParam = new ResponseEntity<>(ERROR, "查询失败");
        }
        return responseParam;
    }

    /**
     * 上架标的
     *
     * @param id
     * @return
     * @Author : zhongpingtang@yiruntz.com 唐忠平
     */
    @RequestMapping("/trunOnBid")
    @ResponseBody
    @Token
    @ActionLog(msg = "执行标的上架操作, 标的id: {args[0]}")
    public ResponseEntity<?> trunOnBid(@RequestParam("id")Integer id) {
        return this.bidInfoFacade.updateBidState(id, InvestConstants.BID_STATE_WAIT_INVEST, null, BaseUtil.getLoginUser().getId());
    }

    /**
     * 下架标的
     *
     * @param id
     * @return
     * @Author : zhongpingtang@yiruntz.com 唐忠平
     */
    @RequestMapping("/trunOffBid")
    @ResponseBody
    @Token
    @ActionLog(msg = "执行标的下架操作, 标的id: {args[0]}")
    public ResponseEntity<?> trunOffBid(@RequestParam("id")Integer id) {
        return this.bidInfoFacade.updateBidState(id, InvestConstants.BID_STATE_WAIT_NEW, null,BaseUtil.getLoginUser().getId());
    }

    /**
     *  逻辑删除标的
     *
     * @param id
     * @return
     * @Author : zhongpingtang@yiruntz.com 唐忠平
     */
    @RequestMapping("/deleteBidInfoOnState")
    @ResponseBody
    @Token
    @ActionLog(msg = "执行标的删除操作, 标的id: {args[0]}")
    public ResponseEntity<?> deleteBidInfoOnState(@RequestParam("id")Integer id) {
        return this.bidInfoFacade.updateBidState(id, InvestConstants.BID_STATE_DELETE, null,BaseUtil.getLoginUser().getId());
    }


    /**
     * 满标审核标的
     *
     * @param id
     * @return
     * @Author : zhongpingtang@yiruntz.com 唐忠平
     */
    @RequestMapping("/auditBid")
    @ResponseBody
    @Token
    @ActionLog(msg = "执行标的审核操作, 标的id: {args[0]} , 审核为状态: {args[1]}")
    public ResponseEntity<?> auditBid(@RequestParam("id") Integer id,
                                      @RequestParam("state")Integer state,
                                      String reason) {
        if ((equelsIntWraperPrimit(state , InvestConstants.BID_STATE_WAIT_LOAN)||equelsIntWraperPrimit(state ,InvestConstants.BID_STATE_CHECK_REJECT))) {
           return this.bidInfoFacade.auditBid(id, state,reason,BaseUtil.getLoginUser().getId());
        }
        return new ResponseEntity<>(ERROR, "审核状态非法");
    }


    /**
     * @return : List<BidInfo>
     * @Description : 意向金的标的信息(购房宝&物业宝)
     * @Method_Name : depositBidList
     * @Creation Date  : 2017年9月30日 下午2:54:37
     * @Author : zhichaoding@hongkun.com zc.ding
     */
    @RequestMapping("/depositBidList")
    @ResponseBody
    @Token
    public List<BidInfoVO> depositBidList() {
        BidInfoVO bidInfoVO = new BidInfoVO();
        bidInfoVO.setProductTypes(Arrays.asList(BID_PRODUCT_BUYHOUSE, BID_PRODUCT_PROPERTY));
        List<BidInfoVO> list = this.bidInfoService.findBidInfoVoList(bidInfoVO);
        return list;
    }


    /**
     * 当前是否有好友推荐启用的规则
     * @return
     */
    @RequestMapping("/hasRecommendEarnEnable")
    @ResponseBody
    public ResponseEntity hasRecommendEarnEnable(){
        VasRebatesRule vasRebatesRule = new VasRebatesRule();
        vasRebatesRule.setType(VasRuleTypeEnum.RECOMMEND.getValue());
        vasRebatesRule.setState(VAS_RULE_STATE_START);
        return new ResponseEntity(SUCCESS, vasRebatesRuleService.findVasRebatesRuleCount(vasRebatesRule)>0);
    }

}
