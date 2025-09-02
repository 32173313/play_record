package com.reward_project.play_record.util;

import com.reward_project.play_record.exception.RuleMatchFailException;
import org.apache.commons.jexl3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RuleMatchUtil {
    // 当前类内部自己使用的常量 修饰符为 private final static
    // 当前类外部使用的常量 修饰符为 public final static
    private final static Logger logger = LoggerFactory.getLogger(RuleMatchUtil.class);

    // 工具类的一定要用 static
    // jc里面变量名和具体值一一对应，然后规则里需要什么就从jc里拿什么
    public static Object buildPrizeStageInfol(String expression, HashMap<String, Long> ruleMatchMap) {
        try {
            JexlEngine jexl = new JexlBuilder().create();
            JexlExpression exp = jexl.createExpression(expression);

            JexlContext jc = new MapContext();

            // 把 ruleMatchMap 里的 所有键值对 转换成一个 Set，里面每个元素就是 Map.Entry（一条键值对）
            Set<Map.Entry<String, Long>> entries = ruleMatchMap.entrySet();

            // 就是把 HashMap 里的键值对，一条一条取出来，存到 jc 里
            for (Map.Entry<String, Long> entry : entries) {
                jc.set(entry.getKey(), entry.getValue());
            }

            return exp.evaluate(jc);

        } catch (Exception e) {
            // list, set and map 都自动实现 toString
            logger.error(String.format("规则计算失败，表达式为%s及参数为%s", expression, ruleMatchMap));
            throw new RuleMatchFailException(String.format("规则计算失败，表达式为%s及参数为%s", expression, ruleMatchMap));
        }
    }
}
