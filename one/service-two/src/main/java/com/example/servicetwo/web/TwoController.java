package com.example.servicetwo.web;

import com.example.servicetwo.feign.IServiceOneFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xiaoqb
 */
@RestController
@RequestMapping("/feign-test")
public class TwoController {
    @Autowired
    private IServiceOneFeignClient serviceOneFeignClient;

    @RequestMapping("/hello")
    public String hello() {
        return serviceOneFeignClient.hello();
    }

    @Value(("${server.port}"))
    private int port;

    @RequestMapping("/getPort")
    public int getPort() {
        return serviceOneFeignClient.getPort();
    }
}
