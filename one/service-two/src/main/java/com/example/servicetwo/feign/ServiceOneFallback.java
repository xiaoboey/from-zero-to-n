package com.example.servicetwo.feign;

import org.springframework.stereotype.Component;

/**
 * @author xiaoq
 */
@Component
public class ServiceOneFallback implements IServiceOneFeignClient {
    @Override
    public String hello() {
        return "error";
    }

    @Override
    public Integer getPort() {
        return Integer.valueOf(-1);
    }
}
