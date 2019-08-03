package com.hongkun.finance.property.dao;

import com.hongkun.finance.property.model.ProPayRecord;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

import java.util.Map;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.property.dao.ProPayRecordDao.java
 * @Class Name    : ProPayRecordDao.java
 * @Description   : GENERATOR DAO类
 * @Author        : generator
 */
public interface ProPayRecordDao extends MyBatisBaseDao<ProPayRecord, java.lang.Long> {
	Pager findProPayRecordVoList(ProPayRecord ProPayRecord, Pager pager);

    /**
     *  @Description    : 更新缴费记录状态
     *  @Method_Name    : updateState
     *  @param params
     *  @return
     *  @return         : ResponseEntity<?>
     *  @Creation Date  : 2018年1月12日 下午1:53:46
     *  @Author         : xuhuiliu@honghun.com.cn 刘旭辉
     */
    Integer updateState(Map<String,Object> params);
}
