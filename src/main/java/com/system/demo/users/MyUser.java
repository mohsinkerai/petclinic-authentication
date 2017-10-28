package com.system.demo.users;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.Assert;

@Entity(name = "user")
@ToString
@EqualsAndHashCode
@Setter
@Getter
@NoArgsConstructor
public class MyUser implements UserDetails {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Column(unique = true)
    private String username;

    @NotNull
    private String password;

    private boolean isExpired;

    private boolean isLocked;

    private boolean enabled;

    private String authority;

    public void setAuthority(String authority) {
        Arrays.stream(authority.split(",")).map(UserAuthority::new)
            .collect(Collectors.toList());
        this.authority = authority;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(authority.split(",")).map(UserAuthority::new)
            .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !isExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public boolean isNew() {
        return this.id == null;
    }

    @Builder
    public MyUser(Long id, String username, String password, boolean isExpired, boolean isLocked,
        boolean enabled, String authority) {
        this.id = id;
        this.username = username;
        this.isExpired = isExpired;
        this.isLocked = isLocked;
        this.enabled = enabled;
        this.authority = authority;
        this.setPassword(password);
    }

    public void setPassword(String password) {
        Assert.hasLength(password, "password should has some length");
        Assert.isTrue(password.length() > 5, "Minimum password length should be 6");
        this.password = new BCryptPasswordEncoder().encode(password);
    }
}
