package com.hongkun.finance.fund.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.fund.dao.FundAdvisoryDao;
import com.hongkun.finance.fund.dao.FundAgreementDao;
import com.hongkun.finance.fund.dao.FundProjectDao;
import com.hongkun.finance.fund.model.FundAdvisory;
import com.hongkun.finance.fund.model.FundAgreement;
import com.hongkun.finance.fund.model.FundProject;
import com.hongkun.finance.fund.model.vo.FundAdvisoryVo;
import com.hongkun.finance.fund.service.FundAdvisoryService;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.DateUtils;
import com.yirun.framework.core.utils.pager.Pager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Project : finance
 * @Program Name  : com.hongkun.finance.fund.service.impl.FundAdvisoryServiceImpl.java
 * @Class Name    : FundAdvisoryServiceImpl.java
 * @Description : GENERATOR SERVICE实现类
 * @Author : generator
 */
@Service
public class FundAdvisoryServiceImpl implements FundAdvisoryService {

    private static final Logger logger = LoggerFactory.getLogger(FundAdvisoryServiceImpl.class);

    /**
     * FundAdvisoryDAO
     */
    @Autowired
    private FundAdvisoryDao fundAdvisoryDao;

    @Autowired
    private FundProjectDao fundProjectDao;

    @Autowired
    private FundAgreementDao fundAgreementDao;

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public void insertFundAdvisory(FundAdvisory fundAdvisory) {
        if (logger.isInfoEnabled()) {
            logger.info("方法名: insertFundAdvisory, 插入咨询信息: {}", fundAdvisory.toString());
        }
        try {
            this.fundAdvisoryDao.save(fundAdvisory);
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("插入咨询信息: {}, 异常信息: {}", fundAdvisory.toString(), e);
            }
            throw new GeneralException("插入咨询信息出错,请重试");
        }
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public void insertFundAdvisoryBatch(List<FundAdvisory> list) {
        this.fundAdvisoryDao.insertBatch(FundAdvisory.class, list);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public void insertFundAdvisoryBatch(List<FundAdvisory> list, int count) {
        if (logger.isDebugEnabled()) {
            logger.debug("default batch insert size is " + count);
        }
        this.fundAdvisoryDao.insertBatch(FundAdvisory.class, list, count);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public void updateFundAdvisory(FundAdvisory fundAdvisory) {
        if (logger.isInfoEnabled()) {
            logger.info("方法名: updateFundAdvisory, 更新咨询信息, 更新为咨询信息: {}", fundAdvisory.toString());
        }
        try {
            this.fundAdvisoryDao.update(fundAdvisory);
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("更新咨询信息异常, 咨询信息: {}, 异常信息: {}", fundAdvisory.toString(), e);
            }
            throw new GeneralException("更新咨询信息出错,请重试");
        }
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public void updateFundAdvisoryBatch(List<FundAdvisory> list, int count) {
        this.fundAdvisoryDao.updateBatch(FundAdvisory.class, list, count);
    }

    @Override
    public FundAdvisory findFundAdvisoryById(int id) {
        return this.fundAdvisoryDao.findByPK(Long.valueOf(id), FundAdvisory.class);
    }

    @Override
    public List<FundAdvisory> findFundAdvisoryList(FundAdvisory fundAdvisory) {
        return this.fundAdvisoryDao.findByCondition(fundAdvisory);
    }

    @Override
    public List<FundAdvisory> findFundAdvisoryList(FundAdvisory fundAdvisory, int start, int limit) {
        return this.fundAdvisoryDao.findByCondition(fundAdvisory, start, limit);
    }

    @Override
    public Pager findFundAdvisoryList(FundAdvisory fundAdvisory, Pager pager) {
        return this.fundAdvisoryDao.findByCondition(fundAdvisory, pager);
    }

    @Override
    public int findFundAdvisoryCount(FundAdvisory fundAdvisory) {
        return this.fundAdvisoryDao.getTotalCount(fundAdvisory);
    }

    @Override
    public Pager findFundAdvisoryList(FundAdvisory fundAdvisory, Pager pager, String sqlName) {
        return this.fundAdvisoryDao.findByCondition(fundAdvisory, pager, sqlName);
    }

    @Override
    public Integer findFundAdvisoryCount(FundAdvisory fundAdvisory, String sqlName) {
        return this.fundAdvisoryDao.getTotalCount(fundAdvisory, sqlName);
    }

    @Override
    public Pager findFundTypeListWithCondition(FundProject project, Pager pager) {
        return this.fundProjectDao.findFundTypeListWithCondition(project, pager);
    }

    @Override
    public Pager findFundAdvisoryListWithCondition(FundAdvisoryVo advisoryVo, Pager pager) {
        return this.fundAdvisoryDao.findFundAdvisoryListWithCondition(advisoryVo, pager);
    }

    @Override
    public int updateFundAdvisoryForSale(FundAdvisory advisory) {
        if (logger.isInfoEnabled()) {
            logger.info("方法名: updateFundAdvisoryForSale, 更新咨询信息, 更新为咨询信息: {}", advisory.toString());
        }
        try {
            return this.fundAdvisoryDao.update(advisory);
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("更新咨询信息异常, 咨询信息: {}, 异常信息: {}", advisory.toString(), e);
            }
            throw new GeneralException("更新咨询信息出错,请重试");
        }
    }

    @Override
    public int findAdvisoryCount(Integer id,Integer parentType) {
        Map<String,Object> params = new HashMap<>(16);
        params.put("regUserId",id);
        params.put("startTime", DateUtils.getFirstTimeOfDay());
        params.put("endTime", DateUtils.getLastTimeOfDay());
        params.put("projectParentType",parentType);
        return this.fundAdvisoryDao.findAdvisoryCount(params);
    }

    @Override
    public Integer findProjectParentTypeByType(Integer projectId) {
        FundProject fundProject = new FundProject();
        fundProject.setType(projectId);
        Pager result = this.fundProjectDao.findByCondition(fundProject, new Pager());
        if (!(result == null || result.getData() == null || result.getData().size() == 0)) {
            FundProject project = (FundProject) result.getData().get(0);
            return project.getParentType();
        }
        return 0;
    }

    @Override
    public FundProject findFundProjectInfo(Integer id) {
        return fundProjectDao.findByPK(Long.valueOf(id),FundProject.class);
    }

    @Override
    public ResponseEntity findFundAdvisoryInfo(Integer id) {
        FundAgreement agreement = new FundAgreement();
        agreement.setId(id);
        List<FundAgreement> list = fundAgreementDao.findByCondition(agreement);
        if(!CollectionUtils.isEmpty(list)){
            return new ResponseEntity<>(Constants.SUCCESS,list.get(0));
        }
        return new ResponseEntity<>(Constants.ERROR,"获取详情失败！");
    }

    @Override
    public int findFundAdvisoryCount(Integer regUserId, Integer infoId, Integer projectParentType) {
        Map<String,Object> params = new HashMap<>(16);
        params.put("regUserId",regUserId);
        params.put("infoIds", infoId);
        params.put("projectParentType",projectParentType);
        return this.fundAdvisoryDao.findAdvisoryCount(params);
    }

}
