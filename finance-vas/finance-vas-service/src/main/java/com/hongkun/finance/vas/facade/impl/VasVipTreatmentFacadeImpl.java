package com.hongkun.finance.vas.facade.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.hongkun.finance.invest.service.BidInvestService;
import com.hongkun.finance.loan.service.BidReceiptPlanService;
import com.hongkun.finance.sms.constants.SmsConstants;
import com.hongkun.finance.sms.constants.SmsMsgTemplate;
import com.hongkun.finance.sms.model.SmsMsgInfo;
import com.hongkun.finance.sms.model.SmsTelMsg;
import com.hongkun.finance.sms.model.SmsWebMsg;
import com.hongkun.finance.sms.utils.SmsSendUtil;
import com.hongkun.finance.user.constants.UserConstants;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.service.DicDataService;
import com.hongkun.finance.user.service.RegUserDetailService;
import com.hongkun.finance.user.service.RegUserService;
import com.hongkun.finance.vas.constants.VasConstants;
import com.hongkun.finance.vas.constants.VasVipConstants;
import com.hongkun.finance.vas.facade.VasVipTreatmentFacade;
import com.hongkun.finance.vas.model.VasVipGradeStandard;
import com.hongkun.finance.vas.model.VasVipGrowRecord;
import com.hongkun.finance.vas.model.VasVipTreatment;
import com.hongkun.finance.vas.model.vo.VasVipGrowRecordMqVO;
import com.hongkun.finance.vas.service.VasCouponDetailService;
import com.hongkun.finance.vas.service.VasVipGradeStandardService;
import com.hongkun.finance.vas.service.VasVipGrowRecordService;
import com.hongkun.finance.vas.service.VasVipTreatmentService;
import com.hongkun.finance.vas.utils.VipGrowRecordUtil;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.singleton.SingletonThreadPool;
import com.yirun.framework.core.utils.AppResultUtil;
import com.yirun.framework.core.utils.DateUtils;
import com.yirun.framework.core.utils.json.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.concurrent.Future;

import static com.hongkun.finance.vas.constants.VasConstants.VAS;
import static com.hongkun.finance.vas.constants.VasConstants.VIP_TREATMENT_TYPE;

/**
 * @Description : 会员待遇facade服务实现类
 * @Project : framework
 * @Program Name  : com.hongkun.finance.vas.facade.impl.VasVipTreatmentFacadeImpl
 * @Author : pengwu@hongkun.com.cn 吴鹏
 */
@Service
public class VasVipTreatmentFacadeImpl implements VasVipTreatmentFacade {

    private static final Logger logger = LoggerFactory.getLogger(VasVipTreatmentFacadeImpl.class);
    @Reference
    RegUserDetailService regUserDetailService;
    @Autowired
    VasVipGrowRecordService vasVipGrowRecordService;
    @Autowired
    VasVipGradeStandardService vasVipGradeStandardService;
    @Autowired
    VasVipTreatmentService vasVipTreatmentService;
    @Autowired
    VasCouponDetailService vasCouponDetailService;
    @Reference
    BidReceiptPlanService bidReceiptPlanService;
    @Reference
    BidInvestService bidInvestService;
    @Reference
    RegUserService regUserService;
    @Reference
    DicDataService dicDataService;

    @Override
    public void vipTreatmentPerBirthDay(Date currentDate, int shardingItem) {
        //获取当天过生日的用户信息集合
        List<RegUser> list = regUserDetailService.findRegUserDetailListByBirthDay(currentDate);
        if (list.size() == 0){
            logger.info("vipTreatmentPerBirthDay, 会员生日跑批, 没有发送卡券的用户");
            return;
        }
        //初始化用户id集合
        List<Integer> userIdList = new ArrayList<>();
        list.forEach(regUser -> userIdList.add(regUser.getId()));
        //根据用户id集合获取其当前对应的会员成长值信息Map
        Map<Integer,VasVipGrowRecord> userGrowValueMap = vasVipGrowRecordService.findUserGrowValueMap(userIdList);
        //获取所有会员等级规则集合
        VasVipGradeStandard  condition = new VasVipGradeStandard();
        condition.setState(VasConstants.VAS_STATE_Y);
        List<VasVipGradeStandard> gradeStandardList = vasVipGradeStandardService.findVasVipGradeStandardList(condition);
        //每日生日跑批生成相关的卡券
        generateCouponDetail(list,userGrowValueMap,gradeStandardList,VasVipConstants.VAS_VIP_TREATMENT_TYPE_BIRTH);
    }

    private void generateCouponDetail(List<RegUser> list, Map<Integer,VasVipGrowRecord> userGrowValueMap,
                                 List<VasVipGradeStandard> gradeStandardList, Integer treatmentType){
        //初始化发送短信集合
        List<SmsMsgInfo> msgTelList = new ArrayList<>();
        //初始化发送站内信集合
        List<SmsMsgInfo> msgWebList = new ArrayList<>();
        //根据发送的会员待遇类型获取对应的会员待遇记录
        VasVipTreatment treatmentCondition = new VasVipTreatment();
        treatmentCondition.setState(VasConstants.VAS_STATE_Y);
        treatmentCondition.setType(treatmentType);
        List<VasVipTreatment> treatmentList = vasVipTreatmentService.findVasVipTreatmentList(treatmentCondition);
        try{
            //循环用户集合，进行卡券发送
            list.forEach(regUser -> {
                //获取当前用户成长值
                int currentGrowValue = 0;
                if (userGrowValueMap.get(regUser.getId()) == null){
                    logger.error("generateCouponDetail, 给用户发送卡券, 用户成长值记录未生成, 用户id: {}, 待遇类型: {}",
                            regUser.getId(), treatmentType);
                }else {
                    currentGrowValue = userGrowValueMap.get(regUser.getId()).getCurrentGrowValue();
                }
                int grade = 0 ;
                for (VasVipGradeStandard vipGradeStandard:gradeStandardList){
                    if (vipGradeStandard.getGrowthValMin() <= currentGrowValue && vipGradeStandard.getGrowthValMax() > currentGrowValue){
                        grade = vipGradeStandard.getLevel();
                        break;
                    }
                }
                //会员待遇规则内容
                List<HashMap<String,Object>> contentList = new ArrayList<>();
                //获取用户注册时间，精确至年月日
                Date registDate = DateUtils.parse(DateUtils.format(regUser.getCreateTime(),"yyyy-MM-dd"));
                //根据用户注册时间获取满足条件的会员待遇
                for (VasVipTreatment vipTreatment:treatmentList){
                    if (registDate.compareTo(vipTreatment.getRegistBeginTime()) >= 0 &&
                            registDate.compareTo(vipTreatment.getRegistEndTime()) <= 0){
                        contentList = JsonUtils.json2Object(vipTreatment.getRuleContents(), List.class,null);
                        break;
                    }
                }
                //根据会员待遇规则发送卡券
                for (HashMap<String,Object> rule:contentList){
                    if (Integer.valueOf(rule.get("grade").toString()) == grade && !rule.get("code").equals("-1")){
                        //处理发送短信和站内信逻辑
                        //替换消息模板的参数
                        List<String> args = new ArrayList<>();
                        if (treatmentType == VasVipConstants.VAS_VIP_TREATMENT_TYPE_BIRTH){
                            args.add(rule.get("rulen").toString());
                            args.add(rule.get("rulem").toString());
                            args.add("%");
                            SmsMsgInfo telMsg = new SmsTelMsg(regUser.getLogin(), SmsMsgTemplate.MSG_TREATMENT_BIRTH.getMsg(),
                                    SmsConstants.SMS_TYPE_NOTICE, args.toArray());
                            SmsMsgInfo webMsg = new SmsWebMsg(regUser.getId(), SmsMsgTemplate.MSG_TREATMENT_BIRTH.getTitle(),
                                    SmsMsgTemplate.MSG_TREATMENT_BIRTH.getMsg(), SmsConstants.SMS_TYPE_NOTICE, args.toArray());
                            msgTelList.add(telMsg);
                            msgWebList.add(webMsg);
                        }else {
                            String content = "";
                            if (treatmentType == VasVipConstants.VAS_VIP_TREATMENT_TYPE_CASH_COUPON){
                                content = "免费提现券";
                            }
                            if (treatmentType == VasVipConstants.VAS_VIP_TREATMENT_TYPE_INVEST_RED_PACKET){
                                content = rule.get("rulem").toString() + "元投资红包";
                            }
                            if (treatmentType == VasVipConstants.VAS_VIP_TREATMENT_TYPE_INVEST_INCREASE){
                                content = rule.get("rulem").toString() + "%加息券";
                            }
                            if (treatmentType == VasVipConstants.VAS_VIP_TREATMENT_TYPE_FRIEND_COUPON){
                                content = rule.get("rulem").toString() + "%好友券";
                            }
                            args.add(rule.get("rulen").toString());
                            args.add(content);

                            SmsMsgInfo webMsg = new SmsWebMsg(regUser.getId(), SmsMsgTemplate.MSG_TREATMENT_PER_MONTH.getTitle(),
                                    SmsMsgTemplate.MSG_TREATMENT_PER_MONTH.getMsg(), SmsConstants.SMS_TYPE_NOTICE, args.toArray());
                            msgWebList.add(webMsg);
                        }
                        //给用户生成卡券
                        int num = Integer.valueOf(rule.get("rulen").toString());
                        vasCouponDetailService.generateUserCouponDetail(regUser.getId(),Integer.valueOf(rule.get("code")
                                .toString()),num,dicDataService.findNameByValue(VAS,VIP_TREATMENT_TYPE,treatmentType));
                        break;
                    }
                }
            });
        }catch (Exception e){
            logger.error("generateCouponDetail, 发送卡券异常, 用户集合: {}, 待遇类型: {}", JSON.toJSON(list), treatmentType);
        }
        //给用户发送短信、站内信
        if (msgTelList.size() > 0){
            SmsSendUtil.sendTelMsgToQueue(msgTelList);
        }
        if (msgWebList.size() > 0){
            SmsSendUtil.sendWebMsgToQueue(msgWebList);
        }
    }
    @Override
    public void vipTreatmentPerMonth(Date currentDate, int shardingItem) {
        //1.获取所有可发送卡券的用户
        RegUser condition = new RegUser();
        condition.setState(UserConstants.USER_STATE_Y);
        condition.setType(UserConstants.USER_TYPE_GENERAL);
        List<RegUser> userList = regUserService.findRegUserList(condition);
        List<Integer> userIdList = new ArrayList<>();
        userList.forEach(regUser -> userIdList.add(regUser.getId()));
        //2.根据用户id集合获取用户成长值信息
        Map<Integer,VasVipGrowRecord> userGrowValueMap = vasVipGrowRecordService.findUserGrowValueMap(userIdList);
        //获取所有会员等级规则集合
        VasVipGradeStandard  con = new VasVipGradeStandard();
        con.setState(VasConstants.VAS_STATE_Y);
        List<VasVipGradeStandard> gradeStandardList = vasVipGradeStandardService.findVasVipGradeStandardList(con);
        //3.获取提现优惠、投资加息、投资红包、好友券对应的会员待遇
        //发送提现优惠券:4
        generateCouponDetail(userList,userGrowValueMap,gradeStandardList,VasVipConstants.VAS_VIP_TREATMENT_TYPE_CASH_COUPON);
        //发送投资加息券:1
        generateCouponDetail(userList,userGrowValueMap,gradeStandardList,VasVipConstants.VAS_VIP_TREATMENT_TYPE_INVEST_INCREASE);
        //发送投资红包:7
        generateCouponDetail(userList,userGrowValueMap,gradeStandardList,VasVipConstants.VAS_VIP_TREATMENT_TYPE_INVEST_RED_PACKET);
        //发送好友券:8
        generateCouponDetail(userList,userGrowValueMap,gradeStandardList,VasVipConstants.VAS_VIP_TREATMENT_TYPE_FRIEND_COUPON);
    }

    @Override
    public void vipDownGrade(Date currentDate, int shardingItem) {
        //1.查询用户等级大于0级的用户
        Map<Integer, VasVipGrowRecord> recordMap = vasVipGrowRecordService.findUserLevelGtZeroMap();
        //获取所有用户id集合
        Set<Integer> userIdList = recordMap.keySet();
        //2.根据查询出的用户id集合，近三个月有降级成长值记录的用户
        if (userIdList.size() >0){
            List<Integer> idList = vasVipGrowRecordService.findUserThreeMonthHasDown(userIdList);
            userIdList.removeAll(idList);
        }
        //3.根据查询出的用户id集合，近三个月有回款计划的用户
        if (userIdList.size() > 0){
            List<Integer> receiptIdList = bidReceiptPlanService.findUserThreeMonthPlan(userIdList);
            userIdList.removeAll(receiptIdList);
        }
        //4.根据查询出的用户id集合，近三个月有投资记录的用户
        if (userIdList.size() > 0){
            List<Integer> investIdList = bidInvestService.findUserThreeMonthInvest(userIdList);
            userIdList.removeAll(investIdList);
        }
        if (userIdList.size() == 0){
            logger.info("vipDownGrade, 没有需要进行降级的用户");
            return;
        }
        //5.执行会员降级，等级大于0的用户除去2、3、4获取的用户集合，剩下的用户为需要进行会员降级的用户
        //获取所有会员等级规则集合
        VasVipGradeStandard  condition = new VasVipGradeStandard();
        condition.setState(VasConstants.VAS_STATE_Y);
        List<VasVipGradeStandard> gradeStandardList = vasVipGradeStandardService.findVasVipGradeStandardList(condition);
        //初始化发送短信集合
        List<SmsMsgInfo> msgTelList = new ArrayList<>();
        //初始化发送站内信集合
        List<SmsMsgInfo> msgWebList = new ArrayList<>();

        //初始化发送成长值记录集合
        List<VasVipGrowRecordMqVO> vasVipList = new ArrayList<>();
        for (Integer userId : userIdList) {
            //获取当前用户的成长值
            int currentGrowValue = recordMap.get(userId).getCurrentGrowValue();
            //默认用户等级为0
            int grade = 0;
            for(VasVipGradeStandard vipGradeStandard:gradeStandardList){
                if (vipGradeStandard.getGrowthValMin() <= currentGrowValue && vipGradeStandard.getGrowthValMax() > currentGrowValue){
                    grade = vipGradeStandard.getLevel();
                    break;
                }
            }
            //只有会员等级>0才进行降级操作
            if (grade > 0){
                int growthValMax = 0;
                grade--;
                for (VasVipGradeStandard vipGradeStandard:gradeStandardList){
                    if (vipGradeStandard.getLevel() == grade){
                        growthValMax = vipGradeStandard.getGrowthValMax();
                        break;
                    }
                }
                if (growthValMax > 0 && currentGrowValue > (growthValMax - 1)){
                    //用户需要减少的成长值
                    int growthValue = currentGrowValue - (growthValMax -1);
                    if (growthValue > 0){
                        VasVipGrowRecordMqVO vasVo = new VasVipGrowRecordMqVO();
                        vasVo.setGrowType(VasVipConstants.VAS_VIP_GROW_TYPE_POINT_DEGRADE);
                        vasVo.setUserId(userId);
                        vasVo.setGrowValue(growthValue*-1);
                        vasVipList.add(vasVo);
                        //替换消息模板的参数
                        List<String> args = new ArrayList<>();
                        args.add(String.valueOf(grade));
                        SmsMsgInfo telMsg = new SmsTelMsg(regUserService.findRegUserById(userId).getLogin(),
                                SmsMsgTemplate.MSG_TREATMENT_DOWN_GRADE.getMsg(), SmsConstants.SMS_TYPE_NOTICE, args.toArray());
                        SmsMsgInfo webMsg = new SmsWebMsg(userId, SmsMsgTemplate.MSG_TREATMENT_DOWN_GRADE.getTitle(),
                                SmsMsgTemplate.MSG_TREATMENT_DOWN_GRADE.getMsg(), SmsConstants.SMS_TYPE_NOTICE, args.toArray());
                        msgTelList.add(telMsg);
                        msgWebList.add(webMsg);
                    }else {
                        logger.error("vipDownGrade, 用户降级成长值数值为负数, 用户id: {}, 成长值: {}", userId, growthValue);
                    }
                }
            }
        }
        if (vasVipList.size() > 0){
            VipGrowRecordUtil.sendVipGrowRecordListToQueue(vasVipList);
        }
        //给用户发送短信、站内信
        if (msgTelList.size() > 0){
            SmsSendUtil.sendTelMsgToQueue(msgTelList);
        }
        if (msgWebList.size() > 0){
            SmsSendUtil.sendWebMsgToQueue(msgWebList);
        }
    }

    @Override
    public Map<String, Object> getAllVipTreatMentList(Date registTime) {
        List<Map<String,Object>> dataList = new ArrayList<>();
        try {
            //①根据用户创建时间获取所有的会员待遇信息
            List<VasVipTreatment> treatmentList = vasVipTreatmentService.getVipTreatMentByRegistTime(registTime);
            //②获取所有的会员等级
            List<VasVipGradeStandard> gradeList = vasVipGradeStandardService.getAllGradeList();
            if (gradeList == null || gradeList.size() == 0){
                return AppResultUtil.errorOfMsg("会员等级配置信息异常!");
            }
            //③根据所有的会员等级，获取其对应的会员待遇信息
            for(VasVipGradeStandard gradeStandard:gradeList){
                //存放level:等级信息，content:等级对应的会员待遇信息
                Map<String,Object> data = new HashMap<>(2);
                data.put("level",gradeStandard.getLevel());
                data.put("levelName",dicDataService.findNameByValue(VasConstants.VAS, VasVipConstants
                        .VAS_VIP_GRADE_NAME,gradeStandard.getLevel()));
                //获取level对应的会员待遇信息
                Future<List<Map<String,Object>>> future = SingletonThreadPool.callCachedThreadPool(() -> vasVipTreatmentService.
                        getVipTreatMentListDescriptionByLevel(gradeStandard.getLevel(),treatmentList));
                List<Map<String,Object>> content = future.get();
                content.forEach(map -> {
                    map.put("typeName",dicDataService.findNameByValue(VasConstants.VAS,VasConstants.VIP_TREATMENT_TYPE,(int)map.get("type")));
                });
                data.put("content",content);
                dataList.add(data);
            }
        }catch (Exception e){
            throw new GeneralException("数据加载异常", e);
        }
        return AppResultUtil.successOfList(dataList);
    }
}
