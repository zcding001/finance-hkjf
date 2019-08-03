package com.hongkun.finance.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.user.dao.DicDataDao;
import com.hongkun.finance.user.model.DicData;
import com.hongkun.finance.user.service.DicDataService;
import com.yirun.framework.core.utils.CommonUtils;
import com.yirun.framework.redis.JedisClusterUtils;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.user.service.impl.DicDataServiceImpl.java
 * @Class Name    : DicDataServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class DicDataServiceImpl implements DicDataService {

	private static final Logger logger = LoggerFactory.getLogger(DicDataServiceImpl.class);

	/**
	 * DicDataDAO
	 */
	@Autowired
	private DicDataDao dicDataDao;
	
	@Override
	public List<DicData> findDicDataList(DicData dicData) {
		return this.dicDataDao.findByCondition(dicData);
	}

	@Override
	public List<DicData> findDicDataList() {
		return this.dicDataDao.findByCondition(new DicData());
	}

	@Override
	public String findNameByValue(String businessName,
			String subjectName,int value) {
		List<DicData> resultList = null;
		List<DicData> subList = findDicDataBySubjectName(businessName,subjectName);
		if(subList!=null&&subList.size()>0){
			resultList = subList.stream().filter(dicData->value==dicData.getValue())
					.collect(Collectors.toList());
		}else{
			DicData dicData  = new DicData();
			dicData.setBusinessName(businessName);
			dicData.setSubjectName(subjectName);
			dicData.setValue(value);
			resultList = this.dicDataDao.findByCondition(dicData);
		}
		if(resultList!=null&&resultList.size()>0){
			DicData result = resultList.get(0);
			if(result!=null){
				return  result.getName();
			}
		}
		return null;
	}
	
	@Override
	public List<DicData> findDicDataBySubjectName(String businessName,
			String subjectName) {
		List<DicData> resultList = null;
		List<DicData> businessList = 
				(List<DicData>) JedisClusterUtils.getObjectForJson(businessName, new TypeReference< List<DicData> >(){});
		if(businessList!=null&&businessList.size()>0){
			resultList = businessList.stream().filter(dicData->subjectName.equals(dicData.getSubjectName()))
				.collect(Collectors.toList());
		}else{
			DicData dicData  = new DicData();
			dicData.setBusinessName(businessName);
			dicData.setSubjectName(subjectName);
			resultList = this.dicDataDao.findByCondition(dicData);
		}
		if(resultList!=null&&resultList.size()>0){
			return resultList;
		}
		return null;
	}
	
	
	@Override
	public void initCache() {
		List<DicData> resultList = findDicDataList();
		if(resultList!=null){
			//筛选 parentName  -  List<DicData> 
			//jdk8特性，通过stream（流）进行筛选，这里是根据businessName分组
			Map<String,List<DicData>> result = 
					resultList.stream().collect(Collectors.groupingBy(DicData::getBusinessName));
			//遍历Map，存入reidis中
			 result.forEach((key,value)->{
				 JedisClusterUtils.setAsJson(key, value);
			 });
		}
	}
	@Override
	public void refreshDicData(String businessName){
		DicData dicData  = new DicData();
		dicData.setBusinessName(businessName);
		List<DicData> resultList = this.dicDataDao.findByCondition(dicData);
		JedisClusterUtils.setAsJson(businessName, resultList);
	}

	@Override
	public String findNamesByValues(String businessName, String subjectName, String values, String splitStr) {
		StringBuilder names = null;
		splitStr = StringUtils.isNotBlank(splitStr)?splitStr:",";
		if (values!=null){
			DicData dicData  = new DicData();
			dicData.setBusinessName(businessName);
			dicData.setSubjectName(subjectName);
			List<DicData> resultList = this.dicDataDao.findByCondition(dicData);
			String[] valueStrs = values.split(splitStr);
			if (valueStrs!=null && valueStrs.length>0 && CommonUtils.isNotEmpty(resultList)){
				for (String valueStr : valueStrs) {
					for (DicData data:resultList){
						if(StringUtils.isNotBlank(valueStr)){
							Integer thisVal = Integer.parseInt(valueStr);
							if (thisVal == data.getValue()){
								if (names == null ){
									names = new StringBuilder("");
									names.append(data.getName());
								}else{
									names.append(splitStr).append(data.getName());
								}
								break;
							}
						}
					}
				}
			}
		}
		return names == null ?null : names.toString();
	}


}
