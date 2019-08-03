package com.hongkun.finance.info.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.info.constant.InfomationConstants;
import com.hongkun.finance.info.dao.InfoInformationNewsDao;
import com.hongkun.finance.info.enums.InfomationNewsEnum;
import com.hongkun.finance.info.model.InfoInformationNews;
import com.hongkun.finance.info.service.InfoInformationNewsService;
import com.hongkun.finance.info.vo.InformationVo;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.utils.pager.Pager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Project : finance
 * @Program Name :
 *          com.hongkun.finance.info.service.impl.InfoInformationNewsServiceImpl.
 *          java
 * @Class Name : InfoInformationNewsServiceImpl.java
 * @Description : GENERATOR SERVICE实现类
 * @Author : generator
 */
@Service
public class InfoInformationNewsServiceImpl implements InfoInformationNewsService {

	private static final Logger logger = LoggerFactory.getLogger(InfoInformationNewsServiceImpl.class);

	/**
	 * InfoInformationNewsDAO
	 */
	@Autowired
	private InfoInformationNewsDao infoInformationNewsDao;

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public int insertInfoInformationNews(InfoInformationNews infoInformationNews) {
		return this.infoInformationNewsDao.save(infoInformationNews);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertInfoInformationNewsBatch(List<InfoInformationNews> list) {
		this.infoInformationNewsDao.insertBatch(InfoInformationNews.class, list);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertInfoInformationNewsBatch(List<InfoInformationNews> list, int count) {
		if (logger.isDebugEnabled()) {
			logger.debug("default batch insert size is " + count);
		}
		this.infoInformationNewsDao.insertBatch(InfoInformationNews.class, list, count);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public int updateInfoInformationNews(InfoInformationNews infoInformationNews) {
		return this.infoInformationNewsDao.update(infoInformationNews);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateInfoInformationNewsBatch(List<InfoInformationNews> list, int count) {
		this.infoInformationNewsDao.updateBatch(InfoInformationNews.class, list, count);
	}

	@Override
	public InfoInformationNews findInfoInformationNewsById(int id) {
		return this.infoInformationNewsDao.findByPK(Long.valueOf(id), InfoInformationNews.class);
	}

	@Override
	public List<InfoInformationNews> findInfoInformationNewsList(InfoInformationNews infoInformationNews) {
		return this.infoInformationNewsDao.findByCondition(infoInformationNews);
	}

	@Override
	public List<InfoInformationNews> findInfoInformationNewsList(InfoInformationNews infoInformationNews, int start,
			int limit) {
		return this.infoInformationNewsDao.findByCondition(infoInformationNews, start, limit);
	}

	@Override
	public Pager findInfoInformationNewsList(InfoInformationNews infoInformationNews, Pager pager) {
		return this.infoInformationNewsDao.findByCondition(infoInformationNews, pager);
	}

	@Override
	public int findInfoInformationNewsCount(InfoInformationNews infoInformationNews) {
		return this.infoInformationNewsDao.getTotalCount(infoInformationNews);
	}

	@Override
	public List<InfoInformationNews> findInformationNewsByTypeAndState(InfomationNewsEnum infomationNewsEnum,
			int state) {
		InfoInformationNews infoInformationNews = new InfoInformationNews();
		infoInformationNews.setState(state);
		infoInformationNews.setType(infomationNewsEnum.getInfomationType());
		return this.infoInformationNewsDao.findByCondition(infoInformationNews);
	}

	@Override
	public int updateInfomationStateById(int id, int state) {
		logger.info("方法: updateInfomationStateById, 通过资讯ID,更新资讯状态, 入参: id: {}, state: {}", id, state);
		try {
			InfoInformationNews infoInformationNews = new InfoInformationNews();
			infoInformationNews.setState(state);
			infoInformationNews.setId(id);
			return this.infoInformationNewsDao.update(infoInformationNews);
		} catch (Exception e) {
			logger.error("通过资讯ID,更新资讯状态, 资讯ID: {}, 更新失败: ", id, e);
			throw new GeneralException("通过资讯ID更新资讯状态失败！");
		}
	}

	@Override
	public int insertInfomationsNews(InformationVo informationCondition) {
		logger.info("方法: insertInfomationsNews, 插入资讯信息, 入参: informationCondition: {}", informationCondition.toString());
		try {
			InfoInformationNews infoInformationNews = new InfoInformationNews();
			if (informationCondition.getType() != null) {
				infoInformationNews.setType(informationCondition.getType());
			}
			if (informationCondition.getResideSelect() != null) {
				infoInformationNews.setResideSelect(informationCondition.getResideSelect());
			}
			if (StringUtils.isNotBlank(informationCondition.getKeyword())) {
				infoInformationNews.setKeyword(informationCondition.getKeyword());
			}
			if (StringUtils.isNotBlank(informationCondition.getTitle())) {
				infoInformationNews.setTitle(informationCondition.getTitle());
			}
			if (StringUtils.isBlank(informationCondition.getContent())) {
				infoInformationNews.setContent("<span></span>");
			} else {
				infoInformationNews.setContent(informationCondition.getContent());
			}
			if (StringUtils.isNotBlank(informationCondition.getSource())) {
				infoInformationNews.setSource(informationCondition.getSource());
			}
			if (StringUtils.isNotBlank(informationCondition.getUrl())) {
				infoInformationNews.setUrl(informationCondition.getUrl());
			}
			infoInformationNews.setImgUrl(informationCondition.getImgUrl());
			infoInformationNews.setPosition(informationCondition.getPosition());
			infoInformationNews.setChannel(informationCondition.getChannel());
			infoInformationNews.setState(InfomationConstants.INFO_NORMAL);
			infoInformationNews.setSort(informationCondition.getSort());
			infoInformationNews.setShowFlag(informationCondition.getShowFlag());
			return this.infoInformationNewsDao.save(infoInformationNews);
		} catch (Exception e) {
			logger.error("插入资讯信息, 保存失败: ", e);
			throw new GeneralException("保存失败!");
		}

	}

	@Override
	public int updateInfomationsNews(InformationVo informationCondition) {
		logger.info("方法: updateInfomationsNews, 更新资讯信息, 入参: informationCondition: {}", informationCondition.toString());
		try {
			InfoInformationNews infoInformationNews = new InfoInformationNews();
			if (informationCondition.getType() != null) {
				infoInformationNews.setType(informationCondition.getType());
			}
			if (informationCondition.getResideSelect() != null) {
				infoInformationNews.setResideSelect(informationCondition.getResideSelect());
			}
			if (StringUtils.isNotBlank(informationCondition.getKeyword())) {
				infoInformationNews.setKeyword(informationCondition.getKeyword());
			}
			if (StringUtils.isNotBlank(informationCondition.getTitle())) {
				infoInformationNews.setTitle(informationCondition.getTitle());
			}
			if (StringUtils.isNotBlank(informationCondition.getContent())) {
				infoInformationNews.setContent(informationCondition.getContent());
			} else {
				infoInformationNews.setContent("<span></span>");
			}
			if (StringUtils.isNotBlank(informationCondition.getSource())) {
				infoInformationNews.setSource(informationCondition.getSource());
			}
			//if (StringUtils.isNotBlank(informationCondition.getUrl())) {
			if (informationCondition.getUrl() != null) {
				infoInformationNews.setUrl(informationCondition.getUrl());
			}
			infoInformationNews.setImgUrl(informationCondition.getImgUrl());
			infoInformationNews.setId(informationCondition.getInfomationId());
			infoInformationNews.setPosition(informationCondition.getPosition());
			infoInformationNews.setChannel(informationCondition.getChannel());
			infoInformationNews.setSort(informationCondition.getSort());
			infoInformationNews.setShowFlag(informationCondition.getShowFlag());
			return this.infoInformationNewsDao.update(infoInformationNews);
		} catch (Exception e) {
			logger.error("更新资讯信息, 更新失败: ", e);
			throw new GeneralException("更新资讯信息失败!");
		}
	}

	@Override
	public int updateInfomationById(int randomNum, int id, int type) {
		logger.info("方法: updateInfomationById, 通过资讯ID,更新资讯信息, 入参: id: {}, type: {}, randomNum: {}", id, type,
				randomNum);
		try {
			InfoInformationNews infoInformationNews = new InfoInformationNews();
			infoInformationNews.setId(id);
			if (type == InfomationConstants.EULOGIZE_NUM) {
				infoInformationNews.setEulogizeNum(randomNum);
			} else {
				infoInformationNews.setBrowseNum(randomNum);
			}
			return this.infoInformationNewsDao.update(infoInformationNews);
		} catch (Exception e) {
			logger.error("通过资讯ID,更新资讯信息, 资讯ID: {}, 更新失败: ", id, e);
			throw new GeneralException("通过资讯ID,更新资讯信息失败！");
		}
	}

	@Override
	public Pager findByCondition(List<Integer> type, InfoInformationNews infoInformationNews, Pager pager) {
		return this.infoInformationNewsDao.findByCondition(type, infoInformationNews, pager);
	}
}
