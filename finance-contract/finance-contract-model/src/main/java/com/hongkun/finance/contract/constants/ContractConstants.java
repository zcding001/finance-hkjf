package com.hongkun.finance.contract.constants;

import com.yirun.framework.core.utils.PropertiesHolder;

/**
 * @Description     : 合同服务常量管理
 * @Project         : framework
 * @Program Name    : com.hongkun.finance.contract.constants.ContractConstants
 * @Author          : pengwu@hongkun.com.cn 吴鹏
 */
public interface ContractConstants {

    /** 查看合同位置：从标地信息位置查看合同*/
    int CONTRACT_LOCATION_BIDD = 1;
    /** 查看合同位置：从投资记录位置查看合同*/
    int CONTRACT_LOCATION_INVEST = 2;
    /** 查看合同位置：查看源项目查看合同*/
    int CONTRACT_LOCATION_ORIGIN_PROJECT = 3;
    /** 查看合同位置：借款人查看合同*/
    int CONTRACT_LOCATION_BORROWER_CONTRACT = 4;
    /** 查看合同来源：PC端查看合同*/
    int CONTRACT_SOURCE_PC = 1;
    /** 查看合同来源：WAP端查看合同*/
    int CONTRACT_SOURCE_WAP = 2;
    /** 查看合同来源：APP端查看合同*/
    int CONTRACT_SOURCE_APP = 3;
    /** 合同模块：禁用状态*/
    int CONTRACT_STATE_N = 0;
    /** 合同模块：启用状态*/
    int CONTRACT_STATE_Y = 1;
    
    /** 合同类型：优选计划*/
    int CONTRACT_TYPE_GOOD_PLAN = 1;
    /** 合同类型：保障计划*/
    int CONTRACT_TYPE_SECURITY_PLAN=2;
    /** 合同类型：乾坤宝服务协议书*/
    int CONTRACT_TYPE_QKB=3;
    /** 合同类型：定向融资认购协议*/
    int CONTRACT_TYPE_DIRECTIONAL=4;
    /** 合同类型：收益权转让认购协议*/
    int CONTRACT_TYPE_USUFRUCT_TRANSFER=5;
    /** 合同类型：债权转让协议*/
    int CONTRACT_TYPE_CREDITOR_TRANSFER=6;
    /** 合同类型：债权转让协议-转让方*/
    int CONTRACT_TYPE_CREDITOR_TRANSFER_TRANSFEROR=13;
    /** 合同消息队列*/
    String MQ_QUEUE_CONINFO = "default_coninfo";
    /** 自定义生成合同消息队列*/
    String MQ_QUEUE_SELF_CONINFO = "self_coninfo";

    /** 公司地址*/
    String COMPANY_ADDRESS = PropertiesHolder.getProperty("company.address");
    /** 公司名称*/
    String COMPANY_NAME = PropertiesHolder.getProperty("company.name");
    /** 公司联系电话*/
    String COMPANY_TEL = PropertiesHolder.getProperty("company.tel");
    
    /**投资记录数据保全数据mq队列**/
    final String MQ_QUEUE_INVEST_PRESERVE = "default_invest_preserve"; 
    /** 保全地址 **/
    String API_ADDRESS = PropertiesHolder.getProperty("apiAddress");
    /** 保全key **/
    String PARTNERKEY = PropertiesHolder.getProperty("partnerKey");
    /** 保全密钥 **/
    String SECRET = PropertiesHolder.getProperty("secret");
    /** 宝全版本 **/
    String DATETYPE = PropertiesHolder.getProperty("dateType_version");
    /** 投资保全-投资实时基础数据-流程号  **/
    static final String INVEST_ANCUN_FLOWNO = "X-0406001";
    /** 投资保全-上传合同追加-流程号  **/
    static final String CONTRACT_ANCUN_FLOWNO = "X-0406002";
    /** 投资保全-还款清单追加-流程号  **/
    static final String REPAY_ANCUN_FLOWNO = "X-0406003";
    
    
    
}
