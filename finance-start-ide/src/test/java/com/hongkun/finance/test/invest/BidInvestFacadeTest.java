package com.hongkun.finance.test.invest;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.contract.model.ConInfoSelfGenerateDTO;
import com.hongkun.finance.invest.facade.BidInvestFacade;
import com.hongkun.finance.invest.model.BidInfo;
import com.hongkun.finance.invest.model.BidInvest;
import com.hongkun.finance.invest.model.vo.BidInfoVO;
import com.hongkun.finance.invest.service.BidInfoService;
import com.hongkun.finance.invest.service.BidInvestService;
import com.hongkun.finance.test.BaseTest;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.service.RegUserDetailService;
import com.hongkun.finance.user.service.RegUserService;
import com.yirun.framework.core.enums.PlatformSourceEnums;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.jms.JmsService;
import com.yirun.framework.jms.enums.DestinationType;
import com.yirun.framework.jms.enums.JmsMessageType;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.hongkun.finance.contract.constants.ContractConstants.CONTRACT_TYPE_CREDITOR_TRANSFER_TRANSFEROR;
import static com.hongkun.finance.contract.constants.ContractConstants.MQ_QUEUE_CONINFO;
import static com.hongkun.finance.contract.constants.ContractConstants.MQ_QUEUE_SELF_CONINFO;

/**
 * @Description : TODO
 * @Project : finance
 * @Program Name  : com.hongkun.finance.test.invest
 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
 */
public class BidInvestFacadeTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(BidInvestFacadeTest.class);
    @Reference
    private BidInvestFacade bidInvestFacade;
    @Reference
    private RegUserService regUserService;
    @Reference
    private RegUserDetailService regUserDetailService;
    @Reference
    private BidInvestService bidInvestService;
    @Reference
    private BidInfoService bidInfoService;
    @Autowired
    private JmsService jmsService;

    private int bidId = 31;

    @Test
    // @Ignore
    public void testInvest() {
//		Object obj = ApplicationContextUtils.getBean(VasCouponDetailService.class);
//		Class<?>[] arr = obj.getClass().getInterfaces();
    	
    	BidInfo bidInfo = bidInfoService.findBidInfoById(794);
        logger.info("================ invest start ============"+bidInfo);
        RegUser regUser = new RegUser();
        regUser.setId(78);

        ResponseEntity<?> result = this.bidInvestFacade.invest(regUser, -1, -1, BigDecimal.valueOf(1000), 111, 1101, 1,PlatformSourceEnums.PC);
        logger.info("\n响应结果\n:{}", result);
        logger.info("================ invest end ============");
    }

    @Test
//	@Ignore
    public void testSaveInvest() {
        BidInvest bidInvest = new BidInvest();
        bidInvest.setInvestAmount(BigDecimal.valueOf(100));

        bidInvestService.insertBidInvest(bidInvest);

        logger.info("================ invest end ============");
        logger.info("================ invest end ============");
    }
    @Test
    public void testConInfo(){
        List<Integer> buyerInvestIds = new ArrayList<>();
        buyerInvestIds.add(1536);
//        ConInfoSelfGenerateDTO dto = new ConInfoSelfGenerateDTO();
//        dto.setContractType(CONTRACT_TYPE_CREDITOR_TRANSFER_TRANSFEROR);
//        dto.setInvestIdList(buyerInvestIds);
        jmsService.sendMsg(MQ_QUEUE_CONINFO, DestinationType.QUEUE,
                buyerInvestIds,JmsMessageType.OBJECT);
    }
    @Test
    public void testBidInfoList(){
        Pager pager = new Pager();
        pager.setPageSize(10);
        pager.setStartNum(1);
        BidInfoVO vo = new  BidInfoVO();
//        Pager result = this.bidInfoService.findBidInfoDetailVoList(pager, vo);

        bidInfoService.findBidInfoById(1);
    }
}
