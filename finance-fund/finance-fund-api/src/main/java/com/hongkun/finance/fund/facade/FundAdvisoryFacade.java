package com.hongkun.finance.fund.facade;

import com.hongkun.finance.fund.model.FundAdvisory;
import com.hongkun.finance.fund.model.vo.FundAdvisoryVo;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;


public interface FundAdvisoryFacade {

    /**
     *  @Description    ：获取股权咨询信息列表
     *  @Method_Name    ：findFundAdvisoryList
     *  @param advisoryVo
     *  @param pager
     *  @return com.yirun.framework.core.utils.pager.Pager
     *  @Creation Date  ：2018年04月28日 16:37
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    Pager findFundAdvisoryList(FundAdvisoryVo advisoryVo, Pager pager);


    /**
     *  @Description    ：并保存咨询项目信息
     *  @Method_Name    ：saveFundAdvisoryInfo
     *  @param advisory
     *  @return com.yirun.framework.core.model.ResponseEntity<?>
     *  @Creation Date  ：2018年05月03日 15:58
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    ResponseEntity<?> saveFundAdvisoryInfo(FundAdvisory advisory);

    /**
     *  @Description    ：咨询信息分配给销售
     *  @Method_Name    ：assignFundAdvisoryToSale
     *  @param advisory
     *  @return void
     *  @Creation Date  ：2018年05月05日 16:09
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    void assignFundAdvisoryToSale(FundAdvisory advisory);

    /**
     *  @Description    ：海外基金状态审核
     *  @Method_Name    ：fundAgreementAudit
     *  @param advisoryVo
     *  @return void
     *  @Creation Date  ：2018年07月06日 15:50
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    void fundAgreementAudit(FundAdvisoryVo advisoryVo);

    /**
     *  @Description    ：打包下载pdf协议、身份证图片、护照图片
     *  @Method_Name    ：fundAgreementDownLoad
     *  @param id  协议信息记录id
     *  @return com.yirun.framework.core.model.ResponseEntity
     *  @Creation Date  ：2018/7/8
     *  @Author         ：pengwu@hongkun.com.cn
     */
    ResponseEntity fundAgreementDownLoad(Integer id);
}
