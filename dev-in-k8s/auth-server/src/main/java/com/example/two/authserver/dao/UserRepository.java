package com.example.two.authserver.dao;

import com.example.two.authserver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author xiaoqb
 */
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
