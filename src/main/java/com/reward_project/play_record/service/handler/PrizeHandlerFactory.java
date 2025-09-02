package com.reward_project.play_record.service.handler;

import com.reward_project.play_record.enums.PrizeTypeEnum;
import com.reward_project.play_record.exception.PrizeTypeNotExistException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class PrizeHandlerFactory {

    // Spring 是一个很大的仓库，本质上是一个巨大的 hashMap，key 是子类定义的名字，value 是对应的.class
    @Autowired
    private ApplicationContext applicationContext;

    // 这里return的是AbstractPrizeHandler的子类
    public AbstractPrizeHandler getHandlerByType(String prizeType) {
        HashMap<String, AbstractPrizeHandler> getHandlerByPrizeTypeHM = buildHashMap();
        return getHandlerByPrizeTypeHM.get(prizeType);

//        if (prizeType.equals(PrizeTypeEnum.COIN.getCode())) {
//            return applicationContext.getBean("coin", CoinPrizeHandler.class);
//        } else if (prizeType.equals(PrizeTypeEnum.CREDIT.getCode())) {
//            return applicationContext.getBean("credit", CreditPrizeHandler.class);
//        } else if (prizeType.equals(PrizeTypeEnum.RED_POCKET.getCode())) {
//            return applicationContext.getBean("red_pocket", RedPocketPrizeHandler.class);
//        } else {
//            return null;
//        }
    }

    private HashMap<String, AbstractPrizeHandler> buildHashMap() {
        HashMap<String, AbstractPrizeHandler> getHandlerByPrizeType = new HashMap<>();
        getHandlerByPrizeType.put(PrizeTypeEnum.COIN.getCode(), applicationContext.getBean("coin", CoinPrizeHandler.class));
        getHandlerByPrizeType.put(PrizeTypeEnum.CREDIT.getCode(), applicationContext.getBean("credit", CreditPrizeHandler.class));
        getHandlerByPrizeType.put(PrizeTypeEnum.RED_POCKET.getCode(), applicationContext.getBean("red_pocket", RedPocketPrizeHandler.class));
        return getHandlerByPrizeType;
    }
}
