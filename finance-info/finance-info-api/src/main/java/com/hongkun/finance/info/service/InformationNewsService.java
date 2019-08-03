package com.hongkun.finance.info.service;

import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;

public interface InformationNewsService {
    /**
     * @Description :通过类型查询资讯分页信息
     * @Method_Name : findInformationByType;
     * @param type
     *            资讯类型
     * @return
     * @return : ResponseEntity<?>;
     * @Creation Date : 2017年8月21日 下午4:58:09;
     * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
     */
    ResponseEntity<?> findInformationByType(int type, Pager pager);

    /**
     * @Description :点赞或浏览资讯，添加点赞和浏览记录
     * @Method_Name : eulogizeInformations;
     * @param infomationsNewsId
     *            资讯ID
     * @param type
     *            资讯记录类型 1-浏览 2-点赞
     * @param source
     *            来源 0-PC 1-WAP 2-Android 3-IOS
     * @param regUserId
     *            用户Id
     * @return
     * @return : ResponseEntity<?>;
     * @Creation Date : 2017年8月21日 下午6:12:43;
     * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
     */
    ResponseEntity<?> eulogizeInformations(int infomationsNewsId, int type, int source, int regUserId);

    /**
     * @Description : 根据类型查询资讯列表
     * @Method_Name : findInformationNewsList;
     * @param channel
     *            渠道 1-PC 2-IOS 3-ANDRIOD 4-WAP'
     * @param position
     *            位置 0-其它 1-首页 2-积分商城'
     * @return
     * @return : ResponseEntity<?>;
     * @Creation Date : 2017年12月1日 上午10:01:45;
     * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
     */
    ResponseEntity<?> findInformationNewsLists(String channel, int position,Integer showFlag);
    /**
     *  @Description    : 查询调查问卷详情信息
     *  @Method_Name    : findQuestionnaireInfo;
     *  @param infomaationId
     *  @param regUserId
     *  @return
     *  @return         : ResponseEntity<?>;
     *  @Creation Date  : 2018年10月30日 下午2:13:15;
     *  @Author         : yanbinghuang@hongkun.com.cn 黄艳兵;
     */
    ResponseEntity<?> findQuestionnaireInfo(Integer infomaationId,Integer regUserId);
    /**
     *  @Description    : 查询调查问卷是否存在
     *  @Method_Name    : findExistQuestion;
     *  @param infomationId
     *  @param regUserId
     *  @return
     *  @return         : ResponseEntity<?>;
     *  @Creation Date  : 2018年10月31日 上午11:00:30;
     *  @Author         : yanbinghuang@hongkun.com.cn 黄艳兵;
     */
    ResponseEntity<?> findExistQuestion(Integer infomationId,Integer regUserId);
    

}
