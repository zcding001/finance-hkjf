package com.hongkun.finance.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.user.model.DicArea;
import com.hongkun.finance.user.model.DicData;
import com.hongkun.finance.user.service.DicAreaService;
import com.hongkun.finance.user.service.DicDataService;
import com.hongkun.finance.user.support.security.OperationTypeEnum;
import com.hongkun.finance.user.support.security.component.annotation.AskForPermission;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.CommonUtils;
import com.yirun.framework.redis.JedisClusterUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static com.yirun.framework.core.commons.Constants.ERROR;
import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * @Description : 处理redis中的字典服务的类
 * @Project : finance
 * @Program Name  : com.hongkun.finance.web.controller.DicController
 * @Author : zhongpingtang@yiruntz.com 唐忠平
 */

@Controller
@RequestMapping("dicController")
@AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
public class DicController {
    @Reference
    private DicDataService dicDataService;
    @Reference
    private DicAreaService dicAreaService;

    /**
     * 根据 businessName，subjectName 返回一个对应的字典list
     *
     * @param businessName
     * @param subjectName
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("/getDicList")
    @ResponseBody
    public ResponseEntity<?> getDicMap(@RequestParam("businessName") String businessName, @RequestParam("subjectName") String subjectName) {
        //找到产品类型标的的名称
        List<DicData> bidProductSelect
                = dicDataService.findDicDataBySubjectName(businessName, subjectName);
        if (bidProductSelect == null || bidProductSelect.size() == 0) {
            return new ResponseEntity(ERROR, "没有相关数据");
        }
        //TODO:zhongping ,看前端所需的数据结构再做修改
//        Map<String, Integer> selectMap
//                = bidProductSelect.stream().collect(Collectors.toMap(e -> e.getName(), e -> e.getValue()));
        return new ResponseEntity(SUCCESS, bidProductSelect);
    }
    
    /**
     *  @Description    : 检索所有的字典表
     *  @Method_Name    : dicList
     *  @return         : List<DicData>
     *  @Creation Date  : 2017年8月31日 下午4:11:34 
     *  @Author         : zhichaoding@hongkun.com zc.ding
     */
    @RequestMapping("/dicList")
    @ResponseBody
    public List<DicData> dicList(){
    	return this.dicDataService.findDicDataList();
    }
    
    /**
     *  @Description    : 检索区域字典表
     *  @Method_Name    : dicAreaList
     *  @return         : List<DicArea>
     *  @Creation Date  : 2017年9月26日 下午4:56:04 
     *  @Author         : zhichaoding@hongkun.com zc.ding
     */
	@RequestMapping("/dicAreaList")
    @ResponseBody
    public List<DicArea> dicAreaList(){
    	List<DicArea> list = JedisClusterUtils.getObjectForJson(DicArea.class.getSimpleName(), new TypeReference< List<DicArea>>(){});
    	if(CommonUtils.isEmpty(list)){
    		list = this.dicAreaService.findDicAreaList(new DicArea());
            JedisClusterUtils.setAsJson(DicArea.class.getSimpleName(), list);
    	}
    	return list;
    }
}
