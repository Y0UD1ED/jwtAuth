package com.example.jwtAuth.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class ExtendUserDetails extends User {
    private Integer id;
    private Collection<Role> roles;

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    public ExtendUserDetails(Integer id, String username, String password, Collection<Role> authorities) {
        super(username, password, authorities);
        this.roles= authorities;
        this.id=id;
    }

    public ExtendUserDetails(Integer id, String username, Collection<Role> authorities) {
        super(username,"null", authorities);
        this.roles= authorities;
        this.id=id;
    }



    public ExtendUserDetails(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
}
