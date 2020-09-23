package com.example.two.authserver.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author xiaoqb
 */
@Configuration
@EnableJpaRepositories(basePackages = {"com.example.two.authserver"})
@EntityScan({"com.example.two"})
@ComponentScan({"com.example.two.authserver"})
public class JpaConfig {
}
