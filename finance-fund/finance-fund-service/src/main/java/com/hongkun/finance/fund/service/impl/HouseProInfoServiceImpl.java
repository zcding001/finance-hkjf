package com.hongkun.finance.fund.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONArray;
import com.hongkun.finance.fund.constants.HouseConstants;
import com.hongkun.finance.fund.dao.*;
import com.hongkun.finance.fund.model.*;
import com.hongkun.finance.fund.model.vo.HouseInfoAndDetail;
import com.hongkun.finance.fund.model.vo.HouseProInfoVo;
import com.hongkun.finance.fund.service.HouseProInfoService;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.CommonUtils;
import com.yirun.framework.core.utils.PropertiesHolder;
import com.yirun.framework.core.utils.pager.Pager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hongkun.finance.fund.constants.HouseConstants.*;
import static com.yirun.framework.core.commons.Constants.ERROR;
import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.fund.service.impl.HouseProInfoServiceImpl.java
 * @Class Name    : HouseProInfoServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class HouseProInfoServiceImpl implements HouseProInfoService {

	private static final Logger logger = LoggerFactory.getLogger(HouseProInfoServiceImpl.class);
	
	/**
	 * HouseProInfoDAO
	 */
	@Autowired
	private HouseProInfoDao houseProInfoDao;
	@Autowired
	private HouseProPicDao houseProPicDao;
	@Autowired
	private HouseProPermitDao houseProPermitDao;
	@Autowired
	private HouseProDetailDao houseProDetailDao;
	@Autowired
	private HouseProIntroduceDao houseProIntroduceDao;

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertHouseProInfo(HouseProInfo houseProInfo) {
		this.houseProInfoDao.save(houseProInfo);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertHouseProInfoBatch(List<HouseProInfo> list) {
		this.houseProInfoDao.insertBatch(HouseProInfo.class, list);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertHouseProInfoBatch(List<HouseProInfo> list, int count) {
		if(logger.isDebugEnabled()){
			logger.debug("default batch insert size is " + count);
		}
		this.houseProInfoDao.insertBatch(HouseProInfo.class, list, count);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateHouseProInfo(HouseProInfo houseProInfo) {
		this.houseProInfoDao.update(houseProInfo);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateHouseProInfoBatch(List<HouseProInfo> list, int count) {
		this.houseProInfoDao.updateBatch(HouseProInfo.class, list, count);
	}
	
	@Override
	public HouseProInfo findHouseProInfoById(int id) {
		return this.houseProInfoDao.findByPK(Long.valueOf(id), HouseProInfo.class);
	}
	
	@Override
	public List<HouseProInfo> findHouseProInfoList(HouseProInfo houseProInfo) {
		return this.houseProInfoDao.findByCondition(houseProInfo);
	}
	
	@Override
	public List<HouseProInfo> findHouseProInfoList(HouseProInfo houseProInfo, int start, int limit) {
		return this.houseProInfoDao.findByCondition(houseProInfo, start, limit);
	}
	
	@Override
	public Pager findHouseProInfoList(HouseProInfo houseProInfo, Pager pager) {
		return this.houseProInfoDao.findByCondition(houseProInfo, pager);
	}
	
	@Override
	public int findHouseProInfoCount(HouseProInfo houseProInfo){
		return this.houseProInfoDao.getTotalCount(houseProInfo);
	}
	
	@Override
	public Pager findHouseProInfoList(HouseProInfo houseProInfo, Pager pager, String sqlName){
		return this.houseProInfoDao.findByCondition(houseProInfo, pager, sqlName);
	}
	
	@Override
	public Integer findHouseProInfoCount(HouseProInfo houseProInfo, String sqlName){
		return this.houseProInfoDao.getTotalCount(houseProInfo, sqlName);
	}

	@Override
	public Pager findHouseInfoVoPageList(Pager pager,String basicDetailUrl) {
		HouseProInfo houseProInfo = new HouseProInfo();
		houseProInfo.setState(HOUSE_INFO_STATE_RUNNING);
		Pager resultPager =  this.houseProInfoDao.findByCondition(houseProInfo, pager, ".findHouseInfoVoPageList");
		List<HouseProInfoVo> voList = (List<HouseProInfoVo>) resultPager.getData();
		if (CommonUtils.isNotEmpty(voList)){
			voList.forEach(vo->{
				//整理户型列表
				String roomType = initRoomTypeNameByValue(vo.getRoomType());
				vo.setRoomType(roomType==null ?"":roomType);
				//查询图片地址
				HouseProPic pic = new HouseProPic();
				pic.setInfoId(vo.getId());
				pic.setType(HOUSE_PIC_TYPE_INDEX);
				List<HouseProPic> pics =  this.houseProPicDao.findByCondition(pic);
				String  basicUrl =  PropertiesHolder.getProperty("oss.url");
				if(StringUtils.isNotBlank(basicDetailUrl)){
					vo.setDetailUrl(basicDetailUrl+"/"+"showHouseDetailById?infoId="+vo.getId());
				}
				if (CommonUtils.isNotEmpty(pics)){
					vo.setPicUrl(basicUrl +"/"+ pics.get(0).getUrl());
				}else{
					vo.setPicUrl(basicUrl + "/house/index_house.jpg");
				}
			});
		}
		resultPager.setData(voList);
		return resultPager;
	}

	/**
	*  @Description    ：把roomType值类型字符串转化为描述字符串 （1，2，3--》“一居，二居，三居”）
	*  @Method_Name    ：initRoomTypeNameByValue
	*  @param roomType
	*  @return java.lang.String
	*  @Creation Date  ：2019/1/10
	*  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
	*/
	private String initRoomTypeNameByValue(String roomType) {
		if (StringUtils.isNotBlank(roomType)){
			String[] vals = roomType.split(",");
			StringBuilder names = new StringBuilder("");
			for (String val :vals){
				switch (val){
					case "1":
						names.append("一居/");
						break;
					case "2":
						names.append("二居/");
						break;
					case "3":
						names.append("三居/");
						break;
					case "4":
						names.append("四居/");
						break;
					case "5":
						names.append("五居/");
						break;
					case "6":
						names.append("六居/");
						break;
				}
			}
			return  names.toString().substring(0,names.length()-1);
		}
		return null;
	}

	@Override
	public HouseInfoAndDetail findHouseInfoAndDetailById(Integer infoId) {
		return houseProInfoDao.findHouseInfoAndDetailById(infoId);
	}

	@Override
	public List<HouseProPic> getHouseProPicList(Integer infoId, Integer type) {
		HouseProPic pic = new HouseProPic();
		pic.setInfoId(infoId);
		pic.setType(type);
		return houseProPicDao.findByCondition(pic);
	}

	@Override
	public List<HouseProPermit> getHouseProPermitList(Integer infoId) {
		HouseProPermit permit = new HouseProPermit();
		permit.setInfoId(infoId);
		return houseProPermitDao.findByCondition(permit);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public ResponseEntity<?> addHouse(HouseProInfo houseProInfo, HouseProDetail houseProDetail,
									  HouseProIntroduce houseProIntroduce, HouseDTO houseDTO) {
		try{
			houseProInfoDao.save(houseProInfo);
			houseProDetail.setId(houseProInfo.getId());
			houseProIntroduce.setId(houseProInfo.getId());
			houseProDetailDao.save(houseProDetail);
			houseProIntroduceDao.save(houseProIntroduce);
			//房产预售证列表信息
			List<HouseProPermit> permitList = JSONArray.parseArray(houseDTO.getPermitList(),HouseProPermit.class);
			permitList.forEach((permit) -> permit.setInfoId(houseProInfo.getId()));
			if (permitList.size() > 0){
				houseProPermitDao.insertBatch(HouseProPermit.class,permitList);
			}
		}catch (Exception e){
			logger.error("addHouse, 添加房产信息异常, houseProInfo: {}, houseProDetail: {}, houseProIntroduce: {}, 异常信息为: ",
					houseProInfo,houseProDetail,houseProIntroduce,e);
			throw new GeneralException("添加房产信息失败！");
		}
		return new ResponseEntity<>(SUCCESS,houseProInfo.getId());
	}

	@Override
	public ResponseEntity<?> upHouse(int id) {
		try {
			HouseProInfo updateTmp = new HouseProInfo();
			updateTmp.setId(id);
			updateTmp.setState(HOUSE_INFO_STATE_RUNNING);
			houseProInfoDao.update(updateTmp);
		}catch (Exception e){
			logger.error("upHouse, 上架房产信息异常, id为: {}, 异常信息为: ",id,e);
			throw new GeneralException("上架房产信息失败");
		}
		return new ResponseEntity<>(SUCCESS);
	}

	@Override
	public ResponseEntity<?> downHouse(int id) {
		try {
			HouseProInfo updateTmp = new HouseProInfo();
			updateTmp.setId(id);
			updateTmp.setState(HouseConstants.HOUSE_INFO_STATE_WAITING);
			houseProInfoDao.update(updateTmp);
		}catch (Exception e){
			logger.error("downHouse, 下架房产信息异常, id为: {}, 异常信息为: ",id,e);
			throw new GeneralException("下架房产信息失败");
		}
		return new ResponseEntity<>(SUCCESS);
	}

	@Override
	public ResponseEntity<?> findHouse(int id) {
		Map<String,Object> params = new HashMap<>();
		//1.查询基础信息
		HouseInfoAndDetail infoAndDetail =  this.findHouseInfoAndDetailById(id);
		if (infoAndDetail == null){
			return new ResponseEntity<>(ERROR,"没有该房产信息！");
		}
		//获取预售证信息
		List<HouseProPic> picList = this.getHouseProPicList(id,null);
		//获取房产图片信息
		List<HouseProPermit> permitList = this.getHouseProPermitList(id);
		//封面图片
		HouseProPic indexPic = null;
		//户型图片
		List<HouseProPic> roomPics = new ArrayList<>();
		//楼盘图片
		List<HouseProPic> otherPics = new ArrayList<>();
		for (HouseProPic pic:picList){
			if (pic.getType() == HOUSE_PIC_TYPE_HOME){
				roomPics.add(pic);
			}else if (pic.getType() == HOUSE_PIC_TYPE_OTHER){
				otherPics.add(pic);
			}else {
				indexPic = pic;
			}
		}
		//组装数据
		params.put("infoAndDetail",infoAndDetail);
		params.put("permitList",permitList);
		params.put("indexPic",indexPic);
		params.put("roomPics",roomPics);
		params.put("otherPics",otherPics);
		return new ResponseEntity<>(SUCCESS,null,params);
	}

	@Override
	public ResponseEntity<?> updateHouse(HouseProInfo houseProInfo, HouseProDetail houseProDetail, HouseProIntroduce houseProIntroduce) {
		try{
			if (houseProInfoDao.findByPK((long)houseProInfo.getId(),HouseProInfo.class) == null){
				return new ResponseEntity<>(ERROR,"编辑的房产信息不存在！");
			}
			houseProInfoDao.update(houseProInfo);
			houseProDetailDao.update(houseProDetail);
			houseProIntroduceDao.update(houseProIntroduce);
		}catch (Exception e){
			logger.error("updateHouse, houseProInfo: {}, houseProDetail: {}, houseProIntroduce: {}, 异常信息: ",
					houseProInfo,houseProDetail,houseProIntroduce,e);
			throw new GeneralException("更新房产信息异常！");
		}
		return new ResponseEntity<>(SUCCESS);
	}
}
