package com.hongkun.finance.payment.dao.impl;

import java.sql.Connection;
import java.util.List;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;

import com.hongkun.finance.payment.dao.FinAccountDao;
import com.hongkun.finance.payment.model.FinAccount;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.core.support.jta.JTAContext;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.payment.dao.impl.FinAccountDaoImpl.java
 * @Class Name : FinAccountDaoImpl.java
 * @Description : GENERATOR DAO实现类
 * @Author : generator
 */
@Dao
public class FinAccountDaoImpl extends MyBatisBaseDaoImpl<FinAccount, java.lang.Long>
        implements FinAccountDao {
    /**
     * 根据用户ID获取对象账户信息
     */
    public String FIND_USER_FINACCOUNT = ".findByRegUserId";
    /**
     * 根据用户ID，批量更新账户
     */
    public String UPDATE_FINACCOUNT_BYUSERID = ".updateAccountByRegUserId";

    @Override
    public FinAccount findByRegUserId(Integer regUserId) {

        return (FinAccount) getCurSqlSessionTemplate()
                .selectOne(FinAccount.class.getName() + FIND_USER_FINACCOUNT, regUserId);
    }

    @Override
    public int updateAccountByUserId(List<FinAccount> finAccountList, Integer count) {
        SqlSession sqlSession = null;
        try {
            if (finAccountList == null) {
                return 0;
            }
            sqlSession = getCurSqlSessionFactory().openSession(ExecutorType.BATCH, false);
            if (JTAContext.TRANSACTION_LOCAL.get() != null) {
                setAutoCommit(sqlSession.getConnection(), false);
                JTAContext.CONNECTION_HOLDER.get().add(sqlSession);
            }

            int num = 0;
            for (FinAccount t : finAccountList) {
                sqlSession.update(t.getClass().getName() + UPDATE_FINACCOUNT_BYUSERID, t);
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
	public List<FinAccount> findFinAccountByRegUserIds(List<Integer> regUserIds) {
		return getCurSqlSessionTemplate()
                .selectList(FinAccount.class.getName() + ".findFinAccountByRegUserIds", regUserIds);
	}

}
