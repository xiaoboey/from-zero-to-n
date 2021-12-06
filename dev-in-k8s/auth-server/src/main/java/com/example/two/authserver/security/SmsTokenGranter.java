package com.example.two.authserver.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.Map;

/**
 * 验证短信验证码
 *
 * @author xiaoqb
 */
public class SmsTokenGranter extends AbstractTokenGranter {
    public static final String GRANT_SMS_CODE = "sms_code";

    protected UserDetailsService userDetailsService;

    public SmsTokenGranter(AuthorizationServerEndpointsConfigurer endpoints,
                           UserDetailsService userDetailsService) {
        super(endpoints.getTokenServices(), endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory(), GRANT_SMS_CODE);
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        //TODO: 这里只是示例，内置了短信验证码

        Map<String, String> parameters = tokenRequest.getRequestParameters();
        String smsCode = parameters.get("sms_code");
        if (smsCode.equals("123456")) {
            UserDetails userDetails = userDetailsService.loadUserByUsername("admin");
            PreAuthenticatedAuthenticationToken authenticationToken = new PreAuthenticatedAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authenticationToken.setDetails(userDetails);

            OAuth2Request oAuth2Request = getRequestFactory().createOAuth2Request(client, tokenRequest);
            return new OAuth2Authentication(oAuth2Request, authenticationToken);
        } else {
            throw new InvalidGrantException("SmsCode invalid!");
        }
    }
}
