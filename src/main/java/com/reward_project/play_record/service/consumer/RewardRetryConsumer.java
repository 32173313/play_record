package com.reward_project.play_record.service.consumer;

import com.reward_project.play_record.entity.PrizeRecord;
import com.reward_project.play_record.exception.SendCoinFailureException;
import com.reward_project.play_record.service.SyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import static com.reward_project.play_record.constant.QueueConstant.REWARD_RETRY_QUEUE;

@Service
@EnableRetry
public class RewardRetryConsumer {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private SyncService syncService;

    Logger logger = LoggerFactory.getLogger(RewardRetryConsumer.class);

    // 监听这个队列
    @RabbitListener(queues = REWARD_RETRY_QUEUE)
    @Retryable(value = {RuntimeException.class, SendCoinFailureException.class}, maxAttempts = 3, backoff = @Backoff(delay = 2000L))
    public void consumer(PrizeRecord prizeRecord) {
        logger.info(String.format("正在进行重试，外部业务号为%s", prizeRecord.getOutBizNo()));
        syncService.sendCoinAndAddPrizeRecord(prizeRecord);
    }

    // 重试完成的结果
    @Recover
    public void recover(PrizeRecord prizeRecord) {
        logger.error(String.format("外部业务号为%s的发奖已重试三次，依旧失败", prizeRecord.getOutBizNo()));
    }
}
