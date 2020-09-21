package com.example.servicetwo.feign;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author xiaoq
 */
@Qualifier("serviceOneFeignClient")
@FeignClient(value = "service-one", fallback = ServiceOneFallback.class)
public interface IServiceOneFeignClient {
    @PostMapping("/hello")
    String hello();

    @PostMapping("/getPort")
    Integer getPort();
}
