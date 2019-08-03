package com.hongkun.finance.info.service;

import java.util.List;

import com.yirun.framework.core.utils.pager.Pager;
import com.hongkun.finance.info.enums.InfomationNewsEnum;
import com.hongkun.finance.info.model.InfoInformationNews;
import com.hongkun.finance.info.vo.InformationVo;

/**
 * @Project : finance
 * @Program Name :
 *          com.hongkun.finance.info.service.InfoInformationNewsService.java
 * @Class Name : InfoInformationNewsService.java
 * @Description : GENERATOR SERVICE类
 * @Author : generator
 */
public interface InfoInformationNewsService {

	/**
	 * @Described : 单条插入
	 * @param infoInformationNews
	 *            持久化的数据对象
	 * @return : void
	 */
	int insertInfoInformationNews(InfoInformationNews infoInformationNews);

	/**
	 * @Described : 批量插入
	 * @param List<InfoInformationNews>
	 *            批量插入的数据
	 * @return : void
	 */
	void insertInfoInformationNewsBatch(List<InfoInformationNews> list);

	/**
	 * @Described : 批量插入
	 * @param List<InfoInformationNews>
	 *            批量插入的数据
	 * @param count
	 *            多少条数提交一次
	 * @return : void
	 */
	void insertInfoInformationNewsBatch(List<InfoInformationNews> list, int count);

	/**
	 * @Described : 更新数据
	 * @param infoInformationNews
	 *            要更新的数据
	 * @return : void
	 */
	int updateInfoInformationNews(InfoInformationNews infoInformationNews);

	/**
	 * @Described : 批量更新数据
	 * @param infoInformationNews
	 *            要更新的数据
	 * @param count
	 *            多少条数提交一次
	 * @return : void
	 */
	void updateInfoInformationNewsBatch(List<InfoInformationNews> list, int count);

	/**
	 * @Described : 通过id查询数据
	 * @param id
	 *            id值
	 * @return InfoInformationNews
	 */
	InfoInformationNews findInfoInformationNewsById(int id);

	/**
	 * @Described : 条件检索数据
	 * @param infoInformationNews
	 *            检索条件
	 * @return List<InfoInformationNews>
	 */
	List<InfoInformationNews> findInfoInformationNewsList(InfoInformationNews infoInformationNews);

	/**
	 * @Described : 条件检索数据
	 * @param infoInformationNews
	 *            检索条件
	 * @param start
	 *            起始页
	 * @param limit
	 *            检索条数
	 * @return List<InfoInformationNews>
	 */
	List<InfoInformationNews> findInfoInformationNewsList(InfoInformationNews infoInformationNews, int start,
			int limit);

	/**
	 * @Described : 条件检索数据
	 * @param infoInformationNews
	 *            检索条件
	 * @param pager
	 *            分页数据
	 * @return Pager
	 */
	Pager findInfoInformationNewsList(InfoInformationNews infoInformationNews, Pager pager);

	/**
	 * @Described : 统计条数
	 * @param infoInformationNews
	 *            检索条件
	 * @param pager
	 *            分页数据
	 * @return int
	 */
	int findInfoInformationNewsCount(InfoInformationNews infoInformationNews);

	/**
	 * @Description : 通过类型和状态查询资讯信息
	 * @Method_Name : findInformationNewsByTypeAndState;
	 * @param InfomationNewsEnum
	 *            资讯类型枚举类
	 * @param state
	 *            状态
	 * @return
	 * @return : List<InfoInformationNews>;
	 * @Creation Date : 2017年8月18日 下午3:01:32;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	List<InfoInformationNews> findInformationNewsByTypeAndState(InfomationNewsEnum infomationNewsEnum, int state);

	/**
	 * @Description : 通过资讯ID更新状态
	 * @Method_Name : updateInfomationStateById;
	 * @param id
	 *            资讯ID
	 * @param state
	 *            状态
	 * @return
	 * @return : int;
	 * @Creation Date : 2017年8月18日 下午3:47:16;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	int updateInfomationStateById(int id, int state);

	/**
	 * @Description : 插入资讯信息
	 * @Method_Name : insertInfomationsNews;
	 * @param informationCondition
	 * @return
	 * @return : int;
	 * @Creation Date : 2017年8月21日 上午10:09:19;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	int insertInfomationsNews(InformationVo informationCondition);

	/**
	 * @Description : 更新资讯信息
	 * @Method_Name : updateInfomationsNews;
	 * @param informationCondition
	 * @return
	 * @return : int;
	 * @Creation Date : 2017年8月21日 下午2:56:28;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	int updateInfomationsNews(InformationVo informationCondition);

	/**
	 * @Description : 通过ID，更新点赞或浏览的随机数
	 * @Method_Name : updateInfomationById;
	 * @param randomNum
	 *            随机数
	 * @param id
	 *            资讯ID
	 * @param type
	 *            类型 1-浏览 2-点赞
	 * @return
	 * @return : int;
	 * @Creation Date : 2017年8月22日 上午10:57:50;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	int updateInfomationById(int randomNum, int id, int type);

	/**
	 * @Description :资讯分页查询
	 * @Method_Name : findByCondition;
	 * @param type
	 *            资讯类型
	 * @param infoInformationNews
	 *            资讯对象
	 * @param pager
	 *            分页对象
	 * @return
	 * @return : Pager;
	 * @Creation Date : 2017年9月21日 上午9:30:39;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	Pager findByCondition(List<Integer> type, InfoInformationNews infoInformationNews, Pager pager);
}
