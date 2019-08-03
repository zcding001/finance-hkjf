package com.hongkun.finance.api.controller.bid;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.invest.constants.InvestConstants;
import com.hongkun.finance.invest.facade.BidInfoFacade;
import com.hongkun.finance.invest.facade.BidInvestFacade;
import com.hongkun.finance.invest.model.vo.AppIndexBidInfoVO;
import com.hongkun.finance.invest.model.vo.BidInfoVO;
import com.hongkun.finance.invest.service.BidInfoService;
import com.hongkun.finance.invest.util.CalcInterestUtil;
import com.hongkun.finance.loan.facade.LoanFacade;
import com.hongkun.finance.point.service.PointRuleService;
import com.hongkun.finance.point.support.CalcPointUtils;
import com.hongkun.finance.qdz.facade.QdzTransferFacade;
import com.hongkun.finance.roster.constants.RosterFlag;
import com.hongkun.finance.roster.constants.RosterType;
import com.hongkun.finance.roster.service.RosInfoService;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.service.DicDataService;
import com.hongkun.finance.user.service.RegUserService;
import com.hongkun.finance.user.support.security.OperationTypeEnum;
import com.hongkun.finance.user.support.security.component.annotation.AskForPermission;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.utils.AppResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

import static com.hongkun.finance.invest.constants.InvestConstants.INVEST;
import static com.hongkun.finance.invest.constants.InvestConstants.REPAYMENT_MODE;
import static com.yirun.framework.core.utils.AppResultUtil.*;

/**
 * 客户端标的信息管理
 *
 * @author :zhongpingtang
 */
@Controller
@RequestMapping("/bidInfoController")
public class BidInfoController {
    private static final Logger logger = LoggerFactory.getLogger(BidInfoController.class);
    @Reference
    private BidInfoFacade bidInfoFacade;
    @Reference
    private BidInfoService bidInfoService;
    @Reference
    private BidInvestFacade bidInvestFacade;
    @Reference
    private DicDataService dicDataService;
    @Reference
    private QdzTransferFacade qdzTransferFacade;
    @Reference
    private LoanFacade loanFacade;
    @Reference
    private PointRuleService pointRuleService;
    @Reference
    private RegUserService regUserService;
    @Reference
    private RosInfoService rosInfoService;


    /**
     *  @Description    ：首页定期产品（钱袋子入口、新手标、爆款、推荐、月季年、体验）
     *  @Method_Name    ：findIndexBidsInfo
     *  @return java.util.Map<java.lang.String,java.lang.Object>
     *  @Creation Date  ：2018年09月18日 10:04
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    @RequestMapping("/findIndexBidsInfo")
    @ResponseBody
    public Map<String, Object> findIndexBidsInfo() {
        RegUser loginUser = BaseUtil.getLoginUser();
        Map<String, Object> result = AppResultUtil.successOfMsg("成功");
        boolean isExchange  =  rosInfoService.validateRoster(loginUser.getId(), RosterType.EXCHANGE_INVEST_SEACH, RosterFlag.WHITE);
        // 无定期投资权限或用户为海外投资用户
        if (isExchange || loginUser == null || !bidInvestFacade.validInvestQualification(loginUser.getId()).validSuc()
                || bidInvestFacade.validOverseaInvestor(loginUser.getId()).validSuc()) {
            return result;
        }
        result.putAll(bidInfoFacade.findAppIndexBidInfo(loginUser));
        result.putAll(getQdzInfo(loginUser));
        result.put("systemTime",new Date());
        return result;
    }


    /**
     *  @Description    ：定期产品列表（钱袋子入口、新手标、爆款、推荐、月季年、体验、直投散标）
     *  @Method_Name    ：findBidInfoList
     *  @return java.util.Map<java.lang.String,java.lang.Object>
     *  @Creation Date  ：2018年09月18日 16:33
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    @RequestMapping("/findBidInfoList")
    @ResponseBody
    public Map<String,Object> findBidInfoList(){
        //RegUser loginUser = BaseUtil.getLoginUser();
        RegUser loginUser = regUserService.findRegUserById(1220);
        Map<String, Object> result = AppResultUtil.successOfMsg("成功");
        boolean isExchange  =  rosInfoService.validateRoster(loginUser.getId(), RosterType.EXCHANGE_INVEST_SEACH, RosterFlag.WHITE);
        // 无定期投资权限
        if (loginUser == null || !bidInvestFacade.validInvestQualification(loginUser.getId()).validSuc()) {
            return result;
        }
        // 海外投资产品
        int overseaFlag = bidInvestFacade.validOverseaInvestor(loginUser.getId()).validSuc() ? 1 : 0;
        if(overseaFlag != 1 && !isExchange){
            result.putAll(getQdzInfo(loginUser));
        }
        result.putAll(bidInfoFacade.findBidInfoList(loginUser,overseaFlag));
        result.put("systemTime",new Date());
        return result;
    }


    /**
     * 查询首页的标的信息--财富plus接口
     *
     * @author :zhongpingtang
     */
    @RequestMapping("/findPlusIndexBidsInfo")
    @ResponseBody
    @AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
    public Map<String, Object> findPlusIndexBidsInfo(@RequestParam(value = "onlyLine", required = false, defaultValue = "false") boolean onlyLine) {

        //鸿坤金服对于非高净值会员，隐藏定期产品，只显示私募 海外和信托产品 。
        if (!bidInvestFacade.validVipStatus(BaseUtil.getLoginUser()).validSuc()) {
            return successOfInProperties(new AppIndexBidInfoVO()).addResParameter("systemTime", new Date())
                                                                 .reNameParameter(AppResultUtil.DATA_LIST, "appLinedBidsInfo");
        }
        //进入正常获取信息逻辑
        AppResultUtil.ExtendMap appLinedBidsInfo = successOfList(bidInfoFacade.findPlusIndexBidsInfo(BaseUtil.getLoginUser()).getAppLinedBidsInfo())
                //给还款类型添加描述
                .addParameterDescInList("biddRepaymentWay", (key) -> dicDataService.findNameByValue(INVEST, REPAYMENT_MODE, (Integer) key))
                .reNameParameter(AppResultUtil.DATA_LIST, "appLinedBidsInfo");
        return appLinedBidsInfo.addResParameter("systemTime", new Date());
    }


    /**
     * @param bidId     当前标的的id
     * @param fromPlace 从什么地方查询标的详情， 1-首页 2-投资记录
     *                  如果是从首页不显示保障合同，从投资记录正常显示
     *                  默认为从投资记录，不隐藏
     * @return java.util.Map<java.lang.String   ,   java.lang.Object>
     * @Description ：查询标的详情信息
     * @Method_Name ：findAppBidDetail
     * @Creation Date  ：2018/5/9
     * @Author ：zhongpingtang@hongkun.com.cn
     */
    @RequestMapping("/findAppBidDetail")
    @ResponseBody
    public Map<String, Object> findAppBidDetail(@RequestParam("bidId") Integer bidId,
                                                @RequestParam(value = "fromPlace", required = false, defaultValue = "2") Integer fromPlace) {
        return bidInfoFacade.findAppBidDetail(bidId, BaseUtil.getLoginUserId(), fromPlace);
    }

    /**
    *  查询预期收益
    *  @param bidId 标的id
    *  @param increaseRate 加息利率
    *  @param amount amount 投资金额
    *  @return java.util.Map<java.lang.String,java.lang.Object>
    *  @date                    ：2018/10/10
    *  @author                  ：zc.ding@foxmail.com
    */
    @RequestMapping("/findExpectAtm")
    @ResponseBody
    public Map<String, Object> findExpectAtm(Integer bidId, BigDecimal amount, BigDecimal increaseRate){
        return AppResultUtil.mapOfResponseEntity(this.loanFacade.findExpectAtm(bidId, amount, increaseRate));
    }

    /**
     * 产品页过滤标的信息-鸿坤金服接口
     *
     * @author :zhongpingtang
     */
    @RequestMapping("/filterProductBidInfo")
    @ResponseBody
    @AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
    public Map<String, Object> filterProductBidInfo(@RequestParam(value = "termValue", required = false) Integer termValue,
                                                    @RequestParam(value = "bidType", required = false) Integer bidType) {
        return bidInfoFacade.filterAppProductBidInfo(termValue, bidType);
    }

    /**
     * 产品页过滤标的信息-财富plus接口
     *
     * @author :zhongpingtang
     */
    @RequestMapping("/filterProductBidInfoForPlus")
    @ResponseBody
    @AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
    public Map<String, Object> filterProductBidInfoForPlus(@RequestParam(value = "termValue", required = false) Integer termValue,
                                                           @RequestParam(value = "bidType", required = false) Integer bidType) {
        return bidInfoFacade.filterProductBidInfoForPlus(termValue, bidType);
    }

    /**
     * @return java.util.Map<java.lang.String   ,   java.lang.Object>
     * @Description ：动态获取需要展示的产品 0-定期, 1-私募, 2-海外 3-信托
     * @Method_Name ：getProductTypes
     * @Creation Date  ：2018年05月17日 11:10
     * @Author ：yunlongliu@hongkun.com.cn
     */
    @ResponseBody
    @RequestMapping("getProductTypes")
    public Map<String, Object> getProductTypes(HttpServletRequest request, HttpServletResponse response) {
        /** 预留接口 **/
        Map<String, Object> resultMap = new HashMap<>();
        //鸿坤金服对于非高净值会员，隐藏定期产品，只显示私募 海外和信托产品 。不再使用财享+接口
        //List<String> list = bidInvestFacade.validVipStatus(BaseUtil.getLoginUser()).validSuc() ?
        //Arrays.asList("0", "1", "2", "3" , "4") : Arrays.asList("1", "2", "3" ,"4");
        RegUser regUser = BaseUtil.getLoginUser();
        // 判断有无定期投资资质决定是否展示定期投资tab 20181120 googe
        List<String> list = Arrays.asList("1", "2", "3" ,"4");
        if(regUser != null){
            list = bidInvestFacade.validInvestQualification(regUser.getId()).validSuc() ?
                    Arrays.asList("0", "1", "2", "3" , "4") : Arrays.asList("1", "2", "3" ,"4");
        }
        resultMap.put("data",list);
        return successOf(resultMap, "成功");
    }

    /**
    *  收益计算器
    *  @param month
    *  @param money
    *  @param rate
    *  @return java.util.Map<java.lang.String,java.lang.Object>
    *  @date                    ：2018/10/18
    *  @author                  ：zc.ding@foxmail.com
    */
    @ResponseBody
    @RequestMapping("calcIncome")
    public Map<String, Object> calcIncome(Integer month, BigDecimal money, BigDecimal rate) {
        return AppResultUtil.successOfMsg("").addResParameter("interest",
                CalcInterestUtil.calcInterest(InvestConstants.BID_TERM_UNIT_MONTH, month, money, rate)).addResParameter("points", CalcPointUtils.calculateInvestPoint(money.doubleValue(), month, InvestConstants.BID_TERM_UNIT_MONTH, this.pointRuleService.getCurrentOnUseRule().getPointInvestBase().intValue()));
    }

    /**
     * 获取钱袋子信息
     * @param loginUser
     * @return
     */
    private Map<String,Object> getQdzInfo(RegUser loginUser) {
        Map<String,Object> result = new HashMap<>();
        //设置钱袋子入口标识
        if(this.bidInvestFacade.validQdzEnable(loginUser.getId()) > 0){
            //查询钱袋子信息
            result.put("qdzInfo", qdzTransferFacade.findQdzInfo(loginUser.getId()).getParams());
        }
        return result;
    }
    /**
     *  @Description    : 查询钱袋子页面定期标的信息
     *  @Method_Name    : findQdzPageBids;
     *  @return
     *  @return         : Map<String,Object>;
     *  @Creation Date  : 2018年10月23日 上午10:39:44;
     *  @Author         : yanbinghuang@hongkun.com.cn 黄艳兵;
     */
    @RequestMapping("/findQdzPageBids")
    @ResponseBody
    public Map<String, Object> findQdzPageBids() {
        RegUser regUser = BaseUtil.getRegUser(BaseUtil.getLoginUserId(),
                () -> this.regUserService.findRegUserById(BaseUtil.getLoginUserId()));
        // 判断有无定期投资权限
        if (regUser == null || !bidInvestFacade.validInvestQualification(regUser.getId()).validSuc()) {
            return AppResultUtil.errorOfMsg("查询失败");
        }
        return AppResultUtil.successOfMsg("查询成功").addResParameter("qdzBidInfo", bidInfoFacade.findQdzPageBidInfo(regUser).get("qdzPageBids"));
    }
   /***
    *  @Description    : 跳转到新手机指引页面
    *  @Method_Name    : firstBidDirect;
    *  @param request
    *  @param source
    *  @return
    *  @return         : ModelAndView;
    *  @Creation Date  : 2019年1月8日 上午9:03:31;
    *  @Author         : yanbinghuang@hongkun.com.cn 黄艳兵;
    */
    @RequestMapping("/firstBidDirect")
    @AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
    @ResponseBody
    public ModelAndView firstBidDirect(HttpServletRequest request,Integer source) {
        ModelAndView mav = new ModelAndView("firstbid/firstbiddetail");
        BidInfoVO bidInfoRusult = null;
        Map<String,Object> result = new HashMap<String,Object>();
        BidInfoVO bidInfo = new BidInfoVO();
        bidInfo.setProductType(InvestConstants.BID_PRODUCT_PREFERRED);
        bidInfo.setStates(Arrays.asList(InvestConstants.BID_STATE_WAIT_INVEST,InvestConstants.BID_STATE_WAIT_AUDIT,InvestConstants.BID_STATE_WAIT_LOAN));
        List<BidInfoVO> bidInfoList = bidInfoService.findBidInfoVOByCondition(bidInfo);
        if(bidInfoList != null && bidInfoList.size()>0){
            bidInfoList.sort(Comparator.comparing(BidInfoVO::getState).thenComparing(BidInfoVO::getCreateTime));
            bidInfoRusult = bidInfoList.get(0);
        }
        mav.addObject("bidId",bidInfoRusult !=null?bidInfoRusult.getId():0);
        mav.addObject("bidName",bidInfoRusult !=null?bidInfoRusult.getTitle():"");
        mav.addObject("source",source);
        mav.addObject("data",result);
        return mav;
    }
}
