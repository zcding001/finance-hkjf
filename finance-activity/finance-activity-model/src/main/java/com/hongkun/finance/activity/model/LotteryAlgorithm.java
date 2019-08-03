package com.hongkun.finance.activity.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * @Description: 抽奖算法
 * @Program: com.hongkun.finance.activity.model.LotteryAlgorithm
 * @Author: yunlongliu@hongkun.com.cn
 * @Date: 2018-10-15 14:07
 **/
public class LotteryAlgorithm {

    private  final Logger logger = LoggerFactory.getLogger(LotteryAlgorithm.class);
    public  LotteryItem getPrize(List<LotteryItem> list){
        LotteryItem lotsItem = new LotteryItem();
        int random = new Random().nextInt(10000);
        int prizeRate = 0;// 中奖率
        Iterator<LotteryItem> it = list.iterator();
        while (it.hasNext()) {
            lotsItem = it.next();
            prizeRate += lotsItem.getItemRate()*100;
            if (random < prizeRate) {
                logger.info("奖品为：{},概率为：{},随机数：{}, 概率基数：{}",lotsItem.getItemName() ,lotsItem.getItemRate(), random,prizeRate);
                break;
            }
        }
        //如果中奖的概率小于等于0，则重新生成奖品，防止用户抽到大奖
        if(lotsItem.getItemRate()<=0){
            logger.error("奖品算法有问题，抽到大奖了"+lotsItem.getItemName());
            lotsItem=getPrize(list);
        }
        return lotsItem;
    }


}
