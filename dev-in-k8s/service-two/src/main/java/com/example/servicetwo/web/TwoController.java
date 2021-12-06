package com.example.servicetwo.web;

import com.example.servicetwo.feign.IServiceOneFeignClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xiaoqb
 */
@RestController
@RequestMapping("/feign-test")
public class TwoController {
    private final IServiceOneFeignClient serviceOneFeignClient;

    public TwoController(@Qualifier("serviceOneFeignClient") IServiceOneFeignClient serviceOneFeignClient) {
        this.serviceOneFeignClient = serviceOneFeignClient;
    }

    @RequestMapping("/hello")
    public String hello() {
        return serviceOneFeignClient.hello();
    }

    @RequestMapping("/getPort")
    public int getPort() {
        return serviceOneFeignClient.getPort();
    }

    @PreAuthorize("isAuthenticated() || isAnonymous()")
    @RequestMapping("/listUserInfo")
    public String listUserInfo() {
        return serviceOneFeignClient.listUserInfo();
    }
}
