package com.example.serviceone.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xiaoqb
 */
@RestController
public class OneController {
    @RequestMapping("/hello")
    public String hello() {
        return "Hello!";
    }

    @Value(("${server.port}"))
    private int port;

    @RequestMapping("/getPort")
    public int getPort() {
        return port;
    }
}
