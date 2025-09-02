package com.reward_project.play_record.integration;

import com.reward_project.play_record.controller.vo.BaseVO;
import com.reward_project.play_record.integration.cmd.SendCoinCmd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
public class SendCoinIntegration {

    @Autowired
    private RestTemplate restTemplate;
    // 异步发奖的话 在用户量大的情况下 延迟较高 用户体验差
    // 重试本身是异步
    public BaseVO sendCoinIntegration(SendCoinCmd sendCoinCmd) {
        String url = "http://localhost:8082/coin/send";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<SendCoinCmd> request = new HttpEntity<>(sendCoinCmd, headers);
        ResponseEntity<BaseVO> exchange = restTemplate.exchange(url, HttpMethod.POST, request, BaseVO.class);
        return exchange.getBody();
    }
}
