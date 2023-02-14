package com.example.two.authserver.entity;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

/**
 * @author xiaoqb
 */
@Entity
@Table(name = "d_role")
public class Role implements GrantedAuthority {

    @Id
    private Long id;
    @Column(nullable = false)
    private String name;

    public Role() {
    }

    public Role(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String getAuthority() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
