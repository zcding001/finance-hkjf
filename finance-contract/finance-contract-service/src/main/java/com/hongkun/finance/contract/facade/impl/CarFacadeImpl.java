package com.hongkun.finance.contract.facade.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.contract.facade.CarFacade;
import com.hongkun.finance.contract.model.CarInfo;
import com.hongkun.finance.contract.service.CarInfoService;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.CommonUtils;
import com.yirun.framework.core.utils.ExcelUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @program: finance-hkjf
 * @description: 车辆信息facade层
 * @author: hehang
 * @create: 2018-10-22 10:56
 **/
@Service
public class CarFacadeImpl implements CarFacade{

    private final Logger logger = LoggerFactory.getLogger(CarFacadeImpl.class);

    @Autowired
    private CarInfoService carInfoService;

    @Override
    public ResponseEntity<?> importCars(String filePath) {
        List<List<String>> dataList = ExcelUtil.getDataList(filePath);
        if (CommonUtils.isEmpty(dataList)) {
            return new ResponseEntity<>(Constants.ERROR, "未找到有效的数据");
        }

        for (int i = 0; i < dataList.size(); i++) {
            List<String> l = dataList.get(i);
            String keyNum = l.get(4);
            if (!keyNum.matches("^[0-9]*$")) {
                return new ResponseEntity<>(Constants.ERROR, "非法钥匙数量[" + keyNum + "],出现在第[" + (i + 1) + "]行");
            }
        }
        for (int i = 0; i < dataList.size(); i++) {
            try {
                List<String> l = dataList.get(i);
                CarInfo carInfo = new CarInfo();
                if (StringUtils.isNotBlank(l.get(3))) {
                    carInfo.setCarBrand(l.get(0));
                }
                if (StringUtils.isNotBlank(l.get(3))) {
                    carInfo.setCarType(l.get(1));
                }
                if (StringUtils.isNotBlank(l.get(3))) {
                    carInfo.setFrameNum(l.get(2));
                }
                if (StringUtils.isNotBlank(l.get(3))) {
                    carInfo.setCarColor(l.get(3));
                }
                if (l.size() >= 5 && StringUtils.isNotBlank(l.get(4))) {
                    carInfo.setKeyNum(Integer.valueOf(l.get(4)));
                }
                if (l.size() >= 6 && StringUtils.isNotBlank(l.get(5))) {
                    carInfo.setRemark(l.get(5));
                }

                this.carInfoService.insertCarInfo(carInfo);
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity<>(Constants.ERROR, "导入第[" + (i + 1) + "]行系统异常，第[" + i + "]行已成功导入。");
            }
        }
        return new ResponseEntity<>(Constants.SUCCESS, "上传成功");
    }

}
