package com.hongkun.finance.activity.facade.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONArray;
import com.hongkun.finance.activity.facade.LotteryItemFacade;
import com.hongkun.finance.activity.model.LotteryItem;
import com.hongkun.finance.activity.service.LotteryActivityService;
import com.hongkun.finance.activity.service.LotteryItemService;
import com.hongkun.finance.user.model.RegUser;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.model.ResponseEntity;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * @Description:
 * @Program: com.hongkun.finance.activity.facade.impl.LotteryItemFacadeImpl
 * @Author: yunlongliu@hongkun.com.cn
 * @Date: 2018-09-29 17:28
 **/
@Service
public class LotteryItemFacadeImpl implements LotteryItemFacade{

    private static final Logger logger = LoggerFactory.getLogger(LotteryItemFacadeImpl.class);

    @Autowired
    private LotteryItemService lotteryItemService;

    @Autowired
    private LotteryActivityService lotteryActivityService;

    @Override
    public ResponseEntity<?> saveLotteryItems(String groupsJSON, RegUser loginUser) {

        try {
            JSONObject reqJSON = JSONObject.parseObject(groupsJSON);
            JSONArray groups = JSONArray.parseArray(reqJSON.get("groups").toString());
            String activityIdIdStr = reqJSON.get("activityId").toString();
            if (StringUtils.isEmpty(activityIdIdStr)) {
                return new ResponseEntity<>(Constants.ERROR, "活动不存在");
            }
            Integer activityId = Integer.valueOf(activityIdIdStr);

            //删除群/奖项的信息
            String delGroups = reqJSON.get("delGroup").toString();
            String delLocations = reqJSON.get("delLocation").toString();
            String[] delLotsGroups = delGroups.split("-");
            String[] delLocationFlags = delLocations.split("-");
            if(delGroups.length() > 0){
                for (int i = 0; i < delLotsGroups.length; i++) {
                    if(!StringUtils.isEmpty(delLotsGroups[i])){
                        Integer tempDelLotsGroup = Integer.valueOf(delLotsGroups[i]);
                        Integer tempDelLocationFlag = Integer.valueOf(delLocationFlags[i]);
                        lotteryItemService.deleteLotteryItemsByGroupAndLocationFlag(activityId,tempDelLotsGroup,tempDelLocationFlag,0);
                    }
                }
            }

            String delItemIds = reqJSON.get("delItems").toString();
            String[] delIds = delItemIds.split("-");
            for (String delId : delIds) {
                if(!StringUtils.isEmpty(delId)){
                    Integer tempId = Integer.valueOf(delId);
                    lotteryItemService.deleteLotteryItemsById(tempId,0);
                }
            }

            // 添加或更新
            if (groups.size() > 0) {
                for (int i = 0; i < groups.size(); i++) {
                    JSONObject groupJSON = JSONObject.parseObject(groups.getString(i));
                    Integer group = Integer.valueOf(groupJSON.get("group").toString());
                    Integer locationFlag = Integer.valueOf(groupJSON.get("locationFlag").toString());
                    JSONArray items = JSONArray.parseArray(groupJSON.get("items").toString());
                    for (int j = 0; j < items.size(); j++) {
                        JSONObject tempItem = JSONObject.parseObject(items.getString(j));
                        Integer lotsType = Integer.valueOf(tempItem.get("lotsType").toString().trim());
                        Double amountAtm = Double.valueOf(tempItem.get("amountAtm").toString().trim());
                        String lotsName = tempItem.get("lotsName").toString();
                        Integer lotsCount = Integer.valueOf(tempItem.get("lotsCount").toString().trim());
                        Double lotsRate = Double.valueOf(tempItem.get("lotsRate").toString().trim());
                        Integer sequenceNum = Integer.valueOf(tempItem.get("sequenceNum").toString());
                        String itemId = tempItem.get("itemId").toString();
                        LotteryItem lotteryItem = new LotteryItem();
                        lotteryItem.setLotteryActivityId(activityId);
                        lotteryItem.setItemName(lotsName);
                        lotteryItem.setItemType(lotsType);
                        lotteryItem.setItemRate(lotsRate);
                        lotteryItem.setState(1);
                        lotteryItem.setAmountAtm(amountAtm);
                        lotteryItem.setItemCount(lotsCount);
                        lotteryItem.setSequenceNum(sequenceNum);
                        lotteryItem.setItemGroup(group);
                        lotteryItem.setLocationFlag(locationFlag);
                        lotteryItem.setRegUserId(loginUser.getId());
                        lotteryItem.setModifyTime(new Date());
                        if (StringUtils.isEmpty(itemId)) {
                            lotteryItemService.insertLotteryItem(lotteryItem);
                        } else {
                            lotteryItem.setId(Integer.valueOf(itemId.trim()));
                            lotteryItemService.updateLotteryItem(lotteryItem);
                        }

                    }
                }
            }

        } catch (Exception e) {
            logger.error("添加抽奖活动奖项失败,信息：{}",groupsJSON,e);
            throw new GeneralException("保存失败");
        }
        return new ResponseEntity<>(Constants.SUCCESS,"保存成功");
    }
}
