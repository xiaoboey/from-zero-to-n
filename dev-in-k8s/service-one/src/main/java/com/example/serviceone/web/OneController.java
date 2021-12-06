package com.example.serviceone.web;

import com.example.serviceone.feign.IAuthServerFeignClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xiaoqb
 */
@RestController
public class OneController {
    @Value(("${server.port}"))
    private int port;

    @Value("${spring.application.name}")
    String clientName;

    private final IAuthServerFeignClient authServerFeignClient;

    public OneController(IAuthServerFeignClient authServerFeignClient) {
        this.authServerFeignClient = authServerFeignClient;
    }

    @RequestMapping("/hello")
    public String hello() {
        return "Hello!";
    }

    @RequestMapping("/getPort")
    public int getPort() {
        return port;
    }

    @RequestMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password) {
        String basic = String.format("%1$s:%2$s", clientName, "123456");
        String authorization = null;
        try {
            authorization = "Basic " + Base64.getEncoder().encodeToString(basic.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return authServerFeignClient.getToken(authorization, "password", username, password, null);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping("/getUserInfo")
    public String getUserInfo(Principal principal) {
        String userName = principal.getName();
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.createObjectNode();
        node.put("name", userName);
        node.put("mobile", "13812345678");
        return node.toString();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping("/listUserInfo")
    public String listUserInfo() {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode nodes = objectMapper.createArrayNode();
        ObjectNode node = nodes.addObject();
        node.put("name", "first");
        node.put("mobile", "13811225678");

        node = nodes.addObject();
        node.put("name", "second");
        node.put("mobile", "13822335678");

        return nodes.toString();
    }

    /**
     * 模拟一个忘记密码通过邮件找回的方法
     *
     * @param mail
     * @return
     */
    @PostAuthorize("returnObject != null")
    @RequestMapping("/forgotPassword")
    public String forgotPassword(@RequestParam(required = false) String mail) {
        boolean valid = "abc@some.com".equals(mail);
        if (valid) {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode node = objectMapper.createObjectNode();
            node.put("mail", mail);
            node.put("new_password", "123456");
            return node.toString();
        } else {
            return null;
        }
    }

    @PostFilter(value = "filterObject.get('author').asText().equals('金庸')")
    @RequestMapping("/listBook")
    public List<ObjectNode> listBook() {
        List<ObjectNode> nodeList = new ArrayList<>();

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.createObjectNode();
        node.put("name", "《飞狐外传》");
        node.put("author", "金庸");
        nodeList.add(node);

        node = objectMapper.createObjectNode();
        node.put("name", "《武林外史》");
        node.put("author", "古龙");
        nodeList.add(node);

        return nodeList;
    }

    @PreFilter(filterTarget = "ids", value = "filterObject > 0")
    @RequestMapping("/listGoods")
    public String listGoods(@RequestParam(value = "ids[]") List<Integer> ids) {
        return String.join(",", ids.stream().map(p -> String.valueOf(p)).collect(Collectors.toList()));
    }
}
