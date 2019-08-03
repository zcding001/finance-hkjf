package com.hongkun.finance.activity.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.activity.dao.LotteryItemDao;
import com.hongkun.finance.activity.model.LotteryItem;
import com.hongkun.finance.activity.model.vo.LotteryActivityItemsVo;
import com.hongkun.finance.activity.service.LotteryItemService;
import com.yirun.framework.core.utils.pager.Pager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.activity.service.impl.LotteryItemServiceImpl.java
 * @Class Name    : LotteryItemServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class LotteryItemServiceImpl implements LotteryItemService {

	private static final Logger logger = LoggerFactory.getLogger(LotteryItemServiceImpl.class);
	
	/**
	 * LotteryItemDAO
	 */
	@Autowired
	private LotteryItemDao lotteryItemDao;


	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertLotteryItem(LotteryItem lotteryItem) {
		this.lotteryItemDao.save(lotteryItem);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertLotteryItemBatch(List<LotteryItem> list) {
		this.lotteryItemDao.insertBatch(LotteryItem.class, list);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertLotteryItemBatch(List<LotteryItem> list, int count) {
		if(logger.isDebugEnabled()){
			logger.debug("default batch insert size is " + count);
		}
		this.lotteryItemDao.insertBatch(LotteryItem.class, list, count);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateLotteryItem(LotteryItem lotteryItem) {
		this.lotteryItemDao.update(lotteryItem);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateLotteryItemBatch(List<LotteryItem> list, int count) {
		this.lotteryItemDao.updateBatch(LotteryItem.class, list, count);
	}
	
	@Override
	public LotteryItem findLotteryItemById(int id) {
		return this.lotteryItemDao.findByPK(Long.valueOf(id), LotteryItem.class);
	}
	
	@Override
	public List<LotteryItem> findLotteryItemList(LotteryItem lotteryItem) {
		return this.lotteryItemDao.findByCondition(lotteryItem);
	}
	
	@Override
	public List<LotteryItem> findLotteryItemList(LotteryItem lotteryItem, int start, int limit) {
		return this.lotteryItemDao.findByCondition(lotteryItem, start, limit);
	}
	
	@Override
	public Pager findLotteryItemList(LotteryItem lotteryItem, Pager pager) {
		return this.lotteryItemDao.findByCondition(lotteryItem, pager);
	}
	
	@Override
	public int findLotteryItemCount(LotteryItem lotteryItem){
		return this.lotteryItemDao.getTotalCount(lotteryItem);
	}
	
	@Override
	public Pager findLotteryItemList(LotteryItem lotteryItem, Pager pager, String sqlName){
		return this.lotteryItemDao.findByCondition(lotteryItem, pager, sqlName);
	}
	
	@Override
	public Integer findLotteryItemCount(LotteryItem lotteryItem, String sqlName){
		return this.lotteryItemDao.getTotalCount(lotteryItem, sqlName);
	}

    @Override
    public List<LotteryActivityItemsVo> getLotteryActivityItems(Integer lotteryActivityId) {
		List<LotteryActivityItemsVo> result = new ArrayList<>();
		List<Integer> lotsGroups = lotteryItemDao.getLotteryItemsGroupByActivityId(lotteryActivityId);
		List<Integer> locationList = lotteryItemDao.getLotteryItemsLocationFlagActivityId(lotteryActivityId);
		for (Integer lotsGroup : lotsGroups) {
			for (Integer location : locationList) {
				LotteryItem lotteryItem = new LotteryItem();
				lotteryItem.setLotteryActivityId(lotteryActivityId);
				lotteryItem.setItemGroup(lotsGroup);
				lotteryItem.setLocationFlag(location);
				lotteryItem.setState(1);
				lotteryItem.setSortColumns("create_time asc");
				List<LotteryItem> items = lotteryItemDao.findByCondition(lotteryItem);
				if(!CollectionUtils.isEmpty(items)){
					LotteryActivityItemsVo vo = new LotteryActivityItemsVo(lotsGroup, location,items);
					result.add(vo);
				}

			}
		}
		return result;
    }

	@Override
	public void deleteLotteryItemsById(Integer tempId, int state) {
		LotteryItem lotteryItem = new LotteryItem();
		lotteryItem.setId(tempId);
		lotteryItem.setState(state);
		this.updateLotteryItem(lotteryItem);
	}

	@Override
	public void deleteLotteryItemsByGroupAndLocationFlag(Integer activityId, Integer tempDelLotsGroup, Integer tempDelLocationFlag, int state) {
		Map<String,Object> params = new HashMap<>();
		params.put("lotteryActivityId",activityId);
		params.put("itemGroup",tempDelLotsGroup);
		params.put("locationFlag",tempDelLocationFlag);
		params.put("state",state);
		lotteryItemDao.deleteLotteryItemsByGroupAndLocationFlag(params);
	}

    @Override
    public List<Integer> getLotteryItemGroupById(Integer id) {
        return lotteryItemDao.getLotteryItemsGroupByActivityId(id);
    }

	@Override
	public List<Integer> getLotteryItemLocationFlagById(Integer id) {
		return lotteryItemDao.getLotteryItemsLocationFlagActivityId(id);
	}

}
