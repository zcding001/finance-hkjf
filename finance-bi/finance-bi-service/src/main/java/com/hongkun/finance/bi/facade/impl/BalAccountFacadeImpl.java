package com.hongkun.finance.bi.facade.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.bi.facade.BalAccountFacade;
import com.hongkun.finance.bi.model.BalAccount;
import com.hongkun.finance.bi.model.BalAccountRecord;
import com.hongkun.finance.bi.service.BalAccountRecordService;
import com.hongkun.finance.bi.service.BalAccountService;
import com.hongkun.finance.payment.service.FinFundtransferService;
import com.hongkun.finance.roster.service.RosInfoService;
import com.hongkun.finance.roster.service.RosNoticeService;
import com.hongkun.finance.sms.model.SmsEmailMsg;
import com.hongkun.finance.sms.utils.SmsSendUtil;
import com.hongkun.finance.user.model.vo.UserSimpleVo;
import com.hongkun.finance.user.model.vo.UserVO;
import com.hongkun.finance.user.service.RegUserService;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.CommonUtils;
import com.yirun.framework.core.utils.CompareUtil;
import com.yirun.framework.core.utils.DateUtils;
import com.yirun.framework.core.utils.pager.Pager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.*;
import static com.hongkun.finance.bi.constants.BiConstants.*;
import static com.hongkun.finance.roster.constants.RosterConstants.ROS_NOTICE_TYPE_BAL;
import static com.hongkun.finance.sms.constants.SmsConstants.SMS_TYPE_NOTICE;

/**
 * @Description : 平台对账
 * @Project : finance
 * @Program Name  : com.hongkun.finance.bi.facade.impl
 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
 */
@Service
public class BalAccountFacadeImpl implements BalAccountFacade {

    private static final Logger logger  = LoggerFactory.getLogger(BalAccountFacadeImpl.class);

    @Reference
    private RegUserService regUserService;
    @Reference
    private FinFundtransferService finFundtransferService;
    @Autowired
    private BalAccountRecordService balAccountRecordService;
    @Autowired
    private BalAccountService balAccountService;
    @Reference
    private RosInfoService rosInfoService;
    @Reference
    private RosNoticeService rosNoticeService;
    @Override
    public ResponseEntity<?> initBalAccount(String tel,Integer balanceType,Integer state,Pager pager) {
        logger.info("initBalAccountInActualTime, 用户实时对账: 手机号: {}, 对账类型: {}, 对账状态: {}",tel,balanceType,state );
        if(StringUtils.isBlank(tel)){
            return new ResponseEntity<>(Constants.SUCCESS,new Pager());
        }
        UserVO userVO = regUserService.findUserWithDetailByLogin(Long.valueOf(tel));
        if(userVO==null){
            return new ResponseEntity<>(Constants.ERROR,"用户不存在");
        }
        if (balanceType==BALANCE_TYPE_ACTUALTIME){
            return initBalountInActualTime(state,userVO.getUserId(),String.valueOf(userVO.getLogin()),userVO.getRealName(),pager);
        }
        if(balanceType==BALANCE_TYPE_RECORD){
            //查询此用户对账记录 根据时间倒序
            BalAccountRecord record = new BalAccountRecord();
            record.setTel(tel);
            if (state != null && state != BALANCE_STATE_ALL){
                record.setState(state);
            }
            record.setSortColumns("created_time DESC");
            Pager resultPager = balAccountRecordService.findBalAccountRecordList(record,pager);
            return new ResponseEntity<>(Constants.SUCCESS,resultPager);
        }
        return new ResponseEntity<>(Constants.ERROR,"请选择正确的对账类型");
    }

    /**
    *  @Description    ：实时对账
    *  @Method_Name    ：initBalAccountInActualTime
    *  @param regUserId
    *  @param pager
    *  @return
    *  @Creation Date  ：2018/5/2
    *  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
    */
    private  ResponseEntity<?>  initBalountInActualTime(Integer state,Integer regUserId,String tel, String realName,Pager pager){
        //实时对账
        List<BalAccount> dataList = new ArrayList<BalAccount>();
        Map<String,Object> result =  initBalanceAccountForOneUser(regUserId,tel,realName);
        if (result!=null){
            BalAccount balAccount = (BalAccount) result.get("balAccount");
            if(balAccount==null){
                BalAccount updateAccount = (BalAccount) result.get("updateAccount");
                BalAccount addAccount = (BalAccount) result.get("addAccount");
                BalAccountRecord record = (BalAccountRecord) result.get("record");
                balAccountService.insertBalAccountForBalance(addAccount,updateAccount,record);
                if(addAccount!=null){
                    if (addAccount.getState()==state){
                        dataList.add(addAccount);
                    }
                    pager.setData(dataList);
                    return new ResponseEntity<>(Constants.SUCCESS,pager);
                }
                if (updateAccount!=null){
                    updateAccount.setRegUserId(regUserId);
                    updateAccount.setTel(tel);
                    updateAccount.setRealName(realName);
                    dataList.add(updateAccount);
                    pager.setData(dataList);
                    return new ResponseEntity<>(Constants.SUCCESS,pager);
                }
            }else{
                dataList.add(balAccount);
                pager.setData(dataList);
                return new ResponseEntity<>(Constants.SUCCESS,pager);
            }
        }
        return new ResponseEntity<>(Constants.ERROR,"对账失败");
    }




    /**
    *  @Description    ：实时对账校验用户
    *  @Method_Name    ：validateForBalance
    *  @param account
    *  @return java.util.List<com.hongkun.finance.user.model.vo.UserVO>
    *  @Creation Date  ：2018/4/28
    *  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
    */
    private List<UserVO> validateForBalance(BalAccount account) {
        //必须输入用户手机号或者真实姓名才能查询用户对账信息
        if(StringUtils.isNotBlank(account.getTel())||StringUtils.isNotBlank(account.getRealName())){
            UserVO userVO = new UserVO();
            userVO.setLogin(Long.valueOf(account.getTel()));
            userVO.setRealName(account.getRealName());
            return   regUserService.findUserWithDetailByInfo(userVO);
        }
        return null;
    }

    @Override
    public void initBalAccountForChange() {
        logger.info("initBalAccountForChange 平台对账定时跑批, 跑批时间: {}", DateUtils.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
        //查询账户变动的所有用户id
        List<Integer> regUserIds =  finFundtransferService.findRegUserIdListYestoday();
        //对每一个用户进行对账  updateList  insertList  insertHisttoryList
        if (CommonUtils.isNotEmpty(regUserIds)){
            List<BalAccountRecord> notEqualRecord = new ArrayList<BalAccountRecord>();
            List<UserSimpleVo> userSimpleVos =  regUserService.findUserSimpleVoList(regUserIds);
            List<BalAccount> updateList = new ArrayList<BalAccount>();
            List<BalAccount> addList = new ArrayList<BalAccount>();
            List<BalAccountRecord> recordList = new ArrayList<BalAccountRecord>();
            userSimpleVos.forEach(uservo->{
                Map<String,Object> result = initBalanceAccountForOneUser(uservo.getId(),String.valueOf(uservo.getLogin()),uservo.getRealName());
                if(result!=null && result.get("balAccount") == null){
                    BalAccount updateAccount = (BalAccount) result.get("updateAccount");
                    BalAccount addAccount = (BalAccount) result.get("addAccount");
                    BalAccountRecord record = (BalAccountRecord) result.get("record");
                    if (updateAccount!=null){
                        updateList.add(updateAccount);
                    }
                    if (addAccount!=null){
                        addList.add(addAccount);
                    }
                    recordList.add(record);
                    if (record.getState() == BALANCE_STATE_IS_NOT_EQUAL){
                        notEqualRecord.add(record);
                    }
                }
            });
            //批量操作
            balAccountService.dealBatchForBalance(updateList,addList,recordList);

            if (CommonUtils.isNotEmpty(notEqualRecord)){
                //发送邮件
                StringBuffer message  = new StringBuffer("账户账不平用户如下: ");
                message.append(" =====姓名:====================手机号:====================").append("<br>");
                notEqualRecord.forEach(record->{
                    String realName = StringUtils.isNotBlank(record.getRealName())?record.getRealName():"";
                    message.append("=====").append(realName).append("====================").append(record.getTel()).append("<br>");
                });
                //查询白名单中用户id
                String receiveEmails = rosNoticeService.getEmailsByType(ROS_NOTICE_TYPE_BAL);
                logger.info("initBalAccountForChange, 发送邮件: {}, 邮件信息: {}",receiveEmails,message.toString());
                if(StringUtils.isNotBlank(receiveEmails)){
                    SmsEmailMsg smsMsgInfo = new SmsEmailMsg(0,receiveEmails,"平台对账-昨日对账帐不平用户通知",message.toString(),SMS_TYPE_NOTICE);
                    SmsSendUtil.sendSmsMsgToQueue(smsMsgInfo);
                }
            }
        }


    }
     /**
     *  @Description    ：一次对账--处理单个用户对账记录
     *  @Method_Name    ：initBalanceAccountForOneUser
     *  @param regUserId
     *  @param tel
     *  @param realName
     *  @return java.util.Map<java.lang.String,java.lang.Object>
     *  @Creation Date  ：2018/5/2
     *  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
     */
     private Map<String,Object> initBalanceAccountForOneUser(Integer regUserId, String tel, String realName) {
         try {
             logger.info("initBalanceAccountForOneUser, 处理单个用户对账记录, regUserId: {}, tel: {}, reanName: {}", regUserId, tel, realName);
             Map<String, Object> resultMap = new HashMap<String, Object>();
             BalAccount balAccount = balAccountService.findBalAccountByRegUserId(regUserId);
             ResponseEntity balanceResult = finFundtransferService.findInAndOutMoneyByRegUserId(regUserId);
             BigDecimal inMoney = (BigDecimal) balanceResult.getParams().get("inMoney");
             BigDecimal outMoney = (BigDecimal) balanceResult.getParams().get("outMoney");
             BigDecimal useableMoney = (BigDecimal)balanceResult.getParams().get("useableMoney");
             BigDecimal freezeMoney = (BigDecimal)balanceResult.getParams().get("freezeMoney");
             Integer isEqual = (Integer) balanceResult.getParams().get("isEqual");
             BalAccount updateAccount = null;
             BalAccount addAccount = null;
             if (balAccount != null) {
                 if (CompareUtil.eq(inMoney,balAccount.getInMoneySum()) && CompareUtil.eq(outMoney,balAccount.getOutMoneySum()) && isEqual == balAccount.getState()){
                     //如果账户没动
                     resultMap.put("balAccount", balAccount);
                     return resultMap;
                 }
                 updateAccount = new BalAccount();
                 updateAccount.setId(balAccount.getId());
                 updateAccount.setInMoneySum(inMoney);
                 updateAccount.setOutMoneySum(outMoney);
                 updateAccount.setUseableMoney(useableMoney);
                 updateAccount.setFreezeMoney(freezeMoney);
                 updateAccount.setState(isEqual);
                 updateAccount.setModifiedTime(new Date());
             } else {
                 addAccount = new BalAccount();
                 addAccount.setRegUserId(regUserId);
                 addAccount.setTel(tel);
                 addAccount.setRealName(realName);
                 addAccount.setInMoneySum(inMoney);
                 addAccount.setOutMoneySum(outMoney);
                 addAccount.setUseableMoney(useableMoney);
                 addAccount.setFreezeMoney(freezeMoney);
                 addAccount.setState(isEqual);
                 addAccount.setCreatedTime(new Date());
                 addAccount.setModifiedTime(new Date());
             }
             BalAccountRecord record = new BalAccountRecord();
             record.setRegUserId(regUserId);
             record.setTel(tel);
             record.setRealName(realName);
             record.setInMoneySum(inMoney);
             record.setOutMoneySum(outMoney);
             record.setUseableMoney(useableMoney);
             record.setFreezeMoney(freezeMoney);
             record.setState(isEqual);
             resultMap.put("updateAccount", updateAccount);
             resultMap.put("addAccount", addAccount);
             resultMap.put("record", record);
             return resultMap;
         } catch (Exception e) {
             logger.error("initBalanceAccountForOneUser, 处理单个用户对账记录, regUserId: {}, tel: {}, reanName: {}, 异常信息\n", regUserId, tel, realName,e);
         }
         return null;
     }
}
