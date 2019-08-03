package com.hongkun.finance.user.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.hongkun.finance.user.model.DicData;

public class DicDataUtilsTest {
	public  void main(String[] args) {
		List<DicData> resultList = new ArrayList<DicData>();
		DicData d1 = new DicData();
		d1.setBusinessName("investStatus");
		d1.setSubjectName("a1");
		d1.setValue(1);
		d1.setName("进行中");
		
		DicData d2 = new DicData();
		d2.setBusinessName("investStatus");
		d2.setSubjectName("a2");
		d2.setValue(2);
		d2.setName("已失效");
		
		DicData d3 = new DicData();
		d3.setBusinessName("biddStatus");
		d3.setSubjectName("b1");
		d3.setValue(1);
		d3.setName("已放款");
		resultList.add(d1);
		resultList.add(d2);
		resultList.add(d3);
		if(resultList!=null){
			//筛选 parentName  -  List<DicData> 
			//jdk8特性，通过stream（流）进行筛选，这里是根据businessName分组
			Map<String,List<DicData>> result = 
					resultList.stream().collect(Collectors.groupingBy(DicData::getBusinessName));
			//遍历Map，存入reidis中
			 result.forEach((key,value)->{
				 //JedisUtils.setAsByte(key, value);
				 System.out.println("key==="+key+"===value:"+value);
			 });
		}
	}
}
