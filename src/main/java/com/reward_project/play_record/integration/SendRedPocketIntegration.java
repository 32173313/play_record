package com.reward_project.play_record.integration;

import com.reward_project.play_record.controller.vo.BaseVO;
import com.reward_project.play_record.integration.cmd.SendCreditCmd;
import com.reward_project.play_record.integration.cmd.SendRedPocketCmd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
public class SendRedPocketIntegration {
    @Autowired
    private RestTemplate restTemplate;
    // 异步发奖的话 在用户量大的情况下 延迟较高 用户体验差
    // 重试本身是异步
    public BaseVO sendRedPocketIntegration(SendRedPocketCmd sendRedPocketCmd) {
        String url = "http://localhost:8084/red/pocket/send";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<SendRedPocketCmd> request = new HttpEntity<>(sendRedPocketCmd, headers);
        ResponseEntity<BaseVO> exchange = restTemplate.exchange(url, HttpMethod.POST, request, BaseVO.class);
        return exchange.getBody();
    }
}
