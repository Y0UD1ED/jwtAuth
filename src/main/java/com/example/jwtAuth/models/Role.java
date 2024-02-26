package com.example.jwtAuth.models;


import org.springframework.security.core.GrantedAuthority;

public class Role implements GrantedAuthority {

    private Integer id;

    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setAuthority(String authority) {
        this.name = authority;
    }


    public Role(String name) {
        this.id=null;
        this.name = name;
    }

    public Role() {
        this.id = null;
        this.name=null;
    }

    public Role(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String getAuthority() {
        return name;
    }
}
