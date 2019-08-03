package com.hongkun.finance.payment.service.impl;

import java.util.List;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.payment.constant.BankConstants;
import com.hongkun.finance.payment.constant.TradeStateConstants;
import com.hongkun.finance.payment.model.FinBankCard;
import com.hongkun.finance.payment.service.FinBankCardBindingService;
import com.hongkun.finance.payment.service.FinBankCardService;
import com.hongkun.finance.sms.constants.SmsConstants;
import com.hongkun.finance.sms.model.SmsWebMsg;
import com.hongkun.finance.sms.utils.SmsSendUtil;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.model.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yirun.framework.core.utils.pager.Pager;

import com.hongkun.finance.payment.model.FinBankCardUpdate;

import com.alibaba.dubbo.config.annotation.Service;

import com.hongkun.finance.payment.dao.FinBankCardUpdateDao;
import com.hongkun.finance.payment.service.FinBankCardUpdateService;
import org.springframework.util.CollectionUtils;

import static com.yirun.framework.core.commons.Constants.ERROR;

/**
 * @Project : finance
 * @Program Name  : com.hongkun.finance.payment.service.impl.FinBankCardUpdateServiceImpl.java
 * @Class Name    : FinBankCardUpdateServiceImpl.java
 * @Description : GENERATOR SERVICE实现类
 * @Author : generator
 */
@Service
public class FinBankCardUpdateServiceImpl implements FinBankCardUpdateService {

    private static final Logger logger = LoggerFactory.getLogger(FinBankCardUpdateServiceImpl.class);

    /**
     * FinBankCardUpdateDAO
     */
    @Autowired
    private FinBankCardUpdateDao finBankCardUpdateDao;

    @Reference
    private FinBankCardService finBankCardService;

    @Reference
    private FinBankCardBindingService finBankCardBindingService;

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public void insertFinBankCardUpdate(FinBankCardUpdate finBankCardUpdate) {
        if (logger.isInfoEnabled()) {
            logger.info("方法名: insertFinBankCardUpdate, 审核解绑银行卡信息: {}", finBankCardUpdate.toString());
        }
        try {
            this.finBankCardUpdateDao.save(finBankCardUpdate);
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("申请解绑银行卡信息: {} 异常信息: {}", finBankCardUpdate.toString(), e);
            }
            throw new GeneralException("申请解绑银行卡信息失败,请重试");
        }

    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public void insertFinBankCardUpdateBatch(List<FinBankCardUpdate> list) {
        this.finBankCardUpdateDao.insertBatch(FinBankCardUpdate.class, list);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public void insertFinBankCardUpdateBatch(List<FinBankCardUpdate> list, int count) {
        if (logger.isDebugEnabled()) {
            logger.debug("default batch insert size is " + count);
        }
        this.finBankCardUpdateDao.insertBatch(FinBankCardUpdate.class, list, count);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public void updateFinBankCardUpdate(FinBankCardUpdate finBankCardUpdate) {
        this.finBankCardUpdateDao.update(finBankCardUpdate);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public void updateFinBankCardUpdateBatch(List<FinBankCardUpdate> list, int count) {
        this.finBankCardUpdateDao.updateBatch(FinBankCardUpdate.class, list, count);
    }

    @Override
    public FinBankCardUpdate findFinBankCardUpdateById(int id) {
        return this.finBankCardUpdateDao.findByPK(Long.valueOf(id), FinBankCardUpdate.class);
    }

    @Override
    public List<FinBankCardUpdate> findFinBankCardUpdateList(FinBankCardUpdate finBankCardUpdate) {
        return this.finBankCardUpdateDao.findByCondition(finBankCardUpdate);
    }

    @Override
    public List<FinBankCardUpdate> findFinBankCardUpdateList(FinBankCardUpdate finBankCardUpdate, int start, int limit) {
        return this.finBankCardUpdateDao.findByCondition(finBankCardUpdate, start, limit);
    }

    @Override
    public Pager findFinBankCardUpdateList(FinBankCardUpdate finBankCardUpdate, Pager pager) {
        return this.finBankCardUpdateDao.findByCondition(finBankCardUpdate, pager);
    }

    @Override
    public int findFinBankCardUpdateCount(FinBankCardUpdate finBankCardUpdate) {
        return this.finBankCardUpdateDao.getTotalCount(finBankCardUpdate);
    }

    @Override
    public Pager findFinBankCardUpdateList(FinBankCardUpdate finBankCardUpdate, Pager pager, String sqlName) {
        return this.finBankCardUpdateDao.findByCondition(finBankCardUpdate, pager, sqlName);
    }

    @Override
    public Integer findFinBankCardUpdateCount(FinBankCardUpdate finBankCardUpdate, String sqlName) {
        return this.finBankCardUpdateDao.getTotalCount(finBankCardUpdate, sqlName);
    }

    @Override
    public Pager findBankCardUpdateList(FinBankCardUpdate finBankCardUpdate, Pager pager) {
        return this.finBankCardUpdateDao.findBankCardUpdateList(finBankCardUpdate, pager);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public ResponseEntity<?> auditBankCardUpdate(FinBankCardUpdate finBankCardUpdate) {
        if (logger.isInfoEnabled()) {
            logger.info("方法名: auditBankCardUpdate, 审核解绑银行卡信息: {}", finBankCardUpdate.toString());
        }
        try {
            FinBankCardUpdate cardUpdate = finBankCardUpdateDao.findByPK(Long.valueOf(finBankCardUpdate.getId()), FinBankCardUpdate.class);
            if (cardUpdate == null) {
                return new ResponseEntity<>(ERROR, "审核信息不存在!");
            }
            int update = this.finBankCardUpdateDao.update(finBankCardUpdate);
            if(update <= 0){
                logger.error("申请记录更新失败: {}, 异常信息: {}", finBankCardUpdate.toString());
                throw new GeneralException("审核银行卡信息失败,请重试");
            }
            // 审核通过
            if (finBankCardUpdate.getState() == BankConstants.BANK_UPDATE_AUDIT_PASS) {
                FinBankCard bankCard = new FinBankCard();
                bankCard.setRegUserId(cardUpdate.getRegUserId());
                bankCard.setBankCard(cardUpdate.getBankCard());
                bankCard.setState(TradeStateConstants.BANK_CARD_STATE_AUTH);
                List<FinBankCard> cardList = finBankCardService.findByCondition(bankCard);
                if (CollectionUtils.isEmpty(cardList)) {
                    return new ResponseEntity<>(ERROR, "银行卡信息不存在!");
                }
                bankCard = cardList.get(0);
                bankCard.setState(TradeStateConstants.BANK_CARD_STATE_AUTH_FORBIDDEN);
                // 更新银行卡信息
                int cardUpdateCount = finBankCardService.updateForUnBinding(bankCard);
                int cardBindingCount = finBankCardBindingService.updateFinBankCardBindingByCardId(bankCard.getId(), TradeStateConstants.BANK_CARD_STATE_AUTH_FORBIDDEN);
                if (cardUpdateCount > 0 && cardBindingCount > 0) {
                    // 发送站内信
                    String title = "银行卡审核通过";
                    String msg = "您提交的更换银行卡申请已审核通过，可以到“银行卡管理”修改银行卡信息。";
                    SmsWebMsg smsWebMsg = new SmsWebMsg(cardUpdate.getRegUserId(),title,msg,SmsConstants.SMS_TYPE_NOTICE);
                    SmsSendUtil.sendSmsMsgToQueue(smsWebMsg);
                }else{
                    logger.error("更新银行卡或第三方协议失败: {}, 异常信息: {}", finBankCardUpdate.toString());
                    throw new GeneralException("审核银行卡信息失败,请重试");
                }

            //审核拒绝
            } else if (finBankCardUpdate.getState() == BankConstants.BANK_UPDATE_AUDIT_REJECT) {
                // 发送站内信
                String title = "银行卡审核拒绝";
                String msg = "您提交的更换银行卡申请未通过审核，可以到“银行卡管理”查看原因或者再次提交申请。";
                SmsWebMsg smsWebMsg = new SmsWebMsg(cardUpdate.getRegUserId(),title,msg,SmsConstants.SMS_TYPE_NOTICE);
                SmsSendUtil.sendSmsMsgToQueue(smsWebMsg);
            }
            return new ResponseEntity<>(Constants.SUCCESS,"审核成功！");
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("审核解绑银行卡信息: {}, 异常信息: {}", finBankCardUpdate.toString(), e);
            }
            throw new GeneralException("审核银行卡信息失败,请重试");
        }


    }

    @Override
    public FinBankCardUpdate findBankCardUpdateInfo(Integer id) {
        return this.finBankCardUpdateDao.findByPK(Long.valueOf(id), FinBankCardUpdate.class);
    }

    @Override
    public FinBankCardUpdate findFinBankCardUpdateByBankIdAndUserId(Integer userId, Integer bankCardId) {
        List<FinBankCardUpdate> list = this.finBankCardUpdateDao.findFinBankCardUpdateByBankIdAndUserId(userId, bankCardId);
        if(!CollectionUtils.isEmpty(list)){
            return list.get(0);
        }
        return null;
    }

}
