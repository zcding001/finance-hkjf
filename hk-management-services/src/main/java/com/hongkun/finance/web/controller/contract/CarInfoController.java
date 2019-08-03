package com.hongkun.finance.web.controller.contract;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.contract.facade.CarFacade;
import com.hongkun.finance.contract.model.CarInfo;
import com.hongkun.finance.contract.service.CarInfoService;
import com.hongkun.finance.user.support.security.annotation.ActionLog;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.annotation.Token;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.StringUtilsExtend;
import com.yirun.framework.core.utils.pager.Pager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;

/**
 * @program: finance-hkjf
 * @description: 车辆信息Controller
 * @author: hehang
 * @create: 2018-08-01 14:58
 **/
@Controller
@RequestMapping("/carInfoController")
public class CarInfoController {
    private static final Logger logger = LoggerFactory.getLogger(CarInfoController.class);

    @Reference
    private CarInfoService carInfoService;
    @Reference
    private CarFacade carFacade;

    /** 
    * @Description: 汽车信息列表 
    * @Param: [carInfo, pager] 
    * @return: com.yirun.framework.core.model.ResponseEntity<?> 
    * @Author: HeHang
    * @Date: 2018/8/6 
    */
    @RequestMapping("/searchCarInfoList")
    @ResponseBody
    ResponseEntity<?> searchCarInfoList(CarInfo carInfo, Pager pager) {
        Pager result = carInfoService.findCarInfoList(carInfo, pager);
        return new ResponseEntity<>(Constants.SUCCESS, result);
    }

    /** 
    * @Description: 添加车辆信息
    * @Param: [carInfo] 
    * @return: com.yirun.framework.core.model.ResponseEntity<?> 
    * @Author: HeHang
    * @Date: 2018/8/6 
    */
    @RequestMapping("/addCarInfo")
    @ResponseBody
    @Token
    ResponseEntity<?> addCarInfo(@Valid CarInfo carInfo){
        carInfo.setModifyTime(new Date());
        carInfo.setCreateTime(new Date());
        if(StringUtilsExtend.isEmpty(carInfo.getCarBrand())){
            carInfo.setCarBrand("");
        }
        if(StringUtilsExtend.isEmpty(carInfo.getCarType())){
            carInfo.setCarType("");
        }
        if(StringUtilsExtend.isEmpty(carInfo.getFrameNum())){
            carInfo.setFrameNum("");
        }
        if(StringUtilsExtend.isEmpty(carInfo.getKeyNum())){
            carInfo.setKeyNum(0);
        }
        if(StringUtilsExtend.isEmpty(carInfo.getRemark())){
            carInfo.setRemark("");
        }
        carInfoService.insertCarInfo(carInfo);
        return new ResponseEntity<>(Constants.SUCCESS);
    }


    /** 
    * @Description: 根据id查询车辆信息 
    * @Param: [id] 
    * @return: com.yirun.framework.core.model.ResponseEntity<?> 
    * @Author: HeHang
    * @Date: 2018/8/6 
    */
    @RequestMapping("/findCarInfoById")
    @ResponseBody
    ResponseEntity<?> findCarInfoById(@RequestParam("id") Integer id){
        return new ResponseEntity<>(Constants.SUCCESS,carInfoService.findCarInfoById(id));
    }

    /** 
    * @Description: 删除车辆信息 
    * @Param: [id] 
    * @return: com.yirun.framework.core.model.ResponseEntity<?> 
    * @Author: HeHang
    * @Date: 2018/8/6 
    */
    @RequestMapping("/deleteCarInfoById")
    @ResponseBody
    ResponseEntity<?> deleteCarInfoById(@RequestParam("id") Integer id){
        return new ResponseEntity<>(Constants.SUCCESS,carInfoService.deleteCarInfoById(id));
    }

    /** 
    * @Description: 修改车辆信息
    * @Param: [carInfo] 
    * @return: com.yirun.framework.core.model.ResponseEntity<?> 
    * @Author: HeHang
    * @Date: 2018/8/6 
    */
    @RequestMapping("/updateCarInfo")
    @ResponseBody
    @Token
    @ActionLog(msg = "修改车辆信息, 信息id: {args[0].id} ")
    ResponseEntity<?> updateCarInfo(@Valid CarInfo carInfo){
        carInfoService.updateCarInfo(carInfo);
        return new ResponseEntity<>(Constants.SUCCESS);
    }

    /** 
    * @Description: 车辆信息导入
    * @param request
    * @param multipartFile
    * @return: com.yirun.framework.core.model.ResponseEntity<?> 
    * @Author: hanghe@hongkunjinfu.com
    * @Date: 2018/10/22 15:11
    */
    @RequestMapping("carInfoImport")
    @ResponseBody
    public ResponseEntity<?> carInfoImport(HttpServletRequest request, @RequestParam("uploadFile") MultipartFile multipartFile){
        ResponseEntity<?> result = BaseUtil.saveFile(request, multipartFile);
        if(result.getResStatus() == Constants.SUCCESS){
            return this.carFacade.importCars((String)result.getResMsg());
        }
        return result;
    }
}
