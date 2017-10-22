package org.springframework.samples.petclinic.users;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

@Data
public class UserAuthority implements GrantedAuthority {

    private String authority;

    public UserAuthority(String authority) {
        this.authority = StringUtils.trimAllWhitespace(authority);

        Assert.hasText(this.authority, "Authority Should have Length Greater than Zero");
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
