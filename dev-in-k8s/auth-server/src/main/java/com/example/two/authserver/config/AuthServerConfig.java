package com.example.two.authserver.config;

import com.example.two.authserver.security.SmsTokenGranter;
import com.example.two.authserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenGranter;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeTokenGranter;
import org.springframework.security.oauth2.provider.implicit.ImplicitTokenGranter;
import org.springframework.security.oauth2.provider.password.ResourceOwnerPasswordTokenGranter;
import org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaoqb
 *
 */
@Configuration
@EnableAuthorizationServer
@EnableResourceServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    /**
     * 配置AuthClient信息
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("service-one")
                .secret(passwordEncoder.encode("123456"))
                .authorizedGrantTypes("client_credentials", "refresh_token", "password", SmsTokenGranter.GRANT_SMS_CODE)
                .accessTokenValiditySeconds(3600)
                .refreshTokenValiditySeconds(7200)
                .scopes("server")
                .and()
                .withClient("service-two")
                .secret(passwordEncoder.encode("123456"))
                .authorizedGrantTypes("client_credentials", "refresh_token", "password")
                .accessTokenValiditySeconds(3600)
                .refreshTokenValiditySeconds(7200)
                .scopes("server");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(tokenStore())
                .authenticationManager(authenticationManager)
                .userDetailsService(userService)
                .reuseRefreshTokens(false)
                .tokenEnhancer(jwtTokenEnhancer());

        // 自定义验证（短信验证码），其他验证模式可以参考这个实现
        List<TokenGranter> tokenGranters = getDefaultTokenGranters(endpoints);
        tokenGranters.add(new SmsTokenGranter(endpoints, userService));
        endpoints.tokenGranter(new CompositeTokenGranter(tokenGranters));
    }

    /**
     * 这部分代码是根据 AuthorizationServerEndpointsConfigurer.getDefaultTokenGranters() 进行了修改
     * 只是做了private method访问的调整，其他代码原封不动
     *
     * @param endpoints
     * @return
     */
    private List<TokenGranter> getDefaultTokenGranters(AuthorizationServerEndpointsConfigurer endpoints) {
        ClientDetailsService clientDetails = endpoints.getClientDetailsService();
        AuthorizationServerTokenServices tokenServices = endpoints.getTokenServices();
        AuthorizationCodeServices authorizationCodeServices = endpoints.getAuthorizationCodeServices();
        OAuth2RequestFactory requestFactory = endpoints.getOAuth2RequestFactory();

        List<TokenGranter> tokenGranters = new ArrayList<TokenGranter>();
        tokenGranters.add(new AuthorizationCodeTokenGranter(tokenServices, authorizationCodeServices, clientDetails,
                requestFactory));
        tokenGranters.add(new RefreshTokenGranter(tokenServices, clientDetails, requestFactory));
        ImplicitTokenGranter implicit = new ImplicitTokenGranter(tokenServices, clientDetails, requestFactory);
        tokenGranters.add(implicit);
        tokenGranters.add(new ClientCredentialsTokenGranter(tokenServices, clientDetails, requestFactory));
        if (authenticationManager != null) {
            tokenGranters.add(new ResourceOwnerPasswordTokenGranter(authenticationManager, tokenServices,
                    clientDetails, requestFactory));
        }
        return tokenGranters;
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtTokenEnhancer());
    }

    /**
     * 设置Jwt处理的非对称加密私钥证书
     * @return
     */
    @Bean
    protected JwtAccessTokenConverter jwtTokenEnhancer() {
        KeyStoreKeyFactory keyStoreKeyFactory =
                new KeyStoreKeyFactory(new ClassPathResource("oauth2-jwt.jks"), "mypass".toCharArray());
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setKeyPair(keyStoreKeyFactory.getKeyPair("oauth2-jwt"));
        return converter;
    }
}
