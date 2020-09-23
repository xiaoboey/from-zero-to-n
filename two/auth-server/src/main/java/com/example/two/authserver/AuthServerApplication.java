package com.example.two.authserver;

import com.example.two.authserver.dao.RoleRepository;
import com.example.two.authserver.dao.UserRepository;
import com.example.two.authserver.entity.Role;
import com.example.two.authserver.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaoqb
 */
@SpringBootApplication
@EnableDiscoveryClient
public class AuthServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthServerApplication.class, args);
    }

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void initialize() {
        //初始化角色表
        if (!roleRepository.findById(1L).isPresent()) {
            Role role = new Role(1L, "ROLE_ADMIN");
            roleRepository.save(role);
        }

        //初始化用户表
        User admin = userRepository.findByUsername("admin");
        if (admin == null) {
            Role role = roleRepository.findById(1L).get();
            List<Role> roleList = new ArrayList<>();
            roleList.add(role);

            //增加用户admin并关联角色ROLE_ADMIN
            admin = new User(1L, "admin", passwordEncoder.encode("admin"));
            admin.setAuthorities(roleList);
            userRepository.save(admin);

            //增加用户worker
            User worker = new User(2L, "worker", passwordEncoder.encode("worker"));
            userRepository.save(worker);
        }
    }
}
