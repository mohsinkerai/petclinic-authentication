package com.system.demo.users;

import org.springframework.security.core.GrantedAuthority;

public enum UserAuthority implements GrantedAuthority {
    ADMIN("Admin"),
    REGISTRAR("Registrar"),
    AUTHORIZER("Authorizer"),
    CARDISSUER("Card Issuer / Printer"),
    FILE_UPLOAD("Volunteer File Upload"),
    VEHICLE_REGISTRAR("Vehicle Registrar"),
    VEHICLE_AUTHORIZER("Vehicle Authorizer"),
    VEHICLE_CARDISSUER("Vehicle Card Issuer / Printer"),
    VEHICLE_FILE_UPLOAD("Vehicle File Upload");

    private UserAuthority(String authority) {
        this.authority = authority;
    }

    private String authority;

    @Override
    public String getAuthority() {
        return this.name();
    }

    public String getDisplayName() {
        return this.authority;
    }
}
