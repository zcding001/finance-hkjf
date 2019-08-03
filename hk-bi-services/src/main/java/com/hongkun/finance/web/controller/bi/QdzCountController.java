package com.hongkun.finance.web.controller.bi;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.bi.facade.BalAccountFacade;
import com.hongkun.finance.qdz.facade.QdzConsoleFacade;
import com.hongkun.finance.qdz.vo.QdzTransferRecordVo;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description : 钱袋子统计
 * @Project : finance
 * @Program Name  : com.hongkun.finance.web.controller.bi
 * @Author : yanbinghuang
 */
@Controller
@RequestMapping("/qdzCountController")
public class QdzCountController {


    @Reference
    private QdzConsoleFacade qdzConsoleFacade;
    /**    
     *  @Description    : 钱袋子转入转出统计
     *  @Method_Name    : searchTransRecordList;
     *  @param qdzTransferRecordVo
     *  @param pager
     *  @return
     *  @return         : ResponseEntity<?>;
     *  @Creation Date  : 2018年9月18日 下午5:30:19;
     *  @Author         : yanbinghuang@hongkun.com.cn 黄艳兵;
     */
    @RequestMapping("/searchTransRecordList")
    @ResponseBody
    ResponseEntity<?> searchTransRecordList(QdzTransferRecordVo qdzTransferRecordVo, Pager pager) {
        qdzTransferRecordVo.setState(1);
        return qdzConsoleFacade.findQdzBillWater(qdzTransferRecordVo, pager);
    }

}
