package com.example.serviceone.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author xiaoqb
 */
@FeignClient(value = "auth-server")
public interface IAuthServerFeignClient {
    /**
     * getToken
     *
     * @param authorization
     * @param grantType
     * @param username
     * @param password
     * @param refreshToken
     * @return
     */
    @PostMapping("/oauth/token")
    String getToken(@RequestHeader(value = "Authorization") String authorization,
                    @RequestParam("grant_type") String grantType,
                    @RequestParam(value = "username", required = false) String username,
                    @RequestParam(value = "password", required = false) String password,
                    @RequestParam(value = "refresh_token", required = false) String refreshToken);

    /**
     * checkToken
     *
     * @param authorization
     * @param token
     * @return
     */
    @PostMapping("/oauth/check_token")
    String checkToken(@RequestHeader(value = "Authorization") String authorization,
                      @RequestParam("token") String token);
}
