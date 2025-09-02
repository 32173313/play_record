package com.reward_project.play_record.service.producer;

import com.reward_project.play_record.entity.PrizeRecord;
import com.reward_project.play_record.service.SyncService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.reward_project.play_record.constant.QueueConstant.*;

@Service
public class RewardRetryProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;


    public void sender(PrizeRecord prizeRecord) {
        rabbitTemplate.convertAndSend(REWARD_RETRY_EXCHANGE, REWARD_RETRY_ROUTING, prizeRecord);
    }
}
