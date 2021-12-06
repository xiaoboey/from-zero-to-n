package com.example.servicetwo.feign;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author xiaoq
 */
@Qualifier("serviceOneFeignClient")
@FeignClient(value = "service-one", configuration = FeignRequestInterceptor.class, fallback = ServiceOneFallback.class)
public interface IServiceOneFeignClient {
    @PostMapping("/hello")
    String hello();

    @PostMapping("/getPort")
    Integer getPort();

    @RequestMapping("/listUserInfo")
    String listUserInfo();
}
