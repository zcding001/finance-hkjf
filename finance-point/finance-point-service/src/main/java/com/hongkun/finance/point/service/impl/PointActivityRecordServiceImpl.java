package com.hongkun.finance.point.service.impl;

import static com.yirun.framework.core.commons.Constants.SUCCESS;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.BeanPropertiesUtil;
import com.yirun.framework.core.utils.pager.Pager;

import com.hongkun.finance.point.model.PointActivityRecord;
import com.hongkun.finance.point.model.PointProduct;
import com.hongkun.finance.point.model.PointProductCategory;
import com.hongkun.finance.point.model.vo.PointProductVO;
import com.alibaba.dubbo.config.annotation.Service;

import com.hongkun.finance.point.dao.PointActivityRecordDao;
import com.hongkun.finance.point.dao.PointProductCategoryDao;
import com.hongkun.finance.point.dao.PointProductDao;
import com.hongkun.finance.point.service.PointActivityRecordService;
import com.hongkun.finance.point.support.CategoryComponent;
import com.hongkun.finance.point.support.CategoryIterator;
import com.hongkun.finance.point.util.CategoryBuilder;
import com.hongkun.finance.user.utils.BaseUtil;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.point.service.impl.PointActivityRecordServiceImpl.java
 * @Class Name    : PointActivityRecordServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class PointActivityRecordServiceImpl implements PointActivityRecordService {

	private static final Logger logger = LoggerFactory.getLogger(PointActivityRecordServiceImpl.class);
	
	/**
	 * PointActivityRecordDAO
	 */
	@Autowired
	private PointActivityRecordDao pointActivityRecordDao;
	@Autowired
    private PointProductDao pointProductDao;
	@Autowired
    private PointProductCategoryDao pointProductCategoryDao;
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertPointActivityRecord(PointActivityRecord pointActivityRecord) {
		this.pointActivityRecordDao.save(pointActivityRecord);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertPointActivityRecordBatch(List<PointActivityRecord> list) {
		this.pointActivityRecordDao.insertBatch(PointActivityRecord.class, list);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertPointActivityRecordBatch(List<PointActivityRecord> list, int count) {
		if(logger.isDebugEnabled()){
			logger.debug("default batch insert size is " + count);
		}
		this.pointActivityRecordDao.insertBatch(PointActivityRecord.class, list, count);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updatePointActivityRecord(PointActivityRecord pointActivityRecord) {
		this.pointActivityRecordDao.update(pointActivityRecord);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updatePointActivityRecordBatch(List<PointActivityRecord> list, int count) {
		this.pointActivityRecordDao.updateBatch(PointActivityRecord.class, list, count);
	}
	
	@Override
	public PointActivityRecord findPointActivityRecordById(int id) {
		return this.pointActivityRecordDao.findByPK(Long.valueOf(id), PointActivityRecord.class);
	}
	
	@Override
	public List<PointActivityRecord> findPointActivityRecordList(PointActivityRecord pointActivityRecord) {
		return this.pointActivityRecordDao.findByCondition(pointActivityRecord);
	}
	
	@Override
	public List<PointActivityRecord> findPointActivityRecordList(PointActivityRecord pointActivityRecord, int start, int limit) {
		return this.pointActivityRecordDao.findByCondition(pointActivityRecord, start, limit);
	}
	
	@Override
	public Pager findPointActivityRecordList(PointActivityRecord pointActivityRecord, Pager pager) {
		return this.pointActivityRecordDao.findByCondition(pointActivityRecord, pager);
	}
	
	@Override
	public int findPointActivityRecordCount(PointActivityRecord pointActivityRecord){
		return this.pointActivityRecordDao.getTotalCount(pointActivityRecord);
	}

    @Override
    public ResponseEntity<?> findSellingRecordList(Integer id, Pager pager) {
        PointActivityRecord pointActivityRecord=new PointActivityRecord();
        pointActivityRecord.setProductId(id);
        Pager pageInfo = pointActivityRecordDao.findByCondition(pointActivityRecord, pager);
        PointProduct pointProduct = pointProductDao.findByPK(Long.valueOf(id), PointProduct.class);
        List<PointProductCategory> allCategories = this.pointProductCategoryDao.findByCondition(new PointProductCategory());
        //step 1:所有的分类
         CategoryBuilder categoryBuilder = new CategoryBuilder(new HashSet<>(allCategories));
        //step 2:递归构造父节点
        CategoryComponent singleNod = categoryBuilder
                 .buildSingleTreeFormChild(categoryBuilder.buildSingleNode(pointProductCategoryDao.findByPK(Long.valueOf(pointProduct.getProductCategoryId()), PointProductCategory.class)));
        List<PointActivityRecord> repalceVOs = new ArrayList<PointActivityRecord>();
        if (!BaseUtil.resultPageHasNoData(pageInfo)) {
            repalceVOs.addAll(
                    pageInfo.getData().stream().map((productRecord) -> {
                        PointActivityRecord newPointProduct = PointActivityRecord.class.cast(productRecord);
                        //拆分属性
                        PointActivityRecord transferVO = new PointActivityRecord();
                        BeanPropertiesUtil.splitProperties(newPointProduct, transferVO);
                        transferVO.setProductName(pointProduct.getName());
                        //构建目录名称
                        transferVO.setProductCategoryName(constructCateStr(singleNod, new StringBuffer()));
                        return transferVO;
                    }).collect(Collectors.toList()));
        }
        pageInfo.setData(repalceVOs);
        return new ResponseEntity<>(SUCCESS,pageInfo);
    }
    /**
     *  @Description    ：构造目录字符串
     *  @Method_Name    ：constructCateStr
     *  @param singleNod
     *  @param buffer
     *  @return java.lang.String
     *  @Creation Date  ：2018/4/24
     *  @Author         ：zhongpingtang@hongkun.com.cn
     */
    private String constructCateStr(CategoryComponent singleNod, StringBuffer buffer) {
        Iterator<CategoryComponent> iterator = singleNod.createIterator();
        if (iterator instanceof CategoryIterator ) {
            buffer.append(singleNod.getTitle() + "->");
            ((CategoryIterator) iterator).iteratorAll((node)-> buffer.append(node.getTitle() + "->"));
            return buffer.toString().substring(0, buffer.lastIndexOf("->"));
        }
        return buffer.append(singleNod.getTitle()).toString();
    }

}
