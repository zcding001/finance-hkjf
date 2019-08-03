package com.hongkun.finance.invest.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.invest.constants.InvestConstants;
import com.hongkun.finance.invest.dao.BidAutoSchemeDao;
import com.hongkun.finance.invest.model.BidAutoScheme;
import com.hongkun.finance.invest.service.BidAutoSchemeService;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.CommonUtils;
import com.yirun.framework.core.utils.DateUtils;
import com.yirun.framework.core.utils.pager.Pager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * @Project       : invest
 * @Program Name  : com.hongkun.finance.invest.service.impl.BidAutoSchemeServiceImpl.java
 * @Class Name    : BidAutoSchemeServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class BidAutoSchemeServiceImpl implements BidAutoSchemeService {

	private static final Logger logger = LoggerFactory.getLogger(BidAutoSchemeServiceImpl.class);
	
	/**
	 * BidAutoSchemeDAO
	 */
	@Autowired
	private BidAutoSchemeDao bidAutoSchemeDao;
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public ResponseEntity<?> insertBidAutoScheme(BidAutoScheme bidAutoScheme) {
		return this.bidAutoSchemeDao.save(bidAutoScheme) > 0 ? ResponseEntity.SUCCESS : new ResponseEntity<>(Constants.ERROR, "保存失败，请联系管理员");
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertBidAutoSchemeBatch(List<BidAutoScheme> list) {
		this.bidAutoSchemeDao.insertBatch(BidAutoScheme.class, list);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertBidAutoSchemeBatch(List<BidAutoScheme> list, int count) {
		if(logger.isDebugEnabled()){
			logger.debug("default batch insert size is " + count);
		}
		this.bidAutoSchemeDao.insertBatch(BidAutoScheme.class, list, count);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public ResponseEntity<?> updateBidAutoScheme(BidAutoScheme bidAutoScheme) {
		logger.info("updateBidAutoScheme, 更新自动投资配置, 用户: {}, 配置信息: {}", bidAutoScheme.getRegUserId(), bidAutoScheme.toString());
		ResponseEntity<?> result = ResponseEntity.SUCCESS;
		try {
			BidAutoScheme cdt = new BidAutoScheme();
			cdt.setModifyTime(new Date());
			HashMap<String, Object> params = new HashMap<>();
			params.put("currIndex",this.bidAutoSchemeDao.getCurrIndex(cdt) + 1);
			result.setParams(params);
			if(bidAutoScheme.getId() != null && bidAutoScheme.getId() > 0) {
				int i = this.bidAutoSchemeDao.update(bidAutoScheme);
				return i > 0 ? result
						: (bidAutoScheme.getState() != null && bidAutoScheme.getState() == 4)
								? new ResponseEntity<>(Constants.ERROR, "删除失败，请联系管理员")
								: new ResponseEntity<>(Constants.ERROR, "更新失败，请联系管理员");
			}else {
				BidAutoScheme bidAutoSchemeTmp = new BidAutoScheme();
				bidAutoSchemeTmp.setRegUserId(bidAutoScheme.getRegUserId());
				List<BidAutoScheme> list = this.bidAutoSchemeDao.findByCondition(bidAutoSchemeTmp);
				if(list != null && list.size() > 0) {
					return new ResponseEntity<>(Constants.ERROR, "已存在自动投资配置，禁止重复添加!");
				}
				return this.bidAutoSchemeDao.save(bidAutoScheme) > 0 ? result : new ResponseEntity<>(Constants.ERROR, "保存失败，请联系管理员");
			}
		} catch (Exception e) {
			logger.error("updateBidAutoScheme, 更新自动投资配置, 用户: {}, 配置信息: {}\n", bidAutoScheme.getRegUserId(), bidAutoScheme.toString(), e);
			throw new GeneralException("无法更新自动投资操作配置");
		}
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateBidAutoSchemeBatch(List<BidAutoScheme> list, int count) {
		this.bidAutoSchemeDao.updateBatch(BidAutoScheme.class, list, count);
	}
	
	@Override
	public BidAutoScheme findBidAutoSchemeById(int id) {
		return this.bidAutoSchemeDao.findByPK(Long.valueOf(id), BidAutoScheme.class);
	}
	
	@Override
	public List<BidAutoScheme> findBidAutoSchemeList(BidAutoScheme bidAutoScheme) {
		bidAutoScheme.setSortColumns("modify_time ASC");
		return this.bidAutoSchemeDao.findByCondition(bidAutoScheme);
	}
	
	@Override
	public List<BidAutoScheme> findBidAutoSchemeList(BidAutoScheme bidAutoScheme, int start, int limit) {
		return this.bidAutoSchemeDao.findByCondition(bidAutoScheme, start, limit);
	}
	
	@Override
	public Pager findBidAutoSchemeList(BidAutoScheme bidAutoScheme, Pager pager) {
		bidAutoScheme.setSortColumns("modify_time ASC");
		if(bidAutoScheme.getModifyTimeEnd() != null) {
			bidAutoScheme.setModifyTimeEnd(DateUtils.addDays(bidAutoScheme.getModifyTimeEnd(), 1));
		}
		Pager result = this.bidAutoSchemeDao.findByCondition(bidAutoScheme, pager);
		if(result != null && result.getData() != null && !result.getData().isEmpty()) {
			List<BidAutoScheme> list = (List<BidAutoScheme> )result.getData();
			list.stream().forEach(e -> {
				BidAutoScheme cdt = new BidAutoScheme();
				cdt.setModifyTime(e.getModifyTime());
				// 失效不参与排名
				if(e.getState().equals(InvestConstants.AUTO_SCHEME_STATE_FORBIDDEN) ||
						e.getEffectiveType().equals(2) && e.getEffectiveEndTime().before(new Date())){
					e.setCurrIndex(InvestConstants.AUTO_SCHEME_STATE_FORBIDDEN);
				}else{
					e.setCurrIndex(this.bidAutoSchemeDao.getCurrIndex(cdt));
				}

			});
		}
		return result;
	}
	
	@Override
	public int findBidAutoSchemeCount(BidAutoScheme bidAutoScheme){
		return this.bidAutoSchemeDao.getTotalCount(bidAutoScheme);
	}
	
	@Override
	public Pager findBidAutoSchemeList(BidAutoScheme bidAutoScheme, Pager pager, String sqlName){
		return this.bidAutoSchemeDao.findByCondition(bidAutoScheme, pager, sqlName);
	}
	
	@Override
	public Integer findBidAutoSchemeCount(BidAutoScheme bidAutoScheme, String sqlName){
		return this.bidAutoSchemeDao.getTotalCount(bidAutoScheme, sqlName);
	}

	@Override
	public BidAutoScheme findBidAutoSchemeByRegUserId(Integer regUserId) {
		BidAutoScheme bidAutoScheme = new BidAutoScheme();
		bidAutoScheme.setSortColumns("modify_time ASC");
		bidAutoScheme.setRegUserId(regUserId);
		List<BidAutoScheme> list = this.bidAutoSchemeDao.findByCondition(bidAutoScheme);
		if(CommonUtils.isNotEmpty(list)) {
			BidAutoScheme tmp = list.get(0);
			BidAutoScheme cdt = new BidAutoScheme();
			cdt.setModifyTime(tmp.getModifyTime());
			// 失效不参与排名
			if(tmp.getState().equals(InvestConstants.AUTO_SCHEME_STATE_FORBIDDEN) ||
					tmp.getEffectiveType().equals(2) && tmp.getEffectiveEndTime().before(new Date())){
				tmp.setCurrIndex(InvestConstants.AUTO_SCHEME_STATE_FORBIDDEN);
			}else{
				tmp.setCurrIndex(this.bidAutoSchemeDao.getCurrIndex(cdt));
			}
			return tmp;
		}
		return null;
	}

	@Override
	public List<BidAutoScheme> findAvailableBidAutoSchemeList(BidAutoScheme bidAutoScheme) {
		return this.bidAutoSchemeDao.findAvailableBidAutoSchemeList(bidAutoScheme);
	}
}
