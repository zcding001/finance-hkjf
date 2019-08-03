package com.hongkun.finance.api.controller.vip;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.payment.constant.TradeStateConstants;
import com.hongkun.finance.payment.model.FinPaymentRecord;
import com.hongkun.finance.payment.service.FinPaymentRecordService;
import com.hongkun.finance.point.service.PointCommonService;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.service.DicDataService;
import com.hongkun.finance.user.utils.BaseUtil;
import com.hongkun.finance.vas.constants.VasConstants;
import com.hongkun.finance.vas.facade.VasVipGrowRecordFacade;
import com.hongkun.finance.vas.facade.VasVipTreatmentFacade;
import com.hongkun.finance.vas.model.VasVipGradeStandard;
import com.hongkun.finance.vas.model.VasVipGrowRecord;
import com.hongkun.finance.vas.model.VasVipGrowRule;
import com.hongkun.finance.vas.service.VasVipGradeStandardService;
import com.hongkun.finance.vas.service.VasVipGrowRecordService;
import com.hongkun.finance.vas.service.VasVipGrowRuleService;
import com.hongkun.finance.vas.service.VasVipTreatmentService;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.AppResultUtil;
import com.yirun.framework.core.utils.pager.Pager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * @Description : 会员等级controller层
 * @Project : framework
 * @Program Name  : com.hongkun.finance.api.controller.vip.VipController
 * @Author : pengwu@hongkun.com.cn 吴鹏
 */
@Controller
@RequestMapping("/vipController")
public class VipController {

    @Reference
    VasVipGrowRecordService vipGrowRecordService;
    @Reference
    VasVipTreatmentService vipTreatmentService;
    @Reference
    VasVipGrowRuleService vipGrowRuleService;
    @Reference
    VasVipGradeStandardService vipGradeStandardService;
    @Reference
    private PointCommonService pointCommonService;
    @Reference
    VasVipGrowRecordFacade vipGrowRecordFacade;
    @Reference
    VasVipTreatmentFacade vipTreatmentFacade;
    @Reference
    DicDataService dicDataService;
    @Reference
    private FinPaymentRecordService finPaymentRecordService;

    /**
     * @param pager {currentPage   当前页码
     *                  pageSize}      每页条数
     * @return : Map<String,Object>
     * @Description : 获取用户成长记录集合
     * @Method_Name : getVipGrowRecordList
     * @Creation Date  : 2018年03月12日 上午10:04:50
     * @Author : pengwu@hongkun.com.cn
     */
    @RequestMapping("/getVipGrowRecordList")
    @ResponseBody
    public Map<String,Object> getVipGrowRecordList(Pager pager){
        pager.setInfiniteMode(true);
        RegUser regUser = BaseUtil.getLoginUser();
        ResponseEntity result = vipGrowRecordService.getUserGrowRecordDetail(regUser.getId(),pager);
        List<VasVipGrowRecord> list = (List<VasVipGrowRecord>) ((Pager)result.getResMsg()).getData();
        return AppResultUtil.mapOfListInProperties(list,SUCCESS,"查询成功",
                "type","growValue","note","createTime").processObjInList((map)->{
            map.put("typeName",dicDataService.findNameByValue(VasConstants.VAS,VasConstants.VIP_GROW_TYPE,(int)map.get("type")));
        }).removeResParameter("type");
    }

    /**
     *  @Description    ：获取符合当前用户注册时间的所有会员待遇信息
     *  @Method_Name    ：getAllVipTreatMentList
     *  @return java.util.Map<java.lang.String,java.lang.Object>
     *  @Creation Date  ：2018/10/10
     *  @Author         ：pengwu@hongkun.com.cn
     */
    @RequestMapping("/getAllVipTreatMentList")
    @ResponseBody
    public Map<String,Object> getAllVipTreatMentList(){
        RegUser regUser = BaseUtil.getLoginUser();
        return vipTreatmentFacade.getAllVipTreatMentList(regUser.getCreateTime());
    }

    /**
     * @return : Map<String,Object>
     * @Description : 获取当前用户成长值任务列表描述信息
     * @Method_Name : getVipGrowRuleListDescription
     * @Creation Date  : 2018年03月12日 上午11:18:50
     * @Author : pengwu@hongkun.com.cn
     */
    @RequestMapping("/getVipGrowRuleListDescription")
    @ResponseBody
    public Map<String,Object> getVipGrowRuleListDescription(){
        RegUser regUser = BaseUtil.getLoginUser();
        List<VasVipGrowRule> list = vipGrowRuleService.getVipGrowRuleByRegistTime(regUser.getCreateTime());
        List<Map<String,Object>> ruleList = new ArrayList<>();
        //推荐任务列表
        List<Map<String,Object>> recommendList = new ArrayList<>();
        //日常任务列表
        List<Integer> condition = Arrays.asList(1,2,6);
        List<Map<String,Object>> dailyList = new ArrayList<>();
        list.forEach((vipGrowRule) -> {
            Map<String,Object> rule = new HashMap<>(4);
            int type = vipGrowRule.getType();
            String growth = vipGrowRule.getGrowValue();
            if (vipGrowRule.getFormulaEnable() == 1){
                growth = growth.replace("money", "投资金额").replace("day","投资天数");
            }
            //已签到用户不展示【3-立即签到】任务
            if ((int)pointCommonService.hasSign(regUser.getId()).getParams().get("state") == 1 && type == 3){
                return;
            }
            //已充值的用户不展示【4-首次充值】任务
            FinPaymentRecord paymentRecord = new FinPaymentRecord();
            paymentRecord.setRegUserId(regUser.getId());
            paymentRecord.setState(TradeStateConstants.ALREADY_PAYMENT);
            if (finPaymentRecordService.findFinPaymentRecordCount(paymentRecord) > 0 && type == 4){
                return;
            }
            rule.put("type",type);
            rule.put("typeName",dicDataService.findNameByValue(VasConstants.VAS, VasConstants.VIP_GROW_TYPE,type));
            String introduction = "";
            String buttonText = "立即完成";
            switch (type) {
                case 1:introduction = "投资定期理财项目，即可获得成长值："+growth;buttonText = "立即投资";break;
                case 2:introduction = "邀请好友成功注册获得"+growth+"成长值";buttonText = "立即邀请";break;
                case 3:introduction = "登录鸿坤金服App，点击首页签到图标即可获得"+growth+"成长值";buttonText = "立即签到";;break;
                case 4:introduction = "登录鸿坤金服网站或者App，完成首次充值即可获得"+growth+"成长值";buttonText = "立即充值";break;
                case 5:introduction = "用户登录鸿坤金服，在积分商城中完成商品兑换即可获得"+growth+"成长值";buttonText = "立即兑换";break;
                case 6:introduction = "邀请好友成功投资即可获得成长值："+growth;buttonText = "立即邀请";break;
                case 7:introduction = "登录鸿坤金服App，点击首页“积分花吧”完成积分支付获得"+growth+"成长值";break;
                case 8:introduction = "用户登录鸿坤金服，完成积分转赠即可获得"+growth+"成长值";break;
                default:break;
            }
            rule.put("introduction",introduction);
            rule.put("buttonText",buttonText);
            //1,2,6为推荐任务，其余为日常任务
            if (condition.contains(type)){
                recommendList.add(rule);
            }else {
                dailyList.add(rule);
            }
        });
        Map<String,Object> recommend = new HashMap<>(2);
        recommend.put("title","推荐任务");
        recommend.put("data",recommendList);
        ruleList.add(recommend);
        Map<String,Object> daily = new HashMap<>(2);
        daily.put("title","日常任务");
        daily.put("data",dailyList);
        ruleList.add(daily);
        return AppResultUtil.successOfList(ruleList,"查询成功",null);
    }


    /**
     * @return : Map<String,Object>
     * @Description : 获取用户会员等级对应的特权描述信息
     * @Method_Name : getVipTreatMentListDescription
     * @Creation Date  : 2018年03月12日 上午11:14:50
     * @Author : pengwu@hongkun.com.cn
     */
    @RequestMapping("/getVipTreatMentListDescription")
    @ResponseBody
    public Map<String,Object> getVipTreatMentListDescription(){
        RegUser regUser = BaseUtil.getLoginUser();
        return AppResultUtil.successOfList(vipTreatmentService.getVipTreatMentListDescription(regUser.getId(),regUser
                .getCreateTime()),"查询成功", null).processObjInList((map) -> {
            map.put("typeName",dicDataService.findNameByValue(VasConstants.VAS,VasConstants.VIP_TREATMENT_TYPE,(int)map.get("type")));
        });
    }

    /**
     * @return : Map<String,Object>
     * @Description : 获取当前用户会员成长介绍
     * @Method_Name : getVipIntroductionList
     * @Creation Date  : 2018年03月12日 上午11:20:50
     * @Author : pengwu@hongkun.com.cn
     */
    @RequestMapping("/getVipIntroductionList")
    @ResponseBody
    public Map<String,Object> getVipIntroductionList(){
        RegUser regUser = BaseUtil.getLoginUser();
        List<Map<String,Object>> introductionList = new ArrayList<>();
        //1.会员成长体系介绍
        List<VasVipGradeStandard> gradeList = vipGradeStandardService.getAllGradeList();
        Map<String,Object> gradeIntroduction = new HashMap<>(2);
        gradeIntroduction.put("title","会员成长体系包括"+gradeList.size()+"个会员等级，会员等级由成长值决定：");
        StringBuilder gradeContent = new StringBuilder("V0(注册即可)");
        gradeList.forEach((vipGradeStandard) -> {
            if (vipGradeStandard.getLevel() != 0){
                gradeContent.append("、V"+vipGradeStandard.getLevel()+"(≥"+vipGradeStandard.getGrowthValMin()+")");
            }
        });
        gradeIntroduction.put("content",gradeContent.toString());
        introductionList.add(gradeIntroduction);
        //2.平台任务介绍
        List<VasVipGrowRule> ruleList = vipGrowRuleService.getVipGrowRuleByRegistTime(regUser.getCreateTime());
        Map<String,Object> ruleIntroduction = new HashMap<>(2);
        ruleIntroduction.put("title","完成平台任务，获取成长值：");
        StringBuilder ruleContent = new StringBuilder("在平台通过");
        //获取邀请好友投资成长值公式
        String growthValue = "";
        for (int i=0;i<ruleList.size();i++){
            ruleContent.append(dicDataService.findNameByValue(VasConstants.VAS, VasConstants.VIP_GROW_TYPE,
                    ruleList.get(i).getType()) + (i != (ruleList.size()-1) ? "、" : ""));
            if (ruleList.get(i).getType() == 6){
                growthValue = ruleList.get(i).getGrowValue();
            }
        }
        ruleContent.append("等来获取成长值。");
        ruleIntroduction.put("content",ruleContent.toString());
        introductionList.add(ruleIntroduction);
        //3.会员降级说明
        Map<String,Object> downGradeIntroduction = new HashMap<>(2);
        downGradeIntroduction.put("title","会员等级降级说明：");
        downGradeIntroduction.put("content","用户超过三个月未投资且无待收金额，第四个月开始降级，成长值变为降级后等级最高成长值。");
        introductionList.add(downGradeIntroduction);
        //4.其他说明
        Map<String,Object> otherIntroduction = new HashMap<>(2);
        otherIntroduction.put("title","其他说明");
        StringBuilder otherContent = new StringBuilder
                ("用户投资和邀请好友投资获取的成长值计算说明：月标按照30天计算；用户邀请的一级好友投资获取的成长值计算说明：按照公式");
        otherContent.append(growthValue.replace("money","投资金额").replace("day","投资天数"));
        otherIntroduction.put("content",otherContent);
        introductionList.add(otherIntroduction);
        return AppResultUtil.successOfList(introductionList,"查询成功",null);
    }

    /**
     *  @Description    ：获取用户会员等级信息
     *  @Method_Name    ：getUserVipInfo
     *
     *  @return java.util.Map<java.lang.String,java.lang.Object>
     *  @Creation Date  ：2018/9/27
     *  @Author         ：pengwu@hongkun.com.cn
     */
    @RequestMapping("/getUserVipInfo")
    @ResponseBody
    public Map<String,Object> getUserVipInfo(){
        RegUser regUser = BaseUtil.getLoginUser();
        return vipGrowRecordFacade.getUserVipInfo(regUser.getId());
    }
}
