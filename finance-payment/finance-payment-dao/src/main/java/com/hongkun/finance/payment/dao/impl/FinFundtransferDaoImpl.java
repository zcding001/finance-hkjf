package com.hongkun.finance.payment.dao.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;

import com.hongkun.finance.payment.dao.FinFundtransferDao;
import com.hongkun.finance.payment.model.FinFundtransfer;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.core.support.jta.JTAContext;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

/**
 * @Project : finance
 * @Program Name :
 *          com.hongkun.finance.payment.dao.impl.FinFundtransferDaoImpl.java
 * @Class Name : FinFundtransferDaoImpl.java
 * @Description : GENERATOR DAO实现类
 * @Author : generator
 */
@Dao
public class FinFundtransferDaoImpl extends MyBatisBaseDaoImpl<FinFundtransfer, java.lang.Long>
		implements FinFundtransferDao {
	/***
	 * 根据FLOWID查询交易流水对象信息
	 */

	public String UPDATE_BY_FLOWID = ".updateByFlowId";

	public String DEL_BY_FLOW_ID = ".deleteByFlowId";

	public String DEL_BY_FLOW_ID_BATCH = ".deleteBatchByFlowId";

	public String FIND_FINTRANSFER_TOTAL_MONEY = ".findFintransferSumMoney";

	public String DEL_BY_P_FLOW_ID = ".deleteByPflowId";

	public String FIND_TRANSFTER_BY_FLOWID = ".findTransfterByFlowId";

	@Override
	public int updateByFlowId(List<FinFundtransfer> finFundtransferList, Integer count) {
		SqlSession sqlSession = null;
		try {
			if (finFundtransferList == null) {
				return 0;
			}
			sqlSession = getCurSqlSessionFactory().openSession(ExecutorType.BATCH, false);
			if (JTAContext.TRANSACTION_LOCAL.get() != null) {
				setAutoCommit(sqlSession.getConnection(), false);
				JTAContext.CONNECTION_HOLDER.get().add(sqlSession);
			}

			int num = 0;
			for (FinFundtransfer t : finFundtransferList) {
				sqlSession.update(t.getClass().getName() + UPDATE_BY_FLOWID, t);
				num++;
				if (count == num) {
					sqlSession.commit();
					num = 0;
				}
			}
			if (JTAContext.TRANSACTION_LOCAL.get() == null) {
				sqlSession.commit();
			}
		} catch (Exception e) {
			if (JTAContext.TRANSACTION_LOCAL.get() == null) {
				if (sqlSession != null) {
					sqlSession.rollback(true);
				}
			}
		} finally {
			if (JTAContext.TRANSACTION_LOCAL.get() == null) {
				if (sqlSession != null) {
					sqlSession.close();
				}
			}
		}
		return count;
	}

	private void setAutoCommit(Connection con, boolean autoCommit) {
		try {
			if (con != null) {
				con.setAutoCommit(autoCommit);
			}
		} catch (Exception e) {

		}
	}

	@Override
	public int deleteByFlowId(FinFundtransfer finFundtransfer) {
		return getCurSqlSessionTemplate().delete(FinFundtransfer.class.getName() + DEL_BY_FLOW_ID, finFundtransfer);
	}

	@Override
	public int deleteByFlowIdBatch(List<String> flowIdList) {
		return getCurSqlSessionTemplate().delete(FinFundtransfer.class.getName() + DEL_BY_FLOW_ID_BATCH, flowIdList);
	}

	@Override
	public BigDecimal findFintransferSumMoney(FinFundtransfer finFundtransfe) {
		return getCurSqlSessionTemplate().selectOne(FinFundtransfer.class.getName() + FIND_FINTRANSFER_TOTAL_MONEY,
				finFundtransfe);
	}

	@Override
	public void deleteByPflowId(String PflowId) {
		getCurSqlSessionTemplate().delete(FinFundtransfer.class.getName() + DEL_BY_P_FLOW_ID, PflowId);
	}

	@Override
	public FinFundtransfer findTransferByFlowId(String flowId) {
		return getCurSqlSessionTemplate().selectOne(FinFundtransfer.class.getName() + FIND_TRANSFTER_BY_FLOWID, flowId);
	}

	@Override
	public List<Integer> findRegUserIdListYestoday() {
		return getCurSqlSessionTemplate().selectList(FinFundtransfer.class.getName() + ".findRegUserIdListYestoday");
	}

}
