package com.hongkun.finance.roster.dao;

import com.hongkun.finance.roster.model.RosStaffInfo;
import com.hongkun.finance.roster.vo.RosStaffInfoVo;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

import java.util.List;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.roster.dao.RosStaffInfoDao.java
 * @Class Name : RosStaffInfoDao.java
 * @Description : GENERATOR DAO类
 * @Author : generator
 */
public interface RosStaffInfoDao extends MyBatisBaseDao<RosStaffInfo, java.lang.Long> {
	/**
	 * @Description : 查询企业员工
	 * @Method_Name : findRosStaffInfoList
	 * @param rosStaffInfoContidion
	 * @return
	 * @return : List<RosStaffInfoVo>
	 * @Creation Date : 2018年1月8日 下午2:20:09
	 * @Author : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	List<RosStaffInfoVo> findRosStaffInfoList(RosStaffInfoVo rosStaffInfoContidion);

	/**
	 * @Description ：查询用户信息
	 * @Method_Name ：findRosStaffInfo
	 * @param regUserId
	 *            : 用户id
	 * @param type
	 * @param state
	 * @return com.hongkun.finance.roster.model.RosStaffInfo
	 * @Creation Date ：2018/4/17
	 * @Author ：zhichaoding@hongkun.com.cn
	 */
	RosStaffInfo findRosStaffInfo(Integer regUserId, Integer type, Integer state,Integer recommendState);

	/**
	 * @Description : 根据用户ID，及类型、状态查询员工信息
	 * @Method_Name : findRosStaffInfo;
	 * @param List<Integer>
	 *            regUserIdList 用户ID
	 * @param type
	 *            用户类型 '类型 1：物业员工，2：销售员工，3：内部员工',
	 * @param state
	 *            '0:删除，1：正常',
	 * @return
	 * @return : List<Integer>;
	 * @Creation Date : 2018年5月7日 下午4:59:16;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	List<Integer> findRosStaffInfoList(List<Integer> regUserIdList, Integer type, Integer state);

	/**
	 * @Description : 通过类型查询用户 ID集合
	 * @Method_Name : findNotExistRosStaffInfo;
	 * @param types
	 * @return
	 * @return : List<Integer>;
	 * @Creation Date : 2018年5月7日 下午6:01:03;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	List<Integer> findRosStaffInfoByTypes(List<Integer> types);
}
