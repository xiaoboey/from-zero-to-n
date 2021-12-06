package com.example.two.authserver.dao;

import com.example.two.authserver.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author xiaoqb
 */
public interface RoleRepository extends JpaRepository<Role, Long> {

}
