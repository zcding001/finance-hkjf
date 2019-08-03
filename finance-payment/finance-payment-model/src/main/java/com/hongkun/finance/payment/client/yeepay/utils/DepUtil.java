package com.hongkun.finance.payment.client.yeepay.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hongkun.finance.payment.client.yeepay.vo.Cityinfo;

public class DepUtil {
	public ArrayList<String> province_list_code = new ArrayList<String>();
	public ArrayList<String> city_list_code = new ArrayList<String>();
	public final static String filePath="city.json";
	public List<Cityinfo> getJSONParserResult(String JSONString, String key) {
		List<Cityinfo> list = new ArrayList<Cityinfo>();
		JsonObject result = new JsonParser().parse(JSONString)
				.getAsJsonObject().getAsJsonObject(key);

		Iterator iterator = result.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, JsonElement> entry = (Entry<String, JsonElement>) iterator
					.next();
			Cityinfo cityinfo = new Cityinfo();

			cityinfo.setCity_name(entry.getValue().getAsString());
			cityinfo.setId(entry.getKey());
			province_list_code.add(entry.getKey());
			list.add(cityinfo);
		}
		System.out.println(province_list_code.size());
		return list;
	}

	public HashMap<String, List<Cityinfo>> getJSONParserResultArray(
			String JSONString, String key) {
		HashMap<String, List<Cityinfo>> hashMap = new HashMap<String, List<Cityinfo>>();
		JsonObject result = new JsonParser().parse(JSONString)
				.getAsJsonObject().getAsJsonObject(key);

		Iterator iterator = result.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, JsonElement> entry = (Entry<String, JsonElement>) iterator
					.next();
			List<Cityinfo> list = new ArrayList<Cityinfo>();
			JsonArray array = entry.getValue().getAsJsonArray();
			for (int i = 0; i < array.size(); i++) {
				Cityinfo cityinfo = new Cityinfo();
				cityinfo.setCity_name(array.get(i).getAsJsonArray().get(1)
						.getAsString());
				cityinfo.setId(array.get(i).getAsJsonArray().get(0)
						.getAsString());
				city_list_code.add(array.get(i).getAsJsonArray().get(0)
						.getAsString());
				list.add(cityinfo);
			}
			hashMap.put(entry.getKey(), list);
		}
		return hashMap;
	}
	/**
	 * 读取json文件
	 * @author liangbin
	 * @date 2016-4-14
	 * @return String
	 */
	public String getJson(String path) {
		InputStream inputStream = null;
		InputStreamReader instreamReader = null;
		BufferedReader reader = null;
		StringBuffer result = new StringBuffer();
		try {
			//System.out.println(DepUtil.class.getClassLoader().getResource("").getPath());
			inputStream = DepUtil.class.getClassLoader().getResourceAsStream(path);
			instreamReader = new InputStreamReader(inputStream, "UTF-8");
			reader = new BufferedReader(instreamReader);
			String tempStr = null;
			while ((tempStr = reader.readLine()) != null) {
				result.append(tempStr);
			}
			reader.close();
			instreamReader.close();
			inputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
				if (instreamReader != null) {
					instreamReader.close();
				}
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (Exception e2) {
			}
		}
		return result.toString();
	}

	/**
	 * 根据Id获取省信息
	 * @param provinceId
	 * @return
	 */
	public String getProvince(String provinceId){
		String provinceName=null;
		String area_str=this.getJson(filePath);
		List<Cityinfo> provinceList=this.getJSONParserResult(area_str, "area0");
		Cityinfo cityInfo=null;
		for(int i=0;i<provinceList.size();i++){
			cityInfo=provinceList.get(i);
			if(provinceId.equals(cityInfo.getId())){
				provinceName=cityInfo.getCity_name();
				break;
			}
		}
		if(provinceName==null){
			provinceName="获取省份异常！";
		}
		return provinceName;
	}
	/**
	 * 根据省id，城市id获取
	 * @param provincId
	 * @param cityId
	 * @return
	 */
	public String getCityName(String provincId,String cityId){
		String cityName=null;
		String area_str=this.getJson(filePath);
		HashMap<String, List<Cityinfo>> citymap=this.getJSONParserResultArray(area_str, "area1");
		Iterator ite=citymap.entrySet().iterator();
		List<Cityinfo> city=null;
		while(ite.hasNext()){
			Map.Entry<String, List<Cityinfo>> entry = (Entry<String, List<Cityinfo>>) ite
					.next();
			if(provincId.equals(entry.getKey())){
				city=entry.getValue();
				for(int c=0;c<city.size();c++){
					if(cityId.equals(city.get(c).getId())){
						cityName=city.get(c).getCity_name();
						break;
					}
				}
			}
		}
		if(cityName==null){
			cityName="获取城市名称异常！";
		}
		return cityName;
	}
}
