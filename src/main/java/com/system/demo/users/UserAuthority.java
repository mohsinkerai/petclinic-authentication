package com.system.demo.users;

import org.springframework.security.core.GrantedAuthority;

public enum UserAuthority implements GrantedAuthority {
    ADMIN("ADMIN"),
    REGISTRAR("REGISTRAR"),
    AUTHORIZER("AUTHORIZER"),
    CARDISSUER("CARDISSUER");

    private UserAuthority(String authority) {
        this.authority = authority;
    }

    private String authority;

    @Override
    public String getAuthority() {
        return authority;
    }
}
