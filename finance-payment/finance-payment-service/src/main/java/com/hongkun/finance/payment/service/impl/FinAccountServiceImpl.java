package com.hongkun.finance.payment.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.payment.constant.BankConstants;
import com.hongkun.finance.payment.constant.TradeTransferConstants;
import com.hongkun.finance.payment.dao.FinAccountDao;
import com.hongkun.finance.payment.dao.FinBankCardDao;
import com.hongkun.finance.payment.dao.FinFundtransferDao;
import com.hongkun.finance.payment.enums.FundtransferBigTypeStateEnum;
import com.hongkun.finance.payment.enums.FundtransferSmallTypeStateEnum;
import com.hongkun.finance.payment.model.FinAccount;
import com.hongkun.finance.payment.model.FinBankCard;
import com.hongkun.finance.payment.model.FinFundtransfer;
import com.hongkun.finance.payment.service.FinAccountService;
import com.hongkun.finance.user.constants.UserConstants;
import com.hongkun.finance.user.utils.ValidateUtil;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.CommonUtils;
import com.yirun.framework.core.utils.pager.Pager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hongkun.finance.payment.constant.PaymentConstants.MAKELOAN_BORRORWERACCOUNT;
import static com.hongkun.finance.payment.constant.PaymentConstants.MAKELOAN_RECEIPTACCOUNT;
/**
 * @Project : finance
 * @Program Name :
 *          com.hongkun.finance.payment.service.impl.FinAccountServiceImpl.java
 * @Class Name : FinAccountServiceImpl.java
 * @Description : GENERATOR SERVICE实现类
 * @Author : generator
 */
@Service
public class FinAccountServiceImpl implements FinAccountService {

	final int BATCH_SIZE = 50;

	/**
	 * FinAccountDAO
	 */
	@Autowired
	private FinAccountDao finAccountDao;
	@Autowired
	private FinFundtransferDao finFundtransferDao;
	@Autowired
	private FinBankCardDao finBankCardDao;

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public int insert(FinAccount finAccount) {
		return this.finAccountDao.save(finAccount);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertBatch(List<FinAccount> list) {
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i += BATCH_SIZE) {
				if (list.size() - i >= BATCH_SIZE) {
					this.finAccountDao.insertBatch(FinAccount.class, list.subList(i, i + BATCH_SIZE), BATCH_SIZE);
				} else {
					this.finAccountDao.insertBatch(FinAccount.class, list.subList(i, list.size()), list.size() - i);
				}
			}
		}
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public int update(FinAccount finAccount) {
		return this.finAccountDao.update(finAccount);
	}

	@Override
	public FinAccount findById(int id) {
		return this.finAccountDao.findByPK(new Long(id), FinAccount.class);
	}

	@Override
	public List<FinAccount> findByCondition(FinAccount finAccount) {
		return this.finAccountDao.findByCondition(finAccount);
	}

	@Override
	public List<FinAccount> findByCondition(FinAccount finAccount, int start, int limit) {
		return this.finAccountDao.findByCondition(finAccount, start, limit);
	}

	@Override
	public Pager findByCondition(FinAccount finAccount, Pager pager) {
		return this.finAccountDao.findByCondition(finAccount, pager);
	}

	@Override
	public FinAccount findByRegUserId(Integer regUserId) {
		return this.finAccountDao.findByRegUserId(regUserId);
	}

	@Override
	public void updateFinAccountBatch(List<FinAccount> list, int count) {
		this.finAccountDao.updateBatch(FinAccount.class, list, count);
	}

	@Override
	public int updateFinAccountBatchByUserId(List<FinAccount> list, int count) {
		return this.finAccountDao.updateAccountByUserId(list, count);
	}

	@Override
	public ResponseEntity<?> findBorroweAndReceiveAccount(Integer borrowerId, Integer receiptUserId) {
		FinAccount borrorwerAccount = this.finAccountDao.findByRegUserId(borrowerId);
		FinAccount receiptAccount = CommonUtils.gtZero(receiptUserId)?this.finAccountDao.findByRegUserId(receiptUserId):null;
		Map<String,Object> params = new HashMap<String,Object>();
		params.put(MAKELOAN_BORRORWERACCOUNT, borrorwerAccount);
		params.put(MAKELOAN_RECEIPTACCOUNT, receiptAccount);
		return new ResponseEntity<>(Constants.SUCCESS,"放款查询账户操作成功",params);
	}

	@Override
	public Map<String, Object> findFinAccontInfo(Integer regUserId) {
		Map<String, Object> map = new HashMap<>();
		FinAccount finAccount = this.finAccountDao.findByRegUserId(regUserId);
		map.put("useableMoney", finAccount.getUseableMoney());
		map.put("freezeMoney", finAccount.getFreezeMoney());
		map.put("nowMoney", finAccount.getNowMoney());
		//查询总利息
		FinFundtransfer finFundtransfe = new FinFundtransfer();
		finFundtransfe.setRegUserId(regUserId);
		finFundtransfe.setSubCode(TradeTransferConstants.getFundTransferSubCodeByType(FundtransferBigTypeStateEnum.INCOME,
				FundtransferSmallTypeStateEnum.INTEREST));
		map.put("interest", this.finFundtransferDao.findFintransferSumMoney(finFundtransfe));
		//查询总加息
		finFundtransfe.setSubCode(TradeTransferConstants.getFundTransferSubCodeByType(FundtransferBigTypeStateEnum.INCOME,
				FundtransferSmallTypeStateEnum.INCREASE_INTEREST));
		map.put("increase", this.finFundtransferDao.findFintransferSumMoney(finFundtransfe));
		//绑卡状态
        map.put("bindingState", 0);
        List<FinBankCard> list = this.finBankCardDao.findByRegUserId(regUserId);
        map.put("bindingState", list.stream().anyMatch(o -> BankConstants.BANK_CARD_NO_RZ.equals(o.getState())) ? BankConstants.BANK_CARD_NO_RZ : map.get("bindingState"));
        map.put("bindingState", list.stream().anyMatch(o -> BankConstants.BANK_CARD_YES_RZ.equals(o.getState())) ? BankConstants.BANK_CARD_YES_RZ : map.get("bindingState"));
		return map;
	}

	@Override
	public ResponseEntity<?> setPayPassword(Integer regUserId, String payPassword) {
		//修改支付码 验证条件，原逻辑是根据流水判断原始密码是否与数据库中当前密码相同，或者数据库密码为空时可以修改或者设置支付码，目前暂时省略

		FinAccount finAccount = finAccountDao.findByRegUserId(regUserId);
		if(finAccount == null){
			return new ResponseEntity<>(Constants.ERROR,"账户不存在");
		}

		FinAccount newFinAccount = new FinAccount();
		newFinAccount.setId(finAccount.getId());
		newFinAccount.setPasswd(payPassword);
		int flag= this.finAccountDao.update(newFinAccount);
		if (flag>0){
			return new ResponseEntity<>(Constants.SUCCESS,"设置支付密码成功");
		}else{
			return new ResponseEntity<>(Constants.ERROR,"设置支付密码失败");
		}
	}

	@Override
	public ResponseEntity<?> judgePayPassword(Integer regUserId, String payPassword) {
		FinAccount finAccount = finAccountDao.findByRegUserId(regUserId);
		if(finAccount == null){
			return new ResponseEntity<>(Constants.ERROR,"账户不存在！");
		}
		if (StringUtils.isBlank(finAccount.getPasswd())){
			return new ResponseEntity<>(UserConstants.NO_PAY_PWD,"请先设置支付密码！");
		}
		ResponseEntity result = ValidateUtil.validatePayPasswd(payPassword);
		if (result.getResStatus() == Constants.ERROR){
			return result;
		}
		if (!result.getResMsg().equals(finAccount.getPasswd())){
			return new ResponseEntity<>(UserConstants.ERROR_PAY_PWD,"支付密码输入错误！");
		}
		return new ResponseEntity<>(Constants.SUCCESS,"支付密码正确！");
	}

	@Override
	public List<FinAccount> findFinAccountByRegUserIds(List<Integer> regUserIds) {
		return finAccountDao.findFinAccountByRegUserIds(regUserIds);
	}
}
