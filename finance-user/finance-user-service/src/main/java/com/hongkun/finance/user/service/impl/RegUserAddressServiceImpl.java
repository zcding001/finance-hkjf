package com.hongkun.finance.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.user.dao.RegUserAddressDao;
import com.hongkun.finance.user.model.RegUserAddress;
import com.hongkun.finance.user.service.RegUserAddressService;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.redis.JedisClusterLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.hongkun.finance.user.constants.UserAddressConstants.ADDRESS_STATE_DEFAULT;
import static com.yirun.framework.core.commons.Constants.*;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.user.service.impl.RegUserAddressServiceImpl.java
 * @Class Name    : RegUserAddressServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class RegUserAddressServiceImpl implements RegUserAddressService {

	private static final Logger logger = LoggerFactory.getLogger(RegUserAddressServiceImpl.class);
	
	/**
	 * RegUserAddressDAO
	 */
	@Autowired
	private RegUserAddressDao regUserAddressDao;
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertRegUserAddress(RegUserAddress regUserAddress) {
		this.regUserAddressDao.save(regUserAddress);
		if(BaseUtil.equelsIntWraperPrimit(regUserAddress.getState(),ADDRESS_STATE_DEFAULT)){
			setDefaultAddress(regUserAddress.getRegUserId(), regUserAddress.getId());
		}
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertRegUserAddressBatch(List<RegUserAddress> list) {
		this.regUserAddressDao.insertBatch(RegUserAddress.class, list);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertRegUserAddressBatch(List<RegUserAddress> list, int count) {
		if(logger.isDebugEnabled()){
			logger.debug("default batch insert size is " + count);
		}
		this.regUserAddressDao.insertBatch(RegUserAddress.class, list, count);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public int updateRegUserAddress(RegUserAddress regUserAddress) {
		return this.regUserAddressDao.update(regUserAddress);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateRegUserAddressBatch(List<RegUserAddress> list, int count) {
		this.regUserAddressDao.updateBatch(RegUserAddress.class, list, count);
	}
	
	@Override
	public RegUserAddress findRegUserAddressById(int id) {
		return this.regUserAddressDao.findByPK(Long.valueOf(id), RegUserAddress.class);
	}
	
	@Override
	public List<RegUserAddress> findRegUserAddressList(RegUserAddress regUserAddress) {
		return this.regUserAddressDao.findByCondition(regUserAddress);
	}
	
	@Override
	public List<RegUserAddress> findRegUserAddressList(RegUserAddress regUserAddress, int start, int limit) {
		return this.regUserAddressDao.findByCondition(regUserAddress, start, limit);
	}
	
	@Override
	public Pager findRegUserAddressList(RegUserAddress regUserAddress, Pager pager) {
		return this.regUserAddressDao.findByCondition(regUserAddress, pager);
	}
	
	@Override
	public int findRegUserAddressCount(RegUserAddress regUserAddress){
		return this.regUserAddressDao.getTotalCount(regUserAddress);
	}
	
	@Override
	public Pager findRegUserAddressList(RegUserAddress regUserAddress, Pager pager, String sqlName){
		return this.regUserAddressDao.findByCondition(regUserAddress, pager, sqlName);
	}
	
	@Override
	public Integer findRegUserAddressCount(RegUserAddress regUserAddress, String sqlName){
		return this.regUserAddressDao.getTotalCount(regUserAddress, sqlName);
	}

	@Override
	public ResponseEntity setDefaultAddress(Integer userId, int addressId) {
		JedisClusterLock jedisLock = new JedisClusterLock();
		String lockKey = LOCK_PREFFIX + RegUserAddressServiceImpl.class.getSimpleName() + userId;
		try {
			//添加redis锁,如果获取锁超时则返回提示信息
			if (!jedisLock.lock(lockKey,LOCK_EXPIRES, Constants.LOCK_WAITTIME)){
				logger.info("用户标识:{},设置默认地址超时，默认地址标识：{}",userId,addressId);
				return new ResponseEntity(ERROR,"设置默认地址超时，请重新操作！");
			}
			int flag = getUserAddressCount(userId,addressId);
			int unFindAddressFlag = 0;
			if (flag == unFindAddressFlag){
				return new ResponseEntity(ERROR,"地址信息不存在！");
			}
			//默认地址状态修改为1
			RegUserAddress condition = new RegUserAddress();
			condition.setRegUserId(userId);
			condition.setState(3);
			List<RegUserAddress> addressList = this.findRegUserAddressList(condition);
			if (addressList != null && addressList.size() > 0){
				RegUserAddress address = new RegUserAddress();
				address.setState(1);
				address.setId(addressList.get(0).getId());
				this.updateRegUserAddress(address);
			}
			//将当前地址状态修改为默认地址
			RegUserAddress update = new RegUserAddress();
			update.setState(3);
			update.setId(addressId);
			this.updateRegUserAddress(update);
		}catch (Exception e){
			logger.info("用户标识:{},设置默认地址时异常，默认地址标识：{}",userId,addressId);
			logger.error(e.getMessage());
			return new ResponseEntity(ERROR,"卡券激活异常，请联系客服人员！");
		}finally {
			jedisLock.freeLock(lockKey);
		}
		return new ResponseEntity(SUCCESS,"修改默认地址成功！");
	}

	@Override
	public ResponseEntity delAddress(Integer userId, int addressId) {
		int flag = getUserAddressCount(userId,addressId);
		if (flag == 0){
			return new ResponseEntity(ERROR,"地址信息不存在！");
		}
		int num = regUserAddressDao.delete(Long.valueOf(addressId),RegUserAddress.class);
		if (num <= 0){
			return new ResponseEntity(ERROR,"地址删除失败！");
		}
		return new ResponseEntity(SUCCESS,"地址删除成功！");
	}

	@Override
	public ResponseEntity updateRegUserAddress(Integer userId, RegUserAddress regUserAddress) {
		int flag = getUserAddressCount(userId,regUserAddress.getId());
		if (flag == 0){
			return new ResponseEntity(ERROR,"地址信息不存在！");
		}
		int num = this.updateRegUserAddress(regUserAddress);
		if (num <= 0){
			return new ResponseEntity(ERROR,"地址更新失败！");
		}
		return new ResponseEntity(SUCCESS,"地址更新成功！");
	}

	private int getUserAddressCount(int userId,int addressId){
		RegUserAddress con = new RegUserAddress();
		con.setRegUserId(userId);
		con.setId(addressId);
		return this.findRegUserAddressCount(con);
	}
}
