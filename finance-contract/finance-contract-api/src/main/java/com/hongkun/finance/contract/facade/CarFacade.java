package com.hongkun.finance.contract.facade;

import com.yirun.framework.core.model.ResponseEntity;

/**
* @Description: 车辆信息Facade层
* @return:  
* @Author: hanghe@hongkunjinfu.com
* @Date: 2018/10/22 10:58
*/
public interface CarFacade {

    ResponseEntity<?> importCars(String filePath);
}
