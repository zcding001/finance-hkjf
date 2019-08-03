package com.hongkun.finance.test.qdz;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Ignore;
import org.junit.Test;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.qdz.facade.QdzTaskJobFacade;
import com.hongkun.finance.qdz.vo.QdzAutoCreditorVo;
import com.hongkun.finance.test.BaseTest;
import com.yirun.framework.core.utils.DateUtils;

public class TestQdz extends BaseTest{
	
	  @Reference
	  private QdzTaskJobFacade qdzTaskJobFacade;
	  
	  //跑批钱袋子记录
	  @Test
	  public void testQdzRateRecord() {
		 qdzTaskJobFacade.qdzRateRecord(); 
	  }
	  //跑批债券
	  @Test
	  public void testQdzcreditorMatch() {
		 qdzTaskJobFacade.creditorMatch(new Date()); 
	  }
	  //跑批利息
	  @Test
	  public void testQdzCalculateInterest() {
	         qdzTaskJobFacade.calculateInterest(DateUtils.parse("2018-12-18"), 9);
	 }
	  //第三方垫息
	  @Test
      public void testThirdAccountPadBearing() {
             qdzTaskJobFacade.thirdAccountPadBearing(new Date());
     }
	  //转出
	  @Test
      public void testSellAutoCreditorByMQ() {
		  QdzAutoCreditorVo qdzAutoCreditorVo = new QdzAutoCreditorVo();
		  qdzAutoCreditorVo.setRate(BigDecimal.valueOf(4.5));
		  qdzAutoCreditorVo.setRegUserId(1203);
		  qdzAutoCreditorVo.setTransMoney(BigDecimal.valueOf(200));
		  qdzAutoCreditorVo.setRepairTime(new Date());
             qdzTaskJobFacade.sellAutoCreditorByMQ(qdzAutoCreditorVo);
     }
	  //恢复利息
	  @Test
	  public void testRepairCalcInterest() {
		  qdzTaskJobFacade.repairCalcInterest("73");
	  }
	  
}
